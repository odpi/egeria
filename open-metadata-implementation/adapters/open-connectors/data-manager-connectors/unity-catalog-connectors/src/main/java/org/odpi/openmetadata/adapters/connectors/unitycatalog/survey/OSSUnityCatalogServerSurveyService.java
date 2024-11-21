/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogMetric;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.*;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyResourceManagerAnnotationType;
import org.odpi.openmetadata.frameworks.surveyaction.measurements.RelationalDatabaseMetric;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceMeasureAnnotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileAnnotation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSSUnityCatalogServerSurveyService extends OSSUnityCatalogServerSurveyBase
{
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

        this.setFinalAnalysisStep();

        try
        {
            connector = performCheckAssetAnalysisStep(OSSUnityCatalogResourceConnector.class,
                                                      OpenMetadataType.SOFTWARE_SERVER.typeName);

            OSSUnityCatalogResourceConnector ucConnector = (OSSUnityCatalogResourceConnector)connector;

            AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();

            /*
             * The MEASURE RESOURCE analysis step gathers simple statistics from Unity Catalog
             */
            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            Map<String, ResourceProperties> catalogList  = new HashMap<>();
            Map<String, ResourceProperties> schemaList   = new HashMap<>();
            Map<String, ResourceProperties> functionList = new HashMap<>();
            Map<String, ResourceProperties> tableList    = new HashMap<>();
            Map<String, ResourceProperties> columnList   = new HashMap<>();
            Map<String, ResourceProperties> volumeList   = new HashMap<>();
            Map<String, ResourceProperties> modelList    = new HashMap<>();

            long catalogCount  = 0;
            long schemaCount   = 0;
            long functionCount = 0;
            long tableCount    = 0;
            long columnCount   = 0;
            long volumeCount   = 0;
            long modelCount    = 0;

            List<CatalogInfo> catalogInfos = ucConnector.listCatalogs();

            if (catalogInfos != null)
            {
                for (CatalogInfo catalogInfo : catalogInfos)
                {
                    if (catalogInfo != null)
                    {
                        ResourceProperties resourceProperties = new ResourceProperties();

                        resourceProperties.description = catalogInfo.getComment();
                        resourceProperties.creationDate = new Date(catalogInfo.getCreated_at());
                        resourceProperties.createdBy = catalogInfo.getCreated_by();
                        if (catalogInfo.getUpdated_at() != 0L)
                        {
                            resourceProperties.lastUpdateDate = new Date(catalogInfo.getUpdated_at());
                        }
                        resourceProperties.lastUpdatedBy = catalogInfo.getUpdated_by();
                        resourceProperties.owner = catalogInfo.getOwner();

                        catalogList.put(catalogInfo.getName(), resourceProperties);
                        catalogCount ++;

                        List<SchemaInfo> schemaInfos = ucConnector.listSchemas(catalogInfo.getName());

                        if (schemaInfos != null)
                        {
                            for (SchemaInfo schemaInfo : schemaInfos)
                            {
                                if (schemaInfo != null)
                                {
                                    resourceProperties = new ResourceProperties();

                                    resourceProperties.description = schemaInfo.getComment();
                                    resourceProperties.creationDate = new Date(schemaInfo.getCreated_at());
                                    resourceProperties.createdBy = schemaInfo.getCreated_by();
                                    if (schemaInfo.getUpdated_at() != 0L)
                                    {
                                        resourceProperties.lastUpdateDate = new Date(schemaInfo.getUpdated_at());
                                    }
                                    resourceProperties.lastUpdatedBy = schemaInfo.getUpdated_by();
                                    resourceProperties.owner = schemaInfo.getOwner();

                                    schemaList.put(schemaInfo.getFull_name(), resourceProperties);
                                    schemaCount ++;

                                    List<VolumeInfo> volumeInfos = ucConnector.listVolumes(catalogInfo.getName(), schemaInfo.getName());

                                    if (volumeInfos != null)
                                    {
                                        for (VolumeInfo volumeInfo : volumeInfos)
                                        {
                                            if (volumeInfo != null)
                                            {
                                                resourceProperties = new ResourceProperties();

                                                resourceProperties.description = volumeInfo.getComment();
                                                resourceProperties.creationDate = new Date(volumeInfo.getCreated_at());
                                                resourceProperties.createdBy = volumeInfo.getCreated_by();
                                                if (volumeInfo.getUpdated_at() != 0L)
                                                {
                                                    resourceProperties.lastUpdateDate = new Date(volumeInfo.getUpdated_at());
                                                }
                                                resourceProperties.lastUpdatedBy = volumeInfo.getUpdated_by();
                                                resourceProperties.owner = volumeInfo.getOwner();

                                                volumeList.put(volumeInfo.getFull_name(), resourceProperties);
                                                volumeCount ++;
                                            }
                                        }
                                    }

                                    List<TableInfo> tableInfos = ucConnector.listTables(catalogInfo.getName(), schemaInfo.getName());

                                    if (tableInfos != null)
                                    {
                                        for (TableInfo tableInfo : tableInfos)
                                        {
                                            if (tableInfo != null)
                                            {
                                                resourceProperties = new ResourceProperties();

                                                resourceProperties.description = tableInfo.getComment();
                                                resourceProperties.creationDate = new Date(tableInfo.getCreated_at());
                                                resourceProperties.createdBy = tableInfo.getCreated_by();
                                                if (tableInfo.getUpdated_at() != 0L)
                                                {
                                                    resourceProperties.lastUpdateDate = new Date(tableInfo.getUpdated_at());
                                                }
                                                resourceProperties.lastUpdatedBy = tableInfo.getUpdated_by();
                                                resourceProperties.owner = tableInfo.getOwner();

                                                tableList.put(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(), resourceProperties);
                                                tableCount ++;

                                                if (tableInfo.getColumns() != null)
                                                {
                                                    for (ColumnInfo columnInfo : tableInfo.getColumns())
                                                    {
                                                        ResourceProperties columnResourceProperties = new ResourceProperties(resourceProperties);

                                                        columnResourceProperties.description = columnInfo.getComment();
                                                        columnList.put(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName() + "." + columnInfo.getName(), columnResourceProperties);
                                                        columnCount ++;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    List<RegisteredModelInfo> registeredModelInfos = ucConnector.listRegisteredModels(catalogInfo.getName(), schemaInfo.getName());

                                    if (registeredModelInfos != null)
                                    {
                                        for (RegisteredModelInfo registeredModelInfo : registeredModelInfos)
                                        {
                                            if (registeredModelInfo != null)
                                            {
                                                resourceProperties = new ResourceProperties();

                                                resourceProperties.description = registeredModelInfo.getComment();
                                                resourceProperties.creationDate = new Date(registeredModelInfo.getCreated_at());
                                                resourceProperties.createdBy = registeredModelInfo.getCreated_by();
                                                if (registeredModelInfo.getUpdated_at() != 0L)
                                                {
                                                    resourceProperties.lastUpdateDate = new Date(registeredModelInfo.getUpdated_at());
                                                }
                                                resourceProperties.lastUpdatedBy = registeredModelInfo.getUpdated_by();
                                                resourceProperties.owner = registeredModelInfo.getOwner();
                                                modelList.put(registeredModelInfo.getFull_name(), resourceProperties);
                                                modelCount ++;
                                            }
                                        }
                                    }

                                    List<FunctionInfo> functionInfos = ucConnector.listFunctions(catalogInfo.getName(), schemaInfo.getName());

                                    if (functionInfos != null)
                                    {
                                        for (FunctionInfo functionInfo : functionInfos)
                                        {
                                            if (functionInfo != null)
                                            {
                                                resourceProperties = new ResourceProperties();

                                                resourceProperties.description = functionInfo.getComment();
                                                resourceProperties.creationDate = new Date(functionInfo.getCreated_at());
                                                resourceProperties.createdBy = functionInfo.getCreated_by();
                                                if (functionInfo.getUpdated_at() != 0L)
                                                {
                                                    resourceProperties.lastUpdateDate = new Date(functionInfo.getUpdated_at());
                                                }
                                                resourceProperties.lastUpdatedBy = functionInfo.getUpdated_by();
                                                resourceProperties.owner = functionInfo.getOwner();

                                                functionList.put(functionInfo.getFull_name(), resourceProperties);
                                                functionCount ++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ResourceMeasureAnnotation resourceMeasureAnnotation = new ResourceMeasureAnnotation();

            setUpAnnotation(resourceMeasureAnnotation, UnityCatalogAnnotationType.SERVER_METRICS);

            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(UnityCatalogMetric.NO_OF_CATALOGS.getPropertyName(), Long.toString(catalogCount));
            resourceProperties.put(RelationalDatabaseMetric.SCHEMA_COUNT.getPropertyName(), Long.toString(schemaCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_FUNCTIONS.getPropertyName(), Long.toString(functionCount));
            resourceProperties.put(RelationalDatabaseMetric.TABLE_COUNT.getPropertyName(), Long.toString(tableCount));
            resourceProperties.put(RelationalDatabaseMetric.COLUMN_COUNT.getPropertyName(), Long.toString(columnCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_VOLUMES.getPropertyName(), Long.toString(volumeCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_MODELS.getPropertyName(), Long.toString(modelCount));

            Map<String, Long>   resourceCounts     = new HashMap<>();

            resourceCounts.put(UnityCatalogMetric.NO_OF_CATALOGS.getPropertyName(), catalogCount);
            resourceCounts.put(RelationalDatabaseMetric.SCHEMA_COUNT.getPropertyName(), schemaCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_FUNCTIONS.getPropertyName(), functionCount);
            resourceCounts.put(RelationalDatabaseMetric.TABLE_COUNT.getPropertyName(), tableCount);
            resourceCounts.put(RelationalDatabaseMetric.COLUMN_COUNT.getPropertyName(), columnCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_VOLUMES.getPropertyName(), volumeCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_MODELS.getPropertyName(), modelCount);

            resourceMeasureAnnotation.setJsonProperties(this.getJSONProperties(resourceCounts));
            resourceMeasureAnnotation.setResourceProperties(resourceProperties);

            annotationStore.addAnnotation(resourceMeasureAnnotation, null);

            if (! finalAnalysisStep.equals(AnalysisStep.MEASURE_RESOURCE.getName()))
            {
                annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

                ResourceProfileAnnotation resourceProfileAnnotation = this.getNameListAnnotation(UnityCatalogAnnotationType.CATALOG_LIST, catalogList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(SurveyDatabaseAnnotationType.SCHEMA_LIST, schemaList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(SurveyResourceManagerAnnotationType.FUNCTION_LIST, functionList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(SurveyDatabaseAnnotationType.TABLE_LIST, tableList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(SurveyDatabaseAnnotationType.COLUMN_LIST, columnList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(SurveyResourceManagerAnnotationType.VOLUME_LIST, volumeList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(SurveyResourceManagerAnnotationType.MODEL_LIST, modelList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                if (! finalAnalysisStep.equals(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName()))
                {
                    annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

                    super.writeInventory("unityCatalog-Server-Resources",
                                         catalogList,
                                         schemaList,
                                         functionList,
                                         tableList,
                                         volumeList);
                }
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
}
