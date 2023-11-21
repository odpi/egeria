/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProfileElement contains the properties and header for a profile of a person, team, engine or organization
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileElement implements MetadataElement
{
    private ElementHeader              elementHeader      = null;
    private ActorProfileProperties     profileProperties  = null;
    private List<UserIdentityElement>  userIdentities     = null;
    private List<ContactMethodElement> contactMethods     = null;


    /**
     * Default constructor
     */
    public ProfileElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProfileElement(ProfileElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            profileProperties = template.getProfileProperties();
            userIdentities = template.getUserIdentities();
            contactMethods = template.getContactMethods();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the profile.
     *
     * @return  properties
     */
    public ActorProfileProperties getProfileProperties()
    {
        return profileProperties;
    }


    /**
     * Set up the profile properties.
     *
     * @param profileProperties  properties
     */
    public void setProfileProperties(ActorProfileProperties profileProperties)
    {
        this.profileProperties = profileProperties;
    }


    /**
     * Return the list of user identities associated with the personal profile.
     *
     * @return list or null
     */
    public List<UserIdentityElement> getUserIdentities()
    {
        if (userIdentities == null)
        {
            return null;
        }
        else if (userIdentities.isEmpty())
        {
            return null;
        }

        return userIdentities;
    }


    /**
     * Set up the list of user identities associated with the personal profile.
     *
     * @param userIdentities list or null
     */
    public void setUserIdentities(List<UserIdentityElement> userIdentities)
    {
        this.userIdentities = userIdentities;
    }


    /**
     * Return the list of contact methods for the individual.
     *
     * @return list or null
     */
    public List<ContactMethodElement> getContactMethods()
    {
        if (contactMethods == null)
        {
            return null;
        }

        if (contactMethods.isEmpty())
        {
            return null;
        }

        return contactMethods;
    }


    /**
     * Set up the list of contact methods for the individual.
     *
     * @param contactMethods list or null
     */
    public void setContactMethods(List<ContactMethodElement> contactMethods)
    {
        this.contactMethods = contactMethods;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProfileElement{" +
                       "elementHeader=" + elementHeader +
                       ", profileProperties=" + profileProperties +
                       ", userIdentities=" + userIdentities +
                       ", contactMethods=" + contactMethods +
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
        ProfileElement that = (ProfileElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(profileProperties, that.profileProperties) &&
                       Objects.equals(userIdentities, that.userIdentities) &&
                       Objects.equals(contactMethods, that.contactMethods);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, profileProperties, userIdentities, contactMethods);
    }
}
