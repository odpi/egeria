/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Tag records a descriptive definition added to an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Tag extends UserFeedbackHeader
{
    private static final long    serialVersionUID = 1L;

    private String  name = null;
    private String  description = null;
    private boolean isPrivate = false;


    /**
     * Default constructor
     */
    public Tag()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Tag(Tag template)
    {
        super(template);

        if (template != null)
        {
            name = template.getName();
            description = template.getDescription();
            isPrivate = template.getIsPrivate();
        }
    }


    /**
     * Return the name of the tag.  this is a descriptive name that describes the element that the tag is attached to.
     *
     * @return tag name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the tag.  this is a descriptive name that describes the element that the tag is attached to.
     *
     * @param name tag name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the tag.  This is optional.
     *
     * @return tag description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the tag.  This is optional.
     *
     * @param description tag description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return whether the tag is private or not.  Private tags are only returned to the user that created them.
     *
     * @return private tag indicator
     */
    public boolean getIsPrivate()
    {
        return isPrivate;
    }


    /**
     * Set whether the tag is private or not.  Private tags are only returned to the user that created them.
     *
     * @param privateTag private tag indicator
     */
    public void setIsPrivate(boolean privateTag)
    {
        isPrivate = privateTag;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "Tag{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isPublic=" + isPrivate +
                ", userId='" + getUserId() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Tag tag = (Tag) objectToCompare;
        return getIsPrivate() == tag.getIsPrivate() &&
                Objects.equals(getName(), tag.getName()) &&
                Objects.equals(getDescription(), tag.getDescription());
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getIsPrivate());
    }
}
