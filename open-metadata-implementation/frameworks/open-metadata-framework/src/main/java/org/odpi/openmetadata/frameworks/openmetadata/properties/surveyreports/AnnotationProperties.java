/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
                @JsonSubTypes.Type(value = ResourceMeasureAnnotationProperties.class, name = "ResourceMeasureAnnotationProperties"),
                @JsonSubTypes.Type(value = DataFieldAnnotationProperties.class, name = "DataFieldAnnotationProperties"),
        })
public class AnnotationProperties extends AuthoredReferenceableProperties
{
    private String annotationType      = null;
    private String summary             = null;
    private long   sampleSize          = 0L;
    private int    samplePercent       = 0;
    private String samplingMethod      = null;
    private int    confidence          = 0;
    private String units               = null;
    private long   absoluteUncertainty = 0L;
    private long   relativeUncertainty = 0L;
    private String expression          = null;
    private String explanation         = null;
    private String analysisStep        = null;
    private String jsonProperties      = null;


    /**
     * Default constructor used by subclasses
     */
    public AnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.ANNOTATION.typeName;
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
            this.annotationType      = template.getAnnotationType();
            this.summary             = template.getSummary();
            this.sampleSize          = template.getSampleSize();
            this.samplePercent       = template.getSamplePercent();
            this.samplingMethod      = template.getSamplingMethod();
            this.units               = template.getUnits();
            this.absoluteUncertainty = template.getAbsoluteUncertainty();
            this.relativeUncertainty = template.getRelativeUncertainty();
            this.confidence          = template.getConfidence();
            this.expression          = template.getExpression();
            this.explanation         = template.getExplanation();
            this.analysisStep        = template.getAnalysisStep();
            this.jsonProperties      = template.getJsonProperties();
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
     * Retrieve the sample size associated with the annotation.
     *
     * @return long
     */
    public long getSampleSize()
    {
        return sampleSize;
    }


    /**
     * Set up the sample size associated with the annotation.
     *
     * @param sampleSize long
     */
    public void setSampleSize(long sampleSize)
    {
        this.sampleSize = sampleSize;
    }


    /**
     * Retrieve the sample percent associated with the annotation.
     *
     * @return int
     */
    public int getSamplePercent()
    {
        return samplePercent;
    }


    /**
     * Set up the sample percent associated with the annotation.
     *
     * @param samplePercent int
     */
    public void setSamplePercent(int samplePercent)
    {
        this.samplePercent = samplePercent;
    }


    /**
     * Retrieve the sampling method associated with the annotation.
     *
     * @return string
     */
    public String getSamplingMethod()
    {
        return samplingMethod;
    }


    /**
     * Set up the sampling method associated with the annotation.
     *
     * @param samplingMethod string
     */
    public void setSamplingMethod(String samplingMethod)
    {
        this.samplingMethod = samplingMethod;
    }


    /**
     * Return the confidence level of the survey action service that the annotation is correct.
     *
     * @return int confidence level
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the confidence level of the survey action service that the annotation is correct.
     *
     * @param confidence int confidence level
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Return the units used to describe the granularity.
     *
     * @return string description
     */
    public String getUnits()
    {
        return units;
    }


    /**
     * Set up the units used to describe the granularity.
     *
     * @param units string description
     */
    public void setUnits(String units)
    {
        this.units = units;
    }


    /**
     * Return the absolute uncertainty.
     *
     * @return long
     */
    public long getAbsoluteUncertainty()
    {
        return absoluteUncertainty;
    }


    /**
     * Set up the absolute uncertainty.
     *
     * @param absoluteUncertainty long
     */
    public void setAbsoluteUncertainty(long absoluteUncertainty)
    {
        this.absoluteUncertainty = absoluteUncertainty;
    }


    /**
     * Return the relative uncertainty.
     *
     * @return long
     */
    public long getRelativeUncertainty()
    {
        return relativeUncertainty;
    }


    /**
     * Set up the relative uncertainty.
     *
     * @param relativeUncertainty long
     */
    public void setRelativeUncertainty(long relativeUncertainty)
    {
        this.relativeUncertainty = relativeUncertainty;
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
                ", sampleSize=" + sampleSize +
                ", samplePercent=" + samplePercent +
                ", samplingMethod='" + samplingMethod + '\'' +
                ", confidenceLevel=" + confidence +
                ", units='" + units + '\'' +
                ", absoluteUncertainty=" + absoluteUncertainty +
                ", relativeUncertainty=" + relativeUncertainty +
                ", expression='" + expression + '\'' +
                ", explanation='" + explanation + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", jsonProperties='" + jsonProperties + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AnnotationProperties that = (AnnotationProperties) objectToCompare;
        return sampleSize == that.sampleSize &&
                samplePercent == that.samplePercent &&
                confidence == that.confidence &&
                absoluteUncertainty == that.absoluteUncertainty &&
                relativeUncertainty == that.relativeUncertainty &&
                Objects.equals(annotationType, that.annotationType) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(samplingMethod, that.samplingMethod) &&
                Objects.equals(units, that.units) &&
                Objects.equals(expression, that.expression) &&
                Objects.equals(explanation, that.explanation) &&
                Objects.equals(analysisStep, that.analysisStep) &&
                Objects.equals(jsonProperties, that.jsonProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), annotationType, summary, sampleSize, samplePercent, samplingMethod, confidence, units, absoluteUncertainty, relativeUncertainty, expression, explanation, analysisStep, jsonProperties);
    }
}
