/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.test.unittest.utilities;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

import static org.testng.Assert.*;

/**
 * ExceptionMessageSetTest is a base class for unit test cases that are validating exception message sets.
 * It contains utilities the validate the message set enum contains unique message ids, non-null names
 * and descriptions and can be serialized to JSON and back again.
 */
public abstract class ExceptionMessageSetTest extends MessageSetTest
{
    /**
     * Validate a single message.
     *
     * @param testMessageValue single message
     * @param messageIdPrefix prefix for message id
     */
    protected void testSingleErrorCodeValue(ExceptionMessageSet testMessageValue,
                                            String              messageIdPrefix)
    {
        ExceptionMessageDefinition messageDefinition =
                testMessageValue.getMessageDefinition("Field1", "Field2", "Field3", "Field4", "Field5", "Field6");

        super.testSingleMessageDefinitionValues(messageDefinition, messageIdPrefix, "Field1", "Field2", "Field3", "Field4", "Field5", "Field6");
        assertNotEquals(0, messageDefinition.getHttpErrorCode());
    }
}
