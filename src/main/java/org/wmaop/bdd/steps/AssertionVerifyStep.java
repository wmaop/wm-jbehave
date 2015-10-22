package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;


public class AssertionVerifyStep extends BaseServiceStep {

	private IData idata;
	private String execService;
	private int invokeCount;
	private String assertionId;

	public AssertionVerifyStep(String assertionId, int invokeCount) {
		idata = IDataFactory.create();
		this.invokeCount = invokeCount;
		this.assertionId = assertionId;
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, assertionId);
		cursor.destroy();
		execService = ASSERTION_INVOKE_COUNT;
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		IData pipeline = invokeService(executionContext, execService, idata); // Dont require IData
		IDataCursor cursor = pipeline.getCursor();
		int actual = IDataUtil.getInt(cursor, "invokeCount", 0);
		assertEquals("Expected " + assertionId + " to be called " + invokeCount + " times but was called " + actual + " times", invokeCount, actual);
	}

}
