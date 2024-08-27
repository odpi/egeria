/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ServerAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Responsible for setting up the mechanisms that support the smooth operation of a clinical trial.
 * This includes:
 * <ul>
 *     <li>Creating a new volume in the data lake catalog (Unity Catalog).</li>
 *     <li>Optionally, linking the new asset created for the volume to the integration connector that maintains the last update date.</li>
 * </ul>
 */
public class CocoClinicalTrialSetUpDataLakeService extends GeneralGovernanceActionService
{
    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        String clinicalTrialId              = null;
        String clinicalTrialName            = null;

        String dataLakeCatalogQualifiedName = null;
        String dataLakeCatalogQualifiedGUID = null;
        String dataLakeCatalogName          = null;
        String dataLakeSchemaName           = null;
        String dataLakeSchemaDescription    = "Example clinical trial used for education and testing of governance procedures.";
        String serverNetworkAddress         = null;

        String dataLakeVolumePathName       = null;
        String dataLakeVolumeName           = "weekly_measurements";
        String dataLakeVolumeDescription    = "Weekly measurements for clinical trial";
        String schemaTemplateGUID           = "5bf92b0f-3970-41ea-b0a3-aacfbf6fd92e";
        String volumeTemplateGUID           = "92d2d2dc-0798-41f0-9512-b10548d312b7";
        String lastUpdateConnectorGUID      = null;

        String hospitalOnboardingProcessGUID = null;

        super.start();

