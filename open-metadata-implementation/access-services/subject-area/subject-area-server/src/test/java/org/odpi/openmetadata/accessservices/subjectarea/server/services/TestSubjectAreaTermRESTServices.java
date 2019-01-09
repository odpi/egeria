/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.InvalidParameterExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.TermResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class TestSubjectAreaTermRESTServices {
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
    public void testCreateTermWithGlossaryName() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testtermguid = "term-guid-1";
        String testglossaryguid = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        String abbreviation = "test abbrev";
        String examples = "test examples";
        // set up the mocks
        EntityDetail mockGlossaryTermAdd = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);
        EntityDetail mockGlossaryTermGet = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);
        EntityDetail mockGlossary= createMockGlossary(displayName);
        TermAnchor termAnchor = new TermAnchor();
        termAnchor.setEntity1Guid(testglossaryguid);
        termAnchor.setEntity2Guid(testglossaryguid);
        Relationship mockRelationship =   TermAnchorMapper.mapTermAnchorToOmrsRelationship(termAnchor);

        // mock out the get the glossary and term gets
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(
                any(),
                any()
        )).thenReturn(mockGlossary,mockGlossaryTermGet);
        // mock out the add entity
        when( oMRSAPIHelper.callOMRSAddEntity(anyString(),any())).thenReturn(mockGlossaryTermAdd);
        // mock out the creation of the relationship to the glossary
        when (oMRSAPIHelper.callOMRSAddRelationship(anyString(),any())).thenReturn(mockRelationship);
        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        GlossarySummary glossary = new GlossarySummary();
        glossary.setGuid(testglossaryguid);
        requestedTerm.setGlossary(glossary);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);

        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));

        Term returnedTerm  = ((TermResponse)response).getTerm();

        assertEquals(requestedTerm.getName(),returnedTerm.getName());

        assertEquals(requestedTerm.getSummary(),returnedTerm.getSummary());

        assertEquals(requestedTerm.getDescription(),returnedTerm.getDescription());
    }
    @Test
    public void testCreateTermWithGlossaryGuid() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testtermguid = "term-guid-1";
        String testglossaryguid = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        String abbreviation = "test abbrev";
        String examples = "test examples";
        // set up the mocks
        EntityDetail mockEntityAdd = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);
        EntityDetail mockEntityGetTerm = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);
        EntityDetail mockEntityGetGlossary = createMockGlossary("GlossaryName1");
        List<EntityDetail> mockGlossaryList = new ArrayList<>();
        EntityDetail mockGlossary= createMockGlossary(displayName);
        mockGlossaryList.add(mockGlossary);

        TermAnchor termAnchor = new TermAnchor();
        termAnchor.setEntity1Guid(testglossaryguid);
        termAnchor.setEntity2Guid(testglossaryguid);
        Relationship mockRelationship =   TermAnchorMapper.mapTermAnchorToOmrsRelationship(termAnchor);

        // mock out the get the glossary by name
        when( oMRSAPIHelper.callFindEntitiesByProperty(
                any(),
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                any(),
                anyInt()

        )).thenReturn(mockGlossaryList);
        // mock out the add entity
        when( oMRSAPIHelper.callOMRSAddEntity(anyString(),any())).thenReturn(mockEntityAdd);
        // mock out the creation of the relationship to the glossary
        when (oMRSAPIHelper.callOMRSAddRelationship(anyString(),any())).thenReturn(mockRelationship);
        // mock out the get entities
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(testuserid,testtermguid)).thenReturn(mockEntityGetTerm);
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(testuserid,testglossaryguid)).thenReturn(mockEntityGetGlossary);

        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        GlossarySummary glossary = new GlossarySummary();
        glossary.setGuid(testglossaryguid);
        requestedTerm.setGlossary(glossary);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);

        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));

        Term returnedTerm  = ((TermResponse)response).getTerm();

        assertEquals(requestedTerm.getName(),returnedTerm.getName());

        assertEquals(requestedTerm.getSummary(),returnedTerm.getSummary());

        assertEquals(requestedTerm.getDescription(),returnedTerm.getDescription());
    }

    @Test
    public void testCreateTermNoGlossary() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testtermguid = "term-guid-1";
        String testglossaryguid = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        String displayName = null;
        String summary = "string1";
        String description = "string2 fsdgsdg";


        Term requestedTerm = new Term();
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);

        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-016"));

        // check with an empty string
        requestedTerm.setName("");
        response =  subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-016"));
    }

    @Test
    public void testCreateTermWithEmptyGlossary() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testtermguid = "term-guid-1";
        String testglossaryguid = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        String displayName = "test";
        String summary = "string1";
        String description = "string2 fsdgsdg";


        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        // set empty glossary
        GlossarySummary glossary = new GlossarySummary();
        requestedTerm.setGlossary(glossary);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);

        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-015"));
        assertTrue(invalidParameterExceptionResponse.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        // check with an empty glossary
        requestedTerm.setGlossary(glossary);
        response =  subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-015"));


    }

    @Test
    public void testCreateTermNonExistantIdedGlossary() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testtermguid = "term-guid-1";
        String testglossaryguid = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        // set up the mocks
        List<EntityDetail> mockGlossaryList = new ArrayList<>();

        // mock out the get the glossary by name
        when( oMRSAPIHelper.callFindEntitiesByProperty(
                any(),
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any(),
                any(),
                any(),
                any(),
                anyInt()

        )).thenReturn(mockGlossaryList);

        when( oMRSAPIHelper.callOMRSGetEntityByGuid(
                any(),
                any()

        )).thenThrow(new UnrecognizedGUIDException(
                1,"a","b","c","d","e","f"
        ));

        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        requestedTerm.setDescription(description);
        GlossarySummary glossary = new GlossarySummary();
        glossary.setGuid(testglossaryguid);
        requestedTerm.setGlossary(glossary);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-027"));

    }
    //TODO
