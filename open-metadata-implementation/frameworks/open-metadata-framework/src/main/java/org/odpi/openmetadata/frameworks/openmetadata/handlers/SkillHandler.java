/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.SkillProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SkillHandler provides methods to manage skill descriptions.
 */
public class SkillHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public SkillHandler(String             localServerName,
                        AuditLog           auditLog,
                        String             localServiceName,
                        OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.SKILL.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public SkillHandler(SkillHandler template,
                        String       specificTypeName)
    {
        super(template, specificTypeName);
    }


    /**
     * Create a new skill.
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
    public String createSkill(String                                userId,
                              NewElementOptions                     newElementOptions,
                              Map<String, ClassificationProperties> initialClassifications,
                              SkillProperties                properties,
                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "createSkill";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a skill using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new skill.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createSkillFromTemplate(String                                userId,
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
     * Update the properties of a skill.
     *
     * @param userId                 userId of the user making the request.
     * @param skillGUID       unique identifier of the skill (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSkill(String                 userId,
                               String                 skillGUID,
                               UpdateOptions          updateOptions,
                               SkillProperties properties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName        = "updateSkill";
        final String guidParameterName = "skillGUID";

        return super.updateElement(userId,
                                   skillGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Delete a skill.
     *
     * @param userId                 userId of the user making the request.
     * @param skillGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSkill(String        userId,
                            String        skillGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String methodName        = "deleteSkill";
        final String guidParameterName = "skillGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(skillGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, skillGUID, deleteOptions);
    }


    /**
     * Returns the list of skills with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSkillsByName(String       userId,
                                                         String       name,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "getSkillsByName";

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
     * Return the properties of a specific skill.
     *
     * @param userId                 userId of the user making the request
     * @param skillGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSkillByGUID(String     userId,
                                                  String     skillGUID,
                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "getSkillByGUID";

        return super.getRootElementByGUID(userId,
                                          skillGUID,
                                          getOptions,
                                          methodName);
    }


    /**
     * Retrieve the list of skill metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSkills(String        userId,
                                                    String        searchString,
                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName  = "findSkills";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
