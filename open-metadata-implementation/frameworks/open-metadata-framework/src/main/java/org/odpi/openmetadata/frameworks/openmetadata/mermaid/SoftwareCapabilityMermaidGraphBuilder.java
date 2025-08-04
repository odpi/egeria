/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SoftwareCapabilityElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset graph.
 */
public class SoftwareCapabilityMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param softwareCapabilityElement content
     */
    public SoftwareCapabilityMermaidGraphBuilder(SoftwareCapabilityElement softwareCapabilityElement)
    {
        super(softwareCapabilityElement);

        this.addRelatedElementSummaries(softwareCapabilityElement.getAssetUse(),
                                        VisualStyle.LINKED_ELEMENT,
                                        softwareCapabilityElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(softwareCapabilityElement.getHostedBy(),
                                        VisualStyle.LINKED_ELEMENT,
                                        softwareCapabilityElement.getElementHeader().getGUID());
    }
}
