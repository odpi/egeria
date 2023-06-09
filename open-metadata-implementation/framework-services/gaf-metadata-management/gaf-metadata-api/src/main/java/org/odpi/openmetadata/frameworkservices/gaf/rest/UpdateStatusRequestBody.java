/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateStatusRequestBody provides a structure for passing the updated status and effectivity dates for a metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UpdateStatusRequestBody extends UpdateRequestBody
{
    private ElementStatus newStatus = null;


    /**
     * Default constructor
     */
    public UpdateStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdateStatusRequestBody(UpdateStatusRequestBody template)
    {
        super(template);

        if (template != null)
        {
            newStatus = template.getNewStatus();
        }
    }


    /**
     * Return the new status value - or null to leave as is.
     *
     * @return element status enum value
     */
    public ElementStatus getNewStatus()
    {
        return newStatus;
    }


    /**
     * Set up the new status value - or null to leave as is.
     *
     * @param newStatus element status enum value
     */
    public void setNewStatus(ElementStatus newStatus)
    {
        this.newStatus = newStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UpdateStatusRequestBody{" +
                       "externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
                       ", forLineage=" + getForLineage() +
                       ", forDuplicateProcessing=" + getForDuplicateProcessing() +
                       ", effectiveTime=" + getEffectiveTime() +
                       ", newStatus=" + newStatus +
                       '}';
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        UpdateStatusRequestBody that = (UpdateStatusRequestBody) objectToCompare;
        return newStatus == that.newStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), newStatus);
    }
}
