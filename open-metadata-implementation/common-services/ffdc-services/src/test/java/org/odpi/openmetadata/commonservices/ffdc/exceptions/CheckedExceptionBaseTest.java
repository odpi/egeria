/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import org.odpi.openmetadata.test.unittest.utilities.OMFCheckedExceptionBasedTest;
import org.testng.annotations.Test;


/**
 * Validate that all the checked exceptions are properly populated and support toString hashCode and equals.
 */
public class CheckedExceptionBaseTest extends OMFCheckedExceptionBasedTest
{

    public CheckedExceptionBaseTest()
    {
    }


    @Test public void testBaseException()
    {
        super.testException(MockCheckedExceptionBase.class);
    }

}
