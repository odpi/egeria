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
import org.odpi.openmetadata.frameworks.opensurvey.measurements.RelationalColumnMeasurement;


/**
 * RelationalColumnMeasurementsTabularDataSetConnector lists the measurements of surveyed relational columns as if they are a tabular data set.
 */
public class RelationalColumnMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = RelationalColumnMeasurementsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public RelationalColumnMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.RELATIONAL_COLUMN_MEASUREMENTS);
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
                                                                 List.of(SurveyDatabaseAnnotationType.COLUMN_MEASUREMENTS.getName())))
        {
            RelationalColumnMeasurement measurement = getMeasurement(annotation, RelationalColumnMeasurement.class);
            Map<String, String> values = getMeasurementValues(annotation);

            if ((measurement != null) && (values != null))
            {
                putValue(values, ProductDataFieldDefinition.RESOURCE_NAME, measurement.getQualifiedColumnName() != null ? measurement.getQualifiedColumnName() : getDisplayName(annotation));
                putValue(values, ProductDataFieldDefinition.DISPLAY_NAME, measurement.getColumnName());
                putValue(values, ProductDataFieldDefinition.DATA_TYPE, measurement.getColumnDataType());
                putValue(values, ProductDataFieldDefinition.DATA_SIZE, measurement.getColumnSize());
                putValue(values, ProductDataFieldDefinition.NOT_NULL, measurement.getColumnNotNull());
                putValue(values, ProductDataFieldDefinition.AVERAGE_WIDTH, measurement.getAverageColumnWidth());
                putValue(values, ProductDataFieldDefinition.NUMBER_OF_DISTINCT_VALUES, measurement.getNumberOfDistinctValues());
                putValue(values, ProductDataFieldDefinition.MOST_COMMON_VALUES, measurement.getMostCommonValues());
                putValue(values, ProductDataFieldDefinition.MOST_COMMON_VALUES_FREQUENCY, measurement.getMostCommonValuesFrequency());

                addRow(annotation, values);
            }
        }
    }
}
