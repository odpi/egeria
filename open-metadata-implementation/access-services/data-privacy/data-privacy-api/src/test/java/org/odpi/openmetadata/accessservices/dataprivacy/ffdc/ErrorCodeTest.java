/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.ffdc;


import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the DataPrivacyErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    final static String  messageIdPrefix = "OMAS-DATA-PRIVACY";


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (DataPrivacyErrorCode errorCode : DataPrivacyErrorCode.values())
        {
            testSingleErrorCodeValue(errorCode, messageIdPrefix);
        }
    }
}
