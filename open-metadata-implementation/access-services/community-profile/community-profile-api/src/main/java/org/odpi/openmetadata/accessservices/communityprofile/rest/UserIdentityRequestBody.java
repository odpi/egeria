/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UserIdentityRequestBody provides the request body payload for working on UserIdentity entities and their
 * link to profiles.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserIdentityRequestBody extends CommunityProfileOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String qualifiedName = null;


    /**
     * Default constructor
     */
    public UserIdentityRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UserIdentityRequestBody(UserIdentityRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.qualifiedName = template.getQualifiedName();
        }
    }


    /**
     * Return the the unique employee number for this governance officer.
     *
     * @return String identifier
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique employee number for this governance officer.
     *
     * @param qualifiedName String identifier
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UserIdentityRequestBody{" +
                       "qualifiedName='" + qualifiedName + '\'' +
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
        UserIdentityRequestBody that = (UserIdentityRequestBody) objectToCompare;
        return Objects.equals(getQualifiedName(), that.getQualifiedName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getQualifiedName());
    }
}
