package uk.co.sysgen.webmethods.testing.steps;

import java.util.ArrayList;
import java.util.List;

public class BddTestBuilder {

	private List<BaseServiceStep> steps = new ArrayList<>();
	private ExecutionContext context;

	public BddTestBuilder(ExecutionContext executionContext) {
		this.context = executionContext;
	}
	
	public InvokeServiceStep withInvokeService(String serviceName, String idataClasspathFile) {
		InvokeServiceStep step = new InvokeServiceStep(serviceName, idataClasspathFile);
		steps.add(step);
		return step;
	}

	public MockServiceStep wirhMockService(String serviceName, String idataClasspathFile) {
		MockServiceStep step = new MockServiceStep(serviceName, idataClasspathFile);
		steps.add(step);
		return step;
	}

	public MockServiceStep wirhMockService(String serviceName, String idataFile, String jexlPipelineExpression) {
		MockServiceStep step = new MockServiceStep(serviceName, idataFile, jexlPipelineExpression);
		steps.add(step);
		return step;
	}

}
