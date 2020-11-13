/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * This is a view of a relationship from the perspective of one of the ends.
 */
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  Reference implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String KEY_RELATED_END_GUID = "relatedEndGuid";
    public static final String KEY_RELATIONSHIP_GUID = "RelationshipGuid";
    public static final String KEY_TYPENAME = "relatedEndType";
    public static final String KEY_UNIQUE_ATTRIBUTES = "uniqueAttributes";

    /*
     In cases where the relationship ends have different names, there will be 2 references
     one from each of the relationship ends perspective.

      There are some attributes that will be the same in each of these references and some
      some attributes that are end specific.
    */

    // relationship information
    protected String relationship_Type = null;
    protected String relationshipGuid;
    protected Map<String, Object> uniqueAttributes;

    // end / reference specific fields
    protected String relatedEndGuid;
    protected String relatedEndType = null;
    protected String referenceName = null;

    /**
     * the type of relationship
     * @return relationship type
     */
    public String getRelationship_Type() {
        return relationship_Type;
    }

    public void setRelationship_Type(String relationship_Type) {
        this.relationship_Type = relationship_Type;
    }


    /**
     * Attributes associated with the relationship itself
     * @return relationship attributes
     */
    public Map<String, Object> getRelationshipAttributes() {
        return relationshipAttributes;
    }

    public void setRelationshipAttributes(Map<String, Object> relationshipAttributes) {
        this.relationshipAttributes = relationshipAttributes;
    }

    private Map<String, Object> relationshipAttributes;

    /**
     * This guid of the related end of this reference
     * @return related end guid
     */
    public String getRelatedEndGuid() {
        return relatedEndGuid;
    }

    public void setRelatedEndGuid(String relatedEndGuid) {
        this.relatedEndGuid = relatedEndGuid;
    }

    /**
     * This is the relationship guid associated with this reference
     * @return relationship guid
     */
    public String getRelationshipGuid() {
        return relationshipGuid;
    }

    public void setRelationshipGuid(String relationshipGuid) {
        this.relationshipGuid = relationshipGuid;
    }

    /**
     * This is the related ends type name
     * @return related ends type name
     */
    public String getRelatedEndType() {
        return relatedEndType;
    }

    public void setRelatedEndType(String relatedEndType) {
        this.relatedEndType = relatedEndType;
    }

    public Reference() {
        this(null, null, (Map<String, Object>) null);
    }

    public Reference(String guid) {
        this(guid, null, (Map<String, Object>) null);
    }

    public Reference(String guid, String relatedEndType) {
        this(guid, relatedEndType, (Map<String, Object>) null);
    }

    public Reference(String relatedEndType, Map<String, Object> uniqueAttributes) {
        this(null, relatedEndType, uniqueAttributes);
    }

    public Reference(String guid, String relatedEndType, Map<String, Object> uniqueAttributes) {
        setRelationshipGuid(guid);
        setRelatedEndType(relatedEndType);
        setUniqueAttributes(uniqueAttributes);
    }

    public Reference(Reference other) {
        if (other != null) {
            setRelationshipGuid(other.getRelationshipGuid());
            setRelatedEndGuid(other.getRelatedEndGuid());
            setRelatedEndType(other.getRelatedEndType());
            setUniqueAttributes(other.getUniqueAttributes());
        }
    }

    public Reference(Map<String, Object> objIdMap) {
        if (objIdMap != null) {
            Object reg = objIdMap.get(KEY_RELATED_END_GUID);
            Object rg = objIdMap.get(KEY_RELATIONSHIP_GUID);
            Object t = objIdMap.get(KEY_TYPENAME);
            Object u = objIdMap.get(KEY_UNIQUE_ATTRIBUTES);

            if (reg != null) {
                setRelatedEndGuid(reg.toString());
            }
            if (rg != null) {
                setRelationshipGuid(rg.toString());
            }


            if (t != null) {
                setRelatedEndType(t.toString());
            }

            if (u instanceof Map) {
                setUniqueAttributes((Map) u);
            }
        }
    }

    /**
     * Get a map of the unique attributes. i.e. the attributes whose values must are all different.
     * @return {@code Map<String,Object> }
     */
    public Map<String, Object> getUniqueAttributes() {
        return uniqueAttributes;
    }

    public void setUniqueAttributes(Map<String, Object> uniqueAttributes) {
        this.uniqueAttributes = uniqueAttributes;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Reference{");
        sb.append("relatedEndGuid='").append(relatedEndGuid).append('\'');
        sb.append("relationshipGuid='").append(relationshipGuid).append('\'');
        sb.append("relatedEndType='").append(relatedEndType).append('\'');
        sb.append(", uniqueAttributes={");
        //AtlasBaseTypeDef.dumpObjects(uniqueAttributes, sb);
        sb.append('}');
        sb.append('}');

        return sb;
    }

    /**
     * get the name of the reference
     * @return reference name
     */
    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reference that = (Reference) o;

        if (relatedEndGuid != null && !Objects.equals(relatedEndGuid, that.relatedEndGuid)) {
            return false;
        }
        if (relationshipGuid != null && !Objects.equals(relationshipGuid, that.relationshipGuid)) {
            return false;
        }

        return Objects.equals(relatedEndType, that.relatedEndType) &&
                Objects.equals(uniqueAttributes, that.uniqueAttributes);
    }

    @Override
    public int hashCode() {
        return relatedEndGuid != null ? Objects.hash(relatedEndGuid) : Objects.hash(relatedEndType, uniqueAttributes);
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

}
