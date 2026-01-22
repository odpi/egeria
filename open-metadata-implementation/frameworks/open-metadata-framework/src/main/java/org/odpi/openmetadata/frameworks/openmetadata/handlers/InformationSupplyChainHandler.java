/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.MetadataRelationshipSummaryConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.InformationSupplyChainMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * InformationSupplyChainHandler provides methods to define information supply chains.
 */
public class InformationSupplyChainHandler extends CollectionHandler
{
    final private MetadataRelationshipSummaryConverter<MetadataRelationshipSummary> metadataRelationshipSummaryConverter;

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

        this.metadataRelationshipSummaryConverter = new MetadataRelationshipSummaryConverter<>(propertyHelper,
                                                                                               localServiceName,
                                                                                               localServerName);
    }


    /**
     * Connect two peers in an information supply chains.  The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param userId          userId of user making request
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param makeAnchorOptions  options to control access to open metadata
     * @param linkProperties   description of the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeersInInformationSupplyChain(String                               userId,
                                                  String                               peerOneGUID,
                                                  String                               peerTwoGUID,
                                                  MakeAnchorOptions                    makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @throws PropertyServerException a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getInformationSupplyChainsByName(String       userId,
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
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findInformationSupplyChains(String        userId,
                                                                     String        searchString,
                                                                     boolean       addImplementation,
                                                                     SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "findInformationSupplyChains";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validatePaging(searchOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           super.addDefaultType(searchOptions));

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
    private List<OpenMetadataRootElement> convertInformationSupplyChains(String                    userId,
                                                                               List<OpenMetadataElement> openMetadataElements,
                                                                               boolean                   addImplementation,
                                                                               QueryOptions              queryOptions,
                                                                               String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<OpenMetadataRootElement> informationSupplyChainElements = new ArrayList<>();

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
            OpenMetadataRootElement openMetadataRootElement = super.convertRootElement(userId, openMetadataElement,queryOptions, methodName);

            /*
             * The implementation is a query to extract all lineage relationships that iscQualifiedName in
             * their properties, and it is set to this information supply chain's qualified name.
             */
            List<OpenMetadataRelationship> lineageRelationships = new ArrayList<>();

            if (addImplementation)
            {
                QueryOptions workingQueryOptions = new QueryOptions(queryOptions);
                workingQueryOptions.setStartFrom(0);
                workingQueryOptions.setPageSize(openMetadataClient.getMaxPagingSize());

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

                while ((relationshipList != null) && (relationshipList.getRelationships() != null))
                {
                    lineageRelationships.addAll(relationshipList.getRelationships());

                    workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataClient.getMaxPagingSize());

                    relationshipList = openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                        null,
                                                                                                        searchProperties,
                                                                                                        workingQueryOptions);
                }
            }

            InformationSupplyChainElement informationSupplyChainElement = new InformationSupplyChainElement(openMetadataRootElement);


            informationSupplyChainElement.setImplementation(metadataRelationshipSummaryConverter.getNewBeans(MetadataRelationshipSummary.class,
                                                                                                             lineageRelationships,
                                                                                                             methodName));

            InformationSupplyChainMermaidGraphBuilder graphBuilder = new InformationSupplyChainMermaidGraphBuilder(informationSupplyChainElement);
            informationSupplyChainElement.setISCImplementationMermaidGraph(graphBuilder.getMermaidGraph(false));

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
}
