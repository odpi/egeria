/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.GuardType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameterType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestTypeType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStepType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;
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
     * Create an integration connector entity.
     *
     * @param connectorProviderName name of the connector provider for the integration connector
     * @param configurationProperties configuration properties for the integration connector (goes in the connection)
     * @param qualifiedName unique name for the integration connector
     * @param displayName display name for the integration connector
     * @param description description about the integration connector
     * @param additionalProperties any other properties
     *
     * @return id for the integration connector
     */
    public String addIntegrationConnector(String              connectorProviderName,
                                          Map<String, Object> configurationProperties,
                                          String              qualifiedName,
                                          String              displayName,
                                          String              description,
                                          String              endpointAddress,
                                          Map<String, String> additionalProperties)
    {
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

            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType());

            String connectorGUID = super.addAsset(DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  displayName,
                                                  description,
                                                  additionalProperties,
                                                  extendedProperties);

            String endpointGUID = null;
            if (endpointAddress != null)
            {
                endpointGUID = super.addEndpoint(connectorGUID,
                                                 DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                                 qualifiedName + "_endpoint",
                                                 displayName + "'s first catalog target",
                                                 "Endpoint for integration connector: " + qualifiedName,
                                                 endpointAddress,
                                                 null,
                                                 null);
            }

            String connectionGUID = super.addConnection(qualifiedName + "_implementation",
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
                                                        DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName());

            if ((connectorGUID != null) && (connectionGUID != null))
            {
                super.addConnectionForAsset(connectorGUID, null, connectionGUID);
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
                                           extendedProperties);
    }


    /**
     * Create the relationship between a governance engine and one of its supported governance services.
     *
     * @param integrationGroupGUID unique identifier of the integration group
     * @param connectorName name of the connector used in messages and REST calls
     * @param connectorUserId  userId for the connector to use (overrides the server's userId)
     * @param metadataSourceQualifiedName unique name of the metadata collection for anything catalogued by this connector
     * @param refreshTimeInterval how long (in minutes) between each refresh of the connector
     * @param integrationConnectorGUID unique identifier of the integration connector
     */
    public void addRegisteredIntegrationConnector(String              integrationGroupGUID,
                                                  String              connectorName,
                                                  String              connectorUserId,
                                                  String              metadataSourceQualifiedName,
                                                  long                refreshTimeInterval,
                                                  String              integrationConnectorGUID)
    {
        final String methodName = "addRegisteredIntegrationConnector";

        EntityDetail groupEntity = archiveBuilder.getEntity(integrationGroupGUID);
        EntityDetail connectorEntity = archiveBuilder.getEntity(integrationConnectorGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(groupEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(connectorEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataType.CONNECTOR_NAME_PROPERTY_NAME, connectorName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataType.CONNECTOR_USER_ID_PROPERTY_NAME, connectorUserId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataType.METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME, metadataSourceQualifiedName, methodName);
        properties = archiveHelper.addLongPropertyToInstance(archiveRootName, properties, OpenMetadataType.REFRESH_TIME_INTERVAL_PROPERTY_NAME, refreshTimeInterval, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(integrationGroupGUID + "_to_" + integrationConnectorGUID + "_" + connectorName + "_registered_integration_connector_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }

    /**
     * Create a governance service entity.
     *
     * @param deployedImplementationType name of governance service subtype to use - default is GovernanceService
     * @param connectorProviderName name of the connector provider for the governance service
     * @param configurationProperties configuration properties for the governance service (goes in the connection)
     * @param qualifiedName unique name for the governance service
     * @param displayName display name for the governance service
     * @param description description about the governance service
     * @param additionalProperties any other properties
     *
     * @return id for the governance service
     */
    public String addGovernanceService(DeployedImplementationType deployedImplementationType,
                                       String                     connectorProviderName,
                                       Map<String, Object>        configurationProperties,
                                       String                     qualifiedName,
                                       String                     displayName,
                                       String                     description,
                                       Map<String, String>        additionalProperties)
    {
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

            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                   deployedImplementationType.getDeployedImplementationType());

            String serviceGUID = super.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                qualifiedName,
                                                displayName,
                                                description,
                                                additionalProperties,
                                                extendedProperties);

            if (serviceGUID != null)
            {
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
                                                            deployedImplementationType.getAssociatedTypeName());

                if (connectionGUID != null)
                {
                    super.addConnectionForAsset(serviceGUID, null, connectionGUID);
                }

                if (serviceProvider instanceof GovernanceServiceProviderBase governanceServiceProviderBase)
                {
                    addSupportedRequestTypes(serviceGUID,
                                             deployedImplementationType.getAssociatedTypeName(),
                                             governanceServiceProviderBase.getSupportedRequestTypes());

                    addSupportedRequestParameters(serviceGUID,
                                                  deployedImplementationType.getAssociatedTypeName(),
                                                  governanceServiceProviderBase.getSupportedRequestParameters());

                    addSupportedActionTargets(serviceGUID,
                                              deployedImplementationType.getAssociatedTypeName(),
                                              governanceServiceProviderBase.getSupportedActionTargetTypes());

                    addProducedRequestParameters(serviceGUID,
                                                  deployedImplementationType.getAssociatedTypeName(),
                                                  governanceServiceProviderBase.getProducedRequestParameters());

                    addProducedActionTargets(serviceGUID,
                                              deployedImplementationType.getAssociatedTypeName(),
                                              governanceServiceProviderBase.getProducedActionTargetTypes());

                    addProducedGuards(serviceGUID,
                                      deployedImplementationType.getAssociatedTypeName(),
                                      governanceServiceProviderBase.getProducedGuards());

                    if (serviceProvider instanceof SurveyActionServiceProvider surveyActionServiceProvider)
                    {
                        addSupportedAnalysisSteps(serviceGUID,
                                                  deployedImplementationType.getAssociatedTypeName(),
                                                  surveyActionServiceProvider.getSupportedAnalysisSteps());

                        addSupportedAnnotationTypes(serviceGUID,
                                                    deployedImplementationType.getAssociatedTypeName(),
                                                    surveyActionServiceProvider.getSupportedAnnotationTypes());
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
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param supportedRequestTypes list of reference values
     */
    public void addSupportedRequestTypes(String                parentGUID,
                                         String                parentTypeName,
                                         List<RequestTypeType> supportedRequestTypes)
    {
        if (supportedRequestTypes != null)
        {
            for (RequestTypeType supportedRequestType : supportedRequestTypes)
            {
                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":SupportedRequestType:" + supportedRequestType.getRequestType(),
                                                           supportedRequestType.getRequestType(),
                                                           supportedRequestType.getDescription(),
                                                           null,
                                                           null,
                                                           null,
                                                           supportedRequestType.getRequestType(),
                                                           false,
                                                           true,
                                                           supportedRequestType.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "supportedRequestTypes",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param supportedRequestParameters list of reference values
     */
    public void addSupportedRequestParameters(String                     parentGUID,
                                              String                     parentTypeName,
                                              List<RequestParameterType> supportedRequestParameters)
    {
        if (supportedRequestParameters != null)
        {
            for (RequestParameterType supportedRequestParameter : supportedRequestParameters)
            {
                String scope = "optional";

                if (supportedRequestParameter.getRequired())
                {
                    scope = "required";
                }

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":SupportedRequestParameter:" + supportedRequestParameter.getName(),
                                                           supportedRequestParameter.getName(),
                                                           supportedRequestParameter.getDescription(),
                                                           supportedRequestParameter.getDataType(),
                                                           supportedRequestParameter.getExample(),
                                                           scope,
                                                           supportedRequestParameter.getName(),
                                                           false,
                                                           true,
                                                           supportedRequestParameter.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "supportedRequestParameters",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param actionTargetTypes list of reference values
     */
    public void addSupportedActionTargets(String                 parentGUID,
                                          String                 parentTypeName,
                                          List<ActionTargetType> actionTargetTypes)
    {
        if (actionTargetTypes != null)
        {
            for (ActionTargetType supportedActionTarget : actionTargetTypes)
            {
                String scope = "optional";

                if (supportedActionTarget.getRequired())
                {
                    scope = "required";
                }

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":SupportedActionTarget:" + supportedActionTarget.getName(),
                                                           supportedActionTarget.getName(),
                                                           supportedActionTarget.getDescription(),
                                                           supportedActionTarget.getTypeName(),
                                                           supportedActionTarget.getDeployedImplementationType(),
                                                           scope,
                                                           supportedActionTarget.getName(),
                                                           false,
                                                           true,
                                                           supportedActionTarget.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "supportedActionTargets",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param analysisStepTypes list of reference values
     */
    public void addSupportedAnalysisSteps(String                 parentGUID,
                                          String                 parentTypeName,
                                          List<AnalysisStepType> analysisStepTypes)
    {
        if (analysisStepTypes != null)
        {
            for (AnalysisStepType analysisStepType : analysisStepTypes)
            {
                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":SupportedAnalysisStep:" + analysisStepType.getName(),
                                                           analysisStepType.getName(),
                                                           analysisStepType.getDescription(),
                                                           null,
                                                           null,
                                                           null,
                                                           analysisStepType.getName(),
                                                           false,
                                                           true,
                                                           analysisStepType.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "supportedAnalysisSteps",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param annotationTypeTypes list of reference values
     */
    public void addSupportedAnnotationTypes(String                   parentGUID,
                                            String                   parentTypeName,
                                            List<AnnotationTypeType> annotationTypeTypes)
    {
        if (annotationTypeTypes != null)
        {
            for (AnnotationTypeType annotationTypeType : annotationTypeTypes)
            {
                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":SupportedAnnotationType:" + annotationTypeType.getName(),
                                                           annotationTypeType.getOpenMetadataTypeName() + ":" + annotationTypeType.getName(),
                                                           annotationTypeType.getSummary(),
                                                           annotationTypeType.getExpression(),
                                                           annotationTypeType.getExplanation(),
                                                           annotationTypeType.getAnalysisStepName(),
                                                           annotationTypeType.getName(),
                                                           false,
                                                           true,
                                                           annotationTypeType.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "supportedAnnotationTypes",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param producedRequestParameters list of reference values
     */
    public void addProducedRequestParameters(String                     parentGUID,
                                             String                     parentTypeName,
                                             List<RequestParameterType> producedRequestParameters)
    {
        if (producedRequestParameters != null)
        {
            for (RequestParameterType producedRequestParameter : producedRequestParameters)
            {
                String scope = "optional";

                if (producedRequestParameter.getRequired())
                {
                    scope = "guaranteed";
                }

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":ProducedRequestParameter:" + producedRequestParameter.getName(),
                                                           producedRequestParameter.getName(),
                                                           producedRequestParameter.getDescription(),
                                                           producedRequestParameter.getDataType(),
                                                           producedRequestParameter.getExample(),
                                                           scope,
                                                           producedRequestParameter.getName(),
                                                           false,
                                                           true,
                                                           producedRequestParameter.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "producedRequestParameters",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param actionTargetTypes list of reference values
     */
    public void addProducedActionTargets(String                 parentGUID,
                                         String                 parentTypeName,
                                         List<ActionTargetType> actionTargetTypes)
    {
        if (actionTargetTypes != null)
        {
            for (ActionTargetType actionTargetType : actionTargetTypes)
            {
                String scope = "optional";

                if (actionTargetType.getRequired())
                {
                    scope = "guaranteed";
                }

                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":ProducedActionTarget:" + actionTargetType.getName(),
                                                           actionTargetType.getName(),
                                                           actionTargetType.getDescription(),
                                                           actionTargetType.getTypeName(),
                                                           actionTargetType.getDeployedImplementationType(),
                                                           scope,
                                                           actionTargetType.getName(),
                                                           false,
                                                           true,
                                                           actionTargetType.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "producedActionTargets",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
                }
            }
        }
    }


    /**
     * Add reference data for governance services and actions.
     *
     * @param parentGUID unique identifier of governance service/action
     * @param parentTypeName type of parent
     * @param guardTypes list of reference values
     */
    public void addProducedGuards(String          parentGUID,
                                  String          parentTypeName,
                                  List<GuardType> guardTypes)
    {
        if (guardTypes != null)
        {
            for (GuardType guardType : guardTypes)
            {
                String validValueGUID = this.addValidValue(null,
                                                           null,
                                                           parentGUID,
                                                           parentTypeName,
                                                           OpenMetadataType.VALID_VALUE_DEFINITION_TYPE_NAME,
                                                           parentTypeName + ":" + parentGUID + ":ProducedGuard:" + guardType.getGuard(),
                                                           guardType.getGuard(),
                                                           guardType.getDescription(),
                                                           null,
                                                           guardType.getCompletionStatus().getName(),
                                                           null,
                                                           guardType.getGuard(),
                                                           false,
                                                           true,
                                                           guardType.getOtherPropertyValues());

                if (validValueGUID != null)
                {
                    addReferenceValueAssignmentRelationship(parentGUID,
                                                            validValueGUID,
                                                            "producedGuards",
                                                            100,
                                                            null,
                                                            null,
                                                            null,
                                                            null);
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
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties)
    {
        String engineTypeName = OpenMetadataType.GOVERNANCE_ENGINE.typeName;

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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.REQUEST_TYPE.name, requestType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SERVICE_REQUEST_TYPE.name, serviceRequestType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REQUEST_PARAMETERS.name, requestParameters, methodName);

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
        String processTypeName = OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME;

        if (typeName != null)
        {
            processTypeName = typeName;
        }

        Map<String, Object> processExtendedProperties = extendedProperties;

        if (processExtendedProperties == null)
        {
            processExtendedProperties = new HashMap<>();

            processExtendedProperties.put(OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier);
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

        String actionTypeName = OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME;

        if (typeName != null)
        {
            actionTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataType.WAIT_TIME_PROPERTY_NAME, waitTime, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> entityClassifications = classifications;

        if (anchorGUID != null)
        {
            if (entityClassifications == null)
            {
                entityClassifications = new ArrayList<>();
            }

            entityClassifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, methodName));
        }

        EntityDetail assetEntity = archiveHelper.getEntityDetail(actionTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);

        archiveBuilder.addEntity(assetEntity);

        addSupportedRequestParameters(assetEntity.getGUID(),
                                      actionTypeName,
                                      supportedRequestParameters);

        addSupportedActionTargets(assetEntity.getGUID(),
                                  actionTypeName,
                                  supportedActionTargets);

        addSupportedAnalysisSteps(assetEntity.getGUID(),
                                  actionTypeName,
                                  supportedAnalysisSteps);

        addSupportedAnnotationTypes(assetEntity.getGUID(),
                                    actionTypeName,
                                    supportedAnnotationTypes);

        addProducedRequestParameters(assetEntity.getGUID(),
                                     actionTypeName,
                                     producedRequestParameters);

        addProducedActionTargets(assetEntity.getGUID(),
                                 actionTypeName,
                                 producedActionTargets);

        addProducedGuards(assetEntity.getGUID(),
                          actionTypeName,
                          producedGuards);

        return assetEntity.getGUID();
    }


    /**
     * Create a governance action process step.
     *
     * @param typeName name of subtype to use - default is GovernanceActionProcessStep
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName type name of the anchor
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

        String actionTypeName = OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME;

        if (typeName != null)
        {
            actionTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataType.WAIT_TIME_PROPERTY_NAME, waitTime, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataType.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME, ignoreMultipleTriggers, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> entityClassifications = classifications;

        if (anchorGUID != null)
        {
            if (entityClassifications == null)
            {
                entityClassifications = new ArrayList<>();
            }

            entityClassifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, methodName));
        }

        EntityDetail assetEntity = archiveHelper.getEntityDetail(actionTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);

        archiveBuilder.addEntity(assetEntity);

        addSupportedRequestParameters(assetEntity.getGUID(),
                                      actionTypeName,
                                      supportedRequestParameters);

        addSupportedActionTargets(assetEntity.getGUID(),
                                  actionTypeName,
                                  supportedActionTargets);

        addSupportedAnalysisSteps(assetEntity.getGUID(),
                                  actionTypeName,
                                  supportedAnalysisSteps);

        addSupportedAnnotationTypes(assetEntity.getGUID(),
                                    actionTypeName,
                                    supportedAnnotationTypes);

        addProducedRequestParameters(assetEntity.getGUID(),
                                     actionTypeName,
                                     producedRequestParameters);

        addProducedActionTargets(assetEntity.getGUID(),
                                 actionTypeName,
                                 producedActionTargets);

        addProducedGuards(assetEntity.getGUID(),
                          actionTypeName,
                          producedGuards);

        return assetEntity.getGUID();
    }



    /**
     * Create the relationship between a governance action process and the first governance action type to execute.
     *
     * @param governanceActionProcessGUID unique identifier of the governance action process
     * @param guard initial guard for the first step in the process
     * @param governanceActionProcessStepGUID unique identifier of the implementing governance engine
     */
    public void addGovernanceActionProcessFlow(String governanceActionProcessGUID,
                                               String guard,
                                               String governanceActionProcessStepGUID)
    {
        final String methodName = "addGovernanceActionFlow";

        EntityDetail processEntity    = archiveBuilder.getEntity(governanceActionProcessGUID);
        EntityDetail actionTypeEntity = archiveBuilder.getEntity(governanceActionProcessStepGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(processEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(actionTypeEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataType.GUARD_PROPERTY_NAME, guard, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME,
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataType.GUARD_PROPERTY_NAME, guard, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME, mandatoryGuard, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(governanceActionProcessStepGUID + "_to_" + nextGovernanceActionTypeGUID + "_next_governance_action_process_step_relationship"),
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(governanceActionGUID + "_to_" + governanceEngineGUID + "_governance_action_executor_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }
}
