/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.adapters.connectors.surveyaction.extractors.FileStatsExtractor;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;

import java.io.File;


/**
 * FileSurveyService is a survey action service implementation for extracting properties about a file.
 */
public class FileSurveyService extends SurveyActionServiceConnector
{
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
            AnnotationStore          annotationStore   = surveyContext.getAnnotationStore();

            /*
             * The asset should have a special connector for files.  If the connector is wrong,
             * the cast will fail.
             */
            connector = performCheckAssetAnalysisStep(BasicFileStore.class, OpenMetadataType.DATA_FILE.typeName);
            BasicFileStore assetConnector = (BasicFileStore)connector;

            File file = assetConnector.getFile();

            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            FileStatsExtractor fileStatsExtractor = new FileStatsExtractor(file,
                                                                           surveyContext.getFileClassifier(),
                                                                           this);

            Annotation measurementAnnotation = fileStatsExtractor.getAnnotation();

            if (measurementAnnotation != null)
            {
                annotationStore.addAnnotation(measurementAnnotation, surveyContext.getAssetGUID());
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
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
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
