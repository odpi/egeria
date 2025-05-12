/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryCategoryElement;
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
            this.addDescription(glossaryCategoryElement);

            String currentDisplayName = glossaryCategoryElement.getGlossaryCategoryProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = glossaryCategoryElement.getGlossaryCategoryProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(glossaryCategoryElement.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       glossaryCategoryElement.getElementHeader().getType().getTypeName(),
                                       checkForClassifications(glossaryCategoryElement.getElementHeader(), VisualStyle.GLOSSARY_CATEGORY));

            super.addRelatedToElementSummaries(glossaryCategoryElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, glossaryCategoryElement.getElementHeader().getGUID());
            super.addRelatedToElementSummaries(glossaryCategoryElement.getChildCategories(), VisualStyle.GLOSSARY_CATEGORY, glossaryCategoryElement.getElementHeader().getGUID());
            super.addRelatedToElementSummaries(glossaryCategoryElement.getTerms(), VisualStyle.GLOSSARY_TERM, glossaryCategoryElement.getElementHeader().getGUID());

            super.addRelatedToElementSummaries(glossaryCategoryElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, glossaryCategoryElement.getElementHeader().getGUID());
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
                String parentCategoryNodeName = glossaryCategoryElement.getParentGlossary().getRelatedElement().getElementHeader().getGUID();

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
                    appendMermaidDottedLine(glossaryCategoryElement.getParentCategory().getRelationshipHeader().getGUID(),
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
