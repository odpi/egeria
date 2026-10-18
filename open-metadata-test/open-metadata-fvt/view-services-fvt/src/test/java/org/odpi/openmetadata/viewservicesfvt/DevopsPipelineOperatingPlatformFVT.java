/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.DevopsPipelineFvtSupport.*;

/**
 * DevopsPipelineOperatingPlatformFVT covers the Devops Pipeline OMVS operating platform requests: the operating
 * platform lifecycle (create, create from template, update, retrieve, by-name, search, delete), OperatingPlatformUse
 * to the IT infrastructure it is installed on (a Host made by Asset Maker), OperatingPlatformManifest and
 * SoftwarePackageDependency to collections of software packages (made by Collection Manager), and the
 * SoftwarePackageManifest classification of such a collection.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DevopsPipelineOperatingPlatformFVT
{
    private static String operatingPlatformGUID = null;
    private static String hostGUID              = null;
    private static String collectionGUID        = null;


    /**
     * A new operating platform can be retrieved with the properties it was created with.
     */
    @Test
    @Order(1)
    void createAndRetrieveOperatingPlatform()
    {
        OperatingPlatformProperties properties = named(new OperatingPlatformProperties(), "OperatingPlatform", "linux");

        properties.setOperatingSystem("Linux");
        properties.setOperatingSystemPatchLevel("6.8.0");

        operatingPlatformGUID = devops.create("/operating-platforms", newElement(properties));

        OpenMetadataRootElement element = retrieve(devops, "/operating-platforms/" + operatingPlatformGUID + "/retrieve");

        assertEquals(OpenMetadataType.OPERATING_PLATFORM.typeName, element.getElementHeader().getType().getTypeName());
        assertTrue(element.getProperties() instanceof OperatingPlatformProperties, "retrieve returned " + element.getProperties());

        OperatingPlatformProperties retrieved = (OperatingPlatformProperties) element.getProperties();

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals("Linux", retrieved.getOperatingSystem());
        assertEquals("6.8.0", retrieved.getOperatingSystemPatchLevel());
    }


    /**
     * A merge update changes only the properties it supplies.
     */
    @Test
    @Order(2)
    void updateOperatingPlatform()
    {
        OperatingPlatformProperties properties = new OperatingPlatformProperties();

        properties.setOperatingSystemPatchLevel("6.8.1");

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        assertTrue(devops.post("/operating-platforms/" + operatingPlatformGUID + "/update", requestBody, BooleanResponse.class).getFlag(),
                   "updateOperatingPlatform reported that nothing was updated");

        OperatingPlatformProperties retrieved = (OperatingPlatformProperties) retrieve(devops, "/operating-platforms/" + operatingPlatformGUID + "/retrieve").getProperties();

        assertEquals("6.8.1", retrieved.getOperatingSystemPatchLevel());
        assertEquals("Linux", retrieved.getOperatingSystem(), "a merge update should keep the properties it did not supply");
    }


    /**
     * The operating platform is found by its exact name and by a search string.
     */
    @Test
    @Order(3)
    void findOperatingPlatform()
    {
        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(ViewServicesFvtTestSupport.qualifiedName("OperatingPlatform", "linux"));

        List<OpenMetadataRootElement> named = devops.elements("/operating-platforms/by-name", byName);

        assertEquals(1, named.size(), "by-name should return exactly the one operating platform: " + named);
        assertEquals(operatingPlatformGUID, named.get(0).getElementHeader().getGUID());

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::OperatingPlatform");

        List<OpenMetadataRootElement> found = devops.elements("/operating-platforms/by-search-string", search);

        assertTrue(contains(found, operatingPlatformGUID), "findOperatingPlatforms did not return " + operatingPlatformGUID);

        for (OpenMetadataRootElement element : found)
        {
            assertEquals(OpenMetadataType.OPERATING_PLATFORM.typeName, element.getElementHeader().getType().getTypeName());
        }
    }


    /**
     * An operating platform made from the first one as a template is a new operating platform with the replacement
     * qualified name and the template's other properties.
     */
    @Test
    @Order(4)
    void createOperatingPlatformFromTemplate()
    {
        OperatingPlatformProperties replacement = new OperatingPlatformProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("OperatingPlatform", "from-template"));

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(operatingPlatformGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setReplacementProperties(replacement);

        String copyGUID = devops.create("/operating-platforms/from-template", requestBody);

        assertNotEquals(operatingPlatformGUID, copyGUID);

        OpenMetadataRootElement copy = retrieve(devops, "/operating-platforms/" + copyGUID + "/retrieve");

        assertEquals(OpenMetadataType.OPERATING_PLATFORM.typeName, copy.getElementHeader().getType().getTypeName());
        assertEquals(replacement.getQualifiedName(), qualifiedName(copy));
        assertEquals("Linux", ((OperatingPlatformProperties) copy.getProperties()).getOperatingSystem(),
                     "the template's operating system should be copied");

        devops.call("/operating-platforms/" + copyGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(devops, "/operating-platforms/" + copyGUID + "/retrieve");
    }


    /**
     * OperatingPlatformUse links the operating platform to the host it is installed on, with the deployer; it is
     * visible from both ends until detached.
     */
    @Test
    @Order(5)
    void operatingPlatformUse()
    {
        hostGUID = createAsset(named(new HostProperties(), "Host", "os-host"));

        String path = "/operating-platforms/" + operatingPlatformGUID + "/operating-platform-use/" + hostGUID;

        OperatingPlatformUseProperties useProperties = new OperatingPlatformUseProperties();

        useProperties.setDeployer("devops-pipeline-fvt");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(useProperties);

        devops.call(path + "/attach", requestBody);

        String relationship = OpenMetadataType.OPERATING_PLATFORM_USE_RELATIONSHIP.typeName;

        List<RelatedMetadataElementSummary> uses = related(retrieve(devops, "/operating-platforms/" + operatingPlatformGUID + "/retrieve"), relationship, hostGUID);

        assertEquals(1, uses.size(), "the operating platform does not show the host it is installed on");
        assertTrue(uses.get(0).getRelationshipProperties() instanceof OperatingPlatformUseProperties properties &&
                           "devops-pipeline-fvt".equals(properties.getDeployer()),
                   "the deployer was not saved: " + uses.get(0).getRelationshipProperties());
        assertEquals(1, related(asset(hostGUID), relationship, operatingPlatformGUID).size(),
                     "the host does not show its operating platform");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(retrieve(devops, "/operating-platforms/" + operatingPlatformGUID + "/retrieve"), relationship, hostGUID).isEmpty(),
                   "the operating platform is still installed on the host after detach");
    }


    /**
     * OperatingPlatformManifest links the operating platform to the collection of software packages it includes;
     * it is visible from both ends until detached.
     */
    @Test
    @Order(6)
    void operatingPlatformManifest()
    {
        collectionGUID = createCollection(named(new CollectionProperties(), "Collection", "software-packages"));

        String path = "/operating-platforms/" + operatingPlatformGUID + "/operating-platform-manifests/" + collectionGUID;

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(new OperatingPlatformManifestProperties());

        devops.call(path + "/attach", requestBody);

        String relationship = OpenMetadataType.OPERATING_PLATFORM_MANIFEST_RELATIONSHIP.typeName;

        assertEquals(1, related(retrieve(devops, "/operating-platforms/" + operatingPlatformGUID + "/retrieve"), relationship, collectionGUID).size(),
                     "the operating platform does not show its manifest");
        assertEquals(1, related(collection(collectionGUID), relationship, operatingPlatformGUID).size(),
                     "the collection does not show the operating platform it is the manifest of");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(retrieve(devops, "/operating-platforms/" + operatingPlatformGUID + "/retrieve"), relationship, collectionGUID).isEmpty(),
                   "the manifest is still linked to the operating platform after detach");
    }


    /**
     * SoftwarePackageDependency links an asset to the collection of software packages it depends on; it is
     * visible from both ends until detached.
     */
    @Test
    @Order(7)
    void softwarePackageDependency()
    {
        String path = "/assets/" + hostGUID + "/software-package-dependencies/" + collectionGUID;

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(new SoftwarePackageDependencyProperties());

        devops.call(path + "/attach", requestBody);

        String relationship = OpenMetadataType.SOFTWARE_PACKAGE_DEPENDENCY_RELATIONSHIP.typeName;

        assertEquals(1, related(asset(hostGUID), relationship, collectionGUID).size(),
                     "the host does not show the software packages it depends on");
        assertEquals(1, related(collection(collectionGUID), relationship, hostGUID).size(),
                     "the collection does not show the asset that depends on it");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(asset(hostGUID), relationship, collectionGUID).isEmpty(),
                   "the host still depends on the software packages after detach");
    }


    /**
     * The SoftwarePackageManifest classification is added to the collection and then removed.
     */
    @Test
    @Order(8)
    void softwarePackageManifest()
    {
        String classification = OpenMetadataType.SOFTWARE_PACKAGE_MANIFEST_CLASSIFICATION.typeName;

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(new SoftwarePackageManifestProperties());

        devops.call("/elements/" + collectionGUID + "/software-package-manifest", requestBody);

        assertNotNull(classification(collection(collectionGUID), classification),
                      "the collection is not classified as a software package manifest");

        devops.call("/elements/" + collectionGUID + "/software-package-manifest/remove", new DeleteClassificationRequestBody());

        assertNull(classification(collection(collectionGUID), classification),
                   "the collection is still classified as a software package manifest after remove");
    }


    /**
     * Deleting the operating platform removes it; the host and collection are cleaned up through their own
     * services.
     */
    @Test
    @Order(9)
    void deleteOperatingPlatform()
    {
        devops.call("/operating-platforms/" + operatingPlatformGUID + "/delete", new DeleteElementRequestBody());

        ViewServicesFvtTestSupport.assertGone(devops, "/operating-platforms/" + operatingPlatformGUID + "/retrieve");

        deleteAsset(hostGUID);
        deleteCollection(collectionGUID);
    }
}
