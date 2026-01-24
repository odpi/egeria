/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;

import java.util.List;
import java.util.Objects;

/**
 * A request body that allows the status of an authored referenceable to be included in the search criteria.
 */
public class ContentStatusSearchString extends SearchStringRequestBody
{
    private List<ContentStatus> contentStatusList = null;

    /**
     * Default constructor
     */
    public ContentStatusSearchString()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ContentStatusSearchString(ContentStatusSearchString template)
    {
        super(template);

        if (template != null)
        {
            contentStatusList = template.getContentStatusList();
        }
    }


    /**
     * Return the status list.
     *
     * @return status enum list
     */
    public List<ContentStatus> getContentStatusList()
    {
        return contentStatusList;
    }


    /**
     * Set up the status list.
     *
     * @param contentStatusList status enum list
     */
    public void setContentStatusList(List<ContentStatus> contentStatusList)
    {
        this.contentStatusList = contentStatusList;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ContentStatusSearchString{" +
                "contentStatusList=" + contentStatusList +
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
        ContentStatusSearchString that = (ContentStatusSearchString) objectToCompare;
        return Objects.equals(contentStatusList, that.contentStatusList);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), contentStatusList);
    }
}
