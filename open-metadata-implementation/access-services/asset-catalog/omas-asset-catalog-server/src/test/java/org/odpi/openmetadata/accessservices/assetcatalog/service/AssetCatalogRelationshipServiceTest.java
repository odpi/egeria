/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.admin.AssetCatalogInstanceHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.RelationshipHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AssetCatalogRelationshipServiceTest {

    private static final String FIRST_GUID = "ababa-123-acbd";
    private static final String SECOND_GUID = "ababc-2134-2341f";
    private final String USER = "test-user";
    private final String SERVER_NAME = "omas";
    private final String RELATIONSHIP_TYPE = "SemanticAssigment";
    @Mock
    RESTExceptionHandler restExceptionHandler;
    private Relationship response;
    @Mock
    private AssetCatalogInstanceHandler instanceHandler;

    @InjectMocks
    private AssetCatalogRelationshipRESTService assetCatalogRelationshipService;

    @Mock
    private RelationshipHandler relationshipHandler;


    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);

        Field instanceHandlerField = ReflectionUtils.findField(AssetCatalogRelationshipRESTService.class, "instanceHandler");
        instanceHandlerField.setAccessible(true);
        ReflectionUtils.setField(instanceHandlerField, assetCatalogRelationshipService, instanceHandler);
        instanceHandlerField.setAccessible(false);

        Field restExceptionHandlerField = ReflectionUtils.findField(AssetCatalogRelationshipRESTService.class, "restExceptionHandler");
        restExceptionHandlerField.setAccessible(true);
        ReflectionUtils.setField(restExceptionHandlerField, assetCatalogRelationshipService, restExceptionHandler);
        restExceptionHandlerField.setAccessible(false);

        response = mockRelationshipResponse();
    }


    @Test
    public void testGetRelationshipBetweenEntities()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        when(instanceHandler.getRelationshipHandler(USER,
                SERVER_NAME,
                "getRelationshipBetweenEntities"))
                .thenReturn(relationshipHandler);


        when(relationshipHandler
                .getRelationshipBetweenEntities(USER, SERVER_NAME, FIRST_GUID, SECOND_GUID, RELATIONSHIP_TYPE))
                .thenReturn(response);

        RelationshipResponse relationshipBetweenEntities = assetCatalogRelationshipService.getRelationshipBetweenEntities(SERVER_NAME,
                USER,
                FIRST_GUID,
                SECOND_GUID,
                RELATIONSHIP_TYPE);
        assertEquals(RELATIONSHIP_TYPE, relationshipBetweenEntities.getRelationship().getType().getName());
        assertEquals(response.getGuid(), relationshipBetweenEntities.getRelationship().getGuid());
        assertEquals(response.getFromEntity().getGuid(), relationshipBetweenEntities.getRelationship().getFromEntity().getGuid());
        assertEquals(response.getToEntity().getGuid(), relationshipBetweenEntities.getRelationship().getToEntity().getGuid());
    }

    private Relationship mockRelationshipResponse() {
        Relationship relationshipsResponse = new Relationship();
        Type type = new Type();
        type.setName(RELATIONSHIP_TYPE);
        relationshipsResponse.setType(type);
        relationshipsResponse.setGuid("d1213-dabcf-dafc");
        relationshipsResponse.setFromEntity(mockElement(FIRST_GUID));
        relationshipsResponse.setToEntity(mockElement(SECOND_GUID));
        return relationshipsResponse;
    }

    private Element mockElement(String guid) {
        Element asset = new Element();
        asset.setGuid(guid);
        return asset;
    }

}
