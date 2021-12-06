/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.securitymanager.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

/**
 * SecurityManagerEventInterface defines how a client gets access to the
 * configuration events produced by the Security Manager OMAS.
 */
public interface SecurityManagerEventInterface
{
    /**
     * Register a listener object that will be passed each of the events published by
     * the Security Manager OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void registerListener(String                       userId,
                          SecurityManagerEventListener listener) throws InvalidParameterException,
                                                                        ConnectionCheckedException,
                                                                        ConnectorCheckedException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException;
}
