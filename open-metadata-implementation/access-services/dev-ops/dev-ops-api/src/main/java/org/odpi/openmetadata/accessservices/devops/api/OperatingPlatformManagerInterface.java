/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.devops.api;

import org.odpi.openmetadata.accessservices.devops.metadataelements.OperatingPlatformElement;
import org.odpi.openmetadata.accessservices.devops.properties.OperatingPlatformProperties;
import org.odpi.openmetadata.accessservices.devops.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * OperatingPlatformManagerInterface provides methods to define operating platforms.
 * OperatingPlatforms describe the operating system and hardware characteristics of a host.
 */
public interface OperatingPlatformManagerInterface
{
    /**
     * Create a new metadata element to represent the operating platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param operatingPlatformProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createOperatingPlatform(String                      userId,
                                   String                      infrastructureManagerGUID,
                                   String                      infrastructureManagerName,
                                   OperatingPlatformProperties operatingPlatformProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException;


    /**
     * Create a new metadata element to represent a operating platform using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new operating platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createOperatingPlatformFromTemplate(String             userId,
                                               String             infrastructureManagerGUID,
                                               String             infrastructureManagerName,
                                               String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Update the metadata element representing a operating platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param operatingPlatformGUID unique identifier of the metadata element to update
     * @param operatingPlatformProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateOperatingPlatform(String                      userId,
                                 String                      infrastructureManagerGUID,
                                 String                      infrastructureManagerName,
                                 boolean                     isMergeUpdate,
                                 String                      operatingPlatformGUID,
                                 OperatingPlatformProperties operatingPlatformProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Remove the metadata element representing a operating platform.  This will delete the operating platform and all categories
     * and terms.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param operatingPlatformGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeOperatingPlatform(String userId,
                                 String infrastructureManagerGUID,
                                 String infrastructureManagerName,
                                 String operatingPlatformGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Set up the link between the operating platform and a host.  If the host is already linked to
     * a different operating platform, the existing link is deleted.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param operatingPlatformGUID unique identifier of the operating platform metadata element
     * @param hostGUID unique identifier of the host
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void linkOperatingPlatformToHost(String userId,
                                     String infrastructureManagerGUID,
                                     String infrastructureManagerName,
                                     String operatingPlatformGUID,
                                     String hostGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Remove any link from an operating platform to the requested host.  This is useful if
     * the host is being decommissioned or undergoing an extensive reinstall.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param hostGUID unique identifier of the host
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearOperatingPlatformFromHost(String userId,
                                        String infrastructureManagerGUID,
                                        String infrastructureManagerName,
                                        String hostGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Retrieve the list of operating platform metadata elements that contain the search string.
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
    List<OperatingPlatformElement> findOperatingPlatforms(String userId,
                                                          String searchString,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Retrieve the list of operating platform metadata elements with a matching qualified or display name.
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
    List<OperatingPlatformElement> getOperatingPlatformsByName(String userId,
                                                               String name,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;



    /**
     * Retrieve the operating platform metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param operatingPlatformGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    OperatingPlatformElement getOperatingPlatformByGUID(String userId,
                                                        String operatingPlatformGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;
}
