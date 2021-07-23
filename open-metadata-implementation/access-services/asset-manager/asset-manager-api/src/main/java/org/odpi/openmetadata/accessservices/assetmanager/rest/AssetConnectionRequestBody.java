/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetConnectionRequestBody describes the request body used when creating a AssetConnection relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetConnectionRequestBody extends AssetManagerIdentifiersRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String assetSummary = null;


    /**
     * Default constructor
     */
    public AssetConnectionRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AssetConnectionRequestBody(AssetConnectionRequestBody template)
    {
        super(template);

        if (template != null)
        {
            assetSummary = template.getAssetSummary();
        }
    }


    /**
     * Return the properties for the relationship.
     *
     * @return properties object
     */
    public String getAssetSummary()
    {
        return assetSummary;
    }


    /**
     * Set up the properties for the relationship.
     *
     * @param assetSummary properties object
     */
    public void setAssetSummary(String assetSummary)
    {
        this.assetSummary = assetSummary;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetConnectionRequestBody{" +
                       "properties=" + assetSummary +
                       ", assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetConnectionRequestBody that = (AssetConnectionRequestBody) objectToCompare;
        return Objects.equals(getAssetSummary(), that.getAssetSummary());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assetSummary);
    }
}
