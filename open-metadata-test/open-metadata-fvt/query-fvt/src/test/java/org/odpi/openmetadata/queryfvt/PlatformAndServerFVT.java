/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PlatformAndServerFVT checks the fundamentals that every other test in this suite relies on: the OMAG
 * Server Platform is up and responding, the metadata access store server that
 * {@link OMAGPlatformExtension} configured and started is active on it, and the content-packs archives
 * it was configured to load actually loaded into the PostgreSQL repository.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PlatformAndServerFVT
{
    @Test
    void platformIsRunning() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("query-fvt Platform",
                                                                                    OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    OMAGPlatformExtension.USER_ID,
                                                                                    null);

        String origin = platformServicesClient.getPlatformOrigin();

        assertNotNull(origin, "Platform origin response should not be null");
        assertFalse(origin.isBlank(), "Platform origin response should not be blank");
    }


    @Test
    void serverIsActive() throws Exception
    {
        PlatformServicesClient platformServicesClient = new PlatformServicesClient("query-fvt Platform",
                                                                                    OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    OMAGPlatformExtension.USER_ID,
                                                                                    null);

        assertTrue(platformServicesClient.isServerKnown(OMAGPlatformExtension.SERVER_NAME),
                   "Server " + OMAGPlatformExtension.SERVER_NAME + " should be known to the platform");

        List<String> activeServers = platformServicesClient.getActiveServers();

        assertNotNull(activeServers, "Active server list should not be null");
        assertTrue(activeServers.contains(OMAGPlatformExtension.SERVER_NAME),
                   "Server " + OMAGPlatformExtension.SERVER_NAME + " should be active, active servers are: " + activeServers);
    }


    @Test
    void archiveContentLoaded() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore     openMetadataStore = connectorContext.getOpenMetadataStore();

        long totalElementCount = openMetadataStore.countMetadataElements(null, null, null, null);

        assertTrue(totalElementCount > 1000,
                   "Expected many thousands of elements from the content-packs archives to already be in " +
                           "the repository, but countMetadataElements(null,...) returned only " + totalElementCount);
    }
}
