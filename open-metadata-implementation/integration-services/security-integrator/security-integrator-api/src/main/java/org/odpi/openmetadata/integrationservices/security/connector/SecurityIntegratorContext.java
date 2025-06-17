/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.security.connector;

import org.odpi.openmetadata.accessservices.securitymanager.api.SecurityManagerEventListener;
import org.odpi.openmetadata.accessservices.securitymanager.client.SecurityManagerClient;
import org.odpi.openmetadata.accessservices.securitymanager.client.SecurityManagerEventClient;
import org.odpi.openmetadata.accessservices.securitymanager.client.UserIdentityManagement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * SecurityIntegratorContext provides a wrapper around the Security Manager OMAS client.
 * It provides the simplified interface to open metadata needed by the SecurityIntegratorConnector.
 */
public class SecurityIntegratorContext extends IntegrationContext
{
    private final SecurityManagerClient      securityManagerClient;
    private final UserIdentityManagement     userIdentityClient;
    private final SecurityManagerEventClient eventClient;

    private final AuditLog auditLog;


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
     * @param securityManagerClient client for exchange requests
     * @param userIdentityClient client form managing user identities
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
    public SecurityIntegratorContext(String                       connectorId,
                                     String                       connectorName,
                                     String                       connectorUserId,
                                     String                       serverName,
                                     OpenIntegrationClient        openIntegrationClient,
                                     GovernanceConfiguration      governanceConfiguration,
                                     OpenMetadataClient           openMetadataStoreClient,
                                     ActionControlInterface       actionControlInterface,
                                     SecurityManagerClient        securityManagerClient,
                                     UserIdentityManagement       userIdentityClient,
                                     SecurityManagerEventClient   eventClient,
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

        this.securityManagerClient = securityManagerClient;
        this.userIdentityClient =userIdentityClient;
        this.eventClient = eventClient;

        this.auditLog            = auditLog;
    }


    /* ========================================================
     * Returning the security manager name from the configuration
     */


    /**
     * Return the qualified name of the security manager that is supplied in the configuration
     * document.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }



    /* ========================================================
     * Register for inbound events from the Security Manager OMAS OutTopic
     */

    /**
     * Register a listener object that will be passed each of the events published by the Security Manager OMAS.
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
    public void registerListener(SecurityManagerEventListener listener) throws InvalidParameterException,
                                                                               ConnectionCheckedException,
                                                                               ConnectorCheckedException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
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
        return securityManagerClient.createSecurityGroup(userId, properties);
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
        securityManagerClient.updateSecurityGroup(userId, securityGroupGUID, isMergeUpdate, properties);
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
        securityManagerClient.deleteSecurityGroup(userId, securityGroupGUID);
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
        return securityManagerClient.getSecurityGroupsForDistinguishedName(userId, distinguishedName, startFrom, pageSize);
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
        return securityManagerClient.getElementsGovernedBySecurityGroup(userId, securityGroupGUID, startFrom, pageSize);
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
        return securityManagerClient.findSecurityGroups(userId, searchString, startFrom, pageSize);
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
                                     ElementProperties parentRelationshipProperties,
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
                                                             TemplateFilter templateFilter,
                                                             List<ElementStatus> limitResultsByStatus,
                                                             Date                asOfTime,
                                                             SequencingOrder sequencingOrder,
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


    /**
     * Return information about a specific actor profile.
     *
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ActorProfileGraphElement getActorProfileByGUID(String actorProfileGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return securityManagerClient.getActorProfileByGUID(userId, actorProfileGUID);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileUserId or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ActorProfileGraphElement getActorProfileByUserId(String actorProfileUserId) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return securityManagerClient.getActorProfileByUserId(userId, actorProfileUserId);
    }


    /**
     * Return information about a named actor profile.
     *
     * @param name unique name for the actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ActorProfileElement> getActorProfileByName(String name,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return securityManagerClient.getActorProfileByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param searchString RegEx string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ActorProfileElement> findActorProfile(String searchString,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        return securityManagerClient.findActorProfile(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of people appointed to a particular role.
     *
     * @param personRoleGUID       unique identifier of the person role
     * @param effectiveTime        time for appointments, null for full appointment history
     * @param startFrom            index of the list to start from (0 for start)
     * @param pageSize             maximum number of elements to return
     *
     * @return list of appointees
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<Appointee> getAppointees(String personRoleGUID,
                                         Date   effectiveTime,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return securityManagerClient.getAppointees(userId, personRoleGUID, effectiveTime, startFrom, pageSize);
    }


    /**
     * Return information about a specific person role.
     *
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *
     * @throws InvalidParameterException personRoleGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ActorRoleElement getPersonRoleByGUID(String personRoleGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return securityManagerClient.getPersonRoleByGUID(userId, personRoleGUID);
    }


    /**
     * Return information about a named person role.
     *
     * @param name unique name for the actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ActorRoleElement> getPersonRoleByName(String name,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return securityManagerClient.getPersonRoleByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of matching roles for the search string.
     *
     * @param searchString RegEx string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ActorRoleElement> findPersonRole(String searchString,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return securityManagerClient.findPersonRole(userId, searchString, startFrom, pageSize);
    }
}
