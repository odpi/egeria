/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.ForeignColumn;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ForeignColumnTest {

	private static final String COLUMN_NAME = "COLUMN_NAME";
	private static final String PK_CATALOG = "PK_CATALOG";
	private static final String PK_COLUMN = "PK_COLUMN";
	private static final String PK_SCHEMA = "PK_SCHEMA";
	private static final String PK_TABLE = "PK_TABLE";

	String master = String.format("{%n" +
			"  \"pkColumn\" : \"PK_COLUMN\",%n" +
			"  \"pkCatalog\" : \"PK_CATALOG\",%n" +
			"  \"pkSchema\" : \"PK_SCHEMA\",%n" +
			"  \"pkTable\" : \"PK_TABLE\",%n" +
			"  \"columnName\" : \"COLUMN_NAME\"%n" +
			"}");

	String master_empty = "{ }";

	@Test
	public void toJson() {
		ForeignColumn obj = new ForeignColumn();

		obj.setColumnName(COLUMN_NAME);
		obj.setPkCatalog(PK_CATALOG);
		obj.setPkColumn(PK_COLUMN);
		obj.setPkSchema(PK_SCHEMA);
		obj.setPkTable(PK_TABLE);

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		TestUtilities.assertObjectJson(new ForeignColumn(), master_empty);
	}

	@Test
	public void fromJson() {

		ForeignColumn obj = TestUtilities.readObjectJson(master, ForeignColumn.class);

		assertEquals(COLUMN_NAME, obj.getColumnName());
		assertEquals(PK_CATALOG, obj.getPkCatalog());
		assertEquals(PK_COLUMN, obj.getPkColumn());
		assertEquals(PK_SCHEMA, obj.getPkSchema());
		assertEquals(PK_TABLE, obj.getPkTable());
	}

}
