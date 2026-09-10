/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a single parameter passed to the job at runtime.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageExecutionParametersRunFacetParameter
{
    private String key = null;
    private String name = null;
    private String description = null;
    private String value = null;


    /**
     * Default constructor
     */
    public OpenLineageExecutionParametersRunFacetParameter()
    {
    }


    /**
     * Return the unique identifier of the parameter.
     *
     * @return string
     */
    public String getKey()
    {
        return key;
    }


    /**
     * Set up the unique identifier of the parameter.
     *
     * @param key string
     */
    public void setKey(String key)
    {
        this.key = key;
    }


    /**
     * Return the human-readable name of the parameter.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the human-readable name of the parameter.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the human-readable description of the parameter.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the human-readable description of the parameter.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the value of the parameter.
     *
     * @return string
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Set up the value of the parameter.
     *
     * @param value string
     */
    public void setValue(String value)
    {
        this.value = value;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageExecutionParametersRunFacetParameter{" +
                       "key='" + key + '\'' +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", value='" + value + '\'' +
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
        OpenLineageExecutionParametersRunFacetParameter that = (OpenLineageExecutionParametersRunFacetParameter) objectToCompare;
        return Objects.equals(key, that.key) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(value, that.value);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(key, name, description, value);
    }
}
