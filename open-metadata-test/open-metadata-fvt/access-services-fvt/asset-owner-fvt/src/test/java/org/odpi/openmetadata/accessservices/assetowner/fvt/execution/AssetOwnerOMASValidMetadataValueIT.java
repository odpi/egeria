/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.fvt.execution;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.odpi.openmetadata.accessservices.assetowner.fvt.validmetadatavalues.ValidMetadataValuesTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTTestFailureException;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * AssetOwnerOMASValidMetadataValueIT is the failsafe wrapper for ValidMetadataValuesTest.
 */
public class AssetOwnerOMASValidMetadataValueIT
{
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
    @ValueSource(strings = {"serverinmem"})
    public void testValidMetadataValue(String server)
    {
        assertDoesNotThrow(() -> runValidMetadataValue(server));
    }


    public void runValidMetadataValue(String serverName) throws FVTTestFailureException
    {
        FVTResults results = ValidMetadataValuesTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT)), FVTConstants.USERID);

        results.printResults(serverName);
        if (! results.isSuccessful())
        {
            throw new FVTTestFailureException(results);
        }
    }
}
