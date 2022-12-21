/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredGovernanceServiceProperties;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionConverter;
import org.odpi.openmetadata.accessservices.governanceengine.converters.GovernanceEngineConverter;
import org.odpi.openmetadata.accessservices.governanceengine.converters.GovernanceServiceConverter;
import org.odpi.openmetadata.accessservices.governanceengine.converters.RegisteredGovernanceServiceConverter;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceEngineElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GovernanceConfigurationHandler provides the open metadata server side implementation of
 * GovernanceConfigurationServer which is part of the Open Governance Framework (ODF).
 */
public class GovernanceConfigurationHandler
{
    private final String                                             serviceName;
    private final String                                             serverName;
    private final RepositoryHandler                                  repositoryHandler;
    private final OMRSRepositoryHelper                               repositoryHelper;
    private final SoftwareCapabilityHandler<GovernanceEngineElement> governanceEngineHandler;
    private final AssetHandler<GovernanceServiceElement>             governanceServiceHandler;
    private final ConnectionHandler<Connection>                      connectionHandler;
    private final ConnectorTypeHandler<ConnectorType>                connectorTypeHandler;
    private final InvalidParameterHandler                            invalidParameterHandler;


    /**
     * Construct the governance engine configuration handler caching the objects
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
    public GovernanceConfigurationHandler(String                             serviceName,
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

        this.governanceEngineHandler = new SoftwareCapabilityHandler<>(new GovernanceEngineConverter<>(repositoryHelper,
                                                                                                       serviceName,
                                                                                                       serverName),
                                                                       GovernanceEngineElement.class,
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

        this.governanceServiceHandler = new AssetHandler<>(new GovernanceServiceConverter<>(repositoryHelper, serviceName, serverName),
                                                          GovernanceServiceElement.class,
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
     * Create a new governance engine definition.
     *
     * @param userId identifier of calling user
     * @param typeName type of governance engine to create
     * @param qualifiedName unique name for the governance engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of governance services that will be associated with
     *                    this governance engine.
     *
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    public  String  createGovernanceEngine(String userId,
                                           String typeName,
                                           String qualifiedName,
                                           String displayName,
                                           String description) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "createGovernanceEngine";
        final String typeNameParameterName = "typeName";

        invalidParameterHandler.validateName(typeName, typeNameParameterName, methodName);

        return governanceEngineHandler.createSoftwareCapability(userId,
                                                                null,
                                                                null,
                                                                typeName,
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
                                                                null,
                                                                null,
                                                                null,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName);
    }


    /**
     * Return the properties from a governance engine definition.  The governance engine
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance engine definition.
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByGUID(String userId,
                                                             String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final  String   methodName = "getGovernanceEngineByGUID";
        final  String   guidParameter = "guid";

        return governanceEngineHandler.getBeanFromRepository(userId,
                                                            guid,
                                                            guidParameter,
                                                            OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                             false,
                                                             false,
                                                             new Date(),
                                                            methodName);
    }


    /**
     * Return the properties from a governance engine definition.  The governance engine
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByName(String userId,
                                                             String name) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final  String   methodName = "getGovernanceEngineByName";
        final  String   nameParameter = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return governanceEngineHandler.getBeanByValue(userId,
                                                     name,
                                                     nameParameter,
                                                     OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_GUID,
                                                     OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                     specificMatchPropertyNames,
                                                     false,
                                                     false,
                                                     null,
                                                     methodName);
    }


    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of governance engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definitions.
     */
    public  List<GovernanceEngineElement> getAllGovernanceEngines(String userId,
                                                                  int    startingFrom,
                                                                  int    maximumResults) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "getAllGovernanceEngines";

