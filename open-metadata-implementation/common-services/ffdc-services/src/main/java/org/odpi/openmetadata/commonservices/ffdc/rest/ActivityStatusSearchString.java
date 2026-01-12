/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;

import java.util.Objects;

/**
 * A request body that allows the status of an activity to be included in the search criteria.
 */
public class ActivityStatusSearchString extends SearchStringRequestBody
{
    private ActivityStatus activityStatus = null;

    /**
     * Default constructor
     */
    public ActivityStatusSearchString()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActivityStatusSearchString(ActivityStatusSearchString template)
    {
        super(template);

        if (template != null)
        {
            activityStatus = template.getActivityStatus();
        }
    }


    /**
     * Return the status value.
     *
     * @return element status enum value
     */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }


    /**
     * Set up the status value.
     *
     * @param activityStatus element status enum value
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActivityStatusSearchString{" +
                "activityStatus=" + activityStatus +
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
        ActivityStatusSearchString that = (ActivityStatusSearchString) objectToCompare;
        return Objects.equals(activityStatus, that.activityStatus);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), activityStatus);
    }
}
