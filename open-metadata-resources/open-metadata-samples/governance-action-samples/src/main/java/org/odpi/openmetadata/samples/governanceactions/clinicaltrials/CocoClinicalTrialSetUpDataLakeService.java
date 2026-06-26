/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata.ClinicalTrialSolutionComponent;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The data lake is unity catalog and delta lake.  It sits inside the data hub.  It is used to store the raw data
 * collected from the clinical trial.  The data lake is also used to store the processed data used for
 * analysis and calculations.
 *
 * This service is responsible for setting up the mechanisms in the data lake that support the smooth operation of a clinical trial.
 * This includes:
 * <ul>
 *     <li>Creating a new volume in the data lake catalog (Unity Catalog).</li>
 *     <li>Optionally, linking the new asset created for the volume to the integration connector that maintains the last update date.</li>
 * </ul>
 */
public class CocoClinicalTrialSetUpDataLakeService extends CocoClinicalTrialBaseService
{
    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        String clinicalTrialId              = null;
        String clinicalTrialName            = null;
        String topLevelProjectGUID          = null;

        String dataLakeCatalogQualifiedName = null;
        String dataLakeCatalogQualifiedGUID = null;
        String dataLakeCatalogName          = null;
        String dataLakeSchemaName           = null;
        String dataLakeSchemaDescription    = "Example clinical trial used for education and testing of governance procedures.";
        String serverNetworkAddress         = null;

        String dataQualityCertificationTypeGUID = null;

        String landingAreaDirectoryTemplateGUID = null;
        String landingAreaFileTemplateGUID      = null;
        String dataLakeFileTemplateGUID         = null;

        String dataLakeVolumePathName           = null;
        String dataLakeVolumeName               = "weekly_measurements";
        String dataLakeVolumeDescription        = "Weekly measurements for clinical trial";
        String schemaTemplateGUID               = "5bf92b0f-3970-41ea-b0a3-aacfbf6fd92e";
        String volumeTemplateGUID               = "92d2d2dc-0798-41f0-9512-b10548d312b7";
        String lastUpdateConnectorGUID          = null;
        String airflowDAGName                   = null;
        String validatedFilesDataSetName        = null;
        String validatedWeeklyFilesTemplateGUID = null;
        String informationSupplyChainGUID       = null;

        String hospitalOnboardingProcessGUID    = null;
        String genericOnboardingPipelineGUID    = null;

        super.start();

        try
        {
            /*
             * Retrieve the values needed from the action targets.
             */
             if (governanceContext.getActionTargetElements() != null)
             {
                for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
                {
                    if (actionTargetElement != null)
                    {
                        if (CocoClinicalTrialActionTarget.PROJECT.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            topLevelProjectGUID = actionTargetElement.getTargetElement().getElementGUID();

                            clinicalTrialId = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                               OpenMetadataProperty.IDENTIFIER.name,
                                                                               actionTargetElement.getTargetElement().getElementProperties(),
                                                                               methodName);
                            clinicalTrialName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                 OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                 actionTargetElement.getTargetElement().getElementProperties(),
                                                                                 methodName);
                        }
                        else if (CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataQualityCertificationTypeGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.CATALOG.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataLakeCatalogQualifiedGUID = actionTargetElement.getTargetElement().getElementGUID();
                            dataLakeCatalogQualifiedName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                            actionTargetElement.getTargetElement().getElementProperties(),
                                                                                            methodName);
                            String dataLakeMetadataCollectionId = actionTargetElement.getTargetElement().getOrigin().getHomeMetadataCollectionId();

                            List<OpenMetadataRootElement> externalIdentifiers = governanceContext.getExternalIdClient().getExternalIdsForElement(dataLakeCatalogQualifiedGUID,
                                                                                                                                                 null);
                            if (externalIdentifiers != null)
                            {
                                for (OpenMetadataRootElement externalIdentifier : externalIdentifiers)
                                {
                                    if ((externalIdentifier != null) &&
                                            (externalIdentifier.getElementHeader().getOrigin().getHomeMetadataCollectionId().equals(dataLakeMetadataCollectionId)) &&
                                            (externalIdentifier.getRelatedBy() != null) &&
                                            (externalIdentifier.getRelatedBy().getRelationshipProperties() instanceof ExternalIdLinkProperties externalIdLinkProperties))
                                    {
                                        if (externalIdLinkProperties.getMappingProperties() != null)
                                        {
                                            dataLakeCatalogName = externalIdLinkProperties.getMappingProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName());
                                            serverNetworkAddress  = externalIdLinkProperties.getMappingProperties().get(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName());

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        else if (CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            lastUpdateConnectorGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.ONBOARD_HOSPITAL_PROCESS.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            hospitalOnboardingProcessGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.INFORMATION_SUPPLY_CHAIN.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            informationSupplyChainGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                        else if (CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            genericOnboardingPipelineGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
             }

            /*
             * Retrieve the data lake information from the request parameters
             * MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getName()
             */
            if (governanceContext.getRequestParameters() != null)
            {
                schemaTemplateGUID     = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_SCHEMA_TEMPLATE.getName());
                volumeTemplateGUID     = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_VOLUME_TEMPLATE.getName());

                landingAreaDirectoryTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_TEMPLATE.getName());
                landingAreaFileTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.LANDING_AREA_FILE_TEMPLATE.getName());
                dataLakeFileTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_FILE_TEMPLATE.getName());

                validatedFilesDataSetName        = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.VALIDATED_WEEKLY_MEASUREMENT_FILES_DATA_SET_NAME.getName());
                validatedWeeklyFilesTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.VALIDATED_DATA_FILES_COLLECTION_TEMPLATE.getName());

