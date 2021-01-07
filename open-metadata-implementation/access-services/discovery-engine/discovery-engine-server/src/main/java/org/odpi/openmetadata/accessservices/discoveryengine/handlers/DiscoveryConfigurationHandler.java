/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.converters.ConnectionConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DiscoveryEnginePropertiesConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DiscoveryServicePropertiesConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.RegisteredDiscoveryServiceConverter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryConfigurationServer;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * DiscoveryConfigurationHandler provides the open metadata server side implementation of
 * DiscoveryConfigurationServer which is part of the Open Discovery Framework (ODF).
 */
public class DiscoveryConfigurationHandler extends DiscoveryConfigurationServer
{
    private String                                                     serviceName;
    private String                                                     serverName;
    private RepositoryHandler                                          repositoryHandler;
    private OMRSRepositoryHelper                                       repositoryHelper;
    private SoftwareServerCapabilityHandler<DiscoveryEngineProperties> discoveryEngineHandler;
    private AssetHandler<DiscoveryServiceProperties>                   discoveryServiceHandler;
    private ConnectionHandler<Connection>                              connectionHandler;
    private ConnectorTypeHandler<ConnectorType>                        connectorTypeHandler;
    private InvalidParameterHandler                                    invalidParameterHandler;


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog logging destination
    */
    public DiscoveryConfigurationHandler(String                             serviceName,
                                         String                             serverName,
                                         InvalidParameterHandler            invalidParameterHandler,
                                         RepositoryHandler                  repositoryHandler,
                                         OMRSRepositoryHelper               repositoryHelper,
                                         String                             localServerUserId,
                                         OpenMetadataServerSecurityVerifier securityVerifier,
                                         List<String>                       supportedZones,
                                         List<String>                       defaultZones,
                                         List<String>                       publishZones,
                                         AuditLog                           auditLog)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;

        this.discoveryEngineHandler = new SoftwareServerCapabilityHandler<>(new DiscoveryEnginePropertiesConverter<>(repositoryHelper,
                                                                                                                     serviceName,
                                                                                                                     serverName),
                                                                            DiscoveryEngineProperties.class,
                                                                            serviceName,
                                                                            serverName,
                                                                            invalidParameterHandler,
                                                                            repositoryHandler,
                                                                            repositoryHelper,
                                                                            localServerUserId,
                                                                            securityVerifier,
                                                                            supportedZones,
                                                                            defaultZones,
                                                                            publishZones,
                                                                            auditLog);

        this.discoveryServiceHandler = new AssetHandler<>(new DiscoveryServicePropertiesConverter<>(repositoryHelper, serviceName, serverName),
                                                          DiscoveryServiceProperties.class,
                                                          serviceName,
                                                          serverName,
                                                          invalidParameterHandler,
                                                          repositoryHandler,
                                                          repositoryHelper,
                                                          localServerUserId,
                                                          securityVerifier,
                                                          supportedZones,
                                                          defaultZones,
                                                          publishZones,
                                                          auditLog);

        this.connectionHandler = new ConnectionHandler<>(new ConnectionConverter<>(repositoryHelper, serviceName, serverName),
                                                         Connection.class,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);

