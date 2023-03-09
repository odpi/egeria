/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.AssetCollectionMember;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  AssetListResponse returns a list of assets from the server.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetListResponse extends CommunityProfileOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<AssetCollectionMember> assets              = null;
    private int                         startingFromElement = 0;
    private int                         totalListSize       = 0;


    /**
     * Default constructor
     */
    public AssetListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetListResponse(AssetListResponse  template)
    {
        super(template);

        if (template != null)
        {
            this.startingFromElement = template.getStartingFromElement();
            this.totalListSize = template.getTotalListSize();
            this.assets = template.getAssets();
        }
    }


    /**
     * Return the list of assets in the response.
     *
     * @return list of assets
     */
    public List<AssetCollectionMember> getAssets()
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
            return assets;
        }
    }


    /**
     * Set up the list of assets for the response.
     *
     * @param assets list of assets
     */
    public void setAssets(List<AssetCollectionMember> assets)
    {
        this.assets = assets;
    }


    /**
     * Return the starting element number from the server side list that this response contains.
     *
     * @return int
     */
    public int getStartingFromElement()
    {
        return startingFromElement;
    }


    /**
     * Set up the starting element number from the server side list that this response contains.
     *
     * @param startingFromElement int
     */
    public void setStartingFromElement(int startingFromElement)
    {
        this.startingFromElement = startingFromElement;
    }


    /**
     * Return the size of the list at the server side.
     *
     * @return int
     */
    public int getTotalListSize()
    {
        return totalListSize;
    }


    /**
     * Set up the size of the list at the server side.
     *
     * @param totalListSize int
     */
    public void setTotalListSize(int totalListSize)
    {
        this.totalListSize = totalListSize;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetListResponse{" +
                "assets=" + assets +
                ", startingFromElement=" + startingFromElement +
                ", totalListSize=" + totalListSize +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
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
        AssetListResponse that = (AssetListResponse) objectToCompare;
        return getStartingFromElement() == that.getStartingFromElement() &&
                getTotalListSize() == that.getTotalListSize() &&
                Objects.equals(getAssets(), that.getAssets());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAssets(), getStartingFromElement(), getTotalListSize());
    }
}
