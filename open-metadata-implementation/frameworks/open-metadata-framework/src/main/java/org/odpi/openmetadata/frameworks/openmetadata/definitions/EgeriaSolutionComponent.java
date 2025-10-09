/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

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
    OPEN_METADATA_REPOSITORY("1dcdc147-e023-4480-ae5c-93c009927b1a",
                             "OPEN-METADATA-REPOSITORY",
                             SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                             DeployedImplementationType.OPEN_METADATA_REPOSITORY.getAssociatedTypeName(),
                             DeployedImplementationType.OPEN_METADATA_REPOSITORY.getDeployedImplementationType(),
                             DeployedImplementationType.OPEN_METADATA_REPOSITORY.getDescription(),
                             null,
                             null),

    METADATA_ACCESS_STORE("74385479-73a1-4e61-9c2f-a98be526acc7",
                          "METADATA-ACCESS-STORE",
                          SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                          EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getAssociatedTypeName(),
                          EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getDeployedImplementationType(),
                          EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getDescription(),
                          new EgeriaSolutionComponent[]{OPEN_METADATA_REPOSITORY},
                          null),


    INTEGRATION_DAEMON("31d227b4-08b2-4544-bd31-f7f1d1f65ec9",
                       "INTEGRATION-DAEMON",
                       SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                       EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getAssociatedTypeName(),
                       EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getDeployedImplementationType(),
                       EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getDescription(),
                       null,
                       null),


    ENGINE_HOST("0ec8aaf3-42cf-4a81-83fa-2db6b20c7507",
                "ENGINE-HOST",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                EgeriaDeployedImplementationType.ENGINE_HOST.getAssociatedTypeName(),
                EgeriaDeployedImplementationType.ENGINE_HOST.getDeployedImplementationType(),
                EgeriaDeployedImplementationType.ENGINE_HOST.getDescription(),
                null,
                null),


    VIEW_SERVER("5037b995-3e3e-4ad1-94f7-1d7960f28b44",
                "VIEW-SERVER",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                EgeriaDeployedImplementationType.VIEW_SERVER.getAssociatedTypeName(),
                EgeriaDeployedImplementationType.VIEW_SERVER.getDeployedImplementationType(),
                EgeriaDeployedImplementationType.VIEW_SERVER.getDescription(),
                null,
                null),

    SERVER_PLATFORM("1442a1bc-f791-4fda-a3be-56fb68934a4c",
                    "SERVER-PLATFORM",
                    SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                    EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getAssociatedTypeName(),
                    EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType(),
                    EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDescription(),
                    new EgeriaSolutionComponent[]{METADATA_ACCESS_STORE, VIEW_SERVER, INTEGRATION_DAEMON, ENGINE_HOST},
                    null),

    LOAD_ARCHIVE("cc3e0a81-ad10-4c72-b6a5-01301c5dd587",
                 "LOAD_ARCHIVE",
                 SolutionComponentType.CONSOLE_COMMAND.getSolutionComponentType(),
                 null,
                 "Load Archive",
                 "A command to load an open metadata archive.",
                 null,
                 null),

    PYEGERIA("c3fd85ae-4226-4d20-b57f-af3b0e748e5f",
             "PYEGERIA",
             SolutionComponentType.SOFTWARE_LIBRARY.getSolutionComponentType(),
             OpenMetadataType.SOFTWARE_LIBRARY_CLASSIFICATION.typeName,
             "pyegeria",
             "Python language library supporting calls to Egeria's REST APIs",
            null,
             null),

    MY_EGERIA("57f6a0c7-fbbe-42b2-a30f-3298a8816096",
             "MY-EGERIA",
             SolutionComponentType.USER_INTERFACE.getSolutionComponentType(),
             null,
             "my_egeria",
             "A user interface for working with Egeria and open metadata.",
             new EgeriaSolutionComponent[]{PYEGERIA},
             null),

    DR_EGERIA("1ddd9283-3dc9-4220-ac42-be0894b5a930",
              "DR-EGERIA",
              SolutionComponentType.SOFTWARE_LIBRARY.getSolutionComponentType(),
              null,
              "Dr.Egeria",
              "A markdown processor that can maintain and retrieve open metadata by processing and creating markdown documents.",
              new EgeriaSolutionComponent[]{PYEGERIA},
              null),

    HEY_EGERIA("b2278203-63a6-4a4f-b142-6f75cd592415",
              "HEY-EGERIA",
              SolutionComponentType.CONSOLE_COMMAND.getSolutionComponentType(),
              null,
              "hey_egeria",
              "A user interface for working with Egeria and open metadata.",
              new EgeriaSolutionComponent[]{
                      PYEGERIA,
                      LOAD_ARCHIVE},
              null),


    OPEN_METADATA_ARCHIVE("8624e13c-6b08-417e-8aee-5d78a5278af2",
                          "OPEN-METADATA-ARCHIVE",
                          SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                          OpenMetadataType.ARCHIVE_FILE.typeName,
                          "Open Metadata Archive",
                          "An archive file containing pre-defined metadata types and instances.",
                          null,
                          null),

    EGERIA_BUILD("f763d7df-1d49-4ceb-a079-c7f40e15982b",
                 "EGERIA-BUILD",
                 SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                 OpenMetadataType.BUILD_INSTRUCTION_FILE.typeName,
                 "Egeria Gradle Build",
                 "Comprehensive build process that complies the software, runs tests and creates the assembly for Egeria's runtime.",
                 null,
                 null),


    JUPYTER_HUB("e3e3a894-43a8-4745-a220-c3c047c01688",
                "JUPYTER-HUB",
                SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                "JupyterHub",
                "Supports browser based interaction with Jupyter Notebooks.",
                new EgeriaSolutionComponent[]{PYEGERIA},
                null),

    DEFAULT_UNITY_CATALOG("d22f1b91-61d9-4454-b4a3-be07a3336874",
                          "DEFAULT-UNITY-CATALOG",
                          SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                          OpenMetadataType.SOFTWARE_SERVER.typeName,
                          "Default Unity Catalog",
                          "Open Source version of Unity Catalog with the default catalog.",
                          null,
                          null),

    RDBMS_UNITY_CATALOG("8b472a17-9c2b-4a31-81b2-7c81fbdb148a",
                          "UNITY-CATALOG-ON-POSTGRES",
                          SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                          OpenMetadataType.SOFTWARE_SERVER.typeName,
                          "Working Unity Catalog",
                          "Open Source version of Unity Catalog backed by a PostgreSQL database.  Initialized with no content.",
                          null,
                          null),

    OL_PROXY("19af8706-17b9-4367-865f-2181dcb51646",
             "OPEN-LINEAGE-PROXY",
             SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
             OpenMetadataType.SOFTWARE_SERVER.typeName,
             "Open Lineage Proxy",
             "Consolidates open lineage events from data engines and pipes them to Apache Kafka.",
             null,
             null),

    OL_KAFKA_TOPIC("d4b3a089-b909-4f49-9d95-bbbba7008f17",
             "OPEN-LINEAGE-TOPIC",
             SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
             OpenMetadataType.TOPIC.typeName,
             "Open Lineage Topic",
             "Stores and distributes Open Lineage Events.",
             null,
             null),

    APACHE_AIRFLOW_JOB("1687e842-6f66-4871-b844-3f96a9f4391f",
                       "APACHE-KAFKA",
                       SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                       OpenMetadataType.SOFTWARE_SERVER.typeName,
                       "Apache Airflow Job",
                       "Runs data movement and transformation pipelines.",
                       new EgeriaSolutionComponent[]{PYEGERIA},
                       null),
    APACHE_AIRFLOW("6db0416a-1e7f-4e7c-aae6-8925c2148820",
                   "APACHE-AIRFLOW",
                   SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                   OpenMetadataType.SOFTWARE_SERVER.typeName,
                   "Apache Airflow",
                   "Runs data movement and transformation pipelines.",
                   new EgeriaSolutionComponent[]{APACHE_AIRFLOW_JOB},
                   null),

    OPEN_METADATA_TOPIC("03310995-216c-49e7-a9fe-8e789b11d37d",
                        "OPEN-METADATA-TOPIC",
                        SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                        OpenMetadataType.TOPIC.typeName,
                        "Open Metadata Topic",
                        "Provides notifications when open metadata changes.",
                        null,
                        null),

    APACHE_KAFKA("d52a42e9-87a1-4382-aa0e-d0a3a63465f6",
                   "APACHE-KAFKA",
                   SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                   OpenMetadataType.SOFTWARE_SERVER.typeName,
                   "Apache Kafka",
                   "Manages a reliable topic based service.",
                   new EgeriaSolutionComponent[]{OL_KAFKA_TOPIC, OPEN_METADATA_TOPIC},
                   null),

    EGERIA_POSTGRESQL_DATABASE("ac2df71c-6041-42b9-b96b-ae036a32f5d8",
                               "EGERIA-POSTGRESQL-DATABASE",
                               SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                               OpenMetadataType.RELATIONAL_DATABASE.typeName,
                               "Egeria PostgreSQL Database",
                               "This database supports the different repositories used by the Egeria runtime.",
                               new EgeriaSolutionComponent[]{OPEN_METADATA_REPOSITORY},
                               null),

    UC_POSTGRESQL_DATABASE("0f732681-e40c-48dc-b5b3-c1e78c05da29",
                               "UNITY-CATALOG-POSTGRESQL-DATABASE",
                               SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                               OpenMetadataType.RELATIONAL_DATABASE.typeName,
                               "Unity Catalog PostgreSQL Database",
                               "This database supports the catalog storage for Unity Catalog.",
                               null,
                               null),


    SUPERSET_POSTGRESQL_DATABASE("a072d124-84ab-45e8-913e-2268cf16ccb6",
                           "SUPERSET-POSTGRESQL-DATABASE",
                           SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                           OpenMetadataType.RELATIONAL_DATABASE.typeName,
                           "Superset PostgreSQL Database",
                           "This database supports the catalog storage for Apache Superset.",
                           null,
                           null),


    AIRFLOW_POSTGRESQL_DATABASE("5b2367e0-d03c-4bd4-bea0-363cf0efd1ee",
                                 "AIRFLOW-POSTGRESQL-DATABASE",
                                 SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                 OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                 "Apache Airflow PostgreSQL Database",
                                 "This database supports the catalog storage for Apache Airflow.",
                                 null,
                                 null),

    MARQUEZ_POSTGRESQL_DATABASE("3093e1c1-f4bf-4406-8c80-a252393f0071",
                                "MARQUEZ-POSTGRESQL-DATABASE",
                                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                "Marquez PostgreSQL Database",
                                "This database supports the catalog storage for Marquez.",
                                null,
                                null),

    POSTGRES_SERVER("235a335a-f010-43ee-b785-3774380b5058",
                 "POSTGRES-SERVER",
                 SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                 OpenMetadataType.SOFTWARE_SERVER.typeName,
                 "PostgreSQL Server",
                 "Hosts relational databases.",
                 new EgeriaSolutionComponent[]{
                         EGERIA_POSTGRESQL_DATABASE,
                         UC_POSTGRESQL_DATABASE,
                         SUPERSET_POSTGRESQL_DATABASE,
                         AIRFLOW_POSTGRESQL_DATABASE,
                         MARQUEZ_POSTGRESQL_DATABASE,
                 },
                 null),

    APACHE_SUPERSET("bea991ef-fe4d-441b-a5c3-70ce595ffe43",
                 "APACHE-SUPERSET",
                 SolutionComponentType.USER_INTERFACE.getSolutionComponentType(),
                 OpenMetadataType.SOFTWARE_SERVER.typeName,
                 "Apache Superset",
                 "Manages the definition and display of reports.",
                 null,
                 null),

    MARQUEZ("61fa7d16-94a6-4a58-a431-8be05f15ea71",
                    "MARQUEZ",
                    SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    "Marquez",
                    "Runs data movement and transformation pipelines.",
                    null,
                    null),

    APACHE_WEB_SERVER("2b9976a6-1466-4b82-be8b-61e80d1adaef",
                      "APACHE-WEB-SERVER",
                      SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                      "Apache Web Server",
                      "Services HTML documents written to the file system.",
                      null,
                      null),

    PYEGERIA_WEB("abbb8ef3-2e23-4e8d-ac72-77e4709cc212",
                      "PYEGERIA-WEB",
                      SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                      OpenMetadataType.SOFTWARE_SERVER.typeName,
                      "Pyegeria-Web",
                      "REST-ful service for calling Dr.Egeria from local tools.",
                      new EgeriaSolutionComponent[]{PYEGERIA},
                      null),

    MARKDOWN_DOCUMENT("fd26eb09-ec95-4d83-b478-c8caee4b1c21",
                   "MARKDOWN-DOCUMENT",
                   SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                   OpenMetadataType.DOCUMENT.typeName,
                   "Markdown Document",
                   "Text file encoded using markdown tags and layout.",
                   null,
                   null),

    HTML_WEBPAGE("843cf81f-ae76-418e-9df3-bb2595e08c15",
                     "HTML-WEBPAGE",
                     SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                     OpenMetadataType.DOCUMENT.typeName,
                     "HTML Webpage",
                     "Text file encoded using HTML tags.",
                     null,
                     null),

    JUPYTER_NOTEBOOK("3af1e6e6-7a65-4d67-8cfd-d202cc5660d7",
                     "JUPYTER-NOTEBOOK",
                     SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                     OpenMetadataType.JSON_FILE.typeName,
                     "Jupyter Notebook",
                     "Text file encoded in JSON that describes a mixture of python code and descriptive text.",
                     null,
                     null),

    USER_WORKSPACE("331875c2-0876-4211-bc52-b18f1e1bb478",
                 "USER-WORKSPACE",
                 SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                 OpenMetadataType.FILE_FOLDER.typeName,
                 "User Workspace",
                 "Directory on the file systems where a user can store their files.",
                 new EgeriaSolutionComponent[]{JUPYTER_NOTEBOOK, MARKDOWN_DOCUMENT, HTML_WEBPAGE},
                 null),


    ;


    private final String                    guid;
    private final String                    identifier;
    private final String                    componentType;
    private final String                    implementationType;
    private final String                    displayName;
    private final String                    description;
    private final EgeriaSolutionComponent[] subComponents;
    private final String                    implementationResource;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param componentType   type of solution component - ege automated process
     * @param implementationType   type of software component - for example, is it a process, of file or database.
     * @param displayName display name of solution component
     * @param description description of solution component
     * @param subComponents optional subcomponents of the solution
     * @param implementationResource components useful when creating implementations
     */
    EgeriaSolutionComponent(String                    guid,
                            String                    identifier,
                            String                    componentType,
                            String                    implementationType,
                            String                    displayName,
                            String                    description,
                            EgeriaSolutionComponent[] subComponents,
                            String                    implementationResource)
    {
        this.guid                   = guid;
        this.identifier             = identifier;
        this.componentType          = componentType;
        this.implementationType     = implementationType;
        this.displayName            = displayName;
        this.description            = description;
        this.subComponents          = subComponents;
        this.implementationResource = implementationResource;
    }


    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the type of solution component - for example, is it a process, of file or database.
     *
     * @return string
     */
    public String getComponentType()
    {
        return componentType;
    }


    /**
     * Return which type of software component is likely to be deployed to implement this solution component.
     *
     * @return string
     */
    public String getImplementationType()
    {
        return implementationType;
    }


    /**
     * Return the display name of the solution component.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the solution component.
     *
     * @return string
     */
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
