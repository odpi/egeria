/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogSurveyRequestParameter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String dataLakeCatalogQualifiedName = null;
        String dataLakeCatalogQualifiedGUID = null;
        String clinicalTrialId              = null;
        String clinicalTrialName            = null;
        String ucCatalogName                = null;
        String ucSchemaName                 = null;
        String serverNetworkAddress         = null;
        String schemaGUID                   = null;
        String dataLakeRootFolderPathName   = null;
        String volumeTemplateGUID           = null;
        String lastUpdateConnectorGUID      = null;

        super.start();

        try
        {
            /**
             * Retrieve template information from the request parameters.
             */
            if (governanceContext.getRequestParameters() != null)
            {
                volumeTemplateGUID = governanceContext.getRequestParameters().get(CocoClinicalTrialRequestParameter.DATA_LAKE_FOLDER_TEMPLATE.getName());
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
                        else if (CocoClinicalTrialActionTarget.SCHEMA.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataLakeCatalogQualifiedGUID = actionTargetElement.getTargetElement().getOrigin().getHomeMetadataCollectionId();
                            dataLakeCatalogQualifiedName = actionTargetElement.getTargetElement().getOrigin().getHomeMetadataCollectionName();
                            schemaGUID = actionTargetElement.getTargetElement().getElementGUID();

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
                                            ucCatalogName = externalIdentifier.getMappingProperties().get(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName());
                                            ucSchemaName  = externalIdentifier.getMappingProperties().get(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName());
                                            serverNetworkAddress  = externalIdentifier.getMappingProperties().get(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName());

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        else if (CocoClinicalTrialActionTarget.ROOT_FOLDER.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            dataLakeRootFolderPathName = propertyHelper.getStringProperty(actionTargetElement.getActionTargetName(),
                                                                                          OpenMetadataProperty.PATH_NAME.name,
                                                                                          actionTargetElement.getTargetElement().getElementProperties(),
                                                                                          methodName);
                        }
                        else if (CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName().equals(actionTargetElement.getActionTargetName()))
                        {
                            lastUpdateConnectorGUID = actionTargetElement.getTargetElement().getElementGUID();
                        }
                    }
                }
            }

             List<String>     outputGuards = new ArrayList<>();
             CompletionStatus completionStatus;

            if ((clinicalTrialId == null) ||
                    (clinicalTrialName == null) ||
                    (dataLakeCatalogQualifiedGUID == null) ||
                    (schemaGUID == null) ||
                    (ucCatalogName == null) ||
                    (ucSchemaName == null) ||
                    (serverNetworkAddress == null) ||
                    (dataLakeRootFolderPathName == null) ||
                    (volumeTemplateGUID == null))
            {
                if (dataLakeCatalogQualifiedGUID == null)
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_CATALOG.getMessageDefinition(governanceServiceName,
                                                                                                                          dataLakeCatalogQualifiedName));
                }
                if ((clinicalTrialId == null) || (clinicalTrialId.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_ID.getName()));
                }
                if ((clinicalTrialName == null) || (clinicalTrialName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialPlaceholderProperty.CLINICAL_TRIAL_NAME.getName()));
                }
                if ((ucCatalogName == null) || (ucCatalogName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()));
                }
                if ((ucSchemaName == null) || (ucSchemaName.isBlank()))
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        UnityCatalogPlaceholderProperty.CATALOG_NAME.getName()));
                }
                if (schemaGUID == null)
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.SCHEMA.getName()));
                }
                if (volumeTemplateGUID == null)
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.VOLUME_TEMPLATE.getName()));
                }
                if (dataLakeRootFolderPathName == null)
                {
                    auditLog.logMessage(methodName, GovernanceActionSamplesAuditCode.MISSING_VALUE.getMessageDefinition(governanceServiceName,
                                                                                                                        CocoClinicalTrialActionTarget.ROOT_FOLDER.getName()));
                }

                completionStatus = CocoClinicalTrialGuard.MISSING_INFO.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.MISSING_INFO.getName());
            }
            else
            {
                String volumeAssetGUID = this.createVolume(dataLakeCatalogQualifiedGUID,
                                                           dataLakeCatalogQualifiedName,
                                                           volumeTemplateGUID,
                                                           serverNetworkAddress,
                                                           ucCatalogName,
                                                           schemaGUID,
                                                           ucSchemaName,
                                                           "weekly_measurements",
                                                           "Weekly measurements for clinical trial " + clinicalTrialId + " - " + clinicalTrialName,
                                                           dataLakeRootFolderPathName);

                monitorVolumeAsset(lastUpdateConnectorGUID,
                                   volumeAssetGUID);

                completionStatus = CocoClinicalTrialGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CocoClinicalTrialGuard.SET_UP_COMPLETE.getName());
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards);
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
     * @param dataLakeRootPathName data lake's location
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
                                String  dataLakeRootPathName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        String volumePathName = dataLakeRootPathName + "/" + catalogName + "/" + schemaName + "/" + volumeName;

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), serverNetworkAddress);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.VOLUME_NAME.getName(), volumeName);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), description);
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
        placeholderProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), volumePathName);
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

        this.provisionVolume(volumePathName, volumeGUID);

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

        if (! volumeDirectory.mkdir())
        {
            auditLog.logMessage(methodName,
                                GovernanceActionSamplesAuditCode.NO_VOLUME_DIRECTORY.getMessageDefinition(governanceServiceName,
                                                                                                          pathName,
                                                                                                          volumeGUID));
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
                                    String volumeAssetGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        if (integrationConnectorGUID != null)
        {
            CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

            catalogTargetProperties.setCatalogTargetName("dataFolder");

            governanceContext.addCatalogTarget(integrationConnectorGUID,
                                               volumeAssetGUID,
                                               catalogTargetProperties);
        }
    }
}
