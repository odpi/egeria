/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.RequestForActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * RequestForActionTargetsTabularDataSetConnector lists the action targets of the request for action annotations from all survey reports as if they are a tabular data set.  There is one row for each survey report and action target of each annotation.
 */
public class RequestForActionTargetsTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = RequestForActionTargetsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public RequestForActionTargetsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.REQUEST_FOR_ACTION_TARGET);
    }


    /**
     * One row for each survey report and action target of each request for action annotation.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
        {
            if (annotation.getRequestForActionTargets() == null)
            {
                continue;
            }

            for (MetadataElementSummary report : getReportsForAnnotation(annotation))
            {
                String        surveyReportGUID = report.getElementHeader().getGUID();
                ElementHeader subject          = getAnnotationSubject(annotation, surveyReportGUID);

                for (RelatedMetadataElementSummary actionTarget : annotation.getRequestForActionTargets())
                {
                    if ((actionTarget != null) && (actionTarget.getRelatedElement() != null))
                    {
                        Map<String, String> values = newValues();

                        putValue(values, ProductDataFieldDefinition.SURVEY_REPORT_GUID, surveyReportGUID);
                        putValue(values, ProductDataFieldDefinition.ACTION_REQUEST_NAME, RequestForActionAnnotationsTabularDataSetConnector.getActionRequestName(annotation));
                        putValue(values, ProductDataFieldDefinition.ACTION_TARGET_RELATIONSHIP_GUID, actionTarget.getRelationshipHeader().getGUID());
                        putValue(values, ProductDataFieldDefinition.ACTION_TARGET_GUID, actionTarget.getRelatedElement().getElementHeader().getGUID());
                        putValue(values, ProductDataFieldDefinition.ACTION_TARGET_TYPE_NAME, actionTarget.getRelatedElement().getElementHeader().getType().getTypeName());

                        if (actionTarget.getRelationshipProperties() instanceof RequestForActionTargetProperties targetProperties)
                        {
                            putValue(values, ProductDataFieldDefinition.ACTION_TARGET_NAME, targetProperties.getActionTargetName());
                        }
                        else
                        {
                            putValue(values, ProductDataFieldDefinition.ACTION_TARGET_NAME, null);
                        }

                        putAnnotationSubjectValues(values, subject);

                        addRow(annotation, values);
                    }
                }
            }
        }
    }
}
