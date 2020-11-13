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
 * this is represents a relationship and the related end.
 */
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipEnd implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String KEY_END_GUID = "EndGuid";
    public static final String KEY_RELATIONSHIP_GUID = "RelationshipGuid";
    public static final String KEY_TYPENAME = "relatedEndType";
    public static final String KEY_UNIQUE_ATTRIBUTES = "uniqueAttributes";

    private String EndGuid;
    private String relationshipGuid;
    private Map<String, Object> uniqueAttributes;
    protected String typeName = null;

    /**
     * This guid of the is the related end of this reference
     * @return guid of the end
     */
    public String getEndGuid() {
        return EndGuid;
    }

    public void setEndGuid(String EndGuid) {
        this.EndGuid = EndGuid;
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
     * @return the related ends name
     */
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public RelationshipEnd() {
        this(null, null, (Map<String, Object>) null);
    }

    public RelationshipEnd(String guid) {
        this(guid, null, (Map<String, Object>) null);
    }

    public RelationshipEnd(String guid, String typeName) {
        this(guid, typeName, (Map<String, Object>) null);
    }

    public RelationshipEnd(String typeName, Map<String, Object> uniqueAttributes) {
        this(null, typeName, uniqueAttributes);
    }

    public RelationshipEnd(String guid, String typeName, Map<String, Object> uniqueAttributes) {
        setRelationshipGuid(guid);
        setTypeName(typeName);
        setUniqueAttributes(uniqueAttributes);
    }

    public RelationshipEnd(RelationshipEnd other) {
        if (other != null) {
            setRelationshipGuid(other.getRelationshipGuid());
            setEndGuid(other.getEndGuid());
            setTypeName(other.getTypeName());
            setUniqueAttributes(other.getUniqueAttributes());
        }
    }

    public RelationshipEnd(Map objIdMap) {
        if (objIdMap != null) {
            Object reg = objIdMap.get(KEY_END_GUID);
            Object rg = objIdMap.get(KEY_RELATIONSHIP_GUID);
            Object t = objIdMap.get(KEY_TYPENAME);
            Object u = objIdMap.get(KEY_UNIQUE_ATTRIBUTES);

            if (reg != null) {
                setEndGuid(reg.toString());
            }
            if (rg != null) {
                setRelationshipGuid(rg.toString());
            }


            if (t != null) {
                setTypeName(t.toString());
            }

            if (u instanceof Map) {
                setUniqueAttributes((Map) u);
            }
        }
    }


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
        sb.append("EndGuid='").append(EndGuid).append('\'');
        sb.append("relationshipGuid='").append(relationshipGuid).append('\'');
        sb.append("relatedEndType='").append(typeName).append('\'');
        sb.append(", uniqueAttributes={");
        //AtlasBaseTypeDef.dumpObjects(uniqueAttributes, sb);
        sb.append('}');
        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelationshipEnd that = (RelationshipEnd) o;

        if (EndGuid != null && !Objects.equals(EndGuid, that.EndGuid)) {
            return false;
        }
        if (relationshipGuid != null && !Objects.equals(relationshipGuid, that.relationshipGuid)) {
            return false;
        }

        return Objects.equals(typeName, that.typeName) &&
                Objects.equals(uniqueAttributes, that.uniqueAttributes);
    }

    @Override
    public int hashCode() {
        return EndGuid != null ? Objects.hash(EndGuid) : Objects.hash(typeName, uniqueAttributes);
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}