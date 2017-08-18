package com.xingyanping.util;

public class ValidationError {
	public boolean result = false;
	public String errorMessage = "无";
	public ValidationError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public ValidationError(boolean result) {
		this.result = result;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
