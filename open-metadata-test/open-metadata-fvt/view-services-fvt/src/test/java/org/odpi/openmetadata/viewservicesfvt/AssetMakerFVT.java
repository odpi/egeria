/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.ActivityStatusFilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ActivityStatusSearchString;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ContentStatusFilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ContentStatusSearchString;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeploymentStatusFilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeploymentStatusSearchString;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.DeployedAPIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.AuditLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.named;

/**
 * AssetMakerFVT covers the Asset Maker's asset lifecycle requests: create (directly and from a template),
 * retrieve, update, the name/search queries, the process, infrastructure and data-asset queries with their status
 * filters, the log/API/encoding classifications, and delete.
 * <br>
 * The actions, software capabilities and the relationship attach/detach requests are covered by
 * AssetMakerActionsFVT, AssetMakerSoftwareCapabilitiesFVT and AssetMakerRelationshipsFVT.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetMakerFVT
{
    private static final String DATA_CATEGORY           = ViewServicesFvtTestSupport.runSearchString() + "::data-category";
    private static final String PROCESS_CATEGORY        = ViewServicesFvtTestSupport.runSearchString() + "::process-category";
    private static final String INFRASTRUCTURE_CATEGORY = ViewServicesFvtTestSupport.runSearchString() + "::infrastructure-category";

    private static String dataFileGUID       = null;
    private static String fromTemplateGUID   = null;
    private static String processGUID        = null;
    private static String hostGUID           = null;
    private static String deployedAPIGUID    = null;

    private static final List<String> createdGUIDs = new ArrayList<>();


    /**
     * createAsset returns a GUID and getAssetByGUID returns the asset with the properties it was created with.
     */
    @Test
    @Order(1)
    void createAndRetrieveAsset()
    {
        DataFileProperties properties = named(new DataFileProperties(), "DataFile", "asset-maker-file");

        properties.setDescription("Created by " + AssetMakerFVT.class.getSimpleName());
        properties.setCategory(DATA_CATEGORY);
        properties.setContentStatus(ContentStatus.DRAFT);
        properties.setFileType("csv");

        dataFileGUID = AssetMakerFvtSupport.createAsset(properties);
        createdGUIDs.add(dataFileGUID);

        OpenMetadataRootElement asset = retrieveAsset(dataFileGUID);

        assertEquals(OpenMetadataType.DATA_FILE.typeName, asset.getElementHeader().getType().getTypeName());
        assertTrue(asset.getProperties() instanceof DataFileProperties, "wrong properties: " + asset.getProperties());

        DataFileProperties retrieved = (DataFileProperties) asset.getProperties();

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals(properties.getDisplayName(), retrieved.getDisplayName());
        assertEquals(properties.getDescription(), retrieved.getDescription());
        assertEquals(ContentStatus.DRAFT, retrieved.getContentStatus());
        assertEquals("csv", retrieved.getFileType());
    }


    /**
     * updateAsset merges the new properties into the asset.
     */
    @Test
    @Order(2)
    void updateAsset()
    {
        DataFileProperties properties = new DataFileProperties();

        properties.setDescription("Updated by " + AssetMakerFVT.class.getSimpleName());
        properties.setContentStatus(ContentStatus.ACTIVE);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        assetMaker.post("/assets/" + dataFileGUID + "/update", requestBody, BooleanResponse.class);

        DataFileProperties retrieved = (DataFileProperties) retrieveAsset(dataFileGUID).getProperties();

        assertEquals(properties.getDescription(), retrieved.getDescription());
        assertEquals(ContentStatus.ACTIVE, retrieved.getContentStatus());
        assertEquals("csv", retrieved.getFileType(), "a merge update should keep the properties it did not supply");
        assertTrue(retrieved.getQualifiedName().endsWith("asset-maker-file"));
    }


    /**
     * getAssetsByName finds the asset by its exact qualified name, and findAssets by the run's search string.
     */
    @Test
    @Order(3)
    void findAssetsByNameAndSearchString()
    {
        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(ViewServicesFvtTestSupport.qualifiedName("DataFile", "asset-maker-file"));

        assertTrue(contains(assetMaker.elements("/assets/by-name", byName), dataFileGUID),
                   "getAssetsByName did not find " + dataFileGUID);

        SearchStringRequestBody bySearch = new SearchStringRequestBody();

        bySearch.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        assertTrue(contains(assetMaker.elements("/assets/by-search-string", bySearch), dataFileGUID),
                   "findAssets did not find " + dataFileGUID);

        byName.setFilter(ViewServicesFvtTestSupport.qualifiedName("DataFile", "no-such-file"));

        assertTrue(assetMaker.elements("/assets/by-name", byName).isEmpty(), "getAssetsByName found an asset that does not exist");
    }


    /**
     * createAssetFromTemplate copies the template asset, taking the replacement properties supplied.
     */
    @Test
    @Order(4)
    void createAssetFromTemplate()
    {
        DataFileProperties replacementProperties = named(new DataFileProperties(), "DataFile", "asset-maker-file-from-template");

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(dataFileGUID);
        requestBody.setReplacementProperties(replacementProperties);
        requestBody.setIsOwnAnchor(true);

        fromTemplateGUID = assetMaker.create("/assets/from-template", requestBody);
        createdGUIDs.add(fromTemplateGUID);

        assertNotEquals(dataFileGUID, fromTemplateGUID);

        OpenMetadataRootElement copy = retrieveAsset(fromTemplateGUID);

        assertEquals(OpenMetadataType.DATA_FILE.typeName, copy.getElementHeader().getType().getTypeName());

        DataFileProperties copied = (DataFileProperties) copy.getProperties();

        assertEquals(replacementProperties.getQualifiedName(), copied.getQualifiedName());
        assertEquals("csv", copied.getFileType(), "the template's file type was not copied");

        AssetMakerFvtSupport.assertLinked(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName, fromTemplateGUID, dataFileGUID);
    }


    /**
     * findProcesses and getProcessesByCategory return a process, and its activity status filters it in or out.
     */
    @Test
    @Order(5)
    void processQueries()
    {
        ProcessProperties properties = named(new ProcessProperties(), "Process", "asset-maker-process");

        properties.setCategory(PROCESS_CATEGORY);
        properties.setActivityStatus(ActivityStatus.IN_PROGRESS);

        processGUID = AssetMakerFvtSupport.createAsset(properties);
        createdGUIDs.add(processGUID);

        ActivityStatusSearchString search = new ActivityStatusSearchString();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());
        search.setActivityStatusList(List.of(ActivityStatus.IN_PROGRESS));

        List<OpenMetadataRootElement> found = assetMaker.elements("/processes/find-by-search-string", search);

        assertTrue(contains(found, processGUID), "findProcesses did not find the in-progress process");
        assertFalse(contains(found, dataFileGUID), "findProcesses returned a data file");

        search.setActivityStatusList(List.of(ActivityStatus.COMPLETED));

        assertFalse(contains(assetMaker.elements("/processes/find-by-search-string", search), processGUID),
                    "findProcesses returned an in-progress process for COMPLETED");

        ActivityStatusFilterRequestBody byCategory = new ActivityStatusFilterRequestBody();

        byCategory.setFilter(PROCESS_CATEGORY);

        assertTrue(contains(assetMaker.elements("/processes/by-category", byCategory), processGUID),
                   "getProcessesByCategory did not find the process");

        byCategory.setActivityStatusList(List.of(ActivityStatus.CANCELLED));

        assertFalse(contains(assetMaker.elements("/processes/by-category", byCategory), processGUID),
                    "getProcessesByCategory returned an in-progress process for CANCELLED");
    }


    /**
     * findInfrastructure and getInfrastructureByCategory return a host, filtered by deployment status.
     */
    @Test
    @Order(6)
    void infrastructureQueries()
    {
        HostProperties properties = named(new HostProperties(), "Host", "asset-maker-host");

        properties.setCategory(INFRASTRUCTURE_CATEGORY);
        properties.setDeploymentStatus(DeploymentStatus.ACTIVE);

        hostGUID = AssetMakerFvtSupport.createAsset(properties);
        createdGUIDs.add(hostGUID);

        DeploymentStatusSearchString search = new DeploymentStatusSearchString();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());
        search.setDeploymentStatusList(List.of(DeploymentStatus.ACTIVE));

        List<OpenMetadataRootElement> found = assetMaker.elements("/infrastructure-assets/by-search-string", search);

        assertTrue(contains(found, hostGUID), "findInfrastructure did not find the active host");
        assertFalse(contains(found, dataFileGUID), "findInfrastructure returned a data file");

        search.setDeploymentStatusList(List.of(DeploymentStatus.DISABLED));

        assertFalse(contains(assetMaker.elements("/infrastructure-assets/by-search-string", search), hostGUID),
                    "findInfrastructure returned an active host for DISABLED");

        DeploymentStatusFilterRequestBody byCategory = new DeploymentStatusFilterRequestBody();

        byCategory.setFilter(INFRASTRUCTURE_CATEGORY);

        assertTrue(contains(assetMaker.elements("/infrastructure-assets/by-category", byCategory), hostGUID),
                   "getInfrastructureByCategory did not find the host");

        byCategory.setDeploymentStatusList(List.of(DeploymentStatus.FAILED));

        assertFalse(contains(assetMaker.elements("/infrastructure-assets/by-category", byCategory), hostGUID),
                    "getInfrastructureByCategory returned an active host for FAILED");
    }


    /**
     * findDataAssets and getDataAssetsByCategory return the data file, filtered by content status.
     */
    @Test
    @Order(7)
    void dataAssetQueries()
    {
        ContentStatusSearchString search = new ContentStatusSearchString();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());
        search.setContentStatusList(List.of(ContentStatus.ACTIVE));

        List<OpenMetadataRootElement> found = assetMaker.elements("/data-assets/by-search-string", search);

        assertTrue(contains(found, dataFileGUID), "findDataAssets did not find the active data file");
        assertFalse(contains(found, hostGUID), "findDataAssets returned a host");

        search.setContentStatusList(List.of(ContentStatus.OBSOLETE));

        assertFalse(contains(assetMaker.elements("/data-assets/by-search-string", search), dataFileGUID),
                    "findDataAssets returned an active data file for OBSOLETE");

        ContentStatusFilterRequestBody byCategory = new ContentStatusFilterRequestBody();

        byCategory.setFilter(DATA_CATEGORY);

        assertTrue(contains(assetMaker.elements("/data-assets/by-category", byCategory), dataFileGUID),
                   "getDataAssetsByCategory did not find the data file");

        byCategory.setContentStatusList(List.of(ContentStatus.DEPRECATED));

        assertFalse(contains(assetMaker.elements("/data-assets/by-category", byCategory), dataFileGUID),
                    "getDataAssetsByCategory returned an active data file for DEPRECATED");
    }


    /**
     * Each of the log classifications can be set on an asset and removed again.
     */
    @Test
    @Order(8)
    void logClassifications()
    {
        AuditLogProperties auditLogProperties = new AuditLogProperties();

        auditLogProperties.setProcess("view-services-fvt");

        Map<String, ClassificationProperties> classifications = Map.of("audit-log", auditLogProperties);

        for (Map.Entry<String, String> entry : Map.of("audit-log", OpenMetadataType.AUDIT_LOG_CLASSIFICATION.typeName,
                                                      "lineage-log", OpenMetadataType.LINEAGE_LOG_CLASSIFICATION.typeName,
                                                      "metering-log", OpenMetadataType.METERING_LOG_CLASSIFICATION.typeName,
                                                      "security-log", OpenMetadataType.SECURITY_LOG_CLASSIFICATION.typeName,
                                                      "exception-backlog", OpenMetadataType.EXCEPTION_BACKLOG_CLASSIFICATION.typeName,
                                                      "log-analysis", OpenMetadataType.LOG_ANALYSIS_CLASSIFICATION.typeName).entrySet())
        {
            NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

            requestBody.setProperties(classifications.get(entry.getKey()));

            assetMaker.call("/assets/" + fromTemplateGUID + "/" + entry.getKey(), requestBody);
            AssetMakerFvtSupport.assertClassified(fromTemplateGUID, entry.getValue(), true);

            assetMaker.call("/assets/" + fromTemplateGUID + "/" + entry.getKey() + "/remove", new DeleteClassificationRequestBody());
            AssetMakerFvtSupport.assertClassified(fromTemplateGUID, entry.getValue(), false);
        }
    }


    /**
     * The three API interface classifications can be set on a deployed API and removed again.
     */
    @Test
    @Order(9)
    void apiClassifications()
    {
        deployedAPIGUID = AssetMakerFvtSupport.createAsset(named(new DeployedAPIProperties(), "DeployedAPI", "asset-maker-api"));
        createdGUIDs.add(deployedAPIGUID);

        for (Map.Entry<String, String> entry : Map.of("listener-interface", OpenMetadataType.LISTENER_INTERFACE_CLASSIFICATION.typeName,
                                                      "publisher-interface", OpenMetadataType.PUBLISHER_INTERFACE_CLASSIFICATION.typeName,
                                                      "request-response-interface", OpenMetadataType.REQUEST_RESPONSE_INTERFACE_CLASSIFICATION.typeName).entrySet())
        {
            assetMaker.call("/deployed-apis/" + deployedAPIGUID + "/" + entry.getKey(), new NewClassificationRequestBody());
            AssetMakerFvtSupport.assertClassified(deployedAPIGUID, entry.getValue(), true);

            assetMaker.call("/deployed-apis/" + deployedAPIGUID + "/" + entry.getKey() + "/remove", new DeleteClassificationRequestBody());
            AssetMakerFvtSupport.assertClassified(deployedAPIGUID, entry.getValue(), false);
        }
    }


    /**
     * The data asset encoding classification is set with its properties, and removed.
     */
    @Test
    @Order(10)
    void dataAssetEncoding()
    {
        DataAssetEncodingProperties properties = new DataAssetEncodingProperties();

        properties.setEncodingType("UTF-8");
        properties.setEncodingLanguage("en");

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        assetMaker.call("/data-assets/" + dataFileGUID + "/encoding", requestBody);

        OpenMetadataRootElement asset = retrieveAsset(dataFileGUID);

        assertNotNull(asset.getElementHeader().getDataAssetEncoding(), "the encoding classification is missing");
        assertTrue(asset.getElementHeader().getDataAssetEncoding().getClassificationProperties() instanceof DataAssetEncodingProperties encoding &&
                           "UTF-8".equals(encoding.getEncodingType()),
                   "the encoding properties are wrong: " + asset.getElementHeader().getDataAssetEncoding().getClassificationProperties());

        assetMaker.call("/data-assets/" + dataFileGUID + "/encoding/remove", new DeleteClassificationRequestBody());

        AssetMakerFvtSupport.assertClassified(dataFileGUID, OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName, false);
    }


    /**
     * deleteAsset removes each asset.
     */
    @Test
    @Order(11)
    void deleteAssets()
    {
        for (String guid : List.copyOf(createdGUIDs))
        {
            assetMaker.call("/assets/" + guid + "/delete", new org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + guid + "/retrieve");
            createdGUIDs.remove(guid);
        }
    }


    /**
     * Remove anything an earlier failure left behind.
     */
    @AfterAll
    static void cleanUp()
    {
        for (String guid : createdGUIDs)
        {
            assetMaker.postUnchecked("/assets/" + guid + "/delete",
                                     new org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody(),
                                     org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse.class);
        }
    }


    private static OpenMetadataRootElement retrieveAsset(String guid)
    {
        OpenMetadataRootElement asset = assetMaker.element("/assets/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(asset, "getAssetByGUID returned nothing for " + guid);
        assertEquals(guid, asset.getElementHeader().getGUID());

        return asset;
    }


    static boolean contains(List<OpenMetadataRootElement> elements, String guid)
    {
        return elements.stream().anyMatch(element -> guid.equals(element.getElementHeader().getGUID()));
    }
}
