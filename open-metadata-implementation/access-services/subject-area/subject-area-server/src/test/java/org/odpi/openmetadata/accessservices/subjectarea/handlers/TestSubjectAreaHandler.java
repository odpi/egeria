/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestSubjectAreaHandler {
    @Test
    public void testSanitiseFindRequest() {
        OMRSAPIHelper omrsapiHelper= new OMRSAPIHelper(null, null, null, null);
        SubjectAreaHandlerTestImplementation handler = new SubjectAreaHandlerTestImplementation(omrsapiHelper, 10);

        FindRequest findRequest = new FindRequest();
        final String searchCriteria = "abc";
        findRequest.setSearchCriteria(searchCriteria);
        boolean exactValue = false;
        boolean ignoreCase = true;

        FindRequest sanitised1 =  handler.sanitiseFindRequest(findRequest, exactValue, ignoreCase );

        assertTrue( matches(sanitised1, "abc"));
        assertTrue( matches(sanitised1, "ABC"));
        assertTrue( matches(sanitised1, "abcd"));
        assertFalse( matches(sanitised1, "aaa"));
        // try an expression that could be problematic for the regex engine for certain inputs
        assertFalse( matches(sanitised1, "(a+)+"));

        exactValue = false;
        ignoreCase = false;
        findRequest.setSearchCriteria(searchCriteria);
        FindRequest sanitised2 =  handler.sanitiseFindRequest(findRequest, exactValue, ignoreCase );

        assertTrue( matches(sanitised2, "abc"));
        assertFalse( matches(sanitised2, "ABC"));
        assertTrue( matches(sanitised2, "abcd"));
        assertFalse( matches(sanitised2, "aaa"));
        assertFalse( matches(sanitised2, "(a+)+"));

        exactValue = true;
        ignoreCase = true;
        findRequest.setSearchCriteria(searchCriteria);

        FindRequest sanitised3 =  handler.sanitiseFindRequest(findRequest, exactValue, ignoreCase );
        assertTrue( matches(sanitised3, "abc"));
        assertTrue( matches(sanitised3, "ABC"));
        assertFalse( matches(sanitised3, "abcd"));
        assertFalse( matches(sanitised3, "aaa"));
        assertFalse( matches(sanitised3, "(a+)+"));

        exactValue = true;
        ignoreCase = false;
        findRequest.setSearchCriteria(searchCriteria);

        FindRequest sanitised4 =  handler.sanitiseFindRequest(findRequest, exactValue, ignoreCase );
        assertTrue( matches(sanitised4, "abc"));
        assertFalse( matches(sanitised4, "ABC"));
        assertFalse( matches(sanitised4, "abcd"));
        assertFalse( matches(sanitised4, "aaa"));
        assertFalse( matches(sanitised4, "(a+)+"));

        // check other combinations

        findRequest.setSearchCriteria("aaaaaa");
        FindRequest sanitised5 =  handler.sanitiseFindRequest(findRequest, exactValue, ignoreCase );
        assertTrue( matches(sanitised5, "aaaaaa"));
        assertFalse( matches(sanitised5, "(a+)+"));

        findRequest.setSearchCriteria("(a+)+");
        FindRequest sanitised6 =  handler.sanitiseFindRequest(findRequest, exactValue, ignoreCase );
        assertTrue( matches(sanitised6, "(a+)+"));
        assertFalse( matches(sanitised6, "aaaaaa"));
        // check empty string matches everything
        findRequest.setSearchCriteria("");
        FindRequest sanitised7 =  handler.sanitiseFindRequest(findRequest, exactValue, ignoreCase );
        assertTrue( matches(sanitised7, "(a+)+"));
        assertTrue( matches(sanitised7, "aaaaaa"));

    }

    private boolean matches(FindRequest sanitised1, String stringToTest) {
        return stringToTest.matches(sanitised1.getSearchCriteria());
    }
}
