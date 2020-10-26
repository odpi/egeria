/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.module;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PropertyType;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;

public class PropertyTypeTest {

	String master = JsonMocks.getProperty(JsonMocks.PROPERTY_NAME,JsonMocks.PROPERTY_VALUE);

	@Test
	public void toJson() {
		PropertyType obj = new PropertyType();

		obj.setName(JsonMocks.PROPERTY_NAME);
		obj.setValue(JsonMocks.PROPERTY_VALUE);

		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		PropertyType obj = new PropertyType();

		TestUtilities.assertObjectJson(obj, JsonMocks.getEmptyJson());
	}

	@Test
	public void fromJson() {

		PropertyType obj = TestUtilities.readObjectJson(master, PropertyType.class);

		assertEquals(JsonMocks.PROPERTY_NAME, obj.getName());
		assertEquals(JsonMocks.PROPERTY_VALUE, obj.getValue());
	}

}
