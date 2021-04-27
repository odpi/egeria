/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NAME;

public class AssetCatalogHandlerTest {

    private static final String RELATIONSHIP_GUID = "212123-abc";
    private static final String ASSET_TYPE = "Process";
    private static final String CLASSIFICATION_NAME = "Confidentiality";
    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;
    private static final String ASSET_TYPE_GUID = "ababa-12232-abc";
    private static final String SEARCH_CRITERIA = "employee";
    private static final String FIRST_GUID = "ababa-123-acbd";
    private static final String SECOND_GUID = "ababc-2134-2341f";
    private static final String RELATIONSHIP_TYPE_GUID = "adadad-bcba-123";
    private final String USER = "test-user";
    private final String RELATIONSHIP_TYPE = "SemanticAssigment";
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

    @InjectMocks
    private CommonHandler commonHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getEntityDetails() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getEntityDetails";

        mockMetadataCollection();
        mockEntityDetails(FIRST_GUID);

        AssetDescription result = assetCatalogHandler.getEntityDetails(USER, FIRST_GUID, ASSET_TYPE);

        assertEquals(FIRST_GUID, result.getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getEntityDetails_throwsInvalidParameterException() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getEntityDetails";
        mockMetadataCollection();
        mockEntityDetails(FIRST_GUID);

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(), this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(InvalidParameterException.class, () -> assetCatalogHandler.getEntityDetails(USER, FIRST_GUID, ASSET_TYPE));
    }

    @Test
    public void getRelationshipsByEntityGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getRelationshipsByEntityGUID";

        List<Relationship> relationshipsByType = Collections.singletonList(mockRelationship());

        when(repositoryHandler.getRelationshipsByType(USER,
                FIRST_GUID,
                ASSET_TYPE,
                null,
                null,
                methodName)).thenReturn(relationshipsByType);

        List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship>
                result = assetCatalogHandler.getRelationshipsByEntityGUID(USER, FIRST_GUID, ASSET_TYPE);


        assertEquals(relationshipsByType.get(0).getGUID(), result.get(0).getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getRelationshipsByEntityGUID_throwsInvalidParameterException() throws UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {

        String methodName = "getRelationshipsByEntityGUID";

        List<Relationship> relationshipsByType = Collections.singletonList(mockRelationship());

        when(repositoryHandler.getRelationshipsByType(USER,
                FIRST_GUID,
                ASSET_TYPE,
                null,
                RELATIONSHIP_TYPE,
                methodName)).thenReturn(relationshipsByType);

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException.class,
                () -> assetCatalogHandler.getRelationshipsByEntityGUID(USER, FIRST_GUID, ASSET_TYPE));

    }

    @Test
    public void getEntityClassificationByName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getEntityClassificationByName";

        mockMetadataCollection();
        mockEntityDetails(FIRST_GUID);

        List<Classification> result = assetCatalogHandler
                .getEntityClassificationByName(USER, FIRST_GUID, ASSET_TYPE, CLASSIFICATION_NAME);


        assertEquals(CLASSIFICATION_NAME, result.get(0).getName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getEntityClassificationByName_throwsInvalidParameterException() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getEntityClassificationByName";

        mockMetadataCollection();
        mockEntityDetails(FIRST_GUID);

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(InvalidParameterException.class,
                () -> assetCatalogHandler.getEntityClassificationByName(USER, FIRST_GUID, ASSET_TYPE, CLASSIFICATION_NAME));

    }

    @Test
    public void getLinkingRelationshipsBetweenAssets()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetCatalogException {
        String methodName = "getLinkingRelationshipsBetweenAssets";

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(mockInstanceGraph());

        List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> result = assetCatalogHandler
                .getLinkingRelationshipsBetweenAssets("server", USER, FIRST_GUID, SECOND_GUID);

        assertEquals(RELATIONSHIP_GUID, result.get(0).getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, "startAssetGUID", methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SECOND_GUID, "endAssetGUID", methodName);
    }

