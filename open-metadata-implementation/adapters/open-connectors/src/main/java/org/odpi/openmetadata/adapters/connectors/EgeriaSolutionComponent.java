/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.InformationSupplyChainDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution components.
 */
public enum EgeriaSolutionComponent implements SolutionComponentDefinition
{
    LOAD_ARCHIVE("cc3e0a81-ad10-4c72-b6a5-01301c5dd587",
                 "LOAD-ARCHIVE",
                 SolutionComponentType.CONSOLE_COMMAND.getSolutionComponentType(),
                 null,
                 "https://egeria-project.org/concepts/open-metadata-archive/",
                 "Load Archive",
                 "A command to load an open metadata archive.",
                 null,
                 null,
                 null),

    MY_EGERIA("57f6a0c7-fbbe-42b2-a30f-3298a8816096",
              "MY-EGERIA",
              SolutionComponentType.USER_INTERFACE.getSolutionComponentType(),
              null,
              null,
              "my_egeria",
              "A user interface for working with Egeria and open metadata.",
              new SolutionComponentDefinition[]{DeployedImplementationType.PYEGERIA.getSolutionComponent()},
              null,
              null),

    DR_EGERIA("1ddd9283-3dc9-4220-ac42-be0894b5a930",
              "DR-EGERIA",
              SolutionComponentType.SOFTWARE_LIBRARY.getSolutionComponentType(),
              null,
              null,
              "Dr.Egeria",
              "A markdown processor that can maintain and retrieve open metadata by processing and creating markdown documents.",
              new SolutionComponentDefinition[]{DeployedImplementationType.PYEGERIA.getSolutionComponent()},
              null,
              null),

    HEY_EGERIA("b2278203-63a6-4a4f-b142-6f75cd592415",
               "HEY-EGERIA",
               SolutionComponentType.CONSOLE_COMMAND.getSolutionComponentType(),
               null,
               "https://egeria-project.org/user-interfaces/hey-egeria/overview/",
               "hey_egeria",
               "A user interface for working with Egeria and open metadata.",
               new SolutionComponentDefinition[]{
                       DeployedImplementationType.PYEGERIA.getSolutionComponent(),
                       LOAD_ARCHIVE},
               null,
               null),

    EGERIA_BUILD("f763d7df-1d49-4ceb-a079-c7f40e15982b",
                 "EGERIA-BUILD",
                 SolutionComponentType.MULTI_STEP_PROCESS.getSolutionComponentType(),
                 OpenMetadataType.BUILD_INSTRUCTION_FILE.typeName,
                 null,
                 "Egeria Gradle Build",
                 "Comprehensive build process that complies the software, runs tests and creates the assembly for Egeria's runtime.",
                 null,
                 null,
                 null),


    JUPYTER_HUB("e3e3a894-43a8-4745-a220-c3c047c01688",
                "JUPYTER-HUB",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                "https://jupyter.org/hub",
                "JupyterHub",
                "Supports browser based interaction with Jupyter Notebooks.",
                new SolutionComponentDefinition[]{DeployedImplementationType.PYEGERIA.getSolutionComponent()},
                null,
                null),

    DEFAULT_UNITY_CATALOG("d22f1b91-61d9-4454-b4a3-be07a3336874",
                          "DEFAULT-UNITY-CATALOG",
                          SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                          OpenMetadataType.SOFTWARE_SERVER.typeName,
                          "https://www.unitycatalog.io/",
                          "Default Unity Catalog",
                          "Open Source version of Unity Catalog with the default catalog.",
                          null,
                          null,
                          null),

    RDBMS_UNITY_CATALOG("8b472a17-9c2b-4a31-81b2-7c81fbdb148a",
                        "UNITY-CATALOG-ON-POSTGRES",
                        SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                        OpenMetadataType.SOFTWARE_SERVER.typeName,
                        "https://www.unitycatalog.io/",
                        "Working Unity Catalog",
                        "Open Source version of Unity Catalog backed by a PostgreSQL database.  Initialized with no content.",
                        null,
                        null,
                        null),

    OL_PROXY("19af8706-17b9-4367-865f-2181dcb51646",
             "OPEN-LINEAGE-PROXY",
             SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
             OpenMetadataType.SOFTWARE_SERVER.typeName,
             "https://egeria-project.org/features/lineage-management/overview/#egerias-open-lineage-support",
             "Open Lineage Proxy",
             "Consolidates open lineage events from data engines and pipes them to Apache Kafka.",
             null,
             null,
             null),

    OL_KAFKA_TOPIC("d4b3a089-b909-4f49-9d95-bbbba7008f17",
                   "OPEN-LINEAGE-TOPIC",
                   SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                   OpenMetadataType.TOPIC.typeName,
                   "https://egeria-project.org/features/lineage-management/overview/#egerias-open-lineage-support",
                   "Open Lineage Topic",
                   "Stores and distributes Open Lineage Events.",
                   null,
                   null,
                   null),

