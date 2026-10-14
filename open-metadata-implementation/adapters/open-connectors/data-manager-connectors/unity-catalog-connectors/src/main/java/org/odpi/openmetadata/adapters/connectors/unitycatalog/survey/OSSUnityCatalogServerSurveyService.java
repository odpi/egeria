/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogMetric;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.*;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyResourceManagerAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDatabaseMetric;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;

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

            AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();

            /*
             * The MEASURE RESOURCE analysis step gathers simple statistics from Unity Catalog
             */
            annotationStore.setAnalysisStep(AnalysisStep.MEASURE_RESOURCE.getName());

            Map<String, ResourceProperties> catalogList    = new HashMap<>();
            Map<String, ResourceProperties> schemaList     = new HashMap<>();
            SchemaContents                  schemaContents = new SchemaContents();

            long catalogCount = 0;
            long schemaCount  = 0;

            List<String> excludedSchemas = super.getArrayRequestOrConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_SCHEMA_NAMES.getName(),
                                                                                        null);

            List<String> includedSchemas = super.getArrayRequestOrConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_SCHEMA_NAMES.getName(),
                                                                                        null);

            /*
             * If the catalogs cannot be listed, there is nothing to survey and the survey fails.  After that,
             * a failure to retrieve the contents of a catalog or schema is logged and the survey continues.
             */
            List<CatalogInfo> catalogInfos = ucConnector.listCatalogs();

            if (catalogInfos != null)
            {
                for (CatalogInfo catalogInfo : catalogInfos)
                {
                    if (! super.isActive())
                    {
                        break;
                    }

                    if (catalogInfo != null)
                    {
                        catalogList.put(catalogInfo.getName(), super.getResourceProperties(catalogInfo));
                        catalogCount ++;

                        List<SchemaInfo> schemaInfos = null;

                        try
                        {
                            schemaInfos = ucConnector.listSchemas(catalogInfo.getName());
                        }
                        catch (Exception error)
                        {
                            super.logRetrievalFailure("schemas", "catalog " + catalogInfo.getName(), error);
                        }

                        if (schemaInfos != null)
                        {
                            for (SchemaInfo schemaInfo : schemaInfos)
                            {
                                if (! super.isActive())
                                {
                                    break;
                                }

                                if ((schemaInfo != null) && (surveyContext.elementShouldBeSurveyed(schemaInfo.getName(), excludedSchemas, includedSchemas)))
                                {
                                    schemaList.put(schemaInfo.getFull_name(), super.getResourceProperties(schemaInfo));
                                    schemaCount ++;

                                    super.surveySchemaContents(ucConnector, catalogInfo.getName(), schemaInfo.getName(), schemaContents);
                                }
                            }
                        }
                    }
                }
            }

            ResourceMeasureAnnotationProperties resourceMeasureAnnotationProperties = new ResourceMeasureAnnotationProperties();

            setUpAnnotation(resourceMeasureAnnotationProperties, UnityCatalogAnnotationType.SERVER_METRICS);

            Map<String, String> resourceProperties = new HashMap<>();

            resourceProperties.put(UnityCatalogMetric.NO_OF_CATALOGS.getPropertyName(), Long.toString(catalogCount));
            resourceProperties.put(RelationalDatabaseMetric.SCHEMA_COUNT.getPropertyName(), Long.toString(schemaCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_FUNCTIONS.getPropertyName(), Long.toString(schemaContents.functionCount));
            resourceProperties.put(RelationalDatabaseMetric.TABLE_COUNT.getPropertyName(), Long.toString(schemaContents.tableCount));
            resourceProperties.put(RelationalDatabaseMetric.COLUMN_COUNT.getPropertyName(), Long.toString(schemaContents.columnCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_VOLUMES.getPropertyName(), Long.toString(schemaContents.volumeCount));
            resourceProperties.put(UnityCatalogMetric.NO_OF_MODELS.getPropertyName(), Long.toString(schemaContents.modelCount));

            Map<String, Long>   resourceCounts     = new HashMap<>();

            resourceCounts.put(UnityCatalogMetric.NO_OF_CATALOGS.getPropertyName(), catalogCount);
            resourceCounts.put(RelationalDatabaseMetric.SCHEMA_COUNT.getPropertyName(), schemaCount);
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

            ResourceProfileAnnotationProperties resourceProfileAnnotation = this.getNameListAnnotation(UnityCatalogAnnotationType.CATALOG_LIST, catalogList);

            annotationStore.addAnnotation(resourceProfileAnnotation, null);

            resourceProfileAnnotation = this.getNameListAnnotation(SurveyDatabaseAnnotationType.SCHEMA_LIST, schemaList);

            annotationStore.addAnnotation(resourceProfileAnnotation, null);

            resourceProfileAnnotation = this.getNameListAnnotation(SurveyResourceManagerAnnotationType.FUNCTION_LIST, schemaContents.functionList);

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

            super.writeInventory("unityCatalog-Server-Resources",
                                 catalogList,
                                 schemaList,
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
