/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * DiscoveryContext provides the discovery service with access to information about
 * the discovery request along with the open metadata repository interfaces.
 */
public class DiscoveryContext
{
    private String                     userId;
    private String                     assetGUID;

    private Map<String, String>        analysisParameters;
    private List<String>               requestedAnnotationTypes;

    private DiscoveryAssetStore        assetStore;
    private DiscoveryAnnotationStore   annotationStore;
    private DiscoveryAssetCatalogStore assetCatalogStore;


    /**
     * Constructor sets up the key parameters for accessing the annotations store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param analysisParameters name-value properties to control the discovery service
     * @param requestedAnnotationTypes annotation types to create
     * @param assetStore discovery asset store for the discovery service
     * @param annotationStore annotation store for the discovery service
     * @param assetCatalogStore the asset catalog store that provides the ability to query assets.
     */
    public    DiscoveryContext(String                     userId,
                               String                     assetGUID,
                               Map<String, String>        analysisParameters,
                               List<String>               requestedAnnotationTypes,
                               DiscoveryAssetStore        assetStore,
                               DiscoveryAnnotationStore   annotationStore,
                               DiscoveryAssetCatalogStore assetCatalogStore)
    {
        this.userId = userId;
        this.assetGUID = assetGUID;
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
        if (template != null)
        {
            userId = template.userId;
            assetGUID = template.getAssetGUID();
            analysisParameters = template.getAnalysisParameters();
            requestedAnnotationTypes = template.getRequestedAnnotationTypes();
            assetStore = template.getAssetStore();
            annotationStore = template.getAnnotationStore();
            assetCatalogStore = template.getAssetCatalogStore();
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
                ", analysisParameters=" + analysisParameters +
                ", requestedAnnotationTypes=" + requestedAnnotationTypes +
                ", assetStore=" + assetStore +
                ", annotationStore=" + annotationStore +
                ", assetCatalogStore=" + assetCatalogStore +
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
                Objects.equals(getAnalysisParameters(), that.getAnalysisParameters()) &&
                Objects.equals(getRequestedAnnotationTypes(), that.getRequestedAnnotationTypes());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(userId, getAssetGUID(), getAnalysisParameters(), getRequestedAnnotationTypes());
    }
}
