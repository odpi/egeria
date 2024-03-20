/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueSetProperties provides the class for ValidValueSet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueSetProperties extends ReferenceableProperties
{
    private String  displayName    = null;
    private String  description    = null;
    private String  category       = null;
    private String  usage          = null;
    private String  scope          = null;
    private String  preferredValue = null;
    private String  dataType       = null;
    private boolean isDeprecated   = false;
    private boolean isCaseSensitive = false;


    /**
     * Constructor
     */
    public ValidValueSetProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueSetProperties(ValidValueSetProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
            category = template.getCategory();
            usage = template.getUsage();
            scope = template.getScope();
            preferredValue = template.getPreferredValue();
            dataType = template.getDataType();
            isDeprecated = template.getIsDeprecated();
            isCaseSensitive = template.getIsCaseSensitive();
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
     * Return the category of reference data for this valid value.
     *
     * @return String name
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * Set up the category of reference data for this valid value.
     *
     * @param category String name
     */
    public void setCategory(String category)
    {
        this.category = category;
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
     * Returns the data type of the preferred value.
     *
     * @return string
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Set up the data type of the preferred value.
     *
     * @param dataType string
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return whether this valid value is case-sensitive, or will match irrespective of case.
     *
     * @return boolean flag
     */
    public boolean getIsCaseSensitive()
    {
        return isCaseSensitive;
    }


    /**
     * Set up whether this valid value is case-sensitive, or will match irrespective of case.
     *
     * @param caseSensitive boolean flag
     */
    public void setIsCaseSensitive(boolean caseSensitive)
    {
        isCaseSensitive = caseSensitive;
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
        return "ValidValueSetProperties{" +
                       "displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", category='" + category + '\'' +
                       ", usage='" + usage + '\'' +
                       ", scope='" + scope + '\'' +
                       ", preferredValue='" + preferredValue + '\'' +
                       ", dataType='" + dataType + '\'' +
                       ", isDeprecated=" + isDeprecated +
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
        ValidValueSetProperties that = (ValidValueSetProperties) objectToCompare;
        return  Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(category, that.category) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(isDeprecated, that.isDeprecated) &&
                Objects.equals(isCaseSensitive, that.isCaseSensitive) &&
                Objects.equals(preferredValue, that.preferredValue) &&
                Objects.equals(dataType, that.dataType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, category, usage, scope, preferredValue, dataType, isDeprecated, isCaseSensitive);
    }
}
