package org.jenkinsci.plugins.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.jenkinsci.plugins.client.build.JenkinsHelper;
import org.jenkinsci.plugins.client.subversion.CIPatchHelper;
import org.jenkinsci.plugins.client.windows.MessageWindow;
import org.jenkinsci.plugins.client.windows.SVNLoginWindow;

public class CIApplication {
	public static CIPatchHelper patchHelper;
	public static CIConfigure ciConfigure;
	public static MainArguments mainArguments;
	public static JenkinsHelper jenkinsHelper;
	public static void main(String[] args) {
		Properties property = System.getProperties();
		property.setProperty("user.dir", property.getProperty("sun.boot.library.path")); // 切换工作路径
		printArgs(args);
		ciConfigure = CIConfigure.getInstance();
		patchHelper = CIPatchHelper.getInstance();
		jenkinsHelper = JenkinsHelper.getInstance();
		if ( args.length < 3 )
			args = initTestArgs();
		
		// 初始化持续集成参数
		mainArguments = MainArguments.getInstance(args);
		
		// 检查SVN认证信息
		while ( !ciConfigure.isSVNEffective() ) {
			System.out.println("启动SVN认证"); 
			SVNLoginWindow.show();
			System.out.println("已认证"); 
		}
		
		// 生成patch文件
		try {
			File[] changeFiles =  mainArguments.getChangeFiles();
			if ( changeFiles!=null ) {
				for ( File file: changeFiles ) {
					patchHelper.doDiff(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageWindow.info(MessageWindow.TitleEnum.ERROR, "生成patch文件失败！", MessageWindow.ExitEnum.FAILURE);
		}
		// 启动远程构建
		jenkinsHelper.buildAll();
		
		while(true);
	}
	public static CIConfigure getCIConfig(){
		return ciConfigure;
	}
	
	// Just for test
	private static String[] initTestArgs() {
		String workspace = "D:\\workspace\\ci-test";
		String svnDepth = "3";
		String submitFileTmpPath = "D:\\workspace\\graduate-project\\CI-AUTOQA\\ci-client\\src\\test\\resources\\submitFileTmpPath";
		String commentTmpPath = "D:\\workspace\\online_meal\\ci-client\\src\\test\\resources\\commentTmpPath";
		String[] args = {"['Test']", submitFileTmpPath, svnDepth, commentTmpPath, workspace };
		System.out.println( "Arg : " + args);
		return args;
	}
	
	public static void printArgs(String[] args) {
		try {
			FileOutputStream fos = new FileOutputStream(new File("D:\\svn-submit.txt"), true);
			PrintWriter writer = new PrintWriter(fos);
			if ( args!=null ) {
				for ( String arg: args ) {
					writer.write(arg+"\r\n");
					System.out.println("Arg : " + arg);
				}
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
