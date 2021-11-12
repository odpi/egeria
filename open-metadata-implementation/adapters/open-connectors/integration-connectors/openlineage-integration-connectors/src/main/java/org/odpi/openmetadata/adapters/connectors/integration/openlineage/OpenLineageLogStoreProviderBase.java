/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * The OpenLineageLogStoreProviderBase provides a base class for the connector provider supporting open lineage
 * log stores.  It extends ConnectorProviderBase which does the creation of connector instances.
 * The subclasses of OpenLineageLogStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the open lineage log connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class OpenLineageLogStoreProviderBase extends ConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public OpenLineageLogStoreProviderBase()
    {
        /*
         * Nothing to do
         */
    }

}

