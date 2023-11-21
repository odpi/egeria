/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The PersonalProfileProperties describes an individual.  Information about the
 * personal profile is stored as a Person entity in the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileProperties extends ActorProfileProperties
{
    private static final long    serialVersionUID = 1L;

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
    private String residentCountry   = null;
    private String timeZone          = null;
    private boolean isPublic         = true;


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
            this.title = template.getTitle();
            this.initials = template.getInitials();
            this.givenNames = template.getGivenNames();
            this.surname = template.getSurname();
            this.fullName = template.getFullName();
            this.pronouns = template.getPronouns();
            this.jobTitle = template.getJobTitle();
            this.employeeNumber = template.getEmployeeNumber();
            this.employeeType = template.getEmployeeType();
            this.preferredLanguage = template.getPreferredLanguage();
            this.residentCountry = template.getResidentCountry();
            this.timeZone = template.getTimeZone();
            this.isPublic = template.getIsPublic();
        }
    }


    /**
     * Return the courtesy title.
     *
     * @return string
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the courtesy title.
     *
     * @param title string
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return the person's initials (first letter of each given name).
     *
     * @return string
     */
    public String getInitials()
    {
        return initials;
    }


    /**
     * Set up the person's initials (first letter of each given name).
     *
     * @param initials string
     */
    public void setInitials(String initials)
    {
        this.initials = initials;
    }


    /**
     * Return the list of given names.
     *
     * @return string
     */
    public String getGivenNames()
    {
        return givenNames;
    }


    /**
     * Set up the list of given names.
     *
     * @param givenNames string
     */
    public void setGivenNames(String givenNames)
    {
        this.givenNames = givenNames;
    }


    /**
     * Return the last, or family name of the person.
     *
     * @return string
     */
    public String getSurname()
    {
        return surname;
    }


    /**
     * Set up the last, or family name of the person.
     *
     * @param surname string
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
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
     * Return the person's pronouns preference.
     *
     * @return string
     */
    public String getPronouns()
    {
        return pronouns;
    }


    /**
     * Set up the person's pronouns preference.
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
     * Return the person's employee number (aka personnel number, serial number).
     *
     * @return string
     */
    public String getEmployeeNumber()
    {
        return employeeNumber;
    }


    /**
     * Set up the person's employee number (aka personnel number, serial number).
     *
     * @param employeeNumber string
     */
    public void setEmployeeNumber(String employeeNumber)
    {
        this.employeeNumber = employeeNumber;
    }


    /**
     * Return the type of employee contract such as full-time, part-time, that the person holds.
     *
     * @return string
     */
    public String getEmployeeType()
    {
        return employeeType;
    }


    /**
     * Set up the type of employee contract such as full-time, part-time, that the person holds.
     *
     * @param employeeType string
     */
    public void setEmployeeType(String employeeType)
    {
        this.employeeType = employeeType;
    }


    /**
     * Return the person's preferred speaking/written language for communicating with them.
     *
     * @return string
     */
    public String getPreferredLanguage()
    {
        return preferredLanguage;
    }


    /**
     * Set up the person's preferred speaking/written language for communicating with them.
     *
     * @param preferredLanguage string
     */
    public void setPreferredLanguage(String preferredLanguage)
    {
        this.preferredLanguage = preferredLanguage;
    }


    /**
     * Return the name of the country that is the person's primary residence.
     *
     * @return string
     */
    public String getResidentCountry()
    {
        return residentCountry;
    }


    /**
     * Set up the name of the country that is the person's primary residence.
     *
     * @param residentCountry string
     */
    public void setResidentCountry(String residentCountry)
    {
        this.residentCountry = residentCountry;
    }


    /**
     * Return the time zone that the person is located in.
     *
     * @return string
     */
    public String getTimeZone()
    {
        return timeZone;
    }


    /**
     * Set up the time zone that the person is located in.
     *
     * @param timeZone string
     */
    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }


    /**
     * Return the flag to indicate whether the profile is public or not.
     *
     * @return boolean flag
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up the flag to indicate whether the profile is public or not.
     *
     * @param aPublic boolean flag
     */
    public void setIsPublic(boolean aPublic)
    {
        isPublic = aPublic;
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
                       "title='" + title + '\'' +
                       ", initials='" + initials + '\'' +
                       ", givenName='" + givenNames + '\'' +
                       ", surname='" + surname + '\'' +
                       ", fullName='" + fullName + '\'' +
                       ", pronouns='" + pronouns + '\'' +
                       ", jobTitle='" + jobTitle + '\'' +
                       ", employeeNumber='" + employeeNumber + '\'' +
                       ", employeeType='" + employeeType + '\'' +
                       ", preferredLanguage='" + preferredLanguage + '\'' +
                       ", residentCountry='" + residentCountry + '\'' +
                       ", timeZone='" + timeZone + '\'' +
                       ", isPublic=" + isPublic +
                       ", knownName='" + getKnownName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        if (! (objectToCompare instanceof PersonalProfileProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return isPublic == that.isPublic &&
                       Objects.equals(title, that.title) &&
                       Objects.equals(initials, that.initials) &&
                       Objects.equals(givenNames, that.givenNames) &&
                       Objects.equals(surname, that.surname) &&
                       Objects.equals(fullName, that.fullName) &&
                       Objects.equals(pronouns, that.pronouns) &&
                       Objects.equals(jobTitle, that.jobTitle) &&
                       Objects.equals(employeeNumber, that.employeeNumber) &&
                       Objects.equals(employeeType, that.employeeType) &&
                       Objects.equals(preferredLanguage, that.preferredLanguage) &&
                       Objects.equals(residentCountry, that.residentCountry) &&
                       Objects.equals(timeZone, that.timeZone);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), title, initials, givenNames, surname, fullName, pronouns, jobTitle, employeeNumber, employeeType,
                            preferredLanguage, residentCountry, timeZone, isPublic);
    }
}
