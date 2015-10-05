package org.wmaop.bdd.steps;

import com.wm.data.IDataFactory;

public class TeardownStep extends BaseServiceStep {

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, "org.wmaop.reset:resetAdviceAndDisable", IDataFactory.create());
	}

}
