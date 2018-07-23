/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.auditlog;


import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Validate the methods of OMRSAuditCode
 */
public class TestOMRSAuditCode
{
    private OMRSAuditCode  getTestObject()
    {
        return OMRSAuditCode.LOCAL_REPOSITORY_INITIALIZING;
    }


    @Test public void testAuditLogMethods()
    {
        OMRSAuditCode  testObject = this.getTestObject();

        assertTrue("OMRS-AUDIT-0003".equals(testObject.getLogMessageId()));
        assertTrue(OMRSAuditLogRecordSeverity.INFO == (testObject.getSeverity()));
        assertTrue("The local repository is initializing with metadata collection id DummyId".equals(testObject.getFormattedLogMessage("DummyId")));
        assertTrue("The local server has started to initialize the local repository.".equals(testObject.getSystemAction()));
        assertTrue("No action is required.  This is part of the normal operation of the server.".equals(testObject.getUserAction()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(OMRSAuditCode.NEW_TYPE_ADDED.toString().contains("OMRSAuditCode"));
    }
}
