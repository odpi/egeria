/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Annotation extends ElementHeader
{
    /*
     * Details from the AnnotationReport entity
     */
    protected String              reportName         = null;
    protected String              reportDescription  = null;
    protected Date                creationDate       = null;
    protected Map<String, Object> analysisParameters = null;

    /*
     * Details from the Annotation entity itself
     */
    protected String           annotationType   = null;
    protected String           summary          = null;
    protected int              confidenceLevel  = 0;
    protected String           expression       = null;
    protected String           explanation      = null;
    protected String           analysisStep     = null;
    protected String           jsonProperties   = null;
    protected AnnotationStatus annotationStatus = null;

    /*
     * Details from the latest AnnotationReview entity.
     */
    protected Date             reviewDate    = null;
    protected String           steward       = null;
    protected String           reviewComment = null;

    /*
     * Additional properties added directly to the Annotation entity and supported by
     * the sub-types of Annotation.
     */
    protected Map<String, Object>  additionalProperties = null;


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
     * @param templateAnnotation template object to copy.
     */
    public Annotation(Annotation templateAnnotation)
    {
        super(templateAnnotation);

        if (templateAnnotation != null)
        {
            this.reportName = templateAnnotation.getReportName();
            this.reportDescription = templateAnnotation.getReportDescription();
            this.creationDate = templateAnnotation.getCreationDate();
            this.analysisParameters = templateAnnotation.getAnalysisParameters();
            this.annotationType = templateAnnotation.getAnnotationType();
            this.summary = templateAnnotation.getSummary();
            this.confidenceLevel = templateAnnotation.getConfidenceLevel();
            this.expression = templateAnnotation.getExpression();
            this.explanation = templateAnnotation.getExplanation();
            this.analysisStep = templateAnnotation.getAnalysisStep();
            this.jsonProperties = templateAnnotation.getJsonProperties();
            this.annotationStatus = templateAnnotation.getAnnotationStatus();
            this.reviewDate = templateAnnotation.getReviewDate();
            this.steward = templateAnnotation.getSteward();
            this.reviewComment = templateAnnotation.getReviewComment();
            this.additionalProperties = new HashMap<>(templateAnnotation.getAdditionalProperties());
        }
    }


    /**
     * Return the name of the discovery analysis report that created this annotation.
     *
     * @return String report name
     */
    public String getReportName()
    {
        return reportName;
    }


    /**
     * Set up the name of the discovery analysis report that created this annotation.
     *
     * @param reportName  String report name
     */
    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }


    /**
     * Return the discovery analysis report description that this annotation is a part of.
     *
     * @return String report description
     */
    public String getReportDescription()
    {
        return reportDescription;
    }


    /**
     * Set up the discovery analysis report description that this annotation is a part of.
     *
     * @param reportDescription String report description
     */
    public void setReportDescription(String reportDescription)
    {
        this.reportDescription = reportDescription;
    }


    /**
     * Return the creation date for the annotation.  If this date is not known then null is returned.
     *
     * @return Date that the annotation was created.
     */
    public Date getCreationDate() {
        return creationDate;
    }


    /**
     * Set up the creation date for the annotation.  If this date is not known then null is returned.
     *
     * @param creationDate Date that the annotation was created.
     */
    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }


    /**
     * Return the properties that hold the parameters used to drive the discovery service's analysis.
     *
     * @return AdditionalProperties object storing the analysis parameters
     */
    public Map<String, Object> getAnalysisParameters()
    {
        return analysisParameters;
    }


    /**
     * Set up the properties that hold the parameters used to drive the discovery service's analysis.
     *
     * @param analysisParameters AdditionalProperties object storing the analysis parameters
     */
    public void setAnalysisParameters(Map<String, Object> analysisParameters)
    {
        this.analysisParameters = analysisParameters;
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
     * @return AdditionalProperties additional properties object
     */
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties for the Annotation.
     *
     * @param additionalProperties additional properties object
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
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
                "reportName='" + reportName + '\'' +
                ", reportDescription='" + reportDescription + '\'' +
                ", creationDate=" + creationDate +
                ", analysisParameters=" + analysisParameters +
                ", annotationType='" + annotationType + '\'' +
                ", summary='" + summary + '\'' +
                ", confidenceLevel=" + confidenceLevel +
                ", expression='" + expression + '\'' +
                ", explanation='" + explanation + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", jsonProperties='" + jsonProperties + '\'' +
                ", annotationStatus=" + annotationStatus +
                ", reviewDate=" + reviewDate +
                ", steward='" + steward + '\'' +
                ", reviewComment='" + reviewComment + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        if (!(objectToCompare instanceof Annotation))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Annotation that = (Annotation) objectToCompare;
        return getConfidenceLevel() == that.getConfidenceLevel() &&
                Objects.equals(getReportName(), that.getReportName()) &&
                Objects.equals(getReportDescription(), that.getReportDescription()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getAnalysisParameters(), that.getAnalysisParameters()) &&
                Objects.equals(getAnnotationType(), that.getAnnotationType()) &&
                Objects.equals(getSummary(), that.getSummary()) &&
                Objects.equals(getExpression(), that.getExpression()) &&
                Objects.equals(getExplanation(), that.getExplanation()) &&
                Objects.equals(getAnalysisStep(), that.getAnalysisStep()) &&
                Objects.equals(getJsonProperties(), that.getJsonProperties()) &&
                getAnnotationStatus() == that.getAnnotationStatus() &&
                Objects.equals(getReviewDate(), that.getReviewDate()) &&
                Objects.equals(getSteward(), that.getSteward()) &&
                Objects.equals(getReviewComment(), that.getReviewComment()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }
}
