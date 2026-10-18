/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ProductManagerHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.SavedQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataSpecProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.viewservices.productmanager.rest.DataContractResponse;
import org.odpi.openmetadata.viewservices.productmanager.rest.DataProductResponse;
import org.odpi.openmetadata.viewservices.productmanager.rest.NewDigitalProductRequestBody;
import org.odpi.openmetadata.viewservices.productmanager.rest.NewSubscriptionTypeRequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProductManagerFVT covers every request of the Product Manager OMVS: creating a digital product with its
 * surroundings, its three kinds of subscription type, product dependencies and product managers, and the Bitol
 * ODCS/ODPS documents - importing them (as a document string and as a bean), generating them from the catalogued
 * elements, and publishing them to an integration daemon.
 * <br>
 * The product's surroundings (its product manager role, folder, data specification and data) are created with
 * the Actor Manager, Collection Manager and Asset Maker, and products are read back and deleted with the
 * Collection Manager since a digital product is a collection.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductManagerFVT
{
    private static final ViewServiceClient productManager    = new ViewServiceClient(ViewServiceDescription.PRODUCT_MANAGER);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    private static final ViewServiceClient actorManager      = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static final List<String> collectionsToDelete = new ArrayList<>();
    private static final List<String> assetsToDelete      = new ArrayList<>();

    private static String productGUID      = null;
    private static String otherProductGUID = null;
    private static String roleGUID         = null;
    private static String folderGUID       = null;
    private static String dataSpecGUID     = null;
    private static String dataAssetGUID    = null;


    /**
     * A digital product created through the product manager is a DigitalProduct, linked to the product manager,
     * folder, data and data specification it was created with.
     */
    @Test
    @Order(1)
    void createDigitalProduct()
    {
        PersonRoleProperties roleProperties = new PersonRoleProperties();

        roleProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("PersonRole", "product-manager"));
        roleProperties.setDisplayName("Product manager FVT role");

        roleGUID = actorManager.create("/actor-roles", ownAnchor(roleProperties));

        folderGUID   = createCollection(collectionProperties(new CollectionProperties(), "folder"));
        dataSpecGUID = createCollection(collectionProperties(new DataSpecProperties(), "data-spec"));

        SavedQueryProperties dataProperties = new SavedQueryProperties();

        dataProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SavedQuery", "product-data"));
        dataProperties.setDisplayName("Product manager FVT data");

        dataAssetGUID = assetMaker.create("/assets", ownAnchor(dataProperties));

        assetsToDelete.add(dataAssetGUID);

        DigitalProductProperties productProperties = collectionProperties(new DigitalProductProperties(), "product");

        productProperties.setProductName("Product manager FVT product");
        productProperties.setIdentifier("PM-FVT-1");
        productProperties.setDeploymentStatus(DeploymentStatus.ACTIVE);

        NewDigitalProductRequestBody requestBody = new NewDigitalProductRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(productProperties);
        requestBody.setProductManagerGUID(roleGUID);
        requestBody.setCollectionGUIDs(List.of(folderGUID));
        requestBody.setProductAssetGUID(dataAssetGUID);
        requestBody.setDataSpecGUID(dataSpecGUID);

        productGUID = productManager.create("/digital-products", requestBody);

        collectionsToDelete.add(0, productGUID);

        OpenMetadataRootElement product = retrieve(productGUID);

        assertEquals(OpenMetadataType.DIGITAL_PRODUCT.typeName, product.getElementHeader().getType().getTypeName());
        assertEquals("Product manager FVT product", assertInstanceOf(DigitalProductProperties.class, product.getProperties()).getProductName());
        assertTrue(RelatedElementChecks.isLinked(product, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, roleGUID),
                   "the product is not assigned to its product manager");
        assertTrue(RelatedElementChecks.isLinked(product, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, folderGUID),
                   "the product is not a member of its folder");
        assertTrue(RelatedElementChecks.isLinked(product, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, dataAssetGUID),
                   "the product does not hold its data");
        assertTrue(RelatedElementChecks.isLinked(product, OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName, dataSpecGUID),
                   "the product is not described by its data specification");

        NewDigitalProductRequestBody otherRequestBody = new NewDigitalProductRequestBody();

        otherRequestBody.setIsOwnAnchor(true);
        otherRequestBody.setProperties(collectionProperties(new DigitalProductProperties(), "other-product"));

        otherProductGUID = productManager.create("/digital-products", otherRequestBody);

        collectionsToDelete.add(0, otherProductGUID);
    }


    /**
     * A subscription type is a subscribing action process that runs the governance action types shipped in the
     * digital products content pack (for example "create subscription", {@code
     * ProductManagerHandler.CREATE_SUBSCRIPTION_ACTION_TYPE_GUID}).  This suite loads only the core content pack, so
     * each of the three requests must be refused, naming the governance action type it could not find, rather
     * than creating a process that could never run.
     */
    @Test
    @Order(2)
    void subscriptionTypesNeedTheDigitalProductsContentPack()
    {
        List<JsonNode> responses = List.of(productManager.postForFailure("/digital-products/" + productGUID + "/subscription-types/one-time",
                                                                         subscriptionType("one-time", 0, null)),
                                           productManager.postForFailure("/digital-products/" + productGUID + "/subscription-types/periodic",
                                                                         subscriptionType("periodic", 24 * 60, null)),
                                           productManager.postForFailure("/digital-products/" + productGUID + "/subscription-types/ongoing-update",
                                                                         subscriptionType("ongoing", 10, List.of(dataAssetGUID))));

        for (JsonNode response : responses)
        {
            assertEquals(404, response.path("relatedHTTPCode").asInt(), "unexpected refusal: " + response);
            assertTrue(response.path("exceptionErrorMessage").asText().contains(ProductManagerHandler.CREATE_SUBSCRIPTION_ACTION_TYPE_GUID),
                       "the refusal does not name the missing governance action type: " + response.path("exceptionErrorMessage").asText());
        }
    }


    /**
     * A product dependency is linked, updated and detached by its relationship GUID, then linked again and
     * detached by the GUIDs of the two products.
     */
    @Test
    @Order(3)
    void productDependencies()
    {
        DigitalProductDependencyProperties properties = new DigitalProductDependencyProperties();

        properties.setLabel("Uses");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String linkPath       = "/digital-products/" + productGUID + "/product-dependencies/" + otherProductGUID + "/attach";
        String relationshipGUID = productManager.create(linkPath, requestBody);

        RelatedMetadataElementSummary dependency = RelatedElementChecks.relationship(retrieve(productGUID),
                                                                                     OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                     otherProductGUID);

        assertNotNull(dependency, "the product does not show the product it depends on");
        assertEquals(relationshipGUID, dependency.getRelationshipHeader().getGUID());

        DigitalProductDependencyProperties update = new DigitalProductDependencyProperties();

        update.setDescription("Needs the other product's data");

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        productManager.call("/digital-product-dependencies/" + relationshipGUID + "/update", updateRequestBody);

        DigitalProductDependencyProperties retrieved = assertInstanceOf(DigitalProductDependencyProperties.class,
                                                                        RelatedElementChecks.relationship(retrieve(productGUID),
                                                                                                          OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                                          otherProductGUID).getRelationshipProperties());

        assertEquals("Needs the other product's data", retrieved.getDescription());
        assertEquals("Uses", retrieved.getLabel(), "a merge update should keep the label");

        productManager.call("/digital-product-dependencies/" + relationshipGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(productGUID), OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName, otherProductGUID),
                    "the product still depends on the detached product");

        productManager.create(linkPath, requestBody);

        assertTrue(RelatedElementChecks.isLinked(retrieve(productGUID), OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName, otherProductGUID),
                   "the product does not show the relinked dependency");

        productManager.call("/digital-products/" + productGUID + "/product-dependencies/" + otherProductGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(productGUID), OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName, otherProductGUID),
                    "the product still depends on the product detached by its ends");
    }


    /**
     * A product manager role linked to a product is visible from the product until it is detached.
     */
    @Test
    @Order(4)
    void productManagers()
    {
        AssignmentScopeProperties properties = new AssignmentScopeProperties();

        properties.setAssignmentType("Owner");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        productManager.call("/digital-products/" + otherProductGUID + "/product-managers/" + roleGUID + "/attach", requestBody);

        assertTrue(RelatedElementChecks.isLinked(retrieve(otherProductGUID), OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, roleGUID),
                   "the product does not show its product manager");

        productManager.call("/digital-products/" + otherProductGUID + "/product-managers/" + roleGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(otherProductGUID), OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName, roleGUID),
                    "the product still shows the detached product manager");
    }


    /**
     * A data contract imported as a YAML document string or as a bean is catalogued, and the data contract
     * generated from the catalogued agreement carries the imported identity.
     */
    @Test
    @Order(5)
    void dataContracts()
    {
        String contractId = UUID.randomUUID().toString();
        String contractName = ViewServicesFvtTestSupport.qualifiedName("DataContract", "string");

        String document = "apiVersion: v3.1.0\n" +
                          "kind: DataContract\n" +
                          "id: " + contractId + "\n" +
                          "name: " + contractName + "\n" +
                          "version: 1.0.0\n" +
                          "status: active\n" +
                          "schema:\n" +
                          "  - name: weekly_measurements\n" +
                          "    physicalType: csv\n" +
                          "    properties:\n" +
                          "      - name: PatientId\n" +
                          "        logicalType: integer\n" +
                          "        primaryKey: true\n" +
                          "      - name: Date\n" +
                          "        logicalType: date\n";

        String stringAgreementGUID = productManager.create("/data-contracts/import-document-string", document);

        collectionsToDelete.add(0, stringAgreementGUID);

        DataContract generated = productManager.get("/agreements/" + stringAgreementGUID + "/data-contract-document", DataContractResponse.class).getDataContract();

        assertNotNull(generated, "generateDataContract returned no data contract");
        assertEquals(contractId, generated.getId());
        assertEquals(contractName, generated.getName());
        assertEquals("1.0.0", generated.getVersion());

        DataContract bean = new DataContract();

        bean.setApiVersion("v3.1.0");
        bean.setKind("DataContract");
        bean.setId(UUID.randomUUID().toString());
        bean.setName(ViewServicesFvtTestSupport.qualifiedName("DataContract", "bean"));
        bean.setVersion("1.0.0");
        bean.setStatus("active");

        String beanAgreementGUID = productManager.create("/data-contracts/import-document", bean);

        collectionsToDelete.add(0, beanAgreementGUID);

        assertNotEquals(stringAgreementGUID, beanAgreementGUID);

        generated = productManager.get("/agreements/" + beanAgreementGUID + "/data-contract-document", DataContractResponse.class).getDataContract();

        assertNotNull(generated, "generateDataContract returned no data contract for the bean import");
        assertEquals(bean.getId(), generated.getId());
        assertEquals(bean.getName(), generated.getName());
    }


    /**
     * A data product imported as a YAML document string or as a bean is catalogued, and the data product
     * generated from the catalogued product carries the imported identity.
     */
    @Test
    @Order(6)
    void dataProducts()
    {
        String productId   = UUID.randomUUID().toString();
        String productName = ViewServicesFvtTestSupport.qualifiedName("DataProduct", "string");

        String document = "apiVersion: v1.0.0\n" +
                          "kind: DataProduct\n" +
                          "id: " + productId + "\n" +
                          "name: " + productName + "\n" +
                          "version: 1.0.0\n" +
                          "status: active\n" +
                          "domain: view-services-fvt\n";

        String stringProductGUID = productManager.create("/data-products/import-document-string", document);

        collectionsToDelete.add(0, stringProductGUID);

        DataProduct generated = productManager.get("/digital-products/" + stringProductGUID + "/data-product-document", DataProductResponse.class).getDataProduct();

        assertNotNull(generated, "generateDataProduct returned no data product");
        assertEquals(productId, generated.getId());
        assertEquals(productName, generated.getName());

        DataProduct bean = new DataProduct();

        bean.setApiVersion("v1.0.0");
        bean.setKind("DataProduct");
        bean.setId(UUID.randomUUID().toString());
        bean.setName(ViewServicesFvtTestSupport.qualifiedName("DataProduct", "bean"));
        bean.setVersion("1.0.0");
        bean.setStatus("active");
        bean.setDomain("view-services-fvt");

        String beanProductGUID = productManager.create("/data-products/import-document", bean);

        collectionsToDelete.add(0, beanProductGUID);

        assertNotEquals(stringProductGUID, beanProductGUID);

        generated = productManager.get("/digital-products/" + beanProductGUID + "/data-product-document", DataProductResponse.class).getDataProduct();

        assertNotNull(generated, "generateDataProduct returned no data product for the bean import");
        assertEquals(bean.getId(), generated.getId());
    }


    /**
     * Publishing a document needs an integration daemon to pass it to.  This suite runs no integration daemon,
     * so the four publish requests are sent to an element that is not one - the digital product - and each must
     * be refused rather than reporting that the document was published.
     */
    @Test
    @Order(7)
    void publishingNeedsAnIntegrationDaemon()
    {
        String root = "/integration-daemons/" + productGUID;

        DataContract dataContract = new DataContract();

        dataContract.setApiVersion("v3.1.0");
        dataContract.setKind("DataContract");
        dataContract.setId(UUID.randomUUID().toString());
        dataContract.setName("Unpublishable contract");
        dataContract.setVersion("1.0.0");
        dataContract.setStatus("active");

        DataProduct dataProduct = new DataProduct();

        dataProduct.setApiVersion("v1.0.0");
        dataProduct.setKind("DataProduct");
        dataProduct.setId(UUID.randomUUID().toString());
        dataProduct.setName("Unpublishable product");
        dataProduct.setVersion("1.0.0");
        dataProduct.setStatus("active");

        List<JsonNode> responses = List.of(productManager.postForFailure(root + "/data-contracts/publish-document-string",
                                                                         "apiVersion: v3.1.0\nkind: DataContract\nid: " + dataContract.getId() + "\nname: Unpublishable\nversion: 1.0.0\nstatus: active\n"),
                                           productManager.postForFailure(root + "/data-contracts/publish-document", dataContract),
                                           productManager.postForFailure(root + "/data-products/publish-document-string",
                                                                         "apiVersion: v1.0.0\nkind: DataProduct\nid: " + dataProduct.getId() + "\nname: Unpublishable\nversion: 1.0.0\nstatus: active\n"),
                                           productManager.postForFailure(root + "/data-products/publish-document", dataProduct));

        for (JsonNode response : responses)
        {
            assertFalse(response.path("exceptionErrorMessage").asText().isEmpty(), "a refused publish did not say why: " + response);
        }
    }


    @AfterAll
    static void deleteEverything()
    {
        for (String guid : assetsToDelete)
        {
            assetMaker.postUnchecked("/assets/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class);
        }

        for (String guid : collectionsToDelete)
        {
            OpenMetadataRootElementResponse existing = collectionManager.postUnchecked("/collections/" + guid + "/retrieve",
                                                                                      new GetRequestBody(),
                                                                                      OpenMetadataRootElementResponse.class);

            if ((existing != null) && (existing.getElement() != null))
            {
                /*
                 * An imported data contract brings its schema - data structures with their data fields - so the
                 * delete must cascade.
                 */
                DeleteElementRequestBody deleteRequestBody = new DeleteElementRequestBody();

                deleteRequestBody.setCascadedDelete(true);

                collectionManager.call("/collections/" + guid + "/delete", deleteRequestBody);
            }

            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + guid + "/retrieve");
        }

        if (roleGUID != null)
        {
            actorManager.call("/actor-roles/" + roleGUID + "/delete", new DeleteElementRequestBody());
        }

        assetsToDelete.clear();
        collectionsToDelete.clear();
    }


    private static NewSubscriptionTypeRequestBody subscriptionType(String       identifier,
                                                                   long         interval,
                                                                   List<String> monitoredResourceGUIDs)
    {
        NewSubscriptionTypeRequestBody requestBody = new NewSubscriptionTypeRequestBody();

        requestBody.setIdentifier(identifier);
        requestBody.setDisplayName("FVT " + identifier + " subscription");
        requestBody.setDescription("Subscription type created by the product manager FVT");
        requestBody.setNotificationInterval(interval);
        requestBody.setMonitoredResourceGUIDs(monitoredResourceGUIDs);

        return requestBody;
    }


    private static <T extends CollectionProperties> T collectionProperties(T      properties,
                                                                           String name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(properties.getClass().getSimpleName(), name));
        properties.setDisplayName("Product manager FVT " + name);

        return properties;
    }


    private static NewElementRequestBody ownAnchor(OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    private static String createCollection(CollectionProperties properties)
    {
        String guid = collectionManager.create("/collections", ownAnchor(properties));

        collectionsToDelete.add(guid);

        return guid;
    }


    private static OpenMetadataRootElement retrieve(String guid)
    {
        OpenMetadataRootElement element = collectionManager.element("/collections/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "collection " + guid + " is missing");

        return element;
    }
}
