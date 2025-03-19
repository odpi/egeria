/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;


import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A description of the default software server templates.
 * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
 * to the supplied connector type and an endpoint,
 */
public enum DataAssetTemplateDefinition implements TemplateDefinition
{
    POSTGRES_DATABASE_TEMPLATE(PostgreSQLTemplateType.POSTGRES_DATABASE_TEMPLATE.getTemplateGUID(),
                               PostgresDeployedImplementationType.POSTGRESQL_DATABASE,
                               PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                               PostgresPlaceholderProperty.DATABASE_DESCRIPTION.getPlaceholder(),
                               PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType() + ":" + PlaceholderProperty.SERVER_NAME.getPlaceholder() + ":" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                               null,
                               null,
                               null,
                               new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                               "jdbc:postgresql://" +
                                       PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                       PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                               null,
                               PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholder(),
                               SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName(),
                               new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                               PlaceholderProperty.SECRETS_STORE.getPlaceholder(),
                               null,
                               PostgresPlaceholderProperty.getPostgresDatabasePlaceholderPropertyTypes(),
                               ContentPackDefinition.POSTGRES_CONTENT_PACK),

    POSTGRES_SCHEMA_TEMPLATE(PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE.getTemplateGUID(),
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                             PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                             PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getPlaceholder(),
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType() + ":" + PlaceholderProperty.SERVER_NAME.getPlaceholder() + ":" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                             null,
                             null,
                             null,
                             new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                             "jdbc:postgresql://" +
                                     PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                     PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" +
                                     PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "?currentSchema=" +
                                     PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                             getPostgresSchemaConfigurationProperties(),
                             PlaceholderProperty.SECRETS_COLLECTION_NAME.getPlaceholder(),
                             SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName(),
                             new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                             PlaceholderProperty.SECRETS_STORE.getPlaceholder(),
                             null,
                             PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes(),
                             ContentPackDefinition.POSTGRES_CONTENT_PACK),

    KAFKA_TOPIC_TEMPLATE(KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateGUID(),
                         DeployedImplementationType.APACHE_KAFKA_TOPIC,
                         KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         DeployedImplementationType.APACHE_KAFKA_TOPIC.getDeployedImplementationType() + ":" + PlaceholderProperty.SERVER_NAME.getPlaceholder() + "." + KafkaPlaceholderProperty.FULL_TOPIC_NAME.getPlaceholder() + ":inOut",
                         null,
                         null,
                         null,
                         new KafkaOpenMetadataTopicProvider().getConnectorType().getGUID(),
                         KafkaPlaceholderProperty.FULL_TOPIC_NAME.getPlaceholder(),
                         getKafkaConfigurationProperties(),
                         null,
                         null,
                         null,
                         null,
                         null,
                         KafkaPlaceholderProperty.getKafkaTopicPlaceholderPropertyTypes(),
                         ContentPackDefinition.CORE_CONTENT_PACK),

    FILE_FOLDER_TEMPLATE("fbdd8efd-1b69-474c-bb6d-0a304b394146",
                         DeployedImplementationType.FILE_FOLDER,
                         PlaceholderProperty.DIRECTORY_NAME.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder(),
                         getFileFolderExtendedProperties(DeployedImplementationType.FILE_FOLDER),
                         null,
                         null,
                         new BasicFolderProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder(),
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         PlaceholderProperty.getFolderPlaceholderPropertyTypes(),
                         ContentPackDefinition.CORE_CONTENT_PACK),

    DATA_FOLDER_TEMPLATE("372a0379-7060-4c9d-8d84-bc709b31794c",
                         DeployedImplementationType.DATA_FOLDER,
                         PlaceholderProperty.DIRECTORY_NAME.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder(),
                         getFileFolderExtendedProperties(DeployedImplementationType.DATA_FOLDER),
                         null,
                         null,
                         new DataFolderProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder(),
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         PlaceholderProperty.getFolderPlaceholderPropertyTypes(),
                         ContentPackDefinition.CORE_CONTENT_PACK),

    FILE_TEMPLATE("ae3067c7-cc72-4a18-88e1-746803c2c86f",
                  DeployedImplementationType.FILE,
                  PlaceholderProperty.FILE_NAME.getPlaceholder(),
                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                  DeployedImplementationType.FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                  getDataFileExtendedProperties(DeployedImplementationType.FILE),
                  PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                  null,
                  new BasicFileStoreProvider().getConnectorType().getGUID(),
                  PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                  null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                  ContentPackDefinition.CORE_CONTENT_PACK),

