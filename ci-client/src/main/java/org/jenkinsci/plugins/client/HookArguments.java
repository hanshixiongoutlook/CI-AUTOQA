package org.jenkinsci.plugins.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HookArguments {
	private static HookArguments _instance;
	/** 提交文件列表存放的临时文件路径 */
	private String submitFileTmpPath;
	/** The depth with which the commit/update is done.*/
	private String svnDepth;
	/** comment存放临时文件路径 */
	private String commentTmpPath;
	/** 工作空间路径 */
	private String workspacePath;
	
	private HookArguments(){}
	
	public static HookArguments getInstance() {
		if ( _instance==null ) {
			return new HookArguments();
		}
		return _instance;
	}
	/**
	 * 
	 * <p>Discription:[SVN提交请求参数实例]</p>
	 * Created on 2017年9月20日
	 * @param args 0.submitFileTmpPath 1.svnDepth 2.commentTmpPath 3.workspacePath
	 * @return
	 * @author[hanshixiong]
	 */
	public static HookArguments getInstance(String[] args) {
		if ( _instance==null ) {
			HookArguments hookManage = new HookArguments();
			hookManage.setSubmitFileTmpPath(args[0]);
			hookManage.setSvnDepth(args[1]);
			hookManage.setCommentTmpPath(args[2]);
			hookManage.setWorkspacePath(args[3]);
			return hookManage;
		}
		return _instance;
	}
	
	/**
	 * 
	 * <p>Discription:[提交文件列表存放的临时文件路径]</p>
	 * Created on 2017年9月19日
	 * @return
	 * @author[hanshixiong]
	 */
	public String getSubmitFileTmpPath() {
		return submitFileTmpPath;
	}
	/**
	 * 
	 * <p>Discription:[提交文件列表存放的临时文件路径]</p>
	 * Created on 2017年9月19日
	 * @param submitFileTmpPath
	 * @author[hanshixiong]
	 */
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
	
	public File[] getChangeFiles() throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(this.getSubmitFileTmpPath()));
		BufferedReader br = new BufferedReader(isr);
		String buf;
		File[] result = new File[]{};
		List<File> files = new ArrayList<File>();
		while ( (buf=br.readLine()) !=null ) {
			System.out.println(buf);
			File f = new File(buf);
			if ( !f.exists() ) {
				throw new IOException("提交文件不存在，"+buf);
			}
			files.add(f);
		}
		return files.toArray(result);
	}

}
