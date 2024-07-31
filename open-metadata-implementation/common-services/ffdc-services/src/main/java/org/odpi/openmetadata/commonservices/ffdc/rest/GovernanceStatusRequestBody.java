/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceDefinitionRequestBody provides a structure used when creating governance definitions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceStatusRequestBody extends ExternalSourceRequestBody
{
    private GovernanceDefinitionStatus status = null;


    /**
     * Default constructor
     */
    public GovernanceStatusRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceStatusRequestBody(GovernanceStatusRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.status = template.getStatus();
        }
    }


    /**
     * Return the initial status of the governance definition.
     *
     * @return instance status
     */
    public GovernanceDefinitionStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the initial status of the governance definition.
     *
     * @param status instance status
     */
    public void setStatus(GovernanceDefinitionStatus status)
    {
        this.status = status;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceStatusRequestBody{" +
                "status=" + status +
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
        if (! (objectToCompare instanceof GovernanceStatusRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return  status == that.status;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), status);
    }
}
