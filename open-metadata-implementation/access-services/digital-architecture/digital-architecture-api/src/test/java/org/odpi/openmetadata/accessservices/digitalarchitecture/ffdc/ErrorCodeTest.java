/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc;


import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;



/**
 * Verify the DigitalArchitectureErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    final static String  messageIdPrefix = "OMAS-DIGITAL-ARCHITECTURE";


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (DigitalArchitectureErrorCode errorCode : DigitalArchitectureErrorCode.values())
        {
            testSingleErrorCodeValue(errorCode, messageIdPrefix);
        }
    }
}