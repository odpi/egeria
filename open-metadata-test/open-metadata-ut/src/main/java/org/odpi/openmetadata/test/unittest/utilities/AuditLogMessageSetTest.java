/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.test.unittest.utilities;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;

import static org.testng.Assert.*;

/**
 * AuditLogMessageSetTest is a base class for unit test cases that are validating audit log message sets.
 * It contains utilities the validate the message set enum contains unique message ids, non-null names
 * and descriptions and can be serialized to JSON and back again.
 */
public abstract class AuditLogMessageSetTest extends MessageSetTest
{
    /**
     * Validate a single message.
     *
     * @param testMessageValue single message
     * @param messageIdPrefix prefix for message id
     */
    public  void testSingleAuditCodeValue(AuditLogMessageSet testMessageValue,
                                          String              messageIdPrefix)
    {
        AuditLogMessageDefinition messageDefinition =
                testMessageValue.getMessageDefinition("Field1", "Field2", "Field3", "Field4", "Field5", "Field6");

        super.testSingleMessageDefinitionValues(messageDefinition, messageIdPrefix, "Field1", "Field2", "Field3", "Field4", "Field5", "Field6");
        assertNotNull(messageDefinition.getSeverity());
    }
}
