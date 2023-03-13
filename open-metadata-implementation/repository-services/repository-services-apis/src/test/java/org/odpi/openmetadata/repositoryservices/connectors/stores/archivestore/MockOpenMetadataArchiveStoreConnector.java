/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

public class MockOpenMetadataArchiveStoreConnector extends OpenMetadataArchiveStoreConnector
{
    /**
     * Return the contents of the archive.
     *
     * @return OpenMetadataArchive object
     */
    public OpenMetadataArchive getArchiveContents()
    {
        return null;
    }


    /**
     * Set new contents into the archive.  This overrides any content previously stored.
     *
     * @param archiveContents  OpenMetadataArchive object
     */
    public void setArchiveContents(OpenMetadataArchive   archiveContents)
    {

    }
}
