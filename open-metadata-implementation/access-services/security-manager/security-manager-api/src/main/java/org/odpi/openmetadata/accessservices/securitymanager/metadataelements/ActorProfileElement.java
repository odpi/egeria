/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.securitymanager.properties.ActorProfileProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;


/**
 * The ActorProfileElement describes an individual, system, team or organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActorProfileElement implements Serializable, MetadataElement
{
    private static final long          serialVersionUID = 1L;

    private ElementHeader                elementHeader        = null;
    private ActorProfileProperties       profileProperties    = null;
    private List<ContactMethodElement>   contactMethods       = null;
    private List<ElementStub>            personRoles          = null; /* Person only */
    private List<ProfileLocationElement> locations            = null;
    private List<ProfileIdentityElement> userIdentities       = null;
    private ElementStub                  superTeam            = null; /* Team only */
    private List<ElementStub>            subTeams             = null; /* Team only */
    private List<ElementStub>            teamLeaderRoles      = null; /* Team only */
    private List<ElementStub>            teamMemberRoles      = null; /* Team only */
    private List<ElementStub>            linkedInfrastructure = null; /* ITProfile only */


    /**
     * Default Constructor
     */
    public ActorProfileElement()
    {
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public ActorProfileElement(ActorProfileElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            profileProperties = template.getProfileProperties();
            contactMethods = template.getContactMethods();
            personRoles = template.getPersonRoles();
            locations = template.getLocations();
            userIdentities = template.getUserIdentities();
            superTeam = template.getSuperTeam();
            subTeams = template.getSubTeams();
            teamLeaderRoles = template.getTeamLeaderRoles();
            teamMemberRoles = template.getTeamMemberRoles();
            linkedInfrastructure = template.getLinkedInfrastructure();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
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
     * Return the contact methods for this profile.
     *
     * @return list of contact methods
     */
    public List<ContactMethodElement> getContactMethods()
    {
        return contactMethods;
    }


    /**
     * Set up the contact methods for this profile.
     *
     * @param contactMethods list of contact methods
     */
    public void setContactMethods(List<ContactMethodElement> contactMethods)
    {
        this.contactMethods = contactMethods;
    }


    /**
     * Return the headers of the person roles that this actor is appointed to.
     *
     * @return role stubs
     */
    public List<ElementStub> getPersonRoles()
    {
        return personRoles;
    }


    /**
     * Set up the headers of the person roles that this actor is appointed to.
     *
     * @param personRoles role stubs
     */
    public void setPersonRoles(List<ElementStub> personRoles)
    {
        this.personRoles = personRoles;
    }



    /**
     * Return the headers of the location that this actor is linked to.
     *
     * @return location stubs
     */
    public List<ProfileLocationElement> getLocations()
    {
        return locations;
    }


    /**
     * Set up the headers of the person roles that this actor is linked to.
     *
     * @param locations location stubs
     */
    public void setLocations(List<ProfileLocationElement> locations)
    {
        this.locations = locations;
    }


    /**
     * Return the list of user identities for this profile.
     *
     * @return list of userIds
     */
    public List<ProfileIdentityElement> getUserIdentities()
    {
        return userIdentities;
    }


    /**
     * Set up the list of user identities for this profile.
     *
     * @param userIdentities list of userIds
     */
    public void setUserIdentities(List<ProfileIdentityElement> userIdentities)
    {
        this.userIdentities = userIdentities;
    }


    /**
     * Return a summary of the team that is above this team in the organizational hierarchy.
     *
     * @return team stub
     */
    public ElementStub getSuperTeam()
    {
        return superTeam;
    }


    /**
     * Set up a summary of the team that is above this team in the organizational hierarchy.
     *
     * @param superTeam  team stub
     */
    public void setSuperTeam(ElementStub superTeam)
    {
        this.superTeam = superTeam;
    }


    /**
     * Return the list of team that report to this team.
     *
     * @return list of team stubs
     */
    public List<ElementStub> getSubTeams()
    {
        return subTeams;
    }


    /**
     * Set up the list of team that report to this team.
     *
     * @param subTeams list of team stubs
     */
    public void setSubTeams(List<ElementStub> subTeams)
    {
        this.subTeams = subTeams;
    }


    /**
     * Return the list of leader roles assigned to this team.
     *
     * @return list of role stubs
     */
    public List<ElementStub> getTeamLeaderRoles()
    {
        return teamLeaderRoles;
    }


    /**
     * Set up the list of leader roles assigned to this team.
     *
     * @param teamLeaderRoles list of role stubs
     */
    public void setTeamLeaderRoles(List<ElementStub> teamLeaderRoles)
    {
        this.teamLeaderRoles = teamLeaderRoles;
    }


    /**
     * Return the list of member roles assigned to this team.
     *
     * @return list of role stubs
     */
    public List<ElementStub> getTeamMemberRoles()
    {
        return teamMemberRoles;
    }


    /**
     * Set up the list of member roles assigned to this team.
     *
     * @param teamMemberRoles list of role stubs
     */
    public void setTeamMemberRoles(List<ElementStub> teamMemberRoles)
    {
        this.teamMemberRoles = teamMemberRoles;
    }


    /**
     * Return the stubs of the pieces of IT infrastructure linked to the profile.
     *
     * @return list of element stubs
     */
    public List<ElementStub> getLinkedInfrastructure()
    {
        return linkedInfrastructure;
    }


    /**
     * Set up the stubs of the pieces of IT infrastructure linked to the profile.
     *
     * @param linkedInfrastructure list of element stubs
     */
    public void setLinkedInfrastructure(List<ElementStub> linkedInfrastructure)
    {
        this.linkedInfrastructure = linkedInfrastructure;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActorProfileElement{" +
                       "elementHeader=" + elementHeader +
                       ", profileProperties=" + profileProperties +
                       ", contactMethods=" + contactMethods +
                       ", personRoles=" + personRoles +
                       ", locations=" + locations +
                       ", userIdentities=" + userIdentities +
                       ", superTeam=" + superTeam +
                       ", subTeams=" + subTeams +
                       ", teamLeaderRoles=" + teamLeaderRoles +
                       ", teamMemberRoles=" + teamMemberRoles +
                       ", linkedInfrastructure=" + linkedInfrastructure +
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
        ActorProfileElement that = (ActorProfileElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(profileProperties, that.profileProperties) &&
                       Objects.equals(contactMethods, that.contactMethods) &&
                       Objects.equals(personRoles, that.personRoles) &&
                       Objects.equals(locations, that.locations) &&
                       Objects.equals(userIdentities, that.userIdentities) &&
                       Objects.equals(superTeam, that.superTeam) &&
                       Objects.equals(subTeams, that.subTeams) &&
                       Objects.equals(teamLeaderRoles, that.teamLeaderRoles) &&
                       Objects.equals(teamMemberRoles, that.teamMemberRoles) &&
                       Objects.equals(linkedInfrastructure, that.linkedInfrastructure);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, profileProperties, contactMethods, userIdentities, superTeam, subTeams,
                            teamLeaderRoles, personRoles, locations, teamMemberRoles, linkedInfrastructure);
    }
}
