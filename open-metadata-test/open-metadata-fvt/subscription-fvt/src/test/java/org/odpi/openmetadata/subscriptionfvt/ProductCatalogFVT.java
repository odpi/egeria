/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProductCatalogFVT covers the first step of a consumer's journey: finding a digital product.
 * <br>
 * Nothing here creates a product.  The catalogue is built by the Jacquard Digital Product Loom running in the
 * integration daemon, from the definitions in {@link ProductDefinitionEnum}, and these tests check that what
 * arrived in the repository is what those definitions describe.  Asserting against the definitions rather
 * than against a written-out list of product names means that adding a product to the catalogue extends this
 * test's coverage without anybody editing it - and adding one that Jacquard cannot build fails here.
 * <br>
 * A consumer looking for a product does not know its qualified name, so both ways of finding one are covered:
 * by the exact name a link would carry, and by searching for words in the product's own description of itself.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ProductCatalogFVT
{
    private static OpenMetadataStore openMetadataStore;
    private static PropertyHelper    propertyHelper;


    /**
     * Build the catalogue once for this class, and create the clients the tests read it back through.
     *
     * @throws Exception the catalogue could not be built
     */
    @BeforeAll
    static void buildCatalogue() throws Exception
    {
        SubscriptionFvtTestSupport.ensureCatalogueBuilt();

        openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        propertyHelper    = new PropertyHelper();
    }


    /**
     * Every product and product family Jacquard defines is in the repository, with the type its definition
     * asks for.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("Jacquard builds a catalogue entry for every product it defines")
    void everyDefinedProductIsCatalogued() throws Exception
    {
        List<String> missingProducts   = new ArrayList<>();
        List<String> wrongTypeProducts = new ArrayList<>();

        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            OpenMetadataElement product = openMetadataStore.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);

            if (product == null)
            {
                missingProducts.add(productDefinition.getQualifiedName());
            }
            else if (! productDefinition.getTypeName().equals(product.getType().getTypeName()))
            {
                wrongTypeProducts.add(productDefinition.getQualifiedName() + " is a " + product.getType().getTypeName()
                                              + " rather than a " + productDefinition.getTypeName());
            }
        }

        assertTrue(missingProducts.isEmpty(),
                   "Jacquard did not create " + missingProducts.size() + " of the " + ProductDefinitionEnum.values().length
                           + " products it defines: " + missingProducts);
        assertTrue(wrongTypeProducts.isEmpty(),
                   "Products were created with the wrong type: " + wrongTypeProducts);
    }


    /**
     * A consumer can find a product by the name that identifies it, and what comes back describes the
     * product well enough to decide whether it is the one wanted.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A digital product can be located by name, and describes itself")
    void productCanBeLocatedByName() throws Exception
    {
        ProductDefinition productDefinition = aProductWithSubscriptions();

        OpenMetadataElement product = openMetadataStore.getMetadataElementByUniqueName(productDefinition.getQualifiedName(),
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(product, "Product " + productDefinition.getQualifiedName() + " was not found by its qualified name");

        assertEquals(productDefinition.getDisplayName(),
                     SubscriptionFvtTestSupport.getStringProperty(product, OpenMetadataProperty.DISPLAY_NAME.name),
                     "Product " + productDefinition.getProductName() + " does not carry the display name its definition gives it");

        assertNotNull(SubscriptionFvtTestSupport.getStringProperty(product, OpenMetadataProperty.DESCRIPTION.name),
                      "Product " + productDefinition.getProductName() + " has no description - a consumer choosing between"
                              + " products has nothing to choose on");
    }


    /**
     * A consumer who does not know a product's name can still find it, by searching for what it is about.
     * This is the search a catalogue's own front page would run.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A digital product can be found by searching its description")
    void productCanBeFoundBySearch() throws Exception
    {
        ProductDefinition productDefinition = aProductWithSubscriptions();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.DISPLAY_NAME.name,
                                                                        productDefinition.getDisplayName(),
                                                                        PropertyComparisonOperator.LIKE));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.DIGITAL_PRODUCT.typeName);
        queryOptions.setPageSize(SubscriptionFvtTestSupport.MAX_PAGE_SIZE);

        List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

        assertNotNull(found, "Searching for digital products called '" + productDefinition.getDisplayName() + "' found nothing");

        boolean foundTheProduct = false;

        for (OpenMetadataElement element : found)
        {
            if (productDefinition.getQualifiedName()
                                 .equals(SubscriptionFvtTestSupport.getStringProperty(element, OpenMetadataProperty.QUALIFIED_NAME.name)))
            {
                foundTheProduct = true;
            }
        }

        assertTrue(foundTheProduct,
                   "Searching digital products for '" + productDefinition.getDisplayName() + "' returned " + found.size()
                           + " result(s), none of which was " + productDefinition.getQualifiedName());
    }


    /**
     * A product family holds the products that belong to it.  This is what makes a family worth subscribing
     * to: one subscription covers everything in it, so the membership is the definition of what is covered.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    @DisplayName("A product family holds the products that belong to it")
    void productFamilyHoldsItsProducts() throws Exception
    {
        ProductDefinition family = aProductFamilyWithMembers();

        OpenMetadataElement familyElement = openMetadataStore.getMetadataElementByUniqueName(family.getQualifiedName(),
                                                                                             OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(familyElement, "Product family " + family.getQualifiedName() + " was not found");
        assertEquals(OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName,
                     familyElement.getType().getTypeName(),
                     family.getQualifiedName() + " is not a product family");

        List<String> memberQualifiedNames = new ArrayList<>();

        for (RelatedMetadataElement member : SubscriptionFvtTestSupport.getRelatedElements(openMetadataStore,
                                                                                           familyElement.getElementGUID(),
                                                                                           OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                           1))
        {
            memberQualifiedNames.add(SubscriptionFvtTestSupport.getStringProperty(member.getElement(),
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name));
        }

        assertFalse(memberQualifiedNames.isEmpty(), "Product family " + family.getProductName() + " holds no members at all");

        List<String> expectedButMissing = new ArrayList<>();

        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            if ((productDefinition.getProductFamilies() != null)
                        && (productDefinition.getProductFamilies().contains(family))
                        && (! memberQualifiedNames.contains(productDefinition.getQualifiedName())))
            {
                expectedButMissing.add(productDefinition.getQualifiedName());
            }
        }

        assertTrue(expectedButMissing.isEmpty(),
                   "Product family " + family.getProductName() + " does not hold the products whose definitions name it as"
                           + " their family: " + expectedButMissing + ".  A subscription to the family would not cover them.");
    }


    /**
     * Return the product these tests use as their example: the first definition that is a product rather than
     * a family and offers at least one subscription type.  Chosen from the definitions rather than named
     * here, so that reordering the catalogue does not break the suite.
     *
     * @return product definition
     */
    static ProductDefinition aProductWithSubscriptions()
    {
        for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
        {
            if ((OpenMetadataType.DIGITAL_PRODUCT.typeName.equals(productDefinition.getTypeName()))
                        && (productDefinition.getSubscriptionTypes() != null)
                        && (! productDefinition.getSubscriptionTypes().isEmpty()))
            {
                return productDefinition;
            }
        }

        throw new AssertionError("No product in the catalogue offers any subscription type - there is nothing for this suite"
                                         + " to subscribe to.");
    }


    /**
     * Return the product family these tests use as their example: the first family that at least one product
     * names as its own.
     *
     * @return product family definition
     */
    static ProductDefinition aProductFamilyWithMembers()
    {
        for (ProductDefinitionEnum candidate : ProductDefinitionEnum.values())
        {
            if (OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName.equals(candidate.getTypeName()))
            {
                for (ProductDefinitionEnum productDefinition : ProductDefinitionEnum.values())
                {
                    if ((productDefinition.getProductFamilies() != null)
                                && (productDefinition.getProductFamilies().contains(candidate)))
                    {
                        return candidate;
                    }
                }
            }
        }

        throw new AssertionError("No product family in the catalogue has any members.");
    }
}
