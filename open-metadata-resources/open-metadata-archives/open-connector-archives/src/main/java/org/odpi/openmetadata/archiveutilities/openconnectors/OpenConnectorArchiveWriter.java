/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.adapters.connectors.datastore.avrofile.AvroFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OpenConnectorArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public class OpenConnectorArchiveWriter extends OMRSArchiveWriter
{
    private static final String archiveFileName = "OpenConnectorsArchive.json";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String                  archiveName        = "OpenConnectorsArchive";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Connector Types and Categories for connectors from the Egeria project.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria Project";
    private static final Date                    creationDate       = new Date(1649686978059L);

    /*
     * Names for standard definitions
     */
    private static final String connectorTypeDirectoryQualifiedName = "OpenMetadataConnectorTypeDirectory_09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String connectorTypeDirectoryDisplayName   = "Open Metadata Connector Type Directory";
    private static final String connectorTypeDirectoryDescription   = "Open Metadata standard connector categories and connector types.";
    private static final String fileConnectorCategoryQualifiedName  = "OpenMetadataFileConnectorCategory_09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
    private static final String fileConnectorCategoryDisplayName    = "Open Metadata File Connector Category";
    private static final String fileConnectorCategoryDescription    = "Open Metadata connector category for connectors that work with files.";
    private static final String kafkaConnectorCategoryQualifiedName = "OpenMetadataKafkaConnectorCategory_09450b83-20ff-4a8b-a8fb-f9b527bbcba6";
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

    private final OMRSArchiveBuilder         archiveBuilder;
    private final SimpleCatalogArchiveHelper archiveHelper;

    /**
     * Default constructor initializes the archive.
     */
    private OpenConnectorArchiveWriter()
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the Coco Types to be based on the existing open metadata types
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new SimpleCatalogArchiveHelper(archiveBuilder,
                                                            archiveGUID,
                                                            archiveName,
                                                            archiveName,
                                                            originatorName,
                                                            creationDate,
                                                            versionNumber,
                                                            versionName);
    }



    /**
     * Returns the open metadata archive containing new metadata entities.
     *
     * @return populated open metadata archive object
     */
    private OpenMetadataArchive getOpenMetadataArchive()
    {
        ConnectorType connectorType;

        String connectorDirectoryTypeGUID = archiveHelper.addConnectorTypeDirectory(connectorTypeDirectoryQualifiedName,
                                                                                    connectorTypeDirectoryDisplayName,
                                                                                    connectorTypeDirectoryDescription,
                                                                                    null);

        String fileConnectorCategoryGUID = archiveHelper.addConnectorCategory(connectorDirectoryTypeGUID,
                                                                              fileConnectorCategoryQualifiedName,
                                                                              fileConnectorCategoryDisplayName,
                                                                              fileConnectorCategoryDescription,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              null);

        String kafkaConnectorCategoryGUID = archiveHelper.addConnectorCategory(connectorDirectoryTypeGUID,
                                                                               kafkaConnectorCategoryQualifiedName,
                                                                               kafkaConnectorCategoryDisplayName,
                                                                               kafkaConnectorCategoryDescription,
                                                                               kafkaConnectorCategoryTargetSource,
                                                                               kafkaConnectorCategoryTargetName,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null);


        AvroFileStoreProvider avroFileStoreProvider = new AvroFileStoreProvider();
        connectorType = avroFileStoreProvider.getConnectorType();

        archiveHelper.addConnectorType(fileConnectorCategoryGUID,
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

        archiveHelper.addConnectorType(fileConnectorCategoryGUID,
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

        archiveHelper.addConnectorType(fileConnectorCategoryGUID,
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

        archiveHelper.addConnectorType(fileConnectorCategoryGUID,
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

        archiveHelper.addConnectorType(fileConnectorCategoryGUID,
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

        archiveHelper.addConnectorType(fileConnectorCategoryGUID,
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

        archiveHelper.addConnectorType(fileConnectorCategoryGUID,
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

        archiveHelper.addConnectorType(kafkaConnectorCategoryGUID,
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

        archiveHelper.saveGUIDs();

        /*
         * The completed archive is ready to be packaged up and returned
         */
        return this.archiveBuilder.getOpenMetadataArchive();
    }


    /**
     * Generates and writes out an open metadata archive containing all the connector types
     * describing the Egeria project open connectors.
     */
    private void writeOpenMetadataArchive()
    {
        try
        {
            System.out.println("Writing to file: " + archiveFileName);

            super.writeOpenMetadataArchive(archiveFileName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error.toString());
        }
    }


    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            OpenConnectorArchiveWriter archiveWriter = new OpenConnectorArchiveWriter();

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error.toString());
            System.exit(-1);
        }
    }
}
