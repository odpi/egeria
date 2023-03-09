/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * This package defines the event payloads sent on the Asset Consumer OMAS's out topic.
 *
 * The event version number indicates which version of the payload is in use.  With the version number in
 * place it is possible to change the payload over time and enable the consumer to adjust.
 * The event type enum defines the type of event.
 *
 * Following the header are the common properties for all asset events.   This includes the latest version of
 * the asset, its origin and the license associated with the Asset's metadata.
 *
 * Finally are the specialist extensions for each type of event.  New asset events include the creation time of the
 * asset.  Updated asset events include the update time and the original values of the asset (if available).
 */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

