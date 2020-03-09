/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

/**
 * MockAPIRequestBody enables the overridden methods of CommunityProfileOMASAPIRequestBody to be tested.
 */
public class MockAPIRequestBody extends CommunityProfileOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MockAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public MockAPIRequestBody(MockAPIRequestBody template)
    {
        super(template);
    }
}
