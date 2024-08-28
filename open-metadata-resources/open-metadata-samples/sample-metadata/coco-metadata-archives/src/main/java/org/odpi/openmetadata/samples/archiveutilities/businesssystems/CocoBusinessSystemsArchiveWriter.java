/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.combo.CocoBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.CocoSustainabilityArchiveWriter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * CocoBusinessSystemsArchiveWriter creates a physical open metadata archive file containing the descriptions of the
 * data flows from Coco Pharmaceuticals business systems to the data lake.
 */
public class CocoBusinessSystemsArchiveWriter extends CocoBaseArchiveWriter
{
    private static final String archiveFileName = "CocoBusinessSystemsArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "ac202586-4042-407b-ae51-8096dfda223e";
    private static final String                  archiveName        = "Coco Pharmaceuticals Business Systems";
    private static final String                  archiveDescription = "The data flows from Coco Pharmaceuticals business systems to the data lake.";


    /**
     * Default constructor initializes the archive.
     */
    public CocoBusinessSystemsArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              new Date(),
              archiveFileName,
              new OpenMetadataArchive[]{ new CocoOrganizationArchiveWriter().getOpenMetadataArchive(),
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
    }


    /**
     * The systems define the hosts.
     */
    private void addHosts()
    {
        for (HostDefinition hostDefinition : HostDefinition.values())
        {

            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put(OpenMetadataType.OPERATING_SYSTEM_PROPERTY_NAME, hostDefinition.getOperatingSystem());
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
            extendedProperties.put(OpenMetadataType.USER_ID_PROPERTY_NAME, systemDefinition.getUserId());


            String serverGUID = archiveHelper.addAsset(OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                       systemDefinition.getQualifiedName(),
                                                       systemDefinition.getSystemId(),
                                                       systemDefinition.getVersionIdentifier(),
                                                       systemDefinition.getDescription(),
                                                       systemDefinition.getZones(),
                                                       null,
                                                       extendedProperties);



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
}