    DATA_FILE_TEMPLATE("66d8dda9-00cf-4e59-938c-4b0583596b1e",
                       DeployedImplementationType.DATA_FILE,
                       PlaceholderProperty.FILE_NAME.getPlaceholder(),
                       PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                       DeployedImplementationType.DATA_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                       getDataFileExtendedProperties(DeployedImplementationType.DATA_FILE),
                       PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                       null,
                       new BasicFileStoreProvider().getConnectorType().getGUID(),
                       PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                       null,
                       null,
                       null,
                       null,
                       null,
                       null,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       ContentPackDefinition.CORE_CONTENT_PACK),

    CSV_FILE_TEMPLATE("13770f93-13c8-42be-9bb8-e0b1b1e52b1f",
                      DeployedImplementationType.CSV_FILE,
                      PlaceholderProperty.FILE_NAME.getPlaceholder(),
                      PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                      DeployedImplementationType.CSV_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                      getDataFileExtendedProperties(DeployedImplementationType.CSV_FILE),
                      PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                      null,
                      new CSVFileStoreProvider().getConnectorType().getGUID(),
                      PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                      ContentPackDefinition.CORE_CONTENT_PACK),

    AVRO_FILE_TEMPLATE("9f5be428-058e-41dd-b506-3a222283b579",
                       DeployedImplementationType.AVRO_FILE,
                       PlaceholderProperty.FILE_NAME.getPlaceholder(),
                       PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                       DeployedImplementationType.AVRO_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                       getDataFileExtendedProperties(DeployedImplementationType.AVRO_FILE),
                       PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                       null,
                       new BasicFileStoreProvider().getConnectorType().getGUID(),
                       PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                       null,
                       null,
                       null,
                       null,
                       null,
                       null,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       ContentPackDefinition.CORE_CONTENT_PACK),

    JSON_FILE_TEMPLATE("c4836635-7e9e-446a-83b5-15e206b1aff3",
                       DeployedImplementationType.JSON_FILE,
                       PlaceholderProperty.FILE_NAME.getPlaceholder(),
                       PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                       DeployedImplementationType.JSON_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                       getDataFileExtendedProperties(DeployedImplementationType.JSON_FILE),
                       PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                       null,
                       new BasicFileStoreProvider().getConnectorType().getGUID(),
                       PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                       null,
                       null,
                       null,
                       null,
                       null,
                       null,
                       PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                       ContentPackDefinition.CORE_CONTENT_PACK),

    PARQUET_FILE_TEMPLATE("7f6cd744-79c3-4d25-a056-eeb1a91574c3",
                          DeployedImplementationType.PARQUET_FILE,
                          PlaceholderProperty.FILE_NAME.getPlaceholder(),
                          PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                          DeployedImplementationType.PARQUET_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                          getDataFileExtendedProperties(DeployedImplementationType.PARQUET_FILE),
                          PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                          null,
                          new BasicFileStoreProvider().getConnectorType().getGUID(),
                          PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                          null,
                          null,
                          null,
                          null,
                          null,
                          null,
                          PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                          ContentPackDefinition.CORE_CONTENT_PACK),

    SPREADSHEET_FILE_TEMPLATE("e4fabff5-2ba9-4050-9076-6ed917970b4c",
                              DeployedImplementationType.SPREADSHEET_FILE,
                              PlaceholderProperty.FILE_NAME.getPlaceholder(),
                              PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                              DeployedImplementationType.SPREADSHEET_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              getDataFileExtendedProperties(DeployedImplementationType.SPREADSHEET_FILE),
                              PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                              null,
                              new BasicFileStoreProvider().getConnectorType().getGUID(),
                              PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                              ContentPackDefinition.CORE_CONTENT_PACK),

    XML_FILE_TEMPLATE("ea67ae71-c674-473e-b38b-689879d2a7d9",
                      DeployedImplementationType.XML_FILE,
                      PlaceholderProperty.FILE_NAME.getPlaceholder(),
                      PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                      DeployedImplementationType.XML_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                      getDataFileExtendedProperties(DeployedImplementationType.XML_FILE),
                      PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                      null,
                      new BasicFileStoreProvider().getConnectorType().getGUID(),
                      PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                      ContentPackDefinition.CORE_CONTENT_PACK),

