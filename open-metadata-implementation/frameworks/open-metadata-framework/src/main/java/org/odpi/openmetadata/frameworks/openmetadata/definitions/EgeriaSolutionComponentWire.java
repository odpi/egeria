/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import java.util.List;

/**
 * Define the linkage between solution components defined for the Egeria runtime.
 */
public enum EgeriaSolutionComponentWire implements SolutionComponentWireDefinition
{

    ENGINE_HOST_METADATA(EgeriaSolutionComponent.ENGINE_HOST,
                         EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                         "access metadata",
                         "The engine host accesses details of the governance engines and their services to build out its configuration.  It listens for new engine action requests that define a request to run a governance service.  When a governance service runs, it accesses the metadata to work on from the metadata access store as well."),

    INTEGRATION_DAEMON_METADATA(EgeriaSolutionComponent.INTEGRATION_DAEMON,
                                EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                                "access metadata",
                                "The integration daemon accesses details of the integration groups and their connectors to build out its configuration.  It listens for new connectors and catalog targets being added to its integration groups to ensure it is running all the requested connectors.  When an integration connectors runs, it accesses the metadata to work on from the metadata access store as well."),

    VIEW_SERVER_METADATA(EgeriaSolutionComponent.VIEW_SERVER,
                         EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                         "access metadata",
                         "The view service accesses metadata on behalf of its REST API callers."),


    NOTEBOOK_TO_PYEGERIA(EgeriaSolutionComponent.JUPYTER_NOTEBOOK,
                         EgeriaSolutionComponent.PYEGERIA,
                         "access metadata",
                         "Jupyter notebooks are able to execute python code, which includes calls to the pyegeria library."),

    PYEGERIA_TO_VIEW_SERVER(EgeriaSolutionComponent.PYEGERIA,
                            EgeriaSolutionComponent.VIEW_SERVER,
                            "access metadata",
                            "The pyegeria library provides a python interface to the view service REST APIs."),

    PYEGERIA_TO_PLATFORM(EgeriaSolutionComponent.PYEGERIA,
                         EgeriaSolutionComponent.SERVER_PLATFORM,
                         "access metadata",
                         "The pyegeria library provides a python interface to the OMAG Server Platform REST APIs.  This includes server administration and status."),

    AIRFLOW_TO_OL_PROXY(EgeriaSolutionComponent.APACHE_AIRFLOW,
                         EgeriaSolutionComponent.OL_PROXY,
                         "open lineage events",
                         "Apache Airflow has native support for Open Lineage events.  These events are sent to the Open Lineage Proxy for distribution."),

    OL_PROXY_TO_KAFKA_TOPIC(EgeriaSolutionComponent.OL_PROXY,
                        EgeriaSolutionComponent.OL_KAFKA_TOPIC,
                        "open lineage events",
                        "Open Lineage Events are published to a Kafka Topic"),


    EGERIA_BUILD_CREATES_ARCHIVE(EgeriaSolutionComponent.EGERIA_BUILD,
                                 EgeriaSolutionComponent.OPEN_METADATA_ARCHIVE,
                                 "constructs",
                                 "The Egeria build constructs the open metadata archives."),

    EGERIA_BUILD_CREATES_PLATFORM(EgeriaSolutionComponent.EGERIA_BUILD,
                                 EgeriaSolutionComponent.SERVER_PLATFORM,
                                 "assembles",
                                 "The Egeria build complies the code for the OMAG Server Platform and assembles it, with the resources it needs to run."),

    LOAD_ARCHIVE_READS_ARCHIVE(EgeriaSolutionComponent.LOAD_ARCHIVE,
                               EgeriaSolutionComponent.OPEN_METADATA_ARCHIVE,
                               "reads",
                               "The load archive command reads the archive."),

    LOAD_ARCHIVE_FILLS_REPO(EgeriaSolutionComponent.LOAD_ARCHIVE,
                               EgeriaSolutionComponent.OPEN_METADATA_REPOSITORY,
                               "load",
                               "The load archive command pushes the archive content into the selected open metadata repository."),

    ;

    final SolutionComponentDefinition component1;
    final SolutionComponentDefinition component2;
    final String                      label;
    final String                      description;

    EgeriaSolutionComponentWire(SolutionComponentDefinition component1,
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
