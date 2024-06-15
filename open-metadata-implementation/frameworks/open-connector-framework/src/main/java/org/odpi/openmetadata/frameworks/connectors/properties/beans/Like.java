/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Like properties object records a single user's "like" of an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Like extends ElementBase
{
    protected String     user       = null;
    protected boolean    isPublic   = false;


    /**
     * Default constructor
     */
    public Like()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template   element to copy
     */
    public Like(Like template)
    {
        super(template);

        if (template != null)
        {
            user = template.getUser();
            isPublic = template.isPublic;
        }
    }


    /**
     * Return the user id of the person who created the like.  Null means the user id is not known.
     *
     * @return String liking user
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the like.  Null means the user id is not known.
     *
     * @param user String liking user
     */
    public void setUser(String user)
    {
        this.user = user;
    }


    /**
     * Return if this like is private to the creating user.
     *
     * @return boolean
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up whether the like is private to the creating user or not.
     *
     * @param aPublic boolean
     */
    public void setIsPublic(boolean aPublic)
    {
        isPublic = aPublic;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Like{" +
                       "extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", user='" + user + '\'' +
                       ", isPublic=" + isPublic +
                       ", headerVersion=" + getHeaderVersion() +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
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
        Like like = (Like) objectToCompare;
        return isPublic == like.isPublic &&
                       Objects.equals(getUser(), like.getUser());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUser(), isPublic);
    }
}