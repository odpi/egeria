/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetConnectionRequestBody carries the parameters for creating a new relationship between an asset and a connection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetConnectionRequestBody extends ExternalSourceRequestBody
{
    private String assetSummary = null;


    /**
     * Default constructor
     */
    public AssetConnectionRequestBody()
    {
    }


    /**
     * Copy/clone constructor
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
     * Return the full path of the file - this should be unique.
     *
     * @return string name
     */
    public String getAssetSummary()
    {
        return assetSummary;
    }


    /**
     * Set up the full path of the file - this should be unique.
     *
     * @param assetSummary string name
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
        return "PathNameRequestBody{" +
                ", assetSummary='" + assetSummary +
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
        return Objects.hash(getAssetSummary());
    }
}
