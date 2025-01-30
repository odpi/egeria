/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's solution blueprint graph.
 */
public class SolutionBlueprintMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param solutionBlueprintElement content
     */
    public SolutionBlueprintMermaidGraphBuilder(SolutionBlueprintElement solutionBlueprintElement)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Component for Solution Blueprint - ");
        mermaidGraph.append(solutionBlueprintElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(solutionBlueprintElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        String currentNodeName    = solutionBlueprintElement.getElementHeader().getGUID();
        String currentDisplayName = solutionBlueprintElement.getProperties().getDisplayName();



        if (solutionBlueprintElement.getSolutionComponents() != null)
        {
            mermaidGraph.append("subgraph " );
            mermaidGraph.append(currentNodeName);
            mermaidGraph.append(" [Components]\n");
            nodeColours.put(currentNodeName, VisualStyle.SOLUTION_BLUEPRINT);

            List<String> solutionWireGUIDs = new ArrayList<>();

            for (SolutionBlueprintComponent node : solutionBlueprintElement.getSolutionComponents())
            {
                if (node != null)
                {
                    currentNodeName    = node.getSolutionComponent().getElementHeader().getGUID();
                    currentDisplayName = node.getSolutionComponent().getProperties().getDisplayName();

                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getSolutionComponent().getProperties().getQualifiedName();
                    }

                    appendNewMermaidNode(currentNodeName,
                                         currentDisplayName,
                                         node.getSolutionComponent().getElementHeader().getType().getTypeName(),
                                         VisualStyle.SOLUTION_COMPONENT);

                    if (node.getSolutionComponent().getWiredToLinks() != null)
                    {
                        for (WiredSolutionComponent line : node.getSolutionComponent().getWiredToLinks())
                        {
                            if ((line != null) && (! solutionWireGUIDs.contains(line.getElementHeader().getGUID())))
                            {
                                super.appendMermaidLine(line.getLinkedElement().getElementHeader().getGUID(),
                                                        this.getListLabel(Collections.singletonList(super.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()))),
                                                        node.getSolutionComponent().getElementHeader().getGUID());

                                solutionWireGUIDs.add(line.getElementHeader().getGUID());
                            }
                        }
                    }

                    if (node.getSolutionComponent().getWiredFromLinks() != null)
                    {
                        for (WiredSolutionComponent line : node.getSolutionComponent().getWiredFromLinks())
                        {
                            if ((line != null) && (! solutionWireGUIDs.contains(line.getElementHeader().getGUID())))
                            {
                                super.appendMermaidLine(node.getSolutionComponent().getElementHeader().getGUID(),
                                                        this.getListLabel(Collections.singletonList(super.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()))),
                                                        line.getLinkedElement().getElementHeader().getGUID());

                                solutionWireGUIDs.add(line.getElementHeader().getGUID());
                            }
                        }
                    }

                    if (node.getSolutionComponent().getActors() != null)
                    {
                        for (RelatedMetadataElementSummary line : node.getSolutionComponent().getActors())
                        {
                            if (line != null)
                            {
                                String actorRoleName = line.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);

                                if (actorRoleName == null)
                                {
                                    actorRoleName = line.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                                }

                                appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                                     actorRoleName,
                                                     line.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                     VisualStyle.SOLUTION_ROLE);

                                String actorRoleDescription = line.getRelationshipProperties().get(OpenMetadataProperty.ROLE.name);

                                if (actorRoleDescription == null)
                                {
                                    actorRoleDescription = super.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName());
                                }
                                super.appendMermaidLine(line.getRelatedElement().getElementHeader().getGUID(),
                                                        this.getListLabel(Collections.singletonList(actorRoleDescription)),
                                                        node.getSolutionComponent().getElementHeader().getGUID());
                            }
                        }
                    }
                }
            }

            mermaidGraph.append("end\n");
        }
        else
        {
            appendNewMermaidNode(currentNodeName,
                                 currentDisplayName,
                                 solutionBlueprintElement.getElementHeader().getType().getTypeName(),
                                 VisualStyle.SOLUTION_BLUEPRINT);
        }
    }


    /**
     * Construct a mermaid markdown graph.
     *
     * @param solutionBlueprintElement content
     */
    public SolutionBlueprintMermaidGraphBuilder(SolutionBlueprintElement solutionBlueprintElement,
                                                boolean                  useOld)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Component for Solution Blueprint - ");
        mermaidGraph.append(solutionBlueprintElement.getProperties().getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(solutionBlueprintElement.getElementHeader().getGUID());
        mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        List<String> usedNodeNames = new ArrayList<>();

        String currentNodeName    = solutionBlueprintElement.getElementHeader().getGUID();
        String currentDisplayName = solutionBlueprintElement.getProperties().getDisplayName();

        appendMermaidNode(currentNodeName,
                          currentDisplayName,
                          solutionBlueprintElement.getElementHeader().getType().getTypeName());

        usedNodeNames.add(currentNodeName);


        if (solutionBlueprintElement.getSolutionComponents() != null)
        {
            for (SolutionBlueprintComponent node : solutionBlueprintElement.getSolutionComponents())
            {
                if (node != null)
                {
                    currentNodeName = node.getSolutionComponent().getElementHeader().getGUID();
                    currentDisplayName   = node.getSolutionComponent().getProperties().getDisplayName();

                    if (currentDisplayName == null)
                    {
                        currentDisplayName = node.getSolutionComponent().getProperties().getQualifiedName();
                    }

                    if (!usedNodeNames.contains(currentNodeName))
                    {
                        appendMermaidNode(currentNodeName,
                                          currentDisplayName,
                                          node.getSolutionComponent().getElementHeader().getType().getTypeName());

                        usedNodeNames.add(currentNodeName);
                    }

                    if (node.getSolutionComponent().getWiredToLinks() != null)
                    {
                        for (WiredSolutionComponent line : node.getSolutionComponent().getWiredToLinks())
                        {
                            if (line != null)
                            {
                                super.appendMermaidLine(line.getLinkedElement().getElementHeader().getGUID(),
                                                        this.getListLabel(Collections.singletonList(line.getElementHeader().getType().getTypeName())),
                                                        node.getSolutionComponent().getElementHeader().getGUID());
                            }
                        }
                    }

                    if (node.getSolutionComponent().getWiredFromLinks() != null)
                    {
                        for (WiredSolutionComponent line : node.getSolutionComponent().getWiredFromLinks())
                        {
                            if (line != null)
                            {
                                super.appendMermaidLine(node.getSolutionComponent().getElementHeader().getGUID(),
                                                        this.getListLabel(Collections.singletonList(line.getElementHeader().getType().getTypeName())),
                                                        line.getLinkedElement().getElementHeader().getGUID());
                            }
                        }
                    }

                    if (node.getSolutionComponent().getActors() != null)
                    {
                        for (RelatedMetadataElementSummary line : node.getSolutionComponent().getActors())
                        {
                            if (line != null)
                            {
                                if (!usedNodeNames.contains(line.getRelatedElement().getElementHeader().getGUID()))
                                {
                                    String actorRoleName = line.getRelatedElement().getProperties().get(OpenMetadataProperty.NAME.name);

                                    if (actorRoleName == null)
                                    {
                                        actorRoleName = line.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
                                    }

                                    appendMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                                      actorRoleName,
                                                      line.getRelatedElement().getElementHeader().getType().getTypeName());

                                    usedNodeNames.add(currentNodeName);
                                }

                                super.appendMermaidLine(line.getRelatedElement().getElementHeader().getGUID(),
                                                        this.getListLabel(Collections.singletonList(line.getRelatedElement().getElementHeader().getType().getTypeName())),
                                                        node.getSolutionComponent().getElementHeader().getGUID());
                            }
                        }
                    }
                }
            }
        }
    }
}
