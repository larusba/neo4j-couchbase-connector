/**
 * Copyright (c) 2004-2016 LARUS Business Automation [http://www.larus-ba.it]
 *
 * This file is part of the "LARUS Integration Framework for Neo4j".
 *
 * The "LARUS Integration Framework for Neo4j" is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.couchbase.client.core.ClusterFacade;
import com.couchbase.client.core.dcp.BucketStreamAggregator;
import com.couchbase.client.core.dcp.BucketStreamAggregatorState;
import com.couchbase.client.core.dcp.BucketStreamState;
import com.couchbase.client.core.message.CouchbaseMessage;
import com.couchbase.client.core.message.cluster.OpenBucketRequest;
import com.couchbase.client.core.message.cluster.SeedNodesRequest;
import com.couchbase.client.core.message.dcp.DCPRequest;
import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.client.core.message.dcp.RemoveMessage;
import com.couchbase.client.core.message.dcp.SnapshotMarkerMessage;
import com.couchbase.client.core.message.kv.MutationToken;
import com.couchbase.client.deps.com.lmax.disruptor.EventHandler;
import com.couchbase.client.deps.com.lmax.disruptor.RingBuffer;
import com.couchbase.client.deps.com.lmax.disruptor.dsl.Disruptor;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * An event catcher for Couchbase {@link DCPRequest}s.
 * 
 * Once you start this {@link Thread}, it will capture all
 * {@link CouchbaseMessage}s and notify them to the {@link EventHandler}
 * provided.
 * 
 * @author Lorenzo Speranzoni
 */
public class CouchbaseEventDispatcher extends Thread {

	/**
	 * The Couchbase cluster to listen to for mutation events.
	 */
	private ClusterFacade couchbaseClusterFacade;

	/**
	 * The Couchbase bucket to listen to for mutation events.
	 */
	private String couchbaseBucket;

	/**
	 * The Couchbase node addesses.
	 */
	private List<String> couchbaseNodes;

	/**
	 * The Couchbase password for the bucket
	 */
	private String couchbasePassword;

	/**
	 * The buffer where new events are published.
	 */
	private RingBuffer<CouchbaseEvent> couchbaseEventBuffer;

	/**
	 * The {@link EventHandler} that will consume detected events.
	 */
	private EventHandler<CouchbaseEvent> eventHandler;

	/**
	 * Constructor.
	 * 
	 * @param couchbaseClusterFacade
	 *            the Couchbase cluster
	 * @param couchbaseBucket
	 *            name of Couchbase bucket
	 * @param couchbaseNodes
	 *            address of Couchbase nodes
	 * @param couchbasePassword
	 *            password for Couchbase bucket
	 * @param eventHandler
	 */
	public CouchbaseEventDispatcher(ClusterFacade couchbaseClusterFacade, String couchbaseBucket,
			List<String> couchbaseNodes, String couchbasePassword, EventHandler<CouchbaseEvent> eventHandler) {

		this.couchbaseClusterFacade = couchbaseClusterFacade;
		this.couchbaseBucket = couchbaseBucket;
		this.couchbasePassword = couchbasePassword;
		this.couchbaseNodes = couchbaseNodes;
		this.eventHandler = eventHandler;

		buildDisruptor();
		connect();
	}

	/**
	 * Builds and starts the event consumer.
	 */
	@SuppressWarnings("unchecked")
	private void buildDisruptor() {

		Disruptor<CouchbaseEvent> disruptor = new Disruptor<CouchbaseEvent>(new CouchbaseEventFactory(), 32,
				Executors.newCachedThreadPool());

		disruptor.handleExceptionsWith(new CouchbaseExceptionLogger());
		disruptor.handleEventsWith(eventHandler);

		this.couchbaseEventBuffer = disruptor.start();
	}

