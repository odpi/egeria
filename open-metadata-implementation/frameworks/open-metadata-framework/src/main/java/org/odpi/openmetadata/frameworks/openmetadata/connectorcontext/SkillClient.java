/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SkillHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.SkillProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with Skill objects.
 */
public class SkillClient extends ConnectorContextClientBase
{
    private final SkillHandler   skillHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public SkillClient(ConnectorContextBase     parentContext,
                       String                   localServerName,
                       String                   localServiceName,
                       String                   connectorUserId,
                       String                   connectorGUID,
                       String                   externalSourceGUID,
                       String                   externalSourceName,
                       OpenMetadataClient       openMetadataClient,
                       AuditLog                 auditLog,
                       int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.skillHandler = new SkillHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public SkillClient(SkillClient template,
                       String             specificTypeName)
    {
        super(template);

        this.skillHandler = new SkillHandler(template.skillHandler, specificTypeName);
    }


    /**
     * Create a new skill.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSkill(NewElementOptions                     newElementOptions,
                              Map<String, ClassificationProperties> initialClassifications,
                              SkillProperties                properties,
                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        String skillGUID = skillHandler.createSkill(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(skillGUID);
        }

        return skillGUID;
    }


    /**
     * Create a new metadata element to represent a skill using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new skill.
     *
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
    public String createSkillFromTemplate(TemplateOptions                       templateOptions,
                                          String                                templateGUID,
                                          EntityProperties                      replacementProperties,
                                          Map<String, ClassificationProperties> replacementClassifications,
                                          Map<String, String>                   placeholderProperties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        String skillGUID = skillHandler.createSkillFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(skillGUID);
        }

        return skillGUID;
    }


    /**
     * Update the properties of a skill.
     *
     * @param skillGUID       unique identifier of the skill (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSkill(String                 skillGUID,
                               UpdateOptions          updateOptions,
                               SkillProperties properties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        boolean updateOccurred = skillHandler.updateSkill(connectorUserId, skillGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(skillGUID);
        }

        return updateOccurred;
    }


    /**
     * Delete a skill.
     *
     * @param skillGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSkill(String        skillGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        skillHandler.deleteSkill(connectorUserId, skillGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(skillGUID);
        }
    }


    /**
     * Returns the list of skills with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSkillsByName(String       name,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return skillHandler.getSkillsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific skill.
     *
     * @param skillGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSkillByGUID(String     skillGUID,
                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return skillHandler.getSkillByGUID(connectorUserId, skillGUID, getOptions);
    }


    /**
     * Retrieve the list of skills metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned skills include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query

     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSkills(String        searchString,
                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return skillHandler.findSkills(connectorUserId, searchString, searchOptions);
    }
}
