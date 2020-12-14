/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.test.unittest.utilities;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;


/**
 * MessageSetTest is a base class for unit test cases that are validating exception message sets.
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
        assertNotNull(messageDefinition.getMessageId(), "Message definition "+messageDefinition.toString()+" has null messageId");
        assertNotNull(messageDefinition.getMessageTemplate(), "Message definition "+messageDefinition.toString()+" has null messageTemplate");
        assertNotNull(messageDefinition.getSystemAction(), "Message definition "+messageDefinition.toString()+" has null system action");
        assertNotNull(messageDefinition.getUserAction(), "Message definition "+messageDefinition.toString()+" has null user action");

        assertNotEquals("", messageDefinition.getMessageId(), "Message definition "+messageDefinition.toString()+" has empty messageId");
        assertNotEquals("", messageDefinition.getMessageTemplate(), "Message definition "+messageDefinition.toString()+" has empty message template");
        assertNotEquals("", messageDefinition.getSystemAction(), "Message definition "+messageDefinition.toString()+" has empty system action");
        assertNotEquals("", messageDefinition.getUserAction(), "Message definition "+messageDefinition.toString()+" has empty user action");

        assertTrue(isUniqueOrdinal(messageDefinition.getMessageId()), "Message definition "+messageDefinition.getMessageId()+" does not have a unique messageId");
        assertTrue(isUniqueMessageTemplate(messageDefinition.getMessageTemplate()), "Message definition "+messageDefinition.getMessageId()+" does not have a " +
                "unique messageTemplate");
        assertTrue(isUniqueSystemAction(messageDefinition.getSystemAction()), "Message definition "+messageDefinition.getMessageId()+" does not have a unique " +
                "system action");
        assertTrue(isUniqueUserAction(messageDefinition.getUserAction()), "Message definition "+messageDefinition.getMessageId()+" does not have a unique user " +
                "action");
        assertTrue(messageDefinition.getMessageId().startsWith(messageIdPrefix), "Message definition "+messageDefinition.getMessageId()+" does not start with the " +
                "correct message prefix: "+messageIdPrefix);
        assertFalse(messageDefinition.getMessageId().endsWith(" "), "Message definition "+messageDefinition.getMessageId()+" has a space at the end of its messageId");

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
