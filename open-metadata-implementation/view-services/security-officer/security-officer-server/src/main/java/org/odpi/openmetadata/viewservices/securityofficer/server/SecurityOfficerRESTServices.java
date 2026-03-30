/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.securityofficer.server;


import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneHierarchyProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The SecurityOfficerRESTServices provides the server-side implementation of the Security Officer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class SecurityOfficerRESTServices extends TokenController
{
    private static final SecurityOfficerInstanceHandler instanceHandler = new SecurityOfficerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SecurityOfficerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SecurityOfficerRESTServices()
    {
    }


    /**
     * Set up a new security access control or update an existing one.
     * This is account is registered with the platform security connector.  The user
     * requires operator permission for the platform unless it is their own security access control they are updating.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param requestBody containing the security access control properties.
     * @return void or exceptions that occur when trying to create the connector:
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse setUserAccount(String                 serverName,
                                       String                 platformGUID,
                                       UserAccountRequestBody requestBody)
    {
        final String methodName = "setUserAccount";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);
                AssetHandler         platformHandler      = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

                OpenMetadataRootElement asset = platformHandler.getAssetByGUID(userId, platformGUID, null);

                Connector connector = connectedAssetClient.getConnectorForAsset(userId, platformGUID, auditLog);

                if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
                {
                    if ((asset != null) && (asset.getProperties() instanceof AssetProperties assetProperties))
                    {
                        omagServerPlatformConnector.setPlatformName(assetProperties.getResourceName());
                    }

                    omagServerPlatformConnector.setDelegatingUserId(userId);
                    omagServerPlatformConnector.start();
                    omagServerPlatformConnector.setUserAccount(requestBody.getUserAccount());
                    omagServerPlatformConnector.disconnect();
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, "<null>");
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return details of a security access control registered with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param accountUserId name of the connector provider class
     * @return security access control bean or exceptions that occur when trying to create the connector:
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public UserAccountResponse getUserAccount(String serverName,
                                              String platformGUID,
                                              String accountUserId)
    {
        final String methodName = "getUserAccount";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        UserAccountResponse response = new UserAccountResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);
            AssetHandler         platformHandler      = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            OpenMetadataRootElement asset = platformHandler.getAssetByGUID(userId, platformGUID, null);

            Connector     connector = connectedAssetClient.getConnectorForAsset(userId, platformGUID, auditLog);

            if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
            {
                if ((asset != null) && (asset.getProperties() instanceof AssetProperties assetProperties))
                {
                    omagServerPlatformConnector.setPlatformName(assetProperties.getResourceName());
                }

                omagServerPlatformConnector.setDelegatingUserId(userId);
                omagServerPlatformConnector.start();
                response.setUserAccount(omagServerPlatformConnector.getUserAccount(accountUserId));
                omagServerPlatformConnector.disconnect();
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Clear the account for a user with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param accountUserId name of the connector provider class
     * @return void or exceptions that occur when trying to create the connector:
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteUserAccount(String serverName,
                                          String platformGUID,
                                          String accountUserId)
    {
        final String methodName = "deleteUserAccount";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);
            AssetHandler         platformHandler      = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            OpenMetadataRootElement asset = platformHandler.getAssetByGUID(userId, platformGUID, null);

            Connector     connector = connectedAssetClient.getConnectorForAsset(userId, platformGUID, auditLog);

            if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
            {
                if ((asset != null) && (asset.getProperties() instanceof AssetProperties assetProperties))
                {
                    omagServerPlatformConnector.setPlatformName(assetProperties.getResourceName());
                }

                omagServerPlatformConnector.setDelegatingUserId(userId);
                omagServerPlatformConnector.start();
                omagServerPlatformConnector.deleteUserAccount(accountUserId);
                omagServerPlatformConnector.disconnect();
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Set up a new security access control or update an existing one.
     * This is account is registered with the platform security connector.  The user
     * requires operator permission for the platform.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param requestBody containing the security access control properties.
     * @return void or exceptions that occur when trying to create the connector:
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse setSecurityAccessControl(String                           serverName,
                                                 String                           platformGUID,
                                                 SecurityAccessControlRequestBody requestBody)
    {
        final String methodName = "setSecurityAccessControl";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);
                AssetHandler         platformHandler      = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

                OpenMetadataRootElement asset = platformHandler.getAssetByGUID(userId, platformGUID, null);

                Connector connector = connectedAssetClient.getConnectorForAsset(userId, platformGUID, auditLog);

                if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
                {
                    if ((asset != null) && (asset.getProperties() instanceof AssetProperties assetProperties))
                    {
                        omagServerPlatformConnector.setPlatformName(assetProperties.getResourceName());
                    }

                    omagServerPlatformConnector.setDelegatingUserId(userId);
                    omagServerPlatformConnector.start();
                    omagServerPlatformConnector.setSecurityAccessControl(requestBody.getSecurityAccessControl());
                    omagServerPlatformConnector.disconnect();
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, "<null>");
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return details of a security access control registered with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param controlName name of the connector provider class
     * @return security access control bean or exceptions that occur when trying to create the connector:
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SecurityAccessControlResponse getSecurityAccessControl(String serverName,
                                                                  String platformGUID,
                                                                  String controlName)
    {
        final String methodName = "getSecurityAccessControl";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SecurityAccessControlResponse response = new SecurityAccessControlResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);
            AssetHandler         platformHandler      = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            OpenMetadataRootElement asset = platformHandler.getAssetByGUID(userId, platformGUID, null);

            Connector     connector = connectedAssetClient.getConnectorForAsset(userId, platformGUID, auditLog);

            if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
            {
                if ((asset != null) && (asset.getProperties() instanceof AssetProperties assetProperties))
                {
                    omagServerPlatformConnector.setPlatformName(assetProperties.getResourceName());
                }

                omagServerPlatformConnector.setDelegatingUserId(userId);
                omagServerPlatformConnector.start();
                response.setSecurityAccessControl(omagServerPlatformConnector.getSecurityAccessControl(controlName));
                omagServerPlatformConnector.disconnect();
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Clear the account for a user with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param controlName name of the control
     * @return void or exceptions that occur when trying to create the connector:
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSecurityAccessControl(String serverName,
                                                    String platformGUID,
                                                    String controlName)
    {
        final String methodName = "deleteSecurityAccessControl";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);
            AssetHandler         platformHandler      = instanceHandler.getSoftwarePlatformHandler(userId, serverName, methodName);

            OpenMetadataRootElement asset = platformHandler.getAssetByGUID(userId, platformGUID, null);

            Connector     connector = connectedAssetClient.getConnectorForAsset(userId, platformGUID, auditLog);

            if (connector instanceof OMAGServerPlatformConnector omagServerPlatformConnector)
            {
                if ((asset != null) && (asset.getProperties() instanceof AssetProperties assetProperties))
                {
                    omagServerPlatformConnector.setPlatformName(assetProperties.getResourceName());
                }

                omagServerPlatformConnector.setDelegatingUserId(userId);
                omagServerPlatformConnector.start();
                omagServerPlatformConnector.deleteSecurityAccessControl(controlName);
                omagServerPlatformConnector.disconnect();
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Attach governance zones in a hierarchy.
     *
     * @param serverName         name of called server
     * @param governanceZoneGUID    unique identifier of the parent governance zone.
     * @param nestedGovernanceZoneGUID    unique identifier of the nested governance zone.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkGovernanceZones(String                     serverName,
                                            String                     governanceZoneGUID,
                                            String                     nestedGovernanceZoneGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkGovernanceZones";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ZoneHierarchyProperties zoneHierarchyProperties)
                {
                    handler.linkGovernanceZones(userId,
                                                governanceZoneGUID,
                                                nestedGovernanceZoneGUID,
                                                requestBody,
                                                zoneHierarchyProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkGovernanceZones(userId,
                                                governanceZoneGUID,
                                                nestedGovernanceZoneGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ZoneHierarchyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkGovernanceZones(userId,
                                         governanceZoneGUID,
                                         nestedGovernanceZoneGUID,
                                         null,
                                         null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach governance zone from a hierarchical relationship.
     *
     * @param serverName         name of called server
     * @param governanceZoneGUID    unique identifier of the parent governance zone.
     * @param nestedGovernanceZoneGUID    unique identifier of the nested governance zone.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachGovernanceZones(String                        serverName,
                                              String                        governanceZoneGUID,
                                              String                        nestedGovernanceZoneGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachGovernanceZones";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachGovernanceZones(userId, governanceZoneGUID, nestedGovernanceZoneGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

}
