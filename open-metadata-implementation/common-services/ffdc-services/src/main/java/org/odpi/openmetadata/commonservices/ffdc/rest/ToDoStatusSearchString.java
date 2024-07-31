/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import java.util.Objects;

public class ToDoStatusSearchString extends ToDoStatusRequestBody
{
    private String searchString = null;

    /**
     * Default constructor
     */
    public ToDoStatusSearchString()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDoStatusSearchString(ToDoStatusRequestBody template)
    {
        super(template);
    }


    /**
     * Return the search string.
     *
     * @return string
     */
    public String getSearchString()
    {
        return searchString;
    }


    /**
     * Set up the search string.
     *
     * @param searchString string
     */
    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ToDoStatusSearchString{" +
                "searchString='" + searchString + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ToDoStatusSearchString that = (ToDoStatusSearchString) objectToCompare;
        return Objects.equals(searchString, that.searchString);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), searchString);
    }
}
