/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;

import java.util.Date;
import java.util.List;

/**
 * SecurityManagerInterface provides the interface both for retrieving additional information in order to process OutTopic events and for
 * making changes to open metadata to match the content of the security manager.
 */
public interface SecurityManagerInterface
{
    /**
     * Create a new security group.  The type of the definition is located in the properties.
     *
     * @param userId calling user
     * @param properties properties of the definition
     *
     * @return unique identifier of the definition
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing the metadata service
     * @throws UserNotAuthorizedException security access problem
     */
    String createSecurityGroup(String                  userId,
                               SecurityGroupProperties properties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;

    /**
     * Update an existing security group.
     *
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  updateSecurityGroup(String                  userId,
                              String                  securityGroupGUID,
                              boolean                 isMergeUpdate,
                              SecurityGroupProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Delete a specific security group.
     *
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  deleteSecurityGroup(String userId,
                              String securityGroupGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Return the list of security groups associated with a unique distinguishedName.  In an ideal world, the should be only one.
     *
     * @param userId calling user
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
    List<SecurityGroupElement> getSecurityGroupsForDistinguishedName(String userId,
                                                                     String distinguishedName,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Return the elements that are governed by the supplied security group.
     *
     * @param userId calling user
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
    List<ElementStub> getElementsGovernedBySecurityGroup(String userId,
                                                         String securityGroupGUID,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Return the list of security groups that match the search string - this can be a regular expression.
     *
     * @param userId calling user
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
    List<SecurityGroupElement> findSecurityGroups(String userId,
                                                  String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;



    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    SecurityGroupElement getSecurityGroupByGUID(String userId,
                                                String securityGroupGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param newIdentity properties for the new userIdentity.
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createUserIdentity(String                 userId,
                              String                 externalSourceGUID,
                              String                 externalSourceName,
                              UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Update a UserIdentity.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties updated properties for the new userIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void updateUserIdentity(String                 userId,
                            String                 externalSourceGUID,
                            String                 externalSourceName,
                            String                 userIdentityGUID,
                            boolean                isMergeUpdate,
                            UserIdentityProperties properties) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException;


    /**
     * Remove a user identity object.  This will fail if a profile would be left without an associated user identity.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteUserIdentity(String userId,
                            String externalSourceGUID,
                            String externalSourceName,
                            String userIdentityGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<UserIdentityElement> findUserIdentities(String userId,
                                                 String searchString,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<UserIdentityElement>  getUserIdentitiesByName(String userId,
                                                       String name,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    UserIdentityElement getUserIdentityByGUID(String userId,
                                              String userIdentityGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Return information about a specific actor profile.
     *
     * @param userId calling user
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ActorProfileElement getActorProfileByGUID(String userId,
                                              String actorProfileGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Return information about a specific actor profile.
     *
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     * @throws InvalidParameterException actorProfileUserId or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ActorProfileElement getActorProfileByUserId(String userId,
                                                String actorProfileUserId) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Return information about a named actor profile.
     *
     * @param userId calling user
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
    List<ActorProfileElement> getActorProfileByName(String userId,
                                                    String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param userId the name of the calling user.
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
    List<ActorProfileElement> findActorProfile(String userId,
                                               String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;



    /**
     * Return the list of people appointed to a particular role.
     *
     * @param userId               calling user
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
    List<Appointee> getAppointees(String userId,
                                  String personRoleGUID,
                                  Date   effectiveTime,
                                  int    startFrom,
                                  int    pageSize) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Return information about a specific person role.
     *
     * @param userId calling user
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *
     * @throws InvalidParameterException personRoleGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ActorRoleElement getPersonRoleByGUID(String userId,
                                         String personRoleGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Return information about a named person role.
     *
     * @param userId calling user
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
    List<ActorRoleElement> getPersonRoleByName(String userId,
                                               String name,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the list of matching roles for the search string.
     *
     * @param userId the name of the calling user.
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
    List<ActorRoleElement> findPersonRole(String userId,
                                          String searchString,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;

}
