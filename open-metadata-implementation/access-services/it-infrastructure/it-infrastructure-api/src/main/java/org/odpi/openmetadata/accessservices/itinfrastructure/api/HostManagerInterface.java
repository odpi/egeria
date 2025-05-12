/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.HostElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * HostManagerInterface defines the client side interface for the IT Infrastructure OMAS that is
 * relevant for host assets.   It provides the ability to
 * define and maintain the metadata about a host and relationships between hosts.
 */
public interface HostManagerInterface
{
    /**
     * Create a new metadata element to represent a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the host be marked as owned by the infrastructure manager so others can not update?
     * @param hostProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createHost(String         userId,
                      String         infrastructureManagerGUID,
                      String         infrastructureManagerName,
                      boolean        infrastructureManagerIsHome,
                      HostProperties hostProperties) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Create a new metadata element to represent a host using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the host be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createHostFromTemplate(String             userId,
                                  String             infrastructureManagerGUID,
                                  String             infrastructureManagerName,
                                  boolean            infrastructureManagerIsHome,
                                  String             templateGUID,
                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Update the metadata element representing a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param hostProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateHost(String         userId,
                    String         infrastructureManagerGUID,
                    String         infrastructureManagerName,
                    String         hostGUID,
                    boolean        isMergeUpdate,
                    HostProperties hostProperties) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Create a relationship between a host and an cluster member host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param hostGUID unique identifier of the host
     * @param clusterMemberGUID unique identifier of the cluster member host 
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupClusterMember(String  userId,
                            String  infrastructureManagerGUID,
                            String  infrastructureManagerName,
                            boolean infrastructureManagerIsHome,
                            String  hostGUID,
                            String  clusterMemberGUID,
                            Date    effectiveFrom,
                            Date    effectiveTo) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Remove a relationship between a host and an cluster member host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the host 
     * @param clusterMemberGUID unique identifier of the cluster member host 
     * @param effectiveTime time when the deployment is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearClusterMember(String userId,
                            String infrastructureManagerGUID,
                            String infrastructureManagerName,
                            String hostGUID,
                            String clusterMemberGUID,
                            Date   effectiveTime) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;



    /**
     * Update the zones for the host asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishHost(String userId,
                     String hostGUID) throws InvalidParameterException,
                                             UserNotAuthorizedException,
                                             PropertyServerException;


    /**
     * Update the zones for the host asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the host is first created).
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawHost(String userId,
                      String hostGUID) throws InvalidParameterException,
                                              UserNotAuthorizedException,
                                              PropertyServerException;


    /**
     * Remove the metadata element representing a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeHost(String userId,
                    String infrastructureManagerGUID,
                    String infrastructureManagerName,
                    String hostGUID) throws InvalidParameterException,
                                            UserNotAuthorizedException,
                                            PropertyServerException;



    /**
     * Retrieve the list of host metadata elements that contain the search string.
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
    List<HostElement> findHosts(String userId,
                                String searchString,
                                Date   effectiveTime,
                                int    startFrom,
                                int    pageSize) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Retrieve the list of host metadata elements with a matching qualified or display name.
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
    List<HostElement> getHostsByName(String userId,
                                     String name,
                                     Date   effectiveTime,
                                     int    startFrom,
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Retrieve the list of hosts created by this caller.
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
    List<HostElement> getHostsForInfrastructureManager(String userId,
                                                       String infrastructureManagerGUID,
                                                       String infrastructureManagerName,
                                                       Date   effectiveTime,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve the host metadata element with the supplied unique identifier.
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
    HostElement getHostByGUID(String userId,
                              String guid) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;

    /**
     * Return the list of cluster members associated with a host.
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the host to query
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
    List<HostElement> getClusterMembersForHost(String userId,
                                               String hostGUID,
                                               Date   effectiveTime,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;
}
