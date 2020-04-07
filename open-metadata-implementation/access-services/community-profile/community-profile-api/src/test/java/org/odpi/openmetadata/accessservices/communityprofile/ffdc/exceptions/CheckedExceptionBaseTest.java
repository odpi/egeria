/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions;

import org.odpi.openmetadata.test.unittest.utilities.OCFCheckedExceptionBasedTest;
import org.testng.annotations.Test;



/**
 * Validate that the checked base exception is properly populated and supports hashCode and
 * equals.
 */
public class CheckedExceptionBaseTest extends OCFCheckedExceptionBasedTest
{

    /**
     * Constructor
     */
    public CheckedExceptionBaseTest()
    {
    }

    @Test public void testBaseException()
    {
        super.testException(MockCheckedExceptionBase.class);
    }


    @Test public void testUserNotAuthorizedException()
    {
        super.testEnhancedException(NoProfileForUserException.class, "TestUserId");
    }

}
