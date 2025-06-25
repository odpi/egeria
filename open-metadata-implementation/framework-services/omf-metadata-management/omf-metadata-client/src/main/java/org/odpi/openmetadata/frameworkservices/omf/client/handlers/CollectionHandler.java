/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.client.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.CollectionConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.CollectionMemberConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.CollectionMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMember;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMemberGraph;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * The CollectionsClient supports requests related to collections.
 */
public class CollectionHandler
{
    private final OpenMetadataStoreHandler openMetadataStoreClient;
    private final InvalidParameterHandler  invalidParameterHandler = new InvalidParameterHandler();
    private final AuditLog                 auditLog;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final String serviceName;
    private final String serverName;


    final private CollectionConverter<CollectionElement>      collectionConverter;
    final private Class<CollectionElement>                    collectionBeanClass       = CollectionElement.class;
    final private CollectionMemberConverter<CollectionMember> collectionMemberConverter;

    final private Class<CollectionMember>                     collectionMemberBeanClass = CollectionMember.class;

    final private boolean forLineage = false;
    final private boolean forDuplicateProcessing = false;

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
    public CollectionHandler(String   localServerName,
                             String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog,
                             String   accessServiceURLMarker,
                             String   serviceName,
                             int      maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog                = auditLog;
        this.serverName              = localServerName;
        this.serviceName             = serviceName;

        collectionConverter = new CollectionConverter<>(propertyHelper,
                                                        serviceName,
                                                        serverName);

        collectionMemberConverter = new CollectionMemberConverter<>(propertyHelper,
                                                                    serviceName,
                                                                    serverName);
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
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @param accessServiceURLMarker which access service to call
     * @param serviceName            local service name
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CollectionHandler(String   localServerName,
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
        this.auditLog                = auditLog;
        this.serverName              = localServerName;
        this.serviceName             = serviceName;

        collectionConverter = new CollectionConverter<>(propertyHelper,
                                                        serviceName,
                                                        serverName);

        collectionMemberConverter = new CollectionMemberConverter<>(propertyHelper,
                                                                    serviceName,
                                                                    serverName);
    }


    /* =====================================================================================================================
     * Collections
     */

    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param userId         userId of user making request
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the collections hang off of
     * @param collectionType filter response by collection type - if null, any value will do
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<CollectionElement> getAttachedCollections(String              userId,
                                                          String              parentGUID,
                                                          String              collectionType,
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
        final String methodName = "getLinkedCollections";
        final String parentGUIDParameterName = "parentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedResources = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                        parentGUID,
                                                                                                        1,
                                                                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                                                        limitResultsByStatus,
                                                                                                        asOfTime,
                                                                                                        sequencingProperty,
                                                                                                        sequencingOrder,
                                                                                                        forLineage,
                                                                                                        forDuplicateProcessing,
                                                                                                        effectiveTime,
                                                                                                        startFrom,
                                                                                                        pageSize);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            List<CollectionElement> filteredCollections = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedResources.getElementList())
            {
                if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.COLLECTION.typeName))
                {
                    CollectionElement collectionElement = collectionConverter.getNewBean(collectionBeanClass, relatedMetadataElement, methodName);

                    if ((collectionType == null) || (collectionType.equals(collectionElement.getProperties().getCollectionType())))
                    {
                        filteredCollections.add(collectionElement);
                    }
                }
            }

            if (! filteredCollections.isEmpty())
            {
                return null;
            }
        }

        return null;
    }


    /**
     * Convert collection objects from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElements retrieved elements
     * @param classificationName option name of a collection classification
     * @param methodName calling method
     * @return list of collection elements
     * @throws PropertyServerException error in retrieved values
     */
    private List<CollectionElement> convertCollections(List<OpenMetadataElement>  openMetadataElements,
                                                       String                     classificationName,
                                                       String                     methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<CollectionElement> collectionElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    if (classificationName == null)
                    {
                        collectionElements.add(collectionConverter.getNewBean(collectionBeanClass, openMetadataElement, methodName));
                    }
                    else
                    {
                        if (openMetadataElement.getClassifications() != null)
                        {
                            for (AttachedClassification classification : openMetadataElement.getClassifications())
                            {
                                if ((classification != null) && (classificationName.equals(classification.getClassificationName())))
                                {
                                    collectionElements.add(collectionConverter.getNewBean(collectionBeanClass, openMetadataElement, methodName));
                                }
                            }
                        }
                    }
                }
            }

            return collectionElements;
        }

        return null;
    }


    /**
     * Returns the list of collections matching the search string - this is coded as a regular expression.
     *
     * @param userId       userId of user making request
     * @param classificationName option name of a collection classification
     * @param searchString string to search for
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<CollectionElement> findCollections(String              userId,
                                                   String              classificationName,
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
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "findCollections";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                templateFilter,
                                                                                                                OpenMetadataType.COLLECTION.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertCollections(openMetadataElements, classificationName, methodName);
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param userId    userId of user making request
     * @param classificationName option name of a collection classification
     * @param name      name of the collections to return - match is full text match in qualifiedName or name
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<CollectionElement> getCollectionsByName(String              userId,
                                                        String              classificationName,
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
        final String methodName = "getCollectionsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.COLLECTION.typeName,
                                                                                                      null,
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ, templateFilter),
                                                                                                      limitResultsByStatus,
                                                                                                      asOfTime,
                                                                                                      propertyHelper.getSearchClassifications(classificationName),
                                                                                                      sequencingProperty,
                                                                                                      sequencingOrder,
                                                                                                      forLineage,
                                                                                                      forDuplicateProcessing,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertCollections(openMetadataElements, null, methodName);
    }


    /**
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param userId         userId of user making request
     * @param classificationName option name of a collection classification
     * @param collectionType the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<CollectionElement> getCollectionsByType(String              userId,
                                                        String              classificationName,
                                                        String              collectionType,
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
        final String methodName = "getCollectionsByType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = List.of(OpenMetadataProperty.COLLECTION_TYPE.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.COLLECTION.typeName,
                                                                                                      null,
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames,
                                                                                                                                               collectionType,
                                                                                                                                               PropertyComparisonOperator.EQ,
                                                                                                                                               templateFilter),
                                                                                                      limitResultsByStatus,
                                                                                                      asOfTime,
                                                                                                      propertyHelper.getSearchClassifications(classificationName),
                                                                                                      sequencingProperty,
                                                                                                      sequencingOrder,
                                                                                                      forLineage,
                                                                                                      forDuplicateProcessing,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertCollections(openMetadataElements, null, methodName);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param userId         userId of user making request
     * @param collectionGUID unique identifier of the required collection
     * @param asOfTime             repository time to use
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return collection properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionElement getCollection(String  userId,
                                           String  collectionGUID,
                                           Date    asOfTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "getCollection";
        final String guidParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   collectionGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   asOfTime,
                                                                                                   effectiveTime);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COLLECTION.typeName)))
        {
            return collectionConverter.getNewBean(collectionBeanClass, openMetadataElement, methodName);
        }

        return null;
    }


    /**
     * Create a new generic collection.
     *
     * @param userId                 userId of user making request.
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID optional scope of the anchor
     * @param optionalClassification classification of the collections - typically RootCollection, Set or Folder
     * @param properties             properties for the collection.
     * @param initialStatus initial status for the collection
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createCollection(String               userId,
                                   String               anchorGUID,
                                   boolean              isOwnAnchor,
                                   String               anchorScopeGUID,
                                   String               optionalClassification,
                                   CollectionProperties properties,
                                   ElementStatus        initialStatus,
                                   String               parentGUID,
                                   String               parentRelationshipTypeName,
                                   ElementProperties    parentRelationshipProperties,
                                   boolean              parentAtEnd1,
                                   Date                 effectiveTime) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "createCollection";
        final String collectionPropertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, collectionPropertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String collectionTypeName = OpenMetadataType.COLLECTION.typeName;

        if (properties.getTypeName() != null)
        {
            collectionTypeName = properties.getTypeName();
        }

        Map<String, ElementProperties> initialClassifications = null;

        if (OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName.equals(optionalClassification))
        {
            initialClassifications = new HashMap<>();

            ElementProperties classificationProperties = null;

            if (properties instanceof CollectionFolderProperties collectionFolderProperties)
            {
                classificationProperties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.ORDER_BY_PROPERTY_NAME.name,
                                                                            collectionFolderProperties.getOrderByPropertyName());
                if (collectionFolderProperties.getCollectionOrder() != null)
                {
                    classificationProperties = propertyHelper.addEnumProperty(classificationProperties,
                                                                              OpenMetadataProperty.COLLECTION_ORDER.name,
                                                                              OrderBy.getOpenTypeName(),
                                                                              collectionFolderProperties.getCollectionOrder().getName());
                }
            }

            initialClassifications.put(OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName, classificationProperties);
        }
        else if (optionalClassification != null)
        {
            initialClassifications = new HashMap<>();

            initialClassifications.put(optionalClassification, null);
        }

        return openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                    null,
                                                                    null,
                                                                    collectionTypeName,
                                                                    initialStatus,
                                                                    initialClassifications,
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
                                                                    false,
                                                                    false,
                                                                    effectiveTime);
    }



    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param userId             calling user
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID optional scope of the anchor
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createCollectionFromTemplate(String              userId,
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
                                               Date                effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return openMetadataStoreClient.createMetadataElementFromTemplate(userId,
                                                                         OpenMetadataType.COLLECTION.typeName,
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
     * Update the properties of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties     properties for the collection.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateCollection(String               userId,
                                   String               collectionGUID,
                                   boolean              replaceAllProperties,
                                   CollectionProperties properties,
                                   Date                 effectiveTime) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "updateCollection";
        final String collectionPropertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, collectionPropertiesName, methodName);

        if (replaceAllProperties)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             collectionGUID,
                                                             replaceAllProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             this.getElementProperties(properties),
                                                             effectiveTime);
    }


    /**
     * Update the status of a digital product.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param collectionGUID      unique identifier of the digital product (returned from create)
     * @param status                 new status for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCollectionStatus(String        userId,
                                       String        externalSourceGUID,
                                       String        externalSourceName,
                                       String        collectionGUID,
                                       ElementStatus status,
                                       boolean       forLineage,
                                       boolean       forDuplicateProcessing,
                                       Date          effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "updateCollectionStatus";
        final String propertiesName = "status";
        final String guidParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(status, propertiesName, methodName);

        openMetadataStoreClient.updateMetadataElementStatusInStore(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   collectionGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   status,
                                                                   effectiveTime);
    }


    /**
     * Return the corresponding element status.
     *
     * @param status status that the digital product can be set
     * @return element status
     */
    public ElementStatus getElementStatus(DigitalProductStatus status)
    {
        if (status != null)
        {
            switch (status)
            {
                case DRAFT -> { return ElementStatus.DRAFT; }
                case PREPARED -> { return ElementStatus.PREPARED; }
                case PROPOSED -> { return ElementStatus.PROPOSED; }
                case APPROVED -> { return ElementStatus.APPROVED; }
                case REJECTED -> { return ElementStatus.REJECTED; }
                case APPROVED_CONCEPT -> { return ElementStatus.APPROVED_CONCEPT; }
                case UNDER_DEVELOPMENT -> { return ElementStatus.UNDER_DEVELOPMENT; }
                case DEVELOPMENT_COMPLETE -> { return ElementStatus.DEVELOPMENT_COMPLETE; }
                case APPROVED_FOR_DEPLOYMENT -> { return ElementStatus.APPROVED_FOR_DEPLOYMENT; }
                case ACTIVE -> { return ElementStatus.ACTIVE; }
                case DISABLED -> { return ElementStatus.DISABLED; }
                case DEPRECATED -> { return ElementStatus.DEPRECATED; }
                case OTHER -> { return ElementStatus.OTHER; }
            }
        }

        return ElementStatus.ACTIVE;
    }


    /**
     * Return the corresponding element status.
     *
     * @param status status that the agreement can be set
     * @return element status
     */
    public ElementStatus getElementStatus(AgreementStatus status)
    {
        if (status != null)
        {
            switch (status)
            {
                case DRAFT -> { return ElementStatus.DRAFT; }
                case PREPARED -> { return ElementStatus.PREPARED; }
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
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param userId         userId of user making request
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param collectionGUID unique identifier of the collection
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param collectionUse  description of how the collection will be used.
     * @param makeAnchor     like the lifecycle of the collection to that of the parent so that if the parent is deleted, so is the collection
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachCollection(String                 userId,
                                 String                 externalSourceGUID,
                                 String                 externalSourceName,
                                 String                 collectionGUID,
                                 String                 parentGUID,
                                 ResourceListProperties collectionUse,
                                 boolean                makeAnchor,
                                 boolean                forLineage,
                                 boolean                forDuplicateProcessing,
                                 Date                   effectiveTime) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "attachCollection";
        final String collectionGUIDParameterName = "collectionGUID";
        final String parentGUIDParameterName = "parentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        ElementProperties properties = null;
        if (collectionUse != null)
        {
            properties = propertyHelper.addStringProperty(null,
                                                          OpenMetadataProperty.RESOURCE_USE.name,
                                                          collectionUse.getResourceUse());
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.RESOURCE_USE_DESCRIPTION.name,
                                                          collectionUse.getResourceUse());
            properties = propertyHelper.addStringMapProperty(properties,
                                                             OpenMetadataProperty.RESOURCE_USE_PROPERTIES.name,
                                                             collectionUse.getResourceUseProperties());
            properties = propertyHelper.addBooleanProperty(properties,
                                                           OpenMetadataProperty.WATCH_RESOURCE.name,
                                                           collectionUse.getWatchResource());
        }

        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                             parentGUID,
                                                             collectionGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             null,
                                                             properties,
                                                             new Date());

        if (makeAnchor)
        {
            OpenMetadataElement parent = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                          parentGUID,
                                                                                          forLineage,
                                                                                          forDuplicateProcessing,
                                                                                          null,
                                                                                          effectiveTime);

            if (parent != null)
            {
                ElementProperties classificationProperties = propertyHelper.addStringProperty(null,
                                                                                              OpenMetadataProperty.ANCHOR_GUID.name,
                                                                                              parent.getElementGUID());

                classificationProperties = propertyHelper.addStringProperty(classificationProperties,
                                                                            OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                                            parent.getType().getTypeName());

                classificationProperties = propertyHelper.addStringProperty(classificationProperties,
                                                                            OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                                            propertyHelper.getDomainName(parent));

                openMetadataStoreClient.classifyMetadataElementInStore(userId,
                                                                       collectionGUID,
                                                                       OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       null,
                                                                       null,
                                                                       classificationProperties,
                                                                       new Date());
            }
        }
    }



    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param userId         userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCollection(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  collectionGUID,
                                 String  parentGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName = "detachCollection";

        final String collectionGUIDParameterName = "collectionGUID";
        final String parentGUIDParameterName = "parentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                             parentGUID,
                                                             collectionGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }




    /**
     * Link two dependent products.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDigitalProductDependency(String                             userId,
                                             String                             externalSourceGUID,
                                             String                             externalSourceName,
                                             String                             consumerDigitalProductGUID,
                                             String                             consumedDigitalProductGUID,
                                             DigitalProductDependencyProperties relationshipProperties,
                                             boolean                            forLineage,
                                             boolean                            forDuplicateProcessing,
                                             Date                               effectiveTime) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkDigitalProductDependency";
        final String end1GUIDParameterName = "consumerDigitalProductGUID";
        final String end2GUIDParameterName = "consumedDigitalProductGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consumerDigitalProductGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(consumedDigitalProductGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                 consumerDigitalProductGUID,
                                                                 consumedDigitalProductGUID,
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
                                                                 OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                 consumerDigitalProductGUID,
                                                                 consumedDigitalProductGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Unlink dependent products.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDigitalProductDependency(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  consumerDigitalProductGUID,
                                               String  consumedDigitalProductGUID,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachDigitalProductDependency";

        final String end1GUIDParameterName = "consumerDigitalProductGUID";
        final String end2GUIDParameterName = "consumedDigitalProductGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consumerDigitalProductGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(consumedDigitalProductGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                             consumerDigitalProductGUID,
                                                             consumedDigitalProductGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }




    /**
     * Attach a subscriber to a subscription.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param digitalSubscriberGUID  unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubscriber(String                      userId,
                               String                      externalSourceGUID,
                               String                      externalSourceName,
                               String                      digitalSubscriberGUID,
                               String                      digitalSubscriptionGUID,
                               DigitalSubscriberProperties relationshipProperties,
                               boolean                     forLineage,
                               boolean                     forDuplicateProcessing,
                               Date                        effectiveTime) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "linkSubscriber";
        final String end1GUIDParameterName = "digitalSubscriberGUID";
        final String end2GUIDParameterName = "digitalSubscriptionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(digitalSubscriberGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(digitalSubscriptionGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName,
                                                                 digitalSubscriberGUID,
                                                                 digitalSubscriptionGUID,
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
                                                                 OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName,
                                                                 digitalSubscriberGUID,
                                                                 digitalSubscriptionGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a subscriber from a subscription.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param digitalSubscriberGUID  unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubscriber(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  digitalSubscriberGUID,
                                 String  digitalSubscriptionGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName = "detachSubscriber";

        final String end1GUIDParameterName = "digitalSubscriberGUID";
        final String end2GUIDParameterName = "digitalSubscriptionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(digitalSubscriberGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(digitalSubscriptionGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName,
                                                             digitalSubscriberGUID,
                                                             digitalSubscriptionGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerGUID      unique identifier of the product manager role
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkProductManager(String                 userId,
                                   String                 externalSourceGUID,
                                   String                 externalSourceName,
                                   String                 digitalProductGUID,
                                   String                 digitalProductManagerGUID,
                                   RelationshipProperties relationshipProperties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "linkProductManager";
        final String end1GUIDParameterName = "digitalProductGUID";
        final String end2GUIDParameterName = "digitalProductManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(digitalProductGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(digitalProductManagerGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.DIGITAL_PRODUCT_MANAGEMENT_RELATIONSHIP.typeName,
                                                                 digitalProductGUID,
                                                                 digitalProductManagerGUID,
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
                                                                 OpenMetadataType.DIGITAL_PRODUCT_MANAGEMENT_RELATIONSHIP.typeName,
                                                                 digitalProductGUID,
                                                                 digitalProductManagerGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerGUID      unique identifier of the product manager role
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProductManager(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  digitalProductGUID,
                                     String  digitalProductManagerGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachProductManager";

        final String end1GUIDParameterName = "digitalProductGUID";
        final String end2GUIDParameterName = "digitalProductManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(digitalProductGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(digitalProductManagerGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.DIGITAL_PRODUCT_MANAGEMENT_RELATIONSHIP.typeName,
                                                             digitalProductGUID,
                                                             digitalProductManagerGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }

    /**
     * Attach an actor to an agreement.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param agreementGUID  unique identifier of the agreement
     * @param actorGUID      unique identifier of the actor
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return unique identifier of the relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkAgreementActor(String                   userId,
                                     String                   externalSourceGUID,
                                     String                   externalSourceName,
                                     String                   agreementGUID,
                                     String                   actorGUID,
                                     AgreementActorProperties relationshipProperties,
                                     boolean                  forLineage,
                                     boolean                  forDuplicateProcessing,
                                     Date                     effectiveTime) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "linkAgreementActor";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "actorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(actorGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            return openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                        externalSourceGUID,
                                                                        externalSourceName,
                                                                        OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName,
                                                                        agreementGUID,
                                                                        actorGUID,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        relationshipProperties.getEffectiveFrom(),
                                                                        relationshipProperties.getEffectiveTo(),
                                                                        null,
                                                                        effectiveTime);
        }
        else
        {
            return openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                        externalSourceGUID,
                                                                        externalSourceName,
                                                                        OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName,
                                                                        agreementGUID,
                                                                        actorGUID,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        effectiveTime);
        }
    }


    /**
     * Detach an actor from an agreement.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param agreementActorRelationshipGUID  unique identifier of the element being described
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementActor(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  agreementActorRelationshipGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachAgreementActor";

        final String end1GUIDParameterName = "agreementActorRelationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(agreementActorRelationshipGUID, end1GUIDParameterName, methodName);

        openMetadataStoreClient.deleteRelationshipInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          agreementActorRelationshipGUID,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime);
    }


    /**
     * Attach an agreement to an element involved in its definition.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param agreementGUID  unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAgreementItem(String                  userId,
                                  String                  externalSourceGUID,
                                  String                  externalSourceName,
                                  String                  agreementGUID,
                                  String                  agreementItemGUID,
                                  AgreementItemProperties relationshipProperties,
                                  boolean                 forLineage,
                                  boolean                 forDuplicateProcessing,
                                  Date                    effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkAgreementItem";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "agreementItemGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(agreementItemGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                 agreementGUID,
                                                                 agreementItemGUID,
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
                                                                 OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                 agreementGUID,
                                                                 agreementItemGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach an agreement from an element involved in its definition.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param agreementGUID  unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementItem(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  agreementGUID,
                                    String  agreementItemGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "detachAgreementItem";

        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "agreementItemGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(agreementItemGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                             agreementGUID,
                                                             agreementItemGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param userId                  userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param agreementGUID  unique identifier of the agreement
     * @param externalReferenceGUID      unique identifier of the external reference describing the location of the contract
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContract(String                 userId,
                             String                 externalSourceGUID,
                             String                 externalSourceName,
                             String                 agreementGUID,
                             String                 externalReferenceGUID,
                             ContractLinkProperties relationshipProperties,
                             boolean                forLineage,
                             boolean                forDuplicateProcessing,
                             Date                   effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkContract";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        if (relationshipProperties != null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName,
                                                                 agreementGUID,
                                                                 externalReferenceGUID,
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
                                                                 OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName,
                                                                 agreementGUID,
                                                                 externalReferenceGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 effectiveTime);
        }
    }


    /**
     * Detach an agreement from an external reference describing the location of the contract documents.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID     unique identifier of the software capability that owns this element
     * @param externalSourceName     unique name of the software capability that owns this element
     * @param agreementGUID  unique identifier of the agreement
     * @param externalReferenceGUID      unique identifier of the external reference describing the location of the contract
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContract(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  agreementGUID,
                               String  externalReferenceGUID,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String methodName = "detachContract";

        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName,
                                                             agreementGUID,
                                                             externalReferenceGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Delete a collection.  It is detached from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection
     * @param cascadedDelete should any nested collections be deleted? If false, the delete fails if there are nested
     *                       collections.  If true, nested collections are delete - but not member elements
     *                       unless they are anchored to the collection
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteCollection(String  userId,
                                 String  collectionGUID,
                                 boolean cascadedDelete) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String methodName = "deleteCollection";
        final String collectionGUIDParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             collectionGUID,
                                                             cascadedDelete,
                                                             false,
                                                             false,
                                                             new Date());
    }



    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
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
     * @return list of member details
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<CollectionMember> getCollectionMembers(String              userId,
                                                       String              collectionGUID,
                                                       List<ElementStatus> limitResultsByStatus,
                                                       Date                asOfTime,
                                                       String              sequencingProperty,
                                                       SequencingOrder     sequencingOrder,
                                                       boolean             forLineage,
                                                       boolean             forDuplicateProcessing,
                                                       Date                effectiveTime,
                                                       int                 startFrom,
                                                       int                 pageSize) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getCollectionMembers";
        final String collectionGUIDParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList linkedResources = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                        collectionGUID,
                                                                                                        1,
                                                                                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                        limitResultsByStatus,
                                                                                                        asOfTime,
                                                                                                        sequencingProperty,
                                                                                                        sequencingOrder,
                                                                                                        forLineage,
                                                                                                        forDuplicateProcessing,
                                                                                                        effectiveTime,
                                                                                                        startFrom,
                                                                                                        pageSize);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            List<CollectionMember> collectionMembers = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : linkedResources.getElementList())
            {
                collectionMembers.add(collectionMemberConverter.getNewBean(collectionMemberBeanClass, relatedMetadataElement, methodName));
            }

            if (! collectionMembers.isEmpty())
            {
                return collectionMembers;
            }
        }

        return null;
    }



    /**
     * Return a graph of elements that are the nested members of a collection along
     * with elements immediately connected to the starting collection.  The result
     * includes a mermaid graph of the returned elements.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
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
     * @return list of member details
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CollectionGraph getCollectionGraph(String              userId,
                                              String              collectionGUID,
                                              List<ElementStatus> limitResultsByStatus,
                                              Date                asOfTime,
                                              String              sequencingProperty,
                                              SequencingOrder     sequencingOrder,
                                              boolean             forLineage,
                                              boolean             forDuplicateProcessing,
                                              Date                effectiveTime,
                                              int                 startFrom,
                                              int                 pageSize) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getCollectionGraph";
        final String collectionGUIDParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   collectionGUID,
                                                                                                   false,
                                                                                                   false,
                                                                                                   null,
                                                                                                   new Date());

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.COLLECTION.typeName)))
        {
            RelatedMetadataElementList linkedResources = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                            collectionGUID,
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

            List<CollectionMemberGraph>  collectionMembers    = new ArrayList<>();
            List<RelatedMetadataElement> otherRelatedElements = new ArrayList<>();

            if ((linkedResources != null) && (linkedResources.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : linkedResources.getElementList())
                {
                    if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName)) && (!relatedMetadataElement.getElementAtEnd1()))
                    {
                        collectionMembers.add(this.convertCollectionMemberGraph(userId,
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

                if (collectionMembers.isEmpty())
                {
                    collectionMembers = null;
                }
            }

            CollectionGraph collectionGraph = new CollectionGraph(collectionConverter.getNewComplexBean(collectionBeanClass, openMetadataElement, otherRelatedElements, methodName));

            collectionGraph.setCollectionMemberGraphs(collectionMembers);
            CollectionMermaidGraphBuilder graphBuilder = new CollectionMermaidGraphBuilder(collectionGraph);

            collectionGraph.setMermaidGraph(graphBuilder.getMermaidGraph());

            return collectionGraph;
        }

        return null;
    }


    /**
     * Return the member related element extracted from the open metadata element.
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
    private CollectionMemberGraph convertCollectionMemberGraph(String                 userId,
                                                               RelatedMetadataElement startingMetadataElement,
                                                               Date                   asOfTime,
                                                               boolean                forLineage,
                                                               boolean                forDuplicateProcessing,
                                                               Date                   effectiveTime,
                                                               String                 methodName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        if (startingMetadataElement != null)
        {
            CollectionMemberGraph collectionMemberGraph = new CollectionMemberGraph(collectionMemberConverter.getNewBean(CollectionMember.class,
                                                                                                                         startingMetadataElement,
                                                                                                                         methodName));

            if (propertyHelper.isTypeOf(startingMetadataElement.getElement(), OpenMetadataType.COLLECTION.typeName))
            {
                RelatedMetadataElementList linkedMembers = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                              startingMetadataElement.getElement().getElementGUID(),
                                                                                                              1,
                                                                                                              OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                              null,
                                                                                                              asOfTime,
                                                                                                              null,
                                                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                                                              forLineage,
                                                                                                              forDuplicateProcessing,
                                                                                                              effectiveTime,
                                                                                                              0,
                                                                                                              invalidParameterHandler.getMaxPagingSize());

                if ((linkedMembers != null) && (linkedMembers.getElementList() != null))
                {
                    List<CollectionMemberGraph> collectionMembers = new ArrayList<>();

                    for (RelatedMetadataElement relatedMetadataElement : linkedMembers.getElementList())
                    {
                        collectionMembers.add(this.convertCollectionMemberGraph(userId,
                                                                                relatedMetadataElement,
                                                                                asOfTime,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));
                    }

                    collectionMemberGraph.setNestedMembers(collectionMembers);
                }
            }

            return collectionMemberGraph;
        }

        return null;
    }


    /**
     *
     * @param userId calling user
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID unique identifier of the collection member
     *
     * @return unique identifier for the relationship between the two elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String getMembershipRelationshipGUID(String userId,
                                                 String collectionGUID,
                                                 String elementGUID) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        OpenMetadataRelationshipList linkedResources = openMetadataStoreClient.getMetadataElementRelationships(userId,
                                                                                                               collectionGUID,
                                                                                                               elementGUID,
                                                                                                               OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                               null,
                                                                                                               null,
                                                                                                               null,
                                                                                                               null,
                                                                                                               false,
                                                                                                               false,
                                                                                                               new Date(),
                                                                                                               0,
                                                                                                               0);

        if ((linkedResources != null) && (linkedResources.getElementList() != null))
        {
            for (OpenMetadataRelationship relatedMetadataElement : linkedResources.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    return relatedMetadataElement.getRelationshipGUID();
                }
            }
        }

        return null;
    }


    /**
     * Add an element to a collection.
     *
     * @param userId               userId of user making request.
     * @param collectionGUID       unique identifier of the collection.
     * @param membershipProperties properties describing the membership characteristics.
     * @param elementGUID          unique identifier of the element.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addToCollection(String                         userId,
                                String                         collectionGUID,
                                CollectionMembershipProperties membershipProperties,
                                String                         elementGUID,
                                Date                           effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "addToCollection";
        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        String relationshipGUID = this.getMembershipRelationshipGUID(userId, collectionGUID, elementGUID);

        if (relationshipGUID == null)
        {
            openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                 OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                 collectionGUID,
                                                                 elementGUID,
                                                                 false,
                                                                 false,
                                                                 null,
                                                                 null,
                                                                 this.getElementProperties(membershipProperties),
                                                                 effectiveTime);
        }
        else
        {
            this.updateCollectionMembership(userId, relationshipGUID, true, membershipProperties, effectiveTime);
        }
    }


    /**
     * Update an element's membership to a collection.
     *
     * @param userId               userId of user making request.
     * @param collectionGUID       unique identifier of the collection.
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param membershipProperties properties describing the membership characteristics.
     * @param elementGUID          unique identifier of the element.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCollectionMembership(String                         userId,
                                           String                         collectionGUID,
                                           boolean                        replaceAllProperties,
                                           CollectionMembershipProperties membershipProperties,
                                           String                         elementGUID,
                                           Date                           effectiveTime) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "updateCollectionMembership";

        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        String relationshipGUID = this.getMembershipRelationshipGUID(userId, collectionGUID, elementGUID);

        if (relationshipGUID != null)
        {
            this.updateCollectionMembership(userId, relationshipGUID, replaceAllProperties, membershipProperties, effectiveTime);
        }
        else
        {
            this.addToCollection(userId, collectionGUID, membershipProperties, elementGUID, effectiveTime);
        }
    }


    /**
     * Update the properties of a collection membership relationship.
     *
     * @param userId calling user
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param relationshipGUID unique identifier of the collection
     * @param membershipProperties properties describing the membership characteristics.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     */
    private void updateCollectionMembership(String                         userId,
                                            String                         relationshipGUID,
                                            boolean                        replaceAllProperties,
                                            CollectionMembershipProperties membershipProperties,
                                            Date                           effectiveTime) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        openMetadataStoreClient.updateRelationshipInStore(userId,
                                                          relationshipGUID,
                                                          replaceAllProperties,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          this.getElementProperties(membershipProperties),
                                                          effectiveTime);
    }


    /**
     * Remove an element from a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the element.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeFromCollection(String userId,
                                     String collectionGUID,
                                     String elementGUID,
                                     Date   effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "removeFromCollection";

        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        String relationshipGUID = this.getMembershipRelationshipGUID(userId, collectionGUID, elementGUID);

        if (relationshipGUID != null)
        {
            openMetadataStoreClient.deleteRelationshipInStore(userId,
                                                              relationshipGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime);
        }
    }




    /*
     * Mapping functions
     */


    /**
     * Convert the collection properties into a set of element properties for the open metadata client.
     *
     * @param collectionProperties supplied collection properties
     * @return element properties
     */
    private ElementProperties getElementProperties(CollectionProperties collectionProperties)
    {
        if (collectionProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   collectionProperties.getQualifiedName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAME.name,
                                                                 collectionProperties.getName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 collectionProperties.getDescription());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.COLLECTION_TYPE.name,
                                                                 collectionProperties.getCollectionType());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    collectionProperties.getAdditionalProperties());

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              collectionProperties.getExtendedProperties());

            if (collectionProperties instanceof AgreementProperties agreementProperties)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.IDENTIFIER.name,
                                                                     agreementProperties.getIdentifier());
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                     agreementProperties.getUserDefinedStatus());

                if (collectionProperties instanceof DigitalSubscriptionProperties digitalSubscriptionProperties)
                {
                    elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                         OpenMetadataProperty.SUPPORT_LEVEL.name,
                                                                         digitalSubscriptionProperties.getSupportLevel());
                    elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                            OpenMetadataProperty.SERVICE_LEVELS.name,
                                                                            digitalSubscriptionProperties.getServiceLevels());
                }
            }
            else if (collectionProperties instanceof DigitalProductProperties digitalProductProperties)
            {
                elementProperties = propertyHelper.addStringProperty(null,
                                                                     OpenMetadataProperty.PRODUCT_NAME.name,
                                                                     digitalProductProperties.getProductName());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                     digitalProductProperties.getUserDefinedStatus());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PRODUCT_TYPE.name,
                                                                     digitalProductProperties.getProductType());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.INTRODUCTION_DATE.name,
                                                                   digitalProductProperties.getIntroductionDate());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.MATURITY.name,
                                                                     digitalProductProperties.getMaturity());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.SERVICE_LIFE.name,
                                                                     digitalProductProperties.getServiceLife());

                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.CURRENT_VERSION.name,
                                                                     digitalProductProperties.getCurrentVersion());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.NEXT_VERSION_DATE.name,
                                                                   digitalProductProperties.getNextVersionDate());

                elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                   OpenMetadataProperty.WITHDRAW_DATE.name,
                                                                   digitalProductProperties.getWithdrawDate());

                elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                        digitalProductProperties.getAdditionalProperties());
            }
            return elementProperties;
        }

        return null;
    }


    /**
     * Convert the digital product properties into a set of element properties for the open metadata client.
     *
     * @param collectionMembershipProperties supplied collection membership properties
     * @return element properties
     */
    private ElementProperties getElementProperties(CollectionMembershipProperties collectionMembershipProperties)
    {
        if (collectionMembershipProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.MEMBERSHIP_RATIONALE.name,
                                                                                   collectionMembershipProperties.getMembershipRationale());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.EXPRESSION.name,
                                                                 collectionMembershipProperties.getExpression());

            if (collectionMembershipProperties.getStatus() != null)
            {
                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                   OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                                                   CollectionMemberStatus.getOpenTypeName(),
                                                                   collectionMembershipProperties.getStatus().getName());
            }

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                 collectionMembershipProperties.getUserDefinedStatus());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.CONFIDENCE.name,
                                                              collectionMembershipProperties.getConfidence());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD.name,
                                                                 collectionMembershipProperties.getSteward());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                 collectionMembershipProperties.getStewardTypeName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                 collectionMembershipProperties.getStewardPropertyName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SOURCE.name,
                                                                 collectionMembershipProperties.getSource());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NOTES.name,
                                                                 collectionMembershipProperties.getNotes());

            return elementProperties;
        }

        return null;
    }



    /**
     * Convert the digital product properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied collection membership properties
     * @return element properties
     */
    private ElementProperties getElementProperties(DigitalProductDependencyProperties properties)
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
     * Convert the digital product subscriber into a set of element properties for the open metadata client.
     *
     * @param properties supplied collection membership properties
     * @return element properties
     */
    private ElementProperties getElementProperties(DigitalSubscriberProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.SUBSCRIBER_ID.name,
                                                    properties.getSubscriberId());
        }

        return null;
    }


    /**
     * Convert the actor properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied collection membership properties
     * @return element properties
     */
    private ElementProperties getElementProperties(AgreementActorProperties properties)
    {
        if (properties != null)
        {
            return propertyHelper.addStringProperty(null,
                                                    OpenMetadataProperty.ACTOR_NAME.name,
                                                    properties.getActorName());
        }

        return null;
    }



    /**
     * Convert the agreement item properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied collection membership properties
     * @return element properties
     */
    private ElementProperties getElementProperties(AgreementItemProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.AGREEMENT_ITEM_ID.name,
                                                                                   properties.getAgreementItemId());
            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                                 OpenMetadataProperty.AGREEMENT_START.name,
                                                                 properties.getAgreementStart());
            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.AGREEMENT_END.name,
                                                               properties.getAgreementEnd());
            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ENTITLEMENTS.name,
                                                                    properties.getEntitlements());
            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.RESTRICTIONS.name,
                                                                    properties.getRestrictions());
            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.OBLIGATIONS.name,
                                                                    properties.getObligations());
            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.USAGE_MEASUREMENTS.name,
                                                                    properties.getUsageMeasurements());

            return elementProperties;
        }

        return null;
    }


    /**
     * Convert the contract link properties into a set of element properties for the open metadata client.
     *
     * @param properties supplied collection membership properties
     * @return element properties
     */
    private ElementProperties getElementProperties(ContractLinkProperties properties)
    {
        if (properties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.CONTRACT_ID.name,
                                                                                   properties.getContractId());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.CONTRACT_LIAISON.name,
                                                                 properties.getContractLiaison());
            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.CONTRACT_LIAISON_TYPE_NAME.name,
                                                                 properties.getContractLiaisonTypeName());
            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.CONTRACT_LIAISON_PROPERTY_NAME.name,
                                                                 properties.getContractLiaisonPropertyName());

            return elementProperties;
        }

        return null;
    }

}
