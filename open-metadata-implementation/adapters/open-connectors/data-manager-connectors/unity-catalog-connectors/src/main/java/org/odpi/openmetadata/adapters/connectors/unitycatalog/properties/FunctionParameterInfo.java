/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Common properties of an element stored in Unity catalog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FunctionParameterInfo extends DataFieldInfo
{
    private FunctionParameterMode parameter_mode = null;
    private FunctionParameterType parameter_type = null;
    private String                parameter_default = null;


    /**
     * Constructor
     */
    public FunctionParameterInfo()
    {
    }

    public FunctionParameterMode getParameter_mode()
    {
        return parameter_mode;
    }

    public void setParameter_mode(FunctionParameterMode parameter_mode)
    {
        this.parameter_mode = parameter_mode;
    }

    public FunctionParameterType getParameter_type()
    {
        return parameter_type;
    }

    public void setParameter_type(FunctionParameterType parameter_type)
    {
        this.parameter_type = parameter_type;
    }

    public String getParameter_default()
    {
        return parameter_default;
    }

    public void setParameter_default(String parameter_default)
    {
        this.parameter_default = parameter_default;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FunctionParameterInfo{" +
                "parameter_mode=" + parameter_mode +
                ", parameter_type=" + parameter_type +
                ", parameter_default='" + parameter_default + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FunctionParameterInfo that = (FunctionParameterInfo) objectToCompare;
        return parameter_mode == that.parameter_mode && parameter_type == that.parameter_type && Objects.equals(parameter_default, that.parameter_default);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), parameter_mode, parameter_type, parameter_default);
    }
}
