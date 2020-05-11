/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.test.model.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import org.odpi.openmetadata.accessservices.cognos.model.module.Column;
import org.odpi.openmetadata.accessservices.cognos.test.utils.TestUtilities;

public class ColumnTest {

	private static final String DATA_TYPE = "DATA_TYPE";
	private static final String VENDOR_TYPE = "VENDOR_TYPE";
	private static final String COLUMN_NAME = "COLUMN_NAME";

	String master = "{\r\n" +
			"  \"name\" : \"COLUMN_NAME\",\r\n" +
			"  \"vendorType\" : \"VENDOR_TYPE\",\r\n" +
			"  \"nullable\" : true,\r\n" +
			"  \"datatype\" : \"DATA_TYPE\"\r\n" +
			"}";

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
		assertEquals(true, obj.isNullable());
		assertEquals(VENDOR_TYPE, obj.getVendorType());
		assertEquals(DATA_TYPE, obj.getDatatype());
	}

}
