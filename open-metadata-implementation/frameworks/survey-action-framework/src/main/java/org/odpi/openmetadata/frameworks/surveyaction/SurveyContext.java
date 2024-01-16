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
        return Objects.hash(userId, assetGUID, requestParameters, assetStore, annotationStore, openMetadataStore);
    }
}
