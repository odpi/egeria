/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.ffdc;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Validate the methods of OMRSErrorCode
 */
public class TestOMRSErrorCode
{
    private OMRSErrorCode  getTestObject()
    {
        return OMRSErrorCode.NO_LOCAL_REPOSITORY;
    }


    @Test public void testAuditLogMethods()
    {
        OMRSErrorCode  testObject = this.getTestObject();

        assertTrue("OMRS-REST-API-503-001 ".equals(testObject.getErrorMessageId()));
        assertTrue(503 == (testObject.getHTTPErrorCode()));
        assertTrue("There is no local repository to support REST API call DummyId".equals(testObject.getFormattedErrorMessage("DummyId")));
        assertTrue("There is no local repository to support REST API call {0}".equals(testObject.getUnformattedErrorMessage()));
        assertTrue("The server has received a call on its open metadata repository REST API services but is unable to process it because the local repository is not active.  This may be because the open metadata services have not been activated.".equals(testObject.getSystemAction()));
        assertTrue("Ensure that the open metadata services have been activated in the server. If they are active and the server is supposed to have a local repository, correct the server's configuration document to include a local repository and activate the open metadata services.".equals(testObject.getUserAction()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(OMRSErrorCode.NO_MATCH_CRITERIA.toString().contains("OMRSErrorCode"));
    }
}
