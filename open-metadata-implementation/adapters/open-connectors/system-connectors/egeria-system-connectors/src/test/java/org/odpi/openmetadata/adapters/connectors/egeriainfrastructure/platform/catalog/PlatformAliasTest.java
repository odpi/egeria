/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;


/**
 * Verify that PlatformAliasCheck recognises two addresses of one running platform, so that
 * the platform is catalogued once rather than once per address.
 */
public class PlatformAliasTest
{
    private static final String LOCAL_ADDRESS    = "https://localhost:9443";
    private static final String EXTERNAL_ADDRESS = "https://host.docker.internal:9443";
    private static final String OTHER_ADDRESS    = "https://other-host:9443";
    private static final String ORIGIN           = "Egeria OMAG Server Platform (version 6.3-SNAPSHOT)";

    private static final PlatformAliasCheck.PlatformIdentity QUICKSTART =
            new PlatformAliasCheck.PlatformIdentity(ORIGIN, new Date(1_760_000_000_000L));

    private static final PlatformAliasCheck.PlatformIdentity OTHER_PLATFORM =
            new PlatformAliasCheck.PlatformIdentity(ORIGIN, new Date(1_760_000_123_456L));


    /**
     * A reader that answers from a fixed map and records which addresses it was asked about.
     */
    private static class MapReader implements PlatformAliasCheck.PlatformIdentityReader
    {
        private final Map<String, PlatformAliasCheck.PlatformIdentity> identities;
        private final List<String> calls = new ArrayList<>();

        MapReader(Map<String, PlatformAliasCheck.PlatformIdentity> identities)
        {
            this.identities = identities;
        }

        @Override
        public PlatformAliasCheck.PlatformIdentity getPlatformIdentity(String platformURLRoot) throws Exception
        {
            calls.add(platformURLRoot);

            if (! identities.containsKey(platformURLRoot))
            {
                throw new Exception("Platform at " + platformURLRoot + " is not reachable");
            }

            return identities.get(platformURLRoot);
        }
    }


    /**
     * The quickstart case: the same platform reached internally and externally is one platform.
     */
    @Test public void testSamePlatformUnderTwoAddresses() throws Exception
    {
        MapReader reader = new MapReader(Map.of(LOCAL_ADDRESS, QUICKSTART, EXTERNAL_ADDRESS, QUICKSTART));

        assertEquals(PlatformAliasCheck.findEarlierAddressOfSamePlatform(EXTERNAL_ADDRESS,
                                                                                         List.of(LOCAL_ADDRESS),
                                                                                         reader),
                     LOCAL_ADDRESS);
    }


    /**
     * A daemon running on a different platform from its metadata access server: both platforms are catalogued.
     */
    @Test public void testDifferentPlatforms() throws Exception
    {
        MapReader reader = new MapReader(Map.of(LOCAL_ADDRESS, QUICKSTART, OTHER_ADDRESS, OTHER_PLATFORM));

        assertNull(PlatformAliasCheck.findEarlierAddressOfSamePlatform(OTHER_ADDRESS,
                                                                                       List.of(LOCAL_ADDRESS),
                                                                                       reader));
    }


    /**
     * The first address has nothing to compare with, so no platform is called at all.
     */
    @Test public void testFirstAddressCallsNoPlatform() throws Exception
    {
        MapReader reader = new MapReader(Map.of());

        assertNull(PlatformAliasCheck.findEarlierAddressOfSamePlatform(LOCAL_ADDRESS, List.of(), reader));
        assertTrue(reader.calls.isEmpty());
    }


    /**
     * A platform that cannot be asked is not guessed about: the exception reaches the caller, which defers
     * the registration to the next refresh.
     */
    @Test public void testUnreachablePlatformIsNotGuessed()
    {
        MapReader reader = new MapReader(Map.of(EXTERNAL_ADDRESS, QUICKSTART));

        assertThrows(Exception.class,
                     () -> PlatformAliasCheck.findEarlierAddressOfSamePlatform(EXTERNAL_ADDRESS,
                                                                                               List.of(LOCAL_ADDRESS),
                                                                                               reader));
    }


    /**
     * Without a start time there is no telling two platforms apart, so they are treated as different.
     */
    @Test public void testMissingStartTimeIsNotTheSamePlatform()
    {
        PlatformAliasCheck.PlatformIdentity noStartTime =
                new PlatformAliasCheck.PlatformIdentity(ORIGIN, null);

        assertFalse(noStartTime.isSamePlatformAs(noStartTime));
        assertFalse(QUICKSTART.isSamePlatformAs(noStartTime));
        assertFalse(QUICKSTART.isSamePlatformAs(null));
    }


    /**
     * Same start time but a different build is a different platform.
     */
    @Test public void testDifferentOriginIsNotTheSamePlatform()
    {
        PlatformAliasCheck.PlatformIdentity otherBuild =
                new PlatformAliasCheck.PlatformIdentity("Some other origin", QUICKSTART.platformStartTime());

        assertFalse(QUICKSTART.isSamePlatformAs(otherBuild));
        assertTrue(QUICKSTART.isSamePlatformAs(new PlatformAliasCheck.PlatformIdentity(ORIGIN,
                                                                                                        new Date(QUICKSTART.platformStartTime().getTime()))));
    }
}
