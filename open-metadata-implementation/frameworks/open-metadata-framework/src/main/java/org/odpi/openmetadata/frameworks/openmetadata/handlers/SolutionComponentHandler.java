/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SolutionComponentHandler provides methods to define solution components and their supporting objects.
 */
public class SolutionComponentHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public SolutionComponentHandler(String             localServerName,
                                    AuditLog           auditLog,
                                    String             serviceName,
                                    OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.SOLUTION_COMPONENT.typeName);
    }


    /**
     * Create a new solution component.
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
    public String createSolutionComponent(String                                userId,
                                          NewElementOptions                     newElementOptions,
                                          Map<String, ClassificationProperties> initialClassifications,
                                          SolutionComponentProperties           properties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "createSolutionComponent";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a solution component using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solution component.
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
    public String createSolutionComponentFromTemplate(String                 userId,
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
     * Update the properties of a solution component.
     *
     * @param userId                 userId of user making request.
     * @param solutionComponentGUID      unique identifier of the solution component (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSolutionComponent(String                      userId,
                                        String                      solutionComponentGUID,
                                        UpdateOptions               updateOptions,
                                        SolutionComponentProperties properties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "updateSolutionComponent";
        final String guidParameterName = "solutionComponentGUID";

        super.updateElement(userId,
                            solutionComponentGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach a solution component to a solution component.
     *
     * @param userId                  userId of user making request
     * @param parentSolutionComponentGUID unique identifier of the parent solution component
     * @param childSolutionComponentGUID     unique identifier of the child solution component
     * @param relationshipProperties  description of the relationship.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubcomponent(String                        userId,
                                 String                        parentSolutionComponentGUID,
                                 String                        childSolutionComponentGUID,
                                 MetadataSourceOptions         metadataSourceOptions,
                                 SolutionCompositionProperties relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "linkSubcomponent";
        final String end1GUIDParameterName = "parentSolutionComponentGUID";
        final String end2GUIDParameterName = "childSolutionComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentSolutionComponentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childSolutionComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName,
                                                        parentSolutionComponentGUID,
                                                        childSolutionComponentGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a solution component from a solution component.
     *
     * @param userId                 userId of user making request.
     * @param parentSolutionComponentGUID    unique identifier of the parent solution component.
     * @param subcomponentGUID    unique identifier of the nested solution component.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubcomponent(String        userId,
                                   String        parentSolutionComponentGUID,
                                   String        subcomponentGUID,
                                   DeleteOptions metadataSourceOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachSubcomponent";

        final String end1GUIDParameterName = "parentSolutionComponentGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentSolutionComponentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(subcomponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName,
                                                        parentSolutionComponentGUID,
                                                        subcomponentGUID,
                                                        metadataSourceOptions);
    }


    /**
     * Attach an element communicating with a solution component.
     *
     * @param userId                  userId of user making request
     * @param solutionComponentOneGUID unique identifier of the solution component at end 1
     * @param solutionComponentTwoGUID unique identifier of the solution component at end 2
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionLinkingWire(String                        userId,
                                        String                        solutionComponentOneGUID,
                                        String                        solutionComponentTwoGUID,
                                        MetadataSourceOptions         metadataSourceOptions,
                                        SolutionLinkingWireProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkSolutionLinkingWire";
        final String end1GUIDParameterName = "solutionComponentOneGUID";
        final String end2GUIDParameterName = "solutionComponentTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionComponentOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionComponentTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                        solutionComponentOneGUID,
                                                        solutionComponentTwoGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an element communicating with a solution component.
     *
     * @param userId                 userId of user making request.
     * @param solutionComponentOneGUID unique identifier of the solution component at end 1
     * @param solutionComponentTwoGUID unique identifier of the solution component at end 2
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionLinkingWire(String        userId,
                                          String        solutionComponentOneGUID,
                                          String        solutionComponentTwoGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "detachSolutionLinkingWire";

        final String end1GUIDParameterName = "solutionComponentOneGUID";
        final String end2GUIDParameterName = "solutionComponentTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionComponentOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionComponentTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                        solutionComponentOneGUID,
                                                        solutionComponentTwoGUID,
                                                        deleteOptions);
    }



    /**
     * Attach a solution component to an actor role.
     *
     * @param userId                  userId of user making request
     * @param solutionRoleGUID unique identifier of the parent
     * @param solutionComponentGUID     unique identifier of the solution component
     * @param relationshipProperties  description of the relationship.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionComponentActor(String                           userId,
                                           String                           solutionRoleGUID,
                                           String                           solutionComponentGUID,
                                           MetadataSourceOptions             metadataSourceOptions,
                                           SolutionComponentActorProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkSolutionComponentActor";
        final String end1GUIDParameterName = "parentSolutionRoleGUID";
        final String end2GUIDParameterName = "solutionComponentActorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                        solutionRoleGUID,
                                                        solutionComponentGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a solution component from an actor role.
     *
     * @param userId                 userId of user making request.
     * @param solutionRoleGUID    unique identifier of the parent solution component.
     * @param solutionComponentGUID    unique identifier of the nested solution component.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionComponentActor(String        userId,
                                             String        solutionRoleGUID,
                                             String        solutionComponentGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachSolutionComponentActor";

        final String end1GUIDParameterName = "parentSolutionRoleGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                        solutionRoleGUID,
                                                        solutionComponentGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a solution component.
     *
     * @param userId                 userId of user making request.
     * @param solutionComponentGUID      unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSolutionComponent(String        userId,
                                        String        solutionComponentGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "deleteSolutionComponent";
        final String guidParameterName = "solutionComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, solutionComponentGUID, deleteOptions);
    }


    /**
     * Returns the list of solution components with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSolutionComponentsByName(String       userId,
                                                                     String       name,
                                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getSolutionComponentsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific solution component.
     *
     * @param userId                 userId of user making request
     * @param solutionComponentGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSolutionComponentByGUID(String     userId,
                                                              String     solutionComponentGUID,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getSolutionComponentByGUID";
        final String guidParameterName = "solutionComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, guidParameterName, methodName);

        return super.getRootElementByGUID(userId, solutionComponentGUID, getOptions, methodName);
    }



    /**
     * Retrieve the list of solution component metadata elements that contain the search string.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param searchOptions           multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSolutionComponents(String        userId,
                                                                String        searchString,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "findSolutionComponents";

        return super.findRootElements(userId, searchString,searchOptions, methodName);
    }


    /**
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param userId                calling user
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param queryOptions           multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelatedMetadataElementSummary> getSolutionComponentImplementations(String              userId,
                                                                                   String              solutionComponentGUID,
                                                                                   QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        final String methodName = "getSolutionComponentImplementations";
        final String parameterName = "solutionComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, parameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           solutionComponentGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                                                           queryOptions);

        return convertSolutionComponentImplementations(relatedMetadataElements, methodName);
    }


    /**
     * Convert the open metadata elements retrieve into summary elements.
     *
     * @param relatedMetadataElementList elements retrieved from the repository
     * @param methodName calling method
     * @return list of summary elements (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<RelatedMetadataElementSummary> convertSolutionComponentImplementations(RelatedMetadataElementList relatedMetadataElementList,
                                                                                        String                     methodName) throws PropertyServerException
    {
        if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
        {
            List<RelatedMetadataElementSummary> solutionComponentImplementationElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    solutionComponentImplementationElements.add(convertSolutionComponentImplementation(relatedMetadataElement, methodName));
                }
            }

            return solutionComponentImplementationElements;
        }

        return null;
    }


    /**
     * Return the solution component implementation extracted from the open metadata element.
     *
     * @param relatedMetadataElement element extracted from the repository
     * @param methodName calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private RelatedMetadataElementSummary convertSolutionComponentImplementation(RelatedMetadataElement relatedMetadataElement,
                                                                                 String                 methodName) throws PropertyServerException
    {
        try
        {
            return propertyHelper.getRelatedElementSummary(relatedMetadataElement);
        }
        catch (PropertyServerException error)
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
