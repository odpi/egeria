/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
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
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PerspectiveProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.SkillProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.SolutionActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
 * ActorManagerRolesFVT covers the Actor Manager OMVS requests for actor roles (through both the
 * {@code actor-roles} and {@code solution-roles} paths), the three kinds of role appointment, perspectives and
 * skills.  Each kind of element goes through the same lifecycle: create, retrieve, find by name and by search
 * string, update, create from template, delete.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActorManagerRolesFVT
{
    private static final ViewServiceClient actorManager = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);

    private static final List<String> profilesToDelete = new ArrayList<>();

    private static String personGUID        = null;
    private static String personRoleGUID    = null;
    private static String teamGUID          = null;
    private static String teamRoleGUID      = null;
    private static String itProfileGUID     = null;
    private static String itProfileRoleGUID = null;


    /**
     * The four role subtypes and the two collection paths each go through the full lifecycle.
     */
    @Test
    @Order(1)
    void actorRoleLifecycles()
    {
        lifecycle("/actor-roles", "PersonRole", PersonRoleProperties::new);
        lifecycle("/actor-roles", "TeamRole", TeamRoleProperties::new);
        lifecycle("/actor-roles", "ITProfileRole", ITProfileRoleProperties::new);
        lifecycle("/solution-roles", "SolutionActorRole", SolutionActorRoleProperties::new);
    }


    /**
     * Perspectives go through the full lifecycle.
     */
    @Test
    @Order(2)
    void perspectiveLifecycle()
    {
        lifecycle("/perspectives", "Perspective", PerspectiveProperties::new);
    }


    /**
     * Skills go through the full lifecycle.
     */
    @Test
    @Order(3)
    void skillLifecycle()
    {
        lifecycle("/skills", "Skill", SkillProperties::new);
    }


    /**
     * A person is appointed to a person role; the appointment is visible from both ends.
     */
    @Test
    @Order(4)
    void personRoleAppointment()
    {
        PersonProperties personProperties = new PersonProperties();

        personProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "role-holder"));
        personProperties.setDisplayName("Role holder");

        personGUID     = profile(personProperties);
        personRoleGUID = role("/actor-roles", "PersonRole", new PersonRoleProperties());

        PersonRoleAppointmentProperties properties = new PersonRoleAppointmentProperties();

        properties.setExpectedTimeAllocationPercent(50);

        appoint("/actor-roles/" + personRoleGUID + "/person-role-appointments/" + personGUID,
                properties, personRoleGUID, personGUID, OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * A team is appointed to a team role.
     */
    @Test
    @Order(5)
    void teamRoleAppointment()
    {
        TeamProperties teamProperties = new TeamProperties();

        teamProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Team", "role-holder"));
        teamProperties.setDisplayName("Role holding team");

        teamGUID     = profile(teamProperties);
        teamRoleGUID = role("/actor-roles", "TeamRole", new TeamRoleProperties());

        appoint("/actor-roles/" + teamRoleGUID + "/team-role-appointments/" + teamGUID,
                new TeamRoleAppointmentProperties(), teamRoleGUID, teamGUID, OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * An IT profile is appointed to an IT profile role.
     */
    @Test
    @Order(6)
    void itProfileRoleAppointment()
    {
        ITProfileProperties itProfileProperties = new ITProfileProperties();

        itProfileProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ITProfile", "role-holder"));
        itProfileProperties.setDisplayName("Role holding IT profile");

        itProfileGUID     = profile(itProfileProperties);
        itProfileRoleGUID = role("/actor-roles", "ITProfileRole", new ITProfileRoleProperties());

        appoint("/actor-roles/" + itProfileRoleGUID + "/it-profile-role-appointments/" + itProfileGUID,
                new ITProfileRoleAppointmentProperties(), itProfileRoleGUID, itProfileGUID, OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * Releasing the person from the role removes the PersonRoleAppointment relationship.
     */
    @Test
    @Order(7)
    void personRoleRelease()
    {
        release("/actor-roles/" + personRoleGUID + "/person-role-appointments/" + personGUID,
                personRoleGUID, personGUID, OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * Releasing the team from the role removes the TeamRoleAppointment relationship.
     */
    @Test
    @Order(8)
    void teamRoleRelease()
    {
        release("/actor-roles/" + teamRoleGUID + "/team-role-appointments/" + teamGUID,
                teamRoleGUID, teamGUID, OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * Releasing the IT profile from the role removes the ITProfileRoleAppointment relationship.
     */
    @Test
    @Order(9)
    void itProfileRoleRelease()
    {
        release("/actor-roles/" + itProfileRoleGUID + "/it-profile-role-appointments/" + itProfileGUID,
                itProfileRoleGUID, itProfileGUID, OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName);
    }


    /**
     * Deleting the appointed roles removes their appointments with them.
     */
    @Test
    @Order(10)
    void deleteAppointedRoles()
    {
        for (String roleGUID : List.of(personRoleGUID, teamRoleGUID, itProfileRoleGUID))
        {
            actorManager.call("/actor-roles/" + roleGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-roles/" + roleGUID + "/retrieve");
        }

        assertNotLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName, personRoleGUID);
        assertNotLinked(retrieve("/actor-profiles/" + teamGUID + "/retrieve"), OpenMetadataType.TEAM_ROLE_APPOINTMENT_RELATIONSHIP.typeName, teamRoleGUID);
        assertNotLinked(retrieve("/actor-profiles/" + itProfileGUID + "/retrieve"), OpenMetadataType.IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP.typeName, itProfileRoleGUID);
    }


    /**
     * Delete the profiles created for the appointments.
     */
    @AfterAll
    static void deleteProfiles()
    {
        for (String profileGUID : profilesToDelete)
        {
            actorManager.call("/actor-profiles/" + profileGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + profileGUID + "/retrieve");
        }
    }


    /**
     * Run one kind of element through create, retrieve, find, update, create from template and delete.
     *
     * @param root collection path for the element type
     * @param typeName expected type name of the elements created
     * @param newProperties creates an empty properties bean of the right subtype
     */
    private static void lifecycle(String                            root,
                                  String                            typeName,
                                  Supplier<ReferenceableProperties> newProperties)
    {
        ReferenceableProperties properties = newProperties.get();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(typeName, root.substring(1)));
        properties.setDisplayName(typeName + " for " + root);
        properties.setDescription("Created by " + ActorManagerRolesFVT.class.getSimpleName());

        String guid = actorManager.create(root, newElement(properties));

        OpenMetadataRootElement element = retrieve(root + "/" + guid + "/retrieve");

        assertEquals(typeName, element.getElementHeader().getType().getTypeName());
        assertEquals(properties.getDisplayName(), ((ReferenceableProperties) element.getProperties()).getDisplayName());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(guid), List.copyOf(guids(actorManager.elements(root + "/by-name", byName))), root + "/by-name");
        assertTrue(guids(actorManager.elements(root + "/by-search-string", search())).contains(guid), root + "/by-search-string did not return " + guid);

        ReferenceableProperties update = newProperties.get();

        update.setDescription("Updated by " + ActorManagerRolesFVT.class.getSimpleName());

        actorManager.post(root + "/" + guid + "/update", updateElement(update), BooleanResponse.class);

        ReferenceableProperties updated = (ReferenceableProperties) retrieve(root + "/" + guid + "/retrieve").getProperties();

        assertEquals(update.getDescription(), updated.getDescription());
        assertEquals(properties.getDisplayName(), updated.getDisplayName(), "a merge update should keep the display name");

        ReferenceableProperties replacement = newProperties.get();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(typeName, root.substring(1) + "-copy"));

        String copyGUID = actorManager.create(root + "/from-template", template(guid, replacement));

        assertNotEquals(guid, copyGUID);

        OpenMetadataRootElement copy = retrieve(root + "/" + copyGUID + "/retrieve");

        assertEquals(typeName, copy.getElementHeader().getType().getTypeName());
        assertEquals(replacement.getQualifiedName(), ((ReferenceableProperties) copy.getProperties()).getQualifiedName());

        for (String deleteGUID : List.of(guid, copyGUID))
        {
            actorManager.call(root + "/" + deleteGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, root + "/" + deleteGUID + "/retrieve");
        }
    }


    private static String profile(ReferenceableProperties properties)
    {
        String guid = actorManager.create("/actor-profiles", newElement(properties));

        profilesToDelete.add(guid);

        return guid;
    }


    private static String role(String                  root,
                               String                  typeName,
                               ReferenceableProperties properties)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(typeName, "appointed"));
        properties.setDisplayName("Appointed " + typeName);

        if (properties instanceof ActorRoleProperties actorRoleProperties)
        {
            actorRoleProperties.setScope("view-services-fvt");
        }

        return actorManager.create(root, newElement(properties));
    }


    /**
     * Attach an appointment and check it from both ends.
     */
    private static void appoint(String                     appointmentPath,
                                RelationshipBeanProperties properties,
                                String                     roleGUID,
                                String                     profileGUID,
                                String                     relationshipTypeName)
    {
        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        actorManager.call(appointmentPath + "/attach", link);

        assertLinked(retrieve("/actor-roles/" + roleGUID + "/retrieve"), relationshipTypeName, profileGUID);
        assertLinked(retrieve("/actor-profiles/" + profileGUID + "/retrieve"), relationshipTypeName, roleGUID);
    }


    /**
     * Detach an appointment and check it has gone from both ends.
     */
    private static void release(String appointmentPath,
                                String roleGUID,
                                String profileGUID,
                                String relationshipTypeName)
    {
        actorManager.call(appointmentPath + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve("/actor-roles/" + roleGUID + "/retrieve"), relationshipTypeName, profileGUID);
        assertNotLinked(retrieve("/actor-profiles/" + profileGUID + "/retrieve"), relationshipTypeName, roleGUID);
    }


    private static OpenMetadataRootElement retrieve(String path)
    {
        OpenMetadataRootElement element = actorManager.element(path, new GetRequestBody());

        assertNotNull(element, path + " returned nothing");

        return element;
    }
}
