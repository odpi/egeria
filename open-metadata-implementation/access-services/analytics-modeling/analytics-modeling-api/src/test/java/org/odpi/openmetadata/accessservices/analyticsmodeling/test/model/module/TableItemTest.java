/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Column;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.TableItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class TableItemTest {

	String master = String.format("{%n" +
			"  \"column\" : { }%n" +
			"}");

	String master_empty = "{ }";

	@Test
	public void toJson() {
		TableItem obj = new TableItem();
		obj.setColumn(new Column());

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		TableItem obj = new TableItem();

		TestUtilities.assertObjectJson(obj, master_empty);
	}

	@Test
	public void fromJson() {

		TableItem obj = TestUtilities.readObjectJson(master, TableItem.class);

		assertNotNull(obj.getColumn());
		TestUtilities.assertObjectJson(obj, master);
	}
}
