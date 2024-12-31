/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.client;

import org.odpi.openmetadata.accessservices.digitalservice.api.CollectionsInterface;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.converters.CollectionConverter;
import org.odpi.openmetadata.frameworks.governanceaction.converters.CollectionMemberConverter;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CollectionMemberStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OrderBy;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMember;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CollectionsClient supports requests related to collections.
 */
public class CollectionsClient extends DigitalServiceBaseClient implements CollectionsInterface
{
    final private CollectionConverter<CollectionElement> collectionConverter;
    final private Class<CollectionElement>                    collectionBeanClass       = CollectionElement.class;
    final private CollectionMemberConverter<CollectionMember> collectionMemberConverter;

    final private Class<CollectionMember>                     collectionMemberBeanClass = CollectionMember.class;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           number of elements that can be returned on a call
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CollectionsClient(String serverName,
                             String serverPlatformURLRoot,
                             int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);

        collectionConverter = new CollectionConverter<>(propertyHelper,
                                                        AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceName(),
                                                        serverName);

        collectionMemberConverter = new CollectionMemberConverter<>(propertyHelper,
                                                                    AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceName(),
                                                                    serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           number of elements that can be returned on a call
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CollectionsClient(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password,
                             int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);

        collectionConverter = new CollectionConverter<>(propertyHelper,
                                                        AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceName(),
                                                        serverName);

        collectionMemberConverter = new CollectionMemberConverter<>(propertyHelper,
                                                                    AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceName(),
                                                                    serverName);
    }


