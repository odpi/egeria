/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashMap;

import static org.testng.Assert.assertEquals;

public class ArrayPropertyValueTest {

    private InstancePropertyCategory category = InstancePropertyCategory.PRIMITIVE;
    private String typeGUID = "TestTypeGUID ";
    private String typeName = "TestTypeName";
    private ArrayPropertyValue arrayPropertyValue;

    @BeforeTest
    private void setObject() {
        ArrayPropertyValue arrayPropertyValue = new ArrayPropertyValue();
        arrayPropertyValue.setArrayCount(100);
        arrayPropertyValue.setTypeGUID(typeGUID);
        arrayPropertyValue.setTypeGUID(typeName);

        InstancePropertyValue propertyValueMock = new InstancePropertyValueMock();
        propertyValueMock.setTypeGUID(typeGUID);
        propertyValueMock.setTypeName(typeName);
        propertyValueMock.setInstancePropertyCategory(category);
        arrayPropertyValue.setArrayValue(0, propertyValueMock);

        InstancePropertyValue propertyValueMock1 = new InstancePropertyValueMock();
        propertyValueMock1.setTypeGUID(typeGUID);
        propertyValueMock1.setTypeName(typeName);
        propertyValueMock1.setInstancePropertyCategory(category);
        arrayPropertyValue.setArrayValue(17, propertyValueMock1);

        InstancePropertyValue propertyValueMock2 = new InstancePropertyValueMock();
        propertyValueMock2.setTypeGUID(typeGUID);
        propertyValueMock2.setTypeName(typeName);
        propertyValueMock2.setInstancePropertyCategory(category);
        arrayPropertyValue.setArrayValue(88, propertyValueMock2);

        this.arrayPropertyValue = arrayPropertyValue;
    }

    @Test
    public void testValueAsObject() {
        Object actual = arrayPropertyValue.valueAsObject();
        Object expected = new HashMap<String, Object>() {{
            put("0", "PropertyValue");
            put("17", "PropertyValue");
            put("88", "PropertyValue");
        }};
        assertEquals(actual, expected);
    }

    @Test
    public void testValueAsString() {
        String actual = arrayPropertyValue.valueAsString();
        String expected = "{0=PropertyValue, 88=PropertyValue, 17=PropertyValue}";

        assertEquals(actual, expected);
    }
}
