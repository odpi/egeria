/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.BusinessCapabilityType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * BusinessCapabilityProperties represents one of an organization's business capabilities.  These can be linked to the teams and digital services
 * that support this capability.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BusinessCapabilityProperties extends ReferenceableProperties
{
    private String                 identifier                 = null;
    private BusinessCapabilityType businessCapabilityType     = null;
    private String                 businessImplementationType = null;

    /**
     * Default constructor
     */
    public BusinessCapabilityProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.BUSINESS_CAPABILITY.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public BusinessCapabilityProperties(BusinessCapabilityProperties template)
    {
        super(template);

        if (template != null)
        {
            this.identifier = template.getIdentifier();
            this.businessCapabilityType = template.getBusinessCapabilityType();
            this.businessImplementationType = template.getBusinessImplementationType();
        }
    }


    /**
     * Return the identifier assigned by the organization to this capability.
     *
     * @return String
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the identifier assigned by the organization to this capability.
     *
     * @param identifier String
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * Return the type of business capability.
     *
     * @return enum
     */
    public BusinessCapabilityType getBusinessCapabilityType()
    {
        return businessCapabilityType;
    }


    /**
     * Set up the type of business capability
     *
     * @param businessCapabilityType enum
     */
    public void setBusinessCapabilityType(BusinessCapabilityType businessCapabilityType)
    {
        this.businessCapabilityType = businessCapabilityType;
    }


    /**
     * Return the type of implementation used for this business capability.
     *
     * @return string
     */
    public String getBusinessImplementationType()
    {
        return businessImplementationType;
    }


    /**
     * Set up the type of implementation used for this business capability.
     *
     * @param businessImplementationType string
     */
    public void setBusinessImplementationType(String businessImplementationType)
    {
        this.businessImplementationType = businessImplementationType;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BusinessCapabilityProperties{" +
                "identifier='" + identifier + '\'' +
                ", businessCapabilityType=" + businessCapabilityType +
                ", businessImplementationType='" + businessImplementationType + '\'' +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof BusinessCapabilityProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(businessCapabilityType, that.businessCapabilityType) &&
                Objects.equals(businessImplementationType, that.businessImplementationType) &&
                Objects.equals(identifier, that.identifier);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), identifier, businessCapabilityType, businessImplementationType);
    }
}
