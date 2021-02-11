package com.example.codejudge;

import java.util.List;

public class Payload {
	private List<String> logFiles;
	private Integer parallelFileProcessingCount;

	public List<String> getLogFiles() {
		return logFiles;
	}

	public void setLogFiles(List<String> logFiles) {
		this.logFiles = logFiles;
	}

	public Integer getParallelFileProcessingCount() {
		return parallelFileProcessingCount;
	}

	public void setParallelFileProcessingCount(int parallelFileProcessingCount) {
		this.parallelFileProcessingCount = parallelFileProcessingCount;
	}

}
