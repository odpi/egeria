/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * SecurityManagerInterface provides the interface for retrieving the identity of an external security manager.
 * It is used when the security manager itself is able to call the Security Manager OMAS directly.
 * The definition of the external security manager is created using the IT Infrastructure OMAS.
 */
public interface SecurityManagerInterface
{
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
