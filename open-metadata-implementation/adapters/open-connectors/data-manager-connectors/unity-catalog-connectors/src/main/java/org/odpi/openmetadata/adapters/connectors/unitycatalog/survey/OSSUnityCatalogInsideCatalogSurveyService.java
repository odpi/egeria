/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.*;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.FunctionInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.SchemaInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.TableInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.VolumeInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceMeasureAnnotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileAnnotation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Survey service that analyses the contents of a catalog within OSS Unity Catalog.
 */
public class OSSUnityCatalogInsideCatalogSurveyService extends OSSUnityCatalogServerSurveyBase
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

            String catalogName = this.getCatalogName();

            AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();

            /*
             * The MEASURE_RESOURCE analysis step gathers simple statistics from Unity Catalog
             */
            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            Map<String, ResourceProperties> schemaList   = new HashMap<>();
            Map<String, ResourceProperties> functionList = new HashMap<>();
            Map<String, ResourceProperties> tableList    = new HashMap<>();
            Map<String, ResourceProperties> volumeList   = new HashMap<>();

            long schemaCount   = 0;
            long functionCount = 0;
            long tableCount    = 0;
            long volumeCount   = 0;

            if (catalogName != null)
            {
                List<SchemaInfo> schemaInfos = ucConnector.listSchemas(catalogName);

                if (schemaInfos != null)
                {
                    for (SchemaInfo schemaInfo : schemaInfos)
                    {
                        if (schemaInfo != null)
                        {
                            ResourceProperties resourceProperties = new ResourceProperties();

                            resourceProperties.description = schemaInfo.getComment();
                            resourceProperties.creationDate = new Date(schemaInfo.getCreated_at());
                            if (schemaInfo.getUpdated_at() != 0L)
                            {
                                resourceProperties.lastUpdateDate = new Date(schemaInfo.getUpdated_at());
                            }
                            schemaList.put(schemaInfo.getFull_name(), resourceProperties);
                            schemaCount ++;

                            List<VolumeInfo> volumeInfos = ucConnector.listVolumes(catalogName, schemaInfo.getName());

                            if (volumeInfos != null)
                            {
                                for (VolumeInfo volumeInfo : volumeInfos)
                                {
                                    if (volumeInfo != null)
                                    {
                                        resourceProperties = new ResourceProperties();

                                        resourceProperties.description = volumeInfo.getComment();
                                        resourceProperties.creationDate = new Date(volumeInfo.getCreated_at());
                                        if (volumeInfo.getUpdated_at() != 0L)
                                        {
                                            resourceProperties.lastUpdateDate = new Date(volumeInfo.getUpdated_at());
                                        }

                                        volumeList.put(volumeInfo.getFull_name(), resourceProperties);
                                        volumeCount ++;
                                    }
                                }
                            }

                            List<TableInfo> tableInfos = ucConnector.listTables(catalogName, schemaInfo.getName());

                            if (tableInfos != null)
                            {
                                for (TableInfo tableInfo : tableInfos)
                                {
                                    if (tableInfo != null)
                                    {
                                        resourceProperties = new ResourceProperties();

                                        resourceProperties.description = tableInfo.getComment();
                                        resourceProperties.creationDate = new Date(tableInfo.getCreated_at());
                                        if (tableInfo.getUpdated_at() != 0L)
                                        {
                                            resourceProperties.lastUpdateDate = new Date(tableInfo.getUpdated_at());
                                        }
                                        tableList.put(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(), resourceProperties);
                                        tableCount ++;
                                    }
                                }
                            }

                            List<FunctionInfo> functionInfos = ucConnector.listFunctions(catalogName, schemaInfo.getName());

                            if (functionInfos != null)
                            {
                                for (FunctionInfo functionInfo : functionInfos)
                                {
                                    if (functionInfo != null)
                                    {
                                        resourceProperties = new ResourceProperties();

                                        resourceProperties.description = functionInfo.getComment();
                                        resourceProperties.creationDate = new Date(functionInfo.getCreated_at());
                                        if (functionInfo.getUpdated_at() != 0L)
                                        {
                                            resourceProperties.lastUpdateDate = new Date(functionInfo.getUpdated_at());
                                        }
                                        functionList.put(functionInfo.getFull_name(), resourceProperties);
                                        functionCount ++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ResourceMeasureAnnotation resourceMeasureAnnotation = new ResourceMeasureAnnotation();

            setUpAnnotation(resourceMeasureAnnotation, UnityCatalogAnnotationType.CATALOG_METRICS);

            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(UnityCatalogMetric.NO_OF_SCHEMAS.getPropertyName(), Long.toString(schemaCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_FUNCTIONS.getPropertyName(), Long.toString(functionCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_TABLES.getPropertyName(), Long.toString(tableCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_VOLUMES.getPropertyName(), Long.toString(volumeCount));

            Map<String, Long>   resourceCounts     = new HashMap<>();

            resourceCounts.put(UnityCatalogMetric.NO_OF_SCHEMAS.getPropertyName(), schemaCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_FUNCTIONS.getPropertyName(), functionCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_TABLES.getPropertyName(), tableCount);
            resourceCounts.put(UnityCatalogMetric.NO_OF_VOLUMES.getPropertyName(), volumeCount);

            resourceMeasureAnnotation.setJsonProperties(this.getJSONProperties(resourceCounts));
            resourceMeasureAnnotation.setResourceProperties(resourceProperties);

            annotationStore.addAnnotation(resourceMeasureAnnotation, null);

            if (! finalAnalysisStep.equals(AnalysisStep.MEASURE_RESOURCE.getName()))
            {
                annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

                ResourceProfileAnnotation resourceProfileAnnotation = this.getNameListAnnotation(UnityCatalogAnnotationType.SCHEMA_LIST, schemaList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(UnityCatalogAnnotationType.FUNCTION_LIST, functionList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(UnityCatalogAnnotationType.TABLE_LIST, tableList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                resourceProfileAnnotation = this.getNameListAnnotation(UnityCatalogAnnotationType.VOLUME_LIST, volumeList);

                annotationStore.addAnnotation(resourceProfileAnnotation, null);

                if (! finalAnalysisStep.equals(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName()))
                {
                    annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

                    super.writeInventory("unityCatalog-Catalog-" + catalogName + "-Resources",
                                         null,
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
