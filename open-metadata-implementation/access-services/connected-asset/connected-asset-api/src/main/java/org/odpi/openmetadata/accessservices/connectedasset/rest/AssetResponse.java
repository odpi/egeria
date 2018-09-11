/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetResponse is the response structure used on the OMAS REST API calls that return a
 * asset bean object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetResponse extends ConnectedAssetOMASAPIResponse
{
    private Asset asset = null;

    /**
     * Default constructor
     */
    public AssetResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetResponse(AssetResponse template)
    {
        super(template);

        if (template != null)
        {
            this.asset = template.getAsset();
        }
    }


    /**
     * Return the asset result.
     *
     * @return unique identifier
     */
    public Asset getAsset()
    {
        return asset;
    }


    /**
     * Set up the asset result.
     *
     * @param asset  unique identifier
     */
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetResponse{" +
                "Asset='" + getAsset() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof AssetResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetResponse that = (AssetResponse) objectToCompare;
        return Objects.equals(asset, that.asset);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(asset);
    }
}
