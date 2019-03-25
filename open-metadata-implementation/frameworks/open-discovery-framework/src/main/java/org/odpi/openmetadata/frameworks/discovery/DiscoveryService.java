/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryServiceException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.ODFErrorCode;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryContext;


/**
 * DiscoveryService describes a specific type of connector that is responsible for analyzing the content
 * of a specific asset.  Information about the asset to analyze is passed in the discovery context.
 * The returned discovery context also contains the results.
 *
 * Some discovery services manage the invocation of other discovery services.  These discovery services are called
 * discovery pipelines.
 */
public abstract class DiscoveryService extends ConnectorBase
{
    protected String           discoveryServiceName = null;
    protected DiscoveryContext discoveryContext = null;


    /**
     * Set up details of the asset to analyze and the results of any previous analysis.
     *
     * @param discoveryContext information about the asset to analyze and the results of analysis of
     *                         other discovery service request.  Partial results from other discovery
     *                         services run as part of the same discovery service request may also be
     *                         stored in the newAnnotations list.
     */
    public synchronized void setDiscoveryContext(DiscoveryContext discoveryContext)
    {
        this.discoveryContext = discoveryContext;
    }


    /**
     * Return the discovery context for this discovery service.  This is typically called after the disconnect()
     * method is called.  If called before disconnect(), it may only contain partial results.
     *
     * @return discovery context containing the results discovered (so far) by the discovery service.
     */
    public synchronized DiscoveryContext getDiscoveryContext()
    {
        return discoveryContext;
    }

    /**
     * Indicates that the discovery service is completely configured and can begin processing.
     *
     * @throws DiscoveryServiceException there is a problem within the discovery service.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();

        if (super.getConnection() != null)
        {
            discoveryServiceName = super.getConnection().getConnectionName();
        }

        if (discoveryContext == null)
        {
            final String methodName = "start";
            ODFErrorCode errorCode    = ODFErrorCode.NULL_DISCOVERY_CONTEXT;
            String       errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(discoveryServiceName);

            throw new DiscoveryServiceException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
