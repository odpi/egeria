/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.securitymanager.api;


import org.odpi.openmetadata.accessservices.securitymanager.properties.SecurityManagerProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * MetadataSourceInterface is the interface used to define information about the third party technologies that
 * an integration daemon is extracting metadata from.
 *
 * These technologies are represented by a software capability in open metadata.
 */
public interface MetadataSourceInterface
{
    /**
     * Create information about a security manager such as a user access directory - such as an LDAP server or access control manager
     * such as Apache Ranger.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param typeName type name for the software server capability
     * @param securityManagerProperties description of the security manager
     *
     * @return unique identifier of the user directory's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String createExternalSecurityManager(String                    userId,
                                         String                    externalSourceGUID,
                                         String                    externalSourceName,
                                         String                    typeName,
                                         SecurityManagerProperties securityManagerProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Retrieve the unique identifier of the external security manager.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  getExternalSecurityManagerGUID(String  userId,
                                           String  qualifiedName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;
}
