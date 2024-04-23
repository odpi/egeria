/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourcePhysicalStatusAnnotation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * FileSurveyService is a survey action service implementation for extracting properties about a file.
 */
public class FileSurveyService extends SurveyActionServiceConnector
{
    private final PropertyHelper propertyHelper = new PropertyHelper();

    private Connector connector = null;


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
            else if (! propertyHelper.isTypeOf(assetUniverse, OpenMetadataType.DATA_FILE.typeName))
            {
                super.throwWrongTypeOfAsset(assetUniverse.getGUID(),
                                            assetUniverse.getType().getTypeName(),
                                            OpenMetadataType.DATA_FILE.typeName,
                                            methodName);
                return;
            }

            /*
             * The asset should have a special connector for files.  If the connector is wrong,
             * the cast will fail.
             */
            connector = assetStore.getConnectorToAsset();
            BasicFileStore assetConnector = (BasicFileStore)connector;

            File file = assetConnector.getFile();

            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            FileClassifier fileClassifier = surveyContext.getFileClassifier();
            FileClassification fileClassification = fileClassifier.classifyFile(file);

            ResourcePhysicalStatusAnnotation measurementAnnotation = new ResourcePhysicalStatusAnnotation();

            measurementAnnotation.setAnnotationType(SurveyFileAnnotationType.MEASUREMENTS.getName());
            measurementAnnotation.setSummary(SurveyFileAnnotationType.MEASUREMENTS.getSummary());
            measurementAnnotation.setExplanation(SurveyFileAnnotationType.MEASUREMENTS.getExplanation());
            measurementAnnotation.setCreateTime(new Date(attr.creationTime().toMillis()));
            measurementAnnotation.setModifiedTime(new Date(file.lastModified()));
            measurementAnnotation.setLastAccessedTime(new Date(attr.lastAccessTime().toMillis()));
            measurementAnnotation.setSize(assetConnector.getFileLength());

            Map<String, String> dataSourceProperties = new HashMap<>();

            dataSourceProperties.put(FileMetric.FILE_NAME.name, fileClassification.getFileName());
            dataSourceProperties.put(FileMetric.PATH_NAME.name, fileClassification.getPathName());
            dataSourceProperties.put(FileMetric.FILE_EXTENSION.name, fileClassification.getFileExtension());
            dataSourceProperties.put(FileMetric.FILE_TYPE.name, fileClassification.getFileType());
            dataSourceProperties.put(FileMetric.DEPLOYED_IMPLEMENTATION_TYPE.name, fileClassification.getDeployedImplementationType());
            dataSourceProperties.put(FileMetric.ENCODING.name, fileClassification.getEncoding());
            dataSourceProperties.put(FileMetric.ASSET_TYPE_NAME.name, fileClassification.getAssetTypeName());
            dataSourceProperties.put(FileMetric.CAN_READ.name, Boolean.toString(fileClassification.isCanRead()));
            dataSourceProperties.put(FileMetric.CAN_WRITE.name, Boolean.toString(fileClassification.isCanWrite()));
            dataSourceProperties.put(FileMetric.CAN_EXECUTE.name, Boolean.toString(fileClassification.isCanExecute()));
            dataSourceProperties.put(FileMetric.IS_SYM_LINK.name, Boolean.toString(fileClassification.isSymLink()));
            dataSourceProperties.put(FileMetric.IS_HIDDEN.name, Boolean.toString(fileClassification.isHidden()));

            measurementAnnotation.setResourceProperties(dataSourceProperties);

            annotationStore.addAnnotation(measurementAnnotation, surveyContext.getAssetGUID());
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
