/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.organization.connector;

import org.odpi.openmetadata.accessservices.communityprofile.api.CommunityProfileEventListener;
import org.odpi.openmetadata.accessservices.communityprofile.client.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OrganizationIntegratorContext provides a wrapper around the Community Profile OMAS client.
 * It provides the simplified interface to open metadata needed by the OrganizationIntegratorConnector.
 */
public class OrganizationIntegratorContext extends IntegrationContext
{
    private final ActorProfileManagement      actorProfileManagement;
    private final ActorRoleManagement         actorRoleManagement;
    private final ContactMethodManagement     contactMethodManagement;
    private final SecurityGroupManagement     securityGroupClient;
    private final UserIdentityManagement      userIdentityClient;
    private final CommunityProfileEventClient eventClient;



    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param actorProfileManagement client for exchange requests
     * @param actorRoleManagement client for exchange requests
     * @param securityGroupManagement client for exchange requests
     * @param userIdentityManagement client for exchange requests
     * @param eventClient client for registered listeners
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the asset manager
     * @param externalSourceName unique name of the software server capability for the asset manager
     * @param maxPageSize max number of elements that can be returned on a query
     * @param auditLog logging destination
     */
    public OrganizationIntegratorContext(String                       connectorId,
                                         String                       connectorName,
                                         String                       connectorUserId,
                                         String                       serverName,
                                         OpenIntegrationClient        openIntegrationClient,
                                         GovernanceConfiguration      governanceConfiguration,
                                         OpenMetadataClient           openMetadataStoreClient,
                                         ActionControlInterface       actionControlInterface,
                                         ActorProfileManagement       actorProfileManagement,
                                         ActorRoleManagement          actorRoleManagement,
                                         ContactMethodManagement      contactMethodManagement,
                                         SecurityGroupManagement      securityGroupManagement,
                                         UserIdentityManagement       userIdentityManagement,
                                         CommunityProfileEventClient  eventClient,
                                         boolean                      generateIntegrationReport,
                                         PermittedSynchronization     permittedSynchronization,
                                         String                       integrationConnectorGUID,
                                         String                       externalSourceGUID,
                                         String                       externalSourceName,
                                         int                          maxPageSize,
                                         AuditLog                     auditLog)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.actorProfileManagement  = actorProfileManagement;
        this.actorRoleManagement     = actorRoleManagement;
        this.contactMethodManagement = contactMethodManagement;
        this.securityGroupClient     = securityGroupManagement;
        this.userIdentityClient      = userIdentityManagement;
        this.eventClient             = eventClient;
    }



    /* ========================================================
     * Returning the external source name from the configuration
     */


    /**
     * Return the qualified name of the external source that is supplied in the configuration
     * document.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /* ========================================================
     * Register for inbound events from the Community Profile OMAS OutTopic
     */

    /**
     * Register a listener object that will be passed each of the events published by the Community Profile OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(CommunityProfileEventListener listener) throws InvalidParameterException,
                                                                                ConnectionCheckedException,
                                                                                ConnectorCheckedException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * Methods to update open metadata
     */

    /**
     * Create a new actor profile.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param properties                   properties for the new element.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createActorProfile(String                 anchorGUID,
                                     boolean                isOwnAnchor,
                                     String                 anchorScopeGUID,
                                     ActorProfileProperties properties,
                                     String                 parentGUID,
                                     String                 parentRelationshipTypeName,
                                     ElementProperties      parentRelationshipProperties,
                                     boolean                parentAtEnd1,
                                     boolean                forLineage,
                                     boolean                forDuplicateProcessing,
                                     Date                   effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        String actorProfileGUID = actorProfileManagement.createActorProfile(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, properties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(actorProfileGUID);
        }

        return actorProfileGUID;
    }


    /**
     * Create a new metadata element to represent an actor profile using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new actor profile.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param effectiveFrom                the date when this element is active - null for active on creation
     * @param effectiveTo                  the date when this element becomes inactive - null for active until deleted
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createActorProfileFromTemplate(String              anchorGUID,
                                                 boolean             isOwnAnchor,
                                                 String              anchorScopeGUID,
                                                 Date                effectiveFrom,
                                                 Date                effectiveTo,
                                                 String              templateGUID,
                                                 ElementProperties   replacementProperties,
                                                 Map<String, String> placeholderProperties,
                                                 String              parentGUID,
                                                 String              parentRelationshipTypeName,
                                                 ElementProperties   parentRelationshipProperties,
                                                 boolean             parentAtEnd1,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String actorProfileGUID = actorProfileManagement.createActorProfileFromTemplate(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, effectiveFrom, effectiveTo, templateGUID, replacementProperties, placeholderProperties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(actorProfileGUID);
        }

        return actorProfileGUID;
    }


    /**
     * Update the properties of an actor profile.
     *
     * @param actorProfileGUID       unique identifier of the actor profile (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateActorProfile(String                 actorProfileGUID,
                                   boolean                replaceAllProperties,
                                   ActorProfileProperties properties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        actorProfileManagement.updateActorProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, replaceAllProperties, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(actorProfileGUID);
        }
    }


    /**
     * Attach a profile to a location.
     *
     * @param locationGUID           unique identifier of the location
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkLocationToProfile(String                    actorProfileGUID,
                                      String                    locationGUID,
                                      ProfileLocationProperties relationshipProperties,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        actorProfileManagement.linkLocationToProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, locationGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach an actor profile from a location.
     *
     * @param locationGUID           unique identifier of the parent actor profile.
     * @param actorProfileGUID            unique identifier of the nested actor profile.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachLocationFromProfile(String  actorProfileGUID,
                                          String  locationGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        actorProfileManagement.detachLocationFromProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, locationGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerPerson(String         personOneGUID,
                               String         personTwoGUID,
                               PeerProperties relationshipProperties,
                               boolean        forLineage,
                               boolean        forDuplicateProcessing,
                               Date           effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        actorProfileManagement.linkPeerPerson(userId, externalSourceGUID, externalSourceName, personOneGUID, personTwoGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach a person profile to one of its peers.
     *
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerPerson(String  personOneGUID,
                                 String  personTwoGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        actorProfileManagement.detachPeerPerson(userId, externalSourceGUID, externalSourceName, personOneGUID, personTwoGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Attach a super team to a subteam.
     *
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamStructure(String                  superTeamGUID,
                                  String                  subteamGUID,
                                  TeamStructureProperties relationshipProperties,
                                  boolean                 forLineage,
                                  boolean                 forDuplicateProcessing,
                                  Date                    effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        actorProfileManagement.linkTeamStructure(userId, externalSourceGUID, externalSourceName, superTeamGUID, subteamGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamStructure(String  superTeamGUID,
                                    String  subteamGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        actorProfileManagement.detachTeamStructure(userId, externalSourceGUID, externalSourceName, superTeamGUID, subteamGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Attach a team to its membership role.
     *
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamToMembershipRole(String                   teamGUID,
                                         String                   personRoleGUID,
                                         TeamMembershipProperties relationshipProperties,
                                         boolean                  forLineage,
                                         boolean                  forDuplicateProcessing,
                                         Date                     effectiveTime) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        actorProfileManagement.linkTeamToMembershipRole(userId, externalSourceGUID, externalSourceName, teamGUID, personRoleGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach a team profile from its membership role.
     *
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamFromMembershipRole(String  teamGUID,
                                             String  personRoleGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        actorProfileManagement.detachTeamFromMembershipRole(userId, externalSourceGUID, externalSourceName, teamGUID, personRoleGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Attach a team to its leadership role.
     *
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamToLeadershipRole(String                   teamGUID,
                                         String                   personRoleGUID,
                                         TeamLeadershipProperties relationshipProperties,
                                         boolean                  forLineage,
                                         boolean                  forDuplicateProcessing,
                                         Date                     effectiveTime) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        actorProfileManagement.linkTeamToLeadershipRole(userId, externalSourceGUID, externalSourceName, teamGUID, personRoleGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach a team profile from its leadership role.
     *
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamFromLeadershipRole(String  teamGUID,
                                             String  personRoleGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        actorProfileManagement.detachTeamFromLeadershipRole(userId, externalSourceGUID, externalSourceName, teamGUID, personRoleGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Delete a actor profile.
     *
     * @param actorProfileGUID       unique identifier of the element
     * @param cascadedDelete         can the actor profile be deleted if it has nested components linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteActorProfile(String  actorProfileGUID,
                                   boolean cascadedDelete,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        actorProfileManagement.deleteActorProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, cascadedDelete, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(actorProfileGUID);
        }
    }


    /**
     * Create a new contact method.
     *
     * @param actorProfileGUID             unique identifier of the actor profile that should be the anchor for the new element.
     * @param properties                   properties for the new element.
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createContactMethod(String                  actorProfileGUID,
                                      ContactMethodProperties properties,
                                      boolean                 forLineage,
                                      boolean                 forDuplicateProcessing,
                                      Date                    effectiveTime) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        String contactMethodGUID = contactMethodManagement.createContactMethod(userId, externalSourceGUID, externalSourceName, actorProfileGUID, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(contactMethodGUID);
        }

        return contactMethodGUID;
    }


    /**
     * Create a new metadata element to represent a contact method using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new contact method.
     *
     * @param actorProfileGUID             unique identifier of the actor profile that should be the anchor for the new element.
     * @param effectiveFrom                the date when this element is active - null for active on creation
     * @param effectiveTo                  the date when this element becomes inactive - null for active until deleted
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createContactMethodFromTemplate(String              actorProfileGUID,
                                                  Date                effectiveFrom,
                                                  Date                effectiveTo,
                                                  String              templateGUID,
                                                  ElementProperties   replacementProperties,
                                                  Map<String, String> placeholderProperties,
                                                  boolean             forLineage,
                                                  boolean             forDuplicateProcessing,
                                                  Date                effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        String contactMethodGUID = contactMethodManagement.createContactMethodFromTemplate(userId, externalSourceGUID, externalSourceName, actorProfileGUID, effectiveFrom, effectiveTo, templateGUID, replacementProperties, placeholderProperties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(contactMethodGUID);
        }

        return contactMethodGUID;
    }


    /**
     * Update the properties of a contact method.
     *
     * @param contactMethodGUID      unique identifier of the contact method (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateContactMethod(String                  contactMethodGUID,
                                    boolean                 replaceAllProperties,
                                    ContactMethodProperties properties,
                                    boolean                 forLineage,
                                    boolean                 forDuplicateProcessing,
                                    Date                    effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        contactMethodManagement.updateContactMethod(userId, externalSourceGUID, externalSourceName, contactMethodGUID, replaceAllProperties, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(contactMethodGUID);
        }
    }


    /**
     * Delete a contact method.
     *
     * @param contactMethodGUID       unique identifier of the element
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContactMethod(String  contactMethodGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        contactMethodManagement.deleteContactMethod(userId, externalSourceGUID, externalSourceName, contactMethodGUID, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(contactMethodGUID);
        }
    }


    /**
     * Returns the list of actor profiles with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<ActorProfileElement> getActorProfilesByName(String              name,
                                                            TemplateFilter      templateFilter,
                                                            List<ElementStatus> limitResultsByStatus,
                                                            Date                asOfTime,
                                                            SequencingOrder     sequencingOrder,
                                                            String              sequencingProperty,
                                                            int                 startFrom,
                                                            int                 pageSize,
                                                            boolean             forLineage,
                                                            boolean             forDuplicateProcessing,
                                                            Date                effectiveTime) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        return actorProfileManagement.getActorProfilesByName(userId, name, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Return the properties of a specific actor profile.
     *
     * @param actorProfileGUID       unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ActorProfileElement getActorProfileByGUID(String  actorProfileGUID,
                                                     Date    asOfTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return actorProfileManagement.getActorProfileByGUID(userId, actorProfileGUID, asOfTime, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Return the properties of a specific actor profile retrieved using an associated userId.
     *
     * @param requiredUserId         identifier of user
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ActorProfileElement getActorProfileByUserId(String  requiredUserId,
                                                       Date    asOfTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return actorProfileManagement.getActorProfileByUserId(userId, requiredUserId, asOfTime, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the list of actor profiles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned actor profiles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ActorProfileElement> findActorProfiles(String              searchString,
                                                       TemplateFilter      templateFilter,
                                                       List<ElementStatus> limitResultsByStatus,
                                                       Date                asOfTime,
                                                       SequencingOrder     sequencingOrder,
                                                       String              sequencingProperty,
                                                       int                 startFrom,
                                                       int                 pageSize,
                                                       boolean             forLineage,
                                                       boolean             forDuplicateProcessing,
                                                       Date                effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return actorProfileManagement.findActorProfiles(userId, searchString, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Create a new actor role.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param properties                   properties for the new element.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createActorRole(String                  anchorGUID,
                                  boolean                 isOwnAnchor,
                                  String                  anchorScopeGUID,
                                  ActorRoleProperties     properties,
                                  String                  parentGUID,
                                  String                  parentRelationshipTypeName,
                                  ElementProperties       parentRelationshipProperties,
                                  boolean                 parentAtEnd1,
                                  boolean                 forLineage,
                                  boolean                 forDuplicateProcessing,
                                  Date                    effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        String actorRoleGUID = actorRoleManagement.createActorRole(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, properties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(actorRoleGUID);
        }

        return actorRoleGUID;
    }


    /**
     * Create a new metadata element to represent an actor role using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new actor role.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param effectiveFrom                the date when this element is active - null for active on creation
     * @param effectiveTo                  the date when this element becomes inactive - null for active until deleted
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createActorRoleFromTemplate(String              anchorGUID,
                                              boolean             isOwnAnchor,
                                              String              anchorScopeGUID,
                                              Date                effectiveFrom,
                                              Date                effectiveTo,
                                              String              templateGUID,
                                              ElementProperties   replacementProperties,
                                              Map<String, String> placeholderProperties,
                                              String              parentGUID,
                                              String              parentRelationshipTypeName,
                                              ElementProperties   parentRelationshipProperties,
                                              boolean             parentAtEnd1,
                                              boolean             forLineage,
                                              boolean             forDuplicateProcessing,
                                              Date                effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        String actorRoleGUID = actorRoleManagement.createActorRoleFromTemplate(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, effectiveFrom, effectiveTo, templateGUID, replacementProperties, placeholderProperties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(actorRoleGUID);
        }

        return actorRoleGUID;
    }


    /**
     * Update the properties of an actor role.
     *
     * @param actorRoleGUID      unique identifier of the actor role (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateActorRole(String                  actorRoleGUID,
                                boolean                 replaceAllProperties,
                                ActorRoleProperties     properties,
                                boolean                 forLineage,
                                boolean                 forDuplicateProcessing,
                                Date                    effectiveTime) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        actorRoleManagement.updateActorRole(userId, externalSourceGUID, externalSourceName, actorRoleGUID, replaceAllProperties, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(actorRoleGUID);
        }
    }

    /**
     * Attach a person role to a person profile.
     *
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPersonRoleToProfile(String                          personRoleGUID,
                                        String                          personProfileGUID,
                                        PersonRoleAppointmentProperties relationshipProperties,
                                        boolean                         forLineage,
                                        boolean                         forDuplicateProcessing,
                                        Date                            effectiveTime) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        actorRoleManagement.linkPersonRoleToProfile(userId, externalSourceGUID, externalSourceName, personRoleGUID, personProfileGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach a person role from a profile.
     *
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPersonRoleFromProfile(String  personRoleGUID,
                                            String  personProfileGUID,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        actorRoleManagement.detachPersonRoleFromProfile(userId, externalSourceGUID, externalSourceName, personRoleGUID, personProfileGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Attach a team role to a team profile.
     *
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTeamRoleToProfile(String                        teamRoleGUID,
                                      String                        teamProfileGUID,
                                      TeamRoleAppointmentProperties relationshipProperties,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      Date                          effectiveTime) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        actorRoleManagement.linkTeamRoleToProfile(userId, externalSourceGUID, externalSourceName, teamRoleGUID, teamProfileGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach a team role from a team profile.
     *
     * @param teamRoleGUID              unique identifier of the team
     * @param teamProfileGUID          unique identifier of the team profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTeamRoleFromProfile(String  teamRoleGUID,
                                          String  teamProfileGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        actorRoleManagement.detachTeamRoleFromProfile(userId, externalSourceGUID, externalSourceName, teamRoleGUID, teamProfileGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Delete a actor role.
     *
     * @param actorRoleGUID      unique identifier of the element
     * @param cascadedDelete         can the actor role be deleted if it has solution components linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteActorRole(String  actorRoleGUID,
                                boolean cascadedDelete,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        actorRoleManagement.deleteActorRole(userId, externalSourceGUID, externalSourceName, actorRoleGUID, cascadedDelete, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(actorRoleGUID);
        }
    }


    /**
     * Returns the list of actor roles with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<ActorRoleElement> getActorRolesByName(String              name,
                                                      TemplateFilter      templateFilter,
                                                      List<ElementStatus> limitResultsByStatus,
                                                      Date                asOfTime,
                                                      SequencingOrder     sequencingOrder,
                                                      String              sequencingProperty,
                                                      int                 startFrom,
                                                      int                 pageSize,
                                                      boolean             forLineage,
                                                      boolean             forDuplicateProcessing,
                                                      Date                effectiveTime) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return actorRoleManagement.getActorRolesByName(userId, name, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Return the properties of a specific actor role.
     *
     * @param actorRoleGUID      unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ActorRoleElement getActorRoleByGUID(String  actorRoleGUID,
                                               Date    asOfTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return actorRoleManagement.getActorRoleByGUID(userId, actorRoleGUID, asOfTime, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned actor roles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString         string to find in the properties
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ActorRoleElement> findActorRoles(String              searchString,
                                                 TemplateFilter      templateFilter,
                                                 List<ElementStatus> limitResultsByStatus,
                                                 Date                asOfTime,
                                                 SequencingOrder     sequencingOrder,
                                                 String              sequencingProperty,
                                                 int                 startFrom,
                                                 int                 pageSize,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return actorRoleManagement.findActorRoles(userId, searchString, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /* ========================================
     * Security Groups
     */

    /**
     * Create a new security group.  The type of the definition is located in the properties.
     *
     * @param properties properties of the definition
     *
     * @return unique identifier of the definition
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing the metadata service
     * @throws UserNotAuthorizedException security access problem
     */
    public String createSecurityGroup(SecurityGroupProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return securityGroupClient.createSecurityGroup(userId, properties);
    }


    /**
     * Update an existing security group.
     *
     * @param securityGroupGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  updateSecurityGroup(String                  securityGroupGUID,
                                     boolean                 isMergeUpdate,
                                     SecurityGroupProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        securityGroupClient.updateSecurityGroup(userId, securityGroupGUID, isMergeUpdate, properties);
    }


    /**
     * Delete a specific security group.
     *
     * @param securityGroupGUID unique identifier of the definition to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  deleteSecurityGroup(String securityGroupGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        securityGroupClient.deleteSecurityGroup(userId, securityGroupGUID);
    }


    /**
     * Return the list of security groups associated with a unique distinguishedName.  In an ideal world, there should be only one.
     *
     * @param distinguishedName unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    public List<SecurityGroupElement> getSecurityGroupsForDistinguishedName(String distinguishedName,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return securityGroupClient.getSecurityGroupsForDistinguishedName(userId, distinguishedName, startFrom, pageSize);
    }


    /**
     * Return the elements that are governed by the supplied security group.
     *
     * @param securityGroupGUID unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for the associated elements
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    public List<ElementStub> getElementsGovernedBySecurityGroup(String securityGroupGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return securityGroupClient.getElementsGovernedBySecurityGroup(userId, securityGroupGUID, startFrom, pageSize);
    }


    /**
     * Return the list of security groups that match the search string - this can be a regular expression.
     *
     * @param searchString value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    public List<SecurityGroupElement> findSecurityGroups(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return securityGroupClient.findSecurityGroups(userId, searchString, startFrom, pageSize);
    }



    /* ========================================================
     * Manage user identities
     */


    /**
     * Create a new user identity.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param properties                   properties for the new element.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createUserIdentity(String                 anchorGUID,
                                     boolean                isOwnAnchor,
                                     String                 anchorScopeGUID,
                                     UserIdentityProperties properties,
                                     String                 parentGUID,
                                     String                 parentRelationshipTypeName,
                                     ElementProperties      parentRelationshipProperties,
                                     boolean                parentAtEnd1,
                                     boolean                forLineage,
                                     boolean                forDuplicateProcessing,
                                     Date                   effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        String userIdentityGUID = userIdentityClient.createUserIdentity(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, properties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(userIdentityGUID);
        }

        return userIdentityGUID;
    }


    /**
     * Create a new metadata element to represent a user identity using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new user identity.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param effectiveFrom                the date when this element is active - null for active on creation
     * @param effectiveTo                  the date when this element becomes inactive - null for active until deleted
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createUserIdentityFromTemplate(String              anchorGUID,
                                                 boolean             isOwnAnchor,
                                                 String              anchorScopeGUID,
                                                 Date                effectiveFrom,
                                                 Date                effectiveTo,
                                                 String              templateGUID,
                                                 ElementProperties   replacementProperties,
                                                 Map<String, String> placeholderProperties,
                                                 String              parentGUID,
                                                 String              parentRelationshipTypeName,
                                                 ElementProperties   parentRelationshipProperties,
                                                 boolean             parentAtEnd1,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String userIdentityGUID = userIdentityClient.createUserIdentityFromTemplate(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, effectiveFrom, effectiveTo, templateGUID, replacementProperties, placeholderProperties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(userIdentityGUID);
        }

        return userIdentityGUID;
    }


    /**
     * Update the properties of a user identity.
     *
     * @param userIdentityGUID      unique identifier of the user identity (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateUserIdentity(String                 userIdentityGUID,
                                   boolean                replaceAllProperties,
                                   UserIdentityProperties properties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        userIdentityClient.updateUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, replaceAllProperties, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(userIdentityGUID);
        }
    }


    /**
     * Attach a profile to a user identity.
     *
     * @param userIdentityGUID unique identifier of the parent
     * @param profileGUID     unique identifier of the actor profile
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkIdentityToProfile(String                    userIdentityGUID,
                                      String                    profileGUID,
                                      ProfileIdentityProperties relationshipProperties,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        userIdentityClient.linkIdentityToProfile(userId, externalSourceGUID, externalSourceName, userIdentityGUID, profileGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach an actor profile from a user identity.
     *
     * @param userIdentityGUID    unique identifier of the parent actor profile.
     * @param profileGUID    unique identifier of the nested actor profile.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProfileIdentity(String  userIdentityGUID,
                                      String  profileGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        userIdentityClient.detachProfileIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, profileGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Add the SecurityGroupMembership classification to the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param properties            properties for the classification
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addSecurityGroupMembership(String                            userIdentityGUID,
                                           SecurityGroupMembershipProperties properties,
                                           boolean                           forLineage,
                                           boolean                           forDuplicateProcessing,
                                           Date                              effectiveTime) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        userIdentityClient.addSecurityGroupMembership(userId, externalSourceGUID, externalSourceName, userIdentityGUID, properties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Update the SecurityGroupMembership classification for the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param properties            properties for the classification
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSecurityGroupMembership(String                            userIdentityGUID,
                                              SecurityGroupMembershipProperties properties,
                                              boolean                           forLineage,
                                              boolean                           forDuplicateProcessing,
                                              Date                              effectiveTime) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        userIdentityClient.updateSecurityGroupMembership(userId, externalSourceGUID, externalSourceName, userIdentityGUID, properties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Remove the SecurityGroupMembership classification from the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeAllSecurityGroupMembership(String                            userIdentityGUID,
                                                 boolean                           forLineage,
                                                 boolean                           forDuplicateProcessing,
                                                 Date                              effectiveTime) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        userIdentityClient.removeAllSecurityGroupMembership(userId, externalSourceGUID, externalSourceName, userIdentityGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Delete a user identity.
     *
     * @param userIdentityGUID      unique identifier of the element
     * @param cascadedDelete         can the user identity be deleted if it has actor profiles linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteUserIdentity(String  userIdentityGUID,
                                   boolean cascadedDelete,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        userIdentityClient.deleteUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, cascadedDelete, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(userIdentityGUID);
        }
    }


    /**
     * Returns the list of user identities with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<UserIdentityElement> getUserIdentitiesByName(String              name,
                                                             TemplateFilter      templateFilter,
                                                             List<ElementStatus> limitResultsByStatus,
                                                             Date                asOfTime,
                                                             SequencingOrder     sequencingOrder,
                                                             String              sequencingProperty,
                                                             int                 startFrom,
                                                             int                 pageSize,
                                                             boolean             forLineage,
                                                             boolean             forDuplicateProcessing,
                                                             Date                effectiveTime) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        return userIdentityClient.getUserIdentitiesByName(userId, name, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Return the properties of a specific user identity.
     *
     * @param userIdentityGUID      unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public UserIdentityElement getUserIdentityByGUID(String  userIdentityGUID,
                                                     Date    asOfTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return userIdentityClient.getUserIdentityByGUID(userId, userIdentityGUID, asOfTime, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the list of user identities metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<UserIdentityElement> findUserIdentities(String              searchString,
                                                        TemplateFilter      templateFilter,
                                                        List<ElementStatus> limitResultsByStatus,
                                                        Date                asOfTime,
                                                        SequencingOrder     sequencingOrder,
                                                        String              sequencingProperty,
                                                        int                 startFrom,
                                                        int                 pageSize,
                                                        boolean             forLineage,
                                                        boolean             forDuplicateProcessing,
                                                        Date                effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return userIdentityClient.findUserIdentities(userId, searchString, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }
}
