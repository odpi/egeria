/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.dataplatform.responses;

/**
 * MockAPIResponse enables the overridden methods of DataPlatformOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends DataPlatformOMASAPIResponse
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
