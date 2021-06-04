/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataCorrelationHeader;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareServerCapabilityElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ElementHeadersResponse;
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @return unique identifier of the asset management's software server capability or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createExternalAssetManager(String                 serverName,
                                                   String                 userId,
                                                   AssetManagerProperties assetManagerProperties)
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
                                                                        OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.ASSET_MANAGER_TYPE_NAME,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
                                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse addExternalIdentifier(String                        serverName,
                                              String                        userId,
                                              String                        openMetadataElementGUID,
                                              String                        openMetadataElementTypeName,
                                              MetadataCorrelationProperties requestBody)
    {
        final String methodName                      = "addExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String assetManagerGUIDParameterName   = "assetManagerGUID";
        final String identifierParameterName         = "correlationProperties.externalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);
                int permittedSynchronizationOrdinal = SynchronizationDirection.BOTH_DIRECTIONS.getOpenTypeOrdinal();
                if (requestBody.getSynchronizationDirection() != null)
                {
                    permittedSynchronizationOrdinal = requestBody.getSynchronizationDirection().getOpenTypeOrdinal();
                }

                int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOpenTypeOrdinal();
                if (requestBody.getKeyPattern() != null)
                {
                    keyPatternOrdinal = requestBody.getKeyPattern().getOpenTypeOrdinal();
                }

                handler.setUpExternalIdentifier(userId,
                                                openMetadataElementGUID,
                                                openMetadataGUIDParameterName,
                                                openMetadataElementTypeName,
                                                requestBody.getExternalIdentifier(),
                                                identifierParameterName,
                                                keyPatternOrdinal,
                                                requestBody.getExternalIdentifierName(),
                                                requestBody.getExternalIdentifierUsage(),
                                                requestBody.getExternalIdentifierSource(),
                                                requestBody.getMappingProperties(),
                                                requestBody.getAssetManagerGUID(),
                                                assetManagerGUIDParameterName,
                                                requestBody.getAssetManagerName(),
                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                permittedSynchronizationOrdinal,
                                                requestBody.getSynchronizationDescription(),
                                                methodName);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse updateExternalIdentifier(String                        serverName,
                                                 String                        userId,
                                                 String                        openMetadataElementGUID,
                                                 String                        openMetadataElementTypeName,
                                                 MetadataCorrelationProperties requestBody)
    {
        final String methodName                      = "updateExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String assetManagerGUIDParameterName   = "assetManagerGUID";
        final String identifierParameterName         = "correlationProperties.externalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);
                int permittedSynchronizationOrdinal = SynchronizationDirection.BOTH_DIRECTIONS.getOpenTypeOrdinal();
                if (requestBody.getSynchronizationDirection() != null)
                {
                    permittedSynchronizationOrdinal = requestBody.getSynchronizationDirection().getOpenTypeOrdinal();
                }

                int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOpenTypeOrdinal();
                if (requestBody.getKeyPattern() != null)
                {
                    keyPatternOrdinal = requestBody.getKeyPattern().getOpenTypeOrdinal();
                }

                handler.setUpExternalIdentifier(userId,
                                                openMetadataElementGUID,
                                                openMetadataGUIDParameterName,
                                                openMetadataElementTypeName,
                                                requestBody.getExternalIdentifier(),
                                                identifierParameterName,
                                                keyPatternOrdinal,
                                                requestBody.getExternalIdentifierName(),
                                                requestBody.getExternalIdentifierUsage(),
                                                requestBody.getExternalIdentifierSource(),
                                                requestBody.getMappingProperties(),
                                                requestBody.getAssetManagerGUID(),
                                                assetManagerGUIDParameterName,
                                                requestBody.getAssetManagerName(),
                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                permittedSynchronizationOrdinal,
                                                requestBody.getSynchronizationDescription(),
                                                methodName);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse removeExternalIdentifier(String                        serverName,
                                                 String                        userId,
                                                 String                        openMetadataElementGUID,
                                                 String                        openMetadataElementTypeName,
                                                 MetadataCorrelationProperties requestBody)
    {
        final String methodName                      = "removeExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String assetManagerGUIDParameterName   = "assetManagerGUID";
        final String identifierParameterName         = "correlationProperties.externalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);
                int permittedSynchronizationOrdinal = SynchronizationDirection.BOTH_DIRECTIONS.getOpenTypeOrdinal();
                if (requestBody.getSynchronizationDirection() != null)
                {
                    permittedSynchronizationOrdinal = requestBody.getSynchronizationDirection().getOpenTypeOrdinal();
                }

                int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOpenTypeOrdinal();
                if (requestBody.getKeyPattern() != null)
                {
                    keyPatternOrdinal = requestBody.getKeyPattern().getOpenTypeOrdinal();
                }

                handler.removeExternalIdentifier(userId,
                                                 openMetadataElementGUID,
                                                 openMetadataGUIDParameterName,
                                                 openMetadataElementTypeName,
                                                 requestBody.getExternalIdentifier(),
                                                 identifierParameterName,
                                                 keyPatternOrdinal,
                                                 requestBody.getExternalIdentifierName(),
                                                 requestBody.getExternalIdentifierUsage(),
                                                 requestBody.getExternalIdentifierSource(),
                                                 requestBody.getMappingProperties(),
                                                 requestBody.getAssetManagerGUID(),
                                                 assetManagerGUIDParameterName,
                                                 requestBody.getAssetManagerName(),
                                                 OpenMetadataAPIMapper.ASSET_MANAGER_TYPE_NAME,
                                                 permittedSynchronizationOrdinal,
                                                 requestBody.getSynchronizationDescription(),
                                                 methodName);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse confirmSynchronization(String                        serverName,
                                               String                        userId,
                                               String                        openMetadataElementGUID,
                                               String                        openMetadataElementTypeName,
                                               MetadataCorrelationProperties requestBody)
    {
        final String methodName                           = "confirmSynchronization";
        final String openMetadataElementGUIDParameterName = "openMetadataElementGUID";
        final String externalIdentifierParameterName      = "externalIdentifier";
        final String assetManagerGUIDParameterName        = "assetManagerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);

                handler.confirmSynchronization(userId,
                                               openMetadataElementGUID,
                                               openMetadataElementGUIDParameterName,
                                               openMetadataElementTypeName,
                                               requestBody.getExternalIdentifier(),
                                               externalIdentifierParameterName,
                                               requestBody.getAssetManagerGUID(),
                                               assetManagerGUIDParameterName,
                                               requestBody.getAssetManagerName(),
                                               OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                               methodName);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody details of the external identifier
     *
     * @return list of linked elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public ElementHeadersResponse getElementsForExternalIdentifier(String                        serverName,
                                                                   String                        userId,
                                                                   int                           startFrom,
                                                                   int                           pageSize,
                                                                   MetadataCorrelationProperties requestBody)
    {
        final String methodName = "getElementsForExternalIdentifier";
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementHeadersResponse response = new ElementHeadersResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);

                response.setElementList(handler.getElementsForExternalIdentifier(userId,
                                                                                 requestBody.getAssetManagerGUID(),
                                                                                 assetManagerGUIDParameterName,
                                                                                 OpenMetadataAPIMapper.ASSET_MANAGER_TYPE_NAME,
                                                                                 requestBody.getAssetManagerName(),
                                                                                 requestBody.getExternalIdentifier(),
                                                                                 startFrom,
                                                                                 pageSize,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

}
