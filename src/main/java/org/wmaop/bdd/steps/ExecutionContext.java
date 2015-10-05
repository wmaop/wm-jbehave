package org.wmaop.bdd.steps;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;

public class ExecutionContext {

	Properties p = new Properties();
	Context context;
	IData pipeline;
	
	public ExecutionContext(String contextFile) {
		try {
			p.load(new FileReader(new File(
					contextFile)));
			context = createConnectionContext();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Context getConnectionContext() {
		return context;
	}
	
	private Context createConnectionContext() throws ServiceException {
		String host = p.getProperty("wm.server.host", "localhost");
		int port = Integer.valueOf(p.getProperty("wm.server.port", "5555"));
		String username = p.getProperty("wm.server.username", "Administrator");
		String password = p.getProperty("wm.server.password", "manage");
		boolean secure = Boolean.valueOf(p.getProperty("wm.server.secure", "false"));
		return connectToServer(host, port, username, password, secure);
	}

	protected Context connectToServer(String host, int port, String username,
			String password, boolean secure) throws ServiceException {
		Context ctx = new Context();
		// TODO figure out how https connections would work?!?!?!?!
		if (secure) {
			throw new UnsupportedOperationException();
		} else {
			ctx.connect(host, port, username, password);
		}
		return ctx;
	}

	public IData getPipeline() {
		return pipeline;
	}

	public void setPipeline(IData pipeline) {
		this.pipeline = pipeline;
	}

}
