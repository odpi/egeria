/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wedgwood;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


/**
 * Checks that a subscription delivery only writes the records that are new, changed or gone, matching records by
 * identity rather than position, and that it reads the destination once without interleaving reads and writes.
 */
public class WedgwoodCopyRecordsTest
{
    private static final List<TabularColumnDescription> KEYED_COLUMNS =
            List.of(new TabularColumnDescription("Id", DataType.STRING, null, false, true),
                    new TabularColumnDescription("Value", DataType.STRING, null, true, false));

    private static final List<TabularColumnDescription> UNKEYED_COLUMNS =
            List.of(new TabularColumnDescription("Id", DataType.STRING, null, false, false),
                    new TabularColumnDescription("Value", DataType.STRING, null, true, false));

    private static final List<TabularColumnDescription> NUMERIC_COLUMNS =
            List.of(new TabularColumnDescription("Id", DataType.STRING, null, false, true),
                    new TabularColumnDescription("Amount", DataType.BIGDECIMAL, null, true, false));

    private static final List<TabularColumnDescription> NUMERIC_KEYED_COLUMNS =
            List.of(new TabularColumnDescription("Id", DataType.LONG, null, false, true),
                    new TabularColumnDescription("Amount", DataType.DOUBLE, null, true, false));

    private static final List<TabularColumnDescription> DATE_COLUMNS =
            List.of(new TabularColumnDescription("Id", DataType.STRING, null, false, true),
                    new TabularColumnDescription("When", DataType.DATE, null, true, false));

    private static final List<TabularColumnDescription> DATE_KEYED_COLUMNS =
            List.of(new TabularColumnDescription("When", DataType.DATE, null, false, true),
                    new TabularColumnDescription("Value", DataType.STRING, null, true, false));


