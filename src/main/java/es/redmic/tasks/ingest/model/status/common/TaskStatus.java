package es.redmic.tasks.ingest.model.status.common;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public enum TaskStatus {

	// @formatter:off
	
	REGISTERED (Constants.REGISTERED),
	ENQUEUED (Constants.ENQUEUED),
	STARTED (Constants.STARTED),
	RUNNING (Constants.RUNNING),
	WAITING_INTERVENTION (Constants.WAITING_INTERVENTION),
	FAILED (Constants.FAILED),
	CANCELLED (Constants.CANCELLED),
	REMOVED (Constants.REMOVED),
	COMPLETED (Constants.COMPLETED);	
	
	// @formatter:on

	final String status;

	TaskStatus(String status) {
		this.status = status;
	}

	public static TaskStatus fromString(String text) {
		if (text != null) {
			for (TaskStatus b : TaskStatus.values()) {
				if (text.equalsIgnoreCase(b.status)) {
					return b;
				}
			}
		}
		throw new IllegalArgumentException(text + " has no corresponding value");
	}

	public String toString() {
		return status;
	}

	public static class Constants {
		public static final String REGISTERED = "registered";
		public static final String ENQUEUED = "enqueued";
		public static final String STARTED = "started";
		public static final String RUNNING = "running";
		public static final String WAITING_INTERVENTION = "waitingIntervention";
		public static final String FAILED = "failed";
		public static final String CANCELLED = "cancelled";
		public static final String REMOVED = "removed";
		public static final String COMPLETED = "completed";
	}

}
