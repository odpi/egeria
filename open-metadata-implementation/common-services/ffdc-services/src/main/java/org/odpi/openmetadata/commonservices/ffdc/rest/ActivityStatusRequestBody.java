/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActivityStatusRequestBody provides a structure for passing the status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActivityStatusRequestBody extends QueryOptions
{
    private List<ActivityStatus> activityStatusList = null;


    /**
     * Default constructor
     */
    public ActivityStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActivityStatusRequestBody(ActivityStatusRequestBody template)
    {
        super(template);

        if (template != null)
        {
            activityStatusList = template.getActivityStatusList();
        }
    }


    /**
     * Return the status list value.
     *
     * @return status enum list
     */
    public List<ActivityStatus> getActivityStatusList()
    {
        return activityStatusList;
    }


    /**
     * Set up the status value.
     *
     * @param activityStatusList status enum list
     */
    public void setActivityStatusList(List<ActivityStatus> activityStatusList)
    {
        this.activityStatusList = activityStatusList;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActivityStatusRequestBody{" +
                "activityStatusList=" + activityStatusList +
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
        ActivityStatusRequestBody that = (ActivityStatusRequestBody) objectToCompare;
        return activityStatusList == that.activityStatusList;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), activityStatusList);
    }
}
