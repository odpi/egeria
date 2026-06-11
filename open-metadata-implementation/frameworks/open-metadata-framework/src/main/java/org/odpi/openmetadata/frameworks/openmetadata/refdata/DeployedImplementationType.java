/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import java.util.Arrays;
import java.util.List;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria. These are encoded in the
 * CoreContentPack.omarchive and are available in the open metadata repository as valid values.
 */
public enum DeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * Root technology type.
     */
    TECHNOLOGY("2b406762-fa26-4ac7-8de8-98d5dd05eaa6",
               "Root Technology Type",
               null,
               OpenMetadataType.REFERENCEABLE.typeName,
               null,
               "Root technology type.",
               "https://egeria-project.org/concepts/deployed-implementation-type/"),

    /**
     * A description of a digital resource.
     */
    ASSET("0e544ca8-f5f7-49dc-ab78-6d4caedfb397",
          "Asset",
          DeployedImplementationType.TECHNOLOGY,
          OpenMetadataType.ASSET.typeName,
          null,
          "A description of a digital resource.",
          "https://egeria-project.org/concepts/asset/"),

    /**
     * A well-defined set of processing steps and decisions that drive a particular aspect of the organization's business.
     */
    PROCESS("eca3e5eb-ad51-4637-82f3-3071c6bf0ffb",
            "Process",
            DeployedImplementationType.ASSET,
            OpenMetadataType.PROCESS.typeName,
            null,
            "A well-defined set of processing steps and decisions that drive a particular aspect of the organization's business.",
            "https://egeria-project.org/concepts/process/"),

    /**
     * A collection of data, either at rest or in motion.
     */
    DATA_ASSET("df79a55a-8cd8-4cf5-a35c-c8bea53db0c4",
               "Data Asset",
               DeployedImplementationType.ASSET,
               OpenMetadataType.DATA_ASSET.typeName,
               null,
               "A collection of data, either at rest or in motion.",
               "https://egeria-project.org/concepts/asset/",
               "ff63580f-2780-4972-bae2-04b56d4bc784",
               SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
               "DATA-ASSET",
               null),

    /**
     * A collection of data, either at rest or in motion.
     */
    DATA_SET("44ad5f7e-804d-4d24-a837-0e7021b12996",
             "Data Set",
             DeployedImplementationType.DATA_ASSET,
             OpenMetadataType.DATA_ASSET.typeName,
             null,
             "A logical collection of data, either at rest or in motion.",
             "https://egeria-project.org/concepts/asset/"),


    /**
     * A logical collection of data, either at rest or in motion, organized into a tabular (columnar) format.
     */
    TABULAR_DATA_SET("f3d81f48-1824-4608-962e-90f3f239e861",
                     "Tabular Data Set",
                     DeployedImplementationType.DATA_SET,
                     OpenMetadataType.TABULAR_DATA_SET.typeName,
                     null,
                     "A logical collection of data, either at rest or in motion, organized into a tabular (columnar) format.",
                     "https://egeria-project.org/concepts/tabular-data-set/",
                     "5ec9db1a-14e8-4e36-b727-7a3fdfa511ea",
                     SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                     "TABULAR-DATA-SET",
                     new SolutionComponentDefinition[]{DATA_ASSET.getSolutionComponent()}),

    /**
     * A collection of tabular data sets.
     */
    TABULAR_DATA_SET_COLLECTION("341e6c53-0fb2-4529-a4cc-8d2fadeec67a",
                                "Tabular Data Set Collection",
                                DeployedImplementationType.DATA_SET,
                                OpenMetadataType.TABULAR_DATA_SET.typeName,
                                null,
                                "A collection of tabular data sets.",
                                "https://egeria-project.org/concepts/tabular-data-set/",
                                "1c06aeff-cee7-4d0d-87c5-65876a8a395e",
                                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                "TABULAR-DATA-SET-COLLECTION",
                                new SolutionComponentDefinition[]{TABULAR_DATA_SET.getSolutionComponent()}),


    /**
     * A data set containing metadata.
     */
    METADATA_COLLECTION("aafc544d-9b9d-4d9c-86a7-3d6ecaf26fb9",
                        "Metadata Collection",
                        DeployedImplementationType.DATA_SET,
                        OpenMetadataType.METADATA_COLLECTION.typeName,
                        null,
                        OpenMetadataType.METADATA_COLLECTION.description,
                        OpenMetadataWikiPages.MODEL_0225_METADATA_REPOSITORIES),

    /**
     * A data set containing user accounts, groups, roles, and security controls.
     */
    SECRETS_COLLECTION("60e38b0c-9220-4a83-9c9e-e83cb5a6c19a",
                        "Secrets Collection",
                        DeployedImplementationType.DATA_SET,
                        OpenMetadataType.SECRETS_COLLECTION.typeName,
                        null,
                        OpenMetadataType.SECRETS_COLLECTION.description,
                        OpenMetadataWikiPages.MODEL_0227_KEYSTORES),

    /**
     * A data store containing cohort membership registration details.
     */
    COHORT_REGISTRY_STORE("6602791d-eb14-4bb6-ae91-7430018b312d",
                          "Cohort Registry Store",
                          null,
                          OpenMetadataType.COHORT_REGISTRY_STORE.typeName,
                          null,
                          OpenMetadataType.COHORT_REGISTRY_STORE.description,
                          OpenMetadataWikiPages.MODEL_0225_METADATA_REPOSITORIES),


    /**
     * A data store containing metadata.
     */
    METADATA_REPOSITORY("3e88a901-804c-4a4a-a89c-983f76e7966b",
                        "Metadata Repository",
                        null,
                        OpenMetadataType.METADATA_REPOSITORY.typeName,
                        null,
                        OpenMetadataType.METADATA_REPOSITORY.description,
                        OpenMetadataWikiPages.MODEL_0225_METADATA_REPOSITORIES),


    /**
     * A metadata repository supporting open metadata types and interfaces.
     */
    OPEN_METADATA_REPOSITORY("106a41b6-20a5-4c12-b62f-26c328d385f2",
                             "Open Metadata Repository",
                             METADATA_REPOSITORY,
                             OpenMetadataType.METADATA_REPOSITORY.typeName,
                             null,
                             "A metadata repository supporting open metadata types and interfaces.",
                             "https://egeria-project.org/concepts/open-metadata-repository/",
                             "1dcdc147-e023-4480-ae5c-93c009927b1a",
                             SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                             "OPEN-METADATA-REPOSITORY",
                             null),


    /**
     * A file containing externally accessible data - other fields provide information on the internal format.
     */
    FILE("b5bb473d-947e-4ffb-8605-b8364c5ce85c",
         "File",
         DeployedImplementationType.DATA_ASSET,
         OpenMetadataType.DATA_FILE.typeName,
         null,
         OpenMetadataType.DATA_FILE.description,
         OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
         "5ac2a078-0b81-487e-b8f0-095e544aaf52",
         SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
         "FILE",
         null),

    /**
     * A file containing program logic.
     */
    PROGRAM_FILE("822af572-069f-4f89-8a52-9a7d5a70092f",
                 "Program File",
                 DeployedImplementationType.FILE,
                 OpenMetadataType.DATA_FILE.typeName,
                 null,
                 "A file containing program logic.",
                 OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),

    /**
     * A file containing program logic.
     */
    DATA_FILE("4985b866-8a79-4298-9b87-fcb5b0c42692",
              "Data File",
              DeployedImplementationType.FILE,
              OpenMetadataType.DATA_FILE.typeName,
              null,
              "A file containing data.  This may be structured data, text or some form or media file.",
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
              "f689b431-3d7a-4f3e-8162-de475d050033",
              SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
              "DATA-FILE",
              null),

    /**
     * CSV Data File.
     */
    CSV_FILE("a312f9d3-0fbb-4d98-8dc9-b298ec972b9e",
             "CSV Data File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.CSV_FILE.typeName,
             null,
             OpenMetadataType.CSV_FILE.description,
             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
             "9f95ea51-562c-4762-abfe-9a4acbe80f3b",
             SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
             "CSV-FILE",
             null),

    /**
     * Avro Data File
     */
    AVRO_FILE("f0fee71a-ed03-44f7-aee3-633732473250",
              "Avro Data File",
              DeployedImplementationType.DATA_FILE,
              OpenMetadataType.AVRO_FILE.typeName,
              null,
              OpenMetadataType.AVRO_FILE.description,
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * JSON Data File
     */
    JSON_FILE("f8cd55d2-394e-4589-b16b-b100eeb6f158",
              "JSON Data File",
              DeployedImplementationType.DATA_FILE,
              OpenMetadataType.JSON_FILE.typeName,
              null,
              OpenMetadataType.JSON_FILE.description,
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * Jupyter Notebook File
     */
    JUPYTER_NOTEBOOK("15dfcf08-d73c-48ae-94e7-dfc3bc916265",
                     "Jupyter Notebook File",
                     DeployedImplementationType.JSON_FILE,
                     OpenMetadataType.JSON_FILE.typeName,
                     null,
                     "Text file encoded in JSON that describes a mixture of python code and descriptive text.",
                     "https://jupyter.org/",
                     "3af1e6e6-7a65-4d67-8cfd-d202cc5660d7",
                     SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                     "JUPYTER-NOTEBOOK",
                     null),

    /**
     * Parquet Data File
     */
    PARQUET_FILE("734d12c6-0459-42ef-bd41-2ce6e45559ba",
                 "Parquet Data File",
                 DeployedImplementationType.DATA_FILE,
                 OpenMetadataType.PARQUET_FILE.typeName,
                 null,
                 OpenMetadataType.PARQUET_FILE.description,
                 OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * Spreadsheet Data File
     */
    SPREADSHEET_FILE("742ae07c-ec29-482b-9260-4ab6bd8dac62",
                     "Spreadsheet Data File",
                     DeployedImplementationType.DATA_FILE,
                     OpenMetadataType.SPREADSHEET_FILE.typeName,
                     null,
                     OpenMetadataType.SPREADSHEET_FILE.description,
                     OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * XML Data File
     */
    XML_FILE("5f5d53fd-028d-4870-8882-1792ce8d353e",
             "XML Data File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.XML_FILE.typeName,
             null,
             OpenMetadataType.XML_FILE.description,
             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),


    /**
     * Document File
     */
    DOCUMENT("54b9def5-f543-4259-afa5-10c89c38313d",
             "Document File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.DOCUMENT.typeName,
             null,
             OpenMetadataType.DOCUMENT.description,
             OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),

    /**
     * Markdown Document File
     */
    MARKDOWN_DOCUMENT("fdfc2d1a-9844-4965-a0cc-ccfbb2f623e3",
                      "Markdown Document File",
                      DeployedImplementationType.DOCUMENT,
                      OpenMetadataType.DOCUMENT.typeName,
                      null,
                      "Text file encoded using markdown tags and layout.",
                      "https://en.wikipedia.org/wiki/Markdown",
                      "fd26eb09-ec95-4d83-b478-c8caee4b1c21",
                      SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                      "MARKDOWN-DOCUMENT",
                      null),


    /**
     * Webpage File
     */
    WEBPAGE("ac3c99d9-4e5a-430b-aac2-091443c95349",
            "Webpage File",
            DeployedImplementationType.DOCUMENT,
            OpenMetadataType.DOCUMENT.typeName,
            null,
            "Text file encoded using HTML tags plus optional script tags, such as JavaScript and CSS.",
            "https://en.wikipedia.org/wiki/HTML",
            "843cf81f-ae76-418e-9df3-bb2595e08c15",
            SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
            "WEBPAGE",
            null),


    /**
     * Audio Data File
     */
    AUDIO_DATA_FILE("e08ff316-5e85-4088-9cd5-d737ed81ccc2",
                    "Audio Data File",
                    DeployedImplementationType.DATA_FILE,
                    OpenMetadataType.AUDIO_FILE.typeName,
                    null,
                    OpenMetadataType.AUDIO_FILE.description,
                    OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),


    /**
     * Video Data File
     */
    VIDEO_DATA_FILE("4eca6f52-6d0d-4219-8428-b70a841276d0",
                    "Video Data File",
                    DeployedImplementationType.DATA_FILE,
                    OpenMetadataType.VIDEO_FILE.typeName,
                    null,
                    OpenMetadataType.VIDEO_FILE.description,
                    OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),


    /**
     * 3D Image Data File
     */
    THREE_D_IMAGE_DATA_FILE("8c7cc3be-365a-48db-bfba-a64da2a441e4",
                            "3D Image Data File",
                            DeployedImplementationType.DATA_FILE,
                            OpenMetadataType.THREE_D_IMAGE_FILE.typeName,
                            null,
                            OpenMetadataType.THREE_D_IMAGE_FILE.description,
                            OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),

    /**
     * Raster Data File
     */
    RASTER_DATA_FILE("c9800490-8706-4d91-bd8f-28813239add3",
                     "Raster Data File",
                     DeployedImplementationType.DATA_FILE,
                     OpenMetadataType.RASTER_FILE.typeName,
                     null,
                     OpenMetadataType.RASTER_FILE.description,
                     OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),

    /**
     * Vector Data File
     */
    VECTOR_DATA_FILE("acefe346-0ab9-4a64-af9b-36f5bfc85508",
                     "Vector Data File",
                     DeployedImplementationType.DATA_FILE,
                     OpenMetadataType.VECTOR_FILE.typeName,
                     null,
                     OpenMetadataType.VECTOR_FILE.description,
                     OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES),


    /**
     * Source Code File
     */
    SOURCE_CODE_FILE("d615f94a-fc78-4f23-8b35-f4af454bc01e",
                     "Source Code File",
                     DeployedImplementationType.PROGRAM_FILE,
                     OpenMetadataType.SOURCE_CODE_FILE.typeName,
                     null,
                     OpenMetadataType.SOURCE_CODE_FILE.description,
                     OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),

    /**
     * Build Instruction File
     */
    BUILD_FILE("d5fa0c48-e24c-44b4-910c-a4f29ec0f889",
               "Build Instruction File",
               DeployedImplementationType.PROGRAM_FILE,
               OpenMetadataType.BUILD_INSTRUCTION_FILE.typeName,
               null,
               OpenMetadataType.BUILD_INSTRUCTION_FILE.description,
               OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),

    /**
     * Executable File
     */
    EXECUTABLE_FILE("9b12b93a-60f7-495a-8e6a-4f2a75d1bed5",
                    "Executable File",
                    DeployedImplementationType.PROGRAM_FILE,
                    OpenMetadataType.EXECUTABLE_FILE.typeName,
                    null,
                    OpenMetadataType.EXECUTABLE_FILE.description,
                    OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * Script File
     */
    SCRIPT_FILE("e348b1a3-3bc7-4921-be4f-572fecde32e4",
                "Script File",
                DeployedImplementationType.PROGRAM_FILE,
                OpenMetadataType.SCRIPT_FILE.typeName,
                null,
                OpenMetadataType.SCRIPT_FILE.description,
                OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * Properties File
     */
    PROPERTIES_FILE("f83e006c-a2bf-40e5-8c04-45e93dee4967",
                    "Properties File",
                    DeployedImplementationType.PROGRAM_FILE,
                    OpenMetadataType.PROPERTIES_FILE.typeName,
                    null,
                    OpenMetadataType.PROPERTIES_FILE.description,
                    OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * YAML File
     */
    YAML_FILE("d650b328-4cd0-4f0f-b0b5-41bc8951c902",
              "YAML File",
              DeployedImplementationType.PROPERTIES_FILE,
              OpenMetadataType.YAML_FILE.typeName,
              null,
              OpenMetadataType.YAML_FILE.description,
              OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS),


    /**
     * Log File
     */
    LOG_FILE("b924be69-f87f-4807-8261-5a899a3508dd",
             "Log File",
             DeployedImplementationType.DATA_FILE,
             OpenMetadataType.LOG_FILE.typeName,
             null,
             OpenMetadataType.LOG_FILE.description,
             OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS),


    /**
     * Archive File
     */
    ARCHIVE_FILE("6148ab2c-26e3-4cb5-870d-d95d070c910f",
                 "Archive File",
                 DeployedImplementationType.DATA_FILE,
                 OpenMetadataType.ARCHIVE_FILE.typeName,
                 null,
                 OpenMetadataType.ARCHIVE_FILE.description,
                 OpenMetadataWikiPages.MODEL_0226_ARCHIVE_FILES),


    /**
     * Open Metadata Archive
     */
    OPEN_METADATA_ARCHIVE("93d47fe4-8b62-4c1e-af89-26743abc6bc8",
                          "Open Metadata Archive",
                          DeployedImplementationType.ARCHIVE_FILE,
                          OpenMetadataType.ARCHIVE_FILE.typeName,
                          null,
                          "An archive file containing pre-defined metadata types and instances.",
                          "https://egeria-project.org/concepts/open-metadata-archive/",
                          "8624e13c-6b08-417e-8aee-5d78a5278af2",
                          SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                          "OPEN-METADATA-ARCHIVE",
                          null),

    /**
     * Keystore File
     */
    KEYSTORE_FILE("409f12a7-b4a3-4e0f-a9bd-8094813346d6",
                  "Keystore File",
                  DeployedImplementationType.DATA_FILE,
                  OpenMetadataType.KEY_STORE_FILE.typeName,
                  null,
                  OpenMetadataType.KEY_STORE_FILE.description,
                  OpenMetadataWikiPages.MODEL_0226_ARCHIVE_FILES),


    /**
     * A data file containing user accounts, groups, roles, and security controls, encoded in YAML format.
     */
    YAML_SECRETS_COLLECTION_FILE("6480544e-bf2e-4024-90ac-dcd166f17248",
                                 "YAML File Secrets Collection",
                                 DeployedImplementationType.KEYSTORE_FILE,
                                 OpenMetadataType.KEY_STORE_FILE.typeName,
                                 null,
                                 "A data file containing user accounts, groups, roles, and security controls, encoded in YAML format.",
                                 OpenMetadataWikiPages.MODEL_0227_KEYSTORES),

    /**
     * A directory (folder) that holds files that are potential data sources.
     */
    FILE_SYSTEM_DIRECTORY("d1986b2f-cc82-4a5a-9e9a-559ec9e98edc",
                          "File System Directory",
                          DeployedImplementationType.DATA_ASSET,
                          OpenMetadataType.FILE_FOLDER.typeName,
                          null,
                          "A directory (folder) that holds files that are potential data sources in a file system.",
                          OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                          "40719374-f686-4faf-8675-e1863bdafa1d",
                          SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                          "FILE-DIRECTORY",
                          new SolutionComponentDefinition[]{
                                  DeployedImplementationType.FILE.getSolutionComponent()
                          }),


    /**
     * A directory (folder) that holds files representing a single data source.
     */
    DATA_FOLDER("0b18ef33-84ee-476d-92fc-82c109824261",
                "Data Folder",
                DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
                OpenMetadataType.DATA_FOLDER.typeName,
                null,
                "A directory (folder) that holds files representing a single data source.",
                OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                "db92678c-a734-4dbb-b7dd-aeaa0905462d",
                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                "DATA-FOLDER",
                new SolutionComponentDefinition[]{
                        DeployedImplementationType.FILE.getSolutionComponent()
                }),

    /**
     * A collection of logically related files representing a single data source.
     */
    DATA_FILE_COLLECTION("6cfedd7c-4aaa-49f2-8e67-b70949928ce6",
                         "Data File Collection",
                         DeployedImplementationType.DATA_SET,
                         OpenMetadataType.DATA_FILE_COLLECTION.typeName,
                         null,
                         "A collection of logically related files representing a single data source.",
                         OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS),

    /**
     * A collection of logically related CSV files each representing a tabular data set.
     */
    CSV_TABULAR_DATA_SET_COLLECTION("caec0804-102b-4364-be07-2eab863efdef",
                                    "CSV Tabular Data Set Collection",
                                    DeployedImplementationType.TABULAR_DATA_SET_COLLECTION,
                                    OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName,
                                    null,
                                    "A collection of logically related CSV files each representing a tabular data set.",
                                    OpenMetadataWikiPages.MODEL_0211_TABULAR_DATA_SETS),

    /**
     * A collection of logically related CSV files representing a single data source.
     */
    CSV_TABULAR_DATA_SET("ad10c3c7-e1f2-45d0-8318-2cae98129610",
                         "CSV Tabular Data Set",
                         DeployedImplementationType.TABULAR_DATA_SET,
                         OpenMetadataType.TABULAR_DATA_SET.typeName,
                         null,
                         "A CSV file accessed as a tabular data set.",
                         OpenMetadataWikiPages.MODEL_0211_TABULAR_DATA_SETS),

    /**
     * A database hosted on a relational database server callable through a JDBC Driver.
     */
    JDBC_RELATIONAL_DATABASE("878b6fb6-a203-42b5-a04c-4f40de319de9",
                             "JDBC Relational Database",
                             DeployedImplementationType.DATA_ASSET,
                             OpenMetadataType.RELATIONAL_DATABASE.typeName,
                             null,
                             "A database hosted on a relational database server callable through a JDBC Driver.",
                             "https://en.wikipedia.org/wiki/Java_Database_Connectivity",
                             "04fc1ac3-e456-41e2-8825-432ef03b06cf",
                             SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                             "JDBC-RELATIONAL-DATABASE",
                             null),


    /**
     * A database schema hosted on a relational database server callable through a JDBC Driver.
     */
    JDBC_RELATIONAL_DATABASE_SCHEMA("10eb9965-80d1-447a-a207-68e39c2ee755",
                                    "JDBC Relational Database Schema",
                                    DeployedImplementationType.DATA_ASSET,
                                    OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                    null,
                                    "A database schema hosted on a relational database server callable through a JDBC Driver.",
                                    "https://en.wikipedia.org/wiki/Java_Database_Connectivity"),

    /**
     * A virtual machine (VM) is a software implementation of a machine (computer) that executes programs like a physical machine
     */
    VIRTUAL_MACHINE("d990e9de-6d17-47ef-bbd6-afd144ef34f5",
                        "Virtual Machine",
                        DeployedImplementationType.TECHNOLOGY,
                        OpenMetadataType.VIRTUAL_MACHINE.typeName,
                        null,
                        "A virtual machine (VM) is a software implementation of a machine (computer) that executes programs like a physical machine.",
                        OpenMetadataWikiPages.MODEL_0035_HOSTS),

    /**
     * A computer (hardware) with an operating system for running software.
     */
    BARE_METAL_COMPUTER("f593a0c5-4a71-4fd9-9963-dbe6ba464865",
                        "Bare Metal Computer",
                        DeployedImplementationType.TECHNOLOGY,
                        OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                        null,
                        "A computer (hardware) with an operating system for running software.",
                        OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A portable computer with a screen, keyboard, and battery power.
     */
    LAPTOP_COMPUTER("38ecbb12-c172-461c-aeac-8d7da0502cf5",
                    "Laptop Computer",
                    DeployedImplementationType.BARE_METAL_COMPUTER,
                    OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                    null,
                    "A portable computer with a screen, keyboard, and battery power.",
                    OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A handheld portable device with network, touchscreen, and battery power.
     */
    SMARTPHONE("94a91316-9f23-4ab5-a907-3c8c6cfea611",
               "Smartphone",
               DeployedImplementationType.BARE_METAL_COMPUTER,
               OpenMetadataType.BARE_METAL_COMPUTER.typeName,
               null,
               "A handheld portable device with network, touchscreen, and battery power.",
               OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A smartphone from Apple Inc. that runs the iOS operating system.
     */
    I_PHONE("6330b52b-1e35-497e-8721-b94b94cb45ab",
            "Apple iPhone",
            DeployedImplementationType.SMARTPHONE,
            OpenMetadataType.BARE_METAL_COMPUTER.typeName,
            null,
            "A smartphone from Apple Inc. that runs the iOS operating system.",
            OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A smartphone that runs the Android operating system.
     */
    ANDROID_PHONE("357bd44d-0325-4d99-a0be-203ba77ce713",
                  "Android Phone",
                  DeployedImplementationType.SMARTPHONE,
                  OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                  null,
                  "A smartphone that runs the Android operating system.",
                  OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A two-handed portable device with network, touchscreen, and battery power.
     */
    TABLET("6f617a49-2f25-44d0-b507-d2e4ac3db603",
           "Tablet",
           DeployedImplementationType.BARE_METAL_COMPUTER,
           OpenMetadataType.BARE_METAL_COMPUTER.typeName,
           null,
           "A two-handed portable device with network, touchscreen, and battery power.",
           OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A tablet from Apple Inc. that runs the iOS operating system.
     */
    I_PAD("92d0e4c8-d7f5-4e8b-ad30-f6f20aaead98",
          "Apple iPad",
          DeployedImplementationType.TABLET,
          OpenMetadataType.BARE_METAL_COMPUTER.typeName,
          null,
          "A tablet from Apple Inc. that runs the iOS operating system.",
          OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A professional laptop supplied by Apple that runs the macOS operating system.
     */
    MACBOOK_PRO("b7327658-41c3-4a53-877b-dd1b7a1dbac4",
                "Apple MacBook Pro",
                DeployedImplementationType.LAPTOP_COMPUTER,
                OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                null,
                "A professional laptop supplied by Apple Inc that runs the macOS operating system.",
                OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A small hardware server enclosed in a protective case that can sit under a desk.
     */
    SMALL_FORM_FACTOR_COMPUTER("4636a5c7-b1e9-4a65-8143-97e362efcc07",
                               "Small Form Factor (SFF) Computer",
                               DeployedImplementationType.BARE_METAL_COMPUTER,
                               OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                               null,
                               "A small hardware server enclosed in a protective case that can sit under a desk.",
                               OpenMetadataWikiPages.MODEL_0035_HOSTS),


    /**
     * A virtual container using the docker platform.
     */
    DOCKER_CONTAINER("8020b056-79be-4378-9e5a-44bc6f3453b5",
                     "Docker Container",
                     TECHNOLOGY,
                     OpenMetadataType.VIRTUAL_CONTAINER.typeName,
                     null,
                     "A virtual container using the docker platform.",
                     OpenMetadataWikiPages.MODEL_0035_HOSTS),

    /**
     * A cluster of nodes for big data workloads.
     */
    HADOOP_CLUSTER("9df11118-9cac-416f-82ba-46e89ff9c693",
                   "Hadoop Cluster",
                   TECHNOLOGY,
                   OpenMetadataType.HOST_CLUSTER.typeName,
                   null,
                   "A cluster of nodes for big data workloads.",
                   OpenMetadataWikiPages.MODEL_0035_HOSTS),

    /**
     * A host cluster managing containerized applications.
     */
    KUBERNETES_CLUSTER("96248666-e040-460b-96dd-2ce10d68664f",
                       "Kubernetes Cluster",
                       TECHNOLOGY,
                       OpenMetadataType.HOST_CLUSTER.typeName,
                       null,
                       "A host cluster managing containerized applications.",
                       OpenMetadataWikiPages.MODEL_0035_HOSTS),

    /**
     * A callable software server.
     */
    SOFTWARE_SERVER("a1a96c4d-5013-4458-9c6b-6520925af6a4",
                    "Software Server",
                    DeployedImplementationType.ASSET,
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    null,
                    "A callable software server.",
                    OpenMetadataWikiPages.MODEL_0040_SOFTWARE_SERVERS,
                    "67280361-6178-4865-81af-de15edc26eab",
                    SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                    "SOFTWARE-SERVER",
                    null),

    /**
     * A software server that supports databases.
     */
    DATABASE_SERVER("ff82019c-8faa-4ac3-8f99-c3a8477743e9",
                    "Database Server",
                    DeployedImplementationType.SOFTWARE_SERVER,
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    null,
                    "A software server that supports databases.",
                    OpenMetadataWikiPages.MODEL_0040_SOFTWARE_SERVERS),


    /**
     * A software server that supports databases.
     */
    POSTGRES_SERVER("ff7c8d09-9813-453e-9895-7a2b0a6b0be3",
                    "PostgreSQL Server",
                    DeployedImplementationType.DATABASE_SERVER,
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    null,
                    "PostgreSQL is an advanced open source relational database.",
                    OpenMetadataWikiPages.MODEL_0040_SOFTWARE_SERVERS,
                    "b2a1f014-d00e-4956-bbad-0ae1d5498841",
                    SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                    "POSTGRESQL-SERVER",
                    null),


    /**
     * A software server that supports the exchange of data.
     */
    INTEGRATION_SERVER("f7ddc726-7f68-466b-a1e5-cf365d31714b",
                       "Integration Server",
                       DeployedImplementationType.SOFTWARE_SERVER,
                       OpenMetadataType.SOFTWARE_SERVER.typeName,
                       null,
                       "A software server that supports the exchange of data.",
                       OpenMetadataWikiPages.MODEL_0040_SOFTWARE_SERVERS),

    /**
     * A python component that implements a micro-workflow that runs in Apache Airflow.  DAG stands for Directed Acyclic Graph, which describes the structure of its implementation.
     */
    AIRFLOW_DAG("85b1f2da-a7ec-487a-8af9-2b108c0dad89",
                "Apache Airflow DAG",
                DeployedImplementationType.PROCESS,
                OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                null,
                "A python component that implements a micro-workflow that runs in Apache Airflow.  DAG stands for Directed Acyclic Graph, which describes the structure of its implementation.",
                OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS,
                "1687e842-6f66-4871-b844-3f96a9f4391f",
                "APACHE-AIRLOW-DAG",
                SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                null),

    /**
     * Runs data movement and transformation pipelines.
     */
    APACHE_AIRFLOW_SERVER("f55a0afd-656c-4550-994b-52c8acfc86f7",
                          "Apache Airflow Server",
                          DeployedImplementationType.INTEGRATION_SERVER,
                          OpenMetadataType.SOFTWARE_SERVER.typeName,
                          null,
                          "Runs data movement and transformation pipelines.",
                          "https://airflow.apache.org/",
                          "6db0416a-1e7f-4e7c-aae6-8925c2148820",
                          SolutionComponentType.DATA_DISTRIBUTION.getSolutionComponentType(),
                          "APACHE-AIRFLOW-SERVER",
                          new SolutionComponentDefinition[]{DeployedImplementationType.AIRFLOW_DAG.getSolutionComponent()}),

    /**
     * A software server that supports the storage of metadata.
     */
    METADATA_SERVER("a672aa09-fa2d-4dc8-86c1-a8ac9a993982",
                    "Metadata Server",
                    DeployedImplementationType.SOFTWARE_SERVER,
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    null,
                    "A software server that supports the storage of metadata.",
                    OpenMetadataWikiPages.MODEL_0040_SOFTWARE_SERVERS),


    /**
     * A callable software capability supporting specific types of assets.
     */
    SOFTWARE_CAPABILITY("a51e49cd-baa1-4195-9766-e97dae5bc7a4",
                        "Software Capability",
                        DeployedImplementationType.TECHNOLOGY,
                        OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                        null,
                        "A callable software capability supporting specific types of assets.",
                        OpenMetadataWikiPages.MODEL_0042_SOFTWARE_CAPABILITIES),

    /**
     * A data catalog for data observability.
     */
    MARQUEZ_SERVER("6f559c4c-2c94-43a5-a3e6-089f62dfe39e",
                   "Marquez Server",
                   DeployedImplementationType.METADATA_SERVER,
                   OpenMetadataType.SOFTWARE_SERVER.typeName,
                   null,
                   "A data catalog for data observability.",
                   "https://marquezproject.ai/",
                   "61fa7d16-94a6-4a58-a431-8be05f15ea71",
                   SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                   "MARQUEZ-SERVER",
                   null),

    /**
     * Manages the definition and display of reports.
     */
    APACHE_SUPERSET("1ab15ae2-b937-496a-94a5-dea035ccf5c4",
                    "Apache Superset",
                    DeployedImplementationType.METADATA_SERVER,
                    OpenMetadataType.SOFTWARE_SERVER.typeName,
                    null,
                    "Manages the definition and display of reports.",
                    "https://superset.apache.org/",
                    "bea991ef-fe4d-441b-a5c3-70ce595ffe43",
                    SolutionComponentType.USER_INTERFACE.getSolutionComponentType(),
                    "APACHE-SUPERSET",
                    null),

    /**
     * A system that manages hierarchically organized files on persistent storage.
     */
    FILE_SYSTEM("e36cdfe0-91e5-4c30-b9b6-4157ee1b5810",
                "File System",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                OpenMetadataType.FILE_SYSTEM.typeName,
                null,
                "A system that manages hierarchically organized files on persistent storage.",
                OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                "5c14f90b-ecc5-4ecb-adfe-36208671bc5d",
                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                "FILE-SYSTEM",
                new SolutionComponentDefinition[]{
                        DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getSolutionComponent(),
                        DeployedImplementationType.FILE.getSolutionComponent()
                }),

    /**
     * A UNIX capability that manages hierarchically organized files on persistent storage.
     */
    UNIX_FILE_SYSTEM("93e1b6df-f597-4d20-b846-7ece880478dc",
                     "UNIX File System",
                     DeployedImplementationType.SOFTWARE_CAPABILITY,
                     OpenMetadataType.FILE_SYSTEM.typeName,
                     null,
                     "A Unix capability that manages hierarchically organized files on persistent storage.",
                     OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS),

    /**
     * Software supporting a business function.
     */
    APPLICATION("6ac87de8-d01f-46bf-8881-0ea3204e716b",
                "Business Application",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                OpenMetadataType.APPLICATION.typeName,
                null,
                "Software supporting a business function.",
                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * Software supporting a business function.
     */
    USER_AUTHENTICATION_MANAGER("a8a29046-6a5b-4a6c-a655-56533757cd97",
                                "User Authentication Manager",
                                DeployedImplementationType.SOFTWARE_CAPABILITY,
                                OpenMetadataType.USER_AUTHENTICATION_MANAGER.typeName,
                                null,
                                "Function that validates the identity of a user via password or other form of identification",
                                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A catalog of metadata that describes assets such as deployed systems, data sources, and processes.
     */
    ASSET_CATALOG("fbe0db34-3996-4be3-8294-422bcd8732d8",
                  "Asset Metadata Catalog",
                  DeployedImplementationType.SOFTWARE_CAPABILITY,
                  OpenMetadataType.INVENTORY_CATALOG.typeName,
                  null,
                  "A catalog of metadata that describes assets such as deployed systems, data sources, and processes.",
                  OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A software capability that provides callable APIs.
     */
    API_MANAGER("18dbff2f-0909-4e85-bbe2-5b5332ddf1f8",
                "API Manager",
                DeployedImplementationType.SOFTWARE_CAPABILITY,
                OpenMetadataType.API_MANAGER.typeName,
                null,
                "A software capability that provides callable APIs.",
                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A callable network address.
     */
    ENDPOINT("69a0e224-70d7-43e4-a4af-ecd0d4fc54eb",
             "Endpoint",
             DeployedImplementationType.TECHNOLOGY,
             OpenMetadataType.ENDPOINT.typeName,
             null,
             "A callable network address.",
             OpenMetadataWikiPages.MODEL_0026_ENDPOINTS),

    /**
     * A callable network address supporting the REST protocol.
     */
    REST_API_ENDPOINT("770c5be2-a5fd-44a7-96d0-96468b9d6473",
                      "REST API Endpoint",
                      DeployedImplementationType.ENDPOINT,
                      OpenMetadataType.ENDPOINT.typeName,
                      null,
                      "A callable network address supporting the REST protocol.",
                      OpenMetadataWikiPages.MODEL_0026_ENDPOINTS,
                      "5800597a-6197-4d0a-bbbf-5e56c302df3a",
                      SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                      "REST-API-ENDPOINT",
                      null),


    /**
     * A remotely callable interface based on the REST protocol.
     */
    REST_API("3907b978-dd4b-4a66-a839-1a6415721673",
                "REST API",
                DeployedImplementationType.DATA_ASSET,
                OpenMetadataType.DEPLOYED_API.typeName,
                null,
                "A remotely callable interface based on the REST protocol.",
                OpenMetadataWikiPages.MODEL_0212_DEPLOYED_APIS),


    /**
     * A callable network address supporting the JDBC protocol.
     */
    JDBC_ENDPOINT("f23c929f-c69a-445e-a0a5-6ae20206e52d",
                  "JDBC Endpoint",
                  DeployedImplementationType.ENDPOINT,
                  OpenMetadataType.ENDPOINT.typeName,
                  null,
                  "A callable network address supporting the JDBC protocol.",
                  OpenMetadataWikiPages.MODEL_0026_ENDPOINTS),


    /**
     * A software capability that provides callable APIs supporting the REST protocol.
     */
    REST_API_MANAGER("0a9947b4-6686-4710-a64f-645f9ce430bd",
                     "REST API Manager",
                     DeployedImplementationType.API_MANAGER,
                     OpenMetadataType.API_MANAGER.typeName,
                     null,
                     "A software capability that provides callable APIs supporting the REST protocol.",
                     OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                     "47ce596f-c3a7-4b97-bdfe-134a463fbd31",
                     SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                     "REST-API-MANAGER",
                     null),

    /**
     * A system that manages the asynchronous exchange of messages (events) from once to potentially many recipients.  Typically, this exchange of events is organized into groups called topics.
     */
    EVENT_BROKER("6b5f1764-c6ee-4cb6-b279-13502c4ad22b",
                 "Event Broker",
                 DeployedImplementationType.SOFTWARE_CAPABILITY,
                 OpenMetadataType.EVENT_BROKER.typeName,
                 null,
                 "A system that manages the asynchronous exchange of messages (events) from once to potentially many recipients.  Typically, this exchange of events is organized into groups called topics.",
                 OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),


    /**
     * A system that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).
     */
    RELATIONAL_DATABASE_MANAGER("dd43d7d7-7c32-42e8-8112-67fa5d5ed15f",
                                "Relational database manager (RDBMS)",
                                DeployedImplementationType.SOFTWARE_CAPABILITY,
                                OpenMetadataType.DATABASE_MANAGER.typeName,
                                null,
                                "A capability that manages collections of data called relational databases which in turn are organized into a tabular format and accessed via the Structured Query Language (SQL).",
                                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES),

    /**
     * A deployable software component.
     */
    SOFTWARE_COMPONENT("29b4afae-7000-4837-a225-d37bc036d503",
                       "Software Component",
                       DeployedImplementationType.PROCESS,
                       OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
                       null,
                       "A deployable software component.",
                       OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS),

    /**
     * Python language library supporting calls to Egeria's REST APIs.
     */
    PYEGERIA("3a3cba55-4a6d-45a4-becf-32e93041e6b0",
             "pyegeria",
             DeployedImplementationType.SOFTWARE_CAPABILITY,
             OpenMetadataType.SOFTWARE_LIBRARY.typeName,
             null,
             "Python language library supporting calls to Egeria's REST APIs.",
             "https://egeria-project.org/concepts/pyegeria/",
             "c3fd85ae-4226-4d20-b57f-af3b0e748e5f",
             "PYEGERIA",
             SolutionComponentType.SOFTWARE_LIBRARY.getSolutionComponentType(),
             null),



    /**
     * A pluggable software component that conforms to the Open Connector Framework (OCF).
     */
    OCF_CONNECTOR("df24f7a6-57e6-4991-ae7a-1dfbdf48fd55",
                  "Open Connector Framework (OCF) Connector",
                  DeployedImplementationType.SOFTWARE_COMPONENT,
                  OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                  null,
                  "A pluggable software component that conforms to the Open Connector Framework (OCF).",
                  "https://egeria-project.org/concepts/connector/"),

    /**
     * A pluggable software component that conforms to the Open Connector Framework (OCF).
     */
    RESOURCE_CONNECTOR("2dd4b390-94d0-49d6-80d9-13dc06a7cf7b",
                       "Resource Connector",
                       DeployedImplementationType.OCF_CONNECTOR,
                       OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                       null,
                       "A pluggable software component that conforms to the Open Connector Framework (OCF) and provides access to a digital resource.",
                       "https://egeria-project.org/concepts/digital-resource/"),

    /**
     * OMRS Repository Connector - Maps open metadata calls to a metadata repository.
     */
    REPOSITORY_CONNECTOR("a6e46bec-a80a-417e-931f-f6d044ff0f33",
                         "OMRS Repository Connector",
                         DeployedImplementationType.OCF_CONNECTOR,
                         OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                         null,
                         "Maps open metadata repository calls defined by the Open Metadata Repository Services (OMRS) to a metadata repository API and event notifications.",
                         "https://egeria-project.org/concepts/repository-connector/"),

    /**
     * OMRS Event Mapper Repository Connector - Maps events from a metadata repository to open metadata events defined by the Open Metadata Repository Services (OMRS)..
     */
    EVENT_MAPPER_REPOSITORY_CONNECTOR("7b80ffc7-0bc9-4430-8f0d-a1a334c37789",
                         "OMRS Event Mapper Repository Connector",
                         DeployedImplementationType.OCF_CONNECTOR,
                         OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                         null,
                         "Maps events from a metadata repository to open metadata events defined by the Open Metadata Repository Services (OMRS).",
                         "https://egeria-project.org/concepts/event-mapper-connector/"),

    /**
     * Open Metadata Archive Store Connector - Reads and writes open metadata types and instances to an open metadata archive.
     */
    OPEN_METADATA_ARCHIVE_CONNECTOR("b0dcf516-4440-4cd3-980b-7cc5eccb9d43",
                                    "Open Metadata Archive Store Connector",
                                    DeployedImplementationType.OCF_CONNECTOR,
                                    OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                    null,
                                    "Reads and writes open metadata types and instances to an open metadata archive.",
                                    "https://egeria-project.org/concepts/open-metadata-archive-store-connector/"),

    /**
     * Open Metadata Topic Connector - Reads and writes open metadata events to a topic.
     */
    OPEN_METADATA_TOPIC_CONNECTOR("82fb8f61-bfc1-442e-8f88-863f09b205c3",
                                    "Open Metadata Topic Connector",
                                    DeployedImplementationType.OCF_CONNECTOR,
                                    OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                    null,
                                    "Reads and writes open metadata events to a topic.",
                                    "https://egeria-project.org/concepts/open-metadata-topic-connector/"),


    /**
     * Writes audit log records to a destination.
     */
    CONFIG_DOCUMENT_STORE_CONNECTOR("c38551d1-e81c-4420-b7db-cfe4401e2289",
                                    "Configuration Document Store Connector",
                                    DeployedImplementationType.OCF_CONNECTOR,
                                    OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                    null,
                                    "Reads and writes OMAG Server Configuration Documents to a store.",
                                    "https://egeria-project.org/concepts/configuration-document-store-connector/"),

    /**
     * Platform Metadata Security Connector - A connector that manages authorization requests to the OMAG Server Platform.
     */
    PLATFORM_SECURITY_CONNECTOR("3e0bced9-b539-4b90-a7d7-2d2034e92af1",
                                "Platform Metadata Security Connector",
                                DeployedImplementationType.OCF_CONNECTOR,
                                OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                null,
                                "A connector that manages authorization requests to the OMAG Server Platform.",
                                "https://egeria-project.org/concepts/platform-metadata-security-connector/"),

    /**
     * Server Metadata Security Connector - A connector that manages authorization requests to the OMAG Server.
     */
    SERVER_SECURITY_CONNECTOR("5aa6a746-9dd4-4cbb-b1aa-e05707a156ce",
                              "Server Metadata Security Connector",
                              DeployedImplementationType.OCF_CONNECTOR,
                              OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                              null,
                              "A connector that manages authorization requests to the OMAG Server.",
                              "https://egeria-project.org/concepts/server-metadata-security-connector/"),


    /**
     * Secrets Store Connector - A connector that manages collections of secrets.
     */
    SECRETS_STORE_CONNECTOR("aaa7a6ca-bfbd-4d3d-86a2-e5436c675b14",
                              "Secrets Store Connector",
                              DeployedImplementationType.OCF_CONNECTOR,
                              OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                              null,
                              "A connector that manages collections of secrets.",
                              "https://egeria-project.org/concepts/secrets-store-connector/"),


    /**
     * Cohort Registry Store - Stores information about the repositories registered in the open metadata repository cohort.
     */
    COHORT_REGISTRY_STORE_CONNECTOR("4fd08eac-b864-4baa-a518-b31eb6d6ca4a",
                                    "Cohort Registry Store",
                                    DeployedImplementationType.OCF_CONNECTOR,
                                    OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                    null,
                                    "Stores information about the repositories registered in the open metadata repository cohort.",
                                    "https://egeria-project.org/concepts/cohort-registry-store-connector/"),

    /**
     * Audit Log Destination - Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.
     */
    AUDIT_LOG_DESTINATION_CONNECTOR("8a1ab260-6acd-4549-b8c3-57243eba9f8e",
                                    "Audit Log Destination",
                                    DeployedImplementationType.OCF_CONNECTOR,
                                    OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                    null,
                                    "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
                                    "https://egeria-project.org/concepts/audit-log-destination-connector/",
                                    "c11370f9-5f84-408a-911a-b2e13696b1b1",
                                    SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                    "AUDIT_LOG_DESTINATION",
                                    null),

    /**
     * Connector that manages metadata exchange with a third party technology.
     */
    INTEGRATION_CONNECTOR("3870ee0a-d4a3-419f-a6d0-1440c60e5c00",
                          "Integration Connector",
                          DeployedImplementationType.OCF_CONNECTOR,
                          OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                          null,
                          "Connector that manages metadata exchange with a third party technology.",
                          "https://egeria-project.org/concepts/integration-connector",
                          "9728be11-43e8-43a8-958f-e67a0d1f2f40",
                          SolutionComponentType.LONG_RUNNING_DAEMON.getSolutionComponentType(),
                          "INTEGRATION-CONNECTOR",
                          null),

    /**
     * Provides the description of a component that implements an automated governance activity.
     */
    GOVERNANCE_SERVICE("f5e38a2f-911d-4718-9039-0eaa25f5b9ee",
                       "Governance Service",
                       DeployedImplementationType.OCF_CONNECTOR,
                       OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                       null,
                       "Provides the description of a component that implements an automated governance activity.",
                       "https://egeria-project.org/concepts/governance-service/",
                       "0002857a-0223-46e9-ac26-56109a3d6833",
                       SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                       "GOVERNANCE-SERVICE",
                       null),

    /**
     * Manages the execution of automated governance activity requested via engine actions.
     */
    GOVERNANCE_ENGINE("4612ab00-dc47-48c6-94eb-552b13a70991",
                      "Governance Engine",
                      DeployedImplementationType.SOFTWARE_CAPABILITY,
                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                      null,
                      "Manages the execution of automated governance activity requested via engine actions.",
                      "https://egeria-project.org/concepts/governance-engine/",
                      "e0b790f8-d7ae-4a08-b403-7351da75a913",
                      SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                      "GOVERNANCE-ENGINE",
                      new SolutionComponentDefinition[]{GOVERNANCE_SERVICE.getSolutionComponent()}),

    /**
     * Governance Action Service - A connector that coordinates governance of digital resources and metadata.
     */
    GOVERNANCE_ACTION_SERVICE_CONNECTOR("d77773c9-cc7c-4571-8472-f2764aa1471a",
                                        "Governance Action Service",
                                        DeployedImplementationType.GOVERNANCE_SERVICE,
                                        OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeName,
                                        null,
                                        "A connector that coordinates governance of digital resources and metadata.",
                                        "https://egeria-project.org/concepts/governance-action-service/",
                                        "6e3d2be9-7369-4883-b443-99fe29d8f157",
                                        SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                                        "GOVERNANCE-ACTION-SERVICE",
                                        null),

    /**
     * Governance Action Engine - A governance engine that runs governance action services.
     */
    GOVERNANCE_ACTION_ENGINE("6195bb4b-8fce-43f8-a92a-8ec12bf4a5b9",
                             "Governance Action Engine",
                             DeployedImplementationType.GOVERNANCE_ENGINE,
                             OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                             null,
                             "A governance engine that runs governance action services.",
                             "https://egeria-project.org/concepts/governance-action-engine/",
                             "913f0e12-67e0-4b07-b085-a3aac1748f07",
                             SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                             "GOVERNANCE-ACTION-ENGINE",
                             new SolutionComponentDefinition[]{GOVERNANCE_ACTION_SERVICE_CONNECTOR.getSolutionComponent()}),


    /**
     * Survey Action Service - A connector that coordinates asset surveys.
     */
    SURVEY_ACTION_SERVICE_CONNECTOR("f3bb3f15-6d09-451d-804c-23ae00529d06",
                                    "Survey Action Service",
                                    DeployedImplementationType.GOVERNANCE_SERVICE,
                                    OpenMetadataType.SURVEY_ACTION_SERVICE.typeName,
                                    null,
                                    "A connector that coordinates asset surveys.",
                                    "https://egeria-project.org/concepts/survey-action-service/",
                                    "12af2794-26cb-4294-98e9-d326f9570b28",
                                    SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                                    "SURVEY-ACTION-SERVICE",
                                    null),

    /**
     * Watchdog Action Service - A connector that coordinates notifications based on situations/events.
     */
    WATCHDOG_ACTION_SERVICE_CONNECTOR("d4af2e0f-78db-4b93-bf5d-53632b33c67a",
                                      "Watchdog Action Service",
                                      DeployedImplementationType.GOVERNANCE_SERVICE,
                                      OpenMetadataType.WATCHDOG_ACTION_SERVICE.typeName,
                                      null,
                                      "A connector that coordinates notifications based on situations/events.",
                                      "https://egeria-project.org/concepts/watchdog-action-service/",
                                      "4733c135-0651-475c-a9ae-fa7b5ddf0823",
                                      SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                                      "WATCHDOG-ACTION-SERVICE",
                                      null),

    /**
     * Survey Action Engine - A governance engine that runs survey action services.
     */
    SURVEY_ACTION_ENGINE("fe4bcc14-985b-421d-b98e-3fddbc884811",
                         "Survey Action Engine",
                         DeployedImplementationType.GOVERNANCE_ENGINE,
                         OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                         null,
                         "A governance engine that runs survey action services.",
                         "https://egeria-project.org/concepts/survey-action-engine/",
                         "b8a2156f-38cb-40b5-be48-0fc8f0c1254d",
                         SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                         "SURVEY-ACTION-ENGINE",
                         new SolutionComponentDefinition[]{SURVEY_ACTION_SERVICE_CONNECTOR.getSolutionComponent()}),

    /**
     * Watchdog Action Engine - A governance engine that runs survey action services.
     */
    WATCHDOG_ACTION_ENGINE("ac264c61-8b57-4afd-a505-d0a2c1fd54cd",
                           "Watchdog Action Engine",
                           DeployedImplementationType.GOVERNANCE_ENGINE,
                           OpenMetadataType.WATCHDOG_ACTION_ENGINE.typeName,
                           null,
                           "A governance engine that runs watchdog action services.",
                           "https://egeria-project.org/concepts/watchdog-action-engine/",
                           "0f6dc658-f299-4066-bd77-1886f7167ec1",
                           SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                           "WATCHDOG-ACTION-ENGINE",
                           new SolutionComponentDefinition[]{WATCHDOG_ACTION_SERVICE_CONNECTOR.getSolutionComponent()}),

    /**
     * Repository Governance Service - A connector that dynamically governs the activity of the open metadata repositories.
     */
    REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR("958fe597-6db9-4c0f-bf3e-54331934b792",
                                            "Repository Governance Service Connector",
                                            DeployedImplementationType.GOVERNANCE_SERVICE,
                                            OpenMetadataType.REPOSITORY_GOVERNANCE_SERVICE.typeName,
                                            null,
                                            "A connector that dynamically governs the activity of the open metadata repositories.",
                                            "https://egeria-project.org/concepts/repository-governance-service",
                                            "da68a506-153b-42a5-b028-fb5ad440c221",
                                            SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                                            "REPOSITORY-GOVERNANCE-SERVICE",
                                            null),

    /**
     * A governance engine that runs repository governance services.
     */
    REPOSITORY_GOVERNANCE_ENGINE("9a754469-05a1-4d4c-b3b5-6b3ee4207ac7",
                                 "Repository Governance Engine",
                                 DeployedImplementationType.GOVERNANCE_ENGINE,
                                 OpenMetadataType.REPOSITORY_GOVERNANCE_ENGINE.typeName,
                                 null,
                                 "A governance engine that runs repository governance services.",
                                 "https://egeria-project.org/concepts/repository-governance-engine/",
                                 "2f4ddd24-0bed-4d3c-b444-45ca0d442716",
                                 SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                                 "REPOSITORY-GOVERNANCE-ENGINE",
                                 new SolutionComponentDefinition[]{REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getSolutionComponent()}),

    /**
     * Governance Action - A description of an action that supports the implementation of a governance program.
     */
    GOVERNANCE_ACTION("f86de45a-6482-4487-ab69-0e1005de8b1a",
                      "Governance Action",
                      null,
                      OpenMetadataType.GOVERNANCE_ACTION.typeName,
                      null,
                      "A description of an action that supports the implementation of a governance program.",
                      "https://egeria-project.org/concepts/governance-action/"),

    /**
     * Governance Action Process - A modelled workflow process to perform a governance action.
     */
    GOVERNANCE_ACTION_PROCESS("0d970e05-6310-49dd-863f-c73401211140",
                              "Governance Action Process",
                              DeployedImplementationType.GOVERNANCE_ACTION,
                              OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                              null,
                              "A modelled workflow process to perform a governance action.",
                              "https://egeria-project.org/concepts/governance-action-process/"),

    /**
     * Governance Action Type - A modelled governance action with a single step.
     */
    GOVERNANCE_ACTION_TYPE("eda3449b-d928-49aa-a67c-bc934e2a2102",
                           "Governance Action Type",
                           DeployedImplementationType.GOVERNANCE_ACTION,
                           OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                           null,
                           "A modelled governance action with a single step.",
                           "https://egeria-project.org/concepts/governance-action-type/"),

    /**
     * Provides the list of integration connectors that should run in an Integration Daemon.  The Integration Daemon is configured with the qualified names of the integration group(s) that provide its connector list.
     */
    INTEGRATION_GROUP("7a49ed4e-6810-4520-836d-aebc8ee341d7",
                      "Dynamic Integration Group",
                      DeployedImplementationType.SOFTWARE_CAPABILITY,
                      OpenMetadataType.INTEGRATION_GROUP.typeName,
                      null,
                      "Provides the list of integration connectors that should run in an Integration Daemon.  The Integration Daemon is configured with the qualified names of the integration group(s) that provide its connector list.",
                      "https://egeria-project.org/concepts/integration-group/"),


    /**
     * An event-based data source
     */
    DATA_FEED("8ff94698-32b6-4765-bcc6-b1357bc1d55b",
              "Data Feed",
              DeployedImplementationType.DATA_ASSET,
              OpenMetadataType.DATA_FEED.typeName,
              null,
              "An event-based data source",
              "https://egeria-project.org/types/2/0210-Data-Stores/"),

    /**
     * An organized event-based data source that supports subscription to a named data feed.
     */
    TOPIC("e3e25371-65da-47a8-bb73-feb7a00b2afe",
          "Data Feed",
          DeployedImplementationType.DATA_FEED,
          OpenMetadataType.TOPIC.typeName,
          null,
          "An organized event-based data source that supports subscription to a named data feed.",
          "https://egeria-project.org/types/2/0210-Data-Stores/"),

    /**
     * An event topic supporting high-speed, reliable event exchange.
     */
    APACHE_KAFKA_TOPIC("20f81045-aa24-4374-92cb-d045ac3e24f6",
                       "Apache Kafka Topic",
                       DeployedImplementationType.DATA_FEED,
                       OpenMetadataType.TOPIC.typeName,
                       null,
                       "An event topic supporting high-speed, reliable event exchange.",
                       "https://kafka.apache.org/",
                       "2c6290c6-c9b4-454d-a79c-17b2c9fdeb33",
                       SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                       "APACHE-KAFKA-TOPIC",
                       null),
    ;


    /**
     * Return the matching ENUM definition.
     *
     * @param deployedImplementationType value to match on
     * @return DeployedImplementationType definition
     */
    public static DeployedImplementationType getDefinitionFromDeployedImplementationType(String deployedImplementationType)
    {
        if (deployedImplementationType != null)
        {
            for (DeployedImplementationType definition : DeployedImplementationType.values())
            {
                if (definition.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    return definition;
                }
            }
        }

        return null;
    }


    private final String                               guid;
    private final String                               deployedImplementationType;
    private final DeployedImplementationTypeDefinition isATypeOf;
    private final String                               associatedTypeName;
    private final String                               associatedClassification;
    private final String                               description;
    private final String                               wikiLink;
    private final String                               solutionComponentGUID;
    private final String                               solutionComponentType;
    private final String                               solutionComponentIdentifier;
    private final SolutionComponentDefinition[]        subComponents;

    /**
     * Constructor for individual enum value.
     *
     * @param guid                        unique identifier of technology type (deployedImplementationType)
     * @param deployedImplementationType  value for deployedImplementationType
     * @param isATypeOf                   optional deployed implementation type that this type "inherits" from
     * @param associatedTypeName          the open metadata type where this value is used
     * @param associatedClassification    the open metadata classification where this value is used
     * @param description                 description of the type
     * @param wikiLink                    url link to more information (optional)
     * @param solutionComponentGUID       unique identifier of the solution component that this deployed implementation type is associated with (optional)
     * @param solutionComponentType       type of the solution component that this deployed implementation type is associated with (optional)
     * @param solutionComponentIdentifier identifier of the solution component that this deployed implementation type is associated with (optional)
     * @param subComponents               optional subcomponents of the solution
     */
    DeployedImplementationType(String                               guid,
                               String                               deployedImplementationType,
                               DeployedImplementationTypeDefinition isATypeOf,
                               String                               associatedTypeName,
                               String                               associatedClassification,
                               String                               description,
                               String                               wikiLink,
                               String                               solutionComponentGUID,
                               String                               solutionComponentType,
                               String                               solutionComponentIdentifier,
                               SolutionComponentDefinition[]        subComponents)
    {
        this.guid                        = guid;
        this.deployedImplementationType  = deployedImplementationType;
        this.isATypeOf                   = isATypeOf;
        this.associatedTypeName          = associatedTypeName;
        this.associatedClassification    = associatedClassification;
        this.description                 = description;
        this.wikiLink                    = wikiLink;
        this.solutionComponentGUID       = solutionComponentGUID;
        this.solutionComponentType       = solutionComponentType;
        this.solutionComponentIdentifier = solutionComponentIdentifier;
        this.subComponents               = subComponents;
    }


    /**
     * Constructor for individual enum value.
     *
     * @param guid                       unique identifier of technology type (deployedImplementationType)
     * @param deployedImplementationType value for deployedImplementationType
     * @param isATypeOf                  optional deployed implementation type that this type "inherits" from
     * @param associatedTypeName         the open metadata type where this value is used
     * @param associatedClassification   the open metadata classification where this value is used
     * @param description                description of the type
     * @param wikiLink                   url link to more information (optional)
     */
    DeployedImplementationType(String guid,
                               String deployedImplementationType,
                               DeployedImplementationTypeDefinition isATypeOf,
                               String associatedTypeName,
                               String associatedClassification,
                               String description,
                               String wikiLink)
    {
        this.guid                        = guid;
        this.deployedImplementationType  = deployedImplementationType;
        this.isATypeOf                   = isATypeOf;
        this.associatedTypeName          = associatedTypeName;
        this.associatedClassification    = associatedClassification;
        this.description                 = description;
        this.wikiLink                    = wikiLink;
        this.solutionComponentGUID       = null;
        this.solutionComponentType       = null;
        this.solutionComponentIdentifier = null;
        this.subComponents               = null;
    }


    /**
     * Return the guid for the deployed technology type - can be null.
     *
     * @return string
     */
    @Override
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the preferred value for the deployed implementation type.
     *
     * @return string
     */
    @Override
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the optional deployed implementation type that this technology inherits from.
     *
     * @return deployed implementation type enum
     */
    @Override
    public DeployedImplementationTypeDefinition getIsATypeOf()
    {
        return isATypeOf;
    }


    /**
     * Return the type name that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getAssociatedTypeName()
    {
        return associatedTypeName;
    }


    /**
     * Return the optional classification name that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getAssociatedClassification()
    {
        return associatedClassification;
    }


    /**
     * Return the description for this value.
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL for more information.
     *
     * @return string url
     */
    @Override
    public String getWikiLink()
    {
        return wikiLink;
    }


    /**
     * Return the optional unique identifier of the solution component that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentGUID()
    {
        return solutionComponentGUID;
    }


    /**
     * Return the solution component type that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentType()
    {
        return solutionComponentType;
    }


    /**
     * Return the solution component identifier that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentIdentifier()
    {
        return solutionComponentIdentifier;
    }


    /**
     * Return the optional list of subcomponents.
     *
     * @return null or list
     */
    @Override
    public  List<SolutionComponentDefinition> getSubComponents()
    {
        return subComponents == null ? null : Arrays.asList(subComponents);
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "DeployedImplementationType{" + deployedImplementationType + '}';
    }
}
