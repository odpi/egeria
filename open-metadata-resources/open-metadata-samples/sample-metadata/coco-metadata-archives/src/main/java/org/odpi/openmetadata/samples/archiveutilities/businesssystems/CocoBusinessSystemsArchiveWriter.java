/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.CocoSustainabilityArchiveWriter;

import java.util.*;


/**
 * CocoBusinessSystemsArchiveWriter creates a physical open metadata archive file containing the descriptions of the
 * data flows from Coco Pharmaceuticals business systems to the data lake.
 */
public class CocoBusinessSystemsArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoBusinessSystemsArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "ac202586-4042-407b-ae51-8096dfda223e";
    private static final String                  archiveName        = "Coco Pharmaceuticals Business Systems";
    private static final String                  archiveDescription = "The data flows from Coco Pharmaceuticals business systems to the data lake.";
    private static final Date                    creationDate       = new Date(1639984840038L);


    /**
     * Default constructor initializes the archive.
     */
    public CocoBusinessSystemsArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              new OpenMetadataArchive[]{ new CorePackArchiveWriter().getOpenMetadataArchive(),
                                         new CocoOrganizationArchiveWriter().getOpenMetadataArchive(),
                                         new CocoSustainabilityArchiveWriter().getOpenMetadataArchive() });
    }


    /**
     * Add the content to the archive builder.
     */
    @Override
    public void getArchiveContent()
    {
        addSolutionRoles();
        addInformationSupplyChains();
        addInformationSupplyChainSegments();
        addInformationSupplyChainLinks();
        addSolutionBlueprints();
        addSolutionComponents();
        addSolutionComponentActorDefinitions();
        addSolutionComponentWires();

        addHosts();
        addSystems();
        addSystemLineage();
    }


    /**
     * The systems define the hosts.
     */
    private void addHosts()
    {
        for (HostDefinition hostDefinition : HostDefinition.values())
        {

            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put(OpenMetadataProperty.OPERATING_SYSTEM.name, hostDefinition.getOperatingSystem());
            additionalProperties.put(OpenMetadataProperty.PATCH_LEVEL.name, hostDefinition.getPatchLevel());

            archiveHelper.addAsset(hostDefinition.getHostType().getOpenMetadataTypeName(),
                                   hostDefinition.getQualifiedName(),
                                   hostDefinition.getHostId(),
                                   null,
                                   hostDefinition.getDescription(),
                                   hostDefinition.getZones(),
                                   additionalProperties,
                                   null);

            archiveHelper.addAssetLocationRelationship(hostDefinition.getHostLocation().getQualifiedName(),
                                                       hostDefinition.getQualifiedName());
        }
    }


    /**
     * The systems define the software servers.
     */
    private void addSystems()
    {
        for (SystemDefinition systemDefinition : SystemDefinition.values())
        {
            Map<String, Object> extendedProperties = new HashMap<>();

            extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, systemDefinition.getSystemType().getPreferredValue());
            extendedProperties.put(OpenMetadataProperty.USER_ID.name, systemDefinition.getUserId());

            archiveHelper.setGUID(systemDefinition.getQualifiedName(), systemDefinition.getSystemGUID());
            String serverGUID = archiveHelper.addAsset(OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                       systemDefinition.getQualifiedName(),
                                                       systemDefinition.getSystemId(),
                                                       systemDefinition.getVersionIdentifier(),
                                                       systemDefinition.getDescription(),
                                                       systemDefinition.getZones(),
                                                       null,
                                                       extendedProperties);
            assert(serverGUID.equals(systemDefinition.getSystemGUID()));


            if (systemDefinition.getSystemType().getServerPurpose() != null)
            {
                archiveHelper.addServerPurposeClassification(serverGUID,
                                                             systemDefinition.getSystemType().getServerPurpose(),
                                                             null);
            }

            if (systemDefinition.getSystemType().getSoftwareServerCapabilities() != null)
            {
                for (String softwareCapabilityTypeName : systemDefinition.getSystemType().getSoftwareServerCapabilities())
                {
                    String softwareCapabilityQName = softwareCapabilityTypeName + " for " + systemDefinition.getQualifiedName();

                    if (softwareCapabilityTypeName.endsWith(OpenMetadataType.ENGINE.typeName))
                    {
                        Classification engineClassification = archiveHelper.getEngineClassification(softwareCapabilityTypeName);

                        archiveHelper.addSoftwareCapability(OpenMetadataType.ENGINE.typeName,
                                                            softwareCapabilityQName,
                                                            softwareCapabilityQName,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            engineClassification,
                                                            serverGUID,
                                                            OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                            OpenMetadataType.ASSET.typeName);
                    }
                    else
                    {
                        archiveHelper.addSoftwareCapability(softwareCapabilityTypeName,
                                                            softwareCapabilityQName,
                                                            softwareCapabilityQName,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            (Classification)null,
                                                            serverGUID,
                                                            OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                            OpenMetadataType.ASSET.typeName);
                    }

                    archiveHelper.addSupportedSoftwareCapabilityRelationship(softwareCapabilityQName,
                                                                             systemDefinition.getQualifiedName(),
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             1);
                }
            }

            if (systemDefinition.getSystemLocation() != null)
            {
                archiveHelper.addAssetLocationRelationship(systemDefinition.getSystemLocation().getQualifiedName(),
                                                           systemDefinition.getQualifiedName());
            }

            if (systemDefinition.getDeployedOn() != null)
            {
                for (HostDefinition hostDefinition : systemDefinition.getDeployedOn())
                {
                    archiveHelper.addDeployedOnRelationship(systemDefinition.getQualifiedName(),
                                                            hostDefinition.getQualifiedName(),
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            1);
                }
            }


            if (systemDefinition.getImplementingComponents() != null)
            {
                for (SolutionComponent solutionComponent : systemDefinition.getImplementingComponents())
                {
                    archiveHelper.addImplementedByRelationship(solutionComponent.getGUID(),
                                                               systemDefinition.getSystemGUID(),
                                                               "archaeological mapping",
                                                               "supports required capability",
                                                               null,
                                                               null);
                }
            }
        }
    }



    /**
     * The systems define the hosts.
     */
    private void addSystemLineage()
    {
        for (SystemLevelLineage systemLevelLineage : SystemLevelLineage.values())
        {
            archiveHelper.addLineageRelationship(systemLevelLineage.getSourceSystem().getSystemGUID(),
                                                 systemLevelLineage.getDestinationSystem().getSystemGUID(),
                                                 systemLevelLineage.getRelationshipName(),
                                                 systemLevelLineage.getRelationshipLabel(),
                                                 systemLevelLineage.getISCQualifiedName());
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

            List<SolutionBlueprint> consumingSolutionBlueprints = solutionComponent.getConsumingBlueprints();
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
}

