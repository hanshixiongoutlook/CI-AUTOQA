package org.jenkinsci.plugins.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



import net.sf.json.JSONArray;

import org.jenkinsci.plugins.client.windows.MessageWindow;
/**
 * 
 * <p>Description: [启动参数处理]</p>
 * Created on 2017年10月10日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class MainArguments {
	private static MainArguments _instance;
	/** 提交文件列表存放的临时文件路径 */
	private String submitFileTmpPath;
	/** The depth with which the commit/update is done.*/
	private String svnDepth;
	/** comment存放临时文件路径 */
	private String commentTmpPath;
	/** 工作空间路径 */
	private String workspacePath;
	/** 待构架job */
	private String jobs;
//	private MainArguments(){}
	/**
	 * 
	 * <p>Discription:[获取构建job列表]</p>
	 * Created on 2017年10月10日
	 * @return
	 * @author[hanshixiong]
	 */
	public String[] getJobs() {
		JSONArray jobArray = JSONArray.fromObject(jobs);
		return (String[]) JSONArray.toArray(jobArray, String.class);
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public String getSubmitFileTmpPath() {
		return submitFileTmpPath;
	}

	public void setSubmitFileTmpPath(String submitFileTmpPath) {
		this.submitFileTmpPath = submitFileTmpPath;
	}

	public String getSvnDepth() {
		return svnDepth;
	}

	public void setSvnDepth(String svnDepth) {
		this.svnDepth = svnDepth;
	}

	public String getCommentTmpPath() {
		return commentTmpPath;
	}

	public void setCommentTmpPath(String commentTmpPath) {
		this.commentTmpPath = commentTmpPath;
	}

	public String getWorkspacePath() {
		return workspacePath;
	}

	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}

	/**
	 *
	 * <p>Discription:[SVN提交请求参数实例]</p>
	 * Created on 2017年9月20日
	 * @param args 0.submitFileTmpPath 1.svnDepth 2.commentTmpPath 3.workspacePath
	 * @return
	 * @author[hanshixiong]
	 */
	public static MainArguments getInstance(String[] args) {
		if ( _instance==null ) {
			_instance = new MainArguments();
			try {
				_instance.submitFileTmpPath = args[args.length-4];
				_instance.svnDepth = args[args.length-3];
				_instance.commentTmpPath = args[args.length-2];
				_instance.workspacePath = args[args.length-1];
				if ( args.length == 5 ) {
					_instance.jobs = args[args.length-5];
				}
			} catch (Exception e) {
				System.out.println("启动参数异常");
				e.printStackTrace();
				MessageWindow.info(MessageWindow.TitleEnum.ERROR, "启动参数异常", MessageWindow.ExitEnum.FAILURE);
			}
		}
		return _instance;
	}
	/**
	 * 
	 * <p>Discription:[获取差异文件列表]</p>
	 * Created on 2017年10月10日
	 * @return
	 * @throws IOException
	 * @author[hanshixiong]
	 */
	public File[] getChangeFiles() throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(this.submitFileTmpPath));
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(isr);
		String buf;
		List<File> files = new ArrayList<File>();
		while ( (buf=br.readLine()) !=null ) {
			System.out.println(buf);
			File f = new File(buf);
			if ( !f.exists() ) {
				throw new IOException("提交文件不存在，"+buf);
			}
			files.add(f);
		}
		return files.toArray(new File[files.size()]);
	}

}
