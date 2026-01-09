/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.FolderRequestParameter;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFolderAnnotationType;
import org.odpi.openmetadata.adapters.connectors.surveyaction.extractors.FileStatsExtractor;
import org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc.SurveyServiceAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.FileDirectoryMeasurement;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.FileDirectoryMetric;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * FolderSurveyService is a survey action service implementation for analysing the files nested in a folder.
 * The depth of the survey is controlled by the analysisLevel request parameter.
 * <ul>
 *     <li>The default value is 'TOP_LEVEL_ONLY' which produces summary statistics for the top-level directory only.</li>
 *     <li>If it is set to 'ALL_FOLDERS' then there are statistics created for the top level directory, and all subdirectories.</li>
 *     <li>If it set to 'TOP_LEVEL_AND_FILES' then statistics are created for the top-level directory and all files that are encountered.</li>
 *     <li>Finally, if it is set to 'ALL_FOLDERS_AND_FILES' then statistics are created for all directories and files encountered.", "string", "myFile.csv".</li>
 * </ul>
 */
public class FolderSurveyService extends SurveyActionServiceConnector
{
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
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            AnnotationStore         annotationStore = getSurveyContext().getAnnotationStore();
            SurveyAssetStore        assetStore      = surveyContext.getAssetStore();
            OpenMetadataRootElement assetElement    = assetStore.getAssetProperties();

            performCheckAssetAnalysisStep(OpenMetadataType.FILE_FOLDER.typeName);

            boolean deepFolderAnalysis = false;
            boolean deepFileAnalysis   = false;
            String  analysisLevel = "TOP_LEVEL_ONLY";

            if ((surveyContext.getRequestParameters() != null) &&
                (surveyContext.getRequestParameters().get(FolderRequestParameter.ANALYSIS_LEVEL.getName()) != null))
            {
                analysisLevel = surveyContext.getRequestParameters().get(FolderRequestParameter.ANALYSIS_LEVEL.getName());

                if ("ALL_FOLDERS".equals(analysisLevel))
                {
                    deepFolderAnalysis = true;
                }
                else if ("TOP_LEVEL_AND_FILES".equals(analysisLevel))
                {
                    deepFileAnalysis = true;
                }
                else if ("ALL_FOLDERS_AND_FILES".equals(analysisLevel))
                {
                    deepFolderAnalysis = true;
                    deepFileAnalysis   = true;
                }
                else
                {
                    analysisLevel = "TOP_LEVEL_ONLY";
                }
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
                throwWrongTypeOfResource(assetElement.getElementHeader().getGUID(),
                                         assetElement.getElementHeader().getType().getTypeName(),
                                         rootFolder.getName(),
                                         "file",
                                         "directory (folder)",
                                         surveyActionServiceName,
                                         methodName);
            }

            if (! rootFolder.exists())
            {
                throwMissingResource(assetElement.getElementHeader().getGUID(),
                                     assetElement.getElementHeader().getType().getTypeName(),
                                     rootFolder.getName(),
                                     methodName);
            }

