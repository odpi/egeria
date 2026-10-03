/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.catalog;

import org.odpi.openmetadata.adapters.connectors.wso2mi.controls.WSO2MIConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIAuditCode;
import org.odpi.openmetadata.adapters.connectors.wso2mi.ffdc.WSO2MIErrorCode;
import org.odpi.openmetadata.adapters.connectors.wso2mi.properties.APIInfo;
import org.odpi.openmetadata.adapters.connectors.wso2mi.resource.WSO2MIResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.DeployedAPIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Collections;
import java.util.List;

/**
 * WSO2MIIntegrationConnector catalogs the REST APIs deployed on a WSO2 Micro Integrator instance as
 * {@code DeployedAPI} open metadata assets.
 *
 * <p><b>Status: v1 (odpi/egeria#9245).</b>  This extends {@code IntegrationConnectorBase} with the
 * {@code WSO2MIResourceConnector} supplied as an embedded connector, matching the scaffold as originally
 * built.  Whether this should instead extend {@code DynamicIntegrationConnectorBase} with a catalog-target
 * processor (as {@code OracleServerIntegrationConnector} does) is still an open design point with the
 * maintainers -- a Micro Integrator is a single addressable instance rather than a server hosting many
 * sub-resources each needing their own catalog target, so the simpler embedded-connector shape was kept
 * for v1 rather than unilaterally adopting the catalog-target pattern.</p>
 *
 * <p>Each deployed API is catalogued as a self-anchored {@code DeployedAPI} asset (mirroring
 * {@code OpenAPIMonitorIntegrationConnector}'s creation pattern for the same open metadata type), keyed by
 * a qualified name built from the API's invocation URL -- the same identifier the Micro Integrator itself
 * treats as unique. No template or pre-registered content-pack element is required for v1.</p>
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
            List<String> excludedAPIs = super.getArrayConfigurationProperty(WSO2MIConfigurationProperty.EXCLUDE_API_LIST.getName(),
                                                                             connectionBean.getConfigurationProperties(),
                                                                             Collections.emptyList());

            List<String> includedAPIs = super.getArrayConfigurationProperty(WSO2MIConfigurationProperty.INCLUDE_API_LIST.getName(),
                                                                             connectionBean.getConfigurationProperties());

            List<APIInfo> deployedAPIs = resourceConnector.listAPIs();

            AssetClient apiClient = integrationContext.getAssetClient(OpenMetadataType.DEPLOYED_API.typeName);

            for (APIInfo apiInfo : deployedAPIs)
            {
                if (integrationContext.elementShouldBeCatalogued(apiInfo.getName(), excludedAPIs, includedAPIs))
                {
                    catalogAPI(apiClient, apiInfo, methodName);
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


    /**
     * Create the DeployedAPI asset for a deployed API if it is not already catalogued.  The qualified name
     * is built from the API's invocation URL, which is the identifier the Micro Integrator itself treats
     * as unique -- the same choice {@code OpenAPIMonitorIntegrationConnector} makes for its own DeployedAPI
     * assets.
     *
     * @param apiClient client for creating/finding DeployedAPI assets
     * @param apiInfo   the deployed API, as reported by the Management API
     * @param callingMethodName name of the calling method, for audit log messages
     *
     * @throws InvalidParameterException  one of the parameters passed to open metadata is invalid
     * @throws PropertyServerException    there is an issue with one of the open metadata repositories
     * @throws UserNotAuthorizedException the connector's userId is not able to create this type of element
     */
    private void catalogAPI(AssetClient apiClient,
                            APIInfo     apiInfo,
                            String      callingMethodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        String qualifiedName = "DeployedAPI:" + apiInfo.getUrl();

        String apiGUID = findExistingAsset(apiClient, qualifiedName);

        if (apiGUID != null)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(callingMethodName,
                                    WSO2MIAuditCode.SKIPPING_API.getMessageDefinition(connectorName,
                                                                                     apiInfo.getName(),
                                                                                     apiGUID));
            }
        }
        else
        {
            DeployedAPIProperties properties = new DeployedAPIProperties();

            properties.setQualifiedName(qualifiedName);
            properties.setDisplayName(apiInfo.getName());
            properties.setVersionIdentifier(apiInfo.getVersion());

            NewElementOptions newElementOptions = new NewElementOptions(apiClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(true);

            apiGUID = apiClient.createAsset(newElementOptions, null, properties, null);

            if (auditLog != null)
            {
                auditLog.logMessage(callingMethodName,
                                    WSO2MIAuditCode.CATALOGED_API.getMessageDefinition(connectorName,
                                                                                      apiInfo.getName(),
                                                                                      apiGUID));
            }
        }
    }


    /**
     * Find an existing DeployedAPI asset with the requested qualified name.  Mirrors
     * {@code OpenAPIMonitorIntegrationConnector}'s helper of the same purpose.
     *
     * @param assetClient client to search with
     * @param qualifiedName unique name to search for
     * @return GUID of the matching asset, or null if not found
     *
     * @throws InvalidParameterException  one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException    there is an issue with one of the open metadata repositories
     */
    private String findExistingAsset(AssetClient assetClient,
                                     String      qualifiedName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        List<OpenMetadataRootElement> elements = assetClient.getAssetsByName(qualifiedName, assetClient.getQueryOptions());

        if (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getProperties() instanceof AssetProperties assetProperties))
                {
                    if (qualifiedName.equals(assetProperties.getQualifiedName()))
                    {
                        return element.getElementHeader().getGUID();
                    }
                }
            }
        }

        return null;
    }
}