    @Test
    public void getLinkingRelationshipsBetweenAssets_throwsInvalidParameterException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        String methodName = "getLinkingRelationshipsBetweenAssets";

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(mockInstanceGraph());

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(InvalidParameterException.class,
                () -> assetCatalogHandler.getLinkingRelationshipsBetweenAssets("server", USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getLinkingRelationshipsBetweenAssets_throwsPropertyServerException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();

        FunctionNotSupportedException mockedException = new FunctionNotSupportedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(), this.getClass().getName(), "");

        doThrow(mockedException).when(metadataCollection).getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null);
        String methodName = "getLinkingRelationshipsBetweenAssets";
        doThrow(new PropertyServerException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "")).when(errorHandler).handleRepositoryError(mockedException, methodName);
        assertThrows(PropertyServerException.class,
                () -> assetCatalogHandler.getLinkingRelationshipsBetweenAssets("server", USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getLinkingRelationshipsBetweenAssets_throwsUserNotAuthorizedException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, UserNotAuthorizedException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();

        doThrow(new org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", "")).when(metadataCollection).getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null);
        String methodName = "getLinkingRelationshipsBetweenAssets";
        doThrow(new UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", "")).when(errorHandler).handleUnauthorizedUser(USER, methodName);

        assertThrows(UserNotAuthorizedException.class,
                () -> assetCatalogHandler.getLinkingRelationshipsBetweenAssets("server", USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getLinkingRelationshipsBetweenAssets_throwsAssetNotFoundException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(null);

        assertThrows(AssetCatalogException.class,
                () -> assetCatalogHandler.getLinkingRelationshipsBetweenAssets("server", USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getIntermediateAssets()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetCatalogException {

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(mockInstanceGraph());

        List<AssetDescription> result = assetCatalogHandler.getIntermediateAssets(USER, FIRST_GUID, SECOND_GUID);

        assertEquals(FIRST_GUID, result.get(0).getGuid());
        String methodName = "getIntermediateAssets";
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, "startAssetGUID", methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SECOND_GUID, "endAssetGUID", methodName);
    }

    @Test
    public void getIntermediateAssets_throwsInvalidParameterException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        String methodName = "getIntermediateAssets";

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(mockInstanceGraph());

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(), this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(InvalidParameterException.class,
                () -> assetCatalogHandler.getIntermediateAssets(USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getIntermediateAssets_throwsPropertyServerException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        String methodName = "getIntermediateAssets";
        FunctionNotSupportedException mockedException = new FunctionNotSupportedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "");

        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenThrow(mockedException);
        doThrow(PropertyServerException.class).when(errorHandler).handleRepositoryError(mockedException, methodName);

        assertThrows(PropertyServerException.class,
                () -> assetCatalogHandler.getIntermediateAssets(USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getIntermediateAssets_throwsUserNotAuthorizedException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, UserNotAuthorizedException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        String methodName = "getIntermediateAssets";
        doThrow(new org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", "")).when(metadataCollection).getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null);
        doThrow(new UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", "")).when(errorHandler).handleUnauthorizedUser(USER, methodName);

        assertThrows(UserNotAuthorizedException.class,
                () -> assetCatalogHandler.getIntermediateAssets(USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getIntermediateAssets_throwsAssetNotFoundException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getLinkingEntities(USER, FIRST_GUID, SECOND_GUID, Collections.singletonList(InstanceStatus.ACTIVE), null))
                .thenReturn(null);

        assertThrows(AssetCatalogException.class,
                () -> assetCatalogHandler.getIntermediateAssets(USER, FIRST_GUID, SECOND_GUID));
    }

    @Test
    public void getRelationships()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getRelationships";

        mockPagedRelationships(methodName);
        mockMetadataCollection();
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> result =
                assetCatalogHandler.getRelationships(USER, FIRST_GUID, ASSET_TYPE,
                        RELATIONSHIP_TYPE, FROM, PAGE_SIZE);

        assertEquals(RELATIONSHIP_GUID, result.get(0).getGuid());
        assertEquals(RELATIONSHIP_TYPE, result.get(0).getType().getName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    @Test
    public void getRelationships_throwsInvalidParameterException() throws UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        String methodName = "getRelationships";

        mockPagedRelationships(methodName);

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException.class,
                () -> assetCatalogHandler.getRelationships(USER, FIRST_GUID, ASSET_TYPE, RELATIONSHIP_TYPE, FROM, PAGE_SIZE));

    }

    @Test
    public void getRelationships_throwsPropertyServerException() throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationships";
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        doThrow(new PropertyServerException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "")).when(repositoryHandler).getPagedRelationshipsByType(USER,
                FIRST_GUID,
                ASSET_TYPE,
                RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE,
                FROM,
                PAGE_SIZE,
                methodName);

        assertThrows(PropertyServerException.class,
                () -> assetCatalogHandler.getRelationships(USER, FIRST_GUID, ASSET_TYPE, RELATIONSHIP_TYPE, FROM, PAGE_SIZE));
    }

    @Test
    public void getRelationships_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationships";
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        doThrow(new UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", "")).when(repositoryHandler).getPagedRelationshipsByType(USER,
                FIRST_GUID,
                ASSET_TYPE,
                RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE,
                FROM,
                PAGE_SIZE,
                methodName);

        assertThrows(UserNotAuthorizedException.class,
                () -> assetCatalogHandler.getRelationships(USER, FIRST_GUID, ASSET_TYPE, RELATIONSHIP_TYPE, FROM, PAGE_SIZE));
    }

    @Test
    public void getEntitiesFromNeighborhood()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetCatalogException, TypeErrorException {

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getEntityNeighborhood(USER,
                FIRST_GUID,
                Collections.singletonList(ASSET_TYPE_GUID),
                Collections.singletonList(RELATIONSHIP_TYPE_GUID),
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                1))
                .thenReturn(mockInstanceGraph());

        SearchParameters searchParams = mockSearchParams();
        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);
        List<AssetDescription> result = assetCatalogHandler.getEntitiesFromNeighborhood("server", USER, FIRST_GUID, searchParams);

        assertEquals(FIRST_GUID, result.get(0).getGuid());
        String methodName = "getEntitiesFromNeighborhood";
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
        verify(invalidParameterHandler, times(1)).validateObject(searchParams, "searchParameter", methodName);
    }

    @Test
    public void getEntitiesFromNeighborhood_throwsInvalidParameterException() throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {
        String methodName = "getEntitiesFromNeighborhood";

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.getEntityNeighborhood(USER,
                FIRST_GUID,
                Collections.singletonList(ASSET_TYPE_GUID),
                Collections.singletonList(RELATIONSHIP_TYPE_GUID),
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                1))
                .thenReturn(mockInstanceGraph());

        SearchParameters searchParams = mockSearchParams();

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException.class,
                () -> assetCatalogHandler.getEntitiesFromNeighborhood("server", USER, FIRST_GUID, searchParams));
    }

    @Test
    public void getEntitiesFromNeighborhood_throwsPropertyServerException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PropertyServerException {

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        SearchParameters searchParams = mockSearchParams();
        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);

        String methodName = "getAssetNeighborhood";
        FunctionNotSupportedException mockedException = new FunctionNotSupportedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "");
        doThrow(mockedException).when(metadataCollection).getEntityNeighborhood(USER,
                FIRST_GUID,
                Collections.singletonList(ASSET_TYPE_GUID),
                Collections.singletonList(RELATIONSHIP_TYPE_GUID),
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                1);
        doThrow(PropertyServerException.class).when(errorHandler).handleRepositoryError(mockedException, methodName);
        assertThrows(PropertyServerException.class,
                () -> assetCatalogHandler.getEntitiesFromNeighborhood("server", USER, FIRST_GUID, searchParams));

    }

    @Test
    public void getEntitiesFromNeighborhood_throwsUserNotAuthorizedException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, UserNotAuthorizedException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        SearchParameters searchParams = mockSearchParams();
        String methodName = "getAssetNeighborhood";
        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);

        doThrow(new org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", "")).when(metadataCollection).getEntityNeighborhood(USER,
                FIRST_GUID,
                Collections.singletonList(ASSET_TYPE_GUID),
                Collections.singletonList(RELATIONSHIP_TYPE_GUID),
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                1);
        doThrow(new UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", "")).when(errorHandler).handleUnauthorizedUser(USER, methodName);
        assertThrows(UserNotAuthorizedException.class,
                () -> assetCatalogHandler.getEntitiesFromNeighborhood("server", USER, FIRST_GUID, searchParams));
    }

    @Test
    public void getEntitiesFromNeighborhood_throwsAssetNotFoundException() throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException {
        OMRSMetadataCollection metadataCollection = mockMetadataCollection();

        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);
        when(metadataCollection.getEntityNeighborhood(USER,
                FIRST_GUID,
                Collections.singletonList(ASSET_TYPE_GUID),
                Collections.singletonList(RELATIONSHIP_TYPE_GUID),
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                1))
                .thenReturn(null);

        SearchParameters searchParams = mockSearchParams();

        assertThrows(AssetCatalogException.class,
                () -> assetCatalogHandler.getEntitiesFromNeighborhood("server", USER, FIRST_GUID, searchParams));

    }

    @Test
    public void getTypeDefGUID() {
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        String typeDefGUID = commonHandler.getTypeDefGUID(USER, RELATIONSHIP_TYPE);
        assertEquals(RELATIONSHIP_TYPE_GUID, typeDefGUID);
    }

    @Test
    public void searchByType() throws InvalidParameterException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        String methodName = "searchByType";
        SearchParameters searchParams = mockSearchParams();
        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);
        mockSearchString(SEARCH_CRITERIA, searchParams.getCaseInsensitive());
        InstanceProperties matchProperties = mockMatchProperties();

        OMRSMetadataCollection metadataCollection = mockMetadataCollection();
        when(metadataCollection.findEntitiesByProperty(USER,
                ASSET_TYPE_GUID,
                matchProperties,
                MatchCriteria.ANY,
                FROM,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                null,
                SequencingOrder.ANY,
                PAGE_SIZE)).thenReturn(mockEntities());

        List<AssetElements> assetElements = assetCatalogHandler.searchByType(USER, SEARCH_CRITERIA, searchParams);
        assertEquals(FIRST_GUID, assetElements.get(0).getGuid());
        assertEquals(ASSET_TYPE, assetElements.get(0).getType().getName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validatePaging(searchParams.getFrom(), searchParams.getPageSize(), methodName);
        verify(invalidParameterHandler, times(1)).validateObject(searchParams, "searchParameter", methodName);
    }

    @Test
    public void searchByType_throwsInvalidParameterException() throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
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

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException.class,
                () -> assetCatalogHandler.searchByType(USER, SEARCH_CRITERIA, searchParams));

    }

    @Test
    public void buildContextByType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "buildContextByType";

        mockMetadataCollection();
        mockEntityDetails(FIRST_GUID);
        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);

        AssetElements assetElements = assetCatalogHandler.buildContextByType(USER, FIRST_GUID, ASSET_TYPE);

        assertEquals(FIRST_GUID, assetElements.getGuid());
        assertEquals(ASSET_TYPE, assetElements.getType().getName());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
    }

    @Test
    public void buildContextByType_throwsInvalidParameterException() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "buildContextByType";
        mockMetadataCollection();
        mockEntityDetails(FIRST_GUID);
        mockTypeDef(ASSET_TYPE, ASSET_TYPE_GUID);

        doThrow(new org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(),
                this.getClass().getName(), "", ""))
                .when(invalidParameterHandler).validateUserId(USER, methodName);

        assertThrows(InvalidParameterException.class,
                () -> assetCatalogHandler.buildContextByType(USER, FIRST_GUID, ASSET_TYPE));
    }

    private SearchParameters mockSearchParams() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLevel(1);
        searchParameters.setFrom(FROM);
        searchParameters.setPageSize(PAGE_SIZE);
        searchParameters.setRelationshipTypeGUIDs(Collections.singletonList(RELATIONSHIP_TYPE_GUID));
        searchParameters.setEntityTypes(Collections.singletonList(ASSET_TYPE));
        searchParameters.setCaseInsensitive(Boolean.FALSE);
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

    private OMRSMetadataCollection mockMetadataCollection() throws RepositoryErrorException {
        OMRSMetadataCollection metadataCollection = mock(OMRSMetadataCollection.class);

        when(repositoryHandler.getMetadataCollection()).thenReturn(metadataCollection);

        when(metadataCollection.getMetadataCollectionId(USER)).thenReturn("metadataCollectionID");
        when(repositoryHelper.getMetadataCollectionName("metadataCollectionID")).thenReturn("metadataCollectionName");
        return metadataCollection;
    }

    private InstanceGraph mockInstanceGraph() {
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

    private void mockEntityDetails(String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(commonHandler
                .getEntityByGUID(USER, guid, ASSET_TYPE))
                .thenReturn(entityDetail);

        when(entityDetail.getGUID()).thenReturn(guid);
        when(entityDetail.getType()).thenReturn(mockType(ASSET_TYPE, ASSET_TYPE_GUID));
        when(entityDetail.getClassifications()).thenReturn(mockClassifications());
        when(entityDetail.getProperties()).thenReturn(mockProperties());
    }

    private InstanceProperties mockProperties() {
        return mock(InstanceProperties.class);
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

    private void mockSearchString(String searchCriteria, boolean isCaseSensitive) {
        when(repositoryHelper.getContainsRegex(searchCriteria, isCaseSensitive)).thenReturn(searchCriteria);
    }

    private InstanceProperties mockMatchProperties() {
        InstanceProperties matchProperties = new InstanceProperties();
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(SEARCH_CRITERIA);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

        matchProperties.setProperty(NAME, primitivePropertyValue);
        return matchProperties;
    }

}
