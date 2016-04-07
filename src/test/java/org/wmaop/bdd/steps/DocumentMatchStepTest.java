package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import com.wm.data.IData;
import com.wm.util.coder.IDataXMLCoder;

public class DocumentMatchStepTest {

	IData getIdataFile(String fileName) throws Exception {
		return new BaseServiceStep() {
			@Override
			void execute(ExecutionContext executionContext) throws Exception {
			}
		}.idataFromClasspathResource(fileName);
	}

	@Test
	public void shouldMatchIDataSnippet() throws Exception {
		match("data/complexsnippet.xml");
	}

	@Test
	public void shouldMatchIDataDocument() throws Exception {
		match("data/complexdocument.xml");
	}
	
	@Test
	public void shouldMatchXMLdocument() throws Exception {
		match("data/xmldocument.xml");
	}

	@Test
	public void shouldMatchArrayElements() throws Exception {
		match("data/complexarraysnippet.xml");
	}

	@Test
	public void shouldMatchSimple() throws Exception {
		match("data/simpleidata.xml", "data/simple.xml", "document");
	}

	@Test
	public void shouldMatchSimpleXmlSnippet() throws Exception {
		match("data/simpleidata.xml", "data/simplesnippet.xml", "document");
	}

	@Test
	public void shouldMatchSimpleIDataSnippet() throws Exception {
		match("data/simpleidata.xml", "data/simpleidatasnippet.xml", "document");
	}
	
	void match(String dataToMatch) throws Exception {
		match("data/complex.xml", dataToMatch, "producer");
	}
	
	void match(String source, String dataToMatch, String documentName) throws Exception {
		IData complex = getIdataFile(source);
		DocumentMatchStep step = new DocumentMatchStep(documentName, dataToMatch);
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
