package test.com.liaison.nashorn;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.script.ScriptEngine;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.*;

import com.liaison.nashorn.RequestHandlerImpl;

public class RequestHandlerImplTest {
	
	private RequestHandlerImpl unit = new RequestHandlerImpl();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.unit = new RequestHandlerImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHandle() {
		HttpExchange exch = mock(HttpExchange.class);
		HttpContext ctx = mock(HttpContext.class);
		
		when(exch.getRequestMethod()).thenReturn("OPTION");
		when(exch.getHttpContext()).thenReturn(ctx);
		when(exch.getResponseBody()).thenReturn(mock(OutputStream.class));
		when(ctx.getPath()).thenReturn("/json2Table");
		
		try {
			this.unit.setJSFolder((new File(".")).getAbsolutePath() + "/src/test/resources");
			this.unit.handle(exch);
			
			verify(exch).sendResponseHeaders(404, "Unsupported method.".length());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} catch (Exception e2) {
			e2.printStackTrace();
			fail("Default exception raised");			
		}
	}
	
	@Test
	public void testHandle_caseGET() {
		HttpExchange exch = mock(HttpExchange.class);
		HttpContext ctx = mock(HttpContext.class);
		
		when(exch.getRequestMethod()).thenReturn("GET");
		when(exch.getHttpContext()).thenReturn(ctx);
		when(exch.getResponseBody()).thenReturn(mock(OutputStream.class));
		when(ctx.getPath()).thenReturn("/json2Table");
		
		try {
			this.unit.setJSFolder((new File(".")).getAbsolutePath() + "/src/test/resources");
			this.unit.handle(exch);
			
			verify(exch).sendResponseHeaders(200, "GET OK".length());
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} catch (Exception e2) {
			e2.printStackTrace();
			fail("Default exception raised");			
		}
	}
	
	@Test
	public void testHandle_casePOST() {
		HttpExchange exch = mock(HttpExchange.class);
		HttpContext ctx = mock(HttpContext.class);
		
		when(exch.getRequestMethod()).thenReturn("POST");
		
		InputStream io = new ByteArrayInputStream("[{}]".getBytes());
		when(exch.getRequestBody()).thenReturn(io);
		when(exch.getHttpContext()).thenReturn(ctx);
		when(exch.getResponseBody()).thenReturn(mock(OutputStream.class));
		when(ctx.getPath()).thenReturn("/json2Table");
		
		try {
			this.unit.setJSFolder((new File(".")).getAbsolutePath() + "/src/test/resources");
			this.unit.handle(exch);
			
			verify(exch).sendResponseHeaders(200, "POST OK".length());
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} catch (Exception e2) {
			e2.printStackTrace();
			fail("Default exception raised");			
		}
	}
}
