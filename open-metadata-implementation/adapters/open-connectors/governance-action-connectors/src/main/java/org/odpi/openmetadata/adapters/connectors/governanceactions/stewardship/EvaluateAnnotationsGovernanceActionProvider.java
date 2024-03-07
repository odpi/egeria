/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

/**
 * EvaluateAnnotationsGovernanceActionProvider is the OCF connector provider for the "Evaluate Annotations"
 * Governance Action Service.
 */
public class EvaluateAnnotationsGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "02e8bace-0035-4e80-afef-f5677a8c5ba4";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Verification:EvaluateAnnotations";
    private static final String  connectorTypeDisplayName = "Evaluate Annotations Governance Action Service";
    private static final String  connectorTypeDescription = "Verification Governance Action Service that checks whether there are Request For Action Annotations in a survey report.";

    private static final String connectorClassName = EvaluateAnnotationsGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public EvaluateAnnotationsGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        super.producedGuards            = EvaluateAnnotationsGuard.getGuardTypes();
        super.producedActionTargetTypes = StewardshipHandoverActionTarget.getActionTargetTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);


        super.connectorTypeBean = connectorType;
    }
}
