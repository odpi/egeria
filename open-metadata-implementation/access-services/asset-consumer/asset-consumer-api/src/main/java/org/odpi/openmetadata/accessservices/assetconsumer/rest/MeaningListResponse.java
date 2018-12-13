/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;

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
public class MeaningListResponse extends AssetConsumerOMASAPIResponse
{
    private List<GlossaryTerm> meanings            = null;
    private int                startingFromElement = 0;
    private int                totalListSize       = 0;


    /**
     * Default constructor
     */
    public MeaningListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MeaningListResponse(MeaningListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.startingFromElement = template.getStartingFromElement();
            this.totalListSize = template.getTotalListSize();
            this.meanings = template.getMeanings();
        }
    }


    /**
     * Return the list of glossary terms in the response.
     *
     * @return list of glossary terms
     */
    public List<GlossaryTerm> getMeanings()
    {
        if (meanings == null)
        {
            return null;
        }
        else if (meanings.isEmpty())
        {
            return null;
        }
        else
        {
            return meanings;
        }
    }


    /**
     * Set up the list of assets for the response.
     *
     * @param meanings list of assets
     */
    public void setMeanings(List<GlossaryTerm> meanings)
    {
        this.meanings = meanings;
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
        return "MeaningListResponse{" +
                "meanings=" + meanings +
                ", startingFromElement=" + startingFromElement +
                ", totalListSize=" + totalListSize +
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
        MeaningListResponse that = (MeaningListResponse) objectToCompare;
        return getStartingFromElement() == that.getStartingFromElement() &&
                getTotalListSize() == that.getTotalListSize() &&
                Objects.equals(getMeanings(), that.getMeanings());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMeanings(), getStartingFromElement(), getTotalListSize());
    }
}
