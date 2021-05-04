/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

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
public class PersonalProfile implements Serializable, MetadataElement
{
    private static final long          serialVersionUID = 1L;

    private ElementHeader       elementHeader        = null;

    private String              employeeNumber       = null; /* qualifiedName */
    private String              fullName             = null; /* fullName */
    private String              knownName            = null; /* name */
    private String              jobTitle             = null; /* jobTitle */
    private String              description          = null; /* description */
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
            this.elementHeader = template.getElementHeader();
            this.employeeNumber = template.getEmployeeNumber();
            this.fullName = template.getFullName();
            this.knownName = template.getKnownName();
            this.jobTitle = template.getJobTitle();
            this.description = template.getDescription();
            this.additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
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
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the job role for this governance officer.  This may relate to the specific
     * governance responsibilities, or may be their main role if the governance responsibilities are
     * just an adjunct to their main role.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
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
        return "PersonalProfile{" +
                       "elementHeader=" + elementHeader +
                       ", employeeNumber='" + employeeNumber + '\'' +
                       ", fullName='" + fullName + '\'' +
                       ", knownName='" + knownName + '\'' +
                       ", jobTitle='" + jobTitle + '\'' +
                       ", description='" + description + '\'' +
                       ", associatedUserIds=" + associatedUserIds +
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
        PersonalProfile that = (PersonalProfile) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(employeeNumber, that.employeeNumber) &&
                       Objects.equals(fullName, that.fullName) &&
                       Objects.equals(knownName, that.knownName) &&
                       Objects.equals(jobTitle, that.jobTitle) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(associatedUserIds, that.associatedUserIds) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, employeeNumber, fullName, knownName, jobTitle, description, associatedUserIds,
                            additionalProperties);
    }
}
