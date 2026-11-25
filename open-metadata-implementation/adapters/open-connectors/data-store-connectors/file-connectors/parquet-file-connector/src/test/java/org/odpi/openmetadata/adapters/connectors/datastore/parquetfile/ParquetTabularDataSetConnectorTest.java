/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TabularFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Test that the ParquetTabularDataSetConnector locates its file from the configuration properties and reads it
 * through the embedded ParquetFileStoreConnector.
 */
public class ParquetTabularDataSetConnectorTest
{
    private static final String resourcesDirectory = "src/test/resources";


    /**
     * Return an initialized connector of the requested class.
     *
     * @param connector connector to initialize
     * @param providerClassName name of its provider
     * @param configurationProperties configuration properties for its connection
     */
    private void initialize(Connector           connector,
                            String              providerClassName,
                            Map<String, Object> configurationProperties) throws Exception
    {
        ConnectorType connectorType = new ConnectorType();

        connectorType.setGUID(UUID.randomUUID().toString());
        connectorType.setQualifiedName("ConnectorType." + providerClassName);
        connectorType.setConnectorProviderClassName(providerClassName);

        Connection connection = new Connection();

        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName("Connection." + providerClassName);
        connection.setDisplayName("Test connection");
        connection.setConnectorType(connectorType);
        connection.setConfigurationProperties(configurationProperties);

        connector.initialize(UUID.randomUUID().toString(), new Connection(connection));
    }


