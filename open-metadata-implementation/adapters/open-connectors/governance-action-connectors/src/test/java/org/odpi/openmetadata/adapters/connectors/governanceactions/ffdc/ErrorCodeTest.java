/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc;

import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the GovernanceActionConnectorsErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    final static String  messageIdPrefix = "GOVERNANCE-ACTION-CONNECTORS";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (GovernanceActionConnectorsErrorCode errorCode : GovernanceActionConnectorsErrorCode.values())
        {
            super.testSingleErrorCodeValue(errorCode, messageIdPrefix);
        }
    }
}
