/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;

/**
 * Links a glossary term to another element such as an asset or schema element to define its meaning.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SemanticAssignment extends Line {
    private static final Logger log = LoggerFactory.getLogger(SemanticAssignment.class);
    private static final String className = SemanticAssignment.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{
            "description",
            "expression",
            "status",
            "confidence",
            "steward",
            "source",

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{
            "description",
            "expression",
            "confidence",
            "steward",
            "source",

            // Terminate the list
            null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[]{
            "status",

            // Terminate the list
            null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[]{

            // Terminate the list
            null
    };
    private static final java.util.Set<String> PROPERTY_NAMES_SET =new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET =new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET =new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET =new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));
    private String assignedElementGuid;
    private String termGuid;


    public SemanticAssignment() {
        initialise();
    }

    private void initialise() {
        name = "SemanticAssignment";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
        entity1Name = "assignedElements";
        entity1Type = "Referenceable";
        entity2Name = "meaning";
        entity2Type = "GlossaryTerm";
        typeDefGuid = "e6670973-645f-441a-bec7-6f5570345b92";
    }

    public SemanticAssignment(Line template) {
        super(template);
        initialise();
    }

    public SemanticAssignment(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "SemanticAssignment";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
    }

    /**
     * {@literal Get the guid of an assigned element, such as an asset or schema element, that is given meaning by the associated Term.}
     *
     * @return {@code String }
     */
    public String getAssignedElementGuid() {
        return assignedElementGuid;
    }

    public void setAssignedElementGuid(String assignedElementGuid) {
        this.assignedElementGuid = assignedElementGuid;
    }

    /**
     * {@literal Get the guid of Term that gives meaning to the assigned element. }
     *
     * @return {@code String }
     */
    public String getTermGuid() {
        return termGuid;
    }

    public void setTermGuid(String termGuid) {
        this.termGuid = termGuid;
    }

    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue = null;
        enumPropertyValue = new EnumPropertyValue();
        // the status of the relationship.
        enumPropertyValue.setOrdinal(status.ordinal());
        enumPropertyValue.setSymbolicName(status.name());
        instanceProperties.setProperty("status", enumPropertyValue);
        MapPropertyValue mapPropertyValue = null;
        PrimitivePropertyValue primitivePropertyValue = null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("description", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("expression", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("status", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("steward", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("source", primitivePropertyValue);
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

    private String description;

    /**
     * {@literal Description of the relationship. }
     *
     * @return {@code String }
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String expression;

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

    private TermAssignmentStatus status;

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

    private Integer confidence;

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

    private String steward;

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

    private String source;

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
