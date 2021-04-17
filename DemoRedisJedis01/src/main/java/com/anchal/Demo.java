package com.anchal;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class Demo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	RedisClusterManager r1 = RedisClusterManager.getInstance();
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int i = Integer.parseInt(request.getParameter("num1"));
		int j = Integer.parseInt(request.getParameter("num2"));

		int k = i+j;
		r1.set("k",String.valueOf(k));
		
		
		
		PrintWriter out = response.getWriter();
		
		r1.set("k1",String.valueOf(k));
		out.print("Result: " + r1.get("k"));
		
	}
	
	public void destroy() {
		r1.close();
	}
	

}
