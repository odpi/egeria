/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wedgwood;

import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;


/**
 * Checks that a subscription delivery only writes the records that are new or have changed, so that delivering a
 * table the destination already holds writes nothing.
 */
public class WedgwoodCopyRecordsTest
{
    /**
     * The first delivery writes every record; delivering the same table again writes none.
     *
     * @throws Exception test failure
     */
    @Test
    public void testUnchangedTableWritesNothing() throws Exception
    {
        InMemoryTable source      = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("3", "c")));
        InMemoryTable destination = new InMemoryTable(List.of());

        assertEquals(copy(source, destination), 3);
        assertEquals(destination.rows, source.rows);

        destination.writeCount = 0;

        assertEquals(copy(source, destination), 0);
        assertEquals(destination.writeCount, 0, "an unchanged table should not be rewritten");
    }


    /**
     * Only the changed record is written; records added to and removed from the end are appended and deleted.
     *
     * @throws Exception test failure
     */
    @Test
    public void testOnlyDifferencesAreWritten() throws Exception
    {
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("3", "c")));

        assertEquals(copy(new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "B"), List.of("3", "c"), List.of("4", "d"))), destination), 2);
        assertEquals(destination.rows, List.of(List.of("1", "a"), List.of("2", "B"), List.of("3", "c"), List.of("4", "d")));

        assertEquals(copy(new InMemoryTable(List.of(List.of("1", "a"))), destination), 3);
        assertEquals(destination.rows, List.of(List.of("1", "a")));
    }


    /**
     * A destination that cannot be read back has every overlapping record written, as before.
     *
     * @throws Exception test failure
     */
    @Test
    public void testWriteOnlyDestinationIsOverwritten() throws Exception
    {
        InMemoryTable           backingTable = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b")));
        WriteOnlyTable          destination  = new WriteOnlyTable(backingTable);

        assertEquals(WedgwoodProvisionSubscriptionGovernanceActionConnector.copyRecords(backingTable, 2, destination, 2), 2);
    }


    private long copy(InMemoryTable source, InMemoryTable destination) throws Exception
    {
        return WedgwoodProvisionSubscriptionGovernanceActionConnector.copyRecords(source, source.getRecordCount(),
                                                                                destination, destination.getRecordCount());
    }


    /**
     * A table held in memory that can be read and written.  Deleting a record moves the later ones up.
     */
    private static class InMemoryTable implements ReadableTabularDataSource, WritableTabularDataSource
    {
        final List<List<String>> rows = new ArrayList<>();
        int writeCount = 0;

        InMemoryTable(List<List<String>> rows)
        {
            this.rows.addAll(rows);
        }

        @Override public long getRecordCount() { return rows.size(); }
        @Override public String getTableName() { return "table"; }
        @Override public String getTableDescription() { return null; }
        @Override public List<TabularColumnDescription> getColumnDescriptions() { return null; }
        @Override public int getColumnNumber(String columnName) { return 0; }
        @Override public List<String> readRecord(long rowNumber) { return rows.get((int) rowNumber); }
        @Override public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) { }
        @Override public void writeRecord(long rowNumber, List<String> dataValues) { rows.set((int) rowNumber, dataValues); writeCount++; }
        @Override public void appendRecord(List<String> dataValues) { rows.add(dataValues); writeCount++; }
        @Override public void deleteRecord(long rowNumber) { rows.remove((int) rowNumber); writeCount++; }
    }


    /**
     * A destination that can only be written to.
     *
     * @param table where the records go
     */
    private record WriteOnlyTable(InMemoryTable table) implements WritableTabularDataSource
    {
        @Override public long getRecordCount() { return table.getRecordCount(); }
        @Override public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) { }
        @Override public void writeRecord(long rowNumber, List<String> dataValues) { table.writeRecord(rowNumber, dataValues); }
        @Override public void appendRecord(List<String> dataValues) { table.appendRecord(dataValues); }
        @Override public void deleteRecord(long rowNumber) { table.deleteRecord(rowNumber); }
    }
}
