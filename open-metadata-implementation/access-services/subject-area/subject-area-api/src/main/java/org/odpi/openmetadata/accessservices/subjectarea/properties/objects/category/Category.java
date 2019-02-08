/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;

public class Category extends Node{
    private GlossarySummary glossary =null;
    private CategorySummary parentCategory;

    public Category() {
        nodeType = NodeType.Category;
    }

    /**
     * The owning glossary for this category.
     * @return <code>GlossarySummary</code>
     */
    public GlossarySummary getGlossary() {
        return glossary;
    }

    public void setGlossary(GlossarySummary glossary) {
        this.glossary = glossary;
    }

    /**
     * "Identifies the parent category."
     * @return <code>CategorySummary</code>
     */
    public CategorySummary getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategorySummary parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("Category=");
        sb.append(super.toString(sb));


        if (glossary!=null ){
            sb.append(", glossary=");
            sb.append(glossary.toString(sb));

        }
        if (parentCategory!=null ){
            sb.append(", parent Category=");
            sb.append(parentCategory.toString(sb));

        }
        sb.append('}');
        return sb;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        Node node = (Node) o;
        if (!node.equals(this)) return false;
        if (glossary != null ? !glossary.equals(category.glossary) : category.glossary != null) return false;
        return  true;
    }

    @Override
    public int hashCode() {
        int  result =super.hashCode();
        result = 31 * result + (glossary != null ? glossary.hashCode() : 0);
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0);
        return result;
    }
}
