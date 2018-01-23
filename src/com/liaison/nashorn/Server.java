package com.liaison.nashorn;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.InetSocketAddress;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Server {
	
	private static final String RSC_JS_FOLDER = "./resources/js"; 
	
	public static void main(String[] args)  {
		try {
			System.out.println("Server at 8001..");
			HttpServer s = HttpServer.create(new InetSocketAddress(8001), 0);
			
			s.createContext("/json2Table", new RequestHandlerImpl());
			s.setExecutor(null);
			s.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static class RequestHandlerImpl implements HttpHandler {

		@Override
		public void handle(HttpExchange arg0) throws IOException {
			String response = "OK";
			int rCode = 200;
			
			
			System.out.println("[" + arg0.getRequestMethod().toString() + "] Request at path : " + arg0.getHttpContext().getPath());
			
			ScriptEngine e = new ScriptEngineManager().getEngineByName("nashorn");
			try {
				e.eval("print(\"Engine ready..\");");
				e.eval(new FileReader(RSC_JS_FOLDER + arg0.getHttpContext().getPath() + ".js"));
				
				Invocable i = (Invocable) e;
				
				if(arg0.getRequestMethod().toString().equals("GET"))  {
					try {
						Object r = i.invokeFunction("doGet");
						response = r.toString();
						rCode = 200;
					} catch (NoSuchMethodException e1) {
						e1.printStackTrace();
					}
				} else if (arg0.getRequestMethod().toString().equals("POST")) {
					final int bufferSize = 1024;
					final char[] buffer = new char[bufferSize];
					final StringBuilder out = new StringBuilder();
					Reader in = new InputStreamReader(arg0.getRequestBody(), "UTF-8");
					for (; ; ) {
					    int rsz = in.read(buffer, 0, buffer.length);
					    if (rsz < 0)
					        break;
					    out.append(buffer, 0, rsz);
					}
					try {
						Object r = i.invokeFunction("doPost", out.toString());
						if(r instanceof ScriptObjectMirror) {
							response = (String) ((ScriptObjectMirror) r).getMember("resp");
							rCode = Integer.parseInt(((ScriptObjectMirror) r).getMember("code").toString());
						} else {
							response = "NOT OK";
							rCode = 500;
						}

					} catch (NoSuchMethodException e1) {
						e1.printStackTrace();
					}
				} else {
					response = "Unsupported method.";
					rCode = 404;
				}
				
				
				
			} catch (ScriptException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			arg0.sendResponseHeaders(rCode, response.length());
			OutputStream os = arg0.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		
	}

}
