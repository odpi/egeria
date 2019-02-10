/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class TestSubjectAreaGlossaryRESTServices {
    final static String TEST_SERVER_NAME = "Test Server";
    @Mock
    private OMRSAPIHelper oMRSAPIHelper;
    @Mock
    private OMRSRepositoryConnector repositoryConnector;

    @BeforeMethod
    public void setup() throws UserNotAuthorizedException, EntityNotKnownException, ConnectorCheckedException, InvalidParameterException, RepositoryErrorException, ConnectionCheckedException {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testC() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Glossary g = new Glossary();
        g.setName("ff");
        String json =objectMapper.writeValueAsString(g);
    }

    @Test
    public void testCreateGlossary() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();

        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        String qualifiedName = "test qname";
        // set up the mocks
        EntityDetail mockEntityAdd = createMockGlossary(displayName,usage,description,testguid1,qualifiedName);
        EntityDetail mockEntityGet = createMockGlossary(displayName,usage,description,testguid1,qualifiedName);
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        mockEntityAdd.setGUID(testguid1);
        mockEntityGet.setGUID(testguid1);
        // mock out the add entity
        when( oMRSAPIHelper.callOMRSAddEntity(anyString(),any())).thenReturn(mockEntityAdd);
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);

        // set the mock omrs in to the rest file.
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        Glossary requestedGlossary = new Glossary();
        requestedGlossary.setName(displayName);
        requestedGlossary.setUsage(usage);
        requestedGlossary.setDescription(description);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.createGlossary(TEST_SERVER_NAME, testuserid, requestedGlossary);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Glossary));
        Glossary returnedGlossary = ((GlossaryResponse)response).getGlossary();

        assertEquals(requestedGlossary.getName(),returnedGlossary.getName());
        assertEquals(requestedGlossary.getUsage(),returnedGlossary.getUsage());
        assertEquals(requestedGlossary.getDescription(),returnedGlossary.getDescription());
    }

    @Test
    public void testCreateGlossaryNoName() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";

        final String testGlossaryName = "TestGlossary";
        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        String displayName = null;
        String usage = "string1";
        String description = "string2 fsdgsdg";
        String qualifiedName = "test qname";
        Glossary requestedGlossary = new Glossary();
        requestedGlossary.setUsage(usage);
        requestedGlossary.setDescription(description);
        requestedGlossary.setQualifiedName(qualifiedName);
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.createGlossary(TEST_SERVER_NAME, testuserid, requestedGlossary);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidExResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidExResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-060"));

        // check with an empty string
        requestedGlossary.setName("");

        response = subjectAreaGlossaryOmasREST.createGlossary(TEST_SERVER_NAME, testuserid, requestedGlossary);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        invalidExResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidExResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-060"));
    }


    @Test
    public void testGetGlossaryByGuid() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";

        final String testGlossaryName = "TestGlossary";
        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();

        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        String qualifiedName = "test qname";
        // set up the mock
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        EntityDetail mockEntityGet = createMockGlossary(displayName,usage,description,testguid1,qualifiedName);

        mockEntityGet.setGUID(testguid1);

        when( oMRSAPIHelper.callOMRSGetEntityByGuid(
                any(),
                any()
        )).thenReturn(mockEntityGet);

        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
        new SubjectAreaServicesInstance(repositoryConnector);


        // set the mock omrs in to the rest file.
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.getGlossaryByGuid(TEST_SERVER_NAME, testuserid, testguid1 );
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Glossary));
        Glossary returnedGlossary = ((GlossaryResponse)response).getGlossary();
        assertTrue(returnedGlossary.getName().equals(displayName));
        assertTrue(returnedGlossary.getUsage().equals(usage));
        assertTrue(returnedGlossary.getDescription().equals(description));
    }


    @Test
    public void testUpdateGlossary() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        // set the mock omrs in to the rest file.
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);


        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        String qualifiedName = "string2 fsdgsdgss";
        final String newName = "newName";
        final String newQualifiedName = qualifiedName +"new";

        final String newUsage = usage +"new";
        final String newDescription = description + "new";
        // set up the mocks
        EntityDetail mockEntityUpdate = createMockGlossary(newName,newUsage,newDescription,testguid1,newQualifiedName);
        EntityDetail mockEntityGet = createMockGlossary(displayName,usage,description,testguid1,qualifiedName);
        List<EntityDetail> mockGlossaryList = new ArrayList<>();

        // mock out the update entity
        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),any())).thenReturn(mockEntityUpdate);
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
        new SubjectAreaServicesInstance(repositoryConnector);

        Glossary requestedGlossary = new Glossary();
        requestedGlossary.setName(newName);
        requestedGlossary.setUsage(newUsage);
        requestedGlossary.setDescription(newDescription);

        requestedGlossary.setQualifiedName(newQualifiedName);


        // mock out the update entity
        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),any())).thenReturn(mockEntityUpdate);
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.updateGlossary(TEST_SERVER_NAME, testuserid,testguid1, requestedGlossary,true);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Glossary));
        Glossary returnedGlossary = ((GlossaryResponse)response).getGlossary();

        assertEquals(newName,returnedGlossary.getName());
    }

    @Test
    public void testDeleteGlossary() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        // mock out the get entity
        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        String qualifiedName = "test qname";

        EntityDetail mockEntityGet = createMockGlossary(displayName,usage,description,testguid1,qualifiedName);
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
        List<Relationship> mockRelationshipList = new ArrayList<>();

        when( oMRSAPIHelper.callGetRelationshipsForEntity(
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                anyInt()

        )).thenReturn(mockRelationshipList);

        when( oMRSAPIHelper.callOMRSDeleteEntity(
                anyString(),
                anyString(),
                anyString(),
                anyString()

        )).thenReturn(mockEntityGet);

        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        new SubjectAreaServicesInstance(repositoryConnector);
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.deleteGlossary(TEST_SERVER_NAME, testuserid, testguid1,false);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Glossary));
    }


    @Test
    public void testDeleteGlossaryWithContent() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";

        // mock out the get entity
        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        String qualifiedName = "test qname";
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        EntityDetail mockEntityGet = createMockGlossary(displayName,usage,description,testguid1,qualifiedName);
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
        List<Relationship> mockRelationshipList = new ArrayList<>();
        mockRelationshipList.add(new Relationship());
        Glossary mockDeletedGlossary = new Glossary();
        when( oMRSAPIHelper.callGetRelationshipsForEntity(
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                anyInt()

        )).thenReturn(mockRelationshipList);

        when( oMRSAPIHelper.callOMRSDeleteEntity(
                anyString(),
                anyString(),
                anyString(),
                anyString()

        )).thenReturn(mockEntityGet);

        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        new SubjectAreaServicesInstance(repositoryConnector);

        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.deleteGlossary(TEST_SERVER_NAME, testuserid, testguid1,false);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidExResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidExResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-033"));
    }

    private static EntityDetail createMockGlossary(String displayName, String usage, String description, String testguid1,String qualifiedName) {
        // TODO          "qualifiedName","additionalProperties",

        EntityDetail mockEntity = new EntityDetail();
        InstanceProperties instanceProperties = new InstanceProperties();

        PrimitivePropertyValue primitivePropertyValue;

        SubjectAreaUtils.addStringToInstanceProperty("displayName",displayName ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("usage",usage ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("description",description ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("qualifiedName",description ,instanceProperties);


        InstanceStatus status = InstanceStatus.ACTIVE;
        mockEntity.setStatus(status);

        // In the models we are generating from we only have map<String,String> types, this code assumes those types.
        mockEntity.setProperties(instanceProperties);
        mockEntity.setGUID(testguid1);
        mockEntity.setVersion(1L);
        InstanceType typeOfEntity = new InstanceType();
        typeOfEntity.setTypeDefName("Glossary");
        typeOfEntity.setTypeDefGUID("test type guid");
        mockEntity.setType(typeOfEntity);

        return mockEntity;
    }

    private OMRSMetadataCollection getMockOmrsMetadataCollection()
    {
        return new OMRSMetadataCollection(null,"111","222",null,null)
        {
            @Override
            public TypeDefGallery getAllTypes(String userId) throws RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public TypeDefGallery findTypesByName(String userId, String name) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<TypeDef> findTypeDefsByCategory(String userId, TypeDefCategory category) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<AttributeTypeDef> findAttributeTypeDefsByCategory(String userId, AttributeTypeDefCategory category) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<TypeDef> findTypeDefsByProperty(String userId, TypeDefProperties matchCriteria) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<TypeDef> findTypesByExternalID(String userId, String standard, String organization, String identifier) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<TypeDef> searchForTypeDefs(String userId, String searchCriteria) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public TypeDef getTypeDefByGUID(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public AttributeTypeDef getAttributeTypeDefByGUID(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public TypeDef getTypeDefByName(String userId, String name) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public AttributeTypeDef getAttributeTypeDefByName(String userId, String name) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public void addTypeDefGallery(String userId, TypeDefGallery newTypes) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefKnownException, TypeDefConflictException, InvalidTypeDefException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void addTypeDef(String userId, TypeDef newTypeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefKnownException, TypeDefConflictException, InvalidTypeDefException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void addAttributeTypeDef(String userId, AttributeTypeDef newAttributeTypeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefKnownException, TypeDefConflictException, InvalidTypeDefException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public boolean verifyTypeDef(String userId, TypeDef typeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException
            {
                return false;
            }

            @Override
            public boolean verifyAttributeTypeDef(String userId, AttributeTypeDef attributeTypeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException
            {
                return false;
            }

            @Override
            public TypeDef updateTypeDef(String userId, TypeDefPatch typeDefPatch) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, PatchErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public void deleteTypeDef(String userId, String obsoleteTypeDefGUID, String obsoleteTypeDefName) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, TypeDefInUseException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void deleteAttributeTypeDef(String userId, String obsoleteTypeDefGUID, String obsoleteTypeDefName) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, TypeDefInUseException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public TypeDef reIdentifyTypeDef(String userId, String originalTypeDefGUID, String originalTypeDefName, String newTypeDefGUID, String newTypeDefName) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public AttributeTypeDef reIdentifyAttributeTypeDef(String userId, String originalAttributeTypeDefGUID, String originalAttributeTypeDefName, String newAttributeTypeDefGUID, String newAttributeTypeDefName) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail isEntityKnown(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntitySummary getEntitySummary(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail getEntityDetail(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityProxyOnlyException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail getEntityDetail(String userId, String guid, Date asOfTime) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityProxyOnlyException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<Relationship> getRelationshipsForEntity(String userId, String entityGUID, String relationshipTypeGUID, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<EntityDetail> findEntitiesByProperty(String userId, String entityTypeGUID, InstanceProperties   exactMatchProperties, MatchCriteria   exactMatchCriteria, InstanceProperties   fuzzyMatchProperties, MatchCriteria   fuzzyMatchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<EntityDetail> findEntitiesByClassification(String userId, String entityTypeGUID, String classificationName, InstanceProperties  exactMatchClassificationProperties, MatchCriteria    exactMatchCriteria, InstanceProperties   fuzzyMatchClassificationProperties, MatchCriteria    fuzzyMatchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, ClassificationErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<EntityDetail> findEntitiesByPropertyValue(String userId, String entityTypeGUID, String searchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship isRelationshipKnown(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship getRelationship(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship getRelationship(String userId, String guid, Date asOfTime) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<Relationship> findRelationshipsByProperty(String userId, String relationshipTypeGUID, InstanceProperties  exactMatchProperties, MatchCriteria    exactMatchCriteria, InstanceProperties   fuzzyMatchProperties, MatchCriteria  fuzzyMatchCriteria, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<Relationship> findRelationshipsByPropertyValue(String userId, String relationshipTypeGUID, String searchCriteria, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public InstanceGraph getLinkingEntities(String userId, String startEntityGUID, String endEntityGUID, List<InstanceStatus> limitResultsByStatus, Date asOfTime) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public InstanceGraph getEntityNeighborhood(String userId, String entityGUID, List<String> entityTypeGUIDs, List<String> relationshipTypeGUIDs, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, int level) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<EntityDetail> getRelatedEntities(String userId, String startEntityGUID, List<String> entityTypeGUIDs, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail addEntity(String userId, String entityTypeGUID, InstanceProperties initialProperties, List<Classification> initialClassifications, InstanceStatus initialStatus) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public void addEntityProxy(String userId, EntityProxy entityProxy) throws InvalidParameterException, RepositoryErrorException, FunctionNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException
            {

            }

            @Override
            public EntityDetail updateEntityStatus(String userId, String entityGUID, InstanceStatus newStatus) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public EntityDetail updateEntityProperties(String userId, String entityGUID, InstanceProperties properties) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public EntityDetail undoEntityUpdate(String userId, String entityGUID) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail deleteEntity(String userId, String typeDefGUID, String typeDefName, String obsoleteEntityGUID) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public void purgeEntity(String userId, String typeDefGUID, String typeDefName, String deletedEntityGUID) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityNotDeletedException, UserNotAuthorizedException, FunctionNotSupportedException
            {

            }

            @Override
            public EntityDetail restoreEntity(String userId, String deletedEntityGUID) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityNotDeletedException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail classifyEntity(String userId, String entityGUID, String classificationName, InstanceProperties classificationProperties) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, PropertyErrorException, UserNotAuthorizedException, FunctionNotSupportedException

            {
                return null;
            }

            @Override
            public EntityDetail declassifyEntity(String userId, String entityGUID, String classificationName) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public EntityDetail updateEntityClassification(String userId, String entityGUID, String classificationName, InstanceProperties properties) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, PropertyErrorException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public Relationship addRelationship(String userId, String relationshipTypeGUID, InstanceProperties initialProperties, String entityOneGUID, String entityTwoGUID, InstanceStatus initialStatus) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, EntityNotKnownException, StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public Relationship updateRelationshipStatus(String userId, String relationshipGUID, InstanceStatus newStatus) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public Relationship updateRelationshipProperties(String userId, String relationshipGUID, InstanceProperties properties) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, PropertyErrorException, UserNotAuthorizedException, FunctionNotSupportedException
            {
                return null;
            }

            @Override
            public Relationship undoRelationshipUpdate(String userId, String relationshipGUID) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship deleteRelationship(String userId, String typeDefGUID, String typeDefName, String obsoleteRelationshipGUID) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public void purgeRelationship(String userId, String typeDefGUID, String typeDefName, String deletedRelationshipGUID) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, RelationshipNotDeletedException, UserNotAuthorizedException, FunctionNotSupportedException
            {

            }

            @Override
            public Relationship restoreRelationship(String userId, String deletedRelationshipGUID) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, RelationshipNotDeletedException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail reIdentifyEntity(String userId, String typeDefGUID, String typeDefName, String entityGUID, String newEntityGUID) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail reTypeEntity(String userId, String entityGUID, TypeDefSummary currentTypeDefSummary, TypeDefSummary newTypeDefSummary) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, ClassificationErrorException, EntityNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail reHomeEntity(String userId, String entityGUID, String typeDefGUID, String typeDefName, String homeMetadataCollectionId, String newHomeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship reIdentifyRelationship(String userId, String typeDefGUID, String typeDefName, String relationshipGUID, String newRelationshipGUID) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship reTypeRelationship(String userId, String relationshipGUID, TypeDefSummary currentTypeDefSummary, TypeDefSummary newTypeDefSummary) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, RelationshipNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship reHomeRelationship(String userId, String relationshipGUID, String typeDefGUID, String typeDefName, String homeMetadataCollectionId, String newHomeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public void saveEntityReferenceCopy(String userId, EntityDetail entity) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, HomeEntityException, EntityConflictException, InvalidEntityException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void purgeEntityReferenceCopy(String userId, String entityGUID, String typeDefGUID, String typeDefName, String homeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, HomeEntityException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void refreshEntityReferenceCopy(String userId, String entityGUID, String typeDefGUID, String typeDefName, String homeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, HomeEntityException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void saveRelationshipReferenceCopy(String userId, Relationship relationship) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, EntityNotKnownException, PropertyErrorException, HomeRelationshipException, RelationshipConflictException, InvalidRelationshipException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void purgeRelationshipReferenceCopy(String userId, String relationshipGUID, String typeDefGUID, String typeDefName, String homeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, HomeRelationshipException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public void refreshRelationshipReferenceCopy(String userId, String relationshipGUID, String typeDefGUID, String typeDefName, String homeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, HomeRelationshipException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }
        };
    }

}