    /* =====================================================================================================================
     * CollectionsInterface methods
     */

    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param userId         userId of user making request
     * @param parentGUID     unique identifier of referenceable object (typically a personal profile, project or
     *                       community) that the collections hang off of
     * @param collectionType filter response by collection type - if null, any value will do
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<CollectionElement> getAttachedCollections(String userId,
                                                          String parentGUID,
                                                          String collectionType,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
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
                                                                                                        null,
                                                                                                        null,
                                                                                                        null,
                                                                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                                                                        false,
                                                                                                        false,
                                                                                                        new Date(),
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
     * Returns the list of collections with a particular classification.
     *
     * @param userId             userId of user making request
     * @param classificationName name of the classification - if null, all collections are returned
     * @param startFrom          index of the list to start from (0 for start)
     * @param pageSize           maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<CollectionElement> getClassifiedCollections(String userId,
                                                            String classificationName,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getClassifiedCollections";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchClassifications searchClassifications = null;

        if (classificationName != null)
        {
            searchClassifications = new SearchClassifications();

            List<ClassificationCondition> classificationConditions = new ArrayList<>();
            ClassificationCondition classificationCondition = new ClassificationCondition();

            classificationCondition.setName(classificationName);

            classificationConditions.add(classificationCondition);

            searchClassifications.setConditions(classificationConditions);
            searchClassifications.setMatchCriteria(MatchCriteria.ALL);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                                      OpenMetadataType.COLLECTION.typeName,
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      searchClassifications,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      SequencingOrder.PROPERTY_ASCENDING,
                                                                                                      false,
                                                                                                      false,
                                                                                                      new Date(),
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertCollections(openMetadataElements, methodName);
    }


    /**
     * Convert collection objects from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElements retrieved elements
     * @param methodName calling method
     * @return list of collection elements
     * @throws PropertyServerException error in retrieved values
     */
    private List<CollectionElement> convertCollections(List<OpenMetadataElement>  openMetadataElements,
                                                       String                     methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<CollectionElement> collectionElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    collectionElements.add(collectionConverter.getNewBean(collectionBeanClass, openMetadataElement, methodName));
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
     * @param searchString string to search for
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize     maximum number of elements to return
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<CollectionElement> findCollections(String userId,
                                                   String searchString,
                                                   int    startFrom,
                                                   int    pageSize,
                                                   Date   effectiveTime) throws InvalidParameterException,
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
                                                                                                                OpenMetadataType.COLLECTION.typeName,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                false,
                                                                                                                false,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertCollections(openMetadataElements, methodName);
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param userId    userId of user making request
     * @param name      name of the collections to return - match is full text match in qualifiedName or name
     * @param startFrom index of the list to start from (0 for start)
     * @param pageSize  maximum number of elements to return
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<CollectionElement> getCollectionsByName(String userId,
                                                        String name,
                                                        int    startFrom,
                                                        int    pageSize,
                                                        Date   effectiveTime) throws InvalidParameterException,
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
                                                                                                      propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      SequencingOrder.PROPERTY_ASCENDING,
                                                                                                      false,
                                                                                                      false,
                                                                                                      effectiveTime,
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertCollections(openMetadataElements, methodName);
    }


    /**
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param userId         userId of user making request
     * @param collectionType the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<CollectionElement> getCollectionsByType(String userId,
                                                        String collectionType,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
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
                                                                                                                                               PropertyComparisonOperator.EQ),
                                                                                                      null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      SequencingOrder.PROPERTY_ASCENDING,
                                                                                                      false,
                                                                                                      false,
                                                                                                      new Date(),
                                                                                                      startFrom,
                                                                                                      pageSize);

        return convertCollections(openMetadataElements, methodName);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param userId         userId of user making request
     * @param collectionGUID unique identifier of the required collection
     *
     * @return collection properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public CollectionElement getCollection(String userId,
                                           String collectionGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "getCollection";
        final String guidParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   collectionGUID,
                                                                                                   false,
                                                                                                   false,
                                                                                                   null,
                                                                                                   new Date());

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
     * @param optionalClassification classification of the collections - typically RootCollection, Set or Folder
     * @param properties             properties for the collection.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createCollection(String               userId,
                                   String               anchorGUID,
                                   boolean              isOwnAnchor,
                                   String               optionalClassification,
                                   CollectionProperties properties,
                                   String               parentGUID,
                                   String               parentRelationshipTypeName,
                                   ElementProperties    parentRelationshipProperties,
                                   boolean              parentAtEnd1) throws InvalidParameterException,
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

        if (OpenMetadataType.FOLDER.typeName.equals(optionalClassification))
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

            initialClassifications.put(OpenMetadataType.FOLDER.typeName, classificationProperties);
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
                                                                    ElementStatus.ACTIVE,
                                                                    initialClassifications,
                                                                    anchorGUID,
                                                                    isOwnAnchor,
                                                                    properties.getEffectiveFrom(),
                                                                    properties.getEffectiveTo(),
                                                                    this.getElementProperties(properties),
                                                                    parentGUID,
                                                                    parentRelationshipTypeName,
                                                                    parentRelationshipProperties,
                                                                    parentAtEnd1);
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
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createCollectionFromTemplate(String                         userId,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               String                         templateGUID,
                                               ElementProperties              replacementProperties,
                                               Map<String, String>            placeholderProperties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return openMetadataStoreClient.createMetadataElementFromTemplate(userId,
                                                                         OpenMetadataType.COLLECTION.typeName,
                                                                         anchorGUID,
                                                                         isOwnAnchor,
                                                                         effectiveFrom,
                                                                         effectiveTo,
                                                                         templateGUID,
                                                                         replacementProperties,
                                                                         placeholderProperties,
                                                                         parentGUID,
                                                                         parentRelationshipTypeName,
                                                                         parentRelationshipProperties,
                                                                         parentAtEnd1);
    }


    /**
     * Create a new collection that represents a digital product.
     *
     * @param userId                   userId of user making request.
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param collectionProperties     properties for the collection.
     * @param digitalProductProperties properties for the attached DigitalProduct classification
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createDigitalProduct(String                   userId,
                                       String                   anchorGUID,
                                       boolean                  isOwnAnchor,
                                       CollectionProperties     collectionProperties,
                                       DigitalProductProperties digitalProductProperties,
                                       String                   parentGUID,
                                       String                   parentRelationshipTypeName,
                                       ElementProperties        parentRelationshipProperties,
                                       boolean                  parentAtEnd1) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "createDigitalProduct";

        invalidParameterHandler.validateUserId(userId, methodName);

        String collectionGUID = this.createCollection(userId,
                                                      anchorGUID,
                                                      isOwnAnchor,
                                                      null,
                                                      collectionProperties,
                                                      parentGUID,
                                                      parentRelationshipTypeName,
                                                      parentRelationshipProperties,
                                                      parentAtEnd1);

        if (collectionGUID != null)
        {
            openMetadataStoreClient.classifyMetadataElementInStore(userId,
                                                                   collectionGUID,
                                                                   OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.typeName,
                                                                   false,
                                                                   false,
                                                                   digitalProductProperties.getEffectiveFrom(),
                                                                   digitalProductProperties.getEffectiveTo(),
                                                                   this.getElementProperties(digitalProductProperties),
                                                                   new Date());
        }

        return collectionGUID;
    }


    /**
     * Update the properties of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties     properties for the collection.
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   updateCollection(String               userId,
                                   String               collectionGUID,
                                   boolean              replaceAllProperties,
                                   CollectionProperties properties) throws InvalidParameterException,
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
                                                             false,
                                                             false,
                                                             this.getElementProperties(properties),
                                                             new Date());
    }


    /**
     * Update the properties of the DigitalProduct classification attached to a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties     properties for the DigitalProduct classification.
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   updateDigitalProduct(String                   userId,
                                       String                   collectionGUID,
                                       boolean                  replaceAllProperties,
                                       DigitalProductProperties properties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "updateDigitalProduct";
        final String propertiesName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesName, methodName);

        openMetadataStoreClient.reclassifyMetadataElementInStore(userId,
                                                                 collectionGUID,
                                                                 OpenMetadataType.DIGITAL_PRODUCT_CLASSIFICATION.typeName,
                                                                 replaceAllProperties,
                                                                 false,
                                                                 false,
                                                                 this.getElementProperties(properties),
                                                                 new Date());
    }


    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param userId         userId of user making request
     * @param collectionGUID unique identifier of the collection
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to
     * @param collectionUse  description of how the collection will be used.
     * @param makeAnchor     like the lifecycle of the collection to that of the parent so that if the parent is deleted, so is the collection
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void attachCollection(String                 userId,
                                 String                 collectionGUID,
                                 String                 parentGUID,
                                 ResourceListProperties collectionUse,
                                 boolean                makeAnchor) throws InvalidParameterException,
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
                                                             OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                             parentGUID,
                                                             collectionGUID,
                                                             false,
                                                             false,
                                                             null,
                                                             null,
                                                             properties,
                                                             new Date());

        if (makeAnchor)
        {
            OpenMetadataElement parent = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                          parentGUID,
                                                                                          false,
                                                                                          false,
                                                                                          null,
                                                                                          new Date());

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
                                                                       false,
                                                                       false,
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
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void detachCollection(String userId,
                                 String collectionGUID,
                                 String parentGUID) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String methodName = "detachCollection";

        final String collectionGUIDParameterName = "collectionGUID";
        final String parentGUIDParameterName = "parentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataStoreClient.deleteRelatedElementsInStore(userId,
                                                             OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                             parentGUID,
                                                             collectionGUID,
                                                             false,
                                                             false,
                                                             new Date());
    }


    /**
     * Delete a collection.  It is detected from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void deleteCollection(String userId,
                                 String collectionGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName = "deleteCollection";
        final String collectionGUIDParameterName = "collectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             collectionGUID,
                                                             false,
                                                             false,
                                                             new Date());
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<CollectionMember> getCollectionMembers(String userId,
                                                       String collectionGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
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
                                                                                                          null,
                                                                                                          null,
                                                                                                          null,
                                                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                                                          false,
                                                                                                          false,
                                                                                                          new Date(),
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

        if (linkedResources != null)
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void addToCollection(String                         userId,
                                String                         collectionGUID,
                                CollectionMembershipProperties membershipProperties,
                                String                         elementGUID) throws InvalidParameterException,
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
                                                                 new Date());
        }
        else
        {
            this.updateCollectionMembership(userId, relationshipGUID, true, membershipProperties);
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void updateCollectionMembership(String                         userId,
                                           String                         collectionGUID,
                                           boolean                        replaceAllProperties,
                                           CollectionMembershipProperties membershipProperties,
                                           String                         elementGUID) throws InvalidParameterException,
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
            this.updateCollectionMembership(userId, relationshipGUID, replaceAllProperties, membershipProperties);
        }
        else
        {
            this.addToCollection(userId, collectionGUID, membershipProperties, elementGUID);
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
     */
    private void updateCollectionMembership(String                         userId,
                                            String                         relationshipGUID,
                                            boolean                        replaceAllProperties,
                                            CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        openMetadataStoreClient.updateRelatedElementsInStore(userId,
                                                             relationshipGUID,
                                                             replaceAllProperties,
                                                             false,
                                                             false,
                                                             this.getElementProperties(membershipProperties),
                                                             new Date());
    }


    /**
     * Remove an element from a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the element.
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void removeFromCollection(String userId,
                                     String collectionGUID,
                                     String elementGUID) throws InvalidParameterException,
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
            openMetadataStoreClient.deleteRelatedElementsInStore(userId,
                                                                 relationshipGUID,
                                                                 false,
                                                                 false,
                                                                 new Date());
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

            return elementProperties;
        }

        return null;
    }


    /**
     * Convert the digital product properties into a set of element properties for the open metadata client.
     *
     * @param digitalProductProperties supplied digital product properties
     * @return element properties
     */
    private ElementProperties getElementProperties(DigitalProductProperties digitalProductProperties)
    {
        if (digitalProductProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.PRODUCT_NAME.name,
                                                                                   digitalProductProperties.getProductName());

            if (digitalProductProperties.getProductStatus() != null)
            {
                elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                     OpenMetadataProperty.PRODUCT_STATUS.name,
                                                                     digitalProductProperties.getProductStatus().getName());
            }

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.PRODUCT_TYPE.name,
                                                                 digitalProductProperties.getProductType());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 digitalProductProperties.getDescription());

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
                                                                 OpenMetadataType.USER_DEFINED_STATUS_PROPERTY_NAME,
                                                                 collectionMembershipProperties.getUserDefinedStatus());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.CONFIDENCE.name,
                                                              collectionMembershipProperties.getConfidence());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataType.CREATED_BY_PROPERTY_NAME,
                                                                 collectionMembershipProperties.getCreatedBy());

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
}
