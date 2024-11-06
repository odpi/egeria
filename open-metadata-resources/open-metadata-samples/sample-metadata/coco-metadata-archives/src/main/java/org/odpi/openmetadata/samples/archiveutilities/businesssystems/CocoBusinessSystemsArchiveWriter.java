/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.CocoSustainabilityArchiveWriter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
                                                 systemLevelLineage.getRelationshipDescription());


        }
    }

}

