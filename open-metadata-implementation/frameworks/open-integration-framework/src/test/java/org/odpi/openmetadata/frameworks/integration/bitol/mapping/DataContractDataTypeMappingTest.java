/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractLogicalType;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractLogicalTypeOptions;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaMap;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


/**
 * Checks the Egeria data type that a data field imported from an ODCS schema property is given.
 */
public class DataContractDataTypeMappingTest
{
    private static DataContractSchemaProperty property(String logicalType, String physicalType)
    {
        DataContractSchemaProperty property = new DataContractSchemaProperty();

        property.setLogicalType(logicalType);
        property.setPhysicalType(physicalType);

        return property;
    }


    /**
     * Each logical type is mapped to the data type that holds its values.
     */
    @Test
    public void testLogicalTypes()
    {
        assertEquals(DataContractMapper.getDataType(property("string", null)), "string");
        assertEquals(DataContractMapper.getDataType(property("date", null)), "localdate");
        assertEquals(DataContractMapper.getDataType(property("timestamp", null)), "date");
        assertEquals(DataContractMapper.getDataType(property("time", null)), "string");
        assertEquals(DataContractMapper.getDataType(property("integer", "bigint")), "long", "the logical type is preferred");
        assertEquals(DataContractMapper.getDataType(property("number", null)), "bigdecimal");
        assertEquals(DataContractMapper.getDataType(property("boolean", null)), "boolean");
        assertEquals(DataContractMapper.getDataType(property("object", null)), "Other");
        assertEquals(DataContractMapper.getDataType(property("vector", null)), "Other");
        assertEquals(DataContractMapper.getDataType(property(" Integer ", null)), "long", "matching ignores case and spaces");
    }


    /**
     * A timestamp whose options say it has no time zone is a date and time without one.
     */
    @Test
    public void testTimestampWithoutTimeZone()
    {
        DataContractSchemaProperty     property = property("timestamp", null);
        DataContractLogicalTypeOptions options  = new DataContractLogicalTypeOptions();

        options.setTimezone(false);
        property.setLogicalTypeOptions(options);

        assertEquals(DataContractMapper.getDataType(property), "localdatetime");

        options.setTimezone(true);

        assertEquals(DataContractMapper.getDataType(property), "date");
    }


    /**
     * An array takes the type of its items, and a map the type of its values, where there is a data type for
     * that.
     */
    @Test
    public void testArraysAndMaps()
    {
        DataContractSchemaProperty strings = property("array", null);
        strings.setItems(property("string", null));
        assertEquals(DataContractMapper.getDataType(strings), "array<string>");

        DataContractSchemaProperty integers = property("array", null);
        integers.setItems(property("integer", null));
        assertEquals(DataContractMapper.getDataType(integers), "array<int>");

        DataContractSchemaProperty objects = property("array", null);
        objects.setItems(property("object", null));
        assertEquals(DataContractMapper.getDataType(objects), "Other");
        assertEquals(DataContractMapper.getDataType(property("array", null)), "Other", "an array of unknown items");

        assertEquals(DataContractMapper.getDataType(map(null, "number")), "map<string,double>");
        assertEquals(DataContractMapper.getDataType(map("string", "timestamp")), "map<string,date>");
        assertEquals(DataContractMapper.getDataType(map("string", "object")), "map<string,object>");
        assertEquals(DataContractMapper.getDataType(map("integer", "string")), "Other", "only maps with string keys have data types");
        assertEquals(DataContractMapper.getDataType(property("map", null)), "map<string,object>");

        DataContractSchemaProperty listValues = map("string", "array");
        listValues.getMap().getValue().setItems(property("string", null));
        assertEquals(DataContractMapper.getDataType(listValues), "map<string, array<string>>");
    }


    /**
     * Without a logical type, a physical type that names a data type or an ODCS logical type is used; anything
     * else has no data type.
     */
    @Test
    public void testPhysicalTypes()
    {
        assertEquals(DataContractMapper.getDataType(property(null, "localdatetime")), "localdatetime");
        assertEquals(DataContractMapper.getDataType(property(null, "date")), "date", "a data type's name is the data type");
        assertEquals(DataContractMapper.getDataType(property(null, "integer")), "long");
        assertNull(DataContractMapper.getDataType(property(null, "varchar(40)")));
        assertNull(DataContractMapper.getDataType(property(null, null)));
        assertNull(DataContractMapper.getDataType(property("not-a-type", null)));
    }


    /**
     * Every logical type is mapped to a data type - one of the valid values of dataType.
     */
    @Test
    public void testEveryLogicalTypeIsMapped()
    {
        for (DataContractLogicalType logicalType : DataContractLogicalType.values())
        {
            String dataType = DataContractMapper.getDataType(property(logicalType.getValue(), null));

            assertNotNull(dataType, logicalType + " should map to a data type");
            assertTrue(Arrays.stream(DataType.values()).anyMatch(candidate -> candidate.getDisplayName().equals(dataType)),
                       logicalType + " mapped to " + dataType);
        }
    }


    private static DataContractSchemaProperty map(String keyType, String valueType)
    {
        DataContractSchemaProperty property = property("map", null);
        DataContractSchemaMap      map      = new DataContractSchemaMap();

        if (keyType != null)
        {
            map.setKey(property(keyType, null));
        }

        map.setValue(property(valueType, null));
        property.setMap(map);

        return property;
    }
}
