/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.connections;

import org.odpi.openmetadata.accessservices.datamanager.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.DatabaseManagerProperties;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;

/**
 * CreateConnectionTest calls the ConnectionClient to create a connection with endpoints and connectorTypes
 * and then retrieve the results.  It uses no external Ids, an effective dates of null and forLineage/forDuplicateProcessing are false.
 */
public class CreateConnectionTest
{
    private final static String testCaseName       = "CreateConnectionTest";

    private final static int    maxPageSize        = 100;


    /*
     * The data manager name is constant - the guid is created as part of the test.
     */
    private final static String databaseManagerName            = "TestConnectionManager";
    private final static String databaseManagerDisplayName     = "ConnectionManager displayName";
    private final static String databaseManagerDescription     = "ConnectionManager description";
    private final static String databaseManagerTypeDescription = "ConnectionManager type";
    private final static String databaseManagerVersion         = "ConnectionManager version";

    private final static String connectionName        = "TestConnection";
    private final static String connectionDisplayName = "Connection displayName";
    private final static String connectionDescription = "Connection description";

    private final static String endpointName                 = "TestEndpoint";
    private final static String endpointDisplayName          = "Endpoint displayName";
    private final static String endpointDescription          = "Endpoint description";

    private static final String connectorTypeQName = "Egeria:ResourceConnector:DataFile";
    private static final String connectorTypeName = "Basic File Store Connector";
    private static final String connectorTypeGUID = "ba213761-f5f5-4cf5-a95f-6150aef09e0b";
    private static final String connectorTypeDescription = "Connector supports reading of Files.";  private final static String connectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider";
    private static final String assetTypeName = "DataFile";



