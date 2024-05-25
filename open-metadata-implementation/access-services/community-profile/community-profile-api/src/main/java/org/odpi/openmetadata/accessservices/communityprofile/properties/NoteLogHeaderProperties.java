/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteLogHeaderProperties covers the properties that are common between a community forum and a persona note log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteLogHeaderProperties extends ReferenceableProperties
{
    private String  name        = null;
    private String  description = null;
    private boolean isPublic    = false;


    /**
     * Default constructor
     */
    public NoteLogHeaderProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteLogHeaderProperties(NoteLogHeaderProperties template)
    {
        super(template);

        if (template != null)
        {
            this.name = template.getName();
            this.description = template.getDescription();
            isPublic = template.getIsPublic();
        }
    }


    /**
     * Return the name of the note log.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the note log.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the note log.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the note log.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return if the link to the note log is private to the creating user.
     *
     * @return boolean
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up whether the link to the note log is private to the creating user or not.
     *
     * @param aPublic boolean
     */
    public void setIsPublic(boolean aPublic)
    {
        isPublic = aPublic;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NoteLogHeaderProperties{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", isPublic=" + isPublic +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        NoteLogHeaderProperties that = (NoteLogHeaderProperties) objectToCompare;
        return isPublic == that.isPublic &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getIsPublic());
    }
}
