/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
    public void linkSolutionComponentToBlueprint(String                         userId,
                                                 String                         parentSolutionBlueprintGUID,
                                                 String                         solutionComponentGUID,
                                                 MetadataSourceOptions          metadataSourceOptions,
                                                 CollectionMembershipProperties relationshipProperties) throws InvalidParameterException,
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
                                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
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
                                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
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
    public List<OpenMetadataRootElement> getSolutionBlueprintsByName(String       userId,
                                                                     String       name,
                                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getSolutionBlueprintsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
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
    public OpenMetadataRootElement getSolutionBlueprintByGUID(String     userId,
                                                              String     solutionBlueprintGUID,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getSolutionBlueprintByGUID";
        final String guidParameterName = "solutionBlueprintGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionBlueprintGUID, guidParameterName, methodName);

        return super.getRootElementByGUID(userId, solutionBlueprintGUID, getOptions, methodName);
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
    public List<OpenMetadataRootElement> findSolutionBlueprints(String        userId,
                                                                String        searchString,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "findSolutionBlueprints";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
