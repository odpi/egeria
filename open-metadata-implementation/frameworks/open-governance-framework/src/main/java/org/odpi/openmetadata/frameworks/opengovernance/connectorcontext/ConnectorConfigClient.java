/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opengovernance.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.opengovernance.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.IntegrationGroupProperties;

import java.util.List;
import java.util.Map;

/**
 * Provides the methods to create, query and maintain the open metadata that controls running connectors.
 */
public class ConnectorConfigClient extends ConnectorContextClientBase
{
    private final GovernanceConfiguration governanceConfiguration;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param governanceConfigurationClient client to access open metadata
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ConnectorConfigClient(ConnectorContextBase    parentContext,
                                 String                  localServerName,
                                 String                  localServiceName,
                                 String                  connectorUserId,
                                 String                  connectorGUID,
                                 String                  externalSourceGUID,
                                 String                  externalSourceName,
                                 GovernanceConfiguration governanceConfigurationClient,
                                 AuditLog                auditLog,
                                 int                     maxPageSize)
    {
        super(parentContext,
              localServerName,
              localServiceName,
              connectorUserId,
              connectorGUID,
              externalSourceGUID,
              externalSourceName,
              auditLog,
              maxPageSize);

        this.governanceConfiguration = governanceConfigurationClient;
    }


