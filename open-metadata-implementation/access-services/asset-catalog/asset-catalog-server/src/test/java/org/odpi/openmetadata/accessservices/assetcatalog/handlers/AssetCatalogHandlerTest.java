/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetNotFoundException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Term;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssetCatalogHandlerTest {

    private static final String RELATIONSHIP_GUID = "212123-abc";
    private static final String ASSET_TYPE = "Process";
    private static final String GUID_PARAMETER = "assetGUID";
    private static final String CLASSIFICATION_NAME = "Confidentiality";
    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;
    private static final String ASSET_TYPE_GUID = "ababa-12232-abc";
    private static final String SEARCH_CRITERIA = "employee";
    private final String USER = "test-user";
    private static final String FIRST_GUID = "ababa-123-acbd";
    private static final String SECOND_GUID = "ababc-2134-2341f";
    private final String RELATIONSHIP_TYPE = "SemanticAssigment";
    private static final String RELATIONSHIP_TYPE_GUID = "adadad-bcba-123";

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private RepositoryErrorHandler errorHandler;

    @InjectMocks
    private AssetCatalogHandler assetCatalogHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getEntityDetails() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityDetails";

        mockEntityDetails(FIRST_GUID, methodName);

        AssetDescription result = assetCatalogHandler.getEntityDetails(USER, FIRST_GUID, ASSET_TYPE);

        assertEquals(FIRST_GUID, result.getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getRelationshipsByEntityGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getRelationshipsByEntityGUID";

        List<Relationship> relationshipsByType = Collections.singletonList(mockRelationship());

        when(repositoryHandler.getRelationshipsByType(USER,
                FIRST_GUID,
                ASSET_TYPE,
                null,
                RELATIONSHIP_TYPE,
                methodName)).thenReturn(relationshipsByType);

        List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship>
                result = assetCatalogHandler.getRelationshipsByEntityGUID(USER, FIRST_GUID, ASSET_TYPE, RELATIONSHIP_TYPE);


        assertEquals(relationshipsByType.get(0).getGUID(), result.get(0).getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getEntityClassificationByName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityClassificationByName";

        mockEntityDetails(FIRST_GUID, "getEntityClassifications");

        List<Classification> result = assetCatalogHandler
                .getEntityClassificationByName(USER, FIRST_GUID, ASSET_TYPE, CLASSIFICATION_NAME);


        assertEquals(CLASSIFICATION_NAME, result.get(0).getName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getLinkingRelationshipsBetweenAssets()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException {
        String methodName = "getLinkingRelationshipsBetweenAssets";

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(mocKInstanceGraph());

        List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> result = assetCatalogHandler
                .getLinkingRelationshipsBetweenAssets("server", USER, FIRST_GUID, SECOND_GUID);

        assertEquals(RELATIONSHIP_GUID, result.get(0).getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, "startAssetGUID", methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SECOND_GUID, "endAssetGUID", methodName);
    }

    @Test
    public void getIntermediateAssets()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException {

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(mocKInstanceGraph());

        List<AssetDescription> result = assetCatalogHandler.getIntermediateAssets(USER, FIRST_GUID, SECOND_GUID);

        assertEquals(FIRST_GUID, result.get(0).getGuid());
        String methodName = "getIntermediateAssets";
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, "startAssetGUID", methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SECOND_GUID, "endAssetGUID", methodName);
    }

    @Test
    public void getRelationships()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getRelationships";

        mockPagedRelationships(methodName);

        List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> result = assetCatalogHandler
                .getRelationships(USER, FIRST_GUID, ASSET_TYPE, RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE, FROM, PAGE_SIZE);


        assertEquals(RELATIONSHIP_GUID, result.get(0).getGuid());
        assertEquals(RELATIONSHIP_TYPE, result.get(0).getTypeDefName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getEntitiesFromNeighborhood()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException, TypeErrorException {

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getEntityNeighborhood(USER,
                FIRST_GUID,
                Collections.singletonList(ASSET_TYPE_GUID),
                Collections.singletonList(RELATIONSHIP_TYPE_GUID),
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                1))
                .thenReturn(mocKInstanceGraph());

        SearchParameters searchParams = mockSearchParams();
        List<AssetDescription> result = assetCatalogHandler.getEntitiesFromNeighborhood("server", USER, FIRST_GUID, searchParams);

        assertEquals(FIRST_GUID, result.get(0).getGuid());
        String methodName = "getEntitiesFromNeighborhood";
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
        verify(invalidParameterHandler, times(1)).validateObject(searchParams, "searchParameter", methodName);
    }

    @Test
    public void getTypeDefGUID()
            throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        String methodName = "getTypeDefGUID";
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        String typeDefGUID = assetCatalogHandler.getTypeDefGUID(USER, RELATIONSHIP_TYPE);
        assertEquals(RELATIONSHIP_TYPE_GUID, typeDefGUID);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
    }

    @Test
    public void searchByType() throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        String methodName = "searchByType";
        SearchParameters searchParams = mockSearchParams();

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.findEntitiesByPropertyValue(USER,
                ASSET_TYPE_GUID,
                SEARCH_CRITERIA,
                FROM,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                null,
                SequencingOrder.ANY,
                PAGE_SIZE)).thenReturn(mockEntities());

        List<Term> terms = assetCatalogHandler.searchByType(USER, SEARCH_CRITERIA, searchParams);
        assertEquals(FIRST_GUID, terms.get(0).getGuid());
        assertEquals(ASSET_TYPE, terms.get(0).getTypeDefName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validatePaging(searchParams.getFrom(), searchParams.getPageSize(), methodName);
        verify(invalidParameterHandler, times(1)).validateObject(searchParams, "searchParameter", methodName);
    }

    @Test
    public void buildContextByType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "buildContextByType";
        mockEntityDetails(FIRST_GUID, "getEntityDetails");
        mockMetadataCollection();
        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);

        Term term = assetCatalogHandler.buildContextByType(USER, FIRST_GUID, ASSET_TYPE);

        assertEquals(FIRST_GUID, term.getGuid());
        assertEquals(ASSET_TYPE, term.getTypeDefName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
    }

    private SearchParameters mockSearchParams() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLevel(1);
        searchParameters.setFrom(FROM);
        searchParameters.setPageSize(PAGE_SIZE);
        searchParameters.setRelationshipTypeGUIDs(Collections.singletonList(RELATIONSHIP_TYPE_GUID));
        searchParameters.setEntityTypeGUIDs(Collections.singletonList(ASSET_TYPE_GUID));
        return searchParameters;
    }


    private void mockPagedRelationships(String methodName) throws UserNotAuthorizedException, PropertyServerException {
        when(repositoryHandler.getPagedRelationshipsByType(USER,
                FIRST_GUID,
                ASSET_TYPE,
                RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE,
                FROM,
                PAGE_SIZE,
                methodName)).thenReturn(mockRelationships());
    }

    private OMRSMetadataCollection mockMetadataCollection() {
        OMRSMetadataCollection metadataCollection = mock(OMRSMetadataCollection.class);

        when(repositoryHandler.getMetadataCollection()).thenReturn(metadataCollection);
        return metadataCollection;
    }

    private InstanceGraph mocKInstanceGraph() {
        InstanceGraph instanceGraph = new InstanceGraph();
        instanceGraph.setEntities(mockEntities());
        instanceGraph.setRelationships(mockRelationships());
        return instanceGraph;
    }

    private List<Relationship> mockRelationships() {
        return Collections.singletonList(mockRelationship());
    }

    private List<EntityDetail> mockEntities() {
        List<EntityDetail> entityDetails = new ArrayList<>();
        EntityDetail entityDetail = new EntityDetail();
        entityDetail.setGUID(FIRST_GUID);

        entityDetail.setType(mockInstanceType(ASSET_TYPE, ASSET_TYPE_GUID));
        entityDetails.add(entityDetail);
        return entityDetails;
    }

    private void mockEntityDetails(String guid, String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(repositoryHandler
                .getEntityByGUID(USER, guid, GUID_PARAMETER, ASSET_TYPE, methodName))
                .thenReturn(entityDetail);

        when(entityDetail.getGUID()).thenReturn(guid);
        when(entityDetail.getType()).thenReturn(mockType(ASSET_TYPE, ASSET_TYPE_GUID));
        when(entityDetail.getClassifications()).thenReturn(mockClassifications());
    }

    private List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> mockClassifications() {
        return Collections.singletonList(mockClassification());
    }

    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification mockClassification() {
        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification classification =
                new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification();
        classification.setName(CLASSIFICATION_NAME);
        return classification;
    }

    private Relationship mockRelationship() {
        Relationship relationship = new Relationship();
        relationship.setGUID(RELATIONSHIP_GUID);
        InstanceType instanceType = mockInstanceType(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);
        relationship.setType(instanceType);
        return relationship;
    }

    private InstanceType mockInstanceType(String typeName, String typeGUID) {
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(typeName);
        instanceType.setTypeDefGUID(typeGUID);
        return instanceType;
    }

    private InstanceType mockType(String typeName, String guid) {
        InstanceType entityTypeDef = new InstanceType();
        entityTypeDef.setTypeDefGUID(guid);
        entityTypeDef.setTypeDefName(typeName);
        return entityTypeDef;
    }


    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
        TypeDefLink typeDefLink = mock(TypeDefLink.class);
        when(typeDefLink.getName()).thenReturn("Referenceable");
        when(entityTypeDef.getSuperType()).thenReturn(typeDefLink);
    }

}
