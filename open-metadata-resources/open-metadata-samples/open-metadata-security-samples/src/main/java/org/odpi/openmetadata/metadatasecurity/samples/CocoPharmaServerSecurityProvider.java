/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityProvider;

/**
 * CocoPharmaServerSecurityProvider is the connector provider to the
 * sample server security connector for the Coco Pharmaceuticals scenarios.
 */
public class CocoPharmaServerSecurityProvider extends OpenMetadataServerSecurityProvider
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public CocoPharmaServerSecurityProvider()
    {
        super();

        Class<?>        connectorClass = CocoPharmaServerSecurityConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
