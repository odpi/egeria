/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.UUID;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's glossary term graph.
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
            String currentDisplayName = glossaryTermElement.getGlossaryTermProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = glossaryTermElement.getGlossaryTermProperties().getQualifiedName();
            }

            super.appendNewMermaidNode(glossaryTermElement.getElementHeader().getGUID(),
                                       currentDisplayName,
                                       super.getTypeNameForEntity(glossaryTermElement.getElementHeader()),
                                       super.getVisualStyleForEntity(glossaryTermElement.getElementHeader(), VisualStyle.GLOSSARY_TERM));

            this.addDescription(glossaryTermElement);

            super.addRelatedElementSummaries(glossaryTermElement.getExternalReferences(), VisualStyle.EXTERNAL_REFERENCE, glossaryTermElement.getElementHeader().getGUID());
            super.addRelatedElementSummaries(glossaryTermElement.getRelatedDefinitions(), VisualStyle.DATA_FIELD, glossaryTermElement.getElementHeader().getGUID());
            super.addRelatedElementSummaries(glossaryTermElement.getRelatedTerms(), VisualStyle.GLOSSARY_TERM, glossaryTermElement.getElementHeader().getGUID());

            if (glossaryTermElement.getSemanticAssignments() != null)
            {
                for (RelatedMetadataElementSummary assignee : glossaryTermElement.getSemanticAssignments())
                {
                    if (assignee != null)
                    {
                        super.appendNewMermaidNode(assignee.getRelatedElement(), VisualStyle.LINKED_ELEMENT);

                        super.appendMermaidLongAnimatedLine(assignee.getRelationshipHeader().getGUID(),
                                                            glossaryTermElement.getElementHeader().getGUID(),
                                                            super.addSpacesToTypeName(assignee.getRelationshipHeader().getType().getTypeName()),
                                                            assignee.getRelatedElement().getElementHeader().getGUID());

                        if (assignee.getRelatedElement().getElementHeader().getClassifications() != null)
                        {
                            for (ElementClassification classification : assignee.getRelatedElement().getElementHeader().getClassifications())
                            {
                                if (classification != null)
                                {
                                    if (OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                                    {
                                        if (classification.getClassificationProperties() != null)
                                        {
                                            if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name) != null)
                                            {
                                                String anchorGUID = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name).toString();

                                                if ((anchorGUID != null) && (! anchorGUID.equals(assignee.getRelatedElement().getElementHeader().getGUID())))
                                                {
                                                    String anchorTypeName = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

                                                    if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name) != null)
                                                    {
                                                        anchorTypeName = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name).toString();
                                                    }
                                                    else if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name) != null)
                                                    {
                                                        /*
                                                         * Try to narrow down the type of the anchor since its actual type is not defined
                                                         */
                                                        anchorTypeName = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name).toString();
                                                    }

                                                    super.appendNewMermaidNode(anchorGUID,
                                                                               anchorGUID,
                                                                               super.addSpacesToTypeName(anchorTypeName),
                                                                               VisualStyle.ANCHOR_ELEMENT);

                                                    super.appendMermaidDottedLine(assignee.getRelationshipHeader().getGUID() + "_Anchor",
                                                                                  assignee.getRelatedElement().getElementHeader().getGUID(),
                                                                                  "Anchored to",
                                                                                  anchorGUID);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            super.addRelatedElementSummaries(glossaryTermElement.getOtherRelatedElements(), VisualStyle.LINKED_ELEMENT, glossaryTermElement.getElementHeader().getGUID());
        }
    }


    /**
     * Add a text box with the description (if any)
     *
     * @param glossaryTermElement element with the potential description
     */
    private void addDescription(GlossaryTermElement glossaryTermElement)
    {
        final String termDetails = "Term Details";

        if (glossaryTermElement.getGlossaryTermProperties() != null)
        {
            super.startSubgraph(termDetails, VisualStyle.DESCRIPTION);

            if (glossaryTermElement.getGlossaryTermProperties().getSummary() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryTermElement.getGlossaryTermProperties().getSummary(),
                                     "Summary",
                                     VisualStyle.DESCRIPTION);
            }
            else if (glossaryTermElement.getGlossaryTermProperties().getDescription() != null)
            {
                String descriptionNodeName = UUID.randomUUID().toString();

                appendNewMermaidNode(descriptionNodeName,
                                     glossaryTermElement.getGlossaryTermProperties().getDescription(),
                                     "Description",
                                     VisualStyle.DESCRIPTION);
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
            else
            {
                appendMermaidDottedLine(null,
                                        glossaryNodeName,
                                        null,
                                        glossaryTermElement.getElementHeader().getGUID());
            }
        }
    }
}
