/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server;

import org.odpi.openmetadata.accessservices.governanceengine.handlers.GovernanceConfigurationHandler;
import org.odpi.openmetadata.accessservices.governanceengine.properties.CatalogTargetProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.IntegrationConnectorProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.IntegrationGroupProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;


/**
 * The IntegrationConfigRESTServices provides the server-side implementation of the services to configure integration groups and integration
 * connectors.
 */
public class IntegrationConfigRESTServices
{
    private static final GovernanceEngineInstanceHandler instanceHandler = new GovernanceEngineInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(IntegrationConfigRESTServices.class),
                                                                                        instanceHandler.getServiceName());
    private  final   RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public IntegrationConfigRESTServices()
    {
    }


    /**
     * Create a new integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestBody containing:
     *                qualifiedName - unique name for the integration group;
     *                displayName - display name for messages and user interfaces;
     *                description - description of the types of integration connectors that wil be associated with
     *                    this integration group.
     *
     * @return unique identifier (guid) of the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public GUIDResponse createIntegrationGroup(String                     serverName,
                                               String                     userId,
                                               IntegrationGroupProperties requestBody)
    {
        final String        methodName = "createIntegrationGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response      = new GUIDResponse();
        AuditLog     auditLog      = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createIntegrationGroup(userId, requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from an integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration group definition.
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public IntegrationGroupElementResponse getIntegrationGroupByGUID(String    serverName,
                                                                     String    userId,
                                                                     String    guid)
    {
        final String        methodName = "getIntegrationGroupByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupElementResponse response = new IntegrationGroupElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationGroupByGUID(userId, guid));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from an integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public  IntegrationGroupElementResponse getIntegrationGroupByName(String    serverName,
                                                                      String    userId,
                                                                      String    name)
    {
        final String        methodName = "getIntegrationGroupByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupElementResponse response = new IntegrationGroupElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationGroupByName(userId, name));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of integration group definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public IntegrationGroupElementsResponse getAllIntegrationGroups(String  serverName,
                                                                    String  userId,
                                                                    int     startingFrom,
                                                                    int     maximumResults)
    {
        final String        methodName = "getAllIntegrationGroups";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupElementsResponse response = new IntegrationGroupElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllIntegrationGroups(userId, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param requestBody containing the new properties of the integration group.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public VoidResponse updateIntegrationGroup(String                     serverName,
                                               String                     userId,
                                               String                     guid,
                                               boolean                    isMergeUpdate,
                                               IntegrationGroupProperties requestBody)
    {
        final String        methodName = "updateIntegrationGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse        response             = new VoidResponse();
        AuditLog            auditLog             = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateIntegrationGroup(userId, guid, isMergeUpdate, requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param requestBody containing the unique name for the integration group.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public  VoidResponse    deleteIntegrationGroup(String             serverName,
                                                   String             userId,
                                                   String             guid,
                                                   DeleteRequestBody requestBody)
    {
        final String        methodName = "deleteIntegrationGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response      = new VoidResponse();
        AuditLog auditLog          = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteIntegrationGroup(userId, guid, requestBody.getQualifiedName());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param serverName name of the service to route the request to
     * @param userId identifier of calling user
     * @param requestBody containing:
     *                    qualifiedName - unique name for the integration connector;
     *                    displayName -  display name for the integration connector;
     *                    description - description of the analysis provided by the integration connector;
     *                    connection -  connection to instantiate the integration connector implementation.
     *
     * @return unique identifier of the integration connector or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public  GUIDResponse  createIntegrationConnector(String                         serverName,
                                                     String                         userId,
                                                     IntegrationConnectorProperties requestBody)
    {
        final String        methodName = "createIntegrationConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response      = new GUIDResponse();
        AuditLog auditLog          = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createIntegrationConnector(userId,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getVersionIdentifier(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getUsesBlockingCalls(),
                                                                    requestBody.getAdditionalProperties(),
                                                                    requestBody.getConnection()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration connector definition.
     *
     * @return properties of the integration connector or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public IntegrationConnectorElementResponse getIntegrationConnectorByGUID(String    serverName,
                                                                             String    userId,
                                                                             String    guid)
    {
        final String        methodName = "getIntegrationConnectorByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationConnectorElementResponse response = new IntegrationConnectorElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationConnectorByGUID(userId, guid));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public  IntegrationConnectorElementResponse getIntegrationConnectorByName(String    serverName,
                                                                              String    userId,
                                                                              String    name)
    {
        final String methodName = "getIntegrationConnectorByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationConnectorElementResponse response = new IntegrationConnectorElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationConnectorByName(userId, name));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of integration connector definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public IntegrationConnectorElementsResponse getAllIntegrationConnectors(String  serverName,
                                                                            String  userId,
                                                                            int     startingFrom,
                                                                            int     maximumResults)
    {
        final String        methodName = "getAllIntegrationConnectors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationConnectorElementsResponse response = new IntegrationConnectorElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllIntegrationConnectors(userId, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public GUIDListResponse getIntegrationConnectorRegistrations(String   serverName,
                                                                 String   userId,
                                                                 String   integrationConnectorGUID)
    {
        final String methodName = "getIntegrationConnectorRegistrations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getIntegrationConnectorRegistrations(userId, integrationConnectorGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of an existing integration connector definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param requestBody containing the new parameters for the integration connector.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public  VoidResponse    updateIntegrationConnector(String                          serverName,
                                                       String                          userId,
                                                       String                          guid,
                                                       boolean                         isMergeUpdate,
                                                       IntegrationConnectorProperties  requestBody)
    {
        final String methodName = "updateIntegrationConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse        response             = new VoidResponse();
        AuditLog            auditLog             = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateIntegrationConnector(userId,
                                                   guid,
                                                   isMergeUpdate,
                                                   requestBody.getQualifiedName(),
                                                   requestBody.getVersionIdentifier(),
                                                   requestBody.getDisplayName(),
                                                   requestBody.getDescription(),
                                                   requestBody.getConnection(),
                                                   requestBody.getAdditionalProperties(),
                                                   null);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param requestBody containing the unique name for the integration connector.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public VoidResponse deleteIntegrationConnector(String            serverName,
                                                   String            userId,
                                                   String            guid,
                                                   DeleteRequestBody requestBody)
    {
        final String methodName = "deleteIntegrationConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response      = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteIntegrationConnector(userId, guid, requestBody.getQualifiedName());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Register an integration connector with a specific integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param requestBody containing registration properties.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public VoidResponse registerIntegrationConnectorWithGroup(String                                   serverName,
                                                              String                                   userId,
                                                              String                                   integrationGroupGUID,
                                                              String                                   integrationConnectorGUID,
                                                              RegisteredIntegrationConnectorProperties requestBody)
    {
        final String methodName = "registerIntegrationConnectorWithEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.registerIntegrationConnectorWithGroup(userId,
                                                              integrationGroupGUID,
                                                              integrationConnectorGUID,
                                                              requestBody.getConnectorName(),
                                                              requestBody.getConnectorUserId(),
                                                              requestBody.getMetadataSourceQualifiedName(),
                                                              requestBody.getStartDate(),
                                                              requestBody.getRefreshTimeInterval(),
                                                              requestBody.getStopDate(),
                                                              requestBody.getPermittedSynchronization());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @return details of the integration connector and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public RegisteredIntegrationConnectorResponse getRegisteredIntegrationConnector(String serverName,
                                                                                    String userId,
                                                                                    String integrationGroupGUID,
                                                                                    String integrationConnectorGUID)
    {
        final String        methodName = "getRegisteredIntegrationConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredIntegrationConnectorResponse response = new RegisteredIntegrationConnectorResponse();
        AuditLog                            auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setRegisteredIntegrationConnector(handler.getRegisteredIntegrationConnector(userId, integrationGroupGUID, integrationConnectorGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the identifiers of the integration connectors registered with an integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public RegisteredIntegrationConnectorsResponse  getRegisteredIntegrationConnectors(String  serverName,
                                                                                       String  userId,
                                                                                       String  integrationGroupGUID,
                                                                                       int     startingFrom,
                                                                                       int     maximumResults)
    {
        final String methodName = "getRegisteredIntegrationConnectors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredIntegrationConnectorsResponse response = new RegisteredIntegrationConnectorsResponse();
        AuditLog                             auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getRegisteredIntegrationConnectors(userId, integrationGroupGUID, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Unregister an integration connector from the integration group.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unregisterIntegrationConnectorFromGroup(String          serverName,
                                                                String          userId,
                                                                String          integrationGroupGUID,
                                                                String          integrationConnectorGUID,
                                                                NullRequestBody requestBody)
    {
        final String        methodName = "unregisterIntegrationConnectorFromGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.unregisterIntegrationConnectorFromGroup(userId, integrationGroupGUID, integrationConnectorGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public VoidResponse addCatalogTarget(String                  serverName,
                                         String                  userId,
                                         String                  integrationConnectorGUID,
                                         String                  metadataElementGUID,
                                         CatalogTargetProperties requestBody)
    {
        final String methodName = "addCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addCatalogTarget(userId,
                                         integrationConnectorGUID,
                                         metadataElementGUID,
                                         requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     *
     * @return details of the integration connector and the elements it is to catalog or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the integration connector definition.
     */
    public CatalogTargetResponse getCatalogTarget(String serverName,
                                                  String userId,
                                                  String integrationConnectorGUID,
                                                  String metadataElementGUID)
    {
        final String        methodName = "getCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CatalogTargetResponse response = new CatalogTargetResponse();
        AuditLog                            auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getCatalogTarget(userId, integrationConnectorGUID, metadataElementGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the integration connector definition.
     */
    public CatalogTargetsResponse  getCatalogTargets(String  serverName,
                                                     String  userId,
                                                     String  integrationConnectorGUID,
                                                     int     startingFrom,
                                                     int     maximumResults)
    {
        final String methodName = "getCatalogTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CatalogTargetsResponse response = new CatalogTargetsResponse();
        AuditLog                             auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getCatalogTargets(userId, integrationConnectorGUID, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param metadataElementGUID unique identifier of the governance service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving/deleting the integration connector definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeCatalogTarget(String          serverName,
                                            String          userId,
                                            String          integrationConnectorGUID,
                                            String          metadataElementGUID,
                                            NullRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeCatalogTarget(userId, integrationConnectorGUID, metadataElementGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
