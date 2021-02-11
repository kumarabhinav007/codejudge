package com.example.codejudge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

	public List<GroupByTime> process(List<String> logFiles, int parallelFileProcessingCount) {
		HashMap<String, Integer> map[][]=new HashMap[logFiles.size()][24*4];
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[i].length;j++) {
				map[i][j]=new HashMap<String, Integer>();
			}
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(parallelFileProcessingCount);
		WorkerThread workers[]=new WorkerThread[logFiles.size()];
		int index=0;
		for(String url: logFiles) {
			workers[index] = new WorkerThread(url, map[index]);
			executor.execute(workers[index]);
			index++;
		}
		for(int i=0;i<logFiles.size();i++) {
			while(!workers[i].isDone());
		}
		for(int i=1;i<map.length;i++) {
			for(int j=0;j<map[i].length;j++) {
				Iterator itr = map[i][j].entrySet().iterator();
				while(itr.hasNext()) {
					Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)itr.next();
					Integer count=map[0][j].get(entry.getKey());
					count=count==null?0:count;
					count+=entry.getValue();
					map[0][j].put(entry.getKey(), count);
				}
			}
		}
		
		long minTimestamp=Long.MAX_VALUE;
		for(int i=0;i<workers.length;i++) {
			minTimestamp=Math.min(minTimestamp, workers[i].getMinTimeStamp());
		}
		Date date=new Date(minTimestamp);
		int hours=date.getHours();
		int minutes=date.getMinutes()/15;
		index=hours*4+minutes;

		List<GroupByTime> result=new ArrayList<GroupByTime>();

		for(int i=0;i<map[0].length;i++) {
			if(map[0][index].size()>0) {
				GroupByTime groupByTime=new GroupByTime();
				int startHour=index/4;
				int startMinute=(index%4)*15;
				int endHour=((index+1)%map[0].length)/4;
				int endMinute=(((index+1)%map[0].length)%4)*15;
				groupByTime.setTime(getTimeString(startHour, startMinute)+"-"+getTimeString(endHour,endMinute));
				
				groupByTime.setLogs(new ArrayList<Log>());
				Iterator itr = map[0][index].entrySet().iterator();
				while(itr.hasNext()) {
					Map.Entry<String, Integer> entry=(Map.Entry<String, Integer>)itr.next();
					Log log=new Log(entry.getKey(), entry.getValue());
					groupByTime.getLogs().add(log);
				}
				result.add(groupByTime);
			}
			index++;
			index=index%map[0].length;
		}
		return result;
	}
	
	private String getTimeString(int hours, int minutes) {
		String hourString=Integer.toString(hours);
		if(hours<10)
			hourString="0"+hourString;

		String minuteString=Integer.toString(minutes);
		if(minutes<10)
			minuteString="0"+minuteString;
		return hourString+":"+minuteString;
	}
}
