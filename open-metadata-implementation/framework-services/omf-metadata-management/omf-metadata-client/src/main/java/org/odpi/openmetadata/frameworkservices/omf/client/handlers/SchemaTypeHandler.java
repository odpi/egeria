/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.client.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.SchemaTypeConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OpenMetadataStoreAuditCode;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OpenMetadataStoreErrorCode;

import java.util.*;

/**
 * SchemaTypeHandler provides methods to define schema types
 * The interface supports the following types of objects
 * <ul>
 *     <li>SchemaType</li>
 *     <li>Person</li>
 *     <li>Team</li>
 *     <li>ITProfile</li>
 * </ul>
 */
public class SchemaTypeHandler
{
    private final OpenMetadataStoreHandler openMetadataStoreClient;
    private final InvalidParameterHandler  invalidParameterHandler = new InvalidParameterHandler();
    private final AuditLog                 auditLog;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final String serviceName;
    private final String serverName;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param localServerName        name of this server (view server)
     * @param serverName             name of the server to connect to
     * @param serverPlatformURLRoot  the network address of the server running the OMAS REST services
     * @param auditLog               logging destination
     * @param accessServiceURLMarker which access service to call
     * @param serviceName            local service name
     * @param maxPageSize            maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SchemaTypeHandler(String localServerName,
                             String serverName,
                             String serverPlatformURLRoot,
                             AuditLog auditLog,
                             String accessServiceURLMarker,
                             String serviceName,
                             int maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog                = auditLog;
        this.serverName              = localServerName;
        this.serviceName             = serviceName;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param localServerName        name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param auditLog              logging destination
     * @param accessServiceURLMarker which access service to call
     * @param serviceName            local service name
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SchemaTypeHandler(String localServerName,
                             String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password,
                             AuditLog auditLog,
                             String accessServiceURLMarker,
                             String serviceName,
                             int maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, userId, password, maxPageSize);
        this.auditLog                = auditLog;
        this.serverName              = localServerName;
        this.serviceName             = serviceName;
    }


