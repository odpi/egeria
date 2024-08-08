/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.security.connector;

import org.odpi.openmetadata.accessservices.securitymanager.api.SecurityManagerEventListener;
import org.odpi.openmetadata.accessservices.securitymanager.client.SecurityManagerClient;
import org.odpi.openmetadata.accessservices.securitymanager.client.SecurityManagerEventClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;

import java.util.Date;
import java.util.List;


/**
 * SecurityIntegratorContext provides a wrapper around the Security Manager OMAS client.
 * It provides the simplified interface to open metadata needed by the SecurityIntegratorConnector.
 */
public class SecurityIntegratorContext extends IntegrationContext
{
    private final SecurityManagerClient      securityManagerClient;
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
     * User identifies
     */

    /**
     * Create a UserIdentity.
     *
     * @param newIdentity properties for the new userIdentity.
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createUserIdentity(UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return securityManagerClient.createUserIdentity(userId, externalSourceGUID, externalSourceName, newIdentity);
    }


    /**
     * Update a UserIdentity.
     *
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties updated properties for the new userIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateUserIdentity(String                 userIdentityGUID,
                                   boolean                isMergeUpdate,
                                   UserIdentityProperties properties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        securityManagerClient.updateUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, isMergeUpdate, properties);
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param userIdentityGUID unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteUserIdentity(String userIdentityGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        securityManagerClient.deleteUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID);
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<UserIdentityElement> findUserIdentities(String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return securityManagerClient.findUserIdentities(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<UserIdentityElement>  getUserIdentitiesByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return securityManagerClient.getUserIdentitiesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentityElement getUserIdentityByGUID(String userIdentityGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return securityManagerClient.getUserIdentityByGUID(userId, userIdentityGUID);
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
    public ActorProfileElement getActorProfileByGUID(String actorProfileGUID) throws InvalidParameterException,
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
    public ActorProfileElement getActorProfileByUserId(String actorProfileUserId) throws InvalidParameterException,
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
    public PersonRoleElement getPersonRoleByGUID(String personRoleGUID) throws InvalidParameterException,
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
    public List<PersonRoleElement> getPersonRoleByName(String name,
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
    public List<PersonRoleElement> findPersonRole(String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return securityManagerClient.findPersonRole(userId, searchString, startFrom, pageSize);
    }
}
