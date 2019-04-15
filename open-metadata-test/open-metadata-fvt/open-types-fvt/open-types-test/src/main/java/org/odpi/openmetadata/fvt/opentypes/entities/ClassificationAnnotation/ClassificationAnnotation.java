/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.ClassificationAnnotation;

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.fvt.opentypes.common.SystemAttributes;
import org.odpi.openmetadata.fvt.opentypes.common.ClassificationBean;
import org.odpi.openmetadata.fvt.opentypes.enums.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;

/**
 * ClassificationAnnotation entity.
   A recommendation for classifications that could be added to all or part of an Asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  ClassificationAnnotation implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ClassificationAnnotation.class);
    private static final String className = ClassificationAnnotation.class.getName();
    private SystemAttributes systemAttributes = null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    List<ClassificationBean> classifications = null;

    private Map<String, Object> extraAttributes =null;
    private Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassificationBeans =null;


    /**
     * Get the system attributes
     * @return SystemAttributes if populated, null otherwise.
     */
    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    // attributes
    public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "candidateClassifications",
        "annotationType",
        "summary",
        "confidenceLevel",
        "expression",
        "explanation",
        "analysisStep",
        "jsonProperties",
        "additionalProperties",
        "annotationStatus",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "annotationType",
        "summary",
        "confidenceLevel",
        "expression",
        "explanation",
        "analysisStep",
        "jsonProperties",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "annotationStatus",

         // Terminate the list
          null
    };
    public static final String[] MAP_NAMES_SET_VALUES = new String[] {
         "candidateClassifications",
         "additionalProperties",

         // Terminate the list
         null
    };
    public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
    public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
               log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        enumPropertyValue = new EnumPropertyValue();
        // status of the processing as a result of the annotation.
        enumPropertyValue.setOrdinal(annotationStatus.ordinal());
        enumPropertyValue.setSymbolicName(annotationStatus.name());
        instanceProperties.setProperty("annotationStatus",enumPropertyValue);
        MapPropertyValue mapPropertyValue=null;
        // Potential classification names and properties as JSON.
        mapPropertyValue = new MapPropertyValue();
        // Additional properties discovered during the analysis.
        mapPropertyValue = new MapPropertyValue();
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(annotationType);
        instanceProperties.setProperty("annotationType",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(summary);
        instanceProperties.setProperty("summary",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(confidenceLevel);
        instanceProperties.setProperty("confidenceLevel",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(expression);
        instanceProperties.setProperty("expression",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(explanation);
        instanceProperties.setProperty("explanation",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(analysisStep);
        instanceProperties.setProperty("analysisStep",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(jsonProperties);
        instanceProperties.setProperty("jsonProperties",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private Map<String,String> candidateClassifications;
       /**
        * {@literal Potential classification names and properties as JSON. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getCandidateClassifications() {
           return this.candidateClassifications;
       }
       public void setCandidateClassifications(Map<String,String> candidateClassifications)  {
           this.candidateClassifications = candidateClassifications;
       }
       private String annotationType;
       /**
        * {@literal Name of the type of annotation. }
        * @return {@code String }
        */
       public String getAnnotationType() {
           return this.annotationType;
       }
       public void setAnnotationType(String annotationType)  {
           this.annotationType = annotationType;
       }
       private String summary;
       /**
        * {@literal Description of the findings. }
        * @return {@code String }
        */
       public String getSummary() {
           return this.summary;
       }
       public void setSummary(String summary)  {
           this.summary = summary;
       }
       private Integer confidenceLevel;
       /**
        * {@literal Level of certainty in the accuracy of the results. }
        * @return {@code Integer }
        */
       public Integer getConfidenceLevel() {
           return this.confidenceLevel;
       }
       public void setConfidenceLevel(Integer confidenceLevel)  {
           this.confidenceLevel = confidenceLevel;
       }
       private String expression;
       /**
        * {@literal Expression used to create the annotation. }
        * @return {@code String }
        */
       public String getExpression() {
           return this.expression;
       }
       public void setExpression(String expression)  {
           this.expression = expression;
       }
       private String explanation;
       /**
        * {@literal Explanation of the analysis. }
        * @return {@code String }
        */
       public String getExplanation() {
           return this.explanation;
       }
       public void setExplanation(String explanation)  {
           this.explanation = explanation;
       }
       private String analysisStep;
       /**
        * {@literal The step in the pipeline that produced the annotation. }
        * @return {@code String }
        */
       public String getAnalysisStep() {
           return this.analysisStep;
       }
       public void setAnalysisStep(String analysisStep)  {
           this.analysisStep = analysisStep;
       }
       private String jsonProperties;
       /**
        * {@literal Additional properties used in the specification. }
        * @return {@code String }
        */
       public String getJsonProperties() {
           return this.jsonProperties;
       }
       public void setJsonProperties(String jsonProperties)  {
           this.jsonProperties = jsonProperties;
       }
       private Map<String,String> additionalProperties;
       /**
        * {@literal Additional properties discovered during the analysis. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getAdditionalProperties() {
           return this.additionalProperties;
       }
       public void setAdditionalProperties(Map<String,String> additionalProperties)  {
           this.additionalProperties = additionalProperties;
       }
       private AnnotationStatus annotationStatus;
       /**
        * {@literal Status of the processing as a result of the annotation. }
        * @return {@code AnnotationStatus }
        */
       public AnnotationStatus getAnnotationStatus() {
           return this.annotationStatus;
       }
       public void setAnnotationStatus(AnnotationStatus annotationStatus)  {
           this.annotationStatus = annotationStatus;
       }
    /**
     * Return the date/time that this ClassificationAnnotation should start to be used (null means it can be used from creationTime).
     * @return Date the ClassificationAnnotation becomes effective.
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
     * Return the date/time that this ClassificationAnnotation should no longer be used.
     *
     * @return Date the ClassificationAnnotation stops being effective.
     */
    public Date getEffectiveToTime()
    {
        return effectiveToTime;
    }
    public void setEffectiveToTime(Date effectiveToTime)
    {
        this.effectiveToTime = effectiveToTime;
    }

    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    public void setClassificationBeans(List<ClassificationBean> classifications) {
        this.classifications = classifications;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return map of attributes, null if there are none
     */
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

     /**
     * ClassificationBeans
     * @return List of ClassificationBeans, null if there are none
     */
    public List<ClassificationBean> getClassificationBeans() {
        return classifications;
    }
    /**
      * Extra classifications are classifications that are not in the open metadata model - we include the OMRS ClassificationBeans.
      * @return Map of classifications with the classification Name as the map key
      */
    public Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> getExtraClassificationBeans() {
        return extraClassificationBeans;
    }

    public void setExtraClassificationBeans(Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassificationBeans) {
        this.extraClassificationBeans = extraClassificationBeans;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("ClassificationAnnotation{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("ClassificationAnnotation Attributes{");
    	sb.append("CandidateClassifications=" +this.candidateClassifications);
    	sb.append("AnnotationType=" +this.annotationType);
    	sb.append("Summary=" +this.summary);
    	sb.append("ConfidenceLevel=" +this.confidenceLevel);
    	sb.append("Expression=" +this.expression);
    	sb.append("Explanation=" +this.explanation);
    	sb.append("AnalysisStep=" +this.analysisStep);
    	sb.append("JsonProperties=" +this.jsonProperties);
    	sb.append("AdditionalProperties=" +this.additionalProperties);
    	sb.append("AnnotationStatus=" +this.annotationStatus);

        sb.append('}');
        if (classifications != null) {
        sb.append(", classifications=[");
            for (ClassificationBean classification:classifications) {
                sb.append(classification.toString()).append(", ");
            }
            sb.append(" ],");
        }
        sb.append(", extraAttributes=[");
        if (extraAttributes !=null) {
            for (String attrname: extraAttributes.keySet()) {
                sb.append(attrname).append(":");
                sb.append(extraAttributes.get(attrname)).append(", ");
            }
        }
        sb.append(" ]");

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }

        ClassificationAnnotation that = (ClassificationAnnotation) o;
        if (this.candidateClassifications != null && !Objects.equals(this.candidateClassifications,that.getCandidateClassifications())) {
             return false;
        }
        if (this.annotationType != null && !Objects.equals(this.annotationType,that.getAnnotationType())) {
             return false;
        }
        if (this.summary != null && !Objects.equals(this.summary,that.getSummary())) {
             return false;
        }
        if (this.confidenceLevel != null && !Objects.equals(this.confidenceLevel,that.getConfidenceLevel())) {
             return false;
        }
        if (this.expression != null && !Objects.equals(this.expression,that.getExpression())) {
             return false;
        }
        if (this.explanation != null && !Objects.equals(this.explanation,that.getExplanation())) {
             return false;
        }
        if (this.analysisStep != null && !Objects.equals(this.analysisStep,that.getAnalysisStep())) {
             return false;
        }
        if (this.jsonProperties != null && !Objects.equals(this.jsonProperties,that.getJsonProperties())) {
             return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
             return false;
        }
        if (this.annotationStatus != null && !Objects.equals(this.annotationStatus,that.getAnnotationStatus())) {
             return false;
        }

        // We view classificationAnnotations as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
          , this.candidateClassifications
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

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
