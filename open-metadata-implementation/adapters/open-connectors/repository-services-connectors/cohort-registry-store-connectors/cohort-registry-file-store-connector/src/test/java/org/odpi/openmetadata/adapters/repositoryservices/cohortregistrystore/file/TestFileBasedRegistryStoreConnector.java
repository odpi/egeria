/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.UUID;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test that the FileBasedRegistryStoreConnector can respond sensibility to many different
 * file states.
 */
public class TestFileBasedRegistryStoreConnector
{
    private Connection getGoodConnection()
    {
        final String endpointGUID      = "8bf8f5fa-b5d8-40e1-a00e-e4a0c59fd6c0";
        final String connectorTypeGUID = "2e1556a3-908f-4303-812d-d81b48b19bab";
        final String connectionGUID    = "b9af734f-f005-4085-9975-bf46c67a099a";

        final String endpointDescription = "OMRS default cohort registry endpoint.";

        String endpointAddress = "Test.registrystore";
        String endpointName    = "DefaultCohortRegistry.Endpoint." + endpointAddress;

        Endpoint endpoint = new Endpoint();

        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setNetworkAddress(endpointAddress);


        final String connectorTypeDescription   = "OMRS default cohort registry connector type.";
        final String connectorTypeJavaClassName = FileBasedRegistryStoreProvider.class.getName();

        String connectorTypeName = "DefaultCohortRegistry.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "OMRS default cohort registry connection.";

        String connectionName = "DefaultCohortRegistry.Connection.Test";

        Connection connection = new Connection();

        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        return connection;
    }


    private Connection getNullEndpointConnectionProperties()
    {
        Connection  connection = getGoodConnection();
        Endpoint    endpoint   = connection.getEndpoint();

        endpoint.setNetworkAddress(null);
        connection.setEndpoint(endpoint);

        return connection;
    }


    private  MemberRegistration  getMemberRegistration(String   name)
    {
        MemberRegistration   member = new MemberRegistration();

        member.setMetadataCollectionId(name + "MetadataCollectionId");
        member.setMetadataCollectionName(name + "MetadataCollectionName");
        member.setOrganizationName(name + "OrganizationName");
        member.setServerName(name + "ServerName");
        member.setServerType(name + "ServerType");
        member.setRegistrationTime(new Date());
        member.setRepositoryConnection(new Connection());

        return member;
    }


    private  void  exerciseActiveConnector(FileBasedRegistryStoreConnector connector)
    {
        MemberRegistration   local = getMemberRegistration("local");

        connector.clearAllRegistrations();

        assertTrue(connector.retrieveLocalRegistration() == null);
        assertTrue(connector.retrieveRemoteRegistrations() == null);

        connector.saveLocalRegistration(null);

        assertTrue(connector.retrieveLocalRegistration() == null);
        assertTrue(connector.retrieveRemoteRegistrations() == null);

        connector.saveLocalRegistration(local);

        assertTrue(local.equals(connector.retrieveLocalRegistration()));
        assertTrue(connector.retrieveRemoteRegistration("FredMetadataCollectionId") == null);
        assertTrue(connector.retrieveRemoteRegistrations() == null);

        connector.saveLocalRegistration(null);

        assertTrue(local.equals(connector.retrieveLocalRegistration()));
        assertTrue(connector.retrieveRemoteRegistration("FredMetadataCollectionId") == null);
        assertTrue(connector.retrieveRemoteRegistrations() == null);

        MemberRegistration   fred = getMemberRegistration("Fred");

        connector.saveRemoteRegistration(fred);

        assertTrue(local.equals(connector.retrieveLocalRegistration()));
        assertTrue(fred.equals(connector.retrieveRemoteRegistration("FredMetadataCollectionId")));
        assertTrue(connector.retrieveRemoteRegistration("JoeMetadataCollectionId") == null);

        connector.removeLocalRegistration();

        assertTrue(connector.retrieveLocalRegistration() == null);
        assertTrue(fred.equals(connector.retrieveRemoteRegistration("FredMetadataCollectionId")));
        assertTrue(connector.retrieveRemoteRegistration("JoeMetadataCollectionId") == null);

        local.setMetadataCollectionId("newLocalMetadataCollectionId");
        connector.saveLocalRegistration(local);

        assertTrue(local.equals(connector.retrieveLocalRegistration()));
        assertTrue(fred.equals(connector.retrieveRemoteRegistration("FredMetadataCollectionId")));
        assertTrue(connector.retrieveRemoteRegistration("JoeMetadataCollectionId") == null);

        MemberRegistration   joe = getMemberRegistration("Joe");

        connector.saveRemoteRegistration(joe);
        assertTrue(local.equals(connector.retrieveLocalRegistration()));
        assertTrue(fred.equals(connector.retrieveRemoteRegistration("FredMetadataCollectionId")));
        assertTrue(joe.equals(connector.retrieveRemoteRegistration("JoeMetadataCollectionId")));
        assertTrue(connector.retrieveRemoteRegistration("newLocalMetadataCollectionId") == null);

        connector.clearAllRegistrations();

        assertTrue(connector.retrieveLocalRegistration() == null);
        assertTrue(connector.retrieveRemoteRegistration("JoeMetadataCollectionId") == null);
        assertTrue(connector.retrieveRemoteRegistrations() == null);
    }


    @Test public void testFileManagement()
    {
        FileBasedRegistryStoreConnector connector = new FileBasedRegistryStoreConnector();


        connector.initialize(UUID.randomUUID().toString(), getGoodConnection());
        assertFalse(connector.isActive());

        try
        {
            connector.start();
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }

        assertTrue(connector.isActive());

        exerciseActiveConnector(connector);

        try
        {
            connector.close();
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }

        assertFalse(connector.isActive());

        connector = new FileBasedRegistryStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getNullEndpointConnectionProperties());
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.close();
            assertFalse(connector.isActive());
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }

    }


    @Test public void testLifecycle()
    {
        FileBasedRegistryStoreConnector connector = new FileBasedRegistryStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getGoodConnection());
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.close();
            assertFalse(connector.isActive());
        }
        catch (Exception exception)
        {
            assertTrue(false);
        }
    }
}
