package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseServiceStepTest {

	@Test
	public void test() throws IOException {
		BaseServiceStep bst = new BaseServiceStep(){
			@Override
			void execute(ExecutionContext executionContext) throws Exception {
			}};
		InputStream is = bst.streamFromClasspathResource("data/applepear.xml");
		assertTrue(is.available() > 0);
	}

}
