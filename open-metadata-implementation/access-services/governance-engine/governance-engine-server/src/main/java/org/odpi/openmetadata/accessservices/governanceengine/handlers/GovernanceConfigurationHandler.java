/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.converters.*;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.*;
import org.odpi.openmetadata.accessservices.governanceengine.properties.CatalogTargetProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.IntegrationGroupProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredGovernanceServiceProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;


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
    private final SoftwareCapabilityHandler<IntegrationGroupElement> integrationGroupHandler;
    private final AssetHandler<GovernanceServiceElement>             governanceServiceHandler;
    private final AssetHandler<IntegrationConnectorElement>          integrationConnectorHandler;
    private final ConnectionHandler<Connection>                      connectionHandler;
    private final InvalidParameterHandler                            invalidParameterHandler;
    private final RegisteredIntegrationConnectorConverter            registeredIntegrationConnectorConverter;
    private final CatalogTargetConverter<CatalogTarget>              catalogTargetConverter;


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
        this.registeredIntegrationConnectorConverter = new RegisteredIntegrationConnectorConverter(repositoryHelper, serviceName);
        this.catalogTargetConverter = new CatalogTargetConverter<>(repositoryHelper, serviceName, serverName);

        this.governanceEngineHandler = new SoftwareCapabilityHandler<>(new GovernanceEngineConverter<>(repositoryHelper, serviceName, serverName),
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

        this.integrationGroupHandler = new SoftwareCapabilityHandler<>(new IntegrationGroupConverter<>(repositoryHelper, serviceName, serverName),
                                                                       IntegrationGroupElement.class,
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

        this.integrationConnectorHandler = new AssetHandler<>(new IntegrationConnectorConverter<>(repositoryHelper, serviceName, serverName),
                                                              IntegrationConnectorElement.class,
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
                                                             OpenMetadataType.GOVERNANCE_ENGINE.typeName,
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
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);

        return governanceEngineHandler.getBeanByValue(userId,
                                                      name,
                                                      nameParameter,
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
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
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
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
                                                                          OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                                          OpenMetadataType.GOVERNANCE_ENGINE.typeName,
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
                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeName,
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
                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeName,
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
            connectionHandler.saveConnection(userId,
                                             null,
                                             null,
                                             assetGUID,
                                             assetGUID,
                                             assetGUIDParameterName,
                                             typeName,
                                             qualifiedName,
                                             connection,
                                             "Connection to create governance service",
                                             false,
                                             false,
                                             effectiveTime,
                                             methodName);
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
                                                               OpenMetadataType.GOVERNANCE_SERVICE.typeName,
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
                                                                     OpenMetadataType.GOVERNANCE_SERVICE.typeGUID,
                                                                     OpenMetadataType.GOVERNANCE_SERVICE.typeName,
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
                                                                   OpenMetadataType.GOVERNANCE_SERVICE.typeGUID,
                                                                   OpenMetadataType.GOVERNANCE_SERVICE.typeName,
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
                                                OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                false,
                                                false,
                                                effectiveTime,
                                                methodName);

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     governanceServiceGUID,
                                                                                     OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                                     OpenMetadataType.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                     OpenMetadataType.CONNECTION_TO_ASSET_TYPE_NAME,
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
                                                           OpenMetadataType.GOVERNANCE_SERVICE.typeGUID,
                                                           OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                           extendedProperties,
                                                           null,
                                                           null,
                                                           false,
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
                                                 OpenMetadataType.GOVERNANCE_SERVICE.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_SERVICE.typeName,
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
     * @param serviceRequestType request type supported by the service
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
                                                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
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
                                                                                OpenMetadataProperty.REQUEST_TYPE.name,
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
                                                                                                        OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                                        defaultAnalysisParameters,
                                                                                                        methodName);

                        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  properties,
                                                                                  OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
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
                                                     OpenMetadataProperty.REQUEST_TYPE.name,
                                                     governanceRequestType,
                                                     methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                                          serviceRequestType,
                                                                          methodName);

        instanceProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                             instanceProperties,
                                                                             OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                             defaultAnalysisParameters,
                                                                             methodName);

        governanceEngineHandler.multiLinkElementToElement(userId,
                                                          null,
                                                          null,
                                                          governanceEngineGUID,
                                                          governanceEngineGUIDParameter,
                                                          OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                          governanceServiceGUID,
                                                          governanceServiceGUIDParameter,
                                                          OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                          false,
                                                          false,
                                                          governanceEngineHandler.getSupportedZones(),
                                                          OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                          OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
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
                                                                                             OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                                             governanceEngineGUID,
                                                                                             OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                             OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
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
                                                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                                      OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                      OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                                                      null,
                                                                                      OpenMetadataType.GOVERNANCE_SERVICE.typeName,
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
                                                                            OpenMetadataProperty.REQUEST_TYPE.name,
                                                                            relationship.getProperties(),
                                                                            methodName);

                    if (requestType != null)
                    {
                        RegisteredGovernanceServiceProperties relationshipProperties = new RegisteredGovernanceServiceProperties();

                        relationshipProperties.setServiceRequestType(repositoryHelper.getStringProperty(serviceName,
                                                                                                        OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                                                                        relationship.getProperties(),
                                                                                                        methodName));
                        relationshipProperties.setRequestParameters(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                                              OpenMetadataProperty.REQUEST_PARAMETERS.name,
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
                                                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
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
                                                                                OpenMetadataProperty.REQUEST_TYPE.name,
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
                                                                     OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                     governanceServiceGUID,
                                                                     governanceServiceGUIDParameter,
                                                                     OpenMetadataType.GOVERNANCE_SERVICE.typeGUID,
                                                                     OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                     false,
                                                                     false,
                                                                     OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
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
                                                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
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
                                                                     OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                     governanceServiceGUID,
                                                                     governanceServiceGUIDParameter,
                                                                     OpenMetadataType.GOVERNANCE_SERVICE.typeGUID,
                                                                     OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                     false,
                                                                     false,
                                                                     OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                                     supportedGovernanceService,
                                                                     new Date(),
                                                                     methodName);
                }
            }
        }
    }


    /*
     * Support for integration groups
     */

    /**
     * Create a new integration group definition.
     *
     * @param userId identifier of calling user
     * @param properties properties for the integration group.
     *
     * @return unique identifier (guid) of the integration group definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    public  String  createIntegrationGroup(String                     userId,
                                           IntegrationGroupProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "createIntegrationGroup";

        return integrationGroupHandler.createSoftwareCapability(userId,
                                                                null,
                                                                null,
                                                                OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                                null,
                                                                properties.getQualifiedName(),
                                                                properties.getDisplayName(),
                                                                properties.getDescription(),
                                                                properties.getTypeDescription(),
                                                                properties.getVersion(),
                                                                properties.getPatchLevel(),
                                                                properties.getSource(),
                                                                properties.getAdditionalProperties(),
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
     * Return the properties from an integration group definition.  The integration group
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the integration group definition.
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public IntegrationGroupElement getIntegrationGroupByGUID(String userId,
                                                             String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final  String   methodName = "getIntegrationGroupByGUID";
        final  String   guidParameter = "guid";

        return integrationGroupHandler.getBeanFromRepository(userId,
                                                             guid,
                                                             guidParameter,
                                                             OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                             false,
                                                             false,
                                                             new Date(),
                                                             methodName);
    }


    /**
     * Return the properties from an integration group definition.  The integration group
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public IntegrationGroupElement getIntegrationGroupByName(String userId,
                                                             String name) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final  String   methodName = "getIntegrationGroupByName";
        final  String   nameParameter = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);

        return integrationGroupHandler.getBeanByValue(userId,
                                                      name,
                                                      nameParameter,
                                                      OpenMetadataType.INTEGRATION_GROUP_TYPE_GUID,
                                                      OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                      specificMatchPropertyNames,
                                                      false,
                                                      false,
                                                      null,
                                                      methodName);
    }


    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of integration group definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definitions.
     */
    public  List<IntegrationGroupElement> getAllIntegrationGroups(String userId,
                                                                  int    startingFrom,
                                                                  int    maximumResults) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "getAllIntegrationGroups";

        return integrationGroupHandler.getBeansByType(userId,
                                                      OpenMetadataType.INTEGRATION_GROUP_TYPE_GUID,
                                                      OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                      null,
                                                      startingFrom,
                                                      maximumResults,
                                                      false,
                                                      false,
                                                      new Date (),
                                                      methodName);
    }


    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties properties for the integration group.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    public void updateIntegrationGroup(String                     userId,
                                       String                     guid,
                                       boolean                    isMergeUpdate,
                                       IntegrationGroupProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateIntegrationGroup";
        final String guidParameter = "guid";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        SoftwareCapabilityBuilder builder = new SoftwareCapabilityBuilder(properties.getQualifiedName(),
                                                                          properties.getDisplayName(),
                                                                          properties.getDescription(),
                                                                          properties.getTypeDescription(),
                                                                          properties.getVersion(),
                                                                          properties.getPatchLevel(),
                                                                          properties.getSource(),
                                                                          properties.getAdditionalProperties(),
                                                                          OpenMetadataType.INTEGRATION_GROUP_TYPE_GUID,
                                                                          OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                                          null,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        InstanceProperties instanceProperties = builder.getInstanceProperties(methodName);

        integrationGroupHandler.updateBeanInRepository(userId,
                                                       null,
                                                       null,
                                                       guid,
                                                       guidParameter,
                                                       OpenMetadataType.INTEGRATION_GROUP_TYPE_GUID,
                                                       OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                       instanceProperties,
                                                       isMergeUpdate,
                                                       methodName);
    }


    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param qualifiedName unique name for the integration group.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public void deleteIntegrationGroup(String userId,
                                       String guid,
                                       String qualifiedName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "deleteIntegrationGroup";
        final String guidParameter = "integrationGroupGUID";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        integrationGroupHandler.deleteBeanInRepository(userId,
                                                       null,
                                                       null,
                                                       guid,
                                                       guidParameter,
                                                       OpenMetadataType.INTEGRATION_GROUP_TYPE_GUID,
                                                       OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                       qualifiedNameParameter,
                                                       qualifiedName,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
    }



    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param userId identifier of calling user
     * @param qualifiedName  unique name for the integration connector.
     * @param displayName   display name for the integration connector.
     * @param versionIdentifier identifier if the version
     * @param description  description of the analysis provided by the integration connector.
     * @param usesBlockingCalls the connector issues blocking calls and needs a dedicated thread.
     * @param additionalProperties additional properties
     * @param connection   connection to instantiate the integration connector implementation.
     *
     * @return unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration connector definition.
     */
    public String createIntegrationConnector(String              userId,
                                             String              qualifiedName,
                                             String              versionIdentifier,
                                             String              displayName,
                                             String              description,
                                             boolean             usesBlockingCalls,
                                             Map<String, String> additionalProperties,
                                             Connection          connection) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "createIntegrationConnector";
        final String connectionParameterName = "createIntegrationConnector";
        final String assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        Date effectiveTime = new Date();

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataType.USES_BLOCKING_CALLS_PROPERTY_NAME, usesBlockingCalls);

        String assetGUID = integrationConnectorHandler.createAssetInRepository(userId,
                                                                               null,
                                                                               null,
                                                                               qualifiedName,
                                                                               displayName,
                                                                               versionIdentifier,
                                                                               description,
                                                                               additionalProperties,
                                                                               OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                               extendedProperties,
                                                                               InstanceStatus.ACTIVE,
                                                                               null,
                                                                               null,
                                                                               effectiveTime,
                                                                               methodName);

        if (assetGUID != null)
        {
            connectionHandler.saveConnection(userId,
                                             null,
                                             null,
                                             assetGUID,
                                             assetGUID,
                                             assetGUIDParameterName,
                                             OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                             qualifiedName,
                                             connection,
                                             "Connection to create integration connector",
                                             false,
                                             false,
                                             effectiveTime,
                                             methodName);
        }

        return assetGUID;
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the integration connector definition.
     *
     * @return properties of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public  IntegrationConnectorElement getIntegrationConnectorByGUID(String    userId,
                                                                      String    guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final  String   methodName = "getIntegrationConnectorByGUID";
        final  String   guidParameter = "guid";

        return integrationConnectorHandler.getAssetWithConnection(userId,
                                                                  guid,
                                                                  guidParameter,
                                                                  OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration connector definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public  IntegrationConnectorElement getIntegrationConnectorByName(String userId,
                                                                      String name) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName    = "getIntegrationConnectorByName";
        final String nameParameter = "name";

        return integrationConnectorHandler.getAssetByNameWithConnection(userId,
                                                                        name,
                                                                        nameParameter,
                                                                        OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_GUID,
                                                                        OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName);
    }


    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of integration connector definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definitions.
     */
    public  List<IntegrationConnectorElement> getAllIntegrationConnectors(String  userId,
                                                                          int     startingFrom,
                                                                          int     maximumResults) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final  String   methodName = "getAllIntegrationConnectors";

        return integrationConnectorHandler.getAllAssetsWithConnection(userId,
                                                                      OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_GUID,
                                                                      OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                      startingFrom,
                                                                      maximumResults,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName);
    }


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param userId identifier of calling user
     * @param integrationConnectorGUID integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public List<String> getIntegrationConnectorRegistrations(String userId,
                                                             String integrationConnectorGUID) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "getAllIntegrationConnectors";
        final String guidParameter = "integrationConnectorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, guidParameter, methodName);

        Date effectiveTime = new Date();

        /*
         * Checks this is a valid, visible service.
         */
        connectionHandler.getEntityFromRepository(userId,
                                                  integrationConnectorGUID,
                                                  guidParameter,
                                                  OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                  null,
                                                  null,
                                                  false,
                                                  false,
                                                  effectiveTime,
                                                  methodName);

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     integrationConnectorGUID,
                                                                                     OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                                     OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID,
                                                                                     OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
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
                    if (relationship.getEntityOneProxy().getGUID() != null)
                    {
                        results.add(relationship.getEntityOneProxy().getGUID());
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
     * Update the properties of an existing integration connector definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param qualifiedName new value for unique name of integration connector.
     * @param versionIdentifier version identifier of the connector.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this integration connector.
     * @param additionalProperties additional properties for the integration group.
     * @param extendedProperties properties to populate the subtype of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration connector definition.
     */
    public void updateIntegrationConnector(String              userId,
                                           String              guid,
                                           boolean             isMergeUpdate,
                                           String              qualifiedName,
                                           String              versionIdentifier,
                                           String              displayName,
                                           String              description,
                                           Connection          connection,
                                           Map<String, String> additionalProperties,
                                           Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "updateIntegrationConnector";
        final String guidParameter = "guid";

        integrationConnectorHandler.updateAssetWithConnection(userId,
                                                              null,
                                                              null,
                                                              guid,
                                                              guidParameter,
                                                              qualifiedName,
                                                              displayName,
                                                              versionIdentifier,
                                                              description,
                                                              additionalProperties,
                                                              OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_GUID,
                                                              OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                              extendedProperties,
                                                              null,
                                                              null,
                                                              isMergeUpdate,
                                                              null,
                                                              connection,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName);
    }


    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param qualifiedName unique name for the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public void deleteIntegrationConnector(String userId,
                                           String guid,
                                           String qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "deleteIntegrationConnector";
        final String qualifiedNameParameter = "qualifiedName";
        final String guidParameter = "integrationConnectorGUID";

        connectionHandler.deleteBeanInRepository(userId,
                                                 null,
                                                 null,
                                                 guid,
                                                 guidParameter,
                                                 OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_GUID,
                                                 OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                 qualifiedNameParameter,
                                                 qualifiedName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
    }


    /**
     * Register an integration connector with a specific integration group.   Both the
     * integration connector and the integration group already exist, so it is
     * just a question of creating a relationship between them.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param connectorName list of governance request types that this integration connector is able to process.
     * @param connectorUserId request type supported by the service
     * @param metadataSourceQualifiedName list of analysis parameters that are passed to the integration connector (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     * @param startDate earliest date to start the connector
     * @param refreshTimeInterval how often to run the connector
     * @param stopDate latest time the connector should run
     * @param permittedSynchronization which direction should synchronization flow?
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public void registerIntegrationConnectorWithGroup(String                   userId,
                                                      String                   integrationGroupGUID,
                                                      String                   integrationConnectorGUID,
                                                      String                   connectorName,
                                                      String                   connectorUserId,
                                                      String                   metadataSourceQualifiedName,
                                                      Date                     startDate,
                                                      long                     refreshTimeInterval,
                                                      Date                     stopDate,
                                                      PermittedSynchronization permittedSynchronization) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String methodName = "registerIntegrationConnectorWithGroup";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String governanceRequestTypeParameter = "connectorName";

        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validateName(connectorName, governanceRequestTypeParameter, methodName);

        InstanceProperties instanceProperties = new InstanceProperties();

        repositoryHelper.addStringPropertyToInstance(serviceName,
                                                     instanceProperties,
                                                     OpenMetadataType.CONNECTOR_NAME_PROPERTY_NAME,
                                                     connectorName,
                                                     methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.CONNECTOR_USER_ID_PROPERTY_NAME,
                                                                          connectorUserId,
                                                                          methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME,
                                                                          metadataSourceQualifiedName,
                                                                          methodName);

        instanceProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                        instanceProperties,
                                                                        OpenMetadataType.START_DATE_PROPERTY_NAME,
                                                                        startDate,
                                                                        methodName);

        instanceProperties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                        instanceProperties,
                                                                        OpenMetadataType.REFRESH_TIME_INTERVAL_PROPERTY_NAME,
                                                                        refreshTimeInterval,
                                                                        methodName);

        instanceProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                        instanceProperties,
                                                                        OpenMetadataType.STOP_DATE_PROPERTY_NAME,
                                                                        stopDate,
                                                                        methodName);

        if (permittedSynchronization != null)
        {
            try
            {
                instanceProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                instanceProperties,
                                                                                OpenMetadataType.PERMITTED_SYNC_PROPERTY_NAME,
                                                                                OpenMetadataType.PERMITTED_SYNC_ENUM_TYPE_GUID,
                                                                                OpenMetadataType.PERMITTED_SYNC_ENUM_TYPE_NAME,
                                                                                permittedSynchronization.getOpenTypeOrdinal(),
                                                                                methodName);
            }
            catch (TypeErrorException error)
            {
                throw new InvalidParameterException(error, OpenMetadataType.KEY_PATTERN_PROPERTY_NAME);
            }
        }

        integrationGroupHandler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     integrationGroupGUID,
                                                     integrationGroupGUIDParameter,
                                                     OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                     integrationConnectorGUID,
                                                     integrationConnectorGUIDParameter,
                                                     OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                     false,
                                                     false,
                                                     integrationGroupHandler.getSupportedZones(),
                                                     OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID,
                                                     OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
                                                     instanceProperties,
                                                     null,
                                                     null,
                                                     new Date(),
                                                     methodName);
    }


    /**
     * Retrieve a specific integration connector registrations with a particular integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the registered integration connector.
     *
     * @return details of the integration connector and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String  userId,
                                                                                   String  integrationGroupGUID,
                                                                                   String  integrationConnectorGUID) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        final String methodName = "getRegisteredIntegrationConnector";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                             integrationConnectorGUID,
                                                                                             OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                                             integrationGroupGUID,
                                                                                             OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID,
                                                                                             OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                                             1,
                                                                                             false,
                                                                                             false,
                                                                                             null,
                                                                                             methodName);


        if (relationships != null)
        {
            if (!relationships.isEmpty())
            {
                return registeredIntegrationConnectorConverter.getBean(this.getIntegrationConnectorByGUID(userId, integrationConnectorGUID), relationships.get(0));
            }
        }

        return null;
    }


    /**
     * Retrieve the identifiers of the registered integration connectors with an integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public List<RegisteredIntegrationConnectorElement> getRegisteredIntegrationConnectors(String userId,
                                                                                          String integrationGroupGUID,
                                                                                          int    startingFrom,
                                                                                          int    maximumResults) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        final String methodName = "getRegisteredIntegrationConnectors";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";

        List<Relationship> relationships = integrationGroupHandler.getAttachmentLinks(userId,
                                                                                      integrationGroupGUID,
                                                                                      integrationGroupGUIDParameter,
                                                                                      OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                                                      OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID,
                                                                                      OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                                      null,
                                                                                      OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                                      2,
                                                                                      false,
                                                                                      false,
                                                                                      startingFrom,
                                                                                      maximumResults,
                                                                                      new Date(),
                                                                                      methodName);

        if (relationships != null)
        {
            List<RegisteredIntegrationConnectorElement> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                IntegrationConnectorElement connectorElement = this.getIntegrationConnectorByGUID(userId, relationship.getEntityTwoProxy().getGUID());

                results.add(registeredIntegrationConnectorConverter.getBean(connectorElement, relationship));
            }

            return results;
        }

        return null;
    }


    /**
     * Unregister all request types for an integration connector from the integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public void unregisterIntegrationConnectorFromGroup(String userId,
                                                        String integrationGroupGUID,
                                                        String integrationConnectorGUID) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "unregisterIntegrationConnectorFromGroup";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        integrationGroupHandler.unlinkElementFromElement(userId,
                                                         false,
                                                         null,
                                                         null,
                                                         integrationGroupGUID,
                                                         integrationGroupGUIDParameter,
                                                         OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME,
                                                         integrationConnectorGUID,
                                                         integrationConnectorGUIDParameter,
                                                         OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_GUID,
                                                         OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                         false,
                                                         false,
                                                         OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID,
                                                         OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
                                                         new Date(),
                                                         methodName);
    }


    /**
     * Add a catalog target to an integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param properties properties for the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    public void addCatalogTarget(String                  userId,
                                 String                  integrationConnectorGUID,
                                 String                  metadataElementGUID,
                                 CatalogTargetProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "addCatalogTarget";
        final String propertiesParameterName = "properties";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String metadataElementGUIDParameter = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, metadataElementGUIDParameter, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        InstanceProperties instanceProperties = new InstanceProperties();

        repositoryHelper.addStringPropertyToInstance(serviceName,
                                                     instanceProperties,
                                                     OpenMetadataType.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                     properties.getCatalogTargetName(),
                                                     methodName);

        integrationGroupHandler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     integrationConnectorGUID,
                                                     integrationConnectorGUIDParameter,
                                                     OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                     metadataElementGUID,
                                                     metadataElementGUIDParameter,
                                                     OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                     false,
                                                     false,
                                                     integrationGroupHandler.getSupportedZones(),
                                                     OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_GUID,
                                                     OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                     instanceProperties,
                                                     null,
                                                     null,
                                                     new Date(),
                                                     methodName);
    }



    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public CatalogTarget getCatalogTarget(String userId,
                                          String integrationConnectorGUID,
                                          String metadataElementGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "getCatalogTarget";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                             integrationConnectorGUID,
                                                                                             OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_GUID,
                                                                                             metadataElementGUID,
                                                                                             OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_GUID,
                                                                                             OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                                                             2,
                                                                                             false,
                                                                                             false,
                                                                                             null,
                                                                                             methodName);


        if (relationships != null)
        {
            if (!relationships.isEmpty())
            {
                return catalogTargetConverter.getNewBean(CatalogTarget.class, relationships.get(0), methodName);
            }
        }

        return null;
    }



    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public List<CatalogTarget> getCatalogTargets(String  userId,
                                                 String  integrationConnectorGUID,
                                                 int     startingFrom,
                                                 int     maximumResults) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "getCatalogTargets";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        List<Relationship> relationships = integrationGroupHandler.getAttachmentLinks(userId,
                                                                                      integrationConnectorGUID,
                                                                                      integrationConnectorGUIDParameter,
                                                                                      OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                                      OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_GUID,
                                                                                      OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                                                      null,
                                                                                      OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                      2,
                                                                                      false,
                                                                                      false,
                                                                                      startingFrom,
                                                                                      maximumResults,
                                                                                      new Date(),
                                                                                      methodName);

        if (relationships != null)
        {
            List<CatalogTarget> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                results.add(catalogTargetConverter.getNewBean(CatalogTarget.class, relationship, methodName));
            }

            return results;
        }

        return null;
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param metadataElementGUID unique identifier of the metadata element.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing/updating the integration connector definition.
     */
    public void removeCatalogTarget(String userId,
                                    String integrationConnectorGUID,
                                    String metadataElementGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "removeCatalogTarget";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String metadataElementGUIDParameter = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, metadataElementGUIDParameter, methodName);

        integrationGroupHandler.unlinkElementFromElement(userId,
                                                         false,
                                                         null,
                                                         null,
                                                         integrationConnectorGUID,
                                                         integrationConnectorGUIDParameter,
                                                         OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME,
                                                         metadataElementGUID,
                                                         metadataElementGUIDParameter,
                                                         OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                                         OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                         false,
                                                         false,
                                                         OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_GUID,
                                                         OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                         new Date(),
                                                         methodName);
    }
}
