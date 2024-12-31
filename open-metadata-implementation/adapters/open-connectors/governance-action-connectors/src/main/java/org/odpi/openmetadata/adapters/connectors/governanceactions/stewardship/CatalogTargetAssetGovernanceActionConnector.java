/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CatalogTargetAssetGovernanceActionConnector creates a catalog target between the supplied integration connector
 * and the supplied asset.
 */
public class CatalogTargetAssetGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public CatalogTargetAssetGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        try
        {
            List<String>              outputGuards        = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;

            ActionTargetElement       integrationConnector = null;
            String                    newAssetGUID         = null;

            if (governanceContext.getActionTargetElements() != null)
            {
                for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
                {
                    if (actionTargetElement != null)
                    {
                        if (ActionTarget.INTEGRATION_CONNECTOR.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            integrationConnector = actionTargetElement;
                        }
                        else if (ActionTarget.NEW_ASSET.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            newAssetGUID = actionTargetElement.getActionTargetGUID();
                        }
                    }
                }
            }

            if (integrationConnector == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_CONNECTOR.getMessageDefinition(governanceServiceName);
                outputGuards.add(CatalogTargetAssetGuard.MISSING_CONNECTOR.getName());
                completionStatus = CatalogTargetAssetGuard.MISSING_CONNECTOR.getCompletionStatus();
            }
            else if (newAssetGUID == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ActionTarget.NEW_ASSET.getName());
                outputGuards.add(CatalogTargetAssetGuard.MISSING_ASSET.getName());
                completionStatus = CatalogTargetAssetGuard.MISSING_ASSET.getCompletionStatus();
            }
            else
            {
                OpenMetadataElement serverElement = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(newAssetGUID);

                String serverName = propertyHelper.getStringProperty(governanceServiceName,
                                                                     OpenMetadataProperty.NAME.name,
                                                                     serverElement.getElementProperties(),
                                                                     methodName);

                String serverType = serverElement.getType().getTypeName();
                String integrationConnectorName = propertyHelper.getStringProperty(governanceServiceName,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   integrationConnector.getTargetElement().getElementProperties(),
                                                                                   methodName);

                ElementProperties catalogTargetProperties = propertyHelper.addStringProperty(null,
                                                                                             OpenMetadataType.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                                                             serverName);

                catalogTargetProperties = propertyHelper.addMapProperty(catalogTargetProperties,
                                                                        OpenMetadataProperty.CONFIGURATION_PROPERTIES.name,
                                                                        this.combineProperties());

                governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                                                      integrationConnector.getTargetElement().getElementGUID(),
                                                                                      newAssetGUID,
                                                                                      null,
                                                                                      null,
                                                                                      catalogTargetProperties);

                messageDefinition = GovernanceActionConnectorsAuditCode.CONNECTOR_CONFIGURED.getMessageDefinition(integrationConnectorName,
                                                                                                                  serverType,
                                                                                                                  serverName);

                completionStatus = CatalogTargetAssetGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CatalogTargetAssetGuard.SET_UP_COMPLETE.getName());
            }

            auditLog.logMessage(methodName, messageDefinition);

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Combine the configuration properties and request parameters to pass them to the integration connector
     * as configuration properties.
     *
     * @return map
     */
    Map<String, Object> combineProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        if (connectionProperties.getConfigurationProperties() != null)
        {
            configurationProperties.putAll(connectionProperties.getConfigurationProperties());
        }

        if (governanceContext.getRequestParameters() != null)
        {
            configurationProperties.putAll(governanceContext.getRequestParameters());
        }

        if (configurationProperties.isEmpty())
        {
            return null;
        }

        return configurationProperties;
    }
}