            /*
             * Scan the folder (and sub-folders) and count up its contents
             */
            auditLog.logMessage(methodName, SurveyServiceAuditCode.SURVEYING_FOLDER.getMessageDefinition(surveyActionServiceName,
                                                                                                         rootFolder.getCanonicalPath(),
                                                                                                         analysisLevel));

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());


            LogFileProgress logFileProgress = new LogFileProgress(auditLog, surveyActionServiceName);

            FolderProfile folderProfile = profileFolder(rootFolder, logFileProgress, deepFolderAnalysis, deepFileAnalysis);

            List<Annotation> annotations = folderProfile.getAnnotations(annotationStore.getSurveyReportGUID());

            if (annotations != null)
            {
                for (Annotation annotation : annotations)
                {
                    if (AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName().equals(annotation.annotationProperties.getAnalysisStep()))
                    {
                        String annotationGUID = annotationStore.addAnnotation(annotation.annotationProperties, null);

                        if (annotation.actionTargetGUID != null)
                        {
                            RequestForActionTargetProperties requestForActionTargetProperties = new RequestForActionTargetProperties();

                            requestForActionTargetProperties.setActionTargetName(annotation.actionTargetName);

                            annotationStore.linkRequestForActionTarget(annotationGUID,
                                                                       annotation.actionTargetGUID,
                                                                       requestForActionTargetProperties);
                        }
                    }
                }

                annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_ACTIONS.getName());

                for (Annotation annotation : annotations)
                {
                    if (AnalysisStep.PRODUCE_ACTIONS.getName().equals(annotation.annotationProperties.getAnalysisStep()))
                    {
                        String annotationGUID = annotationStore.addAnnotation(annotation.annotationProperties, null);

                        if (annotation.actionTargetGUID != null)
                        {
                            RequestForActionTargetProperties requestForActionTargetProperties = new RequestForActionTargetProperties();

                            requestForActionTargetProperties.setActionTargetName(annotation.actionTargetName);

                            annotationStore.linkRequestForActionTarget(annotationGUID,
                                                                       annotation.actionTargetGUID,
                                                                       requestForActionTargetProperties);
                        }
                    }
                }

                annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

                for (Annotation annotation : annotations)
                {
                    if (AnalysisStep.PRODUCE_INVENTORY.getName().equals(annotation.annotationProperties.getAnalysisStep()))
                    {
                        String annotationGUID = annotationStore.addAnnotation(annotation.annotationProperties, null);

                        if (annotation.actionTargetGUID != null)
                        {
                            RequestForActionTargetProperties requestForActionTargetProperties = new RequestForActionTargetProperties();

                            requestForActionTargetProperties.setActionTargetName(annotation.actionTargetName);

                            annotationStore.linkRequestForActionTarget(annotationGUID,
                                                                       annotation.actionTargetGUID,
                                                                       requestForActionTargetProperties);
                        }
                    }
                }
            }
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
     * Profile a single folder.
     *
     * @param fileFolder folder to profile
     * @param logFileProgress progressBar
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    private FolderProfile profileFolder(File                    fileFolder,
                                        LogFileProgress         logFileProgress,
                                        boolean                 deepFolderAnalysis,
                                        boolean                 deepFileAnalysis) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException,
                                                                                         ConnectorCheckedException
    {
        FolderProfile folderProfile = new FolderProfile(auditLog,
                                                        fileFolder.getPath(),
                                                        this,
                                                        surveyActionServiceName,
                                                        surveyContext.getAssetStore());

        if (fileFolder.listFiles() != null)
        {
            for (File nestedFile : Objects.requireNonNull(fileFolder.listFiles()))
            {
                if (nestedFile.isDirectory())
                {
                    folderProfile.folderCount++;
                    logFileProgress.logFilesProcessed();

                    if (!FileUtils.isSymlink(nestedFile))
                    {
                        FolderProfile nestedFolderProfile = profileFolder(nestedFile,
                                                                          logFileProgress,
                                                                          deepFolderAnalysis,
                                                                          deepFileAnalysis);

                        if (deepFolderAnalysis)
                        {
                            folderProfile.saveAnnotation(nestedFolderProfile.getFolderAnnotation());
                        }

                        folderProfile.addNestedProfile(nestedFolderProfile);
                    }
                }
                else if (nestedFile.isFile())
                {
                    try
                    {
                        FileClassifier     fileClassifier     = surveyContext.getFileClassifier();
                        FileClassification fileClassification = fileClassifier.classifyFile(nestedFile);

                        if (deepFileAnalysis)
                        {
                            FileStatsExtractor fileStatsExtractor = new FileStatsExtractor(nestedFile,
                                                                                           fileClassifier,
                                                                                           this);

                            folderProfile.saveAnnotation(new Annotation(fileStatsExtractor.getAnnotation()));
                        }

                        folderProfile.fileCount++;
                        logFileProgress.logFilesProcessed();

                        folderProfile.totalSize = folderProfile.totalSize + fileClassification.getFileSize();

                        if ((fileClassification.getFileType() == null) ||
                             fileClassification.getAssetTypeName() == null ||
                             fileClassification.getDeployedImplementationType() == null)
                        {
                            folderProfile.missingReferenceData.add(fileClassification);
                        }

                        if ((folderProfile.lastFileCreationTime == null) || ((fileClassification.getCreationTime() != null) &&
                                (folderProfile.lastFileCreationTime.before(fileClassification.getCreationTime()))))
                        {
                            folderProfile.lastFileCreationTime = fileClassification.getCreationTime();
                        }

                        if ((folderProfile.lastFileModificationTime == null) || ((fileClassification.getLastModifiedTime() != null) &&
                                (folderProfile.lastFileModificationTime.before(fileClassification.getLastModifiedTime()))))
                        {
                            folderProfile.lastFileModificationTime = fileClassification.getLastModifiedTime();
                        }

                        if ((folderProfile.lastFileAccessTime == null) || ((fileClassification.getLastAccessedTime() != null) &&
                                (folderProfile.lastFileAccessTime.before(fileClassification.getLastAccessedTime()))))
                        {
                            folderProfile.lastFileAccessTime = fileClassification.getLastAccessedTime();
                        }

                        updateValueCount(folderProfile.fileExtensionCounts, fileClassification.getFileExtension());
                        updateValueCount(folderProfile.fileNameCounts, fileClassification.getFileName());
                        updateValueCount(folderProfile.fileTypeCounts, fileClassification.getFileType());
                        updateValueCount(folderProfile.deployedImplementationTypeCounts, fileClassification.getDeployedImplementationType());
                        updateValueCount(folderProfile.assetTypeCounts, fileClassification.getAssetTypeName());

                        if (fileClassification.isCanRead())
                        {
                            folderProfile.canReadCount++;
                        }

                        if (fileClassification.isCanWrite())
                        {
                            folderProfile.canWriteCount++;
                        }

                        if (fileClassification.isCanExecute())
                        {
                            folderProfile.canExecuteCount++;
                        }

                        if (fileClassification.isSymLink())
                        {
                            folderProfile.isSymLink++;
                        }

                        if (fileClassification.isHidden())
                        {
                            folderProfile.isHidden++;
                        }
                    }
                    catch (IOException invalidFile)
                    {
                        final String methodName = "Profile Folder";

                        auditLog.logException(methodName, SurveyServiceAuditCode.FILE_IO_ERROR.getMessageDefinition(methodName,
                                                                                                                    nestedFile.getPath(),
                                                                                                                    invalidFile.getMessage()),
                                              invalidFile);

                        folderProfile.saveInaccessibleFile(nestedFile.getPath(),
                                                           invalidFile.getClass().getName(),
                                                           invalidFile.getMessage());
                    }
                }
            }
        }

        return folderProfile;
    }


    /**
     * FolderProfile gathers information about a directory and its subdirectories.
     */
    private class FolderProfile
    {
        private final AuditLog auditLog;
        private final String   pathName;
        private final SurveyActionServiceConnector surveyActionServiceConnector;
        private final String                       surveyActionServiceName;
        private final SurveyAssetStore             surveyAssetStore;

        private long fileCount       = 0L;
        private long folderCount     = 0L;
        private long canReadCount    = 0L;
        private long canWriteCount   = 0L;
        private long canExecuteCount = 0L;
        private long isSymLink       = 0L;
        private long isHidden        = 0L;

        private double totalSize     = 0D;

        private Date lastFileCreationTime     = null;
        private Date lastFileModificationTime = null;
        private Date lastFileAccessTime       = null;

        private final Map<String, Integer>     fileExtensionCounts              = new HashMap<>();
        private final Map<String, Integer>     fileTypeCounts                   = new HashMap<>();
        private final Map<String, Integer>     fileNameCounts                   = new HashMap<>();
        private final Map<String, Integer>     assetTypeCounts                  = new HashMap<>();
        private final Map<String, Integer>     deployedImplementationTypeCounts = new HashMap<>();
        private final List<FileClassification> missingReferenceData             = new ArrayList<>();
        private final List<InaccessibleFile>   inaccessibleFiles                = new ArrayList<>();

        private final List<Annotation> savedAnnotations = new ArrayList<>();


        /**
         * Set up the folder profile.
         *
         * @param auditLog logging destination
         * @param pathName folder name
         * @param surveyActionServiceConnector this connector
         * @param surveyActionServiceName connector's name
         * @param surveyAssetStore asset store
         */
        public FolderProfile(AuditLog                     auditLog,
                             String                       pathName,
                             SurveyActionServiceConnector surveyActionServiceConnector,
                             String                       surveyActionServiceName,
                             SurveyAssetStore             surveyAssetStore)
        {
            this.auditLog                     = auditLog;
            this.pathName                     = pathName;
            this.surveyActionServiceConnector = surveyActionServiceConnector;
            this.surveyActionServiceName      = surveyActionServiceName;
            this.surveyAssetStore             = surveyAssetStore;
        }


        /**
         * Capture details of inaccessible files.
         *
         * @param fileName name of the file
         * @param exceptionClassName exception when accessing its basic attributes
         * @param exceptionMessage message from exception
         */
        public void saveInaccessibleFile(String fileName,
                                         String exceptionClassName,
                                         String exceptionMessage)
        {
            InaccessibleFile  inaccessibleFile = new InaccessibleFile();

            inaccessibleFile.fileName = fileName;
            inaccessibleFile.exceptionClassName = exceptionClassName;
            inaccessibleFile.exceptionMessage = exceptionMessage;

            inaccessibleFiles.add(inaccessibleFile);
        }


        /**
         * Save an annotation that is part of the folder profile.
         *
         * @param annotation annotation to save
         */
        public void saveAnnotation(Annotation annotation)
        {
            savedAnnotations.add(annotation);
        }


        /**
         * Create the summary stats for this folder.
         *
         * @return annotation
         */
        public Annotation getFolderAnnotation() throws PropertyServerException
        {
            ResourceMeasureAnnotationProperties resourceMeasureAnnotationProperties = new ResourceMeasureAnnotationProperties();

            setUpAnnotation(resourceMeasureAnnotationProperties, SurveyFolderAnnotationType.MEASUREMENTS);

            Map<String, String> fileCountProperties = new HashMap<>();
            fileCountProperties.put(FileDirectoryMetric.FILE_COUNT.getDisplayName(), Long.toString(fileCount));
            fileCountProperties.put(FileDirectoryMetric.TOTAL_FILE_SIZE.getDisplayName(), Double.toString(totalSize));
            fileCountProperties.put(FileDirectoryMetric.SUB_DIRECTORY_COUNT.getDisplayName(), Long.toString(folderCount));
            fileCountProperties.put(FileDirectoryMetric.READABLE_FILE_COUNT.getDisplayName(), Long.toString(canReadCount));
            fileCountProperties.put(FileDirectoryMetric.WRITEABLE_FILE_COUNT.getDisplayName(), Long.toString(canWriteCount));
            fileCountProperties.put(FileDirectoryMetric.EXECUTABLE_FILE_COUNT.getDisplayName(), Long.toString(canExecuteCount));
            fileCountProperties.put(FileDirectoryMetric.SYM_LINK_COUNT.getDisplayName(), Long.toString(isSymLink));
            fileCountProperties.put(FileDirectoryMetric.HIDDEN_FILE_COUNT.getDisplayName(), Long.toString(isHidden));
            fileCountProperties.put(FileDirectoryMetric.FILE_NAME_COUNT.getDisplayName(), Integer.toString(fileNameCounts.size()));
            fileCountProperties.put(FileDirectoryMetric.FILE_EXTENSION_COUNT.getDisplayName(), Integer.toString(fileExtensionCounts.size()));
            fileCountProperties.put(FileDirectoryMetric.FILE_TYPE_COUNT.getDisplayName(), Integer.toString(fileTypeCounts.size()));
            fileCountProperties.put(FileDirectoryMetric.ASSET_TYPE_COUNT.getDisplayName(), Integer.toString(assetTypeCounts.size()));
            fileCountProperties.put(FileDirectoryMetric.DEPLOYED_IMPL_TYPE_COUNT.getDisplayName(), Integer.toString(deployedImplementationTypeCounts.size()));
            fileCountProperties.put(FileDirectoryMetric.UNCLASSIFIED_FILE_COUNT.getDisplayName(), Integer.toString(missingReferenceData.size()));
            fileCountProperties.put(FileDirectoryMetric.INACCESSIBLE_FILE_COUNT.getDisplayName(), Integer.toString(inaccessibleFiles.size()));
            if (lastFileCreationTime != null)
            {
                fileCountProperties.put(FileDirectoryMetric.LAST_FILE_CREATION_TIME.getDisplayName(), lastFileCreationTime.toString());
            }
            if (lastFileModificationTime != null)
            {
                fileCountProperties.put(FileDirectoryMetric.LAST_FILE_MODIFIED_TIME.getDisplayName(), lastFileModificationTime.toString());
            }
            if (lastFileAccessTime != null)
            {
                fileCountProperties.put(FileDirectoryMetric.LAST_FILE_ACCESSED_TIME.getDisplayName(), lastFileAccessTime.toString());
            }

            resourceMeasureAnnotationProperties.setResourceProperties(fileCountProperties);

            FileDirectoryMeasurement fileDirectoryMeasurement = new FileDirectoryMeasurement();

            fileDirectoryMeasurement.setDirectoryName(pathName);
            fileDirectoryMeasurement.setFileCount(fileCount);
            fileDirectoryMeasurement.setTotalFileSize(totalSize);
            fileDirectoryMeasurement.setSubDirectoryCount(folderCount);
            fileDirectoryMeasurement.setReadableFileCount(canReadCount);
            fileDirectoryMeasurement.setWriteableFileCount(canWriteCount);
            fileDirectoryMeasurement.setExecutableFileCount(canExecuteCount);
            fileDirectoryMeasurement.setSymLinkFileCount(isSymLink);
            fileDirectoryMeasurement.setHiddenFileCount(isHidden);
            fileDirectoryMeasurement.setFileNameCount(fileNameCounts.size());
            fileDirectoryMeasurement.setFileExtensionCount(fileExtensionCounts.size());
            fileDirectoryMeasurement.setFileTypeCount(fileTypeCounts.size());
            fileDirectoryMeasurement.setAssetTypeCount(assetTypeCounts.size());
            fileDirectoryMeasurement.setDeployedImplementationTypeCount(deployedImplementationTypeCounts.size());
            fileDirectoryMeasurement.setUnclassifiedFileCount(missingReferenceData.size());
            fileDirectoryMeasurement.setInaccessibleFileCount(inaccessibleFiles.size());
            fileDirectoryMeasurement.setLastFileCreationTime(lastFileCreationTime);
            fileDirectoryMeasurement.setLastFileModificationTime(lastFileModificationTime);
            fileDirectoryMeasurement.setLastFileAccessedTime(lastFileAccessTime);

            resourceMeasureAnnotationProperties.setJsonProperties(surveyActionServiceConnector.getJSONProperties(fileDirectoryMeasurement));

            return new Annotation(resourceMeasureAnnotationProperties);
        }


        /**
         * Incorporate the stats from the nested directory into the parent directory
         *
         * @param nestedFolderProfile nested profile
         */
        public void addNestedProfile(FolderProfile nestedFolderProfile)
        {
            fileCount       = fileCount + nestedFolderProfile.fileCount;
            folderCount     = folderCount + nestedFolderProfile.folderCount;
            canReadCount    = canReadCount + nestedFolderProfile.canReadCount;
            canWriteCount   = canWriteCount + nestedFolderProfile.canWriteCount;
            canExecuteCount = canExecuteCount + nestedFolderProfile.canExecuteCount;
            isSymLink       = isSymLink + nestedFolderProfile.isSymLink;
            isHidden        = isHidden + nestedFolderProfile.isHidden;
            totalSize       = totalSize + nestedFolderProfile.totalSize;

            if ((lastFileCreationTime == null) || ((nestedFolderProfile.lastFileCreationTime != null) &&
                    (lastFileCreationTime.before(nestedFolderProfile.lastFileCreationTime))))
            {
                lastFileCreationTime = nestedFolderProfile.lastFileCreationTime;
            }

            if ((lastFileModificationTime == null) || ((nestedFolderProfile.lastFileModificationTime != null) &&
                    (lastFileModificationTime.before(nestedFolderProfile.lastFileModificationTime))))
            {
                lastFileModificationTime = nestedFolderProfile.lastFileModificationTime;
            }

            if ((lastFileAccessTime == null) || ((nestedFolderProfile.lastFileAccessTime != null) &&
                    (lastFileAccessTime.before(nestedFolderProfile.lastFileAccessTime))))
            {
                lastFileAccessTime = nestedFolderProfile.lastFileAccessTime;
            }

            fileExtensionCounts.putAll(nestedFolderProfile.fileExtensionCounts);
            fileTypeCounts.putAll(nestedFolderProfile.fileTypeCounts);
            fileNameCounts.putAll(nestedFolderProfile.fileNameCounts);
            assetTypeCounts.putAll(nestedFolderProfile.assetTypeCounts);
            deployedImplementationTypeCounts.putAll(nestedFolderProfile.deployedImplementationTypeCounts);
            missingReferenceData.addAll(nestedFolderProfile.missingReferenceData);
            inaccessibleFiles.addAll(nestedFolderProfile.inaccessibleFiles);
            savedAnnotations.addAll(nestedFolderProfile.savedAnnotations);
        }


        /**
         * Return the annotations from this survey
         *
         * @param surveyReportGUID unique identifier of the survey report
         * @return list of annotations
         */
        public List<Annotation> getAnnotations(String surveyReportGUID) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException,
                                                                                         IOException,
                                                                                         ConnectorCheckedException
        {
            List<Annotation> newAnnotations = new ArrayList<>();
            ResourceProfileAnnotationProperties dataProfile = new ResourceProfileAnnotationProperties();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_FILE_EXTENSIONS);
            dataProfile.setValueCount(fileExtensionCounts);
            newAnnotations.add(new Annotation(dataProfile));

            ResourceProfileLogAnnotationProperties dataProfileLog = writeNameCountInventory(SurveyFolderAnnotationType.PROFILE_FILE_NAMES,
                                                                                            "fileNameCounts",
                                                                                            fileNameCounts,
                                                                                            surveyReportGUID);
            newAnnotations.add(new Annotation(dataProfileLog));

            if (! missingReferenceData.isEmpty())
            {
                RequestForActionProperties requestForActionAnnotation = new RequestForActionProperties();

                setUpAnnotation(requestForActionAnnotation, SurveyFolderAnnotationType.MISSING_REF_DATA);

                Annotation annotation = new Annotation(requestForActionAnnotation);
                annotation.actionTargetGUID = setUpMissingRefDateExternalLogFile(surveyReportGUID, missingReferenceData);
                newAnnotations.add(annotation);
            }

            if (! inaccessibleFiles.isEmpty())
            {
                RequestForActionProperties requestForActionAnnotation = new RequestForActionProperties();

                setUpAnnotation(requestForActionAnnotation, SurveyFolderAnnotationType.INACCESSIBLE_FILES);
                Annotation annotation = new Annotation(requestForActionAnnotation);
                annotation.actionTargetGUID = setUpInaccessibleFilesExternalLogFile(surveyReportGUID, inaccessibleFiles);
                newAnnotations.add(annotation);
            }

            dataProfile = new ResourceProfileAnnotationProperties();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_FILE_TYPES);
            dataProfile.setValueCount(fileTypeCounts);
            newAnnotations.add(new Annotation(dataProfile));

            dataProfile = new ResourceProfileAnnotationProperties();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_ASSET_TYPES);
            dataProfile.setValueCount(assetTypeCounts);
            newAnnotations.add(new Annotation(dataProfile));

            dataProfile = new ResourceProfileAnnotationProperties();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_DEP_IMPL_TYPES);
            dataProfile.setValueCount(deployedImplementationTypeCounts);
            newAnnotations.add(new Annotation(dataProfile));


            newAnnotations.add(getFolderAnnotation());

            return newAnnotations;
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

            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setIsOwnAnchor(true);
            newElementOptions.setParentGUID(surveyContext.getConnectorGUID());
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName);

            String assetGUID = surveyAssetStore.addCSVFileToCatalog("Missing reference data for survey report " + surveyReportGUID,
                                                                    "Shows the files that could not be correctly classified from the reference data.",
                                                                    logFile.getCanonicalPath(),
                                                                    null,
                                                                    ',',
                                                                    '"',
                                                                    surveyContext.getEgeriaRelease(),
                                                                    csvFileConnectorTypeGUID,
                                                                    newElementOptions);


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
            final String methodName = "setUpInaccessibleFilesExternalLogFile";

            String           logFileName = "surveys/report-" + surveyReportGUID + "-inaccessibleFiles.csv";
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

            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setIsOwnAnchor(true);
            newElementOptions.setParentGUID(surveyContext.getConnectorGUID());
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName);

            String assetGUID = surveyAssetStore.addCSVFileToCatalog("Inaccessible files detected by " + surveyReportGUID,
                                                                    "Shows the files that could not be accessed.",
                                                                    logFile.getCanonicalPath(),
                                                                    null,
                                                                    ',',
                                                                    '"',
                                                                    surveyContext.getEgeriaRelease(),
                                                                    csvFileConnectorTypeGUID,
                                                                    newElementOptions);


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
     * Description of an annotation and its relationships
     */
    static class Annotation
    {
        AnnotationProperties annotationProperties;
        String               actionTargetName = null;
        String               actionTargetGUID = null;

        public Annotation(AnnotationProperties annotationProperties)
        {
            this.annotationProperties = annotationProperties;
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        if (connector != null)
        {
            connector.disconnect();
        }

        super.disconnect();
    }
}
