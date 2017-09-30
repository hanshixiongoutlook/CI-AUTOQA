package org.jenkinsci.plugins.client.subversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jenkinsci.plugins.client.CIApplication;
import org.jenkinsci.plugins.client.CIConfigure;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
/**
 * 
 * <p>Description: [差异文件助手]</p>
 * Created on 2017年9月30日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class CIPatchHelper {
	/** patch文件存放路径 */
	private final String PATCH_FILE = CIConfigure.DATA_PATH + "patch.diff";
	private static CIPatchHelper _instance;
	private static SVNClientManager svnClientManager;
	private CIPatchHelper(){}
	/**
	 * 
	 * <p>Discription:[设置SVN账号密码]</p>
	 * Created on 2017年9月25日
	 * @param svnuser
	 * @param svnpass
	 * @author[hanshixiong]
	 */
	public void setSVN(String svnuser, String svnpass) {
		@SuppressWarnings("deprecation")
		ISVNAuthenticationManager svnAuth = new BasicAuthenticationManager( svnuser , svnpass );
		svnClientManager.setAuthenticationManager(svnAuth);
	}
	public static CIPatchHelper getInstance(String svnuser, String svnpass) {
		if ( _instance==null ) {
			SVNRepositoryFactoryImpl.setup();
			@SuppressWarnings("deprecation")
			ISVNAuthenticationManager svnAuth = new BasicAuthenticationManager( svnuser , svnpass );
			svnClientManager = SVNClientManager.newInstance(null,svnAuth);
			_instance = new CIPatchHelper();
		}
		return _instance;
	}
	
	public static CIPatchHelper getInstance() {
		if ( _instance==null ) {
			SVNRepositoryFactoryImpl.setup();
			SVNAuth a =  CIApplication.ciConfigure.getSVNAuth();
			String username = a.getUsername();
			String password = a.getPassword();
			@SuppressWarnings("deprecation")
			ISVNAuthenticationManager svnAuth = new BasicAuthenticationManager( username , password );
			svnClientManager = SVNClientManager.newInstance(null,svnAuth);
			_instance = new CIPatchHelper();
			// 初始化patch.diff
			_instance.resetPatch();
		}
		return _instance;
	}
	
	/**
	 * 
	 * <p>Discription:[生成patch.diff文件]</p>
	 * Created on 2017年9月21日
	 * @param changeFile commit的变更文件
	 * @throws FileNotFoundException
	 * @throws SVNException
	 * @author[hanshixiong]
	 */
	public void doDiff(File changeFile) throws FileNotFoundException, SVNException {
		SVNDiffClient diffClient = svnClientManager.getDiffClient();
		File dataPath = new File(CIConfigure.DATA_PATH);
		if ( !dataPath.exists() ) {
			dataPath.mkdir();
		}
        OutputStream os = new FileOutputStream(this.getPatchFilePath(), true);
        diffClient.doDiff( changeFile,
        				   SVNRevision.UNDEFINED, 
        				   SVNRevision.WORKING, 
        				   SVNRevision.HEAD,
        				   SVNDepth.INFINITY, 
        				   true, 
        				   os, 
        				   null);
	}
	/**
	 * 
	 * <p>Discription:[获取patch.diff文件]</p>
	 * Created on 2017年9月21日
	 * @return File
	 * @author[hanshixiong]
	 */
	public File getPatchFile() {
		return new File(PATCH_FILE);
	}
	/**
	 * 
	 * <p>Discription:[获取patch.diff文件路径]</p>
	 * Created on 2017年9月21日
	 * @return
	 * @author[hanshixiong]
	 */
	public String getPatchFilePath() {
		return PATCH_FILE;
	}
	/**
	 * 
	 * <p>Discription:[重置patch.diff，如果存在则删除patch.diff，不存在则什么都不做]</p>
	 * Created on 2017年9月21日
	 * @author[hanshixiong]
	 */
	private void resetPatch() {
		File diff = this.getPatchFile();
		if ( diff.exists() ) {
			diff.delete();
		}
	}
}
