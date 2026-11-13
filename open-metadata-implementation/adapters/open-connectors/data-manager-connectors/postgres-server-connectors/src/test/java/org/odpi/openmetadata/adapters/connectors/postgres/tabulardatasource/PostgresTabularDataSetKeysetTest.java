/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;


/**
 * Checks the condition that starts a window of records after the last record of the window before, so that a
 * table is read through its primary key rather than by stepping through an ever larger offset.
 */
public class PostgresTabularDataSetKeysetTest
{
    private static TabularColumnDescription column(String name, boolean isIdentifier)
    {
        return new TabularColumnDescription(name, DataType.STRING, null, true, isIdentifier);
    }


    /**
     * A single identifying column is compared on its own.
     */
    @Test
    public void testSingleIdentifier()
    {
        List<TabularColumnDescription> columns = List.of(column("product_code", true), column("product_name", false));

        assertEquals(PostgresTabularDataSetConnector.getKeysetCondition(columns, List.of("RM-100", "Rice")),
                     "product_code > 'RM-100'");
    }


    /**
     * Several identifying columns are compared as a row, in column order - the order the records are sorted in.
     */
    @Test
    public void testCompositeIdentifier()
    {
        List<TabularColumnDescription> columns = List.of(column("warehouse_code", true), column("quantity", false),
                                                         column("product_code", true));

        assertEquals(PostgresTabularDataSetConnector.getKeysetCondition(columns, List.of("W1", "12", "O'Brien")),
                     "(warehouse_code, product_code) > ('W1', 'O''Brien')");
    }


    /**
     * Without identifying columns, a value for each, or a record the shape of the columns read, there is no
     * condition and the window is fetched by position.
     */
    @Test
    public void testNoCondition()
    {
        List<TabularColumnDescription> unkeyed = List.of(column("product_code", false), column("product_name", false));
        List<TabularColumnDescription> keyed   = List.of(column("product_code", true), column("product_name", false));

        assertNull(PostgresTabularDataSetConnector.getKeysetCondition(unkeyed, List.of("RM-100", "Rice")));
        assertNull(PostgresTabularDataSetConnector.getKeysetCondition(keyed, Arrays.asList(null, "Rice")));
        assertNull(PostgresTabularDataSetConnector.getKeysetCondition(keyed, List.of("RM-100")));
        assertNull(PostgresTabularDataSetConnector.getKeysetCondition(keyed, null));
        assertNull(PostgresTabularDataSetConnector.getKeysetCondition(null, List.of("RM-100", "Rice")));

        List<TabularColumnDescription> unnamed = new ArrayList<>(keyed);
        unnamed.set(1, null);
        assertNull(PostgresTabularDataSetConnector.getKeysetCondition(unnamed, List.of("RM-100", "Rice")),
                   "every column is read when one has no name, so positions are unknown");
    }
}
