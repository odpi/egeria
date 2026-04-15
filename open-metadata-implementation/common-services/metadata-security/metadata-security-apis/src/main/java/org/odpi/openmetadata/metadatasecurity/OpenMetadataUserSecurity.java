/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecurityAccessControl;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;

import java.util.List;

/**
 * This interface is used to authenticate a user and return the properties known about them that can be included in
 * the resulting JWT token.
 */
public interface OpenMetadataUserSecurity
{
    /**
     * Return the list of defined users.
     *
     * @param userAccountStatus status of the user - or null for any status
     * @param userAccountType   type of user - or null for any type
     * @return list of userIds
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    List<String> getUserList(UserAccountStatus userAccountStatus,
                             UserAccountType   userAccountType) throws UserNotAuthorizedException,
                                                                       InvalidParameterException,
                                                                       PropertyServerException;

    /**
     * Retrieve information about a specific user
     *
     * @param userId calling user
     * @return security properties about the user
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
   OpenMetadataUserAccount getUserAccount(String userId) throws UserNotAuthorizedException,
                                                                InvalidParameterException,
                                                                PropertyServerException;


    /**
     * Create/update information about a specific user.  This is used to update user details and reset the password.
     *
     * @param userAccount security properties about the user
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    void setUserAccount(OpenMetadataUserAccount userAccount) throws UserNotAuthorizedException,
                                                                    InvalidParameterException,
                                                                    PropertyServerException;


    /**
     * Delete information about a specific user.
     *
     * @param userId      calling user
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    void deleteUserAccount(String userId) throws UserNotAuthorizedException,
                                                 InvalidParameterException,
                                                 PropertyServerException;


    /**
     * Retrieve information about a specific security access control.
     *
     * @param controlName calling user
     * @return security access control
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    OpenMetadataSecurityAccessControl getSecurityAccessControl(String controlName) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException;


    /**
     * Create/update information about a specific security access control.
     *
     * @param securityAccessControl control properties
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    void setSecurityAccessControl(OpenMetadataSecurityAccessControl securityAccessControl) throws UserNotAuthorizedException,
                                                                                                  InvalidParameterException,
                                                                                                  PropertyServerException;


    /**
     * Delete information about a specific security access control.
     *
     * @param securityAccessControl calling user
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    void deleteSecurityAccessControl(String securityAccessControl) throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          PropertyServerException;


    /**
     * Determine the appropriate setting for the default zones depending on the user and the
     * default zones set up for the service.  This is called whenever an element is created.
     *
     * @param initialZones default setting of the default zones from the caller's create request.
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of default zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException a problem calculating the zones
     * @throws UserNotAuthorizedException unknown user
     */
    List<String> getDefaultZonesForUser(List<String>  initialZones,
                                        String        typeName,
                                        String        serviceName,
                                        String        userId) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;


    /**
     * Determine the appropriate setting for the zones depending on the user and the
     * current zones set up for the element.  This is called whenever an element is published.
     *
     * @param currentZones default setting of the published zones
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of published zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException a problem calculating the zones
     * @throws UserNotAuthorizedException unknown user
     */
    List<String> getPublishZonesForUser(List<String>  currentZones,
                                        String        typeName,
                                        String        serviceName,
                                        String        userId) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;
}
