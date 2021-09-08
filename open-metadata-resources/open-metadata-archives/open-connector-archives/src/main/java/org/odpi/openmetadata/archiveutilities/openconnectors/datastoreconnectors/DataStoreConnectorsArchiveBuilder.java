/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors.datastoreconnectors;

import org.odpi.openmetadata.adapters.connectors.datastore.avrofile.AvroFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.OpenConnectorArchiveBuilder;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.util.Date;

/**
 * DataStoreConnectorsArchiveBuilder provides the support to build an open metadata archive that
 * contains default connector types for each of the data connectors supported by Egeria.
 * The information for these connector types is extracted from the Connector Provider implementations.
 */
public class DataStoreConnectorsArchiveBuilder extends OpenConnectorArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "acdc5637-92a7-4926-b47b-a3d407546f89";
    private static final String                  archiveRootName    = "DataStoreConnectorTypes";
    private static final String                  archiveName        = "Data Store Open Connector Types";
    private static final String                  archiveLicense     = "Apache 2.0";
    private static final String                  archiveDescription = "Standard connector categories and connector type definitions for data store " +
                                                                              "connectors that follow the Open Connector Framework (OCF).";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1570383395115L);

    /*
     * Names for standard definitions
     */
    private static final String connectorTypeDirectoryQualifiedName = "OpenMetadataConnectorTypeDirectory_acdc5637-92a7-4926-b47b-a3d407546f89";
    private static final String connectorTypeDirectoryDisplayName   = "Open Metadata Connector Type Directory";
    private static final String connectorTypeDirectoryDescription   = "Open Metadata standard connector categories and connector types.";
    private static final String fileConnectorCategoryQualifiedName  = "OpenMetadataFileConnectorCategory_acdc5637-92a7-4926-b47b-a3d407546f89";
    private static final String fileConnectorCategoryDisplayName    = "Open Metadata File Connector Category";
    private static final String fileConnectorCategoryDescription    = "Open Metadata connector category for connectors that work with files.";
    private static final String kafkaConnectorCategoryQualifiedName = "OpenMetadataKafkaConnectorCategory_acdc5637-92a7-4926-b47b-a3d407546f89";
    private static final String kafkaConnectorCategoryDisplayName   = "Open Metadata Apache Kafka Connector Category Directory";
    private static final String kafkaConnectorCategoryDescription   = "Open Metadata connector category for connectors to Apache Kafka.";
    private static final String kafkaConnectorCategoryTargetSource  = "Apache Software Foundation (ASF)";
    private static final String kafkaConnectorCategoryTargetName    = "Apache Kafka.";

    /*
     * Additional AssetTypes for basic file connector
     */
    private static final String jsonFileAssetTypeName  = "JSONFile";
    private static final String jsonFileFormat         = "json";
    private static final String mediaFileAssetTypeName = "MediaFile";
    private static final String documentAssetTypeName  = "Document";


    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    /**
     * Default constructor pushes all archive header values to the superclass
     */
    public DataStoreConnectorsArchiveBuilder()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              archiveType,
              archiveRootName,
              originatorName,
              archiveLicense,
              creationDate,
              versionNumber,
              versionName);
    }


    /**
     * Returns the open metadata type archive containing all of the elements extracted from the connector
     * providers of the featured open connectors.
     *
     * @return populated open metadata archive object
     */
    protected OpenMetadataArchive getOpenMetadataArchive()
    {
        ConnectorType              connectorType;

        String connectorDirectoryTypeGUID = super.addConnectorTypeDirectory(connectorTypeDirectoryQualifiedName,
                                                                            connectorTypeDirectoryDisplayName,
                                                                            connectorTypeDirectoryDescription,
                                                                            null);

        String fileConnectorCategoryGUID = super.addConnectorCategory(connectorDirectoryTypeGUID,
                                                                      fileConnectorCategoryQualifiedName,
                                                                      fileConnectorCategoryDisplayName,
                                                                      fileConnectorCategoryDescription,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null);

        String kafkaConnectorCategoryGUID = super.addConnectorCategory(connectorDirectoryTypeGUID,
                                                                       kafkaConnectorCategoryQualifiedName,
                                                                       kafkaConnectorCategoryDisplayName,
                                                                       kafkaConnectorCategoryDescription,
                                                                       kafkaConnectorCategoryTargetSource,
                                                                       kafkaConnectorCategoryTargetName,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null);

//TODO: Put Cassandra connector back after it gets migrated to new repository and can be used as external connector.

//        CassandraDataStoreProvider cassandraDataStoreProvider = new CassandraDataStoreProvider();
//        connectorType = cassandraDataStoreProvider.getConnectorType();
//
//        super.addConnectorType(connectorType.getGUID(),
//                               connectorType.getQualifiedName(),
//                               connectorType.getDisplayName(),
//                               connectorType.getDescription(),
//                               connectorType.getConnectorProviderClassName(),
//                               connectorType.getRecognizedSecuredProperties(),
//                               connectorType.getRecognizedConfigurationProperties(),
//                               connectorType.getRecognizedAdditionalProperties(),
//                               connectorType.getAdditionalProperties());



        AvroFileStoreProvider avroFileStoreProvider = new AvroFileStoreProvider();
        connectorType = avroFileStoreProvider.getConnectorType();

        super.addConnectorType(fileConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getSupportedAssetTypeName(),
                               connectorType.getExpectedDataFormat(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        CSVFileStoreProvider csvFileStoreProvider = new CSVFileStoreProvider();
        connectorType = csvFileStoreProvider.getConnectorType();

        super.addConnectorType(fileConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getSupportedAssetTypeName(),
                               connectorType.getExpectedDataFormat(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        DataFolderProvider dataFolderProvider = new DataFolderProvider();
        connectorType = dataFolderProvider.getConnectorType();

        super.addConnectorType(fileConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getSupportedAssetTypeName(),
                               connectorType.getExpectedDataFormat(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        BasicFileStoreProvider basicFileStoreProvider = new BasicFileStoreProvider();
        connectorType = basicFileStoreProvider.getConnectorType();

        super.addConnectorType(fileConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getSupportedAssetTypeName(),
                               connectorType.getExpectedDataFormat(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        super.addConnectorType(fileConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName() + jsonFileAssetTypeName,
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               jsonFileAssetTypeName,
                               jsonFileFormat,
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        super.addConnectorType(fileConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName() + mediaFileAssetTypeName,
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               mediaFileAssetTypeName,
                               null,
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        super.addConnectorType(fileConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName() + documentAssetTypeName,
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               documentAssetTypeName,
                               null,
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        KafkaOpenMetadataTopicProvider kafkaOpenMetadataTopicProvider = new KafkaOpenMetadataTopicProvider();
        connectorType = kafkaOpenMetadataTopicProvider.getConnectorType();

        super.addConnectorType(kafkaConnectorCategoryGUID,
                               connectorType.getGUID(),
                               connectorType.getQualifiedName(),
                               connectorType.getDisplayName(),
                               connectorType.getDescription(),
                               connectorType.getSupportedAssetTypeName(),
                               connectorType.getExpectedDataFormat(),
                               connectorType.getConnectorProviderClassName(),
                               connectorType.getConnectorFrameworkName(),
                               connectorType.getConnectorInterfaceLanguage(),
                               connectorType.getConnectorInterfaces(),
                               connectorType.getTargetTechnologySource(),
                               connectorType.getTargetTechnologyName(),
                               connectorType.getTargetTechnologyInterfaces(),
                               connectorType.getTargetTechnologyVersions(),
                               connectorType.getRecognizedSecuredProperties(),
                               connectorType.getRecognizedConfigurationProperties(),
                               connectorType.getRecognizedAdditionalProperties(),
                               connectorType.getAdditionalProperties());

        return super.getOpenMetadataArchive();
    }
}
