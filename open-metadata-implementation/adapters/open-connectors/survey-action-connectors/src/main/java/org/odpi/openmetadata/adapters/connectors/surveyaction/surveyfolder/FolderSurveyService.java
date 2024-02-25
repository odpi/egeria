/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector;
import org.odpi.openmetadata.adapters.connectors.surveyaction.AuditableSurveyService;
import org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc.SurveyServiceAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyOpenMetadataStore;
import org.odpi.openmetadata.frameworks.surveyaction.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.surveyaction.properties.DataProfileAnnotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.DataProfileLogAnnotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.DataSourceMeasurementAnnotation;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * CSVSurveyService is a survey action service implementation for analysing CSV Files to
 * columns and profile the data in them.
 */
public class FolderSurveyService extends AuditableSurveyService
{
    private final PropertyHelper propertyHelper = new PropertyHelper();
    private       Connector      connector      = null;


    private long fileCount = 0L;
    private long folderCount = 0L;
    private long canReadCount    = 0L;
    private long canWriteCount   = 0L;
    private long canExecuteCount = 0L;


    private final Map<String, Integer> fileExtensionCounts = new HashMap<>();
    private final Map<String, Integer> fileTypeCounts = new HashMap<>();
    private final Map<String, Integer> fileNameCounts = new HashMap<>();
    private final Map<String, Integer> assetTypeCounts = new HashMap<>();
    private final Map<String, Integer> deployedImplementationTypeCounts = new HashMap<>();


    /**
     * Return the updated value count for this column.
     *
     * @param existingValueCount current value count
     * @param newFieldValue next field value to process
     */
    private void updateValueCount(Map<String, Integer> existingValueCount, String newFieldValue)
    {
        Integer   countForValue = existingValueCount.get(newFieldValue);

        if (countForValue == null)
        {
            existingValueCount.put(newFieldValue, 1);
        }
        else
        {
            existingValueCount.put(newFieldValue, countForValue + 1);
        }
    }


    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            AnnotationStore          annotationStore   = surveyContext.getAnnotationStore();
            SurveyOpenMetadataStore  openMetadataStore = surveyContext.getOpenMetadataStore();
            SurveyAssetStore         assetStore        = surveyContext.getAssetStore();

            /*
             * Before performing any real work, check the type of the asset.
             */
            AssetUniverse assetUniverse = assetStore.getAssetProperties();

            if (assetUniverse == null)
            {
                super.throwNoAsset(assetStore.getAssetGUID(), methodName);
                return;
            }
            else if (! propertyHelper.isTypeOf(assetUniverse, OpenMetadataType.FILE_FOLDER.typeName))
            {
                super.throwWrongTypeOfAsset(assetUniverse.getGUID(),
                                            assetUniverse.getType().getTypeName(),
                                            OpenMetadataType.FILE_FOLDER.typeName,
                                            methodName);
                return;
            }


            /*
             * The asset should have a special connector for files.  If the connector is wrong,
             * the cast will fail.
             */
            connector = assetStore.getConnectorToAsset();
            BasicFolderConnector assetConnector = (BasicFolderConnector)connector;

            File rootFolder = assetConnector.getFile();

            if (! rootFolder.isDirectory())
            {
                throwWrongTypeOfResource(assetUniverse.getGUID(),
                                         assetUniverse.getType().getTypeName(),
                                         rootFolder.getName(),
                                         "file",
                                         "directory (folder)",
                                         methodName);
            }

            if (! rootFolder.exists())
            {
                throwMissingResource(assetUniverse.getGUID(),
                                     assetUniverse.getType().getTypeName(),
                                     rootFolder.getName(),
                                     methodName);
            }

            /*
             * Scan the folder (and sub-folders) and count up its contents
             */
            auditLog.logMessage(methodName, SurveyServiceAuditCode.SURVEYING_FOLDER.getMessageDefinition(surveyActionServiceName,
                                                                                                         rootFolder.getAbsolutePath()));

            LogFileProgress logFileProgress = new LogFileProgress(auditLog, surveyActionServiceName);

            profileFolder(openMetadataStore, rootFolder, logFileProgress);

