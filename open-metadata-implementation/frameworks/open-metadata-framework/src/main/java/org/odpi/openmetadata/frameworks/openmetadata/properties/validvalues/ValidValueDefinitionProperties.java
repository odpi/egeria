/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueDefinitionProperties provides the common super class for ValidValueSet and ValidValueDefinition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ReferenceDataValueProperties.class, name = "ReferenceDataValueProperties"),
                @JsonSubTypes.Type(value = SpecificationPropertyValueProperties.class, name = "SpecificationPropertyValueProperties"),
                @JsonSubTypes.Type(value = ValidMetadataValueProperties.class, name = "ValidMetadataValue"),
        })
public class ValidValueDefinitionProperties extends ReferenceableProperties
{
    private String  namespace         = null;
    private String  usage             = null;
    private String  scope             = null;
    private String  preferredValue    = null;
    private String  dataType          = null;
    private String  userDefinedStatus = null;
    private boolean isCaseSensitive   = false;


    /**
     * Constructor
     */
    public ValidValueDefinitionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.VALID_VALUE_DEFINITION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueDefinitionProperties(ValidValueDefinitionProperties template)
    {
        super(template);

        if (template != null)
        {
            namespace         = template.getNamespace();
            usage             = template.getUsage();
            scope             = template.getScope();
            preferredValue    = template.getPreferredValue();
            dataType          = template.getDataType();
            userDefinedStatus = template.getUserDefinedStatus();
            isCaseSensitive   = template.getIsCaseSensitive();
        }
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespace string name
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
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
     * Is the valid value deprecated?
     *
     * @return string status
     */
    public String getUserDefinedStatus()
    {
        return userDefinedStatus;
    }


    /**
     * Set whether the valid value is userDefinedStatus or not.  Default is false.
     *
     * @param userDefinedStatus string
     */
    public void setUserDefinedStatus(String userDefinedStatus)
    {
        this.userDefinedStatus = userDefinedStatus;
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
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "ValidValueDefinitionProperties{" +
                "usage='" + usage + '\'' +
                ", scope='" + scope + '\'' +
                ", preferredValue='" + preferredValue + '\'' +
                ", dataType='" + dataType + '\'' +
                ", userDefinedStatus='" + userDefinedStatus + '\'' +
                ", isCaseSensitive=" + isCaseSensitive +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ValidValueDefinitionProperties that = (ValidValueDefinitionProperties) objectToCompare;
        return Objects.equals(usage, that.usage) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(userDefinedStatus, that.userDefinedStatus) &&
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
        return Objects.hash(super.hashCode(), usage, scope, preferredValue, dataType, userDefinedStatus, isCaseSensitive);
    }
}