    DOCUMENT_TEMPLATE("eb6f728d-fa54-4350-9807-1199cbf96851",
                      DeployedImplementationType.DOCUMENT,
                      PlaceholderProperty.FILE_NAME.getPlaceholder(),
                      PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                      DeployedImplementationType.DOCUMENT.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                      getDataFileExtendedProperties(DeployedImplementationType.DOCUMENT),
                      PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                      null,
                      new BasicFileStoreProvider().getConnectorType().getGUID(),
                      PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                      null,
                      null,
                      null,
                      null,
                      null,
                      null,
                      PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                      ContentPackDefinition.CORE_CONTENT_PACK),

    AUDIO_DATA_FILE_TEMPLATE("39b4b670-7f15-4744-a5ba-62e8edafbcee",
                             DeployedImplementationType.AUDIO_DATA_FILE,
                             PlaceholderProperty.FILE_NAME.getPlaceholder(),
                             PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                             DeployedImplementationType.AUDIO_DATA_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             getDataFileExtendedProperties(DeployedImplementationType.AUDIO_DATA_FILE),
                             PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                             null,
                             new BasicFileStoreProvider().getConnectorType().getGUID(),
                             PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                             ContentPackDefinition.CORE_CONTENT_PACK),

    VIDEO_DATA_FILE_TEMPLATE("93b2b722-ec0f-4da4-960a-b8d4922f8bf5",
                             DeployedImplementationType.VIDEO_DATA_FILE,
                             PlaceholderProperty.FILE_NAME.getPlaceholder(),
                             PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                             DeployedImplementationType.VIDEO_DATA_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             getDataFileExtendedProperties(DeployedImplementationType.VIDEO_DATA_FILE),
                             PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                             null,
                             new BasicFileStoreProvider().getConnectorType().getGUID(),
                             PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                             ContentPackDefinition.CORE_CONTENT_PACK),

    THREE_D_IMAGE_DATA_FILE_TEMPLATE("0059ea2b-6292-4cac-aa6f-a80a605f1114",
                                     DeployedImplementationType.THREE_D_IMAGE_DATA_FILE,
                                     PlaceholderProperty.FILE_NAME.getPlaceholder(),
                                     PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                     DeployedImplementationType.THREE_D_IMAGE_DATA_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                                     getDataFileExtendedProperties(DeployedImplementationType.THREE_D_IMAGE_DATA_FILE),
                                     PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                                     null,
                                     new BasicFileStoreProvider().getConnectorType().getGUID(),
                                     PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                                     null,
                                     null,
                                     null,
                                     null,
                                     null,
                                     null,
                                     PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                                     ContentPackDefinition.CORE_CONTENT_PACK),

    RASTER_DATA_FILE_TEMPLATE("47211156-f03f-4881-8526-015e695a3dac",
                              DeployedImplementationType.RASTER_DATA_FILE,
                              PlaceholderProperty.FILE_NAME.getPlaceholder(),
                              PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                              DeployedImplementationType.RASTER_DATA_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              getDataFileExtendedProperties(DeployedImplementationType.RASTER_DATA_FILE),
                              PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                              null,
                              new BasicFileStoreProvider().getConnectorType().getGUID(),
                              PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                              ContentPackDefinition.CORE_CONTENT_PACK),

    VECTOR_DATA_FILE_TEMPLATE("db1bec7f-55a9-40d3-91c0-a57b76d422e2",
                              DeployedImplementationType.VECTOR_DATA_FILE,
                              PlaceholderProperty.FILE_NAME.getPlaceholder(),
                              PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                              DeployedImplementationType.VECTOR_DATA_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              getDataFileExtendedProperties(DeployedImplementationType.VECTOR_DATA_FILE),
                              PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                              null,
                              new BasicFileStoreProvider().getConnectorType().getGUID(),
                              PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                              ContentPackDefinition.CORE_CONTENT_PACK),

