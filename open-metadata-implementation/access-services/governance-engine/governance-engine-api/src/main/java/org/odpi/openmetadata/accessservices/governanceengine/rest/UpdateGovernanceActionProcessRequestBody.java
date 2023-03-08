/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * UpdateGovernanceActionProcessRequestBody describes the request body used to update governance action process properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateGovernanceActionProcessRequestBody extends NewGovernanceActionProcessRequestBody
{
    private static final long    serialVersionUID = 1L;

    private boolean mergeUpdate = false;


    /**
     * Default constructor
     */
    public UpdateGovernanceActionProcessRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public UpdateGovernanceActionProcessRequestBody(UpdateGovernanceActionProcessRequestBody template)
    {
        super(template);

        if (template != null)
        {
            mergeUpdate = template.getMergeUpdate();
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "UpdateGovernanceActionProcessRequestBody{" +
                       "mergeUpdate=" + mergeUpdate +
                       ", initialStatus=" + getProcessStatus() +
                       ", properties=" + getProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UpdateGovernanceActionProcessRequestBody that = (UpdateGovernanceActionProcessRequestBody) objectToCompare;
        return mergeUpdate == that.mergeUpdate;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mergeUpdate);
    }
}
