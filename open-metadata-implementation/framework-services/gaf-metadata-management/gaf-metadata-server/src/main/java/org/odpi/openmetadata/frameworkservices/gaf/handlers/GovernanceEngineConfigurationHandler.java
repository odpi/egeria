/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceEngineElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceServiceElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredGovernanceServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworkservices.gaf.converters.GovernanceEngineConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.GovernanceServiceConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.RegisteredGovernanceServiceConverter;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;


/**
 * GovernanceConfigurationHandler provides the open metadata server side implementation of
 * GovernanceConfigurationServer which is part of the Open Governance Framework (ODF).
 */
public class GovernanceEngineConfigurationHandler
{
    private final String                                             serviceName;
    private final String                                             serverName;
    private final RepositoryHandler                                  repositoryHandler;
    private final OMRSRepositoryHelper                               repositoryHelper;
    private final SoftwareCapabilityHandler<GovernanceEngineElement> governanceEngineHandler;
    private final AssetHandler<GovernanceServiceElement>             governanceServiceHandler;
    private final ConnectionHandler<Connection>                      connectionHandler;
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
    public GovernanceEngineConfigurationHandler(String                             serviceName,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    @SuppressWarnings(value = "unused")
    public  String  createGovernanceEngine(String       userId,
                                           String       typeName,
                                           String       qualifiedName,
                                           String       displayName,
                                           String       description,
                                           List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                                DeployedImplementationType.GOVERNANCE_ENGINE.getDeployedImplementationType(),
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
     * @param serviceSupportedZones supported zones for calling service
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByGUID(String       userId,
                                                             String       guid,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                             serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByName(String       userId,
                                                             String       name,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                      serviceSupportedZones,
                                                      null,
                                                      methodName);
    }


    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param serviceSupportedZones supported zones for calling service
     * @return list of governance engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definitions.
     */
    public  List<GovernanceEngineElement> getAllGovernanceEngines(String       userId,
                                                                  int          startingFrom,
                                                                  int          maximumResults,
                                                                  List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "getAllGovernanceEngines";

        return governanceEngineHandler.getBeansByType(userId,
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                      null,
                                                      false,
                                                      false,
                                                      serviceSupportedZones,
                                                      startingFrom,
                                                      maximumResults,
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
     * @param serviceSupportedZones supported zones for calling service
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
                                       Map<String, Object>   extendedProperties,
                                       List<String>          serviceSupportedZones) throws InvalidParameterException,
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

        InstanceProperties instanceProperties = builder.getInstanceProperties(methodName);

        governanceEngineHandler.updateBeanInRepository(userId,
                                                       null,
                                                       null,
                                                       guid,
                                                       guidParameter,
                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                       false,
                                                       false,
                                                       serviceSupportedZones,
                                                       instanceProperties,
                                                       false,
                                                       new Date(),
                                                       methodName);
    }


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public void deleteGovernanceEngine(String       userId,
                                       String       guid,
                                       String       qualifiedName,
                                       List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                       serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    public String createGovernanceService(String       userId,
                                          String       typeName,
                                          String       qualifiedName,
                                          String       displayName,
                                          String       description,
                                          Connection   connection,
                                          List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                             serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return properties of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public  GovernanceServiceElement getGovernanceServiceByGUID(String       userId,
                                                                String       guid,
                                                                List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                               serviceSupportedZones,
                                                               new Date(),
                                                               methodName);
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public  GovernanceServiceElement getGovernanceServiceByName(String       userId,
                                                                String       name,
                                                                List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                                     serviceSupportedZones,
                                                                     new Date(),
                                                                     methodName);
    }


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of governance service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definitions.
     */
    public  List<GovernanceServiceElement> getAllGovernanceServices(String       userId,
                                                                    int          startingFrom,
                                                                    int          maximumResults,
                                                                    List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                                   serviceSupportedZones,
                                                                   new Date(),
                                                                   methodName);
    }


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param userId identifier of calling user
     * @param governanceServiceGUID governance service to search for.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of governance engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public List<String> getGovernanceServiceRegistrations(String       userId,
                                                          String       governanceServiceGUID,
                                                          List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
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
                                        Map<String, Object> extendedProperties,
                                        List<String>        serviceSupportedZones) throws InvalidParameterException,
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
                                                           serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public void deleteGovernanceService(String       userId,
                                        String       guid,
                                        String       qualifiedName,
                                        List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                 serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @SuppressWarnings(value = "unused")
    public void registerGovernanceServiceWithEngine(String              userId,
                                                    String              governanceEngineGUID,
                                                    String              governanceServiceGUID,
                                                    String              governanceRequestType,
                                                    String              serviceRequestType,
                                                    Map<String, String> defaultAnalysisParameters,
                                                    List<String>        serviceSupportedZones) throws InvalidParameterException,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return details of the governance service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService(String       userId,
                                                                             String       governanceEngineGUID,
                                                                             String       governanceServiceGUID,
                                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
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

            return converter.getBean(this.getGovernanceServiceByGUID(userId, governanceServiceGUID, serviceSupportedZones), relationships);
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public List<RegisteredGovernanceServiceElement> getRegisteredGovernanceServices(String       userId,
                                                                                    String       governanceEngineGUID,
                                                                                    int          startingFrom,
                                                                                    int          maximumResults,
                                                                                    List<String> serviceSupportedZones) throws InvalidParameterException,
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
                        GovernanceServiceElement newElement = this.getGovernanceServiceByGUID(userId,
                                                                                              end2.getGUID(),
                                                                                              serviceSupportedZones);

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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public void unregisterGovernanceServiceRequestFromEngine(String       userId,
                                                             String       requestType,
                                                             String       governanceEngineGUID,
                                                             String       governanceServiceGUID,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                                     serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public void unregisterGovernanceServiceFromEngine(String       userId,
                                                      String       governanceEngineGUID,
                                                      String       governanceServiceGUID,
                                                      List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                                     serviceSupportedZones,
                                                                     OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                                     supportedGovernanceService,
                                                                     new Date(),
                                                                     methodName);
                }
            }
        }
    }
}
