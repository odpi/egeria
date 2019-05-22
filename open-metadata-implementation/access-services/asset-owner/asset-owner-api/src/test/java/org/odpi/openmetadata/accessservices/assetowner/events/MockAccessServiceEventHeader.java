/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.events;


/**
 * MockAccessServiceEventHeader provides a concrete class to test the access service's event header
 */
public class MockAccessServiceEventHeader extends AssetOwnerEventHeader
{
    /**
     * Default constructor
     */
    public MockAccessServiceEventHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public MockAccessServiceEventHeader(MockAccessServiceEventHeader template)
    {
        super(template);
    }
}
