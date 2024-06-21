/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.FunctionProperties;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Payload for creating a function - maps to CreatFunctionRequest.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CreateFunctionRequestBody
{
    private FunctionProperties function_info = null;


    /**
     * Constructor
     */
    public CreateFunctionRequestBody()
    {
    }


    /**
     * Return the properties of the function to create.
     *
     * @return properties
     */
    public FunctionProperties getFunction_info()
    {
        return function_info;
    }


    /**
     * Set up the properties of the function to create.
     *
     * @param function_info properties
     */
    public void setFunction_info(FunctionProperties function_info)
    {
        this.function_info = function_info;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CreateFunctionRequestBody{" +
                "function_info=" + function_info +
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
        CreateFunctionRequestBody that = (CreateFunctionRequestBody) objectToCompare;
        return Objects.equals(function_info, that.function_info);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(function_info);
    }
}
