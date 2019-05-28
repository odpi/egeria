/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PropertyBase;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryContext provides the exchange area for annotations discovered from analysing
 * a specific asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryContext extends PropertyBase
{
    private String              discoveryRequestGUID = null;
    private String              reportQualifiedName  = null;
    private String              reportDisplayName    = null;
    private String              reportDescription    = null;
    private Date                creationDate         = null;
    private Map<String, String> analysisParameters   = null;
    private Connection          assetConnection      = null;
    private List<Annotation>    existingAnnotations  = null;
    private List<Annotation>    newAnnotations       = null;


    /**
     * Default constructor
     */
    public DiscoveryContext()
    {
        super();
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public DiscoveryContext(DiscoveryContext template)
    {
        super(template);

        if (template != null)
        {
            discoveryRequestGUID = template.getDiscoveryRequestGUID();
            reportQualifiedName = template.getReportQualifiedName();
            reportDisplayName = template.getReportDisplayName();
            reportDescription = template.getReportDescription();
            creationDate = template.getCreationDate();
            analysisParameters = template.getAnalysisParameters();
            assetConnection = template.getAssetConnection();
            existingAnnotations = template.getExistingAnnotations();
            newAnnotations = template.getNewAnnotations();
        }
    }


    /**
     * Return the report identifier for this discovery context.  Any new annotations added to tis discovery context
     * will be linked to this report.
     *
     * @return unique identifier (guid) of the new discovery report.
     */
    public String getDiscoveryRequestGUID()
    {
        return discoveryRequestGUID;
    }


    /**
     * Set up the report identifier for this discovery context.  Any new annotations added to tis discovery context
     * will be linked to this report.
     *
     * @param discoveryRequestGUID unique identifier (guid) of the new discovery report.
     */
    public void setDiscoveryRequestGUID(String discoveryRequestGUID)
    {
        this.discoveryRequestGUID = discoveryRequestGUID;
    }


    /**
     * Return the unique name of the discovery analysis report that will result from this discovery request.
     *
     * @return String report name
     */
    public String getReportQualifiedName()
    {
        return reportQualifiedName;
    }


    /**
     * Set up the unique name of the discovery analysis report that will result from this discovery request.
     *
     * @param reportName  String report name
     */
    public void setReportQualifiedName(String reportName)
    {
        this.reportQualifiedName = reportName;
    }


    /**
     * Return the display name of the discovery analysis report that will result from this discovery request.
     *
     * @return String report name
     */
    public String getReportDisplayName()
    {
        return reportDisplayName;
    }


    /**
     * Set up the display name of the discovery analysis report that will result from this discovery request.
     *
     * @param reportName  String report name
     */
    public void setReportDisplayName(String reportName)
    {
        this.reportDisplayName = reportName;
    }


    /**
     * Return the description for the discovery analysis report that will result from this discovery request.
     *
     * @return String report description
     */
    public String getReportDescription()
    {
        return reportDescription;
    }


    /**
     * Set up the description for the discovery analysis report that will result from this discovery request.
     *
     * @param reportDescription String report description
     */
    public void setReportDescription(String reportDescription)
    {
        this.reportDescription = reportDescription;
    }


    /**
     * Return the creation date for the discovery analysis report that will result from this discovery request.
     *
     * @return Date that the annotation was created.
     */
    public Date getCreationDate() {
        return creationDate;
    }


    /**
     * Set up the creation date for discovery analysis report that will result from this discovery request.
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
    public Map<String, String> getAnalysisParameters()
    {
        return analysisParameters;
    }


    /**
     * Set up the properties that hold the parameters used to drive the discovery service's analysis.
     *
     * @param analysisParameters AdditionalProperties object storing the analysis parameters
     */
    public void setAnalysisParameters(Map<String, String> analysisParameters)
    {
        this.analysisParameters = analysisParameters;
    }


    /**
     * Return the connection for the connector to the asset.
     *
     * @return Connection object
     */
    public Connection getAssetConnection()
    {
        return assetConnection;
    }


    /**
     * Set up the connection for the connector to the asset.
     *
     * @param assetConnection Connection object
     */
    public void setAssetConnection(Connection assetConnection)
    {
        this.assetConnection = assetConnection;
    }


    /**
     * Return the list of current annotations associated with the asset.
     *
     * @return list of Annotation objects
     */
    public List<Annotation> getExistingAnnotations()
    {
        return existingAnnotations;
    }


    /**
     * Set up the list of current annotations associated with the asset.
     *
     * @param existingAnnotations list of Annotation objects
     */
    public void setExistingAnnotations(List<Annotation> existingAnnotations)
    {
        this.existingAnnotations = existingAnnotations;
    }


    /**
     * Return the list of annotation object created (so far) by this discovery service.
     *
     * @return list of Annotation objects
     */
    public synchronized List<Annotation> getNewAnnotations()
    {
        return newAnnotations;
    }


    /**
     * Set up the list of annotation object created (so far) by this discovery service.
     *
     * @param newAnnotations list of Annotation objects
     */
    public synchronized void setNewAnnotations(List<Annotation> newAnnotations)
    {
        this.newAnnotations = newAnnotations;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DiscoveryContext{" +
                "discoveryRequestGUID='" + discoveryRequestGUID + '\'' +
                ", reportQualifiedName='" + reportQualifiedName + '\'' +
                ", reportDisplayName='" + reportDisplayName + '\'' +
                ", reportDescription='" + reportDescription + '\'' +
                ", creationDate=" + creationDate +
                ", analysisParameters=" + analysisParameters +
                ", assetConnection=" + assetConnection +
                ", existingAnnotations=" + existingAnnotations +
                ", newAnnotations=" + newAnnotations +
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
        DiscoveryContext that = (DiscoveryContext) objectToCompare;
        return Objects.equals(getDiscoveryRequestGUID(), that.getDiscoveryRequestGUID()) &&
                Objects.equals(getReportQualifiedName(), that.getReportQualifiedName()) &&
                Objects.equals(getReportDisplayName(), that.getReportDisplayName()) &&
                Objects.equals(getReportDescription(), that.getReportDescription()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getAnalysisParameters(), that.getAnalysisParameters()) &&
                Objects.equals(getAssetConnection(), that.getAssetConnection()) &&
                Objects.equals(getExistingAnnotations(), that.getExistingAnnotations()) &&
                Objects.equals(getNewAnnotations(), that.getNewAnnotations());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getDiscoveryRequestGUID(), getReportQualifiedName(), getReportDisplayName(),
                            getReportDescription(), getCreationDate(), getAnalysisParameters(), getAssetConnection(),
                            getExistingAnnotations(), getNewAnnotations());
    }
}
