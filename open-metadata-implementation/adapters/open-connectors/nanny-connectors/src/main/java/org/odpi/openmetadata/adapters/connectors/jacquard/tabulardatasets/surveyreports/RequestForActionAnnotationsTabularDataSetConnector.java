/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.RequestForActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * RequestForActionAnnotationsTabularDataSetConnector lists the request for action annotations from all survey reports as if they are a tabular data set.  There is one row for each survey report that an annotation appears in.
 */
public class RequestForActionAnnotationsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = RequestForActionAnnotationsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public RequestForActionAnnotationsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.REQUEST_FOR_ACTION);
    }


    /**
     * One row for each survey report that each request for action annotation appears in.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
        {
            for (MetadataElementSummary report : getReportsForAnnotation(annotation))
            {
                String surveyReportGUID = report.getElementHeader().getGUID();

                Map<String, String> values = newValues();

                putValue(values, ProductDataFieldDefinition.SURVEY_REPORT_GUID, surveyReportGUID);
                putValue(values, ProductDataFieldDefinition.ACTION_REQUEST_NAME, getActionRequestName(annotation));

                ElementHeader subject = getAnnotationSubject(annotation, surveyReportGUID);

                putAnnotationSubjectValues(values, subject);

                addRow(annotation, values);
            }
        }
    }


    /**
     * Return the name of the action requested.  The surveys do not set actionRequested, so the annotation type,
     * which says what was found, names the request when it is missing.
     *
     * @param annotation request for action annotation
     * @return name
     */
    static String getActionRequestName(OpenMetadataRootElement annotation)
    {
        if (annotation.getProperties() instanceof RequestForActionProperties requestForActionProperties)
        {
            if (requestForActionProperties.getActionRequested() != null)
            {
                return requestForActionProperties.getActionRequested();
            }

            return requestForActionProperties.getAnnotationType();
        }

        return null;
    }
}
