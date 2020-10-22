/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * These are the client interfaces of the Data Manager OMAS.
 *
 * <ul>
 *     <li>MetadataSourceClient enables the caller to create the Software Server Capability that represents
 *         the source, or owner, of the metadata.  These sources are database manager, file managers and
 *         file owning applications.</li>
 *
 *     <li>DatabaseManagerClient enables the caller to describe databases, database schemas, database tables
 *         and database columns.</li>
 *
 *     <li>FilesAndFoldersClient enables the caller to describe files and the organizing folder structure
 *         around it.</li>
 *
 *     <li>DataManagerEventClient enables the client to send and receive events from the Data Manager OMAS.</li>
 * </ul>
 */
package org.odpi.openmetadata.accessservices.datamanager.client;
