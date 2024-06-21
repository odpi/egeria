/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Parameter list
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FunctionParameterInfos
{
    private List<FunctionParameterInfo> parameters = new ArrayList<>();


    /**
     * Constructor
     */
    public FunctionParameterInfos()
    {
    }


    /**
     * Return the parameter list.
     *
     * @return list
     */
    public List<FunctionParameterInfo> getParameters()
    {
        return parameters;
    }


    /**
     * Set up the parameter list
     *
     * @param parameters list
     */
    public void setParameters(List<FunctionParameterInfo> parameters)
    {
        this.parameters = parameters;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FunctionParameterInfos{" +
                "parameters=" + parameters +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        FunctionParameterInfos that = (FunctionParameterInfos) objectToCompare;
        return Objects.equals(parameters, that.parameters);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(parameters);
    }
}
