/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareServerCapabilityElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareServerCapabilityProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * SoftwareServerCapabilityManagerInterface defines the client side interface for the IT Infrastructure OMAS that is
 * relevant for cataloguing software server capabilities.   It provides the ability to
 * define and maintain the metadata about a software server capability and the assets it interacts with.
 */
public interface SoftwareServerCapabilityManagerInterface
{
    /**
     * Create a new metadata element to represent a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param classificationName optional classification name that refines the type of the software server capability.
     * @param capabilityProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    String createSoftwareServerCapability(String                             userId,
                                          String                             infrastructureManagerGUID,
                                          String                             infrastructureManagerName,
                                          boolean                            infrastructureManagerIsHome,
                                          String                             classificationName,
                                          SoftwareServerCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException;


    /**
     * Create a new metadata element to represent a software server capability using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    String createSoftwareServerCapabilityFromTemplate(String             userId,
                                                      String             infrastructureManagerGUID,
                                                      String             infrastructureManagerName,
                                                      boolean            infrastructureManagerIsHome,
                                                      String             templateGUID,
                                                      TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Update the metadata element representing a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param capabilityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param capabilityProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    void updateSoftwareServerCapability(String                             userId,
                                        String                             infrastructureManagerGUID,
                                        String                             infrastructureManagerName,
                                        String                             capabilityGUID,
                                        boolean                            isMergeUpdate,
                                        SoftwareServerCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;


    /**
     * Remove the metadata element representing a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param capabilityGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Deprecated
    void removeSoftwareServerCapability(String userId,
                                        String infrastructureManagerGUID,
                                        String infrastructureManagerName,
                                        String capabilityGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Retrieve the list of software server capability metadata elements that contain the search string.
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
    @Deprecated
    List<SoftwareServerCapabilityElement> findSoftwareServerCapabilities(String userId,
                                                                         String searchString,
                                                                         Date   effectiveTime,
                                                                         int    startFrom,
                                                                         int    pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Retrieve the list of software server capability metadata elements with a matching qualified or display name.
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
    @Deprecated
    List<SoftwareServerCapabilityElement> getSoftwareServerCapabilitiesByName(String userId,
                                                                              String name,
                                                                              Date   effectiveTime,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;


    /**
     * Retrieve the software server capability metadata element with the supplied unique identifier.
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
    @Deprecated
    SoftwareServerCapabilityElement getSoftwareServerCapabilityByGUID(String userId,
                                                                      String guid) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;

}
