package org.jenkinsci.plugins.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class HookArgumentsTest {
	private String[] args;
	private HookArguments hookArgs;
	@Before
	public void setUp() {
		String workspace = "D:\\workspace\\ci-test";
		String svnDepth = "3";
		String submitFileTmpPath = "D:\\workspace\\online_meal\\ci-client\\src\\test\\resources\\submitFileTmpPath";
		String commentTmpPath = "D:\\workspace\\online_meal\\ci-client\\src\\test\\resources\\commentTmpPath";
		args = new String[]{ submitFileTmpPath, svnDepth, commentTmpPath, workspace };
		hookArgs = HookArguments.getInstance(args);
	}
	
	@Test
	public void getSubmits() throws IOException {
		File[] files = hookArgs.getChangeFiles();
		
		for ( File f: files ) {
			System.out.println(f.getName());
		}
	}
}
