/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindRequestBody provides a structure for passing the properties for the find request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindRequestBody extends ResultsRequestBody
{
    private SearchProperties      searchProperties            = null;
    private SearchClassifications matchClassifications        = null;


    /**
     * Default constructor
     */
    public FindRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindRequestBody(FindRequestBody template)
    {
        super(template);

        if (template != null)
        {
            searchProperties            = template.getSearchProperties();
            matchClassifications        = template.getMatchClassifications();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindRequestBody(QueryOptions template)
    {
        super(template);
    }


    /**
     * Return the details of the property values that must be true for the returned metadata elements.
     *
     * @return property specification
     */
    public SearchProperties getSearchProperties()
    {
        return searchProperties;
    }


    /**
     * Set up the details of the property values that must be true for the returned metadata elements.
     *
     * @param searchProperties property specification
     */
    public void setSearchProperties(SearchProperties searchProperties)
    {
        this.searchProperties = searchProperties;
    }



    /**
     * Return details about the classifications that need to be present/absent from the search request.
     *
     * @return classification specification
     */
    public SearchClassifications getMatchClassifications()
    {
        return matchClassifications;
    }


    /**
     * Set up details about the classifications that need to be present/absent from the search request.
     *
     * @param matchClassifications classification specification
     */
    public void setMatchClassifications(SearchClassifications matchClassifications)
    {
        this.matchClassifications = matchClassifications;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "FindRequestBody{" +
                "searchProperties=" + searchProperties +
                ", matchClassifications=" + matchClassifications +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FindRequestBody that = (FindRequestBody) objectToCompare;
        return Objects.equals(searchProperties, that.searchProperties) &&
                Objects.equals(matchClassifications, that.matchClassifications);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), searchProperties, matchClassifications);
    }
}
