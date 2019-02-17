/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetcatalog.client.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Asset;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class AssetCatalogOMASServiceTest {
    private final String assetId = "6662c0f2.e1b1ec6c.54865omh1.pco9ecb.c3g5f1.pfvf6bdv95dnc67jq2jli";
    private final String classificationDescription = "A uniquely identifying relational column.";
    private final String typeDef = "RelationalColumn";
    private final String typeDefDescription = "A semantic description of something, such as a concept, object, asset, technology, role or group.";
    private final String relationshipGuid = "b1c497ce.60641b50.54865oj4d.fnjt4v4.6nj46m.ipef318j22n8qcprd4k6h";

    @Mock
    private AssetCatalog assetCatalog;

    @InjectMocks
    private AssetCatalogOMASService assetCatalogOMASService;

    @Test
    @DisplayName("Asset Summary")
    public void testGetAssetSummary() throws PropertyServerException, InvalidParameterException {
        AssetDescriptionResponse expectedResponse = mockAssetDescriptionResponse();
        when(assetCatalog.getAssetSummary(anyString(), anyString())).thenReturn(expectedResponse);
        List<AssetDescription> resultList = assetCatalogOMASService.getAssetSummary(assetId);
        verifyAssetDescriptionResult(resultList);
    }

    @Test
    @DisplayName("Asset Details")
    public void testGetAssetDetails() throws PropertyServerException, InvalidParameterException {
        AssetDescriptionResponse expectedResponse = mockAssetDescriptionResponse();
        when(assetCatalog.getAssetDetails(anyString(), anyString())).thenReturn(expectedResponse);
        List<AssetDescription> resultList = assetCatalogOMASService.getAssetDetails(assetId);
        verifyAssetDescriptionResult(resultList);
    }

    @Test
    @DisplayName("Asset Universe")
    public void testGetAssetUniverse() throws PropertyServerException, InvalidParameterException {
        AssetDescriptionResponse expectedResponse = mockAssetDescriptionResponse();
        when(assetCatalog.getAssetUniverse(anyString(), anyString())).thenReturn(expectedResponse);
        List<AssetDescription> resultList = assetCatalogOMASService.getAssetUniverse(assetId);
        verifyAssetDescriptionResult(resultList);
    }

    @Test
    @DisplayName("Asset Relationship for all types")
    public void testGetAssetRelationships() throws PropertyServerException, InvalidParameterException {
        RelationshipsResponse expectedResponse = mockRelationshipsResponse();
        when(assetCatalog.getAssetRelationships(anyString(), anyString())).thenReturn(expectedResponse);
        List<Relationship> resultList = assetCatalogOMASService.getAssetRelationships(assetId);
        verifyRelationshipResponse(resultList);
    }
    @Test
    @DisplayName("Asset Relationship for type")
    public void testGetAssetRelationshipsForType() throws PropertyServerException, InvalidParameterException {
        RelationshipsResponse expectedResponse = mockRelationshipsResponse();
        String relationshipType = "SemanticAssignment";
        when(assetCatalog.getAssetRelationshipsForType(anyString(), anyString(), anyString())).thenReturn(expectedResponse);
        List<Relationship> resultList = assetCatalogOMASService.getAssetRelationshipsForType(assetId, relationshipType);
        verifyRelationshipResponse(resultList);
    }
    @Test
    @DisplayName("Asset Classification")
    public void testGetClassificationForAsset() throws PropertyServerException, InvalidParameterException {
        ClassificationsResponse expectedResponse = mockClassificationsResponse();
        when(assetCatalog.getClassificationForAsset(anyString(), anyString())).thenReturn(expectedResponse);

        List<Classification> resultList = assetCatalogOMASService.getClassificationForAsset(assetId);
        verifyClassificationResponse(resultList);
    }

    @Test()
    @DisplayName("Asset Summary - exception")
    public void testGetAssetSummaryException() throws PropertyServerException, InvalidParameterException {
        InvalidParameterException mockedException = mockExceptionResponse("unit-test");
        when(assetCatalog.getAssetSummary(anyString(), anyString())).thenThrow(mockedException);
        InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                () -> assetCatalogOMASService.getAssetSummary("asset-id-does-not-exists"));
        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));

    }
    @Test
    @DisplayName("Asset Details - exception")
    public void testGetAssetDetailsException() throws PropertyServerException, InvalidParameterException {
        InvalidParameterException mockedException = mockExceptionResponse("unit-test");
        when(assetCatalog.getAssetDetails(anyString(), anyString())).thenThrow(mockedException);
        InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                () -> assetCatalogOMASService.getAssetDetails("asset-id-does-not-exists"));
        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));
    }
    @Test
    @DisplayName("Asset Universe - exception")
    public void testGetAssetUniverseException() throws PropertyServerException, InvalidParameterException {
        InvalidParameterException mockedException = mockExceptionResponse("unit-test");
        when(assetCatalog.getAssetUniverse(anyString(), anyString())).thenThrow(mockedException);
        InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                () -> assetCatalogOMASService.getAssetUniverse("asset-id-does-not-exists"));
        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));
    }
    @Test
    @DisplayName("Asset Relationships - exception")
    public void testGetAssetRelationshipsException() throws PropertyServerException, InvalidParameterException {
        InvalidParameterException mockedException = mockExceptionResponse("unit-test");
        when(assetCatalog.getAssetRelationships(anyString(), anyString())).thenThrow(mockedException);
        InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                () -> assetCatalogOMASService.getAssetRelationships("asset-id-does-not-exists"));
        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));
    }
    @Test
    @DisplayName("Asset Relationships for type - exception")
    public void testGetAssetRelationshipsForTypeException() throws PropertyServerException, InvalidParameterException {
        InvalidParameterException mockedException = mockExceptionResponse("unit-test");
        when(assetCatalog.getAssetRelationshipsForType(anyString(), anyString(), anyString())).thenThrow(mockedException);
        InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
                () -> assetCatalogOMASService.getAssetRelationshipsForType(
                        "asset-id-does-not-exists",
                        "relationship-type-does-not-exists"));
        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));
    }
    @Test
    @DisplayName("Asset Classification - exception")
    public void testGetAssetClassificationException() throws PropertyServerException, InvalidParameterException {
        InvalidParameterException mockedException = mockExceptionResponse("unit-test");
        when(assetCatalog.getClassificationForAsset(anyString(), anyString())).thenThrow(mockedException);
        InvalidParameterException thrown = assertThrows(InvalidParameterException.class, ()
                -> assetCatalogOMASService.getClassificationForAsset("asset-id-does-not-exists"));
        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));
    }

    private InvalidParameterException mockExceptionResponse(String... params) {
        AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.PARAMETER_NULL;
        return new InvalidParameterException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                "methodName",
                errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(params),
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
    private AssetDescriptionResponse mockAssetDescriptionResponse() {
        AssetDescriptionResponse expectedResponse = new AssetDescriptionResponse();
        List<AssetDescription> expectedDescriptionList = new ArrayList<>();
        AssetDescription expectedDescription = new AssetDescription();
        expectedDescription.setGuid(assetId);
        expectedDescription.setTypeDefDescription(typeDefDescription);
        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("summary", "Short description of term First Name");
        propertiesMap.put("displayName", "First Name");
        expectedDescription.setProperties(propertiesMap);
        expectedDescriptionList.add(expectedDescription);
        expectedResponse.setAssetDescriptionList(expectedDescriptionList);
        return expectedResponse;
    }
    private RelationshipsResponse mockRelationshipsResponse() {
        RelationshipsResponse expectedResponse = new RelationshipsResponse();
        List<Relationship> expectedRelationshipList = new ArrayList<>();
        Relationship expectedRelationship = new Relationship();
        Asset fromEntity = new Asset();
        fromEntity.setGuid(relationshipGuid);
        fromEntity.setTypeDefName(typeDef);
        expectedRelationship.setFromEntity(fromEntity);
        Asset toEntity = new Asset();
        expectedRelationship.setToEntity(toEntity);
        toEntity.setGuid(assetId);
        toEntity.setTypeDefName("GlossaryTerm");
        expectedRelationshipList.add(expectedRelationship);
        expectedResponse.setRelationships(expectedRelationshipList);
        return expectedResponse;
    }
    private ClassificationsResponse mockClassificationsResponse() {
        ClassificationsResponse expectedResponse = new ClassificationsResponse();
        List<Classification> expectedClassificationList = new ArrayList<>();
        Classification expectedClassification = new Classification();
        expectedClassification.setTypeDefName(typeDef);
        expectedClassification.setTypeDefDescription(classificationDescription);
        expectedClassificationList.add(expectedClassification);
        expectedResponse.setClassifications(expectedClassificationList);
        return expectedResponse;
    }
    private void verifyAssetDescriptionResult(List<AssetDescription> resultList) {
        assertFalse(resultList.isEmpty());
        AssetDescription assetDescription = resultList.get(0);
        assertEquals(assetDescription.getGuid(), assetId);
        assertEquals(assetDescription.getTypeDefDescription(), typeDefDescription);
        assertFalse(assetDescription.getProperties().isEmpty());
    }
    private void verifyRelationshipResponse(List<Relationship> resultList) {
        assertFalse(resultList.isEmpty());
        Relationship relationship = resultList.get(0);
        assertEquals(relationship.getFromEntity().getGuid(), relationshipGuid);
        assertEquals(relationship.getFromEntity().getTypeDefName(), typeDef);
        assertEquals(relationship.getToEntity().getGuid(), assetId);
        String glossaryTermTypeDef = "GlossaryTerm";
        assertEquals(relationship.getToEntity().getTypeDefName(), glossaryTermTypeDef);
    }
    private void verifyClassificationResponse(List<Classification> resultList) {
        assertFalse(resultList.isEmpty());
        Classification classification = resultList.get(0);
        assertEquals(classification.getTypeDefName(), typeDef);
        assertEquals(classification.getTypeDefDescription(), classificationDescription);
    }
}