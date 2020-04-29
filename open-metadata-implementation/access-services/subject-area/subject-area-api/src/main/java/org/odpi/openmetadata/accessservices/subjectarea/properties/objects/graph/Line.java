/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * A relationship between 2 subject area OMAS entities. It is called types as it has named fields for the attributes and references.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {

                // term to term relationship responses
                @JsonSubTypes.Type(value = Hasa.class, name = "Hasa"),
                @JsonSubTypes.Type(value = RelatedTerm.class, name = "RelatedTerm"),
                @JsonSubTypes.Type(value = Synonym.class, name = "Synonym"),
                @JsonSubTypes.Type(value = Antonym.class, name = "Antonym"),
                @JsonSubTypes.Type(value = PreferredTerm.class, name = "PreferredTerm"),
                @JsonSubTypes.Type(value = ReplacementTerm.class, name = "ReplacementTerm"),
                @JsonSubTypes.Type(value = Translation.class, name = "Translation"),
                @JsonSubTypes.Type(value = Isa.class, name = "Isa"),
                @JsonSubTypes.Type(value = ValidValue.class, name = "ValidValue"),
                @JsonSubTypes.Type(value = UsedInContext.class, name = "UsedInContext"),
                @JsonSubTypes.Type(value = IsaTypeOf.class, name = "IsaTypeOf"),
                @JsonSubTypes.Type(value = TypedBy.class, name = "TypedBy"),
                @JsonSubTypes.Type(value = TermAnchor.class, name = "TermAnchor"),
                @JsonSubTypes.Type(value = CategoryAnchor.class, name = "CategoryAnchor"),
                @JsonSubTypes.Type(value = Categorization.class, name = "Categorization"),

        })
            
public class Line implements Serializable {
    protected static final long serialVersionUID = 1L;
    private SystemAttributes systemAttributes = null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    private Map<String, String> additionalProperties;
    protected String typeDefGuid;
    protected LineType lineType;
    protected String entity1Name;
    protected String entity1Type;
    protected String entity2Name;
    protected String entity2Type;
    protected String name;

    /**
     * Default constructor
     */
    public Line() {}
    public Line(Line template) {
            this.setAdditionalProperties(template.getAdditionalProperties());
            this.setSystemAttributes(template.getSystemAttributes());
            this.setGuid(template.getGuid());
            this.setLineType(template.getLineType());
            this.setAdditionalProperties(template.getAdditionalProperties());
    }
    public Line(String name) {
        this.name=name;
        this.lineType=LineType.Unknown;
    }

    public Line(String name,LineType lineType) {
        this.name=name;
        this.lineType=lineType;
    }
    /**
     * Create a typedRelationship from an omrs relationship
     * @param omrsRelationship omrs relationship
     */
    public Line(Relationship omrsRelationship) {
        if (this.systemAttributes ==null) {
            this.systemAttributes = new SystemAttributes();
        }
        this.systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
        this.systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
        this.systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
        this.systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
        this.systemAttributes.setVersion(omrsRelationship.getVersion());

    }

    public String getTypeDefGuid() {
        return typeDefGuid;
    }

    public void setTypeDefGuid(String typeDefGuid) {
        this.typeDefGuid = typeDefGuid;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }
    /**
     * Return the date/time that this line should start to be used (null means it can be used from creationTime).
     * @return Date the line becomes effective.
     */
    public Date getEffectiveFromTime()
    {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime)
    {
        this.effectiveFromTime = effectiveFromTime;
    }
    /**
     * Return the date/time that this line should no longer be used.
     *
     * @return Date the line stops being effective.
     */
    public Date getEffectiveToTime()
    {
        return effectiveToTime;
    }
    public String getGuid() {
        if (this.systemAttributes==null) {
            return null;
        } else {
            return systemAttributes.getGUID();
        }
    }

    public void setGuid(String guid) {
        if (this.systemAttributes==null) {
            this.systemAttributes = new SystemAttributes();
        }
        this.systemAttributes.setGUID(guid);
    }

    public String getName() {
        return name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return extra attributes
     */
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }
    public void setAdditionalProperties(Map<String,String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Create an omrs relationship with the basic fields filled in.
     * @param line this is a line to create the relationship from
     * @return Relationship the created omrs relationship
     */
    public static Relationship createOmrsRelationship(Line line) {
        Relationship omrsRelationship = new Relationship();
        InstanceType typeOfRelationship = new InstanceType();
        typeOfRelationship.setTypeDefName(line.getName());
        typeOfRelationship.setTypeDefGUID(line.getTypeDefGuid());
        omrsRelationship.setType(typeOfRelationship);
        SystemAttributes systemAttributes = line.getSystemAttributes();
        if (systemAttributes ==null) {
            systemAttributes = new SystemAttributes();
        }
        omrsRelationship.setCreatedBy(systemAttributes.getCreatedBy());
        omrsRelationship.setUpdatedBy(systemAttributes.getUpdatedBy());
        omrsRelationship.setCreateTime(systemAttributes.getCreateTime());
        omrsRelationship.setUpdateTime(systemAttributes.getUpdateTime());
        if (systemAttributes.getVersion()==null) {
            omrsRelationship.setVersion(0L);
        } else {
            omrsRelationship.setVersion(systemAttributes.getVersion());
        }
        line.setSystemAttributes(systemAttributes);
        return omrsRelationship;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Line{");
        sb.append("typeDefGuid="+typeDefGuid+",");
        sb.append("lineType=" + lineType.name()+",");
        sb.append("name=" + name);
        if (this.systemAttributes!=null)
        {
            sb.append("systemAttributes { ");
            sb = this.systemAttributes.toString(sb);
            sb.append("}");
        }
        sb.append('}');
        return sb;
    }
}
