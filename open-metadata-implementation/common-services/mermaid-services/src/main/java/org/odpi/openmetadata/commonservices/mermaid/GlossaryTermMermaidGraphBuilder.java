/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's data class graph.
 */
public class GlossaryTermMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param glossaryTermElement content
     */
    public GlossaryTermMermaidGraphBuilder(GlossaryTermElement glossaryTermElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Glossary Term - ");
        mermaidGraph.append(glossaryTermElement.getGlossaryTermProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(glossaryTermElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if (glossaryTermElement != null)
        {
            this.addDescription(glossaryTermElement);

            String currentDisplayName = glossaryTermElement.getGlossaryTermProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = glossaryTermElement.getGlossaryTermProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(glossaryTermElement.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       glossaryTermElement.getElementHeader().getType().getTypeName(),
                                       checkForClassifications(glossaryTermElement.getElementHeader(), VisualStyle.GLOSSARY_TERM));

            super.addRelatedFromElementSummaries(glossaryTermElement.getCategoryMembership(), VisualStyle.GLOSSARY_CATEGORY, glossaryTermElement.getElementHeader().getGUID());

            super.addRelatedToElementSummaries(glossaryTermElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, glossaryTermElement.getElementHeader().getGUID());
            super.addRelatedFromElementSummaries(glossaryTermElement.getRelatedDefinitions(), VisualStyle.DATA_FIELD, glossaryTermElement.getElementHeader().getGUID());
            super.addRelatedFromElementSummaries(glossaryTermElement.getSemanticAssignments(), VisualStyle.LINKED_ELEMENT, glossaryTermElement.getElementHeader().getGUID());
            super.addRelatedToElementSummaries(glossaryTermElement.getRelatedToTerms(), VisualStyle.GLOSSARY_TERM, glossaryTermElement.getElementHeader().getGUID());
            super.addRelatedFromElementSummaries(glossaryTermElement.getRelatedFromTerms(), VisualStyle.GLOSSARY_TERM, glossaryTermElement.getElementHeader().getGUID());

            super.addRelatedToElementSummaries(glossaryTermElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, glossaryTermElement.getElementHeader().getGUID());
        }
    }


    /**
     * Add a text box with the description (if any)
     *
     * @param glossaryTermElement element with the potential description
     */
    private void addDescription(GlossaryTermElement glossaryTermElement)
    {
        if (glossaryTermElement.getGlossaryTermProperties() != null)
        {
            super.startSubgraph("Term Details", VisualStyle.DESCRIPTION);

            String lastNodeName = null;

            if (glossaryTermElement.getGlossaryTermProperties().getAliases() != null)
            {
                for (String alias : glossaryTermElement.getGlossaryTermProperties().getAliases())
                {
                    if (alias != null)
                    {
                        String descriptionNodeName = UUID.randomUUID().toString();

                        appendNewMermaidNode(descriptionNodeName,
                                             alias,
                                             "Alias",
                                             VisualStyle.DESCRIPTION);

                        if (lastNodeName != null)
                        {
                            appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                        }

                        lastNodeName = descriptionNodeName;
                    }
                }
            }

            if (glossaryTermElement.getGlossaryTermProperties().getSummary() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryTermElement.getGlossaryTermProperties().getSummary(),
                                     "Summary",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (glossaryTermElement.getGlossaryTermProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryTermElement.getGlossaryTermProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (glossaryTermElement.getGlossaryTermProperties().getAbbreviation() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryTermElement.getGlossaryTermProperties().getAbbreviation(),
                                     "Abbreviation",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (glossaryTermElement.getGlossaryTermProperties().getExamples() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryTermElement.getGlossaryTermProperties().getExamples(),
                                     "Examples",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }

                lastNodeName = descriptionNodeName;
            }

            if (glossaryTermElement.getGlossaryTermProperties().getUsage() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryTermElement.getGlossaryTermProperties().getUsage(),
                                     "Usage",
                                     VisualStyle.DESCRIPTION);

                if (lastNodeName != null)
                {
                    appendInvisibleMermaidLine(lastNodeName, descriptionNodeName);
                }
            }

            super.endSubgraph();

            String glossaryNodeName = null;

            if (glossaryTermElement.getParentGlossary() != null)
            {
                glossaryNodeName = glossaryTermElement.getParentGlossary().getRelatedElement().getElementHeader().getGUID();

                String glossaryDisplayName = glossaryTermElement.getParentGlossary().getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                if (glossaryDisplayName == null)
                {
                    glossaryDisplayName = glossaryTermElement.getParentGlossary().getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                }

                appendNewMermaidNode(glossaryNodeName,
                                     glossaryDisplayName,
                                     OpenMetadataType.GLOSSARY.typeName,
                                     VisualStyle.GLOSSARY);
            }

            if (glossaryTermElement.getCategoryMembership() != null)
            {
                for (RelatedMetadataElementSummary category : glossaryTermElement.getCategoryMembership())
                {
                    String parentCategoryNodeName = category.getRelatedElement().getElementHeader().getGUID();

                    String categoryDisplayName = category.getRelatedElement().getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

                    if (categoryDisplayName == null)
                    {
                        categoryDisplayName = category.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                    }

                    appendNewMermaidNode(parentCategoryNodeName,
                                         categoryDisplayName,
                                         OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                         VisualStyle.GLOSSARY_CATEGORY);

                    if (glossaryNodeName != null)
                    {
                        appendMermaidDottedLine(null,
                                                glossaryNodeName,
                                                null,
                                                parentCategoryNodeName);
                    }

                    appendMermaidDottedLine(null,
                                            parentCategoryNodeName,
                                            null,
                                            glossaryTermElement.getElementHeader().getGUID());
                }
            }
        }
    }
}
