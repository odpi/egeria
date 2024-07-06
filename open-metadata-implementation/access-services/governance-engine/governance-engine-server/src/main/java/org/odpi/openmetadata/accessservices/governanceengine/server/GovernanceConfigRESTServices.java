/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server;

import org.odpi.openmetadata.accessservices.governanceengine.handlers.GovernanceConfigurationHandler;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;


/**
 * The GovernanceConfigRESTServices provides the server-side implementation of the configuration services
 * from the Open Governance Framework (ODF).  These services configure governance engines and governance services.
 */
public class GovernanceConfigRESTServices
{
    private static final GovernanceEngineInstanceHandler instanceHandler = new GovernanceEngineInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(GovernanceConfigRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private  final   RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceConfigRESTServices()
    {
    }


    /**
     * Create a new governance engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param typeName type of governance engine
     * @param requestBody containing:
     *                qualifiedName - unique name for the governance engine;
     *                displayName - display name for messages and user interfaces;
     *                description - description of the types of governance services that wil be associated with
     *                    this governance engine.
     *
     * @return unique identifier (guid) of the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GUIDResponse createGovernanceEngine(String                         serverName,
                                               String                         userId,
                                               String                         typeName,
                                               NewGovernanceEngineRequestBody requestBody)
    {
        final String        methodName = "createGovernanceEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response      = new GUIDResponse();
        AuditLog     auditLog      = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createGovernanceEngine(userId,
                                                                typeName,
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getDisplayName(),
                                                                requestBody.getDescription()));
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
     * Return the properties from a governance engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance engine definition.
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GovernanceEngineElementResponse getGovernanceEngineByGUID(String    serverName,
                                                                     String    userId,
                                                                     String    guid)
    {
        final String        methodName = "getGovernanceEngineByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineElementResponse response = new GovernanceEngineElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getGovernanceEngineByGUID(userId, guid));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public  GovernanceEngineElementResponse getGovernanceEngineByName(String    serverName,
                                                                       String    userId,
                                                                       String    name)
    {
        final String        methodName = "getGovernanceEngineByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineElementResponse response = new GovernanceEngineElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getGovernanceEngineByName(userId, name));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance engine definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GovernanceEngineElementsResponse getAllGovernanceEngines(String  serverName,
                                                                    String  userId,
                                                                    int     startingFrom,
                                                                    int     maximumResults)
    {
        final String        methodName = "getAllGovernanceEngines";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineElementsResponse response = new GovernanceEngineElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllGovernanceEngines(userId, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param requestBody containing the new properties of the governance engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public VoidResponse updateGovernanceEngine(String                            serverName,
                                               String                            userId,
                                               String                            guid,
                                               UpdateGovernanceEngineRequestBody requestBody)
    {
        final String        methodName = "updateGovernanceEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse        response             = new VoidResponse();
        AuditLog            auditLog             = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateGovernanceEngine(userId,
                                               guid,
                                               requestBody.getQualifiedName(),
                                               requestBody.getDisplayName(),
                                               requestBody.getDescription(),
                                               requestBody.getTypeDescription(),
                                               requestBody.getVersion(),
                                               requestBody.getPatchLevel(),
                                               requestBody.getSource(),
                                               requestBody.getAdditionalProperties(),
                                               requestBody.getExtendedProperties());
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
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param requestBody containing the unique name for the governance engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public  VoidResponse    deleteGovernanceEngine(String             serverName,
                                                   String             userId,
                                                   String             guid,
                                                   DeleteRequestBody requestBody)
    {
        final String        methodName = "deleteGovernanceEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response      = new VoidResponse();
        AuditLog auditLog          = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteGovernanceEngine(userId, guid, requestBody.getQualifiedName());
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
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param serverName name of the service to route the request to
     * @param userId identifier of calling user
     * @param typeName type of governance service
     * @param requestBody containing:
     *                    qualifiedName - unique name for the governance service;
     *                    displayName -  display name for the governance service;
     *                    description - description of the analysis provided by the governance service;
     *                    connection -  connection to instantiate the governance service implementation.
     *
     * @return unique identifier of the governance service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public  GUIDResponse  createGovernanceService(String                          serverName,
                                                  String                          userId,
                                                  String                          typeName,
                                                  NewGovernanceServiceRequestBody requestBody)
    {
        final String        methodName = "createGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response      = new GUIDResponse();
        AuditLog auditLog          = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createGovernanceService(userId,
                                                                 typeName,
                                                                 requestBody.getQualifiedName(),
                                                                 requestBody.getDisplayName(),
                                                                 requestBody.getVersionIdentifier(),
                                                                 requestBody.getDescription(),
                                                                 requestBody.getDeployedImplementationType(),
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
     * Return the properties from a governance service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GovernanceServiceElementResponse getGovernanceServiceByGUID(String    serverName,
                                                                       String    userId,
                                                                       String    guid)
    {
        final String        methodName = "getGovernanceServiceByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceServiceElementResponse response = new GovernanceServiceElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getGovernanceServiceByGUID(userId, guid));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public  GovernanceServiceElementResponse getGovernanceServiceByName(String    serverName,
                                                                        String    userId,
                                                                        String    name)
    {
        final String methodName = "getGovernanceServiceByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceServiceElementResponse response = new GovernanceServiceElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getGovernanceServiceByName(userId, name));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance service definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GovernanceServiceElementsResponse getAllGovernanceServices(String  serverName,
                                                                      String  userId,
                                                                      int     startingFrom,
                                                                      int     maximumResults)
    {
        final String        methodName = "getAllGovernanceServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceServiceElementsResponse response = new GovernanceServiceElementsResponse();
        AuditLog                          auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllGovernanceServices(userId, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param governanceServiceGUID governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GUIDListResponse getGovernanceServiceRegistrations(String   serverName,
                                                              String   userId,
                                                              String   governanceServiceGUID)
    {
        final String methodName = "getGovernanceServiceRegistrations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getGovernanceServiceRegistrations(userId, governanceServiceGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param requestBody containing the new parameters for the governance service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public  VoidResponse    updateGovernanceService(String                             serverName,
                                                    String                             userId,
                                                    String                             guid,
                                                    UpdateGovernanceServiceRequestBody  requestBody)
    {
        final String methodName = "updateGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse        response             = new VoidResponse();
        AuditLog            auditLog             = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateGovernanceService(userId,
                                                guid,
                                                requestBody.getQualifiedName(),
                                                requestBody.getDisplayName(),
                                                requestBody.getVersionIdentifier(),
                                                requestBody.getDescription(),
                                                requestBody.getDeployedImplementationType(),
                                                requestBody.getConnection(),
                                                requestBody.getAdditionalProperties(),
                                                requestBody.getExtendedProperties());
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
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param requestBody containing the unique name for the governance service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public VoidResponse deleteGovernanceService(String            serverName,
                                                String            userId,
                                                String            guid,
                                                DeleteRequestBody requestBody)
    {
        final String methodName = "deleteGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response      = new VoidResponse();
        AuditLog     auditLog      = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteGovernanceService(userId, guid, requestBody.getQualifiedName());
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
     * Register a governance service with a specific governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param requestBody containing:
     *                    governanceServiceGUID - unique identifier of the governance service;
     *                    governanceRequestTypes - list of asset types that this governance service is able to process.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public VoidResponse registerGovernanceServiceWithEngine(String                                   serverName,
                                                            String                                   userId,
                                                            String                                   governanceEngineGUID,
                                                            GovernanceServiceRegistrationRequestBody requestBody)
    {
        final String methodName = "registerGovernanceServiceWithEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.registerGovernanceServiceWithEngine(userId,
                                                            governanceEngineGUID,
                                                            requestBody.getGovernanceServiceGUID(),
                                                            requestBody.getRequestType(),
                                                            requestBody.getServiceRequestType(),
                                                            requestBody.getRequestParameters());
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
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public RegisteredGovernanceServiceResponse getRegisteredGovernanceService(String serverName,
                                                                              String userId,
                                                                              String governanceEngineGUID,
                                                                              String governanceServiceGUID)
    {
        final String        methodName = "getRegisteredGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredGovernanceServiceResponse response = new RegisteredGovernanceServiceResponse();
        AuditLog                            auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setRegisteredGovernanceService(handler.getRegisteredGovernanceService(userId, governanceEngineGUID, governanceServiceGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public RegisteredGovernanceServicesResponse  getRegisteredGovernanceServices(String  serverName,
                                                                                 String  userId,
                                                                                 String  governanceEngineGUID,
                                                                                 int     startingFrom,
                                                                                 int     maximumResults)
    {
        final String methodName = "getRegisteredGovernanceServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredGovernanceServicesResponse response = new RegisteredGovernanceServicesResponse();
        AuditLog                             auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getRegisteredGovernanceServices(userId, governanceEngineGUID, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a request type for a governance service from the governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestType calling request
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unregisterGovernanceServiceRequestFromEngine(String          serverName,
                                                                     String          userId,
                                                                     String          requestType,
                                                                     String          governanceEngineGUID,
                                                                     String          governanceServiceGUID,
                                                                     NullRequestBody requestBody)
    {
        final String        methodName = "unregisterGovernanceServiceRequestFromEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.unregisterGovernanceServiceRequestFromEngine(userId, requestType, governanceEngineGUID, governanceServiceGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Unregister a governance service from the governance engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse unregisterGovernanceServiceFromEngine(String          serverName,
                                                              String          userId,
                                                              String          governanceEngineGUID,
                                                              String          governanceServiceGUID,
                                                              NullRequestBody requestBody)
    {
        final String        methodName = "unregisterGovernanceServiceFromEngine";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceConfigurationHandler handler = instanceHandler.getGovernanceConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.unregisterGovernanceServiceFromEngine(userId, governanceEngineGUID, governanceServiceGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
