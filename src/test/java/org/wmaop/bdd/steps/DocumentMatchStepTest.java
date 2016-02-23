package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import com.wm.data.IData;
import com.wm.util.coder.IDataXMLCoder;

public class DocumentMatchStepTest {

	IData getIdataFile(String fileName) throws Exception {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
		return new IDataXMLCoder().decode(is);
	}

	@Test
	public void shouldMatch() throws Exception {
		IData complex = getIdataFile("data/complex.xml");
		DocumentMatchStep step = new DocumentMatchStep("producer", "data/complexsnippet.xml");
		ExecutionContext executionContext = new ExecutionContext();
		executionContext.setPipeline(complex);
		step.execute(executionContext);
	}

	@Test
	public void shouldMatchArrayElements() throws Exception {
		IData complex = getIdataFile("data/complex.xml");
		DocumentMatchStep step = new DocumentMatchStep("producer", "data/complexarraysnippet.xml");
		ExecutionContext executionContext = new ExecutionContext();
		executionContext.setPipeline(complex);
		step.execute(executionContext);
	}

	@Test
	public void shouldTrapIncorrectElement() throws Exception {
		IData complex = getIdataFile("data/complex.xml");
		DocumentMatchStep step = new DocumentMatchStep("producer", "data/complexsnippet-element.xml");
		ExecutionContext executionContext = new ExecutionContext();
		executionContext.setPipeline(complex);
		try {
			step.execute(executionContext);
			fail();
		} catch (AssertionError e) {
			assertEquals("Failed to locate element in pipeline: producer.serviceNotToExecute", e.getMessage());
		}
	}

	@Test
	public void shouldTrapIncorrectValue() throws Exception {
		IData complex = getIdataFile("data/complex.xml");
		DocumentMatchStep step = new DocumentMatchStep("producer", "data/complexsnippet-value.xml");
		ExecutionContext executionContext = new ExecutionContext();
		executionContext.setPipeline(complex);
		try {
			step.execute(executionContext);
			fail();
		} catch (AssertionError e) {
			assertEquals("Failed to locate element in pipeline: producer.serviceToExecute", e.getMessage());
		}
	}
}
