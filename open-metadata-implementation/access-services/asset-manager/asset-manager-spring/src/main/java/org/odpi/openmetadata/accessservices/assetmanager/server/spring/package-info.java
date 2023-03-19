/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


/**
 * REST API operations and Swagger Annotations
 *
 * The asset manager has multiple parts to its REST API, each focusing on a different collection of metadata types.
 *
 * The AssetManagerOMASResource defines the general operations for all types of asset managers.  This includes the
 * operations for retrieving the out topic, for registering an asset manager
 * and retrieving the identifiers of a pre-registered asset manager.
 *
 * The GlossaryExchangeResource supports metadata from glossaries.  This includes the glossary's categories, terms
 * and term to term relationships.
 */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;
