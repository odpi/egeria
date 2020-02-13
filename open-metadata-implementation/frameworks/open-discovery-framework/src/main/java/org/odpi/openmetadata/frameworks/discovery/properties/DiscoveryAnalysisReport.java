/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryAnalysisReport describes the properties for a discovery analysis report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryAnalysisReport extends Referenceable
{
    private static final long    serialVersionUID = 1L;

    protected String                 displayName            = null;
    protected String                 description            = null;
    protected Date                   creationDate           = null;
    protected Map<String, String>    analysisParameters     = null;
    protected DiscoveryRequestStatus discoveryRequestStatus = null;
    protected String                 assetGUID              = null;
    protected String                 discoveryEngineGUID    = null;
    protected String                 discoveryServiceGUID   = null;
    protected String                 analysisStep           = null;


    /**
     * Default constructor
     */
    public DiscoveryAnalysisReport()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryAnalysisReport(DiscoveryAnalysisReport template)
    {
        super(template);

        if (template == null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            creationDate = template.getCreationDate();
            analysisParameters = template.getAnalysisParameters();
            discoveryRequestStatus = template.getDiscoveryRequestStatus();
            assetGUID = template.getAssetGUID();
            discoveryEngineGUID = template.getDiscoveryEngineGUID();
            discoveryServiceGUID = template.getDiscoveryServiceGUID();
            analysisStep = template.getAnalysisStep();
        }
    }


    /**
     * Return the display name of the discovery analysis report.
     *
     * @return String report name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name of the discovery analysis report.
     *
     * @param reportName  String report name
     */
    public void setDisplayName(String reportName)
    {
        this.displayName = reportName;
    }


    /**
     * Return the discovery analysis report description.
     *
     * @return String report description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the discovery analysis report description.
     *
     * @param reportDescription String report description
     */
    public void setDescription(String reportDescription)
    {
        this.description = reportDescription;
    }


    /**
     * Return the creation date for the report.  If this date is not known then null is returned.
     *
     * @return Date that the report was created.
     */
    public Date getCreationDate()
    {
        return creationDate;
    }


    /**
     * Set up the creation date for the report.  If this date is not known then null is returned.
     *
     * @param creationDate Date that the report was created.
     */
    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
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
     * Return the status of the discovery process.
     *
     * @return status enum
     */
    public DiscoveryRequestStatus getDiscoveryRequestStatus()
    {
        return discoveryRequestStatus;
    }


    /**
     * Set up the status of the discovery process.
     *
     * @param discoveryRequestStatus status enum
     */
    public void setDiscoveryRequestStatus(DiscoveryRequestStatus discoveryRequestStatus)
    {
        this.discoveryRequestStatus = discoveryRequestStatus;
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
     * Return the unique identifier of the discovery engine that ran the discovery service.
     *
     * @return unique identifier (guid)
     */
    public String getDiscoveryEngineGUID()
    {
        return discoveryEngineGUID;
    }


    /**
     * Set up the unique identifier of the discovery engine that ran the discovery service.
     *
     * @param discoveryEngineGUID unique identifier (guid)
     */
    public void setDiscoveryEngineGUID(String discoveryEngineGUID)
    {
        this.discoveryEngineGUID = discoveryEngineGUID;
    }


    /**
     * Return the unique identifier of the discovery service.
     *
     * @return unique identifier (guid)
     */
    public String getDiscoveryServiceGUID()
    {
        return discoveryServiceGUID;
    }


    /**
     * Set up the unique identifier of the discovery service.
     *
     * @param discoveryServiceGUID unique identifier (guid)
     */
    public void setDiscoveryServiceGUID(String discoveryServiceGUID)
    {
        this.discoveryServiceGUID = discoveryServiceGUID;
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
        return "DiscoveryAnalysisReport{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", analysisParameters=" + analysisParameters +
                ", discoveryRequestStatus=" + discoveryRequestStatus +
                ", assetGUID='" + assetGUID + '\'' +
                ", discoveryEngineGUID='" + discoveryEngineGUID + '\'' +
                ", discoveryServiceGUID='" + discoveryServiceGUID + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                ", meanings=" + meanings +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DiscoveryAnalysisReport that = (DiscoveryAnalysisReport) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getAnalysisParameters(), that.getAnalysisParameters()) &&
                Objects.equals(getDiscoveryRequestStatus(), that.getDiscoveryRequestStatus()) &&
                Objects.equals(getAssetGUID(), that.getAssetGUID()) &&
                Objects.equals(getDiscoveryEngineGUID(), that.getDiscoveryEngineGUID()) &&
                Objects.equals(getDiscoveryServiceGUID(), that.getDiscoveryServiceGUID()) &&
                Objects.equals(getAnalysisStep(), that.getAnalysisStep());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getCreationDate(),
                            getAnalysisParameters(), getDiscoveryRequestStatus(), getAssetGUID(), getDiscoveryEngineGUID(),
                            getDiscoveryServiceGUID());
    }
}
