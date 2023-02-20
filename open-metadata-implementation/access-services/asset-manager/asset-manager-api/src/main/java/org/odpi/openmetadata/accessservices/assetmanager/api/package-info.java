/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * This package contains the different interfaces of the asset manager.
 *
 * Each interface is focused on the exchange of groups of related metadata elements between a third party
 * asset manager and open metadata.
 *
 * <ul>
 *     <li>AssetManagerInterface - interface for managing external identifiers</li>
 *     <li>CollaborationExchangeInterface - for exchanging feedback information from users such as likes, ratings and comments.
 *     There is also support for informal tags.</li>
 *     <li>DataAssetExchange - for exchange of data related assets, their schemas, connections and lineage.</li>
 *     <li>GlossaryExchangeInterface - for exchange of Glossaries, GlossaryCategories, GlossaryTerms and
 *     their relationships and classifications. </li>
 *     <li>GovernanceExchangeInterface - for exchange of information about policies and rules.</li>
 *     <li>InfrastructureExchangeInterface - for exchanging metadata about hosts, containers, applications,
 *     servers and server capabilities.</li>
 *     <li>LineageExchangeInterface - for exchanging process definitions and lineage linkage.</li>
 *     <li>StewardshipExchangeInterface - for exchanging information about classifications, exceptions, requests for actions and
 *     resolutions.</li>
 *     <li>ValidValuesExchangeInterface - for exchanging information about reference data.</li>
 * </ul>
 */
package org.odpi.openmetadata.accessservices.assetmanager.api;