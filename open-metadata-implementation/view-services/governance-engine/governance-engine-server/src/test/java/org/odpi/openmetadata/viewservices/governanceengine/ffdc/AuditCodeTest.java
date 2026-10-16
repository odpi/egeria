/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceengine.ffdc;

import org.odpi.openmetadata.test.unittest.utilities.AuditLogMessageSetTest;
import org.testng.annotations.Test;


/**
 * Verify the GovernanceEngineAuditCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class AuditCodeTest extends AuditLogMessageSetTest
{
    final static String  messageIdPrefix = "OMVS-GOVERNANCE-ENGINE";

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllAuditCodeValues()
    {
        for (GovernanceEngineAuditCode errorCode : GovernanceEngineAuditCode.values())
        {
            super.testSingleAuditCodeValue(errorCode, messageIdPrefix);
        }
    }
}
