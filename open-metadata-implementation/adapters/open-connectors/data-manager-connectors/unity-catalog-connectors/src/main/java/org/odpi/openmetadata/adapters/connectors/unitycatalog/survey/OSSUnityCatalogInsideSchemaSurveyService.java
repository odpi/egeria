/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogMetric;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyResourceManagerAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDataManagerMeasurement;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDatabaseMetric;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Survey service that analyses the contents of a schema within OSS Unity Catalog.
 */
public class OSSUnityCatalogInsideSchemaSurveyService extends OSSUnityCatalogServerSurveyBase
{
    /**
     * Indicates that the survey service is completely configured and can begin processing.
     * This is where the function of the discovery service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException a problem within the discovery service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            connector = performCheckAssetAnalysisStep(OSSUnityCatalogResourceConnector.class,
                                                      OpenMetadataType.SOFTWARE_SERVER.typeName);

            OSSUnityCatalogResourceConnector ucConnector = (OSSUnityCatalogResourceConnector)connector;

            if (super.isFinalAnalysisStep())
            {
                return;
            }

            String catalogName = this.getCatalogName();
            String schemaName  = this.getSchemaName();

            AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();

            /*
             * The MEASURE_RESOURCE analysis step gathers simple statistics from Unity Catalog
             */
            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            SchemaContents schemaContents = new SchemaContents();

            if ((catalogName != null) && (schemaName != null))
            {
                super.surveySchemaContents(ucConnector, catalogName, schemaName, schemaContents);
            }

            ResourceMeasureAnnotationProperties resourceMeasureAnnotationProperties = new ResourceMeasureAnnotationProperties();

            setUpAnnotation(resourceMeasureAnnotationProperties, UnityCatalogAnnotationType.SCHEMA_METRICS);

            RelationalDataManagerMeasurement relationalDataManagerMeasurement = new RelationalDataManagerMeasurement();

            relationalDataManagerMeasurement.setResourceName(catalogName + "." + schemaName);
            relationalDataManagerMeasurement.setTableCount(schemaContents.tableCount);
            relationalDataManagerMeasurement.setColumnCount(schemaContents.columnCount);

            resourceMeasureAnnotationProperties.setJsonProperties(this.getJSONProperties(relationalDataManagerMeasurement));

            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(UnityCatalogMetric.NO_OF_FUNCTIONS.getPropertyName(), Long.toString(schemaContents.functionCount));
            resourceProperties.put(RelationalDatabaseMetric.TABLE_COUNT.getPropertyName(), Long.toString(schemaContents.tableCount));
            resourceProperties.put(RelationalDatabaseMetric.COLUMN_COUNT.getPropertyName(), Long.toString(schemaContents.columnCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_VOLUMES.getPropertyName(), Long.toString(schemaContents.volumeCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_MODELS.getPropertyName(), Long.toString(schemaContents.modelCount));

            Map<String, Long>   resourceCounts     = new HashMap<>();

            resourceCounts.put(UnityCatalogMetric.NO_OF_FUNCTIONS.getPropertyName(), schemaContents.functionCount);
            resourceCounts.put(RelationalDatabaseMetric.TABLE_COUNT.getPropertyName(), schemaContents.tableCount);
            resourceCounts.put(RelationalDatabaseMetric.COLUMN_COUNT.getPropertyName(), schemaContents.columnCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_VOLUMES.getPropertyName(), schemaContents.volumeCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_MODELS.getPropertyName(), schemaContents.modelCount);

            resourceMeasureAnnotationProperties.setJsonProperties(this.getJSONProperties(resourceCounts));
            resourceMeasureAnnotationProperties.setResourceProperties(resourceProperties);


            annotationStore.addAnnotation(resourceMeasureAnnotationProperties, null);

            /*
             * Stopping after measuring the resource records its counts without listing its contents.
             */
            if (super.isFinalAnalysisStep())
            {
                return;
            }

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

            ResourceProfileAnnotationProperties resourceProfileAnnotation = this.getNameListAnnotation(SurveyResourceManagerAnnotationType.FUNCTION_LIST, schemaContents.functionList);

            annotationStore.addAnnotation(resourceProfileAnnotation, null);

            resourceProfileAnnotation = this.getNameListAnnotation(SurveyDatabaseAnnotationType.TABLE_LIST, schemaContents.tableList);

            annotationStore.addAnnotation(resourceProfileAnnotation, null);

            resourceProfileAnnotation = this.getNameListAnnotation(SurveyDatabaseAnnotationType.COLUMN_LIST, schemaContents.columnList);

            annotationStore.addAnnotation(resourceProfileAnnotation, null);

            resourceProfileAnnotation = this.getNameListAnnotation(SurveyResourceManagerAnnotationType.VOLUME_LIST, schemaContents.volumeList);

            annotationStore.addAnnotation(resourceProfileAnnotation, null);

            resourceProfileAnnotation = this.getNameListAnnotation(SurveyResourceManagerAnnotationType.MODEL_LIST, schemaContents.modelList);

            annotationStore.addAnnotation(resourceProfileAnnotation, null);

            /*
             * Stopping after profiling records the lists of resources without writing the inventory.
             */
            if (super.isFinalAnalysisStep())
            {
                return;
            }

            annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

            super.writeInventory("unityCatalog-Schema-" + catalogName + "." + schemaName + "-Resources",
                                 null,
                                 null,
                                 schemaContents.functionList,
                                 schemaContents.tableList,
                                 schemaContents.volumeList);
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
}
