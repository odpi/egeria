/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.dataprivacy.responses;

/**
 * MockAPIResponse enables the overridden methods of DataPrivacyOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends DataPrivacyOMASAPIResponse
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