                airflowDAGName            = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.AIRFLOW_DAG_NAME.getName());

                dataLakeSchemaName        = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_SCHEMA_NAME.getName());
                dataLakeSchemaDescription = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_SCHEMA_DESCRIPTION.getName());
                dataLakeVolumePathName    = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_VOLUME_PATH_NAME.getName());
                dataLakeVolumeName        = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_VOLUME_NAME.getName());
                dataLakeVolumeDescription = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_VOLUME_DESCRIPTION.getName());
            }

            List<String>              outputGuards = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition = null;

            if ((clinicalTrialId == null) ||
                    (clinicalTrialName == null) ||
                    (dataQualityCertificationTypeGUID == null) ||
                    (airflowDAGName == null) ||
                    (validatedFilesDataSetName == null) ||
                    (validatedWeeklyFilesTemplateGUID == null) ||
                    (dataLakeCatalogQualifiedGUID == null) ||
                    (dataLakeCatalogName == null) ||
                    (serverNetworkAddress == null) ||
                    (lastUpdateConnectorGUID == null) ||
                    (hospitalOnboardingProcessGUID == null) ||
                    (informationSupplyChainGUID == null) ||
                    (dataLakeSchemaName == null) ||
                    (dataLakeVolumePathName == null) ||
                    (schemaTemplateGUID == null) || (schemaTemplateGUID.isBlank()) ||
                    (volumeTemplateGUID == null) || (volumeTemplateGUID.isBlank()) ||
                    landingAreaDirectoryTemplateGUID == null || landingAreaDirectoryTemplateGUID.isBlank() ||
                    landingAreaFileTemplateGUID == null || landingAreaFileTemplateGUID.isBlank() ||
                    dataLakeFileTemplateGUID == null || dataLakeFileTemplateGUID.isBlank() ||
                    genericOnboardingPipelineGUID == null)
            {
                if (dataLakeCatalogQualifiedGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_CATALOG.getMessageDefinition(governanceServiceName, dataLakeCatalogQualifiedName);
                }
                if ((clinicalTrialId == null) || (clinicalTrialId.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName());
                }
                if ((clinicalTrialName == null) || (clinicalTrialName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName());
                }
                if ((dataQualityCertificationTypeGUID == null) || (dataQualityCertificationTypeGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName());
                }
                if ((airflowDAGName == null) || (airflowDAGName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.AIRFLOW_DAG_NAME.getName());
                }
                if ((validatedFilesDataSetName == null) || (validatedFilesDataSetName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.VALIDATED_WEEKLY_MEASUREMENT_FILES_DATA_SET_NAME.getName());
                }
                if ((validatedWeeklyFilesTemplateGUID == null) || (validatedWeeklyFilesTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.VALIDATED_DATA_FILES_COLLECTION_TEMPLATE.getName());
                }
                if ((dataLakeCatalogName == null) || (dataLakeCatalogName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            UnityCatalogPlaceholderProperty.CATALOG_NAME.getName());
                }
                if ((dataLakeSchemaName == null) || (dataLakeSchemaName.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.DATA_LAKE_SCHEMA_NAME.getName());
                }
                if (volumeTemplateGUID == null || volumeTemplateGUID.isBlank())
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.DATA_LAKE_VOLUME_TEMPLATE.getName());
                }
                if (schemaTemplateGUID == null || schemaTemplateGUID.isBlank())
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.DATA_LAKE_SCHEMA_TEMPLATE.getName());
                }
                if (dataLakeVolumePathName == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.DATA_LAKE_VOLUME_PATH_NAME.getName());
                }
                if (lastUpdateConnectorGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName());
                }
                if (hospitalOnboardingProcessGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.ONBOARD_HOSPITAL_PROCESS.getName());
                }
                if (informationSupplyChainGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.INFORMATION_SUPPLY_CHAIN.getName());
                }
                else if ((landingAreaDirectoryTemplateGUID == null) || (landingAreaDirectoryTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_TEMPLATE.getName());
                }
                else if ((landingAreaFileTemplateGUID == null) || (landingAreaFileTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.LANDING_AREA_FILE_TEMPLATE.getName());
                }
                else if ((dataLakeFileTemplateGUID == null) || (dataLakeFileTemplateGUID.isBlank()))
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialRequestParameter.DATA_LAKE_FILE_TEMPLATE.getName());
                }
                else if (genericOnboardingPipelineGUID == null)
                {
                    messageDefinition = GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                            CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName());
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                String informationSupplyChainQualifiedName = super.getInformationSupplyChainQualifiedName(informationSupplyChainGUID);

                /*
                 * Create a schema in Unity Catalog
                 */
                String ucSchemaGUID = this.createUCSchema(dataLakeCatalogQualifiedGUID,
                                                          dataLakeCatalogQualifiedName,
                                                          schemaTemplateGUID,
                                                          serverNetworkAddress,
                                                          dataLakeCatalogQualifiedGUID,
                                                          dataLakeCatalogName,
                                                          dataLakeSchemaName,
                                                          dataLakeSchemaDescription,
                                                          topLevelProjectGUID);

                /*
                 * Create a volume in Unity Catalog
                 */
                String ucVolumeAssetGUID = this.createVolume(dataLakeCatalogQualifiedGUID,
                                                             dataLakeCatalogQualifiedName,
                                                             volumeTemplateGUID,
                                                             serverNetworkAddress,
                                                             dataLakeCatalogName,
                                                             ucSchemaGUID,
                                                             dataLakeSchemaName,
                                                             dataLakeVolumeName,
                                                             dataLakeVolumeDescription + ": " + clinicalTrialId + " - " + clinicalTrialName,
                                                             dataLakeVolumePathName);

                String validatedFilesDataSetGUID = this.createValidatedFilesDataSet(validatedFilesDataSetName, validatedWeeklyFilesTemplateGUID, topLevelProjectGUID);
                addActionTargetToProcess(hospitalOnboardingProcessGUID, CocoClinicalTrialActionTarget.VALIDATED_WEEKLY_FILES_DATA_SET.getName(), validatedFilesDataSetGUID);
                addActionTargetToProcess(hospitalOnboardingProcessGUID, CocoClinicalTrialActionTarget.GENERIC_ONBOARDING_PIPELINE.getName(), genericOnboardingPipelineGUID);

                /*
                 * Link the validated data set to the data lake folder solution component.
                 */
                addSolutionComponentImplementedByRelationship(ClinicalTrialSolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER.getGUID(), validatedFilesDataSetGUID, informationSupplyChainQualifiedName, "Supports clinical trial " + clinicalTrialId);

                governanceContext.createLineageRelationship(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                                                            validatedFilesDataSetGUID,
                                                            informationSupplyChainQualifiedName,
                                                            "get files from storage",
                                                            "Validated files are retrieved from the Unity Catalog Volume.",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            ucVolumeAssetGUID);

                monitorVolumeAsset(lastUpdateConnectorGUID, ucVolumeAssetGUID, dataLakeCatalogQualifiedName);

                passOnOnboardingParameters(dataLakeVolumePathName,
                                           landingAreaDirectoryTemplateGUID,
                                           landingAreaFileTemplateGUID,
                                           dataLakeFileTemplateGUID,
                                           hospitalOnboardingProcessGUID);

                /*
                 * Create the database schema for the sandbox
                 */
                String sandboxSchemaGUID = createSandboxSchema(topLevelProjectGUID);

                /*
                 * Link the sandbox schema to the sandbox solution component
                 */
                addSolutionComponentImplementedByRelationship(ClinicalTrialSolutionComponent.TREATMENT_VALIDATION_SANDBOX.getGUID(), sandboxSchemaGUID, informationSupplyChainQualifiedName, "Supports clinical trial " + clinicalTrialId);

                /*
                 * Indicate that the sandbox is governed by the data quality certification.
                 */
                addGovernedByRelationship(dataQualityCertificationTypeGUID, sandboxSchemaGUID);

                /*
                 * Create a process to represent the Airflow DAG
                 */
                String populateSandboxGUID = createPopulateSandboxDAG(airflowDAGName, topLevelProjectGUID);

                /*
                 * Link the airflow DAG to the solution component
                 */
                addSolutionComponentImplementedByRelationship(ClinicalTrialSolutionComponent.POPULATE_SANDBOX.getGUID(), populateSandboxGUID, informationSupplyChainQualifiedName, "Supports clinical trial " + clinicalTrialId);

                governanceContext.createLineageRelationship(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                            validatedFilesDataSetGUID,
                                                            informationSupplyChainQualifiedName,
                                                            "Copy Certified Data",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            populateSandboxGUID);

                governanceContext.createLineageRelationship(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                            populateSandboxGUID,
                                                            informationSupplyChainQualifiedName,
                                                            "Save Certified Data",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            sandboxSchemaGUID);

                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            if (messageDefinition != null)
            {
                auditLog.logMessage(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (OMFCheckedExceptionBase error)
        {
            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(GovernanceActionSamplesErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Catalog the schema in the same metadata collection as the catalog.
     * The new asset is linked to the catalog.
     *
     * @param externalSourceGUID unique identifier of the metadata collection for the volume's asset
     * @param externalSourceName unique name of the metadata collection for the volume's asset
     * @param templateGUID template to use to create the asset
     * @param serverNetworkAddress address of the server where the volume can be accessed from
     * @param catalogName name of the catalog
     * @param catalogGUID unique identifier of the catalog
     * @param schemaName short name of the schema
     * @param description description of the schema
     * @param topLevelProjectGUID unique identifier for the top level project - used as a search scope
     * @return guid for the new asset
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException access problem
     */
    private String createUCSchema(String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  templateGUID,
                                  String  serverNetworkAddress,
                                  String  catalogGUID,
                                  String  catalogName,
                                  String  schemaName,
                                  String  description,
                                  String  topLevelProjectGUID) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "createDataLake";
        final String parameterName = "externalSourceGUID";
        final String parameterName2 = "topLevelProjectGUID";
        final String parameterName3 = "catalogGUID";
        final String parameterName4 = "catalogName";
        final String parameterName5 = "schemaName";
        final String parameterName6 = "description";
        final String parameterName7 = "templateGUID";
        final String parameterName8 = "serverNetworkAddress";
        final String parameterName9 = "externalSourceName";

        propertyHelper.validateGUID(externalSourceGUID, parameterName, methodName);
        propertyHelper.validateGUID(topLevelProjectGUID, parameterName2, methodName);
        propertyHelper.validateGUID(catalogGUID, parameterName3, methodName);
        propertyHelper.validateMandatoryName(catalogName, parameterName4, methodName);
        propertyHelper.validateMandatoryName(schemaName, parameterName5, methodName);
        propertyHelper.validateMandatoryName(description, parameterName6, methodName);
        propertyHelper.validateGUID(templateGUID, parameterName7, methodName);
        propertyHelper.validateMandatoryName(serverNetworkAddress, parameterName8, methodName);
        propertyHelper.validateMandatoryName(externalSourceName, parameterName9, methodName);

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), serverNetworkAddress);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), description);
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");

        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

        TemplateOptions templateOptions = new TemplateOptions(openMetadataStore.getMetadataSourceOptions());

        templateOptions.setExternalSourceGUID(externalSourceGUID);
        templateOptions.setExternalSourceName(externalSourceName);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorGUID(catalogGUID);
        templateOptions.setAnchorScopeGUIDs(Collections.singletonList(topLevelProjectGUID));
        templateOptions.setAllowRetrieve(true);
        templateOptions.setParentGUID(catalogGUID);
        templateOptions.setParentAtEnd1(true);
        templateOptions.setParentRelationshipTypeName(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName);

        return openMetadataStore.createMetadataElementFromTemplate(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getAssociatedTypeName(),
                                                                   templateOptions,
                                                                   templateGUID,
                                                                   null,
                                                                   null,
                                                                   placeholderProperties,
                                                                   new NewElementProperties(propertyHelper.addEnumProperty(null,
                                                                                                                           OpenMetadataProperty.USE_TYPE.name,
                                                                                                                           CapabilityAssetUseType.getOpenTypeName(),
                                                                                                                           CapabilityAssetUseType.OWNS.name())));

    }


    /**
     * Catalog the volume in the same metadata collection as the schema.
     * The new asset is linked to the schema.
     *
     * @param externalSourceGUID unique identifier of the metadata collection for the volume's asset
     * @param externalSourceName unique name of the metadata collection for the volume's asset
     * @param templateGUID template to use to create the asset
     * @param serverNetworkAddress address of the server where the volume can be accessed from
     * @param catalogName name of the catalog
     * @param schemaGUID unique identifier of the schema element
     * @param schemaName short name of the schema
     * @param volumeName short name of the volume
     * @param description description of the volume
     * @param dataLakePathName data lake's location
     * @return guid for the new asset
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException access problem
     */
    private String createVolume(String  externalSourceGUID,
                                String  externalSourceName,
                                String  templateGUID,
                                String  serverNetworkAddress,
                                String  catalogName,
                                String  schemaGUID,
                                String  schemaName,
                                String  volumeName,
                                String  description,
                                String  dataLakePathName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "createVolume";
        final String parameterName = "externalSourceGUID";
        final String parameterName2 = "externalSourceName";
        final String parameterName3 = "templateGUID";
        final String parameterName4 = "serverNetworkAddress";
        final String parameterName5 = "catalogName";
        final String parameterName6 = "schemaGUID";
        final String parameterName7 = "schemaName";
        final String parameterName8 = "volumeName";
        final String parameterName9 = "description";
        final String parameterName10 = "dataLakePathName";

        propertyHelper.validateGUID(externalSourceGUID, parameterName, methodName);
        propertyHelper.validateMandatoryName(externalSourceName, parameterName2, methodName);
        propertyHelper.validateGUID(templateGUID, parameterName3, methodName);
        propertyHelper.validateMandatoryName(serverNetworkAddress, parameterName4, methodName);
        propertyHelper.validateMandatoryName(catalogName, parameterName5, methodName);
        propertyHelper.validateGUID(schemaGUID, parameterName6, methodName);
        propertyHelper.validateMandatoryName(schemaName, parameterName7, methodName);
        propertyHelper.validateMandatoryName(volumeName, parameterName8, methodName);
        propertyHelper.validateMandatoryName(description, parameterName9, methodName);
        propertyHelper.validateMandatoryName(dataLakePathName, parameterName10, methodName);

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), serverNetworkAddress);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.VOLUME_NAME.getName(), volumeName);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), description);
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
        placeholderProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), dataLakePathName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getName(), "EXTERNAL");

        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

        TemplateOptions templateOptions = new TemplateOptions(openMetadataStore.getMetadataSourceOptions());

        templateOptions.setExternalSourceGUID(externalSourceGUID);
        templateOptions.setExternalSourceName(externalSourceName);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorGUID(schemaGUID);
        templateOptions.setAnchorScopeGUIDs(null);
        templateOptions.setAllowRetrieve(true);
        templateOptions.setParentGUID(schemaGUID);
        templateOptions.setParentAtEnd1(true);
        templateOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

        String volumeGUID =  governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getAssociatedTypeName(),
                                                                                                        templateOptions,
                                                                                                        templateGUID,
                                                                                                        null,
                                                                                                        null,
                                                                                                        placeholderProperties,
                                                                                                        null);


        this.provisionVolume(dataLakePathName, volumeGUID);

        return volumeGUID;
    }



    /**
     * Create a data set asset for the list of validated weekly measurements files.  This list is populated by the
     * data quality measurement governance action.
     *
     * @param validatedFilesDataSetName name of new data set
     * @param validatedWeeklyFilesTemplateGUID template for data set
     * @param topLevelProjectGUID unique identifier for the top level project - used as a search scope
     * @return guid for the new asset
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException access problem
     */
    private String createValidatedFilesDataSet(String validatedFilesDataSetName,
                                               String validatedWeeklyFilesTemplateGUID,
                                               String topLevelProjectGUID) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createValidatedFilesDataSet";
        final String parameterName = "validatedFilesDataSetName";
        final String parameterName2 = "validatedWeeklyFilesTemplateGUID";
        final String parameterName3 = "topLevelProjectGUID";

        propertyHelper.validateMandatoryName(validatedFilesDataSetName, parameterName, methodName);
        propertyHelper.validateGUID(validatedWeeklyFilesTemplateGUID, parameterName2, methodName);
        propertyHelper.validateGUID(topLevelProjectGUID, parameterName3, methodName);

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.DISPLAY_NAME.getName(), validatedFilesDataSetName);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), CocoClinicalTrialActionTarget.VALIDATED_WEEKLY_FILES_DATA_SET.getDescription());
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
        placeholderProperties.put(PlaceholderProperty.FORMULA.getName(), "Certified file");
        placeholderProperties.put(PlaceholderProperty.FORMULA_TYPE.getName(), "Java Code");

        return governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(OpenMetadataType.DATA_FILE_COLLECTION.typeName,
                                                                                       null,
                                                                                       true,
                                                                                       Collections.singletonList(topLevelProjectGUID),
                                                                                       null,
                                                                                       null,
                                                                                       validatedWeeklyFilesTemplateGUID,
                                                                                       null,
                                                                                       null,
                                                                                       placeholderProperties,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       true);
    }


    /**
     * Create the landing area folder.
     *
     * @param pathName landing area folder name
     * @param volumeGUID unique identifier of the volume
     * @throws InvalidParameterException the parameters are invalid
     */
    private void provisionVolume(String pathName,
                                 String volumeGUID) throws InvalidParameterException
    {
        final String methodName = "provisionVolume";
        final String parameterName = "pathName";
        final String parameterName2 = "volumeGUID";

        propertyHelper.validateMandatoryName(pathName, parameterName, methodName);
        propertyHelper.validateGUID(volumeGUID, parameterName2, methodName);

        File volumeDirectory = new File(pathName);

        if (! volumeDirectory.exists())
        {
            try
            {
                FileUtils.forceMkdir(volumeDirectory);
            }
            catch (IOException error)
            {
                auditLog.logMessage(methodName,
                                    GovernanceActionSamplesAuditCode.NO_VOLUME_DIRECTORY.getMessageDefinition(governanceServiceName,
                                                                                                              pathName,
                                                                                                              volumeGUID));
            }
        }
    }


    /**
     * If an integration connector that can maintain the last update date in the volume, then attach
     * the asset to the connector as a catalog target.
     *
     * @param integrationConnectorGUID unique identifier of the connector (maybe null)
     * @param volumeAssetGUID unique identifier of the volume's asset
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException access problem
     */
    private void monitorVolumeAsset(String integrationConnectorGUID,
                                    String volumeAssetGUID,
                                    String dataLakeCatalogQualifiedName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "monitorVolumeAsset";
        final String parameterName = "integrationConnectorGUID";
        final String parameterName2 = "volumeAssetGUID";
        final String parameterName3 = "dataLakeCatalogQualifiedName";

        propertyHelper.validateGUID(integrationConnectorGUID, parameterName, methodName);
        propertyHelper.validateGUID(volumeAssetGUID, parameterName2, methodName);
        propertyHelper.validateMandatoryName(dataLakeCatalogQualifiedName, parameterName3, methodName);

        if (integrationConnectorGUID != null)
        {
            CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

            catalogTargetProperties.setMetadataCollectionQualifiedName(dataLakeCatalogQualifiedName);
            catalogTargetProperties.setMetadataSourceQualifiedName(dataLakeCatalogQualifiedName);

            catalogTargetProperties.setCatalogTargetName("dataFolder");

            governanceContext.getAssetClient().addCatalogTarget(integrationConnectorGUID,
                                                                volumeAssetGUID,
                                                                new MakeAnchorOptions(governanceContext.getAssetClient().getMetadataSourceOptions()),
                                                                catalogTargetProperties);
        }
    }


    /**
     * Add the volume folder to the hospital onboarding process's request parameters.
     *
     * @param destinationDirectory directory for the volume (and where the files go)
     * @param landingAreaDirectoryTemplateGUID template for the landing area directory
     * @param landingAreaFileTemplateGUID  template for a landing area file
     * @param dataLakeFileTemplateGUID template for a data lake file
     * @param hospitalOnboardingProcessGUID unique identifier of the hospital onboarding process.
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException access problem
     */
    private void passOnOnboardingParameters(String destinationDirectory,
                                            String landingAreaDirectoryTemplateGUID,
                                            String landingAreaFileTemplateGUID,
                                            String dataLakeFileTemplateGUID,
                                            String hospitalOnboardingProcessGUID) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "passOnDestinationFolder";
        final String parameterName = "destinationDirectory";
        final String parameterName2 = "landingAreaDirectoryTemplateGUID";
        final String parameterName3 = "landingAreaFileTemplateGUID";
        final String parameterName4 = "dataLakeFileTemplateGUID";
        final String parameterName5 = "hospitalOnboardingProcessGUID";

        propertyHelper.validateMandatoryName(destinationDirectory, parameterName, methodName);
        propertyHelper.validateGUID(landingAreaDirectoryTemplateGUID, parameterName2, methodName);
        propertyHelper.validateGUID(landingAreaFileTemplateGUID, parameterName3, methodName);
        propertyHelper.validateGUID(dataLakeFileTemplateGUID, parameterName4, methodName);
        propertyHelper.validateGUID(hospitalOnboardingProcessGUID, parameterName5, methodName);

        RelatedMetadataElement processFlowRelationship = governanceContext.getOpenMetadataStore().getRelatedMetadataElement(hospitalOnboardingProcessGUID,
                                                                                                                            1,
                                                                                                                            OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                                                                            null);

        if (processFlowRelationship != null)
        {
            Map<String, String> requestParameters = propertyHelper.getStringMapFromProperty(governanceServiceName,
                                                                                            OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                            processFlowRelationship.getRelationshipProperties(),
                                                                                            methodName);

            if (requestParameters == null)
            {
                requestParameters = new HashMap<>();
            }

            requestParameters.put(CocoClinicalTrialRequestParameter.LANDING_AREA_DIRECTORY_TEMPLATE.getName(), landingAreaDirectoryTemplateGUID);
            requestParameters.put(CocoClinicalTrialRequestParameter.LANDING_AREA_FILE_TEMPLATE.getName(), landingAreaFileTemplateGUID);
            requestParameters.put(CocoClinicalTrialRequestParameter.DATA_LAKE_FILE_TEMPLATE.getName(), dataLakeFileTemplateGUID);
            requestParameters.put(MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getName(), destinationDirectory);

            governanceContext.getOpenMetadataStore().updateRelatedElementsInStore(processFlowRelationship.getRelationshipGUID(),
                                                                                  governanceContext.getOpenMetadataStore().getUpdateOptions(false),
                                                                                  propertyHelper.addStringMapProperty(null,
                                                                                                                      OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                                                      requestParameters));
        }
    }
}
