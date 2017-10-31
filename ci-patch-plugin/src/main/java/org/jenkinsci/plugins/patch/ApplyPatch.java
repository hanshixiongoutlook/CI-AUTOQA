package org.jenkinsci.plugins.patch;

import hudson.Launcher;
import hudson.Launcher.ProcStarter;
import hudson.Proc;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
	 * 
	 * <p>Discription:[采用lancher执行命令，可支持slave模式]</p>
	 * Created on 2017年10月31日
	 * @param build
	 * @param launcher
	 * @param filter
	 * @param listener
	 * @throws Exception
	 * @author[hanshixiong]
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void applyPatch(AbstractBuild build, Launcher launcher, int position, BuildListener listener) throws Exception {
		int prefixNum = getPrefixNumber(build.getWorkspace().toString(), position, listener);
		String[] command = new String[]{"patch", "-p" + prefixNum, "--binary", "--input=patch.diff"};
		this.execute(command, listener, launcher,build);
	}
	/**
	 * 
	 * <p>Discription:[采用lancher执行命令，可支持slave模式]</p>
	 * Created on 2017年10月31日
	 * @param command
	 * @param listener
	 * @param launcher
	 * @param build
	 * @throws Exception
	 * @author[hanshixiong]
	 */
	@SuppressWarnings("rawtypes")
	private void execute(String[] command, BuildListener listener, Launcher launcher, AbstractBuild build) throws Exception {
		ProcStarter pro = launcher.launch();
		pro.pwd(build.getWorkspace());
		pro.cmds(command);
		InputStream out = launcher.launch(pro).getStdout();
		if ( out!=null ) {
			// 注意下面的操作
			String buffer;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(out));
			while ((buffer = bufferedReader.readLine()) != null) {
				listener.getLogger().println("[patch] execute result : " + buffer);
			}
			bufferedReader.close();
		} else {
			listener.getLogger().println("[patch] out is null");
		}
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
	private int getPrefixNumber(String workspace, int position, BuildListener listener) throws IOException {
		String INDEX_FILTER = "Index:";
		String PATH_FILTER = "/";
		String NUM_STRING = "src";
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
		int result = position;
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].equals(NUM_STRING)) {
				result = result + i;
				listener.getLogger().println("[patch] patch -p $num is :" + result);
				return result;
			}
		}
		br.close();
		listener.getLogger().println("[patch] Oh, get num failed!");
		throw new IOException("Oh, Oh, get num failed!");
	}
}
