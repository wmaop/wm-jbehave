package org.wmaop.bdd.steps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
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
		Properties props = loadProperties();

		String host = props.getProperty("wm.server.host", "localhost");
		int port = Integer.parseInt(props.getProperty("wm.server.port", "5555"));
		String username = props.getProperty("wm.server.username", "Administrator");
		String password = props.getProperty("wm.server.password", "manage");
		boolean secure = Boolean.parseBoolean(props.getProperty("wm.server.secure", "false"));

		return connectToServer(host, port, username, password, secure);
	}

	protected Properties loadProperties() {
		Properties system = System.getProperties();
		String jasyptPassword = system.getProperty("wmaopkey");

		Properties props;
		if (jasyptPassword == null) {
			logger.info("Property password environment variable 'wmaopkey' not found.  Properties will not be decrypted.");
			props = new Properties(system);
		} else {
			// Create Jasypt wrapped properties object
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword(jasyptPassword);
			props = new EncryptableProperties(system, encryptor);
		}

		// Attempt to load config.properties file
		String propertiesFilename = props.getProperty("wm.config.filename", "config.properties");
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream resourceStream = loader.getResourceAsStream(propertiesFilename)) {
			props.load(resourceStream);
		} catch (IOException e) {
			logger.error("Failed to read config.properties file: " + e.toString());
		} catch (Exception e) {
			logger.warn("Could not find " + propertiesFilename + " file. Using system properties and default values.");
		}

		return props;
	}

	protected Context connectToServer(String host, int port, String username, String password, boolean secure)
			throws ServiceException {
		Context ctx = new Context();
		if (secure) {
			throw new UnsupportedOperationException();
		} else {
			try {
				ctx.connect(host, port, username, password);
			} catch (Exception e) {
				Assert.fail("Unable to connect to " + host + ':' + port + " with user " + username + " - " + e.getMessage());
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
