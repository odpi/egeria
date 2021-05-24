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
 * personal profile is stored as an Person entity in the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileProperties extends ActorProfileProperties
{
    private static final long    serialVersionUID = 1L;

    private String fullName = null;
    private String jobTitle = null;


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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalProfileProperties{" +
                       "knownName='" + getKnownName() + '\'' +
                       ", fullName='" + fullName + '\'' +
                       ", jobTitle='" + jobTitle + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        PersonalProfileProperties that = (PersonalProfileProperties) objectToCompare;
        return Objects.equals(fullName, that.fullName) &&
                       Objects.equals(jobTitle, that.jobTitle);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fullName, jobTitle);
    }
}
