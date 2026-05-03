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
import java.util.List;


/**
 * Calculates the last time an update was made to the tabular data set that is the target and if it has changes since
 * the last refresh (or this is the first refresh), the DataScope classification is updated with the latest update time.
 * This will be detected as a change to the catalog target by any monitoring process.
 */
public class BabbageAnalyticalEngineTargetProcessor extends CatalogTargetProcessorBase
{
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
                 * action running that is spawned from this governance action type.
                 */
                AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.ENGINE_ACTION.typeName);

                List<OpenMetadataRootElement> activeEngineActions = assetClient.findProcesses(governanceActionTypeProperties.getQualifiedName(),
                                                                                              Collections.singletonList(ActivityStatus.IN_PROGRESS),
                                                                                              assetClient.getSearchOptions(0, 0));

                if ((activeEngineActions == null) || activeEngineActions.isEmpty())
                {
                    String engineActionGUID = integrationContext.getStewardshipAction().initiateGovernanceActionType(governanceActionTypeProperties.getQualifiedName(),
                                                                                                                     Collections.singletonList(integrationContext.getIntegrationConnectorGUID()),
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     connectorName,
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


}
