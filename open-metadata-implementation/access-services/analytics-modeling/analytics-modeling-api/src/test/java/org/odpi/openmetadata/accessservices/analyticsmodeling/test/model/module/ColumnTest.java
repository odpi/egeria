/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Column;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class ColumnTest {

	private static final String DATA_TYPE = "DATA_TYPE";
	private static final String VENDOR_TYPE = "VENDOR_TYPE";
	private static final String COLUMN_NAME = "COLUMN_NAME";

	String master = String.format("{%n" +
			"  \"name\" : \"COLUMN_NAME\",%n" +
			"  \"vendorType\" : \"VENDOR_TYPE\",%n" +
			"  \"nullable\" : true,%n" +
			"  \"datatype\" : \"DATA_TYPE\"%n" +
			"}");

	String master_empty = "{ }";

	@Test
	public void toJson() {
		Column obj = new Column();

		obj.setName(COLUMN_NAME);
		obj.setNullable(true);
		obj.setVendorType(VENDOR_TYPE);
		obj.setDatatype(DATA_TYPE);

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		Column obj = new Column();

		TestUtilities.assertObjectJson(obj, master_empty);
	}

	@Test
	public void fromJson() {

		Column obj = TestUtilities.readObjectJson(master, Column.class);

		assertEquals(COLUMN_NAME, obj.getName());
		assertEquals(Boolean.TRUE, obj.isNullable());
		assertEquals(VENDOR_TYPE, obj.getVendorType());
		assertEquals(DATA_TYPE, obj.getDatatype());
	}

}
