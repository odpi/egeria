/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.InformationSupplyChainConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.InformationSupplyChainMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainComponent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainSegment;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * InformationSupplyChainHandler provides methods to define information supply chains.
 */
public class InformationSupplyChainHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public InformationSupplyChainHandler(String             localServerName,
                                         AuditLog           auditLog,
                                         String             serviceName,
                                         OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName);
    }


    /**
     * Create a new information supply chain.
     *
     * @param userId                 userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties             properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the newly created element
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createInformationSupplyChain(String                                userId,
                                               NewElementOptions                     newElementOptions,
                                               Map<String, ClassificationProperties> initialClassifications,
                                               InformationSupplyChainProperties      properties,
                                               RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                          PropertyServerException,
                                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "createInformationSupplyChain";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an information supply chain using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new information supply chain.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createInformationSupplyChainFromTemplate(String                 userId,
                                                           TemplateOptions        templateOptions,
                                                           String                 templateGUID,
                                                           ElementProperties      replacementProperties,
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
     * Update the properties of an information supply chain.
     *
     * @param userId                 userId of user making request.
     * @param informationSupplyChainGUID         unique identifier of the information supply chain (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateInformationSupplyChain(String                           userId,
                                               String                           informationSupplyChainGUID,
                                               UpdateOptions                    updateOptions,
                                               InformationSupplyChainProperties properties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "updateInformationSupplyChain";
        final String guidParameterName = "informationSupplyChainGUID";

        super.updateElement(userId,
                            informationSupplyChainGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Connect two peers in an information supply chains.  The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param userId          userId of user making request
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param metadataSourceOptions  options to control access to open metadata
     * @param linkProperties   description of the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeersInInformationSupplyChain(String                               userId,
                                                  String                               peerOneGUID,
                                                  String                               peerTwoGUID,
                                                  MetadataSourceOptions                metadataSourceOptions,
                                                  InformationSupplyChainLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "linkPeersInInformationSupplyChain";
        final String end1GUIDParameterName = "peerOneGUID";
        final String end2GUIDParameterName = "peerTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(peerOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(peerTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                                        peerOneGUID,
                                                        peerTwoGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(linkProperties));

    }


    /**
     * Detach two peers in an information supply chain from one another.    The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param userId          userId of user making request.
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkPeersInInformationSupplyChain(String        userId,
                                                    String        peerOneGUID,
                                                    String        peerTwoGUID,
                                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "unlinkPeersInInformationSupplyChain";

        final String end1GUIDParameterName = "peerOneGUID";
        final String end2GUIDParameterName = "peerTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(peerOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(peerTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                                        peerOneGUID,
                                                        peerTwoGUID,
                                                        deleteOptions);
    }



    /**
     * Connect two peers in an information supply chains.  The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param userId          userId of user making request
     * @param informationSupplyChainGUID  unique identifier of the parent information supply chain
     * @param nestedInformationSupplyChainGUID      unique identifier of the child information supply chain
     * @param metadataSourceOptions  options to control access to open metadata
     * @param compositionProperties   description of the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void composeInformationSupplyChains(String                                      userId,
                                               String                                      informationSupplyChainGUID,
                                               String                                      nestedInformationSupplyChainGUID,
                                               MetadataSourceOptions                       metadataSourceOptions,
                                               InformationSupplyChainCompositionProperties compositionProperties) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "composeInformationSupplyChains";
        final String end1GUIDParameterName = "informationSupplyChainGUID";
        final String end2GUIDParameterName = "nestedInformationSupplyChainGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(informationSupplyChainGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedInformationSupplyChainGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName,
                                                        informationSupplyChainGUID,
                                                        nestedInformationSupplyChainGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(compositionProperties));
    }


    /**
     * Detach a nested information supply chain from its parent.
     *
     * @param userId          userId of user making request.
     * @param informationSupplyChainGUID  unique identifier of the parent information supply chain
     * @param nestedInformationSupplyChainGUID      unique identifier of the child information supply chain
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void decomposeInformationSupplyChains(String        userId,
                                                 String        informationSupplyChainGUID,
                                                 String        nestedInformationSupplyChainGUID,
                                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "decomposeInformationSupplyChains";

        final String end1GUIDParameterName = "informationSupplyChainGUID";
        final String end2GUIDParameterName = "nestedInformationSupplyChainGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(informationSupplyChainGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedInformationSupplyChainGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName,
                                                        informationSupplyChainGUID,
                                                        nestedInformationSupplyChainGUID,
                                                        deleteOptions);
    }


    /**
     * Delete an information supply chain and all of its segments.
     *
     * @param userId   userId of user making request.
     * @param informationSupplyChainGUID  unique identifier of the required element.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteInformationSupplyChain(String        userId,
                                             String        informationSupplyChainGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "deleteInformationSupplyChain";
        final String guidParameterName = "informationSupplyChainGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(informationSupplyChainGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, informationSupplyChainGUID, deleteOptions);
    }


    /**
     * Returns the list of information supply chains with a particular name.
     *
     * @param userId     userId of user making request
     * @param name       name of the element to return - match is full text match in qualifiedName or name
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param queryOptions           multiple options to control the query
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformationSupplyChainElement> getInformationSupplyChainsByName(String       userId,
                                                                                String       name,
                                                                                QueryOptions queryOptions,
                                                                                boolean      addImplementation) throws InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getInformationSupplyChainsByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
                                                                                                 null,
                                                                                                 super.addDefaultType(queryOptions));

        return convertInformationSupplyChains(userId,
                                              openMetadataElements,
                                              addImplementation,
                                              super.addDefaultType(queryOptions),
                                              methodName);
    }


    /**
     * Return the properties of a specific information supply chain.
     *
     * @param userId            userId of user making request
     * @param informationSupplyChainGUID    unique identifier of the required element
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public InformationSupplyChainElement getInformationSupplyChainByGUID(String     userId,
                                                                         String     informationSupplyChainGUID,
                                                                         boolean    addImplementation,
                                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getInformationSupplyChainByGUID";
        final String guidParameterName = "informationSupplyChainGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(informationSupplyChainGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                              informationSupplyChainGUID,
                                                                                              super.addDefaultType(getOptions));

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, metadataElementTypeName)))
        {
            return convertInformationSupplyChain(userId,
                                                 openMetadataElement,
                                                 addImplementation,
                                                 new QueryOptions(getOptions),
                                                 methodName);
        }

        return null;
    }



    /**
     * Retrieve the list of information supply chains metadata elements that contain the search string.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<InformationSupplyChainElement> findInformationSupplyChains(String        userId,
                                                                           String        searchString,
                                                                           boolean       addImplementation,
                                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String methodName = "findInformationSupplyChains";
        final String searchStringParameterName = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateSearchString(searchString, searchStringParameterName, methodName);
        propertyHelper.validatePaging(searchOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId, searchString, super.addDefaultType(searchOptions));

        return convertInformationSupplyChains(userId,
                                              openMetadataElements,
                                              addImplementation,
                                              searchOptions,
                                              methodName);
    }


    /**
     * Convert the open metadata elements retrieve into information supply chain elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param queryOptions           multiple options to control the query
     * @param methodName calling method
     * @return list of information supply chains (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<InformationSupplyChainElement> convertInformationSupplyChains(String                    userId,
                                                                               List<OpenMetadataElement> openMetadataElements,
                                                                               boolean                   addImplementation,
                                                                               QueryOptions              queryOptions,
                                                                               String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<InformationSupplyChainElement> informationSupplyChainElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    informationSupplyChainElements.add(convertInformationSupplyChain(userId,
                                                                                     openMetadataElement,
                                                                                     addImplementation,
                                                                                     queryOptions,
                                                                                     methodName));
                }
            }

            return informationSupplyChainElements;
        }

        return null;
    }



    /**
     * Return the information supply chain extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param queryOptions           multiple options to control the query
     * @param methodName calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private InformationSupplyChainElement convertInformationSupplyChain(String              userId,
                                                                        OpenMetadataElement openMetadataElement,
                                                                        boolean             addImplementation,
                                                                        QueryOptions        queryOptions,
                                                                        String              methodName) throws PropertyServerException
    {
        try
        {
            List<InformationSupplyChainComponent> relatedComponents    = new ArrayList<>();
            List<InformationSupplyChainSegment>   relatedSegments      = new ArrayList<>();
            List<RelatedMetadataElement>          otherRelatedElements = new ArrayList<>();

            /*
             * This is a temporary list for holding the elements linked by ImplementedBy and
             * InformationSupplyChainComposition relationships.
             */
            List<RelatedMetadataElement> extractedImplementedByElements = new ArrayList<>();
            List<RelatedMetadataElement> extractedSegmentElements       = new ArrayList<>();

            QueryOptions workingQueryOptions = new QueryOptions(queryOptions);
            workingQueryOptions.setStartFrom(0);
            workingQueryOptions.setPageSize(openMetadataClient.getMaxPagingSize());

            RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                  openMetadataElement.getElementGUID(),
                                                                                                                  0,
                                                                                                                  null,
                                                                                                                  workingQueryOptions);
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
                        {
                            extractedImplementedByElements.add(relatedMetadataElement);
                        }
                        else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                        {
                            extractedSegmentElements.add(relatedMetadataElement);
                        }
                        else
                        {
                            otherRelatedElements.add(relatedMetadataElement);
                        }
                    }
                }

                workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataClient.getMaxPagingSize());
                relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                           openMetadataElement.getElementGUID(),
                                                                                           0,
                                                                                           null,
                                                                                           workingQueryOptions);
            }

            if (! extractedSegmentElements.isEmpty())
            {
                for (RelatedMetadataElement relatedMetadataElement : extractedImplementedByElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        InformationSupplyChainSegment segment = this.getSegment(userId,
                                                                                relatedMetadataElement,
                                                                                queryOptions,
                                                                                methodName);

                        if (segment != null)
                        {
                            relatedSegments.add(segment);
                        }
                    }
                }
            }
            else
            {
                relatedSegments = null;
            }

            if (! extractedImplementedByElements.isEmpty())
            {
                List<String> processedComponentGUIDs = new ArrayList<>();

                for (RelatedMetadataElement relatedMetadataElement : extractedImplementedByElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        InformationSupplyChainComponent informationSupplyChainComponent = this.getInformationSupplyChainComponent(userId,
                                                                                                                                  relatedMetadataElement,
                                                                                                                                  queryOptions,
                                                                                                                                  processedComponentGUIDs,
                                                                                                                                  methodName);


                        if (informationSupplyChainComponent != null)
                        {
                            relatedComponents.add(informationSupplyChainComponent);
                        }
                    }
                }
            }
            else
            {
                relatedComponents = null;
            }
            List<OpenMetadataRelationship> lineageRelationships = null;

            /*
             * The implementation is a query to extract all lineage relationships that iscQualifiedName in
             * their properties, and it is set to this information supply chain's qualified name.
             */
            if (addImplementation)
            {
                lineageRelationships = new ArrayList<>();

                workingQueryOptions.setStartFrom(0);

                SearchProperties        searchProperties   = new SearchProperties();
                List<PropertyCondition> propertyConditions = new ArrayList<>();
                PropertyCondition       propertyCondition  = new PropertyCondition();

                propertyCondition.setProperty(OpenMetadataProperty.ISC_QUALIFIED_NAME.name);

                propertyCondition.setOperator(PropertyComparisonOperator.EQ);
                propertyCondition.setValue(openMetadataElement.getElementProperties().getPropertyValue(OpenMetadataProperty.QUALIFIED_NAME.name));

                propertyConditions.add(propertyCondition);

                searchProperties.setMatchCriteria(MatchCriteria.ANY);
                searchProperties.setConditions(propertyConditions);

                OpenMetadataRelationshipList relationshipList = openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                                            null,
                                                                                                                            searchProperties,
                                                                                                                            workingQueryOptions);

                while ((relationshipList != null) && (relationshipList.getElementList() != null))
                {
                    lineageRelationships.addAll(relationshipList.getElementList());

                    workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataClient.getMaxPagingSize());

                    relationshipList = openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                        null,
                                                                                                        searchProperties,
                                                                                                        workingQueryOptions);
                }
            }

            InformationSupplyChainConverter<InformationSupplyChainElement> converter = new InformationSupplyChainConverter<>(propertyHelper, localServiceName, localServerName, relatedSegments, relatedComponents, lineageRelationships);
            InformationSupplyChainElement informationSupplyChainElement = converter.getNewComplexBean(InformationSupplyChainElement.class,
                                                                                                      openMetadataElement,
                                                                                                      otherRelatedElements,
                                                                                                      methodName);
            if (informationSupplyChainElement != null)
            {
                InformationSupplyChainMermaidGraphBuilder graphBuilder = new InformationSupplyChainMermaidGraphBuilder(informationSupplyChainElement);

                informationSupplyChainElement.setMermaidGraph(graphBuilder.getMermaidGraph(false));
            }

            return informationSupplyChainElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                               methodName,
                                                                                                       localServiceName,
                                                                                                               error.getMessage()),
                                      error);
            }

            throw new PropertyServerException(OMFErrorCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                               localServiceName,
                                                                                                                       error.getMessage()),
                                              error.getClass().getName(),
                                              methodName,
                                              error);
        }
    }


    /**
     * Extract the elements related to this information supply chain's implementation components.  Typically,
     * they are solution components, but there may also be assets, if the design is integrating with
     * existing assets.
     *
     * @param userId calling user
     * @param startingElement retrieved implementation component
     * @param queryOptions           multiple options to control the query
     * @param methodName calling method
     * @return filled out component
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the conversion process, or repository
     * @throws UserNotAuthorizedException authorization issue
     */
    private InformationSupplyChainSegment getSegment(String                 userId,
                                                     RelatedMetadataElement startingElement,
                                                     QueryOptions           queryOptions,
                                                     String                 methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        if (startingElement != null)
        {
            InformationSupplyChainSegment informationSupplyChainComponent = new InformationSupplyChainSegment(propertyHelper.getRelatedElementSummary(startingElement));

            /*
             * Only pick up a single page to limit output
             */
            RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                  startingElement.getElement().getElementGUID(),
                                                                                                                  1,
                                                                                                                  OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                                                                                                  queryOptions);

            if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                List<InformationSupplyChainSegment> nestedSegments = new ArrayList<>();

                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        /*
                         * Solution components need additional information to extract ports and solution linking wires
                         */
                        if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName))
                        {
                            InformationSupplyChainSegment nestedSegment = this.getSegment(userId,
                                                                                          relatedMetadataElement,
                                                                                          queryOptions,
                                                                                          methodName);

                            if (nestedSegment != null)
                            {
                                nestedSegments.add(nestedSegment);
                            }
                        }
                        else
                        {
                            nestedSegments.add(new InformationSupplyChainSegment(propertyHelper.getRelatedElementSummary(relatedMetadataElement)));
                        }
                    }
                }

                if (!nestedSegments.isEmpty())
                {
                    informationSupplyChainComponent.setNestedSegments(nestedSegments);
                }
            }

            return informationSupplyChainComponent;
        }

        return null;
    }


    /**
     * Extract the elements related to this information supply chain's implementation components.  Typically,
     * they are solution components, but there may also be assets, if the design is integrating with
     * existing assets.
     *
     * @param userId calling user
     * @param startingElement retrieved implementation component
     * @param queryOptions           multiple options to control the query
     * @param processedComponentGUIDs list of GUID of components already processed
     * @param methodName calling method
     * @return filled out component
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the conversion process, or repository
     * @throws UserNotAuthorizedException authorization issue
     */
    private InformationSupplyChainComponent getInformationSupplyChainComponent(String                 userId,
                                                                               RelatedMetadataElement startingElement,
                                                                               QueryOptions           queryOptions,
                                                                               List<String>           processedComponentGUIDs,
                                                                               String                 methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        if (startingElement != null)
        {
            InformationSupplyChainComponent       informationSupplyChainComponent = new InformationSupplyChainComponent(propertyHelper.getRelatedElementSummary(startingElement));

            /*
             * Only pick up a single page to limit output
             */
            RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                  startingElement.getElement().getElementGUID(),
                                                                                                                  0,
                                                                                                                  null,
                                                                                                                  queryOptions);

            if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                List<InformationSupplyChainComponent> nestedComponents = new ArrayList<>();

                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if ((relatedMetadataElement != null) && (! processedComponentGUIDs.contains(relatedMetadataElement.getElement().getElementGUID())))
                    {
                        /*
                         * Only process each component once.
                         */
                        processedComponentGUIDs.add(relatedMetadataElement.getElement().getElementGUID());

                        /*
                         * Solution components need additional information to extract ports and solution linking wires
                         */
                        if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.SOLUTION_COMPONENT.typeName))
                        {
                            InformationSupplyChainComponent nestedComponent = this.getInformationSupplyChainComponent(userId,
                                                                                                                      relatedMetadataElement,
                                                                                                                      queryOptions,
                                                                                                                      processedComponentGUIDs,
                                                                                                                      methodName);

                            if (nestedComponent != null)
                            {
                                nestedComponents.add(nestedComponent);
                            }
                        }
                        else
                        {
                            nestedComponents.add(new InformationSupplyChainComponent(propertyHelper.getRelatedElementSummary(relatedMetadataElement)));
                        }
                    }
                }

                if (!nestedComponents.isEmpty())
                {
                    informationSupplyChainComponent.setNestedElements(nestedComponents);
                }
            }

            return informationSupplyChainComponent;
        }

        return null;
    }
}
