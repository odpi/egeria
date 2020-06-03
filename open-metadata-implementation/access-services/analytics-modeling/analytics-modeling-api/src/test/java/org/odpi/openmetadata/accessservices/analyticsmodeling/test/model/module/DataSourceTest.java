/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.DataSource;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Table;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class DataSourceTest {

	private static final String DATASOURCE_NAME = "DATASOURCE_NAME";
	private static final String SCHEMA = "SCHEMA";
	private static final String CATALOG = "CATALOG";

	String master = String.format("{%n" +
			"  \"schema\" : \"SCHEMA\",%n" +
			"  \"catalog\" : \"CATALOG\",%n" +
			"  \"name\" : \"DATASOURCE_NAME\",%n" +
			"  \"table\" : [ { }, { } ]%n" +
			"}");

	String master_empty = "{ }";

	List<Table> tables = Arrays.asList(new Table(), new Table());

	@Test
	public void toJson() {
		DataSource obj = new DataSource();

		obj.setName(DATASOURCE_NAME);
		obj.setCatalog(CATALOG);
		obj.setSchema(SCHEMA);
		obj.setTable(tables);

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		TestUtilities.assertObjectJson(new DataSource(), master_empty);
	}

	@Test
	public void fromJson() {

		DataSource obj = TestUtilities.readObjectJson(master, DataSource.class);

		assertEquals(DATASOURCE_NAME, obj.getName());
		assertEquals(SCHEMA, obj.getSchema());
		assertEquals(CATALOG, obj.getCatalog());
		assertEquals(2, obj.getTable().size());

		TestUtilities.assertObjectJson(obj, master);
	}
}
