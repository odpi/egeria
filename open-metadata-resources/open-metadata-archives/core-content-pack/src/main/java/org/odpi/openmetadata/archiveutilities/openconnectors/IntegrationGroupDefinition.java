/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;

/**
 * Describes the integration groups in the core content packs.
 */
public enum IntegrationGroupDefinition
{
    DEFAULT("2648e33f-7501-4789-b524-f025bb9eaa57",
            "Egeria:IntegrationGroup:Default",
            "DefaultIntegrationGroup",
            "Dynamic integration group to use with an Integration Daemon configuration.",
            ContentPackDefinition.CORE_CONTENT_PACK),

    APACHE_ATLAS("b761cd01-4cab-4838-b10a-11256a01fb09",
                 "Egeria:IntegrationGroup:ApacheAtlas",
                 "ApacheAtlasIntegrationGroup",
                 "Dynamic integration group supporting integration connectors connecting to Apache Atlas.",
                 ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    APACHE_KAFKA("dcca6c7b-bc90-4ddf-9b67-6c4df15c4477",
                 "Egeria:IntegrationGroup:ApacheKafka",
                 "ApacheKafkaIntegrationGroup",
                 "Dynamic integration group supporting integration connectors connecting to Apache Kafka.",
                 ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    APIS("580ae02d-921d-4885-af27-22e5b490de7e",
                 "Egeria:IntegrationGroup:OpenAPIs",
                 "OpenAPIsIntegrationGroup",
                 "Dynamic integration group supporting integration connectors extracting Open API specifications to Open APIs via swagger.",
                 ContentPackDefinition.APIS_CONTENT_PACK),

    NANNY("2d4e8f81-2b07-42da-902a-9f275e93ae79",
         "Egeria:IntegrationGroup:OpenMetadataObservability",
         "OpenMetadataObservabilityIntegrationGroup",
         "Dynamic integration group supporting integration connectors extracting interesting information needed to observe the activity in the Open Metadata Ecosystem.",
         ContentPackDefinition.NANNY_CONTENT_PACK),


    POSTGRES("a5aa2d79-0027-44a7-bd02-f97004f5b324",
                 "Egeria:IntegrationGroup:PostgreSQL",
                 "PostgreSQLIntegrationGroup",
                 "Dynamic integration group supporting integration connectors connecting to PostgreSQL Servers.",
                 ContentPackDefinition.POSTGRES_CONTENT_PACK),

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
     * @return
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
     * Get identifier of content pack where this template should be located.
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
