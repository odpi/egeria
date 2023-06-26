/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActorProfileProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The PersonalProfileProperties describes an individual.  Information about the
 * personal profile is stored as an Person entity in the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileProperties extends ActorProfileProperties
{
    private String title             = null;
    private String initials          = null;
    private String givenNames        = null;
    private String surname           = null;
    private String fullName          = null;
    private String pronouns          = null;
    private String jobTitle          = null;
    private String employeeNumber    = null;
    private String employeeType      = null;
    private String preferredLanguage = null;

    private boolean isPublic = false;



    /**
     * Default Constructor
     */
    public PersonalProfileProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public PersonalProfileProperties(PersonalProfileProperties template)
    {
        super (template);

        if (template != null)
        {
            this.fullName = template.getFullName();
            this.jobTitle = template.getJobTitle();
            this.title = template.getTitle();
            this.initials = template.getInitials();
            this.givenNames = template.getGivenNames();
            this.surname = template.getSurname();
            this.employeeNumber = template.getEmployeeNumber();
            this.employeeType = template.getEmployeeType();
            this.preferredLanguage = template.getPreferredLanguage();
            this.isPublic = template.getIsPublic();
        }
    }


    /**
     * Return the full legal name for this person.
     *
     * @return string name
     */
    public String getFullName()
    {
        return fullName;
    }


    /**
     * Set up the full legal name for this person.
     *
     * @param fullName string name
     */
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }


    /**
     * Return the person's preferred pronouns.
     *
     * @return string
     */
    public String getPronouns()
    {
        return pronouns;
    }


    /**
     * Set up the person's preferred pronouns.
     *
     * @param pronouns string
     */
    public void setPronouns(String pronouns)
    {
        this.pronouns = pronouns;
    }


    /**
     * Return the primary job title for this person.
     *
     * @return string title
     */
    public String getJobTitle()
    {
        return jobTitle;
    }


    /**
     * Set up the primary job title for this person.
     *
     * @param jobTitle string title
     */
    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }


    /**
     * Return the courtesy title for the person.
     *
     * @return string
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the courtesy title for the person.
     *
     * @param title string
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return first letter of each of the person's given names.
     *
     * @return string
     */
    public String getInitials()
    {
        return initials;
    }


    /**
     * Set up first letter of each of the person's given names.
     *
     * @param initials string
     */
    public void setInitials(String initials)
    {
        this.initials = initials;
    }


    /**
     * Return the name strings that are the part of a person's name that is not their surname.
     *
     * @return space separated list of names
     */
    public String getGivenNames()
    {
        return givenNames;
    }


    /**
     * Set up the name strings that are the part of a person's name that is not their surname.
     *
     * @param givenNames space separated list of names
     */
    public void setGivenNames(String givenNames)
    {
        this.givenNames = givenNames;
    }


    /**
     * Return the family name of the person.
     *
     * @return string
     */
    public String getSurname()
    {
        return surname;
    }


    /**
     * Set up the family name of the person.
     *
     * @param surname string
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }


    /**
     * Return the unique identifier of the person used by their employer.
     *
     * @return string
     */
    public String getEmployeeNumber()
    {
        return employeeNumber;
    }


    /**
     * Set up the unique identifier of the person used by their employer.
     *
     * @param employeeNumber string
     */
    public void setEmployeeNumber(String employeeNumber)
    {
        this.employeeNumber = employeeNumber;
    }


    /**
     * Return code used by employer typically to describe the type of employment contract.
     *
     * @return string
     */
    public String getEmployeeType()
    {
        return employeeType;
    }


    /**
     * Set up code used by employer typically to describe the type of employment contract.
     *
     * @param employeeType string
     */
    public void setEmployeeType(String employeeType)
    {
        this.employeeType = employeeType;
    }


    /**
     * Return spoken or written language preferred by the person.
     *
     * @return string
     */
    public String getPreferredLanguage()
    {
        return preferredLanguage;
    }


    /**
     * Set up spoken or written language preferred by the person.
     *
     * @param preferredLanguage string
     */
    public void setPreferredLanguage(String preferredLanguage)
    {
        this.preferredLanguage = preferredLanguage;
    }


    /**
     * Return if the contents of this profile is to be shared with colleagues.
     *
     * @return flag
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up if the contents of this profile is to be shared with colleagues.
     *
     * @param isPublic flag
     */
    public void setIsPublic(boolean isPublic)
    {
        this.isPublic = isPublic;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalProfileProperties{" +
                       "fullName='" + fullName + '\'' +
                       ", jobTitle='" + jobTitle + '\'' +
                       ", title='" + title + '\'' +
                       ", initials='" + initials + '\'' +
                       ", givenNames='" + givenNames + '\'' +
                       ", surname='" + surname + '\'' +
                       ", employeeNumber='" + employeeNumber + '\'' +
                       ", employeeType='" + employeeType + '\'' +
                       ", preferredLanguage='" + preferredLanguage + '\'' +
                       ", isPublic=" + isPublic +
                       ", knownName='" + getKnownName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        PersonalProfileProperties that = (PersonalProfileProperties) objectToCompare;
        return isPublic == that.isPublic &&
                       Objects.equals(fullName, that.fullName) &&
                       Objects.equals(jobTitle, that.jobTitle) &&
                       Objects.equals(title, that.title) &&
                       Objects.equals(initials, that.initials) &&
                       Objects.equals(givenNames, that.givenNames) &&
                       Objects.equals(surname, that.surname) &&
                       Objects.equals(employeeNumber, that.employeeNumber) &&
                       Objects.equals(employeeType, that.employeeType) &&
                       Objects.equals(preferredLanguage, that.preferredLanguage);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fullName, jobTitle, title, initials, givenNames, surname, employeeNumber, employeeType,
                            preferredLanguage, isPublic);
    }
}
