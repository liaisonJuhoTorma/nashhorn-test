package test.com.liaison.nashorn;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.*;

import com.liaison.nashorn.Server;
import com.sun.net.httpserver.HttpServer;

public class ServerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testServer_caseMain() {
		HttpServer s = mock(HttpServer.class);
		
		Server.setServer(s);
		Server.main(null);
		
		verify(s).setExecutor(null);
		verify(s).start();
	}

}
