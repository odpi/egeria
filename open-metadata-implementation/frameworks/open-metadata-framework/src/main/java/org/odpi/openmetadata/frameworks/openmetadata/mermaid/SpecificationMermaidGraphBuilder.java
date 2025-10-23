/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Sho
 */
public class SpecificationMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph - this is used by GAF Open Governance Services
     *
     * @param specification contents
     */
    public SpecificationMermaidGraphBuilder(String                                 elementGUID,
                                            String                                 elementDisplayName,
                                            Map<String, List<Map<String, String>>> specification)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Specification for - ");
        mermaidGraph.append(elementDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(elementGUID);
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if (specification != null)
        {
            for (String propertyType : specification.keySet())
            {
                if (propertyType != null)
                {
                    appendMermaidNode(propertyType,
                                      propertyType,
                                      OpenMetadataProperty.PROPERTY_NAME.name);

                    List<Map<String, String>> propertyList = specification.get(propertyType);

                    if (propertyList != null)
                    {
                        for (int listItemIndex = 0; listItemIndex < propertyList.size(); listItemIndex++)
                        {
                            Map<String, String> propertyDescription = propertyList.get(listItemIndex);
                            if (propertyDescription != null)
                            {
                                String propertyDescriptionId = propertyType + "::" + listItemIndex;
                                String propertyName          = propertyDescription.get(propertyType + "Name");

                                mermaidGraph.append(this.lookupNodeName(this.removeSpaces(propertyDescriptionId)));
                                mermaidGraph.append("(\"`**");
                                mermaidGraph.append(propertyName);
                                mermaidGraph.append("**\n");

                                for (String name : propertyDescription.keySet())
                                {
                                    if ((name != null) && (! name.equals(propertyName) && (! name.equals(propertyType + "Name"))))
                                    {
                                        mermaidGraph.append(name);
                                        mermaidGraph.append(": ");
                                        mermaidGraph.append(propertyDescription.get(name));
                                        mermaidGraph.append("\n");
                                    }
                                }

                                mermaidGraph.append("`\")\n");

                                super.appendMermaidLine(null,
                                                        this.removeSpaces(propertyType),
                                                        null,
                                                        this.removeSpaces(propertyDescriptionId));
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Constructor for the graph.
     *
     * @param rootElement contents
     */
    public SpecificationMermaidGraphBuilder(OpenMetadataRootElement rootElement)
    {
        if (rootElement.getSpecification() != null)
        {
            mermaidGraph.append("---\n");
            mermaidGraph.append("title: Specification for - ");
            mermaidGraph.append(super.getNodeDisplayName(rootElement));
            mermaidGraph.append(" [");
            mermaidGraph.append(rootElement.getElementHeader().getGUID());
            mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            Map<String, List<RelatedMetadataElementSummary>> organizedRelationships = new HashMap<>();

            for (RelatedMetadataElementSummary relationship : rootElement.getSpecificationProperties())
            {
                if ((relationship != null) && (relationship.getRelationshipProperties() instanceof SpecificationPropertyAssignmentProperties specificationPropertyAssignmentProperties))
                {
                    if (specificationPropertyAssignmentProperties.getPropertyName() != null)
                    {
                        List<RelatedMetadataElementSummary> propertyValues = organizedRelationships.get(specificationPropertyAssignmentProperties.getPropertyName());

                        if (propertyValues == null)
                        {
                            propertyValues = new ArrayList<>();
                        }

                        propertyValues.add(relationship);
                        organizedRelationships.put(specificationPropertyAssignmentProperties.getPropertyName(), propertyValues);
                    }
                }
            }

            for (String propertyName : organizedRelationships.keySet())
            {
                this.startSubgraph(propertyName, VisualStyle.DESCRIPTION);

                for (RelatedMetadataElementSummary relationship : organizedRelationships.get(propertyName))
                {
                    if (relationship.getRelatedElement().getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
                    {
                        this.appendMermaidNode(propertyName + "::" + relationship.getRelatedElement().getElementHeader().getGUID(),
                                               validValueDefinitionProperties.getDescription(),
                                               validValueDefinitionProperties.getDisplayName());
                    }
                }

                this.endSubgraph();
            }
        }
    }
}
