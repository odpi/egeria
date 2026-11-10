/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate the parts of the mapping logic that do not need a metadata store: naming, version comparison, status and
 * contact method mapping and the fundamentals copied onto an element's properties.
 */
public class BitolMapperBaseTest
{
    /**
     * A mapper with no context, for the pure functions.
     */
    private static class TestMapper extends BitolMapperBase
    {
        TestMapper()
        {
            super(null);
        }
    }


    /**
     * Default constructor
     */
    public BitolMapperBaseTest()
    {
    }


    /**
     * Qualified names are deterministic and include the version when there is one.
     */
    @Test public void testQualifiedNames()
    {
        assertEquals(BitolMapperBase.getDocumentQualifiedName(BitolDocument.DATA_PRODUCT_KIND, "abc", "1.0.0"), "DataProduct::abc::1.0.0");
        assertEquals(BitolMapperBase.getDocumentQualifiedName(BitolDocument.DATA_CONTRACT_KIND, "abc", null), "DataContract::abc");
        assertEquals(BitolMapperBase.getDocumentQualifiedName(BitolDocument.DATA_CONTRACT_KIND, "abc", " "), "DataContract::abc");
        assertEquals(BitolMapperBase.getExternalIdQualifiedName(BitolDocument.DATA_PRODUCT_KIND, "abc"), "ExternalId::DataProduct::abc");
    }


    /**
     * Versions compare numerically segment by segment, with a leading v ignored.
     */
    @Test public void testCompareVersions()
    {
        assertTrue(BitolMapperBase.compareVersions("1.10.0", "1.2.0") > 0);
        assertTrue(BitolMapperBase.compareVersions("1.2.0", "1.10.0") < 0);
        assertEquals(BitolMapperBase.compareVersions("1.0.0", "v1.0.0"), 0);
        assertEquals(BitolMapperBase.compareVersions("1.0", "1.0.0"), 0);
        assertTrue(BitolMapperBase.compareVersions("2.0.0", "1.9.9") > 0);
        assertTrue(BitolMapperBase.compareVersions("1.0.0", null) > 0);
        assertTrue(BitolMapperBase.compareVersions(null, "1.0.0") < 0);
        assertEquals(BitolMapperBase.compareVersions(null, null), 0);
        assertTrue(BitolMapperBase.compareVersions("1.0.0-beta", "1.0.0-alpha") > 0);
    }


    /**
     * Status and contact method values map onto the open metadata enumerations.
     */
    @Test public void testEnumMappings()
    {
        TestMapper mapper = new TestMapper();

        DigitalProductProperties properties = new DigitalProductProperties();

        mapper.setContentStatus("active", properties);
        assertEquals(properties.getContentStatus(), ContentStatus.ACTIVE);

        mapper.setContentStatus("Draft", properties);
        assertEquals(properties.getContentStatus(), ContentStatus.DRAFT);

        mapper.setContentStatus("proposed", properties);
        assertEquals(properties.getContentStatus(), ContentStatus.PROPOSED);

        mapper.setContentStatus("deprecated", properties);
        assertEquals(properties.getContentStatus(), ContentStatus.DEPRECATED);

        mapper.setContentStatus("in-review", properties);
        assertEquals(properties.getContentStatus(), ContentStatus.OTHER);
        assertEquals(properties.getUserDefinedContentStatus(), "in-review");

        assertEquals(mapper.getContactMethodType("email"), ContactMethodType.EMAIL);
        assertEquals(mapper.getContactMethodType("slack"), ContactMethodType.CHAT);
        assertEquals(mapper.getContactMethodType("teams"), ContactMethodType.CHAT);
        assertEquals(mapper.getContactMethodType("ticket"), ContactMethodType.OTHER);
        assertEquals(mapper.getContactMethodType("carrier pigeon"), ContactMethodType.OTHER);
        assertEquals(mapper.getContactMethodType(null), ContactMethodType.OTHER);

        assertEquals(mapper.parseDate("2023-01-15").getTime(), 1673740800000L - java.util.TimeZone.getDefault().getOffset(1673740800000L));
        assertNull(mapper.parseDate("15/01/2023"));
        assertNull(mapper.parseDate(null));
    }


