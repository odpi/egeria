/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

/**
 * MockAPIResponse enables the overridden methods of GovernanceProgramOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends GovernanceProgramOMASAPIResponse
{
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
