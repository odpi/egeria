/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.events;


/**
 * MockAccessServiceEventHeader provides a concrete class to test the access service's event header
 */
class MockAccessServiceEventHeader extends AssetOwnerEventHeader
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    MockAccessServiceEventHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    MockAccessServiceEventHeader(MockAccessServiceEventHeader template)
    {
        super(template);
    }
}
