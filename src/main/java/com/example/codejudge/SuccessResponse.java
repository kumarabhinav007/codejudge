package com.example.codejudge;

import java.util.List;

public class SuccessResponse implements Response{
	private List<GroupByTime> response;

	public List<GroupByTime> getResponse() {
		return response;
	}

	public void setResponse(List<GroupByTime> response) {
		this.response = response;
	}

}