    /**
     * Create a new schema type.
     *
     * @param userId                       userId of user making request.
     * @param externalSourceGUID           unique identifier of the software capability that owns this element
     * @param externalSourceName           unique name of the software capability that owns this element
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param properties                   properties for the new element.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSchemaType(String                 userId,
                                     String                 externalSourceGUID,
                                     String                 externalSourceName,
                                     String                 anchorGUID,
                                     boolean                isOwnAnchor,
                                     String                 anchorScopeGUID,
                                     SchemaTypeProperties properties,
                                     String                 parentGUID,
                                     String                 parentRelationshipTypeName,
                                     ElementProperties      parentRelationshipProperties,
                                     boolean                parentAtEnd1,
                                     boolean                forLineage,
                                     boolean                forDuplicateProcessing,
                                     Date                   effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName                 = "createSchemaType";
        final String propertiesName             = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String elementTypeName = OpenMetadataType.ACTOR_PROFILE.typeName;

        if (properties.getTypeName() != null)
        {
            elementTypeName = properties.getTypeName();
        }


        return openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    elementTypeName,
                                                                    ElementStatus.ACTIVE,
                                                                    null,
                                                                    anchorGUID,
                                                                    isOwnAnchor,
                                                                    anchorScopeGUID,
                                                                    properties.getEffectiveFrom(),
                                                                    properties.getEffectiveTo(),
                                                                    this.getElementProperties(properties),
                                                                    parentGUID,
                                                                    parentRelationshipTypeName,
                                                                    parentRelationshipProperties,
                                                                    parentAtEnd1,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema type.
     *
     * @param userId                       calling user
     * @param externalSourceGUID           unique identifier of the software capability that owns this element
     * @param externalSourceName           unique name of the software capability that owns this element
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param effectiveFrom                the date when this element is active - null for active on creation
     * @param effectiveTo                  the date when this element becomes inactive - null for active until deleted
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String              userId,
                                                 String              externalSourceGUID,
                                                 String              externalSourceName,
                                                 String              anchorGUID,
                                                 boolean             isOwnAnchor,
                                                 String              anchorScopeGUID,
                                                 Date                effectiveFrom,
                                                 Date                effectiveTo,
                                                 String              templateGUID,
                                                 ElementProperties   replacementProperties,
                                                 Map<String, String> placeholderProperties,
                                                 String              parentGUID,
                                                 String              parentRelationshipTypeName,
                                                 ElementProperties   parentRelationshipProperties,
                                                 boolean             parentAtEnd1,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return openMetadataStoreClient.createMetadataElementFromTemplate(userId,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                         anchorGUID,
                                                                         isOwnAnchor,
                                                                         anchorScopeGUID,
                                                                         effectiveFrom,
                                                                         effectiveTo,
                                                                         templateGUID,
                                                                         replacementProperties,
                                                                         placeholderProperties,
                                                                         parentGUID,
                                                                         parentRelationshipTypeName,
                                                                         parentRelationshipProperties,
                                                                         parentAtEnd1,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime);
    }


    /**
     * Update the properties of a schema type.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param schemaTypeGUID       unique identifier of the schema type (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSchemaType(String                 userId,
                                   String                 externalSourceGUID,
                                   String                 externalSourceName,
                                   String                 schemaTypeGUID,
                                   boolean                replaceAllProperties,
                                   SchemaTypeProperties properties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName                 = "updateSchemaType";
        final String propertiesName             = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName          = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             schemaTypeGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }


    /**
     * Attach a profile to a location.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param schemaTypeGUID       unique identifier of the schema type
     * @param locationGUID           unique identifier of the location
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkLocationToProfile(String                    userId,
                                      String                    externalSourceGUID,
                                      String                    externalSourceName,
                                      String                    schemaTypeGUID,
                                      String                    locationGUID,
                                      ProfileLocationProperties relationshipProperties,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName            = "linkLocationToProfile";
        final String end1GUIDParameterName = "schemaTypeGUID";
        final String end2GUIDParameterName = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                                 schemaTypeGUID,
                                                                 locationGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 this.getElementProperties(relationshipProperties),
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                                 locationGUID,
                                                                 schemaTypeGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a schema type from a location.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param schemaTypeGUID       unique identifier of the schema type
     * @param locationGUID           unique identifier of the location
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachLocationFromProfile(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  schemaTypeGUID,
                                          String  locationGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachLocationFromProfile";

        final String end1GUIDParameterName = "schemaTypeGUID";
        final String end2GUIDParameterName = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                             locationGUID,
                                                             schemaTypeGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerPerson(String         userId,
                               String         externalSourceGUID,
                               String         externalSourceName,
                               String         personOneGUID,
                               String         personTwoGUID,
                               PeerProperties relationshipProperties,
                               boolean        forLineage,
                               boolean        forDuplicateProcessing,
                               Date           effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName            = "linkPeerPerson";
        final String end1GUIDParameterName = "personOneGUID";
        final String end2GUIDParameterName = "personTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personOneGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personTwoGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.PEER_RELATIONSHIP.typeName,
                                                                 personOneGUID,
                                                                 personTwoGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 null,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.PEER_RELATIONSHIP.typeName,
                                                                 personOneGUID,
                                                                 personTwoGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a person profile from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerPerson(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  personOneGUID,
                                 String  personTwoGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName = "detachPeerPerson";
        final String end1GUIDParameterName = "personOneGUID";
        final String end2GUIDParameterName = "personTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personOneGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personTwoGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.PEER_RELATIONSHIP.typeName,
                                                             personOneGUID,
                                                             personTwoGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a super team to a subteam.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamStructure(String                  userId,
                                  String                  externalSourceGUID,
                                  String                  externalSourceName,
                                  String                  superTeamGUID,
                                  String                  subteamGUID,
                                  TeamStructureProperties relationshipProperties,
                                  boolean                 forLineage,
                                  boolean                 forDuplicateProcessing,
                                  Date                    effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName            = "linkTeamStructure";
        final String end1GUIDParameterName = "superTeamGUID";
        final String end2GUIDParameterName = "subteamGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(superTeamGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(subteamGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                                 superTeamGUID,
                                                                 subteamGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 this.getElementProperties(relationshipProperties),
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                                 superTeamGUID,
                                                                 subteamGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamStructure(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  superTeamGUID,
                                    String  subteamGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "detachTeamStructure";
        final String end1GUIDParameterName = "superTeamGUID";
        final String end2GUIDParameterName = "subteamGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(superTeamGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(subteamGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                             superTeamGUID,
                                                             subteamGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach an asset to an IT profile.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssetToProfile(String                            userId,
                                   String                            externalSourceGUID,
                                   String                            externalSourceName,
                                   String                            assetGUID,
                                   String                            itProfileGUID,
                                   ITInfrastructureProfileProperties relationshipProperties,
                                   boolean                           forLineage,
                                   boolean                           forDuplicateProcessing,
                                   Date                              effectiveTime) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkAssetToProfile";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName,
                                                                 assetGUID,
                                                                 itProfileGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 null,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName,
                                                                 assetGUID,
                                                                 itProfileGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach an asset from an IT profile.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param assetGUID              unique identifier of the asset
     * @param itProfileGUID          unique identifier of the IT profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetFromProfile(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  assetGUID,
                                       String  itProfileGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachAssetFromProfile";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName,
                                                             itProfileGUID,
                                                             itProfileGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a team to its membership role.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamToMembershipRole(String                   userId,
                                         String                   externalSourceGUID,
                                         String                   externalSourceName,
                                         String                   teamGUID,
                                         String                   personRoleGUID,
                                         TeamMembershipProperties relationshipProperties,
                                         boolean                  forLineage,
                                         boolean                  forDuplicateProcessing,
                                         Date                     effectiveTime) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName            = "linkTeamToMembershipRole";
        final String end1GUIDParameterName = "teamGUID";
        final String end2GUIDParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                 teamGUID,
                                                                 personRoleGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 this.getElementProperties(relationshipProperties),
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                 teamGUID,
                                                                 personRoleGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a team profile from its membership role.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamFromMembershipRole(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  teamGUID,
                                             String  personRoleGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachTeamFromMembershipRole";
        final String end1GUIDParameterName = "teamGUID";
        final String end2GUIDParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.TEAM_MEMBERSHIP_RELATIONSHIP.typeName,
                                                             teamGUID,
                                                             personRoleGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a team to its leadership role.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamToLeadershipRole(String                   userId,
                                         String                   externalSourceGUID,
                                         String                   externalSourceName,
                                         String                   teamGUID,
                                         String                   personRoleGUID,
                                         TeamLeadershipProperties relationshipProperties,
                                         boolean                  forLineage,
                                         boolean                  forDuplicateProcessing,
                                         Date                     effectiveTime) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName            = "linkTeamToLeadershipRole";
        final String end1GUIDParameterName = "teamGUID";
        final String end2GUIDParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP.typeName,
                                                                 teamGUID,
                                                                 personRoleGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 this.getElementProperties(relationshipProperties),
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP.typeName,
                                                                 teamGUID,
                                                                 personRoleGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a team profile from its leadership role.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamFromLeadershipRole(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  teamGUID,
                                             String  personRoleGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachTeamFromLeadershipRole";
        final String end1GUIDParameterName = "teamGUID";
        final String end2GUIDParameterName = "personRoleGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP.typeName,
                                                             teamGUID,
                                                             personRoleGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a schema type.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param schemaTypeGUID       unique identifier of the element
     * @param cascadedDelete         can the schema type be deleted if it has nested components linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSchemaType(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  schemaTypeGUID,
                                   boolean cascadedDelete,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName        = "deleteSchemaType";
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             schemaTypeGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of schema types with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<SchemaTypeElement> getSchemaTypesByName(String              userId,
                                                            String              name,
                                                            TemplateFilter      templateFilter,
                                                            List<ElementStatus> limitResultsByStatus,
                                                            Date                asOfTime,
                                                            SequencingOrder     sequencingOrder,
                                                            String              sequencingProperty,
                                                            int                 startFrom,
                                                            int                 pageSize,
                                                            boolean             forLineage,
                                                            boolean             forDuplicateProcessing,
                                                            Date                effectiveTime) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName        = "getSchemaTypesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                                                      null,
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ, templateFilter),
                                                                                                      limitResultsByStatus,
                                                                                                      asOfTime,
                                                                                                      null,
                                                                                                      sequencingProperty,
                                                                                                      sequencingOrder,
                                                                                                      forLineage,
                                                                                                      forDuplicateProcessing,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertSchemaTypes(userId,
                                    openMetadataElements,
                                    asOfTime,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the properties of a specific schema type.
     *
     * @param userId                 userId of user making request
     * @param schemaTypeGUID       unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SchemaTypeElement getSchemaTypeByGUID(String  userId,
                                                     String  schemaTypeGUID,
                                                     Date    asOfTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "getSchemaTypeByGUID";
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   schemaTypeGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ACTOR_PROFILE.typeName)))
        {
            return convertSchemaType(userId,
                                       openMetadataElement,
                                       asOfTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
        }

        return null;
    }


    /**
     * Return the properties of a specific schema type retrieved using an associated userId.
     *
     * @param userId                 userId of user making request
     * @param requiredUserId         identifier of user
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SchemaTypeElement getSchemaTypeByUserId(String  userId,
                                                       String  requiredUserId,
                                                       Date    asOfTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "getSchemaTypeByUserId";
        final String nameParameterName = "requiredUserId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(requiredUserId, nameParameterName, methodName);

        OpenMetadataElement userIdentityElement = openMetadataStoreClient.getMetadataElementByUniqueName(userId,
                                                                                                         requiredUserId,
                                                                                                         OpenMetadataProperty.USER_ID.name,
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         asOfTime,
                                                                                                         effectiveTime);

        if (userIdentityElement != null)
        {
            RelatedMetadataElement profileElement = openMetadataStoreClient.getRelatedMetadataElement(userId,
                                                                                                      userIdentityElement.getElementGUID(),
                                                                                                      2,
                                                                                                      OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                                                      forLineage,
                                                                                                      forDuplicateProcessing,
                                                                                                      effectiveTime);

            if (profileElement != null)
            {
                return convertSchemaType(userId,
                                           profileElement.getElement(),
                                           asOfTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
            }
        }

        return null;
    }


    /**
     * Retrieve the list of schema types metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned schema types include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaTypes(String              userId,
                                                       String              searchString,
                                                       TemplateFilter      templateFilter,
                                                       List<ElementStatus> limitResultsByStatus,
                                                       Date                asOfTime,
                                                       SequencingOrder     sequencingOrder,
                                                       String              sequencingProperty,
                                                       int                 startFrom,
                                                       int                 pageSize,
                                                       boolean             forLineage,
                                                       boolean             forDuplicateProcessing,
                                                       Date                effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                = "findSchemaTypes";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                templateFilter,
                                                                                                                OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertSchemaTypes(userId,
                                    openMetadataElements,
                                    asOfTime,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /*
     * Mapping functions
     */


