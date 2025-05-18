/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareServerElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * SoftwareServerManagerInterface defines the client side interface for the IT Infrastructure OMAS that is
 * relevant for softwareServer assets.   It provides the ability to
 * define and maintain the metadata about a software server and the capabilities deployed on it.
 */
public interface SoftwareServerManagerInterface
{

    /*
     * The software server is an IT Infrastructure asset
     */

    /**
     * Create a new metadata element to represent a software server.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the softwareServer be marked as owned by the infrastructure manager so others can not update?
     * @param softwareServerProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSoftwareServer(String                   userId,
                                String                   infrastructureManagerGUID,
                                String                   infrastructureManagerName,
                                boolean                  infrastructureManagerIsHome,
                                SoftwareServerProperties softwareServerProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Create a new metadata element to represent a software server using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the softwareServer be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSoftwareServerFromTemplate(String             userId,
                                            String             infrastructureManagerGUID,
                                            String             infrastructureManagerName,
                                            boolean            infrastructureManagerIsHome,
                                            String             templateGUID,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Update the metadata element representing a software server.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param softwareServerGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param softwareServerProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateSoftwareServer(String                   userId,
                              String                   infrastructureManagerGUID,
                              String                   infrastructureManagerName,
                              String                   softwareServerGUID,
                              boolean                  isMergeUpdate,
                              SoftwareServerProperties softwareServerProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Update the zones for the software server asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param softwareServerGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishSoftwareServer(String userId,
                               String softwareServerGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Update the zones for the software server asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the softwareServer is first created).
     *
     * @param userId calling user
     * @param softwareServerGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawSoftwareServer(String userId,
                                String softwareServerGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Remove the metadata element representing a software server.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param softwareServerGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeSoftwareServer(String userId,
                              String infrastructureManagerGUID,
                              String infrastructureManagerName,
                              String softwareServerGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Retrieve the list of software server metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param effectiveTime time that the element is effective
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SoftwareServerElement> findSoftwareServers(String userId,
                                                    String searchString,
                                                    Date   effectiveTime,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the list of softwareServer metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param effectiveTime time that the element is effective
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SoftwareServerElement> getSoftwareServersByName(String userId,
                                                         String name,
                                                         Date   effectiveTime,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Retrieve the list of server metadata elements with a matching deployed impl type.
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
    List<SoftwareServerElement> getSoftwareServersByDeployedImplType(String userId,
                                                                     String name,
                                                                     Date   effectiveTime,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Retrieve the list of software servers created by this caller.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param effectiveTime time that the element is effective
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SoftwareServerElement> getSoftwareServersForInfrastructureManager(String userId,
                                                                           String infrastructureManagerGUID,
                                                                           String infrastructureManagerName,
                                                                           Date   effectiveTime,
                                                                           int    startFrom,
                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException;


    /**
     * Retrieve the softwareServer metadata element with the supplied unique identifier.
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
    SoftwareServerElement getSoftwareServerByGUID(String userId,
                                                  String guid) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


}
