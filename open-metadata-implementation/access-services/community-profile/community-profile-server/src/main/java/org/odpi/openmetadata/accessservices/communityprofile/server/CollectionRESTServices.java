/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.commonservices.ffdc.rest.CollectionsResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * CollectionRESTServices provides the API operations to create and maintain collection information.
 */
public class CollectionRESTServices
{
    private static final CommunityProfileInstanceHandler   instanceHandler     = new CommunityProfileInstanceHandler();
    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(CollectionRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public CollectionRESTServices()
    {
    }


    /**
     * Create a new metadata element to represent a collection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createCollection(String                   serverName,
                                         String                   userId,
                                         ReferenceableRequestBody requestBody)
    {
        final String methodName = "createCollection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionProperties properties)
                {
                    String collectionGUID = handler.createCollection(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     properties.getQualifiedName(),
                                                                     properties.getName(),
                                                                     properties.getDescription(),
                                                                     properties.getCollectionType(),
                                                                     properties.getAdditionalProperties(),
                                                                     properties.getTypeName(),
                                                                     properties.getExtendedProperties(),
                                                                     null,
                                                                     properties.getEffectiveFrom(),
                                                                     properties.getEffectiveTo(),
                                                                     new Date(),
                                                                     methodName);

                    if (collectionGUID != null)
                    {
                        handler.setVendorProperties(userId,
                                                    collectionGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }

                    response.setGUID(collectionGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a collection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createFolderCollection(String                   serverName,
                                               String                   userId,
                                               ReferenceableRequestBody requestBody)
    {
        final String methodName = "createFolderCollection";
        final String collectionGUIDParameterName = "collectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionFolderProperties properties)
                {
                    String collectionGUID = handler.createCollection(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     properties.getQualifiedName(),
                                                                     properties.getName(),
                                                                     properties.getDescription(),
                                                                     properties.getCollectionType(),
                                                                     properties.getAdditionalProperties(),
                                                                     properties.getTypeName(),
                                                                     properties.getExtendedProperties(),
                                                                     null,
                                                                     properties.getEffectiveFrom(),
                                                                     properties.getEffectiveTo(),
                                                                     new Date(),
                                                                     methodName);

                    if (collectionGUID != null)
                    {
                        handler.addFolderClassificationToCollection(userId,
                                                                    collectionGUID,
                                                                    collectionGUIDParameterName,
                                                                    properties.getCollectionOrder().getOrdinal(),
                                                                    properties.getOrderByPropertyName(),
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    null,
                                                                    methodName);
                        handler.setVendorProperties(userId,
                                                    collectionGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    null,
                                                    methodName);
                    }

                    response.setGUID(collectionGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionProperties.class.getName(), methodName);
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
     * Update the metadata element representing a collection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param collectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateCollection(String                   serverName,
                                         String                   userId,
                                         String                   collectionGUID,
                                         boolean                  isMergeUpdate,
                                         ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateCollection";
        final String collectionGUIDParameterName = "collectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionProperties properties)
                {
                    handler.updateCollection(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            collectionGUID,
                                            collectionGUIDParameterName,
                                            properties.getQualifiedName(),
                                            properties.getName(),
                                            properties.getDescription(),
                                            properties.getCollectionType(),
                                            properties.getAdditionalProperties(),
                                            properties.getTypeName(),
                                            properties.getExtendedProperties(),
                                            properties.getEffectiveFrom(),
                                            properties.getEffectiveTo(),
                                            isMergeUpdate,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                    if ((! isMergeUpdate) || (properties.getVendorProperties() != null))
                    {
                        handler.setVendorProperties(userId,
                                                    collectionGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionProperties.class.getName(), methodName);
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
     * Create a relationship between a collection and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param collectionGUID unique identifier of the collection in the external data manager
     * @param collectionRoleGUID unique identifier of the person role in the external data manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateCollectionMembership(String                  serverName,
                                                   String                  userId,
                                                   String                  collectionGUID,
                                                   String                  collectionRoleGUID,
                                                   boolean                 isMergeUpdate,
                                                   RelationshipRequestBody requestBody)
    {
        final String methodName                      = "updateCollectionMembership";
        final String collectionGUIDParameterName     = "collectionGUID";
        final String collectionRoleGUIDParameterName = "collectionRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    int ordinal = 0;

                    if (properties.getStatus() != null)
                    {
                        ordinal = properties.getStatus().getOrdinal();
                    }

                    handler.updateCollectionMembership(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       collectionGUID,
                                                       collectionGUIDParameterName,
                                                       collectionRoleGUID,
                                                       collectionRoleGUIDParameterName,
                                                       properties.getMembershipRationale(),
                                                       properties.getExpression(),
                                                       ordinal,
                                                       properties.getUserDefinedStatus(),
                                                       properties.getConfidence(),
                                                       properties.getCreatedBy(),
                                                       properties.getSteward(),
                                                       properties.getStewardTypeName(),
                                                       properties.getStewardPropertyName(),
                                                       properties.getSource(),
                                                       properties.getNotes(),
                                                       properties.getEffectiveFrom(),
                                                       properties.getEffectiveTo(),
                                                       isMergeUpdate,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addMemberToCollection(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  collectionGUID,
                                                  collectionGUIDParameterName,
                                                  collectionRoleGUID,
                                                  collectionRoleGUIDParameterName,
                                                  null,
                                                  null,
                                                  null,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionMembershipProperties.class.getName(), methodName);
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
     * Remove a relationship between a collection and a role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param collectionGUID unique identifier of the collection in the external data manager
     * @param collectionRoleGUID unique identifier of the role in the external data manager
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeFromCollection(String                    serverName,
                                             String                    userId,
                                             String                    collectionGUID,
                                             String                    collectionRoleGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName                      = "removeFromCollection";
        final String collectionGUIDParameterName     = "collectionGUID";
        final String collectionRoleGUIDParameterName = "collectionRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeMemberFromCollection(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   collectionGUID,
                                                   collectionGUIDParameterName,
                                                   collectionRoleGUID,
                                                   collectionRoleGUIDParameterName,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a collection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param collectionGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeCollection(String                    serverName,
                                         String                    userId,
                                         String                    collectionGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName = "removeCollection";
        final String collectionGUIDParameterName = "collectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

                handler.removeCollection(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         collectionGUID,
                                         collectionGUIDParameterName,
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of collection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CollectionsResponse findCollections(String                  serverName,
                                               String                  userId,
                                               SearchStringRequestBody requestBody,
                                               int                     startFrom,
                                               int                     pageSize)
    {
        final String methodName = "findCollections";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

                List<CollectionElement> collections = handler.findCollections(userId,
                                                                              requestBody.getSearchString(),
                                                                              searchStringParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

                response.setElements(setUpVendorProperties(userId, collections, handler, methodName));
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
     * Retrieve the list of collection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CollectionsResponse getCollectionsByName(String          serverName,
                                                       String          userId,
                                                       NameRequestBody requestBody,
                                                       int             startFrom,
                                                       int             pageSize)
    {
        final String methodName = "getCollectionsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

                List<CollectionElement> collections = handler.getCollectionsByName(userId,
                                                                                   requestBody.getName(),
                                                                                   nameParameterName,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   false,
                                                                                   false,
                                                                                   new Date(),
                                                                                   methodName);

                response.setElements(setUpVendorProperties(userId, collections, handler, methodName));
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
     * Return information about a person role connected to the named collection.
     *
     * @param serverName called server
     * @param userId calling user
     * @param collectionGUID unique identifier for the collection
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public CollectionMembersResponse getCollectionMembers(String          serverName,
                                                             String          userId,
                                                             String          collectionGUID,
                                                             int             startFrom,
                                                             int             pageSize)
    {
        final String methodName         = "getCollectionMembers";
        final String guidParameterName  = "collectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CollectionMembersResponse response = new CollectionMembersResponse();
        AuditLog                     auditLog = null;

        try
        {
            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<Relationship> membershipLinks = handler.getAttachmentLinks(userId,
                                                                            collectionGUID,
                                                                            guidParameterName,
                                                                            OpenMetadataType.COLLECTION.typeName,
                                                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeGUID,
                                                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                            null,
                                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                                            2,
                                                                            false,
                                                                            false,
                                                                            startFrom,
                                                                            pageSize,
                                                                            new Date(),
                                                                            methodName);


        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of collection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param parentGUID unique identifier of referenceable object (typically a personal profile, project or
     *                   community) that the collections hang off of.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CollectionsResponse getCollections(String          serverName,
                                                 String          userId,
                                                 String          parentGUID,
                                                 int             startFrom,
                                                 int             pageSize)
    {
        final String methodName = "getCollections";
        final String parentGUIDParameterName = "parentGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CollectionsResponse response = new CollectionsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            List<CollectionElement> collections = handler.getAttachedElements(userId,
                                                                              parentGUID,
                                                                              parentGUIDParameterName,
                                                                              OpenMetadataType.REFERENCEABLE.typeName,
                                                                              null,
                                                                              null,
                                                                              OpenMetadataType.COLLECTION.typeName,
                                                                              null,
                                                                              null,
                                                                              2,
                                                                              false,
                                                                              false,
                                                                              startFrom,
                                                                              pageSize,
                                                                              new Date(),
                                                                              methodName);

            response.setElements(setUpVendorProperties(userId, collections, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the collection metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CollectionResponse getCollectionByGUID(String serverName,
                                                  String userId,
                                                  String guid)
    {
        final String methodName = "getCollectionByGUID";
        final String guidParameterName = "collectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CollectionResponse response = new CollectionResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler<CollectionElement> handler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

            CollectionElement collection = handler.getCollectionByGUID(userId,
                                                                       guid,
                                                                       guidParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);

            response.setElement(setUpVendorProperties(userId, collection, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<CollectionElement> setUpVendorProperties(String                              userId,
                                                          List<CollectionElement>             retrievedResults,
                                                          CollectionHandler<CollectionElement> handler,
                                                          String                             methodName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (CollectionElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private CollectionElement setUpVendorProperties(String                             userId,
                                                   CollectionElement                   element,
                                                   CollectionHandler<CollectionElement> handler,
                                                   String                             methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            CollectionProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }
}
