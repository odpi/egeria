/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * OpenLineageLogStore is the specialized data API for an Open Lineage Log Store destination connector.
 */
public interface OpenLineageLogStore
{
    /**
     * Store the open lineage event in the open lineage log store.  If the raw event is null, a json version of the open lineage event is
     * generated using the Egeria beans.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     * @param destinationAddress destination address
     *
     * @throws InvalidParameterException indicates that the openLineageEvent parameter is invalid.
     * @throws UserNotAuthorizedException indicates that the caller is not authorized to access the log store.
     * @throws PropertyServerException  indicates that the  log store is not available or has an error.
     */
    void storeEvent(OpenLineageRunEvent openLineageEvent,
                    String              rawEvent,
                    String              destinationAddress) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;

}
