/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ValidValueImplementation contains the properties for a relationship between an asset and a valid value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ValidValueImplementationAsset.class, name = "ValidValueImplementationAsset"),
                @JsonSubTypes.Type(value = ValidValueImplementationDefinition.class, name = "ValidValueImplementationDefinition")
        })
public class ValidValueImplementation extends PropertyBase
{
    private String              symbolicName            = null;
    private String              implementationValue     = null;
    private Map<String, String> additionalValues        = null;


    /**
     * Default constructor
     */
    public ValidValueImplementation()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValueImplementation(ValidValueImplementation template)
    {
        super(template);

        if (template != null)
        {
            symbolicName            = template.getSymbolicName();
            implementationValue     = template.getImplementationValue();
            additionalValues        = template.getAdditionalValues();
        }
    }


    /**
     * Returns the symbolic name for the valid value that is used to look up the implementation value.
     *
     * @return String name
     */
    public String getSymbolicName()
    {
        return symbolicName;
    }


    /**
     * Set up the symbolic name for the valid value that is used to look up the implementation value.
     *
     * @param symbolicName String name
     */
    public void setSymbolicName(String symbolicName)
    {
        this.symbolicName = symbolicName;
    }


    /**
     * Returns the implementation value for the valid value used in a particular system.
     *
     * @return String value
     */
    public String getImplementationValue()
    {
        return implementationValue;
    }


    /**
     * Set up the implementation value for the valid value used in a particular system.
     *
     * @param implementationValue String value
     */
    public void setImplementationValue(String implementationValue)
    {
        this.implementationValue = implementationValue;
    }


    /**
     * Return the additional values associated with the symbolic name.
     *
     * @return name-value pairs for additional values
     */
    public Map<String, String> getAdditionalValues()
    {
        return additionalValues;
    }


    /**
     * Set up the additional values associated with the symbolic name.
     *
     * @param additionalValues name-value pairs for additional values
     */
    public void setAdditionalValues(Map<String, String> additionalValues)
    {
        this.additionalValues = additionalValues;
    }




    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueImplementation{" +
                ", symbolicName='" + getSymbolicName() + '\'' +
                ", implementationValue='" + getImplementationValue() + '\'' +
                ", additionalValues=" + getAdditionalValues() +
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
        ValidValueImplementation that = (ValidValueImplementation) objectToCompare;
        return Objects.equals(symbolicName, that.symbolicName) &&
                Objects.equals(implementationValue, that.implementationValue) &&
                Objects.equals(additionalValues, that.additionalValues);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(symbolicName, implementationValue, additionalValues);
    }
}
