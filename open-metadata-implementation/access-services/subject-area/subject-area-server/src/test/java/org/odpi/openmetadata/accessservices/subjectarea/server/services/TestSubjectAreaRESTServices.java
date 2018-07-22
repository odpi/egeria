/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;



import org.odpi.openmetadata.accessservices.subjectarea.responses.PossibleClassificationsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.PossibleRelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestSubjectAreaRESTServices {
    @Mock
    private OMRSAPIHelper oMRSAPIHelper;

    @BeforeMethod
    public void setup() throws UserNotAuthorizedException, EntityNotKnownException, ConnectorCheckedException, InvalidParameterException, RepositoryErrorException, ConnectionCheckedException {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testgetPossibleClassificationsforTerm() throws Exception {
        String testuserid = "userid1";
        String testEntityDefname1 = "GlossaryTerm";
        String testEntityDefName2 = "testEntityName2";
        String testClassificationDefname1 = "testClassificationName1";
        String testClassificationDefName2 = "testClassificationName2";

        String entityGuid ="test-entity-guid1";
        SubjectAreaRESTServices subjectAreaOmasREST = new SubjectAreaRESTServices();
        TypeDefGallery mockTypeDefGallery = new TypeDefGallery();
        List<TypeDef> alltypeDefs = new ArrayList<>();

        EntityDef entityDef1 = new EntityDef();
        entityDef1.setName(testEntityDefname1);
        EntityDef entityDef2 = new EntityDef();
        entityDef2.setName(testEntityDefName2);
        ClassificationDef classificationDef1 = new ClassificationDef();
        classificationDef1.setName(testClassificationDefname1);
        ClassificationDef classificationDef2 = new ClassificationDef();
        classificationDef2.setName(testClassificationDefName2);
        classificationDef2.setName(testClassificationDefName2);
        List<TypeDefLink> classificationDef1Valids = new ArrayList<>();
        classificationDef1Valids.add(entityDef1);
        classificationDef1Valids.add(entityDef2);
        classificationDef1.setValidEntityDefs(classificationDef1Valids);

        List<TypeDefLink> classificationDef2Valids = new ArrayList<>();
        classificationDef2Valids.add(entityDef1);
        classificationDef2Valids.add(entityDef2);
        classificationDef2.setValidEntityDefs(classificationDef1Valids);

        alltypeDefs.add(entityDef1);
        alltypeDefs.add(entityDef2);
        alltypeDefs.add(classificationDef1);
        alltypeDefs.add(classificationDef2);

        mockTypeDefGallery.setTypeDefs(alltypeDefs);
        EntityDetail entity1 = new EntityDetail();
        InstanceType template = SubjectAreaUtils.createTemplateFromTypeDef(entityDef1);
        entity1.setType(template);
        when(oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyString())).thenReturn(entity1);
        when(oMRSAPIHelper.callGetTypeDefByName(anyString(),anyString())).thenReturn(entityDef1);
        when(oMRSAPIHelper.callGetAllTypes(anyString())).thenReturn(mockTypeDefGallery);

        // set the mock omrs in to the rest file.
        subjectAreaOmasREST.setOMRSAPIHelper(oMRSAPIHelper);


        SubjectAreaOMASAPIResponse response = subjectAreaOmasREST.getPossibleClassificationsForTerm(testuserid,entityGuid);

        assertTrue(response.getResponseCategory().equals(ResponseCategory.PossibleClassifications));
        PossibleClassificationsResponse possibleClassificationsResponse =(PossibleClassificationsResponse)response;
        Set<String> returnedClassifications= possibleClassificationsResponse.getPossibleClassifications();
        assertEquals(returnedClassifications.size(),2);
        assertTrue(returnedClassifications.contains(testClassificationDefname1));
        assertTrue(returnedClassifications.contains(testClassificationDefName2));
    }
    @Test
    public void testgetPossibleRelationshipsForTerm() throws Exception {
        String testuserid = "userid1";
        String testEntityDefname1 = "GlossaryTerm";
        String testEntityDefName2 = "testEntityName2";
        String testEntityDefName3 = "testEntityName3";
        String testRelationshipDefname1 = "testRelationshipName1";
        String testRelationshipDefName2 = "testRelationshipName2";

        String entityGuid ="test-entity-guid1";
        SubjectAreaRESTServices subjectAreaOmasREST = new SubjectAreaRESTServices();
        TypeDefGallery mockTypeDefGallery = new TypeDefGallery();
        List<TypeDef> alltypeDefs = new ArrayList<>();

        EntityDef entityDef1 = new EntityDef();
        entityDef1.setName(testEntityDefname1);
        EntityDef entityDef2 = new EntityDef();
        entityDef2.setName(testEntityDefName2);
        EntityDef entityDef3 = new EntityDef();
        entityDef3.setName(testEntityDefName3);

        RelationshipDef relationshipDef1 = new RelationshipDef();
        relationshipDef1.setName(testRelationshipDefname1);
        RelationshipEndDef end1_1 = new RelationshipEndDef();
        end1_1.setEntityType(entityDef1);
        relationshipDef1.setEndDef1(end1_1);
        RelationshipEndDef end2_1 = new RelationshipEndDef();
        end2_1.setEntityType(entityDef2);
        relationshipDef1.setEndDef2(end2_1);

        RelationshipDef relationshipDef2 = new RelationshipDef();
        relationshipDef2.setName(testRelationshipDefName2);

        RelationshipEndDef end1_2 = new RelationshipEndDef();
        end1_2.setEntityType(entityDef1);
        relationshipDef2.setEndDef1(end1_1);
        RelationshipEndDef end2_2 = new RelationshipEndDef();
        end2_2.setEntityType(entityDef3);
        relationshipDef2.setEndDef2(end2_2);

        alltypeDefs.add(entityDef1);
        alltypeDefs.add(entityDef2);
        alltypeDefs.add(entityDef3);
        alltypeDefs.add(relationshipDef1);
        alltypeDefs.add(relationshipDef2);

        mockTypeDefGallery.setTypeDefs(alltypeDefs);
        EntityDetail entity1 = new EntityDetail();
        InstanceType template = SubjectAreaUtils.createTemplateFromTypeDef(entityDef1);
        entity1.setType(template);
        when(oMRSAPIHelper.callOMRSGetEntityByGuid(anyString(),anyString())).thenReturn(entity1);
        when(oMRSAPIHelper.callGetTypeDefByName(anyString(),anyString())).thenReturn(entityDef1);
        when( oMRSAPIHelper.callGetAllTypes(anyString())).thenReturn(mockTypeDefGallery);

        // set the mock omrs in to the rest file.
        subjectAreaOmasREST.setOMRSAPIHelper(oMRSAPIHelper);


//        Set<String> returnedRelationships = subjectAreaOmasREST.getPossibleRelationshipsForTerm(testuserid,entityGuid);

        SubjectAreaOMASAPIResponse response = subjectAreaOmasREST.getPossibleRelationshipsForTerm(testuserid,entityGuid);

        assertTrue(response.getResponseCategory().equals(ResponseCategory.PossibleRelationships));
        PossibleRelationshipsResponse possibleRelationshipResponse =(PossibleRelationshipsResponse)response;
        Set<String> returnedRelationships= possibleRelationshipResponse.getPossibleRelationships();


        assertEquals(returnedRelationships.size(),2);
        assertTrue(returnedRelationships.contains(testRelationshipDefname1));
        assertTrue(returnedRelationships.contains(testRelationshipDefName2));
    }
}
