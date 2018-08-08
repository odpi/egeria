/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetowner.responses;

/**
 * MockAPIResponse enables the overridden methods of AssetOwnerOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends AssetOwnerOMASAPIResponse
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
