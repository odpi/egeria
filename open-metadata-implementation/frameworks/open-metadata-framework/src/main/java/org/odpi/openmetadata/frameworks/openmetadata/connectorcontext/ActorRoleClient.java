/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides methods to define actor roles.  The interface supports the following types of objects
 * <ul>
 *     <li>ActorRole</li>
 *     <li>PersonRole</li>
 *     <li>TeamRole</li>
 *     <li>ITProfileRole</li>
 *     <li>GovernanceRole</li>
 *     <li>SolutionRole</li>
 * </ul>
 */
public class ActorRoleClient extends ConnectorContextClientBase
{
    private final ActorRoleHandler      actorRoleHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ActorRoleClient(ConnectorContextBase     parentContext,
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

        this.actorRoleHandler = new ActorRoleHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public ActorRoleClient(ActorRoleClient template,
                           String          specificTypeName)
    {
        super(template);

        this.actorRoleHandler = new ActorRoleHandler(template.actorRoleHandler, specificTypeName);
    }


    /**
     * Create a new actor role.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createActorRole(NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  ActorRoleProperties                   properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException, 
                                                                                                             PropertyServerException, 
                                                                                                             UserNotAuthorizedException
    {
        String actorRoleGUID = actorRoleHandler.createActorRole(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(actorRoleGUID);
        }

        return actorRoleGUID;
    }


    /**
     * Create a new metadata element to represent an actor role using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new actor role.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param allowRetrieve  whether the code allowed to retrieve an existing element, or must it create a new one - the match is done on the
     * qualified name (default is false).
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
    public String createActorRoleFromTemplate(TemplateOptions        templateOptions,
                                              String                 templateGUID,
                                              boolean                allowRetrieve,
                                              ElementProperties      replacementProperties,
                                              Map<String, String>    placeholderProperties,
                                              RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        String actorRoleGUID = actorRoleHandler.createActorRoleFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(actorRoleGUID);
        }

        return actorRoleGUID;
    }


    /**
     * Update the properties of an actor role.
     *
     * @param actorRoleGUID          unique identifier of the actor role (returned from create)
     * @param updateOptions          provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateActorRole(String              actorRoleGUID,
                                   UpdateOptions       updateOptions,
                                   ActorRoleProperties properties) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        boolean updateOccurred = actorRoleHandler.updateActorRole(connectorUserId, actorRoleGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(actorRoleGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach a person role to a person profile.
     *
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPersonRoleToProfile(String                          personRoleGUID,
                                        String                          personProfileGUID,
                                        MetadataSourceOptions           metadataSourceOptions,
                                        PersonRoleAppointmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        actorRoleHandler.linkPersonRoleToProfile(connectorUserId, personRoleGUID, personProfileGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a person role from a profile.
     *
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPersonRoleFromProfile(String        personRoleGUID,
                                            String        personProfileGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        actorRoleHandler.detachPersonRoleFromProfile(connectorUserId, personRoleGUID, personProfileGUID, deleteOptions);
    }


    /**
     * Attach a team role to a team profile.
     *
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamRoleToProfile(String                        teamRoleGUID,
                                      String                        teamProfileGUID,
                                      MetadataSourceOptions         metadataSourceOptions,
                                      TeamRoleAppointmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        actorRoleHandler.linkTeamRoleToProfile(connectorUserId, teamRoleGUID, teamProfileGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a team role from a team profile.
     *
     * @param teamRoleGUID              unique identifier of the team
     * @param teamProfileGUID          unique identifier of the team profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamRoleFromProfile(String        teamRoleGUID,
                                          String        teamProfileGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        actorRoleHandler.detachTeamRoleFromProfile(connectorUserId, teamRoleGUID, teamProfileGUID, deleteOptions);
    }


    /**
     * Attach an IT profile role to an IT profile.
     *
     * @param itProfileRoleGUID       unique identifier of the IT profile role
     * @param itProfileGUID            unique identifier of the IT profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkITProfileRoleToProfile(String                             itProfileRoleGUID,
                                           String                             itProfileGUID,
                                           MetadataSourceOptions              metadataSourceOptions,
                                           ITProfileRoleAppointmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        actorRoleHandler.linkITProfileRoleToProfile(connectorUserId, itProfileRoleGUID, itProfileGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an IT profile role from an IT profile.
     *
     * @param itProfileRoleGUID      unique identifier of the IT profile role
     * @param itProfileGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachITProfileRoleFromProfile(String        itProfileRoleGUID,
                                               String        itProfileGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        actorRoleHandler.detachITProfileRoleFromProfile(connectorUserId, itProfileRoleGUID, itProfileGUID, deleteOptions);
    }


    /**
     * Delete a actor role.
     *
     * @param actorRoleGUID      unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteActorRole(String        actorRoleGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        actorRoleHandler.deleteActorRole(connectorUserId, actorRoleGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(actorRoleGUID);
        }
    }


    /**
     * Returns the list of actor roles with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getActorRolesByName(String       name,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return actorRoleHandler.getActorRolesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific actor role.
     *
     * @param actorRoleGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getActorRoleByGUID(String     actorRoleGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        return actorRoleHandler.getActorRoleByGUID(connectorUserId, actorRoleGUID, getOptions);
    }


    /**
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned actor roles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString         string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findActorRoles(String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return actorRoleHandler.findActorRoles(connectorUserId, searchString, searchOptions);
    }
}
