/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.test.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.cognos.model.module.DataSource;
import org.odpi.openmetadata.accessservices.cognos.model.module.Module;
import org.odpi.openmetadata.accessservices.cognos.test.utils.TestUtilities;

public class ModuleTest {

	private static final String IDENTIFIER = "IDENTIFIER";
	private static final String VERSION = "9.0";

	String master = "{\r\n" +
			"  \"identifier\" : \"IDENTIFIER\",\r\n" +
			"  \"version\" : \"9.0\",\r\n" +
			"  \"dataSource\" : [ { }, { } ]\r\n" +
			"}";

	String master_empty = "{\r\n" +
			"  \"version\" : \"9.0\"\r\n" +
			"}";

	List<DataSource> sources = Arrays.asList(new DataSource(), new DataSource());

	@Test
	public void toJson() {
		Module obj = new Module();

		obj.setIdentifier(IDENTIFIER);
		obj.setDataSource(sources);
		// version set fixed

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		TestUtilities.assertObjectJson(new Module(), master_empty);
	}

	@Test
	public void fromJson() {

		Module obj = TestUtilities.readObjectJson(master, Module.class);

		assertEquals(IDENTIFIER, obj.getIdentifier());
		assertEquals(VERSION, obj.getVersion());
		assertEquals(2, obj.getDataSource().size());

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void completeModule() throws IOException {

		String module = TestUtilities.readJsonFile("/src/test/resources/", "completeModule");

		Module obj = TestUtilities.readObjectJson(module, Module.class);

		assertEquals("AdventureWorks2014.Person", obj.getIdentifier());
		assertEquals(VERSION, obj.getVersion());
		assertEquals(1, obj.getDataSource().size());

		TestUtilities.assertObjectJson(obj, module);
	}

}
