/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

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
 * DiscoveryAnalysisReport describes the properties for a discovery analysis report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryAnalysisReport extends PropertyBase
{
    private static final long    serialVersionUID = 1L;

    private String                 qualifiedName          = null;
    private Map<String, String>    additionalProperties   = null;
    private String                 displayName            = null;
    private String                 description            = null;
    private Date                   creationDate           = null;
    private Map<String, String>    analysisParameters     = null;
    private DiscoveryRequestStatus discoveryRequestStatus = null;
    private String                 assetGUID              = null;
    private String                 discoveryEngineGUID    = null;
    private String                 discoveryServiceGUID   = null;
    private String                 analysisStep           = null;


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

        if (template != null)
        {
            qualifiedName          = template.getQualifiedName();
            additionalProperties   = template.getAdditionalProperties();
            displayName            = template.getDisplayName();
            description            = template.getDescription();
            creationDate           = template.getCreationDate();
            analysisParameters     = template.getAnalysisParameters();
            discoveryRequestStatus = template.getDiscoveryRequestStatus();
            assetGUID              = template.getAssetGUID();
            discoveryEngineGUID    = template.getDiscoveryEngineGUID();
            discoveryServiceGUID   = template.getDiscoveryServiceGUID();
            analysisStep           = template.getAnalysisStep();
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
                "qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", analysisParameters=" + analysisParameters +
                ", discoveryRequestStatus=" + discoveryRequestStatus +
                ", assetGUID='" + assetGUID + '\'' +
                ", discoveryEngineGUID='" + discoveryEngineGUID + '\'' +
                ", discoveryServiceGUID='" + discoveryServiceGUID + '\'' +
                ", analysisStep='" + analysisStep + '\'' +
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
        DiscoveryAnalysisReport that = (DiscoveryAnalysisReport) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(analysisParameters, that.analysisParameters) &&
                discoveryRequestStatus == that.discoveryRequestStatus &&
                Objects.equals(assetGUID, that.assetGUID) &&
                Objects.equals(discoveryEngineGUID, that.discoveryEngineGUID) &&
                Objects.equals(discoveryServiceGUID, that.discoveryServiceGUID) &&
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
        return Objects.hash(qualifiedName, additionalProperties, displayName, description, creationDate, analysisParameters, discoveryRequestStatus
                , assetGUID, discoveryEngineGUID, discoveryServiceGUID, analysisStep);
    }
}
