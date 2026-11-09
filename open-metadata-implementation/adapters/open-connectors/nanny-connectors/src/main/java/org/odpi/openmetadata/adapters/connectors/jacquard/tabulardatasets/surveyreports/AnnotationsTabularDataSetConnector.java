/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * AnnotationsTabularDataSetConnector lists the annotations from all survey reports as if they are a tabular data set.  There is one row for each survey report that an annotation appears in, since annotations are reused by later surveys of the same resource.
 */
public class AnnotationsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = AnnotationsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public AnnotationsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.ANNOTATIONS);
    }


    /**
     * One row for each survey report that each annotation appears in.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.ANNOTATION.typeName))
        {
            for (MetadataElementSummary report : getReportsForAnnotation(annotation))
            {
                String surveyReportGUID = report.getElementHeader().getGUID();

                Map<String, String> values = newValues();

                putSurveyReportValues(values, surveyReportGUID);

                ElementHeader subject = getAnnotationSubject(annotation, surveyReportGUID);

                putAnnotationSubjectValues(values, subject);

                addRow(annotation, values);
            }
        }
    }
}
