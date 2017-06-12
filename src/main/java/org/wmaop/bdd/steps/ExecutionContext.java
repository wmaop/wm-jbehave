package org.wmaop.bdd.steps;

import java.util.Properties;

import org.junit.Assert;

import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;
import com.wm.data.IDataFactory;

public class ExecutionContext {

	private static final Logger logger = Logger.getLogger(ExecutionContext.class);
	private Context context;
	private IData pipeline;
	private Throwable thrownException;
	
	public ExecutionContext() {
		pipeline = IDataFactory.create();
	}
	
	public Context getConnectionContext() throws ServiceException {
		if (context == null) {
			context = createConnectionContext();
		}
		return context;
	}
	
	private Context createConnectionContext() throws ServiceException {
		Properties p = System.getProperties();
		String host = p.getProperty("wm.server.host", "localhost");
		int port = Integer.parseInt(p.getProperty("wm.server.port", "5555"));
		String username = p.getProperty("wm.server.username", "Administrator");
		String password = p.getProperty("wm.server.password", null); // set to null if not defined
		if(password == null){
			// if we don't have a defined password, prompt for one from user
			Console console = System.console();
			try{
				password = new String(console.readPassword("WebMethods Server Password: "));
			} catch(IOException e){
				// if we had an error reading in user's password, use wm default pw
				logger.warn("Could not read password: " + e.toString() );
				logger.warn("Attempting to proceed with webMethods default password.");
				password = "manage";
			}
		}
		boolean secure = Boolean.parseBoolean(p.getProperty("wm.server.secure", "false"));
		return connectToServer(host, port, username, password, secure);
	}

	protected Context connectToServer(String host, int port, String username,
			String password, boolean secure) throws ServiceException {
		Context ctx = new Context();
		if (secure) {
			throw new UnsupportedOperationException();
		} else {
			try {
				ctx.connect(host, port, username, password);
			} catch (Exception e) {
				Assert.fail("Unable to connect to " + host+':'+port+ " with "+username+'/'+password + " - " + e.getMessage());
			}
		}
		return ctx;
	}

	public IData getPipeline() {
		return pipeline;
	}

	public void setPipeline(IData pipeline) {
		this.pipeline = pipeline;
	}

	public void setThrownException(Throwable e) {
		thrownException = e;
	}

	public Throwable getThrownException() {
		return thrownException;
	}
	
	public void terminate() {
		context.disconnect();
		context = null;
	}
}
