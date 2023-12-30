/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataTypesMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GovernanceArchiveHelper creates elements for governance.  This includes governance program definitions, governance engine definitions and
 * governance action process definitions.
 */
public class GovernanceArchiveHelper extends SimpleCatalogArchiveHelper
{
    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    public GovernanceArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                   String                     archiveGUID,
                                   String                     archiveRootName,
                                   String                     originatorName,
                                   Date                       creationDate,
                                   long                       versionNumber,
                                   String                     versionName)
    {
        super(archiveBuilder, archiveGUID, archiveRootName, archiveRootName, originatorName, creationDate, versionNumber, versionName);
    }



    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    public GovernanceArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                   String                     archiveGUID,
                                   String                     archiveName,
                                   String                     archiveRootName,
                                   String                     originatorName,
                                   Date                       creationDate,
                                   long                       versionNumber,
                                   String                     versionName)
    {
        super(archiveBuilder, archiveGUID, archiveName, archiveRootName, originatorName, creationDate, versionNumber, versionName);
    }



    /**
     * Constructor passes parameters used to build the open metadata archive's property header.
     * This version is used for multiple dependant archives, and they need to share the guid map.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveRootName non-spaced root name of the open metadata archive elements.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param guidMapFileName name of the guid map file.
     */
    public GovernanceArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                   String                     archiveGUID,
                                   String                     archiveRootName,
                                   String                     originatorName,
                                   Date                       creationDate,
                                   long                       versionNumber,
                                   String                     versionName,
                                   String                     guidMapFileName)
    {
        super(archiveBuilder, archiveGUID, archiveRootName, originatorName, creationDate, versionNumber, versionName, guidMapFileName);
    }



    /**
     * Create a governance service entity.
     *
     * @param typeName name of governance service subtype to use - default is GovernanceService
     * @param connectorProviderName name of the connector provider for the governance service
     * @param configurationProperties configuration properties for the governance service (goes in the connection)
     * @param qualifiedName unique name for the governance service
     * @param displayName display name for the governance service
     * @param description description about the governance service
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return id for the governance service
     */
    public String addGovernanceService(String              typeName,
                                       String              connectorProviderName,
                                       Map<String, Object> configurationProperties,
                                       String              qualifiedName,
                                       String              displayName,
                                       String              description,
                                       Map<String, String> additionalProperties,
                                       Map<String, Object> extendedProperties)
    {
        String serviceTypeName = OpenMetadataTypesMapper.GOVERNANCE_SERVICE_TYPE_NAME;

        if (typeName != null)
        {
            serviceTypeName = typeName;
        }

        try
        {
            Class<?>   connectorProviderClass = Class.forName(connectorProviderName);
            Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

            ConnectorProviderBase serviceProvider = (ConnectorProviderBase)potentialConnectorProvider;
            ConnectorType         connectorType   = serviceProvider.getConnectorType();

            String connectorTypeGUID = super.addConnectorType(null,
                                                              connectorType.getGUID(),
                                                              connectorType.getQualifiedName(),
                                                              connectorType.getDisplayName(),
                                                              connectorType.getDescription(),
                                                              connectorType.getDeployedImplementationType(),
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
                                                              connectorType.getRecognizedSecuredProperties(),
                                                              connectorType.getRecognizedConfigurationProperties(),
                                                              connectorType.getRecognizedAdditionalProperties(),
                                                              connectorType.getAdditionalProperties());

            String connectionGUID = super.addConnection(qualifiedName + "_implementation",
                                                       displayName + " Governance Service Provider Implementation",
                                                       "Connection for governance service: " + qualifiedName,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       configurationProperties,
                                                       null,
                                                       connectorTypeGUID,
                                                       null);

            String serviceGUID = super.addAsset(serviceTypeName, qualifiedName, displayName, description, additionalProperties, extendedProperties);

            if ((serviceGUID != null) && (connectionGUID != null))
            {
                super.addConnectionForAsset(serviceGUID, null, connectionGUID);
            }

            return serviceGUID;
        }
        catch (Exception error)
        {
            System.out.println("Invalid connector type " + connectorProviderName + " for governance service.  Exception " + error.getClass().getName() + " with message " + error.getMessage());
        }

        return null;
    }


    /**
     * Create a software capability entity got s governance engine.
     *
     * @param typeName name of software capability subtype to use - default is SoftwareCapability
     * @param qualifiedName unique name for the capability
     * @param displayName display name for the capability
     * @param description description about the capability
     * @param capabilityType type
     * @param capabilityVersion version
     * @param patchLevel patch level
     * @param source source
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     *
     * @return id for the capability
     */
    public String addGovernanceEngine(String              typeName,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              capabilityType,
                                      String              capabilityVersion,
                                      String              patchLevel,
                                      String              source,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties)
    {
        String engineTypeName = OpenMetadataTypesMapper.GOVERNANCE_ENGINE_TYPE_NAME;

        if (typeName != null)
        {
            engineTypeName = typeName;
        }

        return super.addSoftwareCapability(engineTypeName, qualifiedName, displayName, description, capabilityType, capabilityVersion, patchLevel, source, additionalProperties, extendedProperties);
    }


    /**
     * Create the relationship between a governance engine and one of its supported governance services.
     *
     * @param engineGUID unique identifier of the asset
     * @param requestType governance request type to use when calling the governance engine
     * @param serviceRequestType  request type to use when calling the service (if null, governance request type is used)
     * @param requestParameters default request parameters to pass to the service when called with this request type
     * @param serviceGUID unique identifier of the service
     */
    public void addSupportedGovernanceService(String              engineGUID,
                                              String              requestType,
                                              String              serviceRequestType,
                                              Map<String, String> requestParameters,
                                              String              serviceGUID)
    {
        final String methodName = "addSupportedGovernanceService";

        EntityDetail engineEntity = archiveBuilder.getEntity(engineGUID);
        EntityDetail serviceEntity = archiveBuilder.getEntity(serviceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(engineEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(serviceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.REQUEST_TYPE_PROPERTY_NAME, requestType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SERVICE_REQUEST_TYPE_PROPERTY_NAME, serviceRequestType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REQUEST_PARAMETERS_PROPERTY_NAME, requestParameters, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(engineGUID + "_to_" + serviceGUID + "_" + requestType + "_supported_governance_service_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a governance action process.
     *
     * @param typeName name of process subtype to use - default is GovernanceActionProcess
     * @param qualifiedName unique name for the capability
     * @param name display name for the capability
     * @param versionIdentifier identifier of the version for the process implementation
     * @param description description about the capability
     * @param formula logic for the process
     * @param domainIdentifier which governance domain - 0=all
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the new entity
     */
    public String addGovernanceActionProcess(String               typeName,
                                             String               qualifiedName,
                                             String               name,
                                             String               versionIdentifier,
                                             String               description,
                                             String               formula,
                                             int                  domainIdentifier,
                                             Map<String, String>  additionalProperties,
                                             Map<String, Object>  extendedProperties,
                                             List<Classification> classifications)
    {
        String processTypeName = OpenMetadataTypesMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME;

        if (typeName != null)
        {
            processTypeName = typeName;
        }

        Map<String, Object> processExtendedProperties = extendedProperties;

        if (processExtendedProperties == null)
        {
            processExtendedProperties = new HashMap<>();

            processExtendedProperties.put(OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier);
        }

        return super.addProcess(processTypeName,
                                qualifiedName,
                                name,
                                versionIdentifier,
                                description,
                                formula,
                                additionalProperties,
                                extendedProperties,
                                classifications);
    }


    /**
     * Create a governance action type.
     *
     * @param typeName name of subtype to use - default is GovernanceActionType
     * @param qualifiedName unique name for the capability
     * @param displayName display name for the capability
     * @param description description about the capability
     * @param producedGuards guards expected from the implementation
     * @param domainIdentifier which governance domain - 0=all
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the new entity
     */
    public String addGovernanceActionProcessStep(String               typeName,
                                                 String               qualifiedName,
                                                 String               displayName,
                                                 String               description,
                                                 int                  domainIdentifier,
                                                 List<String>         producedGuards,
                                                 Map<String, String>  additionalProperties,
                                                 Map<String, Object>  extendedProperties,
                                                 List<Classification> classifications)
    {
        final String methodName = "addGovernanceActionProcessStep";

        String actionTypeName = OpenMetadataTypesMapper.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME;

        if (typeName != null)
        {
            actionTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PRODUCED_GUARDS_PROPERTY_NAME, producedGuards, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(actionTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create the relationship between a governance action process and the first governance action type to execute.
     *
     * @param governanceActionProcessGUID unique identifier of the governance action process
     * @param guard initial guard for the first step in the process
     * @param governanceActionTypeGUID unique identifier of the implementing governance engine
     */
    public void addGovernanceActionFlow(String governanceActionProcessGUID,
                                        String guard,
                                        String governanceActionTypeGUID)
    {
        final String methodName = "addGovernanceActionFlow";

        EntityDetail processEntity    = archiveBuilder.getEntity(governanceActionProcessGUID);
        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionTypeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(processEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(actionTypeEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.GUARD_PROPERTY_NAME, guard, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.GOVERNANCE_ACTION_FLOW_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(governanceActionProcessGUID + "_to_" + governanceActionTypeGUID + "_governance_action_flow_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create the relationship between a governance action type and one of the next governance action type
     * to execute in a process.
     *
     * @param governanceActionProcessStepGUID unique identifier of the governance action type
     * @param guard guard required to run this next action
     * @param mandatoryGuard guard must occur before the next step can run
     * @param ignoreMultipleTriggers only run this once even if the same guard occurs multiple times while it is waiting
     * @param nextGovernanceActionTypeGUID unique identifier of the implementing governance engine
     */
    public void addNextGovernanceActionProcessStep(String  governanceActionProcessStepGUID,
                                                   String  guard,
                                                   boolean mandatoryGuard,
                                                   boolean ignoreMultipleTriggers,
                                                   String  nextGovernanceActionTypeGUID)
    {
        final String methodName = "addNextGovernanceActionProcessStep";

        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionProcessStepGUID);
        EntityDetail nextActionTypeEntity = archiveBuilder.getEntity(nextGovernanceActionTypeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(actionTypeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(nextActionTypeEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.GUARD_PROPERTY_NAME, guard, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MANDATORY_GUARD_PROPERTY_NAME, mandatoryGuard, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME, ignoreMultipleTriggers, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(governanceActionProcessStepGUID + "_to_" + nextGovernanceActionTypeGUID + "_next_governance_action_type_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a governance action type and the governance engine that supplies its implementation.
     *
     * @param governanceActionProcessStepGUID unique identifier of the governance action type
     * @param requestType governance request type to use when calling the engine
     * @param requestParameters default request parameters to pass to the service when called with this request type
     * @param governanceEngineGUID unique identifier of the implementing governance engine
     */
    public void addGovernanceActionProcessStepExecutor(String              governanceActionProcessStepGUID,
                                                       String              requestType,
                                                       Map<String, String> requestParameters,
                                                       String              governanceEngineGUID)
    {
        final String methodName = "addGovernanceActionTypeExecutor";

        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionProcessStepGUID);
        EntityDetail engineEntity = archiveBuilder.getEntity(governanceEngineGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(actionTypeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(engineEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.REQUEST_TYPE_PROPERTY_NAME, requestType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REQUEST_PARAMETERS_PROPERTY_NAME, requestParameters, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.GOVERNANCE_ACTION_PROCESS_STEP_EXECUTOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(governanceActionProcessStepGUID + "_to_" + governanceEngineGUID + "_governance_action_process_step_executor_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }
}
