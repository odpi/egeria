/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bitolfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.BitolMapperBase;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Drops a data contract into the directory the Bitol Files Receiver monitors, refreshes the receiver, and checks
 * that the contract is catalogued and that the file itself is catalogued from the YAML file template with the
 * ODCS deployed implementation type and linked to the agreement as a resource.
 * <br>
 * The document is the Hampton hospital contract with its version raised to 1.2.0, so that it is distinguishable
 * from the versions the other suites publish.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class FilesReceiverFVT
{
    private static final String CONTRACT_DOCUMENT = "DataContract/hampton-hospital-weekly-measurements.odcs.yaml";
    private static final String NEW_VERSION       = "1.2.0";


    @Test
    @DisplayName("A data contract dropped into the loading bay is catalogued, and its file is catalogued and linked to it")
    public void testContractArrivesThroughFiles() throws Exception
    {
        ConnectorContextBase context = ConnectorContextFactory.newContext();

        String       rawDocument  = BitolFvtTestSupport.readSampleDocument(CONTRACT_DOCUMENT).replaceFirst("\nversion: 1\\.0\\.0", "\nversion: " + NEW_VERSION);
        DataContract dataContract = BitolDocumentFormatter.parseDataContract(rawDocument);

        assertEquals(NEW_VERSION, dataContract.getVersion(), "The test document did not get its new version.");

        File dropped = new File(BitolFvtTestSupport.LOADING_BAY_DIRECTORY, "hampton-hospital-weekly-measurements-1.2.0.odcs.yaml");

        Files.writeString(dropped.toPath(), rawDocument, StandardCharsets.UTF_8);

        /*
         * The receiver only publishes on refresh, and its own refresh interval is a minute away.
         */
        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(IntegrationConnectorDefinition.BITOL_FILES_RECEIVER.getConnectorName());

        String qualifiedName = BitolMapperBase.getDocumentQualifiedName(dataContract.getKind(), dataContract.getId(), dataContract.getVersion());

        OpenMetadataElement agreement = BitolFvtTestSupport.waitForElement(context.getOpenMetadataStore(), qualifiedName, "Cataloguing the dropped contract");

        assertEquals(OpenMetadataType.AGREEMENT.typeName, agreement.getType().getTypeName(), "The dropped contract was not catalogued as an Agreement.");

        /*
         * The file is catalogued after the document has been published, so it may arrive a moment later.
         */
        BitolFvtTestSupport.waitFor("The receiver cataloguing " + dropped.getPath() + " and linking it to the agreement", () ->
        {
            try
            {
                return BitolFvtTestSupport.findDocumentFileAsset(context, agreement.getElementGUID(), DeployedImplementationType.OPEN_DATA_CONTRACT_FILE) != null;
            }
            catch (Exception error)
            {
                return false;
            }
        });

        OpenMetadataElement asset = BitolFvtTestSupport.findDocumentFileAsset(context, agreement.getElementGUID(), DeployedImplementationType.OPEN_DATA_CONTRACT_FILE);

        assertNotNull(asset);
        assertEquals(OpenMetadataType.YAML_FILE.typeName, asset.getType().getTypeName(), "The document file should be catalogued from the YAML file template.");
    }
}
