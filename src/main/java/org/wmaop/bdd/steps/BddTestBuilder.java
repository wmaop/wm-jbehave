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

	public void withMockService(String adviceId, String interceptPoint,String serviceName, String idataClasspathFile) throws Exception {
		MockServiceStep step = new MockServiceStep(adviceId, interceptPoint, serviceName, idataClasspathFile);
		step.execute(executionContext);
	}

	public void withMockService(String adviceId, String interceptPoint,String serviceName, String idataFile, String jexlPipelineExpression) throws Exception {
		MockServiceStep step = new MockServiceStep(adviceId, interceptPoint, serviceName, idataFile, jexlPipelineExpression);
		step.execute(executionContext);
	}

	public void withPipelineExpression(String jexlExpression) throws Exception {
		PipelineJexlStep step = new PipelineJexlStep(jexlExpression);
		step.execute(executionContext);
	}

}
