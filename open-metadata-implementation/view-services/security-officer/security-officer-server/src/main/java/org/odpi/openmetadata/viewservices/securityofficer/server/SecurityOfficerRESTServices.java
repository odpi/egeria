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
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneHierarchyProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ResourcePermissionsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecretsCollectionSecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.UserAccountProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.AssociatedSecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProfileProperties;



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
     * Return the list of users registered with the platform security connector.
     *
     * @param serverName  name of called server
     * @param platformGUID unique identifier of the platform
     * @param userAccountStatus status of the user - or null for any status
     * @param userAccountType   type of user - or null for any type
     * @return list of matching userIds in the user directory or exceptions that occur when trying to create the connector:
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public NameListResponse getUserList(String            serverName,
                                        String            platformGUID,
                                        UserAccountStatus userAccountStatus,
                                        UserAccountType   userAccountType)
    {
        final String methodName = "getUserList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NameListResponse response = new NameListResponse();
        AuditLog         auditLog = null;

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

                /*
                UserAccountStatus userAccountStatus = null;
                if (status != null)
                {
                    userAccountStatus = UserAccountStatus.valueOf(status);
                }

                UserAccountType userAccountType = null;
                if (type != null)
                {
                    userAccountType = UserAccountType.valueOf(type);
                }*/

                omagServerPlatformConnector.setDelegatingUserId(userId);
                omagServerPlatformConnector.start();
                response.setNames(omagServerPlatformConnector.getUserList(userAccountStatus, userAccountType));
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


    /*
     * =====================================================================================================================
     * Secrets collection relationships
     */

    /**
     * Attach a security access control to the secrets collection that defines it.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param securityAccessControlGUID unique identifier of the security access control
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkResourcePermissions(String                     serverName,
                                                String                     secretsCollectionGUID,
                                                String                     securityAccessControlGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkResourcePermissions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkResourcePermissions(userId, secretsCollectionGUID, securityAccessControlGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ResourcePermissionsProperties properties)
            {
                handler.linkResourcePermissions(userId, secretsCollectionGUID, securityAccessControlGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkResourcePermissions(userId, secretsCollectionGUID, securityAccessControlGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ResourcePermissionsProperties.class.getName(), methodName);
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
     * Detach a security access control from the secrets collection that defined it.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param securityAccessControlGUID unique identifier of the security access control
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachResourcePermissions(String                        serverName,
                                                  String                        secretsCollectionGUID,
                                                  String                        securityAccessControlGUID,
                                                  DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachResourcePermissions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            handler.detachResourcePermissions(userId, secretsCollectionGUID, securityAccessControlGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a security list to the secrets collection that lists it.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param securityListGUID unique identifier of the security list
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkSecretsCollectionSecurityList(String                     serverName,
                                                          String                     secretsCollectionGUID,
                                                          String                     securityListGUID,
                                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSecretsCollectionSecurityList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkSecretsCollectionSecurityList(userId, secretsCollectionGUID, securityListGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SecretsCollectionSecurityListProperties properties)
            {
                handler.linkSecretsCollectionSecurityList(userId, secretsCollectionGUID, securityListGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkSecretsCollectionSecurityList(userId, secretsCollectionGUID, securityListGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SecretsCollectionSecurityListProperties.class.getName(), methodName);
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
     * Detach a security list from the secrets collection that listed it.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param securityListGUID unique identifier of the security list
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachSecretsCollectionSecurityList(String                        serverName,
                                                            String                        secretsCollectionGUID,
                                                            String                        securityListGUID,
                                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSecretsCollectionSecurityList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            handler.detachSecretsCollectionSecurityList(userId, secretsCollectionGUID, securityListGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a user identity to the secrets collection that configures its account.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkUserAccount(String                     serverName,
                                        String                     secretsCollectionGUID,
                                        String                     userIdentityGUID,
                                        NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkUserAccount";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkUserAccount(userId, secretsCollectionGUID, userIdentityGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof UserAccountProperties properties)
            {
                handler.linkUserAccount(userId, secretsCollectionGUID, userIdentityGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkUserAccount(userId, secretsCollectionGUID, userIdentityGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(UserAccountProperties.class.getName(), methodName);
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
     * Detach a user identity from the secrets collection that configured its account.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachUserAccount(String                        serverName,
                                          String                        secretsCollectionGUID,
                                          String                        userIdentityGUID,
                                          DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachUserAccount";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            handler.detachUserAccount(userId, secretsCollectionGUID, userIdentityGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Associated security lists
     *
     * AssociatedSecurityList is a multi-link relationship - the attach always creates a new relationship and returns
     * its unique identifier; update and detach work on that relationship GUID.
     */

    /**
     * Attach a security list to a security access control that uses it.
     *
     * @param serverName name of the server to route the request to
     * @param securityAccessControlGUID unique identifier of the security access control
     * @param securityListGUID unique identifier of the security list
     * @param requestBody properties for the relationship
     *
     * @return unique identifier of the new relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse linkAssociatedSecurityList(String                     serverName,
                                                   String                     securityAccessControlGUID,
                                                   String                     securityListGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAssociatedSecurityList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssociatedSecurityListProperties properties)
                {
                    response.setGUID(handler.linkAssociatedSecurityList(userId, securityAccessControlGUID, securityListGUID, requestBody, properties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkAssociatedSecurityList(userId, securityAccessControlGUID, securityListGUID, requestBody, null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssociatedSecurityListProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Update the properties of an associated security list relationship.
     *
     * @param serverName name of the server to route the request to
     * @param associatedSecurityListRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse updateAssociatedSecurityList(String                        serverName,
                                                     String                        associatedSecurityListRelationshipGUID,
                                                     UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateAssociatedSecurityList";

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
                if (requestBody.getProperties() instanceof AssociatedSecurityListProperties properties)
                {
                    handler.updateAssociatedSecurityList(userId, associatedSecurityListRelationshipGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssociatedSecurityListProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove an associated security list relationship.
     *
     * @param serverName name of the server to route the request to
     * @param associatedSecurityListRelationshipGUID unique identifier of the relationship
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachAssociatedSecurityList(String                        serverName,
                                                     String                        associatedSecurityListRelationshipGUID,
                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAssociatedSecurityList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachAssociatedSecurityList(userId, associatedSecurityListRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Secrets collection profile classification
     */

    /**
     * Classify a secrets collection with a profile of the user accounts that it holds.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setUserAccountProfile(String                       serverName,
                                              String                       secretsCollectionGUID,
                                              NewClassificationRequestBody requestBody)
    {
        final String methodName = "setUserAccountProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setUserAccountProfile(userId, secretsCollectionGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof UserAccountProfileProperties properties)
            {
                handler.setUserAccountProfile(userId, secretsCollectionGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setUserAccountProfile(userId, secretsCollectionGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(UserAccountProfileProperties.class.getName(), methodName);
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
     * Remove the user account profile from a secrets collection.
     *
     * @param serverName name of the server to route the request to
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearUserAccountProfile(String                          serverName,
                                                String                          secretsCollectionGUID,
                                                DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearUserAccountProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getSecretsCollectionHandler(userId, serverName, methodName);

            handler.clearUserAccountProfile(userId, secretsCollectionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Governance zone profile classification
     */

    /**
     * Classify a governance zone with a profile of its membership.
     *
     * @param serverName name of the server to route the request to
     * @param governanceZoneGUID unique identifier of the governance zone
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setZoneMembershipProfile(String                       serverName,
                                                 String                       governanceZoneGUID,
                                                 NewClassificationRequestBody requestBody)
    {
        final String methodName = "setZoneMembershipProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setZoneMembershipProfile(userId, governanceZoneGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ZoneMembershipProfileProperties properties)
            {
                handler.setZoneMembershipProfile(userId, governanceZoneGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setZoneMembershipProfile(userId, governanceZoneGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ZoneMembershipProfileProperties.class.getName(), methodName);
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
     * Remove the zone membership profile from a governance zone.
     *
     * @param serverName name of the server to route the request to
     * @param governanceZoneGUID unique identifier of the governance zone
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearZoneMembershipProfile(String                          serverName,
                                                   String                          governanceZoneGUID,
                                                   DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearZoneMembershipProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.clearZoneMembershipProfile(userId, governanceZoneGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
