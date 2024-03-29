package es.redmic.tasks.ingest.model.intervention.common;

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

public enum InterventionType {

	// @formatter:off
	
	UNKNOWN (Constants.UNKNOWN), 
	MATCHING (Constants.MATCHING);

	// @formatter:on

	final String type;

	InterventionType(String type) {
		this.type = type;
	}

	public static InterventionType fromString(String text) {
		if (text != null) {
			for (InterventionType b : InterventionType.values()) {
				if (text.equalsIgnoreCase(b.type)) {
					return b;
				}
			}
		}
		throw new IllegalArgumentException(text + " has no corresponding value");
	}

	public String toString() {
		return type;
	}

	public static class Constants {
		public static final String UNKNOWN = "unknown";
		public static final String MATCHING = "matching";
	}

}
