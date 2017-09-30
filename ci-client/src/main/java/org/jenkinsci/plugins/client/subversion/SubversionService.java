package org.jenkinsci.plugins.client.subversion;

import java.io.File;
import java.io.OutputStream;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.ISVNSession;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SubversionService {
	@Test
	public void testCheckout() throws SVNException {
		SVNRepositoryFactoryImpl.setup();
		SVNURL url = SVNURL.parseURIDecoded("svn://");
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		
		SVNClientManager svnClient = SVNClientManager.newInstance((DefaultSVNOptions)options, "***", "***");
		File dstPath = new File("/Users/sky_han/testsvn");
		SVNUpdateClient updateClient = svnClient.getUpdateClient();
		long version = updateClient.doCheckout(url, dstPath, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY,false);
//		ISVNSession i = svnClient.get
	}
	
	
	
	@Test
	public void checkout() {
		try {
			ISVNAuthenticationManager svnAuth = new BasicAuthenticationManager( "****" , "****" );
			SVNClientManager svnClientManager = SVNClientManager.newInstance(null,svnAuth);
			
			SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
			ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
			SVNURL url = SVNURL.parseURIDecoded("svn://");
			SVNRepository repository = SVNRepositoryFactory.create( url , null );
			repository.setAuthenticationManager(svnAuth);
			File dstPath = new File("/Users/sky_han/testsvn");
//			updateClient.doExport(url, dstPath , SVNRevision.HEAD, SVNRevision.HEAD, null, true, SVNDepth.INFINITY);
			updateClient.doCheckout(url, dstPath, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false);
//			updateClient.doCheckout(url, dstPath, SVNRevision.HEAD, SVNRevision.HEAD, false);

		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void svnWcc() {
			ISVNAuthenticationManager svnAuth = new BasicAuthenticationManager( "****" , "*****" );
			SVNClientManager svnClientManager = SVNClientManager.newInstance(null,svnAuth);
			
			SVNWCClient wcClient = svnClientManager.getWCClient();
			SVNDiffClient diffClient = svnClientManager.getDiffClient();
			SVNCommitClient commitClient = svnClientManager.getCommitClient();
//			commitClient.getOperationsFactory().createDiff().
//			wcClient.
//			SVNURL url = SVNURL.parseURIDecoded("svn://");
//			SVNRepository repository = SVNRepositoryFactory.create( url , null );
//			repository.setAuthenticationManager(svnAuth);
//			File dstPath = new File("/Users/sky_han/testsvn");
//			updateClient.doExport(url, dstPath , SVNRevision.HEAD, SVNRevision.HEAD, null, false, SVNDepth.EMPTY);
	}

}
