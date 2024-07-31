/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalDetailsRequestBody provides a structure for passing personal details over a REST API.
 * It is used for creating PersonalProfiles.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileValidatorRequestBody
{
    private String originatingSystemGUID = null;
    private String originatingSystemName = null;

    /**
     * Default constructor
     */
    public PersonalProfileValidatorRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileValidatorRequestBody(PersonalProfileValidatorRequestBody template)
    {
        if (template != null)
        {
            this.originatingSystemGUID = template.getOriginatingSystemGUID();
            this.originatingSystemName = template.getOriginatingSystemName();
        }
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
     * Return the unique name for the originating system.
     *
     * @return string
     */
    public String getOriginatingSystemName()
    {
        return originatingSystemName;
    }


    /**
     * Set up the unique name for the originating system
     *
     * @param originatingSystemName string
     */
    public void setOriginatingSystemName(String originatingSystemName)
    {
        this.originatingSystemName = originatingSystemName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalProfileValidatorRequestBody{" +
                       "originatingSystemGUID='" + originatingSystemGUID + '\'' +
                       ", originatingSystemName='" + originatingSystemName + '\'' +
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
        PersonalProfileValidatorRequestBody that = (PersonalProfileValidatorRequestBody) objectToCompare;
        return Objects.equals(originatingSystemGUID, that.originatingSystemGUID) &&
                       Objects.equals(originatingSystemName, that.originatingSystemName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(originatingSystemGUID, originatingSystemName);
    }
}
