/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;

import java.util.Arrays;
import java.util.List;

/**
 * Describes the solution blueprints in the core content packs.
 */
public enum SolutionBlueprint
{
    /**
     *  Solution blueprint for a default setup.
     */
    DEFAULT("eb83f1a4-684b-4624-bdc8-6b394ce9cdc8",
            "Egeria::SolutionBlueprint::Default",
            "Default Solution Blueprint",
            "Solution blueprint for a default setup.",
            null,
            null,
            ContentPackDefinition.CORE_CONTENT_PACK),

    /**
     *  Solution blueprint for a default setup.
     */
    WORKSPACES("e90fc073-2383-4b52-9eb2-793bad546519",
            "Egeria::SolutionBlueprint::Default",
            "Default Solution Blueprint",
            "Solution blueprint for an egeria-workspaces setup.",
            null,
            null,
            ContentPackDefinition.CORE_CONTENT_PACK),


    /**
     *  Solution Blueprint containing integration connectors for working with files.
     */
    FILES("d89b489d-9661-41ee-838f-37454ebb70bf",
            "Egeria::SolutionBlueprint::Files",
            "Files Solution Blueprint",
            "Solution blueprint containing integration connectors for working with files.",
          null,
          null,
            ContentPackDefinition.FILES_CONTENT_PACK),

    /**
     *  Solution Blueprint containing integration connectors for working with open lineage events.
     */
    OPEN_LINEAGE("368cd66f-1250-491a-9da1-7f70f2963fd1",
          "Egeria::SolutionBlueprint::OpenLineage",
          "OpenLineage Solution Blueprint",
          "Solution blueprint containing integration connectors for working with open lineage events.",
                 null,
                 null,
          ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK),

    /**
     *  Solution Blueprint containing integration connectors for working with Egeria's infrastructure.
     */
    EGERIA("4d033711-0449-4d67-bf98-c7bfcae86b8c",
          "Egeria::SolutionBlueprint::Egeria",
          "Egeria Solution Blueprint",
          "Solution blueprint containing integration connectors for working with Egeria's infrastructure.",
           null,
           null,
          ContentPackDefinition.EGERIA_CONTENT_PACK),

    /**
     *  Solution Blueprint supporting integration connectors connecting to Apache Atlas.
     */
    APACHE_ATLAS("819a7239-f1dc-43af-8501-c7a551655373",
                 "Egeria::SolutionBlueprint::ApacheAtlas",
                 "ApacheAtlas Solution Blueprint",
                 "Solution blueprint supporting integration connectors connecting to Apache Atlas.",
                 null,
                 null,
                 ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    /**
     *  Solution Blueprint supporting integration connectors connecting to Apache Kafka.
     */
    APACHE_KAFKA("a3c6e079-3630-46e9-bf90-339c21964a4e",
                 "Egeria::SolutionBlueprint::ApacheKafka",
                 "Apache Kafka Solution Blueprint",
                 "Solution blueprint supporting integration connectors connecting to Apache Kafka.",
                 null,
                 null,
                 ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    /**
     *  Solution Blueprint supporting integration connectors extracting Open API specifications to Open APIs via swagger.
     */
    APIS("3a07c1f8-c810-4d53-9d28-4bbca1a6ea97",
                 "Egeria::SolutionBlueprint::OpenAPIs",
                 "OpenAPIs Solution Blueprint",
                 "Solution blueprint supporting integration connectors extracting Open API specifications to Open APIs via swagger.",
         null,
         null,
                 ContentPackDefinition.APIS_CONTENT_PACK),

    /**
     *  Solution Blueprint supporting integration connectors extracting interesting information needed to observe the activity in the Open Metadata Ecosystem.
     */
    NANNY("b36c6cc7-26f2-4b9b-b3e3-496f738c8927",
         "Egeria::SolutionBlueprint::OpenMetadataObservability",
         "Open Metadata Observability Solution Blueprint",
         "Solution blueprint supporting integration connectors extracting interesting information needed to observe the activity in the Open Metadata Ecosystem.",
          null,
          null,
         ContentPackDefinition.NANNY_CONTENT_PACK),


    /**
     *  Solution Blueprint supporting integration connectors connecting to PostgreSQL Servers.
     */
    POSTGRES("9c0e55dc-96b4-42de-8cd8-e4b2e2b92a38",
                 "Egeria::SolutionBlueprint::PostgreSQL",
                 "PostgreSQL Solution Blueprint",
                 "Solution blueprint supporting integration connectors connecting to PostgreSQL Servers.",
             null,
             null,
                 ContentPackDefinition.POSTGRES_CONTENT_PACK),

    /**
     *  Solution Blueprint supporting integration connectors connecting to Unity Catalog (UC).
     */
    UNITY_CATALOG("548661f1-e792-4acd-8dc2-0aa41187d6d2",
                  "Egeria::SolutionBlueprint::UnityCatalog",
                  "Unity Catalog Solution Blueprint",
                  "Solution blueprint supporting the exchange of metadata between the open metadata ecosystem and Unity Catalog (UC).",
                  new String[]{IntegrationConnectorDefinition.UC_SERVER_CATALOGUER.getGUID(), IntegrationConnectorDefinition.UC_CATALOG_CATALOGUER.getGUID()},
                  new String[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID()},
                  ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

            ;

    private final String                guid;
    private final String                qualifiedName;
    private final String                name;
    private final String                description;
    private final String[]              solutionComponentGUIDs;
    private final String[]              deployedImplementationTypeGUIDs;
    private final ContentPackDefinition contentPackDefinition;


    SolutionBlueprint(String                guid,
                      String                qualifiedName,
                      String                name,
                      String                description,
                      String[]              solutionComponentGUIDs,
                      String[]              deployedImplementationTypeGUIDs,
                      ContentPackDefinition contentPackDefinition)
    {
        this.guid                            = guid;
        this.qualifiedName                   = qualifiedName;
        this.name                            = name;
        this.description                     = description;
        this.solutionComponentGUIDs          = solutionComponentGUIDs;
        this.deployedImplementationTypeGUIDs = deployedImplementationTypeGUIDs;
        this.contentPackDefinition           = contentPackDefinition;
    }


    /**
     * Return the unique identifier for the solution blueprint.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the solution blueprint.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the name of the solution blueprint.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the solution blueprint.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the solution components included in the solution blueprint.
     * 
     * @return list of guids or null
     */
    public List<String> getSolutionComponentGUIDs()
    {
        if (solutionComponentGUIDs == null)
        {
            return null;
        }
        
        return Arrays.asList(solutionComponentGUIDs);
    }


    /**
     * Return the list of technology types integrated into this solution.
     * 
     * @return list of GUIDs or null
     */
    public List<String> getDeployedImplementationTypeGUIDs()
    {
        if (deployedImplementationTypeGUIDs == null)
        {
            return null;
        }

        return Arrays.asList(deployedImplementationTypeGUIDs);
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
        return " Solution BlueprintDefinition{name='" + name() + "'}";
    }
}
