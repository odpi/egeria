/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataEnumElementDef describes a single valid value defined for an enum.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataEnumElementDef extends OpenMetadataTypeDefElementHeader
{
    private int    ordinal         = 99;
    private String value           = null;
    private String description     = null;
    private String descriptionGUID = null;


    /**
     * Default constructor sets up an empty OpenMetadataEnumElementDef
     */
    public OpenMetadataEnumElementDef()
    {
        super();
    }


    /**
     * Copy/clone constructor sets up an OpenMetadataEnumElementDef based on the values supplied in the template.
     *
     * @param template OpenMetadataEnumElementDef
     */
    public OpenMetadataEnumElementDef(OpenMetadataEnumElementDef template)
    {
        super(template);

        if (template != null)
        {
            ordinal = template.getOrdinal();
            value = template.getValue();
            description = template.getDescription();
            descriptionGUID = template.getDescriptionGUID();
        }
    }


    /**
     * Return the numeric value used for the enum value.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Set up the numeric value for the enum value.
     *
     * @param ordinal int
     */
    public void setOrdinal(int ordinal) { this.ordinal = ordinal; }


    /**
     * Return the symbolic name for the enum value.
     *
     * @return String name
     */
    public String getValue() { return value; }


    /**
     * Set up the symbolic name for the enum value.
     *
     * @param value String name
     */
    public void setValue(String value) { this.value = value; }


    /**
     * Return the description for the enum value.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * Set up the description for the enum value.
     *
     * @param description String
     */
    public void setDescription(String description) { this.description = description; }


    /**
     * Return the unique identifier (guid) of the glossary term that describes this OpenMetadataEnumElementDef.
     *
     * @return String guid
     */
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Set up the unique identifier (guid) of the glossary term that describes this OpenMetadataEnumElementDef.
     *
     * @param descriptionGUID String guid
     */
    public void setDescriptionGUID(String descriptionGUID)
    {
        this.descriptionGUID = descriptionGUID;
    }

    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataEnumElementDef{" +
                "value='" + value + '\'' +
                ", ordinal=" + ordinal +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OpenMetadataEnumElementDef that = (OpenMetadataEnumElementDef) objectToCompare;
        return ordinal == that.ordinal &&
                       Objects.equals(value, that.value) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(descriptionGUID, that.descriptionGUID);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(ordinal, value, description, descriptionGUID);
    }
}
