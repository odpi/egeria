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
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewAttachmentRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactDetailsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactThroughProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITInfrastructureProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityListMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.guids;

/**
 * ActorManagerProfilesFVT covers the Actor Manager OMVS requests for actor profiles, the contribution records
 * attached to them, user identities (with their security group memberships), contact details, IT
 * infrastructure profiles and assignment scopes.  Actor roles, perspectives and skills are in
 * {@link ActorManagerRolesFVT}.
 * <br>
 * Every actor is created with a run-unique qualified name; the profile and identity that MyProfileFVT creates
 * for the suite's user are never touched.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActorManagerProfilesFVT
{
    private static final ViewServiceClient actorManager = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);
    private static final ViewServiceClient assetMaker   = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static String personGUID          = null;
    private static String personCopyGUID      = null;
    private static String itProfileGUID       = null;
    private static String serverGUID          = null;
    private static String contributionGUID    = null;
    private static String userIdentityGUID    = null;
    private static String userIdentityCopyGUID = null;
    private static String contactDetailsGUID  = null;
    private static String contactCopyGUID     = null;


    /**
     * A person profile is created, read back, found by name and by search string, and updated.
     */
    @Test
    @Order(1)
    void personProfileLifecycle()
    {
        PersonProperties properties = new PersonProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "ada"));
        properties.setDisplayName("Ada Profile");
        properties.setDescription("Created by " + ActorManagerProfilesFVT.class.getSimpleName());

        personGUID = actorManager.create("/actor-profiles", newElement(properties));

        OpenMetadataRootElement person = retrieve("/actor-profiles/" + personGUID + "/retrieve");

        assertEquals(OpenMetadataType.PERSON.typeName, person.getElementHeader().getType().getTypeName());
        assertEquals("Ada Profile", ((PersonProperties) person.getProperties()).getDisplayName());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(personGUID), List.copyOf(guids(actorManager.elements("/actor-profiles/by-name", byName))));
        assertTrue(guids(actorManager.elements("/actor-profiles/by-search-string", search())).contains(personGUID),
                   "findActorProfiles did not return " + personGUID);

        PersonProperties update = new PersonProperties();

        update.setDisplayName("Ada Profile (updated)");
        update.setJobTitle("Tester");

        actorManager.post("/actor-profiles/" + personGUID + "/update", updateElement(update), BooleanResponse.class);

        PersonProperties updated = (PersonProperties) retrieve("/actor-profiles/" + personGUID + "/retrieve").getProperties();

        assertEquals("Ada Profile (updated)", updated.getDisplayName());
        assertEquals("Tester", updated.getJobTitle());
        assertEquals(properties.getQualifiedName(), updated.getQualifiedName(), "a merge update should keep the qualified name");
    }


    /**
     * A profile created from the person profile as a template is a new person with the replacement name.
     */
    @Test
    @Order(2)
    void personProfileFromTemplate()
    {
        PersonProperties replacement = new PersonProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "ada-copy"));
        replacement.setDisplayName("Ada Copy");

        personCopyGUID = actorManager.create("/actor-profiles/from-template", template(personGUID, replacement));

        assertNotEquals(personGUID, personCopyGUID);

        OpenMetadataRootElement copy = retrieve("/actor-profiles/" + personCopyGUID + "/retrieve");

        assertEquals(OpenMetadataType.PERSON.typeName, copy.getElementHeader().getType().getTypeName());
        assertEquals(replacement.getQualifiedName(), ((PersonProperties) copy.getProperties()).getQualifiedName());
    }


    /**
     * A contribution record is created for the person, then read, found, updated and deleted.
     */
    @Test
    @Order(3)
    void contributionRecordLifecycle()
    {
        ContributionRecordProperties properties = new ContributionRecordProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ContributionRecord", "ada"));
        properties.setDisplayName("Ada's contributions");
        properties.setKarmaPoints(10);

        NewAttachmentRequestBody requestBody = new NewAttachmentRequestBody();

        requestBody.setProperties(properties);

        contributionGUID = actorManager.create("/actor-profiles/" + personGUID + "/contribution-records", requestBody);

        OpenMetadataRootElement record = retrieve("/contribution-records/" + contributionGUID + "/retrieve");

        assertEquals(10, ((ContributionRecordProperties) record.getProperties()).getKarmaPoints());
        assertLinked(record, OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName, personGUID);
        assertLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName, contributionGUID);

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(contributionGUID), List.copyOf(guids(actorManager.elements("/contribution-records/by-name", byName))));
        assertTrue(guids(actorManager.elements("/contribution-records/by-search-string", search())).contains(contributionGUID));

        ContributionRecordProperties update = new ContributionRecordProperties();

        update.setKarmaPoints(25);

        actorManager.post("/contribution-records/" + contributionGUID, updateElement(update), BooleanResponse.class);

        assertEquals(25, ((ContributionRecordProperties) retrieve("/contribution-records/" + contributionGUID + "/retrieve").getProperties()).getKarmaPoints());

        actorManager.call("/contribution-records/" + contributionGUID + "/delete", new DeleteElementRequestBody());

        ViewServicesFvtTestSupport.assertGone(actorManager, "/contribution-records/" + contributionGUID + "/retrieve");
        retrieve("/actor-profiles/" + personGUID + "/retrieve");
    }


    /**
     * An IT profile describes the identities used by an IT asset; linking and unlinking the asset is visible from
     * both ends.
     */
    @Test
    @Order(4)
    void itInfrastructureProfile()
    {
        ITProfileProperties itProfileProperties = new ITProfileProperties();

        itProfileProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ITProfile", "server-profile"));
        itProfileProperties.setDisplayName("Server profile");

        itProfileGUID = actorManager.create("/actor-profiles", newElement(itProfileProperties));

        assertEquals(OpenMetadataType.IT_PROFILE.typeName, retrieve("/actor-profiles/" + itProfileGUID + "/retrieve").getElementHeader().getType().getTypeName());

        SoftwareServerProperties serverProperties = new SoftwareServerProperties();

        serverProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SoftwareServer", "profiled-server"));
        serverProperties.setDisplayName("Profiled server");

        serverGUID = assetMaker.create("/assets", newElement(serverProperties));

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        ITInfrastructureProfileProperties relationshipProperties = new ITInfrastructureProfileProperties();

        link.setProperties(relationshipProperties);

        actorManager.call("/assets/" + serverGUID + "/it-profiles/" + itProfileGUID + "/attach", link);

        assertLinked(retrieve("/actor-profiles/" + itProfileGUID + "/retrieve"), OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName, serverGUID);
        assertLinked(assetMaker.element("/assets/" + serverGUID + "/retrieve", new GetRequestBody()), OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName, itProfileGUID);
    }


    /**
     * Detaching the asset from its IT profile removes the ITInfrastructureProfile relationship.
     */
    @Test
    @Order(5)
    void itInfrastructureProfileDetach()
    {
        actorManager.call("/assets/" + serverGUID + "/it-profiles/" + itProfileGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve("/actor-profiles/" + itProfileGUID + "/retrieve"), OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName, serverGUID);
    }


    /**
     * A user identity is created, read, found, updated, linked to the person profile and unlinked again.
     */
    @Test
    @Order(6)
    void userIdentityLifecycle()
    {
        UserIdentityProperties properties = new UserIdentityProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("UserIdentity", "ada"));
        properties.setUserId("ada-" + OMAGPlatformExtension.RUN_ID);

        userIdentityGUID = actorManager.create("/user-identities", newElement(properties));

        assertEquals(properties.getUserId(), ((UserIdentityProperties) retrieve("/user-identities/" + userIdentityGUID + "/retrieve").getProperties()).getUserId());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(userIdentityGUID), List.copyOf(guids(actorManager.elements("/user-identities/by-name", byName))));
        assertTrue(guids(actorManager.elements("/user-identities/by-search-string", search())).contains(userIdentityGUID));

        UserIdentityProperties update = new UserIdentityProperties();

        update.setDistinguishedName("cn=ada,ou=fvt");

        actorManager.post("/user-identities/" + userIdentityGUID + "/update", updateElement(update), BooleanResponse.class);

        UserIdentityProperties updated = (UserIdentityProperties) retrieve("/user-identities/" + userIdentityGUID + "/retrieve").getProperties();

        assertEquals("cn=ada,ou=fvt", updated.getDistinguishedName());
        assertEquals(properties.getUserId(), updated.getUserId());

        ProfileIdentityProperties profileIdentityProperties = new ProfileIdentityProperties();

        profileIdentityProperties.setDescription("Ada's own account");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(profileIdentityProperties);

        actorManager.call("/user-identities/" + userIdentityGUID + "/profile-identity/" + personGUID + "/attach", link);

        assertLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName, userIdentityGUID);
        assertLinked(retrieve("/user-identities/" + userIdentityGUID + "/retrieve"), OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName, personGUID);

        actorManager.call("/user-identities/" + userIdentityGUID + "/profile-identity/" + personGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName, userIdentityGUID);
    }


    /**
     * The SecurityListMembership classification is added, changed and removed.
     */
    @Test
    @Order(7)
    void securityGroupMembership()
    {
        SecurityListMembershipProperties properties = new SecurityListMembershipProperties();

        properties.setSecurityGroups(List.of("fvt-readers"));

        NewClassificationRequestBody classify = new NewClassificationRequestBody();

        classify.setProperties(properties);

        actorManager.call("/user-identities/" + userIdentityGUID + "/security-group-memberships/classify", classify);

        assertEquals(List.of("fvt-readers"), membership().getSecurityGroups());

        SecurityListMembershipProperties update = new SecurityListMembershipProperties();

        update.setSecurityGroups(List.of("fvt-readers", "fvt-writers"));

        UpdateClassificationRequestBody reclassify = new UpdateClassificationRequestBody();

        reclassify.setProperties(update);

        actorManager.call("/user-identities/" + userIdentityGUID + "/security-group-memberships/reclassify", reclassify);

        assertEquals(List.of("fvt-readers", "fvt-writers"), membership().getSecurityGroups());

        actorManager.call("/user-identities/" + userIdentityGUID + "/security-group-memberships/declassify", new MetadataSourceRequestBody());

        assertNull(ActorGovernanceFvtSupport.classification(retrieve("/user-identities/" + userIdentityGUID + "/retrieve"),
                                                            OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName),
                   "declassify left the SecurityListMembership classification in place");
    }


    /**
     * A user identity created from a template is a new identity with the replacement properties.
     */
    @Test
    @Order(8)
    void userIdentityFromTemplate()
    {
        UserIdentityProperties replacement = new UserIdentityProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("UserIdentity", "ada-copy"));
        replacement.setUserId("ada-copy-" + OMAGPlatformExtension.RUN_ID);

        userIdentityCopyGUID = actorManager.create("/user-identities/from-template", template(userIdentityGUID, replacement));

        assertNotEquals(userIdentityGUID, userIdentityCopyGUID);
        assertEquals(replacement.getUserId(), ((UserIdentityProperties) retrieve("/user-identities/" + userIdentityCopyGUID + "/retrieve").getProperties()).getUserId());
    }


    /**
     * Contact details are created, read, found, updated, attached to the person and detached again, and copied
     * from a template.
     */
    @Test
    @Order(9)
    void contactDetailsLifecycle()
    {
        ContactDetailsProperties properties = new ContactDetailsProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ContactDetails", "ada-email"));
        properties.setDisplayName("Ada's email");
        properties.setContactType("work");
        properties.setContactMethodType(ContactMethodType.EMAIL);
        properties.setContactMethodValue("ada@example.com");

        contactDetailsGUID = actorManager.create("/contact-details", newElement(properties));

        assertEquals("ada@example.com", ((ContactDetailsProperties) retrieve("/contact-details/" + contactDetailsGUID + "/retrieve").getProperties()).getContactMethodValue());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(contactDetailsGUID), List.copyOf(guids(actorManager.elements("/contact-details/by-name", byName))));
        assertTrue(guids(actorManager.elements("/contact-details/by-search-string", search())).contains(contactDetailsGUID));

        ContactDetailsProperties update = new ContactDetailsProperties();

        update.setContactMethodValue("ada.lovelace@example.com");

        actorManager.post("/contact-details/" + contactDetailsGUID + "/update", updateElement(update), BooleanResponse.class);

        ContactDetailsProperties updated = (ContactDetailsProperties) retrieve("/contact-details/" + contactDetailsGUID + "/retrieve").getProperties();

        assertEquals("ada.lovelace@example.com", updated.getContactMethodValue());
        assertEquals(ContactMethodType.EMAIL, updated.getContactMethodType());

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(new ContactThroughProperties());

        actorManager.call("/elements/" + personGUID + "/contact-details/" + contactDetailsGUID + "/attach", link);

        assertLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName, contactDetailsGUID);
        assertLinked(retrieve("/contact-details/" + contactDetailsGUID + "/retrieve"), OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName, personGUID);

        actorManager.call("/elements/" + personGUID + "/contact-details/" + contactDetailsGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName, contactDetailsGUID);

        ContactDetailsProperties replacement = new ContactDetailsProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ContactDetails", "ada-email-copy"));
        replacement.setContactMethodValue("ada.copy@example.com");

        contactCopyGUID = actorManager.create("/contact-details/from-template", template(contactDetailsGUID, replacement));

        ContactDetailsProperties copy = (ContactDetailsProperties) retrieve("/contact-details/" + contactCopyGUID + "/retrieve").getProperties();

        assertEquals(replacement.getQualifiedName(), copy.getQualifiedName());
        assertEquals("ada.copy@example.com", copy.getContactMethodValue());
    }


    /**
     * The server is the scope of the person's assignment: AssignmentScope is visible from both ends, then removed.
     */
    @Test
    @Order(10)
    void assignmentScope()
    {
        AssignmentScopeProperties properties = new AssignmentScopeProperties();

        properties.setAssignmentType("Operator");
        properties.setDescription("Ada operates the profiled server");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        actorManager.call("/elements/" + serverGUID + "/actors/" + personGUID + "/attach", link);

        assertLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, serverGUID);
        assertLinked(assetMaker.element("/assets/" + serverGUID + "/retrieve", new GetRequestBody()), OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, personGUID);

        actorManager.call("/elements/" + serverGUID + "/actors/" + personGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieve("/actor-profiles/" + personGUID + "/retrieve"), OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, serverGUID);
    }


    /**
     * Everything this class created is deleted and can no longer be retrieved.
     */
    @Test
    @Order(11)
    void deleteEverything()
    {
        for (String guid : List.of(contactDetailsGUID, contactCopyGUID))
        {
            actorManager.call("/contact-details/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/contact-details/" + guid + "/retrieve");
        }

        for (String guid : List.of(userIdentityGUID, userIdentityCopyGUID))
        {
            actorManager.call("/user-identities/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/user-identities/" + guid + "/retrieve");
        }

        for (String guid : List.of(personGUID, personCopyGUID, itProfileGUID))
        {
            actorManager.call("/actor-profiles/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + guid + "/retrieve");
        }

        assetMaker.call("/assets/" + serverGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + serverGUID + "/retrieve");
    }


    private static SecurityListMembershipProperties membership()
    {
        ElementClassification classification = ActorGovernanceFvtSupport.classification(retrieve("/user-identities/" + userIdentityGUID + "/retrieve"),
                                                                                        OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName);

        assertNotNull(classification, "user identity " + userIdentityGUID + " has no SecurityListMembership classification");

        return (SecurityListMembershipProperties) classification.getClassificationProperties();
    }


    static NewElementRequestBody newElement(OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    static UpdateElementRequestBody updateElement(OpenMetadataRootProperties properties)
    {
        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    static TemplateRequestBody template(String                     templateGUID,
                                        OpenMetadataRootProperties replacementProperties)
    {
        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(templateGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setReplacementProperties(replacementProperties);

        return requestBody;
    }


    static SearchStringRequestBody search()
    {
        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        return requestBody;
    }


    private static OpenMetadataRootElement retrieve(String path)
    {
        OpenMetadataRootElement element = actorManager.element(path, new GetRequestBody());

        assertNotNull(element, path + " returned nothing");

        return element;
    }
}
