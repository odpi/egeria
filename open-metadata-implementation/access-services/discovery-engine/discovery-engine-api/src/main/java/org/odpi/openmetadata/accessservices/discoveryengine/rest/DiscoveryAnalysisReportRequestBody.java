/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryAnalysisReportRequestBody provides a structure for passing the properties of
 * a discovery analysis report as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryAnalysisReportRequestBody extends ReferenceableRequestBody
{
    private static final long    serialVersionUID = 1L;

    protected String                 displayName            = null;
    protected String                 description            = null;
    protected Date                   creationDate           = null;
    protected Map<String, String>    analysisParameters     = null;
    protected String                 analysisStep           = null;
    protected DiscoveryRequestStatus discoveryRequestStatus = null;
    protected String                 discoveryEngineGUID    = null;
    protected String                 discoveryServiceGUID   = null;


    /**
     * Default constructor
     */
    public DiscoveryAnalysisReportRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryAnalysisReportRequestBody(DiscoveryAnalysisReportRequestBody template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            creationDate = template.getCreationDate();
            analysisParameters = template.getAnalysisParameters();
            analysisStep = template.getAnalysisStep();
            discoveryRequestStatus = template.getDiscoveryRequestStatus();
            discoveryEngineGUID = template.getDiscoveryEngineGUID();
            discoveryServiceGUID = template.getDiscoveryServiceGUID();
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
     * Return the current analysis step name.
     *
     * @return step name
     */
    public String getAnalysisStep()
    {
        return analysisStep;
    }


    /**
     * Set up the analysis step name.
     *
     * @param analysisStep new step name
     */
    public void setAnalysisStep(String analysisStep)
    {
        this.analysisStep = analysisStep;
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DiscoveryAnalysisReportRequestBody{" +
                       "displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", creationDate=" + creationDate +
                       ", analysisParameters=" + analysisParameters +
                       ", analysisStep='" + analysisStep + '\'' +
                       ", discoveryRequestStatus=" + discoveryRequestStatus +
                       ", discoveryEngineGUID='" + discoveryEngineGUID + '\'' +
                       ", discoveryServiceGUID='" + discoveryServiceGUID + '\'' +
                       ", typeName='" + typeName + '\'' +
                       ", classifications=" + classifications +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", meanings=" + meanings +
                       ", additionalProperties=" + additionalProperties +
                       ", extendedProperties=" + extendedProperties +
                       ", typeName='" + getTypeName() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", meanings=" + getMeanings() +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        DiscoveryAnalysisReportRequestBody that = (DiscoveryAnalysisReportRequestBody) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getCreationDate(), that.getCreationDate()) &&
                       Objects.equals(getAnalysisParameters(), that.getAnalysisParameters()) &&
                       Objects.equals(getAnalysisStep(), that.getAnalysisStep()) &&
                       Objects.equals(getDiscoveryRequestStatus(), that.getDiscoveryRequestStatus()) &&
                       Objects.equals(getDiscoveryEngineGUID(), that.getDiscoveryEngineGUID()) &&
                       Objects.equals(getDiscoveryServiceGUID(), that.getDiscoveryServiceGUID());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getCreationDate(),
                            getAnalysisParameters(), getAnalysisStep(), getDiscoveryRequestStatus(), getDiscoveryEngineGUID(),
                            getDiscoveryServiceGUID());
    }
}
