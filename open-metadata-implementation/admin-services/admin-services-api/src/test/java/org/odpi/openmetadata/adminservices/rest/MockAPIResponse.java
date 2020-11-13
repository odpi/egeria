/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

/**
 * MockAPIResponse enables the overridden methods of AdminServicesAPIResponse to be tested.
 */
public class MockAPIResponse extends AdminServicesAPIResponse
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MockAPIResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public MockAPIResponse(MockAPIResponse template)
    {
        super(template);
    }
}
