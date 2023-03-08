/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import com.google.common.collect.Multimap;
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
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CATEGORY_ANCHOR;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_ANCHOR;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_CATEGORIZATION;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class GlossaryContextHandlerTest {
    private static final String GUID = "guid";
    private static final String USER = "user";
    private static final String CATEGORY_GUID = "categoryGuid";
    @Mock
    private HandlerHelper handlerHelper;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @Mock
    private AssetContextHandler assetContextHandler;
    @InjectMocks
    private GlossaryContextHandler glossaryContextHandler;

    @Test
    void getGlossaryTermDetails() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EntityDetail entityDetail = new EntityDetail();
        when(handlerHelper.getEntityDetails(USER, GUID, GLOSSARY_TERM)).thenReturn(entityDetail);

        EntityDetail response = glossaryContextHandler.getGlossaryTermDetails(USER, GUID);
        assertEquals(response, entityDetail);
    }

    @Test
    void buildGlossaryTermContext() throws OCFCheckedExceptionBase {
        List<Relationship> semanticAssignments = mockGetRelationships(SEMANTIC_ASSIGNMENT, GLOSSARY_TERM, GUID);
        List<Relationship> termCategorizations = mockGetRelationships(TERM_CATEGORIZATION, GLOSSARY_TERM, GUID);
        List<Relationship> glossaries = mockGetRelationships(TERM_ANCHOR, GLOSSARY_TERM, GUID);
        List<Relationship> glossariesForCategories = mockGetRelationships(CATEGORY_ANCHOR, GLOSSARY_CATEGORY, CATEGORY_GUID);

        EntityDetail glossaryTerm = mock(EntityDetail.class);
        when(glossaryTerm.getGUID()).thenReturn(GUID);

        RelationshipsContext semanticAssignmentsContext = mock(RelationshipsContext.class);
        when(handlerHelper.buildContextForRelationships(USER, GUID, semanticAssignments)).thenReturn(semanticAssignmentsContext);

        RelationshipsContext termCategorizationsContext = mock(RelationshipsContext.class);
        when(handlerHelper.buildContextForRelationships(USER, GUID, termCategorizations)).thenReturn(termCategorizationsContext);

        RelationshipsContext glossariesContext = mock(RelationshipsContext.class);
        when(handlerHelper.buildContextForRelationships(USER, GUID, glossaries)).thenReturn(glossariesContext);

        RelationshipsContext glossariesForCategoriesContext = mock(RelationshipsContext.class);
        when(handlerHelper.buildContextForRelationships(USER, GUID, glossariesForCategories)).thenReturn(glossariesForCategoriesContext);

        RelationshipsContext classificationsContext = mock(RelationshipsContext.class);
        when(handlerHelper.buildContextForLineageClassifications(glossaryTerm)).thenReturn(classificationsContext);

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(handlerHelper.getEntityAtTheEnd(USER, GUID, semanticAssignments.get(0))).thenReturn(entityDetail);

        Multimap<String, RelationshipsContext> response = glossaryContextHandler.buildGlossaryTermContext(USER, glossaryTerm);

        verify(invalidParameterHandler, times(1)).validateGUID(GUID, GUID_PARAMETER, "buildGlossaryTermContext");
        Collection<RelationshipsContext> semanticAssignmentsResponse =
                response.get(AssetLineageEventType.SEMANTIC_ASSIGNMENTS_EVENT.getEventTypeName());
        assertFalse(semanticAssignmentsResponse.isEmpty());
        assertEquals(semanticAssignmentsResponse.stream().findFirst().get(), semanticAssignmentsContext);

        Collection<RelationshipsContext> termCategorizationsResponse =
                response.get(AssetLineageEventType.TERM_CATEGORIZATIONS_EVENT.getEventTypeName());
        assertFalse(termCategorizationsResponse.isEmpty());
        assertEquals(termCategorizationsResponse.stream().findFirst().get(), termCategorizationsContext);

        Collection<RelationshipsContext> termAnchorsResponse = response.get(AssetLineageEventType.TERM_ANCHORS_EVENT.getEventTypeName());
        assertFalse(termAnchorsResponse.isEmpty());
        assertEquals(termAnchorsResponse.stream().findFirst().get(), glossariesContext);

        Collection<RelationshipsContext> categoryAnchorsResponse = response.get(AssetLineageEventType.CATEGORY_ANCHORS_EVENT.getEventTypeName());
        assertFalse(categoryAnchorsResponse.isEmpty());
        assertEquals(categoryAnchorsResponse.stream().findFirst().get(), glossariesForCategoriesContext);

        Collection<RelationshipsContext> classificationsResponse =
                response.get(AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT.getEventTypeName());
        assertFalse(classificationsResponse.isEmpty());
        assertEquals(classificationsResponse.stream().findFirst().get(), classificationsContext);

        verify(assetContextHandler, times(1)).buildSchemaElementContext(USER, entityDetail);
    }

    @Test
    void hasGlossaryTermLineageRelationships_true() throws OCFCheckedExceptionBase {
        EntityDetail entityDetail = mock(EntityDetail.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(GLOSSARY_TERM);
        when(entityDetail.getGUID()).thenReturn(GUID);
        when(entityDetail.getType()).thenReturn(instanceType);

        mockGetRelationships(SEMANTIC_ASSIGNMENT, GLOSSARY_TERM, GUID);

        assertTrue(glossaryContextHandler.hasGlossaryTermLineageRelationships(USER, entityDetail));
    }

    @Test
    void hasGlossaryTermLineageRelationships_false() throws OCFCheckedExceptionBase {
        EntityDetail entityDetail = mock(EntityDetail.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(GLOSSARY_TERM);
        when(entityDetail.getGUID()).thenReturn(GUID);
        when(entityDetail.getType()).thenReturn(instanceType);

        when(handlerHelper.getRelationshipsByType(USER, GUID, SEMANTIC_ASSIGNMENT, GLOSSARY_TERM)).thenReturn(new ArrayList<>());

        assertFalse(glossaryContextHandler.hasGlossaryTermLineageRelationships(USER, entityDetail));
    }

    private List<Relationship> mockGetRelationships(String relationshipType, String entityType, String guid) throws OCFCheckedExceptionBase {
        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship = mock(Relationship.class);

        EntityProxy entityOneProxy = mock(EntityProxy.class);
        when(entityOneProxy.getGUID()).thenReturn(guid);
        when(relationship.getEntityOneProxy()).thenReturn(entityOneProxy);

        EntityProxy entityTwoProxy = mock(EntityProxy.class);
        when(entityTwoProxy.getGUID()).thenReturn(CATEGORY_GUID);
        when(relationship.getEntityTwoProxy()).thenReturn(entityTwoProxy);

        relationships.add(relationship);
        when(handlerHelper.getRelationshipsByType(USER, guid, relationshipType, entityType)).thenReturn(relationships);

        return relationships;
    }

}
