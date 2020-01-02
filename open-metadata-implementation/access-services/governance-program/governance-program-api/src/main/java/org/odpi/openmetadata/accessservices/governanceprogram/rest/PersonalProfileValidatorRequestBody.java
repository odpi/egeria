/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalDetailsRequestBody provides a structure for passing personal details over a REST API.
 * It is used for creating PersonalProfiles.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileValidatorRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              employeeNumber       = null;


    /**
     * Default constructor
     */
    public PersonalProfileValidatorRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileValidatorRequestBody(PersonalProfileValidatorRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.employeeNumber = template.getEmployeeNumber();
        }
    }


    /**
     * Return the the unique employee number for this governance officer.
     *
     * @return String identifier
     */
    public String getEmployeeNumber()
    {
        return employeeNumber;
    }


    /**
     * Set up the unique employee number for this governance officer.
     *
     * @param employeeNumber String identifier
     */
    public void setEmployeeNumber(String employeeNumber)
    {
        this.employeeNumber = employeeNumber;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalDetailsRequestBody{" +
                ", employeeNumber='" + employeeNumber +
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
        if (!(objectToCompare instanceof PersonalProfileValidatorRequestBody))
        {
            return false;
        }
        PersonalProfileValidatorRequestBody that = (PersonalProfileValidatorRequestBody) objectToCompare;
        return  Objects.equals(getEmployeeNumber(), that.getEmployeeNumber());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(employeeNumber);
    }
}
