/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.PersonalProfileUniverse;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PersonalProfileResponse is the response structure used on the OMAS REST API calls that return a
 * PersonalProfileUniverse object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileResponse extends FFDCResponseBase
{
    private PersonalProfileUniverse personalProfile = null;


    /**
     * Default constructor
     */
    public PersonalProfileResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileResponse(PersonalProfileResponse template)
    {
        super(template);

        if (template != null)
        {
            this.personalProfile = template.getPersonalProfile();
        }
    }


    /**
     * Return the personalProfile result.
     *
     * @return details of individual
     */
    public PersonalProfileUniverse getPersonalProfile()
    {
        return personalProfile;
    }


    /**
     * Set up the personalProfile result.
     *
     * @param personalProfile details of individual
     */
    public void setPersonalProfile(PersonalProfileUniverse personalProfile)
    {
        this.personalProfile = personalProfile;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonalProfileResponse{" +
                "personalProfile=" + personalProfile +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof PersonalProfileResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        PersonalProfileResponse that = (PersonalProfileResponse) objectToCompare;
        return Objects.equals(personalProfile, that.personalProfile);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), personalProfile);
    }
}