    ARCHIVE_FILE_TEMPLATE("7578e504-d00f-406d-a194-3fc0a351cdf9",
                          DeployedImplementationType.ARCHIVE_FILE,
                          PlaceholderProperty.FILE_NAME.getPlaceholder(),
                          PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                          DeployedImplementationType.ARCHIVE_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                          getDataFileExtendedProperties(DeployedImplementationType.ARCHIVE_FILE),
                          PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                          null,
                          new BasicFileStoreProvider().getConnectorType().getGUID(),
                          PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                          null,
                          null,
                          null,
                          null,
                          null,
                          null,
                          PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                          ContentPackDefinition.CORE_CONTENT_PACK),

    KEYSTORE_FILE_TEMPLATE("fbcfcc0c-1652-421f-b49b-c3e1c108768f",
                           DeployedImplementationType.KEYSTORE_FILE,
                           PlaceholderProperty.FILE_NAME.getPlaceholder(),
                           PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                           DeployedImplementationType.KEYSTORE_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                           getDataFileExtendedProperties(DeployedImplementationType.KEYSTORE_FILE),
                           PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                           null,
                           new BasicFileStoreProvider().getConnectorType().getGUID(),
                           PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                           null,
                           null,
                           null,
                           null,
                           null,
                           null,
                           PlaceholderProperty.getDataFilesPlaceholderPropertyTypes(),
                           ContentPackDefinition.CORE_CONTENT_PACK),

    PROGRAM_FILE_TEMPLATE("32d27e9c-1fdf-455a-ad2a-42b4d7d99108",
                          DeployedImplementationType.PROGRAM_FILE,
                          PlaceholderProperty.FILE_NAME.getPlaceholder(),
                          PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                          DeployedImplementationType.PROGRAM_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                          getDataFileExtendedProperties(DeployedImplementationType.PROGRAM_FILE),
                          PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                          PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                          new BasicFileStoreProvider().getConnectorType().getGUID(),
                          PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                          null,
                          null,
                          null,
                          null,
                          null,
                          null,
                          PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                          ContentPackDefinition.CORE_CONTENT_PACK),

    SOURCE_CODE_FILE_TEMPLATE("9c7013ef-f29b-4b01-a8ea-5ea14f64c67a",
                              DeployedImplementationType.SOURCE_CODE_FILE,
                              PlaceholderProperty.FILE_NAME.getPlaceholder(),
                              PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                              DeployedImplementationType.SOURCE_CODE_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              getDataFileExtendedProperties(DeployedImplementationType.SOURCE_CODE_FILE),
                              PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                              PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                              new BasicFileStoreProvider().getConnectorType().getGUID(),
                              PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                              null,
                              null,
                              null,
                              null,
                              null,
                              null,
                              PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                              ContentPackDefinition.CORE_CONTENT_PACK),

    BUILD_FILE_TEMPLATE("fbb2fa2e-8bcb-402e-9be7-5c6db9f2c504",
                        DeployedImplementationType.BUILD_FILE,
                        PlaceholderProperty.FILE_NAME.getPlaceholder(),
                        PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                        DeployedImplementationType.BUILD_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                        getDataFileExtendedProperties(DeployedImplementationType.BUILD_FILE),
                        PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                        PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                        new BasicFileStoreProvider().getConnectorType().getGUID(),
                        PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                        ContentPackDefinition.CORE_CONTENT_PACK),

    EXECUTABLE_FILE_TEMPLATE("3d99a163-7a13-4576-a212-784010a8302a",
                             DeployedImplementationType.EXECUTABLE_FILE,
                             PlaceholderProperty.FILE_NAME.getPlaceholder(),
                             PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                             DeployedImplementationType.EXECUTABLE_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             getDataFileExtendedProperties(DeployedImplementationType.EXECUTABLE_FILE),
                             PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                             PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                             new BasicFileStoreProvider().getConnectorType().getGUID(),
                             PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                             ContentPackDefinition.CORE_CONTENT_PACK),

    SCRIPT_FILE_TEMPLATE("dbd5e6bb-1ff8-46f4-a007-fb0485f68c92",
                         DeployedImplementationType.SCRIPT_FILE,
                         PlaceholderProperty.FILE_NAME.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         DeployedImplementationType.SCRIPT_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                         getDataFileExtendedProperties(DeployedImplementationType.SCRIPT_FILE),
                         PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                         PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                         new BasicFileStoreProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                         ContentPackDefinition.CORE_CONTENT_PACK),