        try
        {
            /*
             * Retrieve template information from the request parameters.
             */
            if (governanceContext.getRequestParameters() != null)
            {
                volumeTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_VOLUME_TEMPLATE.getName());
            }

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
                            clinicalTrialId = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                               OpenMetadataProperty.IDENTIFIER.name,
                                                                               actionTargetElement.getTargetElement().getElementProperties(),
                                                                               methodName);
                            clinicalTrialName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                 OpenMetadataProperty.NAME.name,
                                                                                 actionTargetElement.getTargetElement().getElementProperties(),
                                                                                 methodName);
                        }
                        else if (CocoClinicalTrialActionTarget.CATALOG.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataLakeCatalogQualifiedGUID = actionTargetElement.getTargetElement().getElementGUID();
                            dataLakeCatalogQualifiedName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                            actionTargetElement.getTargetElement().getElementProperties(),
                                                                                            methodName);

                            List<MetadataCorrelationHeader> externalIdentifiers = governanceContext.getOpenMetadataStore().getMetadataCorrelationHeaders(dataLakeCatalogQualifiedGUID,
                                                                                                                                                         dataLakeCatalogQualifiedName,
                                                                                                                                                         actionTargetElement.getTargetElement().getElementGUID(),
                                                                                                                                                         actionTargetElement.getTargetElement().getType().getTypeName());
                            if (externalIdentifiers != null)
                            {
                                for (MetadataCorrelationHeader externalIdentifier : externalIdentifiers)
                                {
                                    if (externalIdentifier != null)
                                    {
                                        if (externalIdentifier.getMappingProperties() != null)
                                        {
                                            dataLakeCatalogName = externalIdentifier.getMappingProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName());
                                            serverNetworkAddress  = externalIdentifier.getMappingProperties().get(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName());

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
                    (dataLakeCatalogQualifiedGUID == null) ||
                    (dataLakeCatalogName == null) ||
                    (serverNetworkAddress == null) ||
                    (lastUpdateConnectorGUID == null) ||
                    (hospitalOnboardingProcessGUID == null) ||
                    (dataLakeSchemaName == null) ||
                    (dataLakeVolumePathName == null) ||
                    (schemaTemplateGUID == null) || (schemaTemplateGUID.isBlank()) ||
                    (volumeTemplateGUID == null) || (volumeTemplateGUID.isBlank()))
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


                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                String schemaGUID = this.createSchema(dataLakeCatalogQualifiedGUID,
                                                      dataLakeCatalogQualifiedName,
                                                      schemaTemplateGUID,
                                                      serverNetworkAddress,
                                                      dataLakeCatalogQualifiedGUID,
                                                      dataLakeCatalogName,
                                                      dataLakeSchemaName,
                                                      dataLakeSchemaDescription);

                String volumeAssetGUID = this.createVolume(dataLakeCatalogQualifiedGUID,
                                                           dataLakeCatalogQualifiedName,
                                                           volumeTemplateGUID,
                                                           serverNetworkAddress,
                                                           dataLakeCatalogName,
                                                           schemaGUID,
                                                           dataLakeSchemaName,
                                                           dataLakeVolumeName,
                                                           dataLakeVolumeDescription + ": " + clinicalTrialId + " - " + clinicalTrialName,
                                                           dataLakeVolumePathName);

                monitorVolumeAsset(lastUpdateConnectorGUID, volumeAssetGUID, dataLakeCatalogQualifiedName);

                passOnDestinationFolder(dataLakeVolumePathName, hospitalOnboardingProcessGUID);

                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            if (messageDefinition != null)
            {
                auditLog.logMessage(methodName, messageDefinition);
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (OCFCheckedExceptionBase error)
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
     * @return guid for the new asset
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException access problem
     */
    private String createSchema(String  externalSourceGUID,
                                String  externalSourceName,
                                String  templateGUID,
                                String  serverNetworkAddress,
                                String  catalogGUID,
                                String  catalogName,
                                String  schemaName,
                                String  description) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), serverNetworkAddress);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), description);
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");

        governanceContext.getOpenMetadataStore().setExternalSourceIds(externalSourceGUID, externalSourceName);

        String schemaGUID =  governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(DeployedImplementationType.OSS_UC_SCHEMA.getAssociatedTypeName(),
                                                                                                        catalogGUID,
                                                                                                        false,
                                                                                                        null,
                                                                                                        null,
                                                                                                        templateGUID,
                                                                                                        null,
                                                                                                        placeholderProperties,
                                                                                                        catalogGUID,
                                                                                                        OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                                                        propertyHelper.addEnumProperty(null,
                                                                                                                                       OpenMetadataProperty.USE_TYPE.name,
                                                                                                                                       ServerAssetUseType.getOpenTypeName(),
                                                                                                                                       ServerAssetUseType.OWNS.getName()),
                                                                                                        true);

        governanceContext.getOpenMetadataStore().setExternalSourceIds(null, null);

        return schemaGUID;
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
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), serverNetworkAddress);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.VOLUME_NAME.getName(), volumeName);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), description);
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
        placeholderProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), dataLakePathName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getName(), "EXTERNAL");

        governanceContext.getOpenMetadataStore().setExternalSourceIds(externalSourceGUID, externalSourceName);

        String volumeGUID =  governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(DeployedImplementationType.OSS_UC_VOLUME.getAssociatedTypeName(),
                                                                                                        schemaGUID,
                                                                                                        false,
                                                                                                        null,
                                                                                                        null,
                                                                                                        templateGUID,
                                                                                                        null,
                                                                                                        placeholderProperties,
                                                                                                        schemaGUID,
                                                                                                        OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeName,
                                                                                                        null,
                                                                                                        false);

        governanceContext.getOpenMetadataStore().setExternalSourceIds(null, null);

        this.provisionVolume(dataLakePathName, volumeGUID);

        return volumeGUID;
    }


    /**
     * Create the landing area folder.
     *
     * @param pathName landing area folder name
     */
    private void provisionVolume(String pathName,
                                 String volumeGUID)
    {
        final String methodName = "provisionVolume";

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
        if (integrationConnectorGUID != null)
        {
            CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

            catalogTargetProperties.setMetadataSourceQualifiedName(dataLakeCatalogQualifiedName);

            catalogTargetProperties.setCatalogTargetName("dataFolder");

            governanceContext.addCatalogTarget(integrationConnectorGUID,
                                               volumeAssetGUID,
                                               catalogTargetProperties);
        }
    }


    /**
     * Add the volume folder to the hospital onboarding process's request parameters.
     *
     * @param destinationDirectory directory for the volume (and where the files go.
     * @param hospitalOnboardingProcessGUID unique identifier of the hospital onboarding process.
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException access problem
     */
    private void passOnDestinationFolder(String destinationDirectory,
                                         String hospitalOnboardingProcessGUID) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "passOnDestinationFolder";

        RelatedMetadataElement processFlowRelationship = governanceContext.getOpenMetadataStore().getRelatedMetadataElement(hospitalOnboardingProcessGUID,
                                                                                                                            1,
                                                                                                                            OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME,
                                                                                                                            new Date());

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

            requestParameters.put(MoveCopyFileRequestParameter.DESTINATION_DIRECTORY.getName(), destinationDirectory);

            governanceContext.getOpenMetadataStore().updateRelatedElementsInStore(processFlowRelationship.getRelationshipGUID(),
                                                                                  false,
                                                                                  propertyHelper.addStringMapProperty(null,
                                                                                                                      OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                                                      requestParameters));
        }
    }
}
