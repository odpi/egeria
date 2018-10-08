/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.structuredfile;

import org.odpi.openmetadata.adapters.connectors.structuredfile.ffdc.exception.FileReadException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test that the StructuredFileStoreConnector can respond sensibility to many different file states.
 */
public class StructuredFileStoreConnectorTest
{
    private static  String  resourcesDirectory = "target/test-classes/";
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

        String endpointName    = "StructuredFileStore.Endpoint." + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);


        final String connectorTypeDescription   = "StructuredFileStore connector type.";
        final String connectorTypeJavaClassName = StructuredFileStoreProvider.class.getName();

        String connectorTypeName = "StructuredFileStore.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "StructuredFileStore connection.";

        String connectionName = "StructuredFileStore.Connection.Test";

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


    private void scanFile(StructuredFileStoreConnector connector) throws FileReadException
    {
        List<String>   columnNames = connector.getColumnNames();

        assertTrue(columnNames != null);
        assertFalse(columnNames.isEmpty());

        for (int i=0; i<connector.getRecordCount(); i++)
        {
            List<String>  columns = connector.readRecord(i);
            assertTrue(columnNames.size() == columns.size());
        }

        try
        {
            connector.readRecord(10000);
            assertTrue(false);

        }
        catch (FileReadException error)
        {
            assertTrue("The connector is unable to retrieve the requested record because the file is too short.".equals(error.getReportedSystemAction()));
        }
        catch (Throwable exception)
        {
            assertTrue(false);
        }
    }

    @Test public void testSimpleColumns()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

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
        catch (Throwable  error)
        {
            assertTrue(false);
        }
    }

    @Test public void testComplexColumns()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

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
        catch (Throwable  error)
        {
            assertTrue(false);
        }
    }

    @Test public void testUnconventionalFile()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

        try
        {
            Connection           connectionBean       = getConnection(resourcesDirectory + noColumnNamesFile);
            Map<String, Object>  additionalProperties = new HashMap<>();
            List<String>         columnNames          = new ArrayList<>();

            additionalProperties.put(StructuredFileStoreProvider.delimiterCharacterProperty, '-');
            additionalProperties.put(StructuredFileStoreProvider.quoteCharacterProperty, '\'');

            columnNames.add("RecId");
            columnNames.add("ContactType");
            columnNames.add("Number");

            additionalProperties.put(StructuredFileStoreProvider.columnNamesProperty, columnNames);


            connectionBean.setAdditionalProperties(additionalProperties);
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
        catch (Throwable  error)
        {
            assertTrue(false);
        }
    }

    @Test public void testNullFileName()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties(null));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.getFileName();
        }
        catch (FileReadException  error)
        {
            assertTrue("STRUCTURED-FILE-CONNECTOR-400-001 The file name is null in the Connection object StructuredFileStore.Connection.Test".equals(error.getMessage()));
        }
        catch (Throwable exception)
        {
            assertTrue(false);
        }
    }

    @Test public void testEmptyFile()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

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
        catch (Throwable  error)
        {
            assertTrue(false);
        }
    }


    @Test public void testJustColumnNames()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

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
        catch (Throwable  error)
        {
            assertTrue(false);
        }
    }


    @Test public void testUnknownFile()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

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
        catch (FileReadException  error)
        {
            assertTrue("STRUCTURED-FILE-CONNECTOR-404-001 The file named BadFileName in the Connection object StructuredFileStore.Connection.Test does not exist".equals(error.getMessage()));
        }
        catch (Throwable exception)
        {
            assertTrue(false);
        }
    }


    @Test public void testDirectory()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

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
        catch (FileReadException  error)
        {
            assertTrue("STRUCTURED-FILE-CONNECTOR-400-002 The file target/test-classes/ given in Connection object StructuredFileStore.Connection.Test is a directory".equals(error.getMessage()));
        }
        catch (Throwable exception)
        {
            assertTrue(false);
        }
    }

    @Test public void testLifecycle()
    {
        StructuredFileStoreConnector connector = new StructuredFileStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getConnectionProperties("TestFile"));
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.disconnect();
            assertFalse(connector.isActive());
        }
        catch (Throwable exception)
        {
            assertTrue(false);
        }
    }
}
