package org.wmaop.bdd.steps;

import static org.junit.Assert.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.wmaop.bdd.jbehave.InterceptPoint;

import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IDataFactory;
import com.wm.util.coder.IDataXMLCoder;

public class BddTestBuilder {

	final static Logger logger = Logger.getLogger(BddTestBuilder.class);
	private static final String EOL = System.getProperty("line.separator");
	private ExecutionContext executionContext;

	public BddTestBuilder(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public void withInvokeService(String serviceName, String idataClasspathFile) {
		try {
			InvokeServiceStep step = new InvokeServiceStep(serviceName, idataClasspathFile);
			step.execute(executionContext);
		} catch (ServiceException use) {
			if (use.getErrorType().contains("UnknownServiceException")) {
				fail("Unknown service [" + serviceName + ']');
			} else {
				logInvokeException(serviceName, use, use.getErrorType());
			}
		} catch (Exception e) {
			logInvokeException(serviceName, e, null);
		}
	}

	private void logInvokeException(String serviceName, Exception use, String additionalMessage) {
		executionContext.setThrownException(use);
		executionContext.setPipeline(IDataFactory.create()); // Pipeline not set from invoke so prevent NPE
		String msg = additionalMessage ==null?"":" - " + additionalMessage;
		logger.warn("Caught Exception while invoking [" + serviceName + "] this may not be the expected exception and could cause premature step failure.  Error is: " + use.getMessage() + msg);
	}

	protected void executeStep(BaseServiceStep step) {
		try {
			step.execute(executionContext);
		} catch (Throwable e) {
			if (!(e instanceof AssertionError)) {
				logger.error(e);
				showPipeline();
			}
			if (e instanceof RuntimeException){
				throw (RuntimeException)e;
			}
			if (e instanceof Error) {
				throw (Error)e;
			}
			fail(e.getMessage());
		}
	}

	public void withMockService(String adviceId, InterceptPoint invoke, String serviceName, String idataClasspathFile) {
		MockServiceStep step = new MockServiceStep(adviceId, invoke, serviceName, idataClasspathFile);
		executeStep(step);
	}

	public void withMockService(String adviceId, InterceptPoint invoke, String serviceName, String idataFile, String jexlPipelineExpression) {
		MockServiceStep step = new MockServiceStep(adviceId, invoke, serviceName, idataFile, jexlPipelineExpression);
		executeStep(step);
	}

	public void withPipelineExpression(String jexlExpression) {
		PipelineJexlStep step = new PipelineJexlStep(jexlExpression);
		executeStep(step);
	}

	public void teardown() throws Exception {
		new TeardownStep().execute(executionContext);
		executionContext.setPipeline(null);
		executionContext.setThrownException(null);
	}

	public void showPipeline() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Pipeline contents:").append(EOL);
			sb.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-").append(EOL);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			new IDataXMLCoder().encode(baos, executionContext.getPipeline());
			sb.append(baos).append(EOL);
			sb.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-").append(EOL);
			logger.info(sb);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void withAssertion(String assertionId, InterceptPoint interceptPoint, String serviceName, String jexlPipelineExpression) {
		AssertionSetupStep step = new AssertionSetupStep(assertionId, interceptPoint, serviceName, jexlPipelineExpression);
		executeStep(step);
	}

	public void withAssertion(String assertionId, String interceptPoint, String serviceName) {
		AssertionSetupStep step = new AssertionSetupStep(assertionId, interceptPoint, serviceName);
		executeStep(step);
	}

	public void withAssertionInvokeCount(String assertionId, int invokeCount) {
		AssertionVerifyStep step = new AssertionVerifyStep(assertionId, invokeCount);
		executeStep(step);
	}

	public void withVariableExpression(String jexlVariableExpression) {
		PipelineVariableStep step = new PipelineVariableStep(jexlVariableExpression);
		executeStep(step);
	}

	public void withException(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlPipelineExpression, String exception) {
		ExceptionStep step = new ExceptionStep(adviceId, interceptPoint, serviceName, null, exception);
		executeStep(step);
	}

	public void withExceptionVerify(String exceptionName) {
		ExceptionVerifyStep step = new ExceptionVerifyStep(exceptionName);
		executeStep(step);
	}

	public void verify() throws Exception {
		if (executionContext.getThrownException() != null) {
			showPipeline();
			throw (Exception) executionContext.getThrownException(); // Bad. Will blow with error
		}

	}

}
