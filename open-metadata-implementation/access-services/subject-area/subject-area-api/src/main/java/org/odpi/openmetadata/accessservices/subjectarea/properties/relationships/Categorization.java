/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineEnd;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TermCategorizationRelationship is a relationship between a Category and an Term. The Category categorises the Term
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Categorization extends Line {
    private String description = "Links a glossary term into a glossary category.";

    /*
     * Set up end 1.
     */
    protected final static String END_1_NODE_TYPE = "Category";
    protected final static String END_1_ATTRIBUTE_NAME = "categories";
    protected final static String END_1_ATTRIBUTE_DESCRIPTION = "Glossary categories that this term is linked to.";
    protected final static RelationshipEndCardinality END_1_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
    protected final static LineEnd LINE_END_1 = new LineEnd(END_1_NODE_TYPE,
            END_1_ATTRIBUTE_NAME, END_1_ATTRIBUTE_DESCRIPTION, END_1_CARDINALITY);

    /*
     * Set up end 2.
     */
    protected final static String END_2_NODE_TYPE = "Term";
    protected final static String END_2_ATTRIBUTE_NAME = "terms";
    protected final static String END_2_ATTRIBUTE_DESCRIPTION = "Glossary terms linked to this category.";
    protected final static RelationshipEndCardinality END_2_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
    protected final static LineEnd LINE_END_2 = new LineEnd(END_2_NODE_TYPE,
            END_2_ATTRIBUTE_NAME, END_2_ATTRIBUTE_DESCRIPTION, END_2_CARDINALITY);

    private TermRelationshipStatus status;

    /*
     * Set up end 2.
     */
    final String end2NodeType = "Term";
    final String end2AttributeName = "terms";
    final String end2AttributeDescription = "Glossary terms linked to this category.";
    final RelationshipEndCardinality end2Cardinality = RelationshipEndCardinality.ANY_NUMBER;
    private TermRelationshipStatus status;

    public Categorization() {
        super("TermCategorization", "696a81f5-ac60-46c7-b9fd-6979a1e7ad27", LINE_END_1, LINE_END_2);
    }

    /**
     * {@literal Explanation of why this term is in this categorization. }
     *
     * @return {@code String }
     */
    public String getDescription() {
        return this.description;
    }
    /**
     * {@literal Set the description of the relationship. }
     * @param description {@code String }
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * {@literal Status of the relationship. }
     *
     * @return {@code TermRelationshipStatus }
     */
    public TermRelationshipStatus getStatus() {
        return this.status;
    }

    public void setStatus(TermRelationshipStatus status) {
        this.status = status;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append(" TermCategorizationRelationship=");
        sb.append(super.toString(sb));
        sb.append(" TermCategorizationRelationship Attributes{");
        sb.append("description=").append(this.description).append(",");
        if (status != null) {
            sb.append("status=").append(status.name());
        }
        sb.append("}");
        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}