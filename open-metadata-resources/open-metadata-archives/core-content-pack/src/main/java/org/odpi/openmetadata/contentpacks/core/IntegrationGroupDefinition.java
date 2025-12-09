/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

/**
 * Describes the integration groups in the core content packs.
 */
public enum IntegrationGroupDefinition
{
    /**
     * Dynamic integration group to use with an Integration Daemon configuration.
     */
    DEFAULT("2648e33f-7501-4789-b524-f025bb9eaa57",
            "Egeria:IntegrationGroup:Default",
            "DefaultIntegrationGroup",
            "Dynamic integration group to use with an Integration Daemon configuration.",
            ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     * Dynamic integration group containing integration connectors for working with files.
     */
    FILES("4952a23a-77bc-488b-a711-bb35626b23a3",
            "Egeria:IntegrationGroup:Files",
            "FilesIntegrationGroup",
            "Dynamic integration group containing integration connectors for working with files.",
            ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     * Dynamic integration group containing integration connectors for working with open lineage events.
     */
    OPEN_LINEAGE("e6946ed7-ec34-479a-83b3-b22aa9e5cd7f",
          "Egeria:IntegrationGroup:OpenLineage",
          "OpenLineageIntegrationGroup",
          "Dynamic integration group containing integration connectors for working with open lineage events.",
          ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     * Dynamic integration group containing integration connectors for working with Egeria's infrastructure.
     */
    EGERIA("502c6eb0-a9d5-4a65-97dc-5cd5a4f10c36",
          "Egeria:IntegrationGroup:Egeria",
          "EgeriaIntegrationGroup",
          "Dynamic integration group containing integration connectors for working with Egeria's infrastructure.",
          ContentPackDefinition.EGERIA_CONTENT_PACK),

    /**
     * Dynamic integration group supporting integration connectors connecting to Apache Atlas.
     */
    APACHE_ATLAS("b761cd01-4cab-4838-b10a-11256a01fb09",
                 "Egeria:IntegrationGroup:ApacheAtlas",
                 "ApacheAtlasIntegrationGroup",
                 "Dynamic integration group supporting integration connectors connecting to Apache Atlas.",
                 ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     * Dynamic integration group supporting integration connectors connecting to Apache Kafka.
     */
    APACHE_KAFKA("dcca6c7b-bc90-4ddf-9b67-6c4df15c4477",
                 "Egeria:IntegrationGroup:ApacheKafka",
                 "ApacheKafkaIntegrationGroup",
                 "Dynamic integration group supporting integration connectors connecting to Apache Kafka.",
                 ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    /**
     * Dynamic integration group supporting integration connectors extracting Open API specifications to Open APIs via swagger.
     */
    APIS("580ae02d-921d-4885-af27-22e5b490de7e",
                 "Egeria:IntegrationGroup:OpenAPIs",
                 "OpenAPIsIntegrationGroup",
                 "Dynamic integration group supporting integration connectors extracting Open API specifications to Open APIs via swagger.",
                 ContentPackDefinition.APIS_CONTENT_PACK),

    /**
     * Dynamic integration group supporting integration connectors extracting interesting information needed to observe the activity in the Open Metadata Ecosystem.
     */
    NANNY("2d4e8f81-2b07-42da-902a-9f275e93ae79",
         "Egeria:IntegrationGroup:OpenMetadataObservability",
         "OpenMetadataObservabilityIntegrationGroup",
         "Dynamic integration group supporting integration connectors extracting interesting information needed to observe the activity in the Open Metadata Ecosystem.",
         ContentPackDefinition.NANNY_CONTENT_PACK),

    /**
     * Dynamic integration group supporting integration connectors extracting interesting information from the open metadata repositories that make useful digital products.
     */
    AUTO_PRODUCT_MANAGER("bfb863f0-b552-4d6a-91d6-02d7988fcc9a",
          "Egeria:IntegrationGroup:OpenMetadataDigitalProducts",
          "OpenMetadataDigitalProductsIntegrationGroup",
          "Dynamic integration group supporting integration connectors extracting interesting information from the open metadata repositories that make useful digital products.",
          ContentPackDefinition.PRODUCTS_CONTENT_PACK),

    /**
     * Dynamic integration group supporting integration connectors connecting to PostgreSQL Servers.
     */
    POSTGRES("a5aa2d79-0027-44a7-bd02-f97004f5b324",
                 "Egeria:IntegrationGroup:PostgreSQL",
                 "PostgreSQLIntegrationGroup",
                 "Dynamic integration group supporting integration connectors connecting to PostgreSQL Servers.",
                 ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     * Dynamic integration group supporting integration connectors connecting to Unity Catalog (UC).
     */
    UNITY_CATALOG("bf865bc4-fa91-47ce-92cb-dcc7878feeb6",
                  "Egeria:IntegrationGroup:UnityCatalog",
                  "UnityCatalogIntegrationGroup",
                  "Dynamic integration group supporting integration connectors connecting to Unity Catalog (UC).",
                  ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

            ;

    private final String                guid;
    private final String                qualifiedName;
    private final String                name;
    private final String                description;
    private final ContentPackDefinition contentPackDefinition;


    IntegrationGroupDefinition(String guid, String qualifiedName, String name, String description, ContentPackDefinition contentPackDefinition)
    {
        this.guid                  = guid;
        this.qualifiedName         = qualifiedName;
        this.name                  = name;
        this.description           = description;
        this.contentPackDefinition = contentPackDefinition;
    }


    /**
     * Return the unique identifier for the integration group.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the integration group.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the name of the integration group.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the integration group.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Get identifier of content pack where this definition should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupDefinition{name='" + name() + "'}";
    }
}