//
//    @Test
//    public void testCreateTermWithInvalidRelationships() throws Exception {
//        String testuserid = "userid1";
//        String testtermguid = "term-guid-1";
//        String testglossaryguid = "glossary-guid-1";
//        final String testGlossaryName = "TestGlossary";
//        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
//
//        String displayName = "string0";
//        String summary = "string1";
//        String description = "string2 fsdgsdg";
//
//        Term requestedTerm = new Term();
//        requestedTerm.setName(displayName);
//        requestedTerm.setSummary(summary);
//        requestedTerm.setDescription(description);
//        GlossarySummary glossary = new GlossarySummary();
//        glossary.setGuid(testglossaryguid);
//        requestedTerm.setGlossary(glossary);
//        Set<String> categories = new HashSet<>();
//        categories.add("cat1");
//        requestedTerm.setCategories(categories);
//        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);
//        assertNotNull(response);
//        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
//        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
//        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-019"));
//
//        categories = new HashSet<>();
//        requestedTerm.setCategories(categories);
//        Set<String> projects = new HashSet<>();
//        projects.add("projects");
//        requestedTerm.setProjects(projects);
//        response =  subjectAreaTermOmasREST.createTerm(TEST_SERVER_NAME, testuserid, requestedTerm);
//        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
//        invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
//        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-026"));
//    }
//    @Test
//    public void testUpdateTermName() throws Exception {
//        String testuserid = "userid1";
//        String testtermguid = "term-guid-1";
//        String testglossaryguid = "glossary-guid-1";
//        final String testGlossaryName = "TestGlossary";
//        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
//
//        String displayName = "string0";
//        String summary = "string1";
//        String description = "string2 fsdgsdg";
//        final String newName = "newName";
//        String abbreviation = "test abbrev";
//        String examples = "test examples";
//        // set up the mocks
//        EntityDetail mockEntityUpdate = createMockGlossaryTerm(newName,summary,description,testtermguid, abbreviation, examples);
//        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);
//        List<EntityDetail> mockGlossaryList = new ArrayList<>();
//        EntityDetail mockGlossary= createMockGlossary(displayName);
//        mockGlossaryList.add(mockGlossary);
//
//            // mock out the add entity
//        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),any())).thenReturn(mockEntityUpdate);
//             // mock out the get entity
//        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
//
//        // set the mock omrs in to the rest file.
//        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
//
//
//        SubjectAreaOMASAPIResponse response =subjectAreaTermOmasREST.updateTermName(TEST_SERVER_NAME, testuserid,testtermguid, newName);
//        assertNotNull(response);
//        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));
//        Term returnedTerm  = ((TermResponse)response).getTerm();
//        assertEquals(newName,returnedTerm.getName());
//    }

