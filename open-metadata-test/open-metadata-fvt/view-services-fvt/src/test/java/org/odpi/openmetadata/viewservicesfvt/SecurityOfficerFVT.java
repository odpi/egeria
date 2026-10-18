/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.SecretsCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.AssociatedSecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.GovernanceZoneProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ResourcePermissionsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecretsCollectionSecurityListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityAccessControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.UserAccountProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.newElement;

/**
 * SecurityOfficerFVT covers every request of the Security Officer OMVS: the zone hierarchy and zone membership
 * profile, the secrets collection's links to security access controls, security lists and user accounts, its
 * user account profile, the security lists associated with a security access control, and the requests that
 * manage user accounts and security access controls on a running OMAG Server Platform.
 * <br>
 * The zones and the security access control are governance definitions (Governance Officer), the security group
 * is a collection (Collection Manager), the secrets collection and platform are assets (Asset Maker) and the
 * user identity is an actor (Actor Manager).
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityOfficerFVT
{
    private static final ViewServiceClient securityOfficer   = new ViewServiceClient(ViewServiceDescription.SECURITY_OFFICER);
    private static final ViewServiceClient governanceOfficer = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    private static final ViewServiceClient actorManager      = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);

    private static final String UNKNOWN_PLATFORM_GUID = "4c1d7a2e-0000-4000-8000-766965777366";

    private static String outerZoneGUID      = null;
    private static String innerZoneGUID      = null;
    private static String accessControlGUID  = null;
    private static String securityGroupGUID  = null;
    private static String secretsGUID        = null;
    private static String userIdentityGUID   = null;
    private static String platformGUID       = null;


    /**
     * Create the elements that the security officer links together.
     */
    @BeforeAll
    static void createElements()
    {
        outerZoneGUID = zone("outer");
        innerZoneGUID = zone("inner");

        SecurityAccessControlProperties accessControlProperties = new SecurityAccessControlProperties();

        accessControlProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SecurityAccessControl", "payroll"));
        accessControlProperties.setDisplayName("Payroll access");

        accessControlGUID = governanceOfficer.create("/governance-definitions", newElement(accessControlProperties));

        SecurityGroupProperties securityGroupProperties = new SecurityGroupProperties();

        securityGroupProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SecurityGroup", "payroll-clerks"));
        securityGroupProperties.setDisplayName("Payroll clerks");
        securityGroupProperties.setDistinguishedName("cn=payroll-clerks,ou=fvt");

        securityGroupGUID = collectionManager.create("/collections", newElement(securityGroupProperties));

        SecretsCollectionProperties secretsProperties = new SecretsCollectionProperties();

        secretsProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SecretsCollection", "payroll-secrets"));
        secretsProperties.setDisplayName("Payroll secrets");

        secretsGUID = assetMaker.create("/assets", newElement(secretsProperties));

        UserIdentityProperties userIdentityProperties = new UserIdentityProperties();

        userIdentityProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("UserIdentity", "payroll-clerk"));
        userIdentityProperties.setUserId("payroll-clerk-" + OMAGPlatformExtension.RUN_ID);

        userIdentityGUID = actorManager.create("/user-identities", newElement(userIdentityProperties));

        SoftwareServerPlatformProperties platformProperties = new SoftwareServerPlatformProperties();

        platformProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SoftwareServerPlatform", "unconnected-platform"));
        platformProperties.setDisplayName("Platform with no connection");

        platformGUID = assetMaker.create("/assets", newElement(platformProperties));
    }


    /**
     * The inner zone is nested in the outer zone, then removed from it.
     */
    @Test
    @Order(1)
    void zoneHierarchy()
    {
        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(new ZoneHierarchyProperties());

        securityOfficer.call("/governance-zones/" + outerZoneGUID + "/governance-zone-hierarchies/" + innerZoneGUID + "/attach", link);

        assertLinked(governanceDefinition(outerZoneGUID), OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName, innerZoneGUID);
        assertLinked(governanceDefinition(innerZoneGUID), OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName, outerZoneGUID);

        securityOfficer.call("/governance-zones/" + outerZoneGUID + "/governance-zone-hierarchies/" + innerZoneGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(governanceDefinition(outerZoneGUID), OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName, innerZoneGUID);
    }


    /**
     * A zone membership profile records the zone's membership counts and is then removed.
     */
    @Test
    @Order(2)
    void zoneMembershipProfile()
    {
        ZoneMembershipProfileProperties properties = new ZoneMembershipProfileProperties();

        properties.setTotalMembership(42);
        properties.setTypeMembership(Map.of("Asset", 42L));

        NewClassificationRequestBody classify = new NewClassificationRequestBody();

        classify.setProperties(properties);

        securityOfficer.call("/governance-zones/" + outerZoneGUID + "/zone-membership-profile", classify);

        ElementClassification classification = ActorGovernanceFvtSupport.classification(governanceDefinition(outerZoneGUID),
                                                                                        OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName);

        assertNotNull(classification, "setZoneMembershipProfile did not classify the zone");
        assertEquals(42, ((ZoneMembershipProfileProperties) classification.getClassificationProperties()).getTotalMembership());

        securityOfficer.call("/governance-zones/" + outerZoneGUID + "/zone-membership-profile/remove", new DeleteClassificationRequestBody());

        assertNull(ActorGovernanceFvtSupport.classification(governanceDefinition(outerZoneGUID), OpenMetadataType.ZONE_MEMBERSHIP_PROFILE_CLASSIFICATION.typeName),
                   "clearZoneMembershipProfile left the classification in place");
    }


    /**
     * The secrets collection holds the resource permissions for the security access control.
     */
    @Test
    @Order(3)
    void resourcePermissions()
    {
        ResourcePermissionsProperties properties = new ResourcePermissionsProperties();

        properties.setLabel("payroll");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/resource-permissions/" + accessControlGUID + "/attach", link);

        assertLinked(asset(secretsGUID), OpenMetadataType.RESOURCE_PERMISSIONS_RELATIONSHIP.typeName, accessControlGUID);
        assertLinked(governanceDefinition(accessControlGUID), OpenMetadataType.RESOURCE_PERMISSIONS_RELATIONSHIP.typeName, secretsGUID);

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/resource-permissions/" + accessControlGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(secretsGUID), OpenMetadataType.RESOURCE_PERMISSIONS_RELATIONSHIP.typeName, accessControlGUID);
    }


    /**
     * The secrets collection holds the members of the security group.
     */
    @Test
    @Order(4)
    void secretsCollectionSecurityList()
    {
        SecretsCollectionSecurityListProperties properties = new SecretsCollectionSecurityListProperties();

        properties.setLabel("clerks");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/security-lists/" + securityGroupGUID + "/attach", link);

        assertLinked(asset(secretsGUID), OpenMetadataType.SECRETS_COLLECTION_SECURITY_LIST_RELATIONSHIP.typeName, securityGroupGUID);
        assertLinked(collection(securityGroupGUID), OpenMetadataType.SECRETS_COLLECTION_SECURITY_LIST_RELATIONSHIP.typeName, secretsGUID);

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/security-lists/" + securityGroupGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(secretsGUID), OpenMetadataType.SECRETS_COLLECTION_SECURITY_LIST_RELATIONSHIP.typeName, securityGroupGUID);
    }


    /**
     * The secrets collection holds the user account for the user identity.
     */
    @Test
    @Order(5)
    void userAccount()
    {
        UserAccountProperties properties = new UserAccountProperties();

        properties.setLabel("clerk-account");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/user-accounts/" + userIdentityGUID + "/attach", link);

        assertLinked(asset(secretsGUID), OpenMetadataType.USER_ACCOUNT_RELATIONSHIP.typeName, userIdentityGUID);
        assertLinked(actorManager.element("/user-identities/" + userIdentityGUID + "/retrieve", new GetRequestBody()),
                     OpenMetadataType.USER_ACCOUNT_RELATIONSHIP.typeName, secretsGUID);

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/user-accounts/" + userIdentityGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(secretsGUID), OpenMetadataType.USER_ACCOUNT_RELATIONSHIP.typeName, userIdentityGUID);
    }


    /**
     * A user account profile summarises the secrets collection's accounts and is then removed.
     */
    @Test
    @Order(6)
    void userAccountProfile()
    {
        UserAccountProfileProperties properties = new UserAccountProfileProperties();

        properties.setUserAccountCount(3);
        properties.setActiveAccountCount(2);
        properties.setLockedAccountCount(1);

        NewClassificationRequestBody classify = new NewClassificationRequestBody();

        classify.setProperties(properties);

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/user-account-profile", classify);

        ElementClassification classification = ActorGovernanceFvtSupport.classification(asset(secretsGUID),
                                                                                        OpenMetadataType.USER_ACCOUNT_PROFILE_CLASSIFICATION.typeName);

        assertNotNull(classification, "setUserAccountProfile did not classify the secrets collection");

        UserAccountProfileProperties retrieved = (UserAccountProfileProperties) classification.getClassificationProperties();

        assertEquals(3, retrieved.getUserAccountCount());
        assertEquals(1, retrieved.getLockedAccountCount());

        securityOfficer.call("/secrets-collections/" + secretsGUID + "/user-account-profile/remove", new DeleteClassificationRequestBody());

        assertNull(ActorGovernanceFvtSupport.classification(asset(secretsGUID), OpenMetadataType.USER_ACCOUNT_PROFILE_CLASSIFICATION.typeName),
                   "clearUserAccountProfile left the classification in place");
    }


    /**
     * The security group is associated with the access control for an operation; the association is updated
     * through the relationship's GUID and then detached.
     */
    @Test
    @Order(7)
    void associatedSecurityList()
    {
        AssociatedSecurityListProperties properties = new AssociatedSecurityListProperties();

        properties.setOperationName("read");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        String relationshipGUID = securityOfficer.create("/security-access-controls/" + accessControlGUID + "/associated-security-lists/" + securityGroupGUID + "/attach", link);

        assertEquals("read", operationName(relationshipGUID));
        assertLinked(collection(securityGroupGUID), OpenMetadataType.ASSOCIATED_SECURITY_LIST_RELATIONSHIP.typeName, accessControlGUID);

        AssociatedSecurityListProperties update = new AssociatedSecurityListProperties();

        update.setOperationName("update");

        UpdateRelationshipRequestBody updateBody = new UpdateRelationshipRequestBody();

        updateBody.setProperties(update);

        securityOfficer.call("/associated-security-lists/" + relationshipGUID + "/update", updateBody);

        assertEquals("update", operationName(relationshipGUID));

        securityOfficer.call("/associated-security-lists/" + relationshipGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(governanceDefinition(accessControlGUID), OpenMetadataType.ASSOCIATED_SECURITY_LIST_RELATIONSHIP.typeName, securityGroupGUID);
    }


    /**
     * The user account and security access control requests act on a running OMAG Server Platform, through the
     * connection of the platform's asset to the OMAG Server Platform connector.  This suite has no such asset:
     * the template that catalogues one is not in the CoreContentPack, and the hermetic platform has no metadata
     * security connector to manage accounts anyway.  So the working path cannot be driven here; what can be
     * checked is that every request naming a platform that is not catalogued is refused because the platform is
     * not found.
     */
    @Test
    @Order(8)
    void platformSecurityRequestsForAnUnknownPlatform()
    {
        platformRequestsAreRefused("/platforms/" + UNKNOWN_PLATFORM_GUID, 404);
    }


    /**
     * A platform asset with no connection cannot be reached, so its requests must be refused - not reported as
     * successful having done nothing.
     */
    @Test
    @Order(9)
    void platformSecurityRequestsForAnUnconnectedPlatform()
    {
        platformRequestsAreRefused("/platforms/" + platformGUID, 0);
    }


    /**
     * Send each of the seven platform requests and check it is refused.
     *
     * @param platformRoot path of the platform
     * @param expectedHTTPCode the HTTP code the refusal must carry, or 0 for any refusal
     */
    private static void platformRequestsAreRefused(String platformRoot,
                                                   int    expectedHTTPCode)
    {
        String accountUserId = "fvt-account-" + OMAGPlatformExtension.RUN_ID;

        List<JsonNode> responses = new ArrayList<>();

        responses.add(securityOfficer.postForFailure(platformRoot + "/user-accounts",
                                                     Map.of("class", "UserAccountRequestBody",
                                                            "userAccount", Map.of("userId", accountUserId))));
        responses.add(securityOfficer.postForFailure(platformRoot + "/security-access-control",
                                                     Map.of("class", "SecurityAccessControlRequestBody",
                                                            "securityAccessControl", Map.of("controlName", "fvt-control"))));

        responses.add(refused(HttpMethod.GET, platformRoot + "/user-accounts"));
        responses.add(refused(HttpMethod.GET, platformRoot + "/user-accounts/" + accountUserId));
        responses.add(refused(HttpMethod.DELETE, platformRoot + "/user-accounts/" + accountUserId));
        responses.add(refused(HttpMethod.GET, platformRoot + "/security-access-control/fvt-control"));
        responses.add(refused(HttpMethod.DELETE, platformRoot + "/security-access-control/fvt-control"));

        if (expectedHTTPCode != 0)
        {
            for (JsonNode response : responses)
            {
                assertEquals(expectedHTTPCode, response.path("relatedHTTPCode").asInt(), "unexpected refusal: " + response);
            }
        }
    }


    /**
     * Delete everything this class created.
     */
    @AfterAll
    static void deleteElements()
    {
        for (String guid : new String[]{outerZoneGUID, innerZoneGUID, accessControlGUID})
        {
            governanceOfficer.call("/governance-definitions/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/governance-definitions/" + guid + "/retrieve");
        }

        collectionManager.call("/collections/" + securityGroupGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + securityGroupGUID + "/retrieve");

        for (String guid : new String[]{secretsGUID, platformGUID})
        {
            assetMaker.call("/assets/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + guid + "/retrieve");
        }

        actorManager.call("/user-identities/" + userIdentityGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(actorManager, "/user-identities/" + userIdentityGUID + "/retrieve");
    }


    private static String zone(String name)
    {
        GovernanceZoneProperties properties = new GovernanceZoneProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GovernanceZone", name));
        properties.setDisplayName("FVT " + name + " zone");
        properties.setCriteria("Elements used by the " + name + " test");

        return governanceOfficer.create("/governance-definitions", newElement(properties));
    }


    /**
     * Read the operation name of an AssociatedSecurityList relationship from the access control's side.
     */
    private static String operationName(String relationshipGUID)
    {
        for (RelatedMetadataElementSummary summary : ActorGovernanceFvtSupport.relationships(governanceDefinition(accessControlGUID)))
        {
            if ((summary.getRelationshipHeader() != null) && relationshipGUID.equals(summary.getRelationshipHeader().getGUID()))
            {
                assertTrue(summary.getRelationshipProperties() instanceof AssociatedSecurityListProperties,
                           "AssociatedSecurityList relationship has no properties");

                return ((AssociatedSecurityListProperties) summary.getRelationshipProperties()).getOperationName();
            }
        }

        throw new AssertionError("security access control " + accessControlGUID + " has no relationship " + relationshipGUID);
    }


    /**
     * Send a GET or DELETE request that must be refused.
     */
    private static JsonNode refused(HttpMethod method,
                                    String     path)
    {
        String url = OMAGPlatformExtension.getPlatformURLRoot() + "/servers/" + OMAGPlatformExtension.VIEW_SERVER_NAME +
                "/api/open-metadata/security-officer" + path;

        JsonNode response = new RestTemplate().exchange(url, method, null, JsonNode.class).getBody();

        assertNotNull(response, method + " " + path + " returned no response");
        assertNotEquals(200, response.path("relatedHTTPCode").asInt(), "Security Officer accepted " + method + " " + path + ", which should have been refused");

        return response;
    }


    private static OpenMetadataRootElement governanceDefinition(String guid)
    {
        OpenMetadataRootElement element = governanceOfficer.element("/governance-definitions/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "governance definition " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement collection(String guid)
    {
        OpenMetadataRootElement element = collectionManager.element("/collections/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "collection " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement asset(String guid)
    {
        OpenMetadataRootElement element = assetMaker.element("/assets/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "asset " + guid + " is missing");

        return element;
    }
}
