/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworkservices.gaf.converters.CatalogTargetConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.IntegrationConnectorConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.IntegrationGroupConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.RegisteredIntegrationConnectorConverter;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
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
public class IntegrationGroupConfigurationHandler
{
    private final String                                             serviceName;
    private final String                                             serverName;
    private final RepositoryHandler                                  repositoryHandler;
    private final OMRSRepositoryHelper                               repositoryHelper;
    private final SoftwareCapabilityHandler<IntegrationGroupElement> integrationGroupHandler;
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
    public IntegrationGroupConfigurationHandler(String                             serviceName,
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


    /*
     * Support for integration groups
     */

    /**
     * Create a new integration group definition.
     *
     * @param userId identifier of calling user
     * @param properties properties for the integration group.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return unique identifier (guid) of the integration group definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    @SuppressWarnings(value = "unused")
    public  String  createIntegrationGroup(String                     userId,
                                           IntegrationGroupProperties properties,
                                           List<String>               serviceSupportedZones) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "createIntegrationGroup";

        return integrationGroupHandler.createSoftwareCapability(userId,
                                                                null,
                                                                null,
                                                                OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                                null,
                                                                properties.getQualifiedName(),
                                                                properties.getDisplayName(),
                                                                properties.getDescription(),
                                                                DeployedImplementationType.INTEGRATION_GROUP.getDeployedImplementationType(),
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
     * @param serviceSupportedZones supported zones for calling service
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public IntegrationGroupElement getIntegrationGroupByGUID(String       userId,
                                                             String       guid,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final  String   methodName = "getIntegrationGroupByGUID";
        final  String   guidParameter = "guid";

        return integrationGroupHandler.getBeanFromRepository(userId,
                                                             guid,
                                                             guidParameter,
                                                             OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                             false,
                                                             false,
                                                             serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public IntegrationGroupElement getIntegrationGroupByName(String       userId,
                                                             String       name,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                      OpenMetadataType.INTEGRATION_GROUP.typeGUID,
                                                      OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                      specificMatchPropertyNames,
                                                      false,
                                                      false,
                                                      serviceSupportedZones,
                                                      null,
                                                      methodName);
    }


    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param serviceSupportedZones supported zones for calling service
     * @return list of integration group definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definitions.
     */
    public  List<IntegrationGroupElement> getAllIntegrationGroups(String       userId,
                                                                  int          startingFrom,
                                                                  int          maximumResults,
                                                                  List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "getAllIntegrationGroups";

        return integrationGroupHandler.getBeansByType(userId,
                                                      OpenMetadataType.INTEGRATION_GROUP.typeGUID,
                                                      OpenMetadataType.INTEGRATION_GROUP.typeName,
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
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties properties for the integration group.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    public void updateIntegrationGroup(String                     userId,
                                       String                     guid,
                                       boolean                    isMergeUpdate,
                                       IntegrationGroupProperties properties,
                                       List<String>               serviceSupportedZones) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "updateIntegrationGroup";
        final String guidParameter = "guid";
        final String qualifiedNameParameter = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);
        }

        SoftwareCapabilityBuilder builder = new SoftwareCapabilityBuilder(properties.getQualifiedName(),
                                                                          properties.getDisplayName(),
                                                                          properties.getDescription(),
                                                                          DeployedImplementationType.INTEGRATION_GROUP.getDeployedImplementationType(),
                                                                          properties.getVersion(),
                                                                          properties.getPatchLevel(),
                                                                          properties.getSource(),
                                                                          properties.getAdditionalProperties(),
                                                                          OpenMetadataType.INTEGRATION_GROUP.typeGUID,
                                                                          OpenMetadataType.INTEGRATION_GROUP.typeName,
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
                                                       OpenMetadataType.INTEGRATION_GROUP.typeGUID,
                                                       OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                       false,
                                                       false,
                                                       serviceSupportedZones,
                                                       instanceProperties,
                                                       isMergeUpdate,
                                                       new Date(),
                                                       methodName);
    }


    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param qualifiedName unique name for the integration group.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public void deleteIntegrationGroup(String       userId,
                                       String       guid,
                                       String       qualifiedName,
                                       List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                       OpenMetadataType.INTEGRATION_GROUP.typeGUID,
                                                       OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                       qualifiedNameParameter,
                                                       qualifiedName,
                                                       false,
                                                       false,
                                                       serviceSupportedZones,
                                                       new Date(),
                                                       methodName);
    }



    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param userId identifier of calling user
     * @param qualifiedName  unique name for the integration connector.
     * @param name   display name for the integration connector.
     * @param versionIdentifier identifier if the version
     * @param description  description of the analysis provided by the integration connector
     * @param deployedImplementationType technology type
     * @param usesBlockingCalls the connector issues blocking calls and needs a dedicated thread.
     * @param additionalProperties additional properties
     * @param connection   connection to instantiate the integration connector implementation.
     * @param serviceSupportedZones supported zones for calling service
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
                                             String              name,
                                             String              description,
                                             String              deployedImplementationType,
                                             boolean             usesBlockingCalls,
                                             Map<String, String> additionalProperties,
                                             Connection          connection,
                                             List<String>        serviceSupportedZones) throws InvalidParameterException,
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
                                                                               name,
                                                                               versionIdentifier,
                                                                               description,
                                                                               deployedImplementationType,
                                                                               additionalProperties,
                                                                               OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
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
                                             OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                             qualifiedName,
                                             connection,
                                             "Connection to create integration connector",
                                             false,
                                             false,
                                             serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return properties of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public  IntegrationConnectorElement getIntegrationConnectorByGUID(String       userId,
                                                                      String       guid,
                                                                      List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final  String   methodName = "getIntegrationConnectorByGUID";
        final  String   guidParameter = "guid";

        return integrationConnectorHandler.getAssetWithConnection(userId,
                                                                  guid,
                                                                  guidParameter,
                                                                  OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                  false,
                                                                  false,
                                                                  serviceSupportedZones,
                                                                  new Date(),
                                                                  methodName);
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return properties from the integration connector definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public  IntegrationConnectorElement getIntegrationConnectorByName(String       userId,
                                                                      String       name,
                                                                      List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final String methodName    = "getIntegrationConnectorByName";
        final String nameParameter = "name";

        return integrationConnectorHandler.getAssetByNameWithConnection(userId,
                                                                        name,
                                                                        nameParameter,
                                                                        OpenMetadataType.INTEGRATION_CONNECTOR.typeGUID,
                                                                        OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                        false,
                                                                        false,
                                                                        serviceSupportedZones,
                                                                        new Date(),
                                                                        methodName);
    }


    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of integration connector definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definitions.
     */
    public  List<IntegrationConnectorElement> getAllIntegrationConnectors(String       userId,
                                                                          int          startingFrom,
                                                                          int          maximumResults,
                                                                          List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        final  String   methodName = "getAllIntegrationConnectors";

        return integrationConnectorHandler.getAllAssetsWithConnection(userId,
                                                                      OpenMetadataType.INTEGRATION_CONNECTOR.typeGUID,
                                                                      OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                      startingFrom,
                                                                      maximumResults,
                                                                      false,
                                                                      false,
                                                                      serviceSupportedZones,
                                                                      new Date(),
                                                                      methodName);
    }


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param userId identifier of calling user
     * @param integrationConnectorGUID integration connector to search for.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of integration group unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public List<String> getIntegrationConnectorRegistrations(String       userId,
                                                             String       integrationConnectorGUID,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                  OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                  null,
                                                  null,
                                                  false,
                                                  false,
                                                  serviceSupportedZones,
                                                  effectiveTime,
                                                  methodName);

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     integrationConnectorGUID,
                                                                                     OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
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
     * @param name new value for the display name.
     * @param description new value for the description
     * @param deployedImplementationType technology type
     * @param connection connection used to create an instance of this integration connector.
     * @param additionalProperties additional properties for the integration group.
     * @param extendedProperties properties to populate the subtype of the integration connector.
     * @param serviceSupportedZones supported zones for calling service
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
                                           String              name,
                                           String              description,
                                           String              deployedImplementationType,
                                           Connection          connection,
                                           Map<String, String> additionalProperties,
                                           Map<String, Object> extendedProperties,
                                           List<String>        serviceSupportedZones) throws InvalidParameterException,
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
                                                              name,
                                                              versionIdentifier,
                                                              description,
                                                              deployedImplementationType,
                                                              additionalProperties,
                                                              OpenMetadataType.INTEGRATION_CONNECTOR.typeGUID,
                                                              OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                              extendedProperties,
                                                              null,
                                                              null,
                                                              isMergeUpdate,
                                                              null,
                                                              connection,
                                                              false,
                                                              false,
                                                              serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public void deleteIntegrationConnector(String       userId,
                                           String       guid,
                                           String       qualifiedName,
                                           List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                 OpenMetadataType.INTEGRATION_CONNECTOR.typeGUID,
                                                 OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                 qualifiedNameParameter,
                                                 qualifiedName,
                                                 false,
                                                 false,
                                                 serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
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
                                                      PermittedSynchronization permittedSynchronization,
                                                      List<String>             serviceSupportedZones) throws InvalidParameterException,
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
                                                                        OpenMetadataProperty.START_DATE.name,
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
                                                                                OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                                OpenMetadataType.PERMITTED_SYNC_ENUM_TYPE_GUID,
                                                                                OpenMetadataType.PERMITTED_SYNC_ENUM_TYPE_NAME,
                                                                                permittedSynchronization.getOrdinal(),
                                                                                methodName);
            }
            catch (TypeErrorException error)
            {
                throw new InvalidParameterException(error, OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name);
            }
        }

        integrationGroupHandler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     integrationGroupGUID,
                                                     integrationGroupGUIDParameter,
                                                     OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                     integrationConnectorGUID,
                                                     integrationConnectorGUIDParameter,
                                                     OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                     false,
                                                     false,
                                                     serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return details of the integration connector and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String       userId,
                                                                                   String       integrationGroupGUID,
                                                                                   String       integrationConnectorGUID,
                                                                                   List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                                                             OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
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
                return registeredIntegrationConnectorConverter.getBean(this.getIntegrationConnectorByGUID(userId,
                                                                                                          integrationConnectorGUID,
                                                                                                          serviceSupportedZones),
                                                                       relationships.get(0));
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public List<RegisteredIntegrationConnectorElement> getRegisteredIntegrationConnectors(String       userId,
                                                                                          String       integrationGroupGUID,
                                                                                          int          startingFrom,
                                                                                          int          maximumResults,
                                                                                          List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     PropertyServerException
    {
        final String methodName = "getRegisteredIntegrationConnectors";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";

        List<Relationship> relationships = integrationGroupHandler.getAttachmentLinks(userId,
                                                                                      integrationGroupGUID,
                                                                                      integrationGroupGUIDParameter,
                                                                                      OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                                                      OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID,
                                                                                      OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                                      null,
                                                                                      OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
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
                IntegrationConnectorElement connectorElement = this.getIntegrationConnectorByGUID(userId,
                                                                                                  relationship.getEntityTwoProxy().getGUID(),
                                                                                                  serviceSupportedZones);

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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public void unregisterIntegrationConnectorFromGroup(String       userId,
                                                        String       integrationGroupGUID,
                                                        String       integrationConnectorGUID,
                                                        List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                         OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                         integrationConnectorGUID,
                                                         integrationConnectorGUIDParameter,
                                                         OpenMetadataType.INTEGRATION_CONNECTOR.typeGUID,
                                                         OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                         false,
                                                         false,
                                                         serviceSupportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    public String addCatalogTarget(String                  userId,
                                   String                  integrationConnectorGUID,
                                   String                  metadataElementGUID,
                                   CatalogTargetProperties properties,
                                   List<String>            serviceSupportedZones) throws InvalidParameterException,
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

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                                          properties.getCatalogTargetName(),
                                                                          methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.CONNECTION_NAME_PROPERTY_NAME,
                                                                          properties.getConnectionName(),
                                                                          methodName);

        instanceProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                             instanceProperties,
                                                                             OpenMetadataType.TEMPLATES_PROPERTY_NAME,
                                                                             properties.getTemplateProperties(),
                                                                             methodName);

        instanceProperties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                       instanceProperties,
                                                                       OpenMetadataType.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                                       properties.getConfigurationProperties(),
                                                                       methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME,
                                                                          properties.getMetadataSourceQualifiedName(),
                                                                          methodName);

        if (properties.getPermittedSynchronization() != null)
        {
            try
            {
                instanceProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                instanceProperties,
                                                                                OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                                PermittedSynchronization.getOpenTypeGUID(),
                                                                                PermittedSynchronization.getOpenTypeName(),
                                                                                properties.getPermittedSynchronization().getOrdinal(),
                                                                                methodName);
            }
            catch (TypeErrorException error)
            {
                throw new InvalidParameterException(error, OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name);
            }
        }


        if (properties.getDeleteMethod() != null)
        {
            try
            {
                instanceProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                instanceProperties,
                                                                                OpenMetadataProperty.DELETE_METHOD.name,
                                                                                DeleteMethod.getOpenTypeGUID(),
                                                                                DeleteMethod.getOpenTypeName(),
                                                                                properties.getDeleteMethod().getOrdinal(),
                                                                                methodName);
            }
            catch (TypeErrorException error)
            {
                throw new InvalidParameterException(error, OpenMetadataProperty.DELETE_METHOD.name);
            }
        }

        return integrationGroupHandler.multiLinkElementToElement(userId,
                                                                 null,
                                                                 null,
                                                                 integrationConnectorGUID,
                                                                 integrationConnectorGUIDParameter,
                                                                 OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                 metadataElementGUID,
                                                                 metadataElementGUIDParameter,
                                                                 OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                 false,
                                                                 false,
                                                                 serviceSupportedZones,
                                                                 OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_GUID,
                                                                 OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                                 instanceProperties,
                                                                 new Date(),
                                                                 methodName);
    }



    /**
     * Update a catalog target for an integration connector.
     *
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     * @param properties properties for the relationship.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    @SuppressWarnings(value = "unused")
    public void updateCatalogTarget(String                  userId,
                                    String                  relationshipGUID,
                                    CatalogTargetProperties properties,
                                    List<String>            serviceSupportedZones) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "addCatalogTarget";
        final String propertiesParameterName = "properties";
        final String guidParameter = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        InstanceProperties instanceProperties = new InstanceProperties();

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                                          properties.getCatalogTargetName(),
                                                                          methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.CONNECTION_NAME_PROPERTY_NAME,
                                                                          properties.getConnectionName(),
                                                                          methodName);

        instanceProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                             instanceProperties,
                                                                             OpenMetadataType.TEMPLATES_PROPERTY_NAME,
                                                                             properties.getTemplateProperties(),
                                                                             methodName);

        instanceProperties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                       instanceProperties,
                                                                       OpenMetadataType.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                                       properties.getConfigurationProperties(),
                                                                       methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataType.METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME,
                                                                          properties.getMetadataSourceQualifiedName(),
                                                                          methodName);

        if (properties.getPermittedSynchronization() != null)
        {
            try
            {
                instanceProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                instanceProperties,
                                                                                OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                                PermittedSynchronization.getOpenTypeGUID(),
                                                                                PermittedSynchronization.getOpenTypeName(),
                                                                                properties.getPermittedSynchronization().getOrdinal(),
                                                                                methodName);
            }
            catch (TypeErrorException error)
            {
                throw new InvalidParameterException(error, OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name);
            }
        }


        if (properties.getDeleteMethod() != null)
        {
            try
            {
                instanceProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                instanceProperties,
                                                                                OpenMetadataProperty.DELETE_METHOD.name,
                                                                                DeleteMethod.getOpenTypeGUID(),
                                                                                DeleteMethod.getOpenTypeName(),
                                                                                properties.getDeleteMethod().getOrdinal(),
                                                                                methodName);
            }
            catch (TypeErrorException error)
            {
                throw new InvalidParameterException(error, OpenMetadataProperty.DELETE_METHOD.name);
            }
        }

        integrationGroupHandler.updateRelationshipProperties(userId,
                                                             null,
                                                             null,
                                                             relationshipGUID,
                                                             guidParameter,
                                                             OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                             true,
                                                             instanceProperties,
                                                             false,
                                                             false,
                                                             new Date(),
                                                             methodName);
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    @SuppressWarnings(value = "unused")
    public CatalogTarget getCatalogTarget(String       userId,
                                          String       relationshipGUID,
                                          List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getCatalogTarget";
        final String guidParameter = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameter, methodName);

        Relationship relationship = integrationGroupHandler.getAttachmentLink(userId,
                                                                              relationshipGUID,
                                                                              guidParameter,
                                                                              OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                                              new Date(),
                                                                              methodName);


        if (relationship != null)
        {
            return catalogTargetConverter.getNewBean(CatalogTarget.class, relationship, methodName);
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
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of unique identifiers
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public List<CatalogTarget> getCatalogTargets(String       userId,
                                                 String       integrationConnectorGUID,
                                                 int          startingFrom,
                                                 int          maximumResults,
                                                 List<String> serviceSupportedZones) throws InvalidParameterException,
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
                                                                                      OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                                      OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_GUID,
                                                                                      OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                                                      null,
                                                                                      OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                      2,
                                                                                      false,
                                                                                      false,
                                                                                      serviceSupportedZones,
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
     * @param relationshipGUID unique identifier of the relationship.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing/updating the integration connector definition.
     */
    @SuppressWarnings(value = "unused")
    public void removeCatalogTarget(String       userId,
                                    String       relationshipGUID,
                                    List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "removeCatalogTarget";
        final String parameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, parameterName, methodName);

        integrationGroupHandler.deleteRelationship(userId,
                                                   null,
                                                   null,
                                                   relationshipGUID,
                                                   parameterName,
                                                   OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
    }
}
