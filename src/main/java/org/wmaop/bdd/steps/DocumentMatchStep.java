package org.wmaop.bdd.steps;

import org.wmaop.util.jexl.ExpressionProcessor;
import org.wmaop.util.jexl.IDataJexlContext;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class DocumentMatchStep extends BaseServiceStep {

	private final String documentReference;
	private final String idataFile;
	private final String documentName;

	public DocumentMatchStep(String documentName, String idataFile) {
		this.documentName = documentName;
		this.documentReference = ExpressionProcessor.escapedToEncoded(documentName);
		this.idataFile = idataFile;
	}

	@Override
	protected void execute(ExecutionContext executionContext) throws Exception {
		IData potential = idataFile == null ? IDataFactory.create() : getDocumentContents();
		IDataJexlContext docRef = (IDataJexlContext) new IDataJexlContext(executionContext.getPipeline())
				.get(documentReference);
		if (docRef == null) {
			fail("Failed when trying to matching document.  Cannot find document '" + documentName
					+ "' in the pipeline");
		}
		IData doc = docRef.toIData();
		if (!matches(doc, potential, documentName, true)) {
			fail("Failed to match " + documentReference + " in pipeline with file " + idataFile);
		}
	}

	private IData getDocumentContents() throws Exception {
		IData idreturn;
		IData idata = idataFromClasspathResource(idataFile);
		IDataCursor idc = idata.getCursor();
		if (IDataUtil.size(idc) == 1) {
			IData id = IDataUtil.getIData(idc, documentName);
			if (id != null) {
				idreturn = id;
			} else {
				idreturn = idata;
			}
		} else {
			idreturn = idata;
		}
		idc.destroy();
		return idreturn;
	}

	boolean matches(IData document, IData potential, String prefix, boolean reportFail) {
		IDataCursor pc = potential.getCursor();
		IDataCursor dc = document.getCursor();
		while (pc.next()) {
			String key = pc.getKey();
			Object docObj = IDataUtil.get(dc, key);
			Object potObj = pc.getValue();
			if (docObj instanceof IData && potObj instanceof IData) {
				if (!isIDataMatch(prefix, reportFail, key, docObj, potObj)) {
					return false;
				}
			} else if (docObj instanceof IData[]) {
				if (!isIDataArrayMatch(prefix, key, docObj, potObj)) {
					return false;
				}
			} else {
				if (!isObjectMatch(prefix, reportFail, key, docObj, potObj)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isObjectMatch(String prefix, boolean reportFail, String key, Object docObj, Object potObj) {
		if (docObj == null && potObj == null) {
			return true;
		}
		if (docObj == null || potObj == null) {
			if (reportFail) {
				fail("Failed to locate element in pipeline: " + prefix + '.' + key);
			}
			return false;
		}
		Object docVal;
		if (docObj instanceof IData) {
			IDataCursor cursor = ((IData) docObj).getCursor();
			docVal = IDataUtil.get(cursor, "*body");
			cursor.destroy();
		} else {
			docVal = docObj;
		}
		// Bug fix: Accomodate if strings have different line breaks (\n\r and \n)
		if(docVal instanceof String[][] && potObj instanceof String[][]){
			for(String[] a: (String[][]) potObj){
				for(String b: a){
					b = b.replaceAll("\r", "");
				}
			}
			for(String[] a: (String[][]) docVal){
				for(String b: a){
					b = b.replaceAll("\r", "");
				}
			}
		}
		else if(docVal instanceof String[] && potObj instanceof String[]){
			for(String a: (String[]) potObj){
				a = a.replaceAll("\r", "");
			}
			for(String a: (String[]) docVal){
				a = a.replaceAll("\r","");
			}
		}
		else if(docVal instanceof String && potObj instanceof String){
			potObj = (Object) ((String) potObj).replaceAll("\r", "");
			docVal = (Object) ((String) docVal).replaceAll("\r", "");
		}
		//End bug fix
		//Another bug fix: arrays will only be .equals() if they are the same object
		//Instead, use Arrays.equals to evaluate equality of contents of two arrays 
		if ((docVal instanceof String[][] && potObj instanceof String[][])){
			if (!Arrays.equals((String[][])docVal,(String[][])potObj)) {
				if (reportFail) {
					fail("Element " + prefix + '.' + key + " has pipeline value with different array values than test value");
				}
				return false;
			}
		}
		else if ((docVal instanceof String[] && potObj instanceof String[])){
			if (!Arrays.equals((String[])docVal,(String[])potObj)) {
				if (reportFail) {
					fail("Element " + prefix + '.' + key + " has pipeline value with different array values than test value");
				}
				return false;
			}
		}
		else{
			if (!docVal.equals(potObj)) {
				if (reportFail) {
					fail("Element " + prefix + '.' + key + " has pipeline value of '" + docObj + "' but test value of '"
							+ potObj + "'");
				}
				return false;
			}
		}
		return true;
	}

	private boolean isIDataArrayMatch(String prefix, String key, Object docObj, Object potObj) {
		IData[] potArr = (potObj instanceof IData[]) ? ((IData[]) potObj) : new IData[] { (IData) potObj };
		for (IData pot : potArr) {
			boolean potMatch = false;
			for (IData doc : (IData[]) docObj) {
				if ((doc == null && pot == null) || (pot != null && matches(doc, pot, prefix + '.' + key, false))) {
					potMatch = true;
					break;
				}
			}
			if (!potMatch) {
				fail("Failed to match " + prefix + '.' + key);
				return false;
			}
		}
		return true;
	}

	private boolean isIDataMatch(String prefix, boolean reportFail, String key, Object docObj, Object potObj) {
		if (!matches((IData) docObj, (IData) potObj, prefix + '.' + key, reportFail)) {
			if (reportFail) {
				fail("Failed to match values for " + prefix + '.' + key);
			}
			return false;
		}
		return true;
	}
}
