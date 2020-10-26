/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.BaseObjectType;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class BaseObjectTypeTest {

	String master = JsonMocks.getBaseObjectType();

	@Test
	public void toJson() {
		BaseObjectType obj = new BaseObjectType();

		obj.addProperty(JsonMocks.PROPERTY_NAME,JsonMocks.PROPERTY_VALUE);
		obj.addProperty(JsonMocks.PROPERTY_NAME2,JsonMocks.PROPERTY_VALUE2);
		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		BaseObjectType obj = new BaseObjectType();

		TestUtilities.assertObjectJson(obj, JsonMocks.getEmptyJson());
	}

	@Test
	public void fromJson() {

		BaseObjectType obj = TestUtilities.readObjectJson(master, BaseObjectType.class);

		assertEquals(obj.getProperty().size(), 2, "Wrong property collection size");
		assertEquals(obj.getPropertyByName(JsonMocks.PROPERTY_NAME).getValue(), JsonMocks.PROPERTY_VALUE, "Wrong property value");
		assertEquals(obj.getPropertyByName(JsonMocks.PROPERTY_NAME2).getValue(), JsonMocks.PROPERTY_VALUE2, "Wrong property2 value");
	}

}
