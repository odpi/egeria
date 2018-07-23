/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class TestSubjectAreaGlossaryRESTServices {
    @Mock
    private OMRSAPIHelper oMRSAPIHelper;

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
        int u=0;

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
        // set up the mocks
        EntityUniverse mockEntityAdd = createMockGlossary(displayName,usage,description,testguid1);
        EntityUniverse mockEntityGet = createMockGlossary(displayName,usage,description,testguid1);

        mockEntityAdd.setGUID(testguid1);
        mockEntityGet.setGUID(testguid1);

        // mock out the get the glossary by name when it looks for a glossary of our name reqturn an empty list .
        List<EntityDetail> mockGlossaryList = new ArrayList<>();
        when( oMRSAPIHelper.callFindEntitiesByProperty(
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt()

        )).thenReturn(mockGlossaryList);
        // mock out the add entity
        when( oMRSAPIHelper.callOMRSAddEntity(anyString(),anyObject())).thenReturn(mockEntityAdd);
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyObject())).thenReturn(mockEntityGet);

        // set the mock omrs in to the rest file.
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        Glossary requestedGlossary = new Glossary();
        requestedGlossary.setName(displayName);
        requestedGlossary.setUsage(usage);
        requestedGlossary.setDescription(description);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.createGlossary(testuserid, requestedGlossary);
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

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.createGlossary(testuserid, requestedGlossary);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidExResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidExResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-028"));

        // check with an empty string
        requestedGlossary.setName("");

        response = subjectAreaGlossaryOmasREST.createGlossary(testuserid, requestedGlossary);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        invalidExResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidExResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-028"));
    }

    @Test
    public void testCreateGlossaryAlreadyExists() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";

        final String testGlossaryName = "TestGlossary";
        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        // set up the mocks
        EntityUniverse mockEntityGet = createMockGlossary(displayName,usage,description,testguid1);
        List<EntityDetail> mockGlossaryList = new ArrayList<>();
        mockGlossaryList.add(mockEntityGet);


        // mock out the get the glossary by name
        when( oMRSAPIHelper.callFindEntitiesByProperty(
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt()

        )).thenReturn(mockGlossaryList);

        // set the mock omrs in to the rest file.
        Glossary requestedGlossary = new Glossary();
        requestedGlossary.setName(displayName);
        requestedGlossary.setUsage(usage);
        requestedGlossary.setDescription(description);


        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.createGlossary(testuserid, requestedGlossary);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidExResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidExResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-032"));

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
        // set up the mock

        EntityUniverse mockEntityGet = createMockGlossary(displayName,usage,description,testguid1);

        mockEntityGet.setGUID(testguid1);

        when( oMRSAPIHelper.callOMRSGetEntityByGuid(
                anyObject(),
                anyObject()
        )).thenReturn(mockEntityGet);

        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyObject())).thenReturn(mockEntityGet);

        // set the mock omrs in to the rest file.
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.getGlossaryByGuid(testuserid, testguid1 );
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Glossary));
        Glossary returnedGlossary = ((GlossaryResponse)response).getGlossary();
        assertTrue(returnedGlossary.getName().equals(displayName));
        assertTrue(returnedGlossary.getUsage().equals(usage));
        assertTrue(returnedGlossary.getDescription().equals(description));
    }


    @Test
    public void testUpdateGlossaryName() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";
        final String testGlossaryName = "TestGlossary";
        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        // set the mock omrs in to the rest file.
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        final String newName = "newName";
        // set up the mocks
        EntityUniverse mockEntityUpdate = createMockGlossary(newName,usage,description,testguid1);
        EntityUniverse mockEntityGet = createMockGlossary(displayName,usage,description,testguid1);
        List<EntityDetail> mockGlossaryList = new ArrayList<>();

        // mock out the update entity
        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),anyObject())).thenReturn(mockEntityUpdate);
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyObject())).thenReturn(mockEntityGet);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.updateGlossaryName(testuserid,testguid1, newName);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Glossary));
        Glossary returnedGlossary = ((GlossaryResponse)response).getGlossary();

        assertEquals(newName,returnedGlossary.getName());
    }
    @Test
    public void testUpdateGlossaryDescription() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";

        final String testGlossaryName = "TestGlossary";
        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        // set the mock omrs in to the rest file.
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";
        final String newDescription = "new description1";
        // set up the mocks
        EntityUniverse mockEntityUpdate = createMockGlossary(displayName,usage,newDescription ,testguid1);
        EntityUniverse mockEntityGet = createMockGlossary(displayName,usage,newDescription ,testguid1);
        List<EntityDetail> mockGlossaryList = new ArrayList<>();

        // mock out the update entity
        when( oMRSAPIHelper.callOMRSUpdateEntity(anyString(),anyObject())).thenReturn(mockEntityUpdate);
        // mock out the get entity
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyObject())).thenReturn(mockEntityGet);
        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.updateGlossaryDescription(testuserid,testguid1, newDescription);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.Glossary));
        Glossary returnedGlossary = ((GlossaryResponse)response).getGlossary();
    }

    @Test
    public void testDeleteGlossary() throws Exception {
        String testuserid = "userid1";
        String testguid1 = "glossary-guid-1";

        // mock out the get entity
        String displayName = "string0";
        String usage = "string1";
        String description = "string2 fsdgsdg";

        EntityUniverse mockEntityGet = createMockGlossary(displayName,usage,description,testguid1);
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyObject())).thenReturn(mockEntityGet);
        List<Relationship> mockRelationshipList = new ArrayList<>();

        when( oMRSAPIHelper.callGetRelationshipsForEntity(
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt()

        )).thenReturn(mockRelationshipList);

        when( oMRSAPIHelper.callOMRSDeleteEntity(
                anyString(),
                anyString(),
                anyString(),
                anyString()

        )).thenReturn(mockEntityGet);

        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);
        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.deleteGlossary(testuserid, testguid1,false);
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

        EntityUniverse mockEntityGet = createMockGlossary(displayName,usage,description,testguid1);
        when( oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyObject())).thenReturn(mockEntityGet);
        List<Relationship> mockRelationshipList = new ArrayList<>();
        mockRelationshipList.add(new Relationship());
        Glossary mockDeletedGlossary = new Glossary();
        when( oMRSAPIHelper.callGetRelationshipsForEntity(
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyInt()

        )).thenReturn(mockRelationshipList);

        when( oMRSAPIHelper.callOMRSDeleteEntity(
                anyString(),
                anyString(),
                anyString(),
                anyString()

        )).thenReturn(mockEntityGet);

        SubjectAreaGlossaryRESTServices subjectAreaGlossaryOmasREST = new SubjectAreaGlossaryRESTServices();
        subjectAreaGlossaryOmasREST.setOMRSAPIHelper(oMRSAPIHelper);

        SubjectAreaOMASAPIResponse response = subjectAreaGlossaryOmasREST.deleteGlossary(testuserid, testguid1,false);
        assertTrue(response.getResponseCategory().equals(ResponseCategory.InvalidParameterException));
        InvalidParameterExceptionResponse invalidExResponse = (InvalidParameterExceptionResponse)response;
        assertTrue(invalidExResponse.getExceptionErrorMessage().contains("OMAS-SUBJECTAREA-400-033"));
    }

    private static EntityUniverse createMockGlossary(String displayName, String usage, String description, String testguid1) {
        // TODO          "qualifiedName","additionalProperties",

        EntityUniverse mockEntity = new EntityUniverse();
        InstanceProperties instanceProperties = new InstanceProperties();

        PrimitivePropertyValue primitivePropertyValue;

        SubjectAreaUtils.addStringToInstanceProperty("displayName",displayName ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("usage",usage ,instanceProperties);
        SubjectAreaUtils.addStringToInstanceProperty("description",description ,instanceProperties);


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
}
