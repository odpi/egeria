/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworkservices.gaf.converters.RelatedElementsConverter;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenMetadataStoreAuditCode;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.MetadataElementHandler;
import org.odpi.openmetadata.frameworks.governanceaction.properties.TranslationDetail;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ValidMetadataValueDetail;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.ValidValuesHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The OpenMetadataStoreRESTServices provides the server-side implementation of the services used by the governance
 * engine as it is managing requests to execute open governance services in the governance server.
 * These services align with the interface definitions from the Governance Action Framework (GAF).
 */
public class OpenMetadataStoreRESTServices
{
    private final static GAFMetadataManagementInstanceHandler instanceHandler = new GAFMetadataManagementInstanceHandler();

    private final        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OpenMetadataStoreRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final String propertyNameParameter = "propertyName";
    private final String preferredValueParameter = "preferredValue";
    private final String actualValueParameter = "actualValue";

    /**
     * Default constructor
     */
    public OpenMetadataStoreRESTServices()
    {
    }


    /**
     * Log an audit message about this asset.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     * @param governanceService name of governance service
     * @param message        message to log
     *                       
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public VoidResponse logAssetAuditMessage(String serverName,
                                             String serviceURLMarker,
                                             String userId,
                                             String assetGUID,
                                             String governanceService,
                                             String message)
    {
        final String   methodName = "logAssetAuditMessage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            this.getMetadataElementByGUID(serverName, serviceURLMarker, userId, assetGUID, false, false, new Date().getTime());

            auditLog.logMessage(methodName, OpenMetadataStoreAuditCode.ASSET_AUDIT_LOG.getMessageDefinition(assetGUID, governanceService, message));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * The effectiveTime is sent as a long and needs to be converted into a date.
     *
     * @param effectiveTime milliseconds since epoch or 0 to mean null (any time)
     * @return date object or null
     */
    private Date getEffectiveTimeFromLong(long effectiveTime)
    {
        Date result = null;

        if (effectiveTime != 0)
        {
            result = new Date(effectiveTime);
        }

        return result;
    }



    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     * 
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByGUID(String  serverName,
                                                                String  serviceURLMarker,
                                                                String  userId,
                                                                String  elementGUID,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                long    effectiveTime)
    {
        final String methodName = "getMetadataElementByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementResponse response = new OpenMetadataElementResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);


            response.setElement(handler.getMetadataElementByGUID(userId,
                                                                 elementGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                 this.getEffectiveTimeFromLong(effectiveTime),
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
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByUniqueName(String          serverName,
                                                                      String          serviceURLMarker,
                                                                      String          userId,
                                                                      boolean         forLineage,
                                                                      boolean         forDuplicateProcessing,
                                                                      long            effectiveTime,
                                                                      NameRequestBody requestBody)
    {
        final String methodName = "getMetadataElementByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementResponse response = new OpenMetadataElementResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                response.setElement(handler.getMetadataElementByUniqueName(userId,
                                                                           requestBody.getName(),
                                                                           requestBody.getNameParameterName(),
                                                                           requestBody.getNamePropertyName(),
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                           this.getEffectiveTimeFromLong(effectiveTime),
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
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public GUIDResponse getMetadataElementGUIDByUniqueName(String          serverName,
                                                           String          serviceURLMarker,
                                                           String          userId,
                                                           boolean         forLineage,
                                                           boolean         forDuplicateProcessing,
                                                           long            effectiveTime,
                                                           NameRequestBody requestBody)
    {
        final String methodName = "getMetadataElementGUIDByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                response.setGUID(handler.getMetadataElementGUIDByUniqueName(userId,
                                                                            requestBody.getName(),
                                                                            requestBody.getNameParameterName(),
                                                                            requestBody.getNamePropertyName(),
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                            this.getEffectiveTimeFromLong(effectiveTime),
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
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse findMetadataElementsWithString(String                  serverName,
                                                                       String                  serviceURLMarker,
                                                                       String                  userId,
                                                                       boolean                 forLineage,
                                                                       boolean                 forDuplicateProcessing,
                                                                       long                    effectiveTime,
                                                                       int                     startFrom,
                                                                       int                     pageSize,
                                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "findMetadataElementsWithString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                response.setElementList(handler.findMetadataElementsWithString(userId,
                                                                               requestBody.getSearchString(),
                                                                               requestBody.getTypeName(),
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                               this.getEffectiveTimeFromLong(effectiveTime),
                                                                               startFrom,
                                                                               pageSize,
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
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public RelatedMetadataElementListResponse getRelatedMetadataElements(String  serverName,
                                                                         String  serviceURLMarker,
                                                                         String  userId,
                                                                         String  elementGUID,
                                                                         String  relationshipTypeName,
                                                                         boolean forLineage,
                                                                         boolean forDuplicateProcessing,
                                                                         long    effectiveTime,
                                                                         int     startingAtEnd,
                                                                         int     startFrom,
                                                                         int     pageSize)
    {
        final String methodName = "getRelatedMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog                           auditLog = null;
        RelatedMetadataElementListResponse response = new RelatedMetadataElementListResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getRelatedMetadataElements(userId,
                                                                       elementGUID,
                                                                       startingAtEnd,
                                                                       relationshipTypeName,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                       this.getEffectiveTimeFromLong(effectiveTime),
                                                                       startFrom,
                                                                       pageSize,
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
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse findMetadataElements(String          serverName,
                                                             String          serviceURLMarker,
                                                             String          userId,
                                                             boolean         forLineage,
                                                             boolean         forDuplicateProcessing,
                                                             long            effectiveTime,
                                                             int             startFrom,
                                                             int             pageSize,
                                                             FindRequestBody requestBody)
    {
        final String methodName = "findMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                response.setElementList(handler.findMetadataElements(userId,
                                                                     requestBody.getMetadataElementTypeName(),
                                                                     requestBody.getMetadataElementSubtypeName(),
                                                                     requestBody.getSearchProperties(),
                                                                     requestBody.getLimitResultsByStatus(),
                                                                     requestBody.getMatchClassifications(),
                                                                     requestBody.getAsOfTime(),
                                                                     requestBody.getSequencingProperty(),
                                                                     requestBody.getSequencingOrder(),
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                     this.getEffectiveTimeFromLong(effectiveTime),
                                                                     startFrom,
                                                                     pageSize,
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
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public RelatedMetadataElementsListResponse findRelationshipsBetweenMetadataElements(String          serverName,
                                                                                        String          serviceURLMarker,
                                                                                        String          userId,
                                                                                        boolean         forLineage,
                                                                                        boolean         forDuplicateProcessing,
                                                                                        long            effectiveTime,
                                                                                        int             startFrom,
                                                                                        int             pageSize,
                                                                                        FindRequestBody requestBody)
    {
        final String methodName = "findRelationshipsBetweenMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog                            auditLog = null;
        RelatedMetadataElementsListResponse response = new RelatedMetadataElementsListResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                response.setElementList(handler.findRelationshipsBetweenMetadataElements(userId,
                                                                                         requestBody.getMetadataElementTypeName(),
                                                                                         requestBody.getSearchProperties(),
                                                                                         requestBody.getLimitResultsByStatus(),
                                                                                         requestBody.getAsOfTime(),
                                                                                         requestBody.getSequencingProperty(),
                                                                                         requestBody.getSequencingOrder(),
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                         this.getEffectiveTimeFromLong(effectiveTime),
                                                                                         startFrom,
                                                                                         pageSize,
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
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param relationshipGUID unique identifier for the relationship
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public RelatedMetadataElementsResponse getRelationshipByGUID(String  serverName,
                                                                 String  serviceURLMarker,
                                                                 String  userId,
                                                                 String  relationshipGUID,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 long    effectiveTime)
    {
        final String methodName = "getRelationshipByGUID";
        final String guidParameterName = "relationshipGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        RelatedMetadataElementsResponse response = new RelatedMetadataElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

            Relationship relationship = handler.getAttachmentLink(userId,
                                                                 relationshipGUID,
                                                                 guidParameterName,
                                                                 null,
                                                                 this.getEffectiveTimeFromLong(effectiveTime),
                                                                 methodName);

            if (relationship != null)
            {
                RelatedElementsConverter<RelatedMetadataElements> converter = new RelatedElementsConverter<>(handler.getRepositoryHelper(),
                                                                                                             handler.getServiceName(),
                                                                                                             serverName);
                response.setElement(converter.getNewRelationshipBean(RelatedMetadataElements.class,
                                                                     relationship,
                                                                     methodName));
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
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse createMetadataElementInStore(String                        serverName,
                                                     String                        serviceURLMarker,
                                                     String                        userId,
                                                     NewMetadataElementRequestBody requestBody)
    {
        final String methodName = "createMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                response.setGUID(handler.createMetadataElementInStore(userId,
                                                                      requestBody.getExternalSourceGUID(),
                                                                      requestBody.getExternalSourceName(),
                                                                      requestBody.getTypeName(),
                                                                      requestBody.getInitialStatus(),
                                                                      requestBody.getEffectiveFrom(),
                                                                      requestBody.getEffectiveTo(),
                                                                      requestBody.getProperties(),
                                                                      requestBody.getTemplateGUID(),
                                                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                      requestBody.getEffectiveTime(),
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
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new properties
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the properties are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse updateMetadataElementInStore(String                      serverName,
                                                     String                      serviceURLMarker,
                                                     String                      userId,
                                                     String                      metadataElementGUID,
                                                     UpdatePropertiesRequestBody requestBody)
    {
        final String methodName = "updateMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.updateMetadataElementInStore(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     metadataElementGUID,
                                                     requestBody.getReplaceProperties(),
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getProperties(),
                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                     requestBody.getEffectiveTime(),
                                                     methodName);
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
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse updateMetadataElementStatusInStore(String                  serverName,
                                                           String                  serviceURLMarker,
                                                           String                  userId,
                                                           String                  metadataElementGUID,
                                                           UpdateStatusRequestBody requestBody)
    {
        final String methodName = "updateMetadataElementStatusInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.updateMetadataElementStatusInStore(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           metadataElementGUID,
                                                           requestBody.getNewStatus(),
                                                           requestBody.getForLineage(),
                                                           requestBody.getForDuplicateProcessing(),
                                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
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
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse updateMetadataElementEffectivityInStore(String                            serverName,
                                                                String                            serviceURLMarker,
                                                                String                            userId,
                                                                String                            metadataElementGUID,
                                                                UpdateEffectivityDatesRequestBody requestBody)
    {
        final String methodName = "updateMetadataElementEffectivityInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.updateMetadataElementEffectivityInStore(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                metadataElementGUID,
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveFrom(),
                                                                requestBody.getEffectiveTo(),
                                                                instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                requestBody.getEffectiveTime(),
                                                                methodName);
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
     * Delete a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @SuppressWarnings(value = "unused")
    public  VoidResponse deleteMetadataElementInStore(String            serverName,
                                                      String            serviceURLMarker,
                                                      String            userId,
                                                      String            metadataElementGUID,
                                                      UpdateRequestBody requestBody)
    {
        final String methodName = "deleteMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.deleteMetadataElementInStore(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     metadataElementGUID,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                     requestBody.getEffectiveTime(),
                                                     methodName);
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
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param requestBody properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse classifyMetadataElementInStore(String                       serverName,
                                                       String                       serviceURLMarker,
                                                       String                       userId,
                                                       String                       metadataElementGUID,
                                                       String                       classificationName,
                                                       NewClassificationRequestBody requestBody)
    {
        final String methodName = "classifyMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.classifyMetadataElementInStore(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       metadataElementGUID,
                                                       classificationName,
                                                       requestBody.getForLineage(),
                                                       requestBody.getForDuplicateProcessing(),
                                                       requestBody.getEffectiveFrom(),
                                                       requestBody.getEffectiveTo(),
                                                       requestBody.getProperties(),
                                                       instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                       requestBody.getEffectiveTime(),
                                                       methodName);
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
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody new properties for the classification
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse reclassifyMetadataElementInStore(String                      serverName,
                                                         String                      serviceURLMarker,
                                                         String                      userId,
                                                         String                      metadataElementGUID,
                                                         String                      classificationName,
                                                         UpdatePropertiesRequestBody requestBody)
    {
        final String methodName = "reclassifyMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.reclassifyMetadataElementInStore(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         metadataElementGUID,
                                                         classificationName,
                                                         requestBody.getReplaceProperties(),
                                                         requestBody.getForLineage(),
                                                         requestBody.getForDuplicateProcessing(),
                                                         requestBody.getProperties(),
                                                         instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                         requestBody.getEffectiveTime(),
                                                         methodName);
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
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse updateClassificationEffectivityInStore(String                            serverName,
                                                               String                            serviceURLMarker,
                                                               String                            userId,
                                                               String                            metadataElementGUID,
                                                               String                            classificationName,
                                                               UpdateEffectivityDatesRequestBody requestBody)
    {
        final String methodName = "updateClassificationStatusInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.updateClassificationStatusInStore(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          metadataElementGUID,
                                                          classificationName,
                                                          requestBody.getForLineage(),
                                                          requestBody.getForDuplicateProcessing(),
                                                          requestBody.getEffectiveFrom(),
                                                          requestBody.getEffectiveTo(),
                                                          instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                          requestBody.getEffectiveTime(),
                                                          methodName);
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
     * Remove the named classification from a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to remove this classification
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse declassifyMetadataElementInStore(String            serverName,
                                                         String            serviceURLMarker,
                                                         String            userId,
                                                         String            metadataElementGUID,
                                                         String            classificationName,
                                                         UpdateRequestBody requestBody)
    {
        final String methodName = "unclassifyMetadataElementInStore";
        final String metadataElementGUIDParameterName = "metadataElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.unclassifyMetadataElementInStore(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         metadataElementGUID,
                                                         metadataElementGUIDParameterName,
                                                         OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                         classificationName,
                                                         requestBody.getForLineage(),
                                                         requestBody.getForDuplicateProcessing(),
                                                         instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                         requestBody.getEffectiveTime(),
                                                         methodName);
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
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody the properties of the relationship
     *
     * @return unique identifier of the new relationship or
     *
     *  InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse createRelatedElementsInStore(String                        serverName,
                                                     String                        serviceURLMarker,
                                                     String                        userId,
                                                     NewRelatedElementsRequestBody requestBody)
    {
        final String methodName = "createRelatedElementsInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                response.setGUID(handler.createRelatedElementsInStore(userId,
                                                                      requestBody.getExternalSourceGUID(),
                                                                      requestBody.getExternalSourceName(),
                                                                      requestBody.getTypeName(),
                                                                      requestBody.getMetadataElement1GUID(),
                                                                      requestBody.getMetadataElement2GUID(),
                                                                      requestBody.getForLineage(),
                                                                      requestBody.getForDuplicateProcessing(),
                                                                      requestBody.getEffectiveFrom(),
                                                                      requestBody.getEffectiveTo(),
                                                                      requestBody.getProperties(),
                                                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                      requestBody.getEffectiveTime(),
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
     * Update the properties associated with a relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody new properties for the relationship
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to update this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse updateRelatedElementsInStore(String                      serverName,
                                                     String                      serviceURLMarker,
                                                     String                      userId,
                                                     String                      relationshipGUID,
                                                     UpdatePropertiesRequestBody requestBody)
    {
        final String methodName = "updateRelatedElementsInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.updateRelatedElementsInStore(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     relationshipGUID,
                                                     requestBody.getReplaceProperties(),
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getProperties(),
                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                     requestBody.getEffectiveTime(),
                                                     methodName);
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
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse updateRelatedElementsEffectivityInStore(String                            serverName,
                                                                String                            serviceURLMarker,
                                                                String                            userId,
                                                                String                            relationshipGUID,
                                                                UpdateEffectivityDatesRequestBody requestBody)
    {
        final String methodName = "updateRelatedElementsStatusInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.updateRelatedElementsStatusInStore(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           relationshipGUID,
                                                           requestBody.getEffectiveFrom(),
                                                           requestBody.getEffectiveTo(),
                                                           requestBody.getForLineage(),
                                                           requestBody.getForDuplicateProcessing(),
                                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
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
     * Delete a relationship between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    public VoidResponse deleteRelatedElementsInStore(String            serverName,
                                                     String            serviceURLMarker,
                                                     String            userId,
                                                     String            relationshipGUID,
                                                     UpdateRequestBody requestBody)
    {
        final String methodName = "deleteRelatedElementsInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

                handler.deleteRelatedElementsInStore(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     relationshipGUID,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                     requestBody.getEffectiveTime(),
                                                     methodName);
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
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties for the new incident report
     *
     * @return unique identifier of the resulting incident report or
     *
     *  InvalidParameterException null or non-unique qualified name for the incident report
     *  UserNotAuthorizedException this governance action service is not authorized to create an incident report
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse createIncidentReport(String                    serverName,
                                             String                    serviceURLMarker,
                                             String                    userId,
                                             IncidentReportRequestBody requestBody)
    {
        final String methodName = "createIncidentReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        // todo

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a To-Do request for someone to work on.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody unique name for the to do and other characteristics
     *
     * @return unique identifier of new to do element or
     * InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * UserNotAuthorizedException the governance action service is not authorized to create a to-do
     * PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public GUIDResponse openToDo(String          serverName,
                                 String          serviceURLMarker,
                                 String          userId,
                                 ToDoRequestBody requestBody)
    {
        final String methodName = "openToDo";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        // todo

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @return void or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public VoidResponse setTranslation(String            serverName,
                                       String            serviceURLMarker,
                                       String            userId,
                                       String            elementGUID,
                                       TranslationDetail translationDetail)
    {
        final String methodName = "setTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        // todo

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the language is null or not known or not unique (add locale)
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public VoidResponse clearTranslation(String          serverName,
                                         String          serviceURLMarker,
                                         String          userId,
                                         String          elementGUID,
                                         String          language,
                                         String          locale,
                                         NullRequestBody requestBody)
    {
        final String methodName = "clearTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        // todo

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @return the properties of the translation or null if there is none or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public TranslationDetailResponse getTranslation(String serverName,
                                                    String serviceURLMarker,
                                                    String userId,
                                                    String elementGUID,
                                                    String language,
                                                    String locale)
    {
        final String methodName = "getTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        TranslationDetailResponse response = new TranslationDetailResponse();

        // todo

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public TranslationListResponse getTranslations(String serverName,
                                                   String serviceURLMarker,
                                                   String userId,
                                                   String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize)
    {
        final String methodName = "getTranslations";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        TranslationListResponse response = new TranslationListResponse();

        // todo

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Creates the qualifiedName of the element based on the properties supplied.
     *
     * <ul>
     *     <li><i>Egeria:ValidMetadataValue:</i> - This is the name of the top level set.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:</i> - This is the name of a set for a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName-</i> - This is the name of the set for a property name for a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName-</i> - This is the name of the set for a property name for all types where the property name appears.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName-(preferredValue)</i> - This is one of the valid metadata values for the property name when used with a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName-(preferredValue)</i> - This is one of the valid metadata values for the property name when used with any type.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName--mapName--</i> - This is a valid map name for a property name used within a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName--mapName--</i> This is a valid map name for a property name used with any type</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName--mapName--(preferredValue)</i> - This is a valid metadata map value for a property name used within a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName--mapName--(preferredValue)</i> - This is a valid metadata map value for a property name used with any type.</li>
     * </ul>
     *
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @param preferredValue name of the valid value (can be null)
     * @return string (never null)
     */
    private String constructValidValueQualifiedName(String typeName,
                                                    String propertyName,
                                                    String mapName,
                                                    String preferredValue)
    {
        String qualifiedName = "Egeria:ValidMetadataValue:";

        if ((typeName != null) || (propertyName != null))
        {
            if (typeName != null)
            {
                qualifiedName = qualifiedName + typeName + ":";
            }
            else
            {
                qualifiedName = qualifiedName + ":";
            }

            if (propertyName != null)
            {
                qualifiedName = qualifiedName + propertyName + "-";

                if (mapName != null)
                {
                    qualifiedName = qualifiedName + "-" + mapName + "--";
                }

                if (preferredValue != null)
                {
                    qualifiedName = qualifiedName + "(" + preferredValue + ")";
                }
            }
        }

        return qualifiedName;
    }


    /**
     * Find or create the parent set for a valid value.
     *
     * @param userId calling user
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @param handler handler
     * @param methodName calling method
     * @return unique identifier (guid) of the parent set
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String getParentSet(String                                 userId,
                                String                                 typeName,
                                String                                 propertyName,
                                String                                 mapName,
                                ValidValuesHandler<ValidMetadataValue> handler,
                                String                                 methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String parentDescription = "Organizing set for valid metadata values";
        final String parentScope = "Open Metadata Ecosystem";
        final String parentUsage = "Used to control valid values in the open metadata ecosystem.";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        String parentQualifiedName = constructValidValueQualifiedName(typeName, propertyName, mapName, null);

        EntityDetail parentSet = handler.getEntityByValue(userId,
                                                          parentQualifiedName,
                                                          OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                          OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                                          OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                          specificMatchPropertyNames,
                                                          true,
                                                          false,
                                                          null,
                                                          methodName);

        if (parentSet == null)
        {
            String grandParentSetGUID = null;
            String parentDisplayName = parentQualifiedName.substring(26);

            if (mapName != null)
            {
                grandParentSetGUID = getParentSet(userId, typeName, propertyName, null, handler, methodName);
            }
            else if (propertyName != null)
            {
                grandParentSetGUID = getParentSet(userId, typeName, null, null, handler, methodName);
            }
            else if (typeName != null)
            {
                grandParentSetGUID = getParentSet(userId, null, null, null, handler, methodName);
            }

            return handler.createValidValue(userId,
                                            null,
                                            null,
                                            grandParentSetGUID,
                                            grandParentSetGUID,
                                            OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                            false,
                                            parentQualifiedName,
                                            parentDisplayName,
                                            parentDescription,
                                            parentUsage,
                                            parentScope,
                                            null,
                                            false,
                                            null,
                                            null,
                                            null,
                                            null,
                                            false,
                                            false,
                                            null,
                                            methodName);
        }
        else
        {
            return parentSet.getGUID();
        }
    }

    /**
     * Retrieve a valid metadata value entity from the open metadata ecosystem.
     *
     * @param userId calling user
     * @param qualifiedName qualified name
     * @param handler valid values handler
     * @param methodName calling method
     * @return valid metadata value entity
     * @throws InvalidParameterException one of the properties passed to the handler is invalid
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    private EntityDetail getValidMetadataElement(String                                 userId,
                                                 String                                 qualifiedName,
                                                 ValidValuesHandler<ValidMetadataValue> handler,
                                                 String                                 methodName) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return handler.getEntityByValue(userId,
                                        qualifiedName,
                                        OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
                                        OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                        specificMatchPropertyNames,
                                        true,
                                        false,
                                        null,
                                        methodName);
    }


    /**
     * Retrieve a valid metadata value bean from the open metadata ecosystem.
     *
     * @param userId calling user
     * @param qualifiedName qualified name
     * @param handler valid values handler
     * @param methodName calling method
     * @return valid metadata value bean
     * @throws InvalidParameterException one of the properties passed to the handler is invalid
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    private ValidMetadataValue getValidMetadataBean(String                                 userId,
                                                    String                                 qualifiedName,
                                                    ValidValuesHandler<ValidMetadataValue> handler,
                                                    String                                 methodName) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        EntityDetail element = getValidMetadataElement(userId, qualifiedName, handler, methodName);

        if (element != null)
        {
            OpenMetadataAPIGenericConverter<ValidMetadataValue> converter = handler.getConverter();

            return converter.getNewBean(ValidMetadataValue.class, element, methodName);
        }

        return null;
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setUpValidMetadataValue(String             serverName,
                                                String             serviceURLMarker,
                                                String             userId,
                                                String             typeName,
                                                String             propertyName,
                                                ValidMetadataValue requestBody)
    {
        final String methodName = "setUpValidMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
                invalidParameterHandler.validateName(requestBody.getPreferredValue(), preferredValueParameter, methodName);

                ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

                String setGUID = this.getParentSet(userId, typeName, propertyName, null, handler, methodName);

                handler.createValidValue(userId,
                                         null,
                                         null,
                                         setGUID,
                                         setGUID,
                                         OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                         false,
                                         this.constructValidValueQualifiedName(typeName, propertyName, null, requestBody.getPreferredValue()),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getUsage(),
                                         requestBody.getScope(),
                                         requestBody.getPreferredValue(),
                                         requestBody.getIsDeprecated(),
                                         requestBody.getAdditionalProperties(),
                                         null,
                                         requestBody.getEffectiveFrom(),
                                         requestBody.getEffectiveTo(),
                                         false,
                                         false,
                                         null,
                                         methodName);
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
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     *
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setUpValidMetadataMapName(String             serverName,
                                                  String             serviceURLMarker,
                                                  String             userId,
                                                  String             typeName,
                                                  String             propertyName,
                                                  ValidMetadataValue requestBody)
    {
        final String methodName = "setUpValidMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
                invalidParameterHandler.validateName(requestBody.getPreferredValue(), preferredValueParameter, methodName);

                ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

                String setGUID = this.getParentSet(userId, typeName, propertyName, null, handler, methodName);

                handler.createValidValue(userId,
                                         null,
                                         null,
                                         setGUID,
                                         setGUID,
                                         OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                         false,
                                         this.constructValidValueQualifiedName(typeName, propertyName, requestBody.getPreferredValue(), null),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getUsage(),
                                         requestBody.getScope(),
                                         requestBody.getPreferredValue(),
                                         requestBody.getIsDeprecated(),
                                         requestBody.getAdditionalProperties(),
                                         null,
                                         requestBody.getEffectiveFrom(),
                                         requestBody.getEffectiveTo(),
                                         false,
                                         false,
                                         null,
                                         methodName);
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
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     *
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setUpValidMetadataMapValue(String             serverName,
                                                   String             serviceURLMarker,
                                                   String             userId,
                                                   String             typeName,
                                                   String             propertyName,
                                                   String             mapName,
                                                   ValidMetadataValue requestBody)
    {
        final String methodName = "setUpValidMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
                invalidParameterHandler.validateName(requestBody.getPreferredValue(), preferredValueParameter, methodName);

                ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

                String setGUID = this.getParentSet(userId, typeName, propertyName, mapName, handler, methodName);

                handler.createValidValue(userId,
                                         null,
                                         null,
                                         setGUID,
                                         setGUID,
                                         OpenMetadataAPIMapper.VALID_VALUE_SET_TYPE_NAME,
                                         false,
                                         this.constructValidValueQualifiedName(typeName, propertyName, mapName, requestBody.getPreferredValue()),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getUsage(),
                                         requestBody.getScope(),
                                         requestBody.getPreferredValue(),
                                         requestBody.getIsDeprecated(),
                                         requestBody.getAdditionalProperties(),
                                         null,
                                         requestBody.getEffectiveFrom(),
                                         requestBody.getEffectiveTo(),
                                         false,
                                         false,
                                         null,
                                         methodName);
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
     * Remove a valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearValidMetadataValue(String          serverName,
                                                String          serviceURLMarker,
                                                String          userId,
                                                String          typeName,
                                                String          propertyName,
                                                String          preferredValue,
                                                NullRequestBody requestBody)
    {
        final String methodName = "clearValidMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
                invalidParameterHandler.validateName(preferredValue, preferredValueParameter, methodName);

                ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

                String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, null, preferredValue);

                EntityDetail element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

                if (element != null)
                {
                    handler.deleteValidValue(userId,
                                             null,
                                             null,
                                             element.getGUID(),
                                             qualifiedName,
                                             false,
                                             false,
                                             null,
                                             methodName);
                }
                else
                {
                    invalidParameterHandler.throwUnknownElement(userId, qualifiedName, typeName, instanceHandler.getServiceName(serviceURLMarker), serverName, methodName);
                }
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
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public VoidResponse clearValidMetadataMapName(String          serverName,
                                                  String          serviceURLMarker,
                                                  String          userId,
                                                  String          typeName,
                                                  String          propertyName,
                                                  String          preferredValue,
                                                  NullRequestBody requestBody)
    {
        final String methodName = "clearValidMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
                invalidParameterHandler.validateName(preferredValue, preferredValueParameter, methodName);

                ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

                String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, preferredValue, null);

                EntityDetail element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

                if (element != null)
                {
                    handler.deleteValidValue(userId,
                                             null,
                                             null,
                                             element.getGUID(),
                                             qualifiedName,
                                             false,
                                             false,
                                             null,
                                             methodName);
                }
                else
                {
                    invalidParameterHandler.throwUnknownElement(userId, qualifiedName, typeName, instanceHandler.getServiceName(serviceURLMarker), serverName, methodName);
                }
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
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public VoidResponse clearValidMetadataMapValue(String          serverName,
                                                   String          serviceURLMarker,
                                                   String          userId,
                                                   String          typeName,
                                                   String          propertyName,
                                                   String          mapName,
                                                   String          preferredValue,
                                                   NullRequestBody requestBody)
    {
        final String methodName = "clearValidMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
                invalidParameterHandler.validateName(preferredValue, preferredValueParameter, methodName);

                ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

                String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, mapName, preferredValue);

                EntityDetail element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

                if (element != null)
                {
                    handler.deleteValidValue(userId,
                                             null,
                                             null,
                                             element.getGUID(),
                                             qualifiedName,
                                             false,
                                             false,
                                             null,
                                             methodName);
                }
                else
                {
                    invalidParameterHandler.throwUnknownElement(userId, qualifiedName, typeName, instanceHandler.getServiceName(serviceURLMarker), serverName, methodName);
                }
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
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public BooleanResponse validateMetadataValue(String serverName,
                                                 String serviceURLMarker,
                                                 String userId,
                                                 String typeName,
                                                 String propertyName,
                                                 String actualValue)
    {
        final String methodName = "validateMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        BooleanResponse response = new BooleanResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
            invalidParameterHandler.validateName(actualValue, actualValueParameter, methodName);

            ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

            String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, null, actualValue);

            EntityDetail element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

            response.setFlag(false);

            if (element != null)
            {
                response.setFlag(true);
            }
            else if (typeName != null)
            {
                qualifiedName = this.constructValidValueQualifiedName(null, propertyName,null,  actualValue);

                element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

                if (element != null)
                {
                    response.setFlag(true);
                }
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
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public BooleanResponse validateMetadataMapName(String serverName,
                                                   String serviceURLMarker,
                                                   String userId,
                                                   String typeName,
                                                   String propertyName,
                                                   String actualValue)
    {
        final String methodName = "validateMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        BooleanResponse response = new BooleanResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
            invalidParameterHandler.validateName(actualValue, actualValueParameter, methodName);

            ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

            String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, actualValue, null);

            EntityDetail element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

            response.setFlag(false);

            if (element != null)
            {
                response.setFlag(true);
            }
            else if (typeName != null)
            {
                qualifiedName = this.constructValidValueQualifiedName(null, propertyName, actualValue, null);

                element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

                if (element != null)
                {
                    response.setFlag(true);
                }
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
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public BooleanResponse validateMetadataMapValue(String serverName,
                                                    String serviceURLMarker,
                                                    String userId,
                                                    String typeName,
                                                    String propertyName,
                                                    String mapName,
                                                    String actualValue)
    {
        final String methodName = "validateMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        BooleanResponse response = new BooleanResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
            invalidParameterHandler.validateName(actualValue, actualValueParameter, methodName);

            ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

            String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, mapName, actualValue);

            EntityDetail element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

            response.setFlag(false);

            if (element != null)
            {
                response.setFlag(true);
            }
            else if (typeName != null)
            {
                qualifiedName = this.constructValidValueQualifiedName(null, propertyName, mapName, actualValue);

                element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

                if (element != null)
                {
                    response.setFlag(true);
                }
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
     * Retrieve details of a specific valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueResponse getValidMetadataValue(String serverName,
                                                            String serviceURLMarker,
                                                            String userId,
                                                            String typeName,
                                                            String propertyName,
                                                            String preferredValue)
    {
        final String methodName = "getValidMetadataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        ValidMetadataValueResponse response = new ValidMetadataValueResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
            invalidParameterHandler.validateName(preferredValue, preferredValueParameter, methodName);

            ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

            String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, null, preferredValue);

            ValidMetadataValue element = this.getValidMetadataBean(userId, qualifiedName, handler, methodName);

            if (element != null)
            {
                response.setElement(element);
            }
            else
            {
                invalidParameterHandler.throwUnknownElement(userId,
                                                            qualifiedName,
                                                            OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                            instanceHandler.getServiceName(serviceURLMarker),
                                                            serverName,
                                                            methodName);
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
     * Retrieve details of a specific valid name for a map property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueResponse getValidMetadataMapName(String serverName,
                                                              String serviceURLMarker,
                                                              String userId,
                                                              String typeName,
                                                              String propertyName,
                                                              String preferredValue)
    {
        final String methodName = "getValidMetadataMapName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        ValidMetadataValueResponse response = new ValidMetadataValueResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
            invalidParameterHandler.validateName(preferredValue, preferredValueParameter, methodName);

            ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

            String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, preferredValue, null);

            ValidMetadataValue element = this.getValidMetadataBean(userId, qualifiedName, handler, methodName);

            if (element != null)
            {
                response.setElement(element);
            }
            else
            {
                invalidParameterHandler.throwUnknownElement(userId,
                                                            qualifiedName,
                                                            OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                            instanceHandler.getServiceName(serviceURLMarker),
                                                            serverName,
                                                            methodName);
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
     * Retrieve details of a specific valid value for a map name.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueResponse getValidMetadataMapValue(String serverName,
                                                               String serviceURLMarker,
                                                               String userId,
                                                               String typeName,
                                                               String propertyName,
                                                               String mapName,
                                                               String preferredValue)
    {
        final String methodName = "getValidMetadataMapValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        ValidMetadataValueResponse response = new ValidMetadataValueResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);
            invalidParameterHandler.validateName(preferredValue, preferredValueParameter, methodName);

            ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

            String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, mapName, preferredValue);

            ValidMetadataValue element = this.getValidMetadataBean(userId, qualifiedName, handler, methodName);

            if (element != null)
            {
                response.setElement(element);
            }
            else
            {
                invalidParameterHandler.throwUnknownElement(userId,
                                                            qualifiedName,
                                                            OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                            instanceHandler.getServiceName(serviceURLMarker),
                                                            serverName,
                                                            methodName);
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
     * Retrieve all the valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValueListResponse getValidMetadataValues(String serverName,
                                                                 String serviceURLMarker,
                                                                 String userId,
                                                                 String typeName,
                                                                 String propertyName,
                                                                 int    startFrom,
                                                                 int    pageSize)
    {
        final String methodName = "getValidMetadataValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        ValidMetadataValueListResponse response = new ValidMetadataValueListResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);

            ValidValuesHandler<ValidMetadataValue> handler = instanceHandler.getValidMetadataValuesHandler(userId, serverName, methodName);

            String qualifiedName = this.constructValidValueQualifiedName(typeName, propertyName, null, null);

            EntityDetail element = this.getValidMetadataElement(userId, qualifiedName, handler, methodName);

            if (element != null)
            {
                ValidValuesHandler<ValidMetadataValueDetail> detailsHandler = instanceHandler.getValidMetadataValuesDetailHandler(userId, serverName, methodName);

                List<ValidMetadataValueDetail> details = detailsHandler.getValidValueSetMembers(userId,
                                                                                                element.getGUID(),
                                                                                                "element.getGUID()",
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                false,
                                                                                                false,
                                                                                                null,
                                                                                                methodName);

                if (details != null)
                {
                    for (ValidMetadataValueDetail detail : details)
                    {
                        EntityDetail detailElement = this.getValidMetadataElement(userId,
                                                                                  this.constructValidValueQualifiedName(typeName,
                                                                                                                        propertyName,
                                                                                                                        null,
                                                                                                                        detail.getPreferredValue()),
                                                                                  handler,
                                                                                  methodName);

                        if (detailElement == null)
                        {
                            detailElement = this.getValidMetadataElement(userId,
                                                                         this.constructValidValueQualifiedName(typeName,
                                                                                                               propertyName,
                                                                                                               detail.getPreferredValue(),
                                                                                                               null),
                                                                         handler,
                                                                         methodName);
                        }

                        if (detailElement != null)
                        {
                            List<ValidMetadataValue> mapDetails = handler.getValidValueSetMembers(userId,
                                                                                                  detailElement.getGUID(),
                                                                                                  "detail.getGUID()",
                                                                                                  0,
                                                                                                  0,
                                                                                                  false,
                                                                                                  false,
                                                                                                  null,
                                                                                                  methodName);

                            detail.setValidMapNameValues(mapDetails);
                        }
                    }
                }

                response.setElementList(details);
            }
            else
            {
                invalidParameterHandler.throwUnknownElement(userId,
                                                            qualifiedName,
                                                            OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                            instanceHandler.getServiceName(serviceURLMarker),
                                                            serverName,
                                                            methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
