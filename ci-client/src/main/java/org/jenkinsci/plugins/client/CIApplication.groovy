package org.jenkinsci.plugins.client

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.jenkinsci.plugins.client.build.JenkinsHelper
import org.jenkinsci.plugins.client.subversion.CIPatchHelper;
import org.jenkinsci.plugins.client.windows.SVNLoginWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CIApplication {
	public static CIPatchHelper patchHelper;
	public static CIConfigure ciConfigure;
	public static MainArguments mainArguments;
	public static JenkinsHelper jenkinsHelper;
	private static Logger LOG = LoggerFactory.getLogger(CIApplication.class);
	static void main(String[] args) {
		LOG.error "aaaaaaaaa"
		LOG.info "aaaaaaaaa"
		ciConfigure = CIConfigure.getInstance();
		patchHelper = CIPatchHelper.getInstance();
		jenkinsHelper = JenkinsHelper.getInstance();
		/* #### 测试用数据#### */
		args = initTestArgs();
		
		// 初始化持续集成参数
		mainArguments = MainArguments.getInstance(args);
		
		// 检查SVN认证信息
		while ( !ciConfigure.isSVNEffective() ) {
			println "启动SVN认证"
			SVNLoginWindow.show();
			println "已认证"
		}
		
		// 生成patch文件
		File[] changeFiles =  mainArguments.getChangeFiles();
		if ( changeFiles!=null ) {
			changeFiles.each { file ->
				patchHelper.doDiff(file);
			}
		}
		// 启动远程构建
		jenkinsHelper.buildAll();
		
		while(true);
//		printArgs(args);
		// 1. 初始化提交参数
//		HookArguments hookManage = HookArguments.getInstance(args);
		// 2. 生成patch文件 | 处理comment
		
		
		// 3. 触发Jenkins构建
		
		// 4. 监控Jenkins构建
		
		// 5. 决策
//		System.exit(-1);
//		Demo.createAndShowGUI();
		
//		SwtBrowser.getInstance().open("");
	}
	public static CIConfigure getCIConfig(){
		return ciConfigure;
	}
	
	// Just for test
	private static String[] initTestArgs() {
		String workspace = "D:\\workspace\\ci-test";
		String svnDepth = "3";
		String submitFileTmpPath = "D:\\workspace\\online_meal\\ci-client\\src\\test\\resources\\submitFileTmpPath";
		String commentTmpPath = "D:\\workspace\\online_meal\\ci-client\\src\\test\\resources\\commentTmpPath";
		String[] args = ["['TestDemo','Test2']", submitFileTmpPath, svnDepth, commentTmpPath, workspace ];
		println "Arg : " + args;
		return args;
	}
	
	private static void printArgs(String[] args) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(new File("D:\\svn-submit.txt"));
		PrintWriter writer = new PrintWriter(fos);
		if ( args!=null ) {
			for ( String arg: args ) {
				writer.write(arg+"\r\n");
				println "Arg : " + arg;
			}
		}
		writer.flush();
	}
}
