/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.digitalarchitecture.responses;

/**
 * MockAPIResponse enables the overridden methods of DigitalArchitectureOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends DigitalArchitectureOMASAPIResponse
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
