/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourcePhysicalStatusAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * ResourceMeasurementsTabularDataSetConnector lists the individual measurements recorded in resource measurement annotations as if they are a tabular data set.  There is one row per measurement in each annotation, linked to the most recent survey report the annotation appears in.
 */
public class ResourceMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = ResourceMeasurementsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public ResourceMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.RESOURCE_MEASUREMENTS);
    }


    /**
     * One row per entry in the resourceProperties of each resource measurement annotation.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName))
        {
            if ((annotation.getProperties() instanceof ResourceMeasureAnnotationProperties measureProperties) &&
                    (measureProperties.getResourceProperties() != null))
            {
                Map<String, String> commonValues = getMeasurementValues(annotation);

                if (commonValues == null)
                {
                    continue;
                }

                putValue(commonValues, ProductDataFieldDefinition.MEASUREMENT_NAME, measureProperties.getAnnotationType());

                if (measureProperties instanceof ResourcePhysicalStatusAnnotationProperties physicalStatusProperties)
                {
                    putValue(commonValues, ProductDataFieldDefinition.RESOURCE_CREATION_TIME, physicalStatusProperties.getResourceCreateTime());
                    putValue(commonValues, ProductDataFieldDefinition.LAST_MODIFIED_TIME, physicalStatusProperties.getResourceUpdateTime());
                    putValue(commonValues, ProductDataFieldDefinition.RESOURCE_SIZE, physicalStatusProperties.getSize());
                    putValue(commonValues, ProductDataFieldDefinition.ENCODING, physicalStatusProperties.getEncodingType());
                }

                for (Map.Entry<String, String> measurement : measureProperties.getResourceProperties().entrySet())
                {
                    if (measurement.getKey() != null)
                    {
                        Map<String, String> values = newValues();

                        values.putAll(commonValues);

                        putValue(values, ProductDataFieldDefinition.MEASUREMENT_CATEGORY, measurement.getKey());
                        putValue(values, ProductDataFieldDefinition.MEASUREMENT_DISPLAY_NAME, measurement.getKey());
                        putValue(values, ProductDataFieldDefinition.MEASUREMENT_VALUE, measurement.getValue());
                        putValue(values, ProductDataFieldDefinition.MEASUREMENT_NUMERIC_VALUE, getNumericValue(measurement.getValue()));

                        addRow(annotation, values);
                    }
                }
            }
        }
    }
}
