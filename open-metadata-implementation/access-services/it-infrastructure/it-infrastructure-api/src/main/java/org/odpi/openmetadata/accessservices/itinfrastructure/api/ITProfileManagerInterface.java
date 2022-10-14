/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ITProfileElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ContactMethodProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ITProfileProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProfileIdentityProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;


/**
 * ITProfileManagerInterface defines the client interface for setting up the profile and userId for a piece of IT infrastructure.
 */
public interface ITProfileManagerInterface
{
    /**
     * Create a definition of an IT profile.  If the itInfrastructureGUID is provided, it is connected to the infrastructure element that the
     * profile describes using the ITInfrastructureProfile relationship.  If the itUserId is specified, a UserIdentity for that userId is
     * found/created and connected to the new IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the piece of IT infrastructure that is described by the new IT profile.
     * @param itUserId            user Id used by the IT Infrastructure
     * @param properties          properties for a IT profile
     *
     * @return unique identifier of IT profile
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createITProfile(String              userId,
                           String              externalSourceGUID,
                           String              externalSourceName,
                           String              itInfrastructureGUID,
                           String              itUserId,
                           ITProfileProperties properties) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Update the definition of an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itProfileGUID unique identifier of IT profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateITProfile(String              userId,
                         String              externalSourceGUID,
                         String              externalSourceName,
                         String              itProfileGUID,
                         boolean             isMergeUpdate,
                         ITProfileProperties properties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Remove the definition of an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itProfileGUID unique identifier of IT profile
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteITProfile(String userId,
                         String itProfileGUID,
                         String externalSourceGUID,
                         String externalSourceName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Add a new contact method to the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itProfileGUID identifier of the profile to update.
     * @param properties properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addContactMethod(String                  userId,
                            String                  externalSourceGUID,
                            String                  externalSourceName,
                            String                  itProfileGUID,
                            ContactMethodProperties properties) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


    /**
     * Remove an obsolete contact method from the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteContactMethod(String userId,
                             String externalSourceGUID,
                             String externalSourceName,
                             String contactMethodGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Link a piece of infrastructure to an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the IT Infrastructure element
     * @param itProfileGUID unique identifier of the IT profile
     * @param effectiveFrom start date for the  relationship
     * @param effectiveTo end date for the relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkITInfrastructureToProfile(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String itInfrastructureGUID,
                                       String itProfileGUID,
                                       Date   effectiveFrom,
                                       Date   effectiveTo) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Update the effectivity dates of a link from a piece of infrastructure to an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the IT Infrastructure element
     * @param itProfileGUID unique identifier of the IT profile
     * @param effectiveFrom start date for the  relationship
     * @param effectiveTo end date for the relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateITInfrastructureToProfile(String userId,
                                         String externalSourceGUID,
                                         String externalSourceName,
                                         String itInfrastructureGUID,
                                         String itProfileGUID,
                                         Date   effectiveFrom,
                                         Date   effectiveTo) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the link between a piece of infrastructure to an IT profile.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param itInfrastructureGUID unique identifier of the IT Infrastructure element
     * @param itProfileGUID unique identifier of the IT profile
     * @param effectiveTime time that the relationship is active - null for any time
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkITInfrastructureFromProfile(String userId,
                                           String externalSourceGUID,
                                           String externalSourceName,
                                           String itInfrastructureGUID,
                                           String itProfileGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Return information about a specific IT profile.
     *
     * @param userId calling user
     * @param itProfileGUID unique identifier for the IT profile
     *
     * @return properties of the IT profile
     *
     * @throws InvalidParameterException itProfileGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ITProfileElement getITProfileByGUID(String userId,
                                        String itProfileGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Return information about a specific IT profile.
     *
     * @param userId calling user
     * @param itProfileUserId unique identifier for the IT profile
     *
     * @return properties of the IT profile
     *
     * @throws InvalidParameterException itProfileUserId or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ITProfileElement getITProfileByUserId(String userId,
                                          String itProfileUserId) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Return information about a named IT profile.
     *
     * @param userId calling user
     * @param name unique name for the IT profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching IT profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ITProfileElement> getITProfileByName(String userId,
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
     * @return list of matching IT profiles
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<ITProfileElement> findITProfile(String userId,
                                         String searchString,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;


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
     * Remove a user identity object.
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
     * Link a user identity to a profile.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID additional userId for the profile
     * @param profileGUID the profile to add the identity to
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void addIdentityToProfile(String                    userId,
                              String                    externalSourceGUID,
                              String                    externalSourceName,
                              String                    userIdentityGUID,
                              String                    profileGUID,
                              ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;


    /**
     * Update the properties of the relationship between a user identity and profile.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID additional userId for the profile
     * @param profileGUID the profile to add the identity to
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void updateProfileIdentity(String                    userId,
                               String                    externalSourceGUID,
                               String                    externalSourceName,
                               String                    userIdentityGUID,
                               String                    profileGUID,
                               boolean                   isMergeUpdate,
                               ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;


    /**
     * Unlink a user identity from a profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void removeIdentityFromProfile(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String userIdentityGUID,
                                   String profileGUID) throws InvalidParameterException,
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

}