    OPEN_METADATA_TOPIC("03310995-216c-49e7-a9fe-8e789b11d37d",
                        "OPEN-METADATA-TOPIC",
                        SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                        OpenMetadataType.TOPIC.typeName,
                        "https://egeria-project.org/concepts/open-metadata-topic-connector/",
                        "Open Metadata Topic",
                        "Provides notifications when open metadata changes in any of the connected repositories.",
                        null,
                        null,
                        null),

    OPEN_GOVERNANCE_TOPIC("145b6fd1-6267-429a-90f3-851a2dc1350e",
                        "OPEN-GOVERNANCE-TOPIC",
                        SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                        OpenMetadataType.TOPIC.typeName,
                        "https://egeria-project.org/concepts/open-metadata-topic-connector/",
                        "Open Governance Topic",
                        "Provides notifications when governance server configuration (Governance engines, governance services, integration groups, and integration connectors) changes.  It also transmits changes to engine actions to allow the engine host to initiate the actions.",
                        null,
                          null,
                          null),

    AUDIT_LOG_TOPIC("9caf4cb6-c43d-49d5-aa50-02b06fc66225",
                          "AUDIT-LOG-TOPIC",
                          SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                          OpenMetadataType.TOPIC.typeName,
                          "https://egeria-project.org/concepts/audit-log-destination-connector/",
                          "Audit Log Topic",
                          "Provides notifications when audit log events of severity Error, Exception, Activity, Action, Decision, Security or Cohort are written to the audit log by registered OMAG Servers.",
                          null,
                    null,
                    null),

    APACHE_KAFKA("d52a42e9-87a1-4382-aa0e-d0a3a63465f6",
                 "APACHE-KAFKA",
                 SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                 OpenMetadataType.SOFTWARE_SERVER.typeName,
                 "https://kafka.apache.org/",
                 "Apache Kafka",
                 "Manages a reliable topic based service.",
                 new SolutionComponentDefinition[]{OL_KAFKA_TOPIC, OPEN_METADATA_TOPIC, OPEN_GOVERNANCE_TOPIC},
                 null,
                 null),

    EGERIA_POSTGRESQL_DATABASE("ac2df71c-6041-42b9-b96b-ae036a32f5d8",
                               "EGERIA-POSTGRESQL-DATABASE",
                               SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                               OpenMetadataType.RELATIONAL_DATABASE.typeName,
                               null,
                               "Egeria PostgreSQL Database",
                               "This database supports the different repositories used by the Egeria runtime.",
                               new SolutionComponentDefinition[]{
                                       DeployedImplementationType.OPEN_METADATA_REPOSITORY.getSolutionComponent()},
                               null,
                               null),

    UC_POSTGRESQL_DATABASE("0f732681-e40c-48dc-b5b3-c1e78c05da29",
                           "UNITY-CATALOG-POSTGRESQL-DATABASE",
                           SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                           OpenMetadataType.RELATIONAL_DATABASE.typeName,
                           "https://www.postgresql.org/",
                           "Unity Catalog PostgreSQL Database",
                           "This database supports the catalog storage for Unity Catalog.",
                           null,
                           null,
                           null),

    SUPERSET_POSTGRESQL_DATABASE("a072d124-84ab-45e8-913e-2268cf16ccb6",
                                 "SUPERSET-POSTGRESQL-DATABASE",
                                 SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                 OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                 "https://www.postgresql.org/",
                                 "Superset PostgreSQL Database",
                                 "This database supports the catalog storage for Apache Superset.",
                                 null,
                                 null,
                                 null),

    AIRFLOW_POSTGRESQL_DATABASE("5b2367e0-d03c-4bd4-bea0-363cf0efd1ee",
                                "AIRFLOW-POSTGRESQL-DATABASE",
                                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                "https://www.postgresql.org/",
                                "Apache Airflow PostgreSQL Database",
                                "This database supports the catalog storage for Apache Airflow.",
                                null,
                                null,
                                null),

    MARQUEZ_POSTGRESQL_DATABASE("3093e1c1-f4bf-4406-8c80-a252393f0071",
                                "MARQUEZ-POSTGRESQL-DATABASE",
                                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                "https://www.postgresql.org/",
                                "Marquez PostgreSQL Database",
                                "This database supports the catalog storage for Marquez.",
                                null,
                                null,
                                null),

