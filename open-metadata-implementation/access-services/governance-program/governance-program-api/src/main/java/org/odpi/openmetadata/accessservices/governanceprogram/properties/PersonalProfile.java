/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The PersonalProfile describes an individual who has (or will be) appointed to one of the
 * GovernanceOfficerProperties roles defined in the governance program.  Information about the
 * personal profile is stored as an Person entity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfile implements Serializable
{
    private static final long          serialVersionUID = 1L;

    private String              guid                 = null;
    private String              type                 = null;
    private String              employeeNumber       = null;
    private String              fullName             = null;
    private String              knownName            = null;
    private String              jobTitle             = null;
    private String              jobRoleDescription   = null;
    private List<UserIdentity>  associatedUserIds    = null;
    private Map<String, String> additionalProperties = null;



    /**
     * Default Constructor
     */
    public PersonalProfile()
    {
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public PersonalProfile(PersonalProfile template)
    {
        if (template != null)
        {
            this.guid = template.getGUID();
            this.type = template.getType();
            this.employeeNumber = template.getEmployeeNumber();
            this.fullName = template.getFullName();
            this.knownName = template.getKnownName();
            this.jobTitle = template.getJobTitle();
            this.jobRoleDescription = template.getJobRoleDescription();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the unique identifier for this profile.  This value is assigned by the metadata collection
     * when the governance officer profile is created.
     *
     * @return String guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this profile.  This value is assigned by the metadata collection
     * when the governance officer profile is created.
     *
     * @param guid String guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name of the specific type used to store the governance officer's profile.
     *
     * @return String type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the name of the specific type used to store the governance officer's profile.
     *
     * @param type String type name
     */
    public void setType(String type)
    {
        this.type = type;
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
     * Return the userIds associated with the profile.
     *
     * @return list of userIds
     */
    public List<UserIdentity> getAssociatedUserIds()
    {
        if (associatedUserIds == null)
        {
            return null;
        }
        else if (associatedUserIds.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(associatedUserIds);
        }
    }


    /**
     * Set up the userIds associated with the profile.
     *
     * @param associatedUserIds list of userIds
     */
    public void setAssociatedUserIds(List<UserIdentity> associatedUserIds)
    {
        this.associatedUserIds = associatedUserIds;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String,String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String,String> getAdditionalProperties()
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
        return "GovernanceOfficerProperties{" +
                "guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", knownName='" + knownName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobRoleDescription='" + jobRoleDescription + '\'' +
                ", associatedUserIds=" + associatedUserIds + '\'' +
                ", additionalProperties=" + additionalProperties + '\'' +
                ", GUID='" + getGUID() + '\'' +
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
        if (!(objectToCompare instanceof PersonalProfile))
        {
            return false;
        }
        PersonalProfile that = (PersonalProfile) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getEmployeeNumber(), that.getEmployeeNumber()) &&
                Objects.equals(getFullName(), that.getFullName()) &&
                Objects.equals(getKnownName(), that.getKnownName()) &&
                Objects.equals(getJobTitle(), that.getJobTitle()) &&
                Objects.equals(getJobRoleDescription(), that.getJobRoleDescription()) &&
                Objects.equals(getAssociatedUserIds(), that.getAssociatedUserIds()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties());
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid);
    }
}
