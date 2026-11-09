/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;


/**
 * SurveyReportListTabularDataSetProvider is the connector provider for the SurveyReportListTabularDataSetConnector
 * that lists the survey reports stored in open metadata.
 */
public class SurveyReportListTabularDataSetProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = SurveyReportListTabularDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public SurveyReportListTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.SURVEY_REPORT_LIST_TABULAR_DATA_SET,
              connectorClassName,
              List.of(TabularDataSetConfigurationProperty.SERVER_NAME.name,
                      TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.name));
    }
}
