/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Tag provides the basic information about an informal tag.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Tag extends AssetConsumerElementHeader
{
    private String  guid = null;
    private String  name = null;
    private String  description = null;

    /**
     * Default constructor
     */
    public Tag()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public Tag(Tag   template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGUID();
            this.name = template.getName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the unique identifier for this tag.
     *
     * @return string guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this tag.
     *
     * @param guid string guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name of the tag.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the tag.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the tag's meaning.
     *
     * @return description string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the tag's meaning.
     *
     * @param description description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "Tag{" +
                "guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
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
        Tag tag = (Tag) objectToCompare;
        return Objects.equals(guid, tag.guid) &&
                Objects.equals(getName(), tag.getName()) &&
                Objects.equals(getDescription(), tag.getDescription());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, getName(), getDescription());
    }
}