    /**
     * The fundamentals of a document land on the element's properties, with the extras in additional properties.
     */
    @Test public void testFillFundamentals()
    {
        TestMapper  mapper  = new TestMapper();
        DataProduct product = new DataProduct();

        product.setId("fbe8d147");
        product.setName("Customer Data Product");
        product.setVersion("2.1.0");
        product.setStatus("retired");
        product.setDomain("seller");
        product.setTenant("RetailCorp");

        DigitalProductProperties properties = new DigitalProductProperties();

        mapper.fillFundamentals(product, properties, "DataProduct::fbe8d147::2.1.0");

        assertEquals(properties.getQualifiedName(), "DataProduct::fbe8d147::2.1.0");
        assertEquals(properties.getIdentifier(), "fbe8d147");
        assertEquals(properties.getVersionIdentifier(), "2.1.0");
        assertEquals(properties.getDisplayName(), "Customer Data Product");
        assertEquals(properties.getContentStatus(), ContentStatus.OBSOLETE);
        assertEquals(properties.getAdditionalProperties().get("bitol.domain"), "seller");
        assertEquals(properties.getAdditionalProperties().get("bitol.tenant"), "RetailCorp");
        assertEquals(properties.getAdditionalProperties().get("bitol.kind"), BitolDocument.DATA_PRODUCT_KIND);
        assertEquals(properties.getAdditionalProperties().get("bitol.apiVersion"), DataProduct.CURRENT_API_VERSION);
        assertTrue(mapper.isRetired(product));

        product.setStatus("active");
        product.setName(null);
        mapper.fillFundamentals(product, properties, "DataProduct::fbe8d147::2.1.0");
        assertEquals(properties.getDisplayName(), "fbe8d147", "the id is used when there is no name");
        assertFalse(mapper.isRetired(product));
    }


    /**
     * Custom properties attributed to Egeria are written by the generators, recognised by the mappers, and
     * kept out of the element's additional properties; other vendors' custom properties are kept as before.
     */
    @Test public void testEgeriaCustomProperties()
    {
        Date introductionDate = new Date(1700000000000L);

        List<BitolCustomProperty> customProperties = BitolGeneratorBase.addEgeriaProperty(null, BitolMapperBase.EGERIA_MATURITY, "Mature", "Level of maturity");

        customProperties = BitolGeneratorBase.addEgeriaProperty(customProperties, BitolMapperBase.EGERIA_INTRODUCTION_DATE, introductionDate, "Introduced");
        customProperties = BitolGeneratorBase.addEgeriaProperty(customProperties, BitolMapperBase.EGERIA_QUESTIONS, new ArrayList<String>(), "Nothing to add");
        customProperties = BitolGeneratorBase.addEgeriaProperty(customProperties, BitolMapperBase.EGERIA_CATEGORY, null, "Nothing to add");

        assertEquals(customProperties.size(), 2, "null values and empty lists are not written");
        assertEquals(customProperties.get(0).getVendor(), BitolMapperBase.EGERIA_VENDOR);
        assertEquals(customProperties.get(1).getValue(), introductionDate.toInstant().toString(), "dates are written as ISO-8601 instants");

        BitolCustomProperty otherVendor = new BitolCustomProperty();

        otherVendor.setProperty("owner");
        otherVendor.setValue("sales");
        otherVendor.setVendor("acme");
        customProperties.add(otherVendor);

        Map<String, Object> egeriaValues = BitolMapperBase.getEgeriaValues(customProperties);

        assertEquals(egeriaValues.size(), 2);
        assertEquals(BitolMapperBase.getEgeriaString(egeriaValues, BitolMapperBase.EGERIA_MATURITY), "Mature");
        assertEquals(BitolMapperBase.getEgeriaDate(egeriaValues, BitolMapperBase.EGERIA_INTRODUCTION_DATE), introductionDate);
        assertNull(BitolMapperBase.getEgeriaDate(egeriaValues, BitolMapperBase.EGERIA_WITHDRAW_DATE));

        TestMapper          mapper               = new TestMapper();
        Map<String, String> additionalProperties = new HashMap<>();

        mapper.addCustomProperties(customProperties, additionalProperties);

        assertEquals(additionalProperties.get("owner"), "sales");
        assertFalse(additionalProperties.containsKey(BitolMapperBase.EGERIA_MATURITY), "Egeria's properties are restored, not kept");
        assertFalse(additionalProperties.get(BitolMapperBase.ADDITIONAL_PROPERTY_PREFIX + BitolMapperBase.CUSTOM_PROPERTIES_JSON).contains(BitolMapperBase.EGERIA_VENDOR));

        List<BitolCustomProperty> regenerated = BitolGeneratorBase.getCustomProperties(additionalProperties);

        assertEquals(regenerated.size(), 1);
        assertEquals(regenerated.get(0).getVendor(), "acme");
    }


    /**
     * Only the tags that are not already keywords of the element are added, each once, so cataloguing the same
     * document again adds nothing.
     */
    @Test public void testGetNewTags()
    {
        List<String> tags = new ArrayList<>(List.of("Reference Data", "Sales", "Reference Data", " ", "Finance"));

        tags.add(null);

        assertEquals(BitolMapperBase.getNewTags(tags, Set.of()), List.of("Reference Data", "Sales", "Finance"));
        assertEquals(BitolMapperBase.getNewTags(tags, Set.of("Reference Data", "Finance")), List.of("Sales"));
        assertTrue(BitolMapperBase.getNewTags(tags, Set.of("Reference Data", "Sales", "Finance")).isEmpty());
    }
}
