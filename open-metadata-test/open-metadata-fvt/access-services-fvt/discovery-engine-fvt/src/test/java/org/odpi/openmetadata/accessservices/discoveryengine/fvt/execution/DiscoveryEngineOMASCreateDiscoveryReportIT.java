/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.fvt.execution;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.discoveryengine.fvt.discovery.CreateDiscoveryReportTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DiscoveryEngineOMASCreateDiscoveryReportIT is the failsafe wrapper for CreateDiscoveryReportTest.
 */
public class DiscoveryEngineOMASCreateDiscoveryReportIT
{
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testCreateDiscoveryReport(String serverName)
    {
        FVTResults results = CreateDiscoveryReportTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }
}
