/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import org.odpi.openmetadata.test.unittest.utilities.OCFCheckedExceptionBasedTest;
import org.testng.annotations.Test;


/**
 * Validate that all of the checked exceptions are properly populated and support toString hashCode and equals.
 */
public class CheckedExceptionBaseTest extends OCFCheckedExceptionBasedTest
{

    public CheckedExceptionBaseTest()
    {
    }


    @Test public void testBaseException()
    {
        super.testException(MockCheckedExceptionBase.class);
    }


    @Test public void testInvalidParameterException()
    {
        super.testEnhancedException(InvalidParameterException.class, "TestParameterName");
    }

    @Test public void testPropertyServerException()
    {
        super.testException(PropertyServerException.class);
    }

    @Test public void testUserNotAuthorizedException()
    {
        super.testEnhancedException(UserNotAuthorizedException.class, "TestUserId");
    }


}
