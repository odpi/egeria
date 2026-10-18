/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CrowdSourcingRole;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CrowdSourcingContributionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.guids;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.newElement;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.search;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.template;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.updateElement;

/**
 * CommunityMattersFVT covers every request of the Community Matters OMVS: the community lifecycle and the
 * crowd-sourcing contributions that credit an actor with work on an element.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommunityMattersFVT
{
    private static final ViewServiceClient communityMatters = new ViewServiceClient(ViewServiceDescription.COMMUNITY_MATTERS);
    private static final ViewServiceClient actorManager     = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);

    private static String communityGUID     = null;
    private static String communityCopyGUID = null;


    /**
     * A community is created, read back, found by name and search string, and updated.
     */
    @Test
    @Order(1)
    void communityLifecycle()
    {
        CommunityProperties properties = new CommunityProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Community", "testers"));
        properties.setDisplayName("Testers' community");
        properties.setMission("Make sure every view service request works");

        communityGUID = communityMatters.create("/communities", newElement(properties));

        CommunityProperties retrieved = (CommunityProperties) retrieve(communityGUID).getProperties();

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals(properties.getMission(), retrieved.getMission());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(communityGUID), List.copyOf(guids(communityMatters.elements("/communities/by-name", byName))));
        assertTrue(guids(communityMatters.elements("/communities/by-search-string", search())).contains(communityGUID));

        CommunityProperties update = new CommunityProperties();

        update.setMission("Make sure every view service request works, every time");

        communityMatters.post("/communities/" + communityGUID + "/update", updateElement(update), BooleanResponse.class);

        CommunityProperties updated = (CommunityProperties) retrieve(communityGUID).getProperties();

        assertEquals(update.getMission(), updated.getMission());
        assertEquals(properties.getDisplayName(), updated.getDisplayName(), "a merge update should keep the display name");
    }


    /**
     * A community created from a template is a new community with the replacement name.
     */
    @Test
    @Order(2)
    void communityFromTemplate()
    {
        CommunityProperties replacement = new CommunityProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Community", "testers-copy"));
        replacement.setDisplayName("Copied community");

        communityCopyGUID = communityMatters.create("/communities/from-template", template(communityGUID, replacement));

        assertNotEquals(communityGUID, communityCopyGUID);

        OpenMetadataRootElement copy = retrieve(communityCopyGUID);

        assertEquals(OpenMetadataType.COMMUNITY.typeName, copy.getElementHeader().getType().getTypeName());
        assertEquals(replacement.getQualifiedName(), ((CommunityProperties) copy.getProperties()).getQualifiedName());
    }


    /**
     * A person is credited with a contribution to the community; the CrowdSourcingContribution relationship is
     * visible from the community and goes when it is detached.
     */
    @Test
    @Order(3)
    void crowdSourcingContribution()
    {
        String personGUID = contributor("contributor");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(new CrowdSourcingContributionProperties());

        communityMatters.call("/elements/" + communityGUID + "/crowd-sourcing-contributions/" + personGUID + "/attach", link);

        assertLinked(retrieve(communityGUID), OpenMetadataType.CROWD_SOURCING_CONTRIBUTION_RELATIONSHIP.typeName, personGUID);

        communityMatters.call("/elements/" + communityGUID + "/crowd-sourcing-contributions/" + personGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve(communityGUID), OpenMetadataType.CROWD_SOURCING_CONTRIBUTION_RELATIONSHIP.typeName, personGUID);

        deleteContributor(personGUID);
    }


    /**
     * The contribution can record the contributor's role.
     */
    @Test
    @Order(4)
    void crowdSourcingContributionWithRole()
    {
        String personGUID = contributor("proposer");

        CrowdSourcingContributionProperties properties = new CrowdSourcingContributionProperties();

        properties.setRoleType(CrowdSourcingRole.PROPOSER);

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        communityMatters.call("/elements/" + communityGUID + "/crowd-sourcing-contributions/" + personGUID + "/attach", link);

        RelatedMetadataElementSummary contribution = ActorGovernanceFvtSupport.relationshipTo(retrieve(communityGUID),
                                                                                              OpenMetadataType.CROWD_SOURCING_CONTRIBUTION_RELATIONSHIP.typeName,
                                                                                              personGUID);

        assertNotNull(contribution, "the contribution is missing");
        assertTrue(contribution.getRelationshipProperties() instanceof CrowdSourcingContributionProperties,
                   "the contribution has no CrowdSourcingContribution properties");
        assertEquals(CrowdSourcingRole.PROPOSER, ((CrowdSourcingContributionProperties) contribution.getRelationshipProperties()).getRoleType());

        communityMatters.call("/elements/" + communityGUID + "/crowd-sourcing-contributions/" + personGUID + "/detach", new DeleteRelationshipRequestBody());

        deleteContributor(personGUID);
    }


    /**
     * Both communities are deleted.
     */
    @Test
    @Order(5)
    void deleteCommunities()
    {
        for (String guid : List.of(communityGUID, communityCopyGUID))
        {
            communityMatters.call("/communities/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(communityMatters, "/communities/" + guid + "/retrieve");
        }
    }


    private static String contributor(String name)
    {
        PersonProperties personProperties = new PersonProperties();

        personProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "community-" + name));
        personProperties.setDisplayName("Community " + name);

        return actorManager.create("/actor-profiles", newElement(personProperties));
    }


    private static void deleteContributor(String personGUID)
    {
        actorManager.call("/actor-profiles/" + personGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + personGUID + "/retrieve");
    }


    private static OpenMetadataRootElement retrieve(String guid)
    {
        OpenMetadataRootElement element = communityMatters.element("/communities/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "community " + guid + " is missing");

        return element;
    }
}
