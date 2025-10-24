/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValuesImplementationProperties is a java bean used to associate a reference data asset with a valid value.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValuesImplementationProperties extends RelationshipBeanProperties
{
    private String              symbolicName        = null;
    private String              implementationValue = null;
    private Map<String, String> additionalValues    = null;


    /**
     * Default constructor
     */
    public ValidValuesImplementationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ValidValuesImplementationProperties(ValidValuesImplementationProperties template)
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
        if (additionalValues == null)
        {
            return null;
        }
        else if (additionalValues.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalValues);
        }
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ValidValuesImplementationProperties{" +
                "symbolicName='" + symbolicName + '\'' +
                ", implementationValue='" + implementationValue + '\'' +
                ", additionalValues=" + additionalValues +
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
        ValidValuesImplementationProperties asset = (ValidValuesImplementationProperties) objectToCompare;
        return Objects.equals(getSymbolicName(), asset.getSymbolicName()) &&
                Objects.equals(getImplementationValue(), asset.getImplementationValue()) &&
                Objects.equals(getAdditionalValues(), asset.getAdditionalValues());
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSymbolicName(), getImplementationValue(), getAdditionalValues());
    }
}