        return governanceEngineHandler.getBeansByType(userId,
                                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                      null,
                                                      startingFrom,
                                                      maximumResults,
                                                      false,
                                                      false,
                                                      new Date (),
                                                      methodName);
    }


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName new value for unique name of governance engine.
     * @param displayName new value for the display name.
     * @param description new description for the governance engine.
     * @param typeDescription new description of the type ofg governance engine.
     * @param version new version number for the governance engine implementation.
     * @param patchLevel new patch level for the governance engine implementation.
     * @param source new source description for the implementation of the governance engine.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    public void updateGovernanceEngine(String                userId,
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
        final String methodName = "updateGovernanceEngine";
        final String guidParameter = "guid";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        SoftwareCapabilityBuilder builder = new SoftwareCapabilityBuilder(qualifiedName,
                                                                          displayName,
                                                                          description,
                                                                          typeDescription,
                                                                          version,
                                                                          patchLevel,
                                                                          source,
                                                                          additionalProperties,
                                                                          OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                          extendedProperties,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        InstanceProperties properties = builder.getInstanceProperties(methodName);

        governanceEngineHandler.updateBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      guidParameter,
                                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                      properties,
                                                      false,
                                                      methodName);
    }


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public void deleteGovernanceEngine(String userId,
                                       String guid,
                                       String qualifiedName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "deleteGovernanceEngine";
        final String guidParameter = "governanceEngineGUID";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        governanceEngineHandler.deleteBeanInRepository(userId,
                                                      null,
                                                      null,
                                                      guid,
                                                      guidParameter,
                                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                      qualifiedNameParameter,
                                                      qualifiedName,
                                                       false,
                                                       false,
                                                       new Date(),
                                                      methodName);
    }


    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param userId identifier of calling user
     * @param typeName type of governance service
     * @param qualifiedName  unique name for the governance service.
     * @param displayName   display name for the governance service.
     * @param description  description of the analysis provided by the governance service.
     * @param connection   connection to instantiate the governance service implementation.
     *
     * @return unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    public String createGovernanceService(String     userId,
                                          String     typeName,
                                          String     qualifiedName,
                                          String     displayName,
                                          String     description,
                                          Connection connection) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "createGovernanceService";
        final String connectionParameterName = "createGovernanceService";
        final String assetGUIDParameterName = "assetGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID ";
        final String typeNameParameterName = "typeName";

        invalidParameterHandler.validateName(typeName, typeNameParameterName, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        Date effectiveTime = new Date();

        String assetGUID = governanceServiceHandler.createAssetInRepository(userId,
                                                                            null,
                                                                            null,
                                                                            qualifiedName,
                                                                            displayName,
                                                                            null,
                                                                            description,
                                                                            null,
                                                                            typeName,
                                                                            null,
                                                                            InstanceStatus.ACTIVE,
                                                                            null,
                                                                            null,
                                                                            effectiveTime,
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
                                                                                          connectorType.getSupportedAssetTypeName(),
                                                                                          connectorType.getExpectedDataFormat(),
                                                                                          connectorType.getConnectorProviderClassName(),
                                                                                          connectorType.getConnectorFrameworkName(),
                                                                                          connectorType.getConnectorInterfaceLanguage(),
                                                                                          connectorType.getConnectorInterfaces(),
                                                                                          connectorType.getTargetTechnologySource(),
                                                                                          connectorType.getTargetTechnologyName(),
                                                                                          connectorType.getTargetTechnologyInterfaces(),
                                                                                          connectorType.getTargetTechnologyVersions(),
                                                                                          connectorType.getRecognizedAdditionalProperties(),
                                                                                          connectorType.getRecognizedSecuredProperties(),
                                                                                          connectorType.getRecognizedConfigurationProperties(),
                                                                                          connectorType.getAdditionalProperties(),
                                                                                          false,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          methodName);

            if (connectorTypeGUID != null)
            {
                if (connection instanceof VirtualConnection)
                {
                    /*
                     * OpenGovernancePipelines are represented using a VirtualConnection that
                     * nests all the Connections for services to call.
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
                                                                                     null,
                                                                                     null,
                                                                                     false,
                                                                                     false,
                                                                                     effectiveTime,
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
                                                                                                 OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                                 embeddedConnection.getEmbeddedConnection(),
                                                                                                 null,
                                                                                                 false,
                                                                                                 false,
                                                                                                 effectiveTime,
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
                                                                        null,
                                                                        null,
                                                                        false,
                                                                        false,
                                                                        effectiveTime,
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
                                                       null,
                                                       null,
                                                       false,
                                                       false,
                                                       effectiveTime,
                                                       methodName);
                }
            }
        }
        
        return assetGUID;
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public  GovernanceServiceElement getGovernanceServiceByGUID(String    userId,
                                                                String    guid) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final  String   methodName = "getGovernanceServiceByGUID";
        final  String   guidParameter = "guid";

        return governanceServiceHandler.getAssetWithConnection(userId,
                                                              guid,
                                                              guidParameter,
                                                              OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName);
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public  GovernanceServiceElement getGovernanceServiceByName(String userId,
                                                                String name) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName    = "getGovernanceServiceByName";
        final String nameParameter = "name";

        return governanceServiceHandler.getAssetByNameWithConnection(userId,
                                                                     name,
                                                                     nameParameter,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                     false,
                                                                     false,
                                                                     new Date(),
                                                                     methodName);
    }


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definitions.
     */
    public  List<GovernanceServiceElement> getAllGovernanceServices(String  userId,
                                                                    int     startingFrom,
                                                                    int     maximumResults) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final  String   methodName = "getAllGovernanceServices";

        return governanceServiceHandler.getAllAssetsWithConnection(userId,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                  startingFrom,
                                                                  maximumResults,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                  methodName);
    }


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param userId identifier of calling user
     * @param governanceServiceGUID governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public List<String> getGovernanceServiceRegistrations(String userId,
                                                          String governanceServiceGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getAllGovernanceServices";
        final String guidParameter = "governanceServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, guidParameter, methodName);

        Date effectiveTime = new Date();

        /*
         * Checks this is a valid, visible service.
         */
        connectionHandler.getBeanFromRepository(userId,
                                                governanceServiceGUID,
                                                guidParameter,
                                                OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                false,
                                                false,
                                                effectiveTime,
                                                methodName);

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     governanceServiceGUID,
                                                                                     OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                     OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                     OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                     1,
                                                                                     false,
                                                                                     false,
                                                                                     0, 0,
                                                                                     effectiveTime,
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
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName new value for unique name of governance service.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this governance service.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    public void updateGovernanceService(String              userId,
                                        String              guid,
                                        String              qualifiedName,
                                        String              displayName,
                                        String              description,
                                        Connection          connection,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateGovernanceService";
        final String guidParameter = "guid";

        governanceServiceHandler.updateAssetWithConnection(userId,
                                                          null,
                                                          null,
                                                          guid,
                                                          guidParameter,
                                                          qualifiedName,
                                                          displayName,
                                                          null,
                                                          description,
                                                          additionalProperties,
                                                          OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_GUID,
                                                          OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                          extendedProperties,
                                                          null,
                                                          null,
                                                          null,
                                                          connection,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName);
    }


    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName unique name for the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public void deleteGovernanceService(String userId,
                                        String guid,
                                        String qualifiedName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "deleteGovernanceService";
        final String qualifiedNameParameter = "qualifiedName";
        final String guidParameter = "governanceServiceGUID";

        connectionHandler.deleteBeanInRepository(userId,
                                                 null,
                                                 null,
                                                 guid,
                                                 guidParameter,
                                                 OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_GUID,
                                                 OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                 qualifiedNameParameter,
                                                 qualifiedName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
    }


    /**
     * Register a governance service with a specific governance engine.   Both the
     * governance service and the governance engine already exist, so it is
     * just a question of creating a relationship between them.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType list of governance request types that this governance service is able to process.
     * @param defaultAnalysisParameters list of analysis parameters that are passed to the governance service (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public void registerGovernanceServiceWithEngine(String              userId,
                                                    String              governanceEngineGUID,
                                                    String              governanceServiceGUID,
                                                    String              governanceRequestType,
                                                    String              serviceRequestType,
                                                    Map<String, String> defaultAnalysisParameters) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "registerGovernanceServiceWithEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String governanceRequestTypeParameter = "governanceRequestType";

        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);
        invalidParameterHandler.validateName(governanceRequestType, governanceRequestTypeParameter, methodName);

        /*
         * First check if this request type has already been registered.
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       governanceEngineGUID,
                                                                                       OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                       2,
                                                                                       false,
                                                                                       false,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       null,
                                                                                       methodName);


        while (iterator.moreToReceive())
        {
            Relationship supportedGovernanceService = iterator.getNext();

            if (supportedGovernanceService != null)
            {
                String existingRequestType = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                                supportedGovernanceService.getProperties(),
                                                                                methodName);

                if (governanceRequestType.equals(existingRequestType))
                {
                    /*
                     * The request type is already registered.  Is it registered to the same governance service?
                     */
                    EntityProxy existingGovernanceServiceProxy = supportedGovernanceService.getEntityTwoProxy();

                    if ((existingGovernanceServiceProxy != null) && (governanceServiceGUID.equals(existingGovernanceServiceProxy.getGUID())))
                    {
                        /*
                         * The existing registration is for the requested governance service.  All that needs to be done
                         * is to set the request parameters to match the supplied values.
                         */
                        InstanceProperties properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                                        supportedGovernanceService.getProperties(),
                                                                                                        OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                                        defaultAnalysisParameters,
                                                                                                        methodName);

                        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  properties,
                                                                                  OpenMetadataAPIMapper.SERVICE_REQUEST_TYPE_PROPERTY_NAME,
                                                                                  serviceRequestType,
                                                                                  methodName);
                        repositoryHandler.updateRelationshipProperties(userId,
                                                                       null,
                                                                       null,
                                                                       supportedGovernanceService,
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
                                                             supportedGovernanceService,
                                                             methodName);
                    }
                }
            }
        }

        /*
         * If this code executes it means that the governance service can be registered with the governance engine
         * using the supplied request type.
         */

        InstanceProperties instanceProperties = new InstanceProperties();

        repositoryHelper.addStringPropertyToInstance(serviceName,
                                                     instanceProperties,
                                                     OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                     governanceRequestType,
                                                     methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataAPIMapper.SERVICE_REQUEST_TYPE_PROPERTY_NAME,
                                                                          serviceRequestType,
                                                                          methodName);

        repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                        instanceProperties,
                                                        OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                        defaultAnalysisParameters,
                                                        methodName);

        governanceEngineHandler.multiLinkElementToElement(userId,
                                                          null,
                                                          null,
                                                          governanceEngineGUID,
                                                          governanceEngineGUIDParameter,
                                                          OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                          governanceServiceGUID,
                                                          governanceServiceGUIDParameter,
                                                          OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                          false,
                                                          false,
                                                          governanceEngineHandler.getSupportedZones(),
                                                          OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                          OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                          instanceProperties,
                                                          new Date(),
                                                          methodName);
    }


    /**
     * Retrieve a specific governance service registrations with a particular governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the registered governance service.
     *
     * @return details of the governance service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService(String  userId,
                                                                             String  governanceEngineGUID,
                                                                             String  governanceServiceGUID) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceService";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                             governanceServiceGUID,
                                                                                             OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                             governanceEngineGUID,
                                                                                             OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                                                             OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                             1,
                                                                                             false,
                                                                                             false,
                                                                                             null,
                                                                                             methodName);


        if (relationships != null)
        {
            RegisteredGovernanceServiceConverter converter = new RegisteredGovernanceServiceConverter(repositoryHelper, serviceName);

            return converter.getBean(this.getGovernanceServiceByGUID(userId, governanceServiceGUID), relationships);
        }

        return null;
    }


    /**
     * Retrieve the identifiers of the registered governance services with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public List<RegisteredGovernanceServiceElement> getRegisteredGovernanceServices(String userId,
                                                                                    String governanceEngineGUID,
                                                                                    int    startingFrom,
                                                                                    int    maximumResults) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceServices";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";

        List<Relationship> relationships = governanceEngineHandler.getAttachmentLinks(userId,
                                                                                      governanceEngineGUID,
                                                                                      governanceEngineGUIDParameter,
                                                                                      OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                                      OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                                                      OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                      null,
                                                                                      OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                      2,
                                                                                      false,
                                                                                      false,
                                                                                      startingFrom,
                                                                                      maximumResults,
                                                                                      new Date(),
                                                                                      methodName);

        if (relationships != null)
        {
            Map<String, RegisteredGovernanceServiceElement>  governanceServices = new HashMap<>();

            for (Relationship relationship : relationships)
            {
                /*
                 * Process Governance Service (end 2)
                 */
                EntityProxy end2 = relationship.getEntityTwoProxy();

                RegisteredGovernanceServiceElement governanceService = governanceServices.get(end2.getGUID());

                if (governanceService == null)
                {
                    try
                    {
                        GovernanceServiceElement newElement = this.getGovernanceServiceByGUID(userId, end2.getGUID());

                        if (newElement != null)
                        {
                            governanceService = new RegisteredGovernanceServiceElement(newElement);

                            governanceServices.put(end2.getGUID(), governanceService);
                        }
                    }
                    catch (Exception notKnown)
                    {
                        /* ignore */
                    }
                }

                if (governanceService != null)
                {
                    /*
                     * Build the request type list for the service.
                     */
                    String requestType = repositoryHelper.getStringProperty(serviceName,
                                                                            OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                            relationship.getProperties(),
                                                                            methodName);

                    if (requestType != null)
                    {
                        RegisteredGovernanceServiceProperties relationshipProperties = new RegisteredGovernanceServiceProperties();

                        relationshipProperties.setServiceRequestType(repositoryHelper.getStringProperty(serviceName,
                                                                                                        OpenMetadataAPIMapper.SERVICE_REQUEST_TYPE_PROPERTY_NAME,
                                                                                                        relationship.getProperties(),
                                                                                                        methodName));
                        relationshipProperties.setRequestParameters(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                                              OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                                              relationship.getProperties(),
                                                                                                              methodName));

                        Map<String, RegisteredGovernanceServiceProperties> requestTypes = governanceService.getProperties().getRequestTypes();

                        if (requestTypes == null)
                        {
                            requestTypes = new HashMap<>();
                        }

                        requestTypes.put(requestType, relationshipProperties);

                        governanceService.getProperties().setRequestTypes(requestTypes);
                    }
                }
            }

            if (! governanceServices.isEmpty())
            {
                return new ArrayList<>(governanceServices.values());
            }
        }

        return null;
    }


    /**
     * Remove a request type for a governance service from the governance engine.
     *
     * @param userId identifier of calling user
     * @param requestType calling request
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public void unregisterGovernanceServiceRequestFromEngine(String userId,
                                                             String requestType,
                                                             String governanceEngineGUID,
                                                             String governanceServiceGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "unregisterGovernanceServiceFromEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       governanceEngineGUID,
                                                                                       OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                       2,
                                                                                       false,
                                                                                       false,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       null,
                                                                                       methodName);


        while (iterator.moreToReceive())
        {
            Relationship supportedGovernanceService = iterator.getNext();

            if (supportedGovernanceService != null)
            {
                String existingRequestType = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                                supportedGovernanceService.getProperties(),
                                                                                methodName);

                if (requestType.equals(existingRequestType))
                {
                    governanceEngineHandler.unlinkElementFromElement(userId,
                                                                     false,
                                                                     null,
                                                                     null,
                                                                     governanceEngineGUID,
                                                                     governanceEngineGUIDParameter,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                     governanceServiceGUID,
                                                                     governanceServiceGUIDParameter,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                     false,
                                                                     false,
                                                                     OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                     supportedGovernanceService,
                                                                     new Date(),
                                                                     methodName);
                    return;
                }
            }
        }
    }



    /**
     * Unregister all request types for a governance service from the governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public void unregisterGovernanceServiceFromEngine(String userId,
                                                      String governanceEngineGUID,
                                                      String governanceServiceGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "unregisterGovernanceServiceFromEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       governanceEngineGUID,
                                                                                       OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_GUID,
                                                                                       OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                                       2,
                                                                                       false,
                                                                                       false,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       null,
                                                                                       methodName);


        while (iterator.moreToReceive())
        {
            Relationship supportedGovernanceService = iterator.getNext();

            if (supportedGovernanceService != null)
            {
                EntityProxy governanceServiceProxy = supportedGovernanceService.getEntityTwoProxy();

                if ((governanceServiceProxy != null) && (governanceServiceProxy.getGUID().equals(governanceServiceGUID)))
                {
                    governanceEngineHandler.unlinkElementFromElement(userId,
                                                                     false,
                                                                     null,
                                                                     null,
                                                                     governanceEngineGUID,
                                                                     governanceEngineGUIDParameter,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_ENGINE_TYPE_NAME,
                                                                     governanceServiceGUID,
                                                                     governanceServiceGUIDParameter,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.GOVERNANCE_SERVICE_TYPE_NAME,
                                                                     false,
                                                                     false,
                                                                     OpenMetadataAPIMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                     supportedGovernanceService,
                                                                     new Date(),
                                                                     methodName);
                }
            }
        }
    }
}
