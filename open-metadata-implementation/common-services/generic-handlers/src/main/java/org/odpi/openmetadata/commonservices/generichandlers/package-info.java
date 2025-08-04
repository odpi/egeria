/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The generic handlers provide services to translate Open Metadata Store requests
 * built around that service's private beans into calls to the repository
 * services (through the repository handler).
 * The aim is to reduce the coding needed in the specific handlers used by the Open Metadata Store whilst
 * ensuring security and visibility of assets and other sensitive objects are managed correctly.
 *
 * A key part of the capability of the generic handlers is to manage the authorization
 * calls to Open Metadata Security verifier and to ensure assets (and any linked
 * entities that are anchored to it making them logically part of its lifecycle)
 * are only visible when they are members of the supported zones.
 *
 * Finally, the generic handlers maintain the LatestChange classification on anchor Referenceables
 * and the Anchors classification vor all entities that are anchored to a referenceable.
 */
package org.odpi.openmetadata.commonservices.generichandlers;
