/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.builders.DiscoveryEngineBuilder;
import org.odpi.openmetadata.accessservices.discoveryengine.builders.DiscoveryServiceBuilder;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DiscoveryEnginePropertiesConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.DiscoveryServicePropertiesConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.converters.RegisteredDiscoveryServiceConverter;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.ConnectionMapper;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.DiscoveryEnginePropertiesMapper;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.DiscoveryServicePropertiesMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryConfigurationServer;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * DiscoveryConfigurationServerHandler provides the open metadata server side implementation of
 * DiscoveryConfigurationServer which is part of the Open Discovery Framework (ODF).
 */
public class DiscoveryConfigurationServerHandler extends DiscoveryConfigurationServer
{
    private String                  serviceName;
    private OMRSRepositoryConnector repositoryConnector;
    private OMRSRepositoryHelper    repositoryHelper;
    private String                  serverName;
    private ErrorHandler            errorHandler;
    private BasicHandler            basicHandler;
    private ConnectionHandler       connectionHandler;
    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Construct the connection handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    public DiscoveryConfigurationServerHandler(String                  serviceName,
                                               OMRSRepositoryConnector repositoryConnector) throws PropertyServerException
    {
        this.serviceName = serviceName;
        this.repositoryConnector = repositoryConnector;

        OMRSMetadataCollection metadataCollection = this.getMetadataCollection(repositoryConnector);
        this.repositoryHelper = repositoryConnector.getRepositoryHelper();
        this.serverName = repositoryConnector.getServerName();
        this.errorHandler = new ErrorHandler(serviceName, serverName);
        this.basicHandler = new BasicHandler(errorHandler, metadataCollection);
        this.connectionHandler = new ConnectionHandler(serviceName, serverName, repositoryHelper, basicHandler);
    }


