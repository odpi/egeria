/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValue provides the common super class for ValidValueSet and ValidValueDefinition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ValidValueDefinition.class, name = "ValidValueDefinition"),
                @JsonSubTypes.Type(value = ValidValueSet.class, name = "ValidValueSet")
        })
public class ValidValue extends Referenceable
{
    private static final long     serialVersionUID = 1L;

    protected String  displayName    = null;
    protected String  description    = null;
    protected String  usage          = null;
    protected String  scope          = null;
    protected String  preferredValue = null;
    protected boolean isDeprecated   = false;


    /**
     * Constructor
     */
    public ValidValue()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValue(ValidValue template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            usage = template.getUsage();
            scope = template.getScope();
            preferredValue = template.getPreferredValue();
            isDeprecated = template.getIsDeprecated();
        }
    }


    /**
     * Returns the stored display name property for the valid value.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the valid value.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the valid value.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property associated with the valid value.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description of how this valid value should be used.
     *
     * @return String text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the description of how this valid value should be used.
     *
     * @param usage String text
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the scope of values that this valid value covers (normally used with sets)
     *
     * @return String text
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope of values that this valid value covers (normally used with sets)
     *
     * @param scope String text
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the preferred values to use in implementations (normally used with definitions)
     *
     * @return String value
     */
    public String getPreferredValue()
    {
        return preferredValue;
    }


    /**
     * Set up the preferred values to use in implementations (normally used with definitions)
     *
     * @param preferredValue String value
     */
    public void setPreferredValue(String preferredValue)
    {
        this.preferredValue = preferredValue;
    }


    /**
     * Is the valid value deprecated?
     *
     * @return boolean flag
     */
    public boolean getIsDeprecated()
    {
        return isDeprecated;
    }


    /**
     * Set whether the valid value is deprecated or not.  Default is false.
     *
     * @param deprecated boolean flag
     */
    public void setIsDeprecated(boolean deprecated)
    {
        isDeprecated = deprecated;
    }

    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "ValidValue{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", usage='" + usage + '\'' +
                ", scope='" + scope + '\'' +
                ", preferredValue='" + preferredValue + '\'' +
                ", isDeprecated='" + isDeprecated + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", meanings=" + meanings +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                ", extendedProperties=" + extendedProperties + '}';
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
        ValidValue that = (ValidValue) objectToCompare;
        return  Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(isDeprecated, that.isDeprecated) &&
                Objects.equals(preferredValue, that.preferredValue);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, usage, scope, preferredValue, isDeprecated);
    }
}
