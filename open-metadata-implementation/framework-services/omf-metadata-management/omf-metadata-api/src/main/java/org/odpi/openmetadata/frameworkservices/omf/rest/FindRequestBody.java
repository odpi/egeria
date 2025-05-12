/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
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
    private String                metadataElementTypeName     = null;
    private List<String>          metadataElementSubtypeNames = null;
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
            metadataElementTypeName     = template.getMetadataElementTypeName();
            metadataElementSubtypeNames = template.getMetadataElementSubtypeNames();
            searchProperties            = template.getSearchProperties();
            matchClassifications = template.getMatchClassifications();
        }
    }


    /**
     * Return the type of metadata element that the caller is searching for.
     *
     * @return open metadata type name
     */
    public String getMetadataElementTypeName()
    {
        return metadataElementTypeName;
    }


    /**
     * Set up the type of metadata element that the caller is searching for.
     *
     * @param metadataElementTypeName open metadata type name
     */
    public void setMetadataElementTypeName(String metadataElementTypeName)
    {
        this.metadataElementTypeName = metadataElementTypeName;
    }


    /**
     * Return the list of valid subtypes that the returned metadata elements must belong to.
     *
     * @return open metadata type names
     */
    public List<String> getMetadataElementSubtypeNames()
    {
        return metadataElementSubtypeNames;
    }


    /**
     * Set up the list of valid subtypes that the returned metadata elements must belong to.
     *
     * @param metadataElementSubtypeNames open metadata type names
     */
    public void setMetadataElementSubtypeNames(List<String> metadataElementSubtypeNames)
    {
        this.metadataElementSubtypeNames = metadataElementSubtypeNames;
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
                "metadataElementTypeName='" + metadataElementTypeName + '\'' +
                ", metadataElementSubtypeNames=" + metadataElementSubtypeNames +
                ", searchProperties=" + searchProperties +
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
        return Objects.equals(metadataElementTypeName, that.metadataElementTypeName) &&
                Objects.equals(metadataElementSubtypeNames, that.metadataElementSubtypeNames) &&
                Objects.equals(searchProperties, that.searchProperties) &&
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
        return Objects.hash(super.hashCode(), metadataElementTypeName,
                            metadataElementSubtypeNames, searchProperties, matchClassifications);
    }
}
