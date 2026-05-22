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
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.DesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.NestedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Design pattern handler describes how to maintain and query design patterns.
 */
public class DesignPatternHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public DesignPatternHandler(String             localServerName,
                                AuditLog           auditLog,
                                String             serviceName,
                                OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.DESIGN_PATTERN.typeName);
    }



    /**
     * Create a new design pattern.
     *
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDesignPattern(String                                userId,
                                      NewElementOptions                     newElementOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      DesignPatternProperties               properties,
                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "createDesignPattern";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a design pattern using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new design pattern.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createDesignPatternFromTemplate(String                                userId,
                                                  TemplateOptions                       templateOptions,
                                                  String                                templateGUID,
                                                  EntityProperties                      replacementProperties,
                                                  Map<String, ClassificationProperties> replacementClassifications,
                                                  Map<String, String>                   placeholderProperties,
                                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }

    /**
     * Update the properties of a design pattern.
     *
     * @param userId                 userId of the user making the request.
     * @param designPatternGUID          unique identifier of the design pattern (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateDesignPattern(String                  userId,
                                       String                  designPatternGUID,
                                       UpdateOptions           updateOptions,
                                       DesignPatternProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateDesignPattern";
        final String guidParameterName = "designPatternGUID";

        return super.updateElement(userId,
                                   designPatternGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Connect two design patterns as parent and child.
     *
     * @param userId                 userId of the user making the request
     * @param consumingDesignPatternGUID    unique identifier of the parent design pattern
     * @param consumedDesignPatternGUID    unique identifier of the child design pattern
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedDesignPatterns(String                        userId,
                                         String                        consumingDesignPatternGUID,
                                         String                        consumedDesignPatternGUID,
                                         MakeAnchorOptions             makeAnchorOptions,
                                         NestedDesignPatternProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkNestedDesignPatterns";
        final String end1GUIDParameterName = "consumingDesignPatternGUID";
        final String end2GUIDParameterName = "consumedDesignPatternGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(consumingDesignPatternGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(consumedDesignPatternGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                                        consumingDesignPatternGUID,
                                                        consumedDesignPatternGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two design patterns from one another.
     *
     * @param userId                 userId of the user making the request.
     * @param consumingDesignPatternGUID    unique identifier of the parent design pattern.
     * @param consumedDesignPatternGUID    unique identifier of the child design pattern.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedDesignPatterns(String        userId,
                                           String        consumingDesignPatternGUID,
                                           String        consumedDesignPatternGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachNestedDesignPatterns";
        final String end1GUIDParameterName = "consumingDesignPatternGUID";
        final String end2GUIDParameterName = "consumedDesignPatternGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(consumingDesignPatternGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(consumedDesignPatternGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                                        consumingDesignPatternGUID,
                                                        consumedDesignPatternGUID,
                                                        deleteOptions);
    }


    /**
     * Connect two design patterns as parent and child.
     *
     * @param userId                 userId of the user making the request
     * @param generalizedDesignPatternGUID    unique identifier of the generalized design pattern
     * @param SpecializedDesignPatternGUID    unique identifier of the specialized design pattern
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecializedDesignPatterns(String                        userId,
                                              String                        generalizedDesignPatternGUID,
                                              String                        SpecializedDesignPatternGUID,
                                              MakeAnchorOptions             makeAnchorOptions,
                                              NestedDesignPatternProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkSpecializedDesignPatterns";
        final String end1GUIDParameterName = "generalizedDesignPatternGUID";
        final String end2GUIDParameterName = "SpecializedDesignPatternGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(generalizedDesignPatternGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(SpecializedDesignPatternGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SPECIALIZED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                                        generalizedDesignPatternGUID,
                                                        SpecializedDesignPatternGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two design patterns from one another.
     *
     * @param userId                 userId of the user making the request.
     * @param generalizedDesignPatternGUID    unique identifier of the parent design pattern.
     * @param specializedDesignPatternGUID    unique identifier of the child design pattern.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecializedDesignPatterns(String        userId,
                                                String        generalizedDesignPatternGUID,
                                                String        specializedDesignPatternGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "detachSpecializedDesignPatterns";
        final String end1GUIDParameterName = "generalizedDesignPatternGUID";
        final String end2GUIDParameterName = "specializedDesignPatternGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(generalizedDesignPatternGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(specializedDesignPatternGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SPECIALIZED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                                        generalizedDesignPatternGUID,
                                                        specializedDesignPatternGUID,
                                                        deleteOptions);
    }



    /**
     * Connect two design patterns as parent and child.
     *
     * @param userId                 userId of the user making the request
     * @param designPatternOneGUID    unique identifier of the first design pattern
     * @param designPatternTwoGUID    unique identifier of the second design pattern
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRelatedDesignPatterns(String                        userId,
                                          String                        designPatternOneGUID,
                                          String                        designPatternTwoGUID,
                                          MakeAnchorOptions             makeAnchorOptions,
                                          NestedDesignPatternProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "linkRelatedDesignPatterns";
        final String end1GUIDParameterName = "designPatternOneGUID";
        final String end2GUIDParameterName = "designPatternTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designPatternOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(designPatternTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.RELATED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                                        designPatternOneGUID,
                                                        designPatternTwoGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two design patterns from one another.
     *
     * @param userId                 userId of the user making the request.
     * @param designPatternOneGUID    unique identifier of the first design pattern.
     * @param designPatternTwoGUID    unique identifier of the second design pattern.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRelatedDesignPatterns(String        userId,
                                            String        designPatternOneGUID,
                                            String        designPatternTwoGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachRelatedDesignPatterns";
        final String end1GUIDParameterName = "designPatternOneGUID";
        final String end2GUIDParameterName = "designPatternTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designPatternOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(designPatternTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.RELATED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                                        designPatternOneGUID,
                                                        designPatternTwoGUID,
                                                        deleteOptions);
    }



    /**
     * Delete a design pattern.
     *
     * @param userId                 userId of the user making the request.
     * @param designPatternGUID          unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDesignPattern(String        userId,
                                    String        designPatternGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "deleteDesignPattern";
        final String guidParameterName = "designPatternGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designPatternGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, designPatternGUID, deleteOptions);
    }


    /**
     * Returns the list of design patterns with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDesignPatternsByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getDesignPatternsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.PROBLEM_STATEMENT.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific design pattern.
     *
     * @param userId                 userId of the user making the request
     * @param designPatternGUID          unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDesignPatternByGUID(String     userId,
                                                          String     designPatternGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getDesignPatternByGUID";
        final String guidParameterName = "designPatternGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designPatternGUID, guidParameterName, methodName);

        return super.getRootElementByGUID(userId, designPatternGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of design patterns metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findDesignPatterns(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findDesignPatterns";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
