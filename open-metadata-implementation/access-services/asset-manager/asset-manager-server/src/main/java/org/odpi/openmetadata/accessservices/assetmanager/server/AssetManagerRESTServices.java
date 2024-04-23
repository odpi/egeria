/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataCorrelationHeader;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SynchronizationDirection;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ElementHeadersResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.MetadataCorrelationHeadersResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.UpdateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ExternalIdentifierHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The AssetManagerRESTServices provides the server-side implementation of the services
 * that are generic for all types of asset managers.
 */
public class AssetManagerRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(AssetManagerRESTServices.class),
                                                                                          instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

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
                Date effectiveTime = new Date();

                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId,
                                                                                                                      serverName,
                                                                                                                      methodName);

                String softwareCapabilityGUID = handler.createSoftwareCapability(userId,
                                                                                 null,
                                                                                 null,
                                                                                 OpenMetadataType.CATALOG.typeName,
                                                                                 OpenMetadataType.ASSET_MANAGER_TYPE_NAME,
                                                                                 assetManagerProperties.getQualifiedName(),
                                                                                 assetManagerProperties.getTechnicalName(),
                                                                                 assetManagerProperties.getTechnicalDescription(),
                                                                                 assetManagerProperties.getDeployedImplementationType(),
                                                                                 assetManagerProperties.getVersion(),
                                                                                 assetManagerProperties.getPatchLevel(),
                                                                                 assetManagerProperties.getSource(),
                                                                                 assetManagerProperties.getAdditionalProperties(),
                                                                                 null,
                                                                                 assetManagerProperties.getVendorProperties(),
                                                                                 assetManagerProperties.getEffectiveFrom(),
                                                                                 assetManagerProperties.getEffectiveTo(),
                                                                                 false,
                                                                                 false,
                                                                                 effectiveTime,
                                                                                 methodName);

                final String softwareCapabilityGUIDParameterName = "softwareCapabilityGUID";

                handler.maintainSupplementaryProperties(userId,
                                                        softwareCapabilityGUID,
                                                        softwareCapabilityGUIDParameterName,
                                                        OpenMetadataType.CATALOG.typeName,
                                                        assetManagerProperties.getQualifiedName(),
                                                        assetManagerProperties.getDisplayName(),
                                                        assetManagerProperties.getSummary(),
                                                        assetManagerProperties.getDescription(),
                                                        assetManagerProperties.getAbbreviation(),
                                                        assetManagerProperties.getUsage(),
                                                        false,
                                                        false,
                                                        false,
                                                        effectiveTime,
                                                        methodName);

                response.setGUID(softwareCapabilityGUID);
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
     * Typically, the qualified name comes from the integration connector configuration.
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

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId,
                                                                                                                  serverName,
                                                                                                                  methodName);

            response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                qualifiedName,
                                                                nameParameterName,
                                                                false,
                                                                false,
                                                                new Date(),
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
                                                requestBody.getExternalInstanceCreatedBy(),
                                                requestBody.getExternalInstanceCreationTime(),
                                                requestBody.getExternalInstanceLastUpdatedBy(),
                                                requestBody.getExternalInstanceLastUpdateTime(),
                                                requestBody.getExternalInstanceVersion(),
                                                requestBody.getAssetManagerGUID(),
                                                assetManagerGUIDParameterName,
                                                requestBody.getAssetManagerName(),
                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                permittedSynchronizationOrdinal,
                                                requestBody.getSynchronizationDescription(),
                                                null,
                                                null,
                                                false,
                                                false,
                                                new Date(),
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
                                                requestBody.getExternalInstanceCreatedBy(),
                                                requestBody.getExternalInstanceCreationTime(),
                                                requestBody.getExternalInstanceLastUpdatedBy(),
                                                requestBody.getExternalInstanceLastUpdateTime(),
                                                requestBody.getExternalInstanceVersion(),
                                                requestBody.getAssetManagerGUID(),
                                                assetManagerGUIDParameterName,
                                                requestBody.getAssetManagerName(),
                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                permittedSynchronizationOrdinal,
                                                requestBody.getSynchronizationDescription(),
                                                null,
                                                null,
                                                false,
                                                false,
                                                new Date(),
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
     * That the external identifier matches the open metadata GUID.
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
    public BooleanResponse validateExternalIdentifier(String                        serverName,
                                                      String                        userId,
                                                      String                        openMetadataElementGUID,
                                                      String                        openMetadataElementTypeName,
                                                      MetadataCorrelationProperties requestBody)
    {
        final String methodName                      = "validateExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String assetManagerGUIDParameterName   = "assetManagerGUID";
        final String externalIdentifierParameterName = "requestBody.externalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler =
                        instanceHandler.getExternalIdentifierHandler(userId, serverName, methodName);

                if ((requestBody.getExternalIdentifier() != null) &&
                    (requestBody.getAssetManagerGUID() != null) &&
                    (requestBody.getAssetManagerName() != null))
                {
                    response.setFlag(handler.confirmSynchronization(userId,
                                                                    openMetadataElementGUID,
                                                                    openMetadataGUIDParameterName,
                                                                    openMetadataElementTypeName,
                                                                    requestBody.getExternalIdentifier(),
                                                                    externalIdentifierParameterName,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    assetManagerGUIDParameterName,
                                                                    requestBody.getAssetManagerName(),
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                    false,
                                                                    false,
                                                                    null,
                                                                    methodName) != null);
                }
                else
                {
                    response.setFlag(true);
                }
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse removeExternalIdentifier(String            serverName,
                                                 String            userId,
                                                 String            openMetadataElementGUID,
                                                 String            openMetadataElementTypeName,
                                                 boolean           forLineage,
                                                 boolean           forDuplicateProcessing,
                                                 UpdateRequestBody requestBody)
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

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);
                int permittedSynchronizationOrdinal = SynchronizationDirection.BOTH_DIRECTIONS.getOpenTypeOrdinal();
                if (requestBody.getMetadataCorrelationProperties().getSynchronizationDirection() != null)
                {
                    permittedSynchronizationOrdinal = requestBody.getMetadataCorrelationProperties().getSynchronizationDirection().getOpenTypeOrdinal();
                }

                int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOpenTypeOrdinal();
                if (requestBody.getMetadataCorrelationProperties().getKeyPattern() != null)
                {
                    keyPatternOrdinal = requestBody.getMetadataCorrelationProperties().getKeyPattern().getOpenTypeOrdinal();
                }

                handler.removeExternalIdentifier(userId,
                                                 openMetadataElementGUID,
                                                 openMetadataGUIDParameterName,
                                                 openMetadataElementTypeName,
                                                 requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                 identifierParameterName,
                                                 keyPatternOrdinal,
                                                 requestBody.getMetadataCorrelationProperties().getExternalIdentifierName(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalIdentifierUsage(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalIdentifierSource(),
                                                 requestBody.getMetadataCorrelationProperties().getMappingProperties(),
                                                 requestBody.getMetadataCorrelationProperties().getAssetManagerGUID(),
                                                 assetManagerGUIDParameterName,
                                                 requestBody.getMetadataCorrelationProperties().getAssetManagerName(),
                                                 OpenMetadataType.ASSET_MANAGER_TYPE_NAME,
                                                 permittedSynchronizationOrdinal,
                                                 requestBody.getMetadataCorrelationProperties().getSynchronizationDescription(),
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(),
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                               boolean                       forLineage,
                                               boolean                       forDuplicateProcessing,
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
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               forLineage,
                                               forDuplicateProcessing,
                                               null,
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
     * Typically, the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier and its scope
     *
     * @return list of correlation header elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public MetadataCorrelationHeadersResponse getMetadataCorrelationHeaders(String                        serverName,
                                                                            String                        userId,
                                                                            String                        openMetadataElementGUID,
                                                                            String                        openMetadataElementTypeName,
                                                                            boolean                       forLineage,
                                                                            boolean                       forDuplicateProcessing,
                                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName                    = "getMetadataCorrelationHeaders";
        final String openMetadataGUIDParameterName = "openMetadataElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        MetadataCorrelationHeadersResponse response = new MetadataCorrelationHeadersResponse();
        AuditLog                           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);

                response.setElementList(handler.getExternalIdentifiersForScope(userId,
                                                                               openMetadataElementGUID,
                                                                               openMetadataGUIDParameterName,
                                                                               openMetadataElementTypeName,
                                                                               requestBody.getAssetManagerGUID(),
                                                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                               requestBody.getAssetManagerName(),
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               0,
                                                                               0,
                                                                               requestBody.getEffectiveTime(),
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
     * Typically, the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier
     *
     * @return list of linked elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public ElementHeadersResponse getElementsForExternalIdentifier(String            serverName,
                                                                   String            userId,
                                                                   int               startFrom,
                                                                   int               pageSize,
                                                                   boolean           forLineage,
                                                                   boolean           forDuplicateProcessing,
                                                                   UpdateRequestBody requestBody)
    {
        final String methodName = "getElementsForExternalIdentifier";
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementHeadersResponse response = new ElementHeadersResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);

                response.setElementList(handler.getElementsForExternalIdentifier(userId,
                                                                                 requestBody.getMetadataCorrelationProperties().getAssetManagerGUID(),
                                                                                 assetManagerGUIDParameterName,
                                                                                 OpenMetadataType.ASSET_MANAGER_TYPE_NAME,
                                                                                 requestBody.getMetadataCorrelationProperties().getAssetManagerName(),
                                                                                 requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 requestBody.getEffectiveTime(),
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