    PROPERTIES_FILE_TEMPLATE("3b281111-a0ef-4fc4-99e7-9a0ef84a7636",
                             DeployedImplementationType.PROPERTIES_FILE,
                             PlaceholderProperty.FILE_NAME.getPlaceholder(),
                             PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                             DeployedImplementationType.PROPERTIES_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             getDataFileExtendedProperties(DeployedImplementationType.PROPERTIES_FILE),
                             PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                             PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                             new BasicFileStoreProvider().getConnectorType().getGUID(),
                             PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                             ContentPackDefinition.CORE_CONTENT_PACK),

    YAML_FILE_TEMPLATE("2221855b-2b64-4b45-a2ee-c40adc5e2a64",
                             DeployedImplementationType.YAML_FILE,
                             PlaceholderProperty.FILE_NAME.getPlaceholder(),
                             PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                             DeployedImplementationType.YAML_FILE.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "." + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             getDataFileExtendedProperties(DeployedImplementationType.YAML_FILE),
                             PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                             PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                             new BasicFileStoreProvider().getConnectorType().getGUID(),
                             PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                             null,
                             null,
                             null,
                             null,
                             null,
                             null,
                             PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes(),
                             ContentPackDefinition.CORE_CONTENT_PACK),


    ;


    /**
     * Build the configuration properties for a kafka topic.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getKafkaConfigurationProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        Map<String, String> bootstrapServersProperties = new HashMap<>();

        bootstrapServersProperties.put("bootstrap.servers",
                                       PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                               PlaceholderProperty.PORT_NUMBER.getPlaceholder());

        configurationProperties.put(KafkaPlaceholderProperty.EVENT_DIRECTION.getName(), "inOut");
        configurationProperties.put("producer", bootstrapServersProperties);
        configurationProperties.put("consumer", bootstrapServersProperties);

        return configurationProperties;
    }


    /**
     * Build the configuration properties for a kafka topic.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getPostgresSchemaConfigurationProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(JDBCConfigurationProperty.DATABASE_NAME.getName(), PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder());
        configurationProperties.put(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(), PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder());

        return configurationProperties;
    }


    /**
     * Build the extended properties for a folder (directory).
     *
     * @return configuration properties
     */
    private static Map<String, Object> getFileFolderExtendedProperties(DeployedImplementationTypeDefinition deployedImplementationType)
    {
        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());

