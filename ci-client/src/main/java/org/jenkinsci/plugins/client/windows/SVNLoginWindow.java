package org.jenkinsci.plugins.client.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jenkinsci.plugins.client.CIApplication;
/**
 * 
 * <p>Description: [SVN登录信息录入窗口]</p>
 * Created on 2017年9月26日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class SVNLoginWindow {
	private static final String SVN_EXCEPTION = "不能完成SVN认证，即将退出持续集成！";
	private final Display display = Display.getDefault();
	private final Shell shell = new Shell();
	private final Composite composite = new Composite(shell, SWT.BORDER);
	private final Group group = new Group(composite, SWT.NONE);
	private final Text nameText = new Text(group, SWT.BORDER);
	private final Text passwdText = new Text(group, SWT.PASSWORD);
	
	private SVNLoginWindow(){
		this.setComposite()
				 .setShell()
				 .setCancelBtn()
				 .setConfirmBtn()
				 .setGroup()
				 .setNameInput()
				 .setPasswordInput();
	}
	/**
	 * 
	 * <p>Discription:[启动SVN账号密码录入窗口]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	public static void show() {
		new SVNLoginWindow().open();;
	}
	public void open() {
		shell.open();
		shell.layout();
		while( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}
		display.dispose();
	}
	/**
	 * 
	 * <p>Discription:[设置窗口大小和名称]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private SVNLoginWindow setShell() {
		shell.setSize(260, 310);
		shell.setText("SVN登录");
		shell.setToolTipText("输入SVN登录信息");
		shell.addShellListener(new ShellAdapter() {
			// 点击x时退出程序，不填写svn账号无法进行后续操作
			public void shellClosed(ShellEvent e) {
				shell.dispose();
				MessageWindow.exception(SVN_EXCEPTION);
			}
		});
		return this;
	}
	
	/**
	 * 
	 * <p>Discription:[设置Composite大小]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private SVNLoginWindow setComposite() {
		composite.setBounds(20, 25, 210, 220);
		composite.setToolTipText("composite");
		return this;
	}
	/**
	 * 
	 * <p>Discription:[设置组件组]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private SVNLoginWindow setGroup() {
		group.setBounds(20, 20, 160, 160);
		group.setText("请输入:");
		group.setToolTipText("group");
		return this;
	}
	
	/**
	 * 
	 * <p>Discription:[设置用户名输入框]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private SVNLoginWindow setNameInput() {
		Label nameLabel = new Label(group, SWT.NONE);
		nameLabel.setBounds(10, 30, 45, 20);
		nameLabel.setText("用户名：");
		nameText.setBounds(65, 25, 80, 25);
		return this;
	}
	private SVNLoginWindow setPasswordInput() {
		Label passwdLabel = new Label(group, SWT.NONE);
		passwdLabel.setBounds(10, 70, 45, 20);
		passwdLabel.setText("密码：");
		passwdText.setBounds(65, 65, 80, 25);
		return this;
	}
	/**
	 * 
	 * <p>Discription:[设置确定按钮]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private SVNLoginWindow setConfirmBtn() {
		Button confirmBtn = new Button(group, SWT.NONE);
		confirmBtn.setBounds(40, 120, 50, 25);
		confirmBtn.setText("确定");
		confirmBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String username = nameText.getText();
				String password = passwdText.getText();
				if ( username==null || username.trim().isEmpty() ) {
					group.setText("请输入用户名!");
					return;
				}
				if ( password==null || password.trim().isEmpty() ) {
					group.setText("请输入用户名!");
					return;
				}
				CIApplication.getCIConfig().setSVNAuth(username, password);
				shell.dispose();
			}
		});
		return this;
	}
	/**
	 * 
	 * <p>Discription:[设置取消按钮]</p>
	 * Created on 2017年9月26日
	 * @return
	 * @author[hanshixiong]
	 */
	private SVNLoginWindow setCancelBtn() {
		Button cancelBtn = new Button(group, SWT.NONE);
		cancelBtn.setBounds(95, 120, 50, 25);
		cancelBtn.setText("取消");
		cancelBtn.addSelectionListener(new SelectionAdapter(){
			// 点击取消时退出程序，不填写svn账号无法进行后续操作
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
				MessageWindow.exception(SVN_EXCEPTION);
			}
		});
		return this;
	}
	
}
