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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LibraryTermReference is a relationship between a Term and an ExternalGlossaryLink.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibraryTermReference extends Line {
    private static final Logger log = LoggerFactory.getLogger(LibraryTermReference.class);
    private static final String className = LibraryTermReference.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[]{
            "identifier",
            "description",
            "steward",
            "lastVerified",

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[]{
            "identifier",
            "description",
            "steward",
            "lastVerified",

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
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));


    private String description = "Links a glossary term to a glossary term in an external glossary.";

    /*
     * Set up end 1.
     */
    final String end1NodeType = "Term";
    final String end1AttributeName = "localTerms";
    final String end1AttributeDescription = "Related local glossary terms.";
    final RelationshipEndCardinality end1Cardinality = RelationshipEndCardinality.ANY_NUMBER;


    /*
     * Set up end 2.
     */
    final String end2NodeType = "ExternalGlossaryLink";
    final String end2AttributeName = "externalGlossaryTerms";
    final String end2AttributeDescription = "Links to related external glossaries.";
    final RelationshipEndCardinality end2Cardinality = RelationshipEndCardinality.ANY_NUMBER;
    private String identifier;
    private String steward;
    private Date lastVerified;

    public LibraryTermReference() {
        initialise();
    }

    public LibraryTermReference(Line template) {
        super(template);
        initialise();
    }

    public LibraryTermReference(Relationship omrsRelationship) {
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
        name = "LibraryTermReference";
        typeDefGuid = "38c346e4-ddd2-42ef-b4aa-55d53c078d22";
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
        MapPropertyValue mapPropertyValue = null;
        PrimitivePropertyValue primitivePropertyValue = null;
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("identifier", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("description", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("steward", primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("lastVerified", primitivePropertyValue);
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

    /**
     * {@literal Identifier of the corresponding element from the external glossary. }
     *
     * @return {@code String }
     */
    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * {@literal Description of the corresponding element from the external glossary. }
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
     * {@literal Person who established the link to the external glossary. }
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
     * {@literal Date when this reference was last checked. }
     *
     * @return {@code Date }
     */
    public Date getLastVerified() {
        return this.lastVerified;
    }

    public void setLastVerified(Date lastVerified) {
        this.lastVerified = lastVerified;
    }

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append(" LibraryTermReference=");
        sb.append(super.toString(sb));
        sb.append(" LibraryTermReference Attributes{");
        sb.append("identifier=").append(this.identifier).append(",");
        sb.append("description=").append(this.description).append(",");
        sb.append("steward=").append(this.steward).append(",");
        sb.append("lastVerified=").append(this.lastVerified).append(",");
        sb.append("}");
        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}