            DataProfileAnnotation dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile File Extensions");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count the occurrences of each file extension.");
            dataProfile.setValueCount(fileExtensionCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile File Names");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count the occurrences of each file name.");
            dataProfile.setValueCount(fileNameCounts);
            annotationStore.addAnnotation(dataProfile, null);

            DataProfileLogAnnotation dataProfileLog = new DataProfileLogAnnotation();

            dataProfileLog.setAnnotationType("Profile File Names to External Log");
            dataProfileLog.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfileLog.setSummary("Iterate through files under a directory (folder) and count the occurrences of each file name.");
            List<String> dataProfileDataGUIDs = new ArrayList<>();
            dataProfileDataGUIDs.add(setUpExternalLogFile(annotationStore.getSurveyReportGUID(), fileNameCounts));
            dataProfileLog.setDataProfileLogGUIDs(dataProfileDataGUIDs);
            annotationStore.addAnnotation(dataProfileLog, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile File Types");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count the occurrences of each file type.");
            dataProfile.setValueCount(fileTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile Asset Types");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count each potential asset type if they were to be catalogued in open metadata.");
            dataProfile.setValueCount(assetTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile Deployed Implementation Types");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count each potential deployed implementation type if they were to be catalogued in open metadata.");
            dataProfile.setValueCount(deployedImplementationTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            DataSourceMeasurementAnnotation dataSourceMeasurementAnnotation = new DataSourceMeasurementAnnotation();

            dataSourceMeasurementAnnotation.setAnnotationType("Capture File Counts");
            dataSourceMeasurementAnnotation.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataSourceMeasurementAnnotation.setSummary("Count the number of directories and files under the starting directory.");

            Map<String, String> fileCountProperties = new HashMap<>();
            fileCountProperties.put("Number of Files", Long.toString(fileCount));
            fileCountProperties.put("Number of Subdirectories (folders)", Long.toString(folderCount));
            fileCountProperties.put("Readable files/directories", Long.toString(canReadCount));
            fileCountProperties.put("Writable files/directories", Long.toString(canWriteCount));
            fileCountProperties.put("Executable files/directories", Long.toString(canExecuteCount));
            dataSourceMeasurementAnnotation.setDataSourceProperties(fileCountProperties);

            annotationStore.addAnnotation(dataSourceMeasurementAnnotation, null);
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            super.handleUnexpectedException(methodName, error);
        }
    }


    /**
     * Create and catalog a CSV file to store data about the file names.
     *
     * @param surveyReportGUID identifier of the survey report
     * @param fileNameCounts map of file names and how many times they appear
     * @return unique identifier of the GUID for the CSV asset
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     */
    private String setUpExternalLogFile(String               surveyReportGUID,
                                        Map<String, Integer> fileNameCounts) throws IOException,
                                                                            InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "setUpExternalLogFile";

        String           logFileName = "surveys/report-" + surveyReportGUID + "-fileNameCounts.csv";
        SurveyAssetStore assetStore = surveyContext.getAssetStore();

        String assetGUID = assetStore.addCSVFileToCatalog("File name counts for survey report " + surveyReportGUID,
                                                          "Shows how many occurrences of each file name was found in the nested directory structure.",
                                                          logFileName,
                                                          null,
                                                          ',',
                                                          '"');

        File logFile = new File(logFileName);

        try
        {
            FileUtils.sizeOf(logFile);
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                             logFileName));
        }
        catch (IllegalArgumentException notFound)
        {
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                              logFileName,
                                                                                              assetGUID));

            FileUtils.writeStringToFile(logFile, "FileName, Number of Occurrences\n", (String)null, false);
        }


        for (String fileName : fileNameCounts.keySet())
        {
            String row = fileName + "," + fileNameCounts.get(fileName) + "\n";

            FileUtils.writeStringToFile(logFile, row, (String)null, true);
        }

        return assetGUID;
    }


    /**
     * Profile a single folder.
     *
     * @param openMetadataStore open metadata client
     * @param fileFolder folder to profile
     * @param logFileProgress progressBar
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     */
    private void profileFolder(SurveyOpenMetadataStore openMetadataStore,
                               File                    fileFolder,
                               LogFileProgress         logFileProgress) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        if (fileFolder != null)
        {
            if (fileFolder.listFiles() != null)
            {
                for (File nestedFile : Objects.requireNonNull(fileFolder.listFiles()))
                {
                    if (nestedFile.isDirectory())
                    {
                        folderCount++;
                        logFileProgress.logFilesProcessed();

                        profileFolder(openMetadataStore, nestedFile, logFileProgress);
                    }
                    else
                    {
                        fileCount++;
                        logFileProgress.logFilesProcessed();

                        FileClassifier fileClassifier = new FileClassifier(openMetadataStore, nestedFile);

                        updateValueCount(fileExtensionCounts, fileClassifier.getFileExtension());
                        updateValueCount(fileNameCounts, fileClassifier.getFileName());
                        updateValueCount(fileTypeCounts, fileClassifier.getFileType());
                        updateValueCount(deployedImplementationTypeCounts, fileClassifier.getDeployedImplementationType());
                        updateValueCount(assetTypeCounts, fileClassifier.getAssetTypeName());

                        if (fileClassifier.isCanRead())
                        {
                            canReadCount++;
                        }

                        if (fileClassifier.isCanWrite())
                        {
                            canWriteCount++;
                        }

                        if (fileClassifier.isCanExecute())
                        {
                            canExecuteCount++;
                        }
                    }
                }
            }
        }
    }


    /**
     * Provide a periodic progress report for the survey process.
     */
    static class LogFileProgress
    {
        private long fileLogPointer = 0;
        private long fileCount      = 0;

        private final AuditLog auditLog;
        private final String   surveyActionServiceName;

        /**
         * Constructor
         * @param auditLog logging destination
         * @param surveyActionServiceName name of this survey
         */
        public LogFileProgress(AuditLog auditLog,
                               String   surveyActionServiceName)
        {
            this.auditLog = auditLog;
            this.surveyActionServiceName = surveyActionServiceName;
        }


        /**
         * Log a message every 5000 files.
         */
        public void logFilesProcessed()
        {
            final String methodName   = "logFilesProcessed";
            final long   fileLogLimit = 5000;

            fileCount++;
            fileLogPointer++;

            if (fileLogPointer == fileLogLimit)
            {
                fileLogPointer = 0;

                auditLog.logMessage(methodName, SurveyServiceAuditCode.PROGRESS_REPORT.getMessageDefinition(surveyActionServiceName,
                                                                                                            Long.toString(fileCount)));
            }
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        if (connector != null)
        {
            connector.disconnect();
        }

        super.disconnect();
    }
}
