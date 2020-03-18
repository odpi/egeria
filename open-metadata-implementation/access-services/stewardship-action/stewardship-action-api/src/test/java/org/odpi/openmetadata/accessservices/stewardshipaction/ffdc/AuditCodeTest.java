/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.ffdc;

import org.odpi.openmetadata.test.unittest.utilities.AuditLogMessageSetTest;
import org.testng.annotations.Test;

/**
 * Verify the StewardshipActionAuditCodeCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class AuditCodeTest extends AuditLogMessageSetTest
{
    final static String  messageIdPrefix = "OMAS-STEWARDSHIP-ACTION";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllAuditCodeValues()
    {
        for (StewardshipActionAuditCode auditCode : StewardshipActionAuditCode.values())
        {
            testSingleAuditCodeValue(auditCode, messageIdPrefix);
        }
    }
}
