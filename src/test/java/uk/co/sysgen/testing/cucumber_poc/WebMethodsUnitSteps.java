package uk.co.sysgen.testing.cucumber_poc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Properties;

import com.wm.data.IData;
import com.wm.util.coder.IDataXMLCoder;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class WebMethodsUnitSteps {

	private static final String EMPTY_IDATA = "<IDataXMLCoder version=\"1.0\"></IDataXMLCoder>";
	private String username;
	private String password;
	private String serverUrl = "http://localhost:5555";
	private IData pipeline;
	
	
	@Before
	public void init() throws IOException {
		Properties p = new Properties();
		p.load(new FileReader(new File("feature/cucumber.properties")));		
	}
	
	@When("^invoke (.+) with (.+)$")
	public void invoke_service(final String serviceName, String idataFile) throws MalformedURLException, IOException {
		System.out.println("Invoking " +serviceName + " with idata " +idataFile);
		//  use service invoke utility
		InputStream dataStream = this.getClass().getClassLoader().getResourceAsStream(idataFile);
		pipeline = invokeService(serverUrl + '/' + serviceName, dataStream);
		dataStream.close();
	}
	

	@Given("^invoke '(.+)'$")
	public void invoke_service(final String serviceName) throws IOException {
		System.out.println("Invoking " +serviceName);
		pipeline = invokeService(serverUrl + '/' + serviceName);
	}

	@Given("mock (.+) always returning (.+)")
	public void x(String serviceName, String idataFile) {
		System.out.println("Creating mock for " + serviceName
				+ " with canned data " + idataFile);
	}

	@Given("^mock (.+) returning (.+) when (.+)$")
	public void mock_service_returning_when(String serviceName, String idataFile, String expression)
			throws Throwable {
		System.out.println(serviceName + " called with "+idataFile+" when " + expression);
	}
	
	

	@Then("^service (.+) was invoked (.+) times$")
	public void service_was_invoked_times(String serviceName, String invokeCount) throws Throwable {
		System.out.println("verifying service" +serviceName+" was invoked "+invokeCount );
		// Register assertion
	}

	@Then("^pipeline has (.+)$")
	public void pipeline_has_foo_data(String arg1) throws Throwable {
		System.out.println("verifying jexl expression on idata " + arg1);
	}
	
	private IData invokeService(String svcurl, InputStream content) throws IOException {
		URL url = new URL(svcurl);
		HttpURLConnection uc = getConnection(url);
		writeStreamToConnection(content, uc);
		uc.connect();
		InputStream is = uc.getInputStream();
		IData pl = new IDataXMLCoder().decode(is);
		is.close();
		return pl;
	}
	
	private IData invokeService(String svcurl) throws IOException {
		return invokeService(svcurl, new ByteArrayInputStream(EMPTY_IDATA.getBytes()));
	}


	private HttpURLConnection getConnection(URL url) throws IOException,
			ProtocolException {
		String userPassword = username + ":" + password;
		String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		uc.setRequestMethod("POST");
		uc.setDoInput(true);
		uc.setDoOutput(true);
		uc.setRequestProperty("ContentType", "application/xml");
		uc.setRequestProperty("Authorization", "Basic " + encoding);
		uc.setRequestProperty("Accept", "text/xml");
		return uc;
	}


	private void writeStreamToConnection(InputStream content,
			HttpURLConnection uc) throws IOException {
		OutputStream out = uc.getOutputStream();
		byte[] buffer = new byte[4096];
		int n;
		while ((n = content.read(buffer)) > 0) {
		    out.write(buffer, 0, n);
		}
		out.close();
	}
}
