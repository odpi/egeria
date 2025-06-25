/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.client.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.SubjectAreaConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SubjectAreaElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
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
 * Subject area manager describes how to maintain and query subject area definitions.
 */
public class SubjectAreaHandler
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
    public SubjectAreaHandler(String   localServerName,
                              String   serverName,
                              String   serverPlatformURLRoot,
                              AuditLog auditLog,
                              String   accessServiceURLMarker,
                              String   serviceName,
                              int      maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog = auditLog;
        this.serverName = localServerName;
        this.serviceName = serviceName;
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param localServerName       name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param accessServiceURLMarker which access service to call
     * @param serviceName           local service name
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SubjectAreaHandler(String   localServerName,
                              String   serverName,
                              String   serverPlatformURLRoot,
                              String   userId,
                              String   password,
                              AuditLog auditLog,
                              String   accessServiceURLMarker,
                              String   serviceName,
                              int      maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, userId, password, maxPageSize);
        this.auditLog = auditLog;
        this.serverName = localServerName;
        this.serviceName = serviceName;
    }


    /**
     * Create a new subject area.
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
    public String createSubjectArea(String                 userId,
                                    String                 externalSourceGUID,
                                    String                 externalSourceName,
                                    String                 anchorGUID,
                                    boolean                isOwnAnchor,
                                    String                 anchorScopeGUID,
                                    SubjectAreaProperties properties,
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
        final String methodName = "createSubjectArea";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String elementTypeName = OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName;

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
     * Create a new metadata element to represent a subject area using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new subject area.
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
    public String createSubjectAreaFromTemplate(String              userId,
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
                                                                         OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
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
     * Update the properties of a subject area.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param subjectAreaGUID      unique identifier of the subject area (returned from create)
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
    public void updateSubjectArea(String                 userId,
                                  String                 externalSourceGUID,
                                  String                 externalSourceName,
                                  String                 subjectAreaGUID,
                                  boolean                replaceAllProperties,
                                  SubjectAreaProperties properties,
                                  boolean                forLineage,
                                  boolean                forDuplicateProcessing,
                                  Date                   effectiveTime) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "updateSubjectArea";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName = "subjectAreaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             subjectAreaGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }


    /**
     * Attach a profile to a subject area.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param subjectAreaGUID        unique identifier of the parent
     * @param nestedSubjectAreaGUID             unique identifier of the actor profile
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubjectAreas(String                    userId,
                                 String                    externalSourceGUID,
                                 String                    externalSourceName,
                                 String                    subjectAreaGUID,
                                 String                    nestedSubjectAreaGUID,
                                 RelationshipProperties relationshipProperties,
                                 boolean                   forLineage,
                                 boolean                   forDuplicateProcessing,
                                 Date                      effectiveTime) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "linkSubjectAreas";
        final String end1GUIDParameterName = "subjectAreaGUID";
        final String end2GUIDParameterName = "nestedSubjectAreaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nestedSubjectAreaGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                                 subjectAreaGUID,
                                                                 nestedSubjectAreaGUID,
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
                                                                 OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                                 subjectAreaGUID,
                                                                 nestedSubjectAreaGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach an actor profile from a subject area.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param subjectAreaGUID       unique identifier of the parent actor profile
     * @param nestedSubjectAreaGUID            unique identifier of the nested actor profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubjectAreas(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  subjectAreaGUID,
                                   String  nestedSubjectAreaGUID,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachSubjectAreas";

        final String end1GUIDParameterName = "subjectAreaGUID";
        final String end2GUIDParameterName = "nestedSubjectAreaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nestedSubjectAreaGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                             subjectAreaGUID,
                                                             nestedSubjectAreaGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a subject area.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param subjectAreaGUID       unique identifier of the element
     * @param cascadedDelete         can the subject area be deleted if it has actor profiles linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSubjectArea(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  subjectAreaGUID,
                                  boolean cascadedDelete,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String methodName = "deleteSubjectArea";
        final String guidParameterName = "subjectAreaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             subjectAreaGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of subject areas with a particular name.
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
    public List<SubjectAreaElement> getSubjectAreasByName(String              userId,
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
        final String methodName = "getSubjectAreasByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.SUBJECT_AREA_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
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

        return convertSubjectAreas(openMetadataElements, methodName);
    }


    /**
     * Return the properties of a specific subject area.
     *
     * @param userId                 userId of user making request
     * @param subjectAreaGUID      unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SubjectAreaElement getSubjectAreaByGUID(String  userId,
                                                   String  subjectAreaGUID,
                                                   Date    asOfTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "getSubjectAreaByGUID";
        final String guidParameterName = "subjectAreaGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   subjectAreaGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName)))
        {
            return convertSubjectArea(openMetadataElement, methodName);
        }

        return null;
    }


    /**
     * Retrieve the list of subject areas metadata elements that contain the search string.
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
    public List<SubjectAreaElement> findSubjectAreas(String              userId,
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
        final String methodName = "findSubjectAreas";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                templateFilter,
                                                                                                                OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertSubjectAreas(openMetadataElements, methodName);
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
    private ElementProperties getElementProperties(SubjectAreaProperties properties)
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
                                                                 OpenMetadataProperty.SUBJECT_AREA_NAME.name,
                                                                 properties.getSubjectAreaName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.USAGE.name,
                                                                 properties.getUsage());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SCOPE.name,
                                                                 properties.getScope());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                              properties.getDomainIdentifier());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    properties.getAdditionalProperties());

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              properties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }


    /*
     * Converter functions
     */


    /**
     * Convert the open metadata elements retrieved into subject area elements.
     *
     * @param openMetadataElements elements extracted from the repository
     * @param methodName calling method
     * @return list of subject areas (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<SubjectAreaElement> convertSubjectAreas(List<OpenMetadataElement> openMetadataElements,
                                                         String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<SubjectAreaElement> subjectAreaElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    subjectAreaElements.add(convertSubjectArea(openMetadataElement, methodName));
                }
            }

            return subjectAreaElements;
        }

        return null;
    }


    /**
     * Return the subject area extracted from the open metadata element.
     *
     * @param openMetadataElement element extracted from the repository
     * @param methodName calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private SubjectAreaElement convertSubjectArea(OpenMetadataElement openMetadataElement,
                                                  String              methodName) throws PropertyServerException
    {
        try
        {
            SubjectAreaConverter<SubjectAreaElement> converter = new SubjectAreaConverter<>(propertyHelper, serviceName, serverName);
            return converter.getNewBean(SubjectAreaElement.class, openMetadataElement, methodName);

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
