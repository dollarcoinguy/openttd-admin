package com.openttd.admin.model;

/**
 * Wrapper class that adds escaped quote delimiters to string literals
 * used as Gamescript JSON parameters. This is necessary so that
 * they will properly be accepted by the server.
 */
public class JsonStringParameter {
	String value;

	public JsonStringParameter(String initialValue) {
		value = initialValue;
	}

	@Override public String toString() {
		return "\"" + value + "\"";
	}
}
