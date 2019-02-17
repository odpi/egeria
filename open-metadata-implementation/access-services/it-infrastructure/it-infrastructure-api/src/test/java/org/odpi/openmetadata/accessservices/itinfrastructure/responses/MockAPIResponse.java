/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.itinfrastructure.responses;

/**
 * MockAPIResponse enables the overridden methods of ITInfrastructureOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends ITInfrastructureOMASAPIResponse
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
