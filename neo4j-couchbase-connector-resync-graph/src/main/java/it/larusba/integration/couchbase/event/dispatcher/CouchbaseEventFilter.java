/**
 * Copyright (c) 2016 LARUS Business Automation [http://www.larus-ba.it]
 * <p>
 * This file is part of the "LARUS Integration Framework for Neo4j".
 * <p>
 * The "LARUS Integration Framework for Neo4j" is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