    WORKSPACES_POSTGRES_SERVER("235a335a-f010-43ee-b785-3774380b5058",
                               "WORKSPACES-POSTGRES-SERVER",
                               SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                               OpenMetadataType.SOFTWARE_SERVER.typeName,
                               "https://www.postgresql.org/",
                               "Egeria Workspaces PostgreSQL Server",
                               "Hosts relational databases for the Egeria Workspaces deployment.",
                               new EgeriaSolutionComponent[]{
                                       EGERIA_POSTGRESQL_DATABASE,
                                       UC_POSTGRESQL_DATABASE,
                                       SUPERSET_POSTGRESQL_DATABASE,
                                       AIRFLOW_POSTGRESQL_DATABASE,
                                       MARQUEZ_POSTGRESQL_DATABASE,
                               },
                               null,
                               null),

    APACHE_WEB_SERVER("2b9976a6-1466-4b82-be8b-61e80d1adaef",
                      "APACHE-WEB-SERVER",
                      SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                      "https://httpd.apache.org/",
                      "Apache Web Server",
                      "Services HTML documents written to the file system.",
                      null,
                      null,
                      null),

    PYEGERIA_WEB("abbb8ef3-2e23-4e8d-ac72-77e4709cc212",
                 "PYEGERIA-WEB",
                 SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                 OpenMetadataType.SOFTWARE_SERVER.typeName,
                 null,
                 "Pyegeria-Web",
                 "REST-ful service for calling Dr.Egeria from local tools.",
                 new SolutionComponentDefinition[]{DeployedImplementationType.PYEGERIA.getSolutionComponent()},
                 null,
                 null),

    USER_WORKSPACE("331875c2-0876-4211-bc52-b18f1e1bb478",
                   "USER-WORKSPACE",
                   SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                   OpenMetadataType.FILE_FOLDER.typeName,
                   null,
                   "User Workspace",
                   "Directory on the file systems where a user can store their files.",
                   new SolutionComponentDefinition[]{
                           DeployedImplementationType.JUPYTER_NOTEBOOK.getSolutionComponent(),
                           DeployedImplementationType.MARKDOWN_DOCUMENT.getSolutionComponent(),
                           DeployedImplementationType.WEBPAGE.getSolutionComponent()},
                   null,
                   null),


    ;


    private final String                             guid;
    private final String                             identifier;
    private final String                             componentType;
    private final String                             implementationType;
    private final String                             url;
    private final String                             displayName;
    private final String                             description;
    private final SolutionComponentDefinition[]      subComponents;
    private final InformationSupplyChainDefinition[] linkedFromSegment;
    private final String                             implementationResource;


    /**
     * Construct an enum instance.
     *
     * @param guid                   unique identifier
     * @param identifier             identifier
     * @param componentType          type of solution component - ege automated process
     * @param implementationType     type of software component - for example, is it a process, of file or database.
     * @param url                    URL to more information
     * @param displayName            display name of solution component
     * @param description            description of solution component
     * @param subComponents          optional subcomponents of the solution
     * @param linkedFromSegment      links to information supply chain
     * @param implementationResource components useful when creating implementations
     */
    EgeriaSolutionComponent(String                        guid,
                            String                        identifier,
                            String                        componentType,
                            String                        implementationType,
                            String                        url,
                            String                        displayName,
                            String                        description,
                            SolutionComponentDefinition[] subComponents,
                            InformationSupplyChainDefinition[] linkedFromSegment,
                            String                             implementationResource)
    {
        this.guid                   = guid;
        this.identifier             = identifier;
        this.componentType          = componentType;
        this.implementationType     = implementationType;
        this.url                    = url;
        this.displayName            = displayName;
        this.description            = description;
        this.subComponents          = subComponents;
        this.linkedFromSegment      = linkedFromSegment;
        this.implementationResource = implementationResource;
    }


    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    @Override
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the type of solution component - for example, is it a process, of file or database.
     *
     * @return string
     */
    @Override
    public String getComponentType()
    {
        return componentType;
    }


    /**
     * Return which type of software component is likely to be deployed to implement this solution component.
     *
     * @return string
     */
    @Override
    public String getImplementationType()
    {
        return implementationType;
    }


    /**
     * Return an optional URL that describes this solution component.
     *
     * @return string
     */
    @Override
    public String getURL()
    {
        return url;
    }


    /**
     * Return the display name of the solution component.
     *
     * @return string
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution component.
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


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
     * Return the optional list of subcomponents.
     *
     * @return null or list
     */
    @Override
    public List<SolutionComponentDefinition> getSubComponents()
    {
        if (subComponents == null)
        {
            return null;
        }

        return Arrays.asList(subComponents);
    }


    /**
     * Return the segments that preceded this segment.
     *
     * @return list of segments
     */
    @Override
    public List<InformationSupplyChainDefinition> getLinkedFromSegment()
    {
        if (linkedFromSegment == null)
        {
            return null;
        }

        return Arrays.asList(linkedFromSegment);
    }



    /**
     * Return the GUID of the implementation element (or null)
     *
     * @return guid
     */
    @Override
    public String getImplementationResource()
    {
        return implementationResource;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "EgeriaSolutionComponent{" + displayName + '}';
    }
}
