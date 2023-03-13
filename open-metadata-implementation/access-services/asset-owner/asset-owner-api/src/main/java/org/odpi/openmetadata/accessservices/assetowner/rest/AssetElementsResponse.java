/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.AssetElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  AssetElementsResponse returns a list of assets from the server.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetElementsResponse extends PagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<AssetElement> assets = null;


    /**
     * Default constructor
     */
    public AssetElementsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetElementsResponse(AssetElementsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.assets = template.getAssets();
        }
    }


    /**
     * Return the list of assets in the response.
     *
     * @return list of assets
     */
    public List<AssetElement> getAssets()
    {
        if (assets == null)
        {
            return null;
        }
        else if (assets.isEmpty())
        {
            return null;
        }
        else
        {
            List<AssetElement>  clonedList = new ArrayList<>();

            for (AssetElement  existingElement : assets)
            {
                clonedList.add(new AssetElement(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of assets for the response.
     *
     * @param assets list
     */
    public void setAssets(List<AssetElement> assets)
    {
        this.assets = assets;
    }
    

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetElementsResponse{" +
                "assets=" + assets +
                ", startingFromElement=" + getStartingFromElement() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetElementsResponse that = (AssetElementsResponse) objectToCompare;
        return Objects.equals(getAssets(), that.getAssets());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAssets());
    }
}
