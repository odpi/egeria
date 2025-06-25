/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.client.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.ConnectorTypeConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
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
 * ConnectorTypeHandler provides methods to define connector types
 * The interface supports the following types of objects
 * <ul>
 *     <li>ConnectorType</li>
 *     <li>PersonRole</li>
 *     <li>TeamRole</li>
 *     <li>ITProfileRole</li>
 *     <li>GovernanceRole</li>
 *     <li>SolutionRole</li>
 * </ul>
 */
public class ConnectorTypeHandler
{
    private final OpenMetadataStoreHandler openMetadataStoreClient;
    private final InvalidParameterHandler  invalidParameterHandler = new InvalidParameterHandler();
    private final AuditLog                 auditLog;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final String serviceName;
    private final String serverName;
    private final boolean createSolutionRoles;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param localServerName       name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @param accessServiceURLMarker which access service to call
     * @param serviceName           local service name
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectorTypeHandler(String   localServerName,
                                String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog,
                                String   accessServiceURLMarker,
                                String   serviceName,
                                int      maxPageSize,
                                boolean  createSolutionRoles) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog = auditLog;
        this.serverName = localServerName;
        this.serviceName = serviceName;
        this.createSolutionRoles = createSolutionRoles;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectorTypeHandler(String   localServerName,
                                String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                AuditLog auditLog,
                                String   accessServiceURLMarker,
                                String   serviceName,
                                int      maxPageSize,
                                boolean  createSolutionRoles) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, userId, password, maxPageSize);
        this.auditLog = auditLog;
        this.serverName = localServerName;
        this.serviceName = serviceName;
        this.createSolutionRoles = createSolutionRoles;
    }


    /**
     * Create a new connector type.
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
    public String createConnectorType(String                  userId,
                                  String                  externalSourceGUID,
                                  String                  externalSourceName,
                                  String                  anchorGUID,
                                  boolean                 isOwnAnchor,
                                  String                  anchorScopeGUID,
                                  ConnectorTypeProperties properties,
                                  String                  parentGUID,
                                  String                  parentRelationshipTypeName,
                                  ElementProperties       parentRelationshipProperties,
                                  boolean                 parentAtEnd1,
                                  boolean                 forLineage,
                                  boolean                 forDuplicateProcessing,
                                  Date                    effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "createConnectorType";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String elementTypeName = OpenMetadataType.ACTOR_ROLE.typeName;

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
     * Create a new metadata element to represent a connector type using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new connector type.
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
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorTypeFromTemplate(String              userId,
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
                                                                         OpenMetadataType.ACTOR_ROLE.typeName,
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
     * Update the properties of a connector type.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param connectorTypeGUID      unique identifier of the connector type (returned from create)
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
    public void updateConnectorType(String                  userId,
                                String                  externalSourceGUID,
                                String                  externalSourceName,
                                String                  connectorTypeGUID,
                                boolean                 replaceAllProperties,
                                ConnectorTypeProperties     properties,
                                boolean                 forLineage,
                                boolean                 forDuplicateProcessing,
                                Date                    effectiveTime) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "updateConnectorType";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             connectorTypeGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }



    /**
     * Attach a person role to a person profile.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPersonRoleToProfile(String                          userId,
                                        String                          externalSourceGUID,
                                        String                          externalSourceName,
                                        String                          personRoleGUID,
                                        String                          personProfileGUID,
                                        PersonRoleAppointmentProperties relationshipProperties,
                                        boolean                         forLineage,
                                        boolean                         forDuplicateProcessing,
                                        Date                            effectiveTime) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName            = "linkPersonRoleToProfile";
        final String end1GUIDParameterName = "personRoleGUID";
        final String end2GUIDParameterName = "personProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personProfileGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                 personRoleGUID,
                                                                 personProfileGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 getElementProperties(relationshipProperties),
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                 personRoleGUID,
                                                                 personProfileGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a person role from a profile.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPersonRoleFromProfile(String  userId,
                                            String  externalSourceGUID,
                                            String  externalSourceName,
                                            String  personRoleGUID,
                                            String  personProfileGUID,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachPersonRoleFromProfile";
        final String end1GUIDParameterName = "personRoleGUID";
        final String end2GUIDParameterName = "personProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(personRoleGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(personProfileGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                             personProfileGUID,
                                                             personProfileGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a team role to a team profile.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamRoleToProfile(String                        userId,
                                      String                        externalSourceGUID,
                                      String                        externalSourceName,
                                      String                        teamRoleGUID,
                                      String                        teamProfileGUID,
                                      TeamRoleAppointmentProperties relationshipProperties,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      Date                          effectiveTime) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName            = "linkTeamRoleToProfile";
        final String end1GUIDParameterName = "teamRoleGUID";
        final String end2GUIDParameterName = "teamProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamRoleGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(teamProfileGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                 teamRoleGUID,
                                                                 teamProfileGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 getElementProperties(relationshipProperties),
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                 teamRoleGUID,
                                                                 teamProfileGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a team role from a team profile.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param teamRoleGUID              unique identifier of the team
     * @param teamProfileGUID          unique identifier of the team profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamRoleFromProfile(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  teamRoleGUID,
                                          String  teamProfileGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachTeamRoleFromProfile";
        final String end1GUIDParameterName = "teamRoleGUID";
        final String end2GUIDParameterName = "teamProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(teamRoleGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(teamProfileGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                             teamProfileGUID,
                                                             teamProfileGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }



    /**
     * Attach an IT profile role to an IT profile.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param itProfileRoleGUID       unique identifier of the IT profile role
     * @param itProfileGUID            unique identifier of the IT profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkITProfileRoleToProfile(String                             userId,
                                           String                             externalSourceGUID,
                                           String                             externalSourceName,
                                           String                             itProfileRoleGUID,
                                           String                             itProfileGUID,
                                           ITProfileRoleAppointmentProperties relationshipProperties,
                                           boolean                            forLineage,
                                           boolean                            forDuplicateProcessing,
                                           Date                               effectiveTime) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "linkITProfileRoleToProfile";
        final String end1GUIDParameterName = "itProfileRoleGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itProfileRoleGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                 itProfileRoleGUID,
                                                                 itProfileGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 relationshipProperties.getEffectiveFrom(),
                                                                 relationshipProperties.getEffectiveTo(),
                                                                 null, // no properties yet
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                 itProfileRoleGUID,
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
     * Detach an IT profile role from an IT profile.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param itProfileRoleGUID      unique identifier of the IT profile role
     * @param itProfileGUID          unique identifier of the IT profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachITProfileRoleFromProfile(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  itProfileRoleGUID,
                                               String  itProfileGUID,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachITProfileRoleFromProfile";
        final String end1GUIDParameterName = "itProfileRoleGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itProfileRoleGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                             itProfileGUID,
                                                             itProfileGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a connector type.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param connectorTypeGUID      unique identifier of the element
     * @param cascadedDelete         can the connector type be deleted if it has solution components linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteConnectorType(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  connectorTypeGUID,
                                boolean cascadedDelete,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName = "deleteConnectorType";
        final String guidParameterName = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             connectorTypeGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of connector types with a particular name.
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
    public List<ConnectorTypeElement> getConnectorTypesByName(String              userId,
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
        final String methodName = "getConnectorTypesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.ACTOR_ROLE.typeName,
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

        return convertConnectorTypes(userId,
                                 openMetadataElements,
                                 asOfTime,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);
    }


    /**
     * Return the properties of a specific connector type.
     *
     * @param userId                 userId of user making request
     * @param connectorTypeGUID      unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ConnectorTypeElement getConnectorTypeByGUID(String  userId,
                                               String  connectorTypeGUID,
                                               Date    asOfTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "getConnectorTypeByGUID";
        final String guidParameterName = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   connectorTypeGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if (openMetadataElement != null)
        {
            return convertConnectorType(userId,
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
     * Retrieve the list of connector types metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned connector types include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> findConnectorTypes(String              userId,
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
        final String methodName = "findConnectorTypes";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                templateFilter,
                                                                                                                OpenMetadataType.ACTOR_ROLE.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertConnectorTypes(userId,
                                 openMetadataElements,
                                 asOfTime,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);
    }


    /**
     * Convert the open metadata elements retrieve into connector type elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of connector types (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<ConnectorTypeElement> convertConnectorTypes(String                    userId,
                                                     List<OpenMetadataElement> openMetadataElements,
                                                     Date                      asOfTime,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     Date                      effectiveTime,
                                                     String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<ConnectorTypeElement> connectorTypeElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    ConnectorTypeElement connectorTypeElement = convertConnectorType(userId,
                                                                         openMetadataElement,
                                                                         asOfTime,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);
                    if (connectorTypeElement != null)
                    {
                        /*
                         * Only save the roles that either inherit from SolutionConnectorType or are linked to solution components.
                         */
                        connectorTypeElements.add(connectorTypeElement);
                    }
                }
            }

            return connectorTypeElements;
        }

        return null;
    }



    /**
     * Return the connector type extracted from the open metadata element plus linked solution components.
     * Only convert the roles that either inherit from SolutionConnectorType or are linked to solution components.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private ConnectorTypeElement convertConnectorType(String              userId,
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

            ConnectorTypeElement connectorTypeElement;

            ConnectorTypeConverter<ConnectorTypeElement> converter = new ConnectorTypeConverter<>(propertyHelper, serviceName, serverName);
            connectorTypeElement = converter.getNewComplexBean(ConnectorTypeElement.class,
                                                               openMetadataElement,
                                                               relatedMetadataElements,
                                                               methodName);


            return connectorTypeElement;
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
    private ElementProperties getElementProperties(ConnectorTypeProperties properties)
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
    private ElementProperties getElementProperties(PersonRoleAppointmentProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addBooleanProperty(null,
                                                                                    OpenMetadataProperty.IS_PUBLIC.name,
                                                                                    properties.getIsPublic());

            return propertyHelper.addIntProperty(elementProperties,
                                                 OpenMetadataProperty.EXPECTED_TIME_ALLOCATION_PERCENT.name,
                                                 properties.getExpectedTimeAllocationPercent());
        }

        return null;
    }


    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(TeamRoleAppointmentProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addIntProperty(null,
                                                 OpenMetadataProperty.EXPECTED_TIME_ALLOCATION_PERCENT.name,
                                                 properties.getExpectedTimeAllocationPercent());
        }

        return null;
    }
}
