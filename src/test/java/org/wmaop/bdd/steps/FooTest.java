package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.wm.app.b2b.services.RecordToDocumentService;
import com.wm.data.IData;
import com.wm.data.IDataUtil;
import com.wm.util.coder.IDataXMLCoder;
import com.wm.util.coder.InvalidDatatypeException;
import com.wm.util.Values;
import com.wm.util.coder.*;

public class FooTest {

	@Test
	public void test() throws Exception {
		IData snip = new IDataXMLCoder().decode(streamFromClasspathResource("data/complexsnippet.xml"));
		Values doc = new Values();
		IDataUtil.put(doc.getCursor(), "document", snip);
		System.out.println(RecordToDocumentService.getDocument(doc, false));
	}
	protected String stringFromClasspathResource(String fileName) {
		try {
			return IOUtils.toString(streamFromClasspathResource(fileName));
		} catch (Exception e) {
			throw new RuntimeException("Unable to load " + fileName + " from the classpath");
		}
	}
	protected InputStream streamFromClasspathResource(String fileName) {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
			if (is == null) {
				throw new RuntimeException("Unable to load file '" + fileName + "' from the classpath.");
			}
			return is;
		} catch (Exception e) {
			throw new RuntimeException("Unable to load file '" + fileName + "' from the classpath.  Cause is: " + e.getMessage());
		}
	}

}
