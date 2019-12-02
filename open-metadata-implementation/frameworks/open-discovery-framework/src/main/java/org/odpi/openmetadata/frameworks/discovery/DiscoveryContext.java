/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.PropertyBase;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * DiscoveryContext provides the exchange area for annotations discovered from analysing
 * a specific asset.
 */
public class DiscoveryContext extends PropertyBase
{
    protected String                     userId;
    protected String                     assetGUID;
    protected String                     discoveryReportGUID;

    protected Map<String, String>        analysisParameters;
    protected List<String>               requestedAnnotationTypes;

    protected DiscoveryAssetStore        assetStore;
    protected DiscoveryAnnotationStore   annotationStore;
    protected DiscoveryAssetCatalogStore assetCatalogStore;

    protected Date                       creationDate        = new Date();

    protected String                     reportQualifiedName = null;
    protected String                     reportDisplayName   = null;
    protected String                     reportDescription   = null;


    /**
     * Constructor sets up the key parameters for accessing the annotations store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param discoveryReportGUID unique identifier of the discovery request that is used to identifier the
     *                            discovery report.
     * @param analysisParameters name-value properties to control the discovery service
     * @param requestedAnnotationTypes annotation types to create
     * @param assetStore discovery asset store for the discovery service
     * @param annotationStore annotation store for the discovery service
     * @param assetCatalogStore the asset catalog store that provides the ability to query assets.
     */
    public    DiscoveryContext(String                     userId,
                               String                     assetGUID,
                               String                     discoveryReportGUID,
                               Map<String, String>        analysisParameters,
                               List<String>               requestedAnnotationTypes,
                               DiscoveryAssetStore        assetStore,
                               DiscoveryAnnotationStore   annotationStore,
                               DiscoveryAssetCatalogStore assetCatalogStore)
    {
        super();

        this.userId = userId;
        this.assetGUID = assetGUID;
        this.discoveryReportGUID = discoveryReportGUID;
        this.analysisParameters = analysisParameters;
        this.requestedAnnotationTypes = requestedAnnotationTypes;
        this.assetStore = assetStore;
        this.annotationStore = annotationStore;
        this.assetCatalogStore = assetCatalogStore;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object being copied
     */
    public DiscoveryContext(DiscoveryContext template)
    {
        super(template);

        if (template != null)
        {
            userId = template.userId;
            assetGUID = template.getAssetGUID();
            discoveryReportGUID = template.getDiscoveryReportGUID();
            analysisParameters = template.getAnalysisParameters();
            requestedAnnotationTypes = template.getRequestedAnnotationTypes();
            assetStore = template.getAssetStore();
            annotationStore = template.getAnnotationStore();
            assetCatalogStore = template.getAssetCatalogStore();

            creationDate = template.getCreationDate();

            reportQualifiedName = template.getReportQualifiedName();
            reportDisplayName = template.getReportDisplayName();
            reportDescription = template.getReportDescription();
        }
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
     * Return the report identifier for this discovery context.  Any new annotations added to tis discovery context
     * will be linked to this report.
     *
     * @return unique identifier (guid) of the new discovery report.
     */
    public String getDiscoveryReportGUID()
    {
        return discoveryReportGUID;
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
     * Return the list of annotation types required by the requester (null means all types available)
     *
     * @return list of type names
     */
    public List<String> getRequestedAnnotationTypes()
    {
        return requestedAnnotationTypes;
    }


    /**
     * Return the asset catalog store that provides the ability to query assets.
     *
     * @return assetCatalogStore object
     */
    public DiscoveryAssetCatalogStore getAssetCatalogStore()
    {
        return assetCatalogStore;
    }


    /**
     * Return the asset store for the discovery engine.  This is able to provide a connector to the asset
     * configured with the properties of the asset from a property server.
     *
     * @return asset store
     */
    public DiscoveryAssetStore getAssetStore()
    {
        return assetStore;
    }


    /**
     * Return the annotation store for the discovery engine.  This is where the annotations are stored and
     * retrieved from.
     *
     * @return annotation store
     */
    public DiscoveryAnnotationStore getAnnotationStore()
    {
        return annotationStore;
    }


    /**
     * Return the creation date for the discovery analysis report that will result from this discovery request.
     *
     * @return Date that the report was created.
     */
    public Date getCreationDate() {
        return creationDate;
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
     * The discovery engine will set up a default fully-qualified name.  This method enables it to be over-ridden.
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
     * The default name is null.
     *
     * @param reportName  String report name
     */
    public void setReportDisplayName(String reportName)
    {
        this.reportDisplayName = reportName;
    }


    /**
     * Return the description for the discovery analysis report that will result from this discovery request.
     * The default value is null.
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DiscoveryContext{" +
                "userId='" + userId + '\'' +
                ", assetGUID='" + assetGUID + '\'' +
                ", discoveryReportGUID='" + discoveryReportGUID + '\'' +
                ", analysisParameters=" + analysisParameters +
                ", requestedAnnotationTypes=" + requestedAnnotationTypes +
                ", assetStore=" + assetStore +
                ", annotationStore=" + annotationStore +
                ", creationDate=" + creationDate +
                ", reportQualifiedName='" + reportQualifiedName + '\'' +
                ", reportDisplayName='" + reportDisplayName + '\'' +
                ", reportDescription='" + reportDescription + '\'' +
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
        return Objects.equals(userId, that.userId) &&
                Objects.equals(getAssetGUID(), that.getAssetGUID()) &&
                Objects.equals(getDiscoveryReportGUID(), that.getDiscoveryReportGUID()) &&
                Objects.equals(getAnalysisParameters(), that.getAnalysisParameters()) &&
                Objects.equals(getRequestedAnnotationTypes(), that.getRequestedAnnotationTypes()) &&
                Objects.equals(getAssetStore(), that.getAssetStore()) &&
                Objects.equals(getAnnotationStore(), that.getAnnotationStore()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getReportQualifiedName(), that.getReportQualifiedName()) &&
                Objects.equals(getReportDisplayName(), that.getReportDisplayName()) &&
                Objects.equals(getReportDescription(), that.getReportDescription());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(userId, getAssetGUID(), getDiscoveryReportGUID(), getAnalysisParameters(),
                            getRequestedAnnotationTypes(), getAssetStore(), getAnnotationStore(), getCreationDate(),
                            getReportQualifiedName(), getReportDisplayName(), getReportDescription());
    }
}
