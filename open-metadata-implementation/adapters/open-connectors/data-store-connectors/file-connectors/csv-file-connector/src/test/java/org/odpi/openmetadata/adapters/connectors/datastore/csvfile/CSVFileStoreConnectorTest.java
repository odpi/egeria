/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test that the CSVFileStoreConnector can respond sensibility to different file states.
 */
public class CSVFileStoreConnectorTest
{
    private static  String  resourcesDirectory = "src/test/resources/";
    private static  String  complexColumnsWithColumnNamesFile  = "ComplexColumnsWithColumnNames.csv";
    private static  String  emptyFile                          = "EmptyFile.csv";
    private static  String  justColumnNamesFile                = "JustColumnNames.csv";
    private static  String  noColumnNamesFile                  = "NoColumnNames.csv";
    private static  String  simpleColumnsWithColumnNamesFile   = "SimpleColumnsWithColumnNames.csv";


    private Connection getConnection(String   fileName)
    {
        final String endpointGUID      = "8bf8f5fa-b5d8-40e1-a00e-e4a0c59fd6c0";
        final String connectorTypeGUID = "2e1556a3-908f-4303-812d-d81b48b19bab";
        final String connectionGUID    = "b9af734f-f005-4085-9975-bf46c67a099a";

        final String endpointDescription = "File name.";

        String endpointName    = "CSVFileStore.Endpoint." + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);


        final String connectorTypeDescription   = "CSVFileStore connector type.";
        final String connectorTypeJavaClassName = CSVFileStoreProvider.class.getName();

        String connectorTypeName = "CSVFileStore.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "CSVFileStore connection.";

        String connectionName = "CSVFileStore.Connection.Test";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        return connection;
    }


    private ConnectionProperties getConnectionProperties(String   fileName)
    {
        return new ConnectionProperties(getConnection(fileName));
    }


    private void scanFile(CSVFileStoreConnector connector) throws FileException,
                                                                  FileReadException
    {
        List<String>   columnNames = connector.getColumnNames();

        assertTrue(columnNames != null);
        assertFalse(columnNames.isEmpty());

        for (long i=0; i<connector.getRecordCount(); i++)
        {
            List<String>  columns = connector.readRecord(i);
            assertTrue(columnNames.size() == columns.size());
        }

        try
        {
            connector.readRecord(10000L);
            assertTrue(false);

        }
        catch (FileReadException error)
        {
            assertTrue("The connector is unable to retrieve the requested record because the file is too short.".equals(error.getReportedSystemAction()));
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }
    }

    @Test public void testSimpleColumns()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties(resourcesDirectory + simpleColumnsWithColumnNamesFile));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();
            scanFile(connector);

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (Exception  error)
        {
            assertTrue(false);
        }
    }

    @Test public void testComplexColumns()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties(resourcesDirectory + complexColumnsWithColumnNamesFile));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();
            scanFile(connector);

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (Exception  error)
        {
            assertTrue(false);
        }
    }

    @Test public void testUnconventionalFile()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            Connection          connectionBean          = getConnection(resourcesDirectory + noColumnNamesFile);
            Map<String, Object> configurationProperties = new HashMap<>();
            List<String>        columnNames             = new ArrayList<>();

            configurationProperties.put(CSVFileStoreProvider.delimiterCharacterProperty, '-');
            configurationProperties.put(CSVFileStoreProvider.quoteCharacterProperty, '\'');

            columnNames.add("RecId");
            columnNames.add("ContactType");
            columnNames.add("Number");

            configurationProperties.put(CSVFileStoreProvider.columnNamesProperty, columnNames);


            connectionBean.setConfigurationProperties(configurationProperties);
            ConnectionProperties connectionProperties = new ConnectionProperties(connectionBean);

            connector.initialize(UUID.randomUUID().toString(), connectionProperties);
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();
            scanFile(connector);

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (Exception  error)
        {
            assertTrue(false);
        }
    }

    @Test public void testNullFileName()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties(null));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();
        }
        catch (FileException  error)
        {
            assertTrue("BASIC-FILE-CONNECTOR-400-001 The file name is null in the Connection object CSVFileStore.Connection.Test".equals(error.getMessage()));
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }
    }

    @Test public void testEmptyFile()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties(resourcesDirectory + emptyFile));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();
            assertTrue(connector.getRecordCount() == 0);

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (Exception  error)
        {
            assertTrue(false);
        }
    }


    @Test public void testJustColumnNames()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties(resourcesDirectory + justColumnNamesFile));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();
            assertTrue(connector.getRecordCount() == 0);

            List<String>  columnNames = connector.getColumnNames();
            assertTrue(columnNames.size() == 7);

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (Exception  error)
        {
            assertTrue(false);
        }
    }


    @Test public void testUnknownFile()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties("BadFileName"));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (FileException  error)
        {
            assertTrue("BASIC-FILE-CONNECTOR-404-002 The file named BadFileName in the Connection object CSVFileStore.Connection.Test does not exist".equals(error.getMessage()));
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }
    }


    @Test public void testDirectory()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties(resourcesDirectory));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (FileException  error)
        {
            assertTrue("BASIC-FILE-CONNECTOR-400-002 The file src/test/resources/ given in Connection object CSVFileStore.Connection.Test is a directory".equals(error.getMessage()));
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }
    }

    @Test public void testLifecycle()
    {
        CSVFileStoreConnector connector = new CSVFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties("TestFile"));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }
    }
}
