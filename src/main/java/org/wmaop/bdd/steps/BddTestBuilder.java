package org.wmaop.bdd.steps;

public class BddTestBuilder {

	private ExecutionContext executionContext;

	public BddTestBuilder(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}
	
	public void withInvokeService(String serviceName, String idataClasspathFile) throws Exception {
		InvokeServiceStep step = new InvokeServiceStep(serviceName, idataClasspathFile);
		step.execute(executionContext);
	}

	public void withMockService(String serviceName, String idataClasspathFile) throws Exception {
		MockServiceStep step = new MockServiceStep(serviceName, idataClasspathFile);
		step.execute(executionContext);
	}

	public void withMockService(String serviceName, String idataFile, String jexlPipelineExpression) throws Exception {
		MockServiceStep step = new MockServiceStep(serviceName, idataFile, jexlPipelineExpression);
		step.execute(executionContext);
	}

}
