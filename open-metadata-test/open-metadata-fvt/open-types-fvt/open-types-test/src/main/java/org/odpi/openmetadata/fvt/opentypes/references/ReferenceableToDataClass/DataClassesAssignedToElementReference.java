/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToDataClass;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.entities.DataClass.DataClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This is a reference, which is a view of a relationships from the perspective of one of the ends. A relationships is
 * the link between 2 OMAS entities.
 *
 * A reference is used when working with OMAS entity APIs. An OMAS entity has attributes, classifications
 * and references.
 *
 * This Reference is called DataClassesAssignedToElementReference. It is used in type Referenceable to represent a
 * reference to an OMAS entity of type dataClass. The reference also contains information
 * about the relationships; the relationships guid, name, relationships attributes and a list of unique attributes.
 * Relationship attributes are attributes of the relationships.
 *
 * It is possible to work with the relationships itself using the OMAS API using the relationships guid
 * contained in this reference.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClassesAssignedToElementReference extends Reference implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DataClassesAssignedToElementReference.class);
    private static final String className = DataClassesAssignedToElementReference.class.getName();

    final static protected String relationship_Type = "DataClassAssignment";
    protected DataClass dataClass =null;



    public DataClass getDataClass() {
        return  dataClass;
    }
    public void setDataClass(DataClass dataClass) {
        this.dataClass = dataClass;
    }
    private String method;
    /**
     * Method used to identify data class.
     * @return Method
     */
    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    private String status;
    /**
     * The status of the relationship.
     * @return Status
     */
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private String partialMatch;
    /**
     * Are there data values outside of the data class specification?
     * @return PartialMatch
     */
    public String getPartialMatch() {
        return this.partialMatch;
    }

    public void setPartialMatch(String partialMatch) {
        this.partialMatch = partialMatch;
    }
    private String confidence;
    /**
     * Level of confidence in the correctness of the data class assignment.
     * @return Confidence
     */
    public String getConfidence() {
        return this.confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
    private String threshold;
    /**
     * What was the threshold result used to determine that the data class matched.
     * @return Threshold
     */
    public String getThreshold() {
        return this.threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }
    private String valueFrequency;
    /**
     * How often does the data class specification match the data values.
     * @return ValueFrequency
     */
    public String getValueFrequency() {
        return this.valueFrequency;
    }

    public void setValueFrequency(String valueFrequency) {
        this.valueFrequency = valueFrequency;
    }
    private String steward;
    /**
     * Person responsible for validating the data class assignment.
     * @return Steward
     */
    public String getSteward() {
        return this.steward;
    }

    public void setSteward(String steward) {
        this.steward = steward;
    }
    private String source;
    /**
     * Person, organization or automated process that created the data class assignment.
     * @return Source
     */
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public DataClassesAssignedToElementReference() {
        this(null, null, (Map<String, Object>) null);
    }

    public DataClassesAssignedToElementReference(String guid) {
        this(guid, null, (Map<String, Object>) null);
    }

    public DataClassesAssignedToElementReference(String guid, String relatedEndType) {
        this(guid, relatedEndType, (Map<String, Object>) null);
    }

    public DataClassesAssignedToElementReference(String relatedEndType, Map<String, Object> uniqueAttributes) {
        this(null, relatedEndType, uniqueAttributes);
    }

    public DataClassesAssignedToElementReference(String guid, String relatedEndType, Map<String, Object> uniqueAttributes) {
        setRelationshipGuid(guid);
        setRelatedEndType(relatedEndType);
        setUniqueAttributes(uniqueAttributes);
    }

    public DataClassesAssignedToElementReference(Reference other) {
        if (other != null) {
            setRelationshipGuid(other.getRelationshipGuid());
            setRelatedEndGuid(other.getRelatedEndGuid());
            setRelatedEndType(other.getRelatedEndType());
            setUniqueAttributes(other.getUniqueAttributes());
        }
    }
    
      /**
       * Populate the reference from a relationships
       * @param entityGuid String entity Guid
       * @param line OMRSLine
       */
    public DataClassesAssignedToElementReference(String entityGuid, OMRSLine line) {
        setRelationshipGuid(line.getGuid());
        if (entityGuid.equals(line.getEntity1Guid())) {
            setRelatedEndGuid(line.getEntity2Guid());
            setRelatedEndType(line.getEntity2Type());
            // TODO UniqueAttributes
            //setUniqueAttributes(line.get
        } else {
            setRelatedEndGuid(line.getEntity1Guid());
            setRelatedEndType(line.getEntity1Type());
            // TODO UniqueAttributes
            //setUniqueAttributes(line.get
        }
    }    

    public DataClassesAssignedToElementReference(Map objIdMap) {
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

            if (u != null && u instanceof Map) {
                setUniqueAttributes((Map) u);
            }
        }
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Reference{");
        sb.append("relatedEndGuid='").append(getRelatedEndGuid()).append('\'');
        sb.append("relationshipGuid='").append(getRelationshipGuid()).append('\'');
        sb.append("relatedEndType='").append(getRelatedEndType()).append('\'');
        sb.append(", uniqueAttributes={");
//  AtlasBaseTypeDef.dumpObjects(uniqueAttributes, sb);
        sb.append("}");
 	sb.append("{");
	sb.append("this.method ");
	sb.append("this.status ");
	sb.append("this.partialMatch ");
	sb.append("this.confidence ");
	sb.append("this.threshold ");
	sb.append("this.valueFrequency ");
	sb.append("this.steward ");
	sb.append("this.source ");
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

        Reference that = (Reference) o;

        if (relatedEndGuid != null && !Objects.equals(relatedEndGuid, that.getRelatedEndGuid())) {
            return false;
        }
        if (relationshipGuid != null && !Objects.equals(relationshipGuid, that.getRelationshipGuid())) {
            return false;
        }
        DataClassesAssignedToElementReference typedThat =(DataClassesAssignedToElementReference)that;
        if (this.method != null && !Objects.equals(this.method,typedThat.getMethod())) {
            return false;
        }
        if (this.status != null && !Objects.equals(this.status,typedThat.getStatus())) {
            return false;
        }
        if (this.partialMatch != null && !Objects.equals(this.partialMatch,typedThat.getPartialMatch())) {
            return false;
        }
        if (this.confidence != null && !Objects.equals(this.confidence,typedThat.getConfidence())) {
            return false;
        }
        if (this.threshold != null && !Objects.equals(this.threshold,typedThat.getThreshold())) {
            return false;
        }
        if (this.valueFrequency != null && !Objects.equals(this.valueFrequency,typedThat.getValueFrequency())) {
            return false;
        }
        if (this.steward != null && !Objects.equals(this.steward,typedThat.getSteward())) {
            return false;
        }
        if (this.source != null && !Objects.equals(this.source,typedThat.getSource())) {
            return false;
        }

        return Objects.equals(relatedEndType, that.getRelatedEndType()) &&
                Objects.equals(uniqueAttributes, that.getUniqueAttributes());
    }


    @Override
    public int hashCode() {
        return relatedEndGuid != null ? Objects.hash(relatedEndGuid) : Objects.hash(relatedEndType, uniqueAttributes
, this.method
, this.status
, this.partialMatch
, this.confidence
, this.threshold
, this.valueFrequency
, this.steward
, this.source
);
    }
}
