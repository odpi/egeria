/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIParameterProperties is a class for representing a parameter in an API specification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIParameterProperties extends SchemaAttributeProperties
{
    private String parameterType = null;


    /**
     * Default constructor
     */
    public APIParameterProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.API_PARAMETER.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public APIParameterProperties(APIParameterProperties template)
    {
        super(template);

        if (template != null)
        {
            parameterType = template.getParameterType();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public APIParameterProperties(SchemaAttributeProperties template)
    {
        super(template);

        if (template != null)
        {
            if (getExtendedProperties() != null)
            {
                Map<String, Object> extendedProperties = getExtendedProperties();

                parameterType = extendedProperties.get("parameterType").toString();

                extendedProperties.remove("parameterType");

                super.setExtendedProperties(extendedProperties);
            }
        }
    }


    /**
     * Return the type of parameter - for example for REST APIs, is it a PathVariable or a RequestParameter?
     *
     * @return string name
     */
    public String getParameterType()
    {
        return parameterType;
    }


    /**
     * Set up the type of parameter - for example for REST APIs, is it a PathVariable or a RequestParameter?
     *
     * @param parameterType string name
     */
    public void setParameterType(String parameterType)
    {
        this.parameterType = parameterType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "APIParameterProperties{" +
                "parameterType='" + parameterType + '\'' +
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
        APIParameterProperties that = (APIParameterProperties) objectToCompare;
        return Objects.equals(parameterType, that.parameterType);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), parameterType);
    }
}
