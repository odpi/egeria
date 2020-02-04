/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * This package defines the beans that implement the payload for the Discovery Engine OMAS events.
 * These events keep a Discovery Server up to date with configuration for its hosted discovery engines.

 * In the header of each event (see DiscoveryEngineEvent) is an event version number and an event type enum.
 *
 * The event version number indicates which version of the payload is in use.  With the version number in
 * place it is possible to change the payload over time and enable the consumer to adjust.
 * The event type enum defines the type of event.
 *
 * Following the header are the common properties for all discovery engine events.   This includes the unique
 * identifiers of the discovery engine that has had a configuration change.
 *
 * Finally are the additional extensions for each type of event.  For example, the discovery service
 * configuration event has the unique identifiers for the discovery service registration relationship.
 *
 * Notice that the event payloads do not include the details of the configuration event.  Just the identifiers
 * of the affected elements.  The client then uses the DiscoveryConfigurationServer interface to retrieve all of
 * the configuration.
 */
package org.odpi.openmetadata.accessservices.discoveryengine.events;

