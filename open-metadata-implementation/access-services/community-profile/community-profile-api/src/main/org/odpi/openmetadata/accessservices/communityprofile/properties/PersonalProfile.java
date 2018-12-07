/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The PersonalProfile describes an individual.  Information about the
 * personal profile is stored as an Person entity in the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfile extends CommonHeader
{
    private String              employeeNumber       = null;
    private String              fullName             = null;
    private String              knownName            = null;
    private String              jobTitle             = null;
    private String              jobRoleDescription   = null;
    private List<UserIdentity>  associatedUserIds    = null;
    private int                 karmaPoints          = 0;



    /**
     * Default Constructor
     */
    public PersonalProfile()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public PersonalProfile(PersonalProfile template)
    {
        super (template);

        if (template != null)
        {
            this.employeeNumber = template.getEmployeeNumber();
            this.fullName = template.getFullName();
            this.knownName = template.getKnownName();
            this.jobTitle = template.getJobTitle();
            this.jobRoleDescription = template.getJobRoleDescription();
            this.associatedUserIds = template.getAssociatedUserIds();
            this.karmaPoints = template.getKarmaPoints();
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalProfile{" +
                "employeeNumber='" + employeeNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", knownName='" + knownName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobRoleDescription='" + jobRoleDescription + '\'' +
                ", associatedUserIds=" + associatedUserIds +
                ", karmaPoints=" + karmaPoints +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", classifications=" + getClassifications() +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */



    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */

}
