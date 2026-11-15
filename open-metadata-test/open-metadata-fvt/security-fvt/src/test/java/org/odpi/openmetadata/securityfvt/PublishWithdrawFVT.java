/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.CreatedElement;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.CURATOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EMPLOYEE_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OPENER_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OPEN_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.RELEASER_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.RESTRICTED_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.STEWARD_USER_ID;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_ELEMENT_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNDEFINED_ZONES_FOR_USER;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.assertRefused;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.createCollection;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.deleteElement;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.getElement;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.zonesOf;

/**
 * Publishing and withdrawing an element change its governance zones, and so who can see it.  The zones an
 * element moves to are not chosen by the caller: they come from the caller's account in the user directory -
 * {@code publishZones} when the element is published and {@code defaultZones} when it is withdrawn.
 * <br><br>
 * These tests cover each thing an account can say:
 * <ul>
 *     <li>a list of zones - the element's zones are replaced by them;</li>
 *     <li>an empty list - all of the element's zones are removed, so it is visible to everyone;</li>
 *     <li>a property with no value (null) - the same as an empty list;</li>
 *     <li>nothing at all - the request is refused and the element is unchanged, because the directory does not
 *         say what publishing or withdrawing means for this user, and guessing "remove all zones" would open the
 *         element to everyone.</li>
 * </ul>
 * All four users are stewards, so each is permitted every operation in the zones used here: what differs is
 * only the zone settings on their accounts.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PublishWithdrawFVT
{
    private static EgeriaOpenMetadataStoreClient stewardClient;
    private static EgeriaOpenMetadataStoreClient curatorClient;
    private static EgeriaOpenMetadataStoreClient releaserClient;
    private static EgeriaOpenMetadataStoreClient openerClient;
    private static EgeriaOpenMetadataStoreClient employeeClient;

    /**
     * Elements created by the tests, deleted by the steward when the class finishes.
     */
    private static final List<String> createdGUIDs = new ArrayList<>();


    @BeforeAll
    static void createClients() throws Exception
    {
        stewardClient  = SecurityFvtTestSupport.storeClientAs(STEWARD_USER_ID);
        curatorClient  = SecurityFvtTestSupport.storeClientAs(CURATOR_USER_ID);
        releaserClient = SecurityFvtTestSupport.storeClientAs(RELEASER_USER_ID);
        openerClient   = SecurityFvtTestSupport.storeClientAs(OPENER_USER_ID);
        employeeClient = SecurityFvtTestSupport.storeClientAs(EMPLOYEE_USER_ID);
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
     * @return the element
     * @throws Exception creation failed
     */
    private static CreatedElement create(EgeriaOpenMetadataStoreClient client,
                                         String                        userId,
                                         String                        tag,
                                         List<String>                  zones) throws Exception
    {
        CreatedElement element = createCollection(client, userId, tag, zones, null);

        createdGUIDs.add(element.guid());

        return element;
    }


    /**
     * Return the zones of an element as the steward sees them - the steward may read every zone used here.
     *
     * @param element element
     * @return its zones, or null if it has no zone classification
     * @throws Exception the element could not be read
     */
    private static List<String> currentZones(CreatedElement element) throws Exception
    {
        return zonesOf(getElement(stewardClient, STEWARD_USER_ID, element.guid()));
    }


    /**
     * Assert that an element has no governance zones.  Removing the zones removes the classification, but a
     * classification left with an empty list would mean the same thing to the security connector.
     *
     * @param element element
     * @param message what was expected
     * @throws Exception the element could not be read
     */
    private static void assertNoZones(CreatedElement element,
                                      String         message) throws Exception
    {
        List<String> zones = currentZones(element);

        assertTrue((zones == null) || (zones.isEmpty()), message + ".  Zones: " + zones);
    }


    /**
     * An account that lists publish zones moves the element to exactly those zones.  The curator's new element
     * starts in the restricted zone (the curator's default zone), where the employee can not see it; publishing
     * replaces that with the open zone, where the employee can.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void publishingReplacesTheZonesWithTheAccountsPublishZones() throws Exception
    {
        CreatedElement element = create(curatorClient, CURATOR_USER_ID, "publish-to-open", null);

        assertEquals(List.of(RESTRICTED_ZONE), currentZones(element), "The curator's new element should start in the curator's default zone");

        assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                      EMPLOYEE_USER_ID,
                      () -> getElement(employeeClient, EMPLOYEE_USER_ID, element.guid()),
                      EMPLOYEE_USER_ID + " reading an element that has not been published yet");

        curatorClient.publishMetadataElement(CURATOR_USER_ID, element.guid(), null);

        assertEquals(List.of(OPEN_ZONE), currentZones(element),
                     "Publishing should replace the element's zones with the curator's publish zones, not add to them");

        assertDoesNotThrow(() -> getElement(employeeClient, EMPLOYEE_USER_ID, element.guid()),
                           EMPLOYEE_USER_ID + " should be able to read the element once it is published to the open zone");
    }


    /**
     * Withdrawing sets the element's zones back to the account's default zones - exactly those zones, so the
     * zone the element was published to is taken away and the element is hidden again.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void withdrawingReplacesTheZonesWithTheAccountsDefaultZones() throws Exception
    {
        CreatedElement element = create(curatorClient, CURATOR_USER_ID, "withdraw-to-restricted", null);

        curatorClient.publishMetadataElement(CURATOR_USER_ID, element.guid(), null);

        assertEquals(List.of(OPEN_ZONE), currentZones(element), "The element should be in the open zone once published");

        curatorClient.withdrawMetadataElement(CURATOR_USER_ID, element.guid(), null);

        assertEquals(List.of(RESTRICTED_ZONE), currentZones(element),
                     "Withdrawing should replace the element's zones with the curator's default zones.  Leaving the open "
                             + "zone in place would leave the element as visible as it was before it was withdrawn");

        assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                      EMPLOYEE_USER_ID,
                      () -> getElement(employeeClient, EMPLOYEE_USER_ID, element.guid()),
                      EMPLOYEE_USER_ID + " reading an element that has been withdrawn");
    }


    /**
     * Publishing an element that is already in the publish zones, and withdrawing one that is already in the
     * default zones, changes nothing and is not an error.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void publishingAndWithdrawingAreRepeatable() throws Exception
    {
        CreatedElement element = create(curatorClient, CURATOR_USER_ID, "repeatable", null);

        curatorClient.publishMetadataElement(CURATOR_USER_ID, element.guid(), null);
        assertDoesNotThrow(() -> curatorClient.publishMetadataElement(CURATOR_USER_ID, element.guid(), null),
                           "Publishing an element that is already published should not fail");

        assertEquals(List.of(OPEN_ZONE), currentZones(element), "The element should still be in the open zone");

        curatorClient.withdrawMetadataElement(CURATOR_USER_ID, element.guid(), null);
        assertDoesNotThrow(() -> curatorClient.withdrawMetadataElement(CURATOR_USER_ID, element.guid(), null),
                           "Withdrawing an element that is already withdrawn should not fail");

        assertEquals(List.of(RESTRICTED_ZONE), currentZones(element), "The element should still be in the restricted zone");
    }


    /**
     * An account whose publishZones is an empty list says that a published element has no zones.  All of the
     * element's zones are removed, which makes it visible to everyone - and withdrawing puts it back in the
     * account's default zone.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anEmptyPublishZonesListRemovesAllOfTheZones() throws Exception
    {
        CreatedElement element = create(releaserClient, RELEASER_USER_ID, "publish-to-everyone", null);

        assertEquals(List.of(RESTRICTED_ZONE), currentZones(element), "The releaser's new element should start in the releaser's default zone");

        releaserClient.publishMetadataElement(RELEASER_USER_ID, element.guid(), null);

        assertNoZones(element, "Publishing with an empty publishZones list should remove all of the element's zones");

        assertDoesNotThrow(() -> getElement(employeeClient, EMPLOYEE_USER_ID, element.guid()),
                           EMPLOYEE_USER_ID + " should be able to read an element that has had all of its zones removed");

        assertDoesNotThrow(() -> releaserClient.publishMetadataElement(RELEASER_USER_ID, element.guid(), null),
                           "Publishing an element that already has no zones should not fail");

        releaserClient.withdrawMetadataElement(RELEASER_USER_ID, element.guid(), null);

        assertEquals(List.of(RESTRICTED_ZONE), currentZones(element),
                     "Withdrawing should put the zone classification back, with the releaser's default zones in it");
    }


    /**
     * A publishZones or defaultZones property with no value - null - is defined, and means "no zones" just as an
     * empty list does.  The opener's account has both properties with no value, so the opener both publishes and
     * withdraws an element to no zones at all.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNullZoneSettingRemovesAllOfTheZones() throws Exception
    {
        CreatedElement published = create(openerClient, OPENER_USER_ID, "null-publish-zones", List.of(RESTRICTED_ZONE));

        assertEquals(List.of(RESTRICTED_ZONE), currentZones(published), "The element should start in the zone it was created in");

        openerClient.publishMetadataElement(OPENER_USER_ID, published.guid(), null);

        assertNoZones(published, "Publishing with a null publishZones property should remove all of the element's zones");

        CreatedElement withdrawn = create(openerClient, OPENER_USER_ID, "null-default-zones", List.of(RESTRICTED_ZONE));

        openerClient.withdrawMetadataElement(OPENER_USER_ID, withdrawn.guid(), null);

        assertNoZones(withdrawn, "Withdrawing with a null defaultZones property should remove all of the element's zones");
    }


    /**
     * An account with no publishZones property does not say what publishing means for the user.  The request is
     * refused, with a message that names the missing property, and the element keeps the zones it had.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void publishingIsRefusedWhenTheAccountHasNoPublishZones() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "no-publish-zones", List.of(RESTRICTED_ZONE));

        UserNotAuthorizedException error = assertRefused(UNDEFINED_ZONES_FOR_USER,
                                                         STEWARD_USER_ID,
                                                         () -> stewardClient.publishMetadataElement(STEWARD_USER_ID, element.guid(), null),
                                                         STEWARD_USER_ID + " publishing an element with no publishZones on the account");

        assertTrue(error.getReportedErrorMessage().contains("publishZones"),
                   "The refusal should name the property that is missing.  Message: " + error.getReportedErrorMessage());

        assertEquals(List.of(RESTRICTED_ZONE), currentZones(element), "An element that could not be published should keep its zones");
    }


    /**
     * An account with no defaultZones property does not say what withdrawing means for the user.  The request is
     * refused, with a message that names the missing property, and the element keeps the zones it had.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void withdrawingIsRefusedWhenTheAccountHasNoDefaultZones() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "no-default-zones", List.of(OPEN_ZONE));

        UserNotAuthorizedException error = assertRefused(UNDEFINED_ZONES_FOR_USER,
                                                         STEWARD_USER_ID,
                                                         () -> stewardClient.withdrawMetadataElement(STEWARD_USER_ID, element.guid(), null),
                                                         STEWARD_USER_ID + " withdrawing an element with no defaultZones on the account");

        assertTrue(error.getReportedErrorMessage().contains("defaultZones"),
                   "The refusal should name the property that is missing.  Message: " + error.getReportedErrorMessage());

        assertEquals(List.of(OPEN_ZONE), currentZones(element), "An element that could not be withdrawn should keep its zones");
    }


    /**
     * An element with no zones that could not be published still has none - nothing is added on the way to the
     * refusal.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRefusedPublishDoesNotAddAZoneClassification() throws Exception
    {
        CreatedElement element = create(stewardClient, STEWARD_USER_ID, "refused-and-unzoned", null);

        assertRefused(UNDEFINED_ZONES_FOR_USER,
                      STEWARD_USER_ID,
                      () -> stewardClient.publishMetadataElement(STEWARD_USER_ID, element.guid(), null),
                      STEWARD_USER_ID + " publishing an element with no publishZones on the account");

        assertNull(currentZones(element), "An element with no zones that could not be published should still have no zone classification");
    }
}
