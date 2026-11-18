/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;

import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueDataSetProvider;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


/**
 * A subscription to a digital product (or product family) grants the product's license to the subscriber.
 * If the product definition has no license, the subscription request fails.  This test ensures every
 * product and product family in Jacquard's catalog names a license, and that it is a license type rather
 * than one of the other kinds of governance definition.  This includes the products that Jacquard creates
 * dynamically for each reference data set and valid metadata value set that it discovers.
 */
public class ProductDefinitionLicenseTest
{
    /**
     * Every product definition has a license.
     */
    @Test
    public void testEveryProductHasLicense()
    {
        List<String> missingLicenses = new ArrayList<>();

        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            if (productDefinition.getLicense() == null)
            {
                missingLicenses.add(productDefinition.name());
            }
        }

        assertTrue(missingLicenses.isEmpty(),
                   "The following product definitions have a null license, so subscriptions to them will fail: " + missingLicenses);
    }


    /**
     * The governance definition used as a product's license is a license type.
     */
    @Test
    public void testEveryLicenseIsLicenseType()
    {
        List<String> wrongTypes = new ArrayList<>();

        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            ProductGovernanceDefinition license = productDefinition.getLicense();

            if ((license != null) && (! OpenMetadataType.LICENSE_TYPE.typeName.equals(license.getType())))
            {
                wrongTypes.add(productDefinition.name() + " (" + license.name() + " is a " + license.getType() + ")");
            }
        }

        assertTrue(wrongTypes.isEmpty(),
                   "The following product definitions use a governance definition that is not a license type as their license: " + wrongTypes);
    }


    /**
     * The product created dynamically for a reference data set has a license.
     */
    @Test
    public void testReferenceDataSetProductHasLicense()
    {
        ProductDefinition productDefinition = new ReferenceDataSetProvider().getProductDefinition("test-guid",
                                                                                                  "TEST-REFERENCE-DATA-SET",
                                                                                                  "Test Reference Data Set",
                                                                                                  "Test description");

        validateDynamicProductLicense(productDefinition);
    }


    /**
     * The product created dynamically for a valid metadata value set has a license.
     */
    @Test
    public void testValidMetadataValueSetProductHasLicense()
    {
        ProductDefinition productDefinition = new ValidMetadataValueDataSetProvider().getProductDefinition("TEST-VALID-METADATA-VALUE-SET",
                                                                                                           "Test Valid Metadata Value Set",
                                                                                                           "Test description");

        validateDynamicProductLicense(productDefinition);
    }


    /**
     * Check that a dynamically created product definition has a license and that it is a license type.
     *
     * @param productDefinition product definition returned by the connector provider
     */
    private void validateDynamicProductLicense(ProductDefinition productDefinition)
    {
        assertNotNull(productDefinition, "No product definition returned");
        assertNotNull(productDefinition.getLicense(),
                      "Product definition " + productDefinition.getQualifiedName() + " has a null license, so subscriptions to it will fail");
        assertEquals(productDefinition.getLicense().getType(),
                     OpenMetadataType.LICENSE_TYPE.typeName,
                     "Product definition " + productDefinition.getQualifiedName() + " uses a governance definition that is not a license type as its license");
    }
}
