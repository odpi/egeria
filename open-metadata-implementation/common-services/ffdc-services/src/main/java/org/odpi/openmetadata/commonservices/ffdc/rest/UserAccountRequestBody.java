/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UserAccountRequestBody passes information to set up a user account with the security connector to protect
 * requests to the platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserAccountRequestBody
{
    private OpenMetadataUserAccount userAccount = null;


    /**
     * Default constructor
     */
    public UserAccountRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template source
     */
    public UserAccountRequestBody(UserAccountRequestBody template)
    {
        if (template != null)
        {
            userAccount = template.getUserAccount();
        }
    }


    /**
     * Return the user account.
     *
     * @return user account
     */
    public OpenMetadataUserAccount getUserAccount()
    {
        return userAccount;
    }


    /**
     * Set up the user account.
     *
     * @param userAccount user account
     */
    public void setUserAccount(OpenMetadataUserAccount userAccount)
    {
        this.userAccount = userAccount;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "UserAccountRequestBody{" +
                "userAccount=" + userAccount +
                "}";
    }


    /**
     * Compare objects
     *
     * @param objectToCompare object
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
        UserAccountRequestBody that = (UserAccountRequestBody) objectToCompare;
        return Objects.equals(getUserAccount(), that.getUserAccount());
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getUserAccount());
    }
}
