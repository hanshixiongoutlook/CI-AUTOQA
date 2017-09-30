package org.jenkinsci.plugins.client
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


class StartArgs {
	private static StartArgs _instance;
	/** 提交文件列表存放的临时文件路径 */
	String submitFileTmpPath;
	/** The depth with which the commit/update is done.*/
	String svnDepth;
	/** comment存放临时文件路径 */
	String commentTmpPath;
	/** 工作空间路径 */
	String workspacePath;
	
	private HookArguments(){}
	
	public static StartArgs getInstance() {
		if ( _instance==null ) {
			return new StartArgs();
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
	public static StartArgs getInstance(String[] args) {
		if ( _instance==null ) {
			_instance = new StartArgs();
			_instance.submitFileTmpPath = args[0];
			_instance.svnDepth = args[1];
			_instance.commentTmpPath = args[2];
			_instance.workspacePath = args[3];
		}
		return _instance;
	}
	
	public File[] getChangeFiles() throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(this.getSubmitFileTmpPath()));
		BufferedReader br = new BufferedReader(isr);
		String buf;
		File[] result = [];
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
