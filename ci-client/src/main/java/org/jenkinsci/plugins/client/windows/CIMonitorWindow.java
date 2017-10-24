package org.jenkinsci.plugins.client.windows;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jenkinsci.plugins.client.CIApplication;
public class CIMonitorWindow {
	private static CIMonitorWindow _instance = null;
	private static Display display = new Display();
	private static Shell shell = new Shell(display);
	private static final Browser browser = new Browser(shell, SWT.FILL);
	
	public static CIMonitorWindow getInstance() {
		if ( _instance == null ) {
			return new CIMonitorWindow();
		}
		return _instance;
	}
	//http://10.95.121.180:8084/jenkins/build-monitor/?querys=CHECKSTYLE_COMMON~127
	public void open(String url) {
		shell.setText("构建进度");
		shell.setSize(800, 600);
		browser.setBounds(0, 0, 800, 580);
		browser.setUrl(url);
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				System.out.println("[Info] stop all");
				CIApplication.jenkinsHelper.stopAll(); // 取消所有构建
				System.exit(-1);
//				MessageWindow.confirm(MessageWindow.TitleEnum.TIP, "确认取消构建？", MessageWindow.ExitEnum.FAILURE);
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
