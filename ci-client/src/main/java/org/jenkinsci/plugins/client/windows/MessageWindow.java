package org.jenkinsci.plugins.client.windows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jenkinsci.plugins.client.CIApplication;
/**
 * 
 * <p>Description: [信息提示弹窗]</p>
 * Created on 2017年10月12日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class MessageWindow {
	/**
	 * 
	 * <p>Description: [弹窗标题]</p>
	 * Created on 2017年10月12日
	 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
	 * @version 1.0 
	 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
	 */
	public static enum TitleEnum {
		TIP("提示"),ERROR("错误"),SUCCESS("成功"),FAILURE("失败");
		public String title;
		TitleEnum(String title) {
			this.title = title;
		}
	}
	/**
	 * 
	 * <p>Description: [系统退出状态]</p>
	 * Created on 2017年10月12日
	 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
	 * @version 1.0 
	 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
	 */
	public static enum ExitEnum {
		FAILURE(-1),NORMAL(0);
		public int code;
		ExitEnum(int code){
			this.code = code;
		}
	}
	/**
	 * 
	 * <p>Discription:[提示信息]</p>
	 * Created on 2017年10月12日
	 * @param title 弹窗标题
	 * @param message 提示信息
	 * @param exitCode 退出状态
	 * @author[hanshixiong]
	 */
	public static void info(TitleEnum title, String message, ExitEnum exitCode) {
		Display display = new Display();
		Shell shell = new Shell();
		Composite composite = new Composite(shell, SWT.BORDER);
		Label messageLabel = new Label(composite, SWT.NONE);
		new MessageWindow().setComposite(composite)
						   .setShell(shell, title.title, exitCode.code)
						   .setOkBtn(composite, exitCode.code)
						   .setMessageLabel(messageLabel, title.title)
						   .setMessage(messageLabel, message)
						   .open(shell, display);
	}
	public static void confirm(TitleEnum title, String message, ExitEnum exitCode) {
		Display display = new Display();
		final Shell shell = new Shell();
		Composite composite = new Composite(shell, SWT.BORDER);
		Label messageLabel = new Label(composite, SWT.NONE);
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				shell.dispose();
			}
		});
		new MessageWindow().setComposite(composite)
						   .setShell(shell, title.title, exitCode.code)
						   .setConfirmBtn(shell, composite, exitCode.code)
						   .setMessageLabel(messageLabel, title.title)
						   .setMessage(messageLabel, message)
						   .open(shell, display);
	}
	/**
	 * 
	 * <p>Discription:[打开窗口]</p>
	 * Created on 2017年9月30日
	 * @author[hanshixiong]
	 */
	private MessageWindow open(Shell shell, Display display) {
		// 如果只是暂时隐藏则重新展示
		if ( !shell.isVisible() ) {
			shell.setVisible(true);
		}
		shell.open();
		shell.layout();
		while( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}
		display.dispose();
		return this;
	}
	/**
	 * 
	 * <p>Discription:[设置窗口大小和名称]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private MessageWindow setShell(Shell shell, String title, final int exitCode) {
		shell.setSize(310, 180);
		shell.setText(title);
		shell.setToolTipText(title);
		shell.addShellListener(new ShellAdapter() {
			// 点击x时退出程序，不填写svn账号无法进行后续操作
			public void shellClosed(ShellEvent e) {
				System.exit(exitCode);
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
	private MessageWindow setComposite(Composite composite) {
		composite.setBounds(0, 0, 310, 180);
		composite.setToolTipText("composite");
		return this;
	}
	
	/**
	 * 
	 * <p>Discription:[设置确定按钮]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private MessageWindow setOkBtn(Composite composite, final int exitCode) {
		Button confirmBtn = new Button(composite, SWT.NONE);
		confirmBtn.setBounds(90, 100, 100, 30);
		confirmBtn.setText("确定");
		confirmBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				System.exit(exitCode);
			}
		});
		return this;
	}
	
	/**
	 * 
	 * <p>Discription:[设置确定按钮]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private MessageWindow setConfirmBtn(final Shell shell, Composite composite, final int exitCode) {
		Button okBtn = new Button(composite, SWT.NONE);
		okBtn.setBounds(30, 100, 100, 30);
		okBtn.setText("确定");
		okBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				CIApplication.jenkinsHelper.stopAll(); // 退出前取消所有构建
				System.exit(exitCode);
			}
		});
		
		Button cancelBtn = new Button(composite, SWT.NONE);
		cancelBtn.setBounds(120, 100, 100, 30);
		cancelBtn.setText("取消");
		cancelBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				System.exit(exitCode);
				shell.dispose();
			}
		});
		return this;
	}
	/**
	 * 
	 * <p>Discription:[设置窗口默认提示信息]</p>
	 * Created on 2017年9月30日
	 * @return
	 * @author[hanshixiong]
	 */
	private MessageWindow setMessageLabel(Label message, String title) {
		message.setBounds(10, 30, 270, 20);
		message.setText(title);
		return this;
		
	}
	/**
	 * 
	 * <p>Discription:[自定义窗口提示信息]</p>
	 * Created on 2017年9月30日
	 * @param msg
	 * @return
	 * @author[hanshixiong]
	 */
	private MessageWindow setMessage(Label message, String msg) {
		if ( msg!=null && !msg.trim().isEmpty() ) {
			message.setText(msg);
		}
		return this;
	}
}
