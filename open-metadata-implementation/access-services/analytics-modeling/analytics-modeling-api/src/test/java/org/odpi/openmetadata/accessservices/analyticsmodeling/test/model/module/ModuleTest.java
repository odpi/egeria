/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.DataSource;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.MetadataModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ModuleTest {

	private static final String IDENTIFIER = "IDENTIFIER";
	private static final String VERSION = "9.0";

	String master = String.format("{%n" +
			"  \"identifier\" : \"IDENTIFIER\",%n" +
			"  \"version\" : \"9.0\",%n" +
			"  \"dataSource\" : [ { }, { } ]%n" +
			"}");

	String master_empty = String.format("{%n" +
			"  \"version\" : \"9.0\"%n" +
			"}");

	List<DataSource> sources = Arrays.asList(new DataSource(), new DataSource());

	@Test
	public void toJson() {
		MetadataModule obj = new MetadataModule();

		obj.setIdentifier(IDENTIFIER);
		obj.setDataSource(sources);
		// version set fixed

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		TestUtilities.assertObjectJson(new MetadataModule(), master_empty);
	}

	@Test
	public void fromJson() {

		MetadataModule obj = TestUtilities.readObjectJson(master, MetadataModule.class);

		assertEquals(IDENTIFIER, obj.getIdentifier());
		assertEquals(VERSION, obj.getVersion());
		assertEquals(2, obj.getDataSource().size());

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void completeModule() throws IOException {

		String module = TestUtilities.readJsonFile("/src/test/resources/", "completeModule");

		MetadataModule obj = TestUtilities.readObjectJson(module, MetadataModule.class);

		assertEquals("AdventureWorks2014.Person", obj.getIdentifier());
		assertEquals(VERSION, obj.getVersion());
		assertEquals(1, obj.getDataSource().size());

		TestUtilities.assertObjectJson(obj, module);
	}

}
