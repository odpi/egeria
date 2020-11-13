/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

/**
 * MockAccessServiceOutboundEvent provides a concrete class to test the access service's outbound event
 */
public class MockAccessServiceOutboundEvent extends CommunityProfileOutboundEvent
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MockAccessServiceOutboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public MockAccessServiceOutboundEvent(MockAccessServiceOutboundEvent template)
    {
        super(template);
    }
}
