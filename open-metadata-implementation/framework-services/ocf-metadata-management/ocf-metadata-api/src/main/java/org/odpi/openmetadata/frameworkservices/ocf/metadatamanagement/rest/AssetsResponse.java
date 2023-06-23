/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  AssetsResponse returns a list of assets from the server.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetsResponse extends PagedResponse
{
    private List<Asset> assets   = null;


    /**
     * Default constructor
     */
    public AssetsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetsResponse(AssetsResponse template)
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
    public List<Asset> getAssets()
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
            List<Asset>  clonedList = new ArrayList<>();

            for (Asset  existingElement : assets)
            {
                clonedList.add(new Asset(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of assets for the response.
     *
     * @param assets list
     */
    public void setAssets(List<Asset> assets)
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
        return "AssetsResponse{" +
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
        AssetsResponse that = (AssetsResponse) objectToCompare;
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
