package es.redmic.tasks.ingest.model.intervention.common;

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
