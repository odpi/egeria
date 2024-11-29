/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ElementHeadersResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ExternalIdentifierHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The ExternalIdentifierRESTServices provides the server-side implementation of the services
 * that are generic for all types of asset managers.
 */
public class ExternalIdentifierRESTServices
{
    private static final GAFMetadataManagementInstanceHandler instanceHandler = new GAFMetadataManagementInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(ExternalIdentifierRESTServices.class),
                                                                                          instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public ExternalIdentifierRESTServices()
    {
    }

    
    /**
     * Add the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    public VoidResponse addExternalIdentifier(String                               serverName,
                                              String                               serviceURLMarker,
                                              String                               userId,
                                              String                               openMetadataElementGUID,
                                              String                               openMetadataElementTypeName,
                                              boolean                              forLineage,
                                              boolean                              forDuplicateProcessing,
                                              UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "addExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
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
                int permittedSynchronizationOrdinal = PermittedSynchronization.BOTH_DIRECTIONS.getOrdinal();
                if (requestBody.getMetadataCorrelationProperties().getSynchronizationDirection() != null)
                {
                    permittedSynchronizationOrdinal = requestBody.getMetadataCorrelationProperties().getSynchronizationDirection().getOrdinal();
                }

                int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOrdinal();
                if (requestBody.getMetadataCorrelationProperties().getKeyPattern() != null)
                {
                    keyPatternOrdinal = requestBody.getMetadataCorrelationProperties().getKeyPattern().getOrdinal();
                }

                handler.setUpExternalIdentifier(userId,
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
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceCreatedBy(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceCreationTime(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceLastUpdatedBy(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceLastUpdateTime(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceVersion(),
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                externalScopeGUIDParameterName,
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeTypeName(),
                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                permittedSynchronizationOrdinal,
                                                requestBody.getMetadataCorrelationProperties().getSynchronizationDescription(),
                                                instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                requestBody.getEffectiveFrom(),
                                                requestBody.getEffectiveTo(),
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
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    public VoidResponse updateExternalIdentifier(String                               serverName,
                                                 String                               serviceURLMarker,
                                                 String                               userId,
                                                 String                               openMetadataElementGUID,
                                                 String                               openMetadataElementTypeName,
                                                 boolean                              forLineage,
                                                 boolean                              forDuplicateProcessing,
                                                 UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "updateExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
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
                int permittedSynchronizationOrdinal = PermittedSynchronization.BOTH_DIRECTIONS.getOrdinal();
                if (requestBody.getMetadataCorrelationProperties().getSynchronizationDirection() != null)
                {
                    permittedSynchronizationOrdinal = requestBody.getMetadataCorrelationProperties().getSynchronizationDirection().getOrdinal();
                }

                int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOrdinal();
                if (requestBody.getMetadataCorrelationProperties().getKeyPattern() != null)
                {
                    keyPatternOrdinal = requestBody.getMetadataCorrelationProperties().getKeyPattern().getOrdinal();
                }

                handler.setUpExternalIdentifier(userId,
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
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceCreatedBy(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceCreationTime(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceLastUpdatedBy(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceLastUpdateTime(),
                                                requestBody.getMetadataCorrelationProperties().getExternalInstanceVersion(),
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                externalScopeGUIDParameterName,
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                requestBody.getMetadataCorrelationProperties().getExternalScopeTypeName(),
                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                permittedSynchronizationOrdinal,
                                                requestBody.getMetadataCorrelationProperties().getSynchronizationDescription(),
                                                instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                requestBody.getEffectiveFrom(),
                                                requestBody.getEffectiveTo(),
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
     * That the external identifier matches the open metadata GUID.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    public BooleanResponse validateExternalIdentifier(String                               serverName,
                                                      String                               serviceURLMarker,
                                                      String                               userId,
                                                      String                               openMetadataElementGUID,
                                                      String                               openMetadataElementTypeName,
                                                      boolean                              forLineage,
                                                      boolean                              forDuplicateProcessing,
                                                      UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "validateExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
        final String externalIdentifierParameterName = "requestBody.externalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler =
                        instanceHandler.getExternalIdentifierHandler(userId, serverName, methodName);

                if ((requestBody.getMetadataCorrelationProperties().getExternalIdentifier() != null) &&
                    (requestBody.getMetadataCorrelationProperties().getExternalScopeGUID() != null) &&
                    (requestBody.getMetadataCorrelationProperties().getExternalScopeName() != null))
                {
                    response.setFlag(handler.confirmSynchronization(userId,
                                                                    openMetadataElementGUID,
                                                                    openMetadataGUIDParameterName,
                                                                    openMetadataElementTypeName,
                                                                    requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                                    externalIdentifierParameterName,
                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                    externalScopeGUIDParameterName,
                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                    instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
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
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    public VoidResponse removeExternalIdentifier(String                               serverName,
                                                 String                               serviceURLMarker,
                                                 String                               userId,
                                                 String                               openMetadataElementGUID,
                                                 String                               openMetadataElementTypeName,
                                                 boolean                              forLineage,
                                                 boolean                              forDuplicateProcessing,
                                                 UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "removeExternalIdentifier";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String externalScopeGUIDParameterName   = "externalScopeGUID";
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

                handler.removeExternalIdentifier(userId,
                                                 openMetadataElementGUID,
                                                 openMetadataGUIDParameterName,
                                                 openMetadataElementTypeName,
                                                 requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                 identifierParameterName,
                                                 instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                 externalScopeGUIDParameterName,
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                 OpenMetadataType.REFERENCEABLE.typeName,
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
     * Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.
     * The linked open metadata elements are not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param externalScopeGUID unique identifier (GUID) of the scope element in the open metadata ecosystem
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse removeExternalScope(String                   serverName,
                                            String                   serviceURLMarker,
                                            String                   userId,
                                            String                   externalScopeGUID,
                                            boolean                  forLineage,
                                            boolean                  forDuplicateProcessing,
                                            EffectiveTimeRequestBody requestBody)
    {
        final String methodName                      = "removeExternalIdentifier";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";

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

                handler.removeExternalScope(userId,
                                            externalScopeGUID,
                                            externalScopeGUIDParameterName,
                                            instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
                                               String                        serviceURLMarker,
                                               String                        userId,
                                               String                        openMetadataElementGUID,
                                               String                        openMetadataElementTypeName,
                                               MetadataCorrelationProperties requestBody)
    {
        final String methodName                           = "confirmSynchronization";
        final String openMetadataElementGUIDParameterName = "openMetadataElementGUID";
        final String externalIdentifierParameterName      = "externalIdentifier";
        final String externalScopeGUIDParameterName        = "externalScopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                /*
                 * Confirmation occurs using current active elements
                 */
                boolean forLineage = false;
                boolean forDuplicateProcessing = false;
                Date    effectiveTime          = new Date();

                ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> handler = instanceHandler.getExternalIdentifierHandler(userId,
                                                                                                                                           serverName,
                                                                                                                                           methodName);

