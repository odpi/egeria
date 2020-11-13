/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc;


import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the CommunityProfileErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    final static String  messageIdPrefix = "OMAS-COMMUNITY-PROFILE";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (CommunityProfileErrorCode errorCode : CommunityProfileErrorCode.values())
        {
            testSingleErrorCodeValue(errorCode, messageIdPrefix);
        }
    }
}
