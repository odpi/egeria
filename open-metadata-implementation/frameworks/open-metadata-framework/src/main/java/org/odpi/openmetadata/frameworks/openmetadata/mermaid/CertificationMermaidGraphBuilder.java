/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CertificationElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;
import java.util.UUID;


/**
 * Creates a graph for a collection of certification relationships linked to a single starting point
 * and the elements at the other end of the relationship.
 */
public class CertificationMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
     *
     * @param startingElement contents
     */
    public CertificationMermaidGraphBuilder(MetadataElementSummary     startingElement,
                                            List<CertificationElement> certifications)
    {
        String startingNodeId = startingElement.getElementHeader().getGUID();
        String currentDisplayName = super.getNodeDisplayName(startingElement);

        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Certifications - ");
        mermaidGraph.append(currentDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(startingNodeId);
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        super.appendNewMermaidNode(startingNodeId,
                                   currentDisplayName,
                                   this.getTypeNameForEntity(startingElement.getElementHeader()),
                                   getVisualStyleForEntity(startingElement.getElementHeader(), VisualStyle.GOVERNED_ELEMENT));

        this.addDescription(startingElement);

        if (certifications != null)
        {
            for (CertificationElement certificationElement : certifications)
            {
                if (certificationElement != null)
                {
                    super.addRelatedElementSummary(certificationElement,
                                                   super.getVisualStyleForEntityType(certificationElement.getRelatedElement().getElementHeader(), VisualStyle.GOVERNED_ELEMENT),
                                                   startingNodeId);

                    if (certificationElement.getCertifiedBy() != null)
                    {
                        super.appendNewMermaidNode(certificationElement.getCertifiedBy(),
                                                   VisualStyle.GOVERNANCE_ACTOR);

                        super.appendMermaidThinLine(null,
                                                    certificationElement.getRelatedElement().getElementHeader().getGUID(),
                                                    OpenMetadataProperty.CERTIFIED_BY.name,
                                                    certificationElement.getCertifiedBy().getElementHeader().getGUID());
                    }

                    if (certificationElement.getCustodian() != null)
                    {
                        super.appendNewMermaidNode(certificationElement.getCustodian(),
                                                   VisualStyle.GOVERNANCE_ACTOR);

                        super.appendMermaidThinLine(null,
                                                    certificationElement.getRelatedElement().getElementHeader().getGUID(),
                                                    OpenMetadataProperty.CUSTODIAN.name,
                                                    certificationElement.getCustodian().getElementHeader().getGUID());
                    }

                    if (certificationElement.getRecipient() != null)
                    {
                        super.appendNewMermaidNode(certificationElement.getRecipient(),
                                                   VisualStyle.GOVERNANCE_ACTOR);

                        super.appendMermaidThinLine(null,
                                                    certificationElement.getRelatedElement().getElementHeader().getGUID(),
                                                    OpenMetadataProperty.RECIPIENT.name,
                                                    certificationElement.getRecipient().getElementHeader().getGUID());
                    }
                }
            }
        }
    }


    /**
     * Add a text boxes with the description of the process (if any)
     *
     * @param metadataElementSummary element with the potential description
     */
    private void addDescription(MetadataElementSummary metadataElementSummary)
    {
        String lastNodeName = null;

        if (metadataElementSummary.getProperties() != null)
        {
            if (metadataElementSummary.getProperties().get(OpenMetadataProperty.TITLE.name) != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     metadataElementSummary.getProperties().get(OpenMetadataProperty.TITLE.name),
                                     "Title",
                                     VisualStyle.DESCRIPTION);

                lastNodeName = descriptionNodeName;
            }

            if (metadataElementSummary.getProperties().get(OpenMetadataProperty.SUMMARY.name) != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     metadataElementSummary.getProperties().get(OpenMetadataProperty.SUMMARY.name),
                                     "Summary",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (metadataElementSummary.getProperties().get(OpenMetadataProperty.DESCRIPTION.name) != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     metadataElementSummary.getProperties().get(OpenMetadataProperty.DESCRIPTION.name),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }
            }

            if (metadataElementSummary.getProperties().get(OpenMetadataProperty.START.name) != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     metadataElementSummary.getProperties().get(OpenMetadataProperty.START.name),
                                     "Start Date",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }
            }

            if (metadataElementSummary.getProperties().get(OpenMetadataProperty.END.name) != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     metadataElementSummary.getProperties().get(OpenMetadataProperty.END.name),
                                     "End Date",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    super.appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }
            }
        }
    }
}
