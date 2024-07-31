/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetSearchMatches;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  AssetSearchMatchesListResponse returns the list of results from an asset domain search.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetSearchMatchesListResponse extends FFDCResponseBase
{
    private List<AssetSearchMatches> searchMatches = null;


    /**
     * Default constructor
     */
    public AssetSearchMatchesListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetSearchMatchesListResponse(AssetSearchMatchesListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.searchMatches = template.getSearchMatches();
        }
    }


    /**
     * Return the list of matches in the response.
     *
     * @return list of glossary terms
     */
    public List<AssetSearchMatches> getSearchMatches()
    {
        return searchMatches;
    }


    /**
     * Set up the list of matches for the response.
     *
     * @param searchMatches list
     */
    public void setSearchMatches(List<AssetSearchMatches> searchMatches)
    {
        this.searchMatches = searchMatches;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetSearchMatchesListResponse{" +
                "searchMatches=" + searchMatches +
                "} " + super.toString();
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
        AssetSearchMatchesListResponse that = (AssetSearchMatchesListResponse) objectToCompare;
        return Objects.equals(getSearchMatches(), that.getSearchMatches());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSearchMatches());
    }
}
