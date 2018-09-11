/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

/**
 * MockAPIRequestBody enables the overridden methods of AssetConsumerOMASAPIRequestBody to be tested.
 */
public class MockAPIRequestBody extends AssetConsumerOMASAPIRequestBody
{
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
