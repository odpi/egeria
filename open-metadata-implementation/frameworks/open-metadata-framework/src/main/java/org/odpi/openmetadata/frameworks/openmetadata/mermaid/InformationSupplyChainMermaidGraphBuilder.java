/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's information supply chain graph.
 */
public class InformationSupplyChainMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param informationSupplyChainElement content
     */
    public InformationSupplyChainMermaidGraphBuilder(InformationSupplyChainElement informationSupplyChainElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Information Supply Chain - ");
        mermaidGraph.append(informationSupplyChainElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(informationSupplyChainElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        appendNewMermaidNode(informationSupplyChainElement.getElementHeader().getGUID(),
                             super.getNodeDisplayName(informationSupplyChainElement.getElementHeader(), informationSupplyChainElement.getProperties()),
                             informationSupplyChainElement.getElementHeader().getType().getTypeName(),
                             VisualStyle.PRINCIPLE_INFORMATION_SUPPLY_CHAIN);

        super.addRelatedElementSummaries(informationSupplyChainElement.getParents(), VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, informationSupplyChainElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(informationSupplyChainElement.getLinks(), VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, informationSupplyChainElement.getElementHeader().getGUID());

        this.addSegments(informationSupplyChainElement.getSegments(), informationSupplyChainElement.getElementHeader().getGUID());

        if (informationSupplyChainElement.getImplementedByList() != null)
        {
            String areaName = "Design";

            super.startSubgraph(areaName, VisualStyle.SOLUTION_SUBGRAPH);

            this.addInformationSupplyChainComponents(informationSupplyChainElement.getImplementedByList(), informationSupplyChainElement.getElementHeader().getGUID());
            super.addRelatedElementSummaries(informationSupplyChainElement.getLinks(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL, informationSupplyChainElement.getElementHeader().getGUID());

            super.endSubgraph(); // design
        }

        if ((informationSupplyChainElement.getImplementation() != null) &&
                (! informationSupplyChainElement.getImplementation().isEmpty()))
        {
            String areaName = "Implementation";
            super.startSubgraph(areaName, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);

            for (RelationshipElement lineageRelationship : informationSupplyChainElement.getImplementation())
            {
                if (lineageRelationship != null)
                {
                    extractAnchorInfo(lineageRelationship.getEnd1());
                    extractAnchorInfo(lineageRelationship.getEnd2());

                    if (lineageRelationship.getEnd1().getUniqueName() != null)
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd1().getGUID(),
                                             lineageRelationship.getEnd1().getUniqueName(),
                                             lineageRelationship.getEnd1().getType().getTypeName(),
                                             getVisualStyleForEntity(lineageRelationship.getEnd1(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                    }
                    else
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd1().getGUID(),
                                             lineageRelationship.getEnd1().getGUID(),
                                             lineageRelationship.getEnd1().getType().getTypeName(),
                                             getVisualStyleForEntity(lineageRelationship.getEnd1(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                    }

                    if (lineageRelationship.getEnd2().getUniqueName() != null)
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd2().getGUID(),
                                             lineageRelationship.getEnd2().getUniqueName(),
                                             lineageRelationship.getEnd2().getType().getTypeName(),
                                             getVisualStyleForEntity(lineageRelationship.getEnd2(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                    }
                    else
                    {
                        appendNewMermaidNode(lineageRelationship.getEnd2().getGUID(),
                                             lineageRelationship.getEnd2().getGUID(),
                                             lineageRelationship.getEnd2().getType().getTypeName(),
                                             getVisualStyleForEntity(lineageRelationship.getEnd2(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL));
                    }

                    String label = super.addSpacesToTypeName(lineageRelationship.getRelationshipHeader().getType().getTypeName());

                    if ((lineageRelationship.getRelationshipProperties() != null) && (lineageRelationship.getRelationshipProperties().getExtendedProperties() != null))
                    {
                        Object labelObj = lineageRelationship.getRelationshipProperties().getExtendedProperties().get(OpenMetadataProperty.LABEL.name);

                        if (labelObj != null)
                        {
                            label = labelObj + " [" + super.addSpacesToTypeName(lineageRelationship.getRelationshipHeader().getType().getTypeName()) + "]";
                        }
                    }

                    if (propertyHelper.isTypeOf(lineageRelationship.getRelationshipHeader(), OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
                    {
                        appendMermaidThinLine(lineageRelationship.getRelationshipHeader().getGUID(),
                                              lineageRelationship.getEnd1().getGUID(),
                                              label,
                                              lineageRelationship.getEnd2().getGUID());
                    }
                    else
                    {
                        appendMermaidLine(lineageRelationship.getRelationshipHeader().getGUID(),
                                          lineageRelationship.getEnd1().getGUID(),
                                          label,
                                          lineageRelationship.getEnd2().getGUID());
                    }
                }
            }

            super.endSubgraph(); // implementation
        }

        this.addDescriptionArea(informationSupplyChainElement);
    }


    /**
     * Add the segments to the mermaid graph.
     *
     * @param segments retrieved segments
     * @param startingId related starting element id
     */
    private void addSegments(List<InformationSupplyChainSegment> segments,
                             String                              startingId)
    {
        if (segments != null)
        {
            for (InformationSupplyChainSegment segment : segments)
            {
                if (segment != null)
                {
                    super.addRelatedElementSummary(segment, VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, startingId);
                    this.addRelatedElementSummaries(segment.getLinks(), VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG, segment.getRelatedElement().getElementHeader().getGUID());
                    this.addSegments(segment.getNestedSegments(), segment.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }
    }


    /**
     * Add the implementation components to the mermaid graph.
     *
     * @param components retrieved components
     * @param startingId related starting element id
     */
    private void addInformationSupplyChainComponents(List<InformationSupplyChainComponent> components,
                                                     String                                startingId)
    {
        if (components != null)
        {
            for (InformationSupplyChainComponent component : components)
            {
                if (component != null)
                {
                    super.addRelatedElementSummary(component, VisualStyle.DEFAULT_SOLUTION_COMPONENT, startingId);

                    this.addInformationSupplyChainComponents(component.getNestedElements(), component.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }
    }


    /**
     * Add a subgraph for the text boxes with the description and purposes (if any)
     *
     * @param informationSupplyChainElement element with the potential description
     */
    private void addDescriptionArea(InformationSupplyChainElement informationSupplyChainElement)
    {
        if (informationSupplyChainElement.getProperties() != null)
        {
            String areaName = "Overview";
            super.startSubgraph(areaName, VisualStyle.DESCRIPTION);

            if (informationSupplyChainElement.getProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     informationSupplyChainElement.getProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);
            }

            if (informationSupplyChainElement.getProperties().getPurposes() != null)
            {
                for (String purpose : informationSupplyChainElement.getProperties().getPurposes())
                {
                    if (purpose != null)
                    {
                        String purposeNodeName = UUID.randomUUID().toString();

                        appendNewMermaidNode(purposeNodeName,
                                             purpose,
                                             "Purpose",
                                             VisualStyle.DESCRIPTION);
                    }
                }
            }

            super.endSubgraph(); // Overview
        }
    }
}
