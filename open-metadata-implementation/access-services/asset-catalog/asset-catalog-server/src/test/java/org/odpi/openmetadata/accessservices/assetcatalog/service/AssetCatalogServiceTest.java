package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.admin.AssetCatalogInstanceHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Asset;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AssetCatalogServiceTest {

    private static final String USER = "test-user";
    private static final String SERVER_NAME = "omas";
    private static final String FIRST_GUID = "ababa-123-acbd";
    private static final String ASSET_TYPE = "Process";
    private static final String SECOND_GUID = "ababc-2134-2341f";
    private static final String RELATIONSHIP_TYPE = "SemanticAssigment";

    @Mock
    RESTExceptionHandler restExceptionHandler;

    @Mock
    private AssetCatalogInstanceHandler instanceHandler;

    @InjectMocks
    private AssetCatalogRESTService assetCatalogRESTService;

    @Mock
    private AssetCatalogHandler assetCatalogHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        Field instanceHandlerField = ReflectionUtils.findField(AssetCatalogRESTService.class, "instanceHandler");
        instanceHandlerField.setAccessible(true);
        ReflectionUtils.setField(instanceHandlerField, assetCatalogRESTService, instanceHandler);
        instanceHandlerField.setAccessible(false);

        Field restExceptionHandlerField = ReflectionUtils.findField(AssetCatalogRESTService.class, "restExceptionHandler");
        restExceptionHandlerField.setAccessible(true);
        ReflectionUtils.setField(restExceptionHandlerField, assetCatalogRESTService, restExceptionHandler);
        restExceptionHandlerField.setAccessible(false);
    }

    @Test
    public void testGetAssetDetailsByGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        AssetDescription response = mockAssetDescription(FIRST_GUID);

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getAssetDetailsByGUID"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getEntityDetails(USER, FIRST_GUID, ASSET_TYPE))
                .thenReturn(response);

        AssetDescriptionResponse assetDetailsByGUID = assetCatalogRESTService.getAssetDetailsByGUID(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE);
        assertEquals(FIRST_GUID, assetDetailsByGUID.getAssetDescriptionList().get(0).getGuid());
        assertEquals(response.getGuid(), assetDetailsByGUID.getAssetDescriptionList().get(0).getGuid());
        assertEquals(response.getTypeDefName(), assetDetailsByGUID.getAssetDescriptionList().get(0).getTypeDefName());
    }

    private AssetDescription mockAssetDescription(String guid) {
        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGuid(guid);
        assetDescription.setTypeDefName(ASSET_TYPE);
        return assetDescription;
    }

    private Relationship mockRelationshipResponse() {
        Relationship relationshipsResponse = new Relationship();
        relationshipsResponse.setTypeDefName(RELATIONSHIP_TYPE);
        relationshipsResponse.setGuid("d1213-dabcf-dafc");
        relationshipsResponse.setFromEntity(mockAsset(FIRST_GUID));
        relationshipsResponse.setToEntity(mockAsset(SECOND_GUID));
        return relationshipsResponse;
    }

    private Asset mockAsset(String guid) {
        Asset asset = new Asset();
        asset.setGuid(guid);
        return asset;
    }

}
