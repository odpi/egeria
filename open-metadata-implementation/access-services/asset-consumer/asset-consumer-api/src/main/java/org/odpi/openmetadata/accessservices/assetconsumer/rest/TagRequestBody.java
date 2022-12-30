/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InformalTagProperties stores information about a tag connected to an asset.
 * InformalTags provide informal classifications to assets
 * and can be added at any time.
 *
 * InformalTags have the userId of the person who added the tag, the name of the tag and its description.
 *
 * The content of the tag is a personal judgement (which is why the user's id is in the tag)
 * and there is no formal review of the tags.  However, they can be used as a basis for crowd-sourcing
 * Glossary terms.
 *
 * Private InformalTags are only returned to the user that created them.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TagRequestBody implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private boolean isPrivateTag = false;
    private String  name         = null;
    private String  description  = null;


    /**
     * Default constructor
     */
    public TagRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public TagRequestBody(TagRequestBody template)
    {
        if (template != null)
        {
            isPrivateTag = template.getIsPrivateTag();
            name = template.getName();
            description = template.getDescription();
        }
    }


    /**
     * Return boolean flag to say whether the tag is private or not.  A private tag is only seen by the
     * person who set it up.  Public tags are visible to everyone.
     *
     * @return boolean is private flag
     */
    public boolean getIsPrivateTag() {
        return isPrivateTag;
    }


    /**
     * Set up boolean flag to say whether the tag is private or not.  A private tag is only seen by the
     * person who set it up.  Public tags are visible to everyone.
     *
     * @param privateTag indicator of a private tag
     */
    public void setIsPrivateTag(boolean privateTag)
    {
        isPrivateTag = privateTag;
    }


    /**
     * Return the name of the tag.  It is not valid to have a tag with no name.  However, there is a point where
     * the tag object is created and the tag name not yet set, so null is a possible response.
     *
     * @return String tag name
     */
    public String getName() {
        return name;
    }


    /**
     * Set up the name of the tag.  It is not valid to have a tag with no name.  However, there is a point where
     * the tag object is created and the tag name not yet set, so null is a possible response.
     *
     * @param name String tag name
     */
    public void setName(String name)
    {
        this.name = name;
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
        return "TagRequestBody{" +
                       "isPrivateTag=" + isPrivateTag +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
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
        if (! (objectToCompare instanceof TagRequestBody))
        {
            return false;
        }

        TagRequestBody that = (TagRequestBody) objectToCompare;

        if (isPrivateTag != that.isPrivateTag)
        {
            return false;
        }
        if (name != null ? ! name.equals(that.name) : that.name != null)
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
        int result = (isPrivateTag ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}