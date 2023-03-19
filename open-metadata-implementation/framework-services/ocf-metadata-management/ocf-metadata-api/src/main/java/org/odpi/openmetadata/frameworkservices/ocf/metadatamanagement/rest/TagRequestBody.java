/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TagRequestBody provides a structure for passing an informal tag as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TagRequestBody extends FeedbackRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String tagName = null;
    private String tagDescription = null;


    /**
     * Default constructor
     */
    public TagRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TagRequestBody(TagRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.tagName = template.getTagName();
            this.tagDescription = template.getTagDescription();
        }
    }


    /**
     * Return the name of the tag.
     *
     * @return string
     */
    public String getTagName()
    {
        return tagName;
    }


    /**
     * Set up the name of the tag.
     *
     * @param tagName string
     */
    public void setTagName(java.lang.String tagName)
    {
        this.tagName = tagName;
    }


    /**
     * Return the description that accompanies the tag.
     *
     * @return string description
     */
    public String getTagDescription()
    {
        return tagDescription;
    }


    /**
     * Set up the description that accompanies the tag.
     *
     * @param tagDescription String   (optional) description of the tag.  Setting a description, particularly in
     *                           a public tag makes the tag more valuable to other users and can act as an embryonic
     *                          glossary term.
     */
    public void setTagDescription(java.lang.String tagDescription)
    {
        this.tagDescription = tagDescription;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "TagRequestBody{" +
                "tagName='" + tagName + '\'' +
                ", tagDescription='" + tagDescription + '\'' +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        TagRequestBody that = (TagRequestBody) objectToCompare;
        return Objects.equals(getTagName(), that.getTagName()) &&
                Objects.equals(getTagDescription(), that.getTagDescription());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTagName(), getTagDescription());
    }
}
