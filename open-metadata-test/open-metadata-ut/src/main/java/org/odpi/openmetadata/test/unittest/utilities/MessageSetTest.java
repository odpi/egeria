/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.test.unittest.utilities;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;


/**
 * ExceptionMessageSetTest is a base class for unit test cases that are validating exception message sets.
 * It contains utilities the validate the message set enum contains unique message ids, non-null names
 * and descriptions and can be serialized to JSON and back again.
 */
abstract class MessageSetTest
{
    private List<String> existingMessageIds       = new ArrayList<>();
    private List<String> existingMessageTemplates = new ArrayList<>();
    private List<String> existingSystemActions    = new ArrayList<>();
    private List<String> existingUserActions      = new ArrayList<>();

    /**
     * Validate that a supplied ordinal is unique.
     *
     * @param ordinal value to test
     * @return boolean result
     */
    private boolean isUniqueOrdinal(String  ordinal)
    {
        if (existingMessageIds.contains(ordinal))
        {
            return false;
        }
        else
        {
            existingMessageIds.add(ordinal);
            return true;
        }
    }


    /**
     * Validate that a supplied message template is unique.
     *
     * @param template value to test
     * @return boolean result
     */
    private boolean isUniqueMessageTemplate(String  template)
    {
        if (existingMessageTemplates.contains(template))
        {
            return false;
        }
        else
        {
            existingMessageTemplates.add(template);
            return true;
        }
    }


    /**
     * Validate that a supplied system action is unique.
     *
     * @param systemAction value to test
     * @return boolean result
     */
    private boolean isUniqueSystemAction(String  systemAction)
    {
        if (existingSystemActions.contains(systemAction))
        {
            return false;
        }
        else
        {
            existingSystemActions.add(systemAction);
            return true;
        }
    }


    /**
     * Validate that a supplied user action is unique.
     *
     * @param userAction value to test
     * @return boolean result
     */
    private boolean isUniqueUserAction(String  userAction)
    {
        if (existingUserActions.contains(userAction))
        {
            return false;
        }
        else
        {
            existingUserActions.add(userAction);
            return true;
        }
    }


    /**
     * Validate the common values of a message definition.
     *
     * @param messageDefinition object to test
     * @param messageIdPrefix prefix of all message ids
     * @param messageParameters parameters added for message inserts
     */
    void testSingleMessageDefinitionValues(MessageDefinition messageDefinition,
                                           String            messageIdPrefix,
                                           String...         messageParameters)
    {
        assertNotNull(messageDefinition.getMessageId());
        assertNotNull(messageDefinition.getMessageTemplate());
        assertNotNull(messageDefinition.getSystemAction());
        assertNotNull(messageDefinition.getUserAction());

        assertNotEquals("", messageDefinition.getMessageId());
        assertNotEquals("", messageDefinition.getMessageTemplate());
        assertNotEquals("", messageDefinition.getSystemAction());
        assertNotEquals("", messageDefinition.getUserAction());

        assertTrue(isUniqueOrdinal(messageDefinition.getMessageId()));
        assertTrue(isUniqueMessageTemplate(messageDefinition.getMessageTemplate()));
        assertTrue(isUniqueSystemAction(messageDefinition.getSystemAction()));
        assertTrue(isUniqueUserAction(messageDefinition.getUserAction()));
        assertTrue(messageDefinition.getMessageId().startsWith(messageIdPrefix));
        assertFalse(messageDefinition.getMessageId().endsWith(" "));

        if (messageParameters.length == 0)
        {
            assertNull(messageDefinition.getMessageParams());
        }
        else
        {
            assertEquals(messageParameters, messageDefinition.getMessageParams());
        }
    }
}
