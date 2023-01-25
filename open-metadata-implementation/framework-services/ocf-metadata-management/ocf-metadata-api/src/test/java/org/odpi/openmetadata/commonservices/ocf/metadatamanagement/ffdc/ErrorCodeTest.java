/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc;

import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the exception message set enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (OMAGOCFErrorCode errorCode : OMAGOCFErrorCode.values())
        {
            super.testSingleErrorCodeValue(errorCode, "CONNECTED-ASSET-SERVICES");
        }
    }
}
