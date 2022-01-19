/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.PlatformDeploymentElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareServerPlatformElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.PlatformDeploymentProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * SoftwareServerPlatformManagerInterface defines the client side interface for the IT Infrastructure OMAS that is
 * relevant for software server platform assets.   It provides the ability to
 * define and maintain the metadata about a platform and relationships between platforms and hosts.
 */
public interface SoftwareServerPlatformManagerInterface
{
    /**
     * Create a new metadata element to represent a platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the platform be marked as owned by the infrastructure manager so others can not update?
     * @param platformProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSoftwareServerPlatform(String                           userId,
                                        String                           infrastructureManagerGUID,
                                        String                           infrastructureManagerName,
                                        boolean                          infrastructureManagerIsHome,
                                        SoftwareServerPlatformProperties platformProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Create a new metadata element to represent a platform using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the platform be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSoftwareServerPlatformFromTemplate(String             userId,
                                                    String             infrastructureManagerGUID,
                                                    String             infrastructureManagerName,
                                                    boolean            infrastructureManagerIsHome,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


    /**
     * Update the metadata element representing a platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param platformGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param platformProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateSoftwareServerPlatform(String                           userId,
                                      String                           infrastructureManagerGUID,
                                      String                           infrastructureManagerName,
                                      String                           platformGUID,
                                      boolean                          isMergeUpdate,
                                      SoftwareServerPlatformProperties platformProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;



    /**
     * Create a relationship between a platform and a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param platformGUID unique identifier of the platform 
     * @param hostGUID unique identifier of the host
     * @param deploymentProperties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void   deploySoftwareServerPlatformToHost(String                       userId,
                                              String                       infrastructureManagerGUID,
                                              String                       infrastructureManagerName,
                                              String                       platformGUID,
                                              String                       hostGUID,
                                              PlatformDeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;

    /**
     * Update a relationship between a platform and a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param platformGUID unique identifier of the platform
     * @param hostGUID unique identifier of the host
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param deploymentProperties properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateSoftwareServerPlatformOnHost(String                       userId,
                                            String                       infrastructureManagerGUID,
                                            String                       infrastructureManagerName,
                                            String                       platformGUID,
                                            String                       hostGUID,
                                            boolean                      isMergeUpdate,
                                            PlatformDeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;



    /**
     * Remove a relationship between a platform and a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param platformGUID unique identifier of the platform 
     * @param hostGUID unique identifier of the host
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearSoftwareServerPlatformFromHost(String userId,
                                             String infrastructureManagerGUID,
                                             String infrastructureManagerName,
                                             String platformGUID,
                                             String hostGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Update the zones for the platform asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param platformGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishSoftwareServerPlatform(String userId,
                                       String platformGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Update the zones for the platform asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the platform is first created).
     *
     * @param userId calling user
     * @param platformGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawSoftwareServerPlatform(String userId,
                                        String platformGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the metadata element representing a platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param platformGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeSoftwareServerPlatform(String userId,
                                      String infrastructureManagerGUID,
                                      String infrastructureManagerName,
                                      String platformGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;



    /**
     * Retrieve the list of platform metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SoftwareServerPlatformElement> findSoftwareServerPlatforms(String userId,
                                                                    String searchString,
                                                                    Date   effectiveTime,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Retrieve the list of platform metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SoftwareServerPlatformElement> getSoftwareServerPlatformsByName(String userId,
                                                                         String name,
                                                                         Date   effectiveTime,
                                                                         int    startFrom,
                                                                         int    pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Retrieve the list of platforms created by this caller.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SoftwareServerPlatformElement> getSoftwareServerPlatformsForInfrastructureManager(String userId,
                                                                                           String infrastructureManagerGUID,
                                                                                           String infrastructureManagerName,
                                                                                           Date   effectiveTime,
                                                                                           int    startFrom,
                                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException;


    /**
     * Return the list of software server platforms deployed on a particular host.
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the platform to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<PlatformDeploymentElement> getSoftwareServerPlatformsForHost(String userId,
                                                                      String hostGUID,
                                                                      Date   effectiveTime,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;

    /**
     * Return the list of hosts where a software server platform is deployed to.
     *
     * @param userId calling user
     * @param platformGUID unique identifier of the platform to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<PlatformDeploymentElement> getHostsForSoftwareServerPlatform(String userId,
                                                                      String platformGUID,
                                                                      Date   effectiveTime,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


    /**
     * Retrieve the platform metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    SoftwareServerPlatformElement getSoftwareServerPlatformByGUID(String userId,
                                                                  String guid) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;
}
