/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;


/**
 * OMRSMetadataDefaultEventsSecurity provides a default instance events security filter that allows all events to
 * pass unchanged.  It is replaced if the server's security connector implements the events interface.
 */
public class OMRSMetadataDefaultEventsSecurity implements OpenMetadataEventsSecurity
{
    /**
     * Validate whether an event received from another member of the cohort should be processed
     * by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return inbound event to process (may be updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateInboundEvent(String            cohortName,
                                                  OMRSInstanceEvent event)
    {
        return event;
    }


    /**
     * Validate whether an event should be sent to the other members of the cohort by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return outbound event to send (may be updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateOutboundEvent(String            cohortName,
                                                   OMRSInstanceEvent event)
    {
        return event;
    }
}
