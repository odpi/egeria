/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A relationship between 2 subject area OMAS Nodes. It is contains named attributes and has 2 Line ends.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Line.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HasA.class),
        @JsonSubTypes.Type(value = RelatedTerm.class),
        @JsonSubTypes.Type(value = Synonym.class),
        @JsonSubTypes.Type(value = Antonym.class),
        @JsonSubTypes.Type(value = PreferredTerm.class),
        @JsonSubTypes.Type(value = ReplacementTerm.class),
        @JsonSubTypes.Type(value = Translation.class),
        @JsonSubTypes.Type(value = IsA.class),
        @JsonSubTypes.Type(value = ValidValue.class),
        @JsonSubTypes.Type(value = UsedInContext.class),
        @JsonSubTypes.Type(value = IsATypeOf.class),
        @JsonSubTypes.Type(value = TypedBy.class),
        @JsonSubTypes.Type(value = TermAnchor.class),
        @JsonSubTypes.Type(value = CategoryAnchor.class),
        @JsonSubTypes.Type(value = Categorization.class),
})
public class Line implements Serializable, OmasObject {
    protected static final long serialVersionUID = 1L;
    private SystemAttributes systemAttributes = null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    private Map<String, String> additionalProperties;
    protected String typeDefGuid;
    protected LineType lineType;
    // this is the line name
    protected String name;
    protected String openTypeName;
    protected LineEnd end1;
    protected LineEnd end2;


    /**
     * Default constructor
     */
    public Line() {
    }

    public Line(Line template) {
        this.setAdditionalProperties(template.getAdditionalProperties());
        this.setSystemAttributes(template.getSystemAttributes());
        this.setGuid(template.getGuid());
        this.setLineType(template.getLineType());
        this.setAdditionalProperties(template.getAdditionalProperties());
        this.setLineEnds();
    }

    public Line(String name) {
        this(name,LineType.Unknown);
    }

    public Line(String name, LineType lineType) {
        this.name = name;
        this.lineType = lineType;
        this.setLineEnds();
    }
    protected void setLineEnds() {
//        OpenMetadataTypesArchiveAccessor archiveAccessor = OpenMetadataTypesArchiveAccessor.getInstance();
//
//        RelationshipDef relationshipDef =archiveAccessor.getRelationshipDefByGuid(this.typeDefGuid);
//        RelationshipEndDef endDef1 = relationshipDef.getEndDef1();
//        RelationshipEndDef endDef2 = relationshipDef.getEndDef2();
//
//        String end1TypeName = endDef1.getEntityType().getName();
//        String end2TypeName = endDef2.getEntityType().getName();
        // Maybe consider subtypes here? We cannot use the Repositoryhelper isTypeOf because Line is used client side
        // which does not have access to the repository helper.
//        if (end1TypeName.equals("GlossaryTerm")) {
//            end1TypeName = "Term";
//        }
//        if (end2TypeName.equals("GlossaryTerm")) {
//            end2TypeName = "Term";
//        }
//        getLineEnd1();
//        getLineEnd1();
//
//        LineEnd lineEnd1 = new LineEnd(getEnd1TypeName(),
//                                       getEnd1AttributeName(),
//                                       getEnd1AttributeDescription(),
//                                       endDef1.getAttributeCardinality()
//                                       );
//        LineEnd lineEnd2 = new LineEnd(end2TypeName,
//                                       endDef2.getAttributeName(),
//                                       endDef2.getAttributeDescription(),
//                                       endDef2.getAttributeCardinality()
//                                       );
        setEnd1(getLineEnd1());
        setEnd2(getLineEnd2());
    }

    protected LineEnd getLineEnd1() {
       return null;
    }
    protected LineEnd getLineEnd2() {
        return null;
    }

    /**
     * Create a typedRelationship from an omrs relationship
     *
     * @param omrsRelationship omrs relationship
     */
    public Line(Relationship omrsRelationship) {
        this.systemAttributes = new SystemAttributes();
        this.systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
        this.systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
        this.systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
        this.systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
        this.systemAttributes.setVersion(omrsRelationship.getVersion());
        setLineEnds();
    }
//
//    public String getTypeDefGuid() {
//        return typeDefGuid;
//    }
//
//    public void setTypeDefGuid(String typeDefGuid) {
//        this.typeDefGuid = typeDefGuid;
//    }

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
     *
     * @return Date the line becomes effective.
     */
    public Date getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
    }

    /**
     * Return the date/time that this line should no longer be used.
     *
     * @return Date the line stops being effective.
     */
    public Date getEffectiveToTime() {
        return effectiveToTime;
    }

    public String getGuid() {
        if (this.systemAttributes == null) {
            return null;
        } else {
            return systemAttributes.getGUID();
        }
    }

    public void setGuid(String guid) {
        if (this.systemAttributes == null) {
            this.systemAttributes = new SystemAttributes();
        }
        this.systemAttributes.setGUID(guid);
    }

    public String getName() {
        return name;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     *
     * @return extra attributes
     */
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public LineEnd getEnd1() {
        return end1;
    }

    public void setEnd1(LineEnd end1) {
        this.end1 = end1;
    }

    public LineEnd getEnd2() {
        return end2;
    }

    public void setEnd2(LineEnd end2) {
        this.end2 = end2;
    }

    /**
     * Create an omrs relationship with the basic fields filled in.
     *
     * @param line this is a line to create the relationship from
     * @return Relationship the created omrs relationship
     */
    public static Relationship createOmrsRelationship(Line line) {
        Relationship omrsRelationship = new Relationship();
        InstanceType typeOfRelationship = new InstanceType();
        typeOfRelationship.setTypeDefName(line.getName());
//        typeOfRelationship.setTypeDefGUID(line.getTypeDefGuid());
        omrsRelationship.setType(typeOfRelationship);
        SystemAttributes systemAttributes = line.getSystemAttributes();
        if (systemAttributes == null) {
            systemAttributes = new SystemAttributes();
        }
        omrsRelationship.setCreatedBy(systemAttributes.getCreatedBy());
        omrsRelationship.setUpdatedBy(systemAttributes.getUpdatedBy());
        omrsRelationship.setCreateTime(systemAttributes.getCreateTime());
        omrsRelationship.setUpdateTime(systemAttributes.getUpdateTime());
        if (systemAttributes.getVersion() == null) {
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
//        sb.append("typeDefGuid=").append(typeDefGuid).append(",");
        sb.append("lineType=").append(lineType.name()).append(",");
        sb.append("name=").append(name);
        if (this.systemAttributes != null) {
            sb.append("systemAttributes { ");
            sb = this.systemAttributes.toString(sb);
            sb.append("}");
        }
        sb.append('}');
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(null).toString();
    }
}