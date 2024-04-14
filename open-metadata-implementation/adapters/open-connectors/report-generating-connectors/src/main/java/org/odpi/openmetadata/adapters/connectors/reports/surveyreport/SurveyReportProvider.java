/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.reports.surveyreport;

import org.odpi.openmetadata.adapters.connectors.reports.ReportGuard;
import org.odpi.openmetadata.adapters.connectors.reports.ReportRequestParameter;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionTarget;

/**
 * SurveyReportProvider is the OCF connector provider for the "SurveyReport" Governance Action Service.
 */
public class SurveyReportProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "f1071e6c-da8b-4a01-899a-14b9d24b1165";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Reporter:SurveyReport";
    private static final String  connectorTypeDisplayName = "Survey Report Governance Action Service";
    private static final String  connectorTypeDescription = "Outputs a survey report for an asset as a file.";


    private static final String connectorClassName = SurveyReportService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public SurveyReportProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = ReportRequestParameter.getRequestParameterTypes();
        supportedActionTargetTypes = SurveyActionTarget.getActionTargetTypes();
        producedGuards             = ReportGuard.getGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;
    }
}
