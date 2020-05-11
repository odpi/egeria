/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.test.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.cognos.model.module.PrimaryKey;
import org.odpi.openmetadata.accessservices.cognos.test.utils.TestUtilities;

public class PrimaryKeyTest {

	private static final String PK_NAME = "PK_NAME";

	String master = "{\r\n" +
			"  \"name\" : \"PK_NAME\",\r\n" +
			"  \"keyedColumn\" : [ \"KEY_COLUMN_1\", \"KEY_COLUMN_2\" ]\r\n" +
			"}";

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
