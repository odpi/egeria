/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalDetailsRequestBody provides a structure for passing personal details over a REST API.
 * It is used for creating and updating PersonalProfiles.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalDetailsRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              userId               = null;
    private String              employeeNumber       = null;
    private String              fullName             = null;
    private String              knownName            = null;
    private String              jobTitle             = null;
    private String              jobRoleDescription   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public PersonalDetailsRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalDetailsRequestBody(PersonalDetailsRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.userId = template.getUserId();
            this.employeeNumber = template.getEmployeeNumber();
            this.fullName = template.getFullName();
            this.knownName = template.getKnownName();
            this.jobTitle = template.getJobTitle();
            this.jobRoleDescription = template.getJobRoleDescription();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the anchoring userId for this profile.
     *
     * @return string
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the anchoring userId for this profile.
     *
     * @param userId string
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
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
     * Return the full name for this governance officer.
     *
     * @return string name
     */
    public String getFullName()
    {
        return fullName;
    }


    /**
     * Set up the full name for this governance officer.
     *
     * @param fullName string name
     */
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }


    /**
     * Return the preferred name for this governance officer.
     *
     * @return string name
     */
    public String getKnownName()
    {
        return knownName;
    }


    /**
     * Set up the preferred name for this governance officer.
     *
     * @param knownName string name
     */
    public void setKnownName(String knownName)
    {
        this.knownName = knownName;
    }


    /**
     * Return the primary job title for this governance officer.  This may relate to the specific
     * governance responsibilities, or may be their main role if the governance responsibilities are
     * just an adjunct to their main role.
     *
     * @return string title
     */
    public String getJobTitle()
    {
        return jobTitle;
    }


    /**
     * Set up the primary job title for this governance officer.  This may relate to the specific
     * governance responsibilities, or may be their main role if the governance responsibilities are
     * just an adjunct to their main role.
     *
     * @param jobTitle string title
     */
    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }


    /**
     * Return the description of the job role for this governance officer.  This may relate to the specific
     * governance responsibilities, or may be their main role if the governance responsibilities are
     * just an adjunct to their main role.
     *
     * @return string description
     */
    public String getJobRoleDescription()
    {
        return jobRoleDescription;
    }


    /**
     * Set up the description of the job role for this governance officer.  This may relate to the specific
     * governance responsibilities, or may be their main role if the governance responsibilities are
     * just an adjunct to their main role.
     *
     * @param jobRoleDescription string description
     */
    public void setJobRoleDescription(String jobRoleDescription)
    {
        this.jobRoleDescription = jobRoleDescription;
    }



    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
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
                ", userId='" + userId + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", knownName='" + knownName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobRoleDescription='" + jobRoleDescription + '\'' +
                ", additionalProperties=" + additionalProperties +
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
        if (!(objectToCompare instanceof PersonalDetailsRequestBody))
        {
            return false;
        }
        PersonalDetailsRequestBody that = (PersonalDetailsRequestBody) objectToCompare;
        return  Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getEmployeeNumber(), that.getEmployeeNumber()) &&
                Objects.equals(getFullName(), that.getFullName()) &&
                Objects.equals(getKnownName(), that.getKnownName()) &&
                Objects.equals(getJobTitle(), that.getJobTitle()) &&
                Objects.equals(getJobRoleDescription(), that.getJobRoleDescription()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getUserId(), getEmployeeNumber(), getFullName(), getKnownName(), getJobTitle(),
                            getJobRoleDescription(), getAdditionalProperties());
    }
}
