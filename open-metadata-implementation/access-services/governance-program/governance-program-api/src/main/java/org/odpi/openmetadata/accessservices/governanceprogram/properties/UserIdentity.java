/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UserIdentity describes one of an individual's or engine's user identity.  The default model has user identity
 * as a simple userId but this could be extended with subtypes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserIdentity implements Serializable
{
    private static final long          serialVersionUID = 1L;

    private String          guid      = null;
    private String          type      = null;
    private String          userId    = null;


    /**
     * Default constructor
     */
    public UserIdentity()
    {
    }


    /**
     * Copy/clone constructor
     */
    public UserIdentity(UserIdentity template)
    {
        if (template != null)
        {
            this.guid = template.getGUID();
            this.type = template.getType();
            this.userId = template.getUserId();
        }
    }


    /**
     * Return the unique identifier for this definition.  This value is assigned by the metadata collection
     * when the governance definition is created.
     *
     * @return String guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this definition.  This value is assigned by the metadata collection
     * when the governance definition is created.
     *
     * @param guid String guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name of the specific type of governance definition.
     *
     * @return String type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the name of the specific type of the governance definition.
     *
     * @param type String type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the unique name of the user identity for the individual.
     *
     * @return userId string
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the unique name of the user identity for the individual.
     *
     * @param userId string
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UserIdentity{" +
                "guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                ", userId=" + userId + '\'' +
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
        UserIdentity that = (UserIdentity) objectToCompare;
        return  Objects.equals(getGUID(), that.getGUID()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getUserId(), that.getUserId());
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getGUID(), getType(), getUserId());
    }
}
