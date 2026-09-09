/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bitolfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.BitolMapperBase;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataContractGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataProductGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Drives the Coco Pharmaceuticals clinical trial sample documents through the whole chain.  The four data
 * contracts and the data product are published to the integration daemon's REST API; the cataloguers turn
 * them into data sharing agreements and a digital product; the file store writes them out; the generators
 * turn the metadata back into documents; and the publisher connector, given the product catalog as a catalog
 * target, regenerates and republishes them.
 * <br>
 * The tests are ordered because each one builds on the state the previous one left in the repository.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BitolRoundTripFVT
{
    private static final String PRODUCT_DOCUMENT   = "DataProduct/teddy-bear-drop-foot-weekly-measurements.odps.yaml";
    private static final List<String> CONTRACT_DOCUMENTS = List.of("DataContract/hampton-hospital-weekly-measurements.odcs.yaml",
                                                                   "DataContract/oak-dene-hospital-weekly-measurements.odcs.yaml",
                                                                   "DataContract/old-market-hospital-weekly-measurements.odcs.yaml",
                                                                   "DataContract/validated-weekly-measurements.odcs.yaml");

    private static final String DOMAIN = "clinical-trials";


    private static String qualifiedName(BitolDocument document)
    {
        return BitolMapperBase.getDocumentQualifiedName(document.getKind(), document.getId(), document.getVersion());
    }


    private static boolean hasClassification(OpenMetadataElement element,
                                             String              classificationName)
    {
        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if (classificationName.equals(classification.getClassificationName()))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Return the unique identifiers of the related elements of a particular type.
     *
     * @param related related element summaries from a root element
     * @param typeName open metadata type to keep
     * @return list of GUIDs (empty if none)
     */
    private static List<String> relatedGUIDsOfType(List<RelatedMetadataElementSummary> related,
                                                   String                              typeName)
    {
        List<String> results = new ArrayList<>();

        if (related != null)
        {
            for (RelatedMetadataElementSummary summary : related)
            {
                if ((summary != null) && (summary.getRelatedElement() != null) &&
                    (typeName.equals(summary.getRelatedElement().getElementHeader().getType().getTypeName())))
                {
                    results.add(summary.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }

        return results;
    }


    @Test
    @Order(1)
    @DisplayName("Data contracts published to the daemon are catalogued as data sharing agreements")
    public void testDataContractsAreCatalogued() throws Exception
    {
        IntegrationDaemon    integrationDaemon = OMAGPlatformExtension.getIntegrationDaemonClient();
        ConnectorContextBase context           = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = context.getOpenMetadataStore();

        for (String documentName : CONTRACT_DOCUMENTS)
        {
            String       rawDocument  = BitolFvtTestSupport.readSampleDocument(documentName);
            DataContract dataContract = BitolDocumentFormatter.parseDataContract(rawDocument);

            integrationDaemon.publishDataContract(rawDocument);

            OpenMetadataElement agreement = BitolFvtTestSupport.waitForElement(openMetadataStore, qualifiedName(dataContract), "Cataloguing " + documentName);

            assertEquals(OpenMetadataType.AGREEMENT.typeName, agreement.getType().getTypeName(), documentName + " was not catalogued as an Agreement.");
            assertTrue(hasClassification(agreement, OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION.typeName), documentName + " is not classified as a DataSharingAgreement.");

            OpenMetadataRootElement agreementElement = context.getCollectionClient().getCollectionByGUID(agreement.getElementGUID(), context.getCollectionClient().getGetOptions());

            List<String> dataStructures = relatedGUIDsOfType(agreementElement.getAgreementItems(), OpenMetadataType.DATA_STRUCTURE.typeName);

            assertEquals(dataContract.getSchema().size(), dataStructures.size(), documentName + ": each schema object should be a DataStructure agreement item.");

            /*
             * The generator reverses the mapping: the document it produces should have the same identity and
             * schema as the one that was catalogued.
             */
            DataContract regenerated = new DataContractGenerator(context).generateDataContract(agreement.getElementGUID());

            assertEquals(dataContract.getId(), regenerated.getId(), documentName + ": the regenerated contract has a different id.");
            assertEquals(dataContract.getVersion(), regenerated.getVersion(), documentName + ": the regenerated contract has a different version.");
            assertEquals(dataContract.getName(), regenerated.getName(), documentName + ": the regenerated contract has a different name.");
            assertNotNull(regenerated.getSchema(), documentName + ": the regenerated contract has no schema.");
            assertEquals(dataContract.getSchema().size(), regenerated.getSchema().size(), documentName + ": the regenerated contract has a different number of schema objects.");

            List<String> originalProperties    = new ArrayList<>();
            List<String> regeneratedProperties = new ArrayList<>();

            for (DataContractSchemaProperty property : dataContract.getSchema().get(0).getProperties())
            {
                originalProperties.add(property.getName());
            }
            for (DataContractSchemaProperty property : regenerated.getSchema().get(0).getProperties())
            {
                regeneratedProperties.add(property.getName());
            }

            assertEquals(originalProperties, regeneratedProperties, documentName + ": the regenerated schema properties differ from the original.");
            assertTrue(regenerated.getRoles() != null && regenerated.getRoles().size() == dataContract.getRoles().size(), documentName + ": the regenerated contract has a different number of access roles.");
        }
    }


    @Test
    @Order(2)
    @DisplayName("The data product published to the daemon is catalogued as a digital product with its ports")
    public void testDataProductIsCatalogued() throws Exception
    {
        IntegrationDaemon    integrationDaemon = OMAGPlatformExtension.getIntegrationDaemonClient();
        ConnectorContextBase context           = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = context.getOpenMetadataStore();

        String      rawDocument = BitolFvtTestSupport.readSampleDocument(PRODUCT_DOCUMENT);
        DataProduct dataProduct = BitolDocumentFormatter.parseDataProduct(rawDocument);

        integrationDaemon.publishDataProduct(rawDocument);

        OpenMetadataElement product = BitolFvtTestSupport.waitForElement(openMetadataStore, qualifiedName(dataProduct), "Cataloguing " + PRODUCT_DOCUMENT);

        assertEquals(OpenMetadataType.DIGITAL_PRODUCT.typeName, product.getType().getTypeName(), "The data product was not catalogued as a DigitalProduct.");

        OpenMetadataElement catalog = openMetadataStore.getMetadataElementByUniqueName(OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName + "::" + DOMAIN, "qualifiedName");

        assertNotNull(catalog, "The product's domain was not catalogued as a DigitalProductCatalog.");

        OpenMetadataRootElement productElement = context.getCollectionClient().getCollectionByGUID(product.getElementGUID(), context.getCollectionClient().getGetOptions());

        List<String> components = relatedGUIDsOfType(productElement.getCollectionMembers(), OpenMetadataType.SOLUTION_COMPONENT.typeName);

        assertEquals(1, components.size(), "The product should have one SolutionComponent carrying its ports.");

        OpenMetadataRootElement component = context.getSolutionComponentClient().getSolutionComponentByGUID(components.get(0), context.getSolutionComponentClient().getGetOptions());

        int expectedPorts = dataProduct.getInputPorts().size() + dataProduct.getOutputPorts().size();

        assertNotNull(component.getSolutionPorts(), "The product's solution component has no ports.");
        assertEquals(expectedPorts, component.getSolutionPorts().size(), "The product's solution component has the wrong number of ports.");

        /*
         * Round trip through the generator.
         */
        DataProduct regenerated = new DataProductGenerator(context).generateDataProduct(product.getElementGUID());

        assertEquals(dataProduct.getId(), regenerated.getId(), "The regenerated product has a different id.");
        assertEquals(dataProduct.getVersion(), regenerated.getVersion(), "The regenerated product has a different version.");
        assertEquals(DOMAIN, regenerated.getDomain(), "The regenerated product has a different domain.");
        assertNotNull(regenerated.getInputPorts(), "The regenerated product has no input ports.");
        assertEquals(dataProduct.getInputPorts().size(), regenerated.getInputPorts().size(), "The regenerated product has a different number of input ports.");
        assertEquals(dataProduct.getOutputPorts().size(), regenerated.getOutputPorts().size(), "The regenerated product has a different number of output ports.");

        List<String> originalContracts    = new ArrayList<>();
        List<String> regeneratedContracts = new ArrayList<>();

        dataProduct.getOutputPorts().forEach(port -> originalContracts.add(port.getContractId()));
        regenerated.getOutputPorts().forEach(port -> regeneratedContracts.add(port.getContractId()));

        assertEquals(originalContracts, regeneratedContracts, "The regenerated output ports reference different contracts.");
    }


    @Test
    @Order(3)
    @DisplayName("The file store wrote every published document")
    public void testFileStoreWroteTheDocuments() throws Exception
    {
        List<String> allDocuments = new ArrayList<>(CONTRACT_DOCUMENTS);
        allDocuments.add(PRODUCT_DOCUMENT);

        for (String documentName : allDocuments)
        {
            BitolDocument document = BitolDocumentFormatter.parseDocument(BitolFvtTestSupport.readSampleDocument(documentName));
            File          stored   = new File(new File(new File(BitolFvtTestSupport.STORE_DIRECTORY, document.getKind()), document.getId()), document.getVersion() + ".yaml");

            BitolFvtTestSupport.waitFor("The file store writing " + stored.getPath(), stored::isFile);

            BitolDocument reread = BitolDocumentFormatter.parseDocument(Files.readString(stored.toPath(), StandardCharsets.UTF_8));

            assertEquals(document.getId(), reread.getId(), stored.getPath() + " holds a different document.");
        }
    }


    @Test
    @Order(4)
    @DisplayName("The publisher regenerates the products in a catalog attached as a catalog target")
    public void testPublisherRegeneratesTheCatalog() throws Exception
    {
        ConnectorContextBase context           = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = context.getOpenMetadataStore();
        IntegrationDaemon    integrationDaemon = OMAGPlatformExtension.getIntegrationDaemonClient();

        DataProduct dataProduct = BitolDocumentFormatter.parseDataProduct(BitolFvtTestSupport.readSampleDocument(PRODUCT_DOCUMENT));
        File        stored      = new File(new File(new File(BitolFvtTestSupport.STORE_DIRECTORY, dataProduct.getKind()), dataProduct.getId()), dataProduct.getVersion() + ".yaml");

        assertTrue(stored.isFile(), "The store has not yet written " + stored.getPath() + " - the earlier tests did not complete.");

        String storedBefore = Files.readString(stored.toPath(), StandardCharsets.UTF_8);

        OpenMetadataElement catalog = openMetadataStore.getMetadataElementByUniqueName(OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName + "::" + DOMAIN, "qualifiedName");

        assertNotNull(catalog, "The DigitalProductCatalog for domain " + DOMAIN + " is missing.");

        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();
        catalogTargetProperties.setCatalogTargetName("productCatalog");

        context.getAssetClient().addCatalogTarget(IntegrationConnectorDefinition.BITOL_DOCUMENT_PUBLISHER.getGUID(), catalog.getElementGUID(), null, catalogTargetProperties);

        /*
         * A refresh picks up the new catalog target and regenerates every product in it.  The generated
         * document differs in layout from the original that the store holds, so the store rewrites the file.
         */
        integrationDaemon.refreshConnector(IntegrationConnectorDefinition.BITOL_DOCUMENT_PUBLISHER.getConnectorName());

        BitolFvtTestSupport.waitFor("The publisher regenerating " + stored.getPath(), () ->
        {
            try
            {
                return ! storedBefore.equals(Files.readString(stored.toPath(), StandardCharsets.UTF_8));
            }
            catch (Exception error)
            {
                return false;
            }
        });

        DataProduct regenerated = BitolDocumentFormatter.parseDataProduct(Files.readString(stored.toPath(), StandardCharsets.UTF_8));

        assertEquals(dataProduct.getId(), regenerated.getId(), "The regenerated document in the store has a different id.");
        assertEquals(dataProduct.getInputPorts().size(), regenerated.getInputPorts().size(), "The regenerated document in the store has a different number of input ports.");
    }
}
