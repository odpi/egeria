/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElementStub;

import java.util.UUID;


public class GovernanceDefinitionMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param governanceDefinitionGraph contents
     */
    public GovernanceDefinitionMermaidGraphBuilder(GovernanceDefinitionGraph governanceDefinitionGraph)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Governance Definition - ");
        mermaidGraph.append(governanceDefinitionGraph.getProperties().getTitle());
        mermaidGraph.append(" [");
        mermaidGraph.append(governanceDefinitionGraph.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName = governanceDefinitionGraph.getElementHeader().getGUID();
        String currentDisplayName = governanceDefinitionGraph.getProperties().getTitle();

        appendNewMermaidNode(currentNodeName,
                             currentDisplayName,
                             governanceDefinitionGraph.getElementHeader().getType().getTypeName(),
                             getVisualStyleForEntity(governanceDefinitionGraph.getElementHeader(), VisualStyle.GOVERNANCE_DEFINITION));

        this.addDescription(governanceDefinitionGraph);

        if (governanceDefinitionGraph.getParents() != null)
        {
            for (RelatedElementStub parent : governanceDefinitionGraph.getParents())
            {
                currentNodeName    = parent.getRelatedElement().getGUID();
                currentDisplayName = parent.getRelatedElement().getUniqueName();

                appendNewMermaidNode(currentNodeName,
                                     currentDisplayName,
                                     parent.getRelatedElement().getType().getTypeName(),
                                     getVisualStyleForEntity(parent.getRelatedElement(), VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION));


                super.appendMermaidLine(parent.getRelationshipHeader().getGUID(),
                                        parent.getRelatedElement().getGUID(),
                                        parent.getRelationshipHeader().getType().getTypeName(),
                                        governanceDefinitionGraph.getElementHeader().getGUID());

            }
        }

        if (governanceDefinitionGraph.getPeers() != null)
        {
            for (RelatedElementStub peer : governanceDefinitionGraph.getPeers())
            {
                if (peer != null)
                {
                    currentNodeName    = peer.getRelatedElement().getGUID();
                    currentDisplayName = peer.getRelatedElement().getUniqueName();

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         peer.getRelatedElement().getType().getTypeName(),
                                         getVisualStyleForEntity(peer.getRelatedElement(), VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION));

                    super.appendMermaidThinLine(peer.getRelationshipHeader().getGUID(),
                                                peer.getRelatedElement().getGUID(),
                                                peer.getRelationshipHeader().getType().getTypeName(),
                                                governanceDefinitionGraph.getElementHeader().getGUID());
                }
            }
        }

        if (governanceDefinitionGraph.getChildren() != null)
        {
            for (RelatedElementStub child : governanceDefinitionGraph.getChildren())
            {
                if (child != null)
                {
                    currentNodeName    = child.getRelatedElement().getGUID();
                    currentDisplayName = child.getRelatedElement().getUniqueName();

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         child.getRelatedElement().getType().getTypeName(),
                                         getVisualStyleForEntity(child.getRelatedElement(), VisualStyle.SUPPORTING_GOVERNANCE_DEFINITION));

                    super.appendMermaidLine(child.getRelationshipHeader().getGUID(),
                                            governanceDefinitionGraph.getElementHeader().getGUID(),
                                            child.getRelationshipHeader().getType().getTypeName(),
                                            child.getRelatedElement().getGUID());
                }
            }
        }

        if (governanceDefinitionGraph.getMetrics() != null)
        {
            for (RelatedElementStub metrics : governanceDefinitionGraph.getMetrics())
            {
                if (metrics != null)
                {
                    currentNodeName    = metrics.getRelatedElement().getGUID();
                    currentDisplayName = metrics.getRelatedElement().getUniqueName();

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         metrics.getRelatedElement().getType().getTypeName(),
                                         getVisualStyleForEntity(metrics.getRelatedElement(), VisualStyle.GOVERNANCE_METRIC));

                    super.appendMermaidLine(metrics.getRelationshipHeader().getGUID(),
                                            governanceDefinitionGraph.getElementHeader().getGUID(),
                                            metrics.getRelationshipHeader().getType().getTypeName(),
                                            metrics.getRelatedElement().getGUID());
                }
            }
        }

        if (governanceDefinitionGraph.getExternalReferences() != null)
        {
            for (RelatedElementStub externalReference : governanceDefinitionGraph.getExternalReferences())
            {
                if (externalReference != null)
                {
                    currentNodeName    = externalReference.getRelatedElement().getGUID();
                    currentDisplayName = externalReference.getRelatedElement().getUniqueName();

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         externalReference.getRelatedElement().getType().getTypeName(),
                                         getVisualStyleForEntity(externalReference.getRelatedElement(), VisualStyle.EXTERNAL_REFERENCE));

                    super.appendMermaidLine(externalReference.getRelationshipHeader().getGUID(),
                                            governanceDefinitionGraph.getElementHeader().getGUID(),
                                            externalReference.getRelationshipHeader().getType().getTypeName(),
                                            externalReference.getRelatedElement().getGUID());
                }
            }
        }

        if (governanceDefinitionGraph.getOthers() != null)
        {
            for (RelatedElementStub other : governanceDefinitionGraph.getOthers())
            {
                if (other != null)
                {
                    currentNodeName    = other.getRelatedElement().getGUID();
                    currentDisplayName = other.getRelatedElement().getUniqueName();

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         other.getRelatedElement().getType().getTypeName(),
                                         getVisualStyleForEntity(other.getRelatedElement(), VisualStyle.GOVERNED_ELEMENT));

                    super.appendMermaidLine(other.getRelationshipHeader().getGUID(),
                                            governanceDefinitionGraph.getElementHeader().getGUID(),
                                            other.getRelationshipHeader().getType().getTypeName(),
                                            other.getRelatedElement().getGUID());
                }
            }
        }
    }


    /**
     * Add a text boxes with the description of the process (if any)
     *
     * @param governanceDefinitionElement element with the potential description
     */
    private void addDescription(GovernanceDefinitionElement governanceDefinitionElement)
    {
        if (governanceDefinitionElement.getProperties() != null)
        {
            String lastNodeName = null;

            if (governanceDefinitionElement.getProperties().getSummary() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getSummary(),
                                     "Summary",
                                     VisualStyle.DESCRIPTION);

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getScope() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getScope(),
                                     "Scope",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getImportance() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     governanceDefinitionElement.getProperties().getImportance(),
                                     "Importance",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (governanceDefinitionElement.getProperties().getImplications() != null)
            {
                for (String implication : governanceDefinitionElement.getProperties().getImplications())
                {
                    String descriptionNodeName = UUID.randomUUID().toString();

                    appendNewMermaidNode(descriptionNodeName,
                                         implication,
                                         "Implication",
                                         VisualStyle.DESCRIPTION);

                    if (lastNodeName != null)
                    {
                        super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                    }

                    lastNodeName = descriptionNodeName;
                }
            }

            if (governanceDefinitionElement.getProperties().getOutcomes() != null)
            {
                for (String outcome : governanceDefinitionElement.getProperties().getOutcomes())
                {
                    String descriptionNodeName = UUID.randomUUID().toString();

                    appendNewMermaidNode(descriptionNodeName,
                                         outcome,
                                         "Implication",
                                         VisualStyle.DESCRIPTION);

                    if (lastNodeName != null)
                    {
                        super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                    }

                    lastNodeName = descriptionNodeName;
                }
            }

            if (governanceDefinitionElement.getProperties().getResults() != null)
            {
                for (String result : governanceDefinitionElement.getProperties().getResults())
                {
                    String descriptionNodeName = UUID.randomUUID().toString();

                    appendNewMermaidNode(descriptionNodeName,
                                         result,
                                         "Result",
                                         VisualStyle.DESCRIPTION);

                    if (lastNodeName != null)
                    {
                        super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                    }

                    lastNodeName = descriptionNodeName;
                }
            }
        }
    }
}
