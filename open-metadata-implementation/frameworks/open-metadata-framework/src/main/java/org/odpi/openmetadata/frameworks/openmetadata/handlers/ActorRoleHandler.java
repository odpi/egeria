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
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ActorRoleHandler provides methods to define actor roles.
 * The interface supports the following types of objects
 * <ul>
 *     <li>ActorRole</li>
 *     <li>PersonRole</li>
 *     <li>TeamRole</li>
 *     <li>ITProfileRole</li>
 *     <li>GovernanceRole</li>
 *     <li>SolutionRole</li>
 * </ul>
 */
public class ActorRoleHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ActorRoleHandler(String             localServerName,
                            AuditLog           auditLog,
                            String             serviceName,
                            OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              serviceName,
              openMetadataClient,
              OpenMetadataType.ACTOR_ROLE.typeName);
    }


    /**
     * Create a new actor role.
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
    public String createActorRole(String                                userId,
                                  NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  ActorRoleProperties                   properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        final String methodName = "createActorRole";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an actor role using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new actor role.
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
    public String createActorRoleFromTemplate(String                 userId,
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
     * Update the properties of an actor role.
     *
     * @param userId                 userId of user making request.
     * @param actorRoleGUID          unique identifier of the actor role (returned from create)
     * @param updateOptions          provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateActorRole(String              userId,
                                String              actorRoleGUID,
                                UpdateOptions       updateOptions,
                                ActorRoleProperties properties) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "updateActorRole";
        final String guidParameterName = "actorRoleGUID";

        super.updateElement(userId,
                            actorRoleGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }



    /**
     * Attach a person role to a person profile.
     *
     * @param userId                 userId of user making request
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPersonRoleToProfile(String                          userId,
                                        String                          personRoleGUID,
                                        String                          personProfileGUID,
                                        MetadataSourceOptions           metadataSourceOptions,
                                        PersonRoleAppointmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName            = "linkPersonRoleToProfile";
        final String end1GUIDParameterName = "personRoleGUID";
        final String end2GUIDParameterName = "personProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(personRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(personProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                        personRoleGUID,
                                                        personProfileGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a person role from a profile.
     *
     * @param userId                 userId of user making request.
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPersonRoleFromProfile(String        userId,
                                            String        personRoleGUID,
                                            String        personProfileGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachPersonRoleFromProfile";
        final String end1GUIDParameterName = "personRoleGUID";
        final String end2GUIDParameterName = "personProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(personRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(personProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                        personProfileGUID,
                                                        personProfileGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a team role to a team profile.
     *
     * @param userId                 userId of user making request
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamRoleToProfile(String                        userId,
                                      String                        teamRoleGUID,
                                      String                        teamProfileGUID,
                                      MetadataSourceOptions         metadataSourceOptions,
                                      TeamRoleAppointmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName            = "linkTeamRoleToProfile";
        final String end1GUIDParameterName = "teamRoleGUID";
        final String end2GUIDParameterName = "teamProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(teamRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(teamProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                        teamRoleGUID,
                                                        teamProfileGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a team role from a team profile.
     *
     * @param userId                 userId of user making request.
     * @param teamRoleGUID              unique identifier of the team
     * @param teamProfileGUID          unique identifier of the team profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamRoleFromProfile(String        userId,
                                          String        teamRoleGUID,
                                          String        teamProfileGUID,
                                          DeleteOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "detachTeamRoleFromProfile";
        final String end1GUIDParameterName = "teamRoleGUID";
        final String end2GUIDParameterName = "teamProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(teamRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(teamProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                        teamProfileGUID,
                                                        teamProfileGUID,
                                                        metadataSourceOptions);
    }


    /**
     * Attach an IT profile role to an IT profile.
     *
     * @param userId                 userId of user making request
     * @param itProfileRoleGUID       unique identifier of the IT profile role
     * @param itProfileGUID            unique identifier of the IT profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkITProfileRoleToProfile(String                             userId,
                                           String                             itProfileRoleGUID,
                                           String                             itProfileGUID,
                                           MetadataSourceOptions              metadataSourceOptions,
                                           ITProfileRoleAppointmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkITProfileRoleToProfile";
        final String end1GUIDParameterName = "itProfileRoleGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(itProfileRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                        itProfileRoleGUID,
                                                        itProfileGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an IT profile role from an IT profile.
     *
     * @param userId                 userId of user making request.
     * @param itProfileRoleGUID      unique identifier of the IT profile role
     * @param itProfileGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachITProfileRoleFromProfile(String        userId,
                                               String        itProfileRoleGUID,
                                               String        itProfileGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachITProfileRoleFromProfile";
        final String end1GUIDParameterName = "itProfileRoleGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(itProfileRoleGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                        itProfileGUID,
                                                        itProfileGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a actor role.
     *
     * @param userId                 userId of user making request.
     * @param actorRoleGUID      unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteActorRole(String        userId,
                                String        actorRoleGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "deleteActorRole";
        final String guidParameterName = "actorRoleGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actorRoleGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, actorRoleGUID, deleteOptions);
    }


    /**
     * Returns the list of actor roles with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getActorRolesByName(String       userId,
                                                             String       name,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getActorRolesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific actor role.
     *
     * @param userId                 userId of user making request
     * @param actorRoleGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getActorRoleByGUID(String     userId,
                                                      String     actorRoleGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getActorRoleByGUID";

        return super.getRootElementByGUID(userId, actorRoleGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of actor roles metadata elements that contain the search string.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findActorRoles(String        userId,
                                                        String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "findActorRoles";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
