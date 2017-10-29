package org.jenkinsci.plugins.patch;

import hudson.model.BuildListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <p>Description: [应用patch工具类]</p>
 * Created on 2017年10月29日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class ApplyPatch {
	private static ApplyPatch _instance;
	private ApplyPatch() {}
	public static ApplyPatch getInstance() {
		if ( _instance==null ) {
			_instance = new ApplyPatch();
		}
		return _instance;
	}
	/**
	 * <p>
	 * 执行应用patch操作，patch -lp ${num} --binary < patch.diff </br>
	 * 需保证jobs/$job/workpace/patch.diff存在
	 * num从patch.diff中的index解析，Index: D:/demo-project/src/main/java/com/demo/demo.java，取src的脚标</br>
	 * </p>
	 * Created on 2017年10月29日
	 * @param workspace
	 * @return
	 * @throws Exception 
	 * @author[hanshixiong]
	 */
	public void applyPatch(String workspace, String filter, BuildListener listener) throws Exception {
		int prefixNum = getPrefixNumber(workspace, filter, listener);
		String cmdStr = "sudo patch -lp" + prefixNum + " --binary < patch.diff";
		this.execute(cmdStr, listener);
	}
	/**
	 * 
	 * <p>
	 * 解析patch.diff首行：Index: D:/demo-project/src/main/java/com/demo/demo.java</br>
	 * 取src脚标作为num值</br>
	 * 获取失败时，返回-1
	 * </p>
	 * Created on 2017年10月29日
	 * @param workspace job工作空间
	 * @return
	 * @throws IOException
	 * @author[hanshixiong]
	 */
	@SuppressWarnings("resource")
	private int getPrefixNumber(String workspace, String filter, BuildListener listener) throws IOException {
		listener.getLogger().println("[patch] job workspace :" + workspace + " filter is : " + filter);
		String INDEX_FILTER = "Index:";
		String PATH_FILTER = "/";
		String NUM_STRING = StringUtils.isEmpty(filter)?"src":filter;
		String patchFilePatch = workspace + File.separator + "patch.diff";
		listener.getLogger().println("[patch] patch.diff :" + patchFilePatch);
		// 判断patch.diff是否存在
		File patchFile = new File(patchFilePatch);
		if (!patchFile.exists()) {
			listener.getLogger().println("[patch] Oh, patch.diff dosn't exist!");
			throw new IOException("Oh, patch.diff dosn't exist!");
		}
		BufferedReader br = new BufferedReader(new FileReader(patchFilePatch));
		// Index: D:/demo-project/src/main/java/com/demo/demo.java
		String indexWorkPath = br.readLine();
		listener.getLogger().println("[patch] patch.diff first line is :" + indexWorkPath);
		// D:/demo-project/src/main/java/com/demo/demo.java
		indexWorkPath = indexWorkPath.split(INDEX_FILTER)[1];
		listener.getLogger().println("[patch] user svn workspace is :" + indexWorkPath);
		String[] paths = indexWorkPath.split(PATH_FILTER);
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].equals(NUM_STRING)) {
				listener.getLogger().println("[patch] patch -p $num is :" + i);
				return i;
			}
		}
		br.close();
		listener.getLogger().println("[patch] Oh, get num failed!");
		throw new IOException("Oh, Oh, get num failed!");
	}
	/**
	 * 
	 * <p>Discription:[执行Linux命令]</p>
	 * Created on 2017年10月29日
	 * @param command
	 * @throws Exception
	 * @author[hanshixiong]
	 */
	private void execute(String command, BuildListener listener) throws Exception {
		listener.getLogger().println("[patch] execute command : " + command);
		Process proc = Runtime.getRuntime().exec(command);
		// 注意下面的操作
		String buffer;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		while ((buffer = bufferedReader.readLine()) != null) {
			listener.getLogger().println("[patch] execute result : " + buffer);
		}
		bufferedReader.close();
		proc.waitFor();
	}

}
