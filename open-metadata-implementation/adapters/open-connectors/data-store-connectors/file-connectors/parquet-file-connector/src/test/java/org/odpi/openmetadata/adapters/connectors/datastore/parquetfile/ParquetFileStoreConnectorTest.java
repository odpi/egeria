/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetFileMetadata;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Test that the ParquetFileStoreConnector can describe a Parquet file and responds sensibly to files that
 * it is not able to describe.
 */
public class ParquetFileStoreConnectorTest
{
    private static final String resourcesDirectory = "src/test/resources/";


    /**
     * Return a started connector for a file.
     *
     * @param fileName location of the file
     * @return connector
     * @throws Exception test failed
     */
    private ParquetFileStoreConnector getConnector(String fileName) throws Exception
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setGUID(UUID.randomUUID().toString());
        endpoint.setQualifiedName("ParquetFileStore.Endpoint." + fileName);
        endpoint.setNetworkAddress(fileName);

        ConnectorType connectorType = new ConnectorType();

        connectorType.setGUID(UUID.randomUUID().toString());
        connectorType.setQualifiedName("ParquetFileStore.ConnectorType.Test");
        connectorType.setConnectorProviderClassName(ParquetFileStoreProvider.class.getName());

        Connection connection = new Connection();

        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName("ParquetFileStore.Connection.Test");
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        ParquetFileStoreConnector connector = new ParquetFileStoreConnector();

        connector.initialize(UUID.randomUUID().toString(), new Connection(connection));
        assertFalse(connector.isActive());

        connector.start();
        assertTrue(connector.isActive());

