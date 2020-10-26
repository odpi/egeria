/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Annotation is used to record information gleaned from a discovery service.  The subtypes contain more detail.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SchemaAnalysisAnnotation.class, name = "SchemaAnalysisAnnotation"),
                @JsonSubTypes.Type(value = DataSourceMeasurementAnnotation.class, name = "DataSourceMeasurementAnnotation"),
                @JsonSubTypes.Type(value = DataFieldAnnotation.class, name = "DataFieldAnnotation"),
                @JsonSubTypes.Type(value = SuspectDuplicateAnnotation.class, name = "SuspectDuplicateAnnotation"),
        })
public class Annotation extends PropertyBase
{
    private static final long    serialVersionUID = 1L;

    private String           annotationType   = null;
    private String           summary          = null;
    private int              confidenceLevel  = 0;
    private String           expression       = null;
    private String           explanation      = null;
    private String           analysisStep     = null;
    private String           jsonProperties   = null;

    /*
     * Details of Annotations attached to this Annotation
     */
    private int              numAttachedAnnotations = 0;

    /*
     * Details from the latest AnnotationReview entity.
     */
    private AnnotationStatus annotationStatus = null;
    private Date             reviewDate       = null;
    private String           steward          = null;
    private String           reviewComment    = null;


    private Map<String, String>  additionalProperties = null;



    /**
     * Default constructor used by subclasses
     */
    public Annotation()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public Annotation(Annotation template)
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
            this.numAttachedAnnotations = template.getNumAttachedAnnotations();
            this.annotationStatus = template.getAnnotationStatus();
            this.reviewDate = template.getReviewDate();
            this.steward = template.getSteward();
            this.reviewComment = template.getReviewComment();
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
     * Return the confidence level of the discovery service that the annotation is correct.
     *
     * @return int confidence level
     */
    public int getConfidenceLevel()
    {
        return confidenceLevel;
    }


    /**
     * Set up the confidence level of the discovery service that the annotation is correct.
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
     * Return a description of the analysis step that the discovery service was in when it created the annotation.
     *
     * @return String analysis step
     */
    public String getAnalysisStep()
    {
        return analysisStep;
    }


    /**
     * Set up a description of the analysis step that the discovery service was in when it created the annotation.
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
     * Return the current status of the annotation.
     *
     * @return AnnotationStatus current status of annotation
     */
    public AnnotationStatus getAnnotationStatus()
    {
        return annotationStatus;
    }


    /**
     * Set up the current status of the annotation.
     *
     * @param annotationStatus current status of annotation
     */
    public void setAnnotationStatus(AnnotationStatus annotationStatus)
    {
        this.annotationStatus = annotationStatus;
    }


    /**
     * Return the number of annotations attached to the this annotation.  These generally add further information.
     *
     * @return number of annotations
     */
    public int getNumAttachedAnnotations()
    {
        return numAttachedAnnotations;
    }


    /**
     * Set up the number of annotations attached to the this annotation.  These generally add further information.
     *
     * @param number number of annotations
     */
    public void setNumAttachedAnnotations(int number)
    {
        this.numAttachedAnnotations = number;
    }


    /**
     * Return the date that this annotation was reviewed.  If no review has taken place then this property is null.
     *
     * @return Date review date
     */
    public Date getReviewDate()
    {
        return reviewDate;
    }


    /**
     * Set up the date that this annotation was reviewed.  If no review has taken place then this property is null.
     *
     * @param reviewDate review date
     */
    public void setReviewDate(Date reviewDate)
    {
        this.reviewDate = reviewDate;
    }


    /**
     * Return the name of the steward that reviewed the annotation.
     *
     * @return String steward's name.
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the name of the steward that reviewed the annotation.
     *
     * @param steward steward's name.
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return any comments made by the steward during the review.
     *
     * @return String review comment
     */
    public String getReviewComment()
    {
        return reviewComment;
    }


    /**
     * Set up any comments made by the steward during the review.
     *
     * @param reviewComment review comment
     */
    public void setReviewComment(String reviewComment)
    {
        this.reviewComment = reviewComment;
    }


    /**
     * Return the additional properties for the Annotation.
     *
     * @return properties map
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties for the Annotation.
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
        return "Annotation{" +
                "annotationType='" + annotationType + '\'' +
                ", summary='" + summary + '\'' +
                ", confidenceLevel=" + confidenceLevel +
                ", expression='" + expression + '\'' +
                ", explanation='" + explanation + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", jsonProperties='" + jsonProperties + '\'' +
                ", numAttachedAnnotations=" + numAttachedAnnotations +
                ", annotationStatus=" + annotationStatus +
                ", reviewDate=" + reviewDate +
                ", steward='" + steward + '\'' +
                ", reviewComment='" + reviewComment + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        Annotation that = (Annotation) objectToCompare;
        return confidenceLevel == that.confidenceLevel &&
                numAttachedAnnotations == that.numAttachedAnnotations &&
                Objects.equals(annotationType, that.annotationType) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(expression, that.expression) &&
                Objects.equals(explanation, that.explanation) &&
                Objects.equals(analysisStep, that.analysisStep) &&
                Objects.equals(jsonProperties, that.jsonProperties) &&
                annotationStatus == that.annotationStatus &&
                Objects.equals(reviewDate, that.reviewDate) &&
                Objects.equals(steward, that.steward) &&
                Objects.equals(reviewComment, that.reviewComment) &&
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
        return Objects.hash(annotationType, summary, confidenceLevel, expression, explanation, analysisStep, jsonProperties, numAttachedAnnotations
                , annotationStatus, reviewDate, steward, reviewComment, additionalProperties);
    }
}
