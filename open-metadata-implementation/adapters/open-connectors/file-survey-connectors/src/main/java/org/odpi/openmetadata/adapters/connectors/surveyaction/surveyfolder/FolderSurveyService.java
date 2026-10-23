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
    /*
     * The folder being surveyed.  The measurements of the folders and files nested in it are named by their
     * path relative to it.
     */
    private File rootFolder = null;


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
     * @throws ConnectorCheckedException a problem within the discovery service.
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

            if (super.isFinalAnalysisStep())
            {
                return;
            }

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

            if (connector == null)
            {
                /*
                 * Checked before the cast, which succeeds on a null and leaves it to surface as a
                 * NullPointerException from getFile() below - an error naming BasicFolderConnector, which
                 * says nothing about the asset that is actually the problem.
                 */
                super.throwNoAssetConnector(assetStore, methodName);
            }

            BasicFolderConnector assetConnector = (BasicFolderConnector)connector;

            rootFolder = assetConnector.getFile();

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
            logRecord(methodName, SurveyServiceAuditCode.SURVEYING_FOLDER.getMessageDefinition(surveyActionServiceName,
                                                                                               rootFolder.getCanonicalPath(),
                                                                                               analysisLevel));

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());


            LogFileProgress logFileProgress = new LogFileProgress(auditLog, surveyActionServiceName);

            FolderProfile folderProfile = profileFolder(rootFolder, logFileProgress, deepFolderAnalysis, deepFileAnalysis);

            this.storeAnnotations(folderProfile.getProfileAnnotations(), annotationStore);

            /*
             * Stopping here records the folder's measurements and profiles without writing out the files that
             * could not be classified or accessed, or the inventory of file names.
             */
            if (super.isFinalAnalysisStep())
            {
                return;
            }

            annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_ACTIONS.getName());

            this.storeAnnotations(folderProfile.getActionAnnotations(annotationStore.getSurveyReportGUID()), annotationStore);

            /*
             * Stopping here records the requests for action without writing the inventory of file names.
             */
            if (super.isFinalAnalysisStep())
            {
                return;
            }

            annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

            this.storeAnnotations(folderProfile.getInventoryAnnotations(annotationStore.getSurveyReportGUID()), annotationStore);
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
     * Store the annotations produced by an analysis step, along with the link to any action target that an
     * annotation names.
     *
     * @param annotations annotations to store
     * @param annotationStore destination of the annotations
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     */
    private void storeAnnotations(List<Annotation> annotations,
                                  AnnotationStore  annotationStore) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        for (Annotation annotation : annotations)
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


    /**
     * Return the subject name for the measurements of a folder or file found in the survey: its path relative
     * to the surveyed folder, or null for the surveyed folder itself.
     * <br><br>
     * Every measurement in the survey is attached to the surveyed folder's asset, so without a subject their
     * qualified names differ only by the time they were set up - to the millisecond.  A deep survey sets up
     * the measurements of small or empty folders and files far faster than that, and the second annotation
     * with the same name is refused with OMAG-COMMON-409-001, failing the survey.
     *
     * @param file folder or file found in the survey
     * @return subject name or null
     */
    private String getSubjectName(File file)
    {
        if ((rootFolder == null) || (rootFolder.equals(file)))
        {
            return null;
        }

        return rootFolder.toPath().relativize(file.toPath()).toString().replace(File.separatorChar, '/');
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
                                                        getSubjectName(fileFolder),
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
                        FileClassifier     fileClassifier     = surveyContext.getFileClassifier(null, null, null);
                        FileClassification fileClassification = fileClassifier.classifyFile(nestedFile);

                        if (deepFileAnalysis)
                        {
                            FileStatsExtractor fileStatsExtractor = new FileStatsExtractor(nestedFile,
                                                                                           fileClassifier,
                                                                                           this,
                                                                                           getSubjectName(nestedFile));

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

                        logExceptionRecord(methodName, SurveyServiceAuditCode.FILE_IO_ERROR.getMessageDefinition(methodName,
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
        private final String   subjectName;
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

        /*
         * The latest file access time is deliberately not tracked: reading a file changes it, so it would make
         * every survey of an unchanged folder look different.
         */

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
         * @param subjectName folder's path relative to the surveyed folder, or null for the surveyed folder
         * @param surveyActionServiceConnector this connector
         * @param surveyActionServiceName connector's name
         * @param surveyAssetStore asset store
         */
        public FolderProfile(AuditLog                     auditLog,
                             String                       pathName,
                             String                       subjectName,
                             SurveyActionServiceConnector surveyActionServiceConnector,
                             String                       surveyActionServiceName,
                             SurveyAssetStore             surveyAssetStore)
        {
            this.auditLog                     = auditLog;
            this.pathName                     = pathName;
            this.subjectName                  = subjectName;
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
         * @throws PropertyServerException    problem connecting to the open metadata repositories
         * @throws UserNotAuthorizedException insufficient access
         */
        public Annotation getFolderAnnotation() throws PropertyServerException, UserNotAuthorizedException
        {
            ResourceMeasureAnnotationProperties resourceMeasureAnnotationProperties = new ResourceMeasureAnnotationProperties();

            setUpAnnotation(resourceMeasureAnnotationProperties, SurveyFolderAnnotationType.MEASUREMENTS, subjectName);

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

            resourceMeasureAnnotationProperties.setJsonProperties(surveyActionServiceConnector.getJSONProperties(fileDirectoryMeasurement));

            return new Annotation(resourceMeasureAnnotationProperties);
        }


        /**
         * Add the counts from a nested folder to the counts of this folder.  A value found in both folders
         * is counted in both.
         *
         * @param counts counts for this folder
         * @param nestedCounts counts from the nested folder
         */
        private void mergeCounts(Map<String, Integer> counts,
                                 Map<String, Integer> nestedCounts)
        {
            for (String value : nestedCounts.keySet())
            {
                counts.merge(value, nestedCounts.get(value), Integer::sum);
            }
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

            mergeCounts(fileExtensionCounts, nestedFolderProfile.fileExtensionCounts);
            mergeCounts(fileTypeCounts, nestedFolderProfile.fileTypeCounts);
            mergeCounts(fileNameCounts, nestedFolderProfile.fileNameCounts);
            mergeCounts(assetTypeCounts, nestedFolderProfile.assetTypeCounts);
            mergeCounts(deployedImplementationTypeCounts, nestedFolderProfile.deployedImplementationTypeCounts);
            missingReferenceData.addAll(nestedFolderProfile.missingReferenceData);
            inaccessibleFiles.addAll(nestedFolderProfile.inaccessibleFiles);
            savedAnnotations.addAll(nestedFolderProfile.savedAnnotations);
        }


        /**
         * Return the annotations for the Profiling Associated Resources analysis step: the profiles of the
         * files found, the measurements of this folder and the annotations saved from the deeper analysis of
         * nested folders and files (if requested).
         *
         * @return list of annotations
         * @throws PropertyServerException    problem connecting to the open metadata repositories
         * @throws UserNotAuthorizedException insufficient access
         */
        public List<Annotation> getProfileAnnotations() throws PropertyServerException,
                                                               UserNotAuthorizedException
        {
            List<Annotation> newAnnotations = new ArrayList<>();
            ResourceProfileAnnotationProperties dataProfile = new ResourceProfileAnnotationProperties();

            setUpAnnotation(dataProfile, SurveyFolderAnnotationType.PROFILE_FILE_EXTENSIONS);
            dataProfile.setValueCount(fileExtensionCounts);
            newAnnotations.add(new Annotation(dataProfile));

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

            /*
             * The measurements of nested folders and files, gathered when the analysis level asks for them.
             */
            newAnnotations.addAll(savedAnnotations);

            return newAnnotations;
        }


        /**
         * Return the annotations for the Produce Actions analysis step: requests for action that list the
         * files that could not be classified from the reference data, or could not be accessed.  The lists are
         * written to CSV files that are catalogued as the action targets of the requests.
         *
         * @param surveyReportGUID unique identifier of the survey report
         * @return list of annotations
         * @throws InvalidParameterException problem creating CSV file asset
         * @throws PropertyServerException repository problem creating CSV file asset
         * @throws UserNotAuthorizedException authorization problem creating CSV file asset
         * @throws IOException problem writing file
         * @throws ConnectorCheckedException exception thrown if connector is no longer active
         */
        public List<Annotation> getActionAnnotations(String surveyReportGUID) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException,
                                                                                     IOException,
                                                                                     ConnectorCheckedException
        {
            List<Annotation> newAnnotations = new ArrayList<>();

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

            return newAnnotations;
        }


        /**
         * Return the annotations for the Produce Inventory analysis step: the counts of each file name, written
         * to an external log file.
         *
         * @param surveyReportGUID unique identifier of the survey report
         * @return list of annotations
         * @throws InvalidParameterException problem creating the log file asset
         * @throws PropertyServerException repository problem creating the log file asset
         * @throws UserNotAuthorizedException authorization problem creating the log file asset
         * @throws IOException problem writing the log file
         * @throws ConnectorCheckedException exception thrown if connector is no longer active
         */
        public List<Annotation> getInventoryAnnotations(String surveyReportGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException,
                                                                                        IOException,
                                                                                        ConnectorCheckedException
        {
            List<Annotation> newAnnotations = new ArrayList<>();

            ResourceProfileLogAnnotationProperties dataProfileLog = writeNameCountInventory(SurveyFolderAnnotationType.PROFILE_FILE_NAMES,
                                                                                            "fileNameCounts",
                                                                                            fileNameCounts,
                                                                                            surveyReportGUID);
            newAnnotations.add(new Annotation(dataProfileLog));

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
                        fileClassification.getCanonicalPathName() +
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
