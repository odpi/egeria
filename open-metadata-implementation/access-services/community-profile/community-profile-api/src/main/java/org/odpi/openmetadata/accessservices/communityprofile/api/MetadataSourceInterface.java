/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * MetadataSourceInterface is the interface used to define information about the third party technologies that
 * an integration daemon is extracting metadata from.
 *
 * These technologies are represented by a software server capability in open metadata with an appropriate
 * classification.
 */
public interface MetadataSourceInterface
{
    /**
     * Create information about the metadata source that is providing user profile information.
     *
     * @param userId calling user
     * @param properties description of the metadata source
     *
     * @return unique identifier of the user profile manager's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  createMetadataSource(String                   userId,
                                 MetadataSourceProperties properties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;

    /**
     * Retrieve the unique identifier of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String getMetadataSourceGUID(String userId,
                                 String qualifiedName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Retrieve the properties of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    MetadataSourceElement getMetadataSource(String userId,
                                            String metadataSourceGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;

    /**
     * Update classification of the metadata source as being capable if managing user profiles.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void  addUserProfileManagerClassification(String userId,
                                              String metadataSourceGUID) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Update classification of the metadata source that is providing a user access directory information
     * such as the groups and access rights of a user Id.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     **
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void  addUserAccessDirectoryClassification(String userId,
                                               String metadataSourceGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Update classification of the metadata source that is a master data manager for user profile information.
     *
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void addMasterDataManagerClassification(String userId,
                                            String metadataSourceGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;
}
