/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The generic handlers provide services to translate OMAS requests
 * built around that service's private beans into calls to the repository
 * services (through the repository handler).
 * The aim is to reduce the coding needed in the specific handlers used by the Open Metadata Access Services (OMASs) and
 * ensuring security and visibility of assets and other sensitive objects are managed correctly.
 *
 * The OMAS is responsible for providing the bean implementation and
 * a converter object that can convert the content of entities and
 * relationships retrieved through the repository services into the
 * OMAS's beans.
 *
 * The generic handlers take simple properties on create and update methods.
 * They pack these properties into InstanceProperties objects and then pass them
 * to the repository services to update the relevant entities/relationships/classifications
 * in the repository.
 *
 * When instances are retrieved through the repository services,
 * the generic handlers use the OMAS's converters to return the appropriate
 * beans to return on the API.
 *
 * The generic handlers also support the linking and unlinking of entities
 * in the repositories through the maintenance of relationships as well as managing
 * classifications.
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
