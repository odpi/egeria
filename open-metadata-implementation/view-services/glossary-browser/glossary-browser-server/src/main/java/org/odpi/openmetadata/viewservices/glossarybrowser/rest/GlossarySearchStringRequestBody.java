/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossarySearchStringRequestBody is the request body structure used on GlossaryCategory/Term REST API calls that passes a regular expression
 * to use as a search string and the scope of the results can be optionally restricted by glossary.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossarySearchStringRequestBody extends GlossaryResultsRequestBody
{
    private String searchString              = null;
    private String searchStringParameterName = null;
    private String typeName                  = null;
    private String glossaryGUID              = null;

    /**
     * Default constructor
     */
    public GlossarySearchStringRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossarySearchStringRequestBody(GlossarySearchStringRequestBody template)
    {
        super(template);

        if (template != null)
        {
            searchString              = template.getSearchString();
            searchStringParameterName = template.getSearchStringParameterName();
            typeName                  = template.getTypeName();
            glossaryGUID              = template.getGlossaryGUID();
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
     * Return the optional type name for the search results (null means any type).
     *
     * @return unique name of type
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the optional type name for the search results (null means any type).
     *
     * @param typeName unique name of type
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the unique identifier of the glossary scope.
     *
     * @return string guid
     */
    public String getGlossaryGUID()
    {
        return glossaryGUID;
    }


    /**
     * Set up the unique identifier of the glossary scope.
     *
     * @param glossaryGUID string
     */
    public void setGlossaryGUID(String glossaryGUID)
    {
        this.glossaryGUID = glossaryGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GlossarySearchStringRequestBody{" +
                "searchString='" + searchString + '\'' +
                ", searchStringParameterName='" + searchStringParameterName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", glossaryGUID='" + glossaryGUID + '\'' +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GlossarySearchStringRequestBody that = (GlossarySearchStringRequestBody) objectToCompare;
        return Objects.equals(glossaryGUID, that.glossaryGUID) &&
                Objects.equals(searchString, that.searchString) &&
                Objects.equals(searchStringParameterName, that.searchStringParameterName) &&
                Objects.equals(typeName, that.typeName);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), glossaryGUID, searchString, searchStringParameterName, typeName);
    }
}
