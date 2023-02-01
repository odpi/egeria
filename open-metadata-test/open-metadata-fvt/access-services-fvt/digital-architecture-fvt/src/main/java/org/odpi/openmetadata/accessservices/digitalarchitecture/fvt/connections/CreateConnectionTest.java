/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.fvt.connections;

import org.odpi.openmetadata.accessservices.digitalarchitecture.client.ConnectionManager;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.EndpointProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
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

    private final static String connectionName        = "TestConnection";
    private final static String connectionDisplayName = "Connection displayName";
    private final static String connectionDescription = "Connection description";

    private final static String endpointName                 = "TestEndpoint";
    private final static String endpointTechnicalName        = "Endpoint technicalName";
    private final static String endpointTechnicalDescription = "Endpoint technicalDescription";

    private final static String connectorTypeName        = "TestConnectorType";
    private final static String connectorTypeDisplayName = "ConnectorType displayName";
    private final static String connectorTypeDescription = "ConnectorType description";
    private final static String connectorProviderClassName = "string";

    private final static String connectorTypeTwoName        = "TestConnectorType2";
    private final static String connectorTypeTwoDisplayName = "ConnectorType2 displayName";

    private final static boolean isMergeUpdate = true;


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
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceWiki());

        ConnectionManager client = thisTest.getConnectionClient(serverName, serverPlatformRootURL, auditLog);

        String connectionGUID = thisTest.getConnection(client, userId);
        String endpointGUID = thisTest.createEndpoint(client, connectionGUID, userId);
        String connectorTypeGUID = thisTest.createConnectorType(client, connectionGUID, userId);

        /*
         * Check that elements can be deleted one by one
         */
        String activityName = "deleteOneByOne";

        try
        {
            client.removeConnection(userId, connectionGUID);

            activityName = "deleteOneByOne - connection gone";
            thisTest.checkConnectionGone(client, connectionGUID, activityName, userId);
            thisTest.checkConnectorTypeOK(client, connectorTypeGUID, null, activityName, userId);
            thisTest.checkEndpointOK(client, endpointGUID, null, activityName, userId);

            client.removeConnectorType(userId, connectorTypeGUID);

            activityName = "deleteOneByOne - connectorType gone";
            thisTest.checkConnectorTypeGone(client, connectorTypeGUID, null, activityName, userId);
            thisTest.checkEndpointOK(client, endpointGUID, null, activityName, userId);
            thisTest.checkConnectionGone(client, connectionGUID, activityName, userId);

            client.removeEndpoint(userId, endpointGUID);

            activityName = "deleteOneByOne - endpoint gone";
            thisTest.checkConnectorTypeGone(client, connectorTypeGUID, null, activityName, userId);
            thisTest.checkEndpointGone(client, endpointGUID, null, activityName, userId);
            thisTest.checkConnectionGone(client, connectionGUID, activityName, userId);

            /*
             * Update tests
             */
            activityName = "updateNonExistentConnectorType";

            String                    connectorTypeTwoGUID       = "Blah Blah";
            ConnectorTypeProperties connectorTypeProperties = new ConnectorTypeProperties();
            connectorTypeProperties.setQualifiedName(connectorTypeTwoName);
            connectorTypeProperties.setDisplayName(connectorTypeDisplayName); // Note wrong value
            connectorTypeProperties.setDescription(connectorTypeDescription);
            connectorTypeProperties.setConnectorProviderClassName(connectorProviderClassName); // Note wrong value

            try
            {

                client.updateConnectorType(userId, isMergeUpdate, connectorTypeTwoGUID, connectorTypeProperties);
                throw new FVTUnexpectedCondition(testCaseName, activityName);
            }
            catch (InvalidParameterException expectedError)
            {
                // very good
            }

            activityName = "updateConnectorTypeWithSameProperties";

            connectorTypeTwoGUID = client.createConnectorType(userId, connectorTypeProperties);

            ConnectorTypeElement beforeElement = client.getConnectorTypeByGUID(userId, connectorTypeTwoGUID);

            client.updateConnectorType(userId, isMergeUpdate, connectorTypeTwoGUID, connectorTypeProperties);

            ConnectorTypeElement afterElement = client.getConnectorTypeByGUID(userId, connectorTypeTwoGUID);

            /*
             * No change should occur in the version number because the properties are not different.
             */
            if (! beforeElement.getElementHeader().getVersions().equals(afterElement.getElementHeader().getVersions()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(version changed from " + beforeElement.getElementHeader().getVersions() + " to " + afterElement.getElementHeader().getVersions() + ")");
            }

            /*
             * This change affects the entity
             */
            activityName = "updateConnectorTypeProperties";

            connectorTypeProperties.setDisplayName(connectorTypeTwoDisplayName);

            client.updateConnectorType(userId, isMergeUpdate, connectorTypeTwoGUID, connectorTypeProperties);

            afterElement = client.getConnectorTypeByGUID(userId, connectorTypeTwoGUID);

            /*
             * The change should have taken effect.
             */
            if (beforeElement.getElementHeader().getVersions().equals(afterElement.getElementHeader().getVersions()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(version did not change from " + beforeElement.getElementHeader().getVersions() + ")");
            }

            if (! connectorTypeTwoDisplayName.equals(afterElement.getConnectorTypeProperties().getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(display name should be " + connectorTypeTwoDisplayName + " rather than " + afterElement.getConnectorTypeProperties().getDisplayName() + ")");
            }
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
    private ConnectionManager getConnectionClient(String   serverName,
                                                  String   serverPlatformRootURL,
                                                  AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getConnectionExchangeClient";

        try
        {
            DigitalArchitectureRESTClient restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL);

            return new ConnectionManager(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
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
    private void checkConnectionGone(ConnectionManager client,
                                     String            connectionGUID,
                                     String            activityName,
                                     String            userId) throws FVTUnexpectedCondition
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
    private void checkConnectionOK(ConnectionManager client,
                                   String            connectionGUID,
                                   String            activityName,
                                   String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            ConnectionElement retrievedElement = client.getConnectionByGUID(userId, connectionGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectionElement from Retrieve)");
            }

            ConnectionProperties retrievedConnection = retrievedElement.getConnectionProperties();

            if (retrievedConnection == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectionProperties from Retrieve)");
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
            retrievedConnection = retrievedElement.getConnectionProperties();

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
     * @param userId calling user
     * @return GUID of connection
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getConnection(ConnectionManager client,
                                 String            userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getConnection";

        try
        {
            ConnectionProperties properties = new ConnectionProperties();

            properties.setQualifiedName(connectionName);
            properties.setDisplayName(connectionDisplayName);
            properties.setDescription(connectionDescription);

            String connectionGUID = client.createConnection(userId, properties);

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
    private void checkEndpointGone(ConnectionManager client,
                                   String            endpointGUID,
                                   String            connectionGUID,
                                   String            activityName,
                                   String            userId) throws FVTUnexpectedCondition
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
    private void checkEndpointOK(ConnectionManager client,
                                 String            endpointGUID,
                                 String            connectionGUID,
                                 String            activityName,
                                 String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            EndpointElement retrievedElement = client.getEndpointByGUID(userId, endpointGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EndpointElement from Retrieve)");
            }

            EndpointProperties retrievedEndpoint = retrievedElement.getEndpointProperties();

            if (retrievedEndpoint == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EndpointProperties from Retrieve)");
            }

            if (! endpointName.equals(retrievedEndpoint.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve) =>>" + retrievedEndpoint);
            }
            if (! endpointTechnicalName.equals(retrievedEndpoint.getResourceName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad technicalName from Retrieve) =>>" + retrievedEndpoint);
            }
            if (! endpointTechnicalDescription.equals(retrievedEndpoint.getResourceDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad technicalDescription from Retrieve) =>>" + retrievedEndpoint);
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
            retrievedEndpoint = retrievedElement.getEndpointProperties();

            if (! endpointName.equals(retrievedEndpoint.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName) =>>" + retrievedEndpoint);
            }
            if (! endpointTechnicalName.equals(retrievedEndpoint.getResourceName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName) =>>" + retrievedEndpoint);
            }
            if (! endpointTechnicalDescription.equals(retrievedEndpoint.getResourceDescription()))
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
     * @param connectionGUID unique id of the connection
     * @param userId calling user
     * @return GUID of connection
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createEndpoint(ConnectionManager client,
                                  String            connectionGUID,
                                  String            userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createEndpoint";

        try
        {
            EndpointProperties properties = new EndpointProperties();

            properties.setQualifiedName(endpointName);
            properties.setResourceName(endpointTechnicalName);
            properties.setResourceDescription(endpointTechnicalDescription);

            String endpointGUID = client.createEndpoint(userId, properties);

            if (endpointGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for endpointCreate)");
            }
            else
            {
                client.setupEndpoint(userId, connectionGUID, endpointGUID);
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
     * Check a connection connectorType is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param connectorTypeGUID unique id of the connection connectorType to test
     * @param connectionGUID unique id of the connection endpoint to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkConnectorTypeGone(ConnectionManager client,
                                        String            connectorTypeGUID,
                                        String            connectionGUID,
                                        String            activityName,
                                        String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            ConnectorTypeElement retrievedElement = client.getConnectorTypeByGUID(userId, connectorTypeGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(ConnectorType returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getConnectorTypeByGUID returned");
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
                 * Only one connectorType created so nothing should be tied to the connection.
                 */
                ConnectionElement connection    = client.getConnectionByGUID(userId, connectionGUID);
                ElementStub       connectorType = connection.getConnectorType();

                if (connectorType != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Connector Type " + connectorType + " returned for Connection)");
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
     * Create a connection connectorType and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param connectorTypeGUID unique id of the connection connectorType to test
     * @param connectionGUID unique id of the connection endpoint to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkConnectorTypeOK(ConnectionManager client,
                                      String            connectorTypeGUID,
                                      String            connectionGUID,
                                      String            activityName,
                                      String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            ConnectorTypeElement retrievedElement = client.getConnectorTypeByGUID(userId, connectorTypeGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectorTypeElement from Retrieve)");
            }

            ConnectorTypeProperties retrievedConnectorType  = retrievedElement.getConnectorTypeProperties();

            if (retrievedConnectorType == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ConnectorTypeProperties from Retrieve)");
            }

            if (! connectorTypeName.equals(retrievedConnectorType.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve) =>>" + retrievedConnectorType);
            }
            if (! connectorTypeDisplayName.equals(retrievedConnectorType.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve) =>>" + retrievedConnectorType);
            }
            if (! connectorTypeDescription.equals(retrievedConnectorType.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve) =>>" + retrievedConnectorType);
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
            retrievedConnectorType = retrievedElement.getConnectorTypeProperties();

            if (! connectorTypeName.equals(retrievedConnectorType.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName) =>>" + retrievedConnectorType);
            }
            if (! connectorTypeDisplayName.equals(retrievedConnectorType.getDisplayName()))
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
                            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong connector type returned for connection: " + connectorType.getGUID() + "rather than " + connectorTypeGUID + ")");
                        }

                        if (! connectorTypeName.equals(connectorType.getUniqueName()))
                        {
                            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong connector type Qualified Name returned for connection: " + connectorType.getUniqueName() + "rather than " + endpointName + ")");
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
     * @param connectionGUID unique id of the connection to connect the connectorType to
     * @param userId calling user
     * @return GUID of connection
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createConnectorType(ConnectionManager client,
                                       String            connectionGUID,
                                       String            userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createConnectorType";

        try
        {
            ConnectorTypeProperties properties = new ConnectorTypeProperties();

            properties.setQualifiedName(connectorTypeName);
            properties.setDisplayName(connectorTypeDisplayName);
            properties.setDescription(connectorTypeDescription);
            properties.setConnectorProviderClassName(connectorProviderClassName);

            String connectorTypeGUID = client.createConnectorType(userId, properties);

            if (connectorTypeGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for connectorTypeCreate)");
            }
            else
            {
                if (connectionGUID != null)
                {
                    client.setupConnectorType(userId, connectionGUID, connectorTypeGUID);
                }

                checkConnectorTypeOK(client, connectorTypeGUID, connectionGUID, activityName, userId);
            }

            return connectorTypeGUID;
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
}