    /**
     * Create a new governance engine definition.
     *
     * @param governanceEngineType type of governance engine to create
     * @param qualifiedName        unique name for the governance engine.
     * @param displayName          display name for messages and user interfaces.
     * @param description          description of the types of governance services that will be associated with
     *                             this governance engine.
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance engine definition.
     */
    public String createGovernanceEngine(String governanceEngineType, String qualifiedName, String displayName, String description) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createGovernanceEngine(connectorUserId, governanceEngineType, qualifiedName, displayName, description);
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param guid unique identifier (guid) of the governance engine definition.
     * @return properties from the governance engine definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceEngineByGUID(connectorUserId, guid);
    }

    /**
     * Return the properties from a governance engine definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceEngineByName(connectorUserId, name);
    }

    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param governanceEngineType type of governance engine to create
     * @param startingFrom         initial position in the stored list.
     * @param maximumResults       maximum number of definitions to return on this call.
     * @return list of governance engine definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definitions.
     */
    public List<GovernanceEngineElement> getAllGovernanceEngines(String governanceEngineType, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllGovernanceEngines(connectorUserId, governanceEngineType, startingFrom, maximumResults);
    }


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid                 unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName        new value for unique name of governance engine.
     * @param displayName          new value for the display name.
     * @param description          new description for the governance engine.
     * @param typeDescription      new description of the type ofg governance engine.
     * @param version              new version number for the governance engine implementation.
     * @param patchLevel           new patch level for the governance engine implementation.
     * @param source               new source description for the implementation of the governance engine.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties   properties to populate the subtype of the governance engine.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance engine definition.
     */
    public void updateGovernanceEngine(String guid, String qualifiedName, String displayName, String description, String typeDescription, String version, String patchLevel, String source, Map<String, String> additionalProperties, Map<String, Object> extendedProperties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateGovernanceEngine(connectorUserId, guid, qualifiedName, displayName, description, typeDescription, version, patchLevel, source, additionalProperties, extendedProperties);
    }


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param guid          unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    public void deleteGovernanceEngine(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteGovernanceEngine(connectorUserId, guid, qualifiedName);
    }


    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param governanceServiceType type of the governance service to create
     * @param qualifiedName         unique name for the governance service.
     * @param displayName           display name for the governance service.
     * @param description           description of the analysis provided by the governance service.
     * @param connection            connection to instantiate the governance service implementation.
     * @return unique identifier of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance service definition.
     */
    public String createGovernanceService(String governanceServiceType, String qualifiedName, String displayName, String description, Connection connection) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createGovernanceService(connectorUserId, governanceServiceType, qualifiedName, displayName, description, connection);
    }

    /**
     * Return the properties from a governance service definition.
     *
     * @param guid unique identifier (guid) of the governance service definition.
     * @return properties of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service definition.
     */
    public GovernanceServiceElement getGovernanceServiceByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceServiceByGUID(connectorUserId, guid);
    }

    /**
     * Return the properties from a governance service definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance engine definition.
     */
    public GovernanceServiceElement getGovernanceServiceByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceServiceByName(connectorUserId, name);
    }

    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param startingFrom   initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of governance service definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service definitions.
     */
    public List<GovernanceServiceElement> getAllGovernanceServices(int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllGovernanceServices(connectorUserId, startingFrom, maximumResults);
    }

    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param governanceServiceGUID governance service to search for.
     * @return list of governance engine unique identifiers (guids)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    public List<String> getGovernanceServiceRegistrations(String governanceServiceGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getGovernanceServiceRegistrations(connectorUserId, governanceServiceGUID);
    }

    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid                 unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName        new value for unique name of governance service.
     * @param displayName          new value for the display name.
     * @param description          new value for the description.
     * @param connection           connection used to create an instance of this governance service.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties   properties to populate the subtype of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the governance service definition.
     */
    public void updateGovernanceService(String guid, String qualifiedName, String displayName, String description, Connection connection, Map<String, String> additionalProperties, Map<String, Object> extendedProperties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateGovernanceService(connectorUserId, guid, qualifiedName, displayName, description, connection, additionalProperties, extendedProperties);
    }

    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param guid          unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName unique name for the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service definition.
     */
    public void deleteGovernanceService(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteGovernanceService(connectorUserId, guid, qualifiedName);
    }

    /**
     * Register a governance service with a specific governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param requestType           request type that this governance service is able to process.
     * @param requestParameters     list of parameters that are passed to the governance service (via
     *                              the context).  These values can be overridden on the actual governance service request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    public void registerGovernanceServiceWithEngine(String governanceEngineGUID, String governanceServiceGUID, String requestType, Map<String, String> requestParameters) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.registerGovernanceServiceWithEngine(connectorUserId, governanceEngineGUID, governanceServiceGUID, requestType, requestParameters);
    }

    /**
     * Register a governance service with a specific governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType governance request type used by caller.
     * @param serviceRequestType    mapped governance request type that this governance service is able to process.
     * @param requestParameters     list of parameters that are passed to the governance service (via
     *                              the governance context).  These values can be overridden on the actual governance request.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    public void registerGovernanceServiceWithEngine(String governanceEngineGUID, String governanceServiceGUID, String governanceRequestType, String serviceRequestType, Map<String, String> requestParameters) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.registerGovernanceServiceWithEngine(connectorUserId, governanceEngineGUID, governanceServiceGUID, governanceRequestType, serviceRequestType, requestParameters);
    }

    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @return details of the governance service and the asset types it is registered for.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService(String governanceEngineGUID, String governanceServiceGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredGovernanceService(connectorUserId, governanceEngineGUID, governanceServiceGUID);
    }

    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom         initial position in the stored list.
     * @param maximumResults       maximum number of definitions to return on this call.
     * @return list of unique identifiers
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    public List<RegisteredGovernanceServiceElement> getRegisteredGovernanceServices(String governanceEngineGUID, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredGovernanceServices(connectorUserId, governanceEngineGUID, startingFrom, maximumResults);
    }

    /**
     * Unregister a governance service from the governance engine.
     *
     * @param governanceEngineGUID  unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the governance service and/or governance engine definitions.
     */
    public void unregisterGovernanceServiceFromEngine(String governanceEngineGUID, String governanceServiceGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.unregisterGovernanceServiceFromEngine(connectorUserId, governanceEngineGUID, governanceServiceGUID);
    }

    /**
     * Create a new integration group definition.
     *
     * @param properties values that will be associated with this integration group.
     * @return unique identifier (guid) of the integration group definition.  This is for use on other requests.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the integration group definition.
     */
    public String createIntegrationGroup(IntegrationGroupProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createIntegrationGroup(connectorUserId, properties);
    }

    /**
     * Return the properties from an integration group definition.
     *
     * @param guid unique identifier (guid) of the integration group definition.
     * @return properties from the integration group definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    public IntegrationGroupElement getIntegrationGroupByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationGroupByGUID(connectorUserId, guid);
    }

    /**
     * Return the properties from an integration group definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    public IntegrationGroupElement getIntegrationGroupByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationGroupByName(connectorUserId, name);
    }

    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param startingFrom   initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of integration group definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definitions.
     */
    public List<IntegrationGroupElement> getAllIntegrationGroups(int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllIntegrationGroups(connectorUserId, startingFrom, maximumResults);
    }

    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid          unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties    values that will be associated with this integration group.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the integration group definition.
     */
    public void updateIntegrationGroup(String guid, boolean isMergeUpdate, IntegrationGroupProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateIntegrationGroup(connectorUserId, guid, isMergeUpdate, properties);
    }

    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param guid          unique identifier of the integration group - used to locate the definition.
     * @param qualifiedName unique name for the integration group.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    public void deleteIntegrationGroup(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteIntegrationGroup(connectorUserId, guid, qualifiedName);
    }

    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param properties values that will be associated with this integration connector - including the connection.
     * @return unique identifier of the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the integration connector definition.
     */
    public String createIntegrationConnector(IntegrationConnectorProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.createIntegrationConnector(connectorUserId, properties);
    }

    /**
     * Return the properties from an integration connector definition.
     *
     * @param guid unique identifier (guid) of the integration connector definition.
     * @return properties of the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    public IntegrationConnectorElement getIntegrationConnectorByGUID(String guid) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationConnectorByGUID(connectorUserId, guid);
    }

    /**
     * Return the properties from an integration connector definition.
     *
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration group definition.
     */
    public IntegrationConnectorElement getIntegrationConnectorByName(String name) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationConnectorByName(connectorUserId, name);
    }

    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param startingFrom   initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of integration connector definitions.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definitions.
     */
    public List<IntegrationConnectorElement> getAllIntegrationConnectors(int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getAllIntegrationConnectors(connectorUserId, startingFrom, maximumResults);
    }

    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param integrationConnectorGUID integration connector to search for.
     * @return list of integration group unique identifiers (guids)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    public List<String> getIntegrationConnectorRegistrations(String integrationConnectorGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getIntegrationConnectorRegistrations(connectorUserId, integrationConnectorGUID);
    }

    /**
     * Update the properties of an existing integration connector definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param guid          unique identifier of the integration connector - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties    values that will be associated with this integration connector - including the connection.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    public void updateIntegrationConnector(String guid, boolean isMergeUpdate, IntegrationConnectorProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateIntegrationConnector(connectorUserId, guid, isMergeUpdate, properties);
    }

    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param guid          unique identifier of the integration connector - used to locate the definition.
     * @param qualifiedName unique name for the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    public void deleteIntegrationConnector(String guid, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.deleteIntegrationConnector(connectorUserId, guid, qualifiedName);
    }

    /**
     * Register an integration connector with a specific integration group.
     *
     * @param integrationGroupGUID     unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param properties               list of parameters that are used to control to the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    public void registerIntegrationConnectorWithGroup(String integrationGroupGUID, String integrationConnectorGUID, RegisteredIntegrationConnectorProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.registerIntegrationConnectorWithGroup(connectorUserId, integrationGroupGUID, integrationConnectorGUID, properties);
    }

    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param integrationGroupGUID     unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @return details of the integration connector and the asset types it is registered for.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    public RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String integrationGroupGUID, String integrationConnectorGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredIntegrationConnector(connectorUserId, integrationGroupGUID, integrationConnectorGUID);
    }

    /**
     * Retrieve the identifiers of the integration connectors registered with an integration group.
     *
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom         initial position in the stored list.
     * @param maximumResults       maximum number of definitions to return on this call.
     * @return list of unique identifiers
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    public List<RegisteredIntegrationConnectorElement> getRegisteredIntegrationConnectors(String integrationGroupGUID, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getRegisteredIntegrationConnectors(connectorUserId, integrationGroupGUID, startingFrom, maximumResults);
    }

    /**
     * Unregister an integration connector from the integration group.
     *
     * @param integrationGroupGUID     unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector and/or integration group definitions.
     */
    public void unregisterIntegrationConnectorFromGroup(String integrationGroupGUID, String integrationConnectorGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.unregisterIntegrationConnectorFromGroup(connectorUserId, integrationGroupGUID, integrationConnectorGUID);
    }

    /**
     * Add a catalog target to an integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID      unique identifier of the metadata element that is a catalog target.
     * @param properties               properties for the relationship.
     * @return catalog target GUID
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the catalog target definition.
     */
    public String addCatalogTarget(String integrationConnectorGUID, String metadataElementGUID, CatalogTargetProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.addCatalogTarget(connectorUserId, integrationConnectorGUID, metadataElementGUID, properties);
    }

    /**
     * Update a catalog target for an integration connector.
     *
     * @param catalogTargetGUID unique identifier of the relationship.
     * @param properties        properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem storing the catalog target definition.
     */
    public void updateCatalogTarget(String catalogTargetGUID, CatalogTargetProperties properties) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.updateCatalogTarget(connectorUserId, catalogTargetGUID, properties);
    }

    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param relationshipGUID unique identifier of the catalog target.
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    public CatalogTarget getCatalogTarget(String relationshipGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getCatalogTarget(connectorUserId, relationshipGUID);
    }

    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom             initial position in the stored list.
     * @param maximumResults           maximum number of definitions to return on this call.
     * @return list of unique identifiers
     * @throws InvalidParameterException  one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the integration connector definition.
     */
    public List<CatalogTarget> getCatalogTargets(String integrationConnectorGUID, int startingFrom, int maximumResults) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return governanceConfiguration.getCatalogTargets(connectorUserId, integrationConnectorGUID, startingFrom, maximumResults);
    }

    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param relationshipGUID unique identifier of the catalog target relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem accessing/updating the integration connector definition.
     */
    public void removeCatalogTarget(String relationshipGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        governanceConfiguration.removeCatalogTarget(connectorUserId, relationshipGUID);
    }

}
