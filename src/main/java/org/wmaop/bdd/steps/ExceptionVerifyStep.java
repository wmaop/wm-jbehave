package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

public class ExceptionVerifyStep extends BaseServiceStep {

	
	private String exception;

	public ExceptionVerifyStep(String exception) {
		this.exception = exception;
	}
	
	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		Throwable e = executionContext.getThrownException();
		if (e == null) {
			throw new RuntimeException("No exception found from service ");
		}
		if (e instanceof com.wm.app.b2b.client.ServiceException && ((com.wm.app.b2b.client.ServiceException)e).getErrorType() != null) { 
			assertEquals(exception, ((com.wm.app.b2b.client.ServiceException)e).getErrorType());
			executionContext.setThrownException(null);
		}
	}

}
