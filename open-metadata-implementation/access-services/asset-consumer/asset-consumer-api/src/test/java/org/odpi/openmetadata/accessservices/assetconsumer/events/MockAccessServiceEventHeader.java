/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.events;


/**
 * MockAccessServiceEventHeader provides a concrete class to test the access service's event header
 */
public class MockAccessServiceEventHeader extends AssetConsumerEventHeader
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
