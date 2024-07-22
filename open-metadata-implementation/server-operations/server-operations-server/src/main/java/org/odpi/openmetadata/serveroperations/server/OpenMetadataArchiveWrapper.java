/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.serveroperations.server;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

public class OpenMetadataArchiveWrapper implements OpenMetadataArchiveStore
{
    private OpenMetadataArchive archiveContents = null;


    /**
     * Return the contents of the archive.
     *
     * @return OpenMetadataArchive object
     * @throws RepositoryErrorException there is a problem accessing the archive
     */
    @Override
    public OpenMetadataArchive getArchiveContents() throws RepositoryErrorException
    {
        return archiveContents;
    }

    /**
     * Set new contents into the archive.  This overrides any content previously stored.
     *
     * @param archiveContents OpenMetadataArchive object
     */
    @Override
    public void setArchiveContents(OpenMetadataArchive archiveContents)
    {
        this.archiveContents = archiveContents;
    }
}
