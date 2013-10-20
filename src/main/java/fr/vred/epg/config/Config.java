package fr.vred.epg.config;

public enum Config {
	INPUT_URL(true), OUTPUT_PATH(true), REFRESH_TIME(false, "15"), REFRESH_PERIOD(
			false, "5");

	private boolean required;
	private String value;

	Config(boolean isRequired) {
		this.required = isRequired;
	}

	Config(boolean isRequired, String defaultValue) {
		this(isRequired);
		this.value = defaultValue;
	}

	public boolean isRequired() {
		return required;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
