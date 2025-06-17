/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.OriginSeekerGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.OriginSeekerGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.RetentionClassifierGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.remediation.ZonePublisherGovernanceActionProvider;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.EvaluateAnnotationsGuard;
import org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericFolderWatchdogGovernanceActionProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.RequestTypeDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionGuard;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceActionDescription;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.*;

import java.util.*;


/**
 * CocoGovernanceEnginesArchiveWriter creates a physical open metadata archive file containing the governance engine definitions
 * needed by Coco Pharmaceuticals.
 */
public class CocoGovernanceEnginesArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoGovernanceEngineDefinitionsArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9cbd2b33-e80f-4df2-adc6-d859ebff4c34";
    private static final String                  archiveName        = "CocoGovernanceEngineDefinitions";
    private static final String                  archiveDescription = "Governance Engines for Coco Pharmaceuticals.";
    private static final Date                    creationDate       = new Date(1639984840038L);


    /**
     * Default constructor initializes the archive.
     */
    public CocoGovernanceEnginesArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              new OpenMetadataArchive[]{ new CorePackArchiveWriter().getOpenMetadataArchive(),
                                         new CocoGovernanceProgramArchiveWriter().getOpenMetadataArchive() });
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
        final String assetGovernanceEngineUserId      = "assetgovernancenpa";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                 assetGovernanceEngineName,
                                                 assetGovernanceEngineDisplayName,
                                                 assetGovernanceEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 assetGovernanceEngineUserId,
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
        final String assetDiscoveryEngineUserId      = "assetdiscoverynpa";

        return archiveHelper.addGovernanceEngine(OpenMetadataType.SURVEY_ACTION_ENGINE.typeName,
                                                 assetDiscoveryEngineName,
                                                 assetDiscoveryEngineDisplayName,
                                                 assetDiscoveryEngineDescription,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 assetDiscoveryEngineUserId,
                                                 null,
                                                 null);
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
     * @param actionTargets action targets
     * @param governanceActionDescription description of the governance action
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param supportedElementQualifiedName element to link the governance action type to
     */
    private   void addRequestType(String                      governanceEngineGUID,
                                  String                      governanceEngineName,
                                  String                      governanceEngineTypeName,
                                  String                      governanceRequestType,
                                  String                      serviceRequestType,
                                  Map<String, String>         requestParameters,
                                  List<NewActionTarget>       actionTargets,
                                  GovernanceActionDescription governanceActionDescription,
                                  String                      governanceActionTypeGUID,
                                  String                      supportedElementQualifiedName)
    {
        archiveHelper.addSupportedGovernanceService(governanceEngineGUID,
                                                    governanceRequestType,
                                                    serviceRequestType,
                                                    requestParameters,
                                                    governanceActionDescription.governanceServiceGUID);

        String governanceActionTypeQualifiedName = governanceEngineName + "::" + governanceRequestType;

        archiveHelper.setGUID(governanceActionTypeQualifiedName, governanceActionTypeGUID);

        String guid = archiveHelper.addGovernanceActionType(null,
                                                            governanceEngineGUID,
                                                            governanceEngineTypeName,
                                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                            null,
                                                            governanceActionTypeQualifiedName,
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
        assert(governanceActionTypeGUID.equals(guid));

        archiveHelper.addGovernanceActionExecutor(governanceActionTypeGUID,
                                                  governanceRequestType,
                                                  requestParameters,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  governanceEngineGUID);

        if (actionTargets != null)
        {
            for (NewActionTarget actionTarget : actionTargets)
            {
                if (actionTarget != null)
                {
                    archiveHelper.addTargetForActionType(governanceActionTypeGUID, actionTarget);
                }
            }
        }

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




    /**
     * Create solution roles
     */
    private void addSolutionRoles()
    {
        for (SolutionRoleDefinition solutionRoleDefinition : SolutionRoleDefinition.values())
        {
            archiveHelper.setGUID(solutionRoleDefinition.getQualifiedName(), solutionRoleDefinition.getGUID());

            String solutionRoleGUID = archiveHelper.addActorRole(OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                                                                 solutionRoleDefinition.getQualifiedName(),
                                                                 solutionRoleDefinition.getIdentifier(),
                                                                 solutionRoleDefinition.getDisplayName(),
                                                                 solutionRoleDefinition.getDescription(),
                                                                 solutionRoleDefinition.getScope().getPreferredValue(),
                                                                 false,
                                                                 0,
                                                                 null,
                                                                 null);
            assert(solutionRoleGUID.equals(solutionRoleDefinition.getGUID()));
        }
    }


    /**
     * Create supply chains
     */
    private void addInformationSupplyChains()
    {
        final String methodName = "addInformationSupplyChains";

        for (InformationSupplyChain informationSupplyChain : InformationSupplyChain.values())
        {
            archiveHelper.setGUID(informationSupplyChain.getQualifiedName(), informationSupplyChain.getGUID());

            String iscGUID = archiveHelper.addInformationSupplyChain(OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                                                                     informationSupplyChain.getQualifiedName(),
                                                                     informationSupplyChain.getDisplayName(),
                                                                     informationSupplyChain.getDescription(),
                                                                     informationSupplyChain.getScope().getPreferredValue(),
                                                                     informationSupplyChain.getPurposes(),
                                                                     null,
                                                                     null);
            assert(iscGUID.equals(informationSupplyChain.getGUID()));

            if (informationSupplyChain.isTemplate())
            {
                archiveHelper.addTemplateClassification(iscGUID,
                                                        informationSupplyChain.getTemplateName(),
                                                        informationSupplyChain.getTemplateDescription(),
                                                        "V1.0",
                                                        null,
                                                        methodName);
            }
        }
    }


    /**
     * Create supply chain segments
     */
    private void addInformationSupplyChainSegments()
    {
        for (InformationSupplyChainSegment segment : InformationSupplyChainSegment.values())
        {
            archiveHelper.setGUID(segment.getQualifiedName(), segment.getGUID());

            String segmentGUID = archiveHelper.addInformationSupplyChainSegment(segment.getOwningSupplyChain().getGUID(),
                                                                                OpenMetadataType.INFORMATION_SUPPLY_CHAIN_SEGMENT.typeName,
                                                                                segment.getQualifiedName(),
                                                                                segment.getDisplayName(),
                                                                                segment.getDescription(),
                                                                                segment.getScope().getPreferredValue(),
                                                                                segment.getIntegrationStyle(),
                                                                                null,
                                                                                segment.getOwner(),
                                                                                segment.getOwnerTypeName(),
                                                                                segment.getOwnerPropertyName(),
                                                                                null,
                                                                                null);
            assert(segmentGUID.equals(segment.getGUID()));
        }
    }

    /**
     * Link information supply chain segments together.
     */
    private void addInformationSupplyChainLinks()
    {
        for (InformationSupplyChainLink informationSupplyChainLink : InformationSupplyChainLink.values())
        {
            archiveHelper.addInformationSupplyChainLinkRelationship(informationSupplyChainLink.getSegment1().getGUID(),
                                                                    informationSupplyChainLink.getSegment2().getGUID(),
                                                                    informationSupplyChainLink.getLabel(),
                                                                    informationSupplyChainLink.getDescription());
        }
    }


    /**
     * Link solution components together.
     */
    private void addSolutionComponentWires()
    {
        for (SolutionComponentWire solutionComponentWire : SolutionComponentWire.values())
        {
            archiveHelper.addSolutionLinkingWireRelationship(solutionComponentWire.getComponent1().getGUID(),
                                                             solutionComponentWire.getComponent2().getGUID(),
                                                             solutionComponentWire.getLabel(),
                                                             solutionComponentWire.getDescription(),
                                                             solutionComponentWire.getInformationSupplySegmentGUIDs());
        }
    }


    /**
     * Create solution blueprints
     */
    private void addSolutionBlueprints()
    {
        final String methodName = "addSolutionBlueprints";

        for (SolutionBlueprint solutionBlueprint : SolutionBlueprint.values())
        {
            archiveHelper.setGUID(solutionBlueprint.getQualifiedName(), solutionBlueprint.getGUID());

            String blueprintGUID = archiveHelper.addSolutionBlueprint(OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                      solutionBlueprint.getQualifiedName(),
                                                                      solutionBlueprint.getDisplayName(),
                                                                      solutionBlueprint.getDescription(),
                                                                      solutionBlueprint.getVersionIdentifier(),
                                                                      null,
                                                                      null);
            assert (blueprintGUID.equals(solutionBlueprint.getGUID()));

            if (solutionBlueprint.isTemplate())
            {
                archiveHelper.addTemplateClassification(blueprintGUID,
                                                        "Standard Solution Blueprint Template",
                                                        null,
                                                        "V1.0",
                                                        null,
                                                        methodName);
            }
        }
    }


    /**
     * Create solution components
     */
    private void addSolutionComponents()
    {
        for (SolutionComponent solutionComponent : SolutionComponent.values())
        {
            archiveHelper.setGUID(solutionComponent.getQualifiedName(), solutionComponent.getGUID());

            List<SolutionBlueprint> consumingSolutionBlueprints     = solutionComponent.getConsumingBlueprints();
            List<String>            consumingSolutionBlueprintGUIDs = null;

            if (consumingSolutionBlueprints != null)
            {
                consumingSolutionBlueprintGUIDs = new ArrayList<>();

                for (SolutionBlueprint solutionBlueprint : consumingSolutionBlueprints)
                {
                    if (solutionBlueprint != null)
                    {
                        consumingSolutionBlueprintGUIDs.add(solutionBlueprint.getGUID());
                    }
                }
            }

            String componentGUID = archiveHelper.addSolutionComponent(consumingSolutionBlueprintGUIDs,
                                                                      OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                      solutionComponent.getQualifiedName(),
                                                                      solutionComponent.getDisplayName(),
                                                                      solutionComponent.getDescription(),
                                                                      solutionComponent.getVersionIdentifier(),
                                                                      solutionComponent.getComponentType(),
                                                                      solutionComponent.getImplementationType(),
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null);
            assert(componentGUID.equals(solutionComponent.getGUID()));

            if (solutionComponent.getSubComponents() != null)
            {
                for (SolutionComponent subComponent : solutionComponent.getSubComponents())
                {
                    archiveHelper.addSolutionCompositionRelationship(componentGUID, subComponent.getGUID());
                }
            }

            if (solutionComponent.getLinkedFromSegment() != null)
            {
                for (InformationSupplyChainSegment segment : solutionComponent.getLinkedFromSegment())
                {
                    archiveHelper.addImplementedByRelationship(segment.getGUID(),
                                                               solutionComponent.getGUID(),
                                                               "Information Supply Chain Refinement",
                                                               "Supporting Supply Chain",
                                                               null,
                                                               null);
                }
            }

            if (solutionComponent.getImplementationResource() != null)
            {
                archiveHelper.addImplementationResourceRelationship(solutionComponent.getGUID(),
                                                                    solutionComponent.getImplementationResource(),
                                                                    "Standard implementation of the main process step");
            }
        }
    }


    private void addSolutionComponentActorDefinitions()
    {
        for (SolutionComponentActor solutionRoleDefinition : SolutionComponentActor.values())
        {
            archiveHelper.addSolutionComponentActorRelationship(solutionRoleDefinition.getSolutionRole().getGUID(),
                                                                solutionRoleDefinition.getSolutionComponent().getGUID(),
                                                                solutionRoleDefinition.getRole(),
                                                                solutionRoleDefinition.getDescription());
        }
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

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                  DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType(),
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

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                  DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType(),
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

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                  DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType(),
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

        return archiveHelper.addGovernanceService(DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName(),
                                                  DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType(),
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
    @Override
    public void getArchiveContent()
    {
        /*
         * Create the default governance engines
         */
        for (CocoGovernanceEngineDefinition governanceEngineDefinition : CocoGovernanceEngineDefinition.values())
        {
            this.createGovernanceEngine(governanceEngineDefinition);
        }

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        for (CocoGovernanceServiceDefinition governanceServiceDefinition : CocoGovernanceServiceDefinition.values())
        {
            this.addGovernanceServiceDefinition(governanceServiceDefinition);
        }

        addSolutionRoles();
        addInformationSupplyChains();
        addInformationSupplyChainSegments();
        addInformationSupplyChainLinks();
        addSolutionBlueprints();


        /*
         * Connect the governance engines to the governance services using the request types.
         */
        for (CocoRequestTypeDefinition requestTypeDefinition : CocoRequestTypeDefinition.values())
        {
            this.addRequestType(requestTypeDefinition.getGovernanceEngine().getGUID(),
                                requestTypeDefinition.getGovernanceEngine().getName(),
                                requestTypeDefinition.getGovernanceEngine().getType(),
                                requestTypeDefinition.getGovernanceRequestType(),
                                requestTypeDefinition.getServiceRequestType(),
                                requestTypeDefinition.getRequestParameters(),
                                requestTypeDefinition.getActionTargets(),
                                requestTypeDefinition.getGovernanceService().getGovernanceActionDescription(),
                                requestTypeDefinition.getGovernanceActionTypeGUID(),
                                requestTypeDefinition.getSupportedElementQualifiedName());
        }

        addSolutionComponents();
        addSolutionComponentActorDefinitions();
        addSolutionComponentWires();

        this.addOnboardingGovernanceActionProcess();
    }



    /**
     * Create an entity that represents a governance engine.
     *
     * @param governanceEngineDefinition details of the governance engine
     */
    private void createGovernanceEngine(CocoGovernanceEngineDefinition governanceEngineDefinition)
    {
        archiveHelper.setGUID(governanceEngineDefinition.getName(),
                              governanceEngineDefinition.getGUID());

        archiveHelper.addGovernanceEngine(governanceEngineDefinition.getType(),
                                          governanceEngineDefinition.getName(),
                                          governanceEngineDefinition.getDisplayName(),
                                          governanceEngineDefinition.getDescription(),
                                          null,
                                          null,
                                          null,
                                          null,
                                          governanceEngineDefinition.getUserId(),
                                          null,
                                          null);
    }


    /**
     * Add entities for each governance action service.
     *
     * @param governanceServiceDefinition details of governance service
     */
    private void addGovernanceServiceDefinition(CocoGovernanceServiceDefinition governanceServiceDefinition)
    {
        archiveHelper.setGUID(governanceServiceDefinition.getName(), governanceServiceDefinition.getGUID());

        archiveHelper.addGovernanceService(governanceServiceDefinition.getDeployedImplementationType().getAssociatedTypeName(),
                                           governanceServiceDefinition.getDeployedImplementationType().getDeployedImplementationType(),
                                           governanceServiceDefinition.getConnectorProviderClassName(),
                                           null,
                                           governanceServiceDefinition.getName(),
                                           governanceServiceDefinition.getDisplayName(),
                                           governanceServiceDefinition.getDescription(),
                                           null);
    }


    /**
     * Create the onboarding process for clinical trials.
     */
    private void addOnboardingGovernanceActionProcess()
    {
        String qualifiedName = "Coco:GovernanceActionProcess:ClinicalTrials:WeeklyMeasurements:Onboarding";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      qualifiedName,
                                                                      "Onboard Landing Area Files For Clinical Trial Project",
                                                                      "V1.0",
                                                                      """
                                                                              Ensures that new files added to the landing are correctly catalogued in the data lake.

                                                                              This process performs the follow function:
                                                                                   1) The physical file is moved to the data lake and renamed,
                                                                                   2) A new asset is created for the new file,
                                                                                   3) Lineage is created between the original file asset and the new file asset,
                                                                                   4) The owner and origin are assigned,
                                                                                   5) The governance zones are assigned to make the new asset visible to the research team.""",
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + ":MoveWeeklyMeasurementsFile",
                                                                        "Move Weekly Measurements File",
                                                                        "The physical file is moved to the data lake and renamed, an asset is created for the new file (in the quarantine zone) and a lineage relationship is created between the original file asset and the new file asset.",
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step1GUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(step1GUID,
                                                      RequestTypeDefinition.MOVE_FILE.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      RequestTypeDefinition.MOVE_FILE.getGovernanceEngine().getGUID());

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + ":SeekOrigin",
                                                                        "Seek and validate origin",
                                                                        "Validate that origin of the file is correctly set up.",
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step2GUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(step2GUID,
                                                      RequestTypeDefinition.SEEK_ORIGIN.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      RequestTypeDefinition.SEEK_ORIGIN.getGovernanceEngine().getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, MoveCopyFileGuard.PROVISIONING_COMPLETE.getName(), false, step2GUID);
        }

        String step3GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + ":SetRetentionPeriod",
                                                                        "Set retention period",
                                                                        "Set up the dates when the data associated with the asset should be archived and then deleted.",
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step3GUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(step3GUID,
                                                      RequestTypeDefinition.RETENTION_PERIOD.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      RequestTypeDefinition.RETENTION_PERIOD.getGovernanceEngine().getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step2GUID, OriginSeekerGuard.ORIGIN_ASSIGNED.getName(), false, step3GUID);
            archiveHelper.addNextGovernanceActionProcessStep(step2GUID, OriginSeekerGuard.ORIGIN_ALREADY_ASSIGNED.getName(), false, step3GUID);
        }

        String step4GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + ":DataQuality",
                                                                        "Check quality of data",
                                                                        "Validate that origin of the file is correctly set up.",
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step4GUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(step4GUID,
                                                      CocoRequestTypeDefinition.CHECK_DATA.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      CocoRequestTypeDefinition.CHECK_DATA.getGovernanceEngine().getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, MoveCopyFileGuard.PROVISIONING_COMPLETE.getName(), false, step4GUID);
        }

        String step5GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + ":CheckForRFAs",
                                                                        "Check for quality issues",
                                                                        "Validate that there are no reported data qualify issues.",
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step5GUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(step5GUID,
                                                      RequestTypeDefinition.EVALUATE_ANNOTATIONS.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      RequestTypeDefinition.EVALUATE_ANNOTATIONS.getGovernanceEngine().getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step4GUID, SurveyActionGuard.SURVEY_COMPLETED.getName(), false, step5GUID);
        }


        String step6GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        qualifiedName + ":SetZones",
                                                                        "Publish asset (set governance zones)",
                                                                        "Set up the zones in the asset so that is it visible in the data lake.",
                                                                        0,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step6GUID != null)
        {
            archiveHelper.addGovernanceActionExecutor(step6GUID,
                                                      RequestTypeDefinition.ZONE_MEMBER.getGovernanceRequestType(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      RequestTypeDefinition.ZONE_MEMBER.getGovernanceEngine().getGUID());

            archiveHelper.addNextGovernanceActionProcessStep(step3GUID, RetentionClassifierGuard.CLASSIFICATION_ASSIGNED.getName(), true, step6GUID);
            archiveHelper.addNextGovernanceActionProcessStep(step5GUID, EvaluateAnnotationsGuard.ACTIONS_ACTIONED.getName(), true, step6GUID);
        }
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
