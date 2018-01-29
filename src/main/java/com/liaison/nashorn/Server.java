package com.liaison.nashorn;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Server {
	private static HttpServer s = null; 
	
	public static void setServer(HttpServer s) {
		Server.s = s;
	}
	
	public static void main(String[] args)  {
		try {
			
			if(s == null) {
				System.out.println("Server at 8001..");
				s = HttpServer.create(new InetSocketAddress(8001), 0);				
			}
			
			s.createContext("/json2Table", new RequestHandlerImpl());
			s.setExecutor(null);
			s.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
