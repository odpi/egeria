/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

import java.io.Serializable;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A relationship between 2 subject area OMAS Nodes. It is contains named attributes and has 2 relationship ends.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Relationship.class,
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
        @JsonSubTypes.Type(value = SemanticAssignment.class),
        @JsonSubTypes.Type(value = LibraryCategoryReference.class),
        @JsonSubTypes.Type(value = LibraryTermReference.class),
        @JsonSubTypes.Type(value = ProjectScope.class),
        @JsonSubTypes.Type(value = CategoryHierarchyLink.class)
})
abstract public class Relationship implements Serializable, OmasObject {
    protected static final long serialVersionUID = 1L;
    private SystemAttributes systemAttributes = null;
    private Long effectiveFromTime = null;
    private Long effectiveToTime = null;
    private Map<String, String> additionalProperties;
    protected RelationshipType relationshipType;
    private boolean readOnly = false;
    // this is the relationship name
    protected String name;
    protected RelationshipEnd end1;
    protected RelationshipEnd end2;

    protected Relationship(String name, RelationshipEnd end1, RelationshipEnd end2) {
        this.end1 = new RelationshipEnd(end1);
        this.end2 = new RelationshipEnd(end2);
        this.name = name;
        initialise();
    }

    protected void initialise() {
        // set the RelationshipType if this is a RelationshipType enum value.
        try {
            relationshipType = RelationshipType.valueOf(name);
        } catch (IllegalArgumentException e) {
            relationshipType = RelationshipType.Unknown;
        }
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    /**
     * Return the date/time that this relationship should start to be used (null means it can be used from creationTime).
     *
     * @return Date the relationship becomes effective.
     */
    public Long getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Long effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
    }

    /**
     * Return the date/time that this relationship should no longer be used.
     *
     * @return Date the relationship stops being effective.
     */
    public Long getEffectiveToTime() {
        return effectiveToTime;
    }

    public void setEffectiveToTime(Long effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
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

    /**
     * Get relationship end 1. The child relationship sets the appropriate values for its relationship end 1
     *
     * @return RelationshipEnd relationship end 1
     */
    public RelationshipEnd getEnd1() {
        return end1;
    }

    public void setEnd1(RelationshipEnd end1) {
        this.end1 = end1;
    }

    /**
     * Get relationship end 1. The child relationship sets the appropriate values for its relationship end 1
     *
     * @return RelationshipEnd relationship end 1
     */
    public RelationshipEnd getEnd2() {
        return end2;
    }

    public void setEnd2(RelationshipEnd end2) {
        this.end2 = end2;
    }

    /**
     * The relationship is readOnly
     * @return whether read only
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * set whether the relationship is readOnly
     * @param readOnly readonly flag
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Relationship{");
//        sb.append("typeDefGuid=").append(typeDefGuid).append(",");
        sb.append("RelationshipType=").append(relationshipType.name()).append(",");
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