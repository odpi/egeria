/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;

import java.util.List;

/**
 * This interface is used to authenticate a user and return the properties known about them that can be included in
 * the resulting JWT token.
 */
public interface OpenMetadataUserSecurity
{
    /**
     * Retrieve information about a specific user
     *
     * @param userId calling user
     * @return security properties about the user
     * @throws UserNotAuthorizedException user not recognized - or supplied an incorrect password
     */
   OpenMetadataUserAccount getUserAccount(String userId) throws UserNotAuthorizedException;




    /**
     * Determine the appropriate setting for the supported zones depending on the user and the
     * default supported zones set up for the service.  This is called whenever an element is accessed.
     *
     * @param supportedZones default setting of the supported zones
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of supported zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException a problem calculating the zones
     * @throws UserNotAuthorizedException unknown user
     */
    List<String> getSupportedZonesForUser(List<String>  supportedZones,
                                          String        typeName,
                                          String        serviceName,
                                          String        userId) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


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


    /**
     * Determine the appropriate setting for the zones depending on the user and the
     * current zones set up for the element.  This is called whenever an element is withdrawn.
     *
     * @param currentZones default setting of the default zones
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of published zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException a problem calculating the zones
     * @throws UserNotAuthorizedException unknown user
     */
    List<String> getWithdrawZonesForUser(List<String>  currentZones,
                                         String        typeName,
                                         String        serviceName,
                                         String        userId) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException;
}
