/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.events;

import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * OpenMetadataEventInterface defines how a client gets access to the
 * configuration events produced by the OMF Services.
 */
public interface OpenMetadataEventInterface
{
    /**
     * Register a listener object that will be passed each of the events published by the OMF Services.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void registerListener(String                    userId,
                          OpenMetadataEventListener listener) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;
}
