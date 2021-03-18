/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermAssignmentStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipEnd;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Links a glossary term to another element such as an asset or schema element to define its meaning.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SemanticAssignment extends Relationship {
    private String description = "Links a glossary term to another element such as an asset or schema element to define its meaning.";

    /*
     * Set up end 1.
     */
     private static final String END_1_NODE_TYPE = "Referenceable";
     private static final String END_1_ATTRIBUTE_NAME = "assignedElements";
     private static final String END_1_ATTRIBUTE_DESCRIPTION = "Elements identified as managing data that has the same meaning as this glossary term.";
     private static final RelationshipEndCardinality END_1_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
     private static final RelationshipEnd RELATIONSHIP_END_1 = new RelationshipEnd(END_1_NODE_TYPE,
                                                                           END_1_ATTRIBUTE_NAME, END_1_ATTRIBUTE_DESCRIPTION, END_1_CARDINALITY);

    /*
     * Set up end 2.
     */
     private static final String END_2_NODE_TYPE = "Term";
     private static final String END_2_ATTRIBUTE_NAME = "meaning";
     private static final String END_2_ATTRIBUTE_DESCRIPTION = "Semantic definition for this element.";
     private static final RelationshipEndCardinality END_2_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
     private static final RelationshipEnd RELATIONSHIP_END_2 = new RelationshipEnd(END_2_NODE_TYPE,
                                                                           END_2_ATTRIBUTE_NAME, END_2_ATTRIBUTE_DESCRIPTION, END_2_CARDINALITY);

    private String expression;
    private TermAssignmentStatus status;
    private Integer confidence;
    private String steward;
    private String source;

    public SemanticAssignment() {
        super("SemanticAssignment", RELATIONSHIP_END_1, RELATIONSHIP_END_2);
    }

    /**
     * {@literal Description of the relationship. }
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
     * {@literal Expression describing the relationship. }
     *
     * @return {@code String }
     */
    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * {@literal The status of the relationship. }
     *
     * @return {@code TermAssignmentStatus }
     */
    public TermAssignmentStatus getStatus() {
        return this.status;
    }

    public void setStatus(TermAssignmentStatus status) {
        this.status = status;
    }

    /**
     * {@literal Level of confidence in the correctness of the relationship. }
     *
     * @return {@code Integer }
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    /**
     * {@literal Person responsible for the relationship. }
     *
     * @return {@code String }
     */
    public String getSteward() {
        return this.steward;
    }

    public void setSteward(String steward) {
        this.steward = steward;
    }

    /**
     * {@literal Person, organization or automated process that created the relationship. }
     *
     * @return {@code String }
     */
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(" SemanticAssignment=");
        sb.append(super.toString(sb));
        sb.append(" SemanticAssignment Attributes{");
        sb.append("description=").append(this.description).append(",");
        sb.append("expression=").append(this.expression).append(",");
        sb.append("confidence=").append(this.confidence).append(",");
        sb.append("steward=").append(this.steward).append(",");
        sb.append("source=").append(this.source).append(",");
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