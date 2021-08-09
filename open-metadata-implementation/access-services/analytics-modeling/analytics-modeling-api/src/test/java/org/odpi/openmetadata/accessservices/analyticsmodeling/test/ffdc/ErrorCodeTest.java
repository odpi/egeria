/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.ffdc;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;

/**
 * Verify the AnalyticsModelingErrorCode enum contains unique message ids,
 * non-null names, descriptions and can be serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    final static String  messageIdPrefix = "OMAS-ANALYTICS-MODELING";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (AnalyticsModelingErrorCode errorCode : AnalyticsModelingErrorCode.values())
        {
            super.testSingleErrorCodeValue(errorCode, messageIdPrefix);
        }
    }
}
