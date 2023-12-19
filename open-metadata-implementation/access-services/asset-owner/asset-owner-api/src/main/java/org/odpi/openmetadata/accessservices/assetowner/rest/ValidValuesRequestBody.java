/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValuesRequestBody carries the parameters for created a new valid values element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValuesRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private String                      typeName             = null;
    private List<ElementClassification> classifications      = null;
    private String                      qualifiedName        = null;
    private Map<String, String>         additionalProperties = null;
    private Map<String, Object>         extendedProperties   = null;
    private String                      displayName          = null;
    private String                      description          = null;
    private String                      category             = null;
    private String                      usage                = null;
    private String                      scope                = null;
    private String                      preferredValue       = null;
    private boolean                     isDeprecated         = false;


    /**
     * Default constructor
     */
    public ValidValuesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValuesRequestBody(ValidValuesRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.typeName = template.getTypeName();
            this.classifications = template.getClassifications();
            this.qualifiedName = template.getQualifiedName();
            this.additionalProperties = template.getAdditionalProperties();
            this.extendedProperties = template.getExtendedProperties();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.category = template.getCategory();
            this.usage = template.getUsage();
            this.scope = template.getScope();
            this.preferredValue = template.getPreferredValue();
            this.isDeprecated = template.getIsDeprecated();
        }
    }


    /**
     * Return the open metadata type name of this object - this is used to create a subtype of
     * the referenceable.  Any properties associated with this subtype are passed as extended properties.
     *
     * @return string type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the open metadata type name of this object - this is used to create a subtype of
     * the referenceable.  Any properties associated with this subtype are passed as extended properties.
     *
     * @param typeName string type name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the classifications associated with this referenceable.
     *
     * @return list of classifications with their properties
     */
    public List<ElementClassification> getClassifications()
    {
        return classifications;
    }


    /**
     * Set up the list of classifications associated with this referenceable.
     *
     * @param classifications list of classifications with their properties
     */
    public void setClassifications(List<ElementClassification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the properties that are defined for a subtype of referenceable but are not explicitly
     * supported by the bean.
     *
     * @return map of properties
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that are defined for a subtype of referenceable but are not explicitly
     * supported by the bean.
     *
     * @param extendedProperties map of properties
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
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
        return "ValidValuesRequestBody{" +
                       "typeName='" + typeName + '\'' +
                       ", classifications=" + classifications +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", additionalProperties=" + additionalProperties +
                       ", extendedProperties=" + extendedProperties +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", category='" + category + '\'' +
                       ", usage='" + usage + '\'' +
                       ", scope='" + scope + '\'' +
                       ", preferredValue='" + preferredValue + '\'' +
                       ", isDeprecated=" + isDeprecated +
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
        ValidValuesRequestBody that = (ValidValuesRequestBody) objectToCompare;
        return  Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getClassifications(), that.getClassifications()) &&
                Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getExtendedProperties(), that.getExtendedProperties()) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(category, that.category) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(isDeprecated, that.isDeprecated) &&
                Objects.equals(preferredValue, that.preferredValue);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getTypeName(), getClassifications(), getQualifiedName(),
                            getAdditionalProperties(), getExtendedProperties(),
                            displayName, description, category, usage, scope, isDeprecated, preferredValue);
    }
}
