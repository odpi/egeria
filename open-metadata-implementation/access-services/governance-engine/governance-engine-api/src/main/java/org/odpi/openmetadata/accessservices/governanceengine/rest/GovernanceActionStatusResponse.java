/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceActionStatusResponse is the response structure used on the OMAS REST API calls that return a
 * GovernanceActionStatus enum.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionStatusResponse extends GovernanceEngineOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private GovernanceActionStatus governanceActionStatus = null;

    /**
     * Default constructor
     */
    public GovernanceActionStatusResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionStatusResponse(GovernanceActionStatusResponse template)
    {
        super(template);

        if (template != null)
        {
            this.governanceActionStatus = template.getGovernanceActionStatus();
        }
    }


    /**
     * Return the Annotation object.
     *
     * @return governanceActionStatus
     */
    public GovernanceActionStatus getGovernanceActionStatus()
    {
        if (governanceActionStatus == null)
        {
            return null;
        }
        else
        {
            return governanceActionStatus;
        }
    }


    /**
     * Set up the Annotation object.
     *
     * @param governanceActionStatus - governanceActionStatus object
     */
    public void setGovernanceActionStatus(GovernanceActionStatus governanceActionStatus)
    {
        this.governanceActionStatus = governanceActionStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionStatusResponse{" +
                "governanceActionStatus=" + governanceActionStatus +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        GovernanceActionStatusResponse that = (GovernanceActionStatusResponse) objectToCompare;
        return Objects.equals(governanceActionStatus, that.governanceActionStatus);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), governanceActionStatus);
    }
}
