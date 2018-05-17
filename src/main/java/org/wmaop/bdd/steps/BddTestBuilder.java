package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IDataFactory;
import com.wm.util.coder.IDataXMLCoder;

public class BddTestBuilder {

	private static final Logger logger = Logger.getLogger(BddTestBuilder.class);
	private static final String EOL = System.getProperty("line.separator");
	private static final String SEP = EOL + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-" + EOL;
	private final ExecutionContext executionContext;

	private int executedStep = 0;

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
		if (use instanceof ServiceException) {
			executionContext.setPipeline(((ServiceException)use).getErrorInfo().getValues("$pipeline"));
		} else {
			executionContext.setPipeline(IDataFactory.create()); // Pipeline not set from invoke so prevent NPE
		}
		executionContext.setThrownException(use);
		String msg = additionalMessage ==null?"":" - " + additionalMessage;
		logger.warn("Caught Exception while invoking [" + serviceName + "] this may not be the expected exception and could cause premature step failure.  Error is: " + use.getMessage() + msg);
	}

	protected void executeStep(BaseServiceStep step) {
		// Subtle difference in types of error handling to ensure reported correctly
		try {
			step.execute(executionContext);
			executedStep++;
		} catch (AssertionError e) {
			showPipeline();
			throw e;
		} catch (Error e) {
			logger.error(e);
			showPipeline();
			throw e;
		} catch (RuntimeException e) {
			logger.error(e);
			showPipeline();
			throw e;
		} catch (Throwable e) {
			logger.error(e);
			showPipeline();
			fail(e.getMessage());
		}
	}

	public void withMockService(String adviceId, InterceptPoint invoke, String serviceName, List<String> idataFile, String jexlPipelineExpression, String pkgService) {
		MockServiceStep step = new MockServiceStep(adviceId, invoke, serviceName, idataFile, jexlPipelineExpression, pkgService);
		executeStep(step);
	}

	public void withPipelineExpression(String jexlExpression) {
		PipelineJexlStep step = new PipelineJexlStep(jexlExpression);
		executeStep(step);
	}
	
	public int getExecutedStep() {
		return executedStep;
	}
	
	public boolean testConnection() throws ServiceException{
		return executionContext.testConnection();
	}

	public void terminate() {
		executionContext.terminate();
	}
	
	public void teardown() throws Exception {
		new TeardownStep().execute(executionContext);
		executionContext.setPipeline(IDataFactory.create());
		executionContext.setThrownException(null);
		executedStep = 0;
	}

	public void showPipeline() {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			new IDataXMLCoder().encode(baos, executionContext.getPipeline());
			StringBuilder sb = new StringBuilder("Pipeline contents:").append(SEP).append(baos).append(SEP);
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

	public void withException(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlPipelineExpression, String exception, String pkgService) {
		ExceptionStep step = new ExceptionStep(adviceId, interceptPoint, serviceName, jexlPipelineExpression, exception, pkgService);
		executeStep(step);
	}
	
	public void withException(String adviceId, String interceptPoint, String serviceName, String jexlPipelineExpression, String exception, String pkgService) {
		ExceptionStep step = new ExceptionStep(adviceId, interceptPoint, serviceName, jexlPipelineExpression, exception, pkgService);
		executeStep(step);
	}

	public void withExceptionVerify(String exceptionName) {
		ExceptionVerifyStep step = new ExceptionVerifyStep(exceptionName);
		executeStep(step);
	}

	public void verify() throws Throwable {
		if (executionContext.getThrownException() != null) {
			showPipeline();
			throw (Throwable) executionContext.getThrownException();
		}

	}

	public void withMatchesDocument(String document, String idataFile) {
		DocumentMatchStep step = new DocumentMatchStep(document, idataFile);
		executeStep(step);
	}

}
