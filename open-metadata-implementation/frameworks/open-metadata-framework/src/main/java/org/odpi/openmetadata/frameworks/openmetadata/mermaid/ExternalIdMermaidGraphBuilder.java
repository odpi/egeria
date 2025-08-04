/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ExternalIdElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's external id graph.
 */
public class ExternalIdMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param externalIdElement content
     */
    public ExternalIdMermaidGraphBuilder(ExternalIdElement externalIdElement)
    {
        super(externalIdElement);

        this.addRelatedElementSummaries(externalIdElement.getExternalIdScope(),
                                        VisualStyle.LINKED_ELEMENT,
                                        externalIdElement.getElementHeader().getGUID());
    }
}
