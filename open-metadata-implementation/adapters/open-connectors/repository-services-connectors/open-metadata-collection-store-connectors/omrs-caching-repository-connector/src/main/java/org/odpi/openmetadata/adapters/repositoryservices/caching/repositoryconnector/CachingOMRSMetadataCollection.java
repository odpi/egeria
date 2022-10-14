/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector;
import org.odpi.openmetadata.adapters.repositoryservices.caching.auditlog.CachingOMRSErrorCode;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSDynamicTypeMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;


import java.util.*;

/**
 * This sample repository collection is created with an embedded connector that caches content. The embedded connector is initialised and has an associated
 * embedded Metadata collection.
 * <p>
 * The following calls will be delegated to the embedded repository
 * <ol>
 *   <li>Creation of reference entities and relationship</li>
 *   <li>Find instance calls (for entities and relationships)</li>
 *   <li>Type query commands.</li>
 * </ol>
 * <p>
 * For a complete list of calls - then review the methods that override the super class.
 * <p>
 * The above mechanism provides a way of caching content in the embedded connector. Delegation of the many find
 * methods means that not new find implementations need to be written. Prior to thissample - this was where a lot of the development effort
 * went when creating repository proxies.
 * <p>
 * The FileOMRSRepositoryEventMapper, can be used with this repository to form a repository proxy. The event mapper holds all the polling logic
 * and 3rd party specific logic.
 */
public class CachingOMRSMetadataCollection extends OMRSDynamicTypeMetadataCollectionBase {

    private OMRSMetadataCollection embeddedMetadataCollection = null;

    private OMRSRepositoryConnector validEmbeddedOMRSConnector = null;


    /**
     * The caching OMRS metadata collection is initialised with an embedded connector, which all supported OMRS calls are delegated to.
     *
     * @param parentConnector      connector that this metadata collection supports.
     *                             The connector has the information to call the metadata repository.
     * @param repositoryName       name of this repository.
     * @param repositoryHelper     helper that provides methods to repository connectors and repository event mappers
     *                             to build valid type definitions (TypeDefs), entities and relationships.
     * @param repositoryValidator  validator class for checking open metadata repository objects and parameters
     * @param metadataCollectionId unique identifier for the repository
     * @param configuredEmbeddedConnectors the configured embedded connectors
     * @throws RepositoryErrorException repository error exception
     */

    public CachingOMRSMetadataCollection(CachingOMRSRepositoryProxyConnector parentConnector,
                                         String repositoryName,
                                         OMRSRepositoryHelper repositoryHelper,
                                         OMRSRepositoryValidator repositoryValidator,
                                         String metadataCollectionId,
                                         List<Connector> configuredEmbeddedConnectors
                                        ) throws RepositoryErrorException {
        super(parentConnector,
              repositoryName,
              repositoryHelper,
              repositoryValidator,
              metadataCollectionId);
        String methodName = "CachingOMRSMetadataCollection()";
        // check that we have one embedded OMRS connector, error if not

       if (configuredEmbeddedConnectors == null || configuredEmbeddedConnectors.isEmpty()) {
           raiseRepositoryErrorException(CachingOMRSErrorCode.EMBEDDED_CONNECTOR_NOT_SUPPLIED, methodName, null, repositoryName);
       } else if (configuredEmbeddedConnectors.size() > 1) {
           raiseRepositoryErrorException(CachingOMRSErrorCode.MULTIPLE_EMBEDDED_CONNECTORS_SUPPLIED, methodName, null, repositoryName);
       } else {
           Connector connector = configuredEmbeddedConnectors.get(0);
           if (connector instanceof  OMRSRepositoryConnector) {
               validEmbeddedOMRSConnector = (OMRSRepositoryConnector) connector;
           } else {
               raiseRepositoryErrorException(CachingOMRSErrorCode.EMBEDDED_CONNECTOR_WRONG_TYPE, methodName, null, repositoryName);
           }
       }

        this.metadataCollectionId = metadataCollectionId;
        // initialise the embedded connector and stores its collection
        validEmbeddedOMRSConnector.setMetadataCollectionName(metadataCollectionName + "-embedded");
        validEmbeddedOMRSConnector.setRepositoryHelper(repositoryHelper);
        validEmbeddedOMRSConnector.setRepositoryValidator(repositoryValidator);
        // this needs to be done last as it creates the embedded metadata collection if there is not one
        validEmbeddedOMRSConnector.setMetadataCollectionId(metadataCollectionId + "-embedded");

        this.embeddedMetadataCollection = validEmbeddedOMRSConnector.getMetadataCollection();
    }
    public OMRSRepositoryConnector getEmbeddedOMRSConnector() {
        return validEmbeddedOMRSConnector;
    }


    /**
     * Throws a RepositoryErrorException based on the provided parameters.
     *
     * @param errorCode the error code for the exception
     * @param methodName the method name throwing the exception
     * @param cause the underlying cause of the exception (if any, otherwise null)
     * @param params any additional parameters for formatting the error message
     * @throws RepositoryErrorException error contacting the repository
     */
    private void raiseRepositoryErrorException(CachingOMRSErrorCode errorCode, String methodName, Exception cause, String ...params) throws RepositoryErrorException {
        if (cause == null) {
            throw new RepositoryErrorException(errorCode.getMessageDefinition(params),
                                                this.getClass().getName(),
                                                methodName);
        } else {
            throw new RepositoryErrorException(errorCode.getMessageDefinition(params),
                                                this.getClass().getName(),
                                                methodName,
                                                cause);
        }
    }

    // ** Delegate all the collection methods we care about to the embedded connector collection.
    // Currently only read orientated calls in groups 3 and 4 are implmented. See OMRSMetadataCollection class for the meaning of the groups.

