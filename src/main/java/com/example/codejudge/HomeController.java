package com.example.codejudge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	HomeService homeService;

	@PostMapping("/api/process-logs/")
	public Response processLogs(@RequestBody Payload payload) {

		if(CollectionUtils.isEmpty(payload.getLogFiles())) {
			String status="Failure";
			String reason="No log file present";
			Response response=new ErrorResponse(status, reason);
			return response;
		}
		if(payload.getParallelFileProcessingCount()==null) {
			String status="Failure";
			String reason="Required parameter parallelFileProcessingCount not present";
			Response response=new ErrorResponse(status, reason);
			return response;
		}
		if(payload.getParallelFileProcessingCount()==0) {
			String status="Failure";
			String reason="Parallel File Processing count must be greater than zero";
			Response response=new ErrorResponse(status, reason);
			return response;
		}

		ListIterator<String> itr=payload.getLogFiles().listIterator();
		while(itr.hasNext()) {
			System.out.println(itr.next());
		}
		SuccessResponse response=new SuccessResponse();
		response.setResponse(homeService.process(payload.getLogFiles(), payload.getParallelFileProcessingCount()));
		return response;
	}
}
