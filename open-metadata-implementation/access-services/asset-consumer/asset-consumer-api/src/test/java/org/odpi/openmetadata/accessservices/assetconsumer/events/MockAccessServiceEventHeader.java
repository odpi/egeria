/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.events;


/**
 * MockAccessServiceEventHeader provides a concrete class to test the access service's event header
 */
public class MockAccessServiceEventHeader extends AssetConsumerEvent
{
    private static final long    serialVersionUID = 1L;

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
