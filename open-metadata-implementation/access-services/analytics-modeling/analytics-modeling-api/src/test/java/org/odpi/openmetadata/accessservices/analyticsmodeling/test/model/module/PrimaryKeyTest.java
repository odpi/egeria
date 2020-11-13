/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;


import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PrimaryKey;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class PrimaryKeyTest {

	private static final String PK_NAME = "PK_NAME";

	String master = String.format("{%n" +
			"  \"name\" : \"PK_NAME\",%n" +
			"  \"keyedColumn\" : [ \"KEY_COLUMN_1\", \"KEY_COLUMN_2\" ]%n" +
			"}");

	List<String> columns = Arrays.asList("KEY_COLUMN_1", "KEY_COLUMN_2");

	@Test
	public void toJson() {
		PrimaryKey obj = new PrimaryKey();

		obj.setName(PK_NAME);
		obj.setKeyedColumn(columns);

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void fromJson() {

		PrimaryKey obj = TestUtilities.readObjectJson(master, PrimaryKey.class);

		assertEquals(PK_NAME, obj.getName());
		assertEquals(columns, obj.getKeyedColumn());
	}


}
