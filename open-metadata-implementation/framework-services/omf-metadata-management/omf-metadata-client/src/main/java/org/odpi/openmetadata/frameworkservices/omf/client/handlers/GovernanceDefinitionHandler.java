/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.client.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.GovernanceDefinitionConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.GovernanceDefinitionGraphConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
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
 * GovernanceDefinitionHandler is the handler for managing governance definitions.
 */
public class GovernanceDefinitionHandler
{
    private final OpenMetadataStoreHandler openMetadataStoreClient;
    private final InvalidParameterHandler  invalidParameterHandler = new InvalidParameterHandler();
    private final AuditLog                 auditLog;
    private final String                   serverName;
    private final String                   serviceName;

    private final PropertyHelper propertyHelper = new PropertyHelper();



    /**
     * Create a new client.
     *
     * @param localServerName        name of this server (view server)
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @param serviceName name of calling service
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDefinitionHandler(String   localServerName,
                                       String   serverName,
                                       String   serverPlatformURLRoot,
                                       AuditLog auditLog,
                                       String   accessServiceURLMarker,
                                       String   serviceName,
                                       int      maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog                = auditLog;

        this.serverName = localServerName;
        this.serviceName = serviceName;
    }


    /**
     * Create a new client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @param serviceName name of calling service
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDefinitionHandler(String   localServerName,
                                       String   serverName,
                                       String   serverPlatformURLRoot,
                                       String   localServerUserId,
                                       String   localServerUserPassword,
                                       AuditLog auditLog,
                                       String   accessServiceURLMarker,
                                       String   serviceName,
                                       int      maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, localServerUserId, localServerUserPassword, maxPageSize);
        this.auditLog                = auditLog;

        this.serverName = localServerName;
        this.serviceName = serviceName;
    }


    /**
     * Create a new governance definition.
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
     * @param initialStatus                what is the initial status for the governance definition - default value is DRAFT
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
    public String createGovernanceDefinition(String                         userId,
                                             String                         externalSourceGUID,
                                             String                         externalSourceName,
                                             String                         anchorGUID,
                                             boolean                        isOwnAnchor,
                                             String                         anchorScopeGUID,
                                             GovernanceDefinitionProperties properties,
                                             GovernanceDefinitionStatus     initialStatus,
                                             String                         parentGUID,
                                             String                         parentRelationshipTypeName,
                                             ElementProperties              parentRelationshipProperties,
                                             boolean                        parentAtEnd1,
                                             boolean                        forLineage,
                                             boolean                        forDuplicateProcessing,
                                             Date                           effectiveTime) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createGovernanceDefinition";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);
        invalidParameterHandler.validateName(properties.getDocumentIdentifier(), qualifiedNameParameterName, methodName);

        String elementTypeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (properties.getTypeName() != null)
        {
            elementTypeName = properties.getTypeName();
        }


        return openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    elementTypeName,
                                                                    this.getElementStatus(initialStatus),
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
     * Return the corresponding element status.
     *
     * @param governanceDefinitionStatus status that the governance definition can be set
     * @return element status
     */
    private ElementStatus getElementStatus(GovernanceDefinitionStatus governanceDefinitionStatus)
    {
        if (governanceDefinitionStatus != null)
        {
            switch (governanceDefinitionStatus)
            {
                case DRAFT -> { return ElementStatus.DRAFT; }
                case PROPOSED -> { return ElementStatus.PROPOSED; }
                case APPROVED -> { return ElementStatus.APPROVED; }
                case REJECTED -> { return ElementStatus.REJECTED; }
                case ACTIVE -> { return ElementStatus.ACTIVE; }
                case DEPRECATED -> { return ElementStatus.DEPRECATED; }
                case OTHER -> { return ElementStatus.OTHER; }
            }
        }

        return ElementStatus.ACTIVE;
    }


