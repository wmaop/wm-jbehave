package org.wmaop.bdd.steps;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.wm.data.IDataUtil;

public class PipelineVariableStepTest {

	@Test
	public void shouldSetValue() throws Exception {
		//ExecutionContext exc = mock(ExecutionContext.class);
		ExecutionContext exc = new ExecutionContext("src/test/resources/testserver.properties");
		new PipelineVariableStep("foo = 'hello'").execute(exc);
		assertNotNull(exc.getPipeline());
		assertEquals("hello", IDataUtil.getString(exc.getPipeline().getCursor(), "foo"));
	}

}
