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
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataRelationshipSummaryResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LineageLinkerFVT covers every request of the Lineage Linker OMVS: a DataFlow is linked between two data sets,
 * its properties are updated and then it is detached.  The relationship is read back with the Classification
 * Explorer's relationship-summary request, which returns any relationship by its GUID.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LineageLinkerFVT
{
    private static final ViewServiceClient lineageLinker          = new ViewServiceClient(ViewServiceDescription.LINEAGE_LINKER);
    private static final ViewServiceClient assetMaker             = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    private static final ViewServiceClient classificationExplorer = new ViewServiceClient(ViewServiceDescription.CLASSIFICATION_EXPLORER);

    private static String sourceGUID       = null;
    private static String destinationGUID  = null;
    private static String dataFlowGUID     = null;


    @Test
    @Order(1)
    void createDataSets()
    {
        sourceGUID      = createDataSet("source");
        destinationGUID = createDataSet("destination");
    }


    /**
     * linkLineage returns the new relationship's GUID and the relationship links the two data sets with the
     * requested properties.
     */
    @Test
    @Order(2)
    void linkLineage()
    {
        DataFlowProperties properties = new DataFlowProperties();

        properties.setLabel("nightly copy");
        properties.setDescription("Copies the source to the destination each night.");
        properties.setFormula("SELECT * FROM source");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        dataFlowGUID = lineageLinker.create("/from-elements/" + sourceGUID + "/via/" + OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName +
                                                    "/to-elements/" + destinationGUID + "/attach",
                                            requestBody);

        MetadataRelationshipSummary dataFlow = readDataFlow();

        assertEquals(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, dataFlow.getRelationshipHeader().getType().getTypeName());
        assertEquals(sourceGUID, dataFlow.getEnd1().getGUID());
        assertEquals(destinationGUID, dataFlow.getEnd2().getGUID());

        DataFlowProperties stored = assertInstanceOf(DataFlowProperties.class, dataFlow.getRelationshipProperties());

        assertEquals("nightly copy", stored.getLabel());
        assertEquals("SELECT * FROM source", stored.getFormula());
    }


    /**
     * updateLineage with mergeUpdate changes only the properties supplied.
     */
    @Test
    @Order(3)
    void updateLineage()
    {
        DataFlowProperties properties = new DataFlowProperties();

        properties.setLabel("hourly copy");

        UpdateRelationshipRequestBody requestBody = new UpdateRelationshipRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        lineageLinker.call("/relationships/" + dataFlowGUID + "/update", requestBody);

        DataFlowProperties stored = assertInstanceOf(DataFlowProperties.class, readDataFlow().getRelationshipProperties());

        assertEquals("hourly copy", stored.getLabel());
        assertEquals("SELECT * FROM source", stored.getFormula(), "a merge update should keep the formula");
    }


    /**
     * detachLineage removes the relationship.
     */
    @Test
    @Order(4)
    void detachLineage()
    {
        lineageLinker.call("/relationships/" + dataFlowGUID + "/detach", new DeleteRelationshipRequestBody());

        MetadataRelationshipSummaryResponse response = classificationExplorer.postUnchecked("/relationship-summaries/by-guid/" + dataFlowGUID,
                                                                                            new GetRequestBody(),
                                                                                            MetadataRelationshipSummaryResponse.class);

        assertNotNull(response);
        assertTrue((response.getRelatedHTTPCode() != 200) || (response.getRelationship() == null),
                   "the data flow is still returned after detachLineage");
    }


    @AfterAll
    static void deleteDataSets()
    {
        for (String guid : new String[]{sourceGUID, destinationGUID})
        {
            if (guid != null)
            {
                assetMaker.call("/assets/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + guid + "/retrieve");
            }
        }
    }


    private static MetadataRelationshipSummary readDataFlow()
    {
        MetadataRelationshipSummary dataFlow = classificationExplorer.post("/relationship-summaries/by-guid/" + dataFlowGUID,
                                                                           new GetRequestBody(),
                                                                           MetadataRelationshipSummaryResponse.class).getRelationship();

        assertNotNull(dataFlow, "data flow " + dataFlowGUID + " is missing");

        return dataFlow;
    }


    private static String createDataSet(String name)
    {
        DataSetProperties properties = new DataSetProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataSet", "lineage-" + name));
        properties.setDisplayName("Lineage " + name);

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return assetMaker.create("/assets", requestBody);
    }
}
