package org.wmaop.bdd.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.wm.data.IDataUtil;

public class PipelineVariableStepTest {

	@Test
	@Ignore
	public void shouldSetValue() throws Exception {
		ExecutionContext exc = new ExecutionContext();
		new PipelineVariableStep("foo = 'hello'").execute(exc);
		assertNotNull(exc.getPipeline());
		assertEquals("hello", IDataUtil.getString(exc.getPipeline().getCursor(), "foo"));
	}

}
