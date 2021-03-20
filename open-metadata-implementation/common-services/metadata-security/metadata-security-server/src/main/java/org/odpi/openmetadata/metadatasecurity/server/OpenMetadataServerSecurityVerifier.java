/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.server;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.metadatasecurity.*;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.metadatasecurity.properties.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OpenMetadataEventsSecurity;

import java.util.ArrayList;
import java.util.List;


/**
 * OpenMetadataServerSecurityVerifier provides the plug-in point for the open metadata server connector.
 * It supports the same security interfaces, and handles the fact that the security connector is
 * optional.
 */
public class OpenMetadataServerSecurityVerifier implements OpenMetadataRepositorySecurity,
                                                           OpenMetadataEventsSecurity,
                                                           OpenMetadataServerSecurity,
                                                           OpenMetadataServiceSecurity,
                                                           OpenMetadataConnectionSecurity,
                                                           OpenMetadataAssetSecurity
{
    private OpenMetadataRepositorySecurity repositorySecurityConnector = null;
    private OpenMetadataEventsSecurity     eventsSecurityConnector     = null;
    private OpenMetadataServerSecurity     serverSecurityConnector     = null;
    private OpenMetadataServiceSecurity    serviceSecurityConnector    = null;
    private OpenMetadataConnectionSecurity connectionSecurityConnector = null;
    private OpenMetadataAssetSecurity      assetSecurityConnector      = null;

    /**
     * Default constructor
     */
    public OpenMetadataServerSecurityVerifier()
    {
    }


    /**
     * Register an open metadata server security connector to verify access to the server's services.
     *
     * @param localServerUserId local server's userId
     * @param serverName local server's name
     * @param auditLog logging destination
     * @param connection properties used to create the connector
     *
     * @throws InvalidParameterException the connection is invalid
     */
    synchronized public  void registerSecurityValidator(String                                                                    localServerUserId,
                                                        String                                                                    serverName,
                                                        AuditLog                                                                  auditLog,
                                                        org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection   connection) throws InvalidParameterException
    {
        OpenMetadataServerSecurityConnector connector;

        try
        {
            connector = this.getServerSecurityConnector(localServerUserId,
                                                        serverName,
                                                        auditLog,
                                                        connection);

            if (connector instanceof OpenMetadataRepositorySecurity)
            {
                repositorySecurityConnector = (OpenMetadataRepositorySecurity)connector;
            }
            if (connector instanceof OpenMetadataEventsSecurity)
            {
                eventsSecurityConnector = (OpenMetadataEventsSecurity)connector;
            }
            if (connector instanceof OpenMetadataServerSecurity)
            {
                serverSecurityConnector = (OpenMetadataServerSecurity)connector;
            }
            if (connector instanceof OpenMetadataServiceSecurity)
            {
                serviceSecurityConnector = (OpenMetadataServiceSecurity)connector;
            }
            if (connector instanceof OpenMetadataConnectionSecurity)
            {
                connectionSecurityConnector = (OpenMetadataConnectionSecurity)connector;
            }
            if (connector instanceof OpenMetadataAssetSecurity)
            {
                assetSecurityConnector = (OpenMetadataAssetSecurity)connector;
            }
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Return the Open Metadata Server Security Connector for the connection.
     *
     * @param localServerUserId userId of the server
     * @param serverName name of the server
     * @param auditLog logging destination
     * @param connection connection from the configuration document
     * @return connector or null
     * @throws InvalidParameterException connection did not create a connector
     */
    private   OpenMetadataServerSecurityConnector getServerSecurityConnector(String                                                                    localServerUserId,
                                                                             String                                                                    serverName,
                                                                             AuditLog                                                                  auditLog,
                                                                             org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection   connection) throws InvalidParameterException
    {
        final String methodName = "getServerSecurityConnector";

        OpenMetadataServerSecurityConnector serverSecurityConnector = null;

        if (connection != null)
        {
            try
            {
                ConnectorBroker connectorBroker = new ConnectorBroker();
                Connector       connector       = connectorBroker.getConnector(connection);

                serverSecurityConnector = (OpenMetadataServerSecurityConnector)connector;

                serverSecurityConnector.setAuditLog(auditLog);
                serverSecurityConnector.setServerName(serverName);
                serverSecurityConnector.setLocalServerUserId(localServerUserId);
                serverSecurityConnector.start();
            }
            catch (Throwable error)
            {
                /*
                 * The assumption is that any exceptions creating the new connector are down to a bad connection
                 */
                throw new InvalidParameterException(OpenMetadataSecurityErrorCode.BAD_SERVER_SECURITY_CONNECTION.getMessageDefinition(serverName,
                                                                                                                                      error.getMessage(),
                                                                                                                                      connection.toString()),
                                                    OpenMetadataPlatformSecurityVerifier.class.getName(),
                                                    methodName,
                                                    error,
                                                    "connection");
            }
        }

        return serverSecurityConnector;
    }


    /**
     * Determine the appropriate setting for the supported zones depending on the user and the
     * default supported zones set up for the service.  This is called whenever an asset is accessed.
     *
     * @param supportedZones default setting of the supported zones for the service
     * @param serviceName name of the called service
     * @param user name of the user
     *
     * @return list of supported zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Override
    public List<String> setSupportedZonesForUser(List<String>  supportedZones,
                                                 String        serviceName,
                                                 String        user) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (assetSecurityConnector != null)
        {
            return assetSecurityConnector.setSupportedZonesForUser(supportedZones, serviceName, user);
        }

        return supportedZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * default zones.  This is called whenever a new asset is created.
     *
     * The default behavior is to use the default values, unless the zones have been explicitly set up,
     * in which case, they are left unchanged.
     *
     * @param defaultZones setting of the default zones for the service
     * @param ocfAsset initial values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Deprecated
    public List<String> initializeAssetZones(List<String>                                                       defaultZones,
                                             org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset ocfAsset) throws InvalidParameterException,
                                                                                                                                 PropertyServerException
    {
        List<String>  resultingZones = null;

        if (ocfAsset != null)
        {
            Asset asset = getAssetFromOCFAsset(ocfAsset);
            if ((ocfAsset.getZoneMembership() == null) || (ocfAsset.getZoneMembership().isEmpty()))
            {
                resultingZones = defaultZones;
            }
            else
            {
                resultingZones = ocfAsset.getZoneMembership();
            }

            if (assetSecurityConnector != null)
            {
                return assetSecurityConnector.setAssetZonesToDefault(resultingZones, asset);
            }
        }

        return resultingZones;
    }


    /**
     * Transform an OCF Asset in to a metadata security asset
     *
     * @param ocfAsset asset from caller
     * @return asset for security connector
     */
    private Asset getAssetFromOCFAsset(org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset ocfAsset)
    {
        Asset asset = new Asset();

        if (ocfAsset.getType() != null)
        {
            asset.setTypeName(ocfAsset.getType().getElementTypeName());
        }
        asset.setGUID(ocfAsset.getGUID());
        asset.setQualifiedName(ocfAsset.getQualifiedName());
        asset.setDisplayName(ocfAsset.getDisplayName());
        asset.setZoneMembership(ocfAsset.getZoneMembership());
        asset.setOwner(ocfAsset.getOwner());
        if (ocfAsset.getOwnerType() != null)
        {
            asset.setOwnerType(ocfAsset.getOwnerType().getOpenTypeOrdinal());
        }

        return asset;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * default zones.  This is called whenever a new asset is created.
     *
     * The default behavior is to use the default values, unless the zones have been explicitly set up,
     * in which case, they are left unchanged.
     *
     * @param defaultZones setting of the default zones for the service
     * @param asset initial values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Override
    public List<String> setAssetZonesToDefault(List<String>  defaultZones,
                                               Asset         asset) throws InvalidParameterException,
                                                                           PropertyServerException
    {
        List<String>  resultingZones = null;

        if (asset != null)
        {
            if ((asset.getZoneMembership() == null) || (asset.getZoneMembership().isEmpty()))
            {
                resultingZones = defaultZones;
            }
            else
            {
                resultingZones = asset.getZoneMembership();
            }
        }

        if (assetSecurityConnector != null)
        {
            return assetSecurityConnector.setAssetZonesToDefault(resultingZones, asset);
        }

        return resultingZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * settings of both default zones and supported zones.  This method is called whenever an asset's
     * values are changed.
     *
     * The default behavior is to keep the updated zones as they are.
     *
     * @param defaultZones setting of the default zones for the service
     * @param supportedZones setting of the supported zones for the service
     * @param originalAsset original values for the asset
     * @param updatedAsset updated values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Deprecated
    @Override
    public List<String> verifyAssetZones(List<String>  defaultZones,
                                         List<String>  supportedZones,
                                         Asset         originalAsset,
                                         Asset         updatedAsset) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (assetSecurityConnector != null)
        {
            return assetSecurityConnector.verifyAssetZones(defaultZones, supportedZones, originalAsset, updatedAsset);
        }

        List<String>  resultingZones = null;

        if (updatedAsset != null)
        {
            resultingZones = updatedAsset.getZoneMembership();
        }

        return resultingZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * settings of both default zones and supported zones.  This method is called whenever an asset's
     * values are changed.
     *
     * The default behavior is to keep the updated zones as they are.
     *
     * @param defaultZones setting of the default zones for the service
     * @param supportedZones setting of the supported zones for the service
     * @param ocfOriginalAsset original values for the asset
     * @param ocfUpdatedAsset updated values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Deprecated
    public List<String> verifyAssetZones(List<String>                                                       defaultZones,
                                         List<String>                                                       supportedZones,
                                         org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset ocfOriginalAsset,
                                         org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset ocfUpdatedAsset) throws InvalidParameterException,
                                                                                                                                    PropertyServerException
    {
        if (assetSecurityConnector != null)
        {
            Asset originalAsset = this.getAssetFromOCFAsset(ocfOriginalAsset);
            Asset updatedAsset = this.getAssetFromOCFAsset(ocfUpdatedAsset);

            return assetSecurityConnector.verifyAssetZones(defaultZones, supportedZones, null, originalAsset, updatedAsset);
        }

        List<String>  resultingZones = null;

        if (ocfUpdatedAsset != null)
        {
            resultingZones = ocfUpdatedAsset.getZoneMembership();
        }

        return resultingZones;
    }



    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * settings of both default zones and supported zones.  This method is called whenever an asset's
     * values are changed.
     *
     * The default behavior is to keep the updated zones as they are.
     *
     * @param defaultZones setting of the default zones for the service
     * @param supportedZones setting of the supported zones for the service
     * @param originalAsset original values for the asset
     * @param updatedAsset updated values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Override
    public List<String> verifyAssetZones(List<String>  defaultZones,
                                         List<String>  supportedZones,
                                         List<String>  publishZones,
                                         Asset         originalAsset,
                                         Asset         updatedAsset) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (assetSecurityConnector != null)
        {
            return assetSecurityConnector.verifyAssetZones(defaultZones, supportedZones, publishZones, originalAsset, updatedAsset);
        }

        List<String>  resultingZones = null;

        if (updatedAsset != null)
        {
            resultingZones = updatedAsset.getZoneMembership();
        }

        return resultingZones;
    }


    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    @Override
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserForServer(userId);
        }
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    @Override
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerAdmin(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    @Override
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerOperator(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    @Override
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerInvestigator(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue this request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForService(String   userId,
                                        String   serviceName) throws UserNotAuthorizedException
    {
        if (serviceSecurityConnector != null)
        {
            serviceSecurityConnector.validateUserForService(userId, serviceName);
        }
    }


    /**
     * Check that the calling user is authorized to issue this specific request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     * @param serviceOperationName name of called operation
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException
    {
        if (serviceSecurityConnector != null)
        {
            serviceSecurityConnector.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
        }
    }


    /**
     * Tests for whether a specific user should have access to a connection.
     *
     * @param userId identifier of user
     * @param connection connection object
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForConnection(String     userId,
                                           Connection connection) throws UserNotAuthorizedException
    {
        if (connectionSecurityConnector != null)
        {
            connectionSecurityConnector.validateUserForConnection(userId, new Connection(connection));
        }
    }



    /**
     * Select a connection from the list of connections attached to an asset.
     *
     * @param userId calling user
     * @param asset asset requested by caller
     * @param connections list of attached connections
     * @return selected connection or null (pretend there are no connections attached to the asset) or
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public Connection validateUserForAssetConnectionList(String           userId,
                                                         Asset            asset,
                                                         List<Connection> connections) throws UserNotAuthorizedException
    {
        if (connectionSecurityConnector != null)
        {
            List<Connection> clonedConnections;
            if ((connections == null) || (connections.isEmpty()))
            {
                clonedConnections = connections;
            }
            else
            {
                clonedConnections = new ArrayList<>();

                for (Connection connection: connections)
                {
                    clonedConnections.add(new Connection(connection));
                }
            }

            connectionSecurityConnector.validateUserForAssetConnectionList(userId, new Asset(asset), clonedConnections);
        }
        else
        {
            /*
             * If there is no security connector installed in this server, return the first non-null connection in the list.
             * If there are no nun-null connections in the list it drops through to return null.
             */
            if ((connections != null) && (! connections.isEmpty()))
            {
                for (Connection connection : connections)
                {
                    if (connection != null)
                    {
                        return connection;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Tests for whether a specific user should have the right to create an asset within a zone.
     *
     * @param userId identifier of user
     * @param ocfAsset asset from an OCF based module
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    @Deprecated
    public void  validateUserForAssetCreate(String                                                             userId,
                                            org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset ocfAsset) throws UserNotAuthorizedException
    {
        if ((assetSecurityConnector != null) && (ocfAsset != null))
        {
            Asset asset = this.getAssetFromOCFAsset(ocfAsset);

            assetSecurityConnector.validateUserForAssetCreate(userId, asset);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create an asset within a zone.
     *
     * @param userId identifier of user
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    @Override
    public void  validateUserForAssetCreate(String     userId,
                                            Asset      asset) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            assetSecurityConnector.validateUserForAssetCreate(userId, new Asset(asset));
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific asset within a zone.
     *
     * @param userId identifier of user
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    @Override
    public void  validateUserForAssetRead(String     userId,
                                          Asset      asset) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            assetSecurityConnector.validateUserForAssetRead(userId, new Asset(asset));
        }
    }


    /**
     * Tests for whether a specific user should have the right to update an asset.
     * This is used for a general asset update, which may include changes to the
     * zones and the ownership.
     *
     * @param userId identifier of user
     * @param ocfOriginalAsset original asset details
     * @param originalAssetAuditHeader details of the asset's audit header
     * @param ocfNewAsset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Deprecated
    public void  validateUserForAssetDetailUpdate(String                                                             userId,
                                                  org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset ocfOriginalAsset,
                                                  AssetAuditHeader                                                   originalAssetAuditHeader,
                                                  org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset ocfNewAsset) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            Asset originalAsset = this.getAssetFromOCFAsset(ocfOriginalAsset);
            Asset newAsset = this.getAssetFromOCFAsset(ocfNewAsset);

            assetSecurityConnector.validateUserForAssetDetailUpdate(userId, originalAsset, originalAssetAuditHeader, newAsset);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update an asset.
     * This is used for a general asset update, which may include changes to the
     * zones and the ownership.
     *
     * @param userId identifier of user
     * @param originalAsset original asset details
     * @param originalAssetAuditHeader details of the asset's audit header
     * @param newAsset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Override
    public void  validateUserForAssetDetailUpdate(String           userId,
                                                  Asset            originalAsset,
                                                  AssetAuditHeader originalAssetAuditHeader,
                                                  Asset            newAsset) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            assetSecurityConnector.validateUserForAssetDetailUpdate(userId, originalAsset, originalAssetAuditHeader, new Asset(newAsset));
        }
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an asset such as schema and connections.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Override
    public void  validateUserForAssetAttachmentUpdate(String     userId,
                                                      Asset      asset) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            assetSecurityConnector.validateUserForAssetAttachmentUpdate(userId, new Asset(asset));
        }
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the asset.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Override
    public void  validateUserForAssetFeedback(String     userId,
                                              Asset      asset) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            assetSecurityConnector.validateUserForAssetFeedback(userId, new Asset(asset));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an asset within a zone.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Override
    public void  validateUserForAssetDelete(String     userId,
                                            Asset      asset) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            assetSecurityConnector.validateUserForAssetDelete(userId, new Asset(asset));
        }
    }


    /*
     * =========================================================================================================
     * Type security
     */

    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeCreate(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String           userId,
                                           String           metadataCollectionName,
                                           AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeCreate(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String              userId,
                                         String              metadataCollectionName,
                                         AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @param patch changes to the type
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeUpdate(String       userId,
                                           String       metadataCollectionName,
                                           TypeDef      typeDef,
                                           TypeDefPatch patch) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef, patch);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String              userId,
                                           String              metadataCollectionName,
                                           AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeDelete(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String  userId,
                                               String  metadataCollectionName,
                                               TypeDef originalTypeDef,
                                               String  newTypeDefGUID,
                                               String  newTypeDefName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeReIdentify(userId, metadataCollectionName, originalTypeDef, newTypeDefGUID, newTypeDefName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalAttributeTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String           userId,
                                               String           metadataCollectionName,
                                               AttributeTypeDef originalAttributeTypeDef,
                                               String           newTypeDefGUID,
                                               String           newTypeDefName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeReIdentify(userId,
                                                                      metadataCollectionName,
                                                                      originalAttributeTypeDef,
                                                                      newTypeDefGUID,
                                                                      newTypeDefName);
        }
    }


    /*
     * =========================================================================================================
     * Instance Security
     *
     * No specific security checks made when instances are written and retrieved from the local repository.
     * The methods override the super class that throws a user not authorized exception on all access/update
     * requests.
     */

    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityCreate(String                     userId,
                                             String                     metadataCollectionName,
                                             String                     entityTypeGUID,
                                             InstanceProperties         initialProperties,
                                             List<Classification>       initialClassifications,
                                             InstanceStatus             initialStatus) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityCreate(userId,
                                                                    metadataCollectionName,
                                                                    entityTypeGUID,
                                                                    initialProperties,
                                                                    initialClassifications,
                                                                    initialStatus);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return entity to return (may be altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public EntityDetail  validateUserForEntityRead(String          userId,
                                                   String          metadataCollectionName,
                                                   EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateUserForEntityRead(userId, metadataCollectionName, new EntityDetail(instance));
        }

        return instance;
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntitySummaryRead(userId, metadataCollectionName, new EntitySummary(instance));
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityProxyRead(userId, metadataCollectionName, new EntityProxy(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityUpdate(userId, metadataCollectionName, new EntityDetail(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to add a classification to an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationAdd(String               userId,
                                                        String               metadataCollectionName,
                                                        EntityDetail         instance,
                                                        String               classificationName,
                                                        InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationAdd(userId,
                                                             metadataCollectionName,
                                                             instance,
                                                             classificationName,
                                                             properties);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationUpdate(String               userId,
                                                           String               metadataCollectionName,
                                                           EntityDetail         instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationUpdate(userId,
                                                                metadataCollectionName,
                                                                instance,
                                                                classificationName,
                                                                properties);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a classification from an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationDelete(String               userId,
                                                           String               metadataCollectionName,
                                                           EntityDetail         instance,
                                                           String               classificationName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationDelete(userId,
                                                                metadataCollectionName,
                                                                instance,
                                                                classificationName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityDelete(userId, metadataCollectionName, new EntityDetail(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityRestore(String       userId,
                                              String       metadataCollectionName,
                                              String       deletedEntityGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityRestore(userId, metadataCollectionName, deletedEntityGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReIdentification(String       userId,
                                                       String       metadataCollectionName,
                                                       EntityDetail instance,
                                                       String       newGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReIdentification(userId, metadataCollectionName, instance, newGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the type of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReTyping(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReHoming(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               String         newHomeMetadataCollectionId,
                                               String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReHoming(userId,
                                                    metadataCollectionName,
                                                    instance,
                                                    newHomeMetadataCollectionId,
                                                    newHomeMetadataCollectionName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneSummary the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoSummary the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipCreate(String               userId,
                                                   String               metadataCollectionName,
                                                   String               relationshipTypeGUID,
                                                   InstanceProperties   initialProperties,
                                                   EntitySummary        entityOneSummary,
                                                   EntitySummary        entityTwoSummary,
                                                   InstanceStatus       initialStatus) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipCreate(userId,
                                                        metadataCollectionName,
                                                        relationshipTypeGUID,
                                                        initialProperties,
                                                        entityOneSummary,
                                                        entityTwoSummary,
                                                        initialStatus);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public Relationship  validateUserForRelationshipRead(String          userId,
                                                         String          metadataCollectionName,
                                                         Relationship    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateUserForRelationshipRead(userId, metadataCollectionName, new Relationship(instance));
        }

        return instance;
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipUpdate(userId, metadataCollectionName, new Relationship(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipDelete(userId, metadataCollectionName, new Relationship(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipRestore(String       userId,
                                                    String       metadataCollectionName,
                                                    String       deletedRelationshipGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityRestore(userId, metadataCollectionName, deletedRelationshipGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReIdentification(String       userId,
                                                             String       metadataCollectionName,
                                                             Relationship instance,
                                                             String       newGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReIdentification(userId, metadataCollectionName, instance, newGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the type of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReTyping(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReHoming(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     String         newHomeMetadataCollectionId,
                                                     String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReHoming(userId,
                                                          metadataCollectionName,
                                                          instance,
                                                          newHomeMetadataCollectionId,
                                                          newHomeMetadataCollectionName);
        }
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     */
    public boolean  validateEntityReferenceCopySave(EntityDetail   instance)
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateEntityReferenceCopySave(instance);
        }

        return true;
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     */
    public boolean  validateRelationshipReferenceCopySave(Relationship   instance)
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateRelationshipReferenceCopySave(instance);
        }

        return true;
    }


    /**
     * Validate whether an event received from another member of the cohort should be processed
     * by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return inbound event to process (may be updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateInboundEvent(String            cohortName,
                                                  OMRSInstanceEvent event)
    {
        if (eventsSecurityConnector != null)
        {
            return eventsSecurityConnector.validateInboundEvent(cohortName, event);
        }

        return event;
    }


    /**
     * Validate whether an event should be sent to the other members of the cohort by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return outbound event to send (may be updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateOutboundEvent(String            cohortName,
                                                   OMRSInstanceEvent event)
    {
        if (eventsSecurityConnector != null)
        {
            return eventsSecurityConnector.validateOutboundEvent(cohortName, event);
        }

        return event;
    }
}
