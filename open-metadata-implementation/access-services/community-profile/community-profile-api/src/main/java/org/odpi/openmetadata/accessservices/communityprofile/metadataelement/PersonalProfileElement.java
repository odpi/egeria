/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.metadataelement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfileProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalProfileElement contains the properties and header for a personal profile
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader              elementHeader             = null;
    private PersonalProfileProperties  personalProfileProperties = null;
    private ContributionRecordElement  contributionRecord        = null;
    private List<UserIdentityElement>  userIdentities            = null;
    private List<ContactMethodElement> contactMethods            = null;
    private List<String>               peers                     = null;
    private List<String>               roles                     = null;

    /**
     * Default constructor
     */
    public PersonalProfileElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileElement(PersonalProfileElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            personalProfileProperties = template.getPersonalProfileProperties();
            contributionRecord = template.getContributionRecord();
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
    public PersonalProfileProperties getPersonalProfileProperties()
    {
        return personalProfileProperties;
    }


    /**
     * Set up the personal profile properties.
     *
     * @param personalProfileProperties  properties
     */
    public void setPersonalProfileProperties(PersonalProfileProperties personalProfileProperties)
    {
        this.personalProfileProperties = personalProfileProperties;
    }


    /**
     * Return the recognition of the contribution that this user has made to open metadata.
     *
     * @return contribution record
     */
    public ContributionRecordElement getContributionRecord()
    {
        return contributionRecord;
    }


    /**
     * Set up the recognition of the contribution that this user has made to open metadata.
     *
     * @param contributionRecord contribution record
     */
    public void setContributionRecord(ContributionRecordElement contributionRecord)
    {
        this.contributionRecord = contributionRecord;
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
     * Return the list of profile identifiers (GUIDs) for peers.
     *
     * @return list of guids
     */
    public List<String> getPeers()
    {
        return peers;
    }


    /**
     * Set up the list of profile identifiers (GUIDs) for peers.
     *
     * @param peers list of guids
     */
    public void setPeers(List<String> peers)
    {
        this.peers = peers;
    }


    /**
     * Return the list of identifiers for this profile's roles.
     *
     * @return list of guids
     */
    public List<String> getRoles()
    {
        return roles;
    }


    /**
     * Set up the list of identifiers for this profile's roles.
     *
     * @param roles list of guids
     */
    public void setRoles(List<String> roles)
    {
        this.roles = roles;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PersonalProfileElement{" +
                       "elementHeader=" + elementHeader +
                       ", personalProfileProperties=" + personalProfileProperties +
                       ", contributionRecord=" + contributionRecord +
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
        PersonalProfileElement that = (PersonalProfileElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(personalProfileProperties, that.personalProfileProperties) &&
                       Objects.equals(contributionRecord, that.contributionRecord) &&
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
        return Objects.hash(elementHeader, personalProfileProperties, contributionRecord, userIdentities, contactMethods);
    }
}
