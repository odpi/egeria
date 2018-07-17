/* SPDX-License-Identifier: Apache-2.0 */
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
        return OMRSErrorCode.NULL_METADATA_COLLECTION;
    }


    @Test public void testAuditLogMethods()
    {
        OMRSErrorCode  testObject = this.getTestObject();

        assertTrue("OMRS-REPOSITORY-400-025 ".equals(testObject.getErrorMessageId()));
        assertTrue(400 == (testObject.getHTTPErrorCode()));
        assertTrue("Open metadata repository for server DummyId has not initialized correctly because it has a null metadata collection.".equals(testObject.getFormattedErrorMessage("DummyId")));
        assertTrue("Open metadata repository for server {0} has not initialized correctly because it has a null metadata collection.".equals(testObject.getUnformattedErrorMessage()));
        assertTrue("The system is unable to process requests for this repository.".equals(testObject.getSystemAction()));
        assertTrue("Verify that the repository connector is correctly configured in the OMAG server.".equals(testObject.getUserAction()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(OMRSErrorCode.NO_MATCH_CRITERIA.toString().contains("OMRSErrorCode"));
    }
}
