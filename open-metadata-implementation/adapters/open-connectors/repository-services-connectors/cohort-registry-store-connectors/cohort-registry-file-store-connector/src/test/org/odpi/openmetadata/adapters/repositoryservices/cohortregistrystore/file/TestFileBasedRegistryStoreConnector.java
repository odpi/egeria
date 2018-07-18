/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
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
        ConnectorConfigurationFactory factory = new ConnectorConfigurationFactory();
        Connection                    connection;

        connection = factory.getDefaultCohortRegistryConnection("localServer", UUID.randomUUID().toString());

        return connection;
    }


    private ConnectionProperties getGoodConnectionProperties()
    {
        Connection  connection = getGoodConnection();

        return new ConnectionProperties(connection);
    }

    private ConnectionProperties getNullEndpointConnectionProperties()
    {
        Connection  connection = getGoodConnection();
        Endpoint    endpoint   = connection.getEndpoint();

        endpoint.setAddress(null);
        connection.setEndpoint(endpoint);

        return new ConnectionProperties(connection);
    }


    private  MemberRegistration  getMemberRegistration(String   name)
    {
        MemberRegistration   member = new MemberRegistration();

        member.setMetadataCollectionId(name + "MetadataCollectionId");
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


        connector.initialize(UUID.randomUUID().toString(), getGoodConnectionProperties());
        assertFalse(connector.isActive());

        try
        {
            connector.start();
        }
        catch (Throwable exception)
        {
            assertTrue(false);
        }

        assertTrue(connector.isActive());

        exerciseActiveConnector(connector);

        try
        {
            connector.close();
        }
        catch (Throwable exception)
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
        catch (Throwable exception)
        {
            assertTrue(false);
        }

    }


    @Test public void testLifecycle()
    {
        FileBasedRegistryStoreConnector connector = new FileBasedRegistryStoreConnector();

        try
        {
            connector.initialize(UUID.randomUUID().toString(), getGoodConnectionProperties());
            assertFalse(connector.isActive());

            connector.start();
            assertTrue(connector.isActive());

            connector.close();
            assertFalse(connector.isActive());
        }
        catch (Throwable exception)
        {
            assertTrue(false);
        }
    }
}
