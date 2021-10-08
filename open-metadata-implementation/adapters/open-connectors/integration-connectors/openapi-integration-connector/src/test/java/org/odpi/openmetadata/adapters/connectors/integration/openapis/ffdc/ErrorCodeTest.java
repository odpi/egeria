/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.ffdc;

import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the OpenAPIIntegrationConnectorErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest extends ExceptionMessageSetTest
{
    final static String  messageIdPrefix = "OPEN-API-INTEGRATION-CONNECTOR";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (OpenAPIIntegrationConnectorErrorCode errorCode : OpenAPIIntegrationConnectorErrorCode.values())
        {
            super.testSingleErrorCodeValue(errorCode, messageIdPrefix);
        }
    }
}
