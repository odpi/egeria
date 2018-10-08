/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The MyProfile describes an individual.  It is used for managing collections of assets for the
 * individual and is tied to a user identity.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MyProfile extends AssetConsumerElementHeader
{
    private String              guid                 = null;
    private String              type                 = null;
    private String              employeeNumber       = null;
    private String              fullName             = null;
    private String              knownName            = null;
    private String              jobTitle             = null;
    private String              jobRoleDescription   = null;
    private int                 karmaPoints          = 0;
    private List<UserIdentity>  associatedUserIds    = null;
    private Map<String, Object> additionalProperties = null;



    /**
     * Default Constructor
     */
    public MyProfile()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public MyProfile(MyProfile template)
    {
        super(template);

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
            this.associatedUserIds = template.getAssociatedUserIds();
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
     * Return the karma points awarded to this person.
     *
     * @return count
     */
    public int getKarmaPoints()
    {
        return karmaPoints;
    }


    /**
     * Set up the karma points for this person.
     *
     * @param karmaPoints count
     */
    public void setKarmaPoints(int karmaPoints)
    {
        this.karmaPoints = karmaPoints;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String,Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String,Object> getAdditionalProperties()
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "MyProfile{" +
                "guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", knownName='" + knownName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobRoleDescription='" + jobRoleDescription + '\'' +
                ", karmaPoints='" + karmaPoints + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        MyProfile myProfile = (MyProfile) objectToCompare;
        return Objects.equals(guid, myProfile.guid) &&
                Objects.equals(getType(), myProfile.getType()) &&
                Objects.equals(getEmployeeNumber(), myProfile.getEmployeeNumber()) &&
                Objects.equals(getFullName(), myProfile.getFullName()) &&
                Objects.equals(getKnownName(), myProfile.getKnownName()) &&
                Objects.equals(getJobTitle(), myProfile.getJobTitle()) &&
                Objects.equals(getJobRoleDescription(), myProfile.getJobRoleDescription()) &&
                Objects.equals(getKarmaPoints(), myProfile.getKarmaPoints()) &&
                Objects.equals(getAssociatedUserIds(), myProfile.getAssociatedUserIds()) &&
                Objects.equals(getAdditionalProperties(), myProfile.getAdditionalProperties());
    }

    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, getType(), getEmployeeNumber(), getFullName(), getKnownName(), getJobTitle(),
                            getJobRoleDescription(), getKarmaPoints(), getAssociatedUserIds(), getAdditionalProperties());
    }
}
