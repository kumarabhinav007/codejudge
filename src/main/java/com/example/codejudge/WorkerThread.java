package com.example.codejudge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class WorkerThread implements Runnable{

	private String urlAddress;
	private long minTimeStamp;
	private HashMap<String, Integer> map[];
	private volatile boolean isDone;

	public WorkerThread(String urlAddress, HashMap<String, Integer> map[]) {
		this.urlAddress=urlAddress;
		this.map=map;
		this.minTimeStamp=Long.MAX_VALUE;
		this.isDone=false;
	}
	
	public void run() {
		URL url;
		try {
			url = new URL(urlAddress);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			isDone=true;
			return;
		}
		ArrayList<String> exceptionList=new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			String line;
			while((line=br.readLine())!=null) {
				exceptionList.add(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
			isDone=true;
			return;
		}
		for(String str:exceptionList) {
			StringTokenizer st=new StringTokenizer(str);
			st.nextToken();
			long timestamp=Long.parseLong(st.nextToken());
			String exception=st.nextToken();
			Date date = new Date(timestamp);
			int hour=date.getHours();
			int minutes=date.getMinutes()/15;
			int index=hour*4+minutes;
			
			Integer count=map[index].get(exception);
			count=count==null?1:count+1;
			map[index].put(exception, count);
		}
		isDone=true;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public long getMinTimeStamp() {
		return minTimeStamp;
	}

	public void setMinTimeStamp(long minTimeStamp) {
		this.minTimeStamp = minTimeStamp;
	}

	public HashMap<String, Integer>[] getMap() {
		return map;
	}

	public void setMap(HashMap<String, Integer>[] map) {
		this.map = map;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
