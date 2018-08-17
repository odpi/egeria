/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchorMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.accessservices.subjectarea.responses.InvalidParameterExceptionResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.TermResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class TestSubjectAreaTermRESTServices {
    @Mock
    private OMRSAPIHelper oMRSAPIHelper;

    @BeforeMethod
    public void setup() throws UserNotAuthorizedException, EntityNotKnownException, ConnectorCheckedException, InvalidParameterException, RepositoryErrorException, ConnectionCheckedException {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testCreateTerm() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";
        String testguid2 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        String abbreviation = "test abbrev";
        String examples = "test examples";
        // set up the mocks
        EntityDetail mockEntityAdd = createMockGlossaryTerm(displayName,summary,description,testguid1, abbreviation, examples);
        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testguid1, abbreviation, examples);
        List<EntityDetail> mockGlossaryList = new ArrayList<>();
        EntityDetail mockGlossary= createMockGlossary(displayName);
        mockGlossaryList.add(mockGlossary);

        TermAnchor termAnchor = new TermAnchor();
        termAnchor.setEntity1Guid(testguid2);
        termAnchor.setEntity2Guid(testguid2);
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
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);

        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        requestedTerm.setGlossaryName(testGlossaryName);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);

        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));

        Term returnedTerm  = ((TermResponse)response).getTerm();

        assertEquals(requestedTerm.getName(),returnedTerm.getName());

        assertEquals(requestedTerm.getSummary(),returnedTerm.getSummary());

        assertEquals(requestedTerm.getDescription(),returnedTerm.getDescription());
    }

    @Test
    public void testCreateTermNoName() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";
        String testguid2 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = null;
        String summary = "string1";
        String description = "string2 fsdgsdg";


        Term requestedTerm = new Term();
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        requestedTerm.setGlossaryName(testGlossaryName);

        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-016"));

        // check with an empty string
        requestedTerm.setName("");
        response =  subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-016"));
    }

    @Test
    public void testCreateTermNoGlossaryName() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";
        String testguid2 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "test";
        String summary = "string1";
        String description = "string2 fsdgsdg";


        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        // do not set glossary name
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);

        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-015"));
        assertTrue(invalidParameterExceptionResponse.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        // check with an empty string
        requestedTerm.setGlossaryName("");
        response =  subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-015"));


    }
    @Test
    public void testCreateTermNonExistantglossary() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";
        String testguid2 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

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



        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        requestedTerm.setGlossaryName(testGlossaryName);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-025"));

    }

    @Test
    public void testCreateTermWithInvalidRelationships() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";
        String testguid2 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";

        Term requestedTerm = new Term();
        requestedTerm.setName(displayName);
        requestedTerm.setSummary(summary);
        requestedTerm.setDescription(description);
        requestedTerm.setGlossaryName(testGlossaryName);
        Set<String> categories = new HashSet<>();
        categories.add("cat1");
        requestedTerm.setCategories(categories);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-019"));

        categories = new HashSet<>();
        requestedTerm.setCategories(categories);
        Set<String> projects = new HashSet<>();
        projects.add("projects");
        requestedTerm.setProjects(projects);
        response =  subjectAreaTermOmasREST.createTerm(testuserid, requestedTerm);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        invalidParameterExceptionResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidParameterExceptionResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-026"));
    }
    @Test
    public void testUpdateTermName() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";
        String testguid2 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        final String newName = "newName";
        String abbreviation = "test abbrev";
        String examples = "test examples";
        // set up the mocks
        EntityDetail mockEntityUpdate = createMockGlossaryTerm(newName,summary,description,testguid1, abbreviation, examples);
        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testguid1, abbreviation, examples);
        List<EntityDetail> mockGlossaryList = new ArrayList<>();
        EntityDetail mockGlossary= createMockGlossary(displayName);
        mockGlossaryList.add(mockGlossary);

            // mock out the add entity
        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),any())).thenReturn(mockEntityUpdate);
             // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);

        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);


        SubjectAreaOMASAPIResponse response =subjectAreaTermOmasREST.updateTermName(testuserid,testguid1, newName);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));
        Term returnedTerm  = ((TermResponse)response).getTerm();
        assertEquals(newName,returnedTerm.getName());
    }

    @Test
    public void testUpdateTermDescription() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";
        String testguid2 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        final String newDescription = "new Description";
        String abbreviation = "test abbrev";
        String examples = "test examples";
        // set up the mocks
        EntityDetail mockEntityUpdate = createMockGlossaryTerm(displayName,summary,newDescription,testguid1, abbreviation, examples);
        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testguid1, abbreviation, examples);
        List<EntityDetail> mockGlossaryList = new ArrayList<>();
        EntityDetail mockGlossary= createMockGlossary(displayName);
        mockGlossaryList.add(mockGlossary);

        // mock out the add entity
        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),any())).thenReturn(mockEntityUpdate);
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);

        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        SubjectAreaOMASAPIResponse response =subjectAreaTermOmasREST.updateTermDescription(testuserid,testguid1, newDescription);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));
        Term returnedTerm  = ((TermResponse)response).getTerm();
        assertEquals(newDescription,returnedTerm.getDescription());

    }

    @Test
    public void testDeleteTerm() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "term-guid-1";

        // mock out the get entity
        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";
        String abbreviation = "test abbrev";
        String examples = "test examples";
        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testguid1, abbreviation, examples);

        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);

        // the deletion method returns void so we need to mock it out differently.
        when( oMRSAPIHelper.callOMRSDeleteEntity(
                anyString(),
                anyString(),
                anyString(),
                anyString()

        )).thenReturn(mockEntityGet);

        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.deleteTerm(testuserid, testguid1,false);
        assertNotNull(response);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Term));
    }

    @Test
    public void testGetTerm() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "testcreateTerm";
        SubjectAreaTermRESTServices subjectAreaTermOmasREST = new SubjectAreaTermRESTServices();

        String displayName = "string0";
        String summary = "string1";
        String description = "string2 fsdgsdg";

        String abbreviation = "test abbrev";
        String examples = "test examples";
        EntityDetail mockEntityGet = createMockGlossaryTerm(displayName,summary,description,testguid1, abbreviation, examples);
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),any())).thenReturn(mockEntityGet);

        // set the mock omrs in to the rest file.
        subjectAreaTermOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        SubjectAreaOMASAPIResponse response = subjectAreaTermOmasREST.getTermByGuid(testuserid, testguid1);
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
    private static EntityDetail createMockGlossaryTerm(String displayName, String summary, String description, String testguid1, String abbreviation, String examples) {
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
        mockEntity.setGUID(testguid1);
        mockEntity.setVersion(1L);
        InstanceType typeOfEntity = new InstanceType();
        typeOfEntity.setTypeDefName("GlossaryTerm");
        typeOfEntity.setTypeDefGUID("test type guid");
        mockEntity.setType(typeOfEntity);

        return mockEntity;
    }
}
