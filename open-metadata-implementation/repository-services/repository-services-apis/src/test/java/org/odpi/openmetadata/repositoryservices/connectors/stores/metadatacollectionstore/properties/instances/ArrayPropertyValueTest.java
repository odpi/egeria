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
        arrayPropertyValue.setArrayCount(1);
        arrayPropertyValue.setTypeGUID(typeGUID);
        arrayPropertyValue.setTypeGUID(typeName);

        InstancePropertyValue propertyValueMock = new InstancePropertyValueMock();
        propertyValueMock.setTypeGUID(typeGUID);
        propertyValueMock.setTypeName(typeName);
        propertyValueMock.setInstancePropertyCategory(category);
        arrayPropertyValue.setArrayValue(0, propertyValueMock);
        this.arrayPropertyValue = arrayPropertyValue;
    }

    @Test
    public void testValueAsObject() {
        ArrayPropertyValue arrayPropertyValue = new ArrayPropertyValue(this.arrayPropertyValue);
        arrayPropertyValue.setArrayCount(2);

        InstancePropertyValue propertyValueMock = new InstancePropertyValueMock();
        propertyValueMock.setTypeGUID(typeGUID);
        propertyValueMock.setTypeName(typeName);
        propertyValueMock.setInstancePropertyCategory(category);
        arrayPropertyValue.setArrayValue(1, propertyValueMock);

        Object actual = arrayPropertyValue.valueAsObject();
        Object expected = new HashMap<String, Object>() {{
            put("0", "PropertyValue");
            put("1", "PropertyValue");
        }}.values();
        assertEquals((Collection) actual, (Collection) expected);
    }

    @Test
    public void testValueAsString() {
        String actual = arrayPropertyValue.valueAsString();
        String expected = "[PropertyValue]";

        assertEquals(actual, expected);
    }
}
