/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SurveyReport describes the properties for a survey report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SurveyReport extends PropertyBase
{
    private String              qualifiedName         = null;
    private Map<String, String> additionalProperties  = null;
    private String              displayName           = null;
    private String              description           = null;
    private String              purpose               = null;
    private String              user                  = null;
    private Map<String, String> analysisParameters    = null;
    private String              assetGUID             = null;
    private String              engineActionGUID      = null;

    private Date                startDate             = null;
    private String              analysisStep          = null;
    private Date                completionDate        = null;
    private String              completionMessage     = null;



    /**
     * Default constructor
     */
    public SurveyReport()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SurveyReport(SurveyReport template)
    {
        super(template);

        if (template != null)
        {
            qualifiedName          = template.getQualifiedName();
            additionalProperties   = template.getAdditionalProperties();
            displayName            = template.getDisplayName();
            description            = template.getDescription();
            purpose                = template.getPurpose();
            user                   = template.getUser();
            analysisParameters     = template.getAnalysisParameters();
            engineActionGUID       = template.getEngineActionGUID();
            assetGUID              = template.getAssetGUID();
            analysisStep           = template.getAnalysisStep();
            startDate              = template.getStartDate();
            completionDate         = template.getCompletionDate();
            completionMessage      = template.getCompletionMessage();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
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
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Return the display name of the survey report.
     *
     * @return String report name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name of the survey report.
     *
     * @param reportName  String report name
     */
    public void setDisplayName(String reportName)
    {
        this.displayName = reportName;
    }


    /**
     * Return the survey report description.
     *
     * @return String report description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the survey report description.
     *
     * @param reportDescription String report description
     */
    public void setDescription(String reportDescription)
    {
        this.description = reportDescription;
    }


    /**
     * Return the purpose of the survey.
     *
     * @return Date that the report was created.
     */
    public String getPurpose()
    {
        return purpose;
    }


    /**
     * Set up the creation date for the report.  If this date is not known then null is returned.
     *
     * @param purpose Date that the report was created.
     */
    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }

    /**
     * Return the user that ran the survey action service.
     *
     * @return user
     */
    public String getUser()
    {
        return user;
    }


    /**
     * Set up the user that ran the survey action service.
     *
     * @param user userId
     */
    public void setUser(String user)
    {
        this.user = user;
    }


    /**
     * Return the creation date for the report.  If this date is not known then null is returned.
     *
     * @return Date that the report was created.
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the creation date for the report.  If this date is not known then null is returned.
     *
     * @param date Date that the report was created.
     */
    public void setStartDate(Date date)
    {
        this.startDate = date;
    }



    /**
     * Return the completion date for the report.  If this date is not known then null is returned.
     *
     * @return Date that the report was completed.
     */
    public Date getCompletionDate()
    {
        return completionDate;
    }


    /**
     * Set up the completion date for the report.  If this date is not known then null is returned.
     *
     * @param date Date that the report was completed.
     */
    public void setCompletionDate(Date date)
    {
        this.completionDate = date;
    }



    /**
     * Return the parameters used to drive the discovery service's analysis.
     *
     * @return map storing the analysis parameters
     */
    public Map<String, String> getAnalysisParameters()
    {
        return analysisParameters;
    }


    /**
     * Set up the parameters used to drive the discovery service's analysis.
     *
     * @param analysisParameters map storing the analysis parameters
     */
    public void setAnalysisParameters(Map<String, String> analysisParameters)
    {
        this.analysisParameters = analysisParameters;
    }


    /**
     * Return the unique identifier of the asset that was analyzed.
     *
     * @return unique identifier (guid)
     */
    public String getAssetGUID()
    {
        return assetGUID;
    }


    /**
     * Set up the unique identifier of the asset that was analyzed.
     *
     * @param assetGUID unique identifier (guid)
     */
    public void setAssetGUID(String assetGUID)
    {
        this.assetGUID = assetGUID;
    }


    /**
     * Return the completion message - it may be null which means it completed ok.
     *
     * @return string text
     */
    public String getCompletionMessage()
    {
        return completionMessage;
    }


    /**
     * Set up the completion message - it may be null which means it completed ok.
     *
     * @param message text of completion message
     */
    public void setCompletionMessage(String message)
    {
        this.completionMessage = message;
    }


    /**
     * Return the unique identifier for the engine action associated with this report.
     *
     * @return guid
     */
    public String getEngineActionGUID()
    {
        return engineActionGUID;
    }


    /**
     * Set up the unique identifier for the engine action associated with this report.
     *
     * @param engineActionGUID guid
     */
    public void setEngineActionGUID(String engineActionGUID)
    {
        this.engineActionGUID = engineActionGUID;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SurveyReport{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", purpose='" + purpose + '\'' +
                ", user='" + user + '\'' +
                ", analysisParameters=" + analysisParameters +
                ", assetGUID='" + assetGUID + '\'' +
                ", engineActionGUID='" + engineActionGUID + '\'' +
                ", startDate=" + startDate +
                ", analysisStep='" + analysisStep + '\'' +
                ", completionDate=" + completionDate +
                ", completionMessage='" + completionMessage + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        SurveyReport that = (SurveyReport) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(purpose, that.purpose) &&
                Objects.equals(user, that.user) &&
                Objects.equals(analysisParameters, that.analysisParameters) &&
                Objects.equals(assetGUID, that.assetGUID) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(completionDate, that.completionDate) &&
                Objects.equals(completionMessage, that.completionMessage) &&
                Objects.equals(engineActionGUID, that.engineActionGUID) &&
                Objects.equals(analysisStep, that.analysisStep);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, additionalProperties, displayName, description, purpose, user, analysisParameters,
                            engineActionGUID, assetGUID, startDate, completionDate, completionMessage, analysisStep);
    }
}
