/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import java.util.Date;
import java.util.Map;
import java.util.Objects;


/**
 * SurveyContext provides a survey action service with access to information about
 * the survey request along with access to the open metadata repository interfaces.
 */
public class SurveyContext
{
    private final String                  userId;
    protected     String                  surveyReportGUID    = null;
    protected     String                  reportQualifiedName = null;
    protected     String                  reportDisplayName   = null;
    protected     String                  reportDescription   = null;
    protected     Date                    creationDate        = new Date();
    protected     String                  analysisStep        = null;
    private final String                  assetGUID;
    private final Map<String, String>     requestParameters;
    private final SurveyAssetStore        assetStore;
    private final AnnotationStore         annotationStore;
    private final SurveyOpenMetadataStore openMetadataStore;


    /**
     * Constructor sets up the key parameters for using the context.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the survey action service
     * @param assetStore survey asset store for the survey action service
     * @param annotationStore annotation store for the survey action service
     * @param openMetadataStore generic metadata API from the Governance Action Framework (GAF)
     */
    public SurveyContext(String                     userId,
                         String                     assetGUID,
                         Map<String, String>        requestParameters,
                         SurveyAssetStore           assetStore,
                         AnnotationStore            annotationStore,
                         SurveyOpenMetadataStore    openMetadataStore)
    {
        this.userId            = userId;
        this.assetGUID         = assetGUID;
        this.requestParameters = requestParameters;
        this.assetStore        = assetStore;
        this.annotationStore   = annotationStore;
        this.openMetadataStore = openMetadataStore;
    }


    /**
     * Return the report identifier for this survey context.  Any new annotations added to this survey context
     * will be linked to this report.
     *
     * @return unique identifier (guid) of the new survey report.
     */
    public String getSurveyReportGUID()
    {
        return surveyReportGUID;
    }


    /**
     * Return the locally defined analysis step.  This value is used in annotations generated in this phase.
     *
     * @return name of analysis step
     */
    public String getAnalysisStep()
    {
        return analysisStep;
    }


    /**
     * Set up the name of the current analysis step.
     *
     * @param analysisStep name
     */
    public void setAnalysisStep(String analysisStep)
    {
        this.analysisStep = analysisStep;
    }


    /**
     * Return the unique name of the survey report that will result from this survey request.
     *
     * @return String report name
     */
    public String getReportQualifiedName()
    {
        return reportQualifiedName;
    }


    /**
     * Set up the unique name of the survey report that will result from this survey request.
     * The survey action engine will set up a default fully-qualified name.  This method enables it to be overridden.
     *
     * @param reportName  String report name
     */
    public void setReportQualifiedName(String reportName)
    {
        this.reportQualifiedName = reportName;
    }


    /**
     * Return the display name of the survey report that will result from this survey request.
     *
     * @return String report name
     */
    public String getReportDisplayName()
    {
        return reportDisplayName;
    }


    /**
     * Set up the display name of the survey report that will result from this survey request.
     * The default name is null.
     *
     * @param reportName  String report name
     */
    public void setReportDisplayName(String reportName)
    {
        this.reportDisplayName = reportName;
    }


    /**
     * Return the description for the survey report that will result from this survey request.
     * The default value is null.
     *
     * @return String report description
     */
    public String getReportDescription()
    {
        return reportDescription;
    }


    /**
     * Set up the description for the survey report that will result from this survey request.
     *
     * @param reportDescription String report description
     */
    public void setReportDescription(String reportDescription)
    {
        this.reportDescription = reportDescription;
    }


    /**
     * Return the creation date for the survey report that will result from this survey request.
     *
     * @return Date that the report was created.
     */
    public Date getReportCreationDate() {
        return creationDate;
    }


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     */
    public String getAssetGUID()
    {
        return assetGUID;
    }


    /**
     * Return the properties that hold the parameters used to drive the survey action service's analysis.
     *
     * @return AdditionalProperties object storing the analysis parameters
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Return the asset store for the survey action service.  This is able to provide a connector to the asset
     * configured with the properties of the asset from a property server.
     *
     * @return asset store
     */
    public SurveyAssetStore getAssetStore()
    {
        return assetStore;
    }


    /**
     * Return the annotation store for the survey action service.  This is where the annotations are stored and
     * retrieved from.
     *
     * @return annotation store
     */
    public AnnotationStore getAnnotationStore()
    {
        return annotationStore;
    }


    /**
     * Return a generic interface for accessing and updating open metadata elements, classifications and relationships.
     *
     * @return open metadata store
     */
    public SurveyOpenMetadataStore getOpenMetadataStore()
    {
        return openMetadataStore;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SurveyContext{" +
                "userId='" + userId + '\'' +
                ", surveyReportGUID='" + surveyReportGUID + '\'' +
                ", reportQualifiedName='" + reportQualifiedName + '\'' +
                ", reportDisplayName='" + reportDisplayName + '\'' +
                ", reportDescription='" + reportDescription + '\'' +
                ", creationDate=" + creationDate +
                ", analysisStep='" + analysisStep + '\'' +
                ", assetGUID='" + assetGUID + '\'' +
                ", requestParameters=" + requestParameters +
                ", assetStore=" + assetStore +
                ", annotationStore=" + annotationStore +
                ", openMetadataStore=" + openMetadataStore +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        SurveyContext that = (SurveyContext) objectToCompare;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(surveyReportGUID, that.surveyReportGUID) &&
                Objects.equals(reportQualifiedName, that.reportQualifiedName) &&
                Objects.equals(reportDisplayName, that.reportDisplayName) &&
                Objects.equals(reportDescription, that.reportDescription) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(analysisStep, that.analysisStep) &&
                Objects.equals(assetGUID, that.assetGUID) &&
                Objects.equals(requestParameters, that.requestParameters) &&
                Objects.equals(assetStore, that.assetStore) &&
                Objects.equals(annotationStore, that.annotationStore) &&
                Objects.equals(openMetadataStore, that.openMetadataStore);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(userId, surveyReportGUID, reportQualifiedName, reportDisplayName, reportDescription,
                creationDate, analysisStep, assetGUID, requestParameters, assetStore, annotationStore, openMetadataStore);
    }
}
