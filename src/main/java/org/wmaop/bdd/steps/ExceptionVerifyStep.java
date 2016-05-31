package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

public class ExceptionVerifyStep extends BaseServiceStep {

	
	private final String expectedException;

	public ExceptionVerifyStep(String exception) {
		this.expectedException = exception;
	}
	
	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		Throwable e = executionContext.getThrownException();
		if (e == null) {
			fail("No exception found from service ");
		}
		if (e instanceof com.wm.app.b2b.client.ServiceException && ((com.wm.app.b2b.client.ServiceException)e).getErrorType() != null) { 
			assertEquals(expectedException, ((com.wm.app.b2b.client.ServiceException)e).getErrorType());
			executionContext.setThrownException(null);
		}
	}

}