        this.connectorTypeHandler = new ConnectorTypeHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                               ConnectorType.class,
                                                               serviceName,
                                                               serverName,
                                                               invalidParameterHandler,
                                                               repositoryHandler,
                                                               repositoryHelper,
                                                               localServerUserId,
                                                               securityVerifier,
                                                               supportedZones,
                                                               defaultZones,
                                                               publishZones,
                                                               auditLog);
    }


    /**
     * Create a new discovery engine definition.
     *
     * @param userId identifier of calling user
     * @param qualifiedName unique name for the discovery engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of discovery services that will be associated with
     *                    this discovery engine.
     *
     * @return unique identifier (guid) of the discovery engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery engine definition.
     */
    public  String  createDiscoveryEngine(String  userId,
                                          String  qualifiedName,
                                          String  displayName,
                                          String  description) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "createDiscoveryEngine";

        return discoveryEngineHandler.createSoftwareServerCapability(userId,
                                                                     null,
                                                                     null,
                                                                     OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                                     null,
                                                                     qualifiedName,
                                                                     displayName,
                                                                     description,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     methodName);
    }


    /**
     * Return the properties from a discovery engine definition.  The discovery engine
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the discovery engine definition.
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  DiscoveryEngineProperties getDiscoveryEngineByGUID(String    userId,
                                                               String    guid) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final  String   methodName = "getDiscoveryEngineByGUID";
        final  String   guidParameter = "guid";

        return discoveryEngineHandler.getBeanFromRepository(userId,
                                                            guid,
                                                            guidParameter,
                                                            OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                            methodName);
    }


    /**
     * Return the properties from a discovery engine definition.  The discovery engine
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  DiscoveryEngineProperties getDiscoveryEngineByName(String    userId,
                                                               String    name) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final  String   methodName = "getDiscoveryEngineByName";
        final  String   nameParameter = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return discoveryEngineHandler.getBeanByValue(userId,
                                                     name,
                                                     nameParameter,
                                                     OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                     specificMatchPropertyNames,
                                                     methodName);
    }


    /**
     * Return the list of discovery engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of discovery engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definitions.
     */
    public  List<DiscoveryEngineProperties> getAllDiscoveryEngines(String  userId,
                                                                   int     startingFrom,
                                                                   int     maximumResults) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final  String   methodName = "getAllDiscoveryEngines";

        return discoveryEngineHandler.getBeansByType(userId,
                                                     OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                     startingFrom,
                                                     maximumResults,
                                                     methodName);
    }


    /**
     * Update the properties of an existing discovery engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param qualifiedName new value for unique name of discovery engine.
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param typeDescription new description of the type ofg discovery engine.
     * @param version new version number for the discovery engine implementation.
     * @param patchLevel new patch level for the discovery engine implementation.
     * @param source new source description for the implementation of the discovery engine.
     * @param additionalProperties additional properties for the discovery engine.
     * @param extendedProperties properties to populate the subtype of the discovery engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery engine definition.
     */
    public  void    updateDiscoveryEngine(String                userId,
                                          String                guid,
                                          String                qualifiedName,
                                          String                displayName,
                                          String                description,
                                          String                typeDescription,
                                          String                version,
                                          String                patchLevel,
                                          String                source,
                                          Map<String, String>   additionalProperties,
                                          Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "updateDiscoveryEngine";
        final String guidParameter = "guid";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(qualifiedName,
                                                                                      displayName,
                                                                                      description,
                                                                                      typeDescription,
                                                                                      version,
                                                                                      patchLevel,
                                                                                      source,
                                                                                      additionalProperties,
                                                                                      OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                                                      OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                                                      extendedProperties,
                                                                                      repositoryHelper,
                                                                                      serviceName,
                                                                                      serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        discoveryEngineHandler.updateBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      guidParameter,
                                                      OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                      properties,
                                                      false,
                                                      methodName);
    }


    /**
     * Remove the properties of the discovery engine.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param qualifiedName unique name for the discovery engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  void    deleteDiscoveryEngine(String  userId,
                                          String  guid,
                                          String  qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "deleteDiscoveryEngine";
        final String guidParameter = "discoveryEngineGUID";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        discoveryEngineHandler.deleteBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      guidParameter,
                                                      OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                      qualifiedNameParameter,
                                                      qualifiedName,
                                                      methodName);
    }


    /**
     * Create a discovery service definition.  The same discovery service can be associated with multiple
     * discovery engines.
     *
     * @param userId identifier of calling user
     * @param qualifiedName  unique name for the discovery service.
     * @param displayName   display name for the discovery service.
     * @param description  description of the analysis provided by the discovery service.
     * @param connection   connection to instantiate the discovery service implementation.
     *
     * @return unique identifier of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery service definition.
     */
    public  String  createDiscoveryService(String      userId,
                                           String      qualifiedName,
                                           String      displayName,
                                           String      description,
                                           Connection  connection) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "createDiscoveryService";
        final String connectionParameterName = "createDiscoveryService";
        final String assetGUIDParameterName = "assetGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID ";

        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        String assetGUID = discoveryServiceHandler.createAssetInRepository(userId,
                                                                           null,
                                                                           null,
                                                                           qualifiedName,
                                                                           displayName,
                                                                           description,
                                                                           null,
                                                                           null,
                                                                           0,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                           OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                           null,
                                                                           methodName);

        if (assetGUID != null)
        {
            ConnectorType connectorType = connection.getConnectorType();

            String connectorTypeGUID = connectorTypeHandler.getConnectorTypeForConnection(userId,
                                                                                          null,
                                                                                          null,
                                                                                          assetGUID,
                                                                                          connectorType.getQualifiedName(),
                                                                                          connectorType.getDisplayName(),
                                                                                          connectorType.getDescription(),
                                                                                          connectorType.getConnectorProviderClassName(),
                                                                                          connectorType.getRecognizedAdditionalProperties(),
                                                                                          connectorType.getRecognizedSecuredProperties(),
                                                                                          connectorType.getRecognizedConfigurationProperties(),
                                                                                          connectorType.getAdditionalProperties(),
                                                                                          methodName);

            if (connectorTypeGUID != null)
            {
                if (connection instanceof VirtualConnection)
                {
                    /*
                     * OpenDiscoveryPipelines are represented using a VirtualConnection that
                     * nests all of the Connections for services to call.
                     */
                    final String connectionGUIDParameterName = "connection.getGUID";

                    String connectionGUID = connectionHandler.createVirtualConnection(userId,
                                                                                     null,
                                                                                     null,
                                                                                     assetGUID,
                                                                                     assetGUIDParameterName,
                                                                                     connection.getAssetSummary(),
                                                                                     connection.getQualifiedName(),
                                                                                     connection.getDisplayName(),
                                                                                     connection.getDescription(),
                                                                                     connection.getAdditionalProperties(),
                                                                                     connection.getSecuredProperties(),
                                                                                     connection.getConfigurationProperties(),
                                                                                     connection.getUserId(),
                                                                                     connection.getClearPassword(),
                                                                                     connection.getEncryptedPassword(),
                                                                                     connectorTypeGUID,
                                                                                     connectorTypeGUIDParameterName,
                                                                                     methodName);

                    List<EmbeddedConnection> embeddedConnections = ((VirtualConnection) connection).getEmbeddedConnections();

                    if (embeddedConnections != null)
                    {
                        for (EmbeddedConnection embeddedConnection : embeddedConnections)
                        {
                            if (embeddedConnection != null)
                            {
                                String embeddedConnectionGUID = connectionHandler.saveConnection(userId,
                                                                                                 null,
                                                                                                 null,
                                                                                                 assetGUID,
                                                                                                 null,
                                                                                                 assetGUIDParameterName,
                                                                                                 OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                                 embeddedConnection.getEmbeddedConnection(),
                                                                                                 null,
                                                                                                 methodName);
                                connectionHandler.addEmbeddedConnection(userId,
                                                                        null,
                                                                        null,
                                                                        connectionGUID,
                                                                        connectionGUIDParameterName,
                                                                        embeddedConnection.getPosition(),
                                                                        embeddedConnection.getDisplayName(),
                                                                        embeddedConnection.getArguments(),
                                                                        embeddedConnectionGUID,
                                                                        embeddedConnectionGUIDParameterName,
                                                                        methodName);
                            }
                        }
                    }
                }
                else
                {
                    connectionHandler.createConnection(userId,
                                                       null,
                                                       null,
                                                       assetGUID,
                                                       assetGUIDParameterName,
                                                       connection.getAssetSummary(),
                                                       connection.getQualifiedName(),
                                                       connection.getDisplayName(),
                                                       connection.getDescription(),
                                                       connection.getAdditionalProperties(),
                                                       connection.getSecuredProperties(),
                                                       connection.getConfigurationProperties(),
                                                       connection.getUserId(),
                                                       connection.getClearPassword(),
                                                       connection.getEncryptedPassword(),
                                                       connectorTypeGUID,
                                                       connectorTypeGUIDParameterName,
                                                       null,
                                                       null,
                                                       methodName);
                }
            }
        }


        return assetGUID;
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the discovery service definition.
     *
     * @return properties of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definition.
     */
    public  DiscoveryServiceProperties getDiscoveryServiceByGUID(String    userId,
                                                                 String    guid) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final  String   methodName = "getDiscoveryServiceByGUID";
        final  String   guidParameter = "guid";

        return discoveryServiceHandler.getAssetWithConnection(userId,
                                                              guid,
                                                              guidParameter,
                                                              OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                              methodName);
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public  DiscoveryServiceProperties getDiscoveryServiceByName(String    userId,
                                                                 String    name) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName    = "getDiscoveryServiceByName";
        final String nameParameter = "name";

        return discoveryServiceHandler.getAssetByNameWithConnection(userId,
                                                                    name,
                                                                    nameParameter,
                                                                    OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                    methodName);
    }


    /**
     * Return the list of discovery services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definitions.
     */
    public  List<DiscoveryServiceProperties> getAllDiscoveryServices(String  userId,
                                                                     int     startingFrom,
                                                                     int     maximumResults) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final  String   methodName = "getAllDiscoveryServices";

        return discoveryServiceHandler.getAllAssetsWithConnection(userId,
                                                                  OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                  startingFrom,
                                                                  maximumResults,
                                                                  methodName);
    }


    /**
     * Return the list of discovery engines that a specific discovery service is registered with.
     *
     * @param userId identifier of calling user
     * @param discoveryServiceGUID discovery service to search for.
     *
     * @return list of discovery engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  List<String>  getDiscoveryServiceRegistrations(String   userId,
                                                           String   discoveryServiceGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final  String   methodName = "getAllDiscoveryServices";
        final  String   guidParameter = "discoveryServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, guidParameter, methodName);

        /*
         * Checks this is a valid, visible service.
         */
        connectionHandler.getBeanFromRepository(userId,
                                                discoveryServiceGUID,
                                                guidParameter,
                                                OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                methodName);

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     discoveryServiceGUID,
                                                                                     OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                     OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                     OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                     methodName);

        List<String> results = new ArrayList<>();

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (relationship.getGUID() != null)
                    {
                        results.add(relationship.getGUID());
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Update the properties of an existing discovery service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param qualifiedName new value for unique name of discovery service.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this discovery service.
     * @param additionalProperties additional properties for the discovery engine.
     * @param extendedProperties properties to populate the subtype of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery service definition.
     */
    public  void    updateDiscoveryService(String                userId,
                                           String                guid,
                                           String                qualifiedName,
                                           String                displayName,
                                           String                description,
                                           Connection            connection,
                                           Map<String, String>   additionalProperties,
                                           Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "updateDiscoveryService";
        final String guidParameter = "guid";

        discoveryServiceHandler.updateAssetWithConnection(userId,
                                                          null,
                                                          null,
                                                          guid,
                                                          guidParameter,
                                                          qualifiedName,
                                                          displayName,
                                                          description,
                                                          additionalProperties,
                                                          OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                          OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                          extendedProperties,
                                                          null,
                                                          connection,
                                                          methodName);
    }


    /**
     * Remove the properties of the discovery service.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery service is being deleted.  The discovery service is also
     * unregistered from its discovery engines.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param qualifiedName unique name for the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definition.
     */
    public  void    deleteDiscoveryService(String  userId,
                                           String  guid,
                                           String  qualifiedName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "deleteDiscoveryService";
        final String qualifiedNameParameter = "qualifiedName";
        final String guidParameter = "discoveryServiceGUID";

        connectionHandler.deleteBeanInRepository(userId,
                                                 null,
                                                 null,
                                                 guid,
                                                 guidParameter,
                                                 OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                 OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                 qualifiedNameParameter,
                                                 qualifiedName,
                                                 methodName);
    }


    /**
     * Register a discovery service with a specific discovery engine.   Both the
     * discovery service and the discovery engine already exist so it is
     * just a question of creating a relationship between them.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     * @param discoveryRequestType list of discovery request types that this discovery service is able to process.
     * @param defaultAnalysisParameters list of analysis parameters that are passed the the discovery service (via
     *                                  the discovery context).  These values can be overridden on the actual discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  void  registerDiscoveryServiceWithEngine(String               userId,
                                                     String               discoveryEngineGUID,
                                                     String               discoveryServiceGUID,
                                                     String               discoveryRequestType,
                                                     Map<String, String>  defaultAnalysisParameters) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "registerDiscoveryServiceWithEngine";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";
        final String discoveryRequestTypeParameter = "discoveryRequestType";

        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);
        invalidParameterHandler.validateName(discoveryRequestType, discoveryRequestTypeParameter, methodName);

        /*
         * First check if this request type has already been registered.
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       discoveryEngineGUID,
                                                                                       OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       methodName);


        while (iterator.moreToReceive())
        {
            Relationship supportedDiscoveryService = iterator.getNext();

            if (supportedDiscoveryService != null)
            {
                String existingRequestType = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                                supportedDiscoveryService.getProperties(),
                                                                                methodName);

                if (discoveryRequestType.equals(existingRequestType))
                {
                    /*
                     * The request type is already registered.  Is it registered to the same discovery service?
                     */
                    EntityProxy existingDiscoveryServiceProxy = supportedDiscoveryService.getEntityTwoProxy();

                    if ((existingDiscoveryServiceProxy != null) && (discoveryServiceGUID.equals(existingDiscoveryServiceProxy.getGUID())))
                    {
                        /*
                         * The existing registration is for the requested discovery service.  All that needs to be done
                         * is to set the request parameters to match the supplied values.
                         */
                        InstanceProperties properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                                        supportedDiscoveryService.getProperties(),
                                                                                                        OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                                        defaultAnalysisParameters,
                                                                                                        methodName);

                        repositoryHandler.updateRelationshipProperties(userId,
                                                                       null,
                                                                       null,
                                                                       supportedDiscoveryService,
                                                                       properties,
                                                                       methodName);
                        return;
                    }
                    else
                    {
                        /*
                         * Delete the service registration and when this drops out of the loop, the new registration will be added.
                         */
                        repositoryHandler.removeRelationship(userId,
                                                             null,
                                                             null,
                                                             supportedDiscoveryService,
                                                             methodName);
                    }
                }
            }
        }

        /*
         * If this code executes it means that the discovery service can be registered with the discovery engine
         * using the supplied request type.
         */

        InstanceProperties instanceProperties = new InstanceProperties();

        repositoryHelper.addStringPropertyToInstance(serviceName,
                                                     instanceProperties,
                                                     OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                     discoveryRequestType,
                                                     methodName);
        repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                        instanceProperties,
                                                        OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                        defaultAnalysisParameters,
                                                        methodName);

        discoveryEngineHandler.linkElementToElement(userId,
                                                    null,
                                                    null,
                                                    discoveryEngineGUID,
                                                    discoveryEngineGUIDParameter,
                                                    OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                    discoveryServiceGUID,
                                                    discoveryServiceGUIDParameter,
                                                    OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                    OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                    OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                    instanceProperties,
                                                    methodName);
    }


    /**
     * Retrieve a specific discovery service registered with a discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @return details of the discovery service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public RegisteredDiscoveryService getRegisteredDiscoveryService(String  userId,
                                                                    String  discoveryEngineGUID,
                                                                    String  discoveryServiceGUID) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceService";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                             discoveryServiceGUID,
                                                                                             OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                             discoveryEngineGUID,
                                                                                             OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                                                             OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                             methodName);


        if (relationships != null)
        {
            RegisteredDiscoveryServiceConverter converter = new RegisteredDiscoveryServiceConverter(repositoryHelper, serviceName);

            return converter.getBean(this.getDiscoveryServiceByGUID(userId, discoveryServiceGUID), relationships);
        }

        return null;
    }


    /**
     * Retrieve the identifiers of the discovery services registered with a discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  List<String>  getRegisteredDiscoveryServices(String  userId,
                                                         String  discoveryEngineGUID,
                                                         int     startingFrom,
                                                         int     maximumResults) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getRegisteredDiscoveryServices";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";

        return discoveryEngineHandler.getAttachedElementGUIDs(userId,
                                                              discoveryEngineGUID,
                                                              discoveryEngineGUIDParameter,
                                                              OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                              OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                              OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                              OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                              startingFrom,
                                                              maximumResults,
                                                              methodName);
    }


    /**
     * Unregister a discovery service from the discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  void  unregisterDiscoveryServiceFromEngine(String        userId,
                                                       String        discoveryEngineGUID,
                                                       String        discoveryServiceGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "unregisterDiscoveryServiceFromEngine";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);

        discoveryEngineHandler.unlinkElementFromElement(userId,
                                                        false,
                                                        null,
                                                        null,
                                                        discoveryEngineGUID,
                                                        discoveryEngineGUIDParameter,
                                                        OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                        discoveryServiceGUID,
                                                        discoveryServiceGUIDParameter,
                                                        OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                        OpenMetadataAPIMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                        OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                        OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                        methodName);
    }
}
