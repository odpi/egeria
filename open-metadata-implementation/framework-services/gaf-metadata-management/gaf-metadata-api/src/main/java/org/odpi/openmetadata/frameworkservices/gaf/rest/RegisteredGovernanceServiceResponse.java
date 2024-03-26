/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredGovernanceServiceElement;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RegisteredGovernanceServiceResponse is the response structure used on the Governance Engine OMAS REST API calls that returns a
 * RegisteredGovernanceServiceElement object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredGovernanceServiceResponse extends OMAGGAFAPIResponse
{
    private RegisteredGovernanceServiceElement registeredGovernanceService = null;

    /**
     * Default constructor
     */
    public RegisteredGovernanceServiceResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredGovernanceServiceResponse(RegisteredGovernanceServiceResponse template)
    {
        super(template);

        if (template != null)
        {
            this.registeredGovernanceService = template.getRegisteredGovernanceService();
        }
    }


    /**
     * Return the properties object.
     *
     * @return properties object
     */
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService()
    {
        if (registeredGovernanceService == null)
        {
            return null;
        }
        else
        {
            return registeredGovernanceService;
        }
    }


    /**
     * Set up the properties object.
     *
     * @param registeredGovernanceService  properties object
     */
    public void setRegisteredGovernanceService(RegisteredGovernanceServiceElement registeredGovernanceService)
    {
        this.registeredGovernanceService = registeredGovernanceService;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RegisteredGovernanceServiceResponse{" +
                "registeredGovernanceService=" + registeredGovernanceService +
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
        RegisteredGovernanceServiceResponse that = (RegisteredGovernanceServiceResponse) objectToCompare;
        return Objects.equals(getRegisteredGovernanceService(), that.getRegisteredGovernanceService());
    }

    
    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getRegisteredGovernanceService());
    }
}
