/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ClassificationHandlerTest {
    private static final String GUID = "guid";
    @Mock
    private HandlerHelper handlerHelper;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @InjectMocks
    private ClassificationHandler classificationHandler;

    @Test
    void buildClassificationContext() throws OCFCheckedExceptionBase {

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);

        RelationshipsContext context = mock(RelationshipsContext.class);
        when(handlerHelper.buildContextForLineageClassifications(entityDetail)).thenReturn(context);
        Map<String, RelationshipsContext> response =
                classificationHandler.buildClassificationContext(entityDetail, AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
        assertEquals(context, response.get(AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT.getEventTypeName()));
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, GUID_PARAMETER, "buildClassificationContext");
    }
}
