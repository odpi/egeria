/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api;


/**
 * DataAssetExchangeInterface provides methods to define data assets, their schemas and connections, along with supporting objects,
 * and lineage.
 *
 * The interface supports the following types of objects
 *
 * <ul>
 *     <li>Assets - </li>
 *     <li>
 *       <ul>
 *           <li>Data Stores</li>
 *           <li>Data Sets</li>
 *           <li>APIs</li>
 *           <li>Processes</li>
 *       </ul>
 *     </li>
 *     <li>ConnectorTypes - the types of open connectors that can be used to access the asset.</li>
 *     <li>Connections - the connections used to create connector instances that can access the asset.</li>
 *     <li>Endpoints - the network information needed to access the asset.</li>
 *     <li>Schema - description of structure of data </li>
 *     <li>
 *       <ul>
 *           <li>Schema Types</li>
 *           <li>Schema Attributes</li>
 *       </ul>
 *     </li>
 * </ul>
 */
public interface DataAssetExchangeInterface extends SchemaExchangeInterface
{
}
