/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProfileIdentityElement contains the properties and header for a relationship between a profile and a user identity retrieved
 * from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileIdentityElement
{
    private ProfileIdentityProperties profileIdentity = null;
    private UserIdentityElement       userIdentity    = null;


    /**
     * Default constructor
     */
    public ProfileIdentityElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProfileIdentityElement(ProfileIdentityElement template)
    {
        if (template != null)
        {
            profileIdentity = template.getProfileIdentity();
            userIdentity    = template.getUserIdentity();
        }
    }


    /**
     * Return the properties from the profile identity relationship.
     *
     * @return profile identity
     */
    public ProfileIdentityProperties getProfileIdentity()
    {
        return profileIdentity;
    }


    /**
     * Set up the properties from the profile identity relationship.
     *
     * @param profileIdentity profile identity
     */
    public void setProfileIdentity(ProfileIdentityProperties profileIdentity)
    {
        this.profileIdentity = profileIdentity;
    }


    /**
     * Return the properties of the userId.
     *
     * @return properties
     */
    public UserIdentityElement getUserIdentity()
    {
        return userIdentity;
    }


    /**
     * Set up the userId properties.
     *
     * @param userIdentity  properties
     */
    public void setUserIdentity(UserIdentityElement userIdentity)
    {
        this.userIdentity = userIdentity;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProfileIdentityElement{" +
                       "profileIdentity=" + profileIdentity +
                       ", properties=" + userIdentity +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ProfileIdentityElement that = (ProfileIdentityElement) objectToCompare;
        return Objects.equals(profileIdentity, that.profileIdentity) &&
                       Objects.equals(userIdentity, that.userIdentity);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), profileIdentity, userIdentity);
    }
}
