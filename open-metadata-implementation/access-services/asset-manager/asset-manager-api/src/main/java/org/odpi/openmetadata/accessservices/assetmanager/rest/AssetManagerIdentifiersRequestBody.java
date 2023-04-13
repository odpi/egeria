/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetManagerIdentifiersRequestBody provides the identifiers for an asset manager.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetManagerIdentifiersRequestBody implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String assetManagerGUID = null;
    private String assetManagerName = null;


    /**
     * Default constructor
     */
    public AssetManagerIdentifiersRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public AssetManagerIdentifiersRequestBody(AssetManagerIdentifiersRequestBody template)
    {
        if (template != null)
        {
            assetManagerGUID = template.getAssetManagerGUID();
            assetManagerName = template.getAssetManagerName();
        }
    }


    /**
     * Return the unique identifier of the software server capability that represents the asset manager.
     *
     * @return string guid
     */
    public String getAssetManagerGUID()
    {
        return assetManagerGUID;
    }


    /**
     * Set up the unique identifier of the software server capability that represents the asset manager.
     *
     * @param assetManagerGUID string guid
     */
    public void setAssetManagerGUID(String assetManagerGUID)
    {
        this.assetManagerGUID = assetManagerGUID;
    }


    /**
     * Return the qualified name of the software server capability that represents the asset manager.
     *
     * @return string name
     */
    public String getAssetManagerName()
    {
        return assetManagerName;
    }


    /**
     * Set up the qualified name of the software server capability that represents the asset manager.
     *
     * @param assetManagerName string name
     */
    public void setAssetManagerName(String assetManagerName)
    {
        this.assetManagerName = assetManagerName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetManagerIdentifiersRequestBody{" +
                       "assetManagerGUID='" + assetManagerGUID + '\'' +
                       ", assetManagerName='" + assetManagerName + '\'' +
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
        AssetManagerIdentifiersRequestBody that = (AssetManagerIdentifiersRequestBody) objectToCompare;
        return Objects.equals(getAssetManagerGUID(), that.getAssetManagerGUID()) &&
                       Objects.equals(getAssetManagerName(), that.getAssetManagerName());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAssetManagerGUID(), getAssetManagerName());
    }
}
