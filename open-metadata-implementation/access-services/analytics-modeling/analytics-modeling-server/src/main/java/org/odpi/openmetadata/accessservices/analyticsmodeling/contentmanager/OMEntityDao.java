/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.odpi.openmetadata.accessservices.analyticsmodeling.auditlog.AnalyticsModelingAuditCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

/**
 * The class implements access wrapper to retrieve data OMRS.
 * 
 */
public class OMEntityDao {

    protected final OMRSRepositoryConnector enterpriseConnector;
    protected List<String> supportedZones;
    private final AuditLog auditLog;
    
    private String context;
    
	private static final List<InstanceStatus> FILTER_ACTIVE = Arrays.asList(InstanceStatus.ACTIVE);

    public OMEntityDao(OMRSRepositoryConnector enterpriseConnector, List<String> supportedZones, AuditLog auditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.auditLog = auditLog;
        this.supportedZones = supportedZones;
    }

    /**
     * Set context of the execution for logging.
     * @param value high level method/operation requested by user.
     */
    public void setContext(String value) {
    	context = value;
    }
    
    /**
     * Find entities matching criteria.
     * @param matchProperties filter.
     * @param typeName of the requested entity.
     * @param fromElement index of the first entity.
     * @param pageSize number of entities to return.
     * @return list of matching entities
     * @throws AnalyticsModelingCheckedException in case repository fails.
     */
    public List<EntityDetail> findEntities(InstanceProperties matchProperties, String typeName, int fromElement, int pageSize) throws AnalyticsModelingCheckedException {
        // GDW the matchProperties passed to this method should have already converted any exact match string
        // using the getExactMatchRegex repository helper method
        OMRSRepositoryHelper repositoryHelper = enterpriseConnector.getRepositoryHelper();
        TypeDef typeDef = repositoryHelper.getTypeDefByName(Constants.ANALYTICS_MODELING_USER_ID, typeName);
        List<EntityDetail> existingEntities;
        try {
            auditLog.logMessage(context, AnalyticsModelingAuditCode.FIND_ENTITIES.getMessageDefinition(typeDef.getName(),  matchProperties.toString()));
            existingEntities = enterpriseConnector.getMetadataCollection()
            		.findEntitiesByProperty(Constants.ANALYTICS_MODELING_USER_ID,
                                            typeDef.getGUID(),
                                            matchProperties,
                                            MatchCriteria.ALL,
                                            fromElement,
                                            Collections.singletonList(InstanceStatus.ACTIVE),
                                            null,
                                            null,
                                            null,
                                            SequencingOrder.ANY,
                                            pageSize);
        } catch (InvalidParameterException | PropertyErrorException | TypeErrorException 
        		| FunctionNotSupportedException | UserNotAuthorizedException | RepositoryErrorException 
        		| PagingErrorException ex) {
            String keys = String.join(",", matchProperties.getInstanceProperties().keySet());
            String values = matchProperties.getInstanceProperties().values().stream().map(InstancePropertyValue::valueAsString).collect(Collectors.joining(","));
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.GET_ENTITY_EXCEPTION.getMessageDefinition(keys, values),
					this.getClass().getSimpleName(),
					"findEntities",
					ex);

       }
        return existingEntities;
    }

    /**
     * Returns the properties object for the given pair of key - value that can be used for retrieving
     *
     * @param properties - all properties to use for matching
     * @param zoneRestricted to take into account zones.
     * @return properties with the given key - value pair
     */
    public InstanceProperties buildMatchingInstanceProperties(Map<String, String> properties, boolean zoneRestricted) {
        InstanceProperties instanceProperties = new InstanceProperties();
        if(properties != null && properties.size() > 0) {
            for(Map.Entry<String, String> entry :  properties.entrySet()){
                instanceProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.ANALYTICS_MODELING_OMAS_NAME, instanceProperties, entry.getKey(), entry.getValue(), "throw buildMatchingInstanceProperties");
            }
        }
        if(zoneRestricted && supportedZones != null && !supportedZones.isEmpty()){
            instanceProperties = enterpriseConnector.getRepositoryHelper().addStringArrayPropertyToInstance(Constants.ANALYTICS_MODELING_OMAS_NAME, instanceProperties, Constants.ZONE_MEMBERSHIP, supportedZones, "throw buildMatchingInstanceProperties");
        }

        return instanceProperties;
    }
    
	/**
	 * Helper function to fetch entity by GUID from repository.
	 * 
	 * @param guid of the entity to fetch.
	 * @return entity with required GUID.
	 * @throws AnalyticsModelingCheckedException if entity cannot be fetched: failed request to repository or entity not found.
	 */
    public EntityDetail getEntityByGuid(String guid) throws AnalyticsModelingCheckedException  {
        EntityDetail entity = null;
        try {
            entity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.ANALYTICS_MODELING_USER_ID, guid);
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException 
        		| EntityProxyOnlyException | UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.GET_ENTITY_EXCEPTION.getMessageDefinition(Constants.GUID, guid),
					this.getClass().getSimpleName(),
					"getEntityByGuid",
					ex);
        }
        if (entity == null) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getMessageDefinition(Constants.GUID, guid),
					this.getClass().getSimpleName(),
					"getEntityByGuid");
        }

        return entity;
    }
    
	private String getTypeDefGuidByName(String name) {
		return enterpriseConnector.getRepositoryHelper().getTypeDefByName(Constants.ANALYTICS_MODELING_USER_ID, name).getGUID();
	}

	/**
	 * Get relationships of certain type for the entity.
	 * @param entity whose relationships are requested.
	 * @param relationshipType only relationship of the type are requested. All relationships are returned if null.
	 * @return requested relationships.
	 * @throws AnalyticsModelingCheckedException in case of repository fails.
	 */
	public List<Relationship> getRelationshipsForEntity(EntityDetail entity, String relationshipType) throws AnalyticsModelingCheckedException {
		String relationshipTypeGuid = relationshipType == null ? null : getTypeDefGuidByName(relationshipType);
		try {
			return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.ANALYTICS_MODELING_USER_ID,
					entity.getGUID(), relationshipTypeGuid, 0, FILTER_ACTIVE, null, null, null, 0);
		} catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException
				| PropertyErrorException | PagingErrorException | FunctionNotSupportedException
				| UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.GET_RELATIONSHIP_EXCEPTION.getMessageDefinition(relationshipType, getEntityQName(entity)),
					this.getClass().getSimpleName(),
					"getRelationshipsForEntity",
					ex);


		}
	}
	/**
	 * Get relationships of certain GUID for the entity.
	 * @param entity whose relationships are requested.
	 * @param relationshipTypeGUID only relationship of the type are requested. All relationships are returned if null.
	 * @return requested relationships.
	 * @throws AnalyticsModelingCheckedException in case of repository fails.
	 */
	public List<Relationship> getRelationshipsByGUIDForEntity(EntityDetail entity, String relationshipTypeGUID) throws AnalyticsModelingCheckedException {
		try {
			return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.ANALYTICS_MODELING_USER_ID,
					entity.getGUID(), relationshipTypeGUID, 0, FILTER_ACTIVE, null, null, null, 0);
		} catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException
				| PropertyErrorException | PagingErrorException | FunctionNotSupportedException
				| UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.GET_RELATIONSHIP_EXCEPTION.getMessageDefinition(relationshipTypeGUID, getEntityQName(entity)),
					this.getClass().getSimpleName(),
					"getRelationshipsForEntity",
					ex);


		}
	}

	/**
	 * Get qualified name property for the entity.
	 * @param entity whose qualified name is requested.
	 * @return qualified name.
	 */
	public String getEntityQName(EntityDetail entity) {
		return enterpriseConnector.getRepositoryHelper()
				.getStringProperty(Constants.ANALYTICS_MODELING_OMAS_NAME, Constants.QUALIFIED_NAME, entity.getProperties(), context);
	}
	/**
	 * Get entity property of type string.
	 * @param entity whose property is requested.
	 * @param name if the requested property.
	 * @return property value.
	 */
	public String getEntityStringProperty(EntityDetail entity, String name) {
		return enterpriseConnector.getRepositoryHelper()
				.getStringProperty(Constants.ANALYTICS_MODELING_OMAS_NAME, name, entity.getProperties(), context);
	}
	/**
	 * Get entity property of type boolean.
	 * @param entity whose property is requested.
	 * @param name if the requested property.
	 * @return property value.
	 */
	public Boolean getEntityBooleanProperty(EntityDetail entity, String name) {
		return enterpriseConnector.getRepositoryHelper()
				.getBooleanProperty(Constants.ANALYTICS_MODELING_OMAS_NAME, name, entity.getProperties(), context);
	}

	/**
	 * Get entity property of type integer.
	 * @param entity whose property is requested.
	 * @param name if the requested property.
	 * @return property value.
	 */
	public int getEntityIntProperty(EntityDetail entity, String name) {
		return enterpriseConnector.getRepositoryHelper()
				.getIntProperty(Constants.ANALYTICS_MODELING_OMAS_NAME, name, entity.getProperties(), context);
	}

	/**
	 * Get property of type string from collection of instance properties.
	 * @param name if the requested property.
	 * @param properties collection of instance properties
	 * @return property value.
	 */
	public String getStringProperty(String name, InstanceProperties properties) {
		return enterpriseConnector.getRepositoryHelper()
				.getStringProperty(Constants.ANALYTICS_MODELING_OMAS_NAME, name, properties, context);
	}

	/**
	 * Get map of property from collection of instance properties.
	 * @param name if the requested property.
	 * @param properties collection of instance properties
	 * @return property value.
	 */
	public InstanceProperties getMapProperty(InstanceProperties properties, String name) {
		return enterpriseConnector.getRepositoryHelper()
				.getMapProperty(Constants.ANALYTICS_MODELING_OMAS_NAME, name, properties, context);
	}
}
