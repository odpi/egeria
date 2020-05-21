/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;


import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.ForeignColumn;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.ForeignKey;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ForeignKeyTest {

	private static final String FK_NAME = "FK_NAME";

	String master = String.format("{%n" +
			"  \"name\" : \"FK_NAME\",%n" +
			"  \"fkColumn\" : [ {%n" +
			"    \"columnName\" : \"A\"%n" +
			"  }, {%n" +
			"    \"columnName\" : \"B\"%n" +
			"  } ]%n" +
			"}");

	String master_empty = "{ }";

	List<ForeignColumn> columns = Arrays.asList(newNamedFK("A"), newNamedFK("B"));

	/**
	 * Helper function to create foreign key column
	 * @param name of the foreign key columns
	 * @return created new foreign key column
	 */
	ForeignColumn newNamedFK (String name) {
		ForeignColumn ret = new ForeignColumn();
		ret.setColumnName(name);
		return ret;
	}

	@Test
	public void toJson() {
		ForeignKey obj = new ForeignKey();

		obj.setName(FK_NAME);
		obj.setFkColumn(columns);

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		TestUtilities.assertObjectJson(new ForeignColumn(), master_empty);
	}

	@Test
	public void fromJson() {

		ForeignKey obj = TestUtilities.readObjectJson(master, ForeignKey.class);

		assertEquals(FK_NAME, obj.getName());
		assertEquals(2, obj.getFkColumn().size());
		assertEquals("A", obj.getFkColumn().get(0).getColumnName());

		TestUtilities.assertObjectJson(obj, master);
	}

}
