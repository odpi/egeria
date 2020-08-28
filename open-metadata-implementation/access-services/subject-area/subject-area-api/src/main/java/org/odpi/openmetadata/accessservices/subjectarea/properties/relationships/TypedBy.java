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
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the relationship between a spine attribute and its type.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypedBy extends Line {
    private static final Logger log = LoggerFactory.getLogger(TypedBy.class);
    private static final String className = TypedBy.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{
            "description",
            "status",
            "steward",
            "source",

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{
            "description",
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
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));

    private String description = "Defines the relationship between a spine attribute and its type.";
    /*
     * Set up end 1.
     */
    final String end1NodeType = "Term";
    final String end1AttributeName = "attributesTypedBy";
    final String end1AttributeDescription = "Attributes of this type.";
    final RelationshipEndCardinality end1Cardinality = RelationshipEndCardinality.ANY_NUMBER;


    /*
     * Set up end 2.
     */
    final String end2NodeType = "Term";
    final String end2AttributeName = "types";
    final String end2AttributeDescription = "Types for this attribute.";
    final RelationshipEndCardinality end2Cardinality = RelationshipEndCardinality.ANY_NUMBER;
    private TermRelationshipStatus status;
    private String steward;
    private String source;

    public TypedBy() {
        initialise();
    }

    public TypedBy(Line template) {
        super(template);
        initialise();
    }

    public TypedBy(Relationship omrsRelationship) {
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
        name = "TypedBy";
        typeDefGuid = "669e8aa4-c671-4ee7-8d03-f37d09b9d006";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
            setLineEnds();
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
    }

    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue = null;
        enumPropertyValue = new EnumPropertyValue();
        // the status of or confidence in the relationship.
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
     * {@literal The status of or confidence in the relationship. }
     *
     * @return {@code TermRelationshipStatus }
     */
    public TermRelationshipStatus getStatus() {
        return this.status;
    }

    public void setStatus(TermRelationshipStatus status) {
        this.status = status;
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
        sb.append(" TypedBy=");
        sb.append(super.toString(sb));
        sb.append(" TypedBy Attributes{");
        sb.append("description=").append(this.description).append(",");
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