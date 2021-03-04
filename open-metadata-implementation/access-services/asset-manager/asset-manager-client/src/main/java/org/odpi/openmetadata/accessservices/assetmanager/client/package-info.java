/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * These are the client interfaces of the Asset Manager OMAS.
 *
 * <ul>
 *     <li>AssetManagerClient enables the caller to create the Software Server Capability that represents
 *         the source, or owner, of the metadata.  These sources are typically metadata catalogs that
 *         are not sophisticated enough to join an open metadata cohort through the OMRS Repository Connector.</li>
 *
 *     <li>AssetManagerEventClient enables the client to send and receive events from the Asset Manager OMAS.</li>
 *
 *     <li>CollaborationExchangeClient enables the caller to describe comments, likes, ratings and tags.</li>
 *
 *     <li>DataAssetExchangeClient enables the caller to describe data assets, schemas and connections.</li>
 *
 *     <li>GlossaryExchangeClient enables the caller to describe glossaries, glossary categories, glossary terms
 *         and the relationships between them.</li>
 *
 *     <li>GovernanceExchangeClient enables the caller to describe governance policies and rules.</li>
 *
 *     <li>InfrastructureExchangeClient enables the caller to describe servers and related infrastructure.</li>
 *
 *     <li>LineageExchangeClient support for creating lineage from processing engines.</li>
 *
 *     <li>StewardshipExchangeClient enables the caller to describe classifications and issues found in assets.</li>
 *
 *     <li>ValidValuesExchangeClient enables the caller to describe reference data.</li>
 * </ul>
 */
package org.odpi.openmetadata.accessservices.assetmanager.client;
