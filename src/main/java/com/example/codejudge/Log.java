package com.example.codejudge;

public class Log {

	private String exception;
	private Integer count;

	public Log(String exception, Integer count) {
		this.exception = exception;
		this.count = count;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
