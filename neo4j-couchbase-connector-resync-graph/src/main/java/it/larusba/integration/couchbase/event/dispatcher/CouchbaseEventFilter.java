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
package it.larusba.integration.couchbase.event.dispatcher;

import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.client.core.message.dcp.RemoveMessage;

/**
 * If applied, this filter allows only {@link MutationMessage}s and
 * {@link RemoveMessage} to be considered by the {@link EventHandler}.
 *
 * @author Lorenzo Speranzoni
 */
public class CouchbaseEventFilter {

	/**
	 * Returns true if event is a {@link MutationMessage}s and
	 * {@link RemoveMessage}.
	 *
	 * @param couchbaseEvent
	 *            event object from Couchbase.
	 * @return true if event is a mutation.
	 */
	public static boolean accept(final CouchbaseEvent couchbaseEvent) {
		return couchbaseEvent.getMessage() instanceof MutationMessage
				|| couchbaseEvent.getMessage() instanceof RemoveMessage;
	}
}
