/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.test.unittest.utilities;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;


/**
 * Validate that the exception is properly populated and supports toString, hashCode and equals.
 * Verify the deprecated methods work ok
 */
public class OMFCheckedExceptionBasedTest
{
    private final String                     reportingClassName         = "TestClassName";
    private final String                     reportingActionDescription = "TestActionDescription";
    private final Exception                  reportedCaughtException    = new Exception("TestReportedCaughtException");
    private final ExceptionMessageDefinition messageDefinition;
    private final Map<String, Object>        reportedRelatedProperties  = new HashMap<>();
    private final Map<String, Object>        emptyRelatedProperties     = new HashMap<>();

    /**
     * Constructor
     */
    protected OMFCheckedExceptionBasedTest()
    {
        int       reportedHTTPCode           = 404;
        String    reportedErrorMessageId     = "TestErrorMessageId";
        String    reportedErrorMessage       = "TestErrorMessage";
        String    reportedSystemAction       = "TestSystemAction";
        String    reportedUserAction         = "TestUserAction";

        messageDefinition = new ExceptionMessageDefinition(reportedHTTPCode,
                                                           reportedErrorMessageId,
                                                           reportedErrorMessage,
                                                           reportedSystemAction,
                                                           reportedUserAction);

        reportedRelatedProperties.put("propertyName1", "propertyValue1");
    }


