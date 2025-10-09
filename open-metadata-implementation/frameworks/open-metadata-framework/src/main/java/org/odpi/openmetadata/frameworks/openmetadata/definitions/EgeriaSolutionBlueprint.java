/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution blueprints.  Solution blueprints identify the key solution oriented
 * investments.  They may be used to document both the as-is and to-be solutions.
 */
public enum EgeriaSolutionBlueprint implements SolutionBlueprintDefinition
{
    /**
     * Description of the processing used to create the content packs shipped with Egeria.
     */
    OPEN_METADATA_CONTENT_PACK("a5a780a1-6f54-4f3c-bdc5-970c371ffb4e",
                               "OPEN-METADATA-CONTENT-PACK",
                               "Open Metadata Archive Solution Blueprint",
                               "Description of the processing used to create the content packs shipped with Egeria.  These content packs contain open metadata that is generally useful to teams starting to use Egeria.  The core content pack and the open metadata types are loaded automatically when the metadata access store starts up.  The other content packs are loaded on demand.",
                               new SolutionComponentDefinition[]{
                                       EgeriaSolutionComponent.EGERIA_BUILD,
                                       EgeriaSolutionComponent.OPEN_METADATA_ARCHIVE,
                                       EgeriaSolutionComponent.LOAD_ARCHIVE,
                                       EgeriaSolutionComponent.OPEN_METADATA_REPOSITORY}),


    /**
     * Description of the simple open metadata deployment environment used for evaluation and small team deployments.
     */
    EGERIA_WORKSPACES_INFRA("1d1fa174-273f-42bd-a8c0-3470fbc70fdf",
                      "EGERIA-WORKSPACES-RUNTIMES",
                      "Egeria Workspaces Runtimes Solution Blueprint",
                      "Description of the simple open metadata deployment environment used for evaluation and small team deployments.  It includes a configured Egeria runtime plus Apache Kafka, a PostgreSQL server, and an OpenLineage proxy (for receiving open lineage events).  There are optional packages for Marquez, Apache Airflow, Unity Catalog and Apache Superset.",
                      new SolutionComponentDefinition[]{
                              EgeriaSolutionComponent.APACHE_AIRFLOW,
                              EgeriaSolutionComponent.OL_PROXY,
                              EgeriaSolutionComponent.RDBMS_UNITY_CATALOG,
                              EgeriaSolutionComponent.DEFAULT_UNITY_CATALOG,
                              EgeriaSolutionComponent.POSTGRES_SERVER,
                              EgeriaSolutionComponent.SERVER_PLATFORM,
                              EgeriaSolutionComponent.MARQUEZ,
                              EgeriaSolutionComponent.APACHE_WEB_SERVER,
                              EgeriaSolutionComponent.PYEGERIA_WEB,
                              EgeriaSolutionComponent.APACHE_SUPERSET,
                              EgeriaSolutionComponent.APACHE_KAFKA,
                              EgeriaSolutionComponent.JUPYTER_HUB,
                      }),

    EGERIA_WORKSPACES_SERVERS("572ae03b-21c1-4277-8606-8db5d3593a50",
                      "EGERIA-WORKSPACES-SERVERS",
                      "Egeria Workspaces Servers Solution Blueprint",
                      "Description of the simple open metadata deployment environment used for evaluation and small team deployments.  It includes a configured Egeria runtime plus Apache Kafka, a PostgreSQL server, and an OpenLineage proxy (for receiving open lineage events).  There are optional packages for Marquez, Apache Airflow, Unity Catalog and Apache Superset.",
                      new SolutionComponentDefinition[]{
                              EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                              EgeriaSolutionComponent.APACHE_AIRFLOW,
                              EgeriaSolutionComponent.OL_PROXY,
                              EgeriaSolutionComponent.OL_KAFKA_TOPIC,
                              EgeriaSolutionComponent.OPEN_METADATA_TOPIC,
                              EgeriaSolutionComponent.INTEGRATION_DAEMON,
                              EgeriaSolutionComponent.VIEW_SERVER,
                              EgeriaSolutionComponent.ENGINE_HOST,
                              EgeriaSolutionComponent.DR_EGERIA,
                              EgeriaSolutionComponent.HEY_EGERIA,
                              EgeriaSolutionComponent.MY_EGERIA,
                              EgeriaSolutionComponent.JUPYTER_NOTEBOOK,
                      }),


    /**
     * Description of the services configured by Egeria's build process to create a simple open metadata deployment environment used for evaluation.
     */
    DEFAULT_RUNTIME("fb6dd0e5-afe1-4144-9b7d-e4559e7b09d2",
                    "DEFAULT-RUNTIME",
                    "Egeria Default Runtime Solution Blueprint",
                    "Description of the services configured by Egeria's build process to create a simple open metadata deployment environment used for evaluation.",
                    new SolutionComponentDefinition[]{EgeriaSolutionComponent.SERVER_PLATFORM}),

    ;

    private final String                         guid;
    private final String                        identifier;
    private final String                        displayName;
    private final String                        description;
    private final SolutionComponentDefinition[] solutionComponents;

    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param identifier the assigned identifier
     * @param displayName display name of solution blue print
     * @param description description of solution blueprint
     * @param solutionComponents content of the blueprint
     */
    EgeriaSolutionBlueprint(String                            guid,
                            String                            identifier,
                            String                            displayName,
                            String                            description,
                            SolutionComponentDefinition[]     solutionComponents)
    {
        this.guid                    = guid;
        this.identifier              = identifier;
        this.displayName             = displayName;
        this.description             = description;
        this.solutionComponents      = solutionComponents;
    }


    /**
     * Return the unique identifier of this element.  It is only needed if the elements are to be loaded
     * into an open metadata archive.
     *
     * @return string
     */
    public String getGUID() { return guid; }


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    @Override
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the display name of the solution blueprint.
     *
     * @return string
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution blueprint
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionBlueprint{" + displayName + '}';
    }


    /**
     * Return the list of components that are members of the solution blueprint.
     *
     * @return list of component definitions
     */
    @Override
    public List<SolutionComponentDefinition> getSolutionComponents()
    {
        if (solutionComponents != null)
        {
            return new ArrayList<>(Arrays.asList(solutionComponents));
        }

        return null;
    }
}
