/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SearchStringRequestBody is the request body structure used on OMAG REST API calls that passes a regular expression
 * to use as a search string.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchStringRequestBody extends AssetManagerIdentifiersRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String searchString = null;
    private String searchStringParameterName = null;


    /**
     * Default constructor
     */
    public SearchStringRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SearchStringRequestBody(SearchStringRequestBody template)
    {
        super(template);

        if (template != null)
        {
            searchString = template.getSearchString();
        }
    }


    /**
     * Return the search request.
     *
     * @return regEx expression
     */
    public String getSearchString()
    {
        return searchString;
    }


    /**
     * Set up the search.
     *
     * @param searchString regEx expression
     */
    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }


    /**
     * Return the search string parameter name.
     *
     * @return string name
     */
    public String getSearchStringParameterName()
    {
        return searchStringParameterName;
    }


    /**
     * Set up the search string parameter name.
     *
     * @param searchStringParameterName string name
     */
    public void setSearchStringParameterName(String searchStringParameterName)
    {
        this.searchStringParameterName = searchStringParameterName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SearchStringRequestBody{" +
                       "searchString='" + searchString + '\'' +
                       ", searchStringParameterName='" + searchStringParameterName + '\'' +
                       ", assetManagerGUID='" + getAssetManagerGUID() + '\'' +
                       ", assetManagerName='" + getAssetManagerName() + '\'' +
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
        SearchStringRequestBody that = (SearchStringRequestBody) objectToCompare;
        return Objects.equals(getSearchString(), that.getSearchString()) &&
                       Objects.equals(getSearchStringParameterName(), that.getSearchStringParameterName());
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getSearchString(), getSearchStringParameterName());
    }
}
