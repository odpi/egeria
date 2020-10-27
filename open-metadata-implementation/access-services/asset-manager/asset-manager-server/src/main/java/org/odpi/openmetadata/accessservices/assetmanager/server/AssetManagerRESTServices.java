/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareServerCapabilityElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ExternalIdentifierHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareServerCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * The AssetManagerRESTServices provides the server-side implementation of the services
 * that are generic for all types of asset managers.
 */
public class AssetManagerRESTServices
{
    private static AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();

    private static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(AssetManagerRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public AssetManagerRESTServices()
    {
    }


    /**
     * Return the client side connection object for the Asset Manager OMAS's out topic.
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
        final String methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create information about the external asset manager.  This is represented as a software server capability
     * and all information that is specific to the external asset manager (such as the identifiers of the
     * metadata elements it stores) will be linked to it.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetManagerProperties description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the asset management's software server capability
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createExternalAssetManager(String                 serverName,
                                                   String                 userId,
                                                   AssetManagerProperties assetManagerProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "createExternalAssetManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (assetManagerProperties != null)
            {
                SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                response.setGUID(handler.createSoftwareServerCapability(userId,
                                                                        null,
                                                                        null,
                                                                        OpenMetadataAPIMapper.ASSET_MANAGER_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.ASSET_MANAGER_TYPE_NAME,
                                                                        null,
                                                                        assetManagerProperties.getQualifiedName(),
                                                                        assetManagerProperties.getDisplayName(),
                                                                        assetManagerProperties.getDescription(),
                                                                        assetManagerProperties.getTypeDescription(),
                                                                        assetManagerProperties.getVersion(),
                                                                        assetManagerProperties.getPatchLevel(),
                                                                        assetManagerProperties.getSource(),
                                                                        assetManagerProperties.getAdditionalProperties(),
                                                                        assetManagerProperties.getVendorProperties(),
                                                                        methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param qualifiedName unique name to use for the external asset
     *
     * @return unique identifier of the external asset manager's software server capability or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse  getExternalAssetManagerGUID(String serverName,
                                                     String userId,
                                                     String qualifiedName)
    {
        final String methodName = "getExternalAssetManagerGUID";
        final String nameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);

            response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                OpenMetadataAPIMapper.ASSET_MANAGER_TYPE_GUID,
                                                                OpenMetadataAPIMapper.ASSET_MANAGER_TYPE_NAME,
                                                                qualifiedName,
                                                                nameParameterName,
                                                                methodName));

        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param keyPattern style of the external identifier
     * @param description description of the identifier
     * @param mappingProperties additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse updateExternalIdentifier(String              serverName,
                                                 String              userId,
                                                 String              assetManagerGUID,
                                                 String              assetManagerName,
                                                 String              openMetadataGUID,
                                                 String              externalIdentifier,
                                                 KeyPattern          keyPattern,
                                                 String              description,
                                                 Map<String, String> mappingProperties)
    {
        final String methodName                      = "updateExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataGUID";
        final String externalIdentifierParameterName = "externalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdentifierHandler<MetadataCorrelationProperties> handler = instanceHandler.getExternalIdentifierHandler(userId, serverName, methodName);

            handler.updateExternalIdentifier(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             openMetadataGUID,
                                             openMetadataGUIDParameterName,
                                             externalIdentifier,
                                             externalIdentifierParameterName,
                                             keyPattern.getOpenTypeOrdinal(),
                                             description,
                                             mappingProperties,
                                             methodName);

        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit points of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse confirmSynchronization(String serverName,
                                               String userId,
                                               String assetManagerGUID,
                                               String assetManagerName,
                                               String openMetadataGUID,
                                               String externalIdentifier)
    {
        final String methodName                      = "confirmSynchronization";
        final String openMetadataGUIDParameterName   = "openMetadataGUID";
        final String externalIdentifierParameterName = "externalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ExternalIdentifierHandler<MetadataCorrelationProperties> handler = instanceHandler.getExternalIdentifierHandler(userId, serverName, methodName);

            handler.confirmSynchronization(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           openMetadataGUID,
                                           openMetadataGUIDParameterName,
                                           externalIdentifier,
                                           externalIdentifierParameterName,
                                           methodName);

        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
