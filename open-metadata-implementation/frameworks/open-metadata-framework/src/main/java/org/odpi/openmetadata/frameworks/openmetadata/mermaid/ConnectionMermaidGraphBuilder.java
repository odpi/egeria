/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectionElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's connection graph.
 */
public class ConnectionMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param connectionElement content
     */
    public ConnectionMermaidGraphBuilder(ConnectionElement connectionElement)
    {
        super(connectionElement);

        addRelatedElementSummary(connectionElement.getConnectorType(), VisualStyle.LINKED_ELEMENT, connectionElement.getElementHeader().getGUID(), LineStyle.NORMAL);
        addRelatedElementSummary(connectionElement.getEndpoint(), VisualStyle.LINKED_ELEMENT, connectionElement.getElementHeader().getGUID(), LineStyle.NORMAL);

        this.addRelatedElementSummaries(connectionElement.getEmbeddedConnections(),
                                        VisualStyle.LINKED_ELEMENT,
                                        connectionElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(connectionElement.getAssets(),
                                        VisualStyle.LINKED_ELEMENT,
                                        connectionElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(connectionElement.getParentConnections(),
                                        VisualStyle.LINKED_ELEMENT,
                                        connectionElement.getElementHeader().getGUID());
    }
}
