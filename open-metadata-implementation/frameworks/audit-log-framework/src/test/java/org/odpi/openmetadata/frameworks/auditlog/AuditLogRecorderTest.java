/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * AuditLogRecorder and AuditLoggingBase write to the audit log when there is one, and to standard out when there is
 * not - so callers never need to test the audit log for null.
 */
public class AuditLogRecorderTest
{
    private static final String ACTION = "testAction";

    private final ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
    private       PrintStream           originalOut;


    @BeforeMethod
    public void captureStandardOut()
    {
        capturedOut.reset();
        originalOut = System.out;
        System.setOut(new PrintStream(capturedOut, true, StandardCharsets.UTF_8));
    }


    @AfterMethod
    public void restoreStandardOut()
    {
        System.setOut(originalOut);
    }


    @Test
    public void testRecordsGoToTheAuditLog()
    {
        RecordingDestination destination = new RecordingDestination();
        AuditLog             auditLog    = getAuditLog(destination);
        Exception            error       = new IllegalStateException("broken");

        AuditLogRecorder.logRecord(auditLog, ACTION, getMessage());
        AuditLogRecorder.logRecord(auditLog, ACTION, getMessage(), "more detail");
        AuditLogRecorder.logExceptionRecord(auditLog, ACTION, getMessage(), error);
        AuditLogRecorder.logExceptionRecord(auditLog, ACTION, getMessage(), "more detail", error);

        assertEquals(destination.records.size(), 4);

        for (AuditLogRecord record : destination.records)
        {
            assertEquals(record.getMessageId(), "TEST-0001");
            assertEquals(record.getActionDescription(), ACTION);
        }

        assertNull(destination.records.get(0).getExceptionClassName());
        assertEquals(destination.records.get(1).getAdditionalInformation(), List.of("more detail"));
        assertEquals(destination.records.get(2).getExceptionClassName(), IllegalStateException.class.getName());
        assertEquals(destination.records.get(3).getAdditionalInformation(), List.of("more detail"));

        assertEquals(capturedOut.toString(StandardCharsets.UTF_8), "", "Nothing should be written to standard out when there is an audit log");
    }


    @Test
    public void testNoAuditLogWritesToStandardOut()
    {
        AuditLogRecorder.logRecord(null, ACTION, getMessage());
        AuditLogRecorder.logRecord(null, ACTION, getMessage(), "more detail");
        AuditLogRecorder.logExceptionRecord(null, ACTION, getMessage(), new IllegalStateException("broken"));
        AuditLogRecorder.logExceptionRecord(null, ACTION, getMessage(), "more detail", null);

        String output = capturedOut.toString(StandardCharsets.UTF_8);

        assertEquals(output.split("TEST-0001 The widget stopped", -1).length - 1, 4, output);
        assertTrue(output.contains("more detail"), output);
        assertTrue(output.contains("java.lang.IllegalStateException: broken"), "The stack trace should be printed: " + output);
    }


    @Test
    public void testBaseClassUsesItsAuditLog()
    {
        RecordingDestination destination = new RecordingDestination();
        TestComponent        withLog     = new TestComponent(getAuditLog(destination));
        TestComponent        withoutLog  = new TestComponent(null);

        withLog.doWork();
        withoutLog.doWork();

        assertEquals(destination.records.size(), 2);
        assertEquals(capturedOut.toString(StandardCharsets.UTF_8).split("TEST-0001 The widget stopped", -1).length - 1, 2);
    }


    private static AuditLogMessageDefinition getMessage()
    {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition("TEST-0001",
                                                                                    AuditLogRecordSeverityLevel.INFO,
                                                                                    "The widget stopped",
                                                                                    "The system notes it.",
                                                                                    "No action is required.");
        messageDefinition.setMessageParameters();

        return messageDefinition;
    }


    private static AuditLog getAuditLog(AuditLogDestination destination)
    {
        return new AuditLog(destination, 1, ComponentDevelopmentStatus.STABLE, "Test", "Test component", null);
    }


    /**
     * Keeps the records it is sent.
     */
    private static class RecordingDestination extends AuditLogDestination
    {
        private final List<AuditLogRecord> records = new ArrayList<>();

        RecordingDestination()
        {
            super(null);
        }

        @Override
        public void addLogRecord(AuditLogRecord logRecord)
        {
            records.add(logRecord);
        }
    }


    /**
     * A helper class that logs through its base class.
     */
    private static class TestComponent extends AuditLoggingBase
    {
        TestComponent(AuditLog auditLog)
        {
            super(auditLog);
        }

        void doWork()
        {
            logRecord(ACTION, getMessage());
            logExceptionRecord(ACTION, getMessage(), new IllegalStateException("broken"));
        }
    }
}
