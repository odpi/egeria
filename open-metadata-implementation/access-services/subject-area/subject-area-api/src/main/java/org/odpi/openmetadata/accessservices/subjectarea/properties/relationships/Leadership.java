/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipEnd;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Leadership is a relationship between an ActorProfile and another ActorProfile.
 * It is a relationship identifying leaders and their followers.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Leadership extends Relationship {
    final String description = "Relationship identifying the leaders of teams.";

    /*
     * Set up end 1.
     */
     private static final String END_1_NODE_TYPE = "TeamLeader";
     private static final String END_1_ATTRIBUTE_NAME = "teamLeaders";
     private static final String END_1_ATTRIBUTE_DESCRIPTION = "The leaders of the team.";
     private static final RelationshipEndCardinality END_1_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
     private static final RelationshipEnd RELATIONSHIP_END_1 = new RelationshipEnd(END_1_NODE_TYPE,
                                                                           END_1_ATTRIBUTE_NAME, END_1_ATTRIBUTE_DESCRIPTION, END_1_CARDINALITY);

    /*
     * Set up end 2.
     */
     private static final String END_2_NODE_TYPE = "Team";
     private static final String END_2_ATTRIBUTE_NAME = "leadsTeam";
     private static final String END_2_ATTRIBUTE_DESCRIPTION = "The team lead by this team leader.";
     private static final RelationshipEndCardinality END_2_CARDINALITY = RelationshipEndCardinality.ANY_NUMBER;
     private static final RelationshipEnd RELATIONSHIP_END_2 = new RelationshipEnd(END_2_NODE_TYPE,
                                                                           END_2_ATTRIBUTE_NAME, END_2_ATTRIBUTE_DESCRIPTION, END_2_CARDINALITY);

    public Leadership() {
        super("Leadership", RELATIONSHIP_END_1, RELATIONSHIP_END_2);
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(" Leadership=");
        sb.append(super.toString(sb));
        sb.append(" Leadership Attributes{");
        sb.append("}");
        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}