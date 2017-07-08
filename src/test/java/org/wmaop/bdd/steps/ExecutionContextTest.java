package org.wmaop.bdd.steps;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

public class ExecutionContextTest {
	
	@Test
	public void shouldLoadWithNoPropertyFile() {
		System.clearProperty("wm.config.filename");
		
		ExecutionContext ec = new ExecutionContext();
		Properties props = ec.loadProperties();
		assertEquals(null, props.get("wm.server.hostname"));
	}
	
	@Test
	public void shouldLoadWhenNoPropertyFilePresent() {
		System.setProperty("wm.config.filename", "foo");
		System.clearProperty("wmaopkey");

		ExecutionContext ec = new ExecutionContext();
		Properties props = ec.loadProperties();
		assertEquals(null, props.get("wm.server.hostname"));
	}

	@Test
	public void shouldLoadPropertiesWithoutEncryption() {
		System.setProperty("wm.config.filename", "junittest.properties");
		System.clearProperty("wmaopkey");

		ExecutionContext ec = new ExecutionContext();
		Properties props = ec.loadProperties();
		assertEquals("myserver", props.get("wm.server.hostname"));
		assertTrue(((String)props.get("wm.server.password")).startsWith("ENC("));
	}

	@Test
	public void shouldDecryptProperties() {
		System.setProperty("wm.config.filename", "junittest.properties");
		System.setProperty("wmaopkey", "4pPVtN3gCNHe");
		
		ExecutionContext ec = new ExecutionContext();
		Properties props = ec.loadProperties();
		assertEquals("EncodedPropertyPassword", props.get("wm.server.password"));
	}
	
}
