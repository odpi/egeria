/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.subscriptions;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.List;

/**
 * CancelSubscriptionGovernanceActionProvider is the OCF connector provider for the "Cancel Digital Subscription"
 * Governance Action Service.
 */
public class CancelSubscriptionGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "9c45474b-a685-40df-8b40-5b7e7988546a";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:DigitalSubscription:Cancel";
    private static final String  connectorTypeDisplayName = "Cancel Digital Subscription Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that cancels a digital subscription.";

    private static final String connectorClassName = CancelSubscriptionGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CancelSubscriptionGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = null;
        super.producedGuards = ManageDigitalSubscriptionGuard.getGuardTypes();
        super.supportedActionTargetTypes = ManageDigitalSubscriptionActionTarget.getCancelSubscriptionActionTargetTypes();
        super.producedActionTargetTypes = null;

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        super.supportedTechnologyTypes = null;

    }
}
