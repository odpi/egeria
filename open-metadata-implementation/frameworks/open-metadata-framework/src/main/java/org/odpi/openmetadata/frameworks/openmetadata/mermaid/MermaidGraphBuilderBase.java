/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * Provides the basic functions for building flowchart based graph visualizations.
 */
public class MermaidGraphBuilderBase
{
    protected StringBuilder                  mermaidGraph      = new StringBuilder();
    private final   Set<String>              usedNodeNames     = new HashSet<>();
    private final   Set<String>              usedLinkNames     = new HashSet<>();
    private final   Set<String>              animatedLinkNames = new HashSet<>();
    protected final Map<String, VisualStyle> nodeColours       = new HashMap<>();
    protected final PropertyHelper           propertyHelper    = new PropertyHelper();
    protected final String                   sourceName        = "MermaidGraphBuilder";

    /**
     * Map from guid to nodeId
     */
    protected final Map<String, String> guidToNodeIdMap = new HashMap<>();

    /**
     * Map from anchorGUID to the AnchorNode record consisting of the anchorGUID and its typeName
     */
    private final Map<String, AnchorNode>    extractedAnchors = new HashMap<>();
    private final Map<String, Set<String>>   anchorLinks      = new HashMap<>();


    /**
     * Description of an anchor node for the lineage graph.
     *
     * @param anchorGUID unique identifier
     * @param anchorTypeName type name
     */
    record AnchorNode (String anchorGUID, String anchorTypeName) { }


    /**
     * Extract any anchor information to allow the graph to include linkages to anchors.  This can help
     * join up lineage graphs.
     *
     * @param elementHeader header of extracted element
     */
    protected void extractAnchorInfo(ElementHeader elementHeader)
    {
        if (elementHeader != null)
        {
            ElementClassification classification = elementHeader.getAnchor();

            if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name) != null)
            {
                String anchorGUID = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_GUID.name).toString();

                if ((anchorGUID != null) && (! anchorGUID.equals(elementHeader.getGUID())))
                {
                    if (extractedAnchors.get(anchorGUID) == null)
                    {
                        String anchorTypeName;

                        if (classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name) != null)
                        {
                            anchorTypeName = classification.getClassificationProperties().get(OpenMetadataProperty.ANCHOR_TYPE_NAME.name).toString();
                        }
                        else
                        {
                            anchorTypeName = "null";
                        }

                        AnchorNode anchorNode = new AnchorNode(anchorGUID, anchorTypeName);

                        extractedAnchors.put(anchorGUID, anchorNode);
                    }

                    Set<String> anchoredElements = anchorLinks.get(anchorGUID);

                    if (anchoredElements == null)
                    {
                        anchoredElements = new HashSet<>();
                    }

                    anchoredElements.add(elementHeader.getGUID());

