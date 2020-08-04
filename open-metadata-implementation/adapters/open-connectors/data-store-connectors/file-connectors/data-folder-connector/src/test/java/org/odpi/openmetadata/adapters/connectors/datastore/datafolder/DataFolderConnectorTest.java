/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.datafolder;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test that the DataFolderConnector can respond sensibility to many different file states.
 */
public class DataFolderConnectorTest
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

        String endpointName    = "DataFolder.Endpoint." + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);


        final String connectorTypeDescription   = "DataFolder connector type.";
        final String connectorTypeJavaClassName = DataFolderProvider.class.getName();

        String connectorTypeName = "DataFolder.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "DataFolder connection.";

        String connectionName = "DataFolder.Connection.Test";

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


    @Test public void testLifecycle()
    {
        DataFolderConnector connector = new DataFolderConnector();

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
