/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's data class graph.
 */
public class GlossaryCategoryMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param glossaryCategoryElement content
     */
    public GlossaryCategoryMermaidGraphBuilder(GlossaryCategoryElement glossaryCategoryElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Glossary Category - ");
        mermaidGraph.append(glossaryCategoryElement.getGlossaryCategoryProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(glossaryCategoryElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if (glossaryCategoryElement != null)
        {
            String currentDisplayName = glossaryCategoryElement.getGlossaryCategoryProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = glossaryCategoryElement.getGlossaryCategoryProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(glossaryCategoryElement.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       glossaryCategoryElement.getElementHeader().getType().getTypeName(),
                                       checkForClassifications(glossaryCategoryElement.getElementHeader(), VisualStyle.GLOSSARY_CATEGORY));

            this.addDescription(glossaryCategoryElement);

            super.addRelatedToElementSummaries(glossaryCategoryElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, glossaryCategoryElement.getElementHeader().getGUID());
            super.addRelatedToElementSummaries(glossaryCategoryElement.getChildCategories(), VisualStyle.GLOSSARY_CATEGORY, glossaryCategoryElement.getElementHeader().getGUID());
            super.addRelatedToElementSummaries(glossaryCategoryElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, glossaryCategoryElement.getElementHeader().getGUID());

            if (glossaryCategoryElement.getTerms() != null)
            {
                /*
                 * Build a grid of elements in a subgraph
                 */
                super.startSubgraph("Terms", VisualStyle.DESCRIPTION);

                int      nodePointer = 0;
                String[] nodeIds = new String[]{ null, null, null, null, null, null, null, null, null, null };

                for (RelatedMetadataElementSummary term : glossaryCategoryElement.getTerms())
                {
                    appendNewMermaidNode(term.getRelatedElement().getElementHeader().getGUID(),
                                         super.getNodeDisplayName(term.getRelatedElement()),
                                         super.addSpacesToTypeName(term.getRelatedElement().getElementHeader().getType().getTypeName()),
                                         super.checkForClassifications(term.getRelatedElement().getElementHeader(), VisualStyle.GLOSSARY_TERM));

                    if (nodeIds[nodePointer] != null)
                    {
                        appendInvisibleMermaidLine(nodeIds[nodePointer], term.getRelatedElement().getElementHeader().getGUID());
                    }

                    nodeIds[nodePointer] = term.getRelatedElement().getElementHeader().getGUID();

                    if (nodePointer == 9)
                    {
                        nodePointer = 0;
                    }
                    else
                    {
                        nodePointer++;
                    }
                }

                super.endSubgraph();

                appendMermaidDottedLine(null, glossaryCategoryElement.getElementHeader().getGUID(), null, "Terms");
            }
        }
    }


    /**
     * Add a text box with the description (if any)
     *
     * @param glossaryCategoryElement element with the potential description
     */
    private void addDescription(GlossaryCategoryElement glossaryCategoryElement)
    {
        if (glossaryCategoryElement.getGlossaryCategoryProperties() != null)
        {
            if (glossaryCategoryElement.getGlossaryCategoryProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryCategoryElement.getGlossaryCategoryProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                appendInvisibleMermaidLine(descriptionNodeName, glossaryCategoryElement.getElementHeader().getGUID());
            }

            String nextNodeName = null;
            String lineGUID     = null;

            if (glossaryCategoryElement.getParentGlossary() != null)
            {
                nextNodeName = glossaryCategoryElement.getParentGlossary().getRelatedElement().getElementHeader().getGUID();
                lineGUID     = glossaryCategoryElement.getParentGlossary().getRelationshipHeader().getGUID();

                String glossaryDisplayName = glossaryCategoryElement.getParentGlossary().getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                if (glossaryDisplayName == null)
                {
                    glossaryDisplayName = glossaryCategoryElement.getParentGlossary().getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                }

                appendNewMermaidNode(nextNodeName,
                                     glossaryDisplayName,
                                     OpenMetadataType.GLOSSARY.typeName,
                                     VisualStyle.GLOSSARY);
            }

            if (glossaryCategoryElement.getParentCategory() != null)
            {
                String parentCategoryNodeName = glossaryCategoryElement.getParentCategory().getRelatedElement().getElementHeader().getGUID();

                String categoryDisplayName = glossaryCategoryElement.getParentCategory().getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                if (categoryDisplayName == null)
                {
                    categoryDisplayName = glossaryCategoryElement.getParentCategory().getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                }

                appendNewMermaidNode(parentCategoryNodeName,
                                     categoryDisplayName,
                                     OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                     VisualStyle.GLOSSARY_CATEGORY);

                if (nextNodeName != null)
                {
                    appendMermaidDottedLine(lineGUID,
                                            nextNodeName,
                                            null,
                                            parentCategoryNodeName);
                }

                nextNodeName = parentCategoryNodeName;
                lineGUID = glossaryCategoryElement.getParentCategory().getRelationshipHeader().getGUID();
            }

            if (nextNodeName != null)
            {
                appendMermaidDottedLine(lineGUID,
                                        nextNodeName,
                                        null,
                                        glossaryCategoryElement.getElementHeader().getGUID());
            }
        }
    }
}
