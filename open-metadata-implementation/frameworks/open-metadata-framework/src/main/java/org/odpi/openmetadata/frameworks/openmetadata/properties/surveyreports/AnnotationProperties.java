/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AnnotationProperties is used to record information gleaned from a survey action service.  The subtypes contain more detail.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SchemaAnalysisAnnotationProperties.class, name = "SchemaAnalysisAnnotationProperties"),
                @JsonSubTypes.Type(value = ResourceMeasureAnnotationProperties.class, name = "DataSourceMeasurementAnnotation"),
                @JsonSubTypes.Type(value = DataFieldAnnotationProperties.class, name = "DataFieldAnnotationProperties"),
        })
public class AnnotationProperties extends OpenMetadataRootProperties
{
    private String              annotationType       = null;
    private String              summary              = null;
    private int                 confidenceLevel      = 0;
    private String              expression           = null;
    private String              explanation          = null;
    private String              analysisStep         = null;
    private String              jsonProperties       = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor used by subclasses
     */
    public AnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ANNOTATION.typeName);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public AnnotationProperties(AnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.annotationType = template.getAnnotationType();
            this.summary = template.getSummary();
            this.confidenceLevel = template.getConfidenceLevel();
            this.expression = template.getExpression();
            this.explanation = template.getExplanation();
            this.analysisStep = template.getAnalysisStep();
            this.jsonProperties = template.getJsonProperties();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }




    /**
     * Return the informal name for the type of annotation.
     *
     * @return String annotation type
     */
    public String getAnnotationType()
    {
        return annotationType;
    }


    /**
     * Set up the informal name for the type of annotation.
     *
     * @param annotationType String annotation type
     */
    public void setAnnotationType(String annotationType)
    {
        this.annotationType = annotationType;
    }


    /**
     * Return the summary description for the annotation.
     *
     * @return String summary of annotation
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up the summary description for the annotation.
     *
     * @param summary String summary of annotation
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Return the confidence level of the survey action service that the annotation is correct.
     *
     * @return int confidence level
     */
    public int getConfidenceLevel()
    {
        return confidenceLevel;
    }


    /**
     * Set up the confidence level of the survey action service that the annotation is correct.
     *
     * @param confidenceLevel int confidence level
     */
    public void setConfidenceLevel(int confidenceLevel)
    {
        this.confidenceLevel = confidenceLevel;
    }


    /**
     * Return the expression that represent the relationship between the annotation and the asset.
     *
     * @return String expression
     */
    public String getExpression()
    {
        return expression;
    }


    /**
     * Set up the expression that represent the relationship between the annotation and the asset.
     *
     * @param expression String expression
     */
    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    /**
     * Return the explanation for the annotation.
     *
     * @return String explanation
     */
    public String getExplanation() {
        return explanation;
    }


    /**
     * Set up the explanation for the annotation.
     *
     * @param explanation explanation
     */
    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }


    /**
     * Return a description of the analysis step that the survey action service was in when it created the annotation.
     *
     * @return String analysis step
     */
    public String getAnalysisStep()
    {
        return analysisStep;
    }


    /**
     * Set up a description of the analysis step that the survey action service was in when it created the annotation.
     *
     * @param analysisStep analysis step
     */
    public void setAnalysisStep(String analysisStep)
    {
        this.analysisStep = analysisStep;
    }


    /**
     * Return the JSON properties associated with the annotation.
     *
     * @return String JSON properties of annotation
     */
    public String getJsonProperties()
    {
        return jsonProperties;
    }


    /**
     * Set up the JSON properties associated with the annotation.
     *
     * @param jsonProperties JSON properties of annotation
     */
    public void setJsonProperties(String jsonProperties)
    {
        this.jsonProperties = jsonProperties;
    }


    /**
     * Return the additional properties for the AnnotationProperties.
     *
     * @return properties map
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties for the AnnotationProperties.
     *
     * @param additionalProperties properties map
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AnnotationProperties{" +
                "annotationType='" + annotationType + '\'' +
                ", summary='" + summary + '\'' +
                ", confidenceLevel=" + confidenceLevel +
                ", expression='" + expression + '\'' +
                ", explanation='" + explanation + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", jsonProperties='" + jsonProperties + '\'' +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AnnotationProperties that = (AnnotationProperties) objectToCompare;
        return confidenceLevel == that.confidenceLevel &&
                Objects.equals(annotationType, that.annotationType) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(expression, that.expression) &&
                Objects.equals(explanation, that.explanation) &&
                Objects.equals(analysisStep, that.analysisStep) &&
                Objects.equals(jsonProperties, that.jsonProperties) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), annotationType, summary, confidenceLevel, expression, explanation, analysisStep, jsonProperties, additionalProperties);
    }
}
