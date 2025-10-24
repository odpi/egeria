/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.opengovernance.properties.IntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.IntegrationGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RegisteredIntegrationConnectorProperties;
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
    private static final GAFServicesInstanceHandler instanceHandler = new GAFServicesInstanceHandler();

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /*
     * Integration connectors
     */



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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
