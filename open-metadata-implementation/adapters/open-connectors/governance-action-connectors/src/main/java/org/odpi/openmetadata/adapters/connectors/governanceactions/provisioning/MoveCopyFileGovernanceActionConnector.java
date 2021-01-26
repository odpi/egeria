/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.ProvisioningGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MoveCopyFileGovernanceActionConnector moves or copies files from one location to another and creates lineage between them.
 */
class MoveCopyFileGovernanceActionConnector extends ProvisioningGovernanceActionService
{
    static volatile Map<String, Integer> fileIndexMap = new HashMap<>();

    PropertyHelper propertyHelper = new PropertyHelper();



    private static synchronized String getDestinationFileName(String destinationFolderName,
                                                              File   sourceFile,
                                                              String fileNamePattern)
    {
        // todo
        return null;
    }



    /**
     *
     * @param destinationFolderName
     * @param sourceFilePathName full path name of the source file
     * @param fileNamePattern patten for generating the name of the destination file
     * @param copyFile is this a copy of a move?
     * @return name of new file
     */
    private static synchronized String provisionFile(String destinationFolderName,
                                                     String sourceFilePathName,
                                                     String fileNamePattern,
                                                     boolean copyFile) throws IOException
    {
        try
        {
            File   sourceFile          = new File(sourceFilePathName);
            String destinationFileName = getDestinationFileName(destinationFolderName, sourceFile, fileNamePattern);
            File destinationFile       = new File(destinationFileName);

            if (copyFile)
            {
                // todo log audit record
                FileUtils.copyFile(sourceFile, destinationFile, true);
            }
            else
            {
                // todo log audit record
                FileUtils.moveFile(sourceFile, destinationFile);
            }

            return destinationFileName;
        }
        catch (IOException exception)
        {
            // todo log audit record
            throw exception;
        }
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        int fileIndex = 1;

        String  lineageProcessName = this.getClass().getName();
        String  destinationFileNamePattern = "{0}";
        String  sourceFileName = null;
        String  sourceFileGUID = null;
        String  destinationFolderName = null;
        boolean copyFile = true;
        boolean createLineage = true;

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        /*
         * Retrieve the configuration properties from the Connection object.  These properties affect all requests to this connector.
         */
        if (configurationProperties != null)
        {
            Object provisionExternalFiles = configurationProperties.get(MoveCopyFileGovernanceActionProvider.PROVISION_UNCATALOGUED_FILES_CONFIGURATION_PROPERTY);

            /*
             * The name of the source file many be passed explicitly in the request parameters.
             */
            if (provisionExternalFiles != null)
            {
                if (governanceContext.getRequestParameters() != null)
                {
                    sourceFileName = governanceContext.getRequestParameters().get(MoveCopyFileGovernanceActionProvider.SOURCE_FILE_PARAMETER);
                    destinationFolderName = governanceContext.getRequestParameters().get(MoveCopyFileGovernanceActionProvider.DESTINATION_FOLDER_PARAMETER);
                }
            }

            Object noLineageOption = configurationProperties.get(MoveCopyFileGovernanceActionProvider.NO_LINEAGE_CONFIGURATION_PROPERTY);

            if (noLineageOption != null)
            {
                createLineage = false;
            }

            Object processNameOption = configurationProperties.get(MoveCopyFileGovernanceActionProvider.LINEAGE_PROCESS_NAME_CONFIGURATION_PROPERTY);

            if (processNameOption != null)
            {
                lineageProcessName = processNameOption.toString();
            }

            Object fileNamePatternOption = configurationProperties.get(MoveCopyFileGovernanceActionProvider.TARGET_FILE_NAME_PATTERN_CONFIGURATION_PROPERTY);

            if (processNameOption != null)
            {
                destinationFileNamePattern = fileNamePatternOption.toString();
            }
        }

        if (MoveCopyFileGovernanceActionProvider.MOVE_REQUEST_TYPE.equals(governanceContext.getRequestType()))
        {
            copyFile = false;
        }

        if (governanceContext.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
            {
                if (actionTargetElement != null)
                {
                    if (MoveCopyFileGovernanceActionProvider.SOURCE_FILE_ACTION_TARGET.equals(actionTargetElement.getActionTargetName()))
                    {
                        OpenMetadataElement sourceMetadataElement = actionTargetElement.getTargetElement();

                        if (sourceMetadataElement != null)
                        {
                            sourceFileName = this.getPathName(sourceMetadataElement);
                            sourceFileGUID = sourceMetadataElement.getElementGUID();
                        }
                    }
                    else if (MoveCopyFileGovernanceActionProvider.DESTINATION_FOLDER_ACTION_TARGET.equals(actionTargetElement.getActionTargetName()))
                    {
                        OpenMetadataElement destinationMetadataElement = actionTargetElement.getTargetElement();

                        if (destinationMetadataElement != null)
                        {
                            destinationFolderName = this.getPathName(destinationMetadataElement);
                        }
                    }
                }
            }
        }

        try
        {
            String destinationFileName = provisionFile(destinationFolderName, sourceFileName, destinationFileNamePattern, copyFile);

            if (createLineage)
            {
                createLineage(sourceFileGUID, destinationFileName, lineageProcessName);
            }
        }
        catch (Exception  error)
        {
            // todo log audit record
        }
    }


    private String getPathName(OpenMetadataElement metadataElement)
    {
        final String methodName = "getPathName";
        // todo navigate to Endpoint and only use qualifiedName if Endpoint not available

        ElementProperties properties = metadataElement.getElementProperties();

        return propertyHelper.getStringProperty(governanceServiceName,
                                                "qualifiedName",
                                                properties,
                                                methodName);
    }


    /**
     * Create the lineage mapping for the provisioning process.
     *
     * @param sourceFileGUID open metadata unique identifier for the source file asset
     * @param destinationFilePathName name of the file that was created
     * @param lineageProcessName name of this provisioning process
     *
     * @throws InvalidParameterException one of the parameters passed to open metadata is invalid (probably a bug in this code)
     * @throws UserNotAuthorizedException the userId for the connector does not have the authority it needs
     * @throws PropertyServerException there is a problem with the metadata server(s)
     */
    private void createLineage(String  sourceFileGUID,
                               String  destinationFilePathName,
                               String  lineageProcessName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String processTypeName = "TransientEmbeddedProcess";

        String fileName = FilenameUtils.getName(destinationFilePathName);
        String fileExtension = FilenameUtils.getExtension(destinationFilePathName);
        String assetTypeName = "DataFile";

        switch (fileExtension)
        {
            case "csv":
                assetTypeName = "CSVFile";
                break;

            case "json":
                assetTypeName = "JSONFile";
                break;

            case "avro":
                assetTypeName = "AvroFileName";
                break;

            case "pdf":
            case "doc":
            case "docx":
            case "ppt":
            case "pptx":
            case "xls":
            case "xlsx":
            case "md":
                assetTypeName = "Document";
                break;

            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "mp3":
            case "mp4":
                assetTypeName = "MediaFile";
                break;
        }

        String newFileGUID = governanceContext.createAsset(assetTypeName,
                                                           destinationFilePathName,
                                                           fileName,
                                                           null,
                                                           null);

        String processGUID = governanceContext.createProcess(processTypeName,
                                                             ElementStatus.ACTIVE,
                                                             lineageProcessName + ":" + UUID.randomUUID().toString(),
                                                             lineageProcessName,
                                                             null,
                                                             null);

        governanceContext.createLineageMapping(sourceFileGUID, processGUID);
        governanceContext.createLineageMapping(processGUID, newFileGUID);
    }
}
