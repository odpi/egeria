/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;

/**
 * MockAccessServiceInboundEvent provides a concrete class to test the access service's inbound event
 */
public class MockAccessServiceInboundEvent extends CommunityProfileInboundEvent
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MockAccessServiceInboundEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public MockAccessServiceInboundEvent(MockAccessServiceInboundEvent template)
    {
        super(template);
    }
}