    /**
     * Create a new instance of an exception.
     *
     * @param exceptionClass class
     * @param wrappedException optional exception to save
     * @param relatedProperties optional related properties
     * @param <T> class name
     * @return new instance
     */
    private <T> T getBasicException(Class<T>            exceptionClass,
                                    Exception           wrappedException,
                                    Map<String, Object> relatedProperties)
    {
        Constructor<T> constructor;
        T              exception = null;

        try
        {
            if ((wrappedException == null) && (relatedProperties == null))
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription);
            }
            else if ((wrappedException != null) && (relatedProperties != null))
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class,
                                                            Exception.class,
                                                            Map.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription,
                                                      wrappedException,
                                                      relatedProperties);
            }
            else if (wrappedException != null)
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class,
                                                            Exception.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription,
                                                      wrappedException);
            }
            else
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class,
                                                            Map.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription,
                                                      relatedProperties);
            }

        }
        catch (Exception error)
        {
            fail(error.toString());
        }

        return exception;
    }


    /**
     * Create a new instance of an exception.
     *
     * @param exceptionClass class
     * @param wrappedException optional exception to save
     * @param relatedProperties optional related properties
     * @param <T> class name
     * @return new instance
     */
    private <T> T getEnhancedException(Class<T>            exceptionClass,
                                       String              additionalProperty,
                                       Exception           wrappedException,
                                       Map<String, Object> relatedProperties)
    {
        Constructor<T> constructor;
        T              exception = null;

        try
        {
            if ((wrappedException == null) && (relatedProperties == null))
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class,
                                                            String.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription,
                                                      additionalProperty);
            }
            else if ((wrappedException != null) && (relatedProperties != null))
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class,
                                                            Exception.class,
                                                            String.class,
                                                            Map.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription,
                                                      wrappedException,
                                                      additionalProperty,
                                                      relatedProperties);
            }
            else if (wrappedException != null)
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class,
                                                            Exception.class,
                                                            String.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription,
                                                      wrappedException,
                                                      additionalProperty);
            }
            else
            {
                constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class,
                                                            String.class,
                                                            String.class,
                                                            String.class,
                                                            Map.class);
                exception   = constructor.newInstance(messageDefinition,
                                                      reportingClassName,
                                                      reportingActionDescription,
                                                      additionalProperty,
                                                      relatedProperties);
            }

        }
        catch (Exception error)
        {
            fail(error.toString());
        }

        return exception;
    }


    /**
     * Test that a new exception is properly populated
     */
    private void testExceptionValues(OMFCheckedExceptionBase exception,
                                     Exception                nestedException,
                                     Map<String, Object>      exceptionProperties)
    {
        MessageFormatter  messageFormatter = new MessageFormatter();

        assertEquals(messageDefinition.getHttpErrorCode(), exception.getReportedHTTPCode());
        assertEquals(reportingClassName, exception.getReportingClassName());
        assertEquals(reportingActionDescription, exception.getReportingActionDescription());
        assertEquals(messageFormatter.getFormattedMessage(messageDefinition), exception.getReportedErrorMessage());
        assertEquals(messageDefinition.getMessageId(), exception.getReportedErrorMessageId());
        assertEquals(messageDefinition.getMessageParams(), exception.getReportedErrorMessageParameters());
        assertEquals(messageDefinition.getSystemAction(), exception.getReportedSystemAction());
        assertEquals(messageDefinition.getUserAction(), exception.getReportedUserAction());

        if (nestedException == null)
        {
            assertNull(exception.getReportedCaughtException());
        }
        else
        {
            assertEquals(nestedException, exception.getReportedCaughtException());
        }

        if (exceptionProperties == null)
        {
            assertNull(exception.getRelatedProperties());
        }
        else
        {
            assertEquals(exceptionProperties, exception.getRelatedProperties());
        }
    }


    /**
     * Validate that string, equals and hashCode work off of the values of the exception
     *
     * @param exception1 simplest exception
     * @param exception2 exception with related properties
     * @param exception3 exception with wrapped exception
     * @param exception4 exception with wrapped exception and related properties
     * @param className name of exception class
     */
    private void testStringEqualsHashCode(OMFCheckedExceptionBase exception1,
                                          OMFCheckedExceptionBase exception2,
                                          OMFCheckedExceptionBase exception3,
                                          OMFCheckedExceptionBase exception4,
                                          String                   className)
    {

        assertEquals(exception1.hashCode(), exception1.hashCode());
        assertNotEquals(exception2.hashCode(), exception1.hashCode());
        assertNotEquals(exception3.hashCode(), exception1.hashCode());
        assertNotEquals(exception4.hashCode(), exception1.hashCode());

        assertEquals(exception2.hashCode(), exception2.hashCode());
        assertNotEquals(exception3.hashCode(), exception2.hashCode());
        assertNotEquals(exception4.hashCode(), exception2.hashCode());

        assertEquals(exception3.hashCode(), exception3.hashCode());
        assertNotEquals(exception4.hashCode(), exception3.hashCode());

        assertEquals(exception4.hashCode(), exception4.hashCode());

        assertEquals(exception1, exception1);
        assertNotEquals(reportedCaughtException, exception1);
        assertNotEquals(exception2, exception1);
        assertNotEquals(exception3, exception1);
        assertNotEquals(exception4, exception1);

        assertEquals(exception2, exception2);
        assertNotEquals(reportedCaughtException, exception2);
        assertNotEquals(exception3, exception2);
        assertNotEquals(exception4, exception2);

        assertEquals(exception3, exception3);
        assertNotEquals(reportedCaughtException, exception3);
        assertNotEquals(exception4, exception3);

        assertTrue(exception1.toString().contains(className));
        assertTrue(exception2.toString().contains(className));
        assertTrue(exception3.toString().contains(className));
        assertTrue(exception4.toString().contains(className));

        assertEquals(exception1.toString(), exception1.toString());
        assertNotEquals(exception2.toString(), exception1.toString());
        assertNotEquals(exception3.toString(), exception1.toString());
        assertNotEquals(exception4.toString(), exception1.toString());

        assertEquals(exception2.toString(), exception2.toString());
        assertNotEquals(exception3.toString(), exception2.toString());
        assertNotEquals(exception4.toString(), exception2.toString());

        assertEquals(exception3.toString(), exception3.toString());
        assertNotEquals(exception4.toString(), exception3.toString());

        assertEquals(exception4.toString(), exception4.toString());
    }


    protected void testException(Class<?>  exceptionClass)
    {
        OMFCheckedExceptionBase exception1 = (OMFCheckedExceptionBase)getBasicException(exceptionClass, null, null);

        testExceptionValues(exception1, null, null);

        OMFCheckedExceptionBase exception2 = (OMFCheckedExceptionBase)getBasicException(exceptionClass, null, reportedRelatedProperties);

        testExceptionValues(exception2, null, reportedRelatedProperties);

        OMFCheckedExceptionBase exception3 = (OMFCheckedExceptionBase)getBasicException(exceptionClass, reportedCaughtException, null);

        testExceptionValues(exception3, reportedCaughtException, null);

        OMFCheckedExceptionBase exception4 = (OMFCheckedExceptionBase)getBasicException(exceptionClass, reportedCaughtException, reportedRelatedProperties);

        testExceptionValues(exception4, reportedCaughtException, reportedRelatedProperties);

        testStringEqualsHashCode(exception1,
                                 exception2,
                                 exception3,
                                 exception4,
                                 exceptionClass.getSimpleName());

        OMFCheckedExceptionBase exception5 = (OMFCheckedExceptionBase)getBasicException(exceptionClass, null, emptyRelatedProperties);

        assertNull(exception5.getReportedCaughtException());

    }

    protected void testEnhancedException(Class<?>  exceptionClass, String additionalProperty)
    {
        OMFCheckedExceptionBase exception1 = (OMFCheckedExceptionBase)getEnhancedException(exceptionClass, additionalProperty, null, null);

        testExceptionValues(exception1, null, null);

        OMFCheckedExceptionBase exception2 = (OMFCheckedExceptionBase)getEnhancedException(exceptionClass, additionalProperty, null, reportedRelatedProperties);

        testExceptionValues(exception2, null, reportedRelatedProperties);

        OMFCheckedExceptionBase exception3 = (OMFCheckedExceptionBase)getEnhancedException(exceptionClass, additionalProperty, reportedCaughtException, null);

        testExceptionValues(exception3, reportedCaughtException, null);

        OMFCheckedExceptionBase exception4 = (OMFCheckedExceptionBase)getEnhancedException(exceptionClass, additionalProperty, reportedCaughtException, reportedRelatedProperties);

        testExceptionValues(exception4, reportedCaughtException, reportedRelatedProperties);

        testStringEqualsHashCode(exception1,
                                 exception2,
                                 exception3,
                                 exception4,
                                 exceptionClass.getSimpleName());

        OMFCheckedExceptionBase exception5 = (OMFCheckedExceptionBase)getEnhancedException(exceptionClass, additionalProperty, null, emptyRelatedProperties);

        assertNull(exception5.getReportedCaughtException());

    }
}
