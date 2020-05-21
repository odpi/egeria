/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.contentmanager;


import org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OMEntityDaoForTests extends OMEntityDao {

    private static final Logger log = LoggerFactory.getLogger(OMEntityDaoForTests.class);

    public OMEntityDaoForTests(OMRSRepositoryConnector enterpriseConnector, List<String> supportedZones, OMRSAuditLog auditLog) {
    	super (enterpriseConnector, supportedZones, auditLog);
    }

    /**
     * Returns the newly created entity with the specified properties
     *
     * @param metadataCollectionId unique identifier for the metadata collection used for adding entities
     * @param userName             name of the user performing the add operation
     * @param typeName             of the entity type def
     * @param instanceProperties   specific to the entity
     * @param zoneRestricted
     * @return the new entity added to the metadata collection
     * @throws ClassificationErrorException
     * @throws StatusNotSupportedException
     * @throws UserNotAuthorizedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     */
    private EntityDetail addEntity(String metadataCollectionId,
                                   String userName,
                                   String typeName,
                                   InstanceProperties instanceProperties,
                                   List<Classification> classifications,
                                   boolean zoneRestricted) throws ClassificationErrorException, StatusNotSupportedException,
                                                                  UserNotAuthorizedException, InvalidParameterException,
                                                                  RepositoryErrorException, PropertyErrorException,
                                                                  TypeErrorException, FunctionNotSupportedException {
        EntityDetail entity;
            entity = enterpriseConnector.getRepositoryHelper().getSkeletonEntity(Constants.ANALYTICS_MODELING_OMAS_NAME,
                                                                                metadataCollectionId,
                                                                                InstanceProvenanceType.LOCAL_COHORT,
                                                                                userName,
                                                                                typeName);
            entity.setClassifications(classifications);
            if(zoneRestricted && supportedZones != null && !supportedZones.isEmpty()){
                instanceProperties = enterpriseConnector.getRepositoryHelper().addStringArrayPropertyToInstance(Constants.ANALYTICS_MODELING_OMAS_NAME, instanceProperties, Constants.ZONE_MEMBERSHIP, supportedZones, "addEntity");
            }
            
            return enterpriseConnector.getMetadataCollection().addEntity(userName,
                                                                        entity.getType().getTypeDefGUID(),
                                                                        instanceProperties,
                                                                        entity.getClassifications(),
                                                                        entity.getStatus());
    }
    
    public void deleteEntity(EntityDetail entity) throws AnalyticsModelingCheckedException {
    	InstanceType instanceType = entity.getType();
    	try {
			enterpriseConnector.getMetadataCollection().deleteEntity(Constants.ANALYTICS_MODELING_USER_ID,
					instanceType.getTypeDefGUID(), instanceType.getTypeDefName(), entity.getGUID());
		} catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException
				| FunctionNotSupportedException | UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.DELETE_ENTITY_EXCEPTION.getMessageDefinition("QName", getEntityQName(entity)),
					this.getClass().getSimpleName(),
					"deleteEntity",
					ex);
		}
    }

    /**
     * Returns the newly created relationship between 2 entities with the specified properties
     *
     * @param metadataCollectionId unique identifier for the metadata collection used for adding relationships
     * @param typeName             name of the relationship type def
     * @param instanceProperties    properties for the relationship
     * @param entityOneGUID        giud of the first end of the relationship
     * @param entityTwoGUID        giud of the second end of the relationship
     * @return the created relationship
     * @throws AnalyticsModelingCheckedException 
     */
    private Relationship addRelationship(String metadataCollectionId,
                                         String typeName,
                                         InstanceProperties instanceProperties,
                                         String entityOneGUID,
                                         String entityTwoGUID) throws AnalyticsModelingCheckedException {

        Relationship relationship = null;
        try {
            relationship = enterpriseConnector.getRepositoryHelper()
                    .getSkeletonRelationship(Constants.ANALYTICS_MODELING_OMAS_NAME,
                            metadataCollectionId,
                            InstanceProvenanceType.LOCAL_COHORT,
                            Constants.ANALYTICS_MODELING_USER_ID,
                            typeName);
            return enterpriseConnector.getMetadataCollection()
                    .addRelationship(Constants.ANALYTICS_MODELING_USER_ID,
                            relationship.getType().getTypeDefGUID(),
                            instanceProperties,
                            entityOneGUID,
                            entityTwoGUID,
                            InstanceStatus.ACTIVE);
        } catch (StatusNotSupportedException | UserNotAuthorizedException | EntityNotKnownException 
        		| InvalidParameterException | RepositoryErrorException | PropertyErrorException 
        		| TypeErrorException | FunctionNotSupportedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.ADD_RELATIONSHIP_EXCEPTION.getMessageDefinition(typeName),
					this.getClass().getSimpleName(),
					"addRelationship",
					ex);
        }
    }
    
    public void updateEntityProperty(EntityDetail entity, InstanceProperties newProperties) throws AnalyticsModelingCheckedException {
    	try {
			enterpriseConnector.getMetadataCollection().updateEntityProperties(Constants.ANALYTICS_MODELING_USER_ID,
					entity.getGUID(),
					newProperties);
		} catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException
				| PropertyErrorException | UserNotAuthorizedException | FunctionNotSupportedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.UPDATE_PROPERTY_EXCEPTION.getMessageDefinition(getEntityQName(entity), newProperties.toString()),
					this.getClass().getSimpleName(),
					"updateEntityProperty",
					ex);


		}
    }

    public void classifyEntity(EntityDetail entity, String classificationName, InstanceProperties classificationProperties) throws AnalyticsModelingCheckedException {
    	try {
			enterpriseConnector.getMetadataCollection().classifyEntity(Constants.ANALYTICS_MODELING_USER_ID,
					entity.getGUID(),
					classificationName,
					classificationProperties);
		} catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException
				| PropertyErrorException | UserNotAuthorizedException | FunctionNotSupportedException 
				| ClassificationErrorException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.CLASSIFICATION_EXCEPTION.getMessageDefinition(getEntityQName(entity), classificationName),
					this.getClass().getSimpleName(),
					"classifyEntity",
					ex);

		}
    }


	/**
     * Returns the entity of the specified type retrieved based on qualified name
     *
     * @param typeName      name of the type def for the entity to be retrieved
     * @param qualifiedName qualified name property of the entity to be retrieved
     * @param zoneRestricted
     * @return the existing entity with the given qualified name or null if it doesn't exist
	 * @throws AnalyticsModelingCheckedException 
     */
    public EntityDetail getEntity(String typeName, String qualifiedName, boolean zoneRestricted) throws AnalyticsModelingCheckedException {
        Map<String, String>  properties = new HashMap<>();
        // GDW - need to convert qualifiedName to exactMatchRegex
        String qualifiedNameRegex = enterpriseConnector.getRepositoryHelper().getExactMatchRegex(qualifiedName);
        properties.put(Constants.QUALIFIED_NAME, qualifiedNameRegex);
        InstanceProperties matchProperties = buildMatchingInstanceProperties(properties, zoneRestricted);
        List<EntityDetail> existingEntities;
        existingEntities = findEntities(matchProperties, typeName, Constants.START_FROM, Constants.PAGE_SIZE);
        return checkEntities(existingEntities, qualifiedName);
    }

	/**
     * Returns the entity filtered out from entities list based on qualified name
     *
     * @param existingEntities the list of entities to search in
     * @param qualifiedName    qualified name based on which the entity is retrieved
     * @return the entity that has the specified qualified name
     */
    private EntityDetail checkEntities(List<EntityDetail> existingEntities, String qualifiedName) {
        if (existingEntities == null || existingEntities.isEmpty()) {
        	return null;
        }
        return existingEntities.stream().filter(e -> qualifiedName.equals(enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.ANALYTICS_MODELING_OMAS_NAME, Constants.QUALIFIED_NAME, e.getProperties(), "checkEntities"))).findFirst().orElse(null);
    }

    /**
     * Returns the relationship of the given type between the 2 entities
     *
     * @param relationshipType is the name of the relationship type
     * @param guid1            is guid of first end of relationship
     * @param guid2            is guid of the second end f relationship
     * @return the relationship of the given type between the two entities; null if it doesn't exist
     * @throws AnalyticsModelingCheckedException 
     */
    private Relationship checkRelationshipExists(String relationshipType,
                                         String guid1,
                                         String guid2) throws AnalyticsModelingCheckedException {
        List<Relationship> relationships = getRelationships(relationshipType, guid2);
        if (relationships == null || relationships.isEmpty()) {
        	return null;
        }
        
        return relationships.stream().filter(relationship -> relationship.getType().getTypeDefName().equals(relationshipType)
                && checkRelationshipEnds(relationship, guid1, guid2)).findFirst().orElse(null);
    }

    public List<Relationship> getRelationships(String relationshipType, String guid) throws AnalyticsModelingCheckedException {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving relationships of type {} for entity {}", relationshipType, guid);
        }
        try {
            String relationshipTypeGuid = enterpriseConnector.getRepositoryHelper()
                                                            .getTypeDefByName(Constants.ANALYTICS_MODELING_USER_ID, relationshipType)
                                                            .getGUID();

            return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.ANALYTICS_MODELING_USER_ID,
                                                                                        guid,
                                                                                        relationshipTypeGuid,
                                                                                        Constants.START_FROM,
                                                                                        Collections.singletonList(InstanceStatus.ACTIVE),
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        Constants.PAGE_SIZE);
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityNotKnownException | FunctionNotSupportedException | InvalidParameterException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            throw buildRetrieveRelationshipException(guid, relationshipType, e, this.getClass().getName());
        }
    }

    private AnalyticsModelingCheckedException buildRetrieveRelationshipException(String guid, String relationshipType,
			OMRSCheckedExceptionBase e, String name) {
    	
		return new AnalyticsModelingCheckedException(
				AnalyticsModelingErrorCode.GET_RELATIONSHIP_EXCEPTION.getMessageDefinition(relationshipType, guid, name),
				this.getClass().getSimpleName(),
				"deleteEntity",
				e);
	}

	/**
     * Returns true if the provided relationship is between the 2 specified entities
     *
     * @param relationship - the relationship instance to be checked
     * @param guid1        is the guid of one end
     * @param guid2        is the guid of the second end
     * @return boolean
     */
    private boolean checkRelationshipEnds(Relationship relationship, String guid1, String guid2) {
        String end1Guid = relationship.getEntityOneProxy().getGUID();
        String end2Guid = relationship.getEntityTwoProxy().getGUID();
        return (end1Guid.equals(guid1) && end2Guid.equals(guid2)) || (end1Guid.equals(guid2) && end2Guid.equals(guid1));
    }

    /**
     * Returns the entity of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param typeName      is the entity type
     * @param qualifiedName - qualified name property of the entity, unique for the same entity type
     * @param properties    specific to the entity type
     * @return the existing entity with the given qualified name or the newly created entity with the given qualified name
     * @throws InvalidParameterException
     * @throws PropertyErrorException
     * @throws RepositoryErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws StatusNotSupportedException
     * @throws AnalyticsModelingCheckedException 
     */
    public EntityDetail addEntity(String typeName,
                                  String qualifiedName,
                                  InstanceProperties properties,
                                  boolean zoneRestricted) throws InvalidParameterException, PropertyErrorException, RepositoryErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, TypeErrorException, StatusNotSupportedException, AnalyticsModelingCheckedException {
        return addEntity(typeName, qualifiedName, properties, null, zoneRestricted);
    }


    /**
     * Returns the entity of the given type with the specified qualified name; if it doesn't already exists, it is created with the provided instance properties
     *
     * @param typeName        is the entity type
     * @param qualifiedName   - qualified name property of the entity, unique for the same entity type
     * @param properties      specific to the entity type
     * @param classifications classifications to be added to entity
     * @param zoneRestricted
     * @return the existing entity with the given qualified name or the newly created entity with the given qualified name
     * @throws InvalidParameterException
     * @throws StatusNotSupportedException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws TypeErrorException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws ClassificationErrorException
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws AnalyticsModelingCheckedException 
     */
    public EntityDetail addEntity(String typeName,
                                  String qualifiedName,
                                  InstanceProperties properties,
                                  List<Classification> classifications,
                                  boolean zoneRestricted) throws InvalidParameterException, StatusNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeErrorException, FunctionNotSupportedException, PagingErrorException, ClassificationErrorException, UserNotAuthorizedException, RepositoryErrorException, AnalyticsModelingCheckedException {

        OMEntityWrapper wrapper = createOrUpdateEntity(typeName,
                                                        qualifiedName,
                                                        properties,
                                                        classifications,
                                                        false,
                                                        zoneRestricted);
        return wrapper != null ? wrapper.getEntityDetail() : null;
    }

    public OMEntityWrapper createOrUpdateEntity(String typeName,
                                                String qualifiedName,
                                                InstanceProperties properties,
                                                List<Classification> classifications,
                                                boolean update,
                                                boolean zoneRestricted) throws UserNotAuthorizedException, FunctionNotSupportedException,
                                                                               InvalidParameterException, RepositoryErrorException,
                                                                               PropertyErrorException, TypeErrorException,
                                                                               PagingErrorException, ClassificationErrorException,
                                                                               StatusNotSupportedException, EntityNotKnownException, AnalyticsModelingCheckedException {
        EntityDetail entityDetail;
        OMEntityWrapper wrapper;
        entityDetail = getEntity(typeName, qualifiedName, zoneRestricted);
        if (entityDetail == null) {
            entityDetail = addEntity("", Constants.ANALYTICS_MODELING_USER_ID, typeName, properties, classifications, zoneRestricted);
            log.debug("Entity with qualified name {} added", qualifiedName);
            if(log.isDebugEnabled()) {
                log.debug("Entity: {}", entityDetail);
            }
            wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.NEW);
        } else {
            log.debug("Entity with qualified name {} already exists", qualifiedName);
            if(log.isDebugEnabled()) {
                log.debug("Entity: {}", entityDetail);
            }
            wrapper = new OMEntityWrapper(entityDetail, OMEntityWrapper.EntityStatus.EXISTING);

        }

        return wrapper;
    }

    /**
     * Returns the relationship of the given type with the specified qualified name; 
     * if it doesn't already exists, it is created with the provided instance properties
     *
     * @param relationshipType is the relationship type name
     * @param guid1            first end of the relationship
     * @param guid2            second end of the relationship
     * @param instanceProperties       specific to the relationship type
     * @return the existing relationship with the given qualified name or the newly created relationship with the given qualified name
     * @throws AnalyticsModelingCheckedException 
     */
    public Relationship addRelationship(String relationshipType,
                                        String guid1,
                                        String guid2,
                                        InstanceProperties instanceProperties) throws AnalyticsModelingCheckedException {
        Relationship relationship = checkRelationshipExists(relationshipType, guid1, guid2);
        if (relationship == null) {
            relationship = addRelationship("", relationshipType, instanceProperties, guid1, guid2);
        }

        return relationship;
    }
}
