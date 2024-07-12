/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationGroupProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.GovernanceEngineConfigurationHandler;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.IntegrationGroupConfigurationHandler;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.slf4j.LoggerFactory;


/**
 * The GovernanceConfigRESTServices provides the server-side implementation of the configuration services
 * from the Open Governance Framework (ODF).  These services configure governance engines and governance services.
 */
public class GovernanceConfigRESTServices
{
    private static final GAFMetadataManagementInstanceHandler instanceHandler = new GAFMetadataManagementInstanceHandler();

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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                               String                         serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createGovernanceEngine(userId,
                                                                typeName,
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getDisplayName(),
                                                                requestBody.getDescription(),
                                                                instanceHandler.getSupportedZones(userId,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance engine definition.
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GovernanceEngineElementResponse getGovernanceEngineByGUID(String    serverName,
                                                                     String    serviceURLMarker,
                                                                     String    userId,
                                                                     String    guid)
    {
        final String methodName = "getGovernanceEngineByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineElementResponse response = new GovernanceEngineElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getGovernanceEngineByGUID(userId,
                                                                  guid,
                                                                  instanceHandler.getSupportedZones(userId,
                                                                                                    serverName,
                                                                                                    serviceURLMarker,
                                                                                                    methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param requestBody qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public  GovernanceEngineElementResponse getGovernanceEngineByName(String          serverName,
                                                                      String          serviceURLMarker,
                                                                      String          userId,
                                                                      NameRequestBody requestBody)
    {
        final String        methodName = "getGovernanceEngineByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceEngineElementResponse response = new GovernanceEngineElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            if (requestBody != null)
            {
                GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setElement(handler.getGovernanceEngineByName(userId,
                                                                      requestBody.getName(),
                                                                      instanceHandler.getSupportedZones(userId,
                                                                                                        serverName,
                                                                                                        serviceURLMarker,
                                                                                                        methodName)));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NameRequestBody.class.getName());
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
     * Return the list of governance engine definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                    String  serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllGovernanceEngines(userId,
                                                                 startingFrom,
                                                                 maximumResults,
                                                                 instanceHandler.getSupportedZones(userId,
                                                                                                   serverName,
                                                                                                   serviceURLMarker,
                                                                                                   methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                               String                            serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

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
                                               requestBody.getExtendedProperties(),
                                               instanceHandler.getSupportedZones(userId,
                                                                                 serverName,
                                                                                 serviceURLMarker,
                                                                                 methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                   String             serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteGovernanceEngine(userId,
                                               guid,
                                               requestBody.getQualifiedName(),
                                               instanceHandler.getSupportedZones(userId,
                                                                                 serverName,
                                                                                 serviceURLMarker,
                                                                                 methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                  String                          serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createGovernanceService(userId,
                                                                 typeName,
                                                                 requestBody.getQualifiedName(),
                                                                 requestBody.getName(),
                                                                 requestBody.getVersionIdentifier(),
                                                                 requestBody.getDescription(),
                                                                 requestBody.getDeployedImplementationType(),
                                                                 requestBody.getConnection(),
                                                                 instanceHandler.getSupportedZones(userId,
                                                                                                   serverName,
                                                                                                   serviceURLMarker,
                                                                                                   methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GovernanceServiceElementResponse getGovernanceServiceByGUID(String    serverName,
                                                                       String    serviceURLMarker,
                                                                       String    userId,
                                                                       String    guid)
    {
        final String        methodName = "getGovernanceServiceByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceServiceElementResponse response = new GovernanceServiceElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getGovernanceServiceByGUID(userId,
                                                                   guid,
                                                                   instanceHandler.getSupportedZones(userId,
                                                                                                     serverName,
                                                                                                     serviceURLMarker,
                                                                                                     methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param requestBody qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public  GovernanceServiceElementResponse getGovernanceServiceByName(String          serverName,
                                                                        String          serviceURLMarker,
                                                                        String          userId,
                                                                        NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceServiceByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceServiceElementResponse response = new GovernanceServiceElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            if (requestBody != null)
            {
                GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setElement(handler.getGovernanceServiceByName(userId,
                                                                       requestBody.getName(),
                                                                       instanceHandler.getSupportedZones(userId,
                                                                                                         serverName,
                                                                                                         serviceURLMarker,
                                                                                                         methodName)));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NameRequestBody.class.getName());
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
     * Return the list of governance services definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                      String  serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllGovernanceServices(userId,
                                                                  startingFrom,
                                                                  maximumResults,
                                                                  instanceHandler.getSupportedZones(userId,
                                                                                                    serverName,
                                                                                                    serviceURLMarker,
                                                                                                    methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param governanceServiceGUID governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the governance engine definition.
     */
    public GUIDListResponse getGovernanceServiceRegistrations(String   serverName,
                                                              String   serviceURLMarker,
                                                              String   userId,
                                                              String   governanceServiceGUID)
    {
        final String methodName = "getGovernanceServiceRegistrations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getGovernanceServiceRegistrations(userId,
                                                                        governanceServiceGUID,
                                                                        instanceHandler.getSupportedZones(userId,
                                                                                                          serverName,
                                                                                                          serviceURLMarker,
                                                                                                          methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                    String                             serviceURLMarker,
                                                    String                             userId,
                                                    String                             guid,
                                                    UpdateGovernanceServiceRequestBody requestBody)
    {
        final String methodName = "updateGovernanceService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse        response             = new VoidResponse();
        AuditLog            auditLog             = null;

        try
        {
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateGovernanceService(userId,
                                                guid,
                                                requestBody.getQualifiedName(),
                                                requestBody.getName(),
                                                requestBody.getVersionIdentifier(),
                                                requestBody.getDescription(),
                                                requestBody.getDeployedImplementationType(),
                                                requestBody.getConnection(),
                                                requestBody.getAdditionalProperties(),
                                                requestBody.getExtendedProperties(),
                                                instanceHandler.getSupportedZones(userId,
                                                                                  serverName,
                                                                                  serviceURLMarker,
                                                                                  methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                String            serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteGovernanceService(userId,
                                                guid,
                                                requestBody.getQualifiedName(),
                                                instanceHandler.getSupportedZones(userId,
                                                                                  serverName,
                                                                                  serviceURLMarker,
                                                                                  methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                            String                                   serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.registerGovernanceServiceWithEngine(userId,
                                                            governanceEngineGUID,
                                                            requestBody.getGovernanceServiceGUID(),
                                                            requestBody.getRequestType(),
                                                            requestBody.getServiceRequestType(),
                                                            requestBody.getRequestParameters(),
                                                            instanceHandler.getSupportedZones(userId,
                                                                                              serverName,
                                                                                              serviceURLMarker,
                                                                                              methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                              String serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setRegisteredGovernanceService(handler.getRegisteredGovernanceService(userId,
                                                                                           governanceEngineGUID,
                                                                                           governanceServiceGUID,
                                                                                           instanceHandler.getSupportedZones(userId,
                                                                                                                             serverName,
                                                                                                                             serviceURLMarker,
                                                                                                                             methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                                 String  serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getRegisteredGovernanceServices(userId,
                                                                         governanceEngineGUID,
                                                                         startingFrom,
                                                                         maximumResults,
                                                                         instanceHandler.getSupportedZones(userId,
                                                                                                           serverName,
                                                                                                           serviceURLMarker,
                                                                                                           methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                     String          serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.unregisterGovernanceServiceRequestFromEngine(userId,
                                                                 requestType,
                                                                 governanceEngineGUID,
                                                                 governanceServiceGUID,
                                                                 instanceHandler.getSupportedZones(userId,
                                                                                                   serverName,
                                                                                                   serviceURLMarker,
                                                                                                   methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                              String          serviceURLMarker,
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
            GovernanceEngineConfigurationHandler handler = instanceHandler.getGovernanceEngineConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.unregisterGovernanceServiceFromEngine(userId,
                                                          governanceEngineGUID,
                                                          governanceServiceGUID,
                                                          instanceHandler.getSupportedZones(userId,
                                                                                            serverName,
                                                                                            serviceURLMarker,
                                                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /*
     * Integration connectors
     */




    /**
     * Create a new integration group definition.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                               String                     serviceURLMarker,
                                               String                     userId,
                                               IntegrationGroupProperties requestBody)
    {
        final String        methodName = "createIntegrationGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response      = new GUIDResponse();
        AuditLog     auditLog      = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createIntegrationGroup(userId,
                                                                requestBody,
                                                                instanceHandler.getSupportedZones(userId,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration group definition.
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public IntegrationGroupElementResponse getIntegrationGroupByGUID(String    serverName,
                                                                     String    serviceURLMarker,
                                                                     String    userId,
                                                                     String    guid)
    {
        final String        methodName = "getIntegrationGroupByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupElementResponse response = new IntegrationGroupElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationGroupByGUID(userId,
                                                                  guid,
                                                                  instanceHandler.getSupportedZones(userId,
                                                                                                    serverName,
                                                                                                    serviceURLMarker,
                                                                                                    methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public  IntegrationGroupElementResponse getIntegrationGroupByName(String    serverName,
                                                                      String    serviceURLMarker,
                                                                      String    userId,
                                                                      String    name)
    {
        final String        methodName = "getIntegrationGroupByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupElementResponse response = new IntegrationGroupElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationGroupByName(userId,
                                                                  name,
                                                                  instanceHandler.getSupportedZones(userId,
                                                                                                    serverName,
                                                                                                    serviceURLMarker,
                                                                                                    methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                    String  serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllIntegrationGroups(userId,
                                                                 startingFrom,
                                                                 maximumResults,
                                                                 instanceHandler.getSupportedZones(userId,
                                                                                                   serverName,
                                                                                                   serviceURLMarker,
                                                                                                   methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                               String                     serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateIntegrationGroup(userId,
                                               guid,
                                               isMergeUpdate,
                                               requestBody,
                                               instanceHandler.getSupportedZones(userId,
                                                                                 serverName,
                                                                                 serviceURLMarker,
                                                                                 methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                   String             serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteIntegrationGroup(userId,
                                               guid,
                                               requestBody.getQualifiedName(),
                                               instanceHandler.getSupportedZones(userId,
                                                                                 serverName,
                                                                                 serviceURLMarker,
                                                                                 methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                     String                         serviceURLMarker,
                                                     String                         userId,
                                                     IntegrationConnectorProperties requestBody)
    {
        final String        methodName = "createIntegrationConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response      = new GUIDResponse();
        AuditLog auditLog          = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createIntegrationConnector(userId,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getVersionIdentifier(),
                                                                    requestBody.getName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getDeployedImplementationType(),
                                                                    requestBody.getUsesBlockingCalls(),
                                                                    requestBody.getAdditionalProperties(),
                                                                    requestBody.getConnection(),
                                                                    instanceHandler.getSupportedZones(userId,
                                                                                                      serverName,
                                                                                                      serviceURLMarker,
                                                                                                      methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the integration connector definition.
     *
     * @return properties of the integration connector or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public IntegrationConnectorElementResponse getIntegrationConnectorByGUID(String    serverName,
                                                                             String    serviceURLMarker,
                                                                             String    userId,
                                                                             String    guid)
    {
        final String        methodName = "getIntegrationConnectorByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationConnectorElementResponse response = new IntegrationConnectorElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationConnectorByGUID(userId,
                                                                      guid,
                                                                      instanceHandler.getSupportedZones(userId,
                                                                                                        serverName,
                                                                                                        serviceURLMarker,
                                                                                                        methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public  IntegrationConnectorElementResponse getIntegrationConnectorByName(String    serverName,
                                                                              String    serviceURLMarker,
                                                                              String    userId,
                                                                              String    name)
    {
        final String methodName = "getIntegrationConnectorByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationConnectorElementResponse response = new IntegrationConnectorElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getIntegrationConnectorByName(userId,
                                                                      name,
                                                                      instanceHandler.getSupportedZones(userId,
                                                                                                        serverName,
                                                                                                        serviceURLMarker,
                                                                                                        methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                            String  serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getAllIntegrationConnectors(userId,
                                                                     startingFrom,
                                                                     maximumResults,
                                                                     instanceHandler.getSupportedZones(userId,
                                                                                                       serverName,
                                                                                                       serviceURLMarker,
                                                                                                       methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration group definition.
     */
    public GUIDListResponse getIntegrationConnectorRegistrations(String   serverName,
                                                                 String   serviceURLMarker,
                                                                 String   userId,
                                                                 String   integrationConnectorGUID)
    {
        final String methodName = "getIntegrationConnectorRegistrations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse response = new GUIDListResponse();
        AuditLog         auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getIntegrationConnectorRegistrations(userId,
                                                                           integrationConnectorGUID,
                                                                           instanceHandler.getSupportedZones(userId,
                                                                                                             serverName,
                                                                                                             serviceURLMarker,
                                                                                                             methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                       String                          serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateIntegrationConnector(userId,
                                                   guid,
                                                   isMergeUpdate,
                                                   requestBody.getQualifiedName(),
                                                   requestBody.getVersionIdentifier(),
                                                   requestBody.getName(),
                                                   requestBody.getDescription(),
                                                   requestBody.getDeployedImplementationType(),
                                                   requestBody.getConnection(),
                                                   requestBody.getAdditionalProperties(),
                                                   null,
                                                   instanceHandler.getSupportedZones(userId,
                                                                                     serverName,
                                                                                     serviceURLMarker,
                                                                                     methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                   String            serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteIntegrationConnector(userId,
                                                   guid,
                                                   requestBody.getQualifiedName(),
                                                   instanceHandler.getSupportedZones(userId,
                                                                                     serverName,
                                                                                     serviceURLMarker,
                                                                                     methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                              String                                   serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

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
                                                              requestBody.getPermittedSynchronization(),
                                                              instanceHandler.getSupportedZones(userId,
                                                                                                serverName,
                                                                                                serviceURLMarker,
                                                                                                methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                                    String serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setRegisteredIntegrationConnector(handler.getRegisteredIntegrationConnector(userId,
                                                                                                 integrationGroupGUID,
                                                                                                 integrationConnectorGUID,
                                                                                                 instanceHandler.getSupportedZones(userId,
                                                                                                                                   serverName,
                                                                                                                                   serviceURLMarker,
                                                                                                                                   methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                                       String  serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getRegisteredIntegrationConnectors(userId,
                                                                            integrationGroupGUID,
                                                                            startingFrom,
                                                                            maximumResults,
                                                                            instanceHandler.getSupportedZones(userId,
                                                                                                              serverName,
                                                                                                              serviceURLMarker,
                                                                                                              methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                                String          serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.unregisterIntegrationConnectorFromGroup(userId,
                                                            integrationGroupGUID,
                                                            integrationConnectorGUID,
                                                            instanceHandler.getSupportedZones(userId,
                                                                                              serverName,
                                                                                              serviceURLMarker,
                                                                                              methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return guid or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public GUIDResponse addCatalogTarget(String                  serverName,
                                         String                  serviceURLMarker,
                                         String                  userId,
                                         String                  integrationConnectorGUID,
                                         String                  metadataElementGUID,
                                         CatalogTargetProperties requestBody)
    {
        final String methodName = "addCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.addCatalogTarget(userId,
                                                          integrationConnectorGUID,
                                                          metadataElementGUID,
                                                          requestBody,
                                                          instanceHandler.getSupportedZones(userId,
                                                                                            serverName,
                                                                                            serviceURLMarker,
                                                                                            methodName)));
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
     * Update a catalog target for an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the integration service.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public VoidResponse updateCatalogTarget(String                  serverName,
                                            String                  serviceURLMarker,
                                            String                  userId,
                                            String                  relationshipGUID,
                                            CatalogTargetProperties requestBody)
    {
        final String methodName = "updateCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateCatalogTarget(userId,
                                            relationshipGUID,
                                            requestBody,
                                            instanceHandler.getSupportedZones(userId,
                                                                              serverName,
                                                                              serviceURLMarker,
                                                                              methodName));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     *
     * @return details of the integration connector and the elements it is to catalog or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the integration connector definition.
     */
    public CatalogTargetResponse getCatalogTarget(String serverName,
                                                  String serviceURLMarker,
                                                  String userId,
                                                  String relationshipGUID)
    {
        final String        methodName = "getCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CatalogTargetResponse response = new CatalogTargetResponse();
        AuditLog                            auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getCatalogTarget(userId,
                                                         relationshipGUID,
                                                         instanceHandler.getSupportedZones(userId,
                                                                                           serverName,
                                                                                           serviceURLMarker,
                                                                                           methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                                     String  serviceURLMarker,
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
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName,methodName);
            response.setElements(handler.getCatalogTargets(userId,
                                                           integrationConnectorGUID,
                                                           startingFrom,
                                                           maximumResults,
                                                           instanceHandler.getSupportedZones(userId,
                                                                                             serverName,
                                                                                             serviceURLMarker,
                                                                                             methodName)));
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
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the governance service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving/deleting the integration connector definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeCatalogTarget(String          serverName,
                                            String          serviceURLMarker,
                                            String          userId,
                                            String          relationshipGUID,
                                            NullRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            IntegrationGroupConfigurationHandler handler = instanceHandler.getIntegrationGroupConfigurationHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeCatalogTarget(userId,
                                        relationshipGUID,
                                        instanceHandler.getSupportedZones(userId,
                                                                          serverName,
                                                                          serviceURLMarker,
                                                                          methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
