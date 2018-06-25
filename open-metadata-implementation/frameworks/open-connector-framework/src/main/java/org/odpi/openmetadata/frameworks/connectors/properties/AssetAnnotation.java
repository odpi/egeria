/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Annotation;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.AnnotationStatus;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * An annotation describes the results of an analysis undertaken by an Open Discovery Framework (ODF) discovery service.
 * It describes when the analysis happened, the type of analysis and the results.
 */
public class AssetAnnotation extends AssetElementHeader
{
    protected Annotation  annotationBean;

    /**
     * Bean constructor
     *
     * @param annotationBean bean containing all of the properties
     */
    public AssetAnnotation(Annotation  annotationBean)
    {
        super(annotationBean);

        if (annotationBean == null)
        {
            this.annotationBean = new Annotation();
        }
        else
        {
            this.annotationBean = annotationBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this annotation is attached to.
     * @param annotationBean bean containing all of the properties
     */
    public AssetAnnotation(AssetDescriptor    parentAsset,
                           Annotation         annotationBean)
    {
        super(parentAsset, annotationBean);

        if (annotationBean == null)
        {
            this.annotationBean = new Annotation();
        }
        else
        {
            this.annotationBean = annotationBean;
        }
    }


    /**
     * Copy/clone Constructor
     *
     * @param parentAsset description of the asset that this annotation is attached to.
     * @param templateAnnotation template object to copy.
     */
    public AssetAnnotation(AssetDescriptor    parentAsset, AssetAnnotation templateAnnotation)
    {
        super(parentAsset, templateAnnotation);

        if (templateAnnotation == null)
        {
            this.annotationBean = new Annotation();
        }
        else
        {
            this.annotationBean = templateAnnotation.getAnnotationBean();
        }
    }


    /**
     * Return the bean containing all of the properties - used in the cloning process.
     *
     * @return Annotation bean
     */
    protected  Annotation  getAnnotationBean() { return annotationBean; }


    /**
     * Return the name of the discovery analysis report that created this annotation.
     *
     * @return String report name
     */
    public String getReportName() { return annotationBean.getReportName(); }


    /**
     * Return the discovery analysis report description that this annotation is a part of.
     *
     * @return String report description
     */
    public String getReportDescription() { return annotationBean.getReportDescription(); }


    /**
     * Return the creation date for the annotation.  If this date is not known then null is returned.
     *
     * @return Date that the annotation was created.
     */
    public Date getCreationDate() { return annotationBean.getCreationDate(); }


    /**
     * Return the properties that hold the parameters used to drive the discovery service's analysis.
     *
     * @return AdditionalProperties object storing the analysis parameters
     */
    public AdditionalProperties getAnalysisParameters()
    {
        Map<String, Object>   analysisParameters = annotationBean.getAnalysisParameters();

        if (analysisParameters == null)
        {
            return null;
        }
        else if (analysisParameters.isEmpty())
        {
            return null;
        }
        else
        {
            return new AdditionalProperties(super.getParentAsset(), analysisParameters);
        }
    }


    /**
     * Return the informal name for the type of annotation.
     *
     * @return String annotation type
     */
    public String getAnnotationType() { return annotationBean.getAnnotationType(); }


    /**
     * Return the summary description for the annotation.
     *
     * @return String summary of annotation
     */
    public String getSummary() { return annotationBean.getSummary(); }


    /**
     * Return the confidence level of the discovery service that the annotation is correct.
     *
     * @return int confidence level
     */
    public int getConfidenceLevel() { return annotationBean.getConfidenceLevel(); }


    /**
     * Return the expression that represent the relationship between the annotation and the asset.
     *
     * @return String expression
     */
    public String getExpression() { return annotationBean.getExpression(); }


    /**
     * Return the explanation for the annotation.
     *
     * @return String explanation
     */
    public String getExplanation() { return annotationBean.getExplanation(); }


    /**
     * Return a description of the analysis step that the discovery service was in when it created the annotation.
     *
     * @return String analysis step
     */
    public String getAnalysisStep() { return annotationBean.getAnalysisStep(); }


    /**
     * Return the JSON properties associated with the annotation.
     *
     * @return String JSON properties of annotation
     */
    public String getJsonProperties() { return annotationBean.getJsonProperties(); }


    /**
     * Return the current status of the annotation.
     *
     * @return AnnotationStatus current status of annotation
     */
    public AnnotationStatus getAnnotationStatus() { return annotationBean.getAnnotationStatus(); }


    /**
     * Return the date that this annotation was reviewed.  If no review has taken place then this property is null.
     *
     * @return Date review date
     */
    public Date getReviewDate() { return annotationBean.getReviewDate(); }


    /**
     * Return the name of the steward that reviewed the annotation.
     *
     * @return String steward's name.
     */
    public String getSteward() { return annotationBean.getSteward(); }


    /**
     * Return any comments made by the steward during the review.
     *
     * @return String review comment
     */
    public String getReviewComment() { return annotationBean.getReviewComment(); }


    /**
     * Return the additional properties for the Annotation.
     *
     * @return AdditionalProperties additional properties object
     */
    public AdditionalProperties getAdditionalProperties()
    {
        Map<String, Object>   additionalProperties = annotationBean.getAdditionalProperties();

        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new AdditionalProperties(super.getParentAsset(), additionalProperties);
        }
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
        if (!(objectToCompare instanceof AssetAnnotation))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetAnnotation that = (AssetAnnotation) objectToCompare;
        return Objects.equals(getAnnotationBean(), that.getAnnotationBean());
    }
}