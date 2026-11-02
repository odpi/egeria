/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RoledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Creates a mermaid graph rendering of one layer of an information supply chain's implementation.  The
 * implementation is made up of the relationships that name the information supply chain in their iscQualifiedName
 * property.  The elements at the ends of these relationships are divided into layers, each drawn in its own graph,
 * because together they are often too big for mermaid to display.
 * <br><br>
 * The layer's elements are drawn inside a subgraph.  The solution components and digital products that are
 * linked to the layer's elements are drawn outside the subgraph, with their links into it.  So are the anchors of
 * the layer's elements that the layer shows, linked with a thin line labelled "Anchor for".  An anchor is only shown
 * if it is returned as part of the information supply chain.
 */
public class InformationSupplyChainLayerMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * The layers of an information supply chain's implementation.
     */
    public enum Layer
    {
        /**
         * The digital products, along with the solution components linked to them, and the solution components
         * and digital products that anchor them.
         */
        DATA_MESH("Data Mesh"),

        /**
         * The IT infrastructure and software capabilities, along with the solution components that are linked to
         * them or anchor them.
         */
        SYSTEM_FABRIC("System Fabric"),

        /**
         * The data, processes and actions linked by data lineage, control flow and action requester relationships,
         * along with the solution components and digital products linked to them, and the solution components that
         * anchor them.
         */
        DATA_FABRIC("Data Fabric"),

        /**
         * The data and processes linked by data mapping relationships, along with their anchors.
         */
        DATA_MAPPING("Data Mapping");

        private final String areaName;

        Layer(String areaName)
        {
            this.areaName = areaName;
        }

        /**
         * Return the name of the subgraph for the layer.
         *
         * @return string
         */
        public String getAreaName()
        {
            return areaName;
        }
    }


    private final Layer                               layer;
    private final Map<String, ElementStub>            returnedElements  = new HashMap<>();
    private final Map<String, MetadataElementSummary> memberSummaries   = new HashMap<>();


    /**
     * Construct a mermaid markdown graph.
     *
     * @param informationSupplyChainElement content
     * @param layer layer of the implementation to draw
     */
    public InformationSupplyChainLayerMermaidGraphBuilder(InformationSupplyChainElement informationSupplyChainElement,
                                                          Layer                         layer)
    {
        this.layer = layer;

        if ((informationSupplyChainElement.getProperties() instanceof InformationSupplyChainProperties informationSupplyChainProperties) &&
                (informationSupplyChainElement.getImplementation() != null))
        {
            mermaidGraph.append("---\n");
            mermaidGraph.append("title: Information Supply Chain ");
            mermaidGraph.append(layer.getAreaName());
            mermaidGraph.append(" - ");
            mermaidGraph.append(super.removeTroublesomeTitleCharacters(informationSupplyChainProperties.getDisplayName()));
            mermaidGraph.append(" [");
            mermaidGraph.append(informationSupplyChainElement.getElementHeader().getGUID());
            mermaidGraph.append("]\n---\nflowchart TD\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

            /*
             * The solution components that are members of the information supply chain have fuller descriptions
             * than the stubs at the ends of the relationships, so they are used for the display names where possible.
             */
            if (informationSupplyChainElement.getCollectionMembers() != null)
            {
                for (RelatedMetadataElementSummary collectionMember : informationSupplyChainElement.getCollectionMembers())
                {
                    if ((collectionMember != null) && (collectionMember.getRelatedElement() != null))
                    {
                        memberSummaries.put(collectionMember.getRelatedElement().getElementHeader().getGUID(), collectionMember.getRelatedElement());
                    }
                }
            }

            List<MetadataRelationshipSummary> relationships = new ArrayList<>();

            for (MetadataRelationshipSummary relationship : informationSupplyChainElement.getImplementation())
            {
                if ((relationship != null) && (relationship.getEnd1() != null) && (relationship.getEnd2() != null))
                {
                    relationships.add(relationship);
                    returnedElements.put(relationship.getEnd1().getGUID(), relationship.getEnd1());
                    returnedElements.put(relationship.getEnd2().getGUID(), relationship.getEnd2());
                }
            }

            /*
             * Work out which elements belong in the layer's subgraph.
             */
            Map<String, ElementStub> layerElements = new LinkedHashMap<>();

            for (MetadataRelationshipSummary relationship : relationships)
            {
                if (isLayerRelationship(relationship))
                {
                    if (isLayerElement(relationship.getEnd1()))
                    {
                        layerElements.put(relationship.getEnd1().getGUID(), relationship.getEnd1());
                    }

                    if (isLayerElement(relationship.getEnd2()))
                    {
                        layerElements.put(relationship.getEnd2().getGUID(), relationship.getEnd2());
                    }
                }
            }

            if (! layerElements.isEmpty())
            {
                /*
                 * Sort the relationships into those within the layer and those that link an element outside the
                 * layer to it.
                 */
                List<MetadataRelationshipSummary> layerRelationships = new ArrayList<>();
                List<MetadataRelationshipSummary> linkRelationships  = new ArrayList<>();
                Map<String, ElementStub>          linkedElements     = new LinkedHashMap<>();

                for (MetadataRelationshipSummary relationship : relationships)
                {
                    boolean end1InLayer = layerElements.containsKey(relationship.getEnd1().getGUID());
                    boolean end2InLayer = layerElements.containsKey(relationship.getEnd2().getGUID());

                    if (end1InLayer && end2InLayer)
                    {
                        if (isDrawnWithinLayer(relationship))
                        {
                            layerRelationships.add(relationship);
                        }
                    }
                    else if (end1InLayer && isLinkedElement(relationship.getEnd2()))
                    {
                        linkRelationships.add(relationship);
                        linkedElements.put(relationship.getEnd2().getGUID(), relationship.getEnd2());
                    }
                    else if (end2InLayer && isLinkedElement(relationship.getEnd1()))
                    {
                        linkRelationships.add(relationship);
                        linkedElements.put(relationship.getEnd1().getGUID(), relationship.getEnd1());
                    }
                }

                /*
                 * The anchors of the layer's elements that the layer shows are linked to them with a thin line.
                 * Any other anchors are only linked (with a dotted line) if they happen to be in the graph.
                 */
                Map<String, List<String>> anchoredElements = new LinkedHashMap<>();

                for (ElementStub layerElement : layerElements.values())
                {
                    String anchorGUID = getReturnedAnchorGUID(layerElement);

                    if ((anchorGUID != null) && (isShownAnchor(anchorGUID)))
                    {
                        anchoredElements.computeIfAbsent(anchorGUID, k -> new ArrayList<>()).add(layerElement.getGUID());
                    }
                    else if (layer != Layer.DATA_MAPPING)
                    {
                        extractAnchorInfo(layerElement);
                    }
                }

                if (layer != Layer.DATA_MAPPING)
                {
                    for (ElementStub linkedElement : linkedElements.values())
                    {
                        extractAnchorInfo(linkedElement);
                    }
                }

                /*
                 * The elements outside the subgraph are added first so that mermaid does not pull them into it.
                 */
                for (String linkedElementGUID : linkedElements.keySet())
                {
                    appendOutsideNode(linkedElementGUID);
                }

                for (String anchorGUID : anchoredElements.keySet())
                {
                    if (! layerElements.containsKey(anchorGUID))
                    {
                        appendOutsideNode(anchorGUID);
                    }
                }

                if (layer == Layer.DATA_MESH)
                {
                    super.startSubgraph(layer.getAreaName(), VisualStyle.DIGITAL_PRODUCT_GRAPH, "BT"); // BT = Bottom to Top; may want RL = Right to Left
                }
                else
                {
                    super.startSubgraph(layer.getAreaName(), VisualStyle.INFORMATION_SUPPLY_CHAIN_SEG);
                }

                for (ElementStub layerElement : layerElements.values())
                {
                    appendStubNode(layerElement, layer == Layer.DATA_MESH ? VisualStyle.DIGITAL_PRODUCT : VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);
                }

                super.endSubgraph();

                /*
                 * Add the relationships
                 */
                for (MetadataRelationshipSummary relationship : layerRelationships)
                {
                    appendRelationshipLine(relationship);
                }

                for (MetadataRelationshipSummary relationship : linkRelationships)
                {
                    appendRelationshipLine(relationship);
                }

                for (String anchorGUID : anchoredElements.keySet())
                {
                    for (String anchoredElementGUID : anchoredElements.get(anchorGUID))
                    {
                        appendMermaidThinLine(null, anchorGUID, "Anchor for", anchoredElementGUID);
                    }
                }
            }
        }
    }


    /**
     * Does this relationship bring its ends into the layer?
     *
     * @param relationship relationship to test
     * @return boolean flag
     */
    private boolean isLayerRelationship(MetadataRelationshipSummary relationship)
    {
        return switch (layer)
        {
            case DATA_FABRIC  -> (propertyHelper.isTypeOf(relationship.getRelationshipHeader(), OpenMetadataType.DATA_LINEAGE_RELATIONSHIP.typeName)) ||
                                 (propertyHelper.isTypeOf(relationship.getRelationshipHeader(), OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName)) ||
                                 (propertyHelper.isTypeOf(relationship.getRelationshipHeader(), OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName));
            case DATA_MAPPING -> propertyHelper.isTypeOf(relationship.getRelationshipHeader(), OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName);
            default           -> true;
        };
    }


    /**
     * Is this a relationship that is drawn between two elements of the layer?  The data mappings have their own
     * layer, so they are left out of the data fabric, and only the data mappings are drawn in the data mapping layer.
     *
     * @param relationship relationship to test
     * @return boolean flag
     */
    private boolean isDrawnWithinLayer(MetadataRelationshipSummary relationship)
    {
        boolean isDataMapping = propertyHelper.isTypeOf(relationship.getRelationshipHeader(), OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName);

        return switch (layer)
        {
            case DATA_FABRIC  -> ! isDataMapping;
            case DATA_MAPPING -> isDataMapping;
            default           -> true;
        };
    }


    /**
     * Does this element belong in the layer's subgraph?
     *
     * @param elementStub element to test
     * @return boolean flag
     */
    private boolean isLayerElement(ElementStub elementStub)
    {
        return switch (layer)
        {
            case DATA_MESH     -> isDigitalProduct(elementStub);
            case SYSTEM_FABRIC -> isSystemElement(elementStub);
            default            -> (! isDigitalProduct(elementStub)) &&
                                  (! isSystemElement(elementStub)) &&
                                  (! isSolutionComponent(elementStub));
        };
    }


    /**
     * Is this element drawn outside the subgraph when it is linked to one of the layer's elements?
     *
     * @param elementStub element to test
     * @return boolean flag
     */
    private boolean isLinkedElement(ElementStub elementStub)
    {
        return switch (layer)
        {
            case DATA_MESH, SYSTEM_FABRIC -> isSolutionComponent(elementStub);
            case DATA_FABRIC              -> isSolutionComponent(elementStub) || isDigitalProduct(elementStub);
            case DATA_MAPPING             -> false;
        };
    }


    /**
     * Digital products make up the data mesh.
     *
     * @param elementStub element to test
     * @return boolean flag
     */
    private boolean isDigitalProduct(ElementStub elementStub)
    {
        return propertyHelper.isTypeOf(elementStub, OpenMetadataType.DIGITAL_PRODUCT.typeName);
    }


    /**
     * IT infrastructure and software capabilities are the systems that the data and processes run on.
     *
     * @param elementStub element to test
     * @return boolean flag
     */
    private boolean isSystemElement(ElementStub elementStub)
    {
        return (propertyHelper.isTypeOf(elementStub, OpenMetadataType.IT_INFRASTRUCTURE.typeName)) ||
                (propertyHelper.isTypeOf(elementStub, OpenMetadataType.SOFTWARE_CAPABILITY.typeName));
    }


    /**
     * Solution components describe the design that the layers implement.
     *
     * @param elementStub element to test
     * @return boolean flag
     */
    private boolean isSolutionComponent(ElementStub elementStub)
    {
        return propertyHelper.isTypeOf(elementStub, OpenMetadataType.SOLUTION_COMPONENT.typeName);
    }


    /**
     * Return the unique identifier of the element's anchor if the anchor is a different element that is returned
     * as part of the information supply chain.
     *
     * @param elementStub element
     * @return guid or null
     */
    private String getReturnedAnchorGUID(ElementStub elementStub)
    {
        if ((elementStub.getAnchor() != null) &&
                (elementStub.getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties))
        {
            String anchorGUID = anchorsProperties.getAnchorGUID();

            if ((anchorGUID != null) && (! anchorGUID.equals(elementStub.getGUID())) &&
                    ((returnedElements.containsKey(anchorGUID)) || (memberSummaries.containsKey(anchorGUID))))
            {
                return anchorGUID;
            }
        }

        return null;
    }


    /**
     * Is this anchor shown with a thin line to the elements it anchors?  The data mapping layer shows all of its
     * anchors.  The other layers show the solution components, and the data mesh shows the digital products too.
     *
     * @param anchorGUID unique identifier of an anchor that is returned as part of the information supply chain
     * @return boolean flag
     */
    private boolean isShownAnchor(String anchorGUID)
    {
        ElementHeader anchorHeader = getReturnedElementHeader(anchorGUID);

        boolean isSolutionComponent = propertyHelper.isTypeOf(anchorHeader, OpenMetadataType.SOLUTION_COMPONENT.typeName);

        return switch (layer)
        {
            case DATA_MAPPING -> true;
            case DATA_MESH    -> isSolutionComponent || propertyHelper.isTypeOf(anchorHeader, OpenMetadataType.DIGITAL_PRODUCT.typeName);
            default           -> isSolutionComponent;
        };
    }


    /**
     * Return the header of an element that is returned as part of the information supply chain, preferring the
     * fuller description of the information supply chain's members.
     *
     * @param elementGUID unique identifier of the element
     * @return header
     */
    private ElementHeader getReturnedElementHeader(String elementGUID)
    {
        MetadataElementSummary memberSummary = memberSummaries.get(elementGUID);

        if (memberSummary != null)
        {
            return memberSummary.getElementHeader();
        }

        return returnedElements.get(elementGUID);
    }


    /**
     * Add a node for an element that is linked to the layer from outside it, either through a relationship or
     * because it is the anchor of one of the layer's elements.
     *
     * @param elementGUID unique identifier of an element that is returned as part of the information supply chain
     */
    private void appendOutsideNode(String elementGUID)
    {
        ElementHeader elementHeader = getReturnedElementHeader(elementGUID);
        VisualStyle   visualStyle   = VisualStyle.LINEAGE_ANCHOR;

        if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.SOLUTION_COMPONENT.typeName))
        {
            visualStyle = VisualStyle.DEFAULT_SOLUTION_COMPONENT;
        }
        else if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.DIGITAL_PRODUCT.typeName))
        {
            visualStyle = VisualStyle.DIGITAL_PRODUCT;
        }

        MetadataElementSummary memberSummary = memberSummaries.get(elementGUID);

        if (memberSummary != null)
        {
            appendNewMermaidNode(elementGUID,
                                 super.getNodeDisplayName(memberSummary),
                                 super.getTypeNameForEntity(memberSummary.getElementHeader()),
                                 memberSummary.getProperties(),
                                 visualStyle);
        }
        else
        {
            appendStubNode(returnedElements.get(elementGUID), visualStyle);
        }
    }


    /**
     * Add a node for an element that is only known from the end of a relationship.
     *
     * @param elementStub element to add
     * @param defaultVisualStyle style to use if the element's type does not have its own
     */
    private void appendStubNode(ElementStub elementStub,
                                VisualStyle defaultVisualStyle)
    {
        String displayName = elementStub.getUniqueName();

        if (displayName == null)
        {
            displayName = elementStub.getGUID();
        }

        appendNewMermaidNode(elementStub.getGUID(),
                             displayName,
                             elementStub.getType().getTypeName(),
                             getVisualStyleForEntity(elementStub, defaultVisualStyle));
    }


    /**
     * Add a line for one of the implementation relationships.  ImplementedBy is drawn as a thin line.
     *
     * @param relationship relationship to draw
     */
    private void appendRelationshipLine(MetadataRelationshipSummary relationship)
    {
        String label = null;

        if (relationship.getRelationshipProperties() instanceof LabeledRelationshipProperties labeledRelationshipProperties)
        {
            label = labeledRelationshipProperties.getLabel();
        }
        else if (relationship.getRelationshipProperties() instanceof RoledRelationshipProperties roledRelationshipProperties)
        {
            label = roledRelationshipProperties.getRole();
        }

        if (label != null)
        {
            label = label + " [" + super.addSpacesToTypeName(relationship.getRelationshipHeader().getType().getTypeName()) + "]";
        }
        else
        {
            label = super.addSpacesToTypeName(relationship.getRelationshipHeader().getType().getTypeName());
        }

        if (propertyHelper.isTypeOf(relationship.getRelationshipHeader(), OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
        {
            appendMermaidThinLine(relationship.getRelationshipHeader().getGUID(),
                                  relationship.getEnd1().getGUID(),
                                  label,
                                  relationship.getEnd2().getGUID());
        }
        else
        {
            appendMermaidLine(relationship.getRelationshipHeader().getGUID(),
                              relationship.getEnd1().getGUID(),
                              label,
                              relationship.getEnd2().getGUID());
        }
    }
}
