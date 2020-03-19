/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.server;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.*;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaPlatformSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaPlatformSecurityProvider;
import org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaServerSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaServerSecurityProvider;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.List;


/**
 * OpenMetadataServerSecurityVerifier provides the plug-in point for the open metadata server connector.
 * It supports the same security interfaces, and handles the fact that the security connector is
 * optional.
 */
public class OpenMetadataServerSecurityVerifier implements OpenMetadataRepositorySecurity,
                                                           OpenMetadataServerSecurity,
                                                           OpenMetadataServiceSecurity,
                                                           OpenMetadataConnectionSecurity,
                                                           OpenMetadataAssetSecurity
{
    private OpenMetadataServerSecurityConnector connector   = null;

    // Todo remove - temporary workaround to bring connectors into class path
    private CocoPharmaServerSecurityConnector   demoObject1 = null;
    private CocoPharmaServerSecurityProvider    demoObject2 = null;
    private CocoPharmaPlatformSecurityConnector demoObject3 = null;
    private CocoPharmaPlatformSecurityProvider  demoObject4 = null;

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
    synchronized public  void registerSecurityValidator(String       localServerUserId,
                                                        String       serverName,
                                                        AuditLog     auditLog,
                                                        Connection   connection) throws InvalidParameterException
    {
        try
        {
            this.connector = this.getServerSecurityConnector(localServerUserId,
                                                             serverName,
                                                             auditLog,
                                                             connection);
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
    private   OpenMetadataServerSecurityConnector getServerSecurityConnector(String       localServerUserId,
                                                                             String       serverName,
                                                                             AuditLog     auditLog,
                                                                             Connection   connection) throws InvalidParameterException
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
    public List<String> setSupportedZonesForUser(List<String>  supportedZones,
                                                 String        serviceName,
                                                 String        user) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (connector != null)
        {
            return connector.setSupportedZonesForUser(supportedZones, serviceName, user);
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
     * @param asset initial values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    public List<String> initializeAssetZones(List<String>  defaultZones,
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

        if (connector != null)
        {
            return connector.initializeAssetZones(resultingZones, asset);
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
    public List<String> verifyAssetZones(List<String>  defaultZones,
                                         List<String>  supportedZones,
                                         Asset         originalAsset,
                                         Asset         updatedAsset) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (connector != null)
        {
            return connector.verifyAssetZones(defaultZones, supportedZones, originalAsset, updatedAsset);
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
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForServer(userId);
        }
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserAsServerAdmin(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserAsServerOperator(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserAsServerInvestigator(userId);
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
    public void  validateUserForService(String   userId,
                                        String   serviceName) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForService(userId, serviceName);
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
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
        }
    }


    /**
     * Tests for whether a specific user should have access to a connection.
     *
     * @param userId identifier of user
     * @param connection connection object
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    public void  validateUserForConnection(String     userId,
                                           Connection connection) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForConnection(userId, new Connection(connection));
        }
    }


    /**
     * Tests for whether a specific user should have the right to create an asset within a zone.
     *
     * @param userId identifier of user
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    public void  validateUserForAssetCreate(String     userId,
                                            Asset      asset) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForAssetCreate(userId, new Asset(asset));
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific asset within a zone.
     *
     * @param userId identifier of user
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    public void  validateUserForAssetRead(String     userId,
                                          Asset      asset) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForAssetRead(userId, new Asset(asset));
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
    public void  validateUserForAssetDetailUpdate(String           userId,
                                                  Asset            originalAsset,
                                                  AssetAuditHeader originalAssetAuditHeader,
                                                  Asset            newAsset) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForAssetDetailUpdate(userId, originalAsset, originalAssetAuditHeader, new Asset(newAsset));
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
    public void  validateUserForAssetAttachmentUpdate(String     userId,
                                                      Asset      asset) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForAssetAttachmentUpdate(userId, new Asset(asset));
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
    public void  validateUserForAssetFeedback(String     userId,
                                              Asset      asset) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForAssetFeedback(userId, new Asset(asset));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an asset within a zone.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    public void  validateUserForAssetDelete(String     userId,
                                            Asset      asset) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForAssetDelete(userId, new Asset(asset));
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
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeCreate(userId, metadataCollectionName, typeDef);
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
    public void  validateUserForTypeCreate(String           userId,
                                           String           metadataCollectionName,
                                           AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeCreate(userId, metadataCollectionName, attributeTypeDef);
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
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
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
    public void  validateUserForTypeRead(String              userId,
                                         String              metadataCollectionName,
                                         AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
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
    public void  validateUserForTypeUpdate(String       userId,
                                           String       metadataCollectionName,
                                           TypeDef      typeDef,
                                           TypeDefPatch patch) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef, patch);
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
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
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
    public void  validateUserForTypeDelete(String              userId,
                                           String              metadataCollectionName,
                                           AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeDelete(userId, metadataCollectionName, attributeTypeDef);
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
    public void  validateUserForTypeReIdentify(String  userId,
                                               String  metadataCollectionName,
                                               TypeDef originalTypeDef,
                                               String  newTypeDefGUID,
                                               String  newTypeDefName) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeReIdentify(userId, metadataCollectionName, originalTypeDef, newTypeDefGUID, newTypeDefName);
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
    public void  validateUserForTypeReIdentify(String           userId,
                                               String           metadataCollectionName,
                                               AttributeTypeDef originalAttributeTypeDef,
                                               String           newTypeDefGUID,
                                               String           newTypeDefName) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForTypeReIdentify(userId,
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
    public void  validateUserForEntityCreate(String                     userId,
                                             String                     metadataCollectionName,
                                             String                     entityTypeGUID,
                                             InstanceProperties         initialProperties,
                                             List<Classification>       initialClassifications,
                                             InstanceStatus             initialStatus) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityCreate(userId,
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
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    public void  validateUserForEntityRead(String          userId,
                                           String          metadataCollectionName,
                                           EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityRead(userId, metadataCollectionName, new EntityDetail(instance));
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
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntitySummaryRead(userId, metadataCollectionName, new EntitySummary(instance));
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
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityProxyRead(userId, metadataCollectionName, new EntityProxy(instance));
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
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityUpdate(userId, metadataCollectionName, new EntityDetail(instance));
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
    public void  validateUserForEntityClassificationAdd(String               userId,
                                                        String               metadataCollectionName,
                                                        EntityDetail         instance,
                                                        String               classificationName,
                                                        InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityClassificationAdd(userId,
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
    public void  validateUserForEntityClassificationUpdate(String               userId,
                                                           String               metadataCollectionName,
                                                           EntityDetail         instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityClassificationUpdate(userId,
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
    public void  validateUserForEntityClassificationDelete(String               userId,
                                                           String               metadataCollectionName,
                                                           EntityDetail         instance,
                                                           String               classificationName) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityClassificationDelete(userId,
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
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityDelete(userId, metadataCollectionName, new EntityDetail(instance));
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
    public void  validateUserForEntityRestore(String       userId,
                                              String       metadataCollectionName,
                                              String       deletedEntityGUID) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityRestore(userId, metadataCollectionName, deletedEntityGUID);
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
    public void  validateUserForEntityReIdentification(String       userId,
                                                       String       metadataCollectionName,
                                                       EntityDetail instance,
                                                       String       newGUID) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityReIdentification(userId, metadataCollectionName, instance, newGUID);
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
    public void  validateUserForEntityReTyping(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
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
    public void  validateUserForEntityReHoming(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               String         newHomeMetadataCollectionId,
                                               String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForEntityReHoming(userId,
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
    public void  validateUserForRelationshipCreate(String               userId,
                                                   String               metadataCollectionName,
                                                   String               relationshipTypeGUID,
                                                   InstanceProperties   initialProperties,
                                                   EntitySummary        entityOneSummary,
                                                   EntitySummary        entityTwoSummary,
                                                   InstanceStatus       initialStatus) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipCreate(userId,
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
    public void  validateUserForRelationshipRead(String          userId,
                                                 String          metadataCollectionName,
                                                 Relationship    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipRead(userId, metadataCollectionName, new Relationship(instance));
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
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipUpdate(userId, metadataCollectionName, new Relationship(instance));
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
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipDelete(userId, metadataCollectionName, new Relationship(instance));
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
        if (connector != null)
        {
            connector.validateUserForEntityRestore(userId, metadataCollectionName, deletedRelationshipGUID);
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
    public void  validateUserForRelationshipReIdentification(String       userId,
                                                             String       metadataCollectionName,
                                                             Relationship instance,
                                                             String       newGUID) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipReIdentification(userId, metadataCollectionName, instance, newGUID);
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
    public void  validateUserForRelationshipReTyping(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
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
    public void  validateUserForRelationshipReHoming(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     String         newHomeMetadataCollectionId,
                                                     String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (connector != null)
        {
            connector.validateUserForRelationshipReHoming(userId,
                                                          metadataCollectionName,
                                                          instance,
                                                          newHomeMetadataCollectionId,
                                                          newHomeMetadataCollectionName);
        }
    }
}
