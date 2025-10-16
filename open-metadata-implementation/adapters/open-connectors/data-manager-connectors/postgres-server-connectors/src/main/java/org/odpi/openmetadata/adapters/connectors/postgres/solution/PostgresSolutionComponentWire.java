/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.solution;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaSolutionComponent;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentWireDefinition;

import java.util.List;

/**
 * Define the linkage between solution components defined for the Egeria runtime.
 */
public enum PostgresSolutionComponentWire implements SolutionComponentWireDefinition
{
    TABULAR_DATA_SET_TO_POSTGRES(PostgresSolutionComponent.POSTGRES_TABULAR_DATA_SET,
                                 EgeriaSolutionComponent.POSTGRES_SERVER,
                     "manages data",
                     "The PostgreSQL Tabular data set connector is able to define a PostSQL database table and manage data in it."),

    SURVEY_SERVER_TO_POSTGRES(PostgresSolutionComponent.SURVEY_POSTGRES_SERVER,
                                 EgeriaSolutionComponent.POSTGRES_SERVER,
                                 "reads postgres database catalog",
                                 "The PostgreSQL server surveyor reads the database catalog to discover details of the databases on the PostgreSQL server."),

    SURVEY_SERVER_TO_METADATA_STORE(PostgresSolutionComponent.SURVEY_POSTGRES_SERVER,
                              EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                              "writes survey results",
                              "The PostgreSQL server surveyor writes the results of its analysis to a survey report stored in the Metadata Access Store."),

    SURVEY_DATABASE_TO_POSTGRES(PostgresSolutionComponent.SURVEY_POSTGRES_DATABASE,
                              EgeriaSolutionComponent.POSTGRES_SERVER,
                              "reads postgres database catalog",
                              "The PostgreSQL database surveyor reads the database catalog to discover details of a database in the PostgreSQL server."),

    SURVEY_DATABASE_TO_METADATA_STORE(PostgresSolutionComponent.SURVEY_POSTGRES_DATABASE,
                                    EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                                    "writes survey results",
                                    "The PostgreSQL database surveyor writes the results of its analysis to a survey report stored in the Metadata Access Store."),


    CATALOG_SERVER_TO_POSTGRES(PostgresSolutionComponent.CATALOG_POSTGRES_SERVER,
                              EgeriaSolutionComponent.POSTGRES_SERVER,
                              "reads postgres database catalog",
                              "The PostgreSQL server cataloger reads the database catalog to discover details of the databases on the PostgreSQL server."),

    CATALOG_SERVER_TO_METADATA_STORE(PostgresSolutionComponent.CATALOG_POSTGRES_SERVER,
                                    EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                                    "writes catalog entries",
                                    "The PostgreSQL server cataloguer writes the results of its analysis as catalog entries (assets, connections, ...) stored in the Metadata Access Store."),

    CATALOG_DATABASE_TO_POSTGRES(PostgresSolutionComponent.CATALOG_POSTGRES_DATABASE,
                                EgeriaSolutionComponent.POSTGRES_SERVER,
                                "reads postgres database catalog",
                                "The PostgreSQL database cataloguer reads the database catalog to discover details of a database in the PostgreSQL server."),

    CATALOG_DATABASE_TO_METADATA_STORE(PostgresSolutionComponent.CATALOG_POSTGRES_DATABASE,
                                      EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                                      "writes catalog entries",
                                      "The PostgreSQL database cataloguer writes the results of its analysis as catalog entries (assets, schema elements, connections, ...) stored in the Metadata Access Store."),

    ;

    final SolutionComponentDefinition component1;
    final SolutionComponentDefinition component2;
    final String                      label;
    final String                      description;

    PostgresSolutionComponentWire(SolutionComponentDefinition component1,
                                  SolutionComponentDefinition component2,
                                  String                      label,
                                  String                      description)
    {
        this.component1              = component1;
        this.component2              = component2;
        this.label                   = label;
        this.description             = description;
    }


    /**
     * Return the component for end 1
     *
     * @return component definition
     */
    public SolutionComponentDefinition getComponent1()
    {
        return component1;
    }


    /**
     * Return the component for end 2
     *
     * @return component definition
     */
    public SolutionComponentDefinition getComponent2()
    {
        return component2;
    }


    /**
     * Return the relationship label.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Return the relationship description.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the list of ISC qualified names that the wire belongs to.
     *
     * @return list of strings
     */
    public List<String> getISCQualifiedNames()
    {
        return null;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "EgeriaSolutionComponentWire{" +
                "component1=" + component1 +
                ", component2=" + component2 +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", ISCQualifiedNames=" + getISCQualifiedNames() +
                "} " + super.toString();
    }
}
