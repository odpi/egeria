/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.myprofile.metadataelements.PersonalProfileUniverse;

import java.util.List;
import java.util.Map;


/**
 * PersonalProfileManagementInterface defines the client interface for an administrator setting up a profile for an
 * individual.
 */
public interface PersonalProfileManagementInterface
{
    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileUserId userId of the individual whose profile this is.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @return Unique identifier for the personal profile.
     *
     * @throws InvalidParameterException the employee number or full name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    String createPersonalProfile(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              profileUserId,
                                 String              qualifiedName,
                                 String              fullName,
                                 String              knownName,
                                 String              jobTitle,
                                 String              jobRoleDescription,
                                 Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;


    /**
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileGUID unique identifier for the profile.
     * @param qualifiedName personnel/serial/unique employee number of the individual. Used to verify the profileGUID.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param profileProperties  properties about the individual for a new type that is the subclass of Person.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException the known name is null or the qualifiedName does not match the profileGUID.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void   updatePersonalProfile(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              profileGUID,
                                 String              qualifiedName,
                                 String              fullName,
                                 String              knownName,
                                 String              jobTitle,
                                 String              jobRoleDescription,
                                 Map<String, Object> profileProperties,
                                 Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;


    /**
     * Delete the personal profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileGUID unique identifier for the profile.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @throws InvalidParameterException the qualifiedName or guid is null or not recognized
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void   deletePersonalProfile(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String profileGUID,
                                 String qualifiedName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Return the total karma points for this user.
     *
     * @param userId userId of the user making the request.
     * @param profileUserId userId of the profile to update.
     *
     * @return int count of karma points
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    long getKarmaPoints(String userId,
                        String profileUserId) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException;

    /**
     * Return the list of contact methods for this user.
     *
     * @param userId userId of the user making the request.
     * @param profileUserId userId of the profile to update.
     *
     * @return list of contact methods
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<ContactMethodProperties> getContactMethods(String userId,
                                                    String profileUserId) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;


    /**
     * Add a new contact method to the requesting user's profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileUserId userId of the profile to update.
     * @param type type of contact method.
     * @param service service for the contact method.
     * @param value account name for the service.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addContactMethod(String            userId,
                            String            externalSourceGUID,
                            String            externalSourceName,
                            String            profileUserId,
                            ContactMethodType type,
                            String            service,
                            String            value) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;


    /**
     * Remove an obsolete contact method from the requesting user's profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param profileUserId userId of the profile to update.
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     * @param type type of contact method. This is used to confirm that the GUID is the right one.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteContactMethod(String            userId,
                             String            externalSourceGUID,
                             String            externalSourceName,
                             String            profileUserId,
                             String            contactMethodGUID,
                             ContactMethodType type) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;

    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    PersonalProfileUniverse getPersonalProfileByGUID(String userId,
                                                     String profileGUID) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException;

    /**
     * Retrieve a personal profile by userId.
     *
     * @param userId the name of the calling user.
     * @param profileUserId userId associated with the profile.
     *
     * @return personal profile object.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    PersonalProfileUniverse getPersonalProfileForUser(String userId,
                                                      String profileUserId) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException;


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param userId the name of the calling user.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     *
     * @return personal profile object.
     *
     * @throws InvalidParameterException the employee number.
     * @throws PropertyServerException the server is not available, or there is a problem retrieving the profile.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    PersonalProfileUniverse getPersonalProfileByQualifiedName(String userId,
                                                              String qualifiedName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException;


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param userId the name of the calling user.
     * @param name name of individual.
     *
     * @return list of personal profile objects.
     *
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<PersonalProfileUniverse> getPersonalProfilesByName(String userId,
                                                            String name) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException;
}
