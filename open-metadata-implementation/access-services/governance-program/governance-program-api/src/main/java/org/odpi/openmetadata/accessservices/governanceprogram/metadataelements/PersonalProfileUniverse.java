/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfileProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The PersonalProfileDefinition extends PersonalProfileElement to include information about the related userId and contact information
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileUniverse extends PersonalProfileElement
{
    private static final long          serialVersionUID = 1L;

    private List<UserIdentityElement>  userIdentities    = null;
    private List<ContactMethodElement> contactMethods    = null;


    /**
     * Default Constructor
     */
    public PersonalProfileUniverse()
    {
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public PersonalProfileUniverse(PersonalProfileUniverse template)
    {
        if (template != null)
        {
            userIdentities = template.getUserIdentities();
            contactMethods = template.getContactMethods();
        }
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
        return "PersonalProfileUniverse{" +
                       "userIdentities=" + userIdentities +
                       ", contactMethods=" + contactMethods +
                       ", elementHeader=" + getElementHeader() +
                       ", profileProperties=" + getProfileProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        PersonalProfileUniverse that = (PersonalProfileUniverse) objectToCompare;
        return Objects.equals(userIdentities, that.userIdentities) &&
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
        return Objects.hash(super.hashCode(), userIdentities, contactMethods);
    }
}
