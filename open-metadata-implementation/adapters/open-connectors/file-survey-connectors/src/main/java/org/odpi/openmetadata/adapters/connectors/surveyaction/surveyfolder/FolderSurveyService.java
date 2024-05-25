/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector;
import org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc.SurveyServiceAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.*;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * FolderSurveyService is a survey action service implementation for analysing the files nested in a folder.
 */
public class FolderSurveyService extends SurveyActionServiceConnector
{
    private final PropertyHelper propertyHelper = new PropertyHelper();
    private       Connector      connector      = null;


    private long fileCount       = 0L;
    private long folderCount     = 0L;
    private long canReadCount    = 0L;
    private long canWriteCount   = 0L;
    private long canExecuteCount = 0L;

    private Date lastFileCreationTime     = null;
    private Date lastFileModificationTime = null;
    private Date lastFileAccessTime       = null;

    private final List<InaccessibleFile> inaccessibleFiles = new ArrayList<>();

    private final Map<String, Integer>     fileExtensionCounts              = new HashMap<>();
    private final Map<String, Integer>     fileTypeCounts                   = new HashMap<>();
    private final Map<String, Integer>     fileNameCounts                   = new HashMap<>();
    private final Map<String, Integer>     assetTypeCounts                  = new HashMap<>();
    private final Map<String, Integer>     deployedImplementationTypeCounts = new HashMap<>();
    private final List<FileClassification> missingReferenceData             = new ArrayList<>();


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
            AnnotationStore          annotationStore   = getSurveyContext().getAnnotationStore();
            SurveyAssetStore         assetStore        = surveyContext.getAssetStore();

            annotationStore.setAnalysisStep(AnalysisStep.CHECK_ASSET.getName());

            /*
             * Before performing any real work, check the type of the asset.
             */
            AssetUniverse assetUniverse = assetStore.getAssetProperties();

            if (assetUniverse == null)
            {
                super.throwNoAsset(assetStore.getAssetGUID(), surveyActionServiceName, methodName);
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
                                         surveyActionServiceName,
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

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());


            LogFileProgress logFileProgress = new LogFileProgress(auditLog, surveyActionServiceName);

            profileFolder(rootFolder, logFileProgress);

