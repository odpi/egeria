/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PeerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.newElement;

/**
 * PeopleOrganizerFVT covers every request of the People Organizer OMVS: linking two people as peers and two
 * teams into a team structure, and removing those links.  The people and teams are created and deleted with
 * Actor Manager.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PeopleOrganizerFVT
{
    private static final ViewServiceClient peopleOrganizer = new ViewServiceClient(ViewServiceDescription.PEOPLE_ORGANIZER);
    private static final ViewServiceClient actorManager    = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);


    /**
     * Two people become peers; the Peer relationship, with its label, is visible from both people and goes when
     * it is detached.
     */
    @Test
    @Order(1)
    void peerPeople()
    {
        String personOneGUID = person("grace");
        String personTwoGUID = person("alan");

        PeerProperties properties = new PeerProperties();

        properties.setLabel("colleague");
        properties.setDescription("Work together on the FVT");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        peopleOrganizer.call("/actor-profiles/" + personOneGUID + "/peer-persons/" + personTwoGUID + "/attach", link);

        OpenMetadataRootElement personOne = retrieve(personOneGUID);

        assertLinked(personOne, OpenMetadataType.PEER_RELATIONSHIP.typeName, personTwoGUID);
        assertLinked(retrieve(personTwoGUID), OpenMetadataType.PEER_RELATIONSHIP.typeName, personOneGUID);

        RelatedMetadataElementSummary peer = ActorGovernanceFvtSupport.relationshipTo(personOne, OpenMetadataType.PEER_RELATIONSHIP.typeName, personTwoGUID);

        assertTrue(peer.getRelationshipProperties() instanceof PeerProperties, "Peer relationship properties are missing");
        assertEquals("colleague", ((PeerProperties) peer.getRelationshipProperties()).getLabel());

        peopleOrganizer.call("/actor-profiles/" + personOneGUID + "/peer-persons/" + personTwoGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve(personOneGUID), OpenMetadataType.PEER_RELATIONSHIP.typeName, personTwoGUID);
        assertNotLinked(retrieve(personTwoGUID), OpenMetadataType.PEER_RELATIONSHIP.typeName, personOneGUID);

        delete(personOneGUID);
        delete(personTwoGUID);
    }


    /**
     * A team becomes a subteam of another; the super team sees its subteam and the subteam its super team until
     * the TeamStructure relationship is detached.
     */
    @Test
    @Order(2)
    void teamStructure()
    {
        String superTeamGUID = team("department");
        String subteamGUID   = team("squad");

        TeamStructureProperties properties = new TeamStructureProperties();

        properties.setDelegationEscalationAuthority(true);

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        peopleOrganizer.call("/actor-profiles/" + superTeamGUID + "/team-structures/" + subteamGUID + "/attach", link);

        OpenMetadataRootElement superTeam = retrieve(superTeamGUID);
        OpenMetadataRootElement subteam   = retrieve(subteamGUID);

        assertLinked(superTeam, OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName, subteamGUID);
        assertLinked(subteam, OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName, superTeamGUID);
        assertNotNull(subteam.getSuperTeam(), "the subteam does not show its super team");
        assertEquals(superTeamGUID, subteam.getSuperTeam().getRelatedElement().getElementHeader().getGUID());

        peopleOrganizer.call("/actor-profiles/" + superTeamGUID + "/team-structures/" + subteamGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve(superTeamGUID), OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName, subteamGUID);
        assertNotLinked(retrieve(subteamGUID), OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName, superTeamGUID);

        delete(superTeamGUID);
        delete(subteamGUID);
    }


    private static String person(String name)
    {
        PersonProperties properties = new PersonProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "people-organizer-" + name));
        properties.setDisplayName(name);

        return actorManager.create("/actor-profiles", newElement(properties));
    }


    private static String team(String name)
    {
        TeamProperties properties = new TeamProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Team", "people-organizer-" + name));
        properties.setDisplayName(name);

        return actorManager.create("/actor-profiles", newElement(properties));
    }


    private static OpenMetadataRootElement retrieve(String profileGUID)
    {
        OpenMetadataRootElement element = actorManager.element("/actor-profiles/" + profileGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "profile " + profileGUID + " is missing");

        return element;
    }


    private static void delete(String profileGUID)
    {
        actorManager.call("/actor-profiles/" + profileGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + profileGUID + "/retrieve");
    }
}
