/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction;

import org.odpi.openmetadata.adapters.connectors.surveyaction.ffdc.SurveyServiceErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * AuditableSurveyService is a base class for survey action services that provides common error handling routines.
 */
public abstract class AuditableSurveyService extends SurveyActionServiceConnector
{
    /**
     * Log that no asset has been returned to the survey action service.  It is unable to proceed without this basic information.
     *
     * @param assetGUID the unique identifier of the asset from the discovery context
     * @param methodName calling method
     *
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwNoAsset(String    assetGUID,
                                String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SurveyServiceErrorCode.NO_ASSET.getMessageDefinition(assetGUID,
                                                                                                 surveyActionServiceName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the survey action service can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetType type of the asset
     * @param supportedAssetType supported asset types
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfAsset(String    assetGUID,
                                         String    assetType,
                                         String    supportedAssetType,
                                         String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SurveyServiceErrorCode.INVALID_ASSET_TYPE.getMessageDefinition(assetGUID,
                                                                                                           assetType,
                                                                                                           surveyActionServiceName,
                                                                                                           supportedAssetType),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the survey action service can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetTypeName type of the asset
     * @param assetResourceName name of the resource
     * @param assetResourceType type of the resource
     * @param supportedResourceType supported resource types
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfResource(String assetGUID,
                                            String assetTypeName,
                                            String assetResourceName,
                                            String assetResourceType,
                                            String supportedResourceType,
                                            String methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SurveyServiceErrorCode.INVALID_RESOURCE.getMessageDefinition(assetTypeName,
                                                                                                         assetGUID,
                                                                                                         assetResourceName,
                                                                                                         assetResourceType,
                                                                                                         surveyActionServiceName,
                                                                                                         supportedResourceType),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the survey action service can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetTypeName type of the asset
     * @param assetResourceName name of the resource
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwMissingResource(String assetGUID,
                                        String assetTypeName,
                                        String assetResourceName,
                                        String methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SurveyServiceErrorCode.NO_RESOURCE.getMessageDefinition(assetTypeName,
                                                                                                    assetGUID,
                                                                                                    assetResourceName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Log that the survey action service can not process the type of root schema it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param rootSchemaType type of the root schema
     * @param supportedRootSchemaType supported root schema types
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfRootSchema(String    assetGUID,
                                              String    rootSchemaType,
                                              String    supportedRootSchemaType,
                                              String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SurveyServiceErrorCode.INVALID_ROOT_SCHEMA_TYPE.getMessageDefinition(assetGUID,
                                                                                                                 rootSchemaType,
                                                                                                                 surveyActionServiceName,
                                                                                                                 supportedRootSchemaType),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the type name for the asset.  An exception is thrown if the type name is not available.
     *
     * @param asset asset universe
     * @param methodName calling method
     * @return asset's type name
     * @throws ConnectorCheckedException resulting exception
     */
    protected String getAssetTypeName(AssetUniverse     asset,
                                      String            methodName) throws ConnectorCheckedException
    {
        ElementType elementType = asset.getType();

        if (elementType != null)
        {
            return elementType.getTypeName();
        }

        throw new ConnectorCheckedException(SurveyServiceErrorCode.NO_ASSET_TYPE.getMessageDefinition(asset.toString(), surveyActionServiceName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the network address of this asset.
     *
     * @param asset asset to extract address from
     * @return the list of network addresses
     */
    protected List<String> getNetworkAddresses(AssetUniverse asset)
    {
        if (asset != null)
        {
            Connections connections = asset.getConnections();

            if (connections != null)
            {
                List<String>  networkAddresses = new ArrayList<>();

                while (connections.hasNext())
                {
                    Connection connectionProperties = connections.next();

                    if (connectionProperties != null)
                    {
                        Endpoint endpointProperties = connectionProperties.getEndpoint();

                        if (endpointProperties != null)
                        {
                            if (endpointProperties.getAddress() != null)
                            {
                                /*
                                 * Only add one copy of a specific address.
                                 */
                                if (! networkAddresses.contains(endpointProperties.getAddress()))
                                {
                                    networkAddresses.add(endpointProperties.getAddress());
                                }
                            }
                        }
                    }
                }

                if (! networkAddresses.isEmpty())
                {
                    return networkAddresses;
                }
            }
        }

        return null;
    }
}
