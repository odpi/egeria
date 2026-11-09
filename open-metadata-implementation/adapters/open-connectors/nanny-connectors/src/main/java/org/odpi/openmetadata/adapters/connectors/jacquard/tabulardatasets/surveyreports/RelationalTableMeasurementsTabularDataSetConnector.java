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
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalTableMeasurement;


/**
 * RelationalTableMeasurementsTabularDataSetConnector lists the measurements of surveyed relational tables as if they are a tabular data set.
 */
public class RelationalTableMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = RelationalTableMeasurementsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public RelationalTableMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.RELATIONAL_TABLE_MEASUREMENTS);
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
                                                                 List.of(SurveyDatabaseAnnotationType.TABLE_MEASUREMENTS.getName())))
        {
            RelationalTableMeasurement measurement = getMeasurement(annotation, RelationalTableMeasurement.class);
            Map<String, String> values = getMeasurementValues(annotation);

            if ((measurement != null) && (values != null))
            {
                putValue(values, ProductDataFieldDefinition.RESOURCE_NAME, measurement.getQualifiedTableName() != null ? measurement.getQualifiedTableName() : getDisplayName(annotation));
                putValue(values, ProductDataFieldDefinition.DISPLAY_NAME, measurement.getTableName());
                putValue(values, ProductDataFieldDefinition.TABLE_TYPE, measurement.getTableType());
                putValue(values, ProductDataFieldDefinition.COLUMN_COUNT, measurement.getColumnCount());
                putValue(values, ProductDataFieldDefinition.DATA_SIZE, measurement.getTableSize());
                putValue(values, ProductDataFieldDefinition.ROWS_INSERTED, measurement.getNumberOfRowsInserted());
                putValue(values, ProductDataFieldDefinition.ROWS_UPDATED, measurement.getNumberOfRowsUpdated());
                putValue(values, ProductDataFieldDefinition.ROWS_DELETED, measurement.getNumberOfRowsDeleted());
                putValue(values, ProductDataFieldDefinition.RESOURCE_OWNER, measurement.getTableOwner());
                putValue(values, ProductDataFieldDefinition.IS_POPULATED, measurement.getIsPopulated());
                putValue(values, ProductDataFieldDefinition.HAS_INDEXES, measurement.getHasIndexes());
                putValue(values, ProductDataFieldDefinition.HAS_RULES, measurement.getHasRules());
                putValue(values, ProductDataFieldDefinition.HAS_TRIGGERS, measurement.getHasTriggers());
                putValue(values, ProductDataFieldDefinition.HAS_ROW_SECURITY, measurement.getHasRowSecurity());
                putValue(values, ProductDataFieldDefinition.QUERY_DEFINITION, measurement.getQueryDefinition());

                addRow(annotation, values);
            }
        }
    }
}
