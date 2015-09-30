package uk.co.sysgen.webmethods.testing.steps;

import java.util.ArrayList;
import java.util.List;

public class BddTestBuilder {

	private List<BaseServiceStep> steps = new ArrayList<>();
	private ExecutionContext context;

	public BddTestBuilder(ExecutionContext executionContext) {
		this.context = executionContext;
	}
	
	public BddTestBuilder withInvokeService(String serviceName, String idataClasspathFile) {
		steps.add(new InvokeServiceStep(serviceName, idataClasspathFile));
		return this;
	}

	public MockServiceStep getMockServiceStep() {
		return new MockServiceStep(context);
	}

}
