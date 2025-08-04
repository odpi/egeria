/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's asset graph.
 */
public class AssetMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param assetElement content
     */
    public AssetMermaidGraphBuilder(AssetElement assetElement)
    {
        super(assetElement);

        addRelatedElementSummary(assetElement.getRootSchemaType(), VisualStyle.LINKED_ELEMENT, assetElement.getElementHeader().getGUID(), LineStyle.NORMAL);

        this.addRelatedElementSummaries(assetElement.getConnections(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getHostedITAssets(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getDeployedOn(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getSupportedSoftwareCapabilities(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getSupportedDataSets(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getDataSetContent(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getVisibleEndpoint(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getApiEndpoints(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getAttachedStorage(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getParentProcesses(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getChildProcesses(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getPorts(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        addRelatedElementSummary(assetElement.getHomeFolder(), VisualStyle.LINKED_ELEMENT, assetElement.getElementHeader().getGUID(), LineStyle.NORMAL);

        this.addRelatedElementSummaries(assetElement.getNestedFiles(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getLinkedFiles(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getLinkedFolders(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getLinkedMediaFiles(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getTopicSubscribers(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getAssociatedLogs(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        this.addRelatedElementSummaries(assetElement.getAssociatedLogSubjects(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());

        addRelatedElementSummary(assetElement.getCohortMember(), VisualStyle.LINKED_ELEMENT, assetElement.getElementHeader().getGUID(), LineStyle.NORMAL);

        this.addRelatedElementSummaries(assetElement.getArchiveContents(),
                                        VisualStyle.LINKED_ELEMENT,
                                        assetElement.getElementHeader().getGUID());
    }
}
