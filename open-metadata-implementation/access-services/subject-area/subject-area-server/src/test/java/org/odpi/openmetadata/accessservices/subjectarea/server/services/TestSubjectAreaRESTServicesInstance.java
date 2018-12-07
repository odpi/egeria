/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;



import org.odpi.openmetadata.accessservices.subjectarea.responses.PossibleClassificationsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.PossibleRelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestSubjectAreaRESTServicesInstance
{
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
    public void testgetPossibleClassificationsforTerm() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testEntityDefname1 = "GlossaryTerm";
        String testEntityDefName2 = "testEntityName2";
        String testClassificationDefname1 = "testClassificationName1";
        String testClassificationDefName2 = "testClassificationName2";

        String entityGuid ="test-entity-guid1";
        SubjectAreaRESTServicesInstance subjectAreaOmasREST = new SubjectAreaRESTServicesInstance();
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


//        SubjectAreaOMASAPIResponse response = subjectAreaOmasREST.getPossibleClassificationsForTerm(testuserid,entityGuid);
//
//        assertTrue(response.getResponseCategory().equals(ResponseCategory.PossibleClassifications));
//        PossibleClassificationsResponse possibleClassificationsResponse =(PossibleClassificationsResponse)response;
//        Set<String> returnedClassifications= possibleClassificationsResponse.getPossibleClassifications();
//        assertEquals(returnedClassifications.size(),2);
//        assertTrue(returnedClassifications.contains(testClassificationDefname1));
//        assertTrue(returnedClassifications.contains(testClassificationDefName2));
    }
    @Test
    public void testgetPossibleRelationshipsForTerm() throws Exception {
        OMRSMetadataCollection  mockMetadataCollection = getMockOmrsMetadataCollection();
        when(repositoryConnector.getMetadataCollection()).thenReturn(mockMetadataCollection);
        when(repositoryConnector.getServerName()).thenReturn(TEST_SERVER_NAME);
        // initialise the map
        new SubjectAreaServicesInstance(repositoryConnector);
        String testuserid = "userid1";
        String testEntityDefname1 = "GlossaryTerm";
        String testEntityDefName2 = "testEntityName2";
        String testEntityDefName3 = "testEntityName3";
        String testRelationshipDefname1 = "testRelationshipName1";
        String testRelationshipDefName2 = "testRelationshipName2";

        String entityGuid ="test-entity-guid1";
        SubjectAreaRESTServicesInstance subjectAreaOmasREST = new SubjectAreaRESTServicesInstance();
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

        SubjectAreaOMASAPIResponse response = subjectAreaOmasREST.getPossibleRelationshipsForTerm(TEST_SERVER_NAME,testuserid,entityGuid);

        assertTrue(response.getResponseCategory().equals(ResponseCategory.PossibleRelationships));
        PossibleRelationshipsResponse possibleRelationshipResponse =(PossibleRelationshipsResponse)response;
        Set<String> returnedRelationships= possibleRelationshipResponse.getPossibleRelationships();


        assertEquals(returnedRelationships.size(),2);
        assertTrue(returnedRelationships.contains(testRelationshipDefname1));
        assertTrue(returnedRelationships.contains(testRelationshipDefName2));
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
