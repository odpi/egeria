/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.files;

import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.DataAssetTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileExtension;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileName;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;


/**
 * FilesArchiveWriter creates an open metadata archive that includes the connectors and reference data to support
 * the surveying and cataloguing of files in the file systems.
 */
public class FilesArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public FilesArchiveWriter()
    {
        super(ContentPackDefinition.FILES_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.FILES_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.FILES_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.FILES_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {

        /*
         * Add the valid values for the fileType property.
         */
        for (FileType fileType : FileType.values())
        {
            this.addFileType(fileType.getFileTypeName(),
                             fileType.getEncoding(),
                             fileType.getAssetSubTypeName(),
                             fileType.getDeployedImplementationType(),
                             fileType.getDescription());
        }

        /*
         * Add the list of special file names.
         */
        for (FileName fileName : FileName.values())
        {
            this.addFileName(fileName.getFileName(),
                             fileName.getFileType(),
                             fileName.getFileType().getDeployedImplementationType());
        }

        /*
         * Add the list of recognized file extensions.
         */
        for (FileExtension fileExtension : FileExtension.values())
        {
            this.addFileExtension(fileExtension.getFileExtension(), fileExtension.getFileTypes());
        }


        /*
         * Create Egeria's integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.FILES_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.FILES_CONTENT_PACK,
                                       IntegrationGroupDefinition.FILES);

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.FILES_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.FILES_CONTENT_PACK);
        this.addTabularDataSetCatalogTemplates(ContentPackDefinition.FILES_CONTENT_PACK);


        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.FILES_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.FILES_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.FILES_CONTENT_PACK);

        this.createAndSurveyServerGovernanceActionProcess("FileDirectory",
                                                          DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_FILE_FOLDER,
                                                          DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE,
                                                          RequestTypeDefinition.SURVEY_ALL_FOLDERS_AND_FILES,
                                                          DeployedImplementationType.FILE_FOLDER.getQualifiedName());

        this.createAndCatalogServerGovernanceActionProcess("FileDirectory",
                                                           DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_FILE_FOLDER,
                                                           DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE,
                                                           RequestTypeDefinition.CATALOG_FILE_FOLDER,
                                                           DeployedImplementationType.FILE_FOLDER.getQualifiedName());

        this.deleteAsCatalogTargetGovernanceActionProcess("FileDirectory",
                                                          DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName(),
                                                          DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_FILE_FOLDER,
                                                          DeployedImplementationType.FILE_FOLDER.getQualifiedName());

        this.createAndSurveyServerGovernanceActionProcess("DataDirectory",
                                                          DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_DATA_FOLDER,
                                                          DataAssetTemplateDefinition.DATA_FOLDER_TEMPLATE,
                                                          RequestTypeDefinition.SURVEY_FOLDER_AND_FILES,
                                                          DeployedImplementationType.DATA_FOLDER.getQualifiedName());

        this.createAndCatalogServerGovernanceActionProcess("DataDirectory",
                                                           DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_DATA_FOLDER,
                                                           DataAssetTemplateDefinition.DATA_FOLDER_TEMPLATE,
                                                           RequestTypeDefinition.CATALOG_DATA_FOLDER,
                                                           DeployedImplementationType.DATA_FOLDER.getQualifiedName());

        this.deleteAsCatalogTargetGovernanceActionProcess("DataDirectory",
                                                          DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName(),
                                                          DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_DATA_FOLDER,
                                                          DeployedImplementationType.DATA_FOLDER.getQualifiedName());

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.FILES_CONTENT_PACK);
        this.addSolutionLinkingWires(ContentPackDefinition.FILES_CONTENT_PACK);

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }



    /**
     * Add reference data that describes a specific file type.
     *
     * @param fileTypeName               the name of the file type
     * @param encoding                   the optional name of the encoding method used in the file
     * @param deployedImplementationType value for deployedImplementationType
     * @param assetSubTypeName           the open metadata type where this value is used
     * @param description                description of the type
     */
    private void addFileType(String                     fileTypeName,
                             String                     encoding,
                             String                     assetSubTypeName,
                             DeployedImplementationType deployedImplementationType,
                             String                     description)
    {
        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.FILE_TYPE.name,
                                                                null,
                                                                fileTypeName);

        Map<String, String> additionalProperties = new HashMap<>();

        if (encoding != null)
        {
            additionalProperties.put(OpenMetadataProperty.ENCODING_TYPE.name, encoding);
        }

        if (assetSubTypeName != null)
        {
            additionalProperties.put(OpenMetadataValidValues.ASSET_SUB_TYPE_NAME, assetSubTypeName);
        }

        if (additionalProperties.isEmpty())
        {
            additionalProperties = null;
        }

        super.addValidMetadataValue(null,
                                    fileTypeName,
                                    description,
                                    OpenMetadataProperty.FILE_TYPE.name,
                                    DataType.STRING.getName(),
                                    null,
                                    null,
                                    fileTypeName,
                                    additionalProperties);

        if (deployedImplementationType != null)
        {
            String deployedImplementationTypeQName = deployedImplementationType.getQualifiedName();
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, deployedImplementationTypeQName);
        }
    }


    /**
     * Add reference data for a file name.
     *
     * @param fileName                   name of the file
     * @param fileType                   the file type
     * @param deployedImplementationType value for deployedImplementationType
     */
    private void addFileName(String                     fileName,
                             FileType                   fileType,
                             DeployedImplementationType deployedImplementationType)
    {
        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.FILE_NAME.name,
                                                                null,
                                                                fileName);

        this.addValidMetadataValue(fileName,
                                   null,
                                   OpenMetadataProperty.FILE_NAME.name,
                                   null,
                                   null,
                                   fileName);

        if (deployedImplementationType != null)
        {
            String deployedImplementationTypeQName = constructValidValueQualifiedName(deployedImplementationType.getAssociatedTypeName(),
                                                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                      null,
                                                                                      deployedImplementationType.getDeployedImplementationType());
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, deployedImplementationTypeQName);
        }

        if (fileType != null)
        {
            String fileTypeQName = constructValidValueQualifiedName(null,
                                                                    OpenMetadataProperty.FILE_TYPE.name,
                                                                    null,
                                                                    fileType.getFileTypeName());
            this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, fileTypeQName);
        }
    }


    /**
     * Add reference data for a file extension.
     *
     * @param fileExtension   name of the file
     * @param fileTypes      list of matching file types
     */
    private void addFileExtension(String                     fileExtension,
                                  List<FileType> fileTypes)
    {
        String qualifiedName = constructValidValueQualifiedName(null,
                                                                OpenMetadataProperty.FILE_EXTENSION.name,
                                                                null,
                                                                fileExtension);

        this.addValidMetadataValue(fileExtension,
                                   null,
                                   OpenMetadataProperty.FILE_EXTENSION.name,
                                   null,
                                   null,
                                   fileExtension);

        if (fileTypes != null)
        {
            for (FileType fileType : fileTypes)
            {
                String fileTypeQName = constructValidValueQualifiedName(null,
                                                                        OpenMetadataProperty.FILE_TYPE.name,
                                                                        null,
                                                                        fileType.getFileTypeName());
                this.archiveHelper.addConsistentValidValueRelationship(qualifiedName, fileTypeQName);
            }
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
            FilesArchiveWriter archiveWriter = new FilesArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}