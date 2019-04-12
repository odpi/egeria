/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.references.ReferenceableToReferenceable;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.entities.Referenceable.Referenceable;
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
 * This Reference is called RelatedFromObjectAnnotationsReference. It is used in type Referenceable to represent a
 * reference to an OMAS entity of type referenceable. The reference also contains information
 * about the relationships; the relationships guid, name, relationships attributes and a list of unique attributes.
 * Relationship attributes are attributes of the relationships.
 *
 * It is possible to work with the relationships itself using the OMAS API using the relationships guid
 * contained in this reference.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedFromObjectAnnotationsReference extends Reference implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(RelatedFromObjectAnnotationsReference.class);
    private static final String className = RelatedFromObjectAnnotationsReference.class.getName();

    final static protected String relationship_Type = "RelationshipAnnotation";
    protected Referenceable referenceable =null;



    public Referenceable getReferenceable() {
        return  referenceable;
    }
    public void setReferenceable(Referenceable referenceable) {
        this.referenceable = referenceable;
    }
    private String annotationType;
    /**
     * Name of the type of annotation.
     * @return AnnotationType
     */
    public String getAnnotationType() {
        return this.annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
    }
    private String summary;
    /**
     * Description of the findings.
     * @return Summary
     */
    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    private String confidenceLevel;
    /**
     * Level of certainty in the accuracy of the results.
     * @return ConfidenceLevel
     */
    public String getConfidenceLevel() {
        return this.confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
    private String expression;
    /**
     * Expression used to create the annotation.
     * @return Expression
     */
    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
    private String explanation;
    /**
     * Explanation of the analysis.
     * @return Explanation
     */
    public String getExplanation() {
        return this.explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    private String analysisStep;
    /**
     * The step in the pipeline that produced the annotation.
     * @return AnalysisStep
     */
    public String getAnalysisStep() {
        return this.analysisStep;
    }

    public void setAnalysisStep(String analysisStep) {
        this.analysisStep = analysisStep;
    }
    private String jsonProperties;
    /**
     * Additional properties used in the specification.
     * @return JsonProperties
     */
    public String getJsonProperties() {
        return this.jsonProperties;
    }

    public void setJsonProperties(String jsonProperties) {
        this.jsonProperties = jsonProperties;
    }
    private String additionalProperties;
    /**
     * Additional properties discovered during the analysis.
     * @return AdditionalProperties
     */
    public String getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
    private String annotationStatus;
    /**
     * Status of the processing as a result of the annotation.
     * @return AnnotationStatus
     */
    public String getAnnotationStatus() {
        return this.annotationStatus;
    }

    public void setAnnotationStatus(String annotationStatus) {
        this.annotationStatus = annotationStatus;
    }


    public RelatedFromObjectAnnotationsReference() {
        this(null, null, (Map<String, Object>) null);
    }

    public RelatedFromObjectAnnotationsReference(String guid) {
        this(guid, null, (Map<String, Object>) null);
    }

    public RelatedFromObjectAnnotationsReference(String guid, String relatedEndType) {
        this(guid, relatedEndType, (Map<String, Object>) null);
    }

    public RelatedFromObjectAnnotationsReference(String relatedEndType, Map<String, Object> uniqueAttributes) {
        this(null, relatedEndType, uniqueAttributes);
    }

    public RelatedFromObjectAnnotationsReference(String guid, String relatedEndType, Map<String, Object> uniqueAttributes) {
        setRelationshipGuid(guid);
        setRelatedEndType(relatedEndType);
        setUniqueAttributes(uniqueAttributes);
    }

    public RelatedFromObjectAnnotationsReference(Reference other) {
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
    public RelatedFromObjectAnnotationsReference(String entityGuid, OMRSLine line) {
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

    public RelatedFromObjectAnnotationsReference(Map objIdMap) {
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
	sb.append("this.annotationType ");
	sb.append("this.summary ");
	sb.append("this.confidenceLevel ");
	sb.append("this.expression ");
	sb.append("this.explanation ");
	sb.append("this.analysisStep ");
	sb.append("this.jsonProperties ");
	sb.append("this.additionalProperties ");
	sb.append("this.annotationStatus ");
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
        RelatedFromObjectAnnotationsReference typedThat =(RelatedFromObjectAnnotationsReference)that;
        if (this.annotationType != null && !Objects.equals(this.annotationType,typedThat.getAnnotationType())) {
            return false;
        }
        if (this.summary != null && !Objects.equals(this.summary,typedThat.getSummary())) {
            return false;
        }
        if (this.confidenceLevel != null && !Objects.equals(this.confidenceLevel,typedThat.getConfidenceLevel())) {
            return false;
        }
        if (this.expression != null && !Objects.equals(this.expression,typedThat.getExpression())) {
            return false;
        }
        if (this.explanation != null && !Objects.equals(this.explanation,typedThat.getExplanation())) {
            return false;
        }
        if (this.analysisStep != null && !Objects.equals(this.analysisStep,typedThat.getAnalysisStep())) {
            return false;
        }
        if (this.jsonProperties != null && !Objects.equals(this.jsonProperties,typedThat.getJsonProperties())) {
            return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,typedThat.getAdditionalProperties())) {
            return false;
        }
        if (this.annotationStatus != null && !Objects.equals(this.annotationStatus,typedThat.getAnnotationStatus())) {
            return false;
        }

        return Objects.equals(relatedEndType, that.getRelatedEndType()) &&
                Objects.equals(uniqueAttributes, that.getUniqueAttributes());
    }


    @Override
    public int hashCode() {
        return relatedEndGuid != null ? Objects.hash(relatedEndGuid) : Objects.hash(relatedEndType, uniqueAttributes
, this.annotationType
, this.summary
, this.confidenceLevel
, this.expression
, this.explanation
, this.analysisStep
, this.jsonProperties
, this.additionalProperties
, this.annotationStatus
);
    }
}
