/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ContactMethodElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.viewservices.myprofile.metadataelements.PersonalProfileUniverse;

import java.util.List;
import java.util.Map;

/**
 * MyPersonalProfileInterface covers the management of a user's personal profile.
 */
public interface MyPersonalProfileInterface
{
    /**
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile object
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    PersonalProfileUniverse getMyProfile(String userId) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;


    /**
     * Return the total karma points for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return int count of karma points
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    long getMyKarmaPoints(String userId) throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException;


    /**
     * Return the list of contact methods for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return list of contact methods
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<ContactMethodElement> getMyContactDetails(String userId) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Create or update the profile for the requesting user.  This is only permitted if the profile is not locked
     * to (managed by) an external system.
     *
     * @param userId the name of the calling user.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  setUpMyProfile(String              userId,
                         String              qualifiedName,
                         String              fullName,
                         String              knownName,
                         String              jobTitle,
                         String              jobRoleDescription,
                         Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException;


    /**
     * Add a new contact method to the requesting user's profile.
     *
     * @param userId the name of the calling user.
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
    String addMyContactMethod(String              userId,
                              ContactMethodType   type,
                              String              service,
                              String              value) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Remove an obsolete contact method from the requesting user's profile.
     *
     * @param userId the name of the calling user.
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     * @param type type of contact method. This is used to confirm that the GUID is the right one.
     *
     * @throws InvalidParameterException the userId is null or invalid.  Another property is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteMyContactMethod(String            userId,
                               String            contactMethodGUID,
                               ContactMethodType type) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;

    /**
     * Delete the profile for the requesting user.  This is only permitted if the profile is not locked
     * to (managed by) an external system and if the person is not appointed to any roles.
     *
     * @param userId the name of the calling user.
     * @param qualifiedName personnel/serial/unique employee number of the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      deleteMyProfile(String              userId,
                              String              qualifiedName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException;
}
