/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.client.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AssetCatalogClientTest {

    private static final String defaultOMASServerURL = "http://localhost:8081";
    private static final String defaultServerName = "TestServer";
    private static final String defaultUserId = "zebra91";
    private static final String defaultAssetId = "66d7f872-19bd-439c-98ae-c3fe49d8f420";
    private static final String defaultAssetType = "GlossaryTerm";
    private static final String defaultRelationshipId = "c7184523-7ca5-4876-9210-fe1bb1b55cd7";
    private static final String defaultRelationshipType = "SemanticAssignment";

    private AssetCatalog assetCatalog;

    @Mock
    private RESTClientConnector connector;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);

        assetCatalog = new AssetCatalog(defaultServerName, defaultOMASServerURL);
        Field connectorField = ReflectionUtils.findField(AssetCatalog.class, "clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            ReflectionUtils.setField(connectorField, assetCatalog, connector);
            connectorField.setAccessible(false);
        }
    }

    @Test
    public void test() throws Exception {
        AssetDescriptionResponse response = mockAssetDescriptionResponse();

        when(connector.callGetRESTCall(eq("getAssetDetails"), eq(AssetDescriptionResponse.class), anyString(), eq(defaultServerName),
                eq(defaultUserId), eq(response.getAssetDescriptionList().get(0).getGuid()), eq(defaultAssetType))).thenReturn(response);

        AssetDescriptionResponse assetDetails = assetCatalog.getAssetDetails(defaultUserId,
                response.getAssetDescriptionList().get(0).getGuid(),
                defaultAssetType);

        Assert.assertEquals(response.getAssetDescriptionList().get(0).getGuid(), assetDetails.getAssetDescriptionList().get(0).getGuid());
    }

    private AssetDescriptionResponse mockAssetDescriptionResponse() {
        AssetDescriptionResponse expectedResponse = new AssetDescriptionResponse();
        expectedResponse.setRelatedHTTPCode(200);

        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGuid(defaultAssetId);
        List<AssetDescription> list = new ArrayList<>(1);
        list.add(assetDescription);
        expectedResponse.setAssetDescriptionList(list);

        return expectedResponse;
    }
}