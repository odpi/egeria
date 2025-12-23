/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalResourceOriginProperties provides a structure for passing information about a digital resource's origin.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DigitalResourceOriginProperties extends ClassificationBeanProperties
{
    private String              organization                   = null;
    private String              organizationPropertyName       = null;
    private String              businessCapability             = null;
    private String              businessCapabilityPropertyName = null;
    private Map<String, String> otherOriginValues              = null;


    /**
     * Default constructor
     */
    public DigitalResourceOriginProperties()
    {
        super();
        super.typeName = OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DigitalResourceOriginProperties(DigitalResourceOriginProperties template)
    {
        super(template);

        if (template != null)
        {
            this.organization                   = template.getOrganization();
            this.organizationPropertyName       = template.getOrganizationPropertyName();
            this.businessCapability             = template.getBusinessCapability();
            this.businessCapabilityPropertyName = template.getBusinessCapabilityPropertyName();
            this.otherOriginValues              = template.getOtherOriginValues();
        }
    }


    /**
     * Return the unique identifier (GUID) of the organization where this digital resource originated from.
     *
     * @return unique identifier (GUID)
     */
    public String getOrganization()
    {
        return organization;
    }


    /**
     * Set up the identifier of the organization where this digital resource originated from.
     *
     * @param organization  identifier
     */
    public void setOrganization(String organization)
    {
        this.organization = organization;
    }


    /**
     * Return the property name used to supply the organization identifier.
     *
     * @return name
     */
    public String getOrganizationPropertyName()
    {
        return organizationPropertyName;
    }


    /**
     * Set up the property name used to supply the organization identifier.
     *
     * @param organizationPropertyName name
     */
    public void setOrganizationPropertyName(String organizationPropertyName)
    {
        this.organizationPropertyName = organizationPropertyName;
    }



    /**
     * Return the identifier of the business capability where this digital resource originated from.
     *
     * @return identifier
     */
    public String getBusinessCapability()
    {
        return businessCapability;
    }


    /**
     * Set up the unique identifier (GUID) of the business capability where this digital resource originated from.
     *
     * @param businessCapability unique identifier (GUID)
     */
    public void setBusinessCapability(String businessCapability)
    {
        this.businessCapability = businessCapability;
    }


    /**
     * Return the property name used to supply the business capability identifier.
     *
     * @return name
     */
    public String getBusinessCapabilityPropertyName()
    {
        return businessCapabilityPropertyName;
    }


    /**
     * Set up the property name used to supply the business capability identifier.
     *
     * @param businessCapabilityPropertyName name
     */
    public void setBusinessCapabilityPropertyName(String businessCapabilityPropertyName)
    {
        this.businessCapabilityPropertyName = businessCapabilityPropertyName;
    }

    /**
     * Return any other descriptive labels describing origin of the digital resource.
     *
     * @return map of property values
     */
    public Map<String, String> getOtherOriginValues()
    {
        return otherOriginValues;
    }


    /**
     * Set up any descriptive labels describing origin of the digital resource.
     *
     * @param otherOriginValues map of property values
     */
    public void setOtherOriginValues(Map<String, String> otherOriginValues)
    {
        this.otherOriginValues = otherOriginValues;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DigitalResourceOriginProperties{" +
                "organization='" + organization + '\'' +
                ", organizationPropertyName='" + organizationPropertyName + '\'' +
                ", businessCapability='" + businessCapability + '\'' +
                ", businessCapabilityPropertyName='" + businessCapabilityPropertyName + '\'' +
                ", otherOriginValues=" + otherOriginValues +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DigitalResourceOriginProperties that = (DigitalResourceOriginProperties) objectToCompare;
        return Objects.equals(organization, that.organization) &&
                Objects.equals(organizationPropertyName, that.organizationPropertyName) &&
                Objects.equals(businessCapability, that.businessCapability) &&
                Objects.equals(businessCapabilityPropertyName, that.businessCapabilityPropertyName) &&
                Objects.equals(otherOriginValues, that.otherOriginValues);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), organization, organizationPropertyName, businessCapability, businessCapabilityPropertyName, otherOriginValues);
    }
}
