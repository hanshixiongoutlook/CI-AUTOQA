package org.jenkinsci.plugins.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jenkinsci.plugins.client.windows.MessageWindow;
import org.junit.Test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.ComputerSet;
import com.offbytwo.jenkins.model.ComputerWithDetails;
import com.offbytwo.jenkins.model.Executor;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueItem;

public class JenkinsHelperTest {
	private static final String JENKINS_URL = "http://10.95.121.181:8081/jenkins/";
	
	@Test
	public void test() throws URISyntaxException, IOException, InterruptedException {
		JenkinsServer jenkins = new JenkinsServer(new URI(JENKINS_URL));
		List<QueueItem> qList = jenkins.getQueue().getItems();
		JobWithDetails job = jenkins.getJob("Test2");
		for ( QueueItem i: qList ) {
			System.out.println(i.getTask().getName() + "...id=" + i.getId() +" ...isbuildable=" +i.getTask().getUrl());
			
			System.out.println(i.getExecutable());
//			Build b = jenkins.getBuild(i);
//			System.out.println("number=" + b.getNumber() +".." +b.getQueueId());
		}
//		JenkinsServer jenkins = new JenkinsServer(new URI(JENKINS_URL));
//		BuildInfo e = new BuildInfo();
//		e.setJobName("Test2");
//		e.setNumber(1);
//		
//		BuildInfo e2 = new BuildInfo();
//		e2.setJobName("TestDemo");
//		e2.setNumber(10);
//		builds.add(e );
//		builds.add(e2 );
//		this.monitor();
		
//		BuildInfo e3 = new BuildInfo();
//		e3.setBuilding(false);
////		e3.setResult(BuildResult.ABORTED);
//		System.out.println(builds.contains(e3));
	}
	@Test
	public void testGetProgress() throws IOException, InterruptedException {
		MessageWindow.info(MessageWindow.TitleEnum.FAILURE, "hello", MessageWindow.ExitEnum.NORMAL);
//		JenkinsHelper helper = JenkinsHelper.getInstance();
//		int num = 10;
//		Build b = null;
//		while (b == null) {
//			JobWithDetails job = helper.jenkins.getJob("TestDemo");
//			b = job.getBuildByNumber(num);
//			if (b != null) {
//				BuildWithDetails build = b.details();
//				System.out.println("Is building... " + build.isBuilding());
//				BuildResult result = build.getResult();
//				build.Stop();
//				System.out.println("Progress... ");
//				build.getBuiltOn();
//			}
//		}
	}
	public void getNum(ComputerSet computerset, int num) throws IOException {
		
		List<ComputerWithDetails> computers = computerset.getComputers();
		System.out.println("Number... " + num);
		for ( ComputerWithDetails c: computers ) {
//			System.out.println("Job... " + c.get);
			if ( c.getDisplayName().equals("TestDemo")) {
				List<Executor> exes = c.getExecutors();
				for ( Executor e: exes ) {
					if ( e.getNumber()==num ) {
						System.out.println("Progress... " + e.getProgress());
					}
				}
			}
		}
	}

}
