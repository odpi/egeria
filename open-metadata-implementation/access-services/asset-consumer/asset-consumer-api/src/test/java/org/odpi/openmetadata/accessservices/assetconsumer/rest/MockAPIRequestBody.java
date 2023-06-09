/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

/**
 * MockAPIRequestBody enables the overridden methods of AssetConsumerOMASAPIRequestBody to be tested.
 */
class MockAPIRequestBody extends AssetConsumerOMASAPIRequestBody
{
    /**
     * Default constructor
     */
    MockAPIRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    MockAPIRequestBody(MockAPIRequestBody template)
    {
        super(template);
    }
}
