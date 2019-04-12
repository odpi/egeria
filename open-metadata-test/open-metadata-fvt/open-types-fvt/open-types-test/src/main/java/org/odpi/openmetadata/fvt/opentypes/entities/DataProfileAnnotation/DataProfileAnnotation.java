/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataProfileAnnotation;

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
 * DataProfileAnnotation entity.
   A collection of properties about the values stored in a data field, or number of data fields, in an Asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  DataProfileAnnotation implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DataProfileAnnotation.class);
    private static final String className = DataProfileAnnotation.class.getName();
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
        "length",
        "inferredDataType",
        "inferredFormat",
        "inferredLength",
        "inferredPrecision",
        "inferredScale",
        "profileProperties",
        "profileFlags",
        "profileCounts",
        "valueList",
        "valueCount",
        "valueRangeFrom",
        "valueRangeTo",
        "averageValue",
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
        "length",
        "inferredDataType",
        "inferredFormat",
        "inferredLength",
        "inferredPrecision",
        "inferredScale",
        "valueList",
        "valueRangeFrom",
        "valueRangeTo",
        "averageValue",
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
         "profileProperties",
         "profileFlags",
         "profileCounts",
         "valueCount",
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
        // Additional profile properties discovered during the analysis.
        mapPropertyValue = new MapPropertyValue();
        // Additional flags (booleans) discovered during the analysis.
        mapPropertyValue = new MapPropertyValue();
        // Additional counts discovered during the analysis.
        mapPropertyValue = new MapPropertyValue();
        // Count of individual values in the data.
        mapPropertyValue = new MapPropertyValue();
        // Additional properties discovered during the analysis.
        mapPropertyValue = new MapPropertyValue();
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(length);
        instanceProperties.setProperty("length",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(inferredDataType);
        instanceProperties.setProperty("inferredDataType",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(inferredFormat);
        instanceProperties.setProperty("inferredFormat",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(inferredLength);
        instanceProperties.setProperty("inferredLength",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(inferredPrecision);
        instanceProperties.setProperty("inferredPrecision",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(inferredScale);
        instanceProperties.setProperty("inferredScale",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(valueList);
        instanceProperties.setProperty("valueList",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(valueRangeFrom);
        instanceProperties.setProperty("valueRangeFrom",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(valueRangeTo);
        instanceProperties.setProperty("valueRangeTo",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(averageValue);
        instanceProperties.setProperty("averageValue",primitivePropertyValue);
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

       private Integer length;
       /**
        * {@literal Length of the data field. }
        * @return {@code Integer }
        */
       public Integer getLength() {
           return this.length;
       }
       public void setLength(Integer length)  {
           this.length = length;
       }
       private String inferredDataType;
       /**
        * {@literal Inferred data type based on the data values. }
        * @return {@code String }
        */
       public String getInferredDataType() {
           return this.inferredDataType;
       }
       public void setInferredDataType(String inferredDataType)  {
           this.inferredDataType = inferredDataType;
       }
       private String inferredFormat;
       /**
        * {@literal Inferred data format based on the data values. }
        * @return {@code String }
        */
       public String getInferredFormat() {
           return this.inferredFormat;
       }
       public void setInferredFormat(String inferredFormat)  {
           this.inferredFormat = inferredFormat;
       }
       private Integer inferredLength;
       /**
        * {@literal Inferred data field length based on the data values. }
        * @return {@code Integer }
        */
       public Integer getInferredLength() {
           return this.inferredLength;
       }
       public void setInferredLength(Integer inferredLength)  {
           this.inferredLength = inferredLength;
       }
       private Integer inferredPrecision;
       /**
        * {@literal Inferred precision of the data based on the data values. }
        * @return {@code Integer }
        */
       public Integer getInferredPrecision() {
           return this.inferredPrecision;
       }
       public void setInferredPrecision(Integer inferredPrecision)  {
           this.inferredPrecision = inferredPrecision;
       }
       private Integer inferredScale;
       /**
        * {@literal Inferred scale applied to the data based on the data values. }
        * @return {@code Integer }
        */
       public Integer getInferredScale() {
           return this.inferredScale;
       }
       public void setInferredScale(Integer inferredScale)  {
           this.inferredScale = inferredScale;
       }
       private Map<String,String> profileProperties;
       /**
        * {@literal Additional profile properties discovered during the analysis. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getProfileProperties() {
           return this.profileProperties;
       }
       public void setProfileProperties(Map<String,String> profileProperties)  {
           this.profileProperties = profileProperties;
       }
       private Map<String,String> profileFlags;
       /**
        * {@literal Additional flags (booleans) discovered during the analysis. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getProfileFlags() {
           return this.profileFlags;
       }
       public void setProfileFlags(Map<String,String> profileFlags)  {
           this.profileFlags = profileFlags;
       }
       private Map<String,String> profileCounts;
       /**
        * {@literal Additional counts discovered during the analysis. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getProfileCounts() {
           return this.profileCounts;
       }
       public void setProfileCounts(Map<String,String> profileCounts)  {
           this.profileCounts = profileCounts;
       }
       private List<String> valueList;
       /**
        * {@literal List of individual values in the data. }
        * @return {@code {@code List<String> } }
        */
       public List<String> getValueList() {
           return this.valueList;
       }
       public void setValueList(List<String> valueList)  {
           this.valueList = valueList;
       }
       private Map<String,String> valueCount;
       /**
        * {@literal Count of individual values in the data. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getValueCount() {
           return this.valueCount;
       }
       public void setValueCount(Map<String,String> valueCount)  {
           this.valueCount = valueCount;
       }
       private String valueRangeFrom;
       /**
        * {@literal Lowest value in the data. }
        * @return {@code String }
        */
       public String getValueRangeFrom() {
           return this.valueRangeFrom;
       }
       public void setValueRangeFrom(String valueRangeFrom)  {
           this.valueRangeFrom = valueRangeFrom;
       }
       private String valueRangeTo;
       /**
        * {@literal Highest value in the data. }
        * @return {@code String }
        */
       public String getValueRangeTo() {
           return this.valueRangeTo;
       }
       public void setValueRangeTo(String valueRangeTo)  {
           this.valueRangeTo = valueRangeTo;
       }
       private String averageValue;
       /**
        * {@literal Typical value in the data. }
        * @return {@code String }
        */
       public String getAverageValue() {
           return this.averageValue;
       }
       public void setAverageValue(String averageValue)  {
           this.averageValue = averageValue;
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
     * Return the date/time that this DataProfileAnnotation should start to be used (null means it can be used from creationTime).
     * @return Date the DataProfileAnnotation becomes effective.
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
     * Return the date/time that this DataProfileAnnotation should no longer be used.
     *
     * @return Date the DataProfileAnnotation stops being effective.
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

        sb.append("DataProfileAnnotation{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("DataProfileAnnotation Attributes{");
    	sb.append("Length=" +this.length);
    	sb.append("InferredDataType=" +this.inferredDataType);
    	sb.append("InferredFormat=" +this.inferredFormat);
    	sb.append("InferredLength=" +this.inferredLength);
    	sb.append("InferredPrecision=" +this.inferredPrecision);
    	sb.append("InferredScale=" +this.inferredScale);
    	sb.append("ProfileProperties=" +this.profileProperties);
    	sb.append("ProfileFlags=" +this.profileFlags);
    	sb.append("ProfileCounts=" +this.profileCounts);
    	sb.append("ValueList=" +this.valueList);
    	sb.append("ValueCount=" +this.valueCount);
    	sb.append("ValueRangeFrom=" +this.valueRangeFrom);
    	sb.append("ValueRangeTo=" +this.valueRangeTo);
    	sb.append("AverageValue=" +this.averageValue);
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

        DataProfileAnnotation that = (DataProfileAnnotation) o;
        if (this.length != null && !Objects.equals(this.length,that.getLength())) {
             return false;
        }
        if (this.inferredDataType != null && !Objects.equals(this.inferredDataType,that.getInferredDataType())) {
             return false;
        }
        if (this.inferredFormat != null && !Objects.equals(this.inferredFormat,that.getInferredFormat())) {
             return false;
        }
        if (this.inferredLength != null && !Objects.equals(this.inferredLength,that.getInferredLength())) {
             return false;
        }
        if (this.inferredPrecision != null && !Objects.equals(this.inferredPrecision,that.getInferredPrecision())) {
             return false;
        }
        if (this.inferredScale != null && !Objects.equals(this.inferredScale,that.getInferredScale())) {
             return false;
        }
        if (this.profileProperties != null && !Objects.equals(this.profileProperties,that.getProfileProperties())) {
             return false;
        }
        if (this.profileFlags != null && !Objects.equals(this.profileFlags,that.getProfileFlags())) {
             return false;
        }
        if (this.profileCounts != null && !Objects.equals(this.profileCounts,that.getProfileCounts())) {
             return false;
        }
        if (this.valueList != null && !Objects.equals(this.valueList,that.getValueList())) {
             return false;
        }
        if (this.valueCount != null && !Objects.equals(this.valueCount,that.getValueCount())) {
             return false;
        }
        if (this.valueRangeFrom != null && !Objects.equals(this.valueRangeFrom,that.getValueRangeFrom())) {
             return false;
        }
        if (this.valueRangeTo != null && !Objects.equals(this.valueRangeTo,that.getValueRangeTo())) {
             return false;
        }
        if (this.averageValue != null && !Objects.equals(this.averageValue,that.getAverageValue())) {
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

        // We view dataProfileAnnotations as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
          , this.length
          , this.inferredDataType
          , this.inferredFormat
          , this.inferredLength
          , this.inferredPrecision
          , this.inferredScale
          , this.profileProperties
          , this.profileFlags
          , this.profileCounts
          , this.valueList
          , this.valueCount
          , this.valueRangeFrom
          , this.valueRangeTo
          , this.averageValue
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
