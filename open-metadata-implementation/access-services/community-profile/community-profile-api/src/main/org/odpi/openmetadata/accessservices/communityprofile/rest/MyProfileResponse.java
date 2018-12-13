/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * MyProfileResponse is the response structure used on the OMAS REST API calls that return a
 * PersonalProfile object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MyProfileResponse extends CommunityProfileOMASAPIResponse
{
    private PersonalProfile personalProfile = null;


    /**
     * Default constructor
     */
    public MyProfileResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MyProfileResponse(MyProfileResponse template)
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
    public PersonalProfile getPersonalProfile()
    {
        return personalProfile;
    }


    /**
     * Set up the personalProfile result.
     *
     * @param personalProfile details of individual
     */
    public void setPersonalProfile(PersonalProfile personalProfile)
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
        return "MyProfileResponse{" +
                "personalProfiles='" + getPersonalProfile() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
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
        if (!(objectToCompare instanceof MyProfileResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MyProfileResponse that = (MyProfileResponse) objectToCompare;
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
