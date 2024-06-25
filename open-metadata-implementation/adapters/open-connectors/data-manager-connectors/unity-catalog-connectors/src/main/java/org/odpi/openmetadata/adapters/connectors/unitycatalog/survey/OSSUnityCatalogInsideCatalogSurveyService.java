/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogSurveyRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;

/**
 * Survey service that analyses the contents of a catalog within OSS Unity Catalog.
 */
public class OSSUnityCatalogInsideCatalogSurveyService extends SurveyActionServiceConnector
{
    final static PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * This is the point after which the processing stops.  The default is PROFILE - which
     * is the last analysis step.  It can be changed through configuration properties or
     * analysis properties passed when this discovery service is called.
     */
    private String finalAnalysisStep = AnalysisStep.PROFILE_DATA.getName();


    /**
     * Indicates that the survey service is completely configured and can begin processing.
     * This is where the function of the discovery service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        if (connectionProperties.getConfigurationProperties() != null)
        {
            Object finalAnalysisStepPropertyObject = connectionProperties.getConfigurationProperties().get(UnityCatalogSurveyRequestParameter.FINAL_ANALYSIS_STEP.getName());

            if (finalAnalysisStepPropertyObject != null)
            {
                String finalAnalysisProperty = finalAnalysisStepPropertyObject.toString();

                if ((finalAnalysisProperty.equals(AnalysisStep.MEASURE_RESOURCE.getName())) ||
                        (finalAnalysisProperty.equals(AnalysisStep.SCHEMA_EXTRACTION.getName())))
                {
                    finalAnalysisStep = finalAnalysisProperty;
                }
            }
        }

        /*
         * The finalAnalysisStep property in analysisProperties takes precedent over the value in the
         * configuration properties.
         */
        if (surveyContext.getRequestParameters() != null)
        {
            String finalAnalysisProperty = surveyContext.getRequestParameters().get(UnityCatalogSurveyRequestParameter.FINAL_ANALYSIS_STEP.getName());

            if ((finalAnalysisProperty.equals(AnalysisStep.MEASURE_RESOURCE.getName())) ||
                    (finalAnalysisProperty.equals(AnalysisStep.SCHEMA_EXTRACTION.getName())))
            {
                finalAnalysisStep = finalAnalysisProperty;
            }
        }

        try
        {
            String           assetGUID  = surveyContext.getAssetGUID();
            SurveyAssetStore assetStore = surveyContext.getAssetStore();

            surveyContext.getAnnotationStore().setAnalysisStep(AnalysisStep.CHECK_ASSET.getName());

            Connector     connectorToAsset = assetStore.getConnectorToAsset();
            AssetUniverse assetUniverse    = assetStore.getAssetProperties();

            if (connectorToAsset instanceof OSSUnityCatalogResourceConnector atlasConnector)
            {
                atlasConnector.start();

                AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();
                OpenMetadataStore openMetadataStore = surveyContext.getOpenMetadataStore();

                /*
                 * The STATS analysis step gathers simple statistics from Apache Atlas
                 */
                annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            }
            else
            {
                String connectorToAssetClassName = "null";

                if (connectorToAsset != null)
                {
                    connectorToAssetClassName = connectorToAsset.getClass().getName();
                }

                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        UCAuditCode.WRONG_REST_CONNECTOR.getMessageDefinition(surveyActionServiceName,
                                                                                              connectorToAssetClassName,
                                                                                              OSSUnityCatalogResourceConnector.class.getName(),
                                                                                              assetGUID));
                }

                throw new ConnectorCheckedException(UCErrorCode.WRONG_REST_CONNECTOR.getMessageDefinition(surveyActionServiceName,
                                                                                                          connectorToAssetClassName,
                                                                                                          OSSUnityCatalogResourceConnector.class.getName(),
                                                                                                          assetGUID),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(UCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Transfer common properties into the annotation.
     *
     * @param annotation          output annotation
     * @param atlasAnnotationType annotation type definition
     */
    private void setUpAnnotation(Annotation annotation,
                                 UnityCatalogAnnotationType atlasAnnotationType)
    {
        annotation.setAnnotationType(atlasAnnotationType.getName());
        annotation.setAnalysisStep(atlasAnnotationType.getAnalysisStep());
        annotation.setSummary(atlasAnnotationType.getSummary());
        annotation.setExplanation(atlasAnnotationType.getExplanation());
    }
}
