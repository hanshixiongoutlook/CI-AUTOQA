package org.jenkinsci.plugins.han.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;

import org.apache.commons.httpclient.HttpStatus;
import org.jenkinsci.plugins.han.entity.BuildBean;
import org.jenkinsci.plugins.han.entity.BuildEntity;
import org.jenkinsci.plugins.han.entity.BuildQueryEntity;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Extension;
import hudson.model.Build;
import hudson.model.Job;
import hudson.model.JobPropertyDescriptor;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;

import static hudson.Util.filter;
@Extension
public class BuildMonitorAction implements RootAction {
	private static final String STATIC_PATH = "/static" + File.separator;
    public String getIconFileName() {
        return "terminal.png";
    }

    public String getDisplayName() {
        return "Build Monitor";
    }

    public String getUrlName() {
        return "build-monitor";
    }
    
    /**
     * 静态资源代理.
     * 静态资源放置在/resources/static/目录.
     * 获取方式${request.contextPath}/${urlName}/static/{file}
     * @param request
     * @param response
     */
    public void doStatic(StaplerRequest request, StaplerResponse response) {
    		this.setContentType(request, response);
    		try {
    			InputStream file = BuildMonitorAction.class.getResourceAsStream(STATIC_PATH + request.getRestOfPath());
    			// 访问资源不存在，返回404
    			if ( file == null ) {
    				response.setStatus(HttpStatus.SC_NOT_FOUND);
    				return;
    			}
    			ServletOutputStream out = response.getOutputStream();
	    		byte[] buf = new byte[1024];
	    		while(file.read(buf) >0) {
	    			out.write(buf);
	    		}
	    		out.flush();
	    		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    private void setContentType(StaplerRequest request, StaplerResponse response) {
    		String filePath = request.getRestOfPath();
    		response.setCharacterEncoding("utf-8");
		if ( filePath.endsWith(".css") ) 
			response.setContentType("text/css");
		else if ( filePath.endsWith(".js") ) {
			response.setContentType("text/javascript");
		}
		else if ( filePath.endsWith(".png") )
			response.setContentType("image/png");
		else if ( filePath.endsWith(".jpe")||filePath.endsWith(".jpeg") )
			response.setContentType("image/jpeg");
		else if ( filePath.endsWith(".jpg") )
			response.setContentType("application/x-jpg");
    }
    
    public void doHello(StaplerRequest request, StaplerResponse response) {
    		try {
    			Map<String, String[]> param = request.getParameterMap();
    			Iterator<Entry<String, String[]>> paramIt = param.entrySet().iterator();
    			while ( paramIt.hasNext() ) {
    				Entry<String, String[]> p = paramIt.next();
    				System.out.println(p.getKey() + "..." +Arrays.toString(p.getValue()));
    			}
			PrintWriter writer = response.getWriter();
			writer.write("Hello Build Monitor");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void doBuildProgress(StaplerRequest request, StaplerResponse response) {
    		Jenkins _instance = Jenkins.getInstance();
    		JobPropertyDescriptor jobs = _instance.getJobProperty("");
    		List<Job<?, ?>> projects = new ArrayList(filter(_instance.getAllItems(), Job.class));
    		System.out.println("items..."+projects.size());
    		System.out.println("items..."+projects.get(0).getBuildByNumber(Integer.valueOf(request.getParameter("num"))).getExecutor().getProgress());
    
    }
    /**
     * 加载构建信息
     * @param request querys:jobName1~buildNum,jobName2~buildNum
     * @param response
     * @throws IOException 
     */
    public void doLoadBuild(StaplerRequest request, StaplerResponse response ) throws IOException {
    		String queryStr = request.getParameter("querys");
    		if ( queryStr == null || queryStr.trim().isEmpty() ) {
    			// 返回参数错误
    			return;
    		}
    		List<BuildQueryEntity> querys = BuildQueryEntity.getQuerys(queryStr);
    		BuildEntity buildEntity = new BuildEntity();
    		List<BuildBean> result = buildEntity.getBuilds(querys);
    		PrintWriter writer = response.getWriter();
		writer.write(JSONArray.fromObject(result).toString());
		writer.flush();
    }
    
    
}
