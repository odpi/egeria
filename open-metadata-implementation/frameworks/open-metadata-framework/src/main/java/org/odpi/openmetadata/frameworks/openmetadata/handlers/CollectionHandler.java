/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.OpenMetadataRootHierarchyMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.VisualStyle;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.CanonicalVocabularyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.TaxonomyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The CollectionsClient supports requests related to collections.
 */
public class CollectionHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName    name of this server (view server)
     * @param auditLog           logging destination
     * @param localServiceName   local service name
     * @param openMetadataClient access to open metadata
     */
    public CollectionHandler(String             localServerName,
                             AuditLog           auditLog,
                             String             localServiceName,
                             OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, localServiceName, openMetadataClient, OpenMetadataType.COLLECTION.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param localServerName    name of this server (view server)
     * @param auditLog           logging destination
     * @param localServiceName   local service name
     * @param openMetadataClient access to open metadata
     * @param metadataElementTypeName type of principle element
     */
    public CollectionHandler(String             localServerName,
                             AuditLog           auditLog,
                             String             localServiceName,
                             OpenMetadataClient openMetadataClient,
                             String             metadataElementTypeName)
    {
        super(localServerName, auditLog, localServiceName, openMetadataClient, metadataElementTypeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public CollectionHandler(CollectionHandler template,
                             String            specificTypeName)
    {
        super(template, specificTypeName);
    }


    /* =====================================================================================================================
     * Collections
     */

    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param userId       userId of user making request
     * @param parentGUID   unique identifier of referenceable object (typically a personal profile, project or
     *                     community) that the collections hang off of
     * @param queryOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAttachedCollections(String userId,
                                                                String parentGUID,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName              = "getAttachedCollections";
        final String parentGUIDParameterName = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        return this.getRelatedRootElements(userId,
                                           parentGUID,
                                           parentGUIDParameterName,
                                           1,
                                           OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                           OpenMetadataType.COLLECTION.typeName,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of collections matching the search string.
     *
     * @param userId        userId of user making request
     * @param searchString  string to search for
     * @param searchOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> findCollections(String        userId,
                                                         String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "findCollections";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Retrieve the digital products that match the search string and optional status.
     *
     * @param userId       calling user
     * @param searchString string to search for (may include RegExs)
     * @param deploymentStatusList   optional status list
     * @param suppliedSearchOptions   multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> findDigitalProducts(String                 userId,
                                                             String                 searchString,
                                                             List<DeploymentStatus> deploymentStatusList,
                                                             SearchOptions          suppliedSearchOptions) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "findDigitalProducts";

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.DIGITAL_PRODUCT.typeName);
        }

        List<OpenMetadataRootElement> openMetadataElements = this.findRootElements(userId,
                                                                                   searchString,
                                                                                   suppliedSearchOptions,
                                                                                   methodName);

        return filterDigitalProduct(openMetadataElements, deploymentStatusList);
    }


    /**
     * Retrieve the digital products that match the category name and status.
     *
     * @param userId     calling user
     * @param category   type to search for
     * @param deploymentStatusList optional status list
     * @param suppliedQueryOptions multiple options to control the query
     * @return list of action beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<OpenMetadataRootElement> getDigitalProductByCategory(String                 userId,
                                                                     String                 category,
                                                                     List<DeploymentStatus> deploymentStatusList,
                                                                     QueryOptions           suppliedQueryOptions) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getDigitalProductByCategory";

        QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(OpenMetadataType.DIGITAL_PRODUCT.typeName);
        }

        List<OpenMetadataRootElement> openMetadataElements = super.getRootElementsByName(userId,
                                                                                         category,
                                                                                         List.of(OpenMetadataProperty.CATEGORY.name),
                                                                                         queryOptions,
                                                                                         methodName);

        return filterDigitalProduct(openMetadataElements, deploymentStatusList);
    }


    /**
     * Filter digital products by deployment status.
     *
     * @param openMetadataRootElements retrieved elements
     * @param deploymentStatusList           optional status list
     * @return list of process elements
     */
    private List<OpenMetadataRootElement> filterDigitalProduct(List<OpenMetadataRootElement> openMetadataRootElements,
                                                               List<DeploymentStatus>        deploymentStatusList)
    {
        if (openMetadataRootElements != null)
        {
            List<OpenMetadataRootElement> rootElements = new ArrayList<>();

            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                if ((openMetadataRootElement != null) &&
                        (openMetadataRootElement.getProperties() instanceof DigitalProductProperties digitalProductProperties))
                {
                    if ((deploymentStatusList == null) || (deploymentStatusList.isEmpty()))
                    {
                        rootElements.add(openMetadataRootElement);
                    }
                    else if (deploymentStatusList.contains(digitalProductProperties.getDeploymentStatus()))
                    {
                        rootElements.add(openMetadataRootElement);
                    }
                }
            }

            return rootElements;
        }

        return null;
    }


    /**
     * Returns the list of collections with a particular name.
     *
     * @param userId       userId of user making request
     * @param name         name of the collections to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionsByName(String       userId,
                                                              String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getCollectionsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of collections with a particular category.  This is an optional text field in the collection element.
     *
     * @param userId       userId of user making request
     * @param category     the collection type value to match on.  If it is null, all collections with a null category are returned
     * @param queryOptions multiple options to control the query
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionsByCategory(String       userId,
                                                                  String       category,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName                  = "getCollectionsByCategory";
        final String collectionTypeParameterName = "category";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(category, collectionTypeParameterName, methodName);

        List<String> propertyNames = List.of(OpenMetadataProperty.CATEGORY.name);

        return super.getRootElementsByName(userId,
                                           category,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific collection.
     *
     * @param userId         userId of user making request
     * @param collectionGUID unique identifier of the required collection
     * @param getOptions     multiple options to control the query
     * @return collection properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getCollectionByGUID(String     userId,
                                                       String     collectionGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "getCollectionByGUID";

        return super.getRootElementByGUID(userId, collectionGUID, getOptions, methodName);
    }


    /**
     * Create a new generic collection.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the collection.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created Collection
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createCollection(String                                userId,
                                   NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   CollectionProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "createCollection";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param userId                       calling user
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createCollectionFromTemplate(String                 userId,
                                               TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               EntityProperties       replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection (returned from create)
     * @param updateOptions  provides a structure for the additional options when updating an element.
     * @param properties     properties for the collection.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateCollection(String               userId,
                                    String               collectionGUID,
                                    UpdateOptions        updateOptions,
                                    CollectionProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName        = "updateCollection";
        final String guidParameterName = "collectionGUID";

        return super.updateElement(userId,
                                   collectionGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param userId            userId of user making request
     * @param collectionGUID    unique identifier of the collection
     * @param parentGUID        unique identifier of referenceable object that the collection should be attached to
     * @param makeAnchorOptions options to control access to open metadata
     * @param properties        description of how the collection will be used.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachCollection(String userId,
                                 String collectionGUID,
                                 String parentGUID,
                                 MakeAnchorOptions makeAnchorOptions,
                                 ResourceListProperties properties) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName                  = "attachCollection";
        final String collectionGUIDParameterName = "collectionGUID";
        final String parentGUIDParameterName     = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        collectionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCollection(String userId,
                                 String collectionGUID,
                                 String parentGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachCollection";

        final String collectionGUIDParameterName = "collectionGUID";
        final String parentGUIDParameterName     = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        collectionGUID,
                                                        deleteOptions);
    }



    /**
     * Connect a data describing collection to an element using the DataDescription relationship (0580).
     *
     * @param userId            userId of user making request
     * @param dataDescriptionCollectionGUID    unique identifier of the collection
     * @param parentGUID        unique identifier of referenceable object that the collection should be attached to
     * @param makeAnchorOptions options to control access to open metadata
     * @param properties        description of how the collection will be used.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachDataDescription(String                    userId,
                                      String                    parentGUID,
                                      String                    dataDescriptionCollectionGUID,
                                      MakeAnchorOptions         makeAnchorOptions,
                                      DataDescriptionProperties properties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName                  = "attachDataDescription";
        final String collectionGUIDParameterName = "dataDescriptionCollectionGUID";
        final String parentGUIDParameterName     = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDescriptionCollectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        dataDescriptionCollectionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Detach an existing data describing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param userId         userId of user making request.
     * @param dataDescriptionCollectionGUID unique identifier of the collection.
     * @param parentGUID     unique identifier of referenceable object that the collection should be attached to.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataDescription(String        userId,
                                      String        parentGUID,
                                      String        dataDescriptionCollectionGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachDataDescription";

        final String collectionGUIDParameterName = "dataDescriptionCollectionGUID";
        final String parentGUIDParameterName     = "parentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDescriptionCollectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(parentGUID, parentGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        dataDescriptionCollectionGUID,
                                                        deleteOptions);
    }




    /**
     * Attach a solution blueprint to the element it describes.
     *
     * @param userId                  userId of user making request
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionDesign(String                   userId,
                                   String                   parentGUID,
                                   String                   solutionBlueprintGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
                                   SolutionDesignProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkSolutionDesign";
        final String end1GUIDParameterName = "parentGUID";
        final String end2GUIDParameterName = "solutionBlueprintGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionBlueprintGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_DESIGN_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        solutionBlueprintGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a solution blueprint from the element it describes.
     *
     * @param userId                 userId of user making request.
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionDesign(String        userId,
                                     String        parentGUID,
                                     String        solutionBlueprintGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachSolutionDesign";

        final String end1GUIDParameterName = "parentGUID";
        final String end2GUIDParameterName = "solutionBlueprintGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionBlueprintGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_DESIGN_RELATIONSHIP.typeName,
                                                        parentGUID,
                                                        solutionBlueprintGUID,
                                                        deleteOptions);
    }


    /**
     * Link two dependent products.
     *
     * @param userId                     userId of user making request
     * @param consumerDigitalProductGUID unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID unique identifier of the digital product that it is using.
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties     description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDigitalProductDependency(String                             userId,
                                             String                             consumerDigitalProductGUID,
                                             String                             consumedDigitalProductGUID,
                                             MakeAnchorOptions                  makeAnchorOptions,
                                             DigitalProductDependencyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkDigitalProductDependency";
        final String end1GUIDParameterName = "consumerDigitalProductGUID";
        final String end2GUIDParameterName = "consumedDigitalProductGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(consumerDigitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(consumedDigitalProductGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                        consumerDigitalProductGUID,
                                                        consumedDigitalProductGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Unlink dependent products.
     *
     * @param userId                     userId of user making request.
     * @param consumerDigitalProductGUID unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID unique identifier of the digital product that it is using.
     * @param deleteOptions              options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDigitalProductDependency(String userId,
                                               String consumerDigitalProductGUID,
                                               String consumedDigitalProductGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachDigitalProductDependency";

        final String end1GUIDParameterName = "consumerDigitalProductGUID";
        final String end2GUIDParameterName = "consumedDigitalProductGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(consumerDigitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(consumedDigitalProductGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                        consumerDigitalProductGUID,
                                                        consumedDigitalProductGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a subscriber to a subscription.
     *
     * @param userId                  userId of user making request
     * @param digitalSubscriberGUID   unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param makeAnchorOptions   options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubscriber(String                      userId,
                               String                      digitalSubscriberGUID,
                               String                      digitalSubscriptionGUID,
                               MakeAnchorOptions           makeAnchorOptions,
                               DigitalSubscriberProperties relationshipProperties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName            = "linkSubscriber";
        final String end1GUIDParameterName = "digitalSubscriberGUID";
        final String end2GUIDParameterName = "digitalSubscriptionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalSubscriberGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalSubscriptionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName,
                                                        digitalSubscriberGUID,
                                                        digitalSubscriptionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a subscriber from a subscription.
     *
     * @param userId                  userId of user making request.
     * @param digitalSubscriberGUID   unique identifier of the subscriber (referenceable)
     * @param digitalSubscriptionGUID unique identifier of the  digital subscription agreement
     * @param deleteOptions           options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubscriber(String        userId,
                                 String        digitalSubscriberGUID,
                                 String        digitalSubscriptionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachSubscriber";

        final String end1GUIDParameterName = "digitalSubscriberGUID";
        final String end2GUIDParameterName = "digitalSubscriptionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalSubscriberGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalSubscriptionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_SUBSCRIBER_RELATIONSHIP.typeName,
                                                        digitalSubscriberGUID,
                                                        digitalSubscriptionGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a product manager to a digital product.
     *
     * @param userId                        userId of user making request
     * @param digitalProductGUID            unique identifier of the digital product
     * @param digitalProductManagerRoleGUID unique identifier of the product manager role
     * @param makeAnchorOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkProductManager(String                    userId,
                                   String                    digitalProductGUID,
                                   String                    digitalProductManagerRoleGUID,
                                   MakeAnchorOptions         makeAnchorOptions,
                                   AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "linkProductManager";
        final String end1GUIDParameterName = "digitalProductGUID";
        final String end2GUIDParameterName = "digitalProductManagerRoleGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalProductManagerRoleGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        digitalProductManagerRoleGUID,
                                                        digitalProductGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param userId                    userId of user making request.
     * @param digitalProductGUID        unique identifier of the digital product
     * @param digitalProductManagerGUID unique identifier of the product manager role
     * @param deleteOptions             options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProductManager(String        userId,
                                     String        digitalProductGUID,
                                     String        digitalProductManagerGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachProductManager";

        final String end1GUIDParameterName = "digitalProductGUID";
        final String end2GUIDParameterName = "digitalProductManagerGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(digitalProductGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(digitalProductManagerGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        digitalProductManagerGUID,
                                                        digitalProductGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an actor to an agreement.
     *
     * @param userId                 userId of user making request
     * @param agreementGUID          unique identifier of the agreement
     * @param actorGUID              unique identifier of the actor
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkAgreementActor(String                   userId,
                                     String                   agreementGUID,
                                     String                   actorGUID,
                                     MakeAnchorOptions        makeAnchorOptions,
                                     AgreementActorProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkAgreementActor";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, end2GUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.AGREEMENT_ACTOR_RELATIONSHIP.typeName,
                                                               agreementGUID,
                                                               actorGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an actor from an agreement.
     *
     * @param userId                         userId of user making request.
     * @param agreementActorRelationshipGUID unique identifier of the element being described
     * @param deleteOptions                  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementActor(String        userId,
                                     String        agreementActorRelationshipGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachAgreementActor";

        final String end1GUIDParameterName = "agreementActorRelationshipGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementActorRelationshipGUID, end1GUIDParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId,
                                                     agreementActorRelationshipGUID,
                                                     deleteOptions);
    }


    /**
     * Attach an agreement to an element involved in its definition.
     *
     * @param userId                 userId of user making request
     * @param agreementGUID          unique identifier of the agreement
     * @param agreementItemGUID      unique identifier of the agreement item
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAgreementItem(String                  userId,
                                  String                  agreementGUID,
                                  String                  agreementItemGUID,
                                  MakeAnchorOptions       makeAnchorOptions,
                                  AgreementItemProperties relationshipProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkAgreementItem";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "agreementItemGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(agreementItemGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        agreementItemGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an agreement from an element involved in its definition.
     *
     * @param userId            userId of user making request.
     * @param agreementGUID     unique identifier of the agreement
     * @param agreementItemGUID unique identifier of the agreement item
     * @param deleteOptions     options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAgreementItem(String        userId,
                                    String        agreementGUID,
                                    String        agreementItemGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachAgreementItem";

        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "agreementItemGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(agreementItemGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        agreementItemGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an agreement to an external reference element that describes the location of the contract documents.
     *
     * @param userId                 userId of user making request
     * @param agreementGUID          unique identifier of the agreement
     * @param externalReferenceGUID  unique identifier of the external reference describing the location of the contract
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContract(String                 userId,
                             String                 agreementGUID,
                             String                 externalReferenceGUID,
                             MakeAnchorOptions      metadataSourceOptions,
                             ContractLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName            = "linkContract";
        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        externalReferenceGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an agreement from an external reference describing the location of the contract documents.
     *
     * @param userId                userId of user making request.
     * @param agreementGUID         unique identifier of the agreement
     * @param externalReferenceGUID unique identifier of the external reference describing the location of the contract
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContract(String        userId,
                               String        agreementGUID,
                               String        externalReferenceGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachContract";

        final String end1GUIDParameterName = "agreementGUID";
        final String end2GUIDParameterName = "externalReferenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(agreementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(externalReferenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName,
                                                        agreementGUID,
                                                        externalReferenceGUID,
                                                        deleteOptions);
    }


    /**
     * Link dependent business capabilities.
     *
     * @param userId                 userId of user making request
     * @param businessCapabilityGUID          unique identifier of the business capability that is dependent on another
     * @param supportingBusinessCapabilityGUID  unique identifier of the business capability that is supporting
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkBusinessCapabilityDependency(String                                 userId,
                                                 String                                 businessCapabilityGUID,
                                                 String                                 supportingBusinessCapabilityGUID,
                                                 MakeAnchorOptions                      metadataSourceOptions,
                                                 BusinessCapabilityDependencyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName            = "linkBusinessCapabilityDependency";
        final String end1GUIDParameterName = "businessCapabilityGUID";
        final String end2GUIDParameterName = "supportingBusinessCapabilityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(businessCapabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(supportingBusinessCapabilityGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.BUSINESS_CAPABILITY_DEPENDENCY_RELATIONSHIP.typeName,
                                                        businessCapabilityGUID,
                                                        supportingBusinessCapabilityGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach dependent business capabilities.
     *
     * @param userId                userId of user making request.
     * @param businessCapabilityGUID          unique identifier of the business capability that is dependent on another
     * @param supportingBusinessCapabilityGUID  unique identifier of the business capability that is supporting
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachBusinessCapabilityDependency(String        userId,
                                                   String        businessCapabilityGUID,
                                                   String        supportingBusinessCapabilityGUID,
                                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "detachBusinessCapabilityDependency";

        final String end1GUIDParameterName = "businessCapabilityGUID";
        final String end2GUIDParameterName = "supportingBusinessCapabilityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(businessCapabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(supportingBusinessCapabilityGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.BUSINESS_CAPABILITY_DEPENDENCY_RELATIONSHIP.typeName,
                                                        businessCapabilityGUID,
                                                        supportingBusinessCapabilityGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a business capability to an element that provides digital support.
     *
     * @param userId                 userId of user making request
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDigitalSupport(String                   userId,
                                   String                   businessCapabilityGUID,
                                   String                   elementGUID,
                                   MakeAnchorOptions        metadataSourceOptions,
                                   DigitalSupportProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkDigitalSupport";
        final String end1GUIDParameterName = "businessCapabilityGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(businessCapabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_SUPPORT_RELATIONSHIP.typeName,
                                                        businessCapabilityGUID,
                                                        elementGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a business capability from an element that provides digital support.
     *
     * @param userId                userId of user making request.
     * @param businessCapabilityGUID          unique identifier of the business capability
     * @param elementGUID  unique identifier of the element
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDigitalSupport(String        userId,
                                     String        businessCapabilityGUID,
                                     String        elementGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachDigitalSupport";

        final String end1GUIDParameterName = "businessCapabilityGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(businessCapabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DIGITAL_SUPPORT_RELATIONSHIP.typeName,
                                                        businessCapabilityGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }


    /**
     * Classify an element to indicate that it is significant to a particular business capability.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID    unique identifier of the element.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setBusinessSignificant(String                        userId,
                                       String                        elementGUID,
                                       BusinessSignificantProperties properties,
                                       MetadataSourceOptions         metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "setBusinessSignificant";
        final String guidParameterName = "elementGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.BUSINESS_SIGNIFICANT_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the business significant classification from the element.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID    unique identifier of the element.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearBusinessSignificance(String                userId,
                                          String                elementGUID,
                                          MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "clearBusinessSignificance";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.BUSINESS_SIGNIFICANT_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the collection to indicate that it is an editing collection - this means it is
     * a collection of element copies that will eventually be merged back into .
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID    unique identifier of the collection.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setEditingCollection(String                      userId,
                                     String                      collectionGUID,
                                     EditingCollectionProperties properties,
                                     MetadataSourceOptions       metadataSourceOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "setEditingCollection";
        final String guidParameterName = "collectionGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          collectionGUID,
                                                          OpenMetadataType.EDITING_COLLECTION_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the editing collection classification from the collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID    unique identifier of the collection.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearEditingCollection(String                userId,
                                       String                collectionGUID,
                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "clearEditingCollection";
        final String guidParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            collectionGUID,
                                                            OpenMetadataType.EDITING_COLLECTION_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the collection to indicate that it is a staging collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID    unique identifier of the collection.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setStagingCollection(String                    userId,
                                     String                    collectionGUID,
                                     StagingCollectionProperties properties,
                                     MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "setStagingCollection";
        final String guidParameterName = "collectionGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          collectionGUID,
                                                          OpenMetadataType.STAGING_COLLECTION_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the staging collection classification from the collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID    unique identifier of the collection.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearStagingCollection(String                userId,
                                       String                collectionGUID,
                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "clearStagingCollection";
        final String guidParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            collectionGUID,
                                                            OpenMetadataType.STAGING_COLLECTION_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the collection to indicate that it is a scoping collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID    unique identifier of the collection.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setScopingCollection(String                      userId,
                                     String                      collectionGUID,
                                     ScopingCollectionProperties properties,
                                     MetadataSourceOptions       metadataSourceOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "setScopingCollection";
        final String guidParameterName = "collectionGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          collectionGUID,
                                                          OpenMetadataType.SCOPING_COLLECTION_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the scoping collection classification from the collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID    unique identifier of the collection.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearScopingCollection(String                userId,
                                       String                collectionGUID,
                                       MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "clearScopingCollection";
        final String guidParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            collectionGUID,
                                                            OpenMetadataType.SCOPING_COLLECTION_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsTaxonomy(String                userId,
                                      String                glossaryGUID,
                                      TaxonomyProperties properties,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "setGlossaryAsTaxonomy";
        final String guidParameterName = "glossaryGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryGUID,
                                                          OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the taxonomy glossary classification from the glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsTaxonomy(String                userId,
                                        String                glossaryGUID,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "clearGlossaryAsTaxonomy";
        final String guidParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryGUID,
                                                            OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }



    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsCanonical(String                       userId,
                                       String                        glossaryGUID,
                                       CanonicalVocabularyProperties properties,
                                       MetadataSourceOptions         metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "setGlossaryAsCanonical";
        final String guidParameterName = "glossaryGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryGUID,
                                                          OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsCanonical(String                userId,
                                         String                glossaryGUID,
                                         MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "clearGlossaryAsCanonical";
        final String guidParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryGUID,
                                                            OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Delete a collection.  It is detached from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection
     * @param deleteOptions  options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteCollection(String userId,
                                 String collectionGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName                  = "deleteCollection";
        final String collectionGUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, collectionGUID, deleteOptions);
    }


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param queryOptions   options for query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCollectionMembers(String userId,
                                                              String collectionGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName                  = "getCollectionMembers";
        final String collectionGUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        return super.getRelatedRootElements(userId,
                                            collectionGUID,
                                            collectionGUIDParameterName,
                                            1,
                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                            OpenMetadataType.REFERENCEABLE.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Return a nested hierarchy of a collection along
     * with elements immediately connected to the starting collection.  The result
     * includes a mermaid graph of the returned elements.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param queryOptions   multiple options to control the query
     * @return list of member details
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootHierarchy getCollectionHierarchy(String       userId,
                                                            String       collectionGUID,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName                  = "getCollectionHierarchy";
        final String collectionGUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        OpenMetadataRootHierarchy collectionHierarchy = this.convertToCollectionGraph(userId,
                                                                                      this.getCollectionByGUID(userId, collectionGUID, queryOptions),
                                                                                      queryOptions,
                                                                                      0);

        String membershipName = "Collection Hierarchy";

        if ((queryOptions != null) && (queryOptions.getGraphQueryDepth() != 0))
        {
            membershipName = membershipName + " - query depth=" + queryOptions.getGraphQueryDepth();
        }

        if (collectionHierarchy != null)
        {
            OpenMetadataRootHierarchyMermaidGraphBuilder graphBuilder = new OpenMetadataRootHierarchyMermaidGraphBuilder(collectionHierarchy,
                                                                                                                         membershipName,
                                                                                                                         VisualStyle.COLLECTION);

            collectionHierarchy.setMermaidGraph(graphBuilder.getMermaidGraph());

            return collectionHierarchy;
        }


        return null;
    }


    /**
     * Convert the members of this collection to a nested hierarchy of collections.
     *
     * @param userId calling user
     * @param collectionElement starting collection
     * @param queryOptions options to control the query
     * @param currentDepth current depth of this query
     * @return nested hierarchy of collections
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private OpenMetadataRootHierarchy convertToCollectionGraph(String                  userId,
                                                               OpenMetadataRootElement collectionElement,
                                                               QueryOptions            queryOptions,
                                                               int                     currentDepth) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        if (collectionElement != null)
        {
            OpenMetadataRootHierarchy collectionHierarchy = new OpenMetadataRootHierarchy(collectionElement);

            if ((queryOptions == null) || (queryOptions.getGraphQueryDepth() > currentDepth))
            {
                /*
                 * OK to keep digging
                 */
                if (collectionElement.getCollectionMembers() != null)
                {
                    List<OpenMetadataRootHierarchy>     collectionMembers = new ArrayList<>();
                    List<RelatedMetadataElementSummary> otherMembers      = new ArrayList<>();

                    for (RelatedMetadataElementSummary collectionMember : collectionElement.getCollectionMembers())
                    {
                        if (collectionMember != null)
                        {
                            if (propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.COLLECTION.typeName))
                            {
                                OpenMetadataRootElement nestedCollection = this.getCollectionByGUID(userId,
                                                                                                    collectionMember.getRelatedElement().getElementHeader().getGUID(),
                                                                                                    queryOptions);

                                collectionMembers.add(this.convertToCollectionGraph(userId, nestedCollection, queryOptions, currentDepth + 1));
                            }
                            else
                            {
                                otherMembers.add(collectionMember);
                            }
                        }
                    }

                    if (!otherMembers.isEmpty())
                    {
                        collectionHierarchy.setCollectionMembers(otherMembers);
                    }
                    else
                    {
                        collectionHierarchy.setCollectionMembers(null);
                    }

                    if (!collectionMembers.isEmpty())
                    {
                        collectionHierarchy.setOpenMetadataRootHierarchies(collectionMembers);
                    }
                }
            }

            return collectionHierarchy;
        }

        return null;
    }


    /**
     * @param userId         calling user
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the collection member
     * @param queryOptions   multiple options to control the query
     * @return unique identifier for the relationship between the two elements
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String getMembershipRelationshipGUID(String       userId,
                                                 String       collectionGUID,
                                                 String       elementGUID,
                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        OpenMetadataRelationshipList linkedResources = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                          collectionGUID,
                                                                                                          elementGUID,
                                                                                                          OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                          queryOptions);

        if ((linkedResources != null) && (linkedResources.getRelationships() != null))
        {
            for (OpenMetadataRelationship relatedMetadataElement : linkedResources.getRelationships())
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
     * @param userId                userId of user making request.
     * @param collectionGUID        unique identifier of the collection.
     * @param makeAnchorOptions options to control access to open metadata
     * @param membershipProperties  properties describing the membership characteristics.
     * @param elementGUID           unique identifier of the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addToCollection(String                         userId,
                                String                         collectionGUID,
                                String                         elementGUID,
                                MakeAnchorOptions              makeAnchorOptions,
                                CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName                  = "addToCollection";
        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName    = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        String relationshipGUID = this.getMembershipRelationshipGUID(userId, collectionGUID, elementGUID, new QueryOptions(makeAnchorOptions));

        if (relationshipGUID == null)
        {
            openMetadataClient.createRelatedElementsInStore(userId,
                                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                            collectionGUID,
                                                            elementGUID,
                                                            makeAnchorOptions,
                                                            relationshipBuilder.getNewElementProperties(membershipProperties));
        }
        else
        {
            this.updateCollectionMembership(userId,
                                            relationshipGUID,
                                            new UpdateOptions(makeAnchorOptions),
                                            membershipProperties);
        }
    }


    /**
     * Update an element's membership to a collection.
     *
     * @param userId               userId of user making request.
     * @param collectionGUID       unique identifier of the collection.
     * @param elementGUID          unique identifier of the element.
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param membershipProperties properties describing the membership characteristics.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCollectionMembership(String                         userId,
                                           String                         collectionGUID,
                                           String                         elementGUID,
                                           UpdateOptions                  updateOptions,
                                           CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "updateCollectionMembership";

        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName    = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        String relationshipGUID = this.getMembershipRelationshipGUID(userId, collectionGUID, elementGUID, new QueryOptions(updateOptions));

        if (relationshipGUID != null)
        {
            this.updateCollectionMembership(userId, relationshipGUID, updateOptions, membershipProperties);
        }
        else
        {
            this.addToCollection(userId, collectionGUID, elementGUID, new MakeAnchorOptions(updateOptions), membershipProperties);
        }
    }


    /**
     * Update the properties of a collection membership relationship.
     *
     * @param userId               calling user
     * @param relationshipGUID     unique identifier of the collection
     * @param updateOptions        provides a structure for the additional options when updating a relationship.
     * @param membershipProperties properties describing the membership characteristics.
     */
    public void updateCollectionMembership(String                         userId,
                                           String                         relationshipGUID,
                                           UpdateOptions                  updateOptions,
                                           CollectionMembershipProperties membershipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     relationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(membershipProperties));
    }


    /**
     * Remove an element from a collection.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection.
     * @param elementGUID    unique identifier of the element.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeFromCollection(String        userId,
                                     String        collectionGUID,
                                     String        elementGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "removeFromCollection";

        final String collectionGUIDParameterName = "collectionGUID";
        final String elementGUIDParameterName    = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                        collectionGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }
}