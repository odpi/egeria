/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import java.util.List;

/**
 * Define the linkage between solution components defined for the Egeria runtime.
 */
public enum EgeriaSolutionComponentWire implements SolutionComponentWireDefinition
{
    AIRFLOW_POSTGRES(EgeriaSolutionComponent.APACHE_AIRFLOW,
                         EgeriaSolutionComponent.POSTGRES_SERVER,
                         "stores data",
                         "Apache Airflow stores job definitions and its operational data on PostgreSQL."),

    AIRFLOW_POSTGRES_DB(EgeriaSolutionComponent.APACHE_AIRFLOW,
                     EgeriaSolutionComponent.AIRFLOW_POSTGRESQL_DATABASE,
                     "stores data",
                     "Apache Airflow stores job definitions and its operational data on PostgreSQL."),

    SUPERSET_POSTGRES(EgeriaSolutionComponent.APACHE_SUPERSET,
                     EgeriaSolutionComponent.POSTGRES_SERVER,
                     "stores data",
                     "Apache Superset stores database queries and report designs on PostgreSQL."),

    SUPERSET_POSTGRES_DB(EgeriaSolutionComponent.APACHE_SUPERSET,
                      EgeriaSolutionComponent.SUPERSET_POSTGRESQL_DATABASE,
                      "stores data",
                      "Apache Superset stores database queries and report designs on PostgreSQL."),

    UC_POSTGRES(EgeriaSolutionComponent.RDBMS_UNITY_CATALOG,
                      EgeriaSolutionComponent.POSTGRES_SERVER,
                      "stores data",
                      "Unity catalog can be configured to store its metadata catalog in PostgreSQL."),

    UC_POSTGRES_DB(EgeriaSolutionComponent.RDBMS_UNITY_CATALOG,
                         EgeriaSolutionComponent.SUPERSET_POSTGRESQL_DATABASE,
                         "stores data",
                         "Unity catalog can be configured to store its metadata catalog in PostgreSQL."),

    MARQUEZ_POSTGRES(EgeriaSolutionComponent.MARQUEZ,
                EgeriaSolutionComponent.POSTGRES_SERVER,
                "stores data",
                "Marquez stores its metadata catalog in PostgreSQL."),

    MARQUEZ_POSTGRES_DB(EgeriaSolutionComponent.MARQUEZ,
                   EgeriaSolutionComponent.SUPERSET_POSTGRESQL_DATABASE,
                   "stores data",
                   "Marquez stores its metadata catalog in PostgreSQL."),

    EGERIA_POSTGRES(EgeriaSolutionComponent.SERVER_PLATFORM,
                      EgeriaSolutionComponent.POSTGRES_SERVER,
                      "stores data",
                      "Egeria has multiple options for storing both metadata and operational data on PostgreSQL."),

    EGERIA_KAFKA(EgeriaSolutionComponent.SERVER_PLATFORM,
                 EgeriaSolutionComponent.APACHE_KAFKA,
                 "exchanges notifications",
                 "Egeria uses Apache Kafka to both send and receive notifications."),

    METADATA_ACCESS_STORE_REPOSITORY(EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                    EgeriaSolutionComponent.OPEN_METADATA_REPOSITORY,
                    "stores data",
                    "A metadata access store stores its metadata in a metadata repository."),

    METADATA_ACCESS_STORE_EVENTS(EgeriaSolutionComponent.METADATA_ACCESS_STORE,
                                     EgeriaSolutionComponent.OPEN_METADATA_TOPIC,
                                     "metadata change notifications",
                                     "A metadata access store sends a kafka event each time a metadata element, relationship or classification changes."),

    INTEGRATION_DAEMON_EVENTS(EgeriaSolutionComponent.OPEN_METADATA_TOPIC,
                                 EgeriaSolutionComponent.INTEGRATION_DAEMON,
                                 "metadata change notifications",
                                 "An integration connector uses open metadata events to maintain its configuration.  Events are also passed to integration connectors it is hosting so they can monitor changing metadata."),

