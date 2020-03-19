/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc;


import org.odpi.openmetadata.test.unittest.utilities.AuditLogMessageSetTest;
import org.odpi.openmetadata.test.unittest.utilities.ExceptionMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the AssetConsumerAuditCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class AuditCodeTest extends AuditLogMessageSetTest
{
    final static String  messageIdPrefix = "OMAS-ASSET-CONSUMER";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (AssetConsumerAuditCode auditCode : AssetConsumerAuditCode.values())
        {
            super.testSingleAuditCodeValue(auditCode, messageIdPrefix);
        }
    }
}
