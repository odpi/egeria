/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.OriginSeekerGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.ZonePublisherGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericFolderWatchdogGovernanceActionProvider;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.GuardType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameterType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStepType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.combo.CocoBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceProgramArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.ProjectDefinition;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CocoGovernanceEnginesArchiveWriter creates a physical open metadata archive file containing the governance engine definitions
 * needed by Coco Pharmaceuticals.
 */
public class CocoGovernanceEnginesArchiveWriter extends CocoBaseArchiveWriter
{
    private static final String archiveFileName = "CocoGovernanceEngineDefinitionsArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9cbd2b33-e80f-4df2-adc6-d859ebff4c34";
    private static final String                  archiveName        = "CocoGovernanceEngineDefinitions";
    private static final String                  archiveDescription = "Governance Engines for Coco Pharmaceuticals.";


    /**
     * Default constructor initializes the archive.
     */
    public CocoGovernanceEnginesArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              new Date(),
              archiveFileName,
              new OpenMetadataArchive[]{ new CocoGovernanceProgramArchiveWriter().getOpenMetadataArchive() });
    }



    /**
     * Create an entity for the AssetSurvey governance engine.
     *
     * @param clinicalTrialsEngineName name
     * @return unique identifier for the governance engine
     */
    private String getClinicalTrialsEngine(String clinicalTrialsEngineName)
    {
        final String engineDisplayName = "Clinical Trials Engine";
        final String engineDescription = "Manages the set up and operation of clinical trials at Coco Pharmaceuticals.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                 clinicalTrialsEngineName,
                                                 engineDisplayName,
                                                 engineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }


    /**
     * Create an entity for the AssetGovernance governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getAssetGovernanceEngine()
    {
        final String assetGovernanceEngineName        = "AssetGovernance@CocoPharmaceuticals";
        final String assetGovernanceEngineDisplayName = "Asset Governance Governance Action Engine";
        final String assetGovernanceEngineDescription = "Monitors, validates and enriches metadata relating to assets.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                 assetGovernanceEngineName,
                                                 assetGovernanceEngineDisplayName,
                                                 assetGovernanceEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }


    /**
     * Create an entity for the AssetDiscovery governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getAssetDiscoveryEngine()
    {
        final String assetDiscoveryEngineName        = "AssetDiscovery@CocoPharmaceuticals";
        final String assetDiscoveryEngineDisplayName = "Asset Discovery Survey Action Engine";
        final String assetDiscoveryEngineDescription = "Extracts metadata about a digital resource and attach it to its asset description.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                                                 assetDiscoveryEngineName,
                                                 assetDiscoveryEngineDisplayName,
                                                 assetDiscoveryEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }


    /**
     * Create an entity for the AssetQuality governance engine.
     *
     * @return unique identifier for the governance engine
     */
    private String getAssetQualityEngine()
    {
        final String assetQualityEngineName        = "AssetQuality@CocoPharmaceuticals";
        final String assetQualityEngineDisplayName = "Asset Quality Survey Action Engine";
        final String assetQualityEngineDescription = "Assess the quality of a digital resource identified by the asset in the request.";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                                                 assetQualityEngineName,
                                                 assetQualityEngineDisplayName,
                                                 assetQualityEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null);
    }


    /**
     * GovernanceActionDescription provides details for calling a governance service.
     */
    static class GovernanceActionDescription
    {
        String                     governanceServiceGUID        = null;
        String                     governanceServiceDescription = null;
        List<RequestTypeType>      supportedRequestTypes        = null;
        List<RequestParameterType> supportedRequestParameters   = null;
        List<ActionTargetType>     supportedActionTargets       = null;
        List<AnalysisStepType>     supportedAnalysisSteps       = null;
        List<AnnotationTypeType>   supportedAnnotationTypes     = null;
        List<RequestParameterType> producedRequestParameters    = null;
        List<ActionTargetType>     producedActionTargets        = null;
        List<GuardType>            producedGuards               = null;
        ResourceUse                resourceUse                  = null;
    }



    /**
     * Create a governance action description from the governance service's provider.
     *
     * @param resourceUse how is this
     * @param provider connector provider
     * @return governance action description
     */
    private GovernanceActionDescription getGovernanceActionDescription(ResourceUse                   resourceUse,
                                                                       GovernanceServiceProviderBase provider,
                                                                       String                        governanceServiceDescription)
    {
        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.resourceUse                  = resourceUse;
        governanceActionDescription.supportedRequestTypes        = provider.getSupportedRequestTypes();
        governanceActionDescription.supportedRequestParameters   = provider.getSupportedRequestParameters();
        governanceActionDescription.supportedActionTargets       = provider.getSupportedActionTargetTypes();
        governanceActionDescription.producedRequestParameters    = provider.getProducedRequestParameters();
        governanceActionDescription.producedActionTargets        = provider.getProducedActionTargetTypes();
        governanceActionDescription.producedGuards               = provider.getProducedGuards();

        if (provider instanceof SurveyActionServiceProvider surveyActionServiceProvider)
        {
            governanceActionDescription.supportedAnalysisSteps = surveyActionServiceProvider.getSupportedAnalysisSteps();
            governanceActionDescription.supportedAnnotationTypes = surveyActionServiceProvider.getProducedAnnotationTypes();
        }

        governanceActionDescription.governanceServiceDescription = governanceServiceDescription;

        return governanceActionDescription;
    }



    /**
     * Add details of a request type to the engine.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceEngineName name of the governance engine
     * @param governanceEngineTypeName type of engine
     * @param governanceRequestType name of request type
     * @param serviceRequestType internal name of the request type
     * @param requestParameters any request parameters
     * @param governanceActionDescription description of the governance action
     */
    private String addRequestType(String                      governanceEngineGUID,
                                  String                      governanceEngineName,
                                  String                      governanceEngineTypeName,
                                  String                      governanceRequestType,
                                  String                      serviceRequestType,
                                  Map<String, String>         requestParameters,
                                  GovernanceActionDescription governanceActionDescription,
                                  String                      supportedElementQualifiedName)
    {
        archiveHelper.addSupportedGovernanceService(governanceEngineGUID,
                                                    governanceRequestType,
                                                    serviceRequestType,
                                                    requestParameters,
                                                    governanceActionDescription.governanceServiceGUID);

        String governanceActionTypeGUID = archiveHelper.addGovernanceActionType(null,
                                                                                governanceEngineGUID,
                                                                                governanceEngineTypeName,
                                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                governanceEngineName + ":" + governanceRequestType,
                                                                                governanceRequestType + " (" + governanceEngineName + ")",
                                                                                governanceActionDescription.governanceServiceDescription,
                                                                                0,
                                                                                governanceActionDescription.supportedRequestParameters,
                                                                                governanceActionDescription.supportedActionTargets,
                                                                                governanceActionDescription.supportedAnalysisSteps,
                                                                                governanceActionDescription.supportedAnnotationTypes,
                                                                                governanceActionDescription.producedRequestParameters,
                                                                                governanceActionDescription.producedActionTargets,
                                                                                governanceActionDescription.producedGuards,
                                                                                0,
                                                                                null,
                                                                                null,
                                                                                null);


        if (governanceActionTypeGUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(governanceActionTypeGUID,
                                                      governanceRequestType,
                                                      requestParameters,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      governanceEngineGUID);

            if (supportedElementQualifiedName != null)
            {
                String supportedElementGUID = archiveHelper.queryGUID(supportedElementQualifiedName);
                archiveHelper.addResourceListRelationshipByGUID(supportedElementGUID,
                                                                governanceActionTypeGUID,
                                                                governanceActionDescription.resourceUse.getResourceUse(),
                                                                governanceActionDescription.governanceServiceDescription,
                                                                requestParameters,
                                                                false);
            }
        }

        return governanceActionTypeGUID;
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getSetUpClinicalTrialGovernanceActionService()
    {
        final String governanceServiceName = "set-up-clinical-trial-governance-action-service";
        final String governanceServiceDisplayName = "Set up new clinical trial";
        final String governanceServiceDescription = "Sets up the processes that will govern the clinical trial.";
        final String governanceServiceProviderClassName = CocoClinicalTrialSetUpProvider.class.getName();

        CocoClinicalTrialSetUpProvider provider = new CocoClinicalTrialSetUpProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.PROVISION_RESOURCE,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);
        return governanceActionDescription;
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getSetUpDataLakeForClinicalTrialGovernanceActionService()
    {
        final String governanceServiceName = "set-up-data-lake-for-clinical-trial-governance-action-service";
        final String governanceServiceDisplayName = "Set up Data Lake to capture weekly patient measurements";
        final String governanceServiceDescription = "Sets up the storage definitions that support the receipt of weekly patient measurement data for a clinical trial.  This data is accessible through OSS Unity Catalog (UC).";
        final String governanceServiceProviderClassName = CocoClinicalTrialSetUpDataLakeProvider.class.getName();

        CocoClinicalTrialSetUpDataLakeProvider provider = new CocoClinicalTrialSetUpDataLakeProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.PROVISION_RESOURCE,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);
        return governanceActionDescription;
    }


    private GovernanceActionDescription getNominateHospitalForClinicalTrialGovernanceActionService()
    {
        final String governanceServiceName = "nominate-hospital-for-clinical-trial-governance-action-service";
        final String governanceServiceDisplayName = "Nominate a hospital has legal and data management arrangements in place to capture and supply weekly patient measurements as part of a clinical trial.";
        final String governanceServiceDescription = "Checks that the certification type matches the one for the clinical trial project and sets up the certification relationship between the hospital and the certification type.  The start date is null.  The certification relationship identifies the people involved in completing the certification process.";
        final String governanceServiceProviderClassName = CocoClinicalTrialNominateHospitalProvider.class.getName();

        CocoClinicalTrialNominateHospitalProvider provider = new CocoClinicalTrialNominateHospitalProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.CERTIFY_RESOURCE,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);


        return governanceActionDescription;
    }

    private GovernanceActionDescription getCertifyHospitalForClinicalTrialGovernanceActionService()
    {
        final String governanceServiceName = "certify-hospital-for-clinical-trial-governance-action-service";
        final String governanceServiceDisplayName = "Certify that a hospital has legal and data management arrangements in place to capture and supply weekly patient measurements as part of a clinical trial.";
        final String governanceServiceDescription = "Checks that the certification type matches the one for the clinical trial project and sets up the start date in the certification relationship between the hospital and the certification type.";
        final String governanceServiceProviderClassName = CocoClinicalTrialCertifyHospitalProvider.class.getName();

        CocoClinicalTrialCertifyHospitalProvider provider = new CocoClinicalTrialCertifyHospitalProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.CERTIFY_RESOURCE,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);

        return governanceActionDescription;
    }



    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @return descriptive information on the governance service
     */
    private GovernanceActionDescription getHospitalOnboardingClinicalTrialGovernanceActionService()
    {
        final String governanceServiceName = "onboard-hospital-for-clinical-trial-governance-action-service";
        final String governanceServiceDisplayName = "Onboard a Hospital into a Clinical Trial Governance Action Service";
        final String governanceServiceDescription = "Sets up the landing area for data from a hospital as part of a clinical trial, along with the pipeline that catalogued the data and moved it into the data lake.  The aim is that the data is moved from the landing area as soon as possible.";
        final String governanceServiceProviderClassName = CocoClinicalTrialHospitalOnboardingProvider.class.getName();

        CocoClinicalTrialHospitalOnboardingProvider provider = new CocoClinicalTrialHospitalOnboardingProvider();

        GovernanceActionDescription governanceActionDescription = getGovernanceActionDescription(ResourceUse.PROVISION_RESOURCE,
                                                                                                 provider,
                                                                                                 governanceServiceDescription);

        governanceActionDescription.governanceServiceGUID = archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                                                               governanceServiceProviderClassName,
                                                                                               null,
                                                                                               governanceServiceName,
                                                                                               governanceServiceDisplayName,
                                                                                               governanceServiceDescription,
                                                                                               null);
        return governanceActionDescription;
    }


    /**
     * Create an entity for the FileProvisioning governance action service.
     *
     * @return unique identifier for the governance engine
     */
    private String getFileProvisioningGovernanceActionService()
    {
        final String governanceServiceName        = "coco-file-provisioning-governance-action-service";
        final String governanceServiceDisplayName = "File {move, copy, delete} Governance Action Service";
        final String governanceServiceDescription = "Works with files.  The request type defines which action is taken.  " +
                "The request parameters define the source file and destination, along with lineage options";
        final String ftpGovernanceServiceProviderClassName = MoveCopyFileGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                  ftpGovernanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null);
    }


    /**
     * Create an entity for the generic watchdog governance action service.
     *
     * @return unique identifier for the governance engine
     */
    private String getWatchdogGovernanceActionService()
    {
        final String governanceServiceName = "coco-new-measurements-watchdog-governance-action-service";
        final String governanceServiceDisplayName = "New Measurements Watchdog Governance Action Service";
        final String governanceServiceDescription = "Initiates a governance action process when a new weekly measurements file arrives.";
        final String governanceServiceProviderClassName = GenericFolderWatchdogGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                  governanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null);
    }


    /**
     * Add a governance service that walks backwards through an asset's lineage to find an origin classification.  If found, the same origin is added
     * to the asset.
     *
     * @return unique identifier fo the governance service
     */
    private String getZonePublisherGovernanceActionService()
    {
        final String governanceServiceName = "coco-zone-publisher-governance-action-service";
        final String governanceServiceDisplayName = "Update Asset's Zone Membership Governance Action Service";
        final String governanceServiceDescription = "Set up the zone membership for one or more assets supplied as action targets.";
        final String governanceServiceProviderClassName = ZonePublisherGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                  governanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @return unique identifier fo the governance service
     */
    private String getOriginSeekerGovernanceActionService()
    {
        final String governanceServiceName = "coco-origin-seeker-governance-action-service";
        final String governanceServiceDisplayName = "Locate and Set Origin Governance Action Service";
        final String governanceServiceDescription = "Navigates back through the lineage relationships to locate the origin classification(s) from the source(s) and sets it on the requested asset if the origin is unique.";
        final String governanceServiceProviderClassName = OriginSeekerGovernanceActionProvider.class.getName();

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR,
                                                  governanceServiceProviderClassName,
                                                  null,
                                                  governanceServiceName,
                                                  governanceServiceDisplayName,
                                                  governanceServiceDescription,
                                                  null);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addFTPFileRequestType(String governanceEngineGUID,
                                       String governanceServiceGUID)
    {
        final String governanceRequestType = "simulate-ftp";
        final String serviceRequestType = "copy-file";
        final String noLineagePropertyName = "noLineage";

        Map<String, String> requestParameters = new HashMap<>();

        requestParameters.put(noLineagePropertyName, "");

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, serviceRequestType, requestParameters, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addWatchNestedInFolderRequestType(String governanceEngineGUID,
                                                   String governanceServiceGUID)
    {
        final String governanceRequestType = "watch-for-new-files";
        final String serviceRequestType = "watch-nested-in-folder";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, serviceRequestType, null, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addCopyFileRequestType(String governanceEngineGUID,
                                        String governanceServiceGUID)
    {
        final String governanceRequestType = "copy-file";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, null, null, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addMoveFileRequestType(String governanceEngineGUID,
                                        String governanceServiceGUID)
    {
        final String governanceRequestType = "move-file";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, null, null, governanceServiceGUID);
    }



    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addDeleteFileRequestType(String governanceEngineGUID,
                                          String governanceServiceGUID)
    {
        final String governanceRequestType = "delete-file";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceRequestType, null, null, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addSeekOriginRequestType(String governanceEngineGUID,
                                          String governanceServiceGUID)
    {
        final String governanceServiceRequestType = "seek-origin";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceServiceRequestType, null, null, governanceServiceGUID);
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addSetUpClinicalTrialRequestType(String                      governanceEngineGUID,
                                                  String                      governanceEngineName,
                                                  GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "set-up-clinical-trial";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription,
                            ProjectDefinition.CLINICAL_TRIALS.getQualifiedName());
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addSetUpDataLakeForClinicalTrialRequestType(String                      governanceEngineGUID,
                                                             String                      governanceEngineName,
                                                             GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "set-up-data-lake";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription,
                            ProjectDefinition.CLINICAL_TRIALS.getQualifiedName());
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addNominateHospitalToClinicalTrialRequestType(String                      governanceEngineGUID,
                                                               String                      governanceEngineName,
                                                               GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "nominate-hospital";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription,
                            ProjectDefinition.CLINICAL_TRIALS.getQualifiedName());
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addCertifyHospitalToClinicalTrialRequestType(String                      governanceEngineGUID,
                                                              String                      governanceEngineName,
                                                              GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "certify-hospital";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription,
                            ProjectDefinition.CLINICAL_TRIALS.getQualifiedName());
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceEngineName unique name of the governance engine
     * @param governanceActionDescription details for calling the governance service
     */
    private void addOnboardHospitalToClinicalTrialRequestType(String                      governanceEngineGUID,
                                                              String                      governanceEngineName,
                                                              GovernanceActionDescription governanceActionDescription)
    {
        final String governanceRequestType = "onboard-hospital";

        this.addRequestType(governanceEngineGUID,
                            governanceEngineName,
                            OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                            governanceRequestType,
                            null,
                            null,
                            governanceActionDescription,
                            ProjectDefinition.CLINICAL_TRIALS.getQualifiedName());
    }


    /**
     * Set up the request type that links the governance engine to the governance service.
     *
     * @param governanceEngineGUID unique identifier of the governance engine
     * @param governanceServiceGUID unique identifier of the governance service
     */
    private void addSetZoneMembershipRequestType(String governanceEngineGUID,
                                                 String governanceServiceGUID)
    {
        final String governanceServiceRequestType = "set-zone-membership";

        archiveHelper.addSupportedGovernanceService(governanceEngineGUID, governanceServiceRequestType, null, null, governanceServiceGUID);
    }



    /**
     * Add the content to the archive builder.
     */
    public void getArchiveContent()
    {
        /*
         * Create governance services
         */
        String fileProvisionerGUID = this.getFileProvisioningGovernanceActionService();
        String watchDogServiceGUID = this.getWatchdogGovernanceActionService();
        String originSeekerGUID = this.getOriginSeekerGovernanceActionService();
        String zonePublisherGUID = this.getZonePublisherGovernanceActionService();

        String assetGovernanceEngineGUID = this.getAssetGovernanceEngine();

        this.addFTPFileRequestType(assetGovernanceEngineGUID, fileProvisionerGUID);
        this.addWatchNestedInFolderRequestType(assetGovernanceEngineGUID, watchDogServiceGUID);
        this.addSeekOriginRequestType(assetGovernanceEngineGUID, originSeekerGUID);
        this.addSetZoneMembershipRequestType(assetGovernanceEngineGUID, zonePublisherGUID);
        this.addCopyFileRequestType(assetGovernanceEngineGUID, fileProvisionerGUID);
        this.addMoveFileRequestType(assetGovernanceEngineGUID, fileProvisionerGUID);
        this.addDeleteFileRequestType(assetGovernanceEngineGUID, fileProvisionerGUID);


        String assetDiscoveryEngineGUID = this.getAssetDiscoveryEngine();

        String assetQualityEngineGUID = this.getAssetQualityEngine();
        // todo add services when they written

        /*
         * Define the Clinical Trials engine
         */
        GovernanceActionDescription setUpClinicalTrialDescription              = this.getSetUpClinicalTrialGovernanceActionService();
        GovernanceActionDescription clinicalTrialSetUpDataLakeDescription      = this.getSetUpDataLakeForClinicalTrialGovernanceActionService();
        GovernanceActionDescription hospitalNominationDescription              = this.getNominateHospitalForClinicalTrialGovernanceActionService();
        GovernanceActionDescription hospitalCertificationDescription           = this.getCertifyHospitalForClinicalTrialGovernanceActionService();
        GovernanceActionDescription clinicalTrialHospitalOnboardingDescription = this.getHospitalOnboardingClinicalTrialGovernanceActionService();

        String clinicalTrialsEngineName = "ClinicalTrials@CocoPharmaceuticals";
        String clinicalTrialsEngineGUID = this.getClinicalTrialsEngine(clinicalTrialsEngineName);

        this.addSetUpClinicalTrialRequestType(clinicalTrialsEngineGUID, clinicalTrialsEngineName, setUpClinicalTrialDescription);
        this.addSetUpDataLakeForClinicalTrialRequestType(clinicalTrialsEngineGUID, clinicalTrialsEngineName, clinicalTrialSetUpDataLakeDescription);
        this.addNominateHospitalToClinicalTrialRequestType(clinicalTrialsEngineGUID, clinicalTrialsEngineName, hospitalNominationDescription);
        this.addCertifyHospitalToClinicalTrialRequestType(clinicalTrialsEngineGUID, clinicalTrialsEngineName, hospitalCertificationDescription);
        this.addOnboardHospitalToClinicalTrialRequestType(clinicalTrialsEngineGUID, clinicalTrialsEngineName, clinicalTrialHospitalOnboardingDescription);


    }


    /**
     * Generates and writes out an open metadata archive for Coco Pharmaceuticals governance engines.
     */
    public void writeOpenMetadataArchive()
    {
        try
        {
            System.out.println("Writing to file: " + archiveFileName);

            super.writeOpenMetadataArchive(archiveFileName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }
    }
}
