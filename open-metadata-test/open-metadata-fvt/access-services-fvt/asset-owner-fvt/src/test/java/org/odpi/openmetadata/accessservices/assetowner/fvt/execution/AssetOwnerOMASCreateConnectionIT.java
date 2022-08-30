/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.fvt.execution;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.odpi.openmetadata.accessservices.assetowner.fvt.connections.CreateConnectionTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AssetOwnerOMASCreateConnectionIT is the failsafe wrapper for CreateConnectionTest.
 */
public class AssetOwnerOMASCreateConnectionIT
{
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testCreateConnection(String serverName)
    {
        FVTResults results = CreateConnectionTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }
}