    /**
     * The same point in time written in different ways - another offset, ISO form, a date for midnight, or
     * without the offset that a column without a time zone drops - needs no writing; a different time does.
     *
     * @throws Exception test failure
     */
    @Test
    public void testTimesAreComparedAsPointsInTime() throws Exception
    {
        InMemoryTable source = new InMemoryTable(List.of(List.of("1", "2026-06-15 11:20:00+05:30"),
                                                         List.of("2", "2026-06-15 11:20:00"),
                                                         List.of("3", "2026-06-15"),
                                                         List.of("4", "2026-06-15 11:20:00+00"),
                                                         List.of("5", "2026-10-19 14:37:01.481863+00"),
                                                         List.of("6", "2026-06-15 11:20:00+00")));
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "2026-06-15 05:50:00+00"),
                                                              List.of("2", "2026-06-15 11:20:00+01"),
                                                              List.of("3", "2026-06-15 00:00:00+00"),
                                                              List.of("4", "2026-06-15T11:20Z"),
                                                              List.of("5", "2026-10-19 15:37:01.481863+01"),
                                                              List.of("6", "2026-06-15 11:20:00+01")));

        assertEquals(copy(source, destination, DATE_COLUMNS), 1, "only record 6 names a different instant");
        assertEquals(destination.rows.get(5), List.of("6", "2026-06-15 11:20:00+00"));
    }


    /**
     * A point in time that identifies a record matches it when the destination writes the same instant with
     * another offset, rather than the record being deleted and added again.
     *
     * @throws Exception test failure
     */
    @Test
    public void testTimeIdentifiersMatchByInstant() throws Exception
    {
        InMemoryTable source      = new InMemoryTable(List.of(List.of("2026-06-15 11:20:00+05:30", "a"), List.of("2026-06-16 09:00:00+00", "b")));
        InMemoryTable destination = new InMemoryTable(List.of(List.of("2026-06-15 05:50:00+00", "a"), List.of("2026-06-16 10:00:00+01", "b")));

        assertEquals(copy(source, destination, DATE_KEYED_COLUMNS), 0);
    }


    /**
     * Calendar dates, and dates and times without a time zone, are compared the same way as points in time - so
     * a destination created before the kinds were told apart, which holds them with an offset or as midnight,
     * needs no writing.
     *
     * @throws Exception test failure
     */
    @Test
    public void testLocalDatesAndTimesAreComparedAsTimes() throws Exception
    {
        List<TabularColumnDescription> columns =
                List.of(new TabularColumnDescription("Id", DataType.STRING, null, false, true),
                        new TabularColumnDescription("PostedAt", DataType.LOCAL_DATE_TIME, null, true, false),
                        new TabularColumnDescription("BornOn", DataType.LOCAL_DATE, null, true, false));

        InMemoryTable source      = new InMemoryTable(List.of(List.of("1", "2026-06-15 11:20:00", "1980-02-29"),
                                                              List.of("2", "2026-06-15 11:20:00", "1980-02-29")));
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "2026-06-15 11:20:00+01", "1980-02-29 00:00:00+00"),
                                                              List.of("2", "2026-06-15 11:21:00+01", "1980-02-29")));

        assertEquals(copy(source, destination, columns), 1, "only record 2 shows a different time");
    }


    /**
     * A value in a date column that is not a point in time is compared exactly.
     */
    @Test
    public void testNonTimesAreComparedExactly()
    {
        assertEquals(WedgwoodProvisionSubscriptionGovernanceActionConnector.getComparableRecord(List.of("1", "infinity"), DATE_COLUMNS),
                     List.of("1", "infinity"));
        assertFalse(WedgwoodProvisionSubscriptionGovernanceActionConnector.recordsMatch(List.of("1", "infinity"), List.of("1", "-infinity"), DATE_COLUMNS));
    }


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

        assertEquals(copy(source, destination, KEYED_COLUMNS), 3);
        assertEquals(destination.rows, source.rows);

        destination.writeCount = 0;

        assertEquals(copy(source, destination, KEYED_COLUMNS), 0);
        assertEquals(destination.writeCount, 0, "an unchanged table should not be rewritten");
    }


    /**
     * A changed record is overwritten in place; records added and removed are appended and deleted.
     *
     * @throws Exception test failure
     */
    @Test
    public void testOnlyDifferencesAreWritten() throws Exception
    {
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("3", "c")));

        assertEquals(copy(new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "B"), List.of("3", "c"), List.of("4", "d"))), destination, KEYED_COLUMNS), 2);
        assertEquals(destination.rows, List.of(List.of("1", "a"), List.of("2", "B"), List.of("3", "c"), List.of("4", "d")));

        assertEquals(copy(new InMemoryTable(List.of(List.of("1", "a"))), destination, KEYED_COLUMNS), 3);
        assertEquals(destination.rows, List.of(List.of("1", "a")));
    }


    /**
     * A record removed from the middle of the source is removed from the destination, and nothing else is
     * touched.  Comparing by position deleted the last record instead and left the removed one in place.
     *
     * @throws Exception test failure
     */
    @Test
    public void testRecordRemovedFromTheMiddle() throws Exception
    {
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("3", "c"),
                                                              List.of("4", "d"), List.of("5", "e")));
        InMemoryTable source      = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("4", "d"),
                                                              List.of("5", "e")));

        assertEquals(copy(source, destination, KEYED_COLUMNS), 1);
        assertEquals(destination.rows, source.rows);
    }


    /**
     * A record added near the start of the source shifts every later one, but only the new record is written.
     *
     * @throws Exception test failure
     */
    @Test
    public void testRecordInsertedNearTheStart() throws Exception
    {
        List<List<String>> original = new ArrayList<>();

        for (int id = 10; id < 1010; id++)
        {
            original.add(List.of(Integer.toString(id), "v" + id));
        }

        List<List<String>> updated = new ArrayList<>(original);
        updated.add(1, List.of("100a", "new"));

        InMemoryTable destination = new InMemoryTable(original);

        assertEquals(copy(new InMemoryTable(updated), destination, KEYED_COLUMNS), 1);
        assertEquals(destination.writeCount, 1, "only the new record should be written");
        assertEquals(sorted(destination.rows), sorted(updated));
        assertEquals(destination.readCount, original.size(), "each destination record should be read once");
    }


    /**
     * A table with no identifying columns matches whole records: a changed record is replaced by deleting the
     * old one and adding the new one, and a duplicate record is kept as many times as the source holds it.
     *
     * @throws Exception test failure
     */
    @Test
    public void testUnkeyedTableMatchesWholeRecords() throws Exception
    {
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("2", "b"), List.of("3", "c")));
        InMemoryTable source      = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("3", "C"), List.of("3", "C")));

        assertEquals(copy(source, destination, UNKEYED_COLUMNS), 4);
        assertEquals(sorted(destination.rows), sorted(source.rows));

        destination.writeCount = 0;
        assertEquals(copy(source, destination, null), 0);
        assertEquals(destination.writeCount, 0);
    }


    /**
     * A changed record behind deleted ones is written at its position after the deletions.
     *
     * @throws Exception test failure
     */
    @Test
    public void testChangedRecordBehindDeletions() throws Exception
    {
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("3", "c"), List.of("4", "d")));
        InMemoryTable source      = new InMemoryTable(List.of(List.of("3", "C"), List.of("4", "d")));

        assertEquals(copy(source, destination, KEYED_COLUMNS), 3);
        assertEquals(destination.rows, source.rows);
    }


    /**
     * A number the destination writes in a different form - a fixed scale, or a trailing zero - is the same
     * number, so delivering the table again writes nothing; a number that really differs is written.
     *
     * @throws Exception test failure
     */
    @Test
    public void testNumbersAreComparedByValue() throws Exception
    {
        InMemoryTable source      = new InMemoryTable(List.of(List.of("1", "1.5"), List.of("2", "40000"), List.of("3", "-0.0"), List.of("4", "7")));
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "1.50"), List.of("2", "40000.00"), List.of("3", "0"), List.of("4", "7.01")));

        assertEquals(copy(source, destination, NUMERIC_COLUMNS), 1);
        assertEquals(destination.rows.get(3), List.of("4", "7"), "the source's own value should be written");
    }


    /**
     * A numeric identifier matches its record whatever form the destination writes it in, rather than the
     * record being deleted and added again.
     *
     * @throws Exception test failure
     */
    @Test
    public void testNumericIdentifiersMatchByValue() throws Exception
    {
        InMemoryTable source      = new InMemoryTable(List.of(List.of("10", "1.5"), List.of("20", "2")));
        InMemoryTable destination = new InMemoryTable(List.of(List.of("10.0", "1.5"), List.of("20.00", "2.0")));

        assertEquals(copy(source, destination, NUMERIC_KEYED_COLUMNS), 0);
    }


    /**
     * A value in a numeric column that is not a number, and every value of a text column, is compared exactly.
     */
    @Test
    public void testNonNumbersAreComparedExactly()
    {
        assertEquals(WedgwoodProvisionSubscriptionGovernanceActionConnector.getComparableRecord(List.of("1.50", "NaN"), NUMERIC_COLUMNS),
                     List.of("1.50", "NaN"));
        assertEquals(WedgwoodProvisionSubscriptionGovernanceActionConnector.getComparableRecord(Arrays.asList("1", null), NUMERIC_COLUMNS),
                     Arrays.asList("1", null));
        assertEquals(WedgwoodProvisionSubscriptionGovernanceActionConnector.getComparableRecord(List.of("1", "1.50"), null),
                     List.of("1", "1.50"));
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

        assertEquals(WedgwoodProvisionSubscriptionGovernanceActionConnector.copyRecords(backingTable, 2, destination, 2,
                                                                                        KEYED_COLUMNS), 2);
    }


    /**
     * The two sides need not be in the same order: a source read in the reverse order of the destination still
     * finds every record's partner, and changes only what differs.
     *
     * @throws Exception test failure
     */
    @Test
    public void testSidesInDifferentOrders() throws Exception
    {
        List<List<String>> original = new ArrayList<>();

        for (int id = 0; id < 200; id++)
        {
            original.add(List.of(String.format("%03d", id), "v" + id));
        }

        List<List<String>> updated = new ArrayList<>(original);

        updated.remove(150);
        updated.set(20, List.of("020", "changed"));
        updated.add(List.of("999", "new"));
        Collections.reverse(updated);

        InMemoryTable destination = new InMemoryTable(original);

        assertEquals(copy(new InMemoryTable(updated), destination, KEYED_COLUMNS), 3);
        assertEquals(sorted(destination.rows), sorted(updated));
        assertEquals(destination.readCount, original.size(), "each destination record should be read once");
    }


    /**
     * Each kind of change reaches the destination in one call, so a destination that can write many records at
     * once does; new records are appended in source order.
     *
     * @throws Exception test failure
     */
    @Test
    public void testChangesArePassedTogether() throws Exception
    {
        InMemoryTable destination = new InMemoryTable(List.of(List.of("1", "a"), List.of("2", "b"), List.of("3", "c"),
                                                              List.of("4", "d"), List.of("5", "e"), List.of("6", "f")));
        InMemoryTable source      = new InMemoryTable(List.of(List.of("2", "B"), List.of("4", "d"), List.of("5", "E"),
                                                              List.of("7", "g"), List.of("8", "h")));

        assertEquals(copy(source, destination, KEYED_COLUMNS), 7);
        assertEquals(destination.batchCount, 3, "one call each for deletions, overwrites and additions");
        assertEquals(destination.rows, List.of(List.of("2", "B"), List.of("4", "d"), List.of("5", "E"),
                                               List.of("7", "g"), List.of("8", "h")));
    }


    /**
     * Removing several records by their positions before any is removed takes out the right ones, whatever
     * order the positions are listed in, and a position listed twice is removed once.
     *
     * @throws Exception test failure
     */
    @Test
    public void testDeleteRecordsByOriginalPosition() throws Exception
    {
        InMemoryTable table = new InMemoryTable(List.of(List.of("0"), List.of("1"), List.of("2"), List.of("3"), List.of("4")));

        table.deleteRecords(List.of(1L, 4L, 1L, 2L));

        assertEquals(table.rows, List.of(List.of("0"), List.of("3")));
    }


    private long copy(InMemoryTable                  source,
                      InMemoryTable                  destination,
                      List<TabularColumnDescription> columnDescriptions) throws Exception
    {
        destination.readCount = 0;

        long changes = WedgwoodProvisionSubscriptionGovernanceActionConnector.copyRecords(source, source.getRecordCount(),
                                                                                          destination, destination.getRecordCount(),
                                                                                          columnDescriptions);

        assertFalse(destination.readAfterWrite, "the destination should not be read once writing has begun");

        return changes;
    }


    private static List<List<String>> sorted(List<List<String>> rows)
    {
        List<List<String>> sortedRows = new ArrayList<>(rows);

        sortedRows.sort(Comparator.comparing(Object::toString));

        return sortedRows;
    }


    /**
     * A table held in memory that can be read and written by position.  Deleting a record moves the later
     * ones up.  It records how often it is read and whether a read followed a write, which is what made the
     * database connector re-run its read-ahead query for every changed record.
     */
    private static class InMemoryTable implements ReadableTabularDataSource, WritableTabularDataSource
    {
        final List<List<String>> rows = new ArrayList<>();
        int     writeCount     = 0;
        int     batchCount     = 0;
        int     readCount      = 0;
        boolean written        = false;
        boolean readAfterWrite = false;

        InMemoryTable(List<List<String>> rows)
        {
            this.rows.addAll(rows);
        }

        @Override public long getRecordCount() { written = false; readAfterWrite = false; return rows.size(); }
        @Override public String getTableName() { return "table"; }
        @Override public String getTableDescription() { return null; }
        @Override public List<TabularColumnDescription> getColumnDescriptions() { return null; }
        @Override public int getColumnNumber(String columnName) { return 0; }
        @Override public List<String> readRecord(long rowNumber) { readCount++; readAfterWrite |= written; return rows.get((int) rowNumber); }
        @Override public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) { }
        @Override public void writeRecord(long rowNumber, List<String> dataValues) { rows.set((int) rowNumber, dataValues); writeCount++; written = true; }
        @Override public void appendRecord(List<String> dataValues) { rows.add(dataValues); writeCount++; written = true; }
        @Override public void deleteRecord(long rowNumber) { rows.remove((int) rowNumber); writeCount++; written = true; }
        @Override public void writeRecords(Map<Long, List<String>> dataRecords) throws ConnectorCheckedException { batchCount++; WritableTabularDataSource.super.writeRecords(dataRecords); }
        @Override public void appendRecords(List<List<String>> dataRecords) throws ConnectorCheckedException { batchCount++; WritableTabularDataSource.super.appendRecords(dataRecords); }
        @Override public void deleteRecords(List<Long> rowNumbers) throws ConnectorCheckedException { batchCount++; WritableTabularDataSource.super.deleteRecords(rowNumbers); }
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
