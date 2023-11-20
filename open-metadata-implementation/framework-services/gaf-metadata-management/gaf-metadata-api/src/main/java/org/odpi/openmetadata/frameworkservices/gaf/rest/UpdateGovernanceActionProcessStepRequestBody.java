/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessStepProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * UpdateGovernanceActionProcessStepRequestBody describes the request body used to update governance action type properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateGovernanceActionProcessStepRequestBody
{
    private boolean                               mergeUpdate = false;
    private GovernanceActionProcessStepProperties properties  = null;


    /**
     * Default constructor
     */
    public UpdateGovernanceActionProcessStepRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public UpdateGovernanceActionProcessStepRequestBody(UpdateGovernanceActionProcessStepRequestBody template)
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
    public GovernanceActionProcessStepProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the element.
     *
     * @param properties properties object
     */
    public void setProperties(GovernanceActionProcessStepProperties properties)
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
        return "UpdateGovernanceActionProcessStepRequestBody{" +
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
        UpdateGovernanceActionProcessStepRequestBody that = (UpdateGovernanceActionProcessStepRequestBody) objectToCompare;
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