    /**
     * Create a new metadata element to represent a governance definition using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new governance definition.
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
    public String createGovernanceDefinitionFromTemplate(String              userId,
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
                                                                         OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
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
     * Update the properties of a governance definition.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param governanceDefinitionGUID      unique identifier of the governance definition (returned from create)
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
    public void updateGovernanceDefinition(String                  userId,
                                           String                  externalSourceGUID,
                                           String                  externalSourceName,
                                           String                  governanceDefinitionGUID,
                                           boolean                 replaceAllProperties,
                                           GovernanceDefinitionProperties properties,
                                           boolean                 forLineage,
                                           boolean                 forDuplicateProcessing,
                                           Date                    effectiveTime) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "updateGovernanceDefinition";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getDocumentIdentifier(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             governanceDefinitionGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }


    /**
     * Update the properties of a governance definition.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param governanceDefinitionGUID      unique identifier of the governance definition (returned from create)
     * @param status                 new status for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGovernanceDefinitionStatus(String                     userId,
                                                 String                     externalSourceGUID,
                                                 String                     externalSourceName,
                                                 String                     governanceDefinitionGUID,
                                                 GovernanceDefinitionStatus status,
                                                 boolean                    forLineage,
                                                 boolean                    forDuplicateProcessing,
                                                 Date                       effectiveTime) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateGovernanceDefinition";
        final String propertiesName = "status";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(status, propertiesName, methodName);

        openMetadataStoreClient.updateMetadataElementStatusInStore(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   governanceDefinitionGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   this.getElementStatus(status),
                                                                   effectiveTime);
    }


    /**
     * Attach two peer governance definitions.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param relationshipProperties  additional properties for the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerDefinitions(String                    userId,
                                    String                    externalSourceGUID,
                                    String                    externalSourceName,
                                    String                    governanceDefinitionOneGUID,
                                    String                    governanceDefinitionTwoGUID,
                                    String                    relationshipTypeName,
                                    PeerDefinitionProperties  relationshipProperties,
                                    boolean                   forLineage,
                                    boolean                   forDuplicateProcessing,
                                    Date                      effectiveTime) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "linkPeerDefinitions";
        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 relationshipTypeName,
                                                                 governanceDefinitionOneGUID,
                                                                 governanceDefinitionTwoGUID,
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
                                                                 relationshipTypeName,
                                                                 governanceDefinitionOneGUID,
                                                                 governanceDefinitionTwoGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a governance definition from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerDefinitions(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  governanceDefinitionOneGUID,
                                      String  governanceDefinitionTwoGUID,
                                      String  relationshipTypeName,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "detachMemberDataField";

        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             relationshipTypeName,
                                                             governanceDefinitionOneGUID,
                                                             governanceDefinitionTwoGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a supporting governance definition.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param relationshipProperties  additional properties for the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachSupportingDefinition(String                         userId,
                                           String                         externalSourceGUID,
                                           String                         externalSourceName,
                                           String                         governanceDefinitionOneGUID,
                                           String                         governanceDefinitionTwoGUID,
                                           String                         relationshipTypeName,
                                           SupportingDefinitionProperties relationshipProperties,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing,
                                           Date                           effectiveTime) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "attachSupportingDefinition";
        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 relationshipTypeName,
                                                                 governanceDefinitionOneGUID,
                                                                 governanceDefinitionTwoGUID,
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
                                                                 relationshipTypeName,
                                                                 governanceDefinitionOneGUID,
                                                                 governanceDefinitionTwoGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a governance definition from a supporting governance definition.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSupportingDefinition(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  governanceDefinitionOneGUID,
                                           String  governanceDefinitionTwoGUID,
                                           String  relationshipTypeName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachSupportingDefinition";

        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             relationshipTypeName,
                                                             governanceDefinitionOneGUID,
                                                             governanceDefinitionTwoGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a governance definition.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param governanceDefinitionGUID      unique identifier of the element
     * @param cascadedDelete         can the governance definition be deleted if it has data fields linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGovernanceDefinition(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  governanceDefinitionGUID,
                                           boolean cascadedDelete,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "deleteGovernanceDefinition";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             governanceDefinitionGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of governance definitions with a particular name.
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
    public List<GovernanceDefinitionElement> getGovernanceDefinitionsByName(String              userId,
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
        final String methodName = "getGovernanceDefinitionsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
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

        return convertGovernanceDefinitions(openMetadataElements, methodName);
    }


    /**
     * Retrieve the list of governance definitions metadata elements that contain the search string.
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
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GovernanceDefinitionElement> findGovernanceDefinitions(String              userId,
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
        final String methodName = "findGovernanceDefinitions";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                templateFilter,
                                                                                                                OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertGovernanceDefinitions(openMetadataElements, methodName);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param userId                 userId of user making request
     * @param governanceDefinitionGUID      unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GovernanceDefinitionElement getGovernanceDefinitionByGUID(String  userId,
                                                                     String  governanceDefinitionGUID,
                                                                     Date    asOfTime,
                                                                     boolean forLineage,
                                                                     boolean forDuplicateProcessing,
                                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getGovernanceDefinitionByGUID";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   governanceDefinitionGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.GOVERNANCE_DEFINITION.typeName)))
        {
            return convertGovernanceDefinition(openMetadataElement, methodName);
        }

        return null;
    }


    /**
     * Retrieve the definition metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDefinitionGraph getGovernanceDefinitionInContext(String              userId,
                                                                      String              guid,
                                                                      List<ElementStatus> limitResultsByStatus,
                                                                      Date                asOfTime,
                                                                      String              sequencingProperty,
                                                                      SequencingOrder     sequencingOrder,
                                                                      boolean             forLineage,
                                                                      boolean             forDuplicateProcessing,
                                                                      Date                effectiveTime,
                                                                      int                 startFrom,
                                                                      int                 pageSize) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "getGovernanceDefinitionInContext";

        OpenMetadataElement entity = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                      guid,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      asOfTime,
                                                                                      effectiveTime);

        if (entity != null)
        {
            RelatedMetadataElementList relationships = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                          guid,
                                                                                                          0,
                                                                                                          null,
                                                                                                          limitResultsByStatus,
                                                                                                          asOfTime,
                                                                                                          sequencingProperty,
                                                                                                          sequencingOrder,
                                                                                                          forLineage,
                                                                                                          forDuplicateProcessing,
                                                                                                          effectiveTime,
                                                                                                          startFrom,
                                                                                                          pageSize);

            GovernanceDefinitionGraphConverter<GovernanceDefinitionGraph> converter = new GovernanceDefinitionGraphConverter<>(propertyHelper, serviceName, serverName);

            return converter.getNewComplexBean(GovernanceDefinitionGraph.class, entity, relationships, methodName);
        }

        return null;
    }


    /*
     * Design to implementations
     */



