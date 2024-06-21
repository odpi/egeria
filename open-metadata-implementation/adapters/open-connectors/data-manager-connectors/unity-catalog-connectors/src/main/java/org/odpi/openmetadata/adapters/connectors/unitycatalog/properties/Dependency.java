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
 * Describes a dependency of a SQL object. Either the __table__ field or the __function__ field must be defined.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dependency
{
    private TableDependency    table = null;
    private FunctionDependency function = null;


    /**
     * Constructor
     */
    public Dependency()
    {
    }


    /**
     * Return the table dependency.
     *
     * @return dependency
     */
    public TableDependency getTable()
    {
        return table;
    }


    /**
     * Set upi the table dependency.
     *
     * @param table dependency
     */
    public void setTable(TableDependency table)
    {
        this.table = table;
    }


    /**
     * Return the function dependency.
     *
     * @return dependency
     */
    public FunctionDependency getFunction()
    {
        return function;
    }


    /**
     * Set up the function dependency.
     *
     * @param function dependency
     */
    public void setFunction(FunctionDependency function)
    {
        this.function = function;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Dependency{" +
                "table=" + table +
                ", function=" + function +
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
        Dependency that = (Dependency) objectToCompare;
        return Objects.equals(table, that.table) && Objects.equals(function, that.function);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(table, function);
    }
}
