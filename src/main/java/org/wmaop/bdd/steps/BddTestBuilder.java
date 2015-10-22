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
				executionContext.setThrownException(use);
				executionContext.setPipeline(IDataFactory.create()); // Pipeline not set from invoke so prevent NPE
				logger.info("Caught Exception while invoking [" + serviceName + "]  " + use.getMessage() + " - " + use.getErrorType());
			}
		} catch (Exception e) {
			executionContext.setThrownException(e);
			executionContext.setPipeline(IDataFactory.create()); // Pipeline not set from invoke so prevent NPE
			logger.info("Caught Exception while invoking [" + serviceName + "] " + e.getMessage());
		}
	}

	public void withMockService(String adviceId, InterceptPoint invoke,String serviceName, String idataClasspathFile) {
		try {
			MockServiceStep step = new MockServiceStep(adviceId, invoke, serviceName, idataClasspathFile);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void withMockService(String adviceId, InterceptPoint invoke,String serviceName, String idataFile, String jexlPipelineExpression) {
		try {
			MockServiceStep step = new MockServiceStep(adviceId, invoke, serviceName, idataFile, jexlPipelineExpression);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void withPipelineExpression(String jexlExpression) throws Exception {
		PipelineJexlStep step = new PipelineJexlStep(jexlExpression);
		step.execute(executionContext);
	}

	public void teardown() throws Exception {
		new TeardownStep().execute(executionContext);
		executionContext.setPipeline(null);
		executionContext.setThrownException(null);
	}

	public void showPipeline() {
		try {
			System.out.println("Pipeline contents:");
			System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			new IDataXMLCoder().encode(baos, executionContext.getPipeline());
			System.out.println(baos);
			System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void withAssertion(String assertionId, InterceptPoint interceptPoint, String serviceName,
			String jexlPipelineExpression) {
		try {
			AssertionSetupStep step = new AssertionSetupStep(assertionId, interceptPoint, serviceName, jexlPipelineExpression);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	public void withAssertion(String assertionId, String interceptPoint, String serviceName) {
		try {
			AssertionSetupStep step = new AssertionSetupStep(assertionId, interceptPoint, serviceName);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void withAssertionInvokeCount(String assertionId, int invokeCount) {
		try {
			AssertionVerifyStep step = new AssertionVerifyStep(assertionId, invokeCount);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void withVariableExpression(String jexlVariableExpression) {
		try {
			PipelineVariableStep step = new PipelineVariableStep(jexlVariableExpression);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void withException(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlPipelineExpression, String exception) {
		try {
			ExceptionStep step = new ExceptionStep(adviceId, interceptPoint, serviceName, null, exception);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void withExceptionVerify(String exceptionName) {
		try {
			ExceptionVerifyStep step = new ExceptionVerifyStep(exceptionName);
			step.execute(executionContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void verify() throws Exception {
		if (executionContext.getThrownException() != null) {
			throw (Exception) executionContext.getThrownException(); // Bad. Will blow with error
		}
		
	}

}