    /**
     * Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationGUID      unique identifier of the implementation
     * @param relationshipProperties  additional properties for the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDesignToImplementation(String                  userId,
                                           String                  externalSourceGUID,
                                           String                  externalSourceName,
                                           String                  designGUID,
                                           String                  implementationGUID,
                                           ImplementedByProperties relationshipProperties,
                                           boolean                 forLineage,
                                           boolean                 forDuplicateProcessing,
                                           Date                    effectiveTime) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "linkDesignToImplementation";
        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(designGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(implementationGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                 designGUID,
                                                                 implementationGUID,
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
                                                                 OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                 designGUID,
                                                                 implementationGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a design object such as a solution component or governance definition from its implementation.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDesignFromImplementation(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  designGUID,
                                               String  implementationGUID,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachDefinitionImplementation";

        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(designGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(implementationGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                             designGUID,
                                                             implementationGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationResourceGUID      unique identifier of the implementation
     * @param relationshipProperties  additional properties for the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkImplementationResource(String                           userId,
                                           String                           externalSourceGUID,
                                           String                           externalSourceName,
                                           String                           designGUID,
                                           String                           implementationResourceGUID,
                                           ImplementationResourceProperties relationshipProperties,
                                           boolean                          forLineage,
                                           boolean                          forDuplicateProcessing,
                                           Date                             effectiveTime) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkImplementationResource";
        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationResourceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(designGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(implementationResourceGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                                 designGUID,
                                                                 implementationResourceGUID,
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
                                                                 OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                                 designGUID,
                                                                 implementationResourceGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a design object such as a solution component or governance definition from one of its implementation resources.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationResourceGUID     unique identifier of the implementation
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachImplementationResource(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  designGUID,
                                             String  implementationResourceGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachImplementationResource";

        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationResourceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(designGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(implementationResourceGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                             designGUID,
                                                             implementationResourceGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }

    /*
     * Mapping
     */

    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(GovernanceDefinitionProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   properties.getDocumentIdentifier());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.TITLE.name,
                                                                 properties.getTitle());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 properties.getDescription());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SUMMARY.name,
                                                                 properties.getSummary());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SCOPE.name,
                                                                 properties.getScope());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.IMPORTANCE.name,
                                                                 properties.getImportance());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                              properties.getDomainIdentifier());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.IMPLICATIONS.name,
                                                                      properties.getImplications());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.OUTCOMES.name,
                                                                      properties.getOutcomes());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.RESULTS.name,
                                                                      properties.getResults());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    properties.getAdditionalProperties());

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              properties.getExtendedProperties());

            if (properties instanceof GovernanceStrategyProperties governanceStrategyProperties)
            {
                elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                          OpenMetadataProperty.BUSINESS_IMPERATIVES.name,
                                                                          governanceStrategyProperties.getBusinessImperatives());
            }
            else if (properties instanceof GovernanceControlProperties governanceControlProperties)
            {
                if (properties instanceof SecurityGroupProperties securityGroupProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                                         securityGroupProperties.getDistinguishedName());
                }
                else if (properties instanceof NamingStandardRuleProperties namingStandardRuleProperties)
                {
                    elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                              OpenMetadataProperty.NAME_PATTERNS.name,
                                                                              namingStandardRuleProperties.getNamePatterns());
                }

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.IMPLEMENTATION_DESCRIPTION.name,
                                                                     governanceControlProperties.getImplementationDescription());
            }
            else if (properties instanceof RegulationProperties regulationProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.JURISDICTION.name,
                                                                     regulationProperties.getJurisdiction());
            }
            else if (properties instanceof LicenseTypeProperties licenceTypeProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DETAILS.name,
                                                                     licenceTypeProperties.getDetails());
            }
            else if (properties instanceof CertificationTypeProperties certificationTypeProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.DETAILS.name,
                                                                     certificationTypeProperties.getDetails());
            }

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
    private ElementProperties getElementProperties(PeerDefinitionProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.DESCRIPTION.name,
                                                    properties.getDescription());
        }

        return null;
    }


    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(SupportingDefinitionProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.RATIONALE.name,
                                                    properties.getRationale());
        }

        return null;
    }


    /**
     * Convert the specific properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied properties
     * @return element properties
     */
    private ElementProperties getElementProperties(ImplementedByProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.DESIGN_STEP.name,
                                                                                   properties.getDesignStep());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.ROLE.name,
                                                                 properties.getRole());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.TRANSFORMATION.name,
                                                                 properties.getTransformation());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 properties.getDescription());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                 properties.getISCQualifiedName());

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
    private ElementProperties getElementProperties(ImplementationResourceProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.DESCRIPTION.name,
                                                    properties.getDescription());

        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieved into governance definition elements.
     *
     * @param openMetadataElements elements extracted from the repository
     * @param methodName calling method
     * @return list of data fields (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<GovernanceDefinitionElement> convertGovernanceDefinitions(List<OpenMetadataElement> openMetadataElements,
                                                                           String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<GovernanceDefinitionElement> governanceDefinitionElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    governanceDefinitionElements.add(convertGovernanceDefinition(openMetadataElement, methodName));
                }
            }

            return governanceDefinitionElements;
        }

        return null;
    }


    /**
     * Return the governance definition extracted from the open metadata element.
     *
     * @param openMetadataElement element extracted from the repository
     * @param methodName calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private GovernanceDefinitionElement convertGovernanceDefinition(OpenMetadataElement openMetadataElement, String              methodName) throws PropertyServerException
    {
        try
        {
            GovernanceDefinitionConverter<GovernanceDefinitionElement> converter = new GovernanceDefinitionConverter<>(propertyHelper, serviceName, serverName);
            return converter.getNewBean(GovernanceDefinitionElement.class, openMetadataElement, methodName);
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
}
