/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.ffdc;

import org.testng.annotations.Test;
import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;


/**
 * Validate the methods of OMRSErrorCode
 */
public class TestOMRSErrorCode extends ExceptionMessageSetTest
{
    private final static String       messageIdPrefix    = "OMRS-";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (OMRSErrorCode errorCode : OMRSErrorCode.values())
        {
            super.testSingleErrorCodeValue(errorCode, messageIdPrefix);
        }
    }

}
