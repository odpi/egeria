/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.myprofile.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ToDoStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ToDoStatusRequestBody provides a structure for passing the status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToDoStatusRequestBody
{
    private ToDoStatus toDoStatus = null;


    /**
     * Default constructor
     */
    public ToDoStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDoStatusRequestBody(ToDoStatusRequestBody template)
    {
        if (template != null)
        {
            toDoStatus = template.getToDoStatus();
        }
    }


    /**
     * Return the status value.
     *
     * @return element status enum value
     */
    public ToDoStatus getToDoStatus()
    {
        return toDoStatus;
    }


    /**
     * Set up the status value.
     *
     * @param toDoStatus element status enum value
     */
    public void setToDoStatus(ToDoStatus toDoStatus)
    {
        this.toDoStatus = toDoStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ToDoStatusRequestBody{newStatus=" + toDoStatus + '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        ToDoStatusRequestBody that = (ToDoStatusRequestBody) objectToCompare;
        return toDoStatus == that.toDoStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), toDoStatus);
    }
}