    @Override
    public EntityDetail isEntityKnown(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException {
        return embeddedMetadataCollection.isEntityKnown(userId, guid);
    }

    @Override
    public EntitySummary getEntitySummary(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getEntitySummary(userId, guid);
    }

    @Override
    public EntityDetail getEntityDetail(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityProxyOnlyException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getEntityDetail(userId, guid);
    }

    @Override
    public EntityDetail getEntityDetail(String userId, String guid, Date asOfTime) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityProxyOnlyException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getEntityDetail(userId, guid, asOfTime);
    }

    @Override
    public List<Relationship> getRelationshipsForEntity(String userId, String entityGUID, String relationshipTypeGUID, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getRelationshipsForEntity(userId, entityGUID, relationshipTypeGUID, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
    }

    @Override
    public List<EntityDetail> findEntities(String userId, String entityTypeGUID, List<String> entitySubtypeGUIDs, SearchProperties matchProperties, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, SearchClassifications matchClassifications, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.findEntities(userId,
                                                       entityTypeGUID,
                                                       entitySubtypeGUIDs,
                                                       matchProperties,
                                                       fromEntityElement,
                                                       limitResultsByStatus,
                                                       matchClassifications,
                                                       asOfTime,
                                                       sequencingProperty,
                                                       sequencingOrder,
                                                       pageSize);
    }

    @Override
    public List<EntityDetail> findEntitiesByProperty(String userId, String entityTypeGUID, InstanceProperties matchProperties, MatchCriteria matchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.findEntitiesByProperty(userId, entityTypeGUID, matchProperties, matchCriteria, fromEntityElement, limitResultsByStatus, limitResultsByClassification, asOfTime, sequencingProperty, sequencingOrder, pageSize);
    }

    @Override
    public List<EntityDetail> findEntitiesByClassification(String userId, String entityTypeGUID, String classificationName, InstanceProperties matchClassificationProperties, MatchCriteria matchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, ClassificationErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.findEntitiesByClassification(userId, entityTypeGUID, classificationName, matchClassificationProperties, matchCriteria, fromEntityElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
    }

    @Override
    public List<EntityDetail> findEntitiesByPropertyValue(String userId, String entityTypeGUID, String searchCriteria, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.findEntitiesByPropertyValue(userId, entityTypeGUID, searchCriteria, fromEntityElement, limitResultsByStatus, limitResultsByClassification, asOfTime, sequencingProperty, sequencingOrder, pageSize);
    }

    @Override
    public Relationship isRelationshipKnown(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException {
        return embeddedMetadataCollection.isRelationshipKnown(userId, guid);
    }

    @Override
    public Relationship getRelationship(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getRelationship(userId, guid);
    }

    @Override
    public Relationship getRelationship(String userId, String guid, Date asOfTime) throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getRelationship(userId, guid, asOfTime);
    }

    @Override
    public List<Relationship> findRelationships(String userId, String relationshipTypeGUID, List<String> relationshipSubtypeGUIDs, SearchProperties matchProperties, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.findRelationships(userId, relationshipTypeGUID, relationshipSubtypeGUIDs, matchProperties, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
    }

    @Override
    public List<Relationship> findRelationshipsByProperty(String userId, String relationshipTypeGUID, InstanceProperties matchProperties, MatchCriteria matchCriteria, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.findRelationshipsByProperty(userId, relationshipTypeGUID, matchProperties, matchCriteria, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);

    }

    @Override
    public List<Relationship> findRelationshipsByPropertyValue(String userId, String relationshipTypeGUID, String searchCriteria, int fromRelationshipElement, List<InstanceStatus> limitResultsByStatus, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.findRelationshipsByPropertyValue(userId, relationshipTypeGUID, searchCriteria, fromRelationshipElement, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, pageSize);
    }

    @Override
    public InstanceGraph getLinkingEntities(String userId, String startEntityGUID, String endEntityGUID, List<InstanceStatus> limitResultsByStatus, Date asOfTime) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getLinkingEntities(userId, startEntityGUID, endEntityGUID, limitResultsByStatus, asOfTime);
    }

    @Override
    public InstanceGraph getEntityNeighborhood(String userId, String entityGUID, List<String> entityTypeGUIDs, List<String> relationshipTypeGUIDs, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, int level) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getEntityNeighborhood(userId, entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, asOfTime, level);
    }

    @Override
    public List<EntityDetail> getRelatedEntities(String userId, String startEntityGUID, List<String> entityTypeGUIDs, int fromEntityElement, List<InstanceStatus> limitResultsByStatus, List<String> limitResultsByClassification, Date asOfTime, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return embeddedMetadataCollection.getRelatedEntities(userId, startEntityGUID, entityTypeGUIDs, fromEntityElement, limitResultsByStatus, limitResultsByClassification, asOfTime, sequencingProperty, sequencingOrder, pageSize);
    }

    @Override
    public void saveEntityReferenceCopy(String userId, EntityDetail entity) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, HomeEntityException, EntityConflictException, InvalidEntityException, FunctionNotSupportedException, UserNotAuthorizedException {
        embeddedMetadataCollection.saveEntityReferenceCopy(userId, entity);
    }

    @Override
    public void saveRelationshipReferenceCopy(String userId, Relationship relationship) throws InvalidParameterException, RepositoryErrorException, TypeErrorException, EntityNotKnownException, PropertyErrorException, HomeRelationshipException, RelationshipConflictException, InvalidRelationshipException, FunctionNotSupportedException, UserNotAuthorizedException {
        embeddedMetadataCollection.saveRelationshipReferenceCopy(userId, relationship);
    }
}