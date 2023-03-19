/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnalysisReportStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryPipeline;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;

/**
 * SequentialDiscoveryPipeline is a discovery pipeline that provides an inline sequential invocation of the supplied discovery services.
 */
public class SequentialDiscoveryPipeline extends DiscoveryPipeline
{
    /**
     * This implementation provides an inline sequential invocation of the supplied discovery services.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    protected void runDiscoveryPipeline() throws ConnectorCheckedException
    {
        DiscoveryAnalysisReportStore discoveryReport = super.discoveryContext.getAnnotationStore().getDiscoveryReport();

        for (DiscoveryService embeddedDiscoveryService : super.embeddedDiscoveryServices)
        {
            if (embeddedDiscoveryService != null)
            {
                embeddedDiscoveryService.setDiscoveryContext(super.discoveryContext);
                discoveryReport.setAnalysisStep(embeddedDiscoveryService.getConnectorInstanceId());
                embeddedDiscoveryService.start();
                embeddedDiscoveryService.disconnect();
            }
        }
    }
}
