/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.execution;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.glossaries.CreateExchangeGlossaryTest;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.glossaries.CreateManagementGlossaryTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AssetManagerOMASCreateGlossaryIT is the failsafe wrapper for CreateExchangeGlossaryTest and CreateManagementGlossaryTest.
 */
public class AssetManagerOMASCreateGlossaryIT
{
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testExchangeGlossary(String serverName)
    {
        FVTResults results = CreateExchangeGlossaryTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }

    @ParameterizedTest
    @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testManagementGlossary(String serverName)
    {
        FVTResults results = CreateManagementGlossaryTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }
}
