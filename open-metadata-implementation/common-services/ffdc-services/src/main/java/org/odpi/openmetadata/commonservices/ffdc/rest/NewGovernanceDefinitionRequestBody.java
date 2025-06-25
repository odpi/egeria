/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewGovernanceDefinitionRequestBody provides an optional structure used when creating governance definitions.
 * It provides an optional extension to set up the initial status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewGovernanceDefinitionRequestBody extends NewElementRequestBody
{
    private GovernanceDefinitionStatus     initialStatus = null;


    /**
     * Default constructor
     */
    public NewGovernanceDefinitionRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewGovernanceDefinitionRequestBody(NewGovernanceDefinitionRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.initialStatus = template.getInitialStatus();
        }
    }


    /**
     * Return the initial status of the governance definition.
     *
     * @return instance status
     */
    public GovernanceDefinitionStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status of the governance definition.
     *
     * @param initialStatus instance status
     */
    public void setInitialStatus(GovernanceDefinitionStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewGovernanceDefinitionRequestBody{" +
                ", initialStatus=" + initialStatus +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NewGovernanceDefinitionRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return initialStatus == that.initialStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), initialStatus);
    }
}
