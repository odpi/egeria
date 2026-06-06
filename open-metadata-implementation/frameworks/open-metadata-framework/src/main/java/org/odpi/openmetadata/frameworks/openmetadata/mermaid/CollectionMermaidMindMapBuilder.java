/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * Creates a mermaid mind map rendering of the collection.
 */
public class CollectionMermaidMindMapBuilder
{
    private       StringBuilder  mermaidMindMap = new StringBuilder();
    private final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Constructor for the mind map builder.
     *
     * @param collection element to display
     */
    public CollectionMermaidMindMapBuilder(OpenMetadataRootElement collection)
    {
        if (collection.getCollectionMembers() != null)
        {
            int nestedCollectionCount = 0;

            for (RelatedMetadataElementSummary nestedMember : collection.getCollectionMembers())
            {
                if ((nestedMember != null) &&
                        (propertyHelper.isTypeOf(nestedMember.getRelatedElement().getElementHeader(), OpenMetadataType.COLLECTION.typeName)))
                {
                    nestedCollectionCount++;
                }
            }

            if (nestedCollectionCount > 0)
            {
                mermaidMindMap.append("mindmap\n");
                mermaidMindMap.append("**");
                mermaidMindMap.append(collection.getElementHeader().getType().getTypeName());
                mermaidMindMap.append("** ");

                if (collection.getProperties() instanceof CollectionProperties collectionProperties)
                {
                    mermaidMindMap.append(removeTroublesomeCharacters(collectionProperties.getDisplayName()));
                }
                else
                {
                    mermaidMindMap.append(collection.getElementHeader().getGUID());
                }
                mermaidMindMap.append("\n");

                for (RelatedMetadataElementSummary nestedMember : collection.getCollectionMembers())
                {
                    if (nestedMember != null)
                    {
                        getNestedBranches(nestedMember, "    ");
                    }
                }
            }
            else
            {
                mermaidMindMap = null;
            }
        }
        else
        {
            mermaidMindMap = null;
        }
    }


    /**
     * Recursively build the nested branches of the collection.
     *
     * @param nestedMember current member of the collection
     * @param indent       required indentation
     */
    private void getNestedBranches(RelatedMetadataElementSummary nestedMember,
                                   String indent)
    {
        if (nestedMember.getRelatedElement().getProperties() instanceof CollectionProperties collectionProperties)
        {
            mermaidMindMap.append(indent);
            mermaidMindMap.append("**");
            mermaidMindMap.append(nestedMember.getRelatedElement().getElementHeader().getType().getTypeName());
            mermaidMindMap.append("** ");

            if (collectionProperties.getDisplayName() != null)
            {
                mermaidMindMap.append(removeTroublesomeCharacters(collectionProperties.getDisplayName()));
            }
            else
            {
                mermaidMindMap.append(nestedMember.getRelatedElement().getElementHeader().getGUID());
            }

            mermaidMindMap.append("\n");

            if (nestedMember instanceof RelatedMetadataHierarchySummary relatedMetadataHierarchySummary)
            {
                if (relatedMetadataHierarchySummary.getNestedElements() != null)
                {
                    for (RelatedMetadataElementSummary nestedMemberOfNestedMember : relatedMetadataHierarchySummary.getNestedElements())
                    {
                        if ((nestedMemberOfNestedMember != null) &&
                                (! nestedMemberOfNestedMember.getRelatedElement().getElementHeader().getType().getTypeName().equals(OpenMetadataType.DIGITAL_PRODUCT.typeName)) &&
                                (! nestedMemberOfNestedMember.getRelatedElement().getElementHeader().getType().getTypeName().equals(OpenMetadataType.AGREEMENT.typeName)))
                        {
                            getNestedBranches(nestedMemberOfNestedMember, indent + "    ");
                        }
                    }
                }
            }
        }
    }

    /**
     * If a display name has part of a URL in it (eg it is from a qualified name), Mermaid displays "unsupported link"
     * rather than the display name.  This change puts a space between the two slashes to allow the display.
     * In addition, some display names include messages that have double quotes in their content.  This removes them
     * to avoid confusing mermaid.
     * There also seems to be a problem wit the new placeholder properties
     *
     * @param displayName original display name
     * @return doctored display name
     */
    private String removeTroublesomeCharacters(String displayName)
    {
        if (displayName != null)
        {
            String quotesGone = displayName.replaceAll("\"", "'");
            String doubleSlashGone = quotesGone.replaceAll("//", "/ /");
            String placeholderStartGone = doubleSlashGone.replaceAll("~\\{", " *");
            return placeholderStartGone.replaceAll("}~", "* ");
        }

        return null;
    }

    /**
     * Return the mermaid mind map.
     *
     * @return string
     */
    public String getMermaidMindMap()
    {
        if (mermaidMindMap != null)
        {
            return mermaidMindMap.toString();
        }

        return null;
    }
}
