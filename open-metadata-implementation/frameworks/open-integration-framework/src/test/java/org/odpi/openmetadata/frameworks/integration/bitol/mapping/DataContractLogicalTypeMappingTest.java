/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractLogicalType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;


/**
 * Checks the ODCS logical type that a data field without a recorded logical type is exported with.
 */
public class DataContractLogicalTypeMappingTest
{
    /**
     * Each Egeria data type is exported as the logical type that holds its values.
     */
    @Test
    public void testEgeriaDataTypes()
    {
        assertEquals(DataContractGenerator.getLogicalType("string"), "string");
        assertEquals(DataContractGenerator.getLogicalType("char"), "string");
        assertEquals(DataContractGenerator.getLogicalType("int"), "integer");
        assertEquals(DataContractGenerator.getLogicalType("long"), "integer");
        assertEquals(DataContractGenerator.getLogicalType("biginteger"), "integer");
        assertEquals(DataContractGenerator.getLogicalType("bigdecimal"), "number");
        assertEquals(DataContractGenerator.getLogicalType("double"), "number");
        assertEquals(DataContractGenerator.getLogicalType("boolean"), "boolean");
        assertEquals(DataContractGenerator.getLogicalType("date"), "timestamp", "Egeria's date is a point in time");
        assertEquals(DataContractGenerator.getLogicalType("localdatetime"), "timestamp");
        assertEquals(DataContractGenerator.getLogicalType("localdate"), "date");
        assertEquals(DataContractGenerator.getLogicalType("array<string>"), "array");
        assertEquals(DataContractGenerator.getLogicalType("map<string,string>"), "map");
        assertEquals(DataContractGenerator.getLogicalType("map<string, array<string>>"), "map");
        assertEquals(DataContractGenerator.getLogicalType("Other"), "object");
        assertEquals(DataContractGenerator.getLogicalType(" LocalDate "), "date", "matching ignores case and spaces");
    }


    /**
     * Every Egeria data type has a logical type, and every one is a value ODCS defines.
     */
    @Test
    public void testEveryDataTypeIsMapped()
    {
        for (DataType dataType : DataType.values())
        {
            String logicalType = DataContractGenerator.getLogicalType(dataType.getDisplayName());

            assertNotNull(DataContractLogicalType.fromValue(logicalType), dataType + " should map to an ODCS logical type");
        }
    }


    /**
     * A value that is already an ODCS logical type is kept; anything else has no logical type.
     */
    @Test
    public void testOtherValues()
    {
        assertEquals(DataContractGenerator.getLogicalType("integer"), "integer");
        assertEquals(DataContractGenerator.getLogicalType("vector"), "vector");
        assertEquals(DataContractGenerator.getLogicalType("TIME"), "time");
        assertNull(DataContractGenerator.getLogicalType("varchar(40)"));
        assertNull(DataContractGenerator.getLogicalType(null));
    }
}
