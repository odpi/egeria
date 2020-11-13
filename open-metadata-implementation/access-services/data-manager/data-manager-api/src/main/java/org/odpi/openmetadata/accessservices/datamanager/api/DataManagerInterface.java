/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataManagerInterface provides the interface for retrieving the identity of an external data manager.
 * It is used when the data manager itself is able to call the Data Manager OMAS directly.
 * The definition of the external data manager is created using the IT Infrastructure OMAS.
 */
public interface DataManagerInterface
{
    /**
     * Retrieve the unique identifier of the external data manager.
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
    String  getExternalDataManagerGUID(String  userId,
                                       String  qualifiedName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;
}
