/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.client.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.SchemaAttributeConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
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
 * SchemaAttributeHandler provides methods to define schema attributes
 * The interface supports the following types of objects
 * <ul>
 *     <li>SchemaAttribute</li>
 *     <li>Person</li>
 *     <li>Team</li>
 *     <li>ITProfile</li>
 * </ul>
 */
public class SchemaAttributeHandler
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
    public SchemaAttributeHandler(String localServerName,
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
    public SchemaAttributeHandler(String localServerName,
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
     * Create a new schema attribute.
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
    public String createSchemaAttribute(String                 userId,
                                     String                 externalSourceGUID,
                                     String                 externalSourceName,
                                     String                 anchorGUID,
                                     boolean                isOwnAnchor,
                                     String                 anchorScopeGUID,
                                     SchemaAttributeProperties properties,
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
        final String methodName                 = "createSchemaAttribute";
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
     * Create a new metadata element to represent a schema attribute using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema attribute.
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
    public String createSchemaAttributeFromTemplate(String              userId,
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
     * Update the properties of a schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param schemaAttributeGUID       unique identifier of the schema attribute (returned from create)
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
    public void updateSchemaAttribute(String                 userId,
                                   String                 externalSourceGUID,
                                   String                 externalSourceName,
                                   String                 schemaAttributeGUID,
                                   boolean                replaceAllProperties,
                                   SchemaAttributeProperties properties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName                 = "updateSchemaAttribute";
        final String propertiesName             = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName          = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             schemaAttributeGUID,
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
     * @param schemaAttributeGUID       unique identifier of the schema attribute
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
                                      String                    schemaAttributeGUID,
                                      String                    locationGUID,
                                      ProfileLocationProperties relationshipProperties,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName            = "linkLocationToProfile";
        final String end1GUIDParameterName = "schemaAttributeGUID";
        final String end2GUIDParameterName = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                                 schemaAttributeGUID,
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
                                                                 schemaAttributeGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a schema attribute from a location.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param schemaAttributeGUID       unique identifier of the schema attribute
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
                                          String  schemaAttributeGUID,
                                          String  locationGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachLocationFromProfile";

        final String end1GUIDParameterName = "schemaAttributeGUID";
        final String end2GUIDParameterName = "locationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                             locationGUID,
                                                             schemaAttributeGUID,
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
     * Delete a schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param schemaAttributeGUID       unique identifier of the element
     * @param cascadedDelete         can the schema attribute be deleted if it has nested components linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSchemaAttribute(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  schemaAttributeGUID,
                                   boolean cascadedDelete,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName        = "deleteSchemaAttribute";
        final String guidParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             schemaAttributeGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of schema attributes with a particular name.
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
    public List<SchemaAttributeElement> getSchemaAttributesByName(String              userId,
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
        final String methodName        = "getSchemaAttributesByName";
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

        return convertSchemaAttributes(userId,
                                    openMetadataElements,
                                    asOfTime,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the properties of a specific schema attribute.
     *
     * @param userId                 userId of user making request
     * @param schemaAttributeGUID       unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String  userId,
                                                     String  schemaAttributeGUID,
                                                     Date    asOfTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "getSchemaAttributeByGUID";
        final String guidParameterName = "schemaAttributeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   schemaAttributeGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.ACTOR_PROFILE.typeName)))
        {
            return convertSchemaAttribute(userId,
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
     * Return the properties of a specific schema attribute retrieved using an associated userId.
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
    public SchemaAttributeElement getSchemaAttributeByUserId(String  userId,
                                                       String  requiredUserId,
                                                       Date    asOfTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "getSchemaAttributeByUserId";
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
                return convertSchemaAttribute(userId,
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
     * Retrieve the list of schema attributes metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned schema attributes include a list of the components that are associated with it.
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
    public List<SchemaAttributeElement> findSchemaAttributes(String              userId,
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
        final String methodName                = "findSchemaAttributes";
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

        return convertSchemaAttributes(userId,
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
     * Convert the open metadata elements retrieve into schema attribute elements.
     *
     * @param userId                 calling user
     * @param openMetadataElements   elements extracted from the repository
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          effectivity dating for elements
     * @param methodName             calling method
     * @return list of schema attributes (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<SchemaAttributeElement> convertSchemaAttributes(String                    userId,
                                                           List<OpenMetadataElement> openMetadataElements,
                                                           Date                      asOfTime,
                                                           boolean                   forLineage,
                                                           boolean                   forDuplicateProcessing,
                                                           Date                      effectiveTime,
                                                           String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<SchemaAttributeElement> schemaAttributeElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    SchemaAttributeElement schemaAttributeElement = convertSchemaAttribute(userId,
                                                                                  openMetadataElement,
                                                                                  asOfTime,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);
                    if (schemaAttributeElement != null)
                    {
                        schemaAttributeElements.add(schemaAttributeElement);
                    }
                }
            }

            return schemaAttributeElements;
        }

        return null;
    }


    /**
     * Return the schema attribute extracted from the open metadata element plus linked solution components.
     * Only convert the roles that either inherit from SolutionSchemaAttribute or are linked to solution components.
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
    private SchemaAttributeElement convertSchemaAttribute(String              userId,
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


            SchemaAttributeConverter<SchemaAttributeElement> converter = new SchemaAttributeConverter<>(propertyHelper, serviceName, serverName);
            SchemaAttributeElement schemaAttributeElement = converter.getNewComplexBean(SchemaAttributeElement.class,
                                                                                  openMetadataElement,
                                                                                  relatedMetadataElements,
                                                                                  methodName);


            return schemaAttributeElement;
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
    private ElementProperties getElementProperties(SchemaAttributeProperties properties)
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
