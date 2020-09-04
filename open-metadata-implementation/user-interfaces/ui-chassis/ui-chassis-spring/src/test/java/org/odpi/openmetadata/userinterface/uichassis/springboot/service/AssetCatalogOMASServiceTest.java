/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class AssetCatalogOMASServiceTest {

    private static final String CONFIDENTIALITY = "Confidentiality";
    private final String COMPLEX_SCHEMA_TYPE = "ComplexSchemaType";
    private final String assetId = "6662c0f2.e1b1ec6c.54865omh1.pco9ecb.c3g5f1.pfvf6bdv95dnc67jq2jli";
    private final String schemaId = "abababa1.e1b1ec6c.54865omh1.pco9ecb.c3g5f1.pfvf6bdv95dnc67jq2jli";
    private final String typeDef = "Asset";
    private final String relationshipTypeDef = "AssetSchemaType";
    private final String user = "demo";

    @Mock
    private AssetCatalog assetCatalog;

    @InjectMocks
    private AssetCatalogOMASService assetCatalogOMASService;

    @Test
    @DisplayName("Asset Details")
    void testGetAssetDetails() throws PropertyServerException, InvalidParameterException {
        AssetDescriptionResponse expectedResponse = mockAssetDescriptionResponse();
        when(assetCatalog.getAssetDetails(anyString(), anyString(), anyString())).thenReturn(expectedResponse);
        AssetDescription response = assetCatalogOMASService.getAssetDetails(user, assetId, typeDef);
        verifyAssetDescriptionResult(response);
    }

    @Test
    @DisplayName("Asset Universe")
    void testGetAssetUniverse() throws PropertyServerException, InvalidParameterException {
        AssetDescriptionResponse expectedResponse = mockAssetDescriptionResponse();
        when(assetCatalog.getAssetUniverse(anyString(), anyString(), anyString())).thenReturn(expectedResponse);
        AssetDescription response = assetCatalogOMASService.getAssetUniverse(user, assetId, typeDef);
        verifyAssetDescriptionResult(response);
    }

    @Test
    @DisplayName("Asset Relationships by type")
    void testGetAssetRelationships() throws PropertyServerException, InvalidParameterException {
        RelationshipListResponse expectedResponse = mockRelationshipResponse();
        when(assetCatalog.getAssetRelationships(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(expectedResponse);
        List<Relationship> resultList = assetCatalogOMASService.getAssetRelationships(user, assetId, typeDef, relationshipTypeDef, 0, 1);
        verifyRelationshipResponse(resultList);
    }

    @Test
    @DisplayName("Asset Classification")
    void testGetClassificationForAsset() throws PropertyServerException, InvalidParameterException {
        ClassificationListResponse expectedResponse = mockClassificationsResponse();
        when(assetCatalog.getClassificationsForAsset(anyString(), anyString(), anyString(), anyString())).thenReturn(expectedResponse);
        List<Classification> resultList = assetCatalogOMASService.getClassificationsForAsset(user, assetId, typeDef, CONFIDENTIALITY);
        verifyClassificationResponse(resultList);
    }

    private AssetDescriptionResponse mockAssetDescriptionResponse() {
        AssetDescriptionResponse expectedResponse = new AssetDescriptionResponse();

        AssetDescription expectedDescription = new AssetDescription();
        expectedDescription.setGuid(assetId);
        Type type = mockType(typeDef);
        expectedDescription.setType(type);
        Map<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("summary", "Short description of term First Name");
        propertiesMap.put("displayName", "First Name");
        expectedDescription.setProperties(propertiesMap);

        expectedResponse.setAssetDescription(expectedDescription);
        return expectedResponse;
    }

    private RelationshipListResponse mockRelationshipResponse() {
        RelationshipListResponse expectedResponse = new RelationshipListResponse();

        List<Relationship> expectedRelationshipList = new ArrayList<>();
        Relationship expectedRelationship = new Relationship();

        Element fromEntity = new Element();
        fromEntity.setGuid(assetId);
        Type type = mockType(typeDef);
        fromEntity.setType(type);
        expectedRelationship.setFromEntity(fromEntity);

        Element toEntity = new Element();
        expectedRelationship.setToEntity(toEntity);
        toEntity.setGuid(schemaId);
        Type type1 = mockType("ComplexSchemaType");
        toEntity.setType(type1);

        expectedRelationshipList.add(expectedRelationship);
        expectedResponse.setRelationships(expectedRelationshipList);
        return expectedResponse;
    }

    private Type mockType(String complexSchemaType) {
        Type type1 = new Type();
        type1.setName(complexSchemaType);
        return type1;
    }

    private ClassificationListResponse mockClassificationsResponse() {
        ClassificationListResponse expectedResponse = new ClassificationListResponse();
        List<Classification> expectedClassificationList = new ArrayList<>();
        Classification expectedClassification = new Classification();
        Type type = mockType(CONFIDENTIALITY);
        expectedClassification.setType(type);
        expectedClassificationList.add(expectedClassification);
        expectedResponse.setClassifications(expectedClassificationList);
        return expectedResponse;
    }

    private void verifyAssetDescriptionResult(AssetDescription assetDescription) {
        assertEquals(assetDescription.getGuid(), assetId);
        assertFalse(assetDescription.getProperties().isEmpty());
    }

    private void verifyRelationshipResponse(List<Relationship> resultList) {
        assertFalse(resultList.isEmpty());
        Relationship relationship = resultList.get(0);
        assertEquals(relationship.getFromEntity().getGuid(), assetId);
        assertEquals(relationship.getFromEntity().getType().getName(), typeDef);
        assertEquals(relationship.getToEntity().getGuid(), schemaId);
        assertEquals(relationship.getToEntity().getType().getName(), COMPLEX_SCHEMA_TYPE);
    }

    private void verifyClassificationResponse(List<Classification> resultList) {
        assertFalse(resultList.isEmpty());
        Classification classification = resultList.get(0);
        assertEquals(classification.getType().getName(), CONFIDENTIALITY);
    }
}