        return extendedProperties;
    }


    /**
     * Build the extended properties for a folder (directory).
     *
     * @return configuration properties
     */
    private static Map<String, Object> getDataFileExtendedProperties(DeployedImplementationTypeDefinition deployedImplementationType)
    {
        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, PlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, PlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());

        return extendedProperties;
    }


    private final String                               guid;
    private final DeployedImplementationTypeDefinition deployedImplementationType;
    private final String                               assetName;
    private final String                               assetDescription;
    private final String                               qualifiedName;
    private final Map<String, Object>                  extendedProperties;
    private final String                               encoding;
    private final String                               encodingLanguage;
    private final String                               connectorTypeGUID;
    private final String                               networkAddress;
    private final Map<String, Object>                  configurationProperties;
    private final String                               secretsCollectionName;
    private final String                               secretsStorePurpose;
    private final String                               secretsStoreConnectorTypeGUID;
    private final String                               secretsStoreFileName;
    private final List<ReplacementAttributeType>       replacementAttributeTypes;
    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;
    private final ContentPackDefinition                contentPackDefinition;


    /**
     * Construct the description of the template.
     *
     * @param guid fixed unique identifier
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param assetDescription description
     * @param qualifiedName optional server name
     * @param extendedProperties subtype properties for the asset
     * @param encoding           what encoding is needed?
     * @param encodingLanguage           language used to encode the contents of the file
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param secretsCollectionName name of collection of secrets to use in the secrets store
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     * @param configurationProperties  additional properties for the connection
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     * @param contentPackDefinition            which content pack does this server belong?
     */
    DataAssetTemplateDefinition(String                               guid,
                                DeployedImplementationTypeDefinition deployedImplementationType,
                                String                               assetName,
                                String                               assetDescription,
                                String                               qualifiedName,
                                Map<String, Object>                  extendedProperties,
                                String                               encoding,
                                String                               encodingLanguage,
                                String                               connectorTypeGUID,
                                String                               networkAddress,
                                Map<String, Object>                  configurationProperties,
                                String                               secretsCollectionName,
                                String                               secretsStorePurpose,
                                String                               secretsStoreConnectorTypeGUID,
                                String                               secretsStoreFileName,
                                List<ReplacementAttributeType>       replacementAttributeTypes,
                                List<PlaceholderPropertyType>        placeholderPropertyTypes,
                                ContentPackDefinition                contentPackDefinition)
    {
        this.guid                          = guid;
        this.deployedImplementationType    = deployedImplementationType;
        this.assetName                     = assetName;
        this.assetDescription              = assetDescription;
        this.qualifiedName                 = qualifiedName;
        this.extendedProperties            = extendedProperties;
        this.encoding                      = encoding;
        this.encodingLanguage              = encodingLanguage;
        this.connectorTypeGUID             = connectorTypeGUID;
        this.networkAddress                = networkAddress;
        this.configurationProperties       = configurationProperties;
        this.secretsCollectionName         = secretsCollectionName;
        this.secretsStorePurpose           = secretsStorePurpose;
        this.secretsStoreConnectorTypeGUID = secretsStoreConnectorTypeGUID;
        this.secretsStoreFileName          = secretsStoreFileName;
        this.replacementAttributeTypes     = replacementAttributeTypes;
        this.placeholderPropertyTypes      = placeholderPropertyTypes;
        this.contentPackDefinition         = contentPackDefinition;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return guid;
    }


    /**
     * Return the name to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateName()
    {
        return deployedImplementationType.getDeployedImplementationType() + " template";
    }

    /**
     * Return the description to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateDescription()
    {
        return "Create a " + deployedImplementationType.getDeployedImplementationType() + " asset with an associated Connection.";
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "V1.0";
    }


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    @Override
    public DeployedImplementationTypeDefinition getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the value to use in the element that describes its version.
     *
     * @return version identifier placeholder
     */
    @Override
    public String getElementVersionIdentifier()
    {
        return PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder();
    }


    /**
     * Return the name of the asset.
     *
     * @return enum
     */
    public String getAssetName()
    {
        return assetName;
    }

    /**
     * Return the name for the associated capability.
     *
     * @return string
     */
    public String getAssetDescription()
    {
        return assetDescription;
    }


    /**
     * Return the name of the server where this asset resides.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the properties defined for the asset subtype.
     *
     * @return map
     */
    public Map<String, Object> getExtendedProperties()
    {
        return extendedProperties;
    }

    /**
     * Return the type of encoding.  If null no encoding classification is added to the asset.
     *
     * @return string
     */
    public String getEncoding()
    {
        return encoding;
    }


    /**
     * Return the optional encoding language used by the asset.
     *
     * @return string
     */
    public String getEncodingLanguage()
    {
        return encodingLanguage;
    }


    /**
     * Return the connector type GUID for the connection.
     *
     * @return string
     */
    public String getConnectorTypeGUID()
    {
        return connectorTypeGUID;
    }


    /**
     * Return the endpoint address.
     *
     * @return string
     */
    public String getNetworkAddress()
    {
        return networkAddress;
    }


    /**
     * Return the configuration properties for the connection.
     *
     * @return map
     */
    public Map<String, Object> getConfigurationProperties()
    {
        return configurationProperties;
    }


    /**
     * Return the name of the secrets collection to use to locate this asset's secrets.
     *
     * @return name
     */
    public String getSecretsCollectionName()
    {
        return secretsCollectionName;
    }


    /**
     * Return the purpose of the secrets store.
     *
     * @return name
     */
    public String getSecretsStorePurpose()
    {
        return secretsStorePurpose;
    }

    /**
     * Return the optional secrets store connector provider for the server.
     *
     * @return connector provider
     */
    public String getSecretsStoreConnectorTypeGUID()
    {
        return secretsStoreConnectorTypeGUID;
    }


    /**
     * Return the location of the secrets store.
     *
     * @return path name of file
     */
    public String getSecretsStoreFileName()
    {
        return secretsStoreFileName;
    }


    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    @Override
    public List<PlaceholderPropertyType> getPlaceholders()
    {
        return placeholderPropertyTypes;
    }


    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    @Override
    public List<ReplacementAttributeType> getReplacementAttributes()
    {
        return replacementAttributeTypes;
    }


    /**
     * Get identifier of content pack where this template should be located.
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
        return "TemplateDefinition{templateName='" + getTemplateName() + "'}";
    }
}
