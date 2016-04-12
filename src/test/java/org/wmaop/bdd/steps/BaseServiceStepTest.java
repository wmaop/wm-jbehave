package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.wmaop.bdd.steps.BaseServiceStep.ServiceSplit;

public class BaseServiceStepTest {

	private BaseServiceStep bst = new BaseServiceStep() {
		@Override
		void execute(ExecutionContext executionContext) throws Exception {
		}
	};

	@Test
	public void shouldLoadIdata() throws IOException {
		InputStream is = bst.streamFromClasspathResource("data/applepear.xml");
		assertTrue(is.available() > 0);
	}

	@Test
	public void shouldParseServiceName() {

		ServiceSplit svn = bst.splitQualifiedServiceName("foo");
		assertNull(svn.packageName);
		assertEquals("foo", svn.serviceName);

		svn = bst.splitQualifiedServiceName("foo:bar");
		assertEquals("foo", svn.packageName);
		assertEquals("bar", svn.serviceName);
	}

	@Test
	public void shouldParseInterceptPoint() {
		assertEquals(InterceptPoint.before, bst.toInteceptPoint("before"));
		assertEquals(InterceptPoint.invoke, bst.toInteceptPoint("invoke"));
		assertEquals(InterceptPoint.after, bst.toInteceptPoint("after"));
		assertEquals(InterceptPoint.before, bst.toInteceptPoint("BEFORE"));
		assertEquals(InterceptPoint.invoke, bst.toInteceptPoint("INVOKE"));
		assertEquals(InterceptPoint.after, bst.toInteceptPoint("AFTER"));
		
		try {
			bst.toInteceptPoint("foo");
			fail();
		} catch (AssertionError ae) {
			// noop
		}
	}
}
