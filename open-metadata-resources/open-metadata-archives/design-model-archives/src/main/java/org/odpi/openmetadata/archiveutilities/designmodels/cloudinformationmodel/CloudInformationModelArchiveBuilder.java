/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel;


import org.odpi.openmetadata.archiveutilities.designmodels.base.DesignModelArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


class CloudInformationModelArchive extends DesignModelArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9dc75637-92a7-4926-b47b-a3d407546f89";
    private static final String                  archiveName        = "Cloud Information Model";
    private static final String                  archiveLicense     = "Apache 2.0";
    private static final String                  archiveDescription = "Data types for cloud applications.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "The Cloud Information Model";
    private static final Date                    creationDate       = new Date(1570383385107L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private CloudInformationModelParser parser;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    CloudInformationModelArchive(CloudInformationModelParser parser)
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              archiveType,
              originatorName,
              archiveLicense,
              creationDate,
              versionNumber,
              versionName);

        this.parser = parser;
    }


    private Map<String, String> loadGUIDs()
    {
        return new HashMap<>();
    }


    private String  getGUID(String id)
    {
        String guid = idToGUIDMap.get(id);

        if (guid == null)
        {
            guid = UUID.randomUUID().toString();

            idToGUIDMap.put(id, guid);
        }

        return guid;
    }


    private void  saveGUIDs(Map<String, String>  idToGUIDMap)
    {

    }


    /**
     * Returns the open metadata type archive containing all of the standard open metadata types.
     *
     * @return populated open metadata archive object
     */
    protected OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (parser != null)
        {
            /*
             * Convert the metadata extracted by the parser into content for the open metadata archive.
             */
            // todo

            /*
             * Retrieve the assembled archive content.
             */
            return super.getOpenMetadataArchive();
        }
        else
        {
            super.logBadArchiveContent(methodName);
            return null;
        }
    }
}