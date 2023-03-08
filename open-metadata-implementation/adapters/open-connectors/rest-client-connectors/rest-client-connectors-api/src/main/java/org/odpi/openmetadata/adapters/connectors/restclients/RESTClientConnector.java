/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;

public abstract class RESTClientConnector extends ConnectorBase implements RESTClientCalls
{
    /**
     * Default constructor
     */
    public RESTClientConnector()
    {
        super();
    }
}
