/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;

/**
 * OpenMetadataEventsSecurity defines the optional interface that an Open Metadata Server Security
 * Connector can implement to control whether an event is either sent or received from a cohort.
 * Simply implementing this interface means that the security connector will be called
 * each time an event is to be sent or received from a cohort topic.
 *
 * It has two methods, one for sending and one for receiving. The parameters include the cohort name and the event contents.
 * It can return the event unchanged, return a modified event (eg with sensitive content removed) or return null to say that the event
 * is filtered out.
 */
public interface OpenMetadataEventsSecurity
{
    /**
     * Validate whether an event received from another member of the cohort should be processed
     * by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return inbound event to process (may be updated) or null to indicate that the event should be ignored
     */
    OMRSInstanceEvent validateInboundEvent(String            cohortName,
                                           OMRSInstanceEvent event);


    /**
     * Validate whether an event should be sent to the other members of the cohort by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return outbound event to send (may be updated) or null to indicate that the event should be ignored
     */
    OMRSInstanceEvent validateOutboundEvent(String            cohortName,
                                            OMRSInstanceEvent event);
}