    /**
     * Convert the open metadata elements retrieve into schema type elements.
     *
     * @param userId                 calling user
     * @param openMetadataElements   elements extracted from the repository
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          effectivity dating for elements
     * @param methodName             calling method
     * @return list of schema types (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<SchemaTypeElement> convertSchemaTypes(String                    userId,
                                                           List<OpenMetadataElement> openMetadataElements,
                                                           Date                      asOfTime,
                                                           boolean                   forLineage,
                                                           boolean                   forDuplicateProcessing,
                                                           Date                      effectiveTime,
                                                           String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<SchemaTypeElement> schemaTypeElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    SchemaTypeElement schemaTypeElement = convertSchemaType(userId,
                                                                                  openMetadataElement,
                                                                                  asOfTime,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);
                    if (schemaTypeElement != null)
                    {
                        schemaTypeElements.add(schemaTypeElement);
                    }
                }
            }

            return schemaTypeElements;
        }

        return null;
    }


    /**
     * Return the schema type extracted from the open metadata element plus linked solution components.
     * Only convert the roles that either inherit from SolutionSchemaType or are linked to solution components.
     *
     * @param userId                 calling user
     * @param openMetadataElement    element extracted from the repository
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          effectivity dating for elements
     * @param methodName             calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private SchemaTypeElement convertSchemaType(String              userId,
                                                    OpenMetadataElement openMetadataElement,
                                                    Date                asOfTime,
                                                    boolean             forLineage,
                                                    boolean             forDuplicateProcessing,
                                                    Date                effectiveTime,
                                                    String              methodName) throws PropertyServerException
    {
        try
        {
            List<RelatedMetadataElement> relatedMetadataElements = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       0,
                                                                                                                       null,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                relatedMetadataElements.addAll(relatedMetadataElementList.getElementList());

                startFrom                  = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                0,
                                                                                                null,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }


            SchemaTypeConverter<SchemaTypeElement> converter = new SchemaTypeConverter<>(propertyHelper, serviceName, serverName);
            SchemaTypeElement schemaTypeElement = converter.getNewComplexBean(SchemaTypeElement.class,
                                                                                  openMetadataElement,
                                                                                  relatedMetadataElements,
                                                                                  methodName);


            return schemaTypeElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OpenMetadataStoreAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     serviceName,
                                                                                                                     error.getMessage()),
                                      error);
            }

            throw new PropertyServerException(OpenMetadataStoreErrorCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                             methodName,
                                                                                                                             serviceName,
                                                                                                                             error.getMessage()),
                                              error.getClass().getName(),
                                              methodName,
                                              error);
        }
    }



    /*
     * Mapping functions
     */


    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(SchemaTypeProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   properties.getQualifiedName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DISPLAY_NAME.name,
                                                                 properties.getDisplayName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 properties.getDescription());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    properties.getAdditionalProperties());

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              properties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }


    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(ProfileLocationProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.ASSOCIATION_TYPE.name,
                                                    properties.getAssociationType());

        }

        return null;
    }

    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(TeamStructureProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addBooleanProperty(null,
                                                     OpenMetadataProperty.DELEGATION_ESCALATION.name,
                                                     properties.getDelegationEscalationAuthority());

        }

        return null;
    }

    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(TeamMembershipProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.POSITION_NAME.name,
                                                    properties.getPositionName());

        }

        return null;
    }


    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(TeamLeadershipProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.POSITION_NAME.name,
                                                    properties.getPositionName());

        }

        return null;
    }
}
