package com.example.codejudge;

public class ErrorResponse implements Response{
	private String status;
	private String reason;

	public ErrorResponse(String status, String reason) {
		this.status=status;
		this.reason=reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
