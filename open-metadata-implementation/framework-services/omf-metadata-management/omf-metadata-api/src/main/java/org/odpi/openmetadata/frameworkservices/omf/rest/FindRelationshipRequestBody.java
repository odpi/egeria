/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindRelationshipRequestBody provides a structure for passing the properties for the find relationships request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindRelationshipRequestBody extends ResultsRequestBody
{
    private String                relationshipTypeName        = null;
    private SearchProperties      searchProperties            = null;


    /**
     * Default constructor
     */
    public FindRelationshipRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindRelationshipRequestBody(FindRelationshipRequestBody template)
    {
        if (template != null)
        {
            relationshipTypeName = template.getRelationshipTypeName();
            searchProperties     = template.getSearchProperties();
        }
    }


    /**
     * Return the type of relationship that the caller is searching for.
     *
     * @return open metadata type name
     */
    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }


    /**
     * Set up the type of relationship that the caller is searching for.
     *
     * @param relationshipTypeName open metadata type name
     */
    public void setRelationshipTypeName(String relationshipTypeName)
    {
        this.relationshipTypeName = relationshipTypeName;
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "FindRelationshipRequestBody{" +
                "relationshipTypeName='" + relationshipTypeName + '\'' +
                ", searchProperties=" + searchProperties +
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
        FindRelationshipRequestBody that = (FindRelationshipRequestBody) objectToCompare;
        return Objects.equals(relationshipTypeName, that.relationshipTypeName) &&
                Objects.equals(searchProperties, that.searchProperties);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipTypeName, searchProperties);
    }
}
