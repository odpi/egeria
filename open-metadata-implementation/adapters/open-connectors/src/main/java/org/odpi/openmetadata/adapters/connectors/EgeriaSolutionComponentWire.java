/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors;

import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.InformationSupplyChainDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentWireDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Define the linkage between solution components defined for the Egeria runtime.
 */
public enum EgeriaSolutionComponentWire implements SolutionComponentWireDefinition
{
    AIRFLOW_POSTGRES(DeployedImplementationType.APACHE_AIRFLOW_SERVER.getSolutionComponent(),
                     EgeriaSolutionComponent.WORKSPACES_POSTGRES_SERVER,
                         "stores data",
                         "Apache Airflow stores job definitions and its operational data on PostgreSQL.",
                     null),

    AIRFLOW_POSTGRES_DB(DeployedImplementationType.APACHE_AIRFLOW_SERVER.getSolutionComponent(),
                     EgeriaSolutionComponent.AIRFLOW_POSTGRESQL_DATABASE,
                     "stores data",
                     "Apache Airflow stores job definitions and its operational data on PostgreSQL.",
                        null),

    SUPERSET_POSTGRES(DeployedImplementationType.APACHE_SUPERSET.getSolutionComponent(),
                     EgeriaSolutionComponent.WORKSPACES_POSTGRES_SERVER,
                     "stores data",
                     "Apache Superset stores database queries and report designs on PostgreSQL.",
                      null),

    SUPERSET_POSTGRES_DB(DeployedImplementationType.APACHE_SUPERSET.getSolutionComponent(),
                      EgeriaSolutionComponent.SUPERSET_POSTGRESQL_DATABASE,
                      "stores data",
                      "Apache Superset stores database queries and report designs on PostgreSQL.",
                         null),

    UC_POSTGRES(EgeriaSolutionComponent.RDBMS_UNITY_CATALOG,
                      EgeriaSolutionComponent.WORKSPACES_POSTGRES_SERVER,
                      "stores data",
                      "Unity catalog can be configured to store its metadata catalog in PostgreSQL.",
                null),

    UC_POSTGRES_DB(EgeriaSolutionComponent.RDBMS_UNITY_CATALOG,
                         EgeriaSolutionComponent.UC_POSTGRESQL_DATABASE,
                         "stores data",
                         "Unity catalog can be configured to store its metadata catalog in PostgreSQL.",
                   null),

    MARQUEZ_POSTGRES(DeployedImplementationType.MARQUEZ_SERVER.getSolutionComponent(),
                EgeriaSolutionComponent.WORKSPACES_POSTGRES_SERVER,
                "stores data",
                "Marquez stores its metadata catalog in PostgreSQL.",
                     null),

    MARQUEZ_POSTGRES_DB(DeployedImplementationType.MARQUEZ_SERVER.getSolutionComponent(),
                   EgeriaSolutionComponent.MARQUEZ_POSTGRESQL_DATABASE,
                   "stores data",
                   "Marquez stores its metadata catalog in PostgreSQL.",
                        null),

    EGERIA_POSTGRES(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getSolutionComponent(),
                    EgeriaSolutionComponent.WORKSPACES_POSTGRES_SERVER,
                    "stores data",
                    "Egeria has multiple options for storing both metadata and operational data on PostgreSQL.",
                    new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION,
                    EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    EGERIA_KAFKA(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getSolutionComponent(),
                 EgeriaSolutionComponent.APACHE_KAFKA,
                 "exchanges notifications",
                 "Egeria uses Apache Kafka to both send and receive notifications.",
                 new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION,
                         EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    METADATA_ACCESS_STORE_REPOSITORY(EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                                     DeployedImplementationType.OPEN_METADATA_REPOSITORY.getSolutionComponent(),
                                     "stores data",
                                     "A metadata access store stores its metadata in a metadata repository.",
                                     new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION,
                                             EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    METADATA_ACCESS_STORE_AL_EVENTS(EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                                    EgeriaSolutionComponent.AUDIT_LOG_TOPIC,
                                    "audit log notifications",
                                    "A metadata access store sends a kafka event each time an audit log record of severity Error, Exception, Activity, Action, Decision, Security or Cohort.",
                                    new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_OBSERVABILITY}),

    INTEGRATION_DAEMON_AL_EVENTS(EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponent(),
                                    EgeriaSolutionComponent.AUDIT_LOG_TOPIC,
                                    "audit log notifications",
                                    "An integration daemon sends a kafka event each time an audit log record of severity Error, Exception, Activity, Action, Decision, Security or Cohort.",
                                 new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_OBSERVABILITY}),

    ENGINE_HOST_AL_EVENTS(EgeriaDeployedImplementationType.ENGINE_HOST.getSolutionComponent(),
                                 EgeriaSolutionComponent.AUDIT_LOG_TOPIC,
                                 "audit log notifications",
                                 "An engine host sends a kafka event each time an audit log record of severity Error, Exception, Activity, Action, Decision, Security or Cohort.",
                          new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_OBSERVABILITY}),

