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
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITInfrastructureProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PeerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public ActorProfileHandler(ActorProfileHandler template,
                               String       specificTypeName)
    {
        super(template, specificTypeName);
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
                                                 EntityProperties       replacementProperties,
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
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateActorProfile(String                 userId,
                                      String                 actorProfileGUID,
                                      UpdateOptions          updateOptions,
                                      ActorProfileProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName        = "updateActorProfile";
        final String guidParameterName = "actorProfileGUID";

        return super.updateElement(userId,
                                   actorProfileGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param userId                 userId of user making request
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerPerson(String            userId,
                               String            personOneGUID,
                               String            personTwoGUID,
                               MakeAnchorOptions makeAnchorOptions,
                               PeerProperties    relationshipProperties) throws InvalidParameterException,
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
                                                        makeAnchorOptions,
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamStructure(String                  userId,
                                  String                  superTeamGUID,
                                  String                  subteamGUID,
                                  MakeAnchorOptions       makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssetToProfile(String                            userId,
                                   String                            assetGUID,
                                   String                            itProfileGUID,
                                   MakeAnchorOptions                 makeAnchorOptions,
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
                                                        makeAnchorOptions,
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
                                                   OpenMetadataProperty.IDENTIFIER.name,
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

        GetOptions userIdGetOptions = new GetOptions(getOptions);

        userIdGetOptions.setMetadataElementTypeName(OpenMetadataType.USER_IDENTITY.typeName);

        OpenMetadataElement userIdentityElement = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                                    requiredUserId,
                                                                                                    OpenMetadataProperty.USER_ID.name,
                                                                                                    userIdGetOptions);

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
     * Retrieve the list of actor profiles metadata elements that contain the search string.
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
}
