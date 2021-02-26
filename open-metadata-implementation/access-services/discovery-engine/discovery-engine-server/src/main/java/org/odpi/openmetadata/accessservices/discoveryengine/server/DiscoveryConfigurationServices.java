/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;

import org.odpi.openmetadata.accessservices.discoveryengine.handlers.DiscoveryConfigurationHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * The DiscoveryConfigurationServices provides the server-side implementation of the configuration services
 * from the Open Discovery Framework (ODF).  These services configure discovery engines and discovery services.
 */
public class DiscoveryConfigurationServices
{
    private static DiscoveryEngineServiceInstanceHandler instanceHandler = new DiscoveryEngineServiceInstanceHandler();

    private static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DiscoveryConfigurationServices.class),
                                                                                  instanceHandler.getServiceName());
    private    RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DiscoveryConfigurationServices()
    {
    }


    /**
     * Return the connection object for the Discovery Engine OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public ConnectionResponse getOutTopicConnection(String serverName,
                                                    String userId,
                                                    String callerId)
    {
        final String        methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestBody containing:
     *                qualifiedName - unique name for the discovery engine;
     *                displayName - display name for messages and user interfaces;
     *                description - description of the types of discovery services that wil be associated with
     *                    this discovery engine.
     *
     * @return unique identifier (guid) of the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public GUIDResponse createDiscoveryEngine(String                        serverName,
                                              String                        userId,
                                              NewDiscoveryEngineRequestBody requestBody)
    {
        final String        methodName = "createDiscoveryEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        String       qualifiedName = null;
        String       displayName   = null;
        String       description   = null;
        GUIDResponse response      = new GUIDResponse();
        AuditLog     auditLog      = null;


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            description = requestBody.getDescription();
        }

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUID(handler.createDiscoveryEngine(userId,
                                                           qualifiedName,
                                                           displayName,
                                                           description));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public DiscoveryEnginePropertiesResponse getDiscoveryEngineByGUID(String    serverName,
                                                                      String    userId,
                                                                      String    guid)
    {
        final String        methodName = "getDiscoveryEngineByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryEnginePropertiesResponse response = new DiscoveryEnginePropertiesResponse();
        AuditLog                          auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setDiscoveryEngineProperties(handler.getDiscoveryEngineByGUID(userId, guid));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  DiscoveryEnginePropertiesResponse getDiscoveryEngineByName(String    serverName,
                                                                       String    userId,
                                                                       String    name)
    {
        final String        methodName = "getDiscoveryEngineByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryEnginePropertiesResponse response = new DiscoveryEnginePropertiesResponse();
        AuditLog                          auditLog      = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setDiscoveryEngineProperties(handler.getDiscoveryEngineByName(userId, name));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of discovery engine definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery engine definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public DiscoveryEngineListResponse getAllDiscoveryEngines(String  serverName,
                                                              String  userId,
                                                              int     startingFrom,
                                                              int     maximumResults)
    {
        final String        methodName = "getAllDiscoveryEngines";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryEngineListResponse response = new DiscoveryEngineListResponse();
        AuditLog                    auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setDiscoveryEngines(handler.getAllDiscoveryEngines(userId, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of an existing discovery engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param requestBody containing the new properties of the discovery engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    updateDiscoveryEngine(String                           serverName,
                                                  String                           userId,
                                                  String                           guid,
                                                  UpdateDiscoveryEngineRequestBody requestBody)
    {
        final String        methodName = "updateDiscoveryEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        String              qualifiedName        = null;
        String              displayName          = null;
        String              description          = null;
        String              typeDescription      = null;
        String              version              = null;
        String              patchLevel           = null;
        String              source               = null;
        Map<String, String> additionalProperties = null;
        Map<String, Object> extendedProperties   = null;
        VoidResponse        response             = new VoidResponse();
        AuditLog            auditLog             = null;


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            description = requestBody.getDescription();
            typeDescription = requestBody.getTypeDescription();
            version = requestBody.getVersion();
            patchLevel = requestBody.getPatchLevel();
            source = requestBody.getSource();
            additionalProperties = requestBody.getAdditionalProperties();
            extendedProperties = requestBody.getExtendedProperties();
        }

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.updateDiscoveryEngine(userId,
                                          guid,
                                          qualifiedName,
                                          displayName,
                                          description,
                                          typeDescription,
                                          version,
                                          patchLevel,
                                          source,
                                          additionalProperties,
                                          extendedProperties);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the properties of the discovery engine.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery engine is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param requestBody containing the unique name for the discovery engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    deleteDiscoveryEngine(String             serverName,
                                                  String             userId,
                                                  String             guid,
                                                  DeleteRequestBody requestBody)
    {
        final String        methodName = "deleteDiscoveryEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        String       qualifiedName = null;
        VoidResponse response      = new VoidResponse();
        AuditLog auditLog          = null;


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
        }

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteDiscoveryEngine(userId,
                                          guid,
                                          qualifiedName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a discovery service definition.  The same discovery service can be associated with multiple
     * discovery engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestBody containing:
     *                    qualifiedName - unique name for the discovery service;
     *                    displayName -  display name for the discovery service;
     *                    description - description of the analysis provided by the discovery service;
     *                    connection -  connection to instantiate the discovery service implementation.
     *
     * @return unique identifier of the discovery service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  GUIDResponse  createDiscoveryService(String                         serverName,
                                                 String                         userId,
                                                 NewDiscoveryServiceRequestBody requestBody)
    {
        final String        methodName = "createDiscoveryService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        String       qualifiedName = null;
        String       displayName   = null;
        String       description   = null;
        Connection   connection    = null;
        GUIDResponse response      = new GUIDResponse();
        AuditLog auditLog          = null;


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            description = requestBody.getDescription();
            connection = requestBody.getConnection();
        }

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUID(handler.createDiscoveryService(userId,
                                                            qualifiedName,
                                                            displayName,
                                                            description,
                                                            connection));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the discovery service definition.
     *
     * @return properties of the discovery service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public DiscoveryServicePropertiesResponse getDiscoveryServiceByGUID(String    serverName,
                                                                        String    userId,
                                                                        String    guid)
    {
        final String        methodName = "getDiscoveryServiceByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryServicePropertiesResponse response = new DiscoveryServicePropertiesResponse();
        AuditLog                           auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setDiscoveryServiceProperties(handler.getDiscoveryServiceByGUID(userId, guid));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  DiscoveryServicePropertiesResponse getDiscoveryServiceByName(String    serverName,
                                                                         String    userId,
                                                                         String    name)
    {
        final String        methodName = "getDiscoveryServiceByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryServicePropertiesResponse response = new DiscoveryServicePropertiesResponse();
        AuditLog                           auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setDiscoveryServiceProperties(handler.getDiscoveryServiceByName(userId, name));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of discovery services definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery service definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  DiscoveryServiceListResponse getAllDiscoveryServices(String  serverName,
                                                                 String  userId,
                                                                 int     startingFrom,
                                                                 int     maximumResults)
    {
        final String        methodName = "getAllDiscoveryServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryServiceListResponse response = new DiscoveryServiceListResponse();
        AuditLog                     auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setDiscoveryServices(handler.getAllDiscoveryServices(userId, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of discovery engines that a specific discovery service is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryServiceGUID discovery service to search for.
     *
     * @return list of discovery engine unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public GUIDListResponse getDiscoveryServiceRegistrations(String   serverName,
                                                             String   userId,
                                                             String   discoveryServiceGUID)
    {
        final String        methodName = "getDiscoveryServiceRegistrations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getDiscoveryServiceRegistrations(userId, discoveryServiceGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of an existing discovery service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param requestBody containing the new parameters for the discovery service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    updateDiscoveryService(String                             serverName,
                                                   String                             userId,
                                                   String                             guid,
                                                   UpdateDiscoveryServiceRequestBody  requestBody)
    {
        final String        methodName = "updateDiscoveryService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        String              qualifiedName        = null;
        String              displayName          = null;
        String              description          = null;
        Connection          connection           = null;
        Map<String, String> additionalProperties = null;
        Map<String, Object> extendedProperties   = null;

        VoidResponse        response             = new VoidResponse();
        AuditLog            auditLog             = null;


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            description = requestBody.getDescription();
            connection = requestBody.getConnection();
            additionalProperties = requestBody.getAdditionalProperties();
            extendedProperties = requestBody.getExtendedProperties();
        }

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.updateDiscoveryService(userId,
                                           guid,
                                           qualifiedName,
                                           displayName,
                                           description,
                                           connection,
                                           additionalProperties,
                                           extendedProperties);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the properties of the discovery service.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery service is being deleted.  The discovery service is also
     * unregistered from its discovery engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param requestBody containing the unique name for the discovery service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    deleteDiscoveryService(String             serverName,
                                                   String             userId,
                                                   String             guid,
                                                   DeleteRequestBody  requestBody)
    {
        final String        methodName = "deleteDiscoveryService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        String       qualifiedName = null;
        VoidResponse response      = new VoidResponse();
        AuditLog auditLog          = null;


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
        }

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteDiscoveryService(userId,
                                           guid,
                                           qualifiedName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Register a discovery service with a specific discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param requestBody containing:
     *                    discoveryServiceGUID - unique identifier of the discovery service;
     *                    discoveryRequestTypes - list of asset types that this discovery service is able to process.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse  registerDiscoveryServiceWithEngine(String                                  serverName,
                                                             String                                  userId,
                                                             String                                  discoveryEngineGUID,
                                                             DiscoveryServiceRegistrationRequestBody requestBody)
    {
        final String        methodName = "registerDiscoveryServiceWithEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        String              discoveryServiceGUID      = null;
        String              discoveryRequestType      = null;
        Map<String, String> defaultAnalysisParameters = null;
        VoidResponse        response                  = new VoidResponse();
        AuditLog            auditLog                  = null;


        if (requestBody != null)
        {
            discoveryServiceGUID      = requestBody.getDiscoveryServiceGUID();
            discoveryRequestType      = requestBody.getDiscoveryRequestType();
            defaultAnalysisParameters = requestBody.getDefaultAnalysisParameters();
        }

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.registerDiscoveryServiceWithEngine(userId, discoveryEngineGUID, discoveryServiceGUID, discoveryRequestType, defaultAnalysisParameters);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve a specific discovery service registered with a discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @return details of the discovery service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public RegisteredDiscoveryServiceResponse getRegisteredDiscoveryService(String  serverName,
                                                                            String  userId,
                                                                            String  discoveryEngineGUID,
                                                                            String  discoveryServiceGUID)
    {
        final String        methodName = "getRegisteredGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredDiscoveryServiceResponse response = new RegisteredDiscoveryServiceResponse();
        AuditLog                           auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setRegisteredDiscoveryService(handler.getRegisteredDiscoveryService(userId, discoveryEngineGUID, discoveryServiceGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the identifiers of the discovery services registered with a discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  GUIDListResponse  getRegisteredDiscoveryServices(String  serverName,
                                                             String  userId,
                                                             String  discoveryEngineGUID,
                                                             int     startingFrom,
                                                             int     maximumResults)
    {
        final String        methodName = "getRegisteredDiscoveryServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setGUIDs(handler.getRegisteredDiscoveryServices(userId, discoveryEngineGUID, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Unregister a discovery service from the discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unregisterDiscoveryServiceFromEngine(String          serverName,
                                                             String          userId,
                                                             String          discoveryEngineGUID,
                                                             String          discoveryServiceGUID,
                                                             NullRequestBody requestBody)
    {
        final String        methodName = "unregisterDiscoveryServiceFromEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            DiscoveryConfigurationHandler handler = instanceHandler.getDiscoveryConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.unregisterDiscoveryServiceFromEngine(userId, discoveryEngineGUID, discoveryServiceGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
