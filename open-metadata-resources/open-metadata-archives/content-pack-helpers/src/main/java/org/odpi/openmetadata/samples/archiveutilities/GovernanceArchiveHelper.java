/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.Category;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.*;

/**
 * GovernanceArchiveHelper creates elements for governance.  This includes governance program definitions, governance engine definitions and
 * governance action process definitions.
 */
public class GovernanceArchiveHelper extends SimpleCatalogArchiveHelper
{
    /**
     * Constructor passes parameters used to build the open metadata archive's property header.
     * This version is used for multiple dependant archives, and they need to share the guid map.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveRootName non-spaced root name of the open metadata archive elements.
     * @param archiveDescription description of this archive.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param guidMapFileName name of the guid map file.
     */
    public GovernanceArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                   String                     archiveGUID,
                                   String                     archiveRootName,
                                   String                     archiveDescription,
                                   String                     originatorName,
                                   Date                       creationDate,
                                   long                       versionNumber,
                                   String                     versionName,
                                   String                     guidMapFileName)
    {
        super(archiveBuilder, archiveGUID, archiveRootName, archiveDescription, originatorName, creationDate, versionNumber, versionName, guidMapFileName);
    }


    /**
     * Create an integration connector entity.
     *
     * @param connectorProviderName name of the connector provider for the integration connector
     * @param configurationProperties configuration properties for the integration connector (goes in the connection)
     * @param qualifiedName unique name for the integration connector
     * @param displayName display name for the integration connector
     * @param description description about the integration connector
     * @param userId userId to use when calling external services
     * @param secretsCollectionName            name of collection of secrets to use in the secrets store
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     * @param additionalProperties any other properties
     *
     * @return id for the integration connector
     */
    public String addIntegrationConnector(String              connectorProviderName,
                                          Map<String, Object> configurationProperties,
                                          String              qualifiedName,
                                          String              displayName,
                                          String              description,
                                          String              userId,
                                          String              endpointAddress,
                                          String              secretsCollectionName,
                                          String              secretsStorePurpose,
                                          String              secretsStoreConnectorTypeGUID,
                                          String              secretsStoreFileName,
                                          Map<String, String> additionalProperties)
    {
        try
        {
            Class<?>   connectorProviderClass = Class.forName(connectorProviderName);
            Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

            IntegrationConnectorProvider connectorProvider = (IntegrationConnectorProvider)potentialConnectorProvider;
            ConnectorType                connectorType     = connectorProvider.getConnectorType();

            String connectorTypeGUID = super.addConnectorType(connectorType.getGUID(),
                                                              connectorType.getQualifiedName(),
                                                              connectorType.getDisplayName(),
                                                              connectorType.getDescription(),
                                                              connectorType.getSupportedDeployedImplementationType(),
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

            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType());

            String connectorGUID = super.addAsset(DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  displayName,
                                                  description,
                                                  additionalProperties,
                                                  extendedProperties);

            this.addSupportedTemplateTypes(connectorGUID,
                                           DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                           connectorGUID,
                                           DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                           OpenMetadataType.ASSET.typeName,
                                           null,
                                           connectorProvider.getSupportedTemplateTypes());

            this.addSupportedConfigurationProperties(connectorGUID,
                                                     DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                     connectorGUID,
                                                     DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                     OpenMetadataType.ASSET.typeName,
                                                     null,
                                                     connectorProvider.getSupportedConfigurationProperties());

            String endpointGUID = null;
            if (endpointAddress != null)
            {
                endpointGUID = super.addEndpoint(connectorGUID,
                                                 DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                 OpenMetadataType.ASSET.typeName,
                                                 null,
                                                 qualifiedName + "_endpoint",
                                                 displayName + "'s first catalog target",
                                                 "Endpoint for integration connector: " + qualifiedName,
                                                 endpointAddress,
                                                 null,
                                                 null);
            }

            String connectionGUID;

            if (secretsStoreConnectorTypeGUID == null)
            {
                connectionGUID = super.addConnection(OpenMetadataType.CONNECTION.typeName,
                                                     qualifiedName + "_implementation",
                                                     displayName + " Integration Connector Provider Implementation",
                                                     "Connection for integration connector: " + qualifiedName,
                                                     userId,
                                                     null,
                                                     null,
                                                     null,
                                                     configurationProperties,
                                                     null,
                                                     connectorTypeGUID,
                                                     endpointGUID,
                                                     connectorGUID,
                                                     DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                     OpenMetadataType.ASSET.typeName,
                                                     null);
            }
            else
            {
                connectionGUID = super.addConnection(OpenMetadataType.VIRTUAL_CONNECTION.typeName,
                                                     qualifiedName + "_implementation",
                                                     displayName + " Integration Connector Provider Implementation",
                                                     "Connection for integration connector: " + qualifiedName,
                                                     null,
                                                     null,
                                                     null,
                                                     null,
                                                     configurationProperties,
                                                     null,
                                                     connectorTypeGUID,
                                                     endpointGUID,
                                                     connectorGUID,
                                                     DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                     OpenMetadataType.ASSET.typeName,
                                                     null);

                addSecretsConnection(connectionGUID,
                                     qualifiedName,
                                     displayName,
                                     connectorGUID,
                                     DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                     OpenMetadataType.ASSET.typeName,
                                     null,
                                     secretsCollectionName,
                                     secretsStorePurpose,
                                     secretsStoreConnectorTypeGUID,
                                     secretsStoreFileName);
            }

            if ((connectorGUID != null) && (connectionGUID != null))
            {
                super.addConnectionForAsset(connectorGUID, connectionGUID);
            }

            return connectorGUID;
        }
        catch (Exception error)
        {
            System.out.println("Invalid connector type " + connectorProviderName + " for integration connector.  Exception " + error.getClass().getName() + " with message " + error.getMessage());
        }

        return null;
    }



    /**
     * Add a secrets store connection.
     *
     * @param parentConnectionGUID unique identifier of template
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param secretsCollectionName            name of collection of secrets to use in the secrets store
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     */
    public void   addSecretsConnection(String parentConnectionGUID,
                                       String parentQualifiedName,
                                       String parentDisplayName,
                                       String anchorGUID,
                                       String anchorTypeName,
                                       String anchorDomainName,
                                       String anchorScopeGUID,
                                       String secretsCollectionName,
                                       String secretsStorePurpose,
                                       String secretsStoreConnectorTypeGUID,
                                       String secretsStoreFileName)
    {
        Map<String, Object> secretsStoreConfigurationProperties = new HashMap<>();

        secretsStoreConfigurationProperties.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), secretsCollectionName);

        String secretStoreEndpointGUID = this.addEndpoint(anchorGUID,
                                                          anchorTypeName,
                                                          anchorDomainName,
                                                          anchorScopeGUID,
                                                          parentQualifiedName + ":SecretStoreEndpoint",
                                                          parentDisplayName + " secret store endpoint",
                                                          null,
                                                          secretsStoreFileName,
                                                          null,
                                                          null);

        String secretsStoreConnectionGUID = this.addConnection(OpenMetadataType.CONNECTION.typeName,
                                                               parentQualifiedName + ":SecretsStoreConnection",
                                                               parentDisplayName + " secrets store connection",
                                                               null,
                                                               null,
                                                               null,
                                                               null,
                                                               null,
                                                               secretsStoreConfigurationProperties,
                                                               null,
                                                               secretsStoreConnectorTypeGUID,
                                                               secretStoreEndpointGUID,
                                                               anchorGUID,
                                                               anchorTypeName,
                                                               anchorDomainName,
                                                               anchorScopeGUID);

        this.addEmbeddedConnection(parentConnectionGUID,
                                   0,
                                   secretsStorePurpose,
                                   null,
                                   secretsStoreConnectionGUID);
    }


    /**
     * Create a catalog target relationship.
     *
     * @param integrationConnectorGUID the integration connector
     * @param targetElementGUID the target they should work with
     * @param catalogTargetName name for the target
     * @param connectionName name for the connection
     * @param configurationProperties any configuration properties
     * @param templates specific templates to use
     * @param metadataSourceQualifiedName metadata source qualified name for the cataloguing
     */
    public void addCatalogTargetRelationship(String              integrationConnectorGUID,
                                             String              targetElementGUID,
                                             String              catalogTargetName,
                                             String              connectionName,
                                             Map<String, Object> configurationProperties,
                                             Map<String, String> templates,
                                             String              metadataSourceQualifiedName)
    {
        final String methodName = "addCatalogTargetRelationship";

        EntityDetail end1Entity = archiveBuilder.getEntity(integrationConnectorGUID);
        EntityDetail end2Entity = archiveBuilder.getEntity(targetElementGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(end1Entity);
        EntityProxy end2 = archiveHelper.getEntityProxy(end2Entity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.CATALOG_TARGET_NAME.name, catalogTargetName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTION_NAME.name, connectionName, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONFIGURATION_PROPERTIES.name, configurationProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TEMPLATES.name, templates, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.METADATA_SOURCE_QUALIFIED_NAME.name, metadataSourceQualifiedName, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(integrationConnectorGUID + "_to_" + targetElementGUID + "_catalog_target_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a software capability entity for an integration group.
     *
     * @param qualifiedName unique name for the group
     * @param displayName display name for the group
     * @param description description about the group
     * @param capabilityVersion version
     * @param patchLevel patch level
     * @param source source
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     *
     * @return id for the capability
     */
    public String addIntegrationGroup(String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              capabilityVersion,
                                      String              patchLevel,
                                      String              source,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties)
    {
        return super.addSoftwareCapability(DeployedImplementationType.INTEGRATION_GROUP.getAssociatedTypeName(),
                                           qualifiedName,
                                           displayName,
                                           description,
                                           DeployedImplementationType.INTEGRATION_GROUP.getDeployedImplementationType(),
                                           capabilityVersion,
                                           patchLevel,
                                           source,
                                           additionalProperties,
                                           extendedProperties,
                                           (Classification)null,
                                           null,
                                           DeployedImplementationType.INTEGRATION_GROUP.getAssociatedTypeName(),
                                           DeployedImplementationType.SOFTWARE_CAPABILITY.getAssociatedTypeName(),
                                           null);
    }


    /**
     * Create the relationship between a governance engine and one of its supported governance services.
     *
     * @param integrationGroupGUID unique identifier of the integration group
     * @param connectorName name of the connector used in messages and REST calls
     * @param connectorUserId  userId for the connector to use (overrides the server's userId)
     * @param metadataSourceQualifiedName unique name of the metadata collection for anything catalogued by this connector
     * @param refreshTimeInterval how long (in minutes) between each refresh of the connector
     * @param generateIntegrationReport should the integration connector produce integration reports when it refreshes?
     * @param integrationConnectorGUID unique identifier of the integration connector
     */
    public void addRegisteredIntegrationConnector(String              integrationGroupGUID,
                                                  String              connectorName,
                                                  String              connectorUserId,
                                                  String              metadataSourceQualifiedName,
                                                  long                refreshTimeInterval,
                                                  boolean             generateIntegrationReport,
                                                  String              integrationConnectorGUID)
    {
        final String methodName = "addRegisteredIntegrationConnector";

        EntityDetail groupEntity = archiveBuilder.getEntity(integrationGroupGUID);
        EntityDetail connectorEntity = archiveBuilder.getEntity(integrationConnectorGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(groupEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(connectorEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.CONNECTOR_NAME.name, connectorName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTOR_USER_ID.name, connectorUserId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.METADATA_SOURCE_QUALIFIED_NAME.name, metadataSourceQualifiedName, methodName);
        properties = archiveHelper.addLongPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REFRESH_TIME_INTERVAL.name, refreshTimeInterval, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.GENERATE_CONNECTOR_ACTIVITY_REPORT.name, generateIntegrationReport, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(integrationGroupGUID + "_to_" + integrationConnectorGUID + "_" + connectorName + "_registered_integration_connector_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a governance service entity.
     *
     * @param typeName name of governance service subtype to use - default is GovernanceService
     * @param deployedImplementationType deployed implementation type
     * @param connectorProviderName name of the connector provider for the governance service
     * @param configurationProperties configuration properties for the governance service (goes in the connection)
     * @param qualifiedName unique name for the governance service
     * @param displayName display name for the governance service
     * @param description description about the governance service
     * @param additionalProperties any other properties
     *
     * @return id for the governance service
     */
    public String addGovernanceService(String                     typeName,
                                       String                     deployedImplementationType,
                                       String                     connectorProviderName,
                                       Map<String, Object>        configurationProperties,
                                       String                     qualifiedName,
                                       String                     displayName,
                                       String                     description,
                                       Map<String, String>        additionalProperties)
    {
        try
        {
            String assetTypeName = OpenMetadataType.GOVERNANCE_SERVICE.typeName;

            if (typeName != null)
            {
                assetTypeName = typeName;
            }

            Class<?>   connectorProviderClass = Class.forName(connectorProviderName);
            Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

            ConnectorProviderBase serviceProvider = (ConnectorProviderBase)potentialConnectorProvider;
            ConnectorType         connectorType   = serviceProvider.getConnectorType();

            String connectorTypeGUID = super.addConnectorType(connectorType.getGUID(),
                                                              connectorType.getQualifiedName(),
                                                              connectorType.getDisplayName(),
                                                              connectorType.getDescription(),
                                                              connectorType.getSupportedDeployedImplementationType(),
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

            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                   deployedImplementationType);

            String serviceGUID = super.addAsset(assetTypeName,
                                                qualifiedName,
                                                displayName,
                                                description,
                                                additionalProperties,
                                                extendedProperties);

            if (serviceGUID != null)
            {
                this.addSupportedConfigurationProperties(serviceGUID,
                                                         assetTypeName,
                                                         serviceGUID,
                                                         assetTypeName,
                                                         OpenMetadataType.ASSET.typeName,
                                                         null,
                                                         serviceProvider.getSupportedConfigurationProperties());

                this.addSupportedTemplateTypes(serviceGUID,
                                               assetTypeName,
                                               serviceGUID,
                                               assetTypeName,
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               serviceProvider.getSupportedTemplateTypes());

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
                                                            null,
                                                            serviceGUID,
                                                            assetTypeName,
                                                            OpenMetadataType.ASSET.typeName,
                                                            null);

                if (connectionGUID != null)
                {
                    super.addConnectionForAsset(serviceGUID, connectionGUID);
                }

                if (serviceProvider instanceof GovernanceServiceProviderBase governanceServiceProviderBase)
                {
                    addSupportedRequestTypes(serviceGUID,
                                             assetTypeName,
                                             serviceGUID,
                                             assetTypeName,
                                             OpenMetadataType.ASSET.typeName,
                                             null,
                                             governanceServiceProviderBase.getSupportedRequestTypes());

                    addSupportedRequestParameters(serviceGUID,
                                                  assetTypeName,
                                                  serviceGUID,
                                                  assetTypeName,
                                                  OpenMetadataType.ASSET.typeName,
                                                  null,
                                                  governanceServiceProviderBase.getSupportedRequestParameters());

                    addSupportedActionTargets(serviceGUID,
                                              assetTypeName,
                                              serviceGUID,
                                              assetTypeName,
                                              OpenMetadataType.ASSET.typeName,
                                              null,
                                              governanceServiceProviderBase.getSupportedActionTargetTypes());

                    addProducedRequestParameters(serviceGUID,
                                                 assetTypeName,
                                                 serviceGUID,
                                                 assetTypeName,
                                                 OpenMetadataType.ASSET.typeName,
                                                 null,
                                                 governanceServiceProviderBase.getProducedRequestParameters());

                    addProducedActionTargets(serviceGUID,
                                             assetTypeName,
                                             serviceGUID,
                                             assetTypeName,
                                             OpenMetadataType.ASSET.typeName,
                                             null,
                                             governanceServiceProviderBase.getProducedActionTargetTypes());

                    addProducedGuards(serviceGUID,
                                      assetTypeName,
                                      serviceGUID,
                                      assetTypeName,
                                      OpenMetadataType.ASSET.typeName,
                                      null,
                                      governanceServiceProviderBase.getProducedGuards());

                    if (serviceProvider instanceof SurveyActionServiceProvider surveyActionServiceProvider)
                    {
                        addSupportedAnalysisSteps(serviceGUID,
                                                  assetTypeName,
                                                  serviceGUID,
                                                  assetTypeName,
                                                  OpenMetadataType.ASSET.typeName,
                                                  null,
                                                  surveyActionServiceProvider.getSupportedAnalysisSteps());

                        addProducedAnnotationTypes(serviceGUID,
                                                   assetTypeName,
                                                   serviceGUID,
                                                   assetTypeName,
                                                   OpenMetadataType.ASSET.typeName,
                                                   null,
                                                   surveyActionServiceProvider.getProducedAnnotationTypes());
                    }
                }
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
     * Add reference data for catalog templates.
     *
     * @param parentGUID unique identifier of template
     * @param parentTypeName type of template
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param replacementAttributeTypes list of reference values
     */
    public void addReplacementAttributes(String                         parentGUID,
                                         String                         parentTypeName,
                                         String                         anchorGUID,
                                         String                         anchorTypeName,
                                         String                         anchorDomainName,
                                         String                         anchorScopeGUID,
                                         List<ReplacementAttributeType> replacementAttributeTypes)
    {
        if (replacementAttributeTypes != null)
        {
            for (ReplacementAttributeType replacementAttributeType : replacementAttributeTypes)
            {
                Map<String, String> additionalProperties = replacementAttributeType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, replacementAttributeType.getExample());
                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(replacementAttributeType.getRequired()));

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::ReplacementAttribute::" + replacementAttributeType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.REPLACEMENT_ATTRIBUTE.getPropertyType(),
                                                           replacementAttributeType.getName(),
                                                           replacementAttributeType.getDescription(),
                                                           SpecificationPropertyType.REPLACEMENT_ATTRIBUTE.getPropertyType(),
                                                           SpecificationPropertyType.REPLACEMENT_ATTRIBUTE.getDescription(),
                                                           replacementAttributeType.getDataType(),
                                                           null,
                                                           replacementAttributeType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.REPLACEMENT_ATTRIBUTE.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for catalog templates.
     *
     * @param parentGUID unique identifier of template
     * @param parentTypeName type of template
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param placeholderPropertyTypes list of reference values
     */
    public void addPlaceholderProperties(String                        parentGUID,
                                         String                        parentTypeName,
                                         String                        anchorGUID,
                                         String                        anchorTypeName,
                                         String                        anchorDomainName,
                                         String                        anchorScopeGUID,
                                         List<PlaceholderPropertyType> placeholderPropertyTypes)
    {
        if (placeholderPropertyTypes != null)
        {
            for (PlaceholderPropertyType placeholderPropertyType : placeholderPropertyTypes)
            {
                Map<String, String> additionalProperties = placeholderPropertyType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, placeholderPropertyType.getExample());
                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(placeholderPropertyType.getRequired()));

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::PlaceholderProperty::" + placeholderPropertyType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.PLACEHOLDER_PROPERTY.getPropertyType(),
                                                           placeholderPropertyType.getName(),
                                                           placeholderPropertyType.getDescription(),
                                                           SpecificationPropertyType.PLACEHOLDER_PROPERTY.getPropertyType(),
                                                           SpecificationPropertyType.PLACEHOLDER_PROPERTY.getDescription(),
                                                           placeholderPropertyType.getDataType(),
                                                           null,
                                                           placeholderPropertyType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.PLACEHOLDER_PROPERTY.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for catalog templates.
     *
     * @param parentGUID unique identifier of template
     * @param parentTypeName type of template
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param templateTypes list of reference values
     */
    public void addSupportedTemplateTypes(String             parentGUID,
                                          String             parentTypeName,
                                          String             anchorGUID,
                                          String             anchorTypeName,
                                          String             anchorDomainName,
                                          String             anchorScopeGUID,
                                          List<TemplateType> templateTypes)
    {
        if (templateTypes != null)
        {
            for (TemplateType templateType : templateTypes)
            {
                Map<String, String> additionalProperties = templateType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(templateType.getRequired()));

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::SupportedTemplate::" + templateType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.SUPPORTED_TEMPLATE.getPropertyType(),
                                                           templateType.getName(),
                                                           templateType.getDescription(),
                                                           SpecificationPropertyType.SUPPORTED_TEMPLATE.getPropertyType(),
                                                           SpecificationPropertyType.SUPPORTED_TEMPLATE.getDescription(),
                                                           templateType.getOpenMetadataTypeName(),
                                                           null,
                                                           templateType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.SUPPORTED_TEMPLATE.getPropertyType());
                }
            }
        }
    }

    /**
     * Add reference data for connectors.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param configurationPropertyTypes list of reference values
     */
    public void addSupportedConfigurationProperties(String                          parentGUID,
                                                    String                          parentTypeName,
                                                    String                          anchorGUID,
                                                    String                          anchorTypeName,
                                                    String                          anchorDomainName,
                                                    String                          anchorScopeGUID,
                                                    List<ConfigurationPropertyType> configurationPropertyTypes)
    {
        if (configurationPropertyTypes != null)
        {
            for (ConfigurationPropertyType configurationPropertyType : configurationPropertyTypes)
            {
                Map<String, String> additionalProperties = configurationPropertyType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, configurationPropertyType.getExample());
                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(configurationPropertyType.getRequired()));

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::SupportedConfigurationProperty::" + configurationPropertyType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.SUPPORTED_CONFIGURATION_PROPERTY.getPropertyType(),
                                                           configurationPropertyType.getName(),
                                                           configurationPropertyType.getDescription(),
                                                           SpecificationPropertyType.SUPPORTED_CONFIGURATION_PROPERTY.getPropertyType(),
                                                           SpecificationPropertyType.SUPPORTED_CONFIGURATION_PROPERTY.getDescription(),
                                                           configurationPropertyType.getDataType(),
                                                           null,
                                                           configurationPropertyType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.SUPPORTED_CONFIGURATION_PROPERTY.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type name for parent element
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param supportedRequestTypes list of reference values
     */
    public void addSupportedRequestTypes(String                parentGUID,
                                         String                parentTypeName,
                                         String                anchorGUID,
                                         String                anchorTypeName,
                                         String                anchorDomainName,
                                         String                anchorScopeGUID,
                                         List<RequestTypeType> supportedRequestTypes)
    {
        if (supportedRequestTypes != null)
        {
            for (RequestTypeType supportedRequestType : supportedRequestTypes)
            {
                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::SupportedRequestType::" + supportedRequestType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.SUPPORTED_REQUEST_TYPE.getPropertyType(),
                                                           supportedRequestType.getName(),
                                                           supportedRequestType.getDescription(),
                                                           SpecificationPropertyType.SUPPORTED_REQUEST_TYPE.getPropertyType(),
                                                           SpecificationPropertyType.SUPPORTED_REQUEST_TYPE.getDescription(),
                                                           null,
                                                           null,
                                                           supportedRequestType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           supportedRequestType.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.SUPPORTED_REQUEST_TYPE.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param supportedRequestParameters list of reference values
     */
    public void addSupportedRequestParameters(String                     parentGUID,
                                              String                     parentTypeName,
                                              String                     anchorGUID,
                                              String                     anchorTypeName,
                                              String                     anchorDomainName,
                                              String                     anchorScopeGUID,
                                              List<RequestParameterType> supportedRequestParameters)
    {
        if (supportedRequestParameters != null)
        {
            for (RequestParameterType supportedRequestParameter : supportedRequestParameters)
            {
                Map<String, String> additionalProperties = supportedRequestParameter.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, supportedRequestParameter.getExample());
                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(supportedRequestParameter.getRequired()));

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::SupportedRequestParameter::" + supportedRequestParameter.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER.getPropertyType(),
                                                           supportedRequestParameter.getName(),
                                                           supportedRequestParameter.getDescription(),
                                                           SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER.getPropertyType(),
                                                           SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER.getDescription(),
                                                           supportedRequestParameter.getDataType(),
                                                           null,
                                                           supportedRequestParameter.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param actionTargetTypes list of reference values
     */
    public void addSupportedActionTargets(String                 parentGUID,
                                          String                 parentTypeName,
                                          String                 anchorGUID,
                                          String                 anchorTypeName,
                                          String                 anchorDomainName,
                                          String                 anchorScopeGUID,
                                          List<ActionTargetType> actionTargetTypes)
    {
        if (actionTargetTypes != null)
        {
            for (ActionTargetType supportedActionTarget : actionTargetTypes)
            {
                Map<String, String> additionalProperties = supportedActionTarget.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, supportedActionTarget.getDeployedImplementationType());
                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, Boolean.toString(supportedActionTarget.getRequired()));

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::SupportedActionTarget::" + supportedActionTarget.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getPropertyType(),
                                                           supportedActionTarget.getName(),
                                                           supportedActionTarget.getDescription(),
                                                           SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getPropertyType(),
                                                           SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getDescription(),
                                                           supportedActionTarget.getOpenMetadataTypeName(),
                                                           null,
                                                           supportedActionTarget.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param analysisStepTypes list of reference values
     */
    public void addSupportedAnalysisSteps(String                 parentGUID,
                                          String                 parentTypeName,
                                          String                 anchorGUID,
                                          String                 anchorTypeName,
                                          String                 anchorDomainName,
                                          String                 anchorScopeGUID,
                                          List<AnalysisStepType> analysisStepTypes)
    {
        if (analysisStepTypes != null)
        {
            for (AnalysisStepType analysisStepType : analysisStepTypes)
            {
                Map<String, String> additionalProperties = analysisStepType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::SupportedAnalysisStep::" + analysisStepType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.SUPPORTED_ANALYSIS_STEP.getPropertyType(),
                                                           analysisStepType.getName(),
                                                           analysisStepType.getDescription(),
                                                           SpecificationPropertyType.SUPPORTED_ANALYSIS_STEP.getPropertyType(),
                                                           SpecificationPropertyType.SUPPORTED_ANALYSIS_STEP.getDescription(),
                                                           DataType.STRING.getName(),
                                                           null,
                                                           analysisStepType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.SUPPORTED_ANALYSIS_STEP.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param annotationTypeTypes list of reference values
     */
    public void addProducedAnnotationTypes(String                   parentGUID,
                                           String                   parentTypeName,
                                           String                   anchorGUID,
                                           String                   anchorTypeName,
                                           String                   anchorDomainName,
                                           String                   anchorScopeGUID,
                                           List<AnnotationTypeType> annotationTypeTypes)
    {
        if (annotationTypeTypes != null)
        {
            for (AnnotationTypeType annotationTypeType : annotationTypeTypes)
            {
                Map<String, String> additionalProperties = annotationTypeType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put(OpenMetadataProperty.EXPRESSION.name, annotationTypeType.getExpression());
                additionalProperties.put(OpenMetadataProperty.EXPLANATION.name, annotationTypeType.getExplanation());
                additionalProperties.put(OpenMetadataProperty.ANALYSIS_STEP.name, annotationTypeType.getAnalysisStepName());
                additionalProperties.put(OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name, annotationTypeType.getOpenMetadataTypeName());

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::ProducedAnnotationType::" + annotationTypeType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.PRODUCED_ANNOTATION_TYPE.getPropertyType(),
                                                           annotationTypeType.getName(),
                                                           annotationTypeType.getDescription(),
                                                           SpecificationPropertyType.PRODUCED_ANNOTATION_TYPE.getPropertyType(),
                                                           SpecificationPropertyType.PRODUCED_ANNOTATION_TYPE.getDescription(),
                                                           annotationTypeType.getOpenMetadataTypeName(),
                                                           null,
                                                           annotationTypeType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.PRODUCED_ANNOTATION_TYPE.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param producedRequestParameters list of reference values
     */
    public void addProducedRequestParameters(String                     parentGUID,
                                             String                     parentTypeName,
                                             String                     anchorGUID,
                                             String                     anchorTypeName,
                                             String                     anchorDomainName,
                                             String                     anchorScopeGUID,
                                             List<RequestParameterType> producedRequestParameters)
    {
        if (producedRequestParameters != null)
        {
            for (RequestParameterType producedRequestParameter : producedRequestParameters)
            {
                Map<String, String> additionalProperties = producedRequestParameter.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                String required = "false";

                if (producedRequestParameter.getRequired())
                {
                    required = "true";
                }

                additionalProperties.put(OpenMetadataProperty.EXAMPLE.name, producedRequestParameter.getExample());
                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, required);

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::ProducedRequestParameter::" + producedRequestParameter.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.PRODUCED_REQUEST_PARAMETER.getPropertyType(),
                                                           producedRequestParameter.getName(),
                                                           producedRequestParameter.getDescription(),
                                                           SpecificationPropertyType.PRODUCED_REQUEST_PARAMETER.getPropertyType(),
                                                           SpecificationPropertyType.PRODUCED_REQUEST_PARAMETER.getDescription(),
                                                           producedRequestParameter.getDataType(),
                                                           null,
                                                           producedRequestParameter.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.PRODUCED_REQUEST_PARAMETER.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param actionTargetTypes list of reference values
     */
    public void addProducedActionTargets(String                 parentGUID,
                                         String                 parentTypeName,
                                         String                 anchorGUID,
                                         String                 anchorTypeName,
                                         String                 anchorDomainName,
                                         String                 anchorScopeGUID,
                                         List<ActionTargetType> actionTargetTypes)
    {
        if (actionTargetTypes != null)
        {
            for (ActionTargetType actionTargetType : actionTargetTypes)
            {
                Map<String, String> additionalProperties = actionTargetType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                String required = "false";

                if (actionTargetType.getRequired())
                {
                    required = "true";
                }

                additionalProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, actionTargetType.getDeployedImplementationType());
                additionalProperties.put(OpenMetadataProperty.REQUIRED.name, required);

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::ProducedActionTarget::" + actionTargetType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.PRODUCED_ACTION_TARGET.getPropertyType(),
                                                           actionTargetType.getName(),
                                                           actionTargetType.getDescription(),
                                                           SpecificationPropertyType.PRODUCED_ACTION_TARGET.getPropertyType(),
                                                           SpecificationPropertyType.PRODUCED_ACTION_TARGET.getDescription(),
                                                           actionTargetType.getOpenMetadataTypeName(),
                                                           null,
                                                           actionTargetType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.PRODUCED_ACTION_TARGET.getPropertyType());
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param anchorGUID unique identifier of anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID scope of anchor
     * @param guardTypes list of reference values
     */
    public void addProducedGuards(String          parentGUID,
                                  String          parentTypeName,
                                  String          anchorGUID,
                                  String          anchorTypeName,
                                  String          anchorDomainName,
                                  String          anchorScopeGUID,
                                  List<GuardType> guardTypes)
    {
        if (guardTypes != null)
        {
            for (GuardType guardType : guardTypes)
            {
                Map<String, String> additionalProperties = guardType.getOtherPropertyValues();

                if (additionalProperties == null)
                {
                    additionalProperties = new HashMap<>();
                }

                additionalProperties.put("completionStatus", guardType.getCompletionStatus().getName());
                additionalProperties.put("completionStatusDescription", guardType.getCompletionStatus().getDescription());

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           anchorGUID,
                                                           anchorTypeName,
                                                           anchorDomainName,
                                                           anchorScopeGUID,
                                                           OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                           parentTypeName + "::" + parentGUID + "::ProducedGuard::" + guardType.getName(),
                                                           Category.SPECIFICATION_PROPERTY.getName(),
                                                           SpecificationPropertyType.PRODUCED_GUARD.getPropertyType(),
                                                           guardType.getName(),
                                                           guardType.getDescription(),
                                                           SpecificationPropertyType.PRODUCED_GUARD.getPropertyType(),
                                                           SpecificationPropertyType.PRODUCED_GUARD.getDescription(),
                                                           DataType.STRING.getName(),
                                                           guardType.getCompletionStatus().getName(),
                                                           guardType.getName(),
                                                           null,
                                                           null,
                                                           true,
                                                           additionalProperties);

                if (validValueGUID != null)
                {
                    addSpecificationPropertyAssignmentRelationship(parentGUID,
                                                                   validValueGUID,
                                                                   SpecificationPropertyType.PRODUCED_GUARD.getPropertyType());
                }
            }
        }
    }


    /**
     * Create a software capability entity for a governance engine.
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
                                      String              userId,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties)
    {
        String engineTypeName = OpenMetadataType.GOVERNANCE_ENGINE.typeName;

        if (typeName != null)
        {
            engineTypeName = typeName;
        }

        String capabilityGUID  = super.addSoftwareCapability(engineTypeName,
                                                             qualifiedName,
                                                             displayName,
                                                             description,
                                                             capabilityType,
                                                             capabilityVersion,
                                                             patchLevel,
                                                             source,
                                                             additionalProperties,
                                                             extendedProperties,
                                                             (Classification)null,
                                                             null,
                                                             engineTypeName,
                                                             DeployedImplementationType.SOFTWARE_CAPABILITY.getAssociatedTypeName(),
                                                             null);

        if (capabilityGUID != null)
        {
            this.addITProfile(capabilityGUID,
                              userId,
                              qualifiedName + ":ActorProfile",
                              displayName,
                              description,
                              null);
        }

        return capabilityGUID;
    }


    /**
     * Create the relationship between a governance engine and one of its supported governance services.
     *
     * @param engineGUID unique identifier of the asset
     * @param requestType governance request type to use when calling the governance engine
     * @param serviceRequestType  request type to use when calling the service (if null, governance request type is used)
     * @param requestParameters default request parameters to pass to the service when called with this request type
     * @param generateIntegrationReport should an integration report be generated each time the service is called?
     * @param serviceGUID unique identifier of the service
     */
    public void addSupportedGovernanceService(String              engineGUID,
                                              String              requestType,
                                              String              serviceRequestType,
                                              Map<String, String> requestParameters,
                                              boolean             generateIntegrationReport,
                                              String              serviceGUID)
    {
        final String methodName = "addSupportedGovernanceService";

        EntityDetail engineEntity = archiveBuilder.getEntity(engineGUID);
        EntityDetail serviceEntity = archiveBuilder.getEntity(serviceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(engineEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(serviceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.REQUEST_TYPE.name, requestType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SERVICE_REQUEST_TYPE.name, serviceRequestType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REQUEST_PARAMETERS.name, requestParameters, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.GENERATE_CONNECTOR_ACTIVITY_REPORT.name, generateIntegrationReport, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
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
        String processTypeName = OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName;

        if (typeName != null)
        {
            processTypeName = typeName;
        }

        Map<String, Object> processExtendedProperties = extendedProperties;

        if (processExtendedProperties == null)
        {
            processExtendedProperties = new HashMap<>();

            processExtendedProperties.put(OpenMetadataProperty.DOMAIN_IDENTIFIER.name, domainIdentifier);
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
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName type name of the anchor
     * @param anchorDomainName type name of the anchor's domain
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the capability
     * @param displayName display name for the capability
     * @param description description about the capability
     * @param domainIdentifier which governance domain - 0=all
     * @param supportedRequestParameters request parameters to use when triggering this governance action
     * @param supportedActionTargets action targets to use when triggering this governance action
     * @param supportedAnalysisSteps analysis steps supported by this governance action (survey action)
     * @param supportedAnnotationTypes annotation types supported by this governance action (survey action)
     * @param producedRequestParameters request parameters produced by this governance action
     * @param producedActionTargets action targets produced by this governance action
     * @param producedGuards guards expected from the implementation
     * @param waitTime minutes to wait before starting governance action
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the new entity
     */
    public String addGovernanceActionType(String                     typeName,
                                          String                     anchorGUID,
                                          String                     anchorTypeName,
                                          String                     anchorDomainName,
                                          String                     anchorScopeGUID,
                                          String                     qualifiedName,
                                          String                     displayName,
                                          String                     description,
                                          int                        domainIdentifier,
                                          List<RequestParameterType> supportedRequestParameters,
                                          List<ActionTargetType>     supportedActionTargets,
                                          List<AnalysisStepType>     supportedAnalysisSteps,
                                          List<AnnotationTypeType>   supportedAnnotationTypes,
                                          List<RequestParameterType> producedRequestParameters,
                                          List<ActionTargetType>     producedActionTargets,
                                          List<GuardType>            producedGuards,
                                          int                        waitTime,
                                          Map<String, String>        additionalProperties,
                                          Map<String, Object>        extendedProperties,
                                          List<Classification>       classifications)
    {
        final String methodName = "addGovernanceActionType";

        String actionTypeName = OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName;

        if (typeName != null)
        {
            actionTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DOMAIN_IDENTIFIER.name, domainIdentifier, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.WAIT_TIME.name, waitTime, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> entityClassifications = classifications;

        if (anchorGUID != null)
        {
            if (entityClassifications == null)
            {
                entityClassifications = new ArrayList<>();
            }

            entityClassifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        EntityDetail typeEntity = archiveHelper.getEntityDetail(actionTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);

        archiveBuilder.addEntity(typeEntity);

        addSupportedRequestParameters(typeEntity.getGUID(),
                                      actionTypeName,
                                      anchorGUID,
                                      anchorTypeName,
                                      anchorDomainName,
                                      anchorScopeGUID,
                                      supportedRequestParameters);

        addSupportedActionTargets(typeEntity.getGUID(),
                                  actionTypeName,
                                  anchorGUID,
                                  anchorTypeName,
                                  anchorDomainName,
                                  anchorScopeGUID,
                                  supportedActionTargets);

        addSupportedAnalysisSteps(typeEntity.getGUID(),
                                  actionTypeName,
                                  anchorGUID,
                                  anchorTypeName,
                                  anchorDomainName,
                                  anchorScopeGUID,
                                  supportedAnalysisSteps);

        addProducedAnnotationTypes(typeEntity.getGUID(),
                                   actionTypeName,
                                   anchorGUID,
                                   anchorTypeName,
                                   anchorDomainName,
                                   anchorScopeGUID,
                                   supportedAnnotationTypes);

        addProducedRequestParameters(typeEntity.getGUID(),
                                     actionTypeName,
                                     anchorGUID,
                                     anchorTypeName,
                                     anchorDomainName,
                                     anchorScopeGUID,
                                     producedRequestParameters);

        addProducedActionTargets(typeEntity.getGUID(),
                                 actionTypeName,
                                 anchorGUID,
                                 anchorTypeName,
                                 anchorDomainName,
                                 anchorScopeGUID,
                                 producedActionTargets);

        addProducedGuards(typeEntity.getGUID(),
                          actionTypeName,
                          anchorGUID,
                          anchorTypeName,
                          anchorDomainName,
                          anchorScopeGUID,
                          producedGuards);

        return typeEntity.getGUID();
    }


    /**
     * Create a governance action process step.
     *
     * @param typeName name of subtype to use - default is GovernanceActionProcessStep
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName type name of the anchor
     * @param anchorDomainName type name of the anchor's domain
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the capability
     * @param displayName display name for the capability
     * @param description description about the capability
     * @param domainIdentifier which governance domain - 0=all
     * @param supportedRequestParameters request parameters to use when triggering this governance action
     * @param supportedActionTargets action targets to use when triggering this governance action
     * @param supportedAnalysisSteps analysis steps supported by this governance action (survey action)
     * @param supportedAnnotationTypes annotation types supported by this governance action (survey action)
     * @param producedRequestParameters request parameters produced by this governance action
     * @param producedActionTargets action targets produced by this governance action
     * @param producedGuards guards expected from the implementation
     * @param waitTime minutes to wait before starting governance action
     * @param ignoreMultipleTriggers only run this once even if the same guard occurs multiple times while it is waiting
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the new entity
     */
    public String addGovernanceActionProcessStep(String                     typeName,
                                                 String                     anchorGUID,
                                                 String                     anchorTypeName,
                                                 String                     anchorDomainName,
                                                 String                     anchorScopeGUID,
                                                 String                     qualifiedName,
                                                 String                     displayName,
                                                 String                     description,
                                                 int                        domainIdentifier,
                                                 List<RequestParameterType> supportedRequestParameters,
                                                 List<ActionTargetType>     supportedActionTargets,
                                                 List<AnalysisStepType>     supportedAnalysisSteps,
                                                 List<AnnotationTypeType>   supportedAnnotationTypes,
                                                 List<RequestParameterType> producedRequestParameters,
                                                 List<ActionTargetType>     producedActionTargets,
                                                 List<GuardType>            producedGuards,
                                                 int                        waitTime,
                                                 boolean                    ignoreMultipleTriggers,
                                                 Map<String, String>        additionalProperties,
                                                 Map<String, Object>        extendedProperties,
                                                 List<Classification>       classifications)
    {
        final String methodName = "addGovernanceActionProcessStep";

        String actionTypeName = OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName;

        if (typeName != null)
        {
            actionTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DOMAIN_IDENTIFIER.name, domainIdentifier, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.WAIT_TIME.name, waitTime, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IGNORE_MULTIPLE_TRIGGERS.name, ignoreMultipleTriggers, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> entityClassifications = classifications;

        if (anchorGUID != null)
        {
            if (entityClassifications == null)
            {
                entityClassifications = new ArrayList<>();
            }

            entityClassifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        EntityDetail stepEntity = archiveHelper.getEntityDetail(actionTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);

        archiveBuilder.addEntity(stepEntity);

        addSupportedRequestParameters(stepEntity.getGUID(),
                                      actionTypeName,
                                      anchorGUID,
                                      anchorTypeName,
                                      anchorDomainName,
                                      anchorScopeGUID,
                                      supportedRequestParameters);

        addSupportedActionTargets(stepEntity.getGUID(),
                                  actionTypeName,
                                  anchorGUID,
                                  anchorTypeName,
                                  anchorDomainName,
                                  anchorScopeGUID,
                                  supportedActionTargets);

        addSupportedAnalysisSteps(stepEntity.getGUID(),
                                  actionTypeName,
                                  anchorGUID,
                                  anchorTypeName,
                                  anchorDomainName,
                                  anchorScopeGUID,
                                  supportedAnalysisSteps);

        addProducedAnnotationTypes(stepEntity.getGUID(),
                                   actionTypeName,
                                   anchorGUID,
                                   anchorTypeName,
                                   anchorDomainName,
                                   anchorScopeGUID,
                                   supportedAnnotationTypes);

        addProducedRequestParameters(stepEntity.getGUID(),
                                     actionTypeName,
                                     anchorGUID,
                                     anchorTypeName,
                                     anchorDomainName,
                                     anchorScopeGUID,
                                     producedRequestParameters);

        addProducedActionTargets(stepEntity.getGUID(),
                                 actionTypeName,
                                 anchorGUID,
                                 anchorTypeName,
                                 anchorDomainName,
                                 anchorScopeGUID,
                                 producedActionTargets);

        addProducedGuards(stepEntity.getGUID(),
                          actionTypeName,
                          anchorGUID,
                          anchorTypeName,
                          anchorDomainName,
                          anchorScopeGUID,
                          producedGuards);

        return stepEntity.getGUID();
    }



    /**
     * Create the relationship between a governance action process and the first governance action type to execute.
     *
     * @param governanceActionProcessGUID unique identifier of the governance action process
     * @param guard initial guard for the first step in the process
     * @param requestParameters predefined request parameters
     * @param governanceActionProcessStepGUID unique identifier of the implementing governance engine
     */
    public void addGovernanceActionProcessFlow(String              governanceActionProcessGUID,
                                               String              guard,
                                               Map<String, String> requestParameters,
                                               String              governanceActionProcessStepGUID)
    {
        final String methodName = "addGovernanceActionFlow";

        EntityDetail processEntity    = archiveBuilder.getEntity(governanceActionProcessGUID);
        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionProcessStepGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(processEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(actionTypeEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.GUARD.name, guard, methodName);

        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REQUEST_PARAMETERS.name, requestParameters, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(governanceActionProcessGUID + "_to_" + governanceActionProcessStepGUID + "_governance_action_process_flow_relationship"),
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
     * @param nextGovernanceActionTypeGUID unique identifier of the implementing governance engine
     */
    public void addNextGovernanceActionProcessStep(String  governanceActionProcessStepGUID,
                                                   String  guard,
                                                   boolean mandatoryGuard,
                                                   String  nextGovernanceActionTypeGUID)
    {
        final String methodName = "addNextGovernanceActionProcessStep";

        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionProcessStepGUID);
        EntityDetail nextActionTypeEntity = archiveBuilder.getEntity(nextGovernanceActionTypeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(actionTypeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(nextActionTypeEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.GUARD.name, guard, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MANDATORY_GUARD.name, mandatoryGuard, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(governanceActionProcessStepGUID + "_to_" + nextGovernanceActionTypeGUID + "_next_governance_action_process_step_relationship_with_guard_" + guard),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a governance action type/process step and the governance engine that supplies its implementation.
     *
     * @param governanceActionGUID unique identifier of the governance action type/process step
     * @param requestType governance request type to use when calling the engine
     * @param requestParameters default request parameters to pass to the service when called with this request type
     * @param requestParameterFilter list the names of the request parameters to remove from the supplied requestParameters
     * @param requestParameterMap provide a translation map between the supplied name of the requestParameters and the names supported by the implementation of the governance service
     * @param actionTargetFilter list the names of the action targets to remove from the supplied action targets
     * @param actionTargetMap provide a translation map between the supplied name of an action target and the name supported by the implementation of the governance service
     * @param governanceEngineGUID unique identifier of the implementing governance engine
     */
    public void addGovernanceActionExecutor(String              governanceActionGUID,
                                            String              requestType,
                                            Map<String, String> requestParameters,
                                            List<String>        requestParameterFilter,
                                            Map<String, String> requestParameterMap,
                                            List<String>        actionTargetFilter,
                                            Map<String, String> actionTargetMap,
                                            String              governanceEngineGUID)
    {
        final String methodName = "addGovernanceActionExecutor";

        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionGUID);
        EntityDetail engineEntity = archiveBuilder.getEntity(governanceEngineGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(actionTypeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(engineEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.REQUEST_TYPE.name, requestType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REQUEST_PARAMETERS.name, requestParameters, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REQUEST_PARAMETER_FILTER.name, requestParameterFilter, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REQUEST_PARAMETER_MAP.name, requestParameterMap, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ACTION_TARGET_FILTER.name, actionTargetFilter, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ACTION_TARGET_MAP.name, actionTargetMap, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(governanceActionGUID + "_to_" + governanceEngineGUID + "_governance_action_executor_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create the relationship between a governance action type and a pre-defined action target.
     *
     * @param governanceActionTypeGUID unique identifier of the governance action type/process step
     * @param actionTarget details of the action target
     */
    public void addTargetForActionType(String          governanceActionTypeGUID,
                                       NewActionTarget actionTarget)
    {
        final String methodName = "addTargetForActionType";

        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionTypeGUID);
        EntityDetail targetEntity = archiveBuilder.getEntity(actionTarget.getActionTargetGUID());

        EntityProxy end1 = archiveHelper.getEntityProxy(actionTypeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(targetEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ACTION_TARGET_NAME.name, actionTarget.getActionTargetName(), methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(governanceActionTypeGUID + "_to_" + actionTarget.getActionTargetGUID() + "_target_for_action_type_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a referenceable and a data processing purpose.
     *
     * @param referenceableGUID unique identifier of the element that is approved
     * @param dataProcessingPurposeGUID unique identifier of the
     */
    public void addApprovedPurpose(String referenceableGUID,
                                   String dataProcessingPurposeGUID)
    {
        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);
        EntityDetail purposeEntity = archiveBuilder.getEntity(dataProcessingPurposeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(purposeEntity);


        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + dataProcessingPurposeGUID + "_approved_purpose_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }
}
