/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.catalog;

import org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIAuditCode;
import org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIErrorCode;
import org.odpi.openmetadata.adapters.connectors.wso2mi.properties.APIInfo;
import org.odpi.openmetadata.adapters.connectors.wso2mi.resource.WSO2MIResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * WSO2MIIntegrationConnector catalogs the REST APIs deployed on a WSO2 Micro Integrator instance as
 * open metadata assets.
 *
 * <p><b>Status: v1 scaffold (odpi/egeria#9245).</b>  The connector wiring is in place; the mapping of a
 * {@link APIInfo} onto open metadata elements (a {@code DeployedAPI} / {@code Asset} plus a connection)
 * is the next increment and is marked with {@code TODO} below.  Two design points are still being agreed
 * with the Egeria maintainers:</p>
 * <ul>
 *     <li>whether this should extend {@code DynamicIntegrationConnectorBase} with a catalog-target
 *     processor (as {@code OracleServerIntegrationConnector} does) or the simpler
 *     {@code IntegrationConnectorBase} shown here, given a Micro Integrator is a single addressable
 *     instance rather than a server hosting many sub-databases;</li>
 *     <li>the exact open metadata type to represent a deployed API.</li>
 * </ul>
 */
public class WSO2MIIntegrationConnector extends IntegrationConnectorBase
{
    private WSO2MIResourceConnector resourceConnector = null;


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException  the connector detected a problem
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        /*
         * The resource connector that talks to the Micro Integrator Management API is supplied as an
         * embedded connector on this integration connector's connection.
         */
        for (Connector embeddedConnector : super.embeddedConnectors)
        {
            if (embeddedConnector instanceof WSO2MIResourceConnector wso2MIResourceConnector)
            {
                this.resourceConnector = wso2MIResourceConnector;
                this.resourceConnector.start();
                break;
            }
        }

        if (this.resourceConnector == null)
        {
            throw new ConnectorCheckedException(WSO2MIErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         "NullResourceConnector",
                                                                                                         methodName,
                                                                                                         "no embedded WSO2MIResourceConnector was supplied on the connection"),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Called at regular intervals to synchronise the state of the Micro Integrator with the open
     * metadata ecosystem.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to catalog the Micro Integrator.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            List<APIInfo> deployedAPIs = resourceConnector.listAPIs();

            for (APIInfo apiInfo : deployedAPIs)
            {
                /*
                 * TODO (odpi/egeria#9245): create/update an open metadata asset for this deployed API.
                 *   - resolve the DeployedAPI/Asset type
                 *   - apply the include/exclude filters from WSO2MIConfigurationProperty
                 *   - skip APIs that are already catalogued (idempotency, as OracleServerIntegrationConnector does)
                 *   - emit WSO2MIAuditCode.CATALOGED_API / SKIPPING_API
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        WSO2MIAuditCode.CATALOGED_API.getMessageDefinition(connectorName,
                                                                                          apiInfo.getName(),
                                                                                          apiInfo.getUrl()));
                }
            }
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(WSO2MIErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
