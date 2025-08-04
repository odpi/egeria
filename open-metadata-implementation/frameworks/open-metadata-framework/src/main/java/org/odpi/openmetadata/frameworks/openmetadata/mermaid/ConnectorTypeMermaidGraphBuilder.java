/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectorTypeElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's endpoint graph.
 */
public class ConnectorTypeMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param connectorTypeElement content
     */
    public ConnectorTypeMermaidGraphBuilder(ConnectorTypeElement connectorTypeElement)
    {
        super(connectorTypeElement);

        this.addRelatedElementSummaries(connectorTypeElement.getConnections(),
                                        VisualStyle.LINKED_ELEMENT,
                                        connectorTypeElement.getElementHeader().getGUID());
    }
}
