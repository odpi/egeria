/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.adapters.connectors.surveyaction.AuditableSurveyService;
import org.odpi.openmetadata.adapters.connectors.surveyaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.properties.DataSourcePhysicalStatusAnnotation;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * FileSurveyService is a survey action service implementation for analysing a file to
 * columns and profile the data in them.
 */
public class FileSurveyService extends AuditableSurveyService
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

            /*
             * Before performing any real work, check the type of the asset.
             */
            AssetUniverse assetUniverse = assetStore.getAssetProperties();

            if (assetUniverse == null)
            {
                super.throwNoAsset(assetStore.getAssetGUID(), methodName);
                return;
            }
            else if (! propertyHelper.isTypeOf(assetUniverse, OpenMetadataType.DATA_FILE_TYPE_NAME))
            {
                super.throwWrongTypeOfAsset(assetUniverse.getGUID(),
                                            assetUniverse.getType().getTypeName(),
                                            OpenMetadataType.DATA_FILE_TYPE_NAME,
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

            FileClassifier fileClassifier = new FileClassifier(surveyContext.getOpenMetadataStore(), file);

            DataSourcePhysicalStatusAnnotation measurementAnnotation = new DataSourcePhysicalStatusAnnotation();

            measurementAnnotation.setAnnotationType("ExtractDataStoreProperties");
            measurementAnnotation.setSummary("Extract properties from the file.");
            measurementAnnotation.setModifiedTime(new Date(file.lastModified()));
            measurementAnnotation.setSize(Integer.getInteger(String.valueOf(file.length())));

            Map<String, String> dataSourceProperties = new HashMap<>();

            dataSourceProperties.put(OpenMetadataProperty.FILE_NAME.name, file.getName());
            dataSourceProperties.put(OpenMetadataProperty.PATH_NAME.name, file.getAbsolutePath());
            dataSourceProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, fileClassifier.getFileExtension());
            dataSourceProperties.put(OpenMetadataProperty.FILE_TYPE.name, fileClassifier.getFileType());
            dataSourceProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, fileClassifier.getDeployedImplementationType());
            dataSourceProperties.put("assetTypeName", fileClassifier.getAssetTypeName());
            dataSourceProperties.put("canRead", Boolean.toString(fileClassifier.isCanRead()));
            dataSourceProperties.put("canWrite", Boolean.toString(fileClassifier.isCanWrite()));
            dataSourceProperties.put("canExecute", Boolean.toString(fileClassifier.isCanExecute()));

            measurementAnnotation.setDataSourceProperties(dataSourceProperties);

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
