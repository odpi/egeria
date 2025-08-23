/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.openmetadata.converters.SolutionBlueprintConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.SolutionBlueprintMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SolutionBlueprintComponent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SolutionBlueprintElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SolutionBlueprintHandler provides methods to define solution blueprints
 */
public class SolutionBlueprintHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public SolutionBlueprintHandler(String             localServerName,
                                    AuditLog           auditLog,
                                    String             serviceName,
                                    OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.SOLUTION_BLUEPRINT.typeName);
    }


    /**
     * Create a new solution blueprint.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSolutionBlueprint(String                                userId,
                                          NewElementOptions                     newElementOptions,
                                          Map<String, ClassificationProperties> initialClassifications,
                                          SolutionBlueprintProperties           properties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "createSolutionBlueprint";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a solution blueprint using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solution blueprint.
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
    public String createSolutionBlueprintFromTemplate(String                 userId,
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
     * Update the properties of a solution blueprint.
     *
     * @param userId                 userId of user making request.
     * @param solutionBlueprintGUID      unique identifier of the solution blueprint (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSolutionBlueprint(String                      userId,
                                        String                      solutionBlueprintGUID,
                                        UpdateOptions               updateOptions,
                                        SolutionBlueprintProperties properties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "updateSolutionBlueprint";
        final String guidParameterName = "solutionBlueprintGUID";

        super.updateElement(userId,
                            solutionBlueprintGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach a solution component to a solution blueprint.
     *
     * @param userId                  userId of user making request
     * @param parentSolutionBlueprintGUID unique identifier of the parent
     * @param solutionComponentGUID     unique identifier of the solution component
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionComponentToBlueprint(String                                 userId,
                                                 String                                 parentSolutionBlueprintGUID,
                                                 String                                 solutionComponentGUID,
                                                 MetadataSourceOptions                  metadataSourceOptions,
                                                 SolutionBlueprintCompositionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "linkSolutionComponentToBlueprint";
        final String end1GUIDParameterName = "parentSolutionBlueprintGUID";
        final String end2GUIDParameterName = "solutionComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentSolutionBlueprintGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName,
                                                        parentSolutionBlueprintGUID,
                                                        solutionComponentGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a solution component from a solution blueprint.
     *
     * @param userId                 userId of user making request.
     * @param solutionBlueprintGUID    unique identifier of the solution blueprint.
     * @param solutionComponentGUID    unique identifier of the solution component.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionComponentFromBlueprint(String        userId,
                                                     String        solutionBlueprintGUID,
                                                     String        solutionComponentGUID,
                                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachSolutionComponentFromBlueprint";

        final String end1GUIDParameterName = "solutionBlueprintGUID";
        final String end2GUIDParameterName = "solutionComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionBlueprintGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName,
                                                        solutionBlueprintGUID,
                                                        solutionComponentGUID,
                                                        deleteOptions);
    }



    /**
     * Attach a solution blueprint to the element it describes.
     *
     * @param userId                  userId of user making request
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionDesign(String                   userId,
                                   String                   parentGUID,
                                   String                   solutionBlueprintGUID,
                                   MetadataSourceOptions    metadataSourceOptions,
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
                                                        metadataSourceOptions,
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
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
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
     * Delete a solution blueprint.
     *
     * @param userId                    userId of user making request.
     * @param solutionBlueprintGUID      unique identifier of the element
     * @param deleteOptions              options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSolutionBlueprint(String        userId,
                                        String        solutionBlueprintGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "deleteSolutionBlueprint";
        final String guidParameterName = "solutionBlueprintGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionBlueprintGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, solutionBlueprintGUID, deleteOptions);
    }


    /**
     * Returns the list of solution blueprints with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<SolutionBlueprintElement> getSolutionBlueprintsByName(String       userId,
                                                                      String       name,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getSolutionBlueprintsByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
                                                                                                 null,
                                                                                                 super.addDefaultType(queryOptions));

        return convertSolutionBlueprints(userId,
                                         openMetadataElements,
                                         queryOptions,
                                         methodName);
    }


    /**
     * Return the properties of a specific solution blueprint.
     *
     * @param userId                 userId of user making request
     * @param solutionBlueprintGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SolutionBlueprintElement getSolutionBlueprintByGUID(String     userId,
                                                               String     solutionBlueprintGUID,
                                                               GetOptions getOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getSolutionBlueprintByGUID";
        final String guidParameterName = "solutionBlueprintGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionBlueprintGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                              solutionBlueprintGUID,
                                                                                              getOptions);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, metadataElementTypeName)))
        {
            return convertSolutionBlueprint(userId,
                                            openMetadataElement,
                                            new QueryOptions(getOptions),
                                            methodName);
        }

        return null;
    }


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SolutionBlueprintElement> findSolutionBlueprints(String        userId,
                                                                 String        searchString,
                                                                 SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "findSolutionBlueprints";
        final String searchStringParameterName = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateSearchString(searchString, searchStringParameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           super.addDefaultType(searchOptions));

        return convertSolutionBlueprints(userId, openMetadataElements, searchOptions, methodName);
    }


    /**
     * Convert the open metadata elements retrieve into solution blueprint elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param queryOptions           multiple options to control the query
     * @param methodName calling method
     * @return list of solution blueprints (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<SolutionBlueprintElement> convertSolutionBlueprints(String                    userId,
                                                                     List<OpenMetadataElement> openMetadataElements,
                                                                     QueryOptions              queryOptions,
                                                                     String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<SolutionBlueprintElement> solutionBlueprintElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    solutionBlueprintElements.add(convertSolutionBlueprint(userId, openMetadataElement, queryOptions, methodName));
                }
            }

            return solutionBlueprintElements;
        }

        return null;
    }


    /**
     * Return the solution blueprint extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param queryOptions           multiple options to control the query
     * @param methodName calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private SolutionBlueprintElement convertSolutionBlueprint(String              userId,
                                                              OpenMetadataElement openMetadataElement,
                                                              QueryOptions        queryOptions,
                                                              String              methodName) throws PropertyServerException
    {
        try
        {
            List<SolutionBlueprintComponent> solutionBlueprintComponents = new ArrayList<>();
            OpenMetadataConverterBase<SolutionBlueprintComponent> solutionBlueprintConverter = new OpenMetadataConverterBase<>(propertyHelper, localServiceName, localServerName);

            QueryOptions workingQueryOptions = new QueryOptions(queryOptions);
            workingQueryOptions.setStartFrom(0);
            workingQueryOptions.setPageSize(openMetadataClient.getMaxPagingSize());

            RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                  openMetadataElement.getElementGUID(),
                                                                                                                  1,
                                                                                                                  OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                                  workingQueryOptions);
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        SolutionBlueprintComponent solutionBlueprintComponent = new SolutionBlueprintComponent();

                        solutionBlueprintComponent.setElementHeader(solutionBlueprintConverter.getMetadataElementHeader(SolutionBlueprintComponent.class,
                                                                                                                        relatedMetadataElement,
                                                                                                                        relatedMetadataElement.getRelationshipGUID(),
                                                                                                                        null,
                                                                                                                        methodName));
                        SolutionBlueprintCompositionProperties relationshipProperties = new SolutionBlueprintCompositionProperties();
                        ElementProperties                      elementProperties      = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                        relationshipProperties.setRole(solutionBlueprintConverter.removeRole(elementProperties));
                        relationshipProperties.setDescription(solutionBlueprintConverter.removeDescription(elementProperties));
                        relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
                        relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
                        relationshipProperties.setExtendedProperties(solutionBlueprintConverter.getRemainingExtendedProperties(elementProperties));

                        solutionBlueprintComponent.setProperties(relationshipProperties);
                        solutionBlueprintComponent.setSolutionComponent(this.convertSolutionComponent(userId,
                                                                                                      relatedMetadataElement.getElement(),
                                                                                                      false,
                                                                                                      false,
                                                                                                      queryOptions,
                                                                                                      methodName));
                        solutionBlueprintComponents.add(solutionBlueprintComponent);
                    }
                }

                workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataClient.getMaxPagingSize());
                relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                1,
                                                                                                OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                workingQueryOptions);
            }

            SolutionBlueprintConverter<SolutionBlueprintElement> converter                = new SolutionBlueprintConverter<>(propertyHelper, localServiceName, localServerName, solutionBlueprintComponents);
            SolutionBlueprintElement                             solutionBlueprintElement =  converter.getNewBean(SolutionBlueprintElement.class, openMetadataElement, methodName);

            if (solutionBlueprintElement != null)
            {
                SolutionBlueprintMermaidGraphBuilder graphBuilder = new SolutionBlueprintMermaidGraphBuilder(solutionBlueprintElement);

                solutionBlueprintElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return solutionBlueprintElement;
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
