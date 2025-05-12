/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.designmodel.client;

import org.odpi.openmetadata.accessservices.designmodel.api.DataDesignInterface;
import org.odpi.openmetadata.accessservices.designmodel.ffdc.DesignModelAuditCode;
import org.odpi.openmetadata.commonservices.mermaid.DataClassMermaidGraphBuilder;
import org.odpi.openmetadata.commonservices.mermaid.DataFieldMermaidGraphBuilder;
import org.odpi.openmetadata.commonservices.mermaid.DataStructureMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.converters.DataClassConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.DataFieldConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.DataStructureConverter;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataClassElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFieldElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataStructureElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MemberDataField;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * Data design manager describes how to maintain and query data classes, data fields and data structures.
 * They are organized into specialized collections called data dictionaries and data specs (supported with the
 * collection manager).
 */
public class DataDesignManager extends DesignModelClientBase implements DataDesignInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public DataDesignManager(String   serverName,
                             String   serverPlatformURLRoot,
                             int      maxPageSize,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
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
    public DataDesignManager(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             int      maxPageSize,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new data structure.
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
    @Override
    public String createDataStructure(String                  userId,
                                      String                  externalSourceGUID,
                                      String                  externalSourceName,
                                      String                  anchorGUID,
                                      boolean                 isOwnAnchor,
                                      String                  anchorScopeGUID,
                                      DataStructureProperties properties,
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
        final String methodName = "createDataStructure";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String elementTypeName = OpenMetadataType.DATA_STRUCTURE.typeName;

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
                                                                    null,
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
     * Create a new metadata element to represent a data structure using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new data structure.
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
    @Override
    public String createDataStructureFromTemplate(String              userId,
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
                                                                         OpenMetadataType.DATA_STRUCTURE.typeName,
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
     * Update the properties of a data structure.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataStructureGUID      unique identifier of the data structure (returned from create)
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
    @Override
    public void updateDataStructure(String                  userId,
                                    String                  externalSourceGUID,
                                    String                  externalSourceName,
                                    String                  dataStructureGUID,
                                    boolean                 replaceAllProperties,
                                    DataStructureProperties properties,
                                    boolean                 forLineage,
                                    boolean                 forDuplicateProcessing,
                                    Date                    effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateDataStructure";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName = "dataStructureGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataStructureGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             dataStructureGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }


    /**
     * Attach a data field to a data structure.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param parentDataStructureGUID unique identifier of the parent
     * @param memberDataFieldGUID     unique identifier of the data field
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void linkMemberDataField(String                    userId,
                                    String                    externalSourceGUID,
                                    String                    externalSourceName,
                                    String                    parentDataStructureGUID,
                                    String                    memberDataFieldGUID,
                                    MemberDataFieldProperties relationshipProperties,
                                    boolean                   forLineage,
                                    boolean                   forDuplicateProcessing,
                                    Date                      effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkMemberDataField";
        final String end1GUIDParameterName = "parentDataStructureGUID";
        final String end2GUIDParameterName = "memberDataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataStructureGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(memberDataFieldGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                                 parentDataStructureGUID,
                                                                 memberDataFieldGUID,
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
                                                                 OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                                 parentDataStructureGUID,
                                                                 memberDataFieldGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a data field from a data structure.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param parentDataStructureGUID    unique identifier of the parent data field.
     * @param nestedDataFieldGUID    unique identifier of the nested data field.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachMemberDataField(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  parentDataStructureGUID,
                                      String  nestedDataFieldGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachMemberDataField";

        final String end1GUIDParameterName = "parentDataStructureGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataStructureGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nestedDataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                             parentDataStructureGUID,
                                                             nestedDataFieldGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a data structure.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataStructureGUID      unique identifier of the element
     * @param cascadedDelete         can the data structure be deleted if it has data fields linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void deleteDataStructure(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  dataStructureGUID,
                                    boolean cascadedDelete,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "deleteDataStructure";
        final String guidParameterName = "dataStructureGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataStructureGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             dataStructureGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of data structures with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
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
    @Override
    public List<DataStructureElement> getDataStructuresByName(String userId, String name, List<ElementStatus> limitResultsByStatus, Date asOfTime, SequencingOrder sequencingOrder, String sequencingProperty, int startFrom, int pageSize, boolean forLineage, boolean forDuplicateProcessing, Date effectiveTime) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "getDataStructuresByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.DATA_STRUCTURE.typeName,
                                                                                                      null,
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
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

        return convertDataStructures(userId,
                                     openMetadataElements,
                                     asOfTime,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Return the properties of a specific data structure.
     *
     * @param userId                 userId of user making request
     * @param dataStructureGUID      unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public DataStructureElement getDataStructureByGUID(String  userId,
                                                       String  dataStructureGUID,
                                                       Date    asOfTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "getDataStructureByGUID";
        final String guidParameterName = "dataStructureGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataStructureGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   dataStructureGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_STRUCTURE.typeName)))
        {
            return convertDataStructure(userId,
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
     * Retrieve the list of data structures metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
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
    @Override
    public List<DataStructureElement> findDataStructures(String              userId,
                                                         String              searchString,
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
        final String methodName = "findDataStructures";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.DATA_STRUCTURE.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertDataStructures(userId,
                                     openMetadataElements,
                                     asOfTime,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Create a new data field.
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
    @Override
    public String createDataField(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              anchorGUID,
                                  boolean             isOwnAnchor,
                                  String              anchorScopeGUID,
                                  DataFieldProperties properties,
                                  String              parentGUID,
                                  String              parentRelationshipTypeName,
                                  ElementProperties   parentRelationshipProperties,
                                  boolean             parentAtEnd1,
                                  boolean             forLineage,
                                  boolean             forDuplicateProcessing,
                                  Date                effectiveTime) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "createDataField";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String elementTypeName = OpenMetadataType.DATA_FIELD.typeName;

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
                                                                    null,
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
     * Create a new metadata element to represent a data field using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new data field.
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
    @Override
    public String createDataFieldFromTemplate(String              userId,
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
                                                                         OpenMetadataType.DATA_FIELD.typeName,
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
     * Update the properties of a data field.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataFieldGUID          unique identifier of the data field (returned from create)
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
    @Override
    public void updateDataField(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              dataFieldGUID,
                                boolean             replaceAllProperties,
                                DataFieldProperties properties,
                                boolean             forLineage,
                                boolean             forDuplicateProcessing,
                                Date                effectiveTime) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "updateDataField";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             dataFieldGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }


    /**
     * Connect two data field as parent and child.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param parentDataFieldGUID    unique identifier of the parent data field
     * @param nestedDataFieldGUID    unique identifier of the child data field
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void linkNestedDataFields(String                    userId,
                                     String                    externalSourceGUID,
                                     String                    externalSourceName,
                                     String                    parentDataFieldGUID,
                                     String                    nestedDataFieldGUID,
                                     MemberDataFieldProperties relationshipProperties,
                                     boolean                   forLineage,
                                     boolean                   forDuplicateProcessing,
                                     Date                      effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkNestedDataFields";
        final String end1GUIDParameterName = "parentDataFieldGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataFieldGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nestedDataFieldGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                 parentDataFieldGUID,
                                                                 nestedDataFieldGUID,
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
                                                                 OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                 parentDataFieldGUID,
                                                                 nestedDataFieldGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach two data fields from one another.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param parentDataFieldGUID    unique identifier of the parent data field.
     * @param nestedDataFieldGUID    unique identifier of the child data field.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachNestedDataFields(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  parentDataFieldGUID,
                                       String  nestedDataFieldGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachNestedDataFields";
        final String end1GUIDParameterName = "parentDataFieldGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataFieldGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nestedDataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                             parentDataFieldGUID,
                                                             nestedDataFieldGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a data field.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataFieldGUID          unique identifier of the element
     * @param cascadedDelete         should the data field delete nested data fields?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void deleteDataField(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  dataFieldGUID,
                                boolean cascadedDelete,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName = "deleteDataField";
        final String guidParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             dataFieldGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of data fields with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
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
    @Override
    public List<DataFieldElement> getDataFieldsByName(String              userId,
                                                      String              name,
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
        final String methodName = "getDataFieldsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.DATA_FIELD.typeName,
                                                                                                      null,
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
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

        return convertDataFields(userId,
                                 openMetadataElements,
                                 asOfTime,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);
    }


    /**
     * Return the properties of a specific data field.
     *
     * @param userId                 userId of user making request
     * @param dataFieldGUID          unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public DataFieldElement getDataFieldByGUID(String  userId,
                                               String  dataFieldGUID,
                                               Date    asOfTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "getDataFieldByGUID";
        final String guidParameterName = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   dataFieldGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_FIELD.typeName)))
        {
            return convertDataField(userId,
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
     * Retrieve the list of data fields metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
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
    @Override
    public List<DataFieldElement> findDataFields(String              userId,
                                                 String              searchString,
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
        final String methodName = "findDataFields";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.DATA_FIELD.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertDataFields(userId,
                                 openMetadataElements,
                                 asOfTime,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);
    }


    /**
     * Create a new data class.
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
    @Override
    public String createDataClass(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              anchorGUID,
                                  boolean             isOwnAnchor,
                                  String              anchorScopeGUID,
                                  DataClassProperties properties,
                                  String              parentGUID,
                                  String              parentRelationshipTypeName,
                                  ElementProperties   parentRelationshipProperties,
                                  boolean             parentAtEnd1,
                                  boolean             forLineage,
                                  boolean             forDuplicateProcessing,
                                  Date                effectiveTime) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "createDataClass";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String elementTypeName = OpenMetadataType.DATA_CLASS.typeName;

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
                                                                    null,
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
     * Create a new metadata element to represent a data class using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new data class.
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
    @Override
    public String createDataClassFromTemplate(String              userId,
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
                                                                         OpenMetadataType.DATA_CLASS.typeName,
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
     * Update the properties of a data class.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataClassGUID          unique identifier of the data class (returned from create)
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
    @Override
    public void updateDataClass(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              dataClassGUID,
                                boolean             replaceAllProperties,
                                DataClassProperties properties,
                                boolean             forLineage,
                                boolean             forDuplicateProcessing,
                                Date                effectiveTime) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "updateDataClass";
        final String propertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";
        final String guidParameterName = "dataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataClassGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             dataClassGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }


    /**
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param parentDataClassGUID    unique identifier of the parent data class
     * @param childDataClassGUID     unique identifier of the chile data class
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void linkNestedDataClass(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  parentDataClassGUID,
                                    String  childDataClassGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkNestedDataClass";
        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_CLASS_COMPOSITION.typeName,
                                                             parentDataClassGUID,
                                                             childDataClassGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             null,
                                                             null,
                                                             effectiveTime);
    }


    /**
     * Detach two nested data classes from one another.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param parentDataClassGUID    unique identifier of the  parent data class.
     * @param childDataClassGUID     unique identifier of the child data class.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachNestedDataClass(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  parentDataClassGUID,
                                      String  childDataClassGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "detachNestedDataClass";

        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_CLASS_COMPOSITION.typeName,
                                                             parentDataClassGUID,
                                                             childDataClassGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Connect two data classes to show that one provides a more specialist evaluation.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param parentDataClassGUID    unique identifier of the more generic data class
     * @param childDataClassGUID     unique identifier of the more specialized data class
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void linkSpecialistDataClass(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  parentDataClassGUID,
                                        String  childDataClassGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkSpecialistDataClass";
        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_CLASS_HIERARCHY.typeName,
                                                             parentDataClassGUID,
                                                             childDataClassGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             null,
                                                             null,
                                                             effectiveTime);
    }


    /**
     * Detach two data classes from one another.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param parentDataClassGUID    unique identifier of the more generic data class
     * @param childDataClassGUID     unique identifier of the more specialized
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachSpecialistDataClass(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  parentDataClassGUID,
                                          String  childDataClassGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachSpecialistDataClass";

        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_CLASS_HIERARCHY.typeName,
                                                             parentDataClassGUID,
                                                             childDataClassGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a data class.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataClassGUID          unique identifier of the element
     * @param cascadedDelete         can the data class be deleted if other data classes are attached?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void deleteDataClass(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  dataClassGUID,
                                boolean cascadedDelete,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName = "deleteDataClass";
        final String guidParameterName = "dataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataClassGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             dataClassGUID,
                                                             cascadedDelete,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Returns the list of data classes with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
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
    @Override
    public List<DataClassElement> getDataClassesByName(String              userId,
                                                       String              name,
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
        final String methodName = "getDataClassesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.DATA_CLASS.typeName,
                                                                                                      null,
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
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

        return convertDataClasses(userId,
                                  openMetadataElements,
                                  asOfTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return the properties of a specific data class.
     *
     * @param userId                 userId of user making request
     * @param dataClassGUID          unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public DataClassElement getDataClassByGUID(String  userId,
                                               String  dataClassGUID,
                                               Date    asOfTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "getDataClassByGUID";
        final String guidParameterName = "dataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataClassGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   dataClassGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.DATA_CLASS.typeName)))
        {
            return convertDataClass(userId,
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
     * Retrieve the list of data classes metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
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
    @Override
    public List<DataClassElement> findDataClasses(String              userId,
                                                  String              searchString,
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
        final String methodName = "findDataClasses";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.DATA_CLASS.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertDataClasses(userId,
                                  openMetadataElements,
                                  asOfTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Connect an element that is part of a data design to a data class to show that the data class should be used
     * as the specification for the data values when interpreting the data definition.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void linkDataClassDefinition(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  dataDefinitionGUID,
                                        String  dataClassGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkDataClassDefinition";
        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "dataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataClassGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_CLASS_DEFINITION.typeName,
                                                             dataDefinitionGUID,
                                                             dataClassGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             null,
                                                             null,
                                                             effectiveTime);
    }


    /**
     * Detach a data definition from a data class.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachDataClassDefinition(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  dataDefinitionGUID,
                                          String  dataClassGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachDataClassDefinition";

        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "dataClassGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataClassGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_CLASS_DEFINITION.typeName,
                                                             dataDefinitionGUID,
                                                             dataClassGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Connect an element that is part of a data design to a glossary term to show that the term should be used
     * as the semantic definition for the data values when interpreting the data definition.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void linkSemanticDefinition(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  dataDefinitionGUID,
                                       String  glossaryTermGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkSemanticDefinition";
        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName,
                                                             dataDefinitionGUID,
                                                             glossaryTermGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             null,
                                                             null,
                                                             effectiveTime);
    }


    /**
     * Detach a data definition from a glossary term.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachSemanticDefinition(String  userId,
                                         String  externalSourceGUID,
                                         String  externalSourceName,
                                         String  dataDefinitionGUID,
                                         String  glossaryTermGUID,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "detachSemanticDefinition";

        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "glossaryTermGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName,
                                                             dataDefinitionGUID,
                                                             glossaryTermGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Connect a certification type to a data structure to guide the survey action service (that checks the data
     * quality of a data resource as part of certifying it with the supplied certification type) to the definition
     * of the data structure to use as a specification of how the data should be both structured and (if
     * data classes are attached to the associated data fields using the DataClassDefinition relationship)
     * contain the valid values.
     *
     * @param userId                 userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void linkCertificationTypeToDataStructure(String  userId,
                                                     String  externalSourceGUID,
                                                     String  externalSourceName,
                                                     String  certificationTypeGUID,
                                                     String  dataStructureGUID,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "linkCertificationTypeToDataStructure";
        final String end1GUIDParameterName = "certificationTypeGUID";
        final String end2GUIDParameterName = "dataStructureGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataStructureGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP.typeName,
                                                             certificationTypeGUID,
                                                             dataStructureGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             null,
                                                             null,
                                                             effectiveTime);
    }


    /**
     * Detach a data structure from a certification type.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachCertificationTypeToDataStructure(String  userId,
                                                       String  externalSourceGUID,
                                                       String  externalSourceName,
                                                       String  certificationTypeGUID,
                                                       String  dataStructureGUID,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachCertificationTypeToDataStructure";

        final String end1GUIDParameterName = "certificationTypeGUID";
        final String end2GUIDParameterName = "dataStructureGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(certificationTypeGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataStructureGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP.typeName,
                                                             certificationTypeGUID,
                                                             dataStructureGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
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
    private ElementProperties getElementProperties(DataStructureProperties properties)
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

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAMESPACE.name,
                                                                 properties.getNamespace());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                 properties.getVersionIdentifier());

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
    private ElementProperties getElementProperties(DataFieldProperties properties)
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

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAMESPACE.name,
                                                                 properties.getNamespace());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                 properties.getVersionIdentifier());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.ALIASES.name,
                                                                      properties.getAliases());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.NAME_PATTERNS.name,
                                                                      properties.getNamePatterns());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.IS_DEPRECATED.name,
                                                                  properties.getIsDeprecated());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                 properties.getDefaultValue());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.IS_NULLABLE.name,
                                                                  properties.getIsNullable());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DATA_TYPE.name,
                                                                 properties.getDataType());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.MINIMUM_LENGTH.name,
                                                              properties.getMinimumLength());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.LENGTH.name,
                                                              properties.getLength());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.PRECISION.name,
                                                              properties.getPrecision());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.ORDERED_VALUES.name,
                                                                  properties.getOrderedValues());

            if (properties.getSortOrder() != null)
            {
                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                   OpenMetadataProperty.SORT_ORDER.name,
                                                                   DataItemSortOrder.getOpenTypeName(),
                                                                   properties.getSortOrder().getName());
            }

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
    private ElementProperties getElementProperties(MemberDataFieldProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addIntProperty(null,
                                                                                OpenMetadataProperty.POSITION.name,
                                                                                properties.getDataFieldPosition());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.MAX_CARDINALITY.name,
                                                              properties.getMaxCardinality());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.MIN_CARDINALITY.name,
                                                              properties.getMaxCardinality());

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
    private ElementProperties getElementProperties(DataClassProperties properties)
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

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAMESPACE.name,
                                                                 properties.getNamespace());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.MATCH_PROPERTY_NAMES.name,
                                                                      properties.getMatchPropertyNames());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                                OpenMetadataProperty.MATCH_THRESHOLD.name,
                                                                properties.getMatchThreshold());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SPECIFICATION.name,
                                                                 properties.getSpecification());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.SPECIFICATION_DETAILS.name,
                                                                    properties.getSpecificationDetails());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DATA_TYPE.name,
                                                                 properties.getDataType());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.ALLOWS_DUPLICATE_VALUES.name,
                                                                  properties.getAllowsDuplicateValues());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                                  properties.getIsCaseSensitive());

            elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                                  OpenMetadataProperty.IS_NULLABLE.name,
                                                                  properties.getIsNullable());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DEFAULT_VALUE.name,
                                                                 properties.getDefaultValue());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.AVERAGE_VALUE.name,
                                                                 properties.getAverageValue());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.VALUE_LIST.name,
                                                                      properties.getValueList());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                                 properties.getValueRangeFrom());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                                 properties.getValueRangeTo());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.SAMPLE_VALUES.name,
                                                                      properties.getSampleValues());

            elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                      OpenMetadataProperty.DATA_PATTERNS.name,
                                                                      properties.getDataPatterns());

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
     * Convert the open metadata elements retrieved into data structure elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of data structures (or null)
     */
    private List<DataStructureElement> convertDataStructures(String                    userId,
                                                             List<OpenMetadataElement> openMetadataElements,
                                                             Date                      asOfTime,
                                                             boolean                   forLineage,
                                                             boolean                   forDuplicateProcessing,
                                                             Date                      effectiveTime,
                                                             String                    methodName)
    {
        if (openMetadataElements != null)
        {
            List<DataStructureElement> dataStructureElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    dataStructureElements.add(convertDataStructure(userId,
                                                                   openMetadataElement,
                                                                   asOfTime,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName));
                }
            }

            return dataStructureElements;
        }

        return null;
    }



    /**
     * Return the data structure extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private DataStructureElement convertDataStructure(String              userId,
                                                      OpenMetadataElement openMetadataElement,
                                                      Date                asOfTime,
                                                      boolean             forLineage,
                                                      boolean             forDuplicateProcessing,
                                                      Date                effectiveTime,
                                                      String              methodName)
    {
        try
        {
            List<MemberDataField>        relatedFields        = new ArrayList<>();
            List<RelatedMetadataElement> otherRelatedElements = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       1,
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
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName))
                        {
                            relatedFields.add(this.convertMemberDataField(userId,
                                                                          relatedMetadataElement,
                                                                          asOfTime,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName));
                        }
                        else
                        {
                            otherRelatedElements.add(relatedMetadataElement);
                        }
                    }
                }

                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                1,
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


            DataStructureConverter<DataStructureElement> converter = new DataStructureConverter<>(propertyHelper, serviceName, serverName);
            DataStructureElement dataStructureElement = converter.getNewComplexBean(DataStructureElement.class,
                                                                                    openMetadataElement,
                                                                                    otherRelatedElements,
                                                                                    methodName);
            if (dataStructureElement != null)
            {
                if (! relatedFields.isEmpty())
                {
                    dataStructureElement.setMemberDataFields(relatedFields);
                }

                DataStructureMermaidGraphBuilder graphBuilder = new DataStructureMermaidGraphBuilder(dataStructureElement);

                dataStructureElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return dataStructureElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DesignModelAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()));
            }

            return null;
        }
    }


    /**
     * Convert the open metadata elements retrieved into data field elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of data fields (or null)
     */
    private List<DataFieldElement> convertDataFields(String                    userId,
                                                     List<OpenMetadataElement> openMetadataElements,
                                                     Date                      asOfTime,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     Date                      effectiveTime,
                                                     String                    methodName)
    {
        if (openMetadataElements != null)
        {
            List<DataFieldElement> dataFieldElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    dataFieldElements.add(convertDataField(userId,
                                                           openMetadataElement,
                                                           asOfTime,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName));
                }
            }

            return dataFieldElements;
        }

        return null;
    }


    /**
     * Return the data field extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private DataFieldElement convertDataField(String              userId,
                                              OpenMetadataElement openMetadataElement,
                                              Date                asOfTime,
                                              boolean             forLineage,
                                              boolean             forDuplicateProcessing,
                                              Date                effectiveTime,
                                              String              methodName)
    {
        try
        {
            List<MemberDataField>        relatedFields = new ArrayList<>();
            List<RelatedMetadataElement> otherRelatedElements = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       1,
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
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName))
                        {
                            relatedFields.add(this.convertMemberDataField(userId,
                                                                          relatedMetadataElement,
                                                                          asOfTime,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName));
                        }
                        else
                        {
                            otherRelatedElements.add(relatedMetadataElement);
                        }
                    }
                }

                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                1,
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

            DataFieldConverter<DataFieldElement> converter = new DataFieldConverter<>(propertyHelper, serviceName, serverName);
            DataFieldElement dataFieldElement = converter.getNewComplexBean(DataFieldElement.class,
                                                                            openMetadataElement,
                                                                            otherRelatedElements,
                                                                            methodName);
            if (dataFieldElement != null)
            {
                if (! relatedFields.isEmpty())
                {
                    dataFieldElement.setNestedDataFields(relatedFields);
                }

                DataFieldMermaidGraphBuilder graphBuilder = new DataFieldMermaidGraphBuilder(dataFieldElement);

                dataFieldElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return dataFieldElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DesignModelAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()));
            }

            return null;
        }
    }


    /**
     * Return the member data field related element extracted from the open metadata element.
     *
     * @param userId calling user
     * @param startingMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private MemberDataField convertMemberDataField(String                 userId,
                                                   RelatedMetadataElement startingMetadataElement,
                                                   Date                   asOfTime,
                                                   boolean                forLineage,
                                                   boolean                forDuplicateProcessing,
                                                   Date                   effectiveTime,
                                                   String                 methodName)
    {
        if (startingMetadataElement != null)
        {
            MemberDataField memberDataField = new MemberDataField(this.convertDataField(userId,
                                                                                        startingMetadataElement.getElement(),
                                                                                        asOfTime,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName));


            DataFieldConverter<DataFieldElement> converter = new DataFieldConverter<>(propertyHelper, serviceName, serverName);

            memberDataField.setMemberDataFieldProperties(converter.getMemberDataFieldProperties(startingMetadataElement));

            return memberDataField;
        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieve into data class elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of data classes (or null)
     */
    private List<DataClassElement> convertDataClasses(String                    userId,
                                                      List<OpenMetadataElement> openMetadataElements,
                                                      Date                      asOfTime,
                                                      boolean                   forLineage,
                                                      boolean                   forDuplicateProcessing,
                                                      Date                      effectiveTime,
                                                      String                    methodName)
    {
        if (openMetadataElements != null)
        {
            List<DataClassElement> dataClassElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    dataClassElements.add(convertDataClass(userId,
                                                           openMetadataElement,
                                                           asOfTime,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName));
                }
            }

            return dataClassElements;
        }

        return null;
    }


    /**
     * Return the data class extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private DataClassElement convertDataClass(String              userId,
                                              OpenMetadataElement openMetadataElement,
                                              Date                asOfTime,
                                              boolean             forLineage,
                                              boolean             forDuplicateProcessing,
                                              Date                effectiveTime,
                                              String              methodName)
    {
        try
        {
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       1,
                                                                                                                       null,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       0,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());

            DataClassConverter<DataClassElement> converter = new DataClassConverter<>(propertyHelper, serviceName, serverName);

            DataClassElement dataClassElement = converter.getNewComplexBean(DataClassElement.class,
                                                                            openMetadataElement,
                                                                            relatedMetadataElementList,
                                                                            methodName);
            if (dataClassElement != null)
            {
                DataClassMermaidGraphBuilder graphBuilder = new DataClassMermaidGraphBuilder(dataClassElement);

                dataClassElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return dataClassElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DesignModelAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()));
            }

            return null;
        }
    }
}
