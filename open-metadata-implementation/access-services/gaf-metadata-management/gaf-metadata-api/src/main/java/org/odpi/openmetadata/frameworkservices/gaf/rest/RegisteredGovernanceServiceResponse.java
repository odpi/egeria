/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredGovernanceServiceElement;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The response structure used on the Open Survey Framework REST API calls that returns a
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
        return registeredGovernanceService;
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
                "} " + super.toString();
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
