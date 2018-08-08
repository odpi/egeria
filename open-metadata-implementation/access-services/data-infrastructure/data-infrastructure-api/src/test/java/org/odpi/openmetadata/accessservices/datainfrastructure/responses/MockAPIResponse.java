/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.datainfrastructure.responses;

/**
 * MockAPIResponse enables the overridden methods of DataInfrastructureOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends DataInfrastructureOMASAPIResponse
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
