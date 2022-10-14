/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.execution;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.odpi.openmetadata.accessservices.communityprofile.fvt.validvalues.CreateValidValuesSetTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CommunityProfileOMASCreateProfileIT is the failsafe wrapper for CreateValidValuesSetTest.
 */
public class CommunityProfileOMASCreateValidValuesIT
{
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testCreateValidValues(String serverName)
    {
        FVTResults results = CreateValidValuesSetTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }
}
