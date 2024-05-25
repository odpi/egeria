/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionTypeProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * UpdateGovernanceActionTypeRequestBody describes the request body used to update governance action type properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateGovernanceActionTypeRequestBody
{
    private boolean                        mergeUpdate = false;
    private GovernanceActionTypeProperties properties  = null;


    /**
     * Default constructor
     */
    public UpdateGovernanceActionTypeRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public UpdateGovernanceActionTypeRequestBody(UpdateGovernanceActionTypeRequestBody template)
    {
        if (template != null)
        {
            mergeUpdate = template.getMergeUpdate();
            properties = template.getProperties();
        }
    }


    /**
     * Return indication of there properties should be over-write or replace existing properties.
     *
     * @return boolean flag
     */
    public boolean getMergeUpdate()
    {
        return mergeUpdate;
    }


    /**
     * Set up the indication of there properties should be over-write or replace existing properties.
     *
     * @param mergeUpdate boolean flag
     */
    public void setMergeUpdate(boolean mergeUpdate)
    {
        this.mergeUpdate = mergeUpdate;
    }


    /**
     * Return the properties for the element.
     *
     * @return properties object
     */
    public GovernanceActionTypeProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the element.
     *
     * @param properties properties object
     */
    public void setProperties(GovernanceActionTypeProperties properties)
    {
        this.properties = properties;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "UpdateGovernanceActionTypeRequestBody{" +
                       "mergeUpdate=" + mergeUpdate +
                       ", properties=" + properties +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        UpdateGovernanceActionTypeRequestBody that = (UpdateGovernanceActionTypeRequestBody) objectToCompare;
        return mergeUpdate == that.mergeUpdate &&
                       Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(mergeUpdate, properties);
    }
}