    METADATA_ACCESS_STORE_OM_EVENTS(EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                                 EgeriaSolutionComponent.OPEN_METADATA_TOPIC,
                                 "metadata change notifications",
                                 "A metadata access store sends a kafka event each time a metadata element, relationship, or classification changes.",
                                    new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_OBSERVABILITY,
                                    EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION}),

    METADATA_ACCESS_STORE_COHORT_EVENTS(EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                                    EgeriaSolutionComponent.COHORT,
                                    "metadata change notifications",
                                    "A metadata access store sends a cohort event each time a metadata element, relationship, or classification changes.",
                                    new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    INTEGRATION_DAEMON_OM_EVENTS(EgeriaSolutionComponent.OPEN_METADATA_TOPIC,
                                 EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponent(),
                                 "metadata change notifications",
                                 "An integration connector uses open metadata events to monitor changing metadata.",
                                 new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION}),

    ENGINE_HOST_OM_EVENTS(EgeriaSolutionComponent.OPEN_METADATA_TOPIC,
                              EgeriaDeployedImplementationType.ENGINE_HOST.getSolutionComponent(),
                              "metadata change notifications",
                              "A governance service uses open metadata events to monitor changing metadata.",
                          new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION}),

    METADATA_ACCESS_STORE_OG_EVENTS(EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                                    EgeriaSolutionComponent.OPEN_GOVERNANCE_TOPIC,
                                    "open governance notifications",
                                    "A metadata access store sends a kafka event each time a governance engine, governance service, integration group, integration connector or engine action changes.",
                                    new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION}),

    INTEGRATION_DAEMON_OG_EVENTS(EgeriaSolutionComponent.OPEN_GOVERNANCE_TOPIC,
                                 EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponent(),
                                 "configuration change notifications",
                                 "An integration daemon uses open governance events to maintain its configuration.  It listens for new connectors and catalog targets being added to its integration groups to ensure it is running all the requested connectors.",
                                 new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION}),

    ENGINE_HOST_OG_EVENTS(EgeriaSolutionComponent.OPEN_GOVERNANCE_TOPIC,
                          EgeriaDeployedImplementationType.ENGINE_HOST.getSolutionComponent(),
                          "configuration change and engine action notifications",
                          "An engine host uses open governance events to maintain its configuration and detect if there are new engine actions to run.",
                          new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION}),

    ENGINE_HOST_METADATA(EgeriaDeployedImplementationType.ENGINE_HOST.getSolutionComponent(),
                         EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                         "access metadata",
                         "The engine host accesses details of the governance engines and their services to build out its configuration.  When a governance service runs, it accesses the metadata to work on from the metadata access store as well.",
                         new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION,
                         EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    INTEGRATION_DAEMON_METADATA(EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponent(),
                                EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                                "access metadata",
                                "The integration daemon accesses details of the integration groups and their connectors to build out its configuration.  When an integration connectors runs, it accesses the metadata to work on from the metadata access store as well.",
                                new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.DYNAMIC_CONFIGURATION,
                                        EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    VIEW_SERVER_METADATA(EgeriaDeployedImplementationType.VIEW_SERVER.getSolutionComponent(),
                         EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponent(),
                         "access metadata",
                         "The view service accesses metadata on behalf of its REST API callers.",
                         new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    NOTEBOOK_TO_PYEGERIA(DeployedImplementationType.JUPYTER_NOTEBOOK.getSolutionComponent(),
                         DeployedImplementationType.PYEGERIA.getSolutionComponent(),
                         "access metadata",
                         "Jupyter notebooks are able to execute python code, which includes calls to the pyegeria library.",
                         new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    PYEGERIA_TO_VIEW_SERVER(DeployedImplementationType.PYEGERIA.getSolutionComponent(),
                            EgeriaDeployedImplementationType.VIEW_SERVER.getSolutionComponent(),
                            "access metadata",
                            "The pyegeria library provides a python interface to the view service REST APIs.",
                            new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    HEY_EGERIA_TO_VIEW_SERVER(EgeriaSolutionComponent.HEY_EGERIA,
                            EgeriaDeployedImplementationType.VIEW_SERVER.getSolutionComponent(),
                            "access metadata",
                            "The hey_egeria command line tool uses its embedded pyegeria library to call the view service REST APIs.",
                              new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    DR_EGERIA_TO_VIEW_SERVER(EgeriaSolutionComponent.DR_EGERIA,
                              EgeriaDeployedImplementationType.VIEW_SERVER.getSolutionComponent(),
                              "access metadata",
                              "The Dr.Egeria markdown processor uses its embedded pyegeria library to call the view service REST APIs.",
                             new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    DR_EGERIA_TO_MARKDOWN_REPORT(EgeriaSolutionComponent.DR_EGERIA,
                                 DeployedImplementationType.MARKDOWN_DOCUMENT.getSolutionComponent(),
                                 "writes",
                                 "The Dr.Egeria markdown processor is able to create markdown forms and reports based on information received from Egeria.",
                                 new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    DR_EGERIA_TO_HTML_WEBPAGE(EgeriaSolutionComponent.DR_EGERIA,
                                 DeployedImplementationType.WEBPAGE.getSolutionComponent(),
                                 "writes",
                                 "The Dr.Egeria is able to create HTML web page reports based on information received from Egeria.",
                              new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    MARKDOWN_TO_DR_EGERIA(DeployedImplementationType.MARKDOWN_DOCUMENT.getSolutionComponent(),
                          EgeriaSolutionComponent.DR_EGERIA,
                          "read",
                          "The Dr.Egeria markdown processor is able to read markdown documents and turn them into calls to Egeria.",
                          new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    HTML_TO_WEB_SERVER(DeployedImplementationType.WEBPAGE.getSolutionComponent(),
                       EgeriaSolutionComponent.APACHE_WEB_SERVER,
                       "read",
                       "The Apache Web Server is able to display HTML Web Pages in the browser.",
                       new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    MY_EGERIA_TO_VIEW_SERVER(EgeriaSolutionComponent.MY_EGERIA,
                             EgeriaDeployedImplementationType.VIEW_SERVER.getSolutionComponent(),
                             "access metadata",
                             "The my_egeria user interface uses its embedded pyegeria library to call the view service REST APIs.",
                             new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    PYEGERIA_TO_PLATFORM(DeployedImplementationType.PYEGERIA.getSolutionComponent(),
                         EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getSolutionComponent(),
                         "access metadata",
                         "The pyegeria library provides a python interface to the OMAG Server Platform REST APIs.  This includes server administration and status.",
                         new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    AIRFLOW_TO_OL_PROXY(DeployedImplementationType.APACHE_AIRFLOW_SERVER.getSolutionComponent(),
                        EgeriaSolutionComponent.OL_PROXY,
                        "open lineage events",
                        "Apache Airflow has native support for Open Lineage events.  These events are sent to the Open Lineage Proxy for distribution.",
                        new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_LINEAGE_HIGHWAY}),

    OL_PROXY_TO_KAFKA(EgeriaSolutionComponent.OL_PROXY,
                      EgeriaSolutionComponent.APACHE_KAFKA,
                        "open lineage events",
                        "Open Lineage Events are published to a Kafka Topic.",
                      new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_LINEAGE_HIGHWAY}),

    OL_PROXY_TO_KAFKA_TOPIC(EgeriaSolutionComponent.OL_PROXY,
                            EgeriaSolutionComponent.OL_KAFKA_TOPIC,
                            "open lineage events",
                            "Open Lineage Events are published to a Kafka Topic",
                            new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_LINEAGE_HIGHWAY}),

    OL_EVENTS_TO_ID(EgeriaSolutionComponent.OL_KAFKA_TOPIC,
                    EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponent(),
                            "open lineage events",
                            "Open Lineage Events are received by the integration daemon for processing.",
                    new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_LINEAGE_HIGHWAY}),

    ID_OL_EVENTS_TO_MARQUEZ(EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponent(),
                            EgeriaSolutionComponent.OL_CONSUMER,
                    "open lineage events",
                    "Open Lineage Events are received by the integration daemon for processing.",
                    new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_LINEAGE_HIGHWAY}),

    EGERIA_BUILD_CREATES_ARCHIVE(EgeriaSolutionComponent.EGERIA_BUILD,
                                 DeployedImplementationType.OPEN_METADATA_ARCHIVE.getSolutionComponent(),
                                 "constructs",
                                 "The Egeria build constructs the open metadata archives.",
                                 new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    EGERIA_BUILD_CREATES_PLATFORM(EgeriaSolutionComponent.EGERIA_BUILD,
                                  EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getSolutionComponent(),
                                 "assembles",
                                 "The Egeria build complies the code for the OMAG Server Platform and assembles it, with the resources it needs to run.",
                                  null),

    LOAD_ARCHIVE_READS_ARCHIVE(EgeriaSolutionComponent.LOAD_ARCHIVE,
                               DeployedImplementationType.OPEN_METADATA_ARCHIVE.getSolutionComponent(),
                               "reads",
                               "The load archive command reads the archive.",
                               new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    LOAD_ARCHIVE_FILLS_REPO(EgeriaSolutionComponent.LOAD_ARCHIVE,
                            DeployedImplementationType.OPEN_METADATA_REPOSITORY.getSolutionComponent(),
                            "load",
                            "The load archive command pushes the archive content into the selected open metadata repository.",
                            new InformationSupplyChainDefinition[]{EgeriaInformationSupplyChainDefinition.OPEN_METADATA_HIGHWAY}),

    ;

    final SolutionComponentDefinition component1;
    final SolutionComponentDefinition component2;
    final String                      label;
    final String                      description;
    final List<InformationSupplyChainDefinition> informationSupplyChains;

    EgeriaSolutionComponentWire(SolutionComponentDefinition         component1,
                                SolutionComponentDefinition        component2,
                                String                             label,
                                String                             description,
                                InformationSupplyChainDefinition[] informationSupplyChains)
    {
        this.component1              = component1;
        this.component2              = component2;
        this.label                   = label;
        this.description             = description;

        if (informationSupplyChains != null)
        {
            this.informationSupplyChains = Arrays.asList(informationSupplyChains);
        }
        else
        {
            this.informationSupplyChains = null;
        }
    }


    /**
     * Return the component for end 1
     *
     * @return component definition
     */
    @Override
    public SolutionComponentDefinition getComponent1()
    {
        return component1;
    }


    /**
     * Return the component for end 2
     *
     * @return component definition
     */
    @Override
    public SolutionComponentDefinition getComponent2()
    {
        return component2;
    }


    /**
     * Return the relationship label.
     *
     * @return string
     */
    @Override
    public String getLabel()
    {
        return label;
    }


    /**
     * Return the relationship description.
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the list of ISC qualified names that the wire belongs to.
     *
     * @return list of strings
     */
    @Override
    public List<String> getISCQualifiedNames()
    {
        if (informationSupplyChains == null)
        {
            return null;
        }

        List<String> iscQualifiedNames = new ArrayList<>();

        for (InformationSupplyChainDefinition informationSupplyChain : informationSupplyChains)
        {
            iscQualifiedNames.add(informationSupplyChain.getQualifiedName());
        }

        return iscQualifiedNames;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionComponentWire{" +
                "component1=" + component1 +
                ", component2=" + component2 +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", ISCQualifiedNames=" + getISCQualifiedNames() +
                "} " + super.toString();
    }
}
