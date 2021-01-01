/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestSubjectAreaRestClient {
    @Test
    public void testGetMore() throws InvalidParameterException {
        SubjectAreaRestClient client = new SubjectAreaRestClient("aa","bb");
        assertFalse( client.getMore(1,1,1));
        assertTrue( client.getMore(1,1,2));
        assertFalse( client.getMore(10,10,10));
        assertTrue( client.getMore(10,10,11));
        assertFalse( client.getMore(0,10,11));


    }

}
