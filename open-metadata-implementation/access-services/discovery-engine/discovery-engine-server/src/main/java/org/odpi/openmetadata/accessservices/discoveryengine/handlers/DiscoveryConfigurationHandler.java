/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.builders.DiscoveryEngineBuilder;
import org.odpi.openmetadata.accessservices.discoveryengine.builders.DiscoveryServiceBuilder;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DiscoveryEnginePropertiesConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DiscoveryServicePropertiesConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.RegisteredDiscoveryServiceConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ConnectionMapper;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.DiscoveryEnginePropertiesMapper;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.DiscoveryServicePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryConfigurationServer;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;
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
    private String                  serviceName;
    private String                  serverName;
    private RepositoryErrorHandler  errorHandler;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private ConnectionHandler       connectionHandler;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     * @param errorHandler handler for repository service errors
     * @param connectionHandler handler for working with connection objects
     */
    public DiscoveryConfigurationHandler(String                  serviceName,
                                         String                  serverName,
                                         InvalidParameterHandler invalidParameterHandler,
                                         OMRSRepositoryHelper    repositoryHelper,
                                         RepositoryHandler       repositoryHandler,
                                         RepositoryErrorHandler  errorHandler,
                                         ConnectionHandler       connectionHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.errorHandler = errorHandler;
        this.repositoryHandler = repositoryHandler;
        this.connectionHandler = connectionHandler;
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
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        DiscoveryEngineProperties existingEngine = this.getDiscoveryEngineByName(userId, qualifiedName);
        if (existingEngine != null)
        {
            errorHandler.handleDuplicateCreateRequest(DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                      qualifiedName,
                                                      existingEngine.getGUID(),
                                                      methodName);
        }

        /*
         * Build up the properties used to create the engine and pass to the repository services.
         */
        DiscoveryEngineBuilder builder = new DiscoveryEngineBuilder(qualifiedName,
                                                                    displayName,
                                                                    description,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        return repositoryHandler.createEntity(userId,
                                              DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                              DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                              properties,
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameter, methodName);

        EntityDetail retrievedEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         guid,
                                                                         guidParameter,
                                                                         DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                                         methodName);

        if (retrievedEntity != null)
        {
            DiscoveryEnginePropertiesConverter converter = new DiscoveryEnginePropertiesConverter(retrievedEntity,
                                                                                                  repositoryHelper,
                                                                                                  serviceName);

            return converter.getBean();
        }

        return null;
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(name, nameParameter, methodName);

        /*
         * Try to retrieve the entity using the fully qualified name first and then
         * the display name.
         */
        DiscoveryEngineBuilder builder = new DiscoveryEngineBuilder(name,
                                                                    name,
                                                                    null,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);



        List<EntityDetail> retrievedEntities = repositoryHandler.getEntityByName(userId,
                                                                                 builder.getQualifiedNameInstanceProperties(methodName),
                                                                                 DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                                                 methodName);

        if ((retrievedEntities != null) && (retrievedEntities.size() == 1))
        {
            DiscoveryEnginePropertiesConverter converter =
                    new DiscoveryEnginePropertiesConverter(retrievedEntities.get(0),
                                                           repositoryHelper,
                                                           serviceName);

            return converter.getBean();
        }


        retrievedEntities = repositoryHandler.getEntityByName(userId,
                                                              builder.getNameInstanceProperties(methodName),
                                                              DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                              methodName);

        /*
         * Did not retrieve the discovery engine by qualified name so going to try all of the names
         */
        if ((retrievedEntities != null) && (!retrievedEntities.isEmpty()))
        {
            if (retrievedEntities.size() == 1)
            {
                DiscoveryEnginePropertiesConverter converter =
                        new DiscoveryEnginePropertiesConverter(retrievedEntities.get(0),
                                                               repositoryHelper,
                                                               serviceName);

                return converter.getBean();
            }
            else
            {
                errorHandler.handleAmbiguousEntityName(name,
                                                       nameParameter,
                                                       DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                       retrievedEntities,
                                                       methodName);
            }
        }

        return null;
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

        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByType(userId,
                                                                                   DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                                                   startingFrom,
                                                                                   queryPageSize,
                                                                                   methodName);

        /*
         * Convert entities to beans
         */
        if ((retrievedEntities != null) && (!retrievedEntities.isEmpty()))
        {
            List<DiscoveryEngineProperties>   results = new ArrayList<>();

            for (EntityDetail  entityDetail : retrievedEntities)
            {
                if (entityDetail != null)
                {
                    DiscoveryEnginePropertiesConverter converter = new DiscoveryEnginePropertiesConverter(entityDetail,
                                                                                                          repositoryHelper,
                                                                                                          serviceName);

                    results.add(converter.getBean());
                }
            }

            if (!results.isEmpty())
            {
                return results;
            }
        }

        return null;
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
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        DiscoveryEngineBuilder builder = new DiscoveryEngineBuilder(qualifiedName,
                                                                    displayName,
                                                                    description,
                                                                    typeDescription,
                                                                    version,
                                                                    patchLevel,
                                                                    source,
                                                                    additionalProperties,
                                                                    extendedProperties,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        repositoryHandler.updateEntity(userId,
                                       guid,
                                       DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                       DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                       properties,
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
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        repositoryHandler.removeEntity(userId,
                                       guid,
                                       DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                       DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
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
     * @param connection   connection to instanciate the discovery service implementation.
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
        final String qualifiedNameParameter = "qualifiedName";
        final String connectionParameter = "connection";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameter, methodName);

        String connectionGUID = connectionHandler.saveConnection(userId, connection);

        DiscoveryServiceBuilder builder = new DiscoveryServiceBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        String discoveryServiceGUID = repositoryHandler.createEntity(userId,
                                                                     DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                     DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                     properties,
                                                                     methodName);

        repositoryHandler.createRelationship(userId,
                                             DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                             connectionGUID,
                                             discoveryServiceGUID,
                                             null,
                                             methodName);

        return discoveryServiceGUID;
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameter, methodName);

        EntityDetail discoveryServiceEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                guid,
                                                                                guidParameter,
                                                                                DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                methodName);

        return this.getDiscoveryServiceBean(userId, discoveryServiceEntity, methodName);
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        /*
         * Try to retrieve the entity using the fully qualified name first and then
         * the display name.
         */
        DiscoveryServiceBuilder builder = new DiscoveryServiceBuilder(name,
                                                                      name,
                                                                      null,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);


        List<EntityDetail> retrievedEntities = repositoryHandler.getEntityByName(userId,
                                                                                 builder.getQualifiedNameInstanceProperties(methodName),
                                                                                 DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                                 methodName);

        /*
         * Did not retrieve the discovery service by qualified name so going to try all of the names
         */
        if ((retrievedEntities == null) || (retrievedEntities.isEmpty()))
        {
            retrievedEntities = repositoryHandler.getEntityByName(userId,
                                                                  builder.getNameInstanceProperties(methodName),
                                                                  DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                                  methodName);
        }


        if ((retrievedEntities != null) && (!retrievedEntities.isEmpty()))
        {
            if (retrievedEntities.size() == 1)
            {
                return this.getDiscoveryServiceBean(userId, retrievedEntities.get(0), methodName);
            }
            else
            {
                errorHandler.handleAmbiguousEntityName(name,
                                                       nameParameter,
                                                       DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                       retrievedEntities,
                                                       methodName);
            }
        }

        return null;
    }


    /**
     * Return the properties of the discovery service.
     *
     * @param userId calling user
     * @param discoveryServiceEntity entity details from repository
     * @param methodName calling method
     *
     * @return properties of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definition.
     */
    private DiscoveryServiceProperties getDiscoveryServiceBean(String       userId,
                                                               EntityDetail discoveryServiceEntity,
                                                               String       methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        Relationship relationshipToConnection = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                              discoveryServiceEntity.getGUID(),
                                                                                              DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                              DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                              DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                              methodName);


        DiscoveryServicePropertiesConverter converter = new DiscoveryServicePropertiesConverter(discoveryServiceEntity,
                                                                                                relationshipToConnection,
                                                                                                repositoryHelper,
                                                                                                serviceName);



        DiscoveryServiceProperties bean = converter.getDiscoveryServiceBean();

        if (relationshipToConnection != null)
        {
            EntityProxy  end1 = relationshipToConnection.getEntityOneProxy();

            if (end1 != null)
            {
                bean.setConnection(connectionHandler.getConnection(userId, end1.getGUID()));
            }
        }

        return bean;
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

        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByType(userId,
                                                                                   DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                                   startingFrom,
                                                                                   queryPageSize,
                                                                                   methodName);

        /*
         * Convert entities to beans
         */
        if ((retrievedEntities != null) && (!retrievedEntities.isEmpty()))
        {
            List<DiscoveryServiceProperties>   results = new ArrayList<>();

            for (EntityDetail  entityDetail : retrievedEntities)
            {
                if (entityDetail != null)
                {
                    results.add(this.getDiscoveryServiceBean(userId, entityDetail, methodName));
                }
            }

            if (!results.isEmpty())
            {
                return results;
            }
        }

        return null;
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

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     discoveryServiceGUID,
                                                                                     DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                     DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                     DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
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
     * @param shortDescription new value for the short description.
     * @param description new value for the description.
     * @param owner new owner of the discovery service.
     * @param ownerType new type for the owner of the discovery service.
     * @param zoneMembership new list of zones for this discovery service.
     * @param origin properties describing the origin of the discovery service.
     * @param latestChange short description of this update.
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
                                           String                shortDescription,
                                           String                description,
                                           String                owner,
                                           OwnerType             ownerType,
                                           List<String>          zoneMembership,
                                           Map<String, String>   origin,
                                           String                latestChange,
                                           Connection            connection,
                                           Map<String, String>   additionalProperties,
                                           Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "updateDiscoveryService";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        DiscoveryServiceBuilder builder = new DiscoveryServiceBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      owner,
                                                                      ownerType,
                                                                      zoneMembership,
                                                                      origin,
                                                                      latestChange,
                                                                      additionalProperties,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        repositoryHandler.updateEntity(userId,
                                       guid,
                                       DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                       DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                       properties,
                                       methodName);


        Relationship  assetConnectionRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                  guid,
                                                                                                  DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                                  DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                                  DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                                  methodName);
        if (connection == null)
        {
            /*
             * Make sure there is no relationship to a connection
             */
            if (assetConnectionRelationship != null)
            {
                repositoryHandler.removeRelationship(userId,
                                                     DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                     DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                     assetConnectionRelationship.getGUID(),
                                                     methodName);

                EntityProxy connectionProxy = assetConnectionRelationship.getEntityOneProxy();

                if (connectionProxy != null)
                {
                    /*
                     * This deletes the connection and all of its sub parts if it is not connected
                     * to any other assets.
                     */
                    connectionHandler.removeConnection(userId, connectionProxy.getGUID());
                }
            }
        }
        else /* connection to add */
        {
            String  connectionGUID = connectionHandler.saveConnection(userId, connection);

            if (assetConnectionRelationship == null)
            {
                InstanceProperties relationshipProperties = null;

                if (shortDescription != null)
                {
                    relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          AssetMapper.SHORT_DESCRIPTION_PROPERTY_NAME,
                                                                                          shortDescription,
                                                                                          methodName);
                }

                repositoryHandler.createRelationship(userId,
                                                     DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                     connectionGUID,
                                                     guid,
                                                     relationshipProperties,
                                                     methodName);
            }
            else
            {
                repositoryHandler.updateUniqueRelationshipByType(userId,
                                                                 connectionGUID,
                                                                 ConnectionMapper.CONNECTION_TYPE_NAME,
                                                                 guid,
                                                                 DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                 DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                 DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                 methodName);
            }
        }
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        EntityDetail connectionEntity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                                       guid,
                                                                                       DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                       DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                       DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                       methodName);

        if (connectionEntity != null)
        {
            connectionHandler.removeConnection(userId, connectionEntity.getGUID());
        }

        repositoryHandler.removeEntity(userId,
                                       guid,
                                       DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                       DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
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
     * @param discoveryRequestTypes list of discovery request types that this discovery service is able to process.
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
                                                     List<String>         discoveryRequestTypes,
                                                     Map<String, String>  defaultAnalysisParameters) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "registerDiscoveryServiceWithEngine";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";
        final String discoveryRequestTypesParameter = "discoveryRequestTypes";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);
        invalidParameterHandler.validateStringArray(discoveryRequestTypes, discoveryRequestTypesParameter, methodName);

        InstanceProperties instanceProperties = new InstanceProperties();

        repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                          instanceProperties,
                                                          DiscoveryEnginePropertiesMapper.DISCOVERY_REQUEST_TYPES_PROPERTY_NAME,
                                                          discoveryRequestTypes,
                                                          methodName);
        repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                        instanceProperties,
                                                        DiscoveryEnginePropertiesMapper.DEFAULT_ANALYSIS_PARAMETERS_PROPERTY_NAME,
                                                        defaultAnalysisParameters,
                                                        methodName);
        repositoryHandler.createRelationship(userId,
                                             DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_GUID,
                                             discoveryServiceGUID,
                                             discoveryEngineGUID,
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
        final String methodName = "getRegisteredDiscoveryService";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);

        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                                     discoveryServiceGUID,
                                                                                     DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                     discoveryEngineGUID,
                                                                                     DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_GUID,
                                                                                     DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_NAME,
                                                                                     methodName);

        RegisteredDiscoveryServiceConverter converter = new RegisteredDiscoveryServiceConverter(this.getDiscoveryServiceByGUID(userId, discoveryServiceGUID),
                                                                                                relationship,
                                                                                                repositoryHelper,
                                                                                                serviceName);

        return converter.getBean();
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

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        List<Relationship> relationships = repositoryHandler.getPagedRelationshipsByType(userId,
                                                                                         discoveryEngineGUID,
                                                                                         DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                                                         DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_GUID,
                                                                                         DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_NAME,
                                                                                         startingFrom,
                                                                                         queryPageSize,
                                                                                         methodName);

        List<String> results = new ArrayList<>();

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy end1 = relationship.getEntityOneProxy();
                    if (end1.getGUID() != null)
                    {
                        results.add(end1.getGUID());
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

        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_GUID,
                                                            DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_NAME,
                                                            discoveryServiceGUID,
                                                            DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                            discoveryEngineGUID,
                                                            methodName);
    }
}
