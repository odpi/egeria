/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalDetailsRequestBody provides a structure for passing personal details over a REST API.
 * It is used for creating and updating PersonalProfiles for other users.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileRequestBody extends MyProfileRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String profileUserId         = null;
    private String originatingSystemGUID = null;


    /**
     * Default constructor
     */
    public PersonalProfileRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileRequestBody(PersonalProfileRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.profileUserId = template.getProfileUserId();
            this.originatingSystemGUID = template.getOriginatingSystemGUID();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileRequestBody(MyProfileRequestBody template)
    {
        super(template);
    }


    /**
     * Return the anchoring userId for this profile.
     *
     * @return string
     */
    public String getProfileUserId()
    {
        return profileUserId;
    }


    /**
     * Set up the anchoring userId for this profile.
     *
     * @param profileUserId string
     */
    public void setProfileUserId(String profileUserId)
    {
        this.profileUserId = profileUserId;
    }


    /**
     * Return the unique identifier for the originating system.
     *
     * @return string
     */
    public String getOriginatingSystemGUID()
    {
        return originatingSystemGUID;
    }


    /**
     * Set up the unique identifier for the originating system
     *
     * @param originatingSystemGUID string
     */
    public void setOriginatingSystemGUID(String originatingSystemGUID)
    {
        this.originatingSystemGUID = originatingSystemGUID;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalProfileRequestBody{" +
                "profileUserId='" + profileUserId + '\'' +
                "originatingSystemGUID='" + originatingSystemGUID + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", knownName='" + getKnownName() + '\'' +
                ", jobTitle='" + getJobTitle() + '\'' +
                ", jobRoleDescription='" + getJobRoleDescription() + '\'' +
                ", profileProperties=" + getProfileProperties() +
                ", additionalProperties=" + getAdditionalProperties() +
                '}';
    }


    /**
     * Standard method.
     *
     * @param objectToCompare object to compare
     * @return boolean
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
        PersonalProfileRequestBody that = (PersonalProfileRequestBody) objectToCompare;
        return Objects.equals(getProfileUserId(), that.getProfileUserId()) &&
                       Objects.equals(getOriginatingSystemGUID(), that.getOriginatingSystemGUID());
    }


    /**
     * Standard method
     *
     * @return hashcode
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getProfileUserId(), getOriginatingSystemGUID());
    }
}
