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
 * RelatedTerm is a relationship between an entity of type GlossaryTerm and an entity of type GlossaryTerm.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has seeAlso as the proxy name for entity type GlossaryTerm.
 * The second entity proxy has seeAlso as the proxy name for entity type GlossaryTerm.
 *
 * Each entity proxy also stores the entities guid.

 Link between similar glossary terms.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedTerm extends Line {
    private static final Logger log = LoggerFactory.getLogger(RelatedTerm.class);
    private static final String className = RelatedTerm.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
            "description",
            "expression",
            "status",
            "steward",
            "source",

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
            "description",
            "expression",
            "steward",
            "source",

            // Terminate the list
            null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[] {
            "status",

            // Terminate the list
            null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[] {

            // Terminate the list
            null
    };
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));
    private String relatedTerm1Guid;
    private String relatedTerm2Guid;


    public RelatedTerm() {
        initialise();
    }

    private void initialise()
    {
        name = "RelatedTerm";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
        entity1Name = "seeAlso";
        entity1Type = "GlossaryTerm";
        entity2Name = "seeAlso";
        entity2Type = "GlossaryTerm";
        typeDefGuid = "b1161696-e563-4cf9-9fd9-c0c76e47d063";
    }

    public RelatedTerm(Line template) {
        super(template);
        initialise();
    }

    public RelatedTerm(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "RelatedTerm";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
    }
    /**
     * {@literal Get the guid of related term at end 1 of the relationship. }
     * @return {@code String }
     */
    public String getRelatedTerm1Guid()
    {
        return relatedTerm1Guid;
    }

    public void setRelatedTerm1Guid(String relatedTerm1Guid)
    {
        this.relatedTerm1Guid = relatedTerm1Guid;
    }
    /**
     * {@literal Get the guid of related term at end 2 of the relationship. }
     * @return {@code String }
     */
    public String getRelatedTerm2Guid()
    {
        return relatedTerm2Guid;
    }

    public void setRelatedTerm2Guid(String relatedTerm2Guid)
    {
        this.relatedTerm2Guid = relatedTerm2Guid;
    }

    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        enumPropertyValue = new EnumPropertyValue();
        // the status of or confidence in the relationship.
        enumPropertyValue.setOrdinal(status.ordinal());
        enumPropertyValue.setSymbolicName(status.name());
        instanceProperties.setProperty("status",enumPropertyValue);
        MapPropertyValue mapPropertyValue=null;
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("expression",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("status",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("steward",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("source",primitivePropertyValue);
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

    private String description;
    /**
     * {@literal Description of the relationship. }
     * @return {@code String }
     */
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description)  {
        this.description = description;
    }
    private String expression;
    /**
     * {@literal An expression that explains the relationship. }
     * @return {@code String }
     */
    public String getExpression() {
        return this.expression;
    }
    public void setExpression(String expression)  {
        this.expression = expression;
    }
    private TermRelationshipStatus status;
    /**
     * {@literal The status of or confidence in the relationship. }
     * @return {@code TermRelationshipStatus }
     */
    public TermRelationshipStatus getStatus() {
        return this.status;
    }
    public void setStatus(TermRelationshipStatus status)  {
        this.status = status;
    }
    private String steward;
    /**
     * {@literal Person responsible for the relationship. }
     * @return {@code String }
     */
    public String getSteward() {
        return this.steward;
    }
    public void setSteward(String steward)  {
        this.steward = steward;
    }
    private String source;
    /**
     * {@literal Person, organization or automated process that created the relationship. }
     * @return {@code String }
     */
    public String getSource() {
        return this.source;
    }
    public void setSource(String source)  {
        this.source = source;
    }

    @Override
    public StringBuilder toString(StringBuilder sb)
    {
        if (sb == null)
        {
            sb = new StringBuilder();
        }
        sb.append(" RelatedTerm=");
        sb.append(super.toString(sb));
        sb.append(" RelatedTerm Attributes{");
        sb.append("description=").append(this.description).append(",");
        sb.append("expression=").append(this.expression).append(",");
        sb.append("steward=").append(this.steward).append(",");
        sb.append("source=").append(this.source).append(",");
        if ( status!=null) {
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
