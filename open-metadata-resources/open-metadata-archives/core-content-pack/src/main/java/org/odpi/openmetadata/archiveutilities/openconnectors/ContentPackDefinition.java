/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;

/**
 * Identifies the content pack containing specific templates, integration connectors and governance services belong to.
 */
public enum ContentPackDefinition
{
    CORE_CONTENT_PACK("09450b83-20ff-4a8b-a8fb-f9b527bbcba6",
                      "CoreContentPack",
                      "Base Connector Types and Categories for connectors from the Egeria project along with metadata valid values, templates and connectors for the basic types of technology supported by these connectors.",
                      "CoreContentPack.omarchive"),
    UNITY_CATALOG_CONTENT_PACK("e6756296-3fa3-4aa3-9450-0dc44b8beae2",
                               "UnityCatalogContentPack",
                               "Reference data, templates, connectors and governance services for working with Unity Catalog.",
                               "UnityCatalogContentPack.omarchive"),
    APACHE_ATLAS_CONTENT_PACK("59c0beb4-a025-4451-8125-16a853095a50",
                              "ApacheAtlasContentPack",
                              "Reference data, templates, connectors and governance services for working with Apache Atlas.",
                              "ApacheAtlasContentPack.omarchive"),
    POSTGRES_CONTENT_PACK("8be11e8d-3964-40c7-88cd-403526725523",
                          "PostgresContentPack",
                          "Reference data, templates, connectors and governance services for working with a PostgreSQL Server.",
                          "PostgresContentPack.omarchive"),
    APACHE_KAFKA_CONTENT_PACK("b70b4a96-6fed-4411-ad68-1ca48434c5e4",
                              "ApacheKafkaContentPack",
                              "Reference data, templates, connectors and governance services for working with an Apache Kafka Server.",
                              "ApacheKafkaContentPack.omarchive"),
    NANNY_CONTENT_PACK("878b2e0f-6867-43b1-880c-8ee30e88bacb",
                       "EgeriaObservabilityContentPack",
                       "Connectors and governance services used to enable observability functions on Egeria's infrastructure.",
                       "ObservabilityContentPack.omarchive"),
    APIS_CONTENT_PACK("fc0a295b-9a6c-481a-ac8b-8ee4ce457335",
                      "APIsContentPack",
                      "Reference data, templates, connectors and governance services for working with REST APIs via the swagger interface.",
                      "APIsContentPack.omarchive"),

    PRODUCTS_CONTENT_PACK("e8e06304-2eb9-4ebe-bb8c-1ae44635244b",
                      "OpenMetadataProductsContentPack",
                      "Open metadata collections packaged into dynamic digital products.",
                      "OpenMetadataProductsContentPack.omarchive"),
    ;

    private final String archiveGUID;
    private final String archiveName;
    private final String archiveDescription;
    private final String archiveFileName;


    /**
     * Constructor
     *
     * @param archiveFileName name of the archive file
     */
    ContentPackDefinition(String archiveGUID, String archiveName, String archiveDescription, String archiveFileName)
    {
        this.archiveGUID        = archiveGUID;
        this.archiveName        = archiveName;
        this.archiveDescription = archiveDescription;
        this.archiveFileName    = archiveFileName;
    }


    /**
     * Return the unique name of the archive.
     *
     * @return guid
     */
    public String getArchiveGUID()
    {
        return archiveGUID;
    }


    /**
     * Return the display name of the archive.
     *
     * @return name
     */
    public String getArchiveName()
    {
        return archiveName;
    }


    /**
     * Return the description of the contents of the archive.
     *
     * @return text
     */
    public String getArchiveDescription()
    {
        return archiveDescription;
    }


    /**
     * Name of the file name to use.
     *
     * @return string
     */
    public String getArchiveFileName()
    {
        return archiveFileName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ContentPackDefinition{fileName='" + archiveFileName + "'}";
    }
}
