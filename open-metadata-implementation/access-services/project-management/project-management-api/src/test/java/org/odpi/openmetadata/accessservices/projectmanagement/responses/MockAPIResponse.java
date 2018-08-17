/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.projectmanagement.responses;

/**
 * MockAPIResponse enables the overridden methods of ProjectManagementOMASAPIResponse to be tested.
 */
public class MockAPIResponse extends ProjectManagementOMASAPIResponse
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
