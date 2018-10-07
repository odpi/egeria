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
public class Like extends ElementHeader
{
    /*
     * Attributes of a Like
     */
    protected String user = null;


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
     * @param templateLike   element to copy
     */
    public Like(Like templateLike)
    {
        super(templateLike);

        if (templateLike != null)
        {
            user = templateLike.getUser();
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
     * Set up he user id of the person who created the like.  Null means the user id is not known.
     *
     * @param user String liking user
     */
    public void setUser(String user)
    {
        this.user = user;
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
                "user='" + user + '\'' +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        if (!(objectToCompare instanceof Like))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Like like = (Like) objectToCompare;
        return Objects.equals(getUser(), like.getUser());
    }
}