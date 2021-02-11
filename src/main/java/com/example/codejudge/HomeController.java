package com.example.codejudge;

import java.util.ListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

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

		SuccessResponse response=new SuccessResponse();
		response.setResponse(homeService.process(payload.getLogFiles(), payload.getParallelFileProcessingCount()));
		return response;
	}
}
