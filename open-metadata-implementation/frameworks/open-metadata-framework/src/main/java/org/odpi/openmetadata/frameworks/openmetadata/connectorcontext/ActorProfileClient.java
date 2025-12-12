/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with ActorProfile objects: Person, Team and ITProfile
 * along with contact details associated with this profile.
 */
public class ActorProfileClient extends ConnectorContextClientBase
{
    private final ActorProfileHandler   actorProfileHandler;


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
    public ActorProfileClient(ConnectorContextBase     parentContext,
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

        this.actorProfileHandler = new ActorProfileHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public ActorProfileClient(ActorProfileClient template,
                              String             specificTypeName)
    {
        super(template);

        this.actorProfileHandler = new ActorProfileHandler(template.actorProfileHandler, specificTypeName);
    }


    /**
     * Create a new actor profile.
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
    public String createActorProfile(NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     ActorProfileProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        String actorProfileGUID = actorProfileHandler.createActorProfile(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(actorProfileGUID);
        }

        return actorProfileGUID;
    }


    /**
     * Create a new metadata element to represent an actor profile using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new actor profile.
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
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createActorProfileFromTemplate(TemplateOptions        templateOptions,
                                                 String                 templateGUID,
                                                 boolean                allowRetrieve,
                                                 ElementProperties      replacementProperties,
                                                 Map<String, String>    placeholderProperties,
                                                 RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        String actorProfileGUID = actorProfileHandler.createActorProfileFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(actorProfileGUID);
        }

        return actorProfileGUID;
    }


    /**
     * Update the properties of an actor profile.
     *
     * @param actorProfileGUID       unique identifier of the actor profile (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateActorProfile(String                 actorProfileGUID,
                                      UpdateOptions          updateOptions,
                                      ActorProfileProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        boolean updateOccurred = actorProfileHandler.updateActorProfile(connectorUserId, actorProfileGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(actorProfileGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerPerson(String            personOneGUID,
                               String            personTwoGUID,
                               MakeAnchorOptions makeAnchorOptions,
                               PeerProperties    relationshipProperties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        actorProfileHandler.linkPeerPerson(connectorUserId, personOneGUID, personTwoGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a person profile from one of its peers.
     *
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerPerson(String        personOneGUID,
                                 String        personTwoGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        actorProfileHandler.detachPeerPerson(connectorUserId, personOneGUID, personTwoGUID, deleteOptions);
    }


    /**
     * Attach a super team to a subteam.
     *
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamStructure(String                  superTeamGUID,
                                  String                  subteamGUID,
                                  MakeAnchorOptions       makeAnchorOptions,
                                  TeamStructureProperties relationshipProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        actorProfileHandler.linkTeamStructure(connectorUserId, superTeamGUID, subteamGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamStructure(String        superTeamGUID,
                                    String        subteamGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        actorProfileHandler.detachTeamStructure(connectorUserId, superTeamGUID, subteamGUID, deleteOptions);
    }


    /**
     * Attach an asset to an IT profile.
     *
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssetToProfile(String                            assetGUID,
                                   String                            itProfileGUID,
                                   MakeAnchorOptions                 makeAnchorOptions,
                                   ITInfrastructureProfileProperties relationshipProperties) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        actorProfileHandler.linkAssetToProfile(connectorUserId, assetGUID, itProfileGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an asset from an IT profile.
     *
     * @param assetGUID              unique identifier of the asset
     * @param itProfileGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetFromProfile(String        assetGUID,
                                       String        itProfileGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        actorProfileHandler.detachAssetFromProfile(connectorUserId, assetGUID, itProfileGUID, deleteOptions);
    }


    /**
     * Delete a actor profile.
     *
     * @param actorProfileGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteActorProfile(String        actorProfileGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        actorProfileHandler.deleteActorProfile(connectorUserId, actorProfileGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(actorProfileGUID);
        }
    }


    /**
     * Returns the list of actor profiles with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getActorProfilesByName(String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return actorProfileHandler.getActorProfilesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific actor profile.
     *
     * @param actorProfileGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getActorProfileByGUID(String     actorProfileGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return actorProfileHandler.getActorProfileByGUID(connectorUserId, actorProfileGUID, getOptions);
    }


    /**
     * Return the properties of a specific actor profile retrieved using an associated userId.
     *
     * @param requiredUserId         identifier of user
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getActorProfileByUserId(String     requiredUserId,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        return actorProfileHandler.getActorProfileByUserId(connectorUserId, requiredUserId, getOptions);
    }


    /**
     * Retrieve the list of actor profiles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned actor profiles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query

     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findActorProfiles(String        searchString,
                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return actorProfileHandler.findActorProfiles(connectorUserId, searchString, searchOptions);
    }
}
