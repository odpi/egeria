/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * Creates a mermaid mind map rendering of the collection.
 */
public class CollectionMermaidMindMapBuilder
{
    private StringBuilder mermaidMindMap = new StringBuilder();


    /**
     * Constructor for the mind map builder.
     *
     * @param collection element to display
     */
    public CollectionMermaidMindMapBuilder(OpenMetadataRootElement collection)
    {
        if (collection.getCollectionMembers() != null)
        {
            mermaidMindMap.append("mindmap\n");
            mermaidMindMap.append("**");
            mermaidMindMap.append(collection.getElementHeader().getType().getTypeName());
            mermaidMindMap.append("** ");
            if (collection.getProperties() instanceof CollectionProperties collectionProperties)
            {
                mermaidMindMap.append(collectionProperties.getDisplayName());
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
                mermaidMindMap.append(collectionProperties.getDisplayName());
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
