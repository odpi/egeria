/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.babbage;


import org.odpi.openmetadata.adapters.connectors.babbage.ffdc.BabbageAuditCode;
import org.odpi.openmetadata.adapters.connectors.babbage.ffdc.BabbageErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Calculates the last time an update was made to the tabular data set that is the target and if it has changes since
 * the last refresh (or this is the first refresh), the DataScope classification is updated with the latest update time.
 * This will be detected as a change to the catalog target by any monitoring process.
 */
public class BabbageAnalyticalEngineTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * These are the statuses of an engine action that mean it is either running, or is on its way to running.
     * An engine action only reaches IN_PROGRESS once the engine host has claimed it and started the governance
     * service, so the earlier statuses must also be treated as "already requested" to avoid initiating a
     * duplicate engine action for the same governance action type.
     */
    private static final List<ActivityStatus> liveActivityStatuses = List.of(ActivityStatus.REQUESTED,
                                                                            ActivityStatus.APPROVED,
                                                                            ActivityStatus.WAITING,
                                                                            ActivityStatus.ACTIVATING,
                                                                            ActivityStatus.IN_PROGRESS,
                                                                            ActivityStatus.PAUSED);

    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public BabbageAnalyticalEngineTargetProcessor(CatalogTarget            catalogTarget,
                                                  CatalogTargetContext     catalogTargetContext,
                                                  Connector                connectorToTarget,
                                                  String                   connectorName,
                                                  AuditLog                 auditLog)
    {
        super(catalogTarget, catalogTargetContext, connectorToTarget, connectorName, auditLog);
    }




    /* ==============================================================================
     * Standard methods that trigger activity.
     */


    /**
     * Check whether the data set has changed since the last refresh.  If it has then update the asset's
     * DataScope classification.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is unable to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        super.refresh();

        try
        {
            OpenMetadataRootElement catalogTargetElement = this.getCatalogTargetElement();

            if ((catalogTargetElement != null) &&
                    (propertyHelper.isTypeOf(catalogTargetElement.getElementHeader(), OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName)) &&
                    (catalogTargetElement.getProperties() instanceof GovernanceActionTypeProperties governanceActionTypeProperties))
            {
                /*
                 * The catalog target is a governance action type.  Now check if there is an engine
                 * action running, or waiting to run, that is spawned from this governance action type.
                 */
                AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.ENGINE_ACTION.typeName);

                List<OpenMetadataRootElement> activeEngineActions = assetClient.findProcesses(governanceActionTypeProperties.getQualifiedName(),
                                                                                              liveActivityStatuses,
                                                                                              assetClient.getSearchOptions(0, 0));

                if ((activeEngineActions == null) || activeEngineActions.isEmpty())
                {
                    String engineActionGUID = integrationContext.getStewardshipAction().initiateGovernanceActionType(governanceActionTypeProperties.getQualifiedName(),
                                                                                                                     Collections.singletonList(integrationContext.getIntegrationConnectorGUID()),
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     this.getRequestParameters(),
                                                                                                                     connectorName,
                                                                                                                     null,
                                                                                                                     null);

                    auditLog.logMessage(methodName,
                                        BabbageAuditCode.NEW_ENGINE_ACTION.getMessageDefinition(connectorName,
                                                                                                engineActionGUID,
                                                                                                governanceActionTypeProperties.getQualifiedName(),
                                                                                                catalogTargetElement.getElementHeader().getGUID()));
                }
            }

        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BabbageAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                             error.getClass().getName(),
                                                                                             methodName,
                                                                                             error.getMessage()),
                                  error);


            throw new ConnectorCheckedException(BabbageErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the catalog target's configuration properties as request parameters for the governance action it starts.
     *
     * @return request parameters or null
     */
    private Map<String,String> getRequestParameters()
    {
        return getRequestParameters(this.getConfigurationProperties());
    }


    /**
     * Convert configuration properties to request parameters.  Request parameters are strings, so each value is
     * converted with toString(): a list becomes "[a, b]", which ConnectorBase.getArrayValue() reads back as a list.
     * Properties with no value are left out.
     *
     * @param configProperties configuration properties (may be null)
     * @return request parameters or null
     */
    static Map<String,String> getRequestParameters(Map<String, Object> configProperties)
    {
        if (configProperties != null)
        {
            Map<String, String> requestParameters = new HashMap<>();

            for (String key : configProperties.keySet())
            {
                if ((key != null) && (configProperties.get(key) != null))
                {
                    requestParameters.put(key, configProperties.get(key).toString());
                }
            }

            return requestParameters;
        }

        return null;
    }
}
