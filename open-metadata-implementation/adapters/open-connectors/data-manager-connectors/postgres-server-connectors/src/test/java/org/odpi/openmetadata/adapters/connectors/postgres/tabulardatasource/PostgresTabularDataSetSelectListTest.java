/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;


/**
 * Checks that records are read with only the described columns, so that a destination table holding extra
 * columns of its own returns records the same shape as the source records written to it.
 */
public class PostgresTabularDataSetSelectListTest
{
    /**
     * The described columns are selected by name, in their described order.
     */
    @Test
    public void testDescribedColumnsAreSelected()
    {
        List<TabularColumnDescription> columns =
                List.of(new TabularColumnDescription("stock_movement_identifier", DataType.STRING, null, false, true),
                        new TabularColumnDescription("product_code", DataType.STRING, null, true, false),
                        new TabularColumnDescription("warehouse_code", DataType.STRING, null, true, false));

        assertEquals(PostgresTabularDataSetConnector.getSelectList(columns),
                     "stock_movement_identifier, product_code, warehouse_code");
    }


    /**
     * With no described columns, every column is read.
     */
    @Test
    public void testNoColumnsSelectsAll()
    {
        assertEquals(PostgresTabularDataSetConnector.getSelectList(null), "*");
        assertEquals(PostgresTabularDataSetConnector.getSelectList(List.of()), "*");
    }


    /**
     * A column without a name cannot be selected, so every column is read rather than issuing a broken query.
     */
    @Test
    public void testUnnamedColumnSelectsAll()
    {
        List<TabularColumnDescription> columns = new ArrayList<>();

        columns.add(new TabularColumnDescription("product_code", DataType.STRING, null, true, false));
        columns.add(null);

        assertEquals(PostgresTabularDataSetConnector.getSelectList(columns), "*");
    }
}