        return connector;
    }


    /**
     * Read a valid file.
     *
     * @throws Exception test failed
     */
    @Test public void testValidFile() throws Exception
    {
        ParquetFileStoreConnector connector = getConnector(resourcesDirectory + "rowgroups-v2-zstd.parquet");

        assertTrue(connector.getFileName().endsWith("rowgroups-v2-zstd.parquet"));
        assertEquals(connector.getRecordCount(), 5000L);
        assertEquals(connector.getColumnNames(), List.of("id", "name", "day"));

        ParquetFileMetadata fileMetadata = connector.getFileMetadata();

        assertEquals(fileMetadata.getFormatVersion(), 2);
        assertEquals(fileMetadata.getColumnProfiles().size(), 3);

        /*
         * The footer is only read once while the file is unchanged.
         */
        assertSame(connector.getFileMetadata(), fileMetadata);

        connector.disconnect();
        assertFalse(connector.isActive());
    }


    /**
     * The connector notices when the file is replaced.
     *
     * @throws Exception test failed
     */
    @Test public void testChangedFile() throws Exception
    {
        File file = Files.createTempFile("ParquetFileStoreConnectorTest", ".parquet").toFile();

        file.deleteOnExit();

        Files.copy(new File(resourcesDirectory + "rowgroups-v2-zstd.parquet").toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        ParquetFileStoreConnector connector = getConnector(file.getAbsolutePath());

        ParquetFileMetadata fileMetadata = connector.getFileMetadata();

        assertEquals(connector.getRecordCount(), 5000L);

        Files.copy(new File(resourcesDirectory + "nested-v1-gzip.parquet").toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        assertNotSame(connector.getFileMetadata(), fileMetadata);
        assertEquals(connector.getRecordCount(), 20L);

        connector.disconnect();
    }


    /**
     * A file that is not a Parquet file is reported as such.
     *
     * @throws Exception test failed
     */
    @Test public void testNotParquetFile() throws Exception
    {
        ParquetFileStoreConnector connector = getConnector(resourcesDirectory + "not-parquet.parquet");

        try
        {
            connector.getRecordCount();
            fail("The file should not have been accepted");
        }
        catch (FileReadException error)
        {
            assertEquals(error.getReportedErrorMessageId(), "PARQUET-FILE-CONNECTOR-400-001");
            assertTrue(error.getReportedErrorMessage().contains("not-parquet.parquet"));
        }

        connector.disconnect();
    }


    /**
     * A file that does not exist is reported by the base class.
     *
     * @throws Exception test failed
     */
    @Test public void testMissingFile() throws Exception
    {
        ParquetFileStoreConnector connector = getConnector(resourcesDirectory + "missing.parquet");

        try
        {
            connector.getColumnNames();
            fail("The file should not have been found");
        }
        catch (FileException error)
        {
            assertEquals(error.getReportedErrorMessageId(), "BASIC-FILE-CONNECTOR-404-002");
        }

        connector.disconnect();
    }


    /**
     * The rows of data can be read whichever version of the format, encodings and compression algorithm were
     * used to write the file.  These two files hold the same data.
     *
     * @throws Exception test failed
     */
    @Test public void testReadRecords() throws Exception
    {
        for (String fileName : new String[]{"flat-v1-snappy.parquet", "flat-v2-zstd.parquet"})
        {
            ParquetFileStoreConnector connector = getConnector(resourcesDirectory + fileName);

            assertTrue(connector.isDataAccessAvailable());

            /*
             * The values are in the order of the columns: flag, tiny, small, id, big, utiny, usmall, uint, ubig,
             * ratio, amount, price, name, day, at, created, updated, key, payload, empty.
             */
            List<String> firstRow = connector.readRecord(0);

            assertEquals(firstRow.size(), 20, fileName);
            assertEquals(firstRow.get(0), "true", fileName);
            assertEquals(firstRow.get(1), "-50", fileName);
            assertEquals(firstRow.get(3), "-1000", fileName);
            assertEquals(firstRow.get(8), "18000000000000000000", fileName);
            assertEquals(firstRow.get(11), "-50.00", fileName);
            assertNull(firstRow.get(12), fileName);
            assertEquals(firstRow.get(13), "2024-01-01", fileName);
            assertEquals(firstRow.get(14), "08:00:00", fileName);
            assertEquals(firstRow.get(15), "2024-01-01 00:00:00", fileName);
            assertEquals(firstRow.get(17), "00000000-0000-0000-0000-000000000000", fileName);
            assertNull(firstRow.get(19), fileName);

            List<String> lastRow = connector.readRecord(299);

            assertEquals(lastRow.get(0), "false", fileName);
            assertEquals(lastRow.get(3), "1990", fileName);
            assertEquals(lastRow.get(10), "348.5", fileName);
            assertEquals(lastRow.get(12), "name-049", fileName);
            assertEquals(lastRow.get(17), "00000000-0000-0000-0000-000000000299", fileName);

            try
            {
                connector.readRecord(300);
                fail("There is no row 300");
            }
            catch (FileReadException error)
            {
                assertEquals(error.getReportedErrorMessageId(), "PARQUET-FILE-CONNECTOR-400-004");
            }

            connector.disconnect();
        }
    }


    /**
     * Rows can be read in any order from a file with many row groups.  The rows are retrieved a thousand
     * at a time and this file has 5000 rows in row groups of 2048 rows.
     *
     * @throws Exception test failed
     */
    @Test public void testReadRecordsAcrossRowGroups() throws Exception
    {
        ParquetFileStoreConnector connector = getConnector(resourcesDirectory + "rowgroups-v2-zstd.parquet");

        for (long rowNumber : new long[]{4999, 0, 2047, 2048, 999, 1000, 4096, 1})
        {
            List<String> row = connector.readRecord(rowNumber);

            assertEquals(row.size(), 3);
            assertEquals(row.get(0), Long.toString(rowNumber));

            if (rowNumber % 10 == 0)
            {
                assertNull(row.get(1));
            }
            else
            {
                assertEquals(row.get(1), "customer-" + (rowNumber % 20));
            }
        }

        assertEquals(connector.readRecord(4999).get(2), "2021-05-15");

        long rowCount = 0;

        for (long rowNumber = 0; rowNumber < connector.getRecordCount(); rowNumber++)
        {
            assertEquals(connector.readRecord(rowNumber).get(0), Long.toString(rowNumber));
            rowCount++;
        }

        assertEquals(rowCount, 5000L);

        connector.disconnect();
    }


    /**
     * The value of a column that holds nested data is returned as a string.
     *
     * @throws Exception test failed
     */
    @Test public void testReadNestedRecords() throws Exception
    {
        ParquetFileStoreConnector connector = getConnector(resourcesDirectory + "nested-v1-gzip.parquet");

        List<String> row = connector.readRecord(3);

        assertEquals(row.size(), 5);
        assertEquals(row.get(0), "3");
        assertEquals(row.get(1), "[3, 4, 5]");
        assertTrue(row.get(2).contains("road 3"), row.get(2));
        assertTrue(row.get(3).contains("a=3") && row.get(3).contains("b=6"), row.get(3));
        assertTrue(row.get(4).contains("x3"), row.get(4));

        connector.disconnect();
    }


    /**
     * The number of times that each value occurs in a column, most frequent first.
     *
     * @throws Exception test failed
     */
    @Test public void testValueCounts() throws Exception
    {
        ParquetFileStoreConnector connector = getConnector(resourcesDirectory + "rowgroups-v2-zstd.parquet");

        /*
         * The name is null in every tenth row.  That removes customer-0 and customer-10 completely, leaving 18 names
         * that each occur in 250 of the 5000 rows.
         */
        Map<String, Long> valueCounts = connector.getValueCounts("name", 100);

        assertEquals(valueCounts.size(), 18);
        assertEquals(valueCounts.get("customer-7"), Long.valueOf(250L));
        assertFalse(valueCounts.containsKey("customer-10"));
        assertEquals(valueCounts.keySet().iterator().next(), "customer-1");

        /*
         * There are 5000 different values in this column.
         */
        assertNull(connector.getValueCounts("id", 100));

        try
        {
            connector.getValueCounts("no\"such\"column", 100);
            fail("There is no such column");
        }
        catch (FileReadException error)
        {
            assertEquals(error.getReportedErrorMessageId(), "PARQUET-FILE-CONNECTOR-500-002");
        }

        connector.disconnect();
    }


    /**
     * The connector can be used wherever a readable tabular data source is needed.
     *
     * @throws Exception test failed
     */
    @Test public void testTabularDataSource() throws Exception
    {
        ReadableTabularDataSource dataSource = getConnector(resourcesDirectory + "flat-v1-snappy.parquet");

        assertEquals(dataSource.getTableName(), "flat-v1-snappy");
        assertTrue(dataSource.getTableDescription().contains("300 rows"));
        assertEquals(dataSource.getRecordCount(), 300L);
        assertEquals(dataSource.getColumnNumber("id"), 3);
        assertEquals(dataSource.getColumnNumber("missing"), -1);

        List<TabularColumnDescription> columns = dataSource.getColumnDescriptions();

        assertEquals(columns.size(), 20);
        assertEquals(columns.get(3).columnName(), "id");
        assertEquals(columns.get(3).columnDataType(), DataType.INT);
        assertFalse(columns.get(3).isNullable());
        assertEquals(columns.get(11).columnDataType(), DataType.BIGDECIMAL);
        assertEquals(columns.get(12).columnDataType(), DataType.STRING);
        assertTrue(columns.get(12).isNullable());
        assertEquals(columns.get(13).columnDataType(), DataType.LOCAL_DATE);
        assertEquals(columns.get(15).columnDataType(), DataType.LOCAL_DATE_TIME);
        assertEquals(columns.get(16).columnDataType(), DataType.DATE);

        assertEquals(dataSource.readRecord(5).get(dataSource.getColumnNumber("id")), "-950");
    }
}
