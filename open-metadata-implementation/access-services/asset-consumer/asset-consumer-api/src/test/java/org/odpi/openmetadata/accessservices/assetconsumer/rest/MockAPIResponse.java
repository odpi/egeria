/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

/**
 * MockAPIResponse enables the overridden methods of AssetConsumerOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends AssetConsumerOMASAPIResponse
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
