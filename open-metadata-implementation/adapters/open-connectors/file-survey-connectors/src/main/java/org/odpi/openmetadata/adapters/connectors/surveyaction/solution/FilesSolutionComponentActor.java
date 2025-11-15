/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.solution;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.*;

/**
 * Define the relationship between the solution roles and the solution components
 */
public enum FilesSolutionComponentActor implements SolutionComponentActorDefinition
{

    USER_TO_SERVER_SURVEY(EgeriaRoleDefinition.OPEN_METADATA_USER,
                          FilesSolutionComponent.SURVEY_POSTGRES_SERVER,
                          "requests survey",
                          "A user wishing to understand which databases are on a PostgreSQL server can request that a survey is run against the server."),

    USER_TO_DATABASE_SURVEY(EgeriaRoleDefinition.OPEN_METADATA_USER,
                            FilesSolutionComponent.SURVEY_POSTGRES_DATABASE,
                            "requests survey",
                            "A user wishing to understand which schemas, tables and columns are defined for a PostgreSQL database can request that a survey is run against the database."),

    USER_TO_CATALOG_SERVER(EgeriaRoleDefinition.OPEN_METADATA_USER,
                           FilesSolutionComponent.CATALOG_POSTGRES_SERVER,
                           "configures connector",
                           "A user is able to configure the connector with details of the PostgreSQL server that it should monitor and catalog any databases it finds."),

    USER_TO_CATALOG_DATABASE(EgeriaRoleDefinition.OPEN_METADATA_USER,
                             FilesSolutionComponent.CATALOG_POSTGRES_DATABASE,
                             "configures connector",
                             "A user is able to configure the connector with details of the PostgreSQL database that it should monitor and catalog any schemas it finds."),



    ;

    final ActorRoleDefinition         solutionRole;
    final SolutionComponentDefinition solutionComponent;
    final String                      role;
    final String                      description;

    FilesSolutionComponentActor(EgeriaRoleDefinition        solutionRole,
                                SolutionComponentDefinition solutionComponent,
                                String                      role,
                                String                      description)
    {
        this.solutionRole      = solutionRole;
        this.solutionComponent = solutionComponent;
        this.role              = role;
        this.description       = description;
    }


    /**
     * Return the solution role to link to.
     *
     * @return role definition
     */
    @Override
    public ActorRoleDefinition getSolutionRole()
    {
        return solutionRole;
    }

    /**
     * Return the solution component to link to.
     *
     * @return component definition
     */
    @Override
    public SolutionComponentDefinition getSolutionComponent()
    {
        return solutionComponent;
    }


    /**
     * Return the role relationship label.
     *
     * @return string
     */
    @Override
    public String getRole()
    {
        return role;
    }


    /**
     * Return the role relationship description.
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
        return "PostgresSolutionComponentActor{" +
                "solutionRole=" + solutionRole +
                ", productSolutionComponent=" + solutionComponent +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                "}";
    }
}
