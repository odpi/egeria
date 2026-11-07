/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.CreatedElement;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.EMPLOYEE_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.METADATA_STORE_NAME;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.OPERATOR_USER_ID;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.RESTRICTED_ZONE;
import static org.odpi.openmetadata.securityfvt.OMAGPlatformExtension.STEWARD_USER_ID;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.UNAUTHORIZED_ELEMENT_ACCESS;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.assertRefused;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.createCollection;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.deleteElement;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.getElement;
import static org.odpi.openmetadata.securityfvt.SecurityFvtTestSupport.searchFinds;

/**
 * AnchorsZoneResyncFVT covers the search filter on governance zones and the resynchronization that keeps it
 * correct.  The suite's server runs with the filter on, the default (see SecurityFvtTestSupport.securityConnection), so a
 * search leaves out, in the repository, the elements whose Anchors classification lists only zones the caller
 * can not read.  The filter is only right if those zones are: an element whose Anchors classification is out
 * of date is hidden from users who are entitled to see it.
 * <br><br>
 * The tests load an archive holding an anchor in the open zone and a member of it, both with Anchors
 * classifications that wrongly say the restricted zone (see StaleAnchorsZoneArchiveWriter).  They show the
 * wrong result the stale value causes, that resynchronizing corrects it, that a second pass finds nothing to
 * correct, and that a caller can only resynchronize an element it can read.  Two more elements are in no zone:
 * one loaded with no Anchors classification, which every search returns, and one whose Anchors classification holds an
 * empty zone list, which is returned once the resynchronization has rewritten it.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnchorsZoneResyncFVT
{
    private static EgeriaOpenMetadataStoreClient stewardClient;
    private static EgeriaOpenMetadataStoreClient employeeClient;

    private static final CreatedElement ANCHOR = new CreatedElement(StaleAnchorsZoneArchiveWriter.ANCHOR_GUID,
                                                                    StaleAnchorsZoneArchiveWriter.ANCHOR_QUALIFIED_NAME);
    private static final CreatedElement MEMBER = new CreatedElement(StaleAnchorsZoneArchiveWriter.MEMBER_GUID,
                                                                    StaleAnchorsZoneArchiveWriter.MEMBER_QUALIFIED_NAME);
    private static final CreatedElement NO_ANCHORS  = new CreatedElement(StaleAnchorsZoneArchiveWriter.NO_ANCHORS_GUID,
                                                                         StaleAnchorsZoneArchiveWriter.NO_ANCHORS_QUALIFIED_NAME);
    private static final CreatedElement EMPTY_ZONES = new CreatedElement(StaleAnchorsZoneArchiveWriter.EMPTY_ZONES_GUID,
                                                                         StaleAnchorsZoneArchiveWriter.EMPTY_ZONES_QUALIFIED_NAME);


    @BeforeAll
    static void loadStaleArchive() throws Exception
    {
        stewardClient  = SecurityFvtTestSupport.storeClientAs(STEWARD_USER_ID);
        employeeClient = SecurityFvtTestSupport.storeClientAs(EMPLOYEE_USER_ID);

        File archiveFile = new File(OMAGPlatformExtension.DATA_DIRECTORY + "/archives/StaleAnchorsZoneArchive.omarchive").getAbsoluteFile();

        new StaleAnchorsZoneArchiveWriter().writeArchive(archiveFile);

        PlatformServicesClient platformServicesClient = SecurityFvtTestSupport.platformClientAs(OPERATOR_USER_ID);

        platformServicesClient.addOpenMetadataArchiveFile(METADATA_STORE_NAME, archiveFile.getAbsolutePath());
    }


    /**
     * The stale Anchors zones hide the elements from a search by a user who may read them.  This is the wrong
     * result that makes the resynchronization necessary before the filter is enabled.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    @Order(1)
    void staleAnchorsZonesHideReadableElementsFromSearches() throws Exception
    {
        for (CreatedElement element : List.of(ANCHOR, MEMBER))
        {
            assertDoesNotThrow(() -> getElement(employeeClient, EMPLOYEE_USER_ID, element.guid()),
                               EMPLOYEE_USER_ID + " should be able to read " + element.qualifiedName() + " because it is governed by the open zone");

            assertTrue(searchFinds(stewardClient, STEWARD_USER_ID, element),
                       STEWARD_USER_ID + " should find " + element.qualifiedName() + ", since the steward can read the restricted zone too");

            assertFalse(searchFinds(employeeClient, EMPLOYEE_USER_ID, element),
                        "The stale Anchors zones of " + element.qualifiedName() + " should hide it from " + EMPLOYEE_USER_ID +
                                "'s search - if not, the search is not being filtered by zone");
        }

        assertTrue(searchFinds(employeeClient, EMPLOYEE_USER_ID, NO_ANCHORS),
                   EMPLOYEE_USER_ID + " should find an element in no zone that was loaded with no Anchors classification");

        assertDoesNotThrow(() -> getElement(employeeClient, EMPLOYEE_USER_ID, EMPTY_ZONES.guid()),
                           EMPLOYEE_USER_ID + " should be able to read an element whose zone list is empty, since it is in no zone");

        assertFalse(searchFinds(employeeClient, EMPLOYEE_USER_ID, EMPTY_ZONES),
                    "An empty zone list in the Anchors classification should hide the element until it is resynchronized - " +
                            "if this now passes, the resync's rewrite of empty lists may no longer be needed");
    }


    /**
     * Resynchronizing the anchor corrects both Anchors classifications, after which the employee finds both
     * elements.  A second pass has nothing to correct.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    @Order(2)
    void resynchronizingTheAnchorCorrectsItAndItsMembers() throws Exception
    {
        long corrected = stewardClient.resynchronizeAnchorsZoneMembership(STEWARD_USER_ID, ANCHOR.guid(), new MetadataSourceOptions());

        assertEquals(2L, corrected, "Both the anchor's and the member's Anchors classifications were stale");

        for (CreatedElement element : List.of(ANCHOR, MEMBER))
        {
            assertTrue(searchFinds(employeeClient, EMPLOYEE_USER_ID, element),
                       EMPLOYEE_USER_ID + " should find " + element.qualifiedName() + " once its Anchors zones are correct");
        }

        assertEquals(0L,
                     stewardClient.resynchronizeAnchorsZoneMembership(STEWARD_USER_ID, ANCHOR.guid(), new MetadataSourceOptions()),
                     "A second resynchronization of the anchor should find nothing to correct");
    }


    /**
     * Resynchronizing the whole repository corrects the empty zone list, after which both elements in no zone are
     * found, and leaves nothing to correct, so a second pass corrects none.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    @Order(3)
    void resynchronizingTheRepositoryIsRepeatable() throws Exception
    {
        long firstPass = stewardClient.resynchronizeAnchorsZoneMembership(STEWARD_USER_ID, null, new MetadataSourceOptions());

        System.out.println("security-fvt: the first resynchronization of the repository corrected " + firstPass + " Anchors classifications");

        assertTrue(firstPass >= 1, "The empty zone list in " + EMPTY_ZONES.qualifiedName() + " should have been rewritten");

        for (CreatedElement element : List.of(NO_ANCHORS, EMPTY_ZONES))
        {
            assertTrue(searchFinds(employeeClient, EMPLOYEE_USER_ID, element),
                       EMPLOYEE_USER_ID + " should find " + element.qualifiedName() + ", which is in no zone");
        }

        assertEquals(0L,
                     stewardClient.resynchronizeAnchorsZoneMembership(STEWARD_USER_ID, null, new MetadataSourceOptions()),
                     "A second resynchronization of the repository should find nothing to correct");
    }


    /**
     * A caller can only resynchronize an element it may read; asking about one it can not see is refused rather
     * than reporting anything about it.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    @Order(4)
    void anElementTheCallerCanNotReadIsRefused() throws Exception
    {
        CreatedElement restricted = createCollection(stewardClient, STEWARD_USER_ID, "resync-restricted", List.of(RESTRICTED_ZONE), null);

        try
        {
            assertRefused(UNAUTHORIZED_ELEMENT_ACCESS,
                          EMPLOYEE_USER_ID,
                          () -> employeeClient.resynchronizeAnchorsZoneMembership(EMPLOYEE_USER_ID, restricted.guid(), new MetadataSourceOptions()),
                          EMPLOYEE_USER_ID + " resynchronizing an element in a zone it can not read");

            assertEquals(0L,
                         stewardClient.resynchronizeAnchorsZoneMembership(STEWARD_USER_ID, restricted.guid(), new MetadataSourceOptions()),
                         "An element created through the access services should already have correct Anchors zones");
        }
        finally
        {
            deleteElement(stewardClient, STEWARD_USER_ID, restricted.guid());
        }
    }
}
