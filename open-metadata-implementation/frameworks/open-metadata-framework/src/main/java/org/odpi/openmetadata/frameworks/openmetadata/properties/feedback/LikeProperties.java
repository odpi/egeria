/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.feedback;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The LikeProperties object records a single user's "like" of an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LikeProperties
{
    private String     user       = null;


    /**
     * Default constructor
     */
    public LikeProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template   element to copy
     */
    public LikeProperties(LikeProperties template)
    {
        if (template != null)
        {
            user = template.getUser();
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LikeProperties{" +
                       "user='" + user + '\'' +
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
        LikeProperties like = (LikeProperties) objectToCompare;
        return Objects.equals(getUser(), like.getUser());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(user);
    }
}