/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.List;
import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalDataManagerMeasurement;


/**
 * RelationalDataManagerMeasurementsTabularDataSetConnector lists the measurements of surveyed relational data managers (database servers and catalogs) as if they are a tabular data set.
 */
public class RelationalDataManagerMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = RelationalDataManagerMeasurementsTabularDataSetConnector.class.getName();

    /*
     * Annotation type of the Unity Catalog catalog metrics (UnityCatalogAnnotationType.CATALOG_METRICS).  The Unity
     * Catalog connectors are not a runtime dependency of this module, so naming their enum here would fail with
     * NoClassDefFoundError wherever they are not installed.
     */
    private static final String UNITY_CATALOG_METRICS = "Capture Unity Catalog (UC) Catalog Metrics";


    /**
     * Default constructor
     */
    public RelationalDataManagerMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.RELATIONAL_DATA_MANAGER_MEASUREMENTS);
    }


    /**
     * One row per measurement annotation, linked to the most recent survey report it appears in.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                                                                 List.of(SurveyDatabaseAnnotationType.DATABASE_MEASUREMENTS.getName(),
                                                                         UNITY_CATALOG_METRICS)))
        {
            RelationalDataManagerMeasurement measurement = getMeasurement(annotation, RelationalDataManagerMeasurement.class);
            Map<String, String> values = getMeasurementValues(annotation);

            if ((measurement != null) && (values != null))
            {
                putValue(values, ProductDataFieldDefinition.RESOURCE_NAME, measurement.getResourceName() != null ? measurement.getResourceName() : getDisplayName(annotation));
                putValue(values, ProductDataFieldDefinition.SCHEMA_COUNT, measurement.getSchemaCount());
                putValue(values, ProductDataFieldDefinition.TABLE_COUNT, measurement.getTableCount());
                putValue(values, ProductDataFieldDefinition.VIEW_COUNT, measurement.getViewCount());
                putValue(values, ProductDataFieldDefinition.MAT_VIEW_COUNT, measurement.getMaterializedViewCount());
                putValue(values, ProductDataFieldDefinition.COLUMN_COUNT, measurement.getColumnCount());
                putValue(values, ProductDataFieldDefinition.DATA_SIZE, measurement.getSize());
                putValue(values, ProductDataFieldDefinition.ROWS_FETCHED, measurement.getRowsFetched());
                putValue(values, ProductDataFieldDefinition.ROWS_INSERTED, measurement.getRowsInserted());
                putValue(values, ProductDataFieldDefinition.ROWS_UPDATED, measurement.getRowsUpdated());
                putValue(values, ProductDataFieldDefinition.ROWS_DELETED, measurement.getRowsDeleted());
                putValue(values, ProductDataFieldDefinition.SESSION_TIME, measurement.getSessionTime());
                putValue(values, ProductDataFieldDefinition.ACTIVE_TIME, measurement.getActiveTime());
                putValue(values, ProductDataFieldDefinition.LAST_STATS_RESET, measurement.getStatsReset());

                addRow(annotation, values);
            }
        }
    }
}
