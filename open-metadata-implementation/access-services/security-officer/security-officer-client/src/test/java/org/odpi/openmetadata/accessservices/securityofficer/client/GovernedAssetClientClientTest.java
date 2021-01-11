/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernanceClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.GovernedAssetListResponse;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.GovernedAssetResponse;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SoftwareServerCapabilityResponse;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GovernedAssetClientClientTest
{

    private static final String SERVER_URL = "https://localhost:9444";
    private static final String SERVER_NAME = "TestServer";
    private static final String USER_ID = "zebra91";
    private static final String SOFTWARE_SERVER_GUID = "66d7f872-19bd-439c-98ae-c3fe49d8f420";
    private static final String RELATIONSHIP_COLUMN_GUID = "66d7f872-19bd-439c-98ae-3232430022";
    private static final String RELATIONSHIP_COLUMN = "RelationalColumn";
    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;
    private static final String SECURITY_TAGS = "SecurityTags";

    private GovernedAssetClient governedAssetClient;

    @Mock
    private RESTClientConnector connector;

    @Before
    public void before() throws InvalidParameterException {
        MockitoAnnotations.initMocks(this);

        governedAssetClient = new GovernedAssetClient(SERVER_NAME, SERVER_URL);
        Field connectorField = ReflectionUtils.findField(GovernedAssetClient.class, "clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            ReflectionUtils.setField(connectorField, governedAssetClient, connector);
            connectorField.setAccessible(false);
        }
    }

    @Test
    public void testGetGovernedAssetList() throws RESTServerException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GovernedAssetListResponse response = mockGovernedAssetListResponse();

        when(connector
                .callGetRESTCall(eq("getGovernedAssetList"),
                        eq(GovernedAssetListResponse.class),
                        anyString(),
                        eq(SERVER_NAME),
                        eq(USER_ID),
                        eq(SECURITY_TAGS),
                        eq(Arrays.asList(RELATIONSHIP_COLUMN)),
                        eq(FROM), eq(PAGE_SIZE)))
                .thenReturn(response);

        List<GovernedAsset> governedAssetList = governedAssetClient.getGovernedAssetList(USER_ID,
                                                                                         SECURITY_TAGS,
                                                                                         Arrays.asList(RELATIONSHIP_COLUMN),
                                                                                         FROM, PAGE_SIZE);

        Assert.assertEquals(response.getGovernedAssetList().get(0), governedAssetList.get(0));
    }

    @Test
    public void testGetGovernedAsset()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RESTServerException {
        GovernedAssetResponse response = mocKGovernedAssetResponse();

        when(connector
                .callGetRESTCall(eq("getGovernedAsset"),
                        eq(GovernedAssetResponse.class),
                        anyString(),
                        eq(SERVER_NAME),
                        eq(USER_ID),
                        eq(RELATIONSHIP_COLUMN_GUID)))
                .thenReturn(response);

        GovernedAsset governedAsset = this.governedAssetClient.getGovernedAsset(USER_ID, RELATIONSHIP_COLUMN_GUID);

        Assert.assertEquals(response.getAsset(), governedAsset);
    }

    @Test
    public void testGetSoftwareServerCapabilityByGUID()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RESTServerException {
        SoftwareServerCapabilityResponse response = mockSoftwareServerCapabilityResponse();

        when(connector
                .callGetRESTCall(eq("getSoftwareServerCapabilityByGUID"),
                        eq(SoftwareServerCapabilityResponse.class),
                        anyString(),
                        eq(SERVER_NAME),
                        eq(USER_ID),
                        eq(SOFTWARE_SERVER_GUID)))
                .thenReturn(response);

        SoftwareServerCapability softwareServerCapability = governedAssetClient.getSoftwareServerCapabilityByGUID(USER_ID, SOFTWARE_SERVER_GUID);

        Assert.assertEquals(response.getServerCapability(), softwareServerCapability);
    }

    @Test
    public void testCreateSoftwareServerCapability() throws RESTServerException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        StringResponse response = new StringResponse();
        response.setResultString(SOFTWARE_SERVER_GUID);

        SoftwareServerCapabilityRequestBody body = mockSoftwareServerCapabilityRequestBody();

        when(connector
                .callPostRESTCall(eq("createSoftwareServerCapability"),
                        eq(StringResponse.class),
                        anyString(),
                        eq(body),
                        eq(SERVER_NAME),
                        eq(USER_ID)))
                .thenReturn(response);

        String softwareServerCapabilityGUID = governedAssetClient.createSoftwareServerCapability(USER_ID, body);

        Assert.assertEquals(response.getResultString(), softwareServerCapabilityGUID);
    }

    private SoftwareServerCapabilityRequestBody mockSoftwareServerCapabilityRequestBody() {
        SoftwareServerCapabilityRequestBody body = new SoftwareServerCapabilityRequestBody();
        body.setSoftwareServerCapability(mockSoftwareServerCapability());
        return body;
    }

    private SoftwareServerCapabilityResponse mockSoftwareServerCapabilityResponse() {
        SoftwareServerCapabilityResponse softwareServerCapabilityResponse = new SoftwareServerCapabilityResponse();
        softwareServerCapabilityResponse.setServerCapability(mockSoftwareServerCapability());

        return softwareServerCapabilityResponse;
    }

    private SoftwareServerCapability mockSoftwareServerCapability() {
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setGUID(SOFTWARE_SERVER_GUID);

        return softwareServerCapability;
    }

    private GovernedAssetResponse mocKGovernedAssetResponse() {
        GovernedAssetResponse governedAssetResponse = new GovernedAssetResponse();
        governedAssetResponse.setAsset(mockGovernedAsset());
        return governedAssetResponse;
    }

    private GovernedAssetListResponse mockGovernedAssetListResponse() {
        GovernedAssetListResponse response = new GovernedAssetListResponse();
        response.setGovernedAssetList(Arrays.asList(mockGovernedAsset()));

        return response;
    }

    private GovernedAsset mockGovernedAsset() {
        GovernedAsset governedAsset = new GovernedAsset();

        governedAsset.setGuid(RELATIONSHIP_COLUMN_GUID);
        governedAsset.setType(RELATIONSHIP_COLUMN);
        governedAsset.setAssignedGovernanceClassification(getGovernanceClassification());

        return governedAsset;
    }

    private GovernanceClassification getGovernanceClassification() {
        GovernanceClassification governanceClassification = new GovernanceClassification();

        governanceClassification.setName(SECURITY_TAGS);

        return governanceClassification;
    }

}
