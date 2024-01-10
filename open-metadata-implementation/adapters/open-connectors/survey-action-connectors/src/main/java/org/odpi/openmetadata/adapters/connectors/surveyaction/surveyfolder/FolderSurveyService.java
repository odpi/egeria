/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.adapters.connectors.surveyaction.AuditableSurveyService;
import org.odpi.openmetadata.adapters.connectors.surveyaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyOpenMetadataStore;
import org.odpi.openmetadata.frameworks.surveyaction.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.surveyaction.properties.DataProfileAnnotation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * CSVSurveyService is a survey action service implementation for analysing CSV Files to
 * columns and profile the data in them.
 */
public class FolderSurveyService extends AuditableSurveyService
{
    private final PropertyHelper propertyHelper = new PropertyHelper();
    private       Connector      connector      = null;


    private final Map<String, Integer> fileExtensionCounts = new HashMap<>();
    private final Map<String, Integer> fileTypeCounts = new HashMap<>();
    private final Map<String, Integer> fileNameCounts = new HashMap<>();
    private final Map<String, Integer> assetTypeCounts = new HashMap<>();
    private final Map<String, Integer> deployedImplementationTypeCounts = new HashMap<>();
    private final Map<String, Integer> canReadCounts = new HashMap<>();
    private final Map<String, Integer> canWriteCounts = new HashMap<>();
    private final Map<String, Integer> canExecuteCounts = new HashMap<>();


    /**
     * Return the updated value count for this column.
     *
     * @param existingValueCount current value count
     * @param newFieldValue next field value to process
     */
    private void updateValueCount(Map<String, Integer> existingValueCount, boolean newFieldValue)
    {
        Integer existingCount = existingValueCount.get(String.valueOf(newFieldValue));

        if (existingCount == null)
        {
            existingValueCount.put(String.valueOf(newFieldValue), 1);
        }
        else
        {
            existingValueCount.put(String.valueOf(newFieldValue), existingCount + 1);
        }
    }


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
            else if (! propertyHelper.isTypeOf(assetUniverse, OpenMetadataType.FILE_FOLDER_TYPE_NAME))
            {
                super.throwWrongTypeOfAsset(assetUniverse.getGUID(),
                                            assetUniverse.getType().getTypeName(),
                                            OpenMetadataType.FILE_FOLDER_TYPE_NAME,
                                            methodName);
                return;
            }


            /*
             * The asset should have a special connector for files.  If the connector is wrong,
             * the cast will fail.
             */
            connector = assetStore.getConnectorToAsset();
            BasicFileStore assetConnector = (BasicFileStore)connector;

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
            profileFolder(openMetadataStore, rootFolder);

            DataProfileAnnotation dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile File Extensions");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count each file extension.");
            dataProfile.setValueCount(fileExtensionCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile File Names");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count each file name.");
            dataProfile.setValueCount(fileNameCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile File Types");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count each file type.");
            dataProfile.setValueCount(fileTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile Asset Types");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count each potential asset type if they were catalogued in open metadata.");
            dataProfile.setValueCount(assetTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile Deployed Implementation Types");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count each potential deployed implementation type if they were catalogued in open metadata.");
            dataProfile.setValueCount(deployedImplementationTypeCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile Readable Files");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count how many are readable.");
            dataProfile.setValueCount(canReadCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile Writable Files");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count how many are writable.");
            dataProfile.setValueCount(canWriteCounts);
            annotationStore.addAnnotation(dataProfile, null);

            dataProfile = new DataProfileAnnotation();

            dataProfile.setAnnotationType("Profile Executable Files");
            dataProfile.setAnnotationStatus(AnnotationStatus.NEW_ANNOTATION);
            dataProfile.setSummary("Iterate through files under a directory (folder) and count how many are executable.");
            dataProfile.setValueCount(canExecuteCounts);
            annotationStore.addAnnotation(dataProfile, null);
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
     * @param openMetadataStore open metadata client
     * @param fileFolder folder to profile
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem connecting to the open metadata repositories
     * @throws UserNotAuthorizedException insufficient access
     */
    private void profileFolder(SurveyOpenMetadataStore openMetadataStore,
                               File                    fileFolder) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        if ((fileFolder != null) && (fileFolder.listFiles() != null))
        {
            for (File nestedFile : Objects.requireNonNull(fileFolder.listFiles()))
            {
                if (nestedFile.isDirectory())
                {
                    profileFolder(openMetadataStore, nestedFile);
                }
                else
                {
                    FileClassifier fileClassifier = new FileClassifier(openMetadataStore, nestedFile);

                    updateValueCount(fileExtensionCounts, fileClassifier.getFileExtension());
                    updateValueCount(fileNameCounts, nestedFile.getName());
                    updateValueCount(fileTypeCounts, fileClassifier.getFileType());
                    updateValueCount(deployedImplementationTypeCounts, fileClassifier.getDeployedImplementationType());
                    updateValueCount(assetTypeCounts, fileClassifier.getAssetTypeName());

                    updateValueCount(canReadCounts, fileClassifier.isCanRead());
                    updateValueCount(canWriteCounts, fileClassifier.isCanWrite());
                    updateValueCount(canExecuteCounts, fileClassifier.isCanExecute());
                }
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
