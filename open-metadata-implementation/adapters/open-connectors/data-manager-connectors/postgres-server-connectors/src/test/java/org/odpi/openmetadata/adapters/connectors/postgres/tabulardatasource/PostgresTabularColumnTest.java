/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.testng.annotations.Test;

import java.sql.Types;

import static org.testng.Assert.assertEquals;


/**
 * Checks the column types that a tabular data set's columns are created with.
 */
public class PostgresTabularColumnTest
{
    /**
     * A date column keeps its time zone offset, so a time copied from a source that carries one is not stored
     * hours out.
     */
    @Test
    public void testDateColumnKeepsTimeZone()
    {
        PostgresTabularColumn column = new PostgresTabularColumn(new TabularColumnDescription("shipped_at", DataType.DATE, null, true, false));

        assertEquals(column.getColumnType(), ColumnType.TIMESTAMP_WITH_TIME_ZONE);
        assertEquals(column.getColumnType().getPostgresType(), "timestamp(6) with time zone");
    }


    /**
     * A calendar date is created as a date, and a date and time without a time zone as a timestamp without one.
     */
    @Test
    public void testLocalDateAndTimeColumns()
    {
        assertEquals(new PostgresTabularColumn(new TabularColumnDescription("born_on", DataType.LOCAL_DATE, null, true, false)).getColumnType().getPostgresType(),
                     "date");
        assertEquals(new PostgresTabularColumn(new TabularColumnDescription("posted_at", DataType.LOCAL_DATE_TIME, null, true, false)).getColumnType().getPostgresType(),
                     "timestamp(6) without time zone");
    }


    /**
     * Each kind of time read from a table is described as the kind that creates the same column again.  The
     * driver reports both timestamps as {@code Types.TIMESTAMP}, so the type name decides between them.
     */
    @Test
    public void testTimeTypesAreReadBackAsTheyWereCreated()
    {
        assertEquals(PostgresTabularDataSetConnector.getDataType(Types.DATE, "date"), DataType.LOCAL_DATE);
        assertEquals(PostgresTabularDataSetConnector.getDataType(Types.TIMESTAMP, "timestamp"), DataType.LOCAL_DATE_TIME);
        assertEquals(PostgresTabularDataSetConnector.getDataType(Types.TIMESTAMP, "timestamptz"), DataType.DATE);
        assertEquals(PostgresTabularDataSetConnector.getDataType(Types.TIMESTAMP_WITH_TIMEZONE, null), DataType.DATE);
        assertEquals(PostgresTabularDataSetConnector.getDataType(Types.TIMESTAMP, null), DataType.LOCAL_DATE_TIME);

        for (DataType dataType : new DataType[] { DataType.DATE, DataType.LOCAL_DATE_TIME, DataType.LOCAL_DATE })
        {
            ColumnType columnType = new PostgresTabularColumn(new TabularColumnDescription("c", dataType, null, true, false)).getColumnType();
            String     typeName   = columnType.getPostgresType().startsWith("timestamp(6) with time zone") ? "timestamptz"
                                  : columnType.getPostgresType().startsWith("timestamp") ? "timestamp" : "date";

            assertEquals(PostgresTabularDataSetConnector.getDataType(columnType.getJdbcType() == Types.TIMESTAMP_WITH_TIMEZONE ? Types.TIMESTAMP : columnType.getJdbcType(), typeName),
                         dataType, "round trip for " + dataType);
        }
    }


    /**
     * The other column types are unchanged.
     */
    @Test
    public void testOtherColumnTypes()
    {
        assertEquals(new PostgresTabularColumn(new TabularColumnDescription("n", DataType.LONG, null, true, false)).getColumnType(), ColumnType.LONG);
        assertEquals(new PostgresTabularColumn(new TabularColumnDescription("n", DataType.INT, null, true, false)).getColumnType(), ColumnType.INT);
        assertEquals(new PostgresTabularColumn(new TabularColumnDescription("n", DataType.BIGDECIMAL, null, true, false)).getColumnType(), ColumnType.STRING);
    }
}
