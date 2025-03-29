/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;
import java.util.Map;


public class SpecificationMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph
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
                                      OpenMetadataProperty.PROPERTY_TYPE.name);

                    List<Map<String, String>> propertyList = specification.get(propertyType);

                    if (propertyList != null)
                    {
                        for (int listItemIndex = 0; listItemIndex < propertyList.size(); listItemIndex++)
                        {
                            Map<String, String> propertyDescription = propertyList.get(listItemIndex);
                            if (propertyDescription != null)
                            {
                                String propertyDescriptionId = propertyType + ":" + listItemIndex;
                                String propertyName          = propertyDescription.get(propertyType + "Name");

                                mermaidGraph.append(this.removeSpaces(propertyDescriptionId));
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
}
