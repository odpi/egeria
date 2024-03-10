/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.myprofile.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalProfileUniverse contains the properties and header for a personal profile
 * retrieved from the metadata repository along with details of the contribution record, user ids, contact methods, peers and
 * roles that the profile is linked to.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PersonalProfileUniverse extends PersonalProfileElement
{
    private ContributionRecordElement    contributionRecord = null;
    private List<ProfileIdentityElement> userIdentities     = null;
    private List<ContactMethodElement>   contactMethods     = null;
    private List<ElementStub>            peers = null;
    private List<PersonRoleElement>      roles = null;

    /**
     * Default constructor
     */
    public PersonalProfileUniverse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileUniverse(PersonalProfileUniverse template)
    {
        super (template);

        if (template != null)
        {
            contributionRecord = template.getContributionRecord();
            userIdentities = template.getUserIdentities();
            contactMethods = template.getContactMethods();
            peers = template.getPeers();
            roles = template.getRoles();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalProfileUniverse(PersonalProfileElement template)
    {
        super (template);
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
    public List<ProfileIdentityElement> getUserIdentities()
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
    public void setUserIdentities(List<ProfileIdentityElement> userIdentities)
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
     * @return list of stubs for linked elements
     */
    public List<ElementStub> getPeers()
    {
        return peers;
    }


    /**
     * Set up the list of profile identifiers (GUIDs) for peers.
     *
     * @param peers list of stubs for linked elements
     */
    public void setPeers(List<ElementStub> peers)
    {
        this.peers = peers;
    }


    /**
     * Return the list of identifiers for this profile's roles.
     *
     * @return list of stubs for linked elements
     */
    public List<PersonRoleElement> getRoles()
    {
        return roles;
    }


    /**
     * Set up the list of identifiers for this profile's roles.
     *
     * @param roles list of stubs for linked elements
     */
    public void setRoles(List<PersonRoleElement> roles)
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
        return "PersonalProfileUniverse{" +
                       "elementHeader=" + getElementHeader() +
                       ", personalProfileProperties=" + getProfileProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        PersonalProfileUniverse that = (PersonalProfileUniverse) objectToCompare;
        return Objects.equals(contributionRecord, that.contributionRecord) &&
                       Objects.equals(userIdentities, that.userIdentities) &&
                       Objects.equals(contactMethods, that.contactMethods) &&
                       Objects.equals(peers, that.peers) &&
                       Objects.equals(roles, that.roles);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), contributionRecord, userIdentities, contactMethods, peers, roles);
    }
}
