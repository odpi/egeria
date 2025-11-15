/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.properties;


import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;

import java.util.Objects;

/**
 * RequestedCatalogTarget describes a catalog target that an integration connector should refresh.
 */
public class RequestedCatalogTarget extends CatalogTarget
{
    protected final Connector            connectorToTarget;
    protected final CatalogTargetContext integrationContext;


    /**
     * Constructor for new catalog target processor
     *
     * @param template object to copy
     * @param connectorToTarget connector to access the target resource
     */
    public RequestedCatalogTarget(CatalogTarget        template,
                                  CatalogTargetContext catalogTargetContext,
                                  Connector            connectorToTarget)
    {
        super(template);

        this.connectorToTarget  = connectorToTarget;
        this.integrationContext = catalogTargetContext;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestedCatalogTarget(RequestedCatalogTarget template)
    {
        super(template);

        if (template != null)
        {
            connectorToTarget  = template.getConnectorToTarget();
            integrationContext = template.getIntegrationContext();
        }
        else
        {
            connectorToTarget = null;
            integrationContext = null;
        }
    }


    /**
     * Indicates that the catalog target processor is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        if (connectorToTarget != null)
        {
            connectorToTarget.start();
        }
    }


    /**
     * Return the connector to access the catalog target.
     *
     * @return connector
     */
    public Connector getConnectorToTarget()
    {
        return connectorToTarget;
    }



    /**
     * Retrieve the endpoint from the asset connection.
     *
     * @return endpoint or null
     */
    protected String getNetworkAddress()
    {
        Connection assetConnection = connectorToTarget.getConnection();

        if (assetConnection != null)
        {
            Endpoint endpointDetails = assetConnection.getEndpoint();

            if (endpointDetails != null)
            {
                return endpointDetails.getNetworkAddress();
            }
        }

        return null;
    }


    /**
     * Return the integration context for this catalog target.
     *
     * @return specialized context for this connector
     */
    public CatalogTargetContext getIntegrationContext()
    {
        return integrationContext;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RequestedCatalogTarget{" +
                "connectorToTarget=" + connectorToTarget +
                ", integrationContext=" + integrationContext +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RequestedCatalogTarget that = (RequestedCatalogTarget) objectToCompare;
        return Objects.equals(connectorToTarget, that.connectorToTarget) && Objects.equals(integrationContext, that.integrationContext);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connectorToTarget, integrationContext);
    }
}
