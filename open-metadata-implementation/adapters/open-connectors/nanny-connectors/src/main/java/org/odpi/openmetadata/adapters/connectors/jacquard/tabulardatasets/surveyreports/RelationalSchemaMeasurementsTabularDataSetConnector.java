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
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalSchemaMeasurement;


/**
 * RelationalSchemaMeasurementsTabularDataSetConnector lists the measurements of surveyed relational database schemas as if they are a tabular data set.
 */
public class RelationalSchemaMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = RelationalSchemaMeasurementsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public RelationalSchemaMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.RELATIONAL_SCHEMA_MEASUREMENTS);
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
                                                                 List.of(SurveyDatabaseAnnotationType.SCHEMA_MEASUREMENTS.getName())))
        {
            RelationalSchemaMeasurement measurement = getMeasurement(annotation, RelationalSchemaMeasurement.class);
            Map<String, String> values = getMeasurementValues(annotation);

            if ((measurement != null) && (values != null))
            {
                putValue(values, ProductDataFieldDefinition.RESOURCE_NAME, measurement.getQualifiedSchemaName() != null ? measurement.getQualifiedSchemaName() : getDisplayName(annotation));
                putValue(values, ProductDataFieldDefinition.DISPLAY_NAME, measurement.getSchemaName());
                putValue(values, ProductDataFieldDefinition.TABLE_COUNT, measurement.getTableCount());
                putValue(values, ProductDataFieldDefinition.VIEW_COUNT, measurement.getViewCount());
                putValue(values, ProductDataFieldDefinition.MAT_VIEW_COUNT, measurement.getMaterializedViewCount());
                putValue(values, ProductDataFieldDefinition.COLUMN_COUNT, measurement.getColumnCount());
                putValue(values, ProductDataFieldDefinition.DATA_SIZE, measurement.getTotalTableSize());

                addRow(annotation, values);
            }
        }
    }
}
