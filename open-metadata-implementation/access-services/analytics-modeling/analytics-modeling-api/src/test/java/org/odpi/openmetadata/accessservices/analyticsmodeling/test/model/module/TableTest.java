/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.ForeignKey;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PrimaryKey;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Table;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.TableItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class TableTest {

	private static final String TABLE_NAME = "TABLE_NAME";

	String master = String.format("{%n" +
			"  \"name\" : \"TABLE_NAME\",%n" +
			"  \"tableItem\" : [ { }, { } ],%n" +
			"  \"foreignKey\" : [ { }, { } ],%n" +
			"  \"primaryKey\" : [ { }, { } ]%n" +
			"}");

	String master_empty = "{ }";

	List<TableItem> columns = Arrays.asList(new TableItem(), new TableItem());
	List<ForeignKey> foreignKey = Arrays.asList(new ForeignKey(), new ForeignKey());
	List<PrimaryKey> primaryKey = Arrays.asList(new PrimaryKey(), new PrimaryKey());

	@Test
	public void toJson() {
		Table obj = new Table();

		obj.setName(TABLE_NAME);
		obj.setTableItem(columns);
		obj.setForeignKey(foreignKey);
		obj.setPrimaryKey(primaryKey);

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		TestUtilities.assertObjectJson(new Table(), master_empty);
	}

	@Test
	public void fromJson() {

		Table obj = TestUtilities.readObjectJson(master, Table.class);

		assertEquals(TABLE_NAME, obj.getName());
		assertEquals(2, obj.getForeignKey().size());
		assertEquals(2, obj.getPrimaryKey().size());
		assertEquals(2, obj.getTableItem().size());

		TestUtilities.assertObjectJson(obj, master);
	}
}
