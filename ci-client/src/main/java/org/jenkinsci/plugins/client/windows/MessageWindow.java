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
/**
 * 
 * <p>Description: [信息弹窗]</p>
 * Created on 2017年9月30日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class MessageWindow {
	private final Display display = Display.getDefault();
	private final Shell shell = new Shell();
	private final Composite composite = new Composite(shell, SWT.BORDER);
	private final Label message = new Label(composite, SWT.NONE);
	private MessageWindow(){
		this.setComposite()
		 .setShell()
		 .setConfirmBtn()
		 .setMessage();
	}
	/**
	 * 
	 * <p>Discription:[弹出窗口]</p>
	 * Created on 2017年9月30日
	 * @param message 异常信息
	 * @author[hanshixiong]
	 */
	public static void exception(String message) {
		new MessageWindow().setMessage(message).open();
	}
	/**
	 * 
	 * <p>Discription:[弹出窗口，展示默认信息]</p>
	 * Created on 2017年9月30日
	 * @author[hanshixiong]
	 */
	public static void exception() {
		new MessageWindow().open();
	}
	
	/**
	 * 
	 * <p>Discription:[打开窗口]</p>
	 * Created on 2017年9月30日
	 * @author[hanshixiong]
	 */
	private void open() {
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
		
	}
	/**
	 * 
	 * <p>Discription:[设置窗口大小和名称]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private MessageWindow setShell() {
		shell.setSize(310, 180);
		shell.setText("系统异常！");
		shell.setToolTipText("异常");
		shell.addShellListener(new ShellAdapter() {
			// 点击x时退出程序，不填写svn账号无法进行后续操作
			public void shellClosed(ShellEvent e) {
				System.exit(-1);
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
	private MessageWindow setComposite() {
		composite.setBounds(12, 10, 270, 120);
		composite.setToolTipText("composite");
		return this;
	}
	
	/**
	 * 
	 * <p>Discription:[设置确定按钮]</p>
	 * Created on 2017年9月26日
	 * @author[hanshixiong]
	 */
	private MessageWindow setConfirmBtn() {
		Button confirmBtn = new Button(composite, SWT.NONE);
		confirmBtn.setBounds(80, 80, 100, 30);
		confirmBtn.setText("确定");
		confirmBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				System.exit(-1);
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
	private MessageWindow setMessage() {
		message.setBounds(10, 30, 270, 20);
		message.setText("提示");
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
	private MessageWindow setMessage(String msg) {
		if ( msg!=null && !msg.trim().isEmpty() ) {
			message.setText(msg);
		}
		return this;
	}
}
