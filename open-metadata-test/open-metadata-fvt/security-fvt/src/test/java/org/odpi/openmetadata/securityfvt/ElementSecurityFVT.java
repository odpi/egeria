/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.CreatedElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.CURATOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.DIGITAL_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EMPLOYEES_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EMPLOYEE_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.FOUR_EYES_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OPEN_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OWNED_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.RESTRICTED_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.STEWARD_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.UNLISTED_ZONE;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_ELEMENT_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_INSTANCE_CREATE;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.assertRefused;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.createCollection;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.deleteElement;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.getElement;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.searchFinds;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.updateDescription;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.zonesOf;

/**
 * ElementSecurityFVT covers {@code OpenMetadataElementSecurity} - the decisions made about individual
 * metadata elements - as the Open Metadata Access Security Connector makes them from governance zones.
 * Every call here goes through the generic handlers, which ask the server security verifier before a
 * read, a create, an update or a delete, so what is tested is the decision as a user meets it rather than
 * the connector method in isolation.
 * <br><br>
 * The rules the connector applies, and which test shows each:
 * <ul>
 *     <li>an element with no zones, or whose zones have no access control, is open to every admitted
 *     user;</li>
 *     <li>where a zone has a control, the list for the operation applies, and the DEFAULT list applies to
 *     every operation that has no list of its own;</li>
 *     <li>a user is on a list by role, by group, by account type through the dynamic groups, by owning
 *     the element, or by their history of maintaining it;</li>
 *     <li>a zone named after a user is that user's own;</li>
 *     <li>an element in several zones is permitted if any one of them permits;</li>
 *     <li>a refused explicit read is an error, but a refused search result is silently left out;</li>
 *     <li>an account's defaultZones setting is added to whatever the user creates.</li>
 * </ul>
 * Four users take part.  The steward and the curator are in the group the zones grant their DEFAULT
 * operations to, and differ only in that the curator's account carries a defaultZones setting.  The
 * employee and the digital account are in no group; they differ in account type, which is what the
 * dynamic groups turn on.
 * <br><br>
 * Every element is created as its own anchor, so the decision is always made on the element's own
 * classifications; the anchor-based methods of the interface are not exercised here.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ElementSecurityFVT
{
    private static EgeriaOpenMetadataStoreClient stewardClient;
    private static EgeriaOpenMetadataStoreClient curatorClient;
    private static EgeriaOpenMetadataStoreClient employeeClient;
    private static EgeriaOpenMetadataStoreClient digitalClient;

    /**
     * Elements created by the tests, deleted by the steward when the class finishes.
     */
    private static final List<String> createdGUIDs = new ArrayList<>();


    @BeforeAll
    static void createClients() throws Exception
    {
        stewardClient  = SecurityFvtTestSupport.storeClientAs(STEWARD_USER_ID);
        curatorClient  = SecurityFvtTestSupport.storeClientAs(CURATOR_USER_ID);
        employeeClient = SecurityFvtTestSupport.storeClientAs(EMPLOYEE_USER_ID);
        digitalClient  = SecurityFvtTestSupport.storeClientAs(DIGITAL_USER_ID);
    }


    @AfterAll
    static void deleteCreatedElements()
    {
        for (String guid : createdGUIDs)
        {
            try
            {
                deleteElement(stewardClient, STEWARD_USER_ID, guid);
            }
            catch (Exception error)
            {
                System.out.println("security-fvt: could not delete " + guid + " (" + error.getClass().getSimpleName() + ": " + error.getMessage() + ")");
            }
        }
    }


    /**
     * Create an element and remember it for clean-up.
     *
     * @param client client acting as the creator
     * @param userId creator
     * @param tag label
     * @param zones zones, or null
     * @param otherClassifications further classifications, or null
     * @return the element
     * @throws Exception creation failed
     */
    private static CreatedElement create(EgeriaOpenMetadataStoreClient     client,
                                         String                            userId,
                                         String                            tag,
                                         List<String>                      zones,
                                         Map<String, NewElementProperties> otherClassifications) throws Exception
    {
        CreatedElement element = createCollection(client, userId, tag, zones, otherClassifications);

        createdGUIDs.add(element.guid());

        return element;
    }


    /**
     * Assert that a user can read an element both explicitly and through a search.
     *
     * @param client client acting as the user
     * @param userId user
     * @param element element
     * @param why what should make it visible, for the failure message
     * @throws Exception unexpected failure
     */
    private static void assertVisibleTo(EgeriaOpenMetadataStoreClient client,
                                        String                        userId,
                                        CreatedElement                element,
                                        String                        why) throws Exception
    {
        OpenMetadataElement retrieved = assertDoesNotThrow(() -> getElement(client, userId, element.guid()),
                                                           userId + " should be able to read the element explicitly because " + why);

        assertEquals(element.guid(), retrieved.getElementGUID(), "The element read should be the one asked for");
        assertTrue(searchFinds(client, userId, element),
                   userId + " should find the element in a search because " + why);
    }


    /**
     * Assert that a user is refused an explicit read of an element, and that a search leaves it out.
     *
     * @param client client acting as the user
     * @param userId user
     * @param element element
     * @param why what should make it invisible, for the failure message
     * @throws Exception unexpected failure
     */
    private static void assertHiddenFrom(EgeriaOpenMetadataStoreClient client,
                                         String                        userId,
                                         CreatedElement                element,
                                         String                        why) throws Exception
    {
        assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                      userId,
                      () -> getElement(client, userId, element.guid()),
                      userId + " reading the element explicitly, which should be refused because " + why + ",");

        assertFalse(searchFinds(client, userId, element),
                    userId + " should not find the element in a search because " + why);
    }


    /**
     * An element with no ZoneMembership classification is visible to everyone who is admitted to the
     * server and service.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anElementWithNoZonesIsVisibleToEveryone() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "no-zones", null, null);

        assertNull(zonesOf(getElement(stewardClient, STEWARD_USER_ID, element.guid())),
                   "The steward has no defaultZones setting, so the element should have no zone classification");

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "it is in no zone");
        assertVisibleTo(digitalClient, DIGITAL_USER_ID, element, "it is in no zone");
    }


    /**
     * A zone that has no access control defined does not restrict anything.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aZoneWithoutAnAccessControlIsOpen() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "unlisted-zone", List.of(UNLISTED_ZONE), null);

        assertEquals(List.of(UNLISTED_ZONE), zonesOf(getElement(stewardClient, STEWARD_USER_ID, element.guid())),
                     "The element should carry the zone it was created in");

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "its zone has no access control");
        assertVisibleTo(digitalClient, DIGITAL_USER_ID, element, "its zone has no access control");
    }


    /**
     * A zone whose control grants everything to the stewards group hides its elements from everyone else:
     * an explicit read is refused, and a search leaves the element out without complaint.
     * <br><br>
     * The steward's search is the control for the employee's: it shows that the element is there to be
     * found, so the employee's empty result is the security connector's doing rather than the search's.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRestrictedZoneHidesElementsFromUsersOutsideItsGroup() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "restricted", List.of(RESTRICTED_ZONE), null);

        assertVisibleTo(stewardClient, STEWARD_USER_ID, element, "the steward is in the group the zone is granted to");
        assertHiddenFrom(employeeClient, EMPLOYEE_USER_ID, element, "the employee is not in the group the zone is granted to");
    }


    /**
     * A user who is not permitted CREATE in a zone cannot create an element in it.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void creatingInARestrictedZoneIsRefusedToUsersOutsideItsGroup() throws Exception
    {
        assertRefused(UNAUTHORIZED_INSTANCE_CREATE,
                      EMPLOYEE_USER_ID,
                      () -> create(employeeClient, EMPLOYEE_USER_ID, "restricted-by-employee", List.of(RESTRICTED_ZONE), null),
                      "Creating an element in the restricted zone as the employee");
    }


    /**
     * An operation-specific list overrides the DEFAULT list for that operation only.  The open zone grants
     * READ to all users and everything else to stewards, so the employee can read the element but can
     * neither update nor delete it, and the refusal names the operation that was refused.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anOperationSpecificListOverridesTheDefaultForThatOperationOnly() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "open", List.of(OPEN_ZONE), null);

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "READ in the open zone is granted to all users");

        UserNotAuthorizedException error = assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                                                         EMPLOYEE_USER_ID,
                                                         () -> updateDescription(employeeClient, EMPLOYEE_USER_ID, element.guid(), "changed by the employee"),
                                                         "Updating an element in the open zone as the employee");

        assertTrue(error.getReportedErrorMessage().contains("UpdateProperties"),
                   "The refusal should name the operation refused.  Message: " + error.getReportedErrorMessage());

        assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                      EMPLOYEE_USER_ID,
                      () -> deleteElement(employeeClient, EMPLOYEE_USER_ID, element.guid()),
                      "Deleting an element in the open zone as the employee");

        assertDoesNotThrow(() -> updateDescription(stewardClient, STEWARD_USER_ID, element.guid(), "changed by the steward"),
                           "The steward should be able to update an element in the open zone under the DEFAULT list");
    }


    /**
     * The dynamic groups are built from the account type.  The employees zone grants everything to
     * employeeUsers, so the employee is admitted and the digital account, which is admitted to the server
     * and the service just as the employee is, is not.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aDynamicGroupAdmitsByAccountType() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "employees-only", List.of(EMPLOYEES_ZONE), null);

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "the employee's account type puts it in employeeUsers");
        assertHiddenFrom(digitalClient, DIGITAL_USER_ID, element, "a digital account is not in employeeUsers");
    }


    /**
     * An element in more than one zone is visible if any one of its zones permits.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anyOneOfAnElementsZonesIsEnoughToPermit() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "restricted-and-open", List.of(RESTRICTED_ZONE, OPEN_ZONE), null);

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "the open zone permits READ even though the restricted zone does not");
    }


    /**
     * A zone named after a user is that user's personal zone: the user is admitted by name, and everyone
     * else is subject to the zone's control.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aZoneNamedAfterAUserIsThatUsersOwn() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "personal", List.of(EMPLOYEE_USER_ID), null);

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "the zone is named after the employee");
        assertHiddenFrom(digitalClient, DIGITAL_USER_ID, element, "the zone is the employee's own and its control admits only stewards");
    }


    /**
     * The instanceOwner dynamic group is built from the element's Ownership classification.  The owned
     * zone grants everything to owners and stewards, so an element the steward creates with the employee
     * as its owner is visible to the employee, and not to the digital account, which is neither.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theInstanceOwnerGroupAdmitsOnlyTheElementsOwners() throws Exception
    {
        Map<String, NewElementProperties> ownership = Map.of(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                             SecurityFvtTestSupport.ownershipProperties(List.of(EMPLOYEE_USER_ID)));

        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "owned", List.of(OWNED_ZONE), ownership);

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "the employee is named as an owner");
        assertHiddenFrom(digitalClient, DIGITAL_USER_ID, element, "the digital account is not an owner and not a steward");
    }


    /**
     * The newMaintainer and existingMaintainer dynamic groups are built from the element's maintainedBy
     * history.  The four-eyes zone grants UPDATE_PROPERTIES only to newMaintainer, so each update must
     * come from someone who has not touched the element before: the steward who created it is refused,
     * the employee is permitted once, and having become a maintainer is refused the second time.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theMaintainerGroupsFollowTheElementsHistory() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "four-eyes", List.of(FOUR_EYES_ZONE), null);

        assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                      STEWARD_USER_ID,
                      () -> updateDescription(stewardClient, STEWARD_USER_ID, element.guid(), "changed by its creator"),
                      "Updating a four-eyes element as the steward who created it");

        assertDoesNotThrow(() -> updateDescription(employeeClient, EMPLOYEE_USER_ID, element.guid(), "changed by a new maintainer"),
                           "The employee, who has not maintained the element, should be permitted to update it");

        OpenMetadataElement updated = getElement(stewardClient, STEWARD_USER_ID, element.guid());

        assertNotNull(updated.getVersions().getMaintainedBy(), "The element should record who has maintained it");
        assertTrue(updated.getVersions().getMaintainedBy().contains(EMPLOYEE_USER_ID),
                   "After updating the element the employee should be recorded as one of its maintainers");

        assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                      EMPLOYEE_USER_ID,
                      () -> updateDescription(employeeClient, EMPLOYEE_USER_ID, element.guid(), "changed again by the same maintainer"),
                      "Updating a four-eyes element a second time as the same user");
    }


    /**
     * An account's defaultZones setting is added to whatever the account creates.  The curator's account
     * names the restricted zone, so an element the curator creates with no zones of its own lands in the
     * restricted zone - and is hidden from the employee like anything else there.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anAccountsDefaultZonesAreAppliedToWhatItCreates() throws Exception
    {
        CreatedElement element = create(curatorClient, CURATOR_USER_ID, "curated", null, null);

        assertEquals(List.of(RESTRICTED_ZONE), zonesOf(getElement(curatorClient, CURATOR_USER_ID, element.guid())),
                     "An element the curator creates with no zones should be placed in the curator's default zone");

        assertHiddenFrom(employeeClient, EMPLOYEE_USER_ID, element, "the curator's default zone is the restricted zone");
    }


    /**
     * The defaultZones setting is added to, not substituted for, the zones the user asked for.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anAccountsDefaultZonesAreAddedToTheZonesRequested() throws Exception
    {
        CreatedElement element = create(curatorClient, CURATOR_USER_ID, "curated-and-open", List.of(OPEN_ZONE), null);

        List<String> zones = zonesOf(getElement(curatorClient, CURATOR_USER_ID, element.guid()));

        assertNotNull(zones, "The element should have a zone classification");
        assertTrue(zones.contains(OPEN_ZONE), "The zone the curator asked for should be present.  Zones: " + zones);
        assertTrue(zones.contains(RESTRICTED_ZONE), "The curator's default zone should have been added.  Zones: " + zones);

        assertVisibleTo(employeeClient, EMPLOYEE_USER_ID, element, "the open zone permits READ regardless of the added restricted zone");
    }
}
