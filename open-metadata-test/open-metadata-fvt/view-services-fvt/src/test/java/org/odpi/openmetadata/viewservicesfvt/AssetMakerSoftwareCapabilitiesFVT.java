/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.GovernanceServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.IntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.SupportedGovernanceServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.IntegrationGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFVT.contains;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.named;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.newElement;

/**
 * AssetMakerSoftwareCapabilitiesFVT covers the Asset Maker's software capability requests (create, create from
 * template, update, retrieve, search, by name, by deployed implementation type, by hosting infrastructure),
 * capability asset use, the governance engine/service and integration group/connector registrations, and the
 * catalog targets of an integration connector.
 * <br>
 * Asset Maker has no software capability delete request, so they are deleted with deleteAsset, which deletes any
 * element.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetMakerSoftwareCapabilitiesFVT
{
    private static final PropertyHelper propertyHelper = new PropertyHelper();

    private static final String DEPLOYED_IMPLEMENTATION_TYPE = ViewServicesFvtTestSupport.runSearchString() + "::capability-type";

    private static String serverGUID               = null;
    private static String capabilityGUID           = null;
    private static String copiedCapabilityGUID     = null;
    private static String dataFileGUID             = null;
    private static String governanceEngineGUID     = null;
    private static String governanceServiceGUID    = null;
    private static String integrationGroupGUID     = null;
    private static String integrationConnectorGUID = null;

    private static final List<String> createdGUIDs = new ArrayList<>();


    /**
     * A software capability is created, read back and updated.
     */
    @Test
    @Order(1)
    void createRetrieveAndUpdateCapability()
    {
        SoftwareCapabilityProperties properties = named(new SoftwareCapabilityProperties(), "SoftwareCapability", "asset-maker-capability");

        properties.setDeployedImplementationType(DEPLOYED_IMPLEMENTATION_TYPE);
        properties.setPatchLevel("1");

        capabilityGUID = created(assetMaker.create("/software-capabilities", newElement(properties)));

        OpenMetadataRootElement capability = retrieveCapability(capabilityGUID);

        assertEquals(OpenMetadataType.SOFTWARE_CAPABILITY.typeName, capability.getElementHeader().getType().getTypeName());
        assertEquals(properties.getQualifiedName(), ((SoftwareCapabilityProperties) capability.getProperties()).getQualifiedName());
        assertEquals("1", ((SoftwareCapabilityProperties) capability.getProperties()).getPatchLevel());

        SoftwareCapabilityProperties update = new SoftwareCapabilityProperties();

        update.setPatchLevel("2");
        update.setDescription("Updated by " + AssetMakerSoftwareCapabilitiesFVT.class.getSimpleName());

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setProperties(update);

        assetMaker.post("/software-capabilities/" + capabilityGUID + "/update", requestBody, BooleanResponse.class);

        SoftwareCapabilityProperties updated = (SoftwareCapabilityProperties) retrieveCapability(capabilityGUID).getProperties();

        assertEquals("2", updated.getPatchLevel());
        assertEquals(update.getDescription(), updated.getDescription());
        assertEquals(properties.getQualifiedName(), updated.getQualifiedName(), "a merge update should keep the qualified name");
    }


    /**
     * createSoftwareCapabilityFromTemplate copies the capability with a new qualified name.
     */
    @Test
    @Order(2)
    void createCapabilityFromTemplate()
    {
        SoftwareCapabilityProperties replacementProperties = named(new SoftwareCapabilityProperties(), "SoftwareCapability", "asset-maker-capability-copy");

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(capabilityGUID);
        requestBody.setReplacementProperties(replacementProperties);

        copiedCapabilityGUID = created(assetMaker.create("/software-capabilities/from-template", requestBody));

        assertNotEquals(capabilityGUID, copiedCapabilityGUID);

        SoftwareCapabilityProperties copy = (SoftwareCapabilityProperties) retrieveCapability(copiedCapabilityGUID).getProperties();

        assertEquals(replacementProperties.getQualifiedName(), copy.getQualifiedName());
        assertEquals("2", copy.getPatchLevel(), "the template's patch level was not copied");
        assertEquals(DEPLOYED_IMPLEMENTATION_TYPE, copy.getDeployedImplementationType());
    }


    /**
     * The capability queries find both capabilities.
     */
    @Test
    @Order(3)
    void findCapabilities()
    {
        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        List<OpenMetadataRootElement> found = assetMaker.elements("/software-capabilities/search", search);

        assertTrue(contains(found, capabilityGUID) && contains(found, copiedCapabilityGUID), "findSoftwareCapabilities missed a capability");

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(ViewServicesFvtTestSupport.qualifiedName("SoftwareCapability", "asset-maker-capability"));

        found = assetMaker.elements("/software-capabilities/by-name", byName);

        assertTrue(contains(found, capabilityGUID), "getSoftwareCapabilitiesByName did not find the capability");
        assertFalse(contains(found, copiedCapabilityGUID), "getSoftwareCapabilitiesByName returned the copy for the original's name");

        FilterRequestBody byType = new FilterRequestBody();

        byType.setFilter(DEPLOYED_IMPLEMENTATION_TYPE);

        found = assetMaker.elements("/software-capabilities/by-deployed-implementation-type", byType);

        assertTrue(contains(found, capabilityGUID) && contains(found, copiedCapabilityGUID),
                   "getSoftwareCapabilitiesByDeployedImplementationType missed a capability");
    }


    /**
     * A capability is linked to the server that supports it, and listed as the server's capability.
     */
    @Test
    @Order(4)
    void supportedSoftwareCapability()
    {
        serverGUID = created(AssetMakerFvtSupport.createAsset(named(new SoftwareServerProperties(), "SoftwareServer", "capability-host")));

        assetMaker.call("/assets/" + serverGUID + "/supported-software-capabilities/" + capabilityGUID + "/attach", new NewRelationshipRequestBody());

        AssetMakerFvtSupport.assertLinked(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName, serverGUID, capabilityGUID);

        List<OpenMetadataRootElement> capabilities = assetMaker.elements("/infrastructure/" + serverGUID + "/software-capabilities", new ResultsRequestBody());

        assertTrue(contains(capabilities, capabilityGUID), "getSoftwareCapabilitiesForInfrastructure did not return the capability");
        assertFalse(contains(capabilities, copiedCapabilityGUID), "getSoftwareCapabilitiesForInfrastructure returned an unlinked capability");

        assetMaker.call("/assets/" + serverGUID + "/supported-software-capabilities/" + capabilityGUID + "/detach", new DeleteRelationshipRequestBody());

        AssetMakerFvtSupport.assertNotLinked(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName, serverGUID, capabilityGUID);
        assertFalse(contains(assetMaker.elements("/infrastructure/" + serverGUID + "/software-capabilities", new ResultsRequestBody()), capabilityGUID),
                    "getSoftwareCapabilitiesForInfrastructure still returns the detached capability");
    }


    /**
     * A capability uses an asset; getCapabilityUse lists the capability from the asset.
     */
    @Test
    @Order(5)
    void capabilityAssetUse()
    {
        dataFileGUID = created(AssetMakerFvtSupport.createAsset(named(new DataFileProperties(), "DataFile", "capability-file")));

        CapabilityAssetUseProperties properties = new CapabilityAssetUseProperties();

        properties.setUseType(CapabilityAssetUseType.OWNS);

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        assetMaker.call("/software-capabilities/" + capabilityGUID + "/assets/" + dataFileGUID + "/attach", requestBody);

        AssetMakerFvtSupport.assertLinked(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName, capabilityGUID, dataFileGUID);
        assertTrue(contains(assetMaker.elements("/assets/" + dataFileGUID + "/capability-use", new ResultsRequestBody()), capabilityGUID),
                   "getCapabilityUse did not return the capability");
    }


    /**
     * removeCapabilityAssetUse removes the relationship.
     * <br>
     * Observed: the request succeeds but the CapabilityAssetUse relationship is still there.
     * SoftwareCapabilityHandler.removeCapabilityAssetUse calls detachRelatedElementsInStore with the asset as end 1
     * and the capability as end 2, the reverse of addCapabilityAssetUse and of the type definition, so nothing
     * matches.
     */
    @Test
    @Order(51)
    void removeCapabilityAssetUse()
    {
        assetMaker.call("/software-capabilities/" + capabilityGUID + "/assets/" + dataFileGUID + "/detach", new DeleteRelationshipRequestBody());

        AssetMakerFvtSupport.assertNotLinked(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName, capabilityGUID, dataFileGUID);
        assertTrue(assetMaker.elements("/assets/" + dataFileGUID + "/capability-use", new ResultsRequestBody()).isEmpty(),
                   "getCapabilityUse still returns the detached capability");
    }


    /**
     * A governance service is registered with a governance engine, its registration updated, listed from the
     * service, and removed.
     */
    @Test
    @Order(60)
    void supportedGovernanceService()
    {
        governanceEngineGUID  = created(assetMaker.create("/software-capabilities",
                                                          newElement(named(new GovernanceEngineProperties(), "GovernanceEngine", "asset-maker-engine"))));
        governanceServiceGUID = created(AssetMakerFvtSupport.createAsset(named(new GovernanceServiceProperties(), "GovernanceService", "asset-maker-service")));

        SupportedGovernanceServiceProperties properties = new SupportedGovernanceServiceProperties();

        properties.setRequestType("fvt-request");
        properties.setServiceRequestType("fvt-service-request");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String relationshipGUID = assetMaker.create("/governance-engines/" + governanceEngineGUID + "/supported-governance-services/" + governanceServiceGUID + "/attach",
                                                    requestBody);

        AssetMakerFvtSupport.assertLinked(OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName, governanceEngineGUID, governanceServiceGUID);
        assertTrue(contains(assetMaker.elements("/governance-services/" + governanceServiceGUID + "/governance-engines", new ResultsRequestBody()), governanceEngineGUID),
                   "getGovernanceEngines did not return the engine");

        properties.setServiceRequestType("fvt-updated-service-request");

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setProperties(properties);

        assetMaker.call("/supported-governance-services/" + relationshipGUID + "/update", updateRequestBody);

        assertTrue(AssetMakerFvtSupport.relationshipPropertiesJSON(governanceEngineGUID, governanceServiceGUID).contains("fvt-updated-service-request"),
                   "the update to the supported governance service is not visible");

        assetMaker.call("/supported-governance-services/" + relationshipGUID + "/detach", new DeleteRelationshipRequestBody());

        AssetMakerFvtSupport.assertNotLinked(OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName, governanceEngineGUID, governanceServiceGUID);
        assertTrue(assetMaker.elements("/governance-services/" + governanceServiceGUID + "/governance-engines", new ResultsRequestBody()).isEmpty(),
                   "getGovernanceEngines still returns the engine");
    }


    /**
     * An integration connector is registered with an integration group, listed from the connector, and removed.
     */
    @Test
    @Order(70)
    void registeredIntegrationConnector()
    {
        integrationGroupGUID     = created(assetMaker.create("/software-capabilities",
                                                             newElement(named(new IntegrationGroupProperties(), "IntegrationGroup", "asset-maker-group"))));
        integrationConnectorGUID = created(AssetMakerFvtSupport.createAsset(named(new IntegrationConnectorProperties(), "IntegrationConnector", "asset-maker-connector")));

        assetMaker.call("/integration-groups/" + integrationGroupGUID + "/registered-integration-connectors/" + integrationConnectorGUID + "/attach",
                        new NewRelationshipRequestBody());

        AssetMakerFvtSupport.assertLinked(OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName, integrationGroupGUID, integrationConnectorGUID);
        assertTrue(contains(assetMaker.elements("/integration-connectors/" + integrationConnectorGUID + "/integration-groups", new ResultsRequestBody()), integrationGroupGUID),
                   "getIntegrationGroups did not return the group");

        assetMaker.call("/integration-groups/" + integrationGroupGUID + "/registered-integration-connectors/" + integrationConnectorGUID + "/detach",
                        new DeleteRelationshipRequestBody());

        AssetMakerFvtSupport.assertNotLinked(OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName, integrationGroupGUID, integrationConnectorGUID);
        assertTrue(assetMaker.elements("/integration-connectors/" + integrationConnectorGUID + "/integration-groups", new ResultsRequestBody()).isEmpty(),
                   "getIntegrationGroups still returns the group");
    }


    /**
     * Catalog targets are added to the integration connector, read, updated, listed and removed - once by
     * relationship GUID and once by the two element GUIDs.
     */
    @Test
    @Order(80)
    void catalogTargets()
    {
        CatalogTargetProperties properties = new CatalogTargetProperties();

        properties.setCatalogTargetName("fvt-target");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String relationshipGUID = assetMaker.create("/integration-connectors/" + integrationConnectorGUID + "/catalog-targets/" + serverGUID, requestBody);

        OpenMetadataRelationship relationship = getCatalogTarget(relationshipGUID);

        assertEquals(integrationConnectorGUID, relationship.getElementGUIDAtEnd1());
        assertEquals(serverGUID, relationship.getElementGUIDAtEnd2());
        assertEquals("fvt-target", propertyHelper.getStringProperty("test", "catalogTargetName", relationship.getRelationshipProperties(), "catalogTargets"));

        properties.setCatalogTargetName("fvt-target-updated");

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setProperties(properties);

        assetMaker.call("/catalog-targets/" + relationshipGUID + "/update", updateRequestBody);

        assertEquals("fvt-target-updated",
                     propertyHelper.getStringProperty("test", "catalogTargetName", getCatalogTarget(relationshipGUID).getRelationshipProperties(), "catalogTargets"));

        properties.setCatalogTargetName("fvt-second-target");

        assetMaker.create("/integration-connectors/" + integrationConnectorGUID + "/catalog-targets/" + dataFileGUID, requestBody);

        List<OpenMetadataRootElement> targets = assetMaker.elements("/integration-connectors/" + integrationConnectorGUID + "/catalog-targets", new ResultsRequestBody());

        assertTrue(contains(targets, serverGUID) && contains(targets, dataFileGUID), "getCatalogTargets missed a catalog target");

        assetMaker.call("/catalog-targets/" + relationshipGUID + "/remove", new DeleteRelationshipRequestBody());
        assetMaker.call("/integration-connectors/" + integrationConnectorGUID + "/catalog-targets/" + dataFileGUID + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(assetMaker.elements("/integration-connectors/" + integrationConnectorGUID + "/catalog-targets", new ResultsRequestBody()).isEmpty(),
                   "getCatalogTargets still returns removed catalog targets");
    }


    /**
     * Delete everything that was created.
     */
    @Test
    @Order(90)
    void deleteEverything()
    {
        for (String guid : List.copyOf(createdGUIDs))
        {
            AssetMakerFvtSupport.delete(guid);
            createdGUIDs.remove(guid);
        }
    }


    @AfterAll
    static void cleanUp()
    {
        for (String guid : createdGUIDs)
        {
            assetMaker.postUnchecked("/assets/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class);
        }
    }


    private static String created(String guid)
    {
        createdGUIDs.add(guid);

        return guid;
    }


    private static OpenMetadataRootElement retrieveCapability(String guid)
    {
        OpenMetadataRootElement capability = assetMaker.element("/software-capabilities/" + guid, new GetRequestBody());

        assertNotNull(capability, "getSoftwareCapabilityByGUID returned nothing for " + guid);
        assertEquals(guid, capability.getElementHeader().getGUID());

        return capability;
    }


    private static OpenMetadataRelationship getCatalogTarget(String relationshipGUID)
    {
        OpenMetadataRelationship relationship = assetMaker.post("/catalog-targets/" + relationshipGUID,
                                                                new GetRequestBody(),
                                                                OpenMetadataRelationshipResponse.class).getElement();

        assertNotNull(relationship, "getCatalogTarget returned nothing");
        assertEquals(relationshipGUID, relationship.getRelationshipGUID());

        return relationship;
    }
}
