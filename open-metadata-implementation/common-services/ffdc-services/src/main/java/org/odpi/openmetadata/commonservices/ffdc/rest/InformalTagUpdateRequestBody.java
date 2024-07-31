/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InformalTagProperties stores information about a tag connected to an asset.
 * InformalTags provide informal classifications to assets
 * and can be added at any time.
 * <br><br>
 * InformalTags have the userId of the person who added the tag, the name of the tag and its description.
 * <br><br>
 * The content of the tag is a personal judgement (which is why the user's id is in the tag)
 * and there is no formal review of the tags.  However, they can be used as a basis for crowd-sourcing
 * Glossary terms.
 * <br><br>
 * Private InformalTags are only returned to the user that created them.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformalTagUpdateRequestBody
{
    private String  description  = null;


    /**
     * Default constructor
     */
    public InformalTagUpdateRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public InformalTagUpdateRequestBody(InformalTagUpdateRequestBody template)
    {
        if (template != null)
        {
            description = template.getDescription();
        }
    }


    /**
     * Return the tag description null means no description is available.
     *
     * @return String tag description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the tag description null means no description is available.
     *
     * @param tagDescription  tag description
     */
    public void setDescription(String tagDescription) {
        this.description = tagDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InformalTagUpdateRequestBody{" +
                       "description='" + description + '\'' +
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
        if (! (objectToCompare instanceof InformalTagUpdateRequestBody that))
        {
            return false;
        }

        return description != null ? description.equals(that.description) : that.description == null;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return description != null ? description.hashCode() : 0;
    }
}