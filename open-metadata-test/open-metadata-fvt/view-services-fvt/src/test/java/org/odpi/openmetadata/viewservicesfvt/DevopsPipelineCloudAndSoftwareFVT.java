/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudProviderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudTenantProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.DependentSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.GeneratedTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.ReusableTechniqueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.ReusableTechniqueUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.RunnableSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.SoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.SoftwareSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.DevopsPipelineFvtSupport.*;

/**
 * DevopsPipelineCloudAndSoftwareFVT covers the Devops Pipeline OMVS cloud classifications (CloudPlatform on a
 * software server platform, CloudProvider on a host, CloudTenant on a software server, CloudService on a software
 * capability) and its software development requests (the DependentSoftwareComponent, SoftwareSource and
 * ReusableTechniqueUse relationships and the GeneratedTarget and ReusableTechnique classifications).
 * <br>
 * The assets are made by Asset Maker, the software component (a collection) by Collection Manager, and the
 * software capability is a network gateway made by this service, since Asset Maker has no delete for software
 * capabilities.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DevopsPipelineCloudAndSoftwareFVT
{
    private static String dependentComponentGUID  = null;
    private static String dependedOnComponentGUID = null;
    private static String softwareComponentGUID   = null;


    /**
     * CloudPlatform classifies a software server platform, with its deployed implementation type, until removed.
     */
    @Test
    @Order(1)
    void cloudPlatform()
    {
        String platformGUID = createAsset(named(new SoftwareServerPlatformProperties(), "SoftwareServerPlatform", "cloud"));

        CloudPlatformProperties properties = new CloudPlatformProperties();

        properties.setDeployedImplementationType("Kubernetes");

        assertClassificationLifecycle("/software-server-platforms/" + platformGUID + "/cloud-platform",
                                      properties,
                                      new DeleteClassificationRequestBody(),
                                      OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION.typeName,
                                      () -> asset(platformGUID),
                                      retrieved -> (retrieved instanceof CloudPlatformProperties cloudPlatformProperties) &&
                                              "Kubernetes".equals(cloudPlatformProperties.getDeployedImplementationType()));

        deleteAsset(platformGUID);
    }


    /**
     * CloudProvider classifies a host, with the provider's name, until removed.
     */
    @Test
    @Order(2)
    void cloudProvider()
    {
        String hostGUID = createAsset(named(new HostProperties(), "Host", "cloud-provider"));

        CloudProviderProperties properties = new CloudProviderProperties();

        properties.setProviderName("FVT Cloud");

        assertClassificationLifecycle("/hosts/" + hostGUID + "/cloud-provider",
                                      properties,
                                      new DeleteClassificationRequestBody(),
                                      OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION.typeName,
                                      () -> asset(hostGUID),
                                      retrieved -> (retrieved instanceof CloudProviderProperties cloudProviderProperties) &&
                                              "FVT Cloud".equals(cloudProviderProperties.getProviderName()));

        deleteAsset(hostGUID);
    }


    /**
     * CloudTenant classifies a software server, with the tenant's name and type, until removed.
     */
    @Test
    @Order(3)
    void cloudTenant()
    {
        String serverGUID = createAsset(named(new SoftwareServerProperties(), "SoftwareServer", "cloud-tenant"));

        CloudTenantProperties properties = new CloudTenantProperties();

        properties.setTenantName("fvt-tenant");
        properties.setTenantType("Dedicated");

        assertClassificationLifecycle("/software-servers/" + serverGUID + "/cloud-tenant",
                                      properties,
                                      new DeleteClassificationRequestBody(),
                                      OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeName,
                                      () -> asset(serverGUID),
                                      retrieved -> (retrieved instanceof CloudTenantProperties cloudTenantProperties) &&
                                              "fvt-tenant".equals(cloudTenantProperties.getTenantName()) &&
                                              "Dedicated".equals(cloudTenantProperties.getTenantType()));

        deleteAsset(serverGUID);
    }


    /**
     * CloudService classifies a software capability (here a network gateway), with its offering name and service
     * type, until removed.
     */
    @Test
    @Order(4)
    void cloudService()
    {
        String capabilityGUID = devops.create("/network-gateways", newElement(named(new NetworkGatewayProperties(), "NetworkGateway", "cloud-service")));

        CloudServiceProperties properties = new CloudServiceProperties();

        properties.setOfferingName("FVT Object Store");
        properties.setServiceType("Storage");

        assertClassificationLifecycle("/software-capabilities/" + capabilityGUID + "/cloud-service",
                                      properties,
                                      new DeleteClassificationRequestBody(),
                                      OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName,
                                      () -> retrieve(devops, "/network-gateways/" + capabilityGUID + "/retrieve"),
                                      retrieved -> (retrieved instanceof CloudServiceProperties cloudServiceProperties) &&
                                              "FVT Object Store".equals(cloudServiceProperties.getOfferingName()) &&
                                              "Storage".equals(cloudServiceProperties.getServiceType()));

        devops.call("/network-gateways/" + capabilityGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(devops, "/network-gateways/" + capabilityGUID + "/retrieve");
    }


    /**
     * DependentSoftwareComponent links one runnable software component to another that it depends on; it is
     * visible from both ends until detached.
     */
    @Test
    @Order(5)
    void dependentSoftwareComponent()
    {
        dependentComponentGUID  = createAsset(named(new RunnableSoftwareComponentProperties(), "RunnableSoftwareComponent", "dependent"));
        dependedOnComponentGUID = createAsset(named(new RunnableSoftwareComponentProperties(), "RunnableSoftwareComponent", "depended-on"));

        assertRelationshipLifecycle("/runnable-software-components/" + dependentComponentGUID + "/dependent-software-component/" + dependedOnComponentGUID,
                                    new DependentSoftwareComponentProperties(),
                                    OpenMetadataType.DEPENDENT_SOFTWARE_COMPONENT_RELATIONSHIP.typeName,
                                    dependentComponentGUID,
                                    () -> asset(dependentComponentGUID),
                                    dependedOnComponentGUID,
                                    () -> asset(dependedOnComponentGUID));
    }


    /**
     * SoftwareSource links an asset to the software component it was built from; it is visible from both ends
     * until detached.
     */
    @Test
    @Order(6)
    void softwareSource()
    {
        softwareComponentGUID = createCollection(named(new SoftwareComponentProperties(), "SoftwareComponent", "source"));

        assertEquals(OpenMetadataType.SOFTWARE_COMPONENT.typeName, collection(softwareComponentGUID).getElementHeader().getType().getTypeName());

        assertRelationshipLifecycle("/assets/" + dependentComponentGUID + "/software-source/" + softwareComponentGUID,
                                    new SoftwareSourceProperties(),
                                    OpenMetadataType.SOFTWARE_SOURCE_RELATIONSHIP.typeName,
                                    dependentComponentGUID,
                                    () -> asset(dependentComponentGUID),
                                    softwareComponentGUID,
                                    () -> collection(softwareComponentGUID));
    }


    /**
     * The ReusableTechnique classification marks the software component as a technique, which the runnable
     * component then uses through ReusableTechniqueUse.  Removing the classification is checked with the
     * MetadataSourceRequestBody that the remove request takes.
     */
    @Test
    @Order(7)
    void reusableTechnique()
    {
        String classification = OpenMetadataType.REUSABLE_TECHNIQUE_CLASSIFICATION.typeName;

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(new ReusableTechniqueProperties());

        devops.call("/elements/" + softwareComponentGUID + "/reusable-technique", requestBody);

        assertNotNull(classification(collection(softwareComponentGUID), classification),
                      "the software component is not classified as a reusable technique");

        assertRelationshipLifecycle("/elements/" + dependentComponentGUID + "/reusable-technique-use/" + softwareComponentGUID,
                                    new ReusableTechniqueUseProperties(),
                                    OpenMetadataType.REUSABLE_TECHNIQUE_USE_RELATIONSHIP.typeName,
                                    dependentComponentGUID,
                                    () -> asset(dependentComponentGUID),
                                    softwareComponentGUID,
                                    () -> collection(softwareComponentGUID));

        devops.call("/elements/" + softwareComponentGUID + "/reusable-technique/remove", new MetadataSourceRequestBody());

        assertNull(classification(collection(softwareComponentGUID), classification),
                   "the software component is still a reusable technique after remove");
    }


    /**
     * GeneratedTarget classifies the runnable component as built output, with its build details, until removed.
     */
    @Test
    @Order(8)
    void generatedTarget()
    {
        GeneratedTargetProperties properties = new GeneratedTargetProperties();

        properties.setPurpose("release");
        properties.setBuildId("build-42");
        properties.setBuildTool("Gradle");
        properties.setBuildToolVersion("9.7.1");

        assertClassificationLifecycle("/elements/" + dependedOnComponentGUID + "/generated-target",
                                      properties,
                                      new MetadataSourceRequestBody(),
                                      OpenMetadataType.GENERATED_TARGET_CLASSIFICATION.typeName,
                                      () -> asset(dependedOnComponentGUID),
                                      retrieved -> (retrieved instanceof GeneratedTargetProperties generatedTargetProperties) &&
                                              "build-42".equals(generatedTargetProperties.getBuildId()) &&
                                              "Gradle".equals(generatedTargetProperties.getBuildTool()));
    }


    /**
     * Remove the elements this class made.
     */
    @AfterAll
    static void cleanUp()
    {
        deleteAsset(dependentComponentGUID);
        deleteAsset(dependedOnComponentGUID);
        deleteCollection(softwareComponentGUID);
    }


    /**
     * Classify an element, check the classification and its properties are visible, then remove it and check it
     * has gone.
     */
    private static void assertClassificationLifecycle(String                                          path,
                                                      ClassificationBeanProperties                    properties,
                                                      Object                                          removeRequestBody,
                                                      String                                          classificationName,
                                                      Supplier<OpenMetadataRootElement>               retriever,
                                                      Predicate<ClassificationBeanProperties>                     expectedProperties)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        devops.call(path, requestBody);

        ElementClassification classification = classification(retriever.get(), classificationName);

        assertNotNull(classification, path + " did not add the " + classificationName + " classification");
        assertTrue(expectedProperties.test(classification.getClassificationProperties()),
                   "the " + classificationName + " properties were not saved: " + classification.getClassificationProperties());

        devops.call(path + "/remove", removeRequestBody);

        assertNull(classification(retriever.get(), classificationName),
                   path + "/remove did not remove the " + classificationName + " classification");
    }


    /**
     * Attach a relationship, check it is visible from both ends, detach it and check it has gone.
     */
    private static void assertRelationshipLifecycle(String                            path,
                                                    RelationshipBeanProperties        properties,
                                                    String                            relationshipTypeName,
                                                    String                            end1GUID,
                                                    Supplier<OpenMetadataRootElement> end1,
                                                    String                            end2GUID,
                                                    Supplier<OpenMetadataRootElement> end2)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        devops.call(path + "/attach", requestBody);

        assertEquals(1, related(end1.get(), relationshipTypeName, end2GUID).size(),
                     "end 1 does not show the " + relationshipTypeName + " relationship after " + path + "/attach");
        assertEquals(1, related(end2.get(), relationshipTypeName, end1GUID).size(),
                     "end 2 does not show the " + relationshipTypeName + " relationship after " + path + "/attach");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(end1.get(), relationshipTypeName, end2GUID).isEmpty(),
                   "end 1 still shows the " + relationshipTypeName + " relationship after " + path + "/detach");
        assertTrue(related(end2.get(), relationshipTypeName, end1GUID).isEmpty(),
                   "end 2 still shows the " + relationshipTypeName + " relationship after " + path + "/detach");
    }
}
