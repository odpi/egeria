/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

/**
 * The state of an event that was received from a connector
 */
public enum IncomingEventState
{
    /**
     * The event has been created but has not yet been distributed
     * to all topic listeners.
     */
    CREATED,
    /**
     * The event has been distributed to all topic listeners.
     */
    DISTRIBUTED_TO_ALL_TOPIC_LISTENERS
}