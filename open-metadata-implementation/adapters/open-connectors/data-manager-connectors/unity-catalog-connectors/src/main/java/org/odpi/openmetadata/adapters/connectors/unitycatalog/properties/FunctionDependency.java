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
 * Describes a function that is dependent on a SQL object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FunctionDependency
{
    private String function_full_name = null;


    /**
     * Constructor
     */
    public FunctionDependency()
    {
    }


    /**
     * Return the full name of the dependent function, in the form of __catalog_name__.__schema_name__.__function_name__.
     * 
     * @return functionFullName
     */
    public String getFunction_full_name()
    {
        return function_full_name;
    }

    
    /**
     * Set up the full name of the dependent function, in the form of __catalog_name__.__schema_name__.__function_name__.
     * 
     * @param function_full_name functionFullName
     */
    public void setFunction_full_name(String function_full_name)
    {
        this.function_full_name = function_full_name;
    }

    
    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FunctionDependency{" +
                "function_full_name='" + function_full_name + '\'' +
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
        FunctionDependency that = (FunctionDependency) objectToCompare;
        return Objects.equals(function_full_name, that.function_full_name);
    }

    
    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(function_full_name);
    }
}
