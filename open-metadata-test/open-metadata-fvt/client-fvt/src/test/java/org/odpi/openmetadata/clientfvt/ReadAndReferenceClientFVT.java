/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.InformationSupplyChainClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.LineageClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataTypesClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SpecificationPropertyClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ValidMetadataValuesClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefGallery;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ReadAndReferenceClientFVT covers the clients that read rather than create - the classification explorer,
 * lineage, information supply chains, specification properties - together with the two lower-level clients
 * (the generic store and the types client) and the valid metadata values client, which maintains reference
 * data for type attributes rather than elements.
 * <br>
 * Where a client only reads, the test creates whatever it needs to read first, so the assertion is about this
 * run's own data rather than about whatever happens to be in the repository.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ReadAndReferenceClientFVT
{
    /**
     * The generic store must create, retrieve, search for and delete an element.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void openMetadataStoreRoundTripsAnElement() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String            qualifiedName     = ClientFvtTestSupport.newQualifiedName("StoreElement");
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        String elementGUID = openMetadataStore.createMetadataElementInStore(
                OpenMetadataType.COLLECTION.typeName,
                newElementOptions,
                null,
                new NewElementProperties(new PropertyHelper().addStringProperty(null,
                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                 qualifiedName)),
                null);

        try
        {
            assertNotNull(elementGUID, "createMetadataElementInStore returned no GUID");

            OpenMetadataElement element = openMetadataStore.getMetadataElementByGUID(elementGUID);

            assertNotNull(element, "The element could not be read back from the store");
            assertEquals(qualifiedName,
                         element.getElementProperties().getPropertiesAsStrings().get(OpenMetadataProperty.QUALIFIED_NAME.name),
                         "The element came back with a different qualified name");

            assertNotNull(openMetadataStore.findMetadataElementsWithString(qualifiedName, new SearchOptions()),
                          "findMetadataElementsWithString failed");
        }
        finally
        {
            ClientFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }


    /**
     * The types client must return the type system and resolve a type by name.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void openMetadataTypesClientReturnsTheTypeSystem() throws Exception
    {
        ConnectorContextBase    connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataTypesClient typesClient      = connectorContext.getOpenMetadataTypesClient();

        OpenMetadataTypeDefGallery gallery = typesClient.getAllTypes(false, false);

        assertNotNull(gallery, "getAllTypes returned nothing");
        assertNotNull(gallery.getTypeDefs(), "getAllTypes returned a gallery with no type definitions");
        assertFalse(gallery.getTypeDefs().isEmpty(), "getAllTypes returned an empty type system");

        assertNotNull(typesClient.getTypeDefByName(false, false, OpenMetadataType.COLLECTION.typeName),
                      "getTypeDefByName could not resolve Collection");
    }


    /**
     * The classification explorer must find, by GUID and by type, an element this test created.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void classificationExplorerClientReadsAnElement() throws Exception
    {
        ConnectorContextBase         connectorContext = ConnectorContextFactory.newContext();
        ClassificationExplorerClient explorerClient   = connectorContext.getClassificationExplorerClient();

        String hostGUID = FeedbackClientFVT.createHostCollection(connectorContext, "Explorer");

        try
        {
            assertNotNull(explorerClient.getRootElementByGUID(hostGUID, new GetOptions()),
                          "getRootElementByGUID could not find the element this test created");

            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);

            assertNotNull(explorerClient.getRootElementsByType(queryOptions),
                          "getRootElementsByType failed for Collection");
        }
        finally
        {
            FeedbackClientFVT.cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * The lineage client must link two elements and then detach them again.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void lineageClientLinksAndDetaches() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        LineageClient        lineageClient    = connectorContext.getLineageClient();

        String sourceGUID      = FeedbackClientFVT.createHostCollection(connectorContext, "LineageSource");
        String destinationGUID = FeedbackClientFVT.createHostCollection(connectorContext, "LineageDestination");
        String lineageGUID     = null;

        try
        {
            lineageGUID = lineageClient.linkLineage(sourceGUID,
                                                      destinationGUID,
                                                      OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                      new MakeAnchorOptions(),
                                                      null);

            assertNotNull(lineageGUID, "linkLineage returned no relationship GUID");
        }
        finally
        {
            if (lineageGUID != null)
            {
                lineageClient.detachLineage(lineageGUID, new DeleteOptions());
            }

            FeedbackClientFVT.cleanUpHost(connectorContext, sourceGUID);
            FeedbackClientFVT.cleanUpHost(connectorContext, destinationGUID);
        }
    }


    /**
     * The information supply chain client's retrieval calls must execute.  Supply chains are built from
     * several elements linked together, so this checks the read surface rather than constructing one.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void informationSupplyChainClientReads() throws Exception
    {
        ConnectorContextBase         connectorContext = ConnectorContextFactory.newContext();
        InformationSupplyChainClient supplyChainClient = connectorContext.getInformationSupplyChainClient();

        // A search that matches nothing returns null rather than an empty list, so the assertion is that the
        // call completes - the same convention the lifecycle tests use for by-name retrieval and search.
        supplyChainClient.getInformationSupplyChainsByName("client-fvt-no-such-chain", new QueryOptions(), false);
        supplyChainClient.findInformationSupplyChains("client-fvt-no-such-chain", false, new SearchOptions());
    }


    /**
     * The specification property client's retrieval calls must execute against the reference data the core
     * content pack loads.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void specificationPropertyClientReads() throws Exception
    {
        ConnectorContextBase        connectorContext = ConnectorContextFactory.newContext();
        SpecificationPropertyClient specificationClient = connectorContext.getSpecificationPropertyClient();

        // As above - no match returns null, so this checks the call executes.
        specificationClient.getSpecificationPropertiesByName("client-fvt-no-such-property", new QueryOptions());
    }


    /**
     * A valid metadata value must be accepted for a type's property, validate as valid, and stop validating
     * once it has been cleared.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void validMetadataValuesClientSetsValidatesAndClears() throws Exception
    {
        ConnectorContextBase      connectorContext = ConnectorContextFactory.newContext();
        ValidMetadataValuesClient validValuesClient = connectorContext.getValidMetadataValuesClient();

        String typeName      = OpenMetadataType.COLLECTION.typeName;
        String propertyName  = OpenMetadataProperty.CATEGORY.name;
        String preferredValue = "client-fvt-valid-value";

        ValidMetadataValue validMetadataValue = new ValidMetadataValue();

        validMetadataValue.setPreferredValue(preferredValue);
        validMetadataValue.setDisplayName("client-fvt valid value");
        validMetadataValue.setDescription("Created by client-fvt to prove the valid values client works.");

        try
        {
            validValuesClient.setUpValidMetadataValue(typeName, propertyName, validMetadataValue);

            assertTrue(validValuesClient.validateMetadataValue(typeName, propertyName, preferredValue),
                       "A valid metadata value that was just set up did not validate");
        }
        finally
        {
            try
            {
                validValuesClient.clearValidMetadataValue(typeName, propertyName, preferredValue, new DeleteOptions());
            }
            catch (Exception ignored)
            {
                // best effort
            }
        }
    }
}
