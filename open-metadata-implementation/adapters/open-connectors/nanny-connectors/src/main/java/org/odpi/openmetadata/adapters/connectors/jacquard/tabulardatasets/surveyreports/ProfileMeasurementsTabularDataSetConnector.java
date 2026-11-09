/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.HashMap;
import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * ProfileMeasurementsTabularDataSetConnector lists the individual counts recorded in resource profile annotations as if they are a tabular data set.  There is one row per counted value in each annotation, linked to the most recent survey report the annotation appears in.
 */
public class ProfileMeasurementsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = ProfileMeasurementsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public ProfileMeasurementsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.PROFILE_MEASUREMENTS);
    }


    /**
     * One row per counted value in each resource profile annotation.  The value counts are used when present,
     * otherwise the profile counts.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName))
        {
            if (annotation.getProperties() instanceof ResourceProfileAnnotationProperties profileProperties)
            {
                Map<String, Long> counts = new HashMap<>();

                if ((profileProperties.getValueCount() != null) && (! profileProperties.getValueCount().isEmpty()))
                {
                    for (Map.Entry<String, Integer> valueCount : profileProperties.getValueCount().entrySet())
                    {
                        counts.put(valueCount.getKey(), valueCount.getValue() == null ? null : valueCount.getValue().longValue());
                    }
                }
                else if (profileProperties.getProfileCounts() != null)
                {
                    counts.putAll(profileProperties.getProfileCounts());
                }

                if (counts.isEmpty())
                {
                    continue;
                }

                Map<String, String> commonValues = getMeasurementValues(annotation);

                if (commonValues == null)
                {
                    continue;
                }

                putValue(commonValues, ProductDataFieldDefinition.MEASUREMENT_NAME, profileProperties.getAnnotationType());

                for (Map.Entry<String, Long> count : counts.entrySet())
                {
                    if (count.getKey() != null)
                    {
                        Map<String, String> values = newValues();

                        values.putAll(commonValues);

                        putValue(values, ProductDataFieldDefinition.MEASUREMENT_CATEGORY, count.getKey());
                        putValue(values, ProductDataFieldDefinition.MEASUREMENT_VALUE, count.getValue());
                        putValue(values, ProductDataFieldDefinition.MEASUREMENT_NUMERIC_VALUE, count.getValue());

                        addRow(annotation, values);
                    }
                }
            }
        }
    }
}