                    anchorLinks.put(anchorGUID, anchoredElements);
                }
            }
        }
    }


    /**
     * Add links to anchors to the graph.
     *
     * @param allAnchors should all anchors be added - or just those to known nodes?
     */
    protected void addAnchorLinks(boolean allAnchors)
    {
        for (String anchorGUID : anchorLinks.keySet())
        {
            Set<String> anchoredElements = anchorLinks.get(anchorGUID);
            AnchorNode  anchorNode       = extractedAnchors.get(anchorGUID);

            if (allAnchors)
            {
                appendNewMermaidNode(anchorGUID,
                                     anchorGUID,
                                     anchorNode.anchorTypeName,
                                     VisualStyle.LINEAGE_ANCHOR);

                for (String anchoredElement : anchoredElements)
                {
                    appendMermaidDottedLine(null,
                                            anchorGUID,
                                            "Anchor for",
                                            anchoredElement);
                }
            }
            else if (usedNodeNames.contains(anchorGUID))
            {
                for (String anchoredElement : anchoredElements)
                {
                    appendMermaidDottedLine(null,
                                            anchorGUID,
                                            "Anchor for",
                                            anchoredElement);
                }
            }
        }
    }


    /**
     * Switch the visual style of an element if the Memento or Template/TemplateSubstitute classification is set.
     *
     * @param classificationName name
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForClassification(String classificationName)
    {
        if (classificationName != null)
        {
            if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.MEMENTO;
            }
            else if (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.TEMPLATE;
            }
            else if (OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.TEMPLATE;
            }
            else if (OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.OBJECT_IDENTIFIER;
            }
            else if (OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.DATA_SHARING_AGREEMENT;
            }
            else if (OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.ROOT_COLLECTION;
            }
            else if (OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.FOLDER;
            }
            else if (OpenMetadataType.HOME_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.HOME_COLLECTION;
            }
            else if (OpenMetadataType.RESULTS_SET_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.RESULTS_SET;
            }
            else if (OpenMetadataType.RECENT_ACCESS_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.RECENT_ACCESS;
            }
            else if (OpenMetadataType.WORK_ITEM_LIST_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return VisualStyle.WORK_ITEM_LIST;
            }
        }

        return null;
    }


    /**
     * Switch the visual style of an element if the Memento or Template/TemplateSubstitute classification is set.
     *
     * @param classificationName name
     * @return visual style to use
     */
    protected boolean checkForClassificationTypeName(String classificationName)
    {
        if (classificationName != null)
        {
            if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.HOME_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.RESULTS_SET_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.RECENT_ACCESS_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.WORK_ITEM_LIST_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.NAMESPACE_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.EVENT_SET_COLLECTION.typeName.equals(classificationName))
            {
                return true;
            }
            else if (OpenMetadataType.CONTEXT_EVENT_COLLECTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return true;
            }

            else return OpenMetadataType.CONCEPT_MODEL_CLASSIFICATION.typeName.equals(classificationName);
        }

        return false;
    }


    /**
     * Switch the visual style of an element if the Memento or Template/TemplateSubstitute classification is set.
     *
     * @param elementHeader header of element
     * @param defaultVisualStyle normal visual style for element
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForClassifications(ElementHeader elementHeader,
                                                           VisualStyle   defaultVisualStyle)
    {
        if (elementHeader != null)
        {
            if (elementHeader.getMemento() != null)
            {
                return VisualStyle.MEMENTO;
            }

            if (elementHeader.getTemplate() != null)
            {
                return VisualStyle.TEMPLATE;
            }

            if (elementHeader.getCollectionRoles() != null)
            {
                for (ElementClassification classification : elementHeader.getCollectionRoles())
                {
                    if (classification != null)
                    {
                        VisualStyle specialVisualStyle = this.getVisualStyleForClassification(classification.getClassificationName());

                        if (specialVisualStyle != null)
                        {
                            return specialVisualStyle;
                        }
                    }
                }
            }

            if (elementHeader.getProjectRoles() != null)
            {
                for (ElementClassification classification : elementHeader.getProjectRoles())
                {
                    if (classification != null)
                    {
                        VisualStyle specialVisualStyle = this.getVisualStyleForClassification(classification.getClassificationName());

                        if (specialVisualStyle != null)
                        {
                            return specialVisualStyle;
                        }
                    }
                }
            }

            if (elementHeader.getOtherClassifications() != null)
            {
                for (ElementClassification classification : elementHeader.getOtherClassifications())
                {
                    if (classification != null)
                    {
                        VisualStyle specialVisualStyle = this.getVisualStyleForClassification(classification.getClassificationName());

                        if (specialVisualStyle != null)
                        {
                            return specialVisualStyle;
                        }
                    }
                }
            }
        }

        return defaultVisualStyle;
    }


    /**
     * Switch the visual style of an element if the Memento or Template classification is set.
     *
     * @param openMetadataElement header of element
     * @param defaultVisualStyle normal visual style for element
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForClassifications(OpenMetadataElement openMetadataElement,
                                                           VisualStyle         defaultVisualStyle)
    {
        if ((openMetadataElement != null) && (openMetadataElement.getClassifications() != null))
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (classification != null)
                {
                    VisualStyle specialVisualStyle = this.getVisualStyleForClassification(classification.getClassificationName());

                    if (specialVisualStyle != null)
                    {
                        return specialVisualStyle;
                    }
                }
            }
        }

        return defaultVisualStyle;
    }


    /**
     * Switch the visual style of an element if the Memento or Template classification is set.
     *
     * @param openMetadataElement header of element
     * @param defaultVisualStyle normal visual style for element
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForClassifications(OpenMetadataElementStub openMetadataElement,
                                                           VisualStyle             defaultVisualStyle)
    {
        if ((openMetadataElement != null) && (openMetadataElement.getClassifications() != null))
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (classification != null)
                {
                    VisualStyle specialVisualStyle = this.getVisualStyleForClassification(classification.getClassificationName());

                    if (specialVisualStyle != null)
                    {
                        return specialVisualStyle;
                    }
                }
            }
        }

        return defaultVisualStyle;
    }



    /**
     * Add a subgraph of an element's classification to the graph.
     *
     * @param elementHeader element's header
     */
    protected void addClassifications(ElementHeader elementHeader)
    {
        if (elementHeader != null)
        {
            this.startSubgraph("Classifications", VisualStyle.DESCRIPTION, "TB");

            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getAnchor());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getZoneMembership());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getSubjectArea());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getImpact());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getCriticality());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getConfidentiality());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getConfidence());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getRetention());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getDigitalResourceOrigin());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getOwnership());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getMemento());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getTemplate());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getSchemaType());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getCalculatedValue());
            addClassificationToGraph(elementHeader.getGUID(), elementHeader.getPrimaryKey());

            if (elementHeader.getExecutionPoints() != null)
            {
                for (ElementClassification classification : elementHeader.getExecutionPoints())
                {
                    addClassificationToGraph(elementHeader.getGUID(), classification);
                }
            }

            if (elementHeader.getResourceManagerRoles() != null)
            {
                for (ElementClassification classification : elementHeader.getResourceManagerRoles())
                {
                    addClassificationToGraph(elementHeader.getGUID(), classification);
                }
            }

            if (elementHeader.getServerPurposes() != null)
            {
                for (ElementClassification classification : elementHeader.getServerPurposes())
                {
                    addClassificationToGraph(elementHeader.getGUID(), classification);
                }
            }

            if (elementHeader.getCollectionRoles() != null)
            {
                for (ElementClassification classification : elementHeader.getCollectionRoles())
                {
                    addClassificationToGraph(elementHeader.getGUID(), classification);
                }
            }

            if (elementHeader.getProjectRoles() != null)
            {
                for (ElementClassification classification : elementHeader.getProjectRoles())
                {
                    addClassificationToGraph(elementHeader.getGUID(), classification);
                }
            }

            if (elementHeader.getDuplicateClassifications() != null)
            {
                for (ElementClassification classification : elementHeader.getDuplicateClassifications())
                {
                    addClassificationToGraph(elementHeader.getGUID(), classification);
                }
            }

            if (elementHeader.getOtherClassifications() != null)
            {
                for (ElementClassification classification : elementHeader.getOtherClassifications())
                {
                    addClassificationToGraph(elementHeader.getGUID(), classification);
                }
            }

            endSubgraph();
        }
    }


    /**
     * Add a classification as a node in the graph.
     *
     * @param elementGUID unique identifier of the element that the classification cones from
     * @param classification classification to add
     */
    protected void addClassificationToGraph(String                elementGUID,
                                            ElementClassification classification)
    {
        if (classification != null)
        {
            StringBuilder stringBuilder = new StringBuilder();
            if (classification.getClassificationProperties() != null)
            {
                for (String propertyName : classification.getClassificationProperties().keySet())
                {
                    stringBuilder.append(propertyName);

                    Object propertyValue = classification.getClassificationProperties().get(propertyName);
                    if (propertyValue != null)
                    {
                        stringBuilder.append("\n");
                        stringBuilder.append(" - ");
                        stringBuilder.append(propertyValue);
                        stringBuilder.append("\n");
                    }
                }
            }

            appendNewMermaidNode(elementGUID + ":" + classification.getClassificationName(),
                                 stringBuilder.toString(),
                                 classification.getClassificationName(),
                                 getVisualStyleForClassification(classification.getClassificationName()));
        }
    }

    /**
     * Return the standard visual styles for entities.
     *
     * @param elementHeader header of the entity
     * @return visual style to use
     */
    protected String getTypeNameForEntity(ElementHeader elementHeader)
    {
        if (elementHeader.getTemplate() != null)
        {
            return elementHeader.getType().getTypeName() + " [" + elementHeader.getTemplate().getClassificationName() + "]";
        }

        if (elementHeader.getCalculatedValue() != null)
        {
            return elementHeader.getType().getTypeName() + " [" + elementHeader.getCalculatedValue().getClassificationName() + "]";
        }

        if (elementHeader.getPrimaryKey() != null)
        {
            return elementHeader.getType().getTypeName() + " [" + elementHeader.getPrimaryKey().getClassificationName() + "]";
        }

        if (elementHeader.getDuplicateClassifications() != null)
        {
            for (ElementClassification classification : elementHeader.getDuplicateClassifications())
            {
                if (classification != null)
                {
                    return elementHeader.getType().getTypeName() + " [" + classification.getClassificationName() + "]";
                }
            }
        }


        if (elementHeader.getProjectRoles() != null)
        {
            for (ElementClassification classification : elementHeader.getProjectRoles())
            {
                if (classification != null)
                {
                    return elementHeader.getType().getTypeName() + " [" + classification.getClassificationName() + "]";
                }
            }
        }

        if (elementHeader.getCollectionRoles() != null)
        {
            for (ElementClassification classification : elementHeader.getCollectionRoles())
            {
                if (classification != null)
                {
                    return elementHeader.getType().getTypeName() + " [" + classification.getClassificationName() + "]";
                }
            }
        }

        if (elementHeader.getServerPurposes() != null)
        {
            for (ElementClassification classification : elementHeader.getServerPurposes())
            {
                if (classification != null)
                {
                    return elementHeader.getType().getTypeName() + " [" + classification.getClassificationName() + "]";
                }
            }
        }

        if (elementHeader.getOtherClassifications() != null)
        {
            for (ElementClassification classification : elementHeader.getOtherClassifications())
            {
                if (checkForClassificationTypeName(classification.getClassificationName()))
                {
                    return elementHeader.getType().getTypeName() + " [" + classification.getClassificationName() + "]";
                }
            }
        }

        return elementHeader.getType().getTypeName();
    }


    /**
     * Return the type name label.
     *
     * @param openMetadataElement header of the entity
     * @return visual style to use
     */
    protected String getTypeNameForEntity(OpenMetadataElement openMetadataElement)
    {
        if (openMetadataElement.getClassifications() != null)
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (checkForClassificationTypeName(classification.getClassificationName()))
                {
                    return openMetadataElement.getType().getTypeName() + " [" + classification.getClassificationName() + "]";
                }
            }
        }

        return openMetadataElement.getType().getTypeName();
    }


    /**
     * Return the type name label.
     *
     * @param openMetadataElement header of the entity stub
     * @return visual style to use
     */
    protected String getTypeNameForEntity(OpenMetadataElementStub openMetadataElement)
    {
        if (openMetadataElement.getClassifications() != null)
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (checkForClassificationTypeName(classification.getClassificationName()))
                {
                    return openMetadataElement.getType().getTypeName() + " [" + classification.getClassificationName() + "]";
                }
            }
        }

        return openMetadataElement.getType().getTypeName();
    }


    /**
     * Return the standard visual styles for entities.
     *
     * @param elementHeader header of the entity
     * @param defaultVisualStyle default style to use if no special style defined
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForEntity(ElementHeader elementHeader,
                                                  VisualStyle   defaultVisualStyle)
    {
        VisualStyle typeBasedStyle = this.getVisualStyleForEntityType(elementHeader, defaultVisualStyle);

        return this.getVisualStyleForClassifications(elementHeader, typeBasedStyle);
    }


    /**
     * Return the standard visual styles for entities.
     *
     * @param openMetadataElement header of the entity
     * @param defaultVisualStyle default style to use if no special style defined
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForEntity(OpenMetadataElement openMetadataElement,
                                                  VisualStyle         defaultVisualStyle)
    {
        VisualStyle typeBasedStyle = this.getVisualStyleForEntityType(openMetadataElement, defaultVisualStyle);

        return this.getVisualStyleForClassifications(openMetadataElement, typeBasedStyle);
    }


    /**
     * Return the standard visual styles for entities.
     *
     * @param openMetadataElement header of the entity
     * @param defaultVisualStyle default style to use if no special style defined
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForEntity(OpenMetadataElementStub openMetadataElement,
                                                  VisualStyle             defaultVisualStyle)
    {
        VisualStyle typeBasedStyle = this.getVisualStyleForEntityType(openMetadataElement, defaultVisualStyle);

        return this.getVisualStyleForClassifications(openMetadataElement, typeBasedStyle);
    }


    /**
     * Return the standard visual styles for entities.
     *
     * @param elementControlHeader header of the entity
     * @param defaultVisualStyle default style to use if no special style defined
     * @return visual style to use
     */
    protected VisualStyle getVisualStyleForEntityType(ElementControlHeader elementControlHeader,
                                                      VisualStyle          defaultVisualStyle)
    {
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.EXTERNAL_ID.typeName))
        {
            return VisualStyle.EXTERNAL_ID;
        }
        else if ((propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.COMMENT.typeName)) ||
                 (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.LIKE.typeName)) ||
                 (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.RATING.typeName)))
        {
            return VisualStyle.FEEDBACK;
        }
        else if ((propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.SEARCH_KEYWORD.typeName)) ||
                 (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.INFORMAL_TAG.typeName)))
        {
            return VisualStyle.TAG;
        }
        else if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.ACTOR.typeName))
        {
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.USER_IDENTITY.typeName))
            {
                return VisualStyle.USER_IDENTITY;
            }
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.TEAM.typeName))
            {
                return VisualStyle.GOVERNANCE_TEAM;
            }

            return VisualStyle.GOVERNANCE_ACTOR;
        }
        else if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.COLLECTION.typeName))
        {
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DIGITAL_PRODUCT.typeName))
            {
                return VisualStyle.DIGITAL_PRODUCT;
            }

            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.AGREEMENT.typeName))
            {
                if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName))
                {
                    return VisualStyle.DIGITAL_SUBSCRIPTION;
                }

                return VisualStyle.AGREEMENT;
            }

            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName))
            {
                return VisualStyle.DATA_DICTIONARY;
            }

            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_SPEC_COLLECTION.typeName))
            {
                return VisualStyle.DATA_SPEC;
            }

            return VisualStyle.COLLECTION;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.EXTERNAL_REFERENCE.typeName))
        {
            return VisualStyle.EXTERNAL_REFERENCE;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.HOST.typeName))
        {
            return VisualStyle.HOST;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName))
        {
            return VisualStyle.GOVERNANCE_ACTION;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.ENGINE_ACTION.typeName))
        {
            return VisualStyle.ENGINE_ACTION;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName))
        {
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName))
            {
                return VisualStyle.GOVERNANCE_ACTION_PROCESS_STEP;
            }

            return VisualStyle.GOVERNANCE_ACTION;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName))
        {
            return VisualStyle.GOVERNANCE_ACTION;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.VALID_VALUE_DEFINITION.typeName))
        {
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.VALID_VALUE_DEFINITION.typeName))
            {
                return VisualStyle.VALID_VALUE_SET;
            }

            return VisualStyle.VALID_VALUE;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.ASSET.typeName))
        {
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_ASSET.typeName))
            {
                if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.FILE_FOLDER.typeName))
                {
                    if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_FOLDER.typeName))
                    {
                        return VisualStyle.DATA_STORE;
                    }

                    return VisualStyle.FILE_FOLDER;
                }

                if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_STORE.typeName))
                {
                    if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_FILE.typeName))
                    {
                        return VisualStyle.DATA_FILE;
                    }

                    return VisualStyle.DATA_STORE;
                }

                if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
                {
                    return VisualStyle.DEPLOYED_DB_SCHEMA;
                }

                return VisualStyle.DATA_ASSET;
            }

            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.IT_INFRASTRUCTURE.typeName))
            {
                return VisualStyle.IT_ASSET;
            }

            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.PROCESS.typeName))
            {
                if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DEPLOYED_API.typeName))
                {
                    return VisualStyle.DEPLOYED_API;
                }
                if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DEPLOYED_CONNECTOR.typeName))
                {
                    return VisualStyle.CONNECTOR;
                }

                return VisualStyle.PROCESS;
            }

            return VisualStyle.ASSET;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName))
        {
            return VisualStyle.INFORMATION_SUPPLY_CHAIN;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.SOLUTION_BLUEPRINT.typeName))
        {
            return VisualStyle.SOLUTION_BLUEPRINT;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.ACTOR.typeName))
        {
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.TEAM.typeName))
            {
                return VisualStyle.GOVERNANCE_TEAM;
            }

            return VisualStyle.GOVERNANCE_ACTOR;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.SOLUTION_PORT.typeName))
        {
            return VisualStyle.SOLUTION_PORT;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.SOLUTION_COMPONENT.typeName))
        {
            return VisualStyle.DEFAULT_SOLUTION_COMPONENT;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GOVERNANCE_DEFINITION.typeName))
        {
            if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GOVERNANCE_METRIC.typeName))
            {
                return VisualStyle.GOVERNANCE_METRIC;
            }

            return VisualStyle.GOVERNANCE_DEFINITION;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.PROJECT.typeName))
        {
            return VisualStyle.PROJECT;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_STRUCTURE.typeName))
        {
            return VisualStyle.DATA_STRUCTURE;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_FIELD.typeName))
        {
            return VisualStyle.DATA_FIELD;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.DATA_CLASS.typeName))
        {
            return VisualStyle.DATA_CLASS;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GLOSSARY.typeName))
        {
            return VisualStyle.GLOSSARY;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.GLOSSARY_TERM.typeName))
        {
            return VisualStyle.GLOSSARY_TERM;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.SCHEMA_ELEMENT.typeName))
        {
            return VisualStyle.SCHEMA_ELEMENT;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
        {
            return VisualStyle.REQUEST_FOR_ACTION;
        }
        if (propertyHelper.isTypeOf(elementControlHeader, OpenMetadataType.TO_DO.typeName))
        {
            return VisualStyle.TO_DO;
        }

        return defaultVisualStyle;
    }


    /**
     * Use the type of the relationship to determine the shape of a linked entity.
     *
     * @param relationshipHeader header of the relationship
     * @return visual style enum
     */
    protected VisualStyle getVisualStyleForRelationship(ElementControlHeader relationshipHeader)
    {
        VisualStyle visualStyle = VisualStyle.LINKED_ELEMENT;

        if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
        {
            visualStyle = VisualStyle.DEFAULT_SOLUTION_COMPONENT;
        }
        else if (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName))
        {
            visualStyle = VisualStyle.HOST;
        }
        else if ((propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName)) ||
                (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName)) ||
                (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName)) ||
                (propertyHelper.isTypeOf(relationshipHeader, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName)))
        {
            visualStyle = VisualStyle.LINEAGE_ELEMENT;
        }

        return visualStyle;
    }


    /**
     * Map the solution component type to an appropriate visual style.
     *
     * @param solutionComponentType type of component
     * @return visual style enum
     */
    protected VisualStyle getVisualStyleForSolutionComponent(String solutionComponentType)
    {
        if (solutionComponentType != null)
        {
            for (SolutionComponentVisualStyle solutionComponentVisualStyle : SolutionComponentVisualStyle.values())
            {
                if (solutionComponentType.equals(solutionComponentVisualStyle.getSolutionComponentType().getSolutionComponentType()))
                {
                    return solutionComponentVisualStyle.getVisualStyle();
                }
            }
        }

        return VisualStyle.DEFAULT_SOLUTION_COMPONENT;
    }


    /**
     * Convert a type name into a spaced string to allow the names to wrap nicely in a mermaid box.
     *
     * @param typeName type name
     * @return spaced type name
     */
    protected String addSpacesToTypeName(String typeName)
    {
        StringBuilder spacedName = new StringBuilder();

        for (char c : typeName.toCharArray())
        {
            if ((Character.isUpperCase(c)) && (! spacedName.isEmpty()))
            {
                spacedName.append(' ');
            }

            spacedName.append(c);
        }

        return spacedName.toString();
    }


    /**
     * Convert an array into a comma separated string.
     *
     * @param labelValues array of labels
     * @return string value
     */
    public String getListLabel(List<String> labelValues)
    {
        if (labelValues != null)
        {
            StringBuilder stringBuilder = new StringBuilder();
            boolean firstValue = true;

            for (String labelValue : labelValues)
            {
                if (! firstValue)
                {
                    stringBuilder.append(",\n");
                }

                firstValue = false;
                stringBuilder.append(labelValue);
            }

            return stringBuilder.toString();
        }

        return "";
    }


    /**
     * Add a node to the graph for a metadata element summary.
     *
     * @param elementSummary entity summary
     * @param defaultVisualStyle default style to use if nothing is explicitly defined for the element's type or
     *                          classifications.
     */
    public void appendNewMermaidNode(MetadataElementSummary elementSummary,
                                     VisualStyle            defaultVisualStyle)
    {
        this.appendNewMermaidNode(elementSummary.getElementHeader().getGUID(),
                                  this.getNodeDisplayName(elementSummary),
                                  this.getTypeNameForEntity(elementSummary.getElementHeader()),
                                  this.getVisualStyleForEntity(elementSummary.getElementHeader(), defaultVisualStyle));
    }


    /**
     * Create a node in the mermaid graph.
     *
     * @param currentNodeName unique name/identifier
     * @param currentDisplayName display name
     * @param currentType type of element
     */
    public void appendMermaidNode(String        currentNodeName,
                                  String        currentDisplayName,
                                  String        currentType)
    {
        mermaidGraph.append(this.lookupNodeName(currentNodeName));
        mermaidGraph.append("(\"`*");
        mermaidGraph.append(addSpacesToTypeName(currentType));
        mermaidGraph.append("*\n");

        if (currentDisplayName != null)
        {
            mermaidGraph.append("**");
            mermaidGraph.append(removeDoubleSlash(currentDisplayName.trim()));
            mermaidGraph.append("**");
        }

        mermaidGraph.append("`\")\n");
    }


    /**
     * Create a node in the mermaid graph.
     *
     * @param currentNodeName unique name/identifier
     * @param currentDisplayName display name
     * @param currentType type of element
     * @param visualStyle mermaid defined shape and colour value
     * @return whether a new node was created or not.
     */
    public boolean appendNewMermaidNode(String      currentNodeName,
                                        String      currentDisplayName,
                                        String      currentType,
                                        VisualStyle visualStyle)
    {
        if (!usedNodeNames.contains(currentNodeName))
        {
            usedNodeNames.add(currentNodeName);

            if (visualStyle != null)
            {
                nodeColours.put(this.lookupNodeName(currentNodeName), visualStyle);
            }

            mermaidGraph.append(this.lookupNodeName(currentNodeName));
            mermaidGraph.append("@{ shape: ");
            if ((visualStyle != null) && (visualStyle.getShape() != null))
            {
                mermaidGraph.append(visualStyle.getShape());
            }
            else
            {
                mermaidGraph.append("rounded");
            }
            mermaidGraph.append(", label: \"*");
            mermaidGraph.append(addSpacesToTypeName(currentType));
            mermaidGraph.append("*\n");
            if (currentDisplayName != null)
            {
                mermaidGraph.append("**");
                mermaidGraph.append(removeDoubleSlash(currentDisplayName.trim()));
                mermaidGraph.append("**");
            }
            mermaidGraph.append("\"}\n");

            return true;
        }

        return false;
    }


    /**
     * Create a node in the mermaid graph.
     *
     * @param currentNodeName unique name/identifier
     * @param currentDisplayName display name
     * @param currentType type of element
     * @param additionalProperties additional properties to add to the node
     * @param visualStyle mermaid defined shape and colour value
     * @return whether a new node was created or not.
     */
    public boolean appendNewMermaidNode(String              currentNodeName,
                                        String              currentDisplayName,
                                        String              currentType,
                                        Map<String, String> additionalProperties,
                                        VisualStyle         visualStyle)
    {
        if (!usedNodeNames.contains(currentNodeName))
        {
            usedNodeNames.add(currentNodeName);

            if (visualStyle != null)
            {
                nodeColours.put(this.lookupNodeName(currentNodeName), visualStyle);
            }

            mermaidGraph.append(this.lookupNodeName(currentNodeName));
            mermaidGraph.append("@{ shape: ");
            if ((visualStyle != null) && (visualStyle.getShape() != null))
            {
                mermaidGraph.append(visualStyle.getShape());
            }
            else
            {
                mermaidGraph.append("rounded");
            }
            mermaidGraph.append(", label: \"*");
            mermaidGraph.append(addSpacesToTypeName(currentType));
            mermaidGraph.append("*\n**");
            mermaidGraph.append(removeDoubleSlash(currentDisplayName));
            mermaidGraph.append("**");

            if ((additionalProperties != null) && (! additionalProperties.isEmpty()))
            {
                for (String propertyName : additionalProperties.keySet())
                {
                    mermaidGraph.append("\n");
                    mermaidGraph.append(propertyName);
                    mermaidGraph.append(": ");
                    mermaidGraph.append(removeDoubleSlash(additionalProperties.get(propertyName)));
                }
            }
            mermaidGraph.append("\"}\n");

            return true;
        }

        return false;
    }


    /**
     * If a display name has part of a URL in it (eg it is from a qualified name), Mermaid displays "unsupported link"
     * rather than the display name.  This change puts a space between the two slashes to allow the display.
     * In addition, some display names include messages that have double quotes in their content.  This removes them
     * to avoid confusing mermaid.
     *
     * @param displayName original display name
     * @return doctored display name
     */
    private String removeDoubleSlash(String displayName)
    {
        if (displayName != null)
        {
            String quotesGone = displayName.replaceAll("\"", "'");
            return quotesGone.replaceAll("//", "/ /");
        }

        return null;
    }


    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     * @param lineStyle required style of line
     */
    public void appendMermaidStyledLine(String    lineName,
                                        String    end1Id,
                                        String    label,
                                        String    end2Id,
                                        LineStyle lineStyle)
    {
        if (lineStyle != null)
        {
            switch (lineStyle)
            {
                case LONG -> appendMermaidLongLine(lineName, end1Id, label, end2Id);
                case THIN -> appendMermaidThinLine(lineName, end1Id, label, end2Id);
                case DOTTED -> appendMermaidDottedLine(lineName, end1Id, label, end2Id);
                case INVISIBLE -> appendInvisibleMermaidLine(end1Id, end2Id);
                case ANIMATED_LONG -> appendMermaidLongAnimatedLine(lineName, end1Id, label, end2Id);
                default -> appendMermaidLine(lineName, end1Id, label, end2Id);
            }
        }
        else
        {
            appendMermaidLine(lineName, end1Id, label, end2Id);
        }
    }


    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidThinLine(String lineName,
                                      String end1Id,
                                      String label,
                                      String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }

            mermaidGraph.append(this.lookupNodeName(end1Id));

            if (label != null)
            {
                mermaidGraph.append("-->|\"");
                mermaidGraph.append(label);
                mermaidGraph.append("\"|");
            }
            else
            {
                mermaidGraph.append("-->");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }



    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidLongAnimatedLine(String lineName,
                                              String end1Id,
                                              String label,
                                              String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }

            mermaidGraph.append(this.lookupNodeName(end1Id));
            mermaidGraph.append(" ");

            if (lineName != null)
            {
                mermaidGraph.append(this.lookupNodeName(lineName));
                mermaidGraph.append("@");

                animatedLinkNames.add(this.lookupNodeName(lineName));
            }

            if (label != null)
            {

                mermaidGraph.append("-- \"");
                mermaidGraph.append(label);
                mermaidGraph.append("\" ------>");
            }
            else
            {
                mermaidGraph.append("------>");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }


    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidLongLine(String lineName,
                                      String end1Id,
                                      String label,
                                      String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }

            mermaidGraph.append(this.lookupNodeName(end1Id));
            mermaidGraph.append(" ");

            if (label != null)
            {
                mermaidGraph.append("-- \"");
                mermaidGraph.append(label);
                mermaidGraph.append("\" ------>");
            }
            else
            {
                mermaidGraph.append("------>");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }


    /**
     * Append a new single pixel width line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidDottedLine(String lineName,
                                        String end1Id,
                                        String label,
                                        String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }

            mermaidGraph.append(this.lookupNodeName(end1Id));

            if (label != null)
            {
                mermaidGraph.append("-. \"");
                mermaidGraph.append(label);
                mermaidGraph.append("\" .->");
            }
            else
            {
                mermaidGraph.append("-.->");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }


    /**
     * Append a line to the graph.
     *
     * @param lineName unique identifier of the line - may be null
     * @param end1Id identifier of the starting end
     * @param label label for the line
     * @param end2Id identifier of the ending end
     */
    public void appendMermaidLine(String lineName,
                                  String end1Id,
                                  String label,
                                  String end2Id)
    {
        if ((lineName == null) || (! usedLinkNames.contains(lineName)))
        {
            if (lineName != null)
            {
                usedLinkNames.add(lineName);
            }
            mermaidGraph.append(this.lookupNodeName(end1Id));

            if (label != null)
            {
                mermaidGraph.append("==>|\"");
                mermaidGraph.append(label);
                mermaidGraph.append("\"|");
            }
            else
            {
                mermaidGraph.append("==>");
            }

            mermaidGraph.append(this.lookupNodeName(end2Id));
            mermaidGraph.append("\n");
        }
    }


    /**
     * Append an invisible line to the graph.  This is used to line up descriptions etc
     *
     * @param end1Id identifier of the starting end
     * @param end2Id identifier of the ending end
     */
    public void appendInvisibleMermaidLine(String end1Id,
                                           String end2Id)
    {
        mermaidGraph.append(this.lookupNodeName(end1Id));
        mermaidGraph.append("~~~");
        mermaidGraph.append(this.lookupNodeName(end2Id));
        mermaidGraph.append("\n");
    }


    /**
     * Start a subgraph in the mermaid graph.
     *
     * @param subgraphName name of subgraph
     * @param visualStyle style of subgraph background
     */
    public void startSubgraph(String      subgraphName,
                              VisualStyle visualStyle)
    {
        startSubgraph(subgraphName, visualStyle, null);
    }


    /**
     * Start a subgraph in the mermaid graph.
     *
     * @param subgraphName name of subgraph
     * @param visualStyle style of subgraph background
     */
    public void startSubgraph(String      subgraphName,
                              VisualStyle visualStyle,
                              String      direction)
    {
        String subgraphId = this.lookupNodeName(subgraphName);

        mermaidGraph.append("subgraph ");
        mermaidGraph.append(subgraphId);
        mermaidGraph.append(" [");
        mermaidGraph.append(subgraphName);
        mermaidGraph.append("]\n");

        nodeColours.put(subgraphId, visualStyle);

        if (direction != null)
        {
            mermaidGraph.append("direction ");
            mermaidGraph.append(direction);
            mermaidGraph.append("\n");
        }
    }


    public void endSubgraph()
    {
        mermaidGraph.append("end\n");
    }



    /**
     * Remove all the spaces from the node name along with the curly braces - found in the templates.
     *
     * @param currentNodeName qualifiedName
     * @return qualified name without spaces
     */
    String removeSpaces(String currentNodeName)
    {
        if (currentNodeName != null)
        {
            String noSpaces = currentNodeName.replaceAll("\\s+", "");
            return noSpaces.replaceAll("[\\[\\](){}]", "");
        }

        return null;
    }


    /**
     * Return the name of the node to use in the mermaid graph.
     *
     * @param guid unique identifier of element
     * @return nodeId to use in mermaid
     */
    String lookupNodeName(String guid)
    {
        String nodeId = guidToNodeIdMap.get(guid);

        if (nodeId == null)
        {
            nodeId = Integer.toString(guidToNodeIdMap.size() + 1);
            guidToNodeIdMap.put(guid, nodeId);
        }

        return nodeId;
    }


    /**
     * Extract the name of a node.
     *
     * @param metadataElementSummary description of an element
     * @return string name
     */
    protected String getNodeDisplayName(MetadataElementSummary metadataElementSummary)
    {
        String nodeDisplayName = null;

        if (metadataElementSummary.getProperties() != null)
        {
            nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.RESOURCE_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.ROLE.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.FULL_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.USER_ID.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.DISTINGUISHED_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.IDENTIFIER.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.ANNOTATION_TYPE.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.STARS.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.URL.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = metadataElementSummary.getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
            }
        }

        if (nodeDisplayName == null)
        {
            if (metadataElementSummary.getElementHeader().getType().getTypeName().equals(OpenMetadataType.LIKE.typeName))
            {
                nodeDisplayName = metadataElementSummary.getElementHeader().getVersions().getCreatedBy();
            }
            else
            {
                nodeDisplayName = metadataElementSummary.getElementHeader().getGUID();
            }
        }

        return nodeDisplayName;
    }


    /**
     * Extract the name of a node.
     *
     * @param openMetadataRootElement description of an element
     * @return string name
     */
    protected String getNodeDisplayName(OpenMetadataRootElement openMetadataRootElement)
    {
        String nodeDisplayName = null;

        if (openMetadataRootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            nodeDisplayName = referenceableProperties.getDisplayName();

            if (nodeDisplayName == null)
            {
                nodeDisplayName = referenceableProperties.getQualifiedName();
            }
        }
        else if (openMetadataRootElement.getProperties() instanceof LikeProperties)
        {
            nodeDisplayName = openMetadataRootElement.getElementHeader().getVersions().getCreatedBy();
        }

        if (nodeDisplayName == null)
        {
            if (openMetadataRootElement.getElementHeader().getType().getTypeName().equals(OpenMetadataType.LIKE.typeName))
            {
                nodeDisplayName = openMetadataRootElement.getElementHeader().getVersions().getCreatedBy();
            }
            else
            {
                nodeDisplayName = openMetadataRootElement.getElementHeader().getGUID();
            }
        }

        return nodeDisplayName;
    }


    /**
     * Extract the name of a node.
     *
     * @param elementHeader element header
     * @param properties description of an element
     * @return string name
     */
    protected String getNodeDisplayName(ElementHeader              elementHeader,
                                        OpenMetadataRootProperties properties)
    {
        Object nodeDisplayName = null;

        if (properties instanceof ReferenceableProperties referenceableProperties)
        {
            nodeDisplayName = referenceableProperties.getDisplayName();

            if (nodeDisplayName == null)
            {
                if (properties instanceof AssetProperties assetProperties)
                {
                    nodeDisplayName = assetProperties.getResourceName();
                }
                else if (properties instanceof ActorProperties)
                {
                    if (properties instanceof UserIdentityProperties userIdentityProperties)
                    {
                        nodeDisplayName = userIdentityProperties.getUserId();

                        if (nodeDisplayName == null)
                        {
                            nodeDisplayName = userIdentityProperties.getDistinguishedName();
                        }
                    }
                    else if (properties instanceof ActorRoleProperties actorRoleProperties)
                    {
                        nodeDisplayName = actorRoleProperties.getIdentifier();
                    }
                    else if (properties instanceof ActorProfileProperties)
                    {
                        if (properties instanceof PersonProperties personProperties)
                        {
                            nodeDisplayName = personProperties.getFullName();
                        }
                        else if (properties instanceof TeamProperties teamProperties)
                        {
                            nodeDisplayName = teamProperties.getIdentifier();
                        }
                    }
                }
                else if (properties instanceof ContributionRecordProperties contributionRecordProperties)
                {
                    nodeDisplayName = "karma points: " + contributionRecordProperties.getKarmaPoints();
                }
                else if (properties instanceof ProjectProperties projectProperties)
                {
                    nodeDisplayName = projectProperties.getIdentifier();
                }
                else if (properties instanceof ExternalReferenceProperties externalReferenceProperties)
                {
                    nodeDisplayName = externalReferenceProperties.getURL();
                }
                else if (properties instanceof ExternalIdProperties externalIdentifierProperties)
                {
                    nodeDisplayName = externalIdentifierProperties.getIdentifier();
                }
            }

            if (nodeDisplayName == null)
            {
                nodeDisplayName = referenceableProperties.getQualifiedName();
            }
        }
        else if (properties != null)
        {
            nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.DISPLAY_NAME.name);

            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.RESOURCE_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.FULL_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.USER_ID.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.DISTINGUISHED_NAME.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.IDENTIFIER.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.ANNOTATION_TYPE.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.STARS.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.URL.name);
            }
            if (nodeDisplayName == null)
            {
                nodeDisplayName = properties.getExtendedProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name);
            }
        }

        if (nodeDisplayName == null)
        {
            if (elementHeader.getType().getTypeName().equals(OpenMetadataType.LIKE.typeName))
            {
                nodeDisplayName = elementHeader.getVersions().getCreatedBy();
            }
            else
            {
                nodeDisplayName = elementHeader.getGUID();
            }
        }

        return nodeDisplayName.toString();
    }


    /**
     * Link the related element into the graph.
     *
     * @param relatedMetadataElement related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     */
    public void addRelatedNodeSummary(RelatedMetadataNodeSummary relatedMetadataElement,
                                      VisualStyle                visualStyle)
    {
        if (relatedMetadataElement != null)
        {
            addRelatedElementSummary(relatedMetadataElement, visualStyle, relatedMetadataElement.getStartingElementGUID());
        }
    }


    /**
     * Link the related element into the graph.
     *
     * @param relatedMetadataElement related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     * @param lineStyle visual style of lines
     */
    public void addRelatedNodeSummary(RelatedMetadataNodeSummary relatedMetadataElement,
                                      VisualStyle                visualStyle,
                                      LineStyle                  lineStyle)
    {
        if (relatedMetadataElement != null)
        {
            addRelatedElementSummary(relatedMetadataElement, visualStyle, relatedMetadataElement.getStartingElementGUID(), lineStyle);
        }
    }


    /**
     * Link the list of related elements into the graph.
     *
     * @param relatedMetadataElements list of related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     * @param startingEndId identity of the starting node
     */
    public void addRelatedElementSummaries(List<RelatedMetadataElementSummary> relatedMetadataElements,
                                           VisualStyle                         visualStyle,
                                           String                              startingEndId)
    {
        addRelatedElementSummaries(relatedMetadataElements, visualStyle, startingEndId, LineStyle.NORMAL);
    }


    /**
     * Link the list of related elements into the graph.
     *
     * @param relatedMetadataElements list of related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     * @param startingEndId identity of the starting node
     */
    public void addRelatedElementSummaries(List<RelatedMetadataElementSummary> relatedMetadataElements,
                                           VisualStyle                         visualStyle,
                                           String                              startingEndId,
                                           LineStyle                           lineStyle)
    {
        if (relatedMetadataElements != null)
        {
            for (RelatedMetadataElementSummary relatedMetadataElement : relatedMetadataElements)
            {
                addRelatedElementSummary(relatedMetadataElement, visualStyle, startingEndId, lineStyle);
            }
        }
    }


    /**
     * Link the related element into the graph.
     *
     * @param relatedMetadataElement related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     */
    public void addRelatedElementSummary(RelatedMetadataElementSummary relatedMetadataElement,
                                         VisualStyle                   visualStyle,
                                         String                        startingEndId)
    {
        addRelatedElementSummary(relatedMetadataElement, visualStyle, startingEndId, LineStyle.NORMAL);
    }


    /**
     * Link the related element into the graph.
     *
     * @param relatedMetadataElement related element (if null, nothing is added to the graph)
     * @param visualStyle visual style for new nodes
     */
    public void addRelatedElementSummary(RelatedMetadataElementSummary relatedMetadataElement,
                                         VisualStyle                   visualStyle,
                                         String                        startingEndId,
                                         LineStyle                     lineStyle)
    {
        if (relatedMetadataElement != null)
        {
            appendNewMermaidNode(relatedMetadataElement.getRelatedElement(), visualStyle);

            String label = null;

            if (relatedMetadataElement.getRelationshipProperties() != null)
            {
                label = relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);

                if (label == null)
                {
                    label = getCardinalityLabel(relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.POSITION.name),
                                                relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.MIN_CARDINALITY.name),
                                                relatedMetadataElement.getRelationshipProperties().get(OpenMetadataProperty.MAX_CARDINALITY.name));
                }
            }

            if (label == null)
            {
                label = this.addSpacesToTypeName(relatedMetadataElement.getRelationshipHeader().getType().getTypeName());
            }
            else
            {
                label = label + " [" + this.addSpacesToTypeName(relatedMetadataElement.getRelationshipHeader().getType().getTypeName()) + "]";
            }

            if (relatedMetadataElement.getRelatedElementAtEnd1())
            {
                appendMermaidStyledLine(relatedMetadataElement.getRelationshipHeader().getGUID(),
                                        relatedMetadataElement.getRelatedElement().getElementHeader().getGUID(),
                                        label,
                                        startingEndId,
                                        lineStyle);
            }
            else
            {
                appendMermaidStyledLine(relatedMetadataElement.getRelationshipHeader().getGUID(),
                                        startingEndId,
                                        label,
                                        relatedMetadataElement.getRelatedElement().getElementHeader().getGUID(),
                                        lineStyle);
            }

            if (relatedMetadataElement instanceof RelatedMetadataHierarchySummary relatedMetadataHierarchySummary)
            {
                this.addRelatedElementSummaries(relatedMetadataHierarchySummary.getNestedElements(),
                                                visualStyle,
                                                relatedMetadataHierarchySummary.getRelatedElement().getElementHeader().getGUID(),
                                                lineStyle);
            }
        }
    }


    /**
     * Take the string versions of the position, minCardinality and maxCardinality and turn them into a label.
     * If none of these properties are set then null is returned.
     *
     * @param positionString string value of the position attribute
     * @param minCardinalityString string value of the minCardinality attribute
     * @param maxCardinalityString string value of the maxCardinality attribute
     * @return label or null
     */
    protected String getCardinalityLabel(String positionString,
                                         String minCardinalityString,
                                         String maxCardinalityString)
    {
        if ((positionString == null) && (minCardinalityString == null) && (maxCardinalityString == null))
        {
            return null;
        }

        int position = 0;
        int minCardinality = 0;
        int maxCardinality = -1;

        if (positionString != null)
        {
            position = Integer.parseInt(positionString);
        }

        if (minCardinalityString != null)
        {
            minCardinality = Integer.parseInt(minCardinalityString);
        }

        if (minCardinalityString != null)
        {
            minCardinality = Integer.parseInt(minCardinalityString);
        }

        return getCardinalityLabel(position, minCardinality, maxCardinality);
    }


    /**
     * Take the position, minCardinality and maxCardinality attributes and turn them into a label.
     *
     * @param position position attribute
     * @param minCardinality minCardinality attribute
     * @param maxCardinality maxCardinality attribute
     * @return label
     */
    protected String getCardinalityLabel(int position,
                                         int minCardinality,
                                         int maxCardinality)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        stringBuilder.append(position);
        stringBuilder.append("] ");

        if (minCardinality < 0)
        {
            stringBuilder.append("*");
        }
        else
        {
            stringBuilder.append(minCardinality);
        }

        stringBuilder.append("..");

        if (maxCardinality < 0)
        {
            stringBuilder.append("*");
        }
        else
        {
            stringBuilder.append(maxCardinality);
        }

        return stringBuilder.toString();
    }


    /**
     * Determine the cardinality of the data field.
     *
     * @param memberDataFieldProperties relationship properties
     * @return label for the mermaid graph line
     */
    private String getCardinalityLinkName(MemberDataFieldProperties memberDataFieldProperties)
    {
        if (memberDataFieldProperties != null)
        {
            return this.getCardinalityLabel(memberDataFieldProperties.getPosition(),
                                            memberDataFieldProperties.getMinCardinality(),
                                            memberDataFieldProperties.getMaxCardinality());
        }

        return "*";
    }


    /**
     * Add a solution component to graph.
     *
     * @param parentNodeName identifier of the parent node (maybe null)
     * @param parentLinkLabel add label to parent link - also optional
     * @param solutionComponentElement element to process
     * @param solutionLinkingWireGUIDs list of solution wires already defined
     * @param fullDisplay print all elements
     */
    protected void addSolutionComponentToGraph(String                   parentNodeName,
                                               String                   parentLinkLabel,
                                               SolutionComponentElement solutionComponentElement,
                                               List<String>             solutionLinkingWireGUIDs,
                                               boolean                  fullDisplay)

    {
        if (solutionComponentElement != null)
        {
            String currentNodeName    = solutionComponentElement.getElementHeader().getGUID();
            String currentDisplayName = solutionComponentElement.getProperties().getDisplayName();

            if (currentDisplayName == null)
            {
                currentDisplayName = solutionComponentElement.getProperties().getQualifiedName();
            }

            if (solutionComponentElement.getProperties() != null)
            {
                appendNewMermaidNode(currentNodeName,
                                     currentDisplayName,
                                     solutionComponentElement.getElementHeader().getType().getTypeName(),
                                     getVisualStyleForClassifications(solutionComponentElement.getElementHeader(),
                                                                      this.getVisualStyleForSolutionComponent(solutionComponentElement.getProperties().getSolutionComponentType())));
            }
            else
            {
                appendNewMermaidNode(currentNodeName,
                                     currentDisplayName,
                                     solutionComponentElement.getElementHeader().getType().getTypeName(),
                                     getVisualStyleForEntity(solutionComponentElement.getElementHeader(),
                                                             VisualStyle.DEFAULT_SOLUTION_COMPONENT));
            }

            if (solutionComponentElement.getWiredToLinks() != null)
            {
                for (WiredSolutionComponent line : solutionComponentElement.getWiredToLinks())
                {
                    if ((line != null) && (! solutionLinkingWireGUIDs.contains(line.getElementHeader().getGUID())))
                    {
                        String relatedComponentDisplayName = getNodeDisplayName(line.getLinkedElement());

                        if (line.getLinkedElement().getProperties() != null)
                        {
                            relatedComponentDisplayName = this.getNodeDisplayName(line.getLinkedElement());
                        }

                        appendNewMermaidNode(line.getLinkedElement().getElementHeader().getGUID(),
                                             relatedComponentDisplayName,
                                             line.getLinkedElement().getElementHeader().getType().getTypeName(),
                                             getVisualStyleForClassifications(line.getElementHeader(),
                                                                              this.getVisualStyleForSolutionComponent(line.getLinkedElement().getElementHeader().getType().getTypeName())));

                        List<String> labelList = new ArrayList<>();

                        if ((line.getProperties() != null) && (line.getProperties().getLabel() != null))
                        {
                            labelList.add(line.getProperties().getLabel());
                            labelList.add("[" + this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()) + "]");
                        }
                        else
                        {
                            labelList.add(this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()));
                        }

                        this.appendMermaidLine(line.getElementHeader().getGUID(),
                                               line.getLinkedElement().getElementHeader().getGUID(),
                                               this.getListLabel(labelList),
                                               solutionComponentElement.getElementHeader().getGUID());

                        solutionLinkingWireGUIDs.add(line.getElementHeader().getGUID());
                    }
                }
            }

            if (solutionComponentElement.getWiredFromLinks() != null)
            {
                for (WiredSolutionComponent line : solutionComponentElement.getWiredFromLinks())
                {
                    if ((line != null) && (! solutionLinkingWireGUIDs.contains(line.getElementHeader().getGUID())))
                    {
                        appendNewMermaidNode(line.getLinkedElement(), VisualStyle.DEFAULT_SOLUTION_COMPONENT);

                        List<String> labelList = new ArrayList<>();

                        if ((line.getProperties() != null) && (line.getProperties().getLabel() != null))
                        {
                            labelList.add(line.getProperties().getLabel());
                            labelList.add("[" + this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()) + "]");
                        }
                        else
                        {
                            labelList.add(this.addSpacesToTypeName(line.getElementHeader().getType().getTypeName()));
                        }

                        this.appendMermaidLine(line.getElementHeader().getGUID(),
                                               solutionComponentElement.getElementHeader().getGUID(),
                                               this.getListLabel(labelList),
                                               line.getLinkedElement().getElementHeader().getGUID());

                        solutionLinkingWireGUIDs.add(line.getElementHeader().getGUID());
                    }
                }
            }

            if (solutionComponentElement.getActors() != null)
            {
                for (RelatedMetadataElementSummary line : solutionComponentElement.getActors())
                {
                    if (line != null)
                    {
                        String actorRoleName = getNodeDisplayName(line.getRelatedElement());

                        appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                             actorRoleName,
                                             line.getRelatedElement().getElementHeader().getType().getTypeName(),
                                             getVisualStyleForEntity(line.getRelatedElement().getElementHeader(),
                                                                     VisualStyle.GOVERNANCE_ACTOR));

                        String actorRoleDescription = line.getRelationshipProperties().get(OpenMetadataProperty.ROLE.name);

                        if (actorRoleDescription == null)
                        {
                            actorRoleDescription = this.addSpacesToTypeName(this.getTypeNameForEntity(line.getRelatedElement().getElementHeader()));
                        }

                        this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                               line.getRelatedElement().getElementHeader().getGUID(),
                                               this.getListLabel(Collections.singletonList(actorRoleDescription)),
                                               solutionComponentElement.getElementHeader().getGUID());
                    }
                }
            }

            if (fullDisplay)
            {
                if (solutionComponentElement.getBlueprints() != null)
                {
                    for (RelatedMetadataElementSummary line : solutionComponentElement.getBlueprints())
                    {
                        if (line != null)
                        {
                            String actorRoleName = getNodeDisplayName(line.getRelatedElement());

                            appendNewMermaidNode(line.getRelatedElement().getElementHeader().getGUID(),
                                                 actorRoleName,
                                                 line.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                 getVisualStyleForEntity(line.getRelatedElement().getElementHeader(),
                                                                         VisualStyle.SOLUTION_BLUEPRINT));

                            String label = null;

                            if (line.getRelationshipProperties() != null)
                            {
                                label = line.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);
                            }

                            if (label == null)
                            {
                                label = this.addSpacesToTypeName(line.getRelatedElement().getElementHeader().getType().getTypeName());
                            }

                            this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                   line.getRelatedElement().getElementHeader().getGUID(),
                                                   this.getListLabel(Collections.singletonList(label)),
                                                   solutionComponentElement.getElementHeader().getGUID());
                        }
                    }
                }

                if (solutionComponentElement.getImplementations() != null)
                {
                    for (RelatedMetadataElementSummary line : solutionComponentElement.getImplementations())
                    {
                        if (line != null)
                        {
                            appendNewMermaidNode(line.getRelatedElement(), VisualStyle.INFORMATION_SUPPLY_CHAIN_IMPL);

                            String label = null;

                            if (line.getRelationshipProperties() != null)
                            {
                                label = line.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);
                            }

                            if (label == null)
                            {
                                label = this.addSpacesToTypeName(line.getRelationshipHeader().getType().getTypeName());
                            }

                            this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                   solutionComponentElement.getElementHeader().getGUID(),
                                                   this.getListLabel(Collections.singletonList(label)),
                                                   line.getRelatedElement().getElementHeader().getGUID());
                        }
                    }
                }

                if (solutionComponentElement.getOtherElements() != null)
                {
                    for (RelatedMetadataElementSummary line : solutionComponentElement.getOtherElements())
                    {
                        if (line != null)
                        {
                            appendNewMermaidNode(line.getRelatedElement(), VisualStyle.LINKED_ELEMENT);

                            String label = null;

                            if (line.getRelationshipProperties() != null)
                            {
                                label = line.getRelationshipProperties().get(OpenMetadataProperty.LABEL.name);
                            }

                            if (label == null)
                            {
                                label = this.addSpacesToTypeName(line.getRelationshipHeader().getType().getTypeName());
                            }

                            if (line.getRelatedElementAtEnd1())
                            {
                                this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                       line.getRelatedElement().getElementHeader().getGUID(),
                                                       this.getListLabel(Collections.singletonList(label)),
                                                       solutionComponentElement.getElementHeader().getGUID());
                            }
                            else
                            {
                                this.appendMermaidLine(line.getRelationshipHeader().getGUID(),
                                                       solutionComponentElement.getElementHeader().getGUID(),
                                                       this.getListLabel(Collections.singletonList(label)),
                                                       line.getRelatedElement().getElementHeader().getGUID());
                            }
                        }
                    }
                }
            }

            if (parentNodeName != null)
            {
                this.appendMermaidLine(null,
                                       parentNodeName,
                                       this.getListLabel(Collections.singletonList(parentLinkLabel)),
                                       solutionComponentElement.getElementHeader().getGUID());
            }
        }
    }


    /**
     * Add styling for nodes as requested.  These go at the end of the graph.
     */
    protected void addStyles(Map<String, VisualStyle> nodeColours)
    {
        if (! nodeColours.isEmpty())
        {
            for (String nodeId : nodeColours.keySet())
            {
                boolean isFirst = true;

                VisualStyle visualStyle = nodeColours.get(nodeId);

                if (visualStyle.getTextColour() != null || visualStyle.getFillColour() != null || visualStyle.getLineColour() != null)
                {
                    mermaidGraph.append("style ");
                    mermaidGraph.append(nodeId);

                    if (visualStyle.getTextColour() != null)
                    {
                        mermaidGraph.append(" color:");
                        mermaidGraph.append(visualStyle.getTextColour());

                        isFirst = false;
                    }

                    if (visualStyle.getFillColour() != null)
                    {
                        if (isFirst)
                        {
                            isFirst = false;
                        }
                        else
                        {
                            mermaidGraph.append(",");
                        }

                        mermaidGraph.append(" fill:");
                        mermaidGraph.append(visualStyle.getFillColour());
                    }

                    if (visualStyle.getLineColour() != null)
                    {
                        if (! isFirst)
                        {
                            mermaidGraph.append(",");
                        }

                        mermaidGraph.append(" stroke:");
                        mermaidGraph.append(visualStyle.getLineColour());
                    }

                    mermaidGraph.append("\n");
                }
            }
        }
    }


    /**
     * Add the names of links that should be animated
     */
    private void addAnimation()
    {
        for (String lineName : animatedLinkNames)
        {
            mermaidGraph.append(lineName);
            mermaidGraph.append("@{ animation: fast }");
            mermaidGraph.append("\n");
        }
    }



    /**
     * Clear the graph
     */
    protected void clearGraph()
    {
        mermaidGraph = null;
    }


    /**
     * Add anchors, any style requests and return the built mermaid graph.
     *
     * @param allAnchors should all anchors be added - or just those to known nodes?
     * @return string markdown
     */
    public String getMermaidGraph(boolean allAnchors)
    {
        if (mermaidGraph == null)
        {
            return null;
        }

        addAnchorLinks(allAnchors);

        return this.getMermaidGraph();
    }


    /**
     * Add any style requests and return the built mermaid graph.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        if (mermaidGraph == null)
        {
            return null;
        }

        addStyles(nodeColours);
        addAnimation();

        return mermaidGraph.toString();
    }
}