    ENGINE_HOST_EVENTS(EgeriaSolutionComponent.OPEN_METADATA_TOPIC,
                              EgeriaSolutionComponent.ENGINE_HOST,
                              "metadata change notifications",
                              "An engine host uses open metadata events to maintain its configuration.  Events are also passed to watchdog governance services it is hosting so they can monitor changing metadata."),

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

    HEY_EGERIA_TO_VIEW_SERVER(EgeriaSolutionComponent.HEY_EGERIA,
                            EgeriaSolutionComponent.VIEW_SERVER,
                            "access metadata",
                            "The hey_egeria command line tool uses its embedded pyegeria library to call the view service REST APIs."),

    DR_EGERIA_TO_VIEW_SERVER(EgeriaSolutionComponent.DR_EGERIA,
                              EgeriaSolutionComponent.VIEW_SERVER,
                              "access metadata",
                              "The Dr.Egeria markdown processor uses its embedded pyegeria library to call the view service REST APIs."),

    DR_EGERIA_TO_MARKDOWN_REPORT(EgeriaSolutionComponent.DR_EGERIA,
                             EgeriaSolutionComponent.MARKDOWN_DOCUMENT,
                             "writes",
                             "The Dr.Egeria markdown processor is able to create markdown forms and reports based on information received from Egeria."),

    DR_EGERIA_TO_HTML_WEBPAGE(EgeriaSolutionComponent.DR_EGERIA,
                                 EgeriaSolutionComponent.HTML_WEBPAGE,
                                 "writes",
                                 "The Dr.Egeria is able to create HTML web page reports based on information received from Egeria."),

    MARKDOWN_TO_DR_EGERIA(EgeriaSolutionComponent.MARKDOWN_DOCUMENT,
                          EgeriaSolutionComponent.DR_EGERIA,
                                 "read",
                                 "The Dr.Egeria markdown processor is able to read markdown documents and turn them into calls to Egeria."),

    HTML_TO_WEB_SERVER(EgeriaSolutionComponent.HTML_WEBPAGE,
                         EgeriaSolutionComponent.APACHE_WEB_SERVER,
                       "read",
                       "The Apache Web Server is able to display HTML Web Pages in the browser."),

    MY_EGERIA_TO_VIEW_SERVER(EgeriaSolutionComponent.MY_EGERIA,
                             EgeriaSolutionComponent.VIEW_SERVER,
                             "access metadata",
                             "The my_egeria user interface uses its embedded pyegeria library to call the view service REST APIs."),

    PYEGERIA_TO_PLATFORM(EgeriaSolutionComponent.PYEGERIA,
                         EgeriaSolutionComponent.SERVER_PLATFORM,
                         "access metadata",
                         "The pyegeria library provides a python interface to the OMAG Server Platform REST APIs.  This includes server administration and status."),

    AIRFLOW_TO_OL_PROXY(EgeriaSolutionComponent.APACHE_AIRFLOW,
                         EgeriaSolutionComponent.OL_PROXY,
                         "open lineage events",
                         "Apache Airflow has native support for Open Lineage events.  These events are sent to the Open Lineage Proxy for distribution."),

    OL_PROXY_TO_KAFKA(EgeriaSolutionComponent.OL_PROXY,
                        EgeriaSolutionComponent.APACHE_KAFKA,
                        "open lineage events",
                        "Open Lineage Events are published to a Kafka Topic"),

    OL_PROXY_TO_KAFKA_TOPIC(EgeriaSolutionComponent.OL_PROXY,
                            EgeriaSolutionComponent.OL_KAFKA_TOPIC,
                            "open lineage events",
                            "Open Lineage Events are published to a Kafka Topic"),

    OL_EVENTS_TO_ID(EgeriaSolutionComponent.OL_KAFKA_TOPIC,
                            EgeriaSolutionComponent.INTEGRATION_DAEMON,
                            "open lineage events",
                            "Open Lineage Events are received by the integration daemon for processing."),

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
