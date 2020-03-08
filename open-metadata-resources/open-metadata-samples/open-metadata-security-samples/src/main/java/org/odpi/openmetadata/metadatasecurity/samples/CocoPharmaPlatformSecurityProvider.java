/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataPlatformSecurityProvider;

/**
 * CocoPharmaPlatformSecurityProvider is the connector provider to the
 * sample platform security connector for the Coco Pharmaceuticals scenarios.
 */
public class CocoPharmaPlatformSecurityProvider extends OpenMetadataPlatformSecurityProvider
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public CocoPharmaPlatformSecurityProvider()
    {
        super();

        Class<?>   connectorClass = CocoPharmaPlatformSecurityConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
