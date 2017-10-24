package hudson.scm.patch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ApplyPatch {
	
	public static void main(String[] args) throws Exception {
		 String cmdstring = "echo hello"; //这里也可以是ksh等
		 Process proc = Runtime.getRuntime().exec(cmdstring);
		 
		 proc = Runtime.getRuntime().exec(cmdstring);
		 // 注意下面的操作 
		 String ls_1;
		 BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(proc.getInputStream()));
		 while ( (ls_1=bufferedReader.readLine()) != null) {
			 System.out.println(ls_1);
		 }
		 bufferedReader.close();
		 proc.waitFor();
	}

}
