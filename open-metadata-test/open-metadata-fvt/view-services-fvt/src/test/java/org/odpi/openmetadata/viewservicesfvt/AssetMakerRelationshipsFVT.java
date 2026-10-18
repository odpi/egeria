/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.TabularDataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.DeployedAPIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.ArchiveFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.MediaFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.IncidentReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.named;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.newElement;

/**
 * AssetMakerRelationshipsFVT covers the Asset Maker's attach/detach requests between assets (and the ports,
 * endpoints and collections that some of them link assets to).  Each pair of requests is checked the same way:
 * after the attach the relationship is visible from both ends, after the detach from neither.  Fresh elements are
 * used for each relationship so that nothing else links them.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetMakerRelationshipsFVT
{
    private static final ViewServiceClient connectionMaker   = new ViewServiceClient(ViewServiceDescription.CONNECTION_MAKER);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);

    private static final List<String> createdGUIDs = new ArrayList<>();


    @Test
    @Order(1)
    void deployedOn()
    {
        String server = asset(new SoftwareServerProperties(), "SoftwareServer", "deployed-server");
        String host   = asset(new HostProperties(), "Host", "deployment-host");

        linkCycle("/assets/" + server + "/deployed-on/" + host, OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName, server, host);
    }


    /**
     * Data set content: attach returns the relationship GUID, which update and the second form of detach use.
     */
    @Test
    @Order(2)
    void dataSetContent()
    {
        String dataSet = asset(new TabularDataSetProperties(), "TabularDataSet", "content-data-set");
        String file    = asset(new DataFileProperties(), "DataFile", "content-file");

        DataSetContentProperties properties = new DataSetContentProperties();

        properties.setQueryId("q1");
        properties.setQuery("select * from fvt");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String relationshipGUID = assetMaker.create("/data-sets/" + dataSet + "/data-set-content/" + file + "/attach", requestBody);

        assertLinked(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, dataSet, file);
        assertTrue(AssetMakerFvtSupport.relationshipPropertiesJSON(dataSet, file).contains("select * from fvt"),
                   "the data set content properties are missing");

        properties.setQuery("select name from fvt");

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setProperties(properties);

        assetMaker.call("/data-set-content/" + relationshipGUID + "/update", updateRequestBody);

        assertTrue(AssetMakerFvtSupport.relationshipPropertiesJSON(dataSet, file).contains("select name from fvt"),
                   "the update to the data set content properties is not visible");

        assetMaker.call("/data-set-content/" + relationshipGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, dataSet, file);

        assetMaker.create("/data-sets/" + dataSet + "/data-set-content/" + file + "/attach", new NewRelationshipRequestBody());
        assertLinked(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, dataSet, file);

        assetMaker.call("/data-sets/" + dataSet + "/data-set-content/" + file + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, dataSet, file);
    }


    @Test
    @Order(3)
    void reports()
    {
        String originator  = asset(new ProcessProperties(), "Process", "report-originator");
        String priorReport = asset(new ReportProperties(), "Report", "prior-report");
        String report      = asset(new ReportProperties(), "Report", "follow-on-report");
        String subject     = asset(new DataFileProperties(), "DataFile", "report-subject");

        linkCycle("/elements/" + originator + "/originated-reports/" + report, OpenMetadataType.REPORT_ORIGINATOR_RELATIONSHIP.typeName, originator, report);
        linkCycle("/reports/" + priorReport + "/follow-on-reports/" + report, OpenMetadataType.REPORT_DEPENDENCY_RELATIONSHIP.typeName, priorReport, report);
        linkCycle("/elements/" + subject + "/reports/" + report, OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName, subject, report);
    }


    @Test
    @Order(4)
    void impactedResource()
    {
        String resource = asset(new DataFileProperties(), "DataFile", "impacted-file");
        String incident = asset(new IncidentReportProperties(), "IncidentReport", "incident");

        linkCycle("/resources/" + resource + "/impacted-resources/" + incident, OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName, resource, incident);
    }


    @Test
    @Order(5)
    void apiEndpoint()
    {
        String api = asset(new DeployedAPIProperties(), "DeployedAPI", "api");

        EndpointProperties endpointProperties = named(new EndpointProperties(), "Endpoint", "api-endpoint");

        endpointProperties.setNetworkAddress("https://localhost:9443/fvt-api");

        String endpoint = created(connectionMaker.create("/endpoints", newElement(endpointProperties)));

        linkCycle("/deployed-apis/" + api + "/api-endpoints/" + endpoint, OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName, api, endpoint);
    }


    @Test
    @Order(6)
    void processesAndPorts()
    {
        String parent = asset(new ProcessProperties(), "Process", "parent-process");
        String child  = asset(new ProcessProperties(), "Process", "child-process");

        linkCycle("/processes/" + parent + "/process-hierarchies/" + child, OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName, parent, child);

        String port         = created(AssetMakerFvtSupport.createGenericElement(OpenMetadataType.PORT.typeName, "process-port"));
        String delegatePort = created(AssetMakerFvtSupport.createGenericElement(OpenMetadataType.PORT.typeName, "delegate-port"));

        linkCycle("/processes/" + parent + "/ports/" + port, OpenMetadataType.PROCESS_PORT_RELATIONSHIP.typeName, parent, port);
        linkCycle("/ports/" + port + "/port-delegations/" + delegatePort, OpenMetadataType.PORT_DELEGATION_RELATIONSHIP.typeName, port, delegatePort);
    }


    @Test
    @Order(7)
    void filesAndFolders()
    {
        String folder      = asset(new FileFolderProperties(), "FileFolder", "parent-folder");
        String childFolder = asset(new FileFolderProperties(), "FileFolder", "child-folder");
        String file        = asset(new DataFileProperties(), "DataFile", "folder-file");

        linkCycle("/file-folders/" + folder + "/nested-files/" + file, OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName, folder, file);
        linkCycle("/file-folders/" + folder + "/linked-files/" + file, OpenMetadataType.LINKED_FILE_RELATIONSHIP.typeName, folder, file);
        linkCycle("/file-folders/" + folder + "/folder-hierarchies/" + childFolder, OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName, folder, childFolder);
    }


    @Test
    @Order(8)
    void logsAndSampleData()
    {
        String element    = asset(new SoftwareServerProperties(), "SoftwareServer", "logging-server");
        String log        = asset(new DataFileProperties(), "DataFile", "server-log");
        String sampleData = asset(new DataFileProperties(), "DataFile", "sample-data");

        linkCycle("/elements/" + element + "/associated-logs/" + log, OpenMetadataType.ASSOCIATED_LOG_RELATIONSHIP.typeName, element, log);
        linkCycle("/elements/" + element + "/sample-data/" + sampleData, OpenMetadataType.SAMPLE_DATA_RELATIONSHIP.typeName, element, sampleData);
    }


    @Test
    @Order(9)
    void archivesAndMedia()
    {
        String archive    = asset(new ArchiveFileProperties(), "ArchiveFile", "archive");
        String collection = created(collectionManager.create("/collections", newElement(named(new CollectionProperties(), "Collection", "archive-contents"))));

        linkCycle("/archive-files/" + archive + "/archive-contents/" + collection, OpenMetadataType.ARCHIVE_CONTENTS_RELATIONSHIP.typeName, archive, collection);

        String media       = asset(new MediaFileProperties(), "MediaFile", "media");
        String linkedMedia = asset(new MediaFileProperties(), "MediaFile", "linked-media");

        linkCycle("/media-files/" + media + "/linked-media/" + linkedMedia, OpenMetadataType.LINKED_MEDIA_RELATIONSHIP.typeName, media, linkedMedia);
    }


    @Test
    @Order(10)
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


    /**
     * Attach, check visible from both ends, detach, check gone from both ends.
     *
     * @param pathPrefix request path without the /attach or /detach
     * @param relationshipTypeName expected relationship type
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     */
    private static void linkCycle(String pathPrefix,
                                  String relationshipTypeName,
                                  String end1GUID,
                                  String end2GUID)
    {
        assetMaker.call(pathPrefix + "/attach", new NewRelationshipRequestBody());
        assertLinked(relationshipTypeName, end1GUID, end2GUID);

        assetMaker.call(pathPrefix + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(relationshipTypeName, end1GUID, end2GUID);
    }


    private static <T extends ReferenceableProperties> String asset(T      properties,
                                                                   String typeName,
                                                                   String name)
    {
        return created(AssetMakerFvtSupport.createAsset(named(properties, typeName, name)));
    }


    private static String created(String guid)
    {
        createdGUIDs.add(guid);

        return guid;
    }
}
