package org.jenkinsci.plugins.client.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jenkinsci.plugins.client.CIConfigure;

public class CILogger extends PrintWriter{
	private static CILogger _instance;
	public static String LOG_PATH = CIConfigure.DATA_PATH + "ci.log";
	public static CILogger getInstance() {
		if ( _instance==null ) {
			try {
				_instance = new CILogger(new File(LOG_PATH));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return _instance;
	}
	private CILogger(File file) throws FileNotFoundException {
		super(file);
	}

	public void info(String msg) {
		String finalMsg = "[Info] " + getTime() + " " + msg;
		this.println(finalMsg);
		this.flush();
		System.out.println(finalMsg);
	}
	
	public void error(String msg) {
		String finalMsg = "[error] " + getTime() + " " + msg;
		this.println(finalMsg);
		this.flush();
		System.out.println(finalMsg);
	}
	
	public String getTime(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return df.format(new Date());
	}
}
