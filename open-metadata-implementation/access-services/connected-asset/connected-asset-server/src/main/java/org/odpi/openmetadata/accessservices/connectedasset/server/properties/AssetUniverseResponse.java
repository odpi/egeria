/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.server.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetUniverseResponse is the response structure used on the Connected Asset OMAS REST API calls that return an
 * AssetUniverse object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetUniverseResponse extends ConnectedAssetOMASAPIResponse
{
    private AssetUniverse assetUniverse = null;


    /**
     * Default constructor
     */
    public AssetUniverseResponse()
    {
    }


    /**
     * Return the AssetUniverse object.
     *
     * @return all details known about the asset
     */
    public AssetUniverse getAssetUniverse()
    {
        return assetUniverse;
    }


    /**
     * Set up the AssetUniverse object.
     *
     * @param assetUniverse - all details known about the asset
     */
    public void setAssetUniverse(AssetUniverse assetUniverse)
    {
        this.assetUniverse = assetUniverse;
    }


    @Override
    public String toString()
    {
        return "AssetUniverseResponse{" +
                "assetUniverse=" + assetUniverse +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