//    @Test
//    public void testUpdateTermDescription() throws Exception {
//        String testuserid = "userid1";
//        String testtermguid = "term-guid-1";
//        String testglossaryguid = "glossary-guid-1";
//        final String testGlossaryName = "TestGlossary";
//        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
//
//        String displayName = "string0";
//        String summary = "string1";
//        String description = "string2 fsdgsdg";
//        final String newDescription = "new Description";
//        String abbreviation = "test abbrev";
//        String examples = "test examples";
//        // set up the mocks
//        EntityDetail mockEntityUpdate = createMockGlossaryTerm(displayName,summary,newDescription,testtermguid, abbreviation, examples);
//        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);
//        List<EntityDetail> mockGlossaryList = new ArrayList<>();
//        EntityDetail mockGlossary= createMockGlossary(displayName);
//        mockGlossaryList.add(mockGlossary);
//
//        // mock out the add entity
//        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),any())).thenReturn(mockEntityUpdate);
//        // mock out the get entity
//        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);
//
//        // set the mock omrs in to the rest file.
//        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
//
//        SubjectAreaOMASAPIResponse response =subjectAreaTermOmasREST.updateTermDescription(TEST_SERVER_NAME, testuserid,testtermguid, newDescription);
//        assertNotNull(response);
//        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));
//        Term returnedTerm  = ((TermResponse)response).getTerm();
//        assertEquals(newDescription,returnedTerm.getDescription());
//
//    }

    @Test
    public void testDeleteTerm() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testtermguid = "term-guid-1";

        // mock out the get entity
        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        String abbreviation = "test abbrev";
        String examples = "test examples";
        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);

        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);

        // the deletion method returns void so we need to mock it out differently.
        when( oMRSAPIHelper.callOMRSDeleteEntity(
                anyString(),
                anyString(),
                anyString(),
                anyString()

        )).thenReturn(mockEntityGet);

        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.deleteTerm(TEST_SERVER_NAME, testuserid, testtermguid,false);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));
    }

    @Test
    public void testGetTerm() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testtermguid = "testcreateTerm";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";

        String abbreviation = "test abbrev";
        String examples = "test examples";
        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testtermguid, abbreviation, examples);
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);

        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.getTermByGuid(TEST_SERVER_NAME, testuserid, testtermguid);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));
        Term returnedTerm  = ((TermResponse)response).getTerm();

        assertEquals(displayName,returnedTerm.getName());

        assertEquals(summary,returnedTerm.getSummary());

        assertEquals(description,returnedTerm.getDescription());
    }
    private EntityDetail createMockGlossary(String displayName) {
        EntityDetail mockGlossary= new EntityDetail();
        InstanceType typeOfEntity = new InstanceType();
        typeOfEntity.setTypeDefName("Glossary");
        mockGlossary.setType(typeOfEntity);
        InstanceProperties instanceProperties = new InstanceProperties();
        PrimitivePropertyValue primitivePropertyValue;
        ArrayPropertyValue arrayPropertyValue;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(displayName);
        instanceProperties.setProperty("displayName", primitivePropertyValue);

        // In the models we are generating from we only have map<String,String> types, this code assumes those types.
        mockGlossary.setProperties(instanceProperties);
        return mockGlossary;
    }
    private static EntityDetail createMockGlossaryTerm(String displayName, String summary, String description, String testtermguid, String abbreviation, String examples) {
        EntityDetail mockEntity = new EntityDetail();
        InstanceProperties instanceProperties = new InstanceProperties();

        SubjectAreaUtils.addStringToInstanceProperty("displayName",displayName ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("summary",summary ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("description",description ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("examples",description ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("abbreviatiop",description ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("usage",description ,instanceProperties);

        InstanceStatus status = InstanceStatus.ACTIVE;
        mockEntity.setStatus(status);

        // In the models we are generating from we only have map<String,String> types, this code assumes those types.
        mockEntity.setProperties(instanceProperties);
        mockEntity.setGUID(testtermguid);
        mockEntity.setVersion(1L);
        InstanceType typeOfEntity = new InstanceType();
        typeOfEntity.setTypeDefName("GlossaryTerm");
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
            public List<EntityDetail> findEntitiesByProperty(String userId, String entityTypeGUID, InstanceProperties matchProperties, MatchCriteria matchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public List<EntityDetail> findEntitiesByClassification(String userId, String entityTypeGUID, String classificationName, InstanceProperties matchClassificationProperties, MatchCriteria matchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, ClassificationErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
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
            public List<Relationship> findRelationshipsByProperty(String userId, String relationshipTypeGUID, InstanceProperties matchProperties, MatchCriteria matchCriteria, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException
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
            public EntityDetail addEntity(String userId, String entityTypeGUID, InstanceProperties initialProperties, List<Classification> initialClassifications, InstanceStatus initialStatus) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public void addEntityProxy(String userId, EntityProxy entityProxy) throws InvalidParameterException, RepositoryErrorException, FunctionNotSupportedException, UserNotAuthorizedException
            {

            }

            @Override
            public EntityDetail updateEntityStatus(String userId, String entityGUID, InstanceStatus newStatus) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, StatusNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail updateEntityProperties(String userId, String entityGUID, InstanceProperties properties) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, UserNotAuthorizedException
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
            public void purgeEntity(String userId, String typeDefGUID, String typeDefName, String deletedEntityGUID) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityNotDeletedException, UserNotAuthorizedException
            {

            }

            @Override
            public EntityDetail restoreEntity(String userId, String deletedEntityGUID) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityNotDeletedException, FunctionNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail classifyEntity(String userId, String entityGUID, String classificationName, InstanceProperties classificationProperties) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, PropertyErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail declassifyEntity(String userId, String entityGUID, String classificationName) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public EntityDetail updateEntityClassification(String userId, String entityGUID, String classificationName, InstanceProperties properties) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, PropertyErrorException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship addRelationship(String userId, String relationshipTypeGUID, InstanceProperties initialProperties, String entityOneGUID, String entityTwoGUID, InstanceStatus initialStatus) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, EntityNotKnownException, StatusNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship updateRelationshipStatus(String userId, String relationshipGUID, InstanceStatus newStatus) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, StatusNotSupportedException, UserNotAuthorizedException
            {
                return null;
            }

            @Override
            public Relationship updateRelationshipProperties(String userId, String relationshipGUID, InstanceProperties properties) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, PropertyErrorException, UserNotAuthorizedException
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
            public void purgeRelationship(String userId, String typeDefGUID, String typeDefName, String deletedRelationshipGUID) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, RelationshipNotDeletedException, UserNotAuthorizedException
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
