/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.responses;

/**
 * MockAPIResponse enables the overridden methods of ConnectedAssetOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends ConnectedAssetOMASAPIResponse
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
