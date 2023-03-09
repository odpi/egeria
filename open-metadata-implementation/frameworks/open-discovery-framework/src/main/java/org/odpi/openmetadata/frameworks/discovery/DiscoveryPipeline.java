/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryServiceException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.ODFErrorCode;

import java.util.List;

/**
 * DiscoveryPipeline is a discovery service that is responsible for choreographing the discovery services
 * passed on initializeEmbeddedConnectors.
 */
public abstract class DiscoveryPipeline extends DiscoveryService implements VirtualConnectorExtension
{
    protected List<DiscoveryService> embeddedDiscoveryServices = null;


    /**
     * Start the pipeline.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName   = "start";

        /*
         * Check that the discovery context is not null and anything else is set up correctly
         */
        super.start();

        embeddedDiscoveryServices = getEmbeddedDiscoveryServices();

        if (embeddedDiscoveryServices == null)
        {
            throw new DiscoveryServiceException(ODFErrorCode.NO_EMBEDDED_DISCOVERY_SERVICES.getMessageDefinition(discoveryServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }

        runDiscoveryPipeline();
    }


    /**
     * This implementation provides an inline sequential invocation of the supplied discovery services.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    protected abstract void runDiscoveryPipeline() throws ConnectorCheckedException;


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