            ResourceProfileAnnotation dataProfile = new ResourceProfileAnnotation();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_FILE_EXTENSIONS);
            dataProfile.setValueCount(fileExtensionCounts);
            annotationStore.addAnnotation(dataProfile, null);

            ResourceProfileLogAnnotation dataProfileLog = new ResourceProfileLogAnnotation();

            setUpAnnotation(dataProfileLog, SurveyFolderAnnotationType.PROFILE_FILE_NAMES);
            List<String> dataProfileDataGUIDs = new ArrayList<>();
            dataProfileDataGUIDs.add(setUpExternalLogFile(annotationStore.getSurveyReportGUID(), fileNameCounts));
            dataProfileLog.setResourceProfileLogGUIDs(dataProfileDataGUIDs);
            annotationStore.addAnnotation(dataProfileLog, null);

            if (! missingReferenceData.isEmpty())
            {
                RequestForActionAnnotation requestForActionAnnotation = new RequestForActionAnnotation();

                setUpAnnotation(requestForActionAnnotation, SurveyFolderAnnotationType.MISSING_REF_DATA);
                List<String> requestForActionTargetGUIDs = new ArrayList<>();
                requestForActionTargetGUIDs.add(setUpMissingRefDateExternalLogFile(annotationStore.getSurveyReportGUID(), missingReferenceData));
                requestForActionAnnotation.setActionTargetGUIDs(requestForActionTargetGUIDs);
                annotationStore.addAnnotation(requestForActionAnnotation, null);
            }

            if (! inaccessibleFiles.isEmpty())
            {
                RequestForActionAnnotation requestForActionAnnotation = new RequestForActionAnnotation();

                setUpAnnotation(requestForActionAnnotation, SurveyFolderAnnotationType.INACCESSIBLE_FILES);
                List<String> requestForActionTargetGUIDs = new ArrayList<>();
                requestForActionTargetGUIDs.add(setUpInaccessibleFilesExternalLogFile(annotationStore.getSurveyReportGUID(), inaccessibleFiles));
                requestForActionAnnotation.setActionTargetGUIDs(requestForActionTargetGUIDs);
                annotationStore.addAnnotation(requestForActionAnnotation, null);
            }

            dataProfile = new ResourceProfileAnnotation();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_FILE_TYPES);
            dataProfile.setValueCount(fileTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new ResourceProfileAnnotation();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_ASSET_TYPES);
            dataProfile.setValueCount(assetTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new ResourceProfileAnnotation();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_DEP_IMPL_TYPES);
            dataProfile.setValueCount(deployedImplementationTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            ResourceMeasureAnnotation resourceMeasureAnnotation = new ResourceMeasureAnnotation();

            setUpAnnotation(resourceMeasureAnnotation, SurveyFolderAnnotationType.MEASUREMENTS);

            Map<String, String> fileCountProperties = new HashMap<>();
            fileCountProperties.put(FolderMetric.FILE_COUNT.getName(), Long.toString(fileCount));
            fileCountProperties.put(FolderMetric.SUB_DIRECTORY_COUNT.getName(), Long.toString(folderCount));
            fileCountProperties.put(FolderMetric.READABLE_FILE_COUNT.getName(), Long.toString(canReadCount));
            fileCountProperties.put(FolderMetric.WRITEABLE_FILE_COUNT.getName(), Long.toString(canWriteCount));
            fileCountProperties.put(FolderMetric.EXECUTABLE_FILE_COUNT.getName(), Long.toString(canExecuteCount));
            if (lastFileCreationTime != null)
            {
                fileCountProperties.put(FolderMetric.LAST_FILE_CREATION_TIME.getName(), lastFileCreationTime.toString());
            }
            if (lastFileModificationTime != null)
            {
                fileCountProperties.put(FolderMetric.LAST_FILE_MODIFIED_TIME.getName(), lastFileModificationTime.toString());
            }
            if (lastFileAccessTime != null)
            {
                fileCountProperties.put(FolderMetric.LAST_FILE_ACCESSED_TIME.getName(), lastFileAccessTime.toString());
            }

            resourceMeasureAnnotation.setResourceProperties(fileCountProperties);

            annotationStore.addAnnotation(resourceMeasureAnnotation, null);
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
     * Transfer common properties into the annotation.
     *
     * @param annotation output annotation
     * @param annotationType annotation type definition
     */
    private void setUpAnnotation(Annotation                 annotation,
                                 SurveyFolderAnnotationType annotationType)
    {
        annotation.setAnnotationType(annotationType.getName());
        annotation.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());
        annotation.setSummary(annotationType.getSummary());
        annotation.setExplanation(annotationType.getExplanation());
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
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    private String setUpExternalLogFile(String               surveyReportGUID,
                                        Map<String, Integer> fileNameCounts) throws IOException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException,
                                                                                    ConnectorCheckedException
    {
        final String methodName = "setUpExternalLogFile";

        String           logFileName = "surveys/report-" + surveyReportGUID + "-fileNameCounts.csv";
        SurveyAssetStore assetStore = surveyContext.getAssetStore();
        File             logFile = new File(logFileName);
        boolean          newLogFile = false;

        try
        {
            FileUtils.sizeOf(logFile);
        }
        catch (IllegalArgumentException notFound)
        {
            newLogFile = true;
            FileUtils.writeStringToFile(logFile, "FileName, Number of Occurrences\n", (String)null, false);
        }

        String assetGUID = assetStore.addCSVFileToCatalog("File name counts for survey report " + surveyReportGUID,
                                                          "Shows how many occurrences of each file name was found in the nested directory structure.",
                                                          logFile.getAbsolutePath(),
                                                          null,
                                                          ',',
                                                          '"');

        if (newLogFile)
        {
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                              logFileName,
                                                                                              assetGUID));
        }
        else
        {
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                             logFileName));
        }

        for (String fileName : fileNameCounts.keySet())
        {
            String row = fileName + "," + fileNameCounts.get(fileName) + "\n";

            FileUtils.writeStringToFile(logFile, row, (String)null, true);
        }

        return assetGUID;
    }


    /**
     * Create and catalog a CSV file to store data about the file names.
     *
     * @param surveyReportGUID identifier of the survey report
     * @param missingReferenceData list of incompletely classified files
     * @return unique identifier of the GUID for the CSV asset
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    private String setUpMissingRefDateExternalLogFile(String                   surveyReportGUID,
                                                      List<FileClassification> missingReferenceData) throws IOException,
                                                                                                            InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            ConnectorCheckedException
    {
        final String methodName = "setUpExternalLogFile";

        String           logFileName = "surveys/report-" + surveyReportGUID + "-missingReferenceData.csv";
        SurveyAssetStore assetStore = surveyContext.getAssetStore();
        File             logFile = new File(logFileName);
        boolean          newLogFile = false;

        try
        {
            FileUtils.sizeOf(logFile);
        }
        catch (IllegalArgumentException notFound)
        {
            newLogFile = true;
            FileUtils.writeStringToFile(logFile, "FileName,FileExtension,PathName,FileType,AssetTypeName,DeployedImplementationType,Encoding\n", (String)null, false);
        }

        String assetGUID = assetStore.addCSVFileToCatalog("Missing reference data for survey report " + surveyReportGUID,
                                                          "Shows the files that could not be correctly classified from the reference data.",
                                                          logFile.getAbsolutePath(),
                                                          null,
                                                          ',',
                                                          '"');


        if (newLogFile)
        {
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                              logFileName,
                                                                                              assetGUID));
        }
        else
        {
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                             logFileName));
        }


        for (FileClassification fileClassification : missingReferenceData)
        {
            String stringBuilder = fileClassification.getFileName() +
                    "," +
                    fileClassification.getFileExtension() +
                    "," +
                    fileClassification.getPathName() +
                    "," +
                    fileClassification.getFileType() +
                    "," +
                    fileClassification.getAssetTypeName() +
                    "," +
                    fileClassification.getDeployedImplementationType() +
                    "," +
                    fileClassification.getEncoding() +
                    "\n";

            FileUtils.writeStringToFile(logFile, stringBuilder, (String)null, true);
        }

        return assetGUID;
    }



    /**
     * Create and catalog a CSV file to store data about the file names.
     *
     * @param surveyReportGUID identifier of the survey report
     * @param inaccessibleFiles list of files that could not be accessed
     * @return unique identifier of the GUID for the CSV asset
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    private String setUpInaccessibleFilesExternalLogFile(String                 surveyReportGUID,
                                                         List<InaccessibleFile> inaccessibleFiles) throws IOException,
                                                                                                          InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          ConnectorCheckedException
    {
        final String methodName = "setUpExternalLogFile";

        String           logFileName = "surveys/report-" + surveyReportGUID + "-inaccessibleFiles.csv";
        SurveyAssetStore assetStore = surveyContext.getAssetStore();
        File             logFile = new File(logFileName);
        boolean          newLogFile = false;

        try
        {
            FileUtils.sizeOf(logFile);
        }
        catch (IllegalArgumentException notFound)
        {
            newLogFile = true;
            FileUtils.writeStringToFile(logFile, "FileName,Exception,Message\n", (String)null, false);
        }

        String assetGUID = assetStore.addCSVFileToCatalog("Inaccessible files detected by " + surveyReportGUID,
                                                          "Shows the files that could not be accessed.",
                                                          logFile.getAbsolutePath(),
                                                          null,
                                                          ',',
                                                          '"');


        if (newLogFile)
        {
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.CREATING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                              logFileName,
                                                                                              assetGUID));
        }
        else
        {
            auditLog.logMessage(methodName,
                                SurveyServiceAuditCode.REUSING_LOG_FILE.getMessageDefinition(surveyActionServiceName,
                                                                                             logFileName));
        }


        for (InaccessibleFile inaccessibleFile : inaccessibleFiles)
        {
            String stringBuilder = inaccessibleFile.fileName +
                    "," +
                    inaccessibleFile.exceptionClassName +
                    "," +
                    inaccessibleFile.exceptionMessage +
                    "\n";

            FileUtils.writeStringToFile(logFile, stringBuilder, (String)null, true);
        }

        return assetGUID;
    }


    /**
     * Profile a single folder.
     *
     * @param fileFolder folder to profile
     * @param logFileProgress progressBar
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    private void profileFolder(File                    fileFolder,
                               LogFileProgress         logFileProgress) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException
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

                        if (! FileUtils.isSymlink(nestedFile))
                        {
                            profileFolder(nestedFile, logFileProgress);
                        }
                    }
                    else if (nestedFile.isFile())
                    {
                        try
                        {
                            FileClassifier     fileClassifier     = surveyContext.getFileClassifier();
                            FileClassification fileClassification = fileClassifier.classifyFile(nestedFile);

                            fileCount++;
                            logFileProgress.logFilesProcessed();

                            if ((fileClassification.getFileType() == null) ||
                                    fileClassification.getAssetTypeName() == null ||
                                    fileClassification.getDeployedImplementationType() == null)
                            {
                                missingReferenceData.add(fileClassification);
                            }

                            if ((lastFileCreationTime == null) || ((fileClassification.getCreationTime() != null) &&
                                    (lastFileCreationTime.before(fileClassification.getCreationTime()))))
                            {
                                lastFileCreationTime = fileClassification.getCreationTime();
                            }

                            if ((lastFileModificationTime == null) || ((fileClassification.getLastModifiedTime() != null) &&
                                    (lastFileModificationTime.before(fileClassification.getLastModifiedTime()))))
                            {
                                lastFileModificationTime = fileClassification.getLastModifiedTime();
                            }

                            if ((lastFileAccessTime == null) || ((fileClassification.getLastAccessedTime() != null) &&
                                    (lastFileAccessTime.before(fileClassification.getLastAccessedTime()))))
                            {
                                lastFileAccessTime = fileClassification.getLastAccessedTime();
                            }

                            updateValueCount(fileExtensionCounts, fileClassification.getFileExtension());
                            updateValueCount(fileNameCounts, fileClassification.getFileName());
                            updateValueCount(fileTypeCounts, fileClassification.getFileType());
                            updateValueCount(deployedImplementationTypeCounts, fileClassification.getDeployedImplementationType());
                            updateValueCount(assetTypeCounts, fileClassification.getAssetTypeName());

                            if (fileClassification.isCanRead())
                            {
                                canReadCount++;
                            }

                            if (fileClassification.isCanWrite())
                            {
                                canWriteCount++;
                            }

                            if (fileClassification.isCanExecute())
                            {
                                canExecuteCount++;
                            }
                        }
                        catch (IOException invalidFile)
                        {
                            final String methodName = "Profile Folder";

                            auditLog.logException(methodName, SurveyServiceAuditCode.FILE_IO_ERROR.getMessageDefinition(methodName,
                                                                                                                        nestedFile.getPath(),
                                                                                                                        invalidFile.getMessage()),
                                                  invalidFile);

                            InaccessibleFile inaccessibleFile = new InaccessibleFile();

                            inaccessibleFile.fileName = nestedFile.getPath();
                            inaccessibleFile.exceptionClassName = invalidFile.getClass().getName();
                            inaccessibleFile.exceptionMessage = invalidFile.getMessage();

                            inaccessibleFiles.add(inaccessibleFile);
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
     * This class is used in capturing details of files that returned an IOException when the surveyor tried to access its basic properties.
     */
    static class InaccessibleFile
    {
        String fileName;
        String exceptionClassName;
        String exceptionMessage;
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
