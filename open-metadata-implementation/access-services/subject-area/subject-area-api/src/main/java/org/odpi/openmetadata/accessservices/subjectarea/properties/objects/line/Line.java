/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.io.Serializable;
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
                @JsonSubTypes.Type(value = TermHASARelationship.class, name = "TermHASARelationship"),
                @JsonSubTypes.Type(value = RelatedTerm.class, name = "RelatedTerm"),
                @JsonSubTypes.Type(value = Synonym.class, name = "Synonym"),
                @JsonSubTypes.Type(value = Antonym.class, name = "Antonym"),
                @JsonSubTypes.Type(value = PreferredTerm.class, name = "PreferredTerm"),
                @JsonSubTypes.Type(value = ReplacementTerm.class, name = "ReplacementTerm"),
                @JsonSubTypes.Type(value = Translation.class, name = "Translation"),
                @JsonSubTypes.Type(value = ISARelationship.class, name = "ISARelationship"),
                @JsonSubTypes.Type(value = ValidValue.class, name = "ValidValue"),
                @JsonSubTypes.Type(value = UsedInContext.class, name = "UsedInContext"),
                @JsonSubTypes.Type(value = TermISATypeOFRelationship.class, name = "TermISATYPEOFRelationship"),
                @JsonSubTypes.Type(value = TermTYPEDBYRelationship.class, name = "TermTYPEDBYRelationship"),

        })
            
public class Line implements Serializable {
    protected static final long serialVersionUID = 1L;
    private SystemAttributes systemAttributes = null;
    private Map<String, Object> extraAttributes;
    protected String typeDefGuid;
    protected LineType lineType;
    protected String entity1Name;
    protected String entity1Type;
    protected String entity1PropertyName;
    protected String entity1Label;

    protected String entity2Name;
    protected String entity2Type;
    protected String entity2PropertyName;
    protected String entity2Label;
    protected String name;

    /**
     * Default constructor
     */
    public Line() {}
    public Line(Line template) {
//            this.setRelatedTerm1Guid(template.getRelatedTerm1Guid());
//            this.setEntity1Label(template.getEntity1Label());
//            this.setEntity1Name(template.getEntity1Name());
//            this.setEntity1PropertyName(template.getEntity1PropertyName());
//            this.setRelatedTerm2Guid(template.getRelatedTerm2Guid());
//            this.setEntity2Label(template.getEntity2Label());
//            this.setEntity2Name(template.getEntity2Name());
//            this.setEntity2PropertyName(template.getEntity2PropertyName());
            this.setExtraAttributes(template.getExtraAttributes());
            this.setSystemAttributes(template.getSystemAttributes());
            this.setGuid(template.getGuid());
            this.setLineType(template.getLineType());
            this.setExtraAttributes(template.getExtraAttributes());
    }
    public Line(String name) {
        this.name=name;
        this.lineType=LineType.Other;
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

//        this.entity1Name = omrsRelationship.getEntityOnePropertyName();
//        this.entity1Type = omrsRelationship.getEntityOneProxy().getType().getTypeDefName();
//        this.relatedTerm1Guid = omrsRelationship.getEntityOneProxy().getGUID();
//
//        this.entity2Name = omrsRelationship.getEntityTwoPropertyName();
//        this.entity2Type = omrsRelationship.getEntityTwoProxy().getType().getTypeDefName();
//        this.relatedTerm2Guid = omrsRelationship.getEntityTwoProxy().getGUID();
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

//    public void setEntity1Name(String entity1Name) {
//        this.entity1Name = entity1Name;
//    }
//    public void setEntity1Type(String entity1Type) {
//        this.entity1Type = entity1Type;
//    }
//
//    public String getEntity1PropertyName() {
//        return entity1PropertyName;
//    }
//
//    public void setEntity1PropertyName(String entity1PropertyName) {
//        this.entity1PropertyName = entity1PropertyName;
//    }
//
//    public String getEntity1Label() {
//        return entity1Label;
//    }
//
//    public void setEntity1Label(String entity1Label) {
//        this.entity1Label = entity1Label;
//    }
//
//    public void setEntity2Name(String entity2Name) {
//        this.entity2Name = entity2Name;
//    }
//
//    public void setEntity2Type(String entity2Type) {
//        this.entity2Type = entity2Type;
//    }
//
//    public String getEntity2PropertyName() {
//        return entity2PropertyName;
//    }
//
//    public void setEntity2PropertyName(String entity2PropertyName) {
//        this.entity2PropertyName = entity2PropertyName;
//    }
//
//    public String getEntity2Label() {
//        return entity2Label;
//    }
//
//    public void setEntity2Label(String entity2Label) {
//        this.entity2Label = entity2Label;
//    }
//
    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }
//
//    public String getRelatedTerm1Guid() {
//        return relatedTerm1Guid;
//    }
//
//    public void setRelatedTerm1Guid(String relatedTerm1Guid) {
//        this.relatedTerm1Guid = relatedTerm1Guid;
//    }
//
//    public String getRelatedTerm2Guid() {
//        return relatedTerm2Guid;
//    }
//
//    public void setRelatedTerm2Guid(String relatedTerm2Guid) {
//        this.relatedTerm2Guid = relatedTerm2Guid;
//    }

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
//    public String getEntity1Name() {
//        return entity1Name;
//    }
//    public String getEntity2Name() {
//        return entity2Name;
//    }
//    public String getEntity1Type() { return entity1Type; }
//    public String getEntity2Type() { return entity2Type; }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return extra attributes
     */
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }
    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
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
//        //set proxy 1
//        EntityProxy entityOne = new EntityProxy();
//        entityOne.setGUID(line.getRelatedTerm1Guid());
//        String type1 = line.getEntity1Type();
//        InstanceType instancetype1 = new InstanceType();
//        instancetype1.setTypeDefName(type1);
//        entityOne.setType(instancetype1);
//        //set proxy 2
//        EntityProxy entityTwo = new EntityProxy();
//        entityTwo.setGUID(line.getRelatedTerm2Guid());
//        String type2 = line.getEntity2Type();
//        InstanceType instancetype2 = new InstanceType();
//        instancetype2.setTypeDefName(type2);
//        entityTwo.setType(instancetype2);
//
//        omrsRelationship.setEntityOneProxy(entityOne);
//        omrsRelationship.setEntityTwoProxy(entityTwo);
        return omrsRelationship;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Line{");
        sb.append("typeDefGuid="+typeDefGuid+",");
        sb.append("lineType=" + lineType.name()+",");
//        sb.append("entity1Name="+entity1Name+",");
//        sb.append("entity1Type="+entity1Type+",");
//        sb.append("relatedTerm1Guid="+relatedTerm1Guid+",");
//        sb.append("entity1PropertyName="+ entity1PropertyName+",");
//        sb.append("entity1Label="+ entity1Label+",");
//
//        sb.append("entity2Name="+entity2Name+",");
//        sb.append("entity2Type="+entity2Type+",");
//        sb.append("relatedTerm2Guid="+relatedTerm2Guid+",");
//        sb.append("entity2PropertyName="+ entity2PropertyName+",");
//        sb.append("entity2Label="+ entity2Label+",");
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
