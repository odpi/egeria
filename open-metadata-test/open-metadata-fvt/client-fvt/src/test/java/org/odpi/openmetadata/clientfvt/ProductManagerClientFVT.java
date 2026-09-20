/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ProductManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProductManagerClientFVT covers {@code ProductManagerClient}, which does not fit
 * {@link ClientLifecycleFVT}: it has no retrieve, search, update or delete surface of its own, only create
 * methods.  A digital product is a Collection once it exists, and it is read, searched and maintained through
 * {@code CollectionClient} like any other.
 * <br><br>
 * What is covered here is {@code createDigitalProduct}, driven with only the properties it needs and every
 * optional attachment left null - which is the check worth having in this suite, because it is the one that
 * says the client can be called by a connector that has nothing else set up yet.
 * <br><br>
 * The client's three subscription-type methods are deliberately not exercised here.  They depend on content
 * from the digital products content pack - the Baudot subscription manager, license types, service level
 * objectives - which this suite does not load, and they already have thorough coverage in
 * {@code ProductManagerFVT} in
 * <a href="../../subscription-fvt">subscription-fvt</a>, where that content pack is loaded and a real
 * subscription is driven end to end.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ProductManagerClientFVT
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Create a digital product with no supporting elements attached to it, and read it back.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void productManagerClientCreatesADigitalProduct() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        ProductManagerClient productManager   = connectorContext.getProductManagerClient();
        OpenMetadataStore    store            = connectorContext.getOpenMetadataStore();

        String qualifiedName = ClientFvtTestSupport.newQualifiedName("DigitalProduct");

        NewElementOptions newElementOptions = new NewElementOptions(store.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        DigitalProductProperties properties = new DigitalProductProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName("client-fvt digital product");
        properties.setDescription("A digital product created through the product manager client by client-fvt.");
        properties.setProductName("client-fvt digital product");
        properties.setIntroductionDate(new Date());
        properties.setContentStatus(ContentStatus.ACTIVE);
        properties.setDeploymentStatus(DeploymentStatus.ACTIVE);

        String productGUID = productManager.createDigitalProduct(newElementOptions,
                                                                  null,
                                                                  properties,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null);

        try
        {
            assertNotNull(productGUID, "createDigitalProduct returned no GUID");

            OpenMetadataElement product = store.getMetadataElementByGUID(productGUID);

            assertNotNull(product, "The digital product could not be read back after being created");
            assertNotNull(product.getType(), "The digital product came back with no type");
            assertEquals(qualifiedName,
                         propertyHelper.getStringProperty("client-fvt",
                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                           product.getElementProperties(),
                                                           "productManagerClientCreatesADigitalProduct"),
                         "The digital product came back with a different qualified name");

            /*
             * A digital product is a Collection, which is what lets the collection client read and maintain
             * it once the product manager has created it.
             */
            assertTrue(OpenMetadataType.DIGITAL_PRODUCT.typeName.equals(product.getType().getTypeName()),
                       "createDigitalProduct created a " + product.getType().getTypeName()
                               + " rather than a " + OpenMetadataType.DIGITAL_PRODUCT.typeName);
        }
        finally
        {
            ClientFvtTestSupport.purgeElement(store, productGUID);
        }
    }
}
