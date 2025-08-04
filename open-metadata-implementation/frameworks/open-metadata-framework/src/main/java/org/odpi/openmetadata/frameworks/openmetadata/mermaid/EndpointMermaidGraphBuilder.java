/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's endpoint graph.
 */
public class EndpointMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param endpointElement content
     */
    public EndpointMermaidGraphBuilder(EndpointElement endpointElement)
    {
        super(endpointElement);

        addRelatedElementSummary(endpointElement.getServerEndpoint(), VisualStyle.LINKED_ELEMENT, endpointElement.getElementHeader().getGUID(), LineStyle.NORMAL);

        this.addRelatedElementSummaries(endpointElement.getDeployedAPIs(),
                                        VisualStyle.LINKED_ELEMENT,
                                        endpointElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(endpointElement.getConnections(),
                                        VisualStyle.LINKED_ELEMENT,
                                        endpointElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(endpointElement.getVisibleInNetworks(),
                                        VisualStyle.LINKED_ELEMENT,
                                        endpointElement.getElementHeader().getGUID());
    }
}
