/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.ffdc;


import org.odpi.openmetadata.test.unittest.utilities.AuditLogMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the SecurityManagerAuditCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class AuditCodeTest extends AuditLogMessageSetTest
{
    final static String  messageIdPrefix = "OMAS-SECURITY-MANAGER";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (SecurityManagerAuditCode auditCode : SecurityManagerAuditCode.values())
        {
            super.testSingleAuditCodeValue(auditCode, messageIdPrefix);
        }
    }
}
