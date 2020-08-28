/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineEnd;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Project Scope relationship links documentation, assets and definitions to the project.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectScope extends Line {
    private static final Logger log = LoggerFactory.getLogger(ProjectScope.class);
    private static final String className = ProjectScope.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{
            "description",
            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{
            "description",
            // Terminate the list
            null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));

    private String description = "The documentation, assets and definitions that are affected by the project.";

    /*
     * Set up end 1.
     */
    final String end1NodeType = "Project";
    final String end1AttributeName = "impactingProjects";
    final String end1AttributeDescription = "The projects that are making changes to these elements.";
    final RelationshipEndCardinality end1Cardinality = RelationshipEndCardinality.ANY_NUMBER;


    /*
     * Set up end 2.
     */
    final String end2NodeType = "Referenceable";
    final String end2AttributeName = "projectScope";
    final String end2AttributeDescription = "The elements that are being changed by this project.";
    final RelationshipEndCardinality end2Cardinality = RelationshipEndCardinality.ANY_NUMBER;

    public ProjectScope() {
        initialise();
    }

    public ProjectScope(Line template) {
        super(template);
        initialise();
    }

    public ProjectScope(Relationship omrsRelationship) {
        super(omrsRelationship);
        initialise();
    }

    @Override
    protected LineEnd getLineEnd1() {
        return new LineEnd(this.end1NodeType,
                           this.end1AttributeName,
                           this.end1AttributeDescription,
                           this.end1Cardinality);
    }

    @Override
    protected LineEnd getLineEnd2() {
        return new LineEnd(this.end2NodeType,
                           this.end2AttributeName,
                           this.end2AttributeDescription,
                           this.end2Cardinality);
    }

    private void initialise() {
        name = "ProjectScope";
        typeDefGuid = "bc63ac45-b4d0-4fba-b583-92859de77dd8";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
            setLineEnds();
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }


    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(" ProjectScopeRelationship=");
        sb.append(super.toString(sb));
        sb.append(" ProjectScopeRelationship Attributes{");
        sb.append("}");
        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    /**
     * {@literal Description of the scope of the project. }
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

}