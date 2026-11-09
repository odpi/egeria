/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import java.util.Map;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;


/**
 * SurveyReportListTabularDataSetConnector lists the survey reports stored in open metadata as if they are a tabular data set.  There is one row per survey report.
 */
public class SurveyReportListTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = SurveyReportListTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public SurveyReportListTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.SURVEY_REPORT_LIST);
    }


    /**
     * One row per survey report.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        for (OpenMetadataRootElement surveyReport : getSurveyReports().values())
        {
            Map<String, String> values = newValues();

            putSurveyReportValues(values, surveyReport);

            /*
             * The row's own identifier is the report's GUID, which comes from its header.
             */
            values.remove(ProductDataFieldDefinition.SURVEY_REPORT_GUID.getDisplayName());

            addRow(surveyReport, values);
        }
    }
}
