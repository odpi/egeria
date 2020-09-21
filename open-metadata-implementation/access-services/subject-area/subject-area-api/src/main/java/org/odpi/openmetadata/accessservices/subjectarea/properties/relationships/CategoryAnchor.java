/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineEnd;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CategoryAnchorRelationship is a relationship a Glossary and a Category.
 * It connects a glossary category with its owning glossary.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryAnchor extends Line {
    private String description = "Connects a glossary category with its owning glossary.";

    /*
     * Set up end 1.
     */
    protected final static String END_1_NODE_TYPE = "Glossary";
    protected final static String END_1_ATTRIBUTE_NAME = "anchor";
    protected final static String END_1_ATTRIBUTE_DESCRIPTION = "Owning glossary for this category.";
    protected final static RelationshipEndCardinality END_1_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
    protected final static LineEnd LINE_END_1 = new LineEnd(END_1_NODE_TYPE,
            END_1_ATTRIBUTE_NAME, END_1_ATTRIBUTE_DESCRIPTION, END_1_CARDINALITY);

    /*
     * Set up end 2.
     */
    protected final static String END_2_NODE_TYPE = "Category";
    protected final static String END_2_ATTRIBUTE_NAME = "categories";
    protected final static String END_2_ATTRIBUTE_DESCRIPTION = "Categories owned by this glossary.";
    protected final static RelationshipEndCardinality END_2_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
    protected final static LineEnd LINE_END_2 = new LineEnd(END_2_NODE_TYPE,
            END_2_ATTRIBUTE_NAME, END_2_ATTRIBUTE_DESCRIPTION, END_2_CARDINALITY);

    public CategoryAnchor() {
        super("CategoryAnchor", "c628938e-815e-47db-8d1c-59bb2e84e028", LINE_END_1, LINE_END_2);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append(" CategoryAnchorRelationship=");
        sb.append(super.toString(sb));
        sb.append(" CategoryAnchorRelationship Attributes{");
        sb.append("}");
        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}