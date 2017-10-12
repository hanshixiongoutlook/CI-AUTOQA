package org.jenkinsci.plugins.client;

import org.jenkinsci.plugins.client.windows.CIMonitorWindow;
import org.junit.Test;

public class CIMonitorWindowTest {

	@Test
	public void testBrowser() {
		CIMonitorWindow.getInstance().open("file:///D:/ci/AA-gbts_12_proton/index.html");
	}
}
