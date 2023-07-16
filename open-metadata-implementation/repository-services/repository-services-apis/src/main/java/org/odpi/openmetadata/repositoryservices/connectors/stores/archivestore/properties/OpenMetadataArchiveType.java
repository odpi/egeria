/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataArchiveType defines the origin of the open metadata archive.  Content pack means tha the archive contains
 * pre-defined types and instances for a particular use case.  Metadata export is a collection of types and instances
 * from a particular metadata server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataArchiveType implements Serializable
{
    /**
     * Content Pack - A collection of metadata elements that define a standard or support a specific use case.
     */
    CONTENT_PACK    (1, "ContentPack",
                        "A collection of metadata elements that define a standard or support a specific use case."),

    /**
     * Metadata Export - A collection of metadata elements that have been extracted from a specific open metadata repository to load into another.
     */
    METADATA_EXPORT (2, "MetadataExport",
                        "A collection of metadata elements that have been extracted from a specific open metadata repository to load into another."),

    /**
     * Repository Back-up - A collection of metadata elements that have been extracted from a specific open metadata repository to server as a back-up.
     */
    REPOSITORY_BACKUP (3, "RepositoryBackup",
                     "A collection of metadata elements that have been extracted from a specific open metadata repository to server as a back up.");

    private static final long    serialVersionUID = 1L;

    private final int    archiveTypeCode;
    private final String archiveTypeName;
    private final String archiveTypeDescription;


    /**
     * Constructor fo an enum instance.
     *
     * @param archiveTypeCode  code number for the archive type
     * @param archiveTypeName  name for the archive type
     * @param archiveTypeDescription  default description ofr the archive type
     */
    OpenMetadataArchiveType(int archiveTypeCode, String archiveTypeName, String archiveTypeDescription)
    {
        this.archiveTypeCode = archiveTypeCode;
        this.archiveTypeName = archiveTypeName;
        this.archiveTypeDescription = archiveTypeDescription;
    }


    /**
     * Return the code number for the archive type.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return archiveTypeCode;
    }


    /**
     * Return the printable name for the archive type.
     *
     * @return String archive type name
     */
    public String getName()
    {
        return archiveTypeName;
    }


    /**
     * Return the default description of the archive type.
     *
     * @return String archive description
     */
    public String getDescription()
    {
        return archiveTypeDescription;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataArchiveType{" +
                "archiveTypeCode=" + archiveTypeCode +
                ", archiveTypeName='" + archiveTypeName + '\'' +
                ", archiveTypeDescription='" + archiveTypeDescription + '\'' +
                '}';
    }
}
