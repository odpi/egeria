/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

/**
 * DataManagerEventInterface defines how a client gets access to the
 * configuration events produced by the Data Manager OMAS.
 */
public interface DataManagerEventInterface
{
    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
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
    void registerListener(String                   userId,
                          DataManagerEventListener listener) throws InvalidParameterException,
                                                                    ConnectionCheckedException,
                                                                    ConnectorCheckedException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;
}
