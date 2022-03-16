/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.fvt.execution;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.governanceprogram.fvt.definitions.CreateDefinitionTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GovernanceProgramOMASCreateDefinitionIT is the failsafe wrapper for CreateDefinitionTest.
 */
public class GovernanceProgramOMASCreateDefinitionIT
{
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testCreateDB(String serverName)
    {
        FVTResults results = CreateDefinitionTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }
}
