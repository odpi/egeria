/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MyProfileRequestBody provides a structure for passing personal details over a REST API.
 * It is used for creating and updating a profile for the calling user.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MyProfileRequestBody
{
    private String              qualifiedName        = null;
    private String              fullName             = null;
    private String              knownName            = null;
    private String              jobTitle             = null;
    private String              jobRoleDescription   = null;
    private Map<String, Object> profileProperties    = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public MyProfileRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MyProfileRequestBody(MyProfileRequestBody template)
    {
        if (template != null)
        {
            this.qualifiedName = template.getQualifiedName();
            this.fullName = template.getFullName();
            this.knownName = template.getKnownName();
            this.jobTitle = template.getJobTitle();
            this.jobRoleDescription = template.getJobRoleDescription();
            this.profileProperties = template.getProfileProperties();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the unique name for this person - may be employee identifier.
     *
     * @return String identifier
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name for this person - may be employee identifier.
     *
     * @param qualifiedName String identifier
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the full name for this person.
     *
     * @return string name
     */
    public String getFullName()
    {
        return fullName;
    }


    /**
     * Set up the full name for this person.
     *
     * @param fullName string name
     */
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }


    /**
     * Return the preferred name for this person.
     *
     * @return string name
     */
    public String getKnownName()
    {
        return knownName;
    }


    /**
     * Set up the preferred name for this person.
     *
     * @param knownName string name
     */
    public void setKnownName(String knownName)
    {
        this.knownName = knownName;
    }


    /**
     * Return the primary job title for this person.  This may relate to the specific
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
     * Set up the primary job title for this person.  This may relate to the specific
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
     * Return the description of the job role for this person.  This may relate to the specific
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
     * Set up the description of the job role for this person.  This may relate to the specific
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
     * Set up profile properties.  These are properties that come from the subclass of Person.
     * Null means no profile properties are available.
     *
     * @param profileProperties  map from string (property name) to object (property value)
     */
    public void setProfileProperties(Map<String, Object> profileProperties)
    {
        this.profileProperties = profileProperties;
    }


    /**
     * Return profile properties.  These are properties that come from the subclass of Person.
     * Null means no profile properties are available.
     *
     * @return map from string (property name) to object (property value)
     */
    public Map<String,Object> getProfileProperties()
    {
        if (profileProperties == null)
        {
            return null;
        }
        else if (profileProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(profileProperties);
        }
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
        return "MyProfileRequestBody{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", knownName='" + knownName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobRoleDescription='" + jobRoleDescription + '\'' +
                ", profileProperties=" + profileProperties +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        MyProfileRequestBody that = (MyProfileRequestBody) objectToCompare;
        return Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getFullName(), that.getFullName()) &&
                Objects.equals(getKnownName(), that.getKnownName()) &&
                Objects.equals(getJobTitle(), that.getJobTitle()) &&
                Objects.equals(getJobRoleDescription(), that.getJobRoleDescription()) &&
                Objects.equals(getProfileProperties(), that.getProfileProperties()) &&
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
        return Objects.hash(getQualifiedName(), getFullName(), getKnownName(), getJobTitle(), getJobRoleDescription(),
                            getProfileProperties(), getAdditionalProperties());
    }
}
