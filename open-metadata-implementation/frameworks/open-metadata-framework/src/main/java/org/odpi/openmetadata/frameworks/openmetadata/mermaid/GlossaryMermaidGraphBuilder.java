/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ChildCategoryElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryElement;

import java.util.List;
import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's glossary graph.
 */
public class GlossaryMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param glossaryElement content
     */
    public GlossaryMermaidGraphBuilder(GlossaryElement glossaryElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Glossary - ");
        mermaidGraph.append(glossaryElement.getGlossaryProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(glossaryElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if (glossaryElement != null)
        {
            String currentDisplayName = glossaryElement.getGlossaryProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = glossaryElement.getGlossaryProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(glossaryElement.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       glossaryElement.getElementHeader().getType().getTypeName(),
                                       getVisualStyleForEntity(glossaryElement.getElementHeader(), VisualStyle.GLOSSARY_CATEGORY));

            this.addDescription(glossaryElement);

            super.addClassifications(glossaryElement.getElementHeader());
            super.addRelatedElementSummaries(glossaryElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, glossaryElement.getElementHeader().getGUID());
            super.addRelatedElementSummaries(glossaryElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, glossaryElement.getElementHeader().getGUID());

            if (glossaryElement.getCategories() != null)
            {
                /*
                 * Build a grid of elements in a subgraph
                 */
                super.startSubgraph("Category Hierarchy", VisualStyle.DESCRIPTION, "LR");

                this.addChildCategories(null, glossaryElement.getCategories());

                super.endSubgraph();

                appendMermaidDottedLine(null, glossaryElement.getElementHeader().getGUID(), null, "Category Hierarchy");
            }
        }
    }


    /**
     * Add the child categories to the graph.
     *
     * @param parentNodeName id of the parent
     * @param childCategories list of child categories
     */
    private void addChildCategories(String                     parentNodeName,
                                    List<ChildCategoryElement> childCategories)
    {
        if (childCategories != null)
        {
            for (ChildCategoryElement childCategoryElement : childCategories)
            {
                addChildCategory(parentNodeName, childCategoryElement);
            }
        }
    }


    /**
     * Add the child category to the graph.
     *
     * @param parentNodeName id of the parent
     * @param childCategoryElement child category
     */
    private void addChildCategory(String               parentNodeName,
                                  ChildCategoryElement childCategoryElement)
    {
        appendNewMermaidNode(childCategoryElement.getRelatedElement(), VisualStyle.GLOSSARY_CATEGORY);

        if (parentNodeName != null)
        {
            appendMermaidDottedLine(null, parentNodeName, null, childCategoryElement.getRelatedElement().getElementHeader().getGUID());
        }

        addChildCategories(childCategoryElement.getRelatedElement().getElementHeader().getGUID(),
                           childCategoryElement.getChildCategories());
    }



    /**
     * Add a text box with the description (if any)
     *
     * @param glossaryElement element with the potential description
     */
    private void addDescription(GlossaryElement glossaryElement)
    {
        if (glossaryElement.getGlossaryProperties() != null)
        {
            if (glossaryElement.getGlossaryProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryElement.getGlossaryProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                appendInvisibleMermaidLine(descriptionNodeName, glossaryElement.getElementHeader().getGUID());
            }
        }
    }
}
