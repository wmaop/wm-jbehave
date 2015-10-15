package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.wmaop.bdd.steps.BaseServiceStep.ServiceSplit;

public class BaseServiceStepTest {

	@Test
	public void shouldLoadIdata() throws IOException {
		BaseServiceStep bst = new BaseServiceStep() {
			@Override
			void execute(ExecutionContext executionContext) throws Exception {
			}
		};
		InputStream is = bst.streamFromClasspathResource("data/applepear.xml");
		assertTrue(is.available() > 0);
	}

	@Test
	public void shouldParseServiceName() {
		BaseServiceStep bst = new BaseServiceStep() {
			@Override
			void execute(ExecutionContext executionContext) throws Exception {
			}
		};
		ServiceSplit svn = bst.splitQualifiedServiceName("foo");
		assertNull(svn.packageName);
		assertEquals("foo", svn.serviceName);
		
		svn = bst.splitQualifiedServiceName("foo:bar");
		assertEquals("foo", svn.packageName);
		assertEquals("bar", svn.serviceName);
	}
}
