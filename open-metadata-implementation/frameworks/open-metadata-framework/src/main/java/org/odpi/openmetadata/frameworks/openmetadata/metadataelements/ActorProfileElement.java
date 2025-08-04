/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The ActorProfileElement describes an individual, system, team or organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActorProfileElement extends OpenMetadataRootElement
{
    private RelatedMetadataElementSummary       contributionRecord   = null;
    private List<RelatedMetadataElementSummary> actorRoles           = null;
    private List<RelatedMetadataElementSummary> peers                = null; /* Person only */
    private List<RelatedMetadataElementSummary> locations            = null;
    private List<RelatedMetadataElementSummary> userIdentities       = null;
    private RelatedMetadataElementSummary       superTeam            = null; /* Team only */
    private List<RelatedMetadataElementSummary> subTeams             = null; /* Team only */
    private List<RelatedMetadataElementSummary> businessCapabilities = null; /* Team only */
    private List<RelatedMetadataElementSummary> linkedInfrastructure = null; /* ITProfile only */


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
    public ActorProfileElement(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public ActorProfileElement(ActorProfileElement template)
    {
        super(template);

        if (template != null)
        {
            contributionRecord   = template.getContributionRecord();
            actorRoles           = template.getActorRoles();
            peers                = template.getPeers();
            locations            = template.getLocations();
            userIdentities       = template.getUserIdentities();
            superTeam            = template.getSuperTeam();
            subTeams             = template.getSubTeams();
            businessCapabilities = template.getBusinessCapabilities();
            linkedInfrastructure = template.getLinkedInfrastructure();
        }
    }

    /**
     * Return the contribution record (only if this profile is for a person).
     *
     * @return contribution record
     */
    public RelatedMetadataElementSummary getContributionRecord()
    {
        return contributionRecord;
    }


    /**
     * Set up the contribution record (only if this profile is for a person).
     *
     * @param contributionRecord contribution record
     */
    public void setContributionRecord(RelatedMetadataElementSummary contributionRecord)
    {
        this.contributionRecord = contributionRecord;
    }


    /**
     * Return the headers of the person roles that this actor is appointed to.
     *
     * @return role stubs
     */
    public List<RelatedMetadataElementSummary> getActorRoles()
    {
        return actorRoles;
    }


    /**
     * Set up the headers of the person roles that this actor is appointed to.
     *
     * @param actorRoles role stubs
     */
    public void setActorRoles(List<RelatedMetadataElementSummary> actorRoles)
    {
        this.actorRoles = actorRoles;
    }


    /**
     * Return the list of profile identifiers (GUIDs) for peers.
     *
     * @return list of stubs for linked elements
     */
    public List<RelatedMetadataElementSummary> getPeers()
    {
        return peers;
    }


    /**
     * Set up the list of profile identifiers (GUIDs) for peers.
     *
     * @param peerGovernanceDefinitions list of stubs for linked elements
     */
    public void setPeers(List<RelatedMetadataElementSummary> peerGovernanceDefinitions)
    {
        this.peers = peerGovernanceDefinitions;
    }


    /**
     * Return the headers of the location that this actor is linked to.
     *
     * @return location stubs
     */
    public List<RelatedMetadataElementSummary> getLocations()
    {
        return locations;
    }


    /**
     * Set up the headers of the person roles that this actor is linked to.
     *
     * @param locations location stubs
     */
    public void setLocations(List<RelatedMetadataElementSummary> locations)
    {
        this.locations = locations;
    }


    /**
     * Return the list of user identities for this profile.
     *
     * @return list of userIds
     */
    public List<RelatedMetadataElementSummary> getUserIdentities()
    {
        return userIdentities;
    }


    /**
     * Set up the list of user identities for this profile.
     *
     * @param userIdentities list of userIds
     */
    public void setUserIdentities(List<RelatedMetadataElementSummary> userIdentities)
    {
        this.userIdentities = userIdentities;
    }


    /**
     * Return a summary of the team that is above this team in the organizational hierarchy.
     *
     * @return team stub
     */
    public RelatedMetadataElementSummary getSuperTeam()
    {
        return superTeam;
    }


    /**
     * Set up a summary of the team that is above this team in the organizational hierarchy.
     *
     * @param superTeam  team stub
     */
    public void setSuperTeam(RelatedMetadataElementSummary superTeam)
    {
        this.superTeam = superTeam;
    }


    /**
     * Return the list of team that report to this team.
     *
     * @return list of team stubs
     */
    public List<RelatedMetadataElementSummary> getSubTeams()
    {
        return subTeams;
    }


    /**
     * Set up the list of team that report to this team.
     *
     * @param subTeams list of team stubs
     */
    public void setSubTeams(List<RelatedMetadataElementSummary> subTeams)
    {
        this.subTeams = subTeams;
    }


    /**
     * Return the business capability linked via the organizational capability relationship.
     *
     * @return list of capabilities
     */
    public List<RelatedMetadataElementSummary> getBusinessCapabilities()
    {
        return businessCapabilities;
    }


    /**
     * Set up the business capability linked via the organizational capability relationship.
     *
     * @param businessCapabilities list of capabilities
     */
    public void setBusinessCapabilities(List<RelatedMetadataElementSummary> businessCapabilities)
    {
        this.businessCapabilities = businessCapabilities;
    }


    /**
     * Return the stubs of the pieces of IT infrastructure linked to the profile.
     *
     * @return list of element stubs
     */
    public List<RelatedMetadataElementSummary> getLinkedInfrastructure()
    {
        return linkedInfrastructure;
    }


    /**
     * Set up the stubs of the pieces of IT infrastructure linked to the profile.
     *
     * @param linkedInfrastructure list of element stubs
     */
    public void setLinkedInfrastructure(List<RelatedMetadataElementSummary> linkedInfrastructure)
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
                "contributionRecord=" + contributionRecord +
                ", actorRoles=" + actorRoles +
                ", peers=" + peers +
                ", locations=" + locations +
                ", userIdentities=" + userIdentities +
                ", superTeam=" + superTeam +
                ", subTeams=" + subTeams +
                ", businessCapabilities=" + businessCapabilities +
                ", linkedInfrastructure=" + linkedInfrastructure +
                "} " + super.toString();
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
        return Objects.equals(contributionRecord, that.contributionRecord) &&
                       Objects.equals(actorRoles, that.actorRoles) &&
                       Objects.equals(peers, that.peers) &&
                       Objects.equals(locations, that.locations) &&
                       Objects.equals(userIdentities, that.userIdentities) &&
                       Objects.equals(superTeam, that.superTeam) &&
                       Objects.equals(subTeams, that.subTeams) &&
                       Objects.equals(businessCapabilities, that.businessCapabilities) &&
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
        return Objects.hash(super.hashCode(), contributionRecord, actorRoles, peers, locations, userIdentities, superTeam, subTeams, businessCapabilities, linkedInfrastructure);
    }
}
