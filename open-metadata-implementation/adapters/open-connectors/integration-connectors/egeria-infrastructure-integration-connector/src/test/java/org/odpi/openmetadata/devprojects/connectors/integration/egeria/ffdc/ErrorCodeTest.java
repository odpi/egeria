/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.egeria.ffdc;

import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the EgeriaInfrastructureConnectorErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    final static String messageIdPrefix1 = "EGERIA-INFRASTRUCTURE-CONNECTORS";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues1()
    {
        for (EgeriaInfrastructureConnectorErrorCode errorCode : EgeriaInfrastructureConnectorErrorCode.values())
        {
            super.testSingleErrorCodeValue(errorCode, messageIdPrefix1);
        }
    }
}