	/**
	 * Connect this dispatcher to Couchbase.
	 */
	private void connect() {

		this.couchbaseClusterFacade.send(new SeedNodesRequest(couchbaseNodes)).timeout(2, TimeUnit.SECONDS).toBlocking()
				.single();

		this.couchbaseClusterFacade.send(new OpenBucketRequest(this.couchbaseBucket, couchbasePassword))
				.timeout(2, TimeUnit.SECONDS).toBlocking().single();
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		BucketStreamAggregator aggregator = new BucketStreamAggregator("couchbase2neo4j(" + this.hashCode() + ")",
				this.couchbaseClusterFacade, this.couchbaseBucket);

		MutationToken[] tokens = aggregator.getCurrentState()
				.map(new Func1<BucketStreamAggregatorState, MutationToken[]>() {
					@Override
					public MutationToken[] call(BucketStreamAggregatorState aggregatorState) {
						List<MutationToken> tokens = new ArrayList<MutationToken>(aggregatorState.size());
						for (BucketStreamState streamState : aggregatorState) {
							tokens.add(new MutationToken(streamState.partition(), streamState.vbucketUUID(),
									streamState.startSequenceNumber(), couchbaseBucket));
						}
						return tokens.toArray(new MutationToken[tokens.size()]);
					}
				}).toBlocking().first();

		BucketStreamAggregatorState state = new BucketStreamAggregatorState();

		for (MutationToken token : tokens) {
			long start = 0, end = 0;
			switch ("OUT") {
			case "IN":
				start = 0;
				end = token.sequenceNumber();
				break;
			case "OUT":
				start = token.sequenceNumber();
				end = 0xffffffff;
				break;
			case "BOTH":
				start = 0;
				end = 0xffffffff;
				break;
			}
			state.put(new BucketStreamState((short) token.vbucketID(), token.vbucketUUID(), start, end, start, end));
		}

		aggregator.feed(state).toBlocking().forEach(buildAction1(state));
	}

	/**
	 * Builds the {@link Action1} to invoke for each item emitted by the
	 * {@code BlockingObservable}.
	 * 
	 * @param state
	 *            current state of the stream aggregator.
	 * @return the {@link Action1} created.
	 */
	private Action1<DCPRequest> buildAction1(BucketStreamAggregatorState state) {
		return new Action1<DCPRequest>() {
			@Override
			public void call(final DCPRequest dcpRequest) {
				if (dcpRequest instanceof SnapshotMarkerMessage) {
					SnapshotMarkerMessage snapshotMarker = (SnapshotMarkerMessage) dcpRequest;
					final BucketStreamState oldState = state.get(snapshotMarker.partition());
					state.put(new BucketStreamState(snapshotMarker.partition(), oldState.vbucketUUID(),
							snapshotMarker.startSequenceNumber(), oldState.endSequenceNumber(),
							snapshotMarker.startSequenceNumber(), snapshotMarker.endSequenceNumber()));
				} else if (dcpRequest instanceof RemoveMessage) {
					RemoveMessage msg = (RemoveMessage) dcpRequest;
					final BucketStreamState oldState = state.get(msg.partition());
					state.put(new BucketStreamState(msg.partition(), oldState.vbucketUUID(), msg.bySequenceNumber(),
							oldState.endSequenceNumber(),
							Math.max(msg.bySequenceNumber(), oldState.snapshotStartSequenceNumber()),
							oldState.snapshotEndSequenceNumber()));
				} else if (dcpRequest instanceof MutationMessage) {
					MutationMessage msg = (MutationMessage) dcpRequest;
					final BucketStreamState oldState = state.get(msg.partition());
					state.put(new BucketStreamState(msg.partition(), oldState.vbucketUUID(), msg.bySequenceNumber(),
							oldState.endSequenceNumber(),
							Math.max(msg.bySequenceNumber(), oldState.snapshotStartSequenceNumber()),
							oldState.snapshotEndSequenceNumber()));
				}
				couchbaseEventBuffer.tryPublishEvent(CouchbaseEvent.DEFAULT_COUCHBASE_MESSAGE_TRANSLATOR, dcpRequest);
			}
		};
	}
}