    /**
     * Run all the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return  results of running test
     */
    public static FVTResults performFVT(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId)
    {
        FVTResults results = new FVTResults(testCaseName);

        results.incrementNumberOfTests();
        try
        {
            CreateConnectionTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        return results;
    }


    /**
     * Run all the tests in this class.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLogDestination logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private static void runIt(String                 serverPlatformRootURL,
                              String                 serverName,
                              String                 userId,
                              FVTAuditLogDestination auditLogDestination) throws FVTUnexpectedCondition
    {
        CreateConnectionTest thisTest = new CreateConnectionTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceWiki());

        ConnectionManagerClient client = thisTest.getConnectionClient(serverName, serverPlatformRootURL, auditLog);

        String databaseManagerGUID = thisTest.getDatabaseManager(serverName, serverPlatformRootURL, userId);
        String connectionGUID = thisTest.getConnection(client, databaseManagerGUID, userId);
        String endpointGUID = thisTest.createEndpoint(client, databaseManagerGUID, connectionGUID, userId);
        thisTest.addConnectorType(client, databaseManagerGUID, connectionGUID, userId);

        /*
         * Check that elements can be deleted one by one
         */
        String activityName = "deleteOneByOne";

        try
        {
            activityName = "deleteOneByOne - endpoint gone";

            client.removeEndpoint(userId, databaseManagerGUID, databaseManagerName, endpointGUID);

            thisTest.checkConnectionOK(client, connectionGUID, activityName, userId);
            thisTest.checkConnectorTypeOK(client, connectionGUID, activityName, userId);
            thisTest.checkEndpointGone(client, endpointGUID, null, activityName, userId);

            activityName = "deleteOneByOne - connection gone";
            client.removeConnection(userId, databaseManagerGUID, databaseManagerName, connectionGUID);

            thisTest.checkConnectorTypeOK(client, null, activityName, userId);
            thisTest.checkEndpointGone(client, endpointGUID, null, activityName, userId);
            thisTest.checkConnectionGone(client, connectionGUID, activityName, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private ConnectionManagerClient getConnectionClient(String   serverName,
                                                         String   serverPlatformRootURL,
                                                         AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getConnectionManagerClient";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);

            return new ConnectionManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a connection manager entity and return its GUID.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return unique identifier of the connection manager entity
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getDatabaseManager(String   serverName,
                                      String   serverPlatformRootURL,
                                      String   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getDatabaseManager";

        try
        {
            DataManagerRESTClient     restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);
            MetadataSourceClient client          = new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize);

            DatabaseManagerProperties properties = new DatabaseManagerProperties();
            properties.setQualifiedName(databaseManagerName);
            properties.setResourceName(databaseManagerDisplayName);
            properties.setResourceDescription(databaseManagerDescription);
            properties.setDeployedImplementationType(databaseManagerTypeDescription);
            properties.setVersion(databaseManagerVersion);

            String databaseManagerGUID = client.createDatabaseManager(userId, null, null, properties);

            if (databaseManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            String retrievedDatabaseManagerGUID = client.getMetadataSourceGUID(userId, databaseManagerName);

            if (retrievedDatabaseManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Retrieve)");
            }

            if (! retrievedDatabaseManagerGUID.equals(databaseManagerGUID))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Inconsistent GUIDs)");
            }

            return databaseManagerGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a connection is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param connectionGUID unique id of the connection to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkConnectionGone(ConnectionManagerClient client,
                                     String                  connectionGUID,
                                     String                  activityName,
                                     String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            ConnectionElement retrievedElement = client.getConnectionByGUID(userId, connectionGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Connection returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Retrieve returned)");
        }
        catch (InvalidParameterException expectedException)
        {
            // all ok
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



    /**
     * Check connection is ok.
     *
     * @param client interface to Data Manager OMAS
     * @param connectionGUID unique id of the connection
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkConnectionOK(ConnectionManagerClient client,
                                   String                  connectionGUID,
                                   String                  activityName,
                                   String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            ConnectionElement retrievedElement = client.getConnectionByGUID(userId, connectionGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectionElement from Retrieve)");
            }

            ConnectionProperties retrievedConnection = retrievedElement.getProperties();

            if (retrievedConnection == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectionDetails from Retrieve)");
            }

            if (! connectionName.equals(retrievedConnection.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve) =>>" + retrievedConnection);
            }
            if (! connectionDisplayName.equals(retrievedConnection.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve) =>>" + retrievedConnection);
            }
            if (! connectionDescription.equals(retrievedConnection.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve) =>>" + retrievedConnection);
            }

            List<ConnectionElement> connectionList = client.getConnectionsByName(userId, connectionName, 0, maxPageSize);

            if (connectionList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Connection for RetrieveByName)");
            }
            else if (connectionList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty Connection list for RetrieveByName)");
            }
            else if (connectionList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Connection list for RetrieveByName contains" + connectionList.size() +
                                                         " elements) =>>" + connectionList);
            }

            retrievedElement = connectionList.get(0);
            retrievedConnection = retrievedElement.getProperties();

            if (! connectionName.equals(retrievedConnection.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName) =>>" + retrievedConnection);
            }
            if (! connectionDisplayName.equals(retrievedConnection.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName) =>>" + retrievedConnection);
            }
            if (! connectionDescription.equals(retrievedConnection.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName) =>>" + retrievedConnection);
            }

            connectionList = client.getConnectionsByName(userId, connectionName, 1, maxPageSize);

            if (connectionList != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "Connection for RetrieveByName (from 1) =>>" + retrievedConnection);
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a connection and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the connection manager
     * @param userId calling user
     * @return GUID of connection
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getConnection(ConnectionManagerClient client,
                                 String                   databaseManagerGUID,
                                 String                   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getConnection";

        try
        {
            ConnectionProperties properties = new ConnectionProperties();

            properties.setQualifiedName(connectionName);
            properties.setDisplayName(connectionDisplayName);
            properties.setDescription(connectionDescription);

            String connectionGUID = client.createConnection(userId, databaseManagerGUID, databaseManagerName, properties);

            if (connectionGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }
            else
            {
                checkConnectionOK(client, connectionGUID, activityName, userId);
            }

            return connectionGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a connection endpoint is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param endpointGUID unique id of the connection endpoint to test
     * @param connectionGUID unique id of the connection to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkEndpointGone(ConnectionManagerClient client,
                                   String                  endpointGUID,
                                   String                  connectionGUID,
                                   String                  activityName,
                                   String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            EndpointElement retrievedElement = client.getEndpointByGUID(userId, endpointGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Endpoint returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getEndpointByGUID returned");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

        if (connectionGUID != null)
        {
            try
            {
                /*
                 * Only one endpoint created so nothing should be tied to the connection.
                 */
                ConnectionElement connection = client.getConnectionByGUID(userId, connectionGUID);
                ElementStub       endpoint   = connection.getEndpoint();

                if (endpoint != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Endpoint " + endpoint + " returned for connection)");
                }
            }
            catch(FVTUnexpectedCondition testCaseError)
            {
                throw testCaseError;
            }
            catch(Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }
        }
    }




    /**
     * Check a connection endpoint is stored OK.
     *
     * @param client interface to Data Manager OMAS
     * @param endpointGUID unique id of the connection
     * @param connectionGUID unique id of the connection
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkEndpointOK(ConnectionManagerClient client,
                                 String                   endpointGUID,
                                 String                   connectionGUID,
                                 String                   activityName,
                                 String                   userId) throws FVTUnexpectedCondition
    {
        try
        {
            EndpointElement retrievedElement = client.getEndpointByGUID(userId, endpointGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EndpointElement from Retrieve)");
            }

            EndpointProperties retrievedEndpoint = retrievedElement.getProperties();

            if (retrievedEndpoint == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EndpointDetails from Retrieve)");
            }

            if (! endpointName.equals(retrievedEndpoint.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve) =>>" + retrievedEndpoint);
            }
            if (! endpointDisplayName.equals(retrievedEndpoint.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve) =>>" + retrievedEndpoint);
            }
            if (! endpointDescription.equals(retrievedEndpoint.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve) =>>" + retrievedEndpoint);
            }

            List<EndpointElement> endpointList = client.getEndpointsByName(userId, endpointName, 0, maxPageSize);

            if (endpointList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Endpoint for RetrieveByName)");
            }
            else if (endpointList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty Endpoint list for RetrieveByName)");
            }
            else if (endpointList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Endpoint list for RetrieveByName contains" + endpointList.size() +
                                                         " elements)");
            }

            retrievedElement = endpointList.get(0);
            retrievedEndpoint = retrievedElement.getProperties();

            if (! endpointName.equals(retrievedEndpoint.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName) =>>" + retrievedEndpoint);
            }
            if (! endpointDisplayName.equals(retrievedEndpoint.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName) =>>" + retrievedEndpoint);
            }
            if (! endpointDescription.equals(retrievedEndpoint.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName) =>>" + retrievedEndpoint);
            }


            if (connectionGUID != null)
            {
                try
                {
                    /*
                     * Endpoint should be tied to the connection.
                     */
                    ConnectionElement connection = client.getConnectionByGUID(userId, connectionGUID);
                    ElementStub       endpoint   = connection.getEndpoint();

                    if (endpoint == null)
                    {
                        throw new FVTUnexpectedCondition(testCaseName, activityName + "(No Endpoint returned for connection)");
                    }
                    else
                    {
                        if (! endpointGUID.equals(endpoint.getGUID()))
                        {
                            System.out.println("ConnectionGUID: " + connectionGUID);
                            System.out.println("EndpointGUID: " + endpointGUID);
                            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong Endpoint returned for connection: " + endpoint.getGUID() + "rather than " + endpointGUID + ")");
                        }

                        if (! endpointName.equals(endpoint.getUniqueName()))
                        {
                            System.out.println("ConnectionGUID: " + connectionGUID);
                            System.out.println("Endpoint: " + endpoint);
                            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong Endpoint Qualified Name returned for connection: " + endpoint.getUniqueName() + "rather than " + endpointName + ")");
                        }
                    }
                }
                catch(FVTUnexpectedCondition testCaseError)
                {
                    throw testCaseError;
                }
                catch(Exception unexpectedError)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
                }
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a connection endpoint and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param databaseManagerGUID unique id of the connection manager
     * @param connectionGUID unique id of the connection
     * @param userId calling user
     * @return GUID of connection
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createEndpoint(ConnectionManagerClient client,
                                  String                   databaseManagerGUID,
                                  String                   connectionGUID,
                                  String                   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createEndpoint";

        try
        {
            EndpointProperties properties = new EndpointProperties();

            properties.setQualifiedName(endpointName);
            properties.setName(endpointDisplayName);
            properties.setResourceDescription(endpointDescription);

            String endpointGUID = client.createEndpoint(userId, databaseManagerGUID, databaseManagerName, properties);

            if (endpointGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for endpointCreate)");
            }
            else
            {
                client.setupEndpoint(userId, databaseManagerGUID, databaseManagerName, connectionGUID, endpointGUID);
                checkEndpointOK(client, endpointGUID, connectionGUID, activityName, userId);
            }

            return endpointGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a connection connectorType and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param connectionGUID unique id of the connection endpoint to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkConnectorTypeOK(ConnectionManagerClient client,
                                      String                  connectionGUID,
                                      String                  activityName,
                                      String                  userId) throws FVTUnexpectedCondition
    {
        try
        {
            ConnectorTypeElement retrievedElement = client.getConnectorTypeByGUID(userId, connectorTypeGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectorTypeElement from Retrieve)");
            }

            ConnectorTypeProperties retrievedConnectorType = retrievedElement.getProperties();

            if (retrievedConnectorType == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectorTypeDetails from Retrieve)");
            }

            if (! connectorTypeQName.equals(retrievedConnectorType.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve) =>>" + retrievedConnectorType);
            }
            if (! connectorTypeName.equals(retrievedConnectorType.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve) =>>" + retrievedConnectorType);
            }
            if (! connectorTypeDescription.equals(retrievedConnectorType.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve) =>>" + retrievedConnectorType);
            }
            if (! assetTypeName.equals(retrievedConnectorType.getSupportedAssetTypeName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad assetTypeName from Retrieve) =>>" + retrievedConnectorType);
            }
            if (! connectorProviderClassName.equals(retrievedConnectorType.getConnectorProviderClassName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad connectorProviderClassName from Retrieve) =>>" + retrievedConnectorType);
            }

            List<ConnectorTypeElement> connectorTypeList = client.getConnectorTypesByName(userId, connectorTypeName, 0, maxPageSize);

            if (connectorTypeList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectorType for RetrieveByName)");
            }
            else if (connectorTypeList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty ConnectorType list for RetrieveByName)");
            }
            else if (connectorTypeList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(ConnectorType list for RetrieveByName contains" + connectorTypeList.size() +
                                                         " elements)");
            }

            retrievedElement = connectorTypeList.get(0);
            retrievedConnectorType = retrievedElement.getProperties();

            if (! connectorTypeQName.equals(retrievedConnectorType.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName) =>>" + retrievedConnectorType);
            }
            if (! connectorTypeName.equals(retrievedConnectorType.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName) =>>" + retrievedConnectorType);
            }
            if (! connectorTypeDescription.equals(retrievedConnectorType.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName) =>>" + retrievedConnectorType);
            }

            if (connectionGUID != null)
            {
                try
                {
                    /*
                     * ConnectorType should be tied to the connection.
                     */
                    ConnectionElement connection    = client.getConnectionByGUID(userId, connectionGUID);
                    ElementStub       connectorType = connection.getConnectorType();

                    if (connectorType == null)
                    {
                        throw new FVTUnexpectedCondition(testCaseName, activityName + "(No connectorType returned for connection: " + connection + ")");
                    }
                    else
                    {
                        if (! connectorTypeGUID.equals(connectorType.getGUID()))
                        {
                            throw new FVTUnexpectedCondition(testCaseName, activityName + " (Wrong connector type returned for connection: " + connectorType.getGUID() + "rather than " + connectorTypeGUID + ")");
                        }

                        if (! connectorTypeQName.equals(connectorType.getUniqueName()))
                        {
                            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong connector type Qualified Name returned for connection's connectorType: " + connectorType.getUniqueName() + " rather than " + connectorTypeQName + ") Connection is " + connection);
                        }
                    }
                }
                catch(FVTUnexpectedCondition testCaseError)
                {
                    throw testCaseError;
                }
                catch(Exception unexpectedError)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
                }
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a connection connectorType and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param databaseManagerGUID unique id of the connection manager
     * @param connectionGUID unique id of the connection to connect the connectorType to
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void addConnectorType(ConnectionManagerClient client,
                                  String                  databaseManagerGUID,
                                  String                  connectionGUID,
                                  String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "addConnectorType";

        try
        {
            if (connectionGUID != null)
            {
                client.setupConnectorType(userId, databaseManagerGUID, databaseManagerName, connectionGUID, connectorTypeGUID);
            }
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
}