    /**
     * Retrieve the metadata collection for the handler.
     *
     * @param repositoryConnector repository for this handler
     * @return metadata collection for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    private OMRSMetadataCollection getMetadataCollection(OMRSRepositoryConnector repositoryConnector) throws PropertyServerException
    {
        try
        {
            return repositoryConnector.getMetadataCollection();
        }
        catch (Throwable    error)
        {
            final String methodName = "getMetadataCollection";

            String  repositoryConnectorDescription;

            if (repositoryConnector == null)
            {
                repositoryConnectorDescription = "<null>";
            }
            else
            {
                repositoryConnectorDescription = repositoryConnector.getServerName() + "[isActive=" + repositoryConnector.isActive() + "]";
            }

            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.NO_METADATA_COLLECTION;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(repositoryConnectorDescription);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

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

        return basicHandler.createEntity(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        EntityDetail retrievedEntity = basicHandler.getEntityByGUID(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

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



        List<EntityDetail> retrievedEntities = basicHandler.getEntityByName(userId,
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


        retrievedEntities = basicHandler.getEntityByName(userId,
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
        else
        {
            errorHandler.handleNoEntity(DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                        DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                        builder.getNameInstanceProperties(methodName),
                                        methodName);
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
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        List<EntityDetail> retrievedEntities = basicHandler.getEntityByType(userId,
                                                                            DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                                                            startingFrom,
                                                                            maximumResults,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

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

        basicHandler.updateEntity(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        DiscoveryEngineBuilder builder = new DiscoveryEngineBuilder(qualifiedName,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        InstanceProperties validatingProperties = builder.getInstanceProperties(methodName);

        basicHandler.deleteEntity(userId,
                                  guid,
                                  DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                  DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                  validatingProperties,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        String connectionGUID = connectionHandler.saveConnection(userId, connection);

        DiscoveryServiceBuilder builder = new DiscoveryServiceBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        String discoveryServiceGUID = basicHandler.createEntity(userId,
                                                                DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                properties,
                                                                methodName);

        basicHandler.createRelationship(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        EntityDetail discoveryServiceEntity = basicHandler.getEntityByGUID(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

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


        List<EntityDetail> retrievedEntities = basicHandler.getEntityByName(userId,
                                                                            builder.getQualifiedNameInstanceProperties(methodName),
                                                                            DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                            methodName);

        /*
         * Did not retrieve the discovery service by qualified name so going to try all of the names
         */
        if ((retrievedEntities == null) || (retrievedEntities.isEmpty()))
        {
            retrievedEntities = basicHandler.getEntityByName(userId,
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
        else
        {
            errorHandler.handleNoEntity(DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_GUID,
                                        DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                        builder.getNameInstanceProperties(methodName),
                                        methodName);
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
        Relationship relationshipToConnection = basicHandler.getUniqueRelationshipByType(userId,
                                                                                         discoveryServiceEntity.getGUID(),
                                                                                         DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                         DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                         DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                         methodName);


        DiscoveryServicePropertiesConverter converter = new DiscoveryServicePropertiesConverter(discoveryServiceEntity,
                                                                                                relationshipToConnection,
                                                                                                repositoryHelper,
                                                                                                serviceName);



        DiscoveryServiceProperties bean = converter.getBean();

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
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        List<EntityDetail> retrievedEntities = basicHandler.getEntityByType(userId,
                                                                            DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                                                            startingFrom,
                                                                            maximumResults,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        List<Relationship>  relationships = basicHandler.getRelationshipsByType(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        DiscoveryServiceBuilder builder = new DiscoveryServiceBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      owner,
                                                                      ownerType,
                                                                      zoneMembership,
                                                                      latestChange,
                                                                      additionalProperties,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        basicHandler.updateEntity(userId,
                                  guid,
                                  DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                  DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                  properties,
                                  methodName);


        Relationship  assetConnectionRelationship = basicHandler.getUniqueRelationshipByType(userId,
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
                basicHandler.deleteRelationship(userId,
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
                basicHandler.createRelationship(userId,
                                                DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                connectionGUID,
                                                guid,
                                                null,
                                                methodName);
            }
            else
            {
                basicHandler.updateUniqueRelationshipByType(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        EntityDetail connectionEntity = basicHandler.getEntityForRelationshipType(userId,
                                                                                  guid,
                                                                                  DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                                                  DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                  DiscoveryServicePropertiesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                  methodName);

        if (connectionEntity != null)
        {
            connectionHandler.removeConnection(userId, connectionEntity.getGUID());
        }


        DiscoveryServiceBuilder builder = new DiscoveryServiceBuilder(qualifiedName,
                                                                      null,
                                                                      null,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        InstanceProperties validatingProperties = builder.getInstanceProperties(methodName);

        basicHandler.deleteEntity(userId,
                                  guid,
                                  DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_GUID,
                                  DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                  validatingProperties,
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
     * @param assetTypes list of asset types that this discovery service is able to process.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public  void  registerDiscoveryServiceWithEngine(String        userId,
                                                     String        discoveryEngineGUID,
                                                     String        discoveryServiceGUID,
                                                     List<String>  assetTypes) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "registerDiscoveryServiceWithEngine";
        final String discoveryEngineGUIDParameter = "discoveryEngineGUID";
        final String discoveryServiceGUIDParameter = "discoveryServiceGUID";
        final String assetTypesParameter = "assetTypes";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameter, methodName);
        invalidParameterHandler.validateStringArray(assetTypes, assetTypesParameter, methodName);
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        InstanceProperties instanceProperties = new InstanceProperties();

        repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                          instanceProperties,
                                                          DiscoveryEnginePropertiesMapper.ASSET_TYPES_PROPERTY_NAME,
                                                          assetTypes,
                                                          methodName);
        basicHandler.createRelationship(userId,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        Relationship relationship = basicHandler.getRelationshipBetweenEntities(userId,
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
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        List<Relationship> relationships = basicHandler.getPagedRelationshipsByType(userId,
                                                                                    discoveryEngineGUID,
                                                                                    DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                                                                    DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_GUID,
                                                                                    DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_NAME,
                                                                                    startingFrom,
                                                                                    maximumResults,
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
        errorHandler.validateRepositoryConnector(repositoryConnector, methodName);

        basicHandler.deleteRelationshipBetweenEntities(userId,
                                                       DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_GUID,
                                                       DiscoveryEnginePropertiesMapper.SUPPORTED_DISCOVERY_SERVICE_TYPE_NAME,
                                                       discoveryServiceGUID,
                                                       DiscoveryServicePropertiesMapper.DISCOVERY_SERVICE_TYPE_NAME,
                                                       discoveryEngineGUID,
                                                       methodName);
    }
}
