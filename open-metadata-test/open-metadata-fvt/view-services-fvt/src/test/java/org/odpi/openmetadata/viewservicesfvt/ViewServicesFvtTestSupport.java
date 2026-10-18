/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Helpers shared by the view-services-fvt test classes.
 */
public class ViewServicesFvtTestSupport
{
    /**
     * Prefix of every qualified name the suite creates.
     */
    public static final String TEST_MARKER = "view-services-fvt";


    /**
     * Build a qualified name that is unique to this run, so that a run against a PostgreSQL repository holding
     * a previous run's elements neither collides with them nor finds them in a search.
     *
     * @param typeName type of the element
     * @param name distinguishing name
     * @return qualified name
     */
    public static String qualifiedName(String typeName,
                                       String name)
    {
        return TEST_MARKER + "::" + OMAGPlatformExtension.RUN_ID + "::" + typeName + "::" + name;
    }


    /**
     * A search string that only finds this run's elements.
     *
     * @return search string
     */
    public static String runSearchString()
    {
        return TEST_MARKER + "::" + OMAGPlatformExtension.RUN_ID;
    }


    /**
     * Check that a retrieve request no longer returns its element - either the service reports that it is not
     * found, or it returns nothing.
     *
     * @param client client for the service
     * @param retrievePath path of the retrieve request
     */
    public static void assertGone(ViewServiceClient client,
                                  String            retrievePath)
    {
        OpenMetadataRootElementResponse response = client.postUnchecked(retrievePath, new GetRequestBody(), OpenMetadataRootElementResponse.class);

        assertNotNull(response);
        assertTrue((response.getRelatedHTTPCode() != 200) || (response.getElement() == null),
                   retrievePath + " still returns the element after it was deleted");
    }
}
