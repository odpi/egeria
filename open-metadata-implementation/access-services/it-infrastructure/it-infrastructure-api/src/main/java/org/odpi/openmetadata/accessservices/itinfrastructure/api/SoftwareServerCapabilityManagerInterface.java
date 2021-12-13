/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ServerAssetUseElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareServerCapabilityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerCapabilityProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

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
    SoftwareServerCapabilityElement getSoftwareServerCapabilityByGUID(String userId,
                                                                      String guid) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /*
     * A software server capability works with assets
     */

    /**
     * Create a new metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param capabilityGUID unique identifier of a software server capability
     * @param assetGUID unique identifier of an asset
     * @param properties properties about the ServerAssetUse relationship
     *
     * @return unique identifier of the new ServerAssetUse relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createServerAssetUse(String                   userId,
                                String                   infrastructureManagerGUID,
                                String                   infrastructureManagerName,
                                boolean                  infrastructureManagerIsHome,
                                String                   capabilityGUID,
                                String                   assetGUID,
                                ServerAssetUseProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Update the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the ServerAssetUse relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateServerAssetUse(String                   userId,
                              String                   infrastructureManagerGUID,
                              String                   infrastructureManagerName,
                              String                   serverAssetUseGUID,
                              boolean                  isMergeUpdate,
                              ServerAssetUseProperties properties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Remove the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeServerAssetUse(String userId,
                              String infrastructureManagerGUID,
                              String infrastructureManagerName,
                              String serverAssetUseGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Return the list of server asset use relationships associated with a software server capability.
     *
     * @param userId calling user
     * @param capabilityGUID unique identifier of the software server capability to query
     * @param useType value to search for.  Null means all use types.
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ServerAssetUseElement> getServerAssetUsesForCapability(String             userId,
                                                                String             capabilityGUID,
                                                                ServerAssetUseType useType,
                                                                Date               effectiveTime,
                                                                int                startFrom,
                                                                int                pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Return the list of software server capabilities that make use of a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to query
     * @param useType Optionally restrict the search to a specific user type.  Null means all use types.
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ServerAssetUseElement> getCapabilityUsesForAsset(String             userId,
                                                          String             assetGUID,
                                                          ServerAssetUseType useType,
                                                          Date               effectiveTime,
                                                          int                startFrom,
                                                          int                pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Retrieve the list of relationships between a specific software server capability and a specific asset.
     *
     * @param userId calling user
     * @param capabilityGUID unique identifier of a software server capability
     * @param assetGUID unique identifier of an asset
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ServerAssetUseElement> getServerAssetUsesForElements(String userId,
                                                              String capabilityGUID,
                                                              String assetGUID,
                                                              Date   effectiveTime,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Retrieve the server asset use type relationship with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ServerAssetUseElement getServerAssetUseByGUID(String userId,
                                                  String guid) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;
}
