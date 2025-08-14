/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.ActorProfileMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ActorProfileHandler provides methods to define actor profiles
 * The interface supports the following types of objects
 * <ul>
 *     <li>ActorProfile</li>
 *     <li>Person</li>
 *     <li>Team</li>
 *     <li>ITProfile</li>
 * </ul>
 */
public class ActorProfileHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public ActorProfileHandler(String             localServerName,
                               AuditLog           auditLog,
                               String             localServiceName,
                               OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.ACTOR_PROFILE.typeName);
    }


    /**
     * Create a new actor profile.
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
    public String createActorProfile(String                                userId,
                                     NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     ActorProfileProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName = "createActorProfile";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an actor profile using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new actor profile.
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
    public String createActorProfileFromTemplate(String                 userId,
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
     * Update the properties of an actor profile.
     *
     * @param userId                 userId of user making request.
     * @param actorProfileGUID       unique identifier of the actor profile (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateActorProfile(String                 userId,
                                   String                 actorProfileGUID,
                                   UpdateOptions          updateOptions,
                                   ActorProfileProperties properties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName        = "updateActorProfile";
        final String guidParameterName = "actorProfileGUID";

        super.updateElement(userId,
                            actorProfileGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach a profile to a location.
     *
     * @param userId                 userId of user making request
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param locationGUID           unique identifier of the location
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkLocationToProfile(String                    userId,
                                      String                    actorProfileGUID,
                                      String                    locationGUID,
                                      MetadataSourceOptions     metadataSourceOptions,
                                      ProfileLocationProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkLocationToProfile";
        final String end1GUIDParameterName = "actorProfileGUID";
        final String end2GUIDParameterName = "locationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actorProfileGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                        actorProfileGUID,
                                                        locationGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an actor profile from a location.
     *
     * @param userId                 userId of user making request.
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param locationGUID           unique identifier of the location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachLocationFromProfile(String        userId,
                                          String        actorProfileGUID,
                                          String        locationGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "detachLocationFromProfile";

        final String end1GUIDParameterName = "actorProfileGUID";
        final String end2GUIDParameterName = "locationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actorProfileGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                        locationGUID,
                                                        actorProfileGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param userId                 userId of user making request
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerPerson(String                userId,
                               String                personOneGUID,
                               String                personTwoGUID,
                               MetadataSourceOptions metadataSourceOptions,
                               PeerProperties        relationshipProperties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "linkPeerPerson";
        final String end1GUIDParameterName = "personOneGUID";
        final String end2GUIDParameterName = "personTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(personOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(personTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PEER_RELATIONSHIP.typeName,
                                                        personOneGUID,
                                                        personTwoGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a person profile from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerPerson(String        userId,
                                 String        personOneGUID,
                                 String        personTwoGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachPeerPerson";
        final String end1GUIDParameterName = "personOneGUID";
        final String end2GUIDParameterName = "personTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(personOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(personTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PEER_RELATIONSHIP.typeName,
                                                        personOneGUID,
                                                        personTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a super team to a subteam.
     *
     * @param userId                 userId of user making request
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamStructure(String                  userId,
                                  String                  superTeamGUID,
                                  String                  subteamGUID,
                                  MetadataSourceOptions   metadataSourceOptions,
                                  TeamStructureProperties relationshipProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkTeamStructure";
        final String end1GUIDParameterName = "superTeamGUID";
        final String end2GUIDParameterName = "subteamGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(superTeamGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(subteamGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                        superTeamGUID,
                                                        subteamGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param userId                 userId of user making request.
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamStructure(String        userId,
                                    String        superTeamGUID,
                                    String        subteamGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachTeamStructure";
        final String end1GUIDParameterName = "superTeamGUID";
        final String end2GUIDParameterName = "subteamGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(superTeamGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(subteamGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                        superTeamGUID,
                                                        subteamGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an asset to an IT profile.
     *
     * @param userId                 userId of user making request
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssetToProfile(String                            userId,
                                   String                            assetGUID,
                                   String                            itProfileGUID,
                                   MetadataSourceOptions             metadataSourceOptions,
                                   ITInfrastructureProfileProperties relationshipProperties) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "linkAssetToProfile";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        itProfileGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an asset from an IT profile.
     *
     * @param userId                 userId of user making request.
     * @param assetGUID              unique identifier of the asset
     * @param itProfileGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetFromProfile(String        userId,
                                       String        assetGUID,
                                       String        itProfileGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachAssetFromProfile";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "itProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(itProfileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName,
                                                        itProfileGUID,
                                                        itProfileGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a actor profile.
     *
     * @param userId                 userId of user making request.
     * @param actorProfileGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteActorProfile(String        userId,
                                   String        actorProfileGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName        = "deleteActorProfile";
        final String guidParameterName = "actorProfileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actorProfileGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, actorProfileGUID, deleteOptions);
    }


    /**
     * Returns the list of actor profiles with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getActorProfilesByName(String       userId,
                                                                String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getActorProfilesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific actor profile.
     *
     * @param userId                 userId of user making request
     * @param actorProfileGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getActorProfileByGUID(String     userId,
                                                         String     actorProfileGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getActorProfileByGUID";

        return super.getRootElementByGUID(userId,
                                          actorProfileGUID,
                                          getOptions,
                                          methodName);
    }


    /**
     * Return the properties of a specific actor profile retrieved using an associated userId.
     *
     * @param userId                 userId of user making request
     * @param requiredUserId         identifier of user
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getActorProfileByUserId(String     userId,
                                                           String     requiredUserId,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName        = "getActorProfileByUserId";
        final String nameParameterName = "requiredUserId";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(requiredUserId, nameParameterName, methodName);

        OpenMetadataElement userIdentityElement = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                                    requiredUserId,
                                                                                                    OpenMetadataProperty.USER_ID.name,
                                                                                                    getOptions);

        if (userIdentityElement != null)
        {
            RelatedMetadataElement profileElement = openMetadataClient.getRelatedMetadataElement(userId,
                                                                                                 userIdentityElement.getElementGUID(),
                                                                                                 2,
                                                                                                 OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                                                 getOptions);

            if (profileElement != null)
            {
                return convertRootElement(userId,
                                          profileElement.getElement(),
                                          new QueryOptions(getOptions),
                                          methodName);
            }
        }

        return null;
    }


    /**
     * Retrieve the list of actor profiles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned actor profiles include a list of the components that are associated with it.
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
    public List<OpenMetadataRootElement> findActorProfiles(String        userId,
                                                           String        searchString,
                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName  = "findActorProfiles";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * If the actor role has linked solution components, they are extracted and the OpenMetadataRootElement is
     * converted into an ActorRoleElement before the mermaid graph is added.  Otherwise, the standard mermaid
     * graph is added.
     *
     * @param rootElement element extracted from the repository
     * @return bean or null
     */
    protected OpenMetadataRootElement addMermaidToRootElement(OpenMetadataRootElement rootElement)
    {
        if (rootElement != null)
        {
            ActorProfileElement actorProfileElement = new ActorProfileElement(rootElement);

            if (actorProfileElement.getOtherRelatedElements() != null)
            {
                List<RelatedMetadataElementSummary> actorRoles           = new ArrayList<>();
                List<RelatedMetadataElementSummary> peers                = new ArrayList<>();
                List<RelatedMetadataElementSummary> locations            = new ArrayList<>();
                List<RelatedMetadataElementSummary> userIdentities       = new ArrayList<>();
                List<RelatedMetadataElementSummary> subTeams             = new ArrayList<>();
                List<RelatedMetadataElementSummary> businessCapabilities = new ArrayList<>();
                List<RelatedMetadataElementSummary> linkedInfrastructure = new ArrayList<>();
                List<RelatedMetadataElementSummary> other                = new ArrayList<>();

                for (RelatedMetadataElementSummary relatedMetadataElementSummary : actorProfileElement.getOtherRelatedElements())
                {
                    if (relatedMetadataElementSummary != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName))
                        {
                            actorProfileElement.setContributionRecord(relatedMetadataElementSummary);
                        }
                        else if ((propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName)) ||
                                (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName)) ||
                                (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName)))
                        {
                            actorRoles.add(relatedMetadataElementSummary);
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.PEER_RELATIONSHIP.typeName))
                        {
                            peers.add(relatedMetadataElementSummary);
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName))
                        {
                            locations.add(relatedMetadataElementSummary);
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName))
                        {
                            userIdentities.add(relatedMetadataElementSummary);
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName))
                        {
                            if (relatedMetadataElementSummary.getRelatedElementAtEnd1())
                            {
                                actorProfileElement.setSuperTeam(relatedMetadataElementSummary);
                            }
                            else
                            {
                                subTeams.add(relatedMetadataElementSummary);
                            }
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.BUSINESS_CAPABILITY_TEAM_RELATIONSHIP.typeName))
                        {
                            businessCapabilities.add(relatedMetadataElementSummary);
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElementSummary.getRelationshipHeader(), OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName))
                        {
                            linkedInfrastructure.add(relatedMetadataElementSummary);
                        }
                        else
                        {
                            other.add(relatedMetadataElementSummary);
                        }
                    }
                }

                if (! actorRoles.isEmpty())
                {
                    actorProfileElement.setActorRoles(actorRoles);
                }

                if (! peers.isEmpty())
                {
                    actorProfileElement.setPeerGovernanceDefinitions(peers);
                }

                if (! locations.isEmpty())
                {
                    actorProfileElement.setLocations(locations);
                }

                if (! userIdentities.isEmpty())
                {
                    actorProfileElement.setUserIdentities(userIdentities);
                }

                if (! subTeams.isEmpty())
                {
                    actorProfileElement.setSubTeams(subTeams);
                }

                if (! businessCapabilities.isEmpty())
                {
                    actorProfileElement.setBusinessCapabilities(businessCapabilities);
                }

                if (! linkedInfrastructure.isEmpty())
                {
                    actorProfileElement.setLinkedInfrastructure(linkedInfrastructure);
                }

                if (! other.isEmpty())
                {
                    actorProfileElement.setOtherRelatedElements(other);
                }
                else
                {
                    actorProfileElement.setOtherRelatedElements(null);
                }
            }

            ActorProfileMermaidGraphBuilder graphBuilder = new ActorProfileMermaidGraphBuilder(actorProfileElement);

            actorProfileElement.setMermaidGraph(graphBuilder.getMermaidGraph());

            return actorProfileElement;
        }

        return null;
    }
}
