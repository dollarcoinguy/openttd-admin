package com.openttd.admin.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.openttd.constant.TestMode;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class RemoteGSJson {
	int requestID = -1;
	String method = "";
	ArrayList<Object> args = new ArrayList<>();
	int companyMode = Integer.MIN_VALUE;
	int testMode = Integer.MIN_VALUE;

	public String toJsonString() {
		Gson gson = new Gson();
		JsonObject payload = new JsonObject();
		payload.addProperty("action", "call");
		if (requestID != -1) {
			payload.addProperty("number", requestID);
		}
		payload.addProperty("method", method);
		if (companyMode != Integer.MIN_VALUE) {
			payload.addProperty("companymode", companyMode);
		}
		if (testMode != Integer.MIN_VALUE) {
			payload.addProperty("testmode", testMode);
		}
		if (args != null) {
			JsonArray argsPayload = new JsonArray();
			for (Object parameter : args) {
				switch (parameter) {
					case Integer i -> argsPayload.add(i);
					case String s -> argsPayload.add(s);
					case Long l -> argsPayload.add(l);
					case Boolean b -> argsPayload.add(b);
					case JsonStringParameter jsp -> argsPayload.add(jsp.toString());
					case null, default -> throw new RuntimeException("Unsupported RemoteGSJson parameter type");
				}
			}
			payload.add("args", argsPayload);
		}
		String serializedPayload = gson.toJson(payload);
		if (serializedPayload.getBytes().length > 1457) {
			throw new UnsupportedOperationException("Cannot send OpenTTD Gamescript JSON packets longer than 1457 bytes");
		} else {
			return serializedPayload;
		}
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public ArrayList<Object> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<Object> args) {
		this.args = args;
	}

	public int getCompanyMode() {
		return companyMode;
	}

	public void setCompanyMode(int companymode) {
		this.companyMode = companymode;
	}

	public int getTestMode() {
		return testMode;
	}

	public void setTestMode(TestMode testMode) {
		this.testMode = testMode.ordinal();
	}
}
