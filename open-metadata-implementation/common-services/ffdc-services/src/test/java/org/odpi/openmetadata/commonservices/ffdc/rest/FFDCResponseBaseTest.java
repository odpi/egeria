/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.test.unittest.utilities.BeanTestBase;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNull;

/**
 * Test the overridden methods of FFDCResponseBase
 */
public class FFDCResponseBaseTest extends BeanTestBase
{
    private static MessageFormatter           messageFormatter = new MessageFormatter();
    private            ExceptionMessageDefinition messageDefinition;

    private int       reportedHTTPCode           = 404;
    private String    reportedErrorMessageId     = "TestErrorMessageId";
    private String    reportedErrorMessage       = "TestErrorMessage";
    private String    reportedSystemAction       = "TestSystemAction";
    private String    reportedUserAction         = "TestUserAction";
    private String    actionDescription          = "TestActionDescription";
    private String    exceptionClassName         = NullPointerException.class.getName();

    private Map<String, Object> exceptionProperties = new HashMap<>();
    private Map<String, Object> emptyProperties     = new HashMap<>();


    /**
     * Default constructor
     */
    public FFDCResponseBaseTest()
    {
        messageDefinition = new ExceptionMessageDefinition(reportedHTTPCode,
                                                           reportedErrorMessageId,
                                                           reportedErrorMessage,
                                                           reportedSystemAction,
                                                           reportedUserAction);

        exceptionProperties.put("propertyName1", "propertyValue1");
    }


    /**
     * Set up the base class properties of an example object to test.
     */
    protected void fillTestObject(FFDCResponseBase  testObject)
    {
        testObject.setExceptionClassName(exceptionClassName);
        testObject.setExceptionErrorMessage(messageFormatter.getFormattedMessage(messageDefinition));
        testObject.setExceptionSystemAction(messageDefinition.getSystemAction());
        testObject.setExceptionUserAction(messageDefinition.getUserAction());

        testObject.setRelatedHTTPCode(400);
        testObject.setExceptionProperties(exceptionProperties);
        testObject.setActionDescription(actionDescription);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    protected void validateResultObjectBase(FFDCResponseBase  resultObject)
    {
        assertEquals(reportedHTTPCode, resultObject.getRelatedHTTPCode());

        assertEquals(exceptionClassName, resultObject.getExceptionClassName());
        assertEquals(actionDescription, resultObject.getActionDescription());
        assertEquals(exceptionProperties, resultObject.getExceptionProperties());

        assertEquals(messageDefinition.getHttpErrorCode(), resultObject.getRelatedHTTPCode());
        assertEquals(messageFormatter.getFormattedMessage(messageDefinition), resultObject.getExceptionErrorMessage());
        assertEquals(messageDefinition.getSystemAction(), resultObject.getExceptionSystemAction());
        assertEquals(messageDefinition.getUserAction(), resultObject.getExceptionUserAction());
    }


    /**
     * Validate that the object is initialized properly
     */
    protected void testNullObjectBase(FFDCResponseBase  nullObject)
    {
        assertEquals(nullObject.getRelatedHTTPCode(), 200);
        assertNull(nullObject.getExceptionClassName());
        assertNull(nullObject.getActionDescription());
        assertNull(nullObject.getExceptionErrorMessage());
        assertNull(nullObject.getExceptionErrorMessageId());
        assertNull(nullObject.getExceptionErrorMessageParameters());
        assertNull(nullObject.getExceptionCausedBy());
        assertNull(nullObject.getExceptionSystemAction());
        assertNull(nullObject.getExceptionUserAction());

        assertNull(nullObject.getExceptionProperties());
        nullObject.setExceptionProperties(emptyProperties);
        assertNull(nullObject.getExceptionProperties());
    }


    @Test public void TestToString()
    {
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("MockAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("MockAPIResponse"));

    }


    @Test public void testBaseResponse()
    {
        MockAPIResponse  testObject = new MockAPIResponse();
        fillTestObject(testObject);
        MockAPIResponse  clonedObject = new MockAPIResponse(testObject);
        MockAPIResponse  differentObject = new MockAPIResponse();
        fillTestObject(differentObject);
        differentObject.setExceptionClassName("TestClassName");

        MockAPIResponse result = testResponseObject(testObject, clonedObject, differentObject, MockAPIResponse.class);

        assertEquals(result, testObject);
    }


    @Test public void testBooleanResponse()
    {
        BooleanResponse  testObject = new BooleanResponse();
        fillTestObject(testObject);
        testObject.setFlag(false);
        BooleanResponse  clonedObject = new BooleanResponse(testObject);
        BooleanResponse  differentObject = new BooleanResponse();
        fillTestObject(differentObject);
        differentObject.setFlag(true);

        BooleanResponse result = testResponseObject(testObject, clonedObject, differentObject, BooleanResponse.class);

        assertEquals(result, testObject);

    }

}
