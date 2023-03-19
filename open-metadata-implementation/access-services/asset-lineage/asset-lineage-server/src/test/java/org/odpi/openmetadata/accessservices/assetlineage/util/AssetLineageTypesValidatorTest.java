/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_INCOMPLETE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FLOW;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.REFERENCEABLE;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class AssetLineageTypesValidatorTest {

    private static final String SERVER_NAME = "serverName";
    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @InjectMocks
    private AssetLineageTypesValidator assetLineageTypesValidator;

    @Test
    void hasValidClassificationTypes() {
        EntityDetail entityDetail = mock(EntityDetail.class);
        List<Classification> classifications = mockClassifications();
        when(entityDetail.getClassifications()).thenReturn(classifications);

        boolean result = assetLineageTypesValidator.hasValidClassificationTypes(entityDetail);
        assertTrue(result);
    }

    @Test
    void hasValidClassificationTypes_false() {
        EntityDetail entityDetail = mock(EntityDetail.class);
        boolean result = assetLineageTypesValidator.hasValidClassificationTypes(entityDetail);
        assertFalse(result);
    }

    @Test
    void isValidLineageRelationshipType() {
        Relationship relationship = mock(Relationship.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(DATA_FLOW);
        when(relationship.getType()).thenReturn(instanceType);
        EntityProxy entityOneProxy = mock(EntityProxy.class);
        when(entityOneProxy.getType()).thenReturn(instanceType);
        when(relationship.getEntityOneProxy()).thenReturn(entityOneProxy);
        EntityProxy entityTwoProxy = mock(EntityProxy.class);
        when(relationship.getEntityTwoProxy()).thenReturn(entityTwoProxy);
        when(entityTwoProxy.getType()).thenReturn(instanceType);

        boolean result = assetLineageTypesValidator.isValidLineageRelationshipType(relationship);
        assertTrue(result);
    }

    @Test
    void isValidLineageRelationshipType_false() {
        Relationship relationship = mock(Relationship.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn("test");
        when(relationship.getType()).thenReturn(instanceType);
        EntityProxy entityOneProxy = mock(EntityProxy.class);
        when(entityOneProxy.getType()).thenReturn(instanceType);
        when(relationship.getEntityOneProxy()).thenReturn(entityOneProxy);
        EntityProxy entityTwoProxy = mock(EntityProxy.class);
        when(relationship.getEntityTwoProxy()).thenReturn(entityTwoProxy);
        when(entityTwoProxy.getType()).thenReturn(instanceType);

        boolean result = assetLineageTypesValidator.isValidLineageRelationshipType(relationship);
        assertFalse(result);
    }

    @Test
    void isValidLineageEntityType() {
        EntityDetail entityDetail = mock(EntityDetail.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(entityDetail.getType()).thenReturn(instanceType);
        when(instanceType.getTypeDefName()).thenReturn(ASSET);
        when(repositoryHelper.isTypeOf(SERVER_NAME, ASSET, ASSET)).thenReturn(true);

        boolean result = assetLineageTypesValidator.isValidLineageEntityType(entityDetail, SERVER_NAME);
        assertTrue(result);
    }

    @Test
    void isValidLineageEntityType_false() {
        EntityDetail entityDetail = mock(EntityDetail.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(entityDetail.getType()).thenReturn(instanceType);
        when(instanceType.getTypeDefName()).thenReturn(REFERENCEABLE);

        boolean result = assetLineageTypesValidator.isValidLineageEntityType(entityDetail, SERVER_NAME);
        assertFalse(result);
    }

    @Test
    void filterLineageClassifications() {
        EntityDetail entityDetail = mock(EntityDetail.class);
        List<Classification> classifications = mockClassifications();
        when(entityDetail.getClassifications()).thenReturn(classifications);

        List<Classification> result = assetLineageTypesValidator.filterLineageClassifications(classifications);
        assertEquals(classifications, result);
    }

    private List<Classification> mockClassifications() {
        Classification classification = mock(Classification.class);
        InstanceType classificationType = mock(InstanceType.class);
        when(classificationType.getTypeDefName()).thenReturn(CLASSIFICATION_NAME_INCOMPLETE);
        when(classification.getType()).thenReturn(classificationType);
        return Collections.singletonList(classification);
    }
}
