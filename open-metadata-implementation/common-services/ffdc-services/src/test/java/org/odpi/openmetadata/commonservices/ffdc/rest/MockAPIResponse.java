/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

/**
 * MockAPIResponse enables the overridden methods of FFDCResponseBase to be tested.
 */
public class MockAPIResponse extends FFDCResponseBase
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
