/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tabulardatafvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * TabularDataSetContractFVT drives every tabular data set in the product catalogue through the contract its
 * consumers rely on.
 * <br><br>
 * These connectors turn open metadata into rows and columns, and everything downstream - the report
 * generator, the provisioning governance action that copies a data set into a file or a database table -
 * works from the shape they describe rather than from anything it knows about them.  So the shape has to be
 * true:
 * <ul>
 *     <li><b>A name.</b>  The table name becomes a file name, or a database table name, in whatever the data
 *     is provisioned into.</li>
 *     <li><b>Columns that agree with themselves.</b>  {@code getColumnNumber} is how a consumer finds a
 *     value in a row, so it has to return the position that column actually occupies in
 *     {@code getColumnDescriptions}.  A disagreement of one silently shifts every value into its
 *     neighbour's column - the data still arrives, and it is wrong.</li>
 *     <li><b>Rows the width of the columns.</b>  A row shorter than the column list is a provisioning run
 *     that fails part way through, or a CSV whose fields slip out of step from that row onward.</li>
 *     <li><b>An honest record count.</b>  Consumers read {@code 0..getRecordCount()-1}, so a count that
 *     overshoots is a read past the end.</li>
 * </ul>
 * None of that is checked anywhere today, and none of it fails loudly if it is wrong - a column index that
 * is off by one produces a full, plausible, wrong table.
 * <br><br>
 * The suite deliberately asserts on <b>shape rather than content</b>.  What rows a data set has depends on
 * what the repository happens to hold, and pinning that down would make the suite a test of the content
 * packs instead of the connectors.  Shape holds whatever the data is - including when there is none.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class TabularDataSetContractFVT
{
    /**
     * How many records to read per data set.  The contract is the same for the first row and the thousandth,
     * and some of these data sets are large - every open metadata type, every property of every type - so
     * reading all of them would turn a contract check into a soak test.
     */
    private static final int RECORDS_TO_SAMPLE = 5;


    /**
     * The data sets the product catalogue declares a connector for.
     *
     * @return test cases
     */
    static List<TabularDataSetCatalog.DataSetUnderTest> dataSets()
    {
        return TabularDataSetCatalog.dataSetsUnderTest();
    }


    /**
     * Take one data set through the whole contract.
     *
     * @param dataSet the data set to drive
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("dataSets")
    @DisplayName("Every tabular data set in the catalogue honours the contract its consumers rely on")
    void dataSetHonoursItsContract(TabularDataSetCatalog.DataSetUnderTest dataSet) throws Exception
    {
        Connector connector = dataSet.connectorProvider().getConnector(connectionFor(dataSet.productName()));

        assertTrue(connector instanceof ReadableTabularDataSource,
                   dataSet.productName() + " is published as a tabular data set but its connector is a " +
                           connector.getClass().getSimpleName() + ", which does not implement" +
                           " ReadableTabularDataSource - nothing downstream could read it");

        ReadableTabularDataSource dataSource = (ReadableTabularDataSource) connector;

        try
        {
            connector.start();

            if (dataSource instanceof ReadableTabularDataCollection collection)
            {
                /*
                 * A collection serves many tables through one connector.  Each has to be brought into focus
                 * and then honours the same contract, so the check is the same one applied to each in turn.
                 */
                List<String> tableNames = collection.getTableNames();

                assertNotNull(tableNames, dataSet.productName() + " is a collection but returns no table names");

                for (String tableName : tableNames)
                {
                    collection.setTableName(tableName, null);
                    checkOneTable(dataSource, dataSet.productName() + " / " + tableName);
                }
            }
            else
            {
                checkOneTable(dataSource, dataSet.productName());
            }
        }
        finally
        {
            connector.disconnect();
        }
    }


    /**
     * Check the table currently in focus.
     *
     * @param dataSource the connector
     * @param what names the data set in any failure message
     * @throws Exception a failure reading the data set
     */
    private void checkOneTable(ReadableTabularDataSource dataSource, String what) throws Exception
    {
        String tableName = dataSource.getTableName();

        assertNotNull(tableName, what + " has no table name - it becomes the file or table name wherever" +
                                         " this data set is provisioned to");
        assertFalse(tableName.isBlank(), what + " has a blank table name");

        List<TabularColumnDescription> columns = dataSource.getColumnDescriptions();

        assertNotNull(columns, what + " describes no columns");
        assertFalse(columns.isEmpty(), what + " describes an empty column list - there would be nothing to" +
                                               " provision");

        Set<String> namesSeen = new HashSet<>();

        for (int position = 0; position < columns.size(); position++)
        {
            TabularColumnDescription column = columns.get(position);

            assertNotNull(column, what + " has a null column at position " + position);
            assertNotNull(column.columnName(), what + " has a column with no name at position " + position);
            assertFalse(column.columnName().isBlank(),
                        what + " has a blank column name at position " + position);
            assertNotNull(column.columnDataType(),
                          what + " column '" + column.columnName() + "' has no data type - a consumer" +
                                  " creating a table for this data set has nothing to declare it as");

            assertTrue(namesSeen.add(column.columnName()),
                       what + " describes the column '" + column.columnName() + "' more than once, so a" +
                               " consumer looking it up by name cannot tell which position is meant");

            /*
             * The important one.  getColumnNumber is how a consumer finds a value in a row, so it has to
             * agree with the order the columns were described in.  Off by one here shifts every value into
             * the next column and nothing complains.
             */
            assertEquals(position,
                         dataSource.getColumnNumber(column.columnName()),
                         what + ": getColumnNumber(\"" + column.columnName() + "\") disagrees with the" +
                                 " position that column occupies in getColumnDescriptions, so values would be" +
                                 " read from the wrong column");
        }

        long recordCount = dataSource.getRecordCount();

        assertTrue(recordCount >= 0, what + " reports a negative record count of " + recordCount);

        long recordsToRead = Math.min(recordCount, RECORDS_TO_SAMPLE);

        for (long rowNumber = 0; rowNumber < recordsToRead; rowNumber++)
        {
            List<String> record = dataSource.readRecord(rowNumber);

            assertNotNull(record, what + " returns no record at row " + rowNumber + " although it reports " +
                                          recordCount + " records");
            if (columns.size() != record.size())
            {
                /*
                 * Both lists are reported, because which end is wrong is the whole question: a row with an
                 * extra value means the connector emits something it never declared, and a short row means
                 * it declared a column it does not fill.
                 */
                fail(what + " row " + rowNumber + " has " + record.size() + " values for " + columns.size() +
                             " columns - a consumer writing this out would put values under the wrong" +
                             " headings from here on.\n      columns declared: " + columnNames(columns) +
                             "\n      values returned : " + record);
            }
        }

        checkReadingPastTheEnd(dataSource, what, recordCount, columns.size());
    }


    /**
     * The column names, for a failure message that shows which end of a mismatch is wrong.
     *
     * @param columns the declared columns
     * @return their names, in order
     */
    private List<String> columnNames(List<TabularColumnDescription> columns)
    {
        List<String> names = new ArrayList<>();

        for (TabularColumnDescription column : columns)
        {
            names.add(column.columnName());
        }

        return names;
    }


    /**
     * A read past the last record must not answer with a row.
     * <br><br>
     * Consumers read until the record count, so this should never happen in a well behaved consumer - but a
     * connector that answers a read past the end with a row of nulls, or with the last row again, turns an
     * off-by-one in a consumer into duplicated or invented data rather than an error.  Returning nothing, or
     * refusing, are both fine; inventing a row is not.
     *
     * @param dataSource the connector
     * @param what names the data set in any failure message
     * @param recordCount the count it reported
     * @param columnCount how many columns it described
     */
    private void checkReadingPastTheEnd(ReadableTabularDataSource dataSource,
                                        String                    what,
                                        long                      recordCount,
                                        int                       columnCount)
    {
        try
        {
            List<String> pastTheEnd = dataSource.readRecord(recordCount);

            if ((pastTheEnd != null) && (pastTheEnd.size() == columnCount))
            {
                fail(what + " answered a read of row " + recordCount + " - one past the last record it" +
                             " reports - with a full row of " + columnCount + " values. A consumer that" +
                             " miscounts would silently take that as data.");
            }
        }
        catch (Exception expected)
        {
            /*
             * Refusing the read is a perfectly good answer, and is what most of these connectors do.
             */
        }
    }


    /**
     * Build the connection a tabular data set connector needs: where the metadata is, which server holds it,
     * and who to ask as.
     * <br><br>
     * These connectors are configured rather than injected - each one builds its own open metadata client in
     * {@code start()} from its connection - so this is the whole of what they need.
     *
     * @param dataSetName names the connector instance, which is what appears in its audit log messages
     * @return connection
     */
    private Connection connectionFor(String dataSetName)
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setNetworkAddress(OMAGPlatformExtension.getPlatformURLRoot());

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put("serverName", OMAGPlatformExtension.SERVER_NAME);
        configurationProperties.put("maxPageSize", OMAGPlatformExtension.MAX_PAGE_SIZE);

        Connection connection = new Connection();

        connection.setQualifiedName("TabularDataFVT::" + dataSetName);
        connection.setDisplayName(dataSetName);
        connection.setEndpoint(endpoint);
        connection.setConfigurationProperties(configurationProperties);
        connection.setUserId(OMAGPlatformExtension.USER_ID);

        return connection;
    }


    /**
     * The catalogue still declares the data sets this suite exists to drive, and every provider that serves
     * tabular data is either in it or listed as a deliberate absentee with a reason.
     * <br><br>
     * Without this, a refactor that emptied the catalogue would leave the parameterised test above with no
     * cases and the suite reporting success over nothing.
     */
    @org.junit.jupiter.api.Test
    @DisplayName("The product catalogue still declares the tabular data sets")
    void theCatalogueStillDeclaresItsDataSets()
    {
        List<TabularDataSetCatalog.DataSetUnderTest> dataSets = TabularDataSetCatalog.dataSetsUnderTest();

        assertTrue(dataSets.size() >= TabularDataSetCatalog.FEWEST_EXPECTED_DATA_SETS,
                   "The product catalogue declares only " + dataSets.size() + " tabular data sets, fewer" +
                           " than the " + TabularDataSetCatalog.FEWEST_EXPECTED_DATA_SETS + " this suite" +
                           " expects to find. Has ProductDefinitionEnum been reorganised?");

        List<String> withoutAName = new ArrayList<>();

        for (TabularDataSetCatalog.DataSetUnderTest dataSet : dataSets)
        {
            if ((dataSet.productName() == null) || dataSet.productName().isBlank())
            {
                withoutAName.add(dataSet.definition().name());
            }
        }

        assertTrue(withoutAName.isEmpty(),
                   "These product definitions serve a data set but have no product name: " + withoutAName);
    }
}