                handler.confirmSynchronization(userId,
                                               openMetadataElementGUID,
                                               openMetadataElementGUIDParameterName,
                                               openMetadataElementTypeName,
                                               requestBody.getExternalIdentifier(),
                                               externalIdentifierParameterName,
                                               requestBody.getExternalScopeGUID(),
                                               externalScopeGUIDParameterName,
                                               requestBody.getExternalScopeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
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
     * Retrieve the external identifiers for a particular metadata element.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier and its scope
     *
     * @return list of correlation header elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public MetadataCorrelationHeadersResponse getExternalIdentifiers(String                        serverName,
                                                                     String                        serviceURLMarker,
                                                                     String                        userId,
                                                                     String                        openMetadataElementGUID,
                                                                     String                        openMetadataElementTypeName,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
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
                                                                               instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                               requestBody.getExternalScopeGUID(),
                                                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
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


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically, the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    public ElementHeadersResponse getElementsForExternalIdentifier(String                               serverName,
                                                                   String                               serviceURLMarker,
                                                                   String                               userId,
                                                                   int                                  startFrom,
                                                                   int                                  pageSize,
                                                                   boolean                              forLineage,
                                                                   boolean                              forDuplicateProcessing,
                                                                   UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName = "getElementsForExternalIdentifier";
        final String externalScopeGUIDParameterName = "externalScopeGUID";

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

                response.setElementHeaders(handler.getElementsForExternalIdentifier(userId,
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                                    externalScopeGUIDParameterName,
                                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                                                    instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
