package com.itmencompany.mvc.model;

public class ResultMessage {
	private String message;
	private String value;
	private Boolean error;

	public ResultMessage() {

	}

	public ResultMessage(String message, String value) {

	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}
}
