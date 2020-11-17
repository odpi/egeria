/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaProperties is a bean containing the common attributes for schema types and schema attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SchemaAttributeProperties.class, name = "SchemaAttributeProperties"),
                @JsonSubTypes.Type(value = SchemaTypeProperties.class,      name = "SchemaTypeProperties"),
        })
public class SchemaProperties extends SupplementaryProperties
{
    private static final long     serialVersionUID = 1L;

    private boolean isDeprecated         = false;
    private String  technicalName        = null;
    private String  technicalDescription = null;

    /**
     * Default constructor
     */
    public SchemaProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public SchemaProperties(SchemaProperties template)
    {
        super(template);

        if (template != null)
        {
            isDeprecated = template.getIsDeprecated();
            technicalName = template.getTechnicalName();
            technicalDescription = template.getTechnicalDescription();
        }
    }


    /**
     * Is the schema element deprecated?
     *
     * @return boolean flag
     */
    public boolean getIsDeprecated()
    {
        return isDeprecated;
    }


    /**
     * Set whether the schema element deprecated or not.  Default is false.
     *
     * @param deprecated boolean flag
     */
    public void setIsDeprecated(boolean deprecated)
    {
        isDeprecated = deprecated;
    }


    /**
     * Return the simple name of the schema element.
     *
     * @return string name
     */
    public String getTechnicalName() { return technicalName; }


    /**
     * Set up the simple name of the schema element.
     *
     * @param name String display name
     */
    public void setTechnicalName(String   name)
    {
        this.technicalName = name;
    }


    /**
     * Returns the stored technicalDescription property for the schema element.
     *
     * @return string technicalDescription
     */
    public String getTechnicalDescription()
    {
        return technicalDescription;
    }


    /**
     * Set up the stored technicalDescription property for the schema element.
     *
     * @param technicalDescription string technicalDescription
     */
    public void setTechnicalDescription(String technicalDescription)
    {
        this.technicalDescription = technicalDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaProperties{" +
                       "isDeprecated=" + isDeprecated +
                       ", technicalName='" + technicalName + '\'' +
                       ", technicalDescription='" + technicalDescription + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaProperties that = (SchemaProperties) objectToCompare;
        return isDeprecated == that.isDeprecated &&
                Objects.equals(technicalName, that.technicalName) &&
                Objects.equals(technicalDescription, that.technicalDescription);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isDeprecated, technicalName, technicalDescription);
    }
}