    /**
     * Return a started tabular data set connector with an embedded file store connector.
     *
     * @param tableName name of the table (file name without the extension)
     * @param tableDescription optional description
     * @return connector
     */
    private ParquetTabularDataSetConnector getConnector(String tableName,
                                                        String tableDescription) throws Exception
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getName(), resourcesDirectory);
        configurationProperties.put(TabularFileConfigurationProperty.TABLE_NAME.getName(), tableName);

        if (tableDescription != null)
        {
            configurationProperties.put(TabularFileConfigurationProperty.TABLE_DESCRIPTION.getName(), tableDescription);
        }

        ParquetFileStoreConnector fileStoreConnector = new ParquetFileStoreConnector();

        this.initialize(fileStoreConnector, ParquetFileStoreProvider.class.getName(), null);

        ParquetTabularDataSetConnector connector = new ParquetTabularDataSetConnector();

        this.initialize(connector, ParquetTabularDataSetProvider.class.getName(), configurationProperties);

        List<Connector> embeddedConnectors = new ArrayList<>();
        embeddedConnectors.add(fileStoreConnector);
        connector.initializeEmbeddedConnectors(embeddedConnectors);

        connector.start();

        return connector;
    }


    @Test
    public void testProvider()
    {
        ParquetTabularDataSetProvider provider = new ParquetTabularDataSetProvider();

        ConnectorType connectorType = provider.getConnectorType();

        assertEquals(connectorType.getConnectorProviderClassName(), ParquetTabularDataSetProvider.class.getName());
        assertEquals(connectorType.getSupportedDeployedImplementationType(), DeployedImplementationType.PARQUET_TABULAR_DATA_SET.getDeployedImplementationType());
        assertTrue(connectorType.getConnectorInterfaces().contains(ReadableTabularDataSource.class.getName()));
        assertTrue(connectorType.getRecognizedConfigurationProperties().contains(TabularFileConfigurationProperty.TABLE_NAME.getName()));
    }


    @Test
    public void testReadTable() throws Exception
    {
        ParquetTabularDataSetConnector connector = this.getConnector("flat-v2-zstd", "My table");

        ParquetFileStoreConnector directConnector = new ParquetFileStoreConnector();
        this.initialize(directConnector, ParquetFileStoreProvider.class.getName(), null);
        directConnector.setFileStoreName(resourcesDirectory + "/flat-v2-zstd.parquet");
        directConnector.start();

        assertEquals(connector.getTableName(), "flat-v2-zstd");
        assertEquals(connector.getTableDescription(), "My table");
        assertEquals(connector.getRecordCount(), directConnector.getRecordCount());
        assertTrue(connector.getRecordCount() > 0);

        List<TabularColumnDescription> columns = connector.getColumnDescriptions();

        assertEquals(columns, directConnector.getColumnDescriptions());
        assertEquals(connector.getColumnNumber(columns.get(1).columnName()), 1);
        assertEquals(connector.getColumnNumber("no-such-column"), -1);

        assertTrue(connector.isDataAccessAvailable());

        for (long row = 0; row < connector.getRecordCount(); row++)
        {
            List<String> record = connector.readRecord(row);

            assertEquals(record.size(), columns.size());
            assertEquals(record, directConnector.readRecord(row));
        }

        connector.disconnect();
        directConnector.disconnect();
    }


    /**
     * Create the connector through the connector broker, using a virtual connection with the same structure
     * as the one in the Parquet Tabular Data Set template (the embedded connection has no endpoint).
     */
    @Test
    public void testVirtualConnection() throws Exception
    {
        ConnectorType fileStoreConnectorType = new ParquetFileStoreProvider().getConnectorType();
        Connection    fileStoreConnection    = new Connection();

        fileStoreConnection.setGUID(UUID.randomUUID().toString());
        fileStoreConnection.setQualifiedName("FileStore.Connection.Test");
        fileStoreConnection.setConnectorType(fileStoreConnectorType);

        EmbeddedConnection embeddedConnection = new EmbeddedConnection();

        embeddedConnection.setPosition(0);
        embeddedConnection.setEmbeddedConnection(fileStoreConnection);

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getName(), resourcesDirectory);
        configurationProperties.put(TabularFileConfigurationProperty.TABLE_NAME.getName(), "rowgroups-v2-zstd");

        VirtualConnection virtualConnection = new VirtualConnection();

        virtualConnection.setGUID(UUID.randomUUID().toString());
        virtualConnection.setQualifiedName("DataSet.Connection.Test");
        virtualConnection.setConnectorType(new ParquetTabularDataSetProvider().getConnectorType());
        virtualConnection.setConfigurationProperties(configurationProperties);
        virtualConnection.setEmbeddedConnections(List.of(embeddedConnection));

        Connector connector = new ConnectorBroker().getConnector(virtualConnection);

        assertTrue(connector instanceof ReadableTabularDataSource);

        connector.start();

        ReadableTabularDataSource dataSource = (ReadableTabularDataSource) connector;

        assertTrue(dataSource.getRecordCount() > 0);
        assertEquals(dataSource.readRecord(dataSource.getRecordCount() - 1).size(), dataSource.getColumnDescriptions().size());

        connector.disconnect();
    }


    /**
     * Step through every table in a collection, using a connector created by the connector broker from a
     * virtual connection with the same structure as the Parquet Tabular Data Set Collection template.
     */
    @Test
    public void testCollection() throws Exception
    {
        Connection fileStoreConnection = new Connection();

        fileStoreConnection.setGUID(UUID.randomUUID().toString());
        fileStoreConnection.setQualifiedName("FileStore.Connection.Test");
        fileStoreConnection.setConnectorType(new ParquetFileStoreProvider().getConnectorType());

        EmbeddedConnection embeddedConnection = new EmbeddedConnection();

        embeddedConnection.setPosition(0);
        embeddedConnection.setEmbeddedConnection(fileStoreConnection);

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(TabularFileConfigurationProperty.DIRECTORY_PATH_NAME.getName(), resourcesDirectory);

        ConnectorType connectorType = new ParquetTabularDataSetCollectionProvider().getConnectorType();

        assertEquals(connectorType.getSupportedDeployedImplementationType(), DeployedImplementationType.PARQUET_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType());
        assertTrue(connectorType.getConnectorInterfaces().contains(ReadableTabularDataCollection.class.getName()));

        VirtualConnection virtualConnection = new VirtualConnection();

        virtualConnection.setGUID(UUID.randomUUID().toString());
        virtualConnection.setQualifiedName("DataSetCollection.Connection.Test");
        virtualConnection.setConnectorType(connectorType);
        virtualConnection.setConfigurationProperties(configurationProperties);
        virtualConnection.setEmbeddedConnections(List.of(embeddedConnection));

        Connector connector = new ConnectorBroker().getConnector(virtualConnection);

        assertTrue(connector instanceof ReadableTabularDataCollection);

        connector.start();

        ReadableTabularDataCollection collection = (ReadableTabularDataCollection) connector;

        /*
         * Every Parquet file in the directory is a table - including the one that is not valid, which is
         * reported when it is brought into focus and read.  Other files are ignored.
         */
        List<String> tableNames = collection.getTableNames();

        assertEquals(tableNames, List.of("empty-uncompressed",
                                         "flat-v1-snappy",
                                         "flat-v2-zstd",
                                         "nested-v1-gzip",
                                         "not-parquet",
                                         "rowgroups-v2-zstd"));

        int tablesRead = 0;

        for (String tableName : tableNames)
        {
            collection.setTableName(tableName, null);

            assertEquals(collection.getTableName(), tableName);

            if ("not-parquet".equals(tableName))
            {
                try
                {
                    collection.getRecordCount();
                    fail("Invalid file not detected");
                }
                catch (ConnectorCheckedException expected)
                {
                    assertEquals(expected.getReportedErrorMessageId(), "PARQUET-FILE-CONNECTOR-400-001");
                }

                continue;
            }

            assertTrue(collection.getTableDescription().contains(tableName + ".parquet"));

            int columnCount = collection.getColumnDescriptions().size();

            assertTrue(columnCount > 0);

            for (long row = 0; row < collection.getRecordCount(); row++)
            {
                assertEquals(collection.readRecord(row).size(), columnCount);
            }

            tablesRead++;
        }

        assertEquals(tablesRead, 5);

        /*
         * The description supplied with the table name is used in preference to the one derived from the file.
         */
        collection.setTableName("flat-v1-snappy", "My description");
        assertEquals(collection.getTableDescription(), "My description");

        connector.disconnect();
    }


    @Test
    public void testDefaultDescription() throws Exception
    {
        ParquetTabularDataSetConnector connector = this.getConnector("flat-v1-snappy", null);

        assertNotNull(connector.getTableDescription());
        assertTrue(connector.getTableDescription().contains("flat-v1-snappy.parquet"));

        connector.disconnect();
    }


    @Test
    public void testMissingFile() throws Exception
    {
        ParquetTabularDataSetConnector connector = this.getConnector("no-such-table", null);

        try
        {
            connector.getRecordCount();
            fail("Missing file not detected");
        }
        catch (FileException expected)
        {
            assertTrue(expected.getMessage().contains("no-such-table.parquet"));
        }

        connector.disconnect();
    }


    @Test
    public void testNoEmbeddedConnector() throws Exception
    {
        ParquetTabularDataSetConnector connector = new ParquetTabularDataSetConnector();

        this.initialize(connector, ParquetTabularDataSetProvider.class.getName(), null);

        try
        {
            connector.start();
            fail("Missing embedded connector not detected");
        }
        catch (ConnectorCheckedException expected)
        {
            assertTrue(expected.getReportedErrorMessageId().equals("PARQUET-FILE-CONNECTOR-400-006"));
        }
    }
}
