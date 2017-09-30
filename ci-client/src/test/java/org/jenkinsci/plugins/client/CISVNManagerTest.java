package org.jenkinsci.plugins.client;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.jenkinsci.plugins.client.subversion.CIPatchHelper;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.SVNPath;
import org.tmatesoft.svn.core.wc.ISVNDiffGenerator;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class CISVNManagerTest {
	private String[] args;
	SVNClientManager svnClientManager;
	private HookArguments hookArgs;
	@Before
	public void setUp() {
		SVNRepositoryFactoryImpl.setup();
		String workspace = "D:\\workspace\\ci-test";
		String svnDepth = "3";
		String submitFileTmpPath = "D:\\workspace\\online_meal\\ci-client\\src\\test\\resources\\submitFileTmpPath";
		String commentTmpPath = "D:\\workspace\\online_meal\\ci-client\\src\\test\\resources\\commentTmpPath";
		args = new String[]{ submitFileTmpPath, svnDepth, commentTmpPath, workspace };
		hookArgs = HookArguments.getInstance(args);
		ISVNAuthenticationManager svnAuth = new BasicAuthenticationManager( "*****" , "*****" );
		svnClientManager = SVNClientManager.newInstance(null,svnAuth);
	}
	
	@Test
	public void testDoDiff() throws SVNException, IOException {
		try {
			HookArguments hook = HookArguments.getInstance(args );
			SVNDiffClient diffClient = svnClientManager.getDiffClient();
			OutputStream os = new FileOutputStream("D:\\patch.diff");
			diffClient.doDiff(hookArgs.getChangeFiles()[0], 
							  SVNRevision.UNDEFINED, 
							  SVNRevision.WORKING, 
							  SVNRevision.HEAD,  
							  SVNDepth.INFINITY, 
							  true, 
							  os, 
							  null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test() throws Exception {
		CIPatchHelper ciMng = CIPatchHelper.getInstance("*****", "*****");
		ciMng.doDiff(hookArgs.getChangeFiles()[0]);
	}
	@Test
	public void doDiffTest() throws SVNException, IOException {
        File wcRoot = new File("D:/workspace/ci-test/online-meal-scheduler/pom.xml");
        File wcRoot2 = new File("D:\\");
        try {

//            SVNClientManager clientManager = SVNClientManager.newInstance();
//            SVNWCClient wcClient = SVNClientManager.newInstance().getWCClient();
            //set some property on a working copy directory
//            wcClient.doSetProperty(new File(wcRoot, "A/B"), "spam", SVNPropertyValue.create("egg"), false,
//                    SVNDepth.EMPTY, null, null);

            //5. now run diff the working copy against the repository
            SVNDiffClient diffClient = svnClientManager.getDiffClient();
            /*
             * This corresponds to 'svn diff -rHEAD'.
             */
            OutputStream os = new FileOutputStream("D:\\patch.diff",true);
            diffClient.doDiff(wcRoot, SVNRevision.UNDEFINED, SVNRevision.WORKING, SVNRevision.HEAD,
                    SVNDepth.INFINITY, true, os, null);
            diffClient.doDiff(wcRoot2, SVNRevision.UNDEFINED, SVNRevision.WORKING, SVNRevision.HEAD,
                    SVNDepth.INFINITY, true, os, null);
        } catch (SVNException svne) {
            System.out.println(svne.getErrorMessage());
            System.exit(1);
        }
	}

}
