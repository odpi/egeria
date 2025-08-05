/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.GovernanceDefinitionGraphMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SupportingDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * GovernanceDefinitionHandler is the handler for managing governance definitions.
 */
public class GovernanceDefinitionHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    GovernanceDefinitionHandler(String             localServerName,
                                AuditLog           auditLog,
                                String             serviceName,
                                OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.GOVERNANCE_DEFINITION.typeName);
    }


    /**
     * Create a new governance definition.
     *
     * @param userId                       userId of user making request.
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createGovernanceDefinition(String                                userId,
                                             NewElementOptions                     newElementOptions,
                                             Map<String, ClassificationProperties> initialClassifications,
                                             GovernanceDefinitionProperties        properties,
                                             RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                        PropertyServerException,
                                                                                                                        UserNotAuthorizedException
    {
        final String methodName = "createGovernanceDefinition";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }

    
    /**
     * Create a new metadata element to represent a governance definition using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new governance definition.
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
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGovernanceDefinitionFromTemplate(String                 userId,
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
     * Update the properties of a governance definition.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionGUID      unique identifier of the governance definition (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGovernanceDefinition(String                         userId,
                                           String                         governanceDefinitionGUID,
                                           UpdateOptions                  updateOptions,
                                           GovernanceDefinitionProperties properties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "updateGovernanceDefinition";
        final String guidParameterName = "governanceDefinitionGUID";

        super.updateElement(userId,
                            governanceDefinitionGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }
    

    /**
     * Attach two peer governance definitions.
     *
     * @param userId                  userId of user making request
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerDefinitions(String                    userId,
                                    String                    governanceDefinitionOneGUID,
                                    String                    governanceDefinitionTwoGUID,
                                    String                    relationshipTypeName,
                                    MetadataSourceOptions     metadataSourceOptions,
                                    PeerDefinitionProperties  relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "linkPeerDefinitions";
        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a governance definition from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerDefinitions(String        userId,
                                      String        governanceDefinitionOneGUID,
                                      String        governanceDefinitionTwoGUID,
                                      String        relationshipTypeName,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachMemberDataField";

        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a supporting governance definition.
     *
     * @param userId                  userId of user making request
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachSupportingDefinition(String                         userId,
                                           String                         governanceDefinitionOneGUID,
                                           String                         governanceDefinitionTwoGUID,
                                           String                         relationshipTypeName,
                                           MetadataSourceOptions          metadataSourceOptions,
                                           SupportingDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "attachSupportingDefinition";
        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));

    }


    /**
     * Detach a governance definition from a supporting governance definition.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSupportingDefinition(String        userId,
                                           String        governanceDefinitionOneGUID,
                                           String        governanceDefinitionTwoGUID,
                                           String        relationshipTypeName,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachSupportingDefinition";

        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a governance definition.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionGUID      unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGovernanceDefinition(String        userId,
                                           String        governanceDefinitionGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "deleteGovernanceDefinition";
        final String guidParameterName = "governanceDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, governanceDefinitionGUID, deleteOptions);
    }


    /**
     * Returns the list of governance definitions with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGovernanceDefinitionsByName(String       userId,
                                                                        String       name,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getGovernanceDefinitionsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Retrieve the list of governance definitions metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGovernanceDefinitions(String        userId,
                                                                   String        searchString,
                                                                   SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "findGovernanceDefinitions";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param userId                 userId of user making request
     * @param governanceDefinitionGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGovernanceDefinitionByGUID(String     userId,
                                                                 String     governanceDefinitionGUID,
                                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getGovernanceDefinitionByGUID";

        return super.getRootElementByGUID(userId, governanceDefinitionGUID, getOptions, methodName);
    }


    /**
     * Retrieve the definition metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the requested metadata element
     * @param queryOptions           multiple options to control the query
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDefinitionGraph getGovernanceDefinitionInContext(String       userId,
                                                                      String       governanceDefinitionGUID,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        OpenMetadataRootElement rootElement = this.getGovernanceDefinitionByGUID(userId, governanceDefinitionGUID, queryOptions);

        if (rootElement instanceof GovernanceDefinitionElement governanceDefinitionElement)
        {
            GovernanceDefinitionGraph governanceDefinitionGraph = new GovernanceDefinitionGraph(governanceDefinitionElement);

            Set<String>  processedGovernanceDefinitions = new HashSet<>(Collections.singletonList(governanceDefinitionGUID));

            governanceDefinitionGraph.setRelatedGovernanceDefinitions(this.getRelatedGovernanceDefinitions(userId,
                                                                                                           governanceDefinitionElement,
                                                                                                           queryOptions,
                                                                                                           processedGovernanceDefinitions));

            /*
             * Replaces the graph added by addMermaidToRootElement().
             */
            GovernanceDefinitionGraphMermaidGraphBuilder mermaidGraphBuilder = new GovernanceDefinitionGraphMermaidGraphBuilder(governanceDefinitionGraph);

            governanceDefinitionGraph.setMermaidGraph(mermaidGraphBuilder.getMermaidGraph());

            return governanceDefinitionGraph;
        }

        return null;
    }


    /**
     * Retrieve the list of related governance definitions not currently in the list.
     *
     * @param userId calling user
     * @param startingGovernanceDefinition place to start
     * @param queryOptions type of query
     * @param processedGovernanceDefinitions list of guids of governance definitions already processed.
     * @return list of connected governance definitions not yet processed
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private List<GovernanceDefinitionElement> getRelatedGovernanceDefinitions(String                        userId,
                                                                              GovernanceDefinitionElement   startingGovernanceDefinition,
                                                                              QueryOptions                  queryOptions,
                                                                              Set<String>                   processedGovernanceDefinitions) throws InvalidParameterException,
                                                                                                                                                   PropertyServerException,
                                                                                                                                                   UserNotAuthorizedException
    {
        List<GovernanceDefinitionElement> relatedElements = new ArrayList<>();

        if (! processedGovernanceDefinitions.contains(startingGovernanceDefinition.getElementHeader().getGUID()))
        {
            if (startingGovernanceDefinition.getPeerGovernanceDefinitions() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingGovernanceDefinition.getPeerGovernanceDefinitions())
                {
                    if ((relatedDefinition != null) && (! processedGovernanceDefinitions.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        GovernanceDefinitionElement relatedElement = (GovernanceDefinitionElement) this.getGovernanceDefinitionByGUID(userId,
                                                                                                                                   relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                                   queryOptions);

                        relatedElements.add(relatedElement);
                        processedGovernanceDefinitions.add(relatedElement.getElementHeader().getGUID());

                        getRelatedGovernanceDefinitions(userId, relatedElement, queryOptions, processedGovernanceDefinitions);
                    }
                }
            }

            if (startingGovernanceDefinition.getSupportingGovernanceDefinitions() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingGovernanceDefinition.getSupportingGovernanceDefinitions())
                {
                    if ((relatedDefinition != null) && (! processedGovernanceDefinitions.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        GovernanceDefinitionElement relatedElement = (GovernanceDefinitionElement) this.getGovernanceDefinitionByGUID(userId,
                                                                                                                                   relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                                   queryOptions);

                        relatedElements.add(relatedElement);
                        processedGovernanceDefinitions.add(relatedElement.getElementHeader().getGUID());

                        getRelatedGovernanceDefinitions(userId, relatedElement, queryOptions, processedGovernanceDefinitions);
                    }
                }
            }

            if (startingGovernanceDefinition.getSupportedGovernanceDefinitions() != null)
            {
                for (RelatedMetadataElementSummary relatedDefinition : startingGovernanceDefinition.getSupportedGovernanceDefinitions())
                {
                    if ((relatedDefinition != null) && (! processedGovernanceDefinitions.contains(relatedDefinition.getRelatedElement().getElementHeader().getGUID())))
                    {
                        GovernanceDefinitionElement relatedElement = (GovernanceDefinitionElement) this.getGovernanceDefinitionByGUID(userId,
                                                                                                                                      relatedDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                                      queryOptions);

                        relatedElements.add(relatedElement);
                        processedGovernanceDefinitions.add(relatedElement.getElementHeader().getGUID());

                        getRelatedGovernanceDefinitions(userId, relatedElement, queryOptions, processedGovernanceDefinitions);
                    }
                }
            }
        }

        return relatedElements;
    }


    /*
     * Design to implementations
     */


    /**
     * Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship.
     *
     * @param userId                  userId of user making request
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationGUID      unique identifier of the implementation
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDesignToImplementation(String                  userId,
                                           String                  designGUID,
                                           String                  implementationGUID,
                                           MetadataSourceOptions   metadataSourceOptions,
                                           ImplementedByProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkDesignToImplementation";
        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a design object such as a solution component or governance definition from its implementation.
     *
     * @param userId                 userId of user making request.
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDesignFromImplementation(String        userId,
                                               String        designGUID,
                                               String        implementationGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachDefinitionImplementation";

        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship.
     *
     * @param userId                  userId of user making request
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationResourceGUID      unique identifier of the implementation
     * @param relationshipProperties  additional properties for the relationship.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkImplementationResource(String                           userId,
                                           String                           designGUID,
                                           String                           implementationResourceGUID,
                                           MetadataSourceOptions            metadataSourceOptions,
                                           ImplementationResourceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkImplementationResource";
        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationResourceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationResourceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationResourceGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a design object such as a solution component or governance definition from one of its implementation resources.
     *
     * @param userId                 userId of user making request.
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationResourceGUID     unique identifier of the implementation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachImplementationResource(String        userId,
                                             String        designGUID,
                                             String        implementationResourceGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachImplementationResource";

        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationResourceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationResourceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationResourceGUID,
                                                        deleteOptions);
    }
}
