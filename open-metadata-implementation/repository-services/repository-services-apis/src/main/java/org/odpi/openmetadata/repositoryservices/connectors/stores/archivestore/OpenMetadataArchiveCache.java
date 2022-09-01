/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;


import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

/**
 * <p>
 * OpenMetadataArchiveCache is an optional interface for a builder that is accumulating the archive contents in memory.
 * </p>
 * <p>
 *     An open metadata archive has 3 sections:
 * </p>
 * <ul>
 *     <li>
 *         Archive header properties
 *     </li>
 *     <li>
 *         Type store: an ordered list of type definitions
 *     </li>
 *     <li>
 *         Instance store: a list of entities and relationships
 *     </li>
 * </ul>
 */
public interface OpenMetadataArchiveCache
{
    /**
     * Once the content of the archive has been added to the archive builder, an archive object can be retrieved.
     *
     * @return open metadata archive object with all the supplied content in it.
     */
    OpenMetadataArchive getOpenMetadataArchive();
}
