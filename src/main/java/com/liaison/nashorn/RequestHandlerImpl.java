package com.liaison.nashorn;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class RequestHandlerImpl implements HttpHandler {
	
	private String RSC_JS_FOLDER = "./services";
	private ScriptEngine e = null;
	
	public RequestHandlerImpl() {
		this.e = new ScriptEngineManager().getEngineByName("nashorn");
	}
	
	public void setEngine(ScriptEngine e) {
		this.e = e;
	}
	
	public void setJSFolder(String path) {
		this.RSC_JS_FOLDER = path;
	}
	
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		String response = "NOT FOUND";
		int rCode = 404;
		
		
		System.out.println("[" + arg0.getRequestMethod().toString() + "] Request at path : " + arg0.getHttpContext().getPath());
		
		try {
			this.e.eval("print(\"Engine ready..\");");
			this.e.eval(new FileReader(RSC_JS_FOLDER + arg0.getHttpContext().getPath() + ".js"));
			
			Invocable i = (Invocable) this.e;
			
			if(arg0.getRequestMethod().toString().equals("GET"))  {
				try {
					String arg = "";
					Object r = i.invokeFunction("doGet", arg);
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
			e1.printStackTrace();
		}
		
		arg0.sendResponseHeaders(rCode, response.length());
		OutputStream os = arg0.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
