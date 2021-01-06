/* SPDX-License-Identifier: Apache 2.0 */
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
    protected List<Connector>        embeddedConnectors = null;
    protected List<DiscoveryService> embeddedDiscoveryServices = null;


    /**
     * Set up the list of discovery services connectors that will be invoked as part of this discovery pipeline.
     *
     * The connectors are initialized waiting to start.  After start() is called on the
     * discovery pipeline, it will choreograph the invocation of its embedded discovery services by calling
     * start() to each of them when they are to run. Similarly for disconnect().
     *
     * @param embeddedConnectors  list of embedded connectors that are hopefully discovery services
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


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

        embeddedDiscoveryServices = getEmbeddedDiscoveryServices(embeddedConnectors);

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
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
