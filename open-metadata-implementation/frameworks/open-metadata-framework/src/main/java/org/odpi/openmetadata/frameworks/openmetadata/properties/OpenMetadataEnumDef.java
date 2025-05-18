/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The OpenMetadataEnumDef describes an open metadata enumeration.  This enumeration consists of a list of valid values
 * (stored in OpenMetadataEnumElementDef objects) and a default value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataEnumDef extends OpenMetadataAttributeTypeDef
{
    private List<OpenMetadataEnumElementDef> elementDefs  = null;
    private OpenMetadataEnumElementDef       defaultValue = null;


    /**
     * Default constructor sets up an empty OpenMetadataEnumDef.
     */
    public OpenMetadataEnumDef()
    {
        super(OpenMetadataAttributeTypeDefCategory.ENUM_DEF);
    }


    /**
     * Copy/clone constructor sets the OpenMetadataEnumDef based on the values from the supplied template.
     *
     * @param template OpenMetadataEnumDef
     */
    public OpenMetadataEnumDef(OpenMetadataEnumDef template)
    {
        super(template);

        if (template != null)
        {
            if (template.getElementDefs() != null)
            {
                elementDefs = new ArrayList<>(template.getElementDefs());
            }

            defaultValue = template.getDefaultValue();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataAttributeTypeDef
     */
    public OpenMetadataAttributeTypeDef cloneFromSubclass()
    {
        return new OpenMetadataEnumDef(this);
    }


    /**
     * Return the list of defined Enum values for this OpenMetadataEnumDef.
     *
     * @return EnumElementDefs list
     */
    public List<OpenMetadataEnumElementDef> getElementDefs()
    {
        return elementDefs;
    }


    /**
     * Set up the list of defined Enum values for this OpenMetadataEnumDef.
     *
     * @param elementDefs EnumElementDefs list
     */
    public void setElementDefs(List<OpenMetadataEnumElementDef> elementDefs) {this.elementDefs = new ArrayList<>(elementDefs); }


    /**
     * Return the default value for the OpenMetadataEnumDef.
     *
     * @return OpenMetadataEnumElementDef representing the default value
     */
    public OpenMetadataEnumElementDef getDefaultValue() { return defaultValue; }


    /**
     * Set up the default value for the OpenMetadataEnumDef.
     *
     * @param defaultValue OpenMetadataEnumElementDef representing the default value
     */
    public void setDefaultValue(OpenMetadataEnumElementDef defaultValue) {this.defaultValue = defaultValue; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataEnumDef{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", version=" + getVersion() +
                ", versionName='" + getVersionName() + '\'' +
                ", category=" + getCategory() +
                ", GUID='" + getGUID() + '\'' +
                ", descriptionGUID='" + getDescriptionGUID() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataEnumDef enumDef = (OpenMetadataEnumDef) objectToCompare;
        return Objects.equals(elementDefs, enumDef.elementDefs) &&
                       Objects.equals(defaultValue, enumDef.defaultValue);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementDefs, defaultValue);
    }
}
