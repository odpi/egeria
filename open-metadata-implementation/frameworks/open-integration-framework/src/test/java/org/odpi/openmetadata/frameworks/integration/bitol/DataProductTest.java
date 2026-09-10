/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolStatus;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSupportScope;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSupportTool;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductInputPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductManagementPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductManagementPortContent;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductManagementPortType;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductOutputPort;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentTest.YAML_MAPPER;
import static org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentTest.readResource;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Validate the detailed mapping of the ODPS customer data product example onto the DataProduct beans.
 */
public class DataProductTest
{
    /**
     * Default constructor
     */
    public DataProductTest()
    {
    }


    /**
     * Check every section of the customer data product example.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testCustomerDataProduct() throws IOException
    {
        DataProduct product = YAML_MAPPER.readValue(readResource("bitol/odps/customer-data-product.odps.yaml"), DataProduct.class);

        assertEquals(product.getApiVersion(), "v1.1.0");
        assertEquals(product.getName(), "Customer Data Product");
        assertEquals(product.getId(), "fbe8d147-28db-4f1d-bedf-a3fe9f458427");
        assertEquals(product.getDomain(), "seller");
        assertEquals(BitolStatus.fromValue(product.getStatus()), BitolStatus.DRAFT);
        assertEquals(product.getTenant(), "RetailCorp");
        assertEquals(product.getVersion(), "v1.1.0");
        assertEquals(product.getType(), "aggregate");
        assertNotNull(product.getSynonyms());
        assertNotNull(product.getContext());
        assertEquals(product.getDescription().getPurpose(), "Enterprise view of a customer.");
        assertEquals(product.getDescription().getLimitations(), "No known limitations.");
        assertEquals(product.getTags(), List.of("customer"));
        assertEquals(product.getProductCreatedTs(), "2023-01-15T10:30:00Z");

        /*
         * Input ports: two versions of the same port are separate entries.
         */
        assertEquals(product.getInputPorts().size(), 4);
        DataProductInputPort onlineTransactions = product.getInputPorts().get(3);
        assertEquals(onlineTransactions.getName(), "onlinetransactions");
        assertEquals(onlineTransactions.getVersion(), "1.1.0");
        assertEquals(onlineTransactions.getContractId(), "ec2a112d-5cfe-49f3-8760-f9cfb4597547");
        assertEquals(onlineTransactions.getTags(), List.of("transactions"));
        assertEquals(onlineTransactions.getCustomProperties().get(0).getProperty(), "transactions_version");
        assertEquals(onlineTransactions.getAuthoritativeDefinitions().get(0).getType(), "data_dictionary");

        /*
         * Output ports including SBOM and input contract dependencies.
         */
        assertEquals(product.getOutputPorts().size(), 4);
        DataProductOutputPort rawTransactionsV2 = product.getOutputPorts().get(1);
        assertEquals(rawTransactionsV2.getName(), "rawtransactions");
        assertEquals(rawTransactionsV2.getDescription(), "Raw Transactions");
        assertEquals(rawTransactionsV2.getType(), "tables");
        assertEquals(rawTransactionsV2.getVersion(), "2.0.0");
        assertEquals(rawTransactionsV2.getContractId(), "c2798941-1b7e-4b03-9e0d-955b1a872b33");
        assertEquals(rawTransactionsV2.getSbom().size(), 2);
        assertEquals(rawTransactionsV2.getSbom().get(0).getId(), "sbom-runtime");
        assertEquals(rawTransactionsV2.getSbom().get(0).getType(), "external");
        assertEquals(rawTransactionsV2.getSbom().get(0).getUrl(), "https://mysbomserver/transactions/runtime.cdx.json");
        assertEquals(rawTransactionsV2.getInputContracts().size(), 2);
        assertEquals(rawTransactionsV2.getInputContracts().get(0).getId(), "dbb7b1eb-7628-436e-8914-2a00638ba6db");
        assertEquals(rawTransactionsV2.getInputContracts().get(0).getVersion(), "2.0.0");
        assertNull(product.getOutputPorts().get(0).getSbom());

        /*
         * Management ports.
         */
        assertEquals(product.getManagementPorts().size(), 1);
        DataProductManagementPort dictionary = product.getManagementPorts().get(0);
        assertEquals(DataProductManagementPortContent.fromValue(dictionary.getContent()), DataProductManagementPortContent.DICTIONARY);
        assertEquals(DataProductManagementPortType.fromValue(dictionary.getType()), DataProductManagementPortType.TOPIC);
        assertEquals(dictionary.getName(), "tpc-dict-update");
        assertEquals(dictionary.getDescription(), "Kafka topic for dictionary updates");
        assertEquals(dictionary.getTags(), List.of("kafka"));
        assertEquals(dictionary.getCustomProperties().get(0).getValue(), "true");
        assertEquals(dictionary.getAuthoritativeDefinitions().get(0).getUrl(), "https://mykafka/topic");

        /*
         * Support and team.
         */
        assertEquals(product.getSupport().size(), 2);
        assertEquals(BitolSupportTool.fromValue(product.getSupport().get(0).getTool()), BitolSupportTool.SLACK);
        assertEquals(BitolSupportScope.fromValue(product.getSupport().get(1).getScope()), BitolSupportScope.ISSUES);
        assertEquals(product.getSupport().get(1).getUrl(), "mailto:data-support@retailcorp.com");

        assertEquals(product.getTeam().getName(), "Data Team");
        assertEquals(product.getTeam().getTags(), List.of("data", "team"));
        assertEquals(product.getTeam().getCustomProperties().get(0).getProperty(), "data_team");
        assertEquals(product.getTeam().getMembers().size(), 2);
        assertEquals(product.getTeam().getMembers().get(0).getUsername(), "john.doe@retailcorp.com");
        assertEquals(product.getTeam().getMembers().get(0).getRole(), "owner");
        assertEquals(product.getTeam().getMembers().get(1).getDateIn(), "2023-02-01");
    }


    /**
     * Check the minimal example.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testSimpleDataProduct() throws IOException
    {
        DataProduct product = YAML_MAPPER.readValue(readResource("bitol/odps/simple-data-product.odps.yaml"), DataProduct.class);

        assertEquals(product.getId(), "064c4630-8aad-4dc0-ba95-0f69940e6b18");
        assertEquals(BitolStatus.fromValue(product.getStatus()), BitolStatus.ACTIVE);
        assertEquals(product.getVersion(), "v1.0.0");
        assertEquals(product.getTags(), List.of("test", "simple"));
        assertEquals(product.getInputPorts().size(), 1);
        assertEquals(product.getOutputPorts().size(), 1);
        assertEquals(product.getOutputPorts().get(0).getContractId(), "87654321-4321-4321-4321-cba987654321");
        assertNull(product.getManagementPorts());
        assertNull(product.getSupport());
        assertNull(product.getTeam());
    }
}
