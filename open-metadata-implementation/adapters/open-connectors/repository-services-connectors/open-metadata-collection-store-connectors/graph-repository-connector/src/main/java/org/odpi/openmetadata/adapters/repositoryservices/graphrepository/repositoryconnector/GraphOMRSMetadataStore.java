/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;


import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.attribute.Text;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.tinkerpop.gremlin.process.traversal.P.eq;
import static org.apache.tinkerpop.gremlin.process.traversal.P.gt;
import static org.apache.tinkerpop.gremlin.process.traversal.P.gte;
import static org.apache.tinkerpop.gremlin.process.traversal.P.lt;
import static org.apache.tinkerpop.gremlin.process.traversal.P.lte;
import static org.apache.tinkerpop.gremlin.process.traversal.P.neq;
import static org.apache.tinkerpop.gremlin.process.traversal.P.within;
import static org.apache.tinkerpop.gremlin.process.traversal.P.without;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_CURRENT_STATUS;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_IS_PROXY;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_TYPE_NAME;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_PREFIX_CLASSIFICATION;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_PREFIX_ENTITY;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_PREFIX_RELATIONSHIP;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_RELATIONSHIP_GUID;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_RELATIONSHIP_TYPE_NAME;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_NAME_TYPE_NAME;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.corePropertiesClassification;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.corePropertiesEntity;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.corePropertiesRelationship;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.corePropertyTypes;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.getPropertyKeyClassification;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.getPropertyKeyEntity;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.getPropertyKeyRelationship;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSGraphFactory.corePropertyMixedIndexMappings;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.IN;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.LIKE;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory.PRIMITIVE;


/**
 * GraphOMRSMetadataStore provides the graph store for the GraphRepositoryConnector
 * The Graph Store is implemented using JanusGraph and is used to store instances.
 * There is no type graph because the RCM is used to get any information about TypeDefs and AttributeTypeDefs.
 */
class GraphOMRSMetadataStore {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSMetadataStore.class);

    private final String repositoryName;
    private final String metadataCollectionId;

    private final OMRSRepositoryHelper repositoryHelper;

    // The instance graph is used to store entities (vertices) and relationships (edges).

    GraphOMRSGraphFactory graphFactory;

    private final JanusGraph instanceGraph;
    private final GraphOMRSRelationshipMapper relationshipMapper;
    private final GraphOMRSEntityMapper entityMapper;
    private final GraphOMRSClassificationMapper classificationMapper;


    /**
     * Typical constructor
     *
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param repositoryName       name of this repository
     * @param repositoryHelper     utilities
     * @param auditLog             logging destination
     * @param storageProperties    properties for the graph DB
     * @throws RepositoryErrorException problem with the graph database.
     */
    GraphOMRSMetadataStore(String                 metadataCollectionId,
                           String                 repositoryName,
                           OMRSRepositoryHelper   repositoryHelper,
                           AuditLog               auditLog,
                           Map<String, Object>    storageProperties)

    throws RepositoryErrorException
    {

        final String methodName = "GraphOMRSMetadataStore";

        this.metadataCollectionId = metadataCollectionId;
        this.repositoryName = repositoryName;
        this.repositoryHelper = repositoryHelper;


        try
        {
            graphFactory = new GraphOMRSGraphFactory();
            synchronized (GraphOMRSMetadataStore.class)
            {
                instanceGraph = graphFactory.open(metadataCollectionId, repositoryName, auditLog, storageProperties);
            }
        }
        catch (RepositoryErrorException e)
        {
            log.error("{} Could not open graph database", methodName);
            throw e;
        }

        this.relationshipMapper = new GraphOMRSRelationshipMapper(metadataCollectionId, repositoryName, repositoryHelper);
        this.entityMapper = new GraphOMRSEntityMapper(metadataCollectionId, repositoryName, repositoryHelper);
        this.classificationMapper = new GraphOMRSClassificationMapper(metadataCollectionId, repositoryName, repositoryHelper);

    }


    // A note on existence checking:
    // The MDC will NOT have already checked that there is not already an entity or entity proxy wth the same GUID.
    // Although we KNOW that this is an attempt to create a new entity and that the GUID has just been generated,
    // so we COULD re-spin it, we should NOT do that here - it should be in the MDC layer and RepoHelper layer.
    // Therefore if we get a GUID clash here we throw an exception.
    //
    synchronized EntityDetail createEntityInStore(EntityDetail entity)

    throws RepositoryErrorException,
           InvalidParameterException
    {
        final String methodName = "createEntityInStore";

        Vertex vertex;

        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entity.getGUID());

        if (vertexIt.hasNext())
        {

            vertex = vertexIt.next();
            log.debug("{} found existing vertex {}", methodName, vertex);

            /*
             * If the existing vertex is for a proxy entity then check that the supplied entity is a ref copy (i.e.
             * metadataCollection is not local) and if so, clear flag and use the same vertex for the ref copy.
             * If the existing vertex is NOT for a proxy entity OR we find it is a local entity then throw error.
             */

            if (entityMapper.isProxy(vertex))
            {


                if (!metadataCollectionId.equals(entity.getMetadataCollectionId()))
                {
                    log.debug("{} existing vertex is a proxy", methodName);
                    // Replace the proxy with the ref copy - make sure the proxy flag is cleared then proceed to populating it below...
                    entityMapper.clearProxy(vertex);

                }
                else
                {
                    log.error("{} existing vertex apparently a proxy, but has local metadataCollectionId", methodName);
                    g.tx().rollback();

                    final String parameterName = "metadataCollectionId";

                    throw new InvalidParameterException(GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                      this.getClass().getName(),
                                                                                                                      repositoryName),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        parameterName);
                }
            }
            else
            {
                log.error("{} existing vertex for GUID {} and it is not a proxy", methodName, entity.getGUID());
                g.tx().rollback();

                String parameterName = "entity";

                throw new InvalidParameterException(GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                  this.getClass().getName(),
                                                                                                                  repositoryName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);
            }
        }
        else
        {

            // No existing vertex found - create one
            log.debug("{} create vertex for entity {}", methodName, entity.getGUID());
            vertex = g.addV("Entity").next();
        }

        // Whether created new or reusing old proxy, populate the vertex.

        try
        {
            entityMapper.mapEntityDetailToVertex(entity, vertex);

            // Create a vertex per classification and link them to the entity vertex
            List<Classification> classifications = entity.getClassifications();
            if (classifications != null)
            {
                for (Classification classification : classifications)
                {
                    log.debug("{} add classification: {} ", methodName, classification.getName());
                    Vertex classificationVertex = g.addV("Classification").next();
                    classificationMapper.mapClassificationToVertex(classification, classificationVertex);
                    vertex.addEdge("Classifier", classificationVertex);
                }
            }

        }
        catch (Exception e)
        {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.ENTITY_NOT_CREATED.getMessageDefinition(
                            entity.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }


        g.tx().commit();

        return entity;
    }

    // A note on existence checking:
    // The MDC has already checked that there is not already an entity or entity proxy wth the same GUID.
    // So create an entity and note that it is a proxy (flag)
    // If the MDC found that an entity (of any description, entity, proxy or reference copy) is present - then it will not have asked you to create the proxy
    // So - if we do find that there is a GUID clash then throw exception.
    //
    synchronized void createEntityProxyInStore(EntityProxy entityProxy)

    throws RepositoryErrorException,
           InvalidParameterException
    {
        final String methodName = "createEntityProxyInStore";

        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityProxy.getGUID());
        if (vertexIt.hasNext())
        {
            Vertex vertex = vertexIt.next();
            log.error("{} createEntityProxyInStore found existing vertex {}", methodName, vertex);
            g.tx().rollback();

            throw new InvalidParameterException(
                    GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS.getMessageDefinition(
                            entityProxy.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "entityProxy");
        }

        Vertex vertex = g.addV("Entity").next();

        try
        {
            entityMapper.mapEntityProxyToVertex(entityProxy, vertex);

            // Create a vertex per classification and link them to the entity vertex
            List<Classification> classifications = entityProxy.getClassifications();
            if (classifications != null)
            {
                for (Classification classification : classifications)
                {
                    log.debug("{} add classification {}", methodName, classification.getName());
                    Vertex classificationVertex = g.addV("Classification").next();
                    classificationMapper.mapClassificationToVertex(classification, classificationVertex);
                    vertex.addEdge("Classifier", classificationVertex);
                }
            }

        }
        catch (Exception e)
        {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.ENTITY_NOT_CREATED.getMessageDefinition(
                            entityProxy.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }

        g.tx().commit();

    }


    /*
     *  If there is no entity that has the GUID of the entity to be saved, create an entity detail using the passed entity.
     *
     *  If there is an entity with the same GUID this is not always an error condition: There may already be an
     *  entity in the store that has the same GUID, for either of the following reasons:
     *  1) A request to save a reference copy pertains to either creation of a ref copy or update of a ref copy. It would
     *     be an error if the existing entity has a different metadataCollectionId to the entity to be saved. In this case
     *     an exception is thrown.
     *  2) There may already be a proxy (e.g. from earlier creation of a relationship). Because we are being passed a
     *     full entity detail to save as a reference copy, the ref copy replaces the proxy. But the reference copy must
     *     have the same metadataCollectionId as the proxy, else there has been confusion as to who owns the entity
     *
     *  In summary
     *  - if no entity, create the ref copy
     *  - if existing entity,
     *     - if it is a proxy
     *           check the metadataCollectionId matches the one passed.
     *           if matching metadataCollectionId
     *               update the existing entity, replacing the proxy with the ref copy; the proxy flag will be cleared.
     *           else
     *               error
     *     - else not a proxy
     *           if matching metadataCollectionId
     *               update the existing entity
     *           else
     *               error
     *
     *  This can be simplified to:
     *  - if no entity
     *        create the ref copy
     *  - if existing entity,
     *        if matching metadataCollectionId
     *             update the existing entity, clearing proxy flag if it was set
     *         else
     *             error
     */
    synchronized void saveEntityReferenceCopyToStore(EntityDetail entity)

    throws InvalidParameterException,
           RepositoryErrorException

    {

        final String methodName = "saveEntityReferenceCopyToStore";

        Vertex vertex;

        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entity.getGUID());

        if (vertexIt.hasNext())
        {

            vertex = vertexIt.next();
            log.debug("{} found existing vertex {}", methodName, vertex);

            /*
             * Check the metadataCollectionId is not local and that it matches the metadataCollectionId of the
             * passed entity
             */
            String vertexMetadataCollectionId = entityMapper.getEntityMetadataCollectionId(vertex);

            if (! vertexMetadataCollectionId.equals(entity.getMetadataCollectionId()))
            {

                /*
                 *  Error condition
                 *  The local repository already has a proxy or reference copy of an entity from a repository other than the one that
                 *  submitted this reference copy.
                 */

                log.error("{} found an existing vertex from a different source, with metadataCollectionId {}", methodName, vertexMetadataCollectionId);
                g.tx().rollback();

                throw new InvalidParameterException(
                        GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS.getMessageDefinition(
                                entity.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName,
                        "entity");
            }

        }
        else
        {

            // No existing vertex found - create one
            log.debug("{} create vertex for entity {}", methodName, entity.getGUID());
            vertex = g.addV("Entity").next();
        }

        /*
         * Whether this just created a new vertex or is reusing an existing vertex (for a reference copy or proxy),
         * populate the vertex.
         * The mapping of an entity detail to the vertex will clear the proxy flag, even if previously set.
         */


        try
        {
            entityMapper.mapEntityDetailToVertex(entity, vertex);

            // Create a vertex per classification and link them to the entity vertex
            List<Classification> classifications = entity.getClassifications();
            if (classifications != null)
            {
                for (Classification classification : classifications)
                {
                    log.debug("{} add classification: {} ", methodName, classification.getName());
                    Vertex classificationVertex = g.addV("Classification").next();
                    classificationMapper.mapClassificationToVertex(classification, classificationVertex);
                    vertex.addEdge("Classifier", classificationVertex);
                }
            }

        }
        catch (Exception e)
        {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.ENTITY_NOT_CREATED.getMessageDefinition(
                            entity.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }


        g.tx().commit();
    }


    /*
     *  If there is no entity that has the GUID of the entity to be saved, create an entity detail using the passed entity.
     *
     *  If there is an entity with the same GUID this is not always an error condition: There may already be an
     *  entity in the store that has the same GUID, for either of the following reasons:
     *  1) A request to save a reference copy pertains to either creation of a ref copy or update of a ref copy. It would
     *     be an error if the existing entity has a different metadataCollectionId to the entity to be saved. In this case
     *     an exception is thrown.
     *  2) There may already be an entity, a proxy or a reference copy. Update only its classifications.
     *
     *  In summary
     *  - if no entity, create the entity as entity proxy
     *  - if existing entity
     *     - if it's a proxy
     *           check the metadataCollectionId matches the one passed (between entities)
     *           if matching metadataCollectionId
     *               update only its classifications
     *           else
     *               error
     *     - else not a proxy
     *           if matching metadataCollectionId (between entities)
     *               update only its classifications
     *           else
     *               error
     *
     *  This can be simplified to:
     *  - if no entity
     *        create the entity as entity proxy
     *  - if existing entity,
     *        if matching metadataCollectionId (between entities)
     *             update only its classifications
     *         else
     *             error
     */
    synchronized void saveEntityReferenceCopyToStore(EntityProxy entity)

            throws InvalidParameterException,
                   RepositoryErrorException

    {

        final String methodName = "saveEntityReferenceCopyToStore(proxy)";

        Vertex vertex;

        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entity.getGUID());

        if (vertexIt.hasNext())
        {

            vertex = vertexIt.next();
            log.debug("{} found existing vertex {}", methodName, vertex);

            /*
             * Check the metadataCollectionId is not local and that it matches the metadataCollectionId of the
             * passed entity
             */
            String vertexMetadataCollectionId = entityMapper.getEntityMetadataCollectionId(vertex);

            if (metadataCollectionId.equals(entity.getMetadataCollectionId())
                        || !vertexMetadataCollectionId.equals(entity.getMetadataCollectionId()))
            {

                /*
                 *  Error condition
                 *  Either the local repository is being asked to save a reference copy of something it already owns,
                 *  or it already has a proxy or reference copy of an entity from a repository other than the one that
                 *  submitted this reference copy.
                 */

                log.error("{} found an existing vertex from a different source, with metadataCollectionId {}", methodName, vertexMetadataCollectionId);
                g.tx().rollback();

                throw new InvalidParameterException(
                        GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS.getMessageDefinition(
                                entity.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName,
                        "entity");
            }

        }
        else
        {

            // No existing vertex found - create one and populate it with the entity proxy data
            log.debug("{} create vertex for entity {}", methodName, entity.getGUID());
            vertex = g.addV("Entity").next();
            entityMapper.mapEntityProxyToVertex(entity, vertex);
        }

        try
        {
            updateEntityClassifications(entity, vertex, g);
        }
        catch (Exception e)
        {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.ENTITY_NOT_CREATED.getMessageDefinition(
                            entity.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }


        g.tx().commit();
    }


    synchronized EntityDetail getEntityDetailFromStore(String guid)

    throws EntityNotKnownException,
           EntityProxyOnlyException,
           RepositoryErrorException
    {

        String methodName = "getEntityDetailFromStore";


        EntityDetail entity = null;

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);

        // Although this traversal could only look for non-proxy entities, e.g. by adding
        //   gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);
        // it is better to get the entity whether a proxy or not because we need to throw a
        // different exception if a proxy is found, compared to no entity being found

        if (gt.hasNext())
        {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            try
            {
                if (vertex != null)
                {
                    log.debug("{} found entity vertex {}", methodName, vertex);

                    // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                    Boolean isProxy = entityMapper.isProxy(vertex);

                    if (!isProxy)
                    {
                        entity = new EntityDetail();
                        entityMapper.mapVertexToEntityDetail(vertex, entity);
                    }
                    else
                    {
                        // We know this is a proxy - throw the appropriate exception
                        log.warn("{} found entity but it is only a proxy, guid {}", methodName, guid);
                        g.tx().rollback();

                        throw new EntityProxyOnlyException(
                                GraphOMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(
                                        guid, methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName);
                    }

                }

            }
            catch (RepositoryErrorException e)
            {

                log.error("{} Caught exception {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                guid, methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }

        }
        else
        {

            // Entity was not found by GUID
            log.warn("{} entity with GUID {} not found", methodName, guid);
            g.tx().rollback();

            throw new EntityNotKnownException(
                    GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                            guid, methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }


        g.tx().commit();

        return entity;
    }

    synchronized EntitySummary getEntitySummaryFromStore(String guid)

    throws EntityNotKnownException,
           RepositoryErrorException
    {

        String methodName = "getEntitySummaryFromStore";


        EntitySummary entity = null;

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);

        if (gt.hasNext())
        {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            try
            {
                if (vertex != null)
                {
                    log.debug("{} found entity vertex {}", methodName, vertex);
                    entity = new EntitySummary();
                    entityMapper.mapVertexToEntitySummary(vertex, entity);
                }

            }
            catch (RepositoryErrorException e)
            {

                log.error("{} Caught exception {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                guid, methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }

        }
        else
        {

            // Entity was not found by GUID
            log.warn("{} entity with GUID {} not found", methodName, guid);
            g.tx().rollback();

            throw new EntityNotKnownException(
                    GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                            guid, methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }


        g.tx().commit();

        return entity;
    }


    synchronized EntityProxy getEntityProxyFromStore(String guid)

    throws RepositoryErrorException

    {
        String methodName = "getEntityProxyFromStore";

        EntityProxy entityProxy = null;

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Vertex> vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);
        if (vi.hasNext())
        {
            Vertex vertex = vi.next();

            try
            {
                if (vertex != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertex);

                    // Could test here whether vertex is for a proxy, but it doesn't matter whether the vertex represents a full entity
                    // (i.e. EntityDetail of a local/reference copy) as opposed to an EntityProxy. It can be retrieved as a proxy anyway...

                    entityProxy = new EntityProxy();

                    entityMapper.mapVertexToEntityProxy(vertex, entityProxy);

                }
            }
            catch (Exception e)
            {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                guid, methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        g.tx().commit();

        return entityProxy;
    }


    // Sanity check that the relationship (guid) does not already exist.
    // Existence checking of the entities has already been performed by the MDC.
    // The relationship therefore already has valid proxy entities included.
    // This method needs to locate the vertices so that the edge can be created in the graph.
    // If either of these fails then throw exception
    //
    synchronized void createRelationshipInStore(Relationship relationship)

    throws RepositoryErrorException,
           InvalidParameterException

    {
        String methodName = "createRelationshipInStore";

        // Begin a graph transaction. Locate the vertices for the ends, and create an edge between them.

        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, relationship.getGUID());
        if (edgeIt.hasNext())
        {
            Edge edge = edgeIt.next();
            log.error("{} found existing edge {}", methodName, edge);
            g.tx().rollback();

            throw new InvalidParameterException(
                    GraphOMRSErrorCode.RELATIONSHIP_ALREADY_EXISTS.getMessageDefinition(
                            relationship.getGUID(),
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName, "relationship");
        }


        String entityOneGUID = relationship.getEntityOneProxy().getGUID();
        String entityTwoGUID = relationship.getEntityTwoProxy().getGUID();

        Vertex vertexOne = null;
        Vertex vertexTwo = null;

        Iterator<Vertex> vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityOneGUID);
        if (vi.hasNext())
        {
            vertexOne = vi.next();
            log.debug("{} found entityOne vertex {}", methodName, vertexOne);
        }

        vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityTwoGUID);
        if (vi.hasNext())
        {
            vertexTwo = vi.next();
            log.debug("{} found entityTwo vertex {}", methodName, vertexTwo);
        }

        // If either end vertex does not exist then abandon the attempt - there should be at least a proxy if not a full entity in the store for each end
        if (vertexOne == null || vertexTwo == null)
        {
            log.error("{} Could not find both ends for relationship {}", methodName, relationship.getGUID());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.RELATIONSHIP_NOT_CREATED.getMessageDefinition(
                            relationship.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }

        Edge edge = vertexOne.addEdge("Relationship", vertexTwo);

        try
        {

            relationshipMapper.mapRelationshipToEdge(relationship, edge);

        }
        catch (Exception e)
        {
            log.error("{} Caught exception from relationship mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.RELATIONSHIP_NOT_CREATED.getMessageDefinition(
                            relationship.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }

        log.debug("{} Commit tx containing creation of edge", methodName);
        g.tx().commit();

    }


    /*
     *  This method will save a copy of the relationship to the graph.
     *
     *  The save could be the result of creation of a new relationship or it could be an update to an existing relationship.
     *  For example, a property update or soft delete of a remotely homed relationship. Accordingly, there may already be
     *  an edge in the graph, or not.
     *
     *  If there is already an edge - update it. If there is not an existing edge create one and map it.
     *
     *  In bth cases (create and update)...
     *
     *  Check that both the entities exist in the graph, using the entity GUID of the proxy in
     *  the passed relationship. For each end:
     *
     *
     *  If there is no entity that has the GUID of the passed entity, create a proxy entity using the passed entity.
     *
     *  If there is an entity with the same GUID then use it. The existing entity may be any of the following
     *  cases:
     *     * An existing EntityDetail for a locally homed entity
     *     * An existing EntityDetail for a remotely homed entity - i.e. a reference copy
     *     * An existing EntityProxy
     *
     *  Note that in the last case (only) it is possible that the repository should perform a detailed comparison of
     *  the stored proxy versus the passed proxy. It is not clear what to do if certain fields differ. For example,
     *  the core properties of the two proxies could differ - they could have been created/updated at different times by
     *  different users. But their unique attributes should match. If they differ it is not clear which is 'correct'. For
     *  this reason the repository will currently treat a proxy the same as the other two cases, and will use what it
     *  already has stored, ignoring the specific content of the proxy passed in the relationship.
     *
     *
     *  In summary
     *  - for each end:
     *      - if no entity, create a proxy
     *      - if existing entity,
     *           check the metadataCollectionId matches the one passed.
     *           if ! matching metadataCollectionId
     *               error
     *           else
     *               proceed to create the relationship reference copy
     *  - if relationship GUID does not exist
     *       create edge and map relationship
     *  - else relationship GUID exists
     *       - if metadataCollectionId is local or stored vs passed metadataCollectionIds differ
     *             error
     *       - else metadataCollectionId is not local and values match
     *             update existing edge by mapping relationship
     */
    synchronized void saveRelationshipReferenceCopyToStore(Relationship relationship)

    throws InvalidParameterException,
           RepositoryErrorException

    {

        final String methodName = "saveRelationshipReferenceCopyToStore";

        GraphTraversalSource g = instanceGraph.traversal();

        Vertex vertex;

        // Process end 1
        EntityProxy entityOne = relationship.getEntityOneProxy();

        Iterator<Vertex> vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityOne.getGUID());

        if (vertexIt.hasNext())
        {

            /*
             * There is a vertex for the entity.
             * It could be the master, a ref copy or a proxy. In any of these cases
             * it will be reused.
             * There is no point performing validation checks on type, home metadataCollection, etc
             * because there could be pending events that this repository has not seen yet. Any
             * updates to the entity will be handled via entity instance events.
             */

            vertex = vertexIt.next();
            log.debug("{} found existing vertex for end1 {}", methodName, vertex);

        }
        else
        {
            // Entity does not exist, create proxy
            createEntityProxyInStore(entityOne);
        }

        // Process end 2
        EntityProxy entityTwo = relationship.getEntityTwoProxy();
        vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityTwo.getGUID());

        if (vertexIt.hasNext())
        {

            /*
             * There is a vertex for the entity.
             * It could be the master, a ref copy or a proxy. In any of these cases
             * it will be reused.
             * There is no point performing validation checks on type, home metadataCollection, etc
             * because there could be pending events that this repository has not seen yet. Any
             * updates to the entity will be handled via entity instance events.
             */

            vertex = vertexIt.next();
            log.debug("{} found existing vertex for end2 {}", methodName, vertex);
        }
        else
        {
            // Entity does not exist, create proxy
            createEntityProxyInStore(entityTwo);
        }


        /*
         * Both ends have been checked and there are vertices for both.
         * Because we might have either created or retrieved the vertices for the entities, re-fetch them here
         * and throw an exception on any error.
         */

        Vertex vertexOne = null;
        Vertex vertexTwo = null;

        vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityOne.getGUID());
        if (vertexIt.hasNext())
        {
            vertexOne = vertexIt.next();
        }
        vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityTwo.getGUID());
        if (vertexIt.hasNext())
        {
            vertexTwo = vertexIt.next();
        }
        if (vertexOne == null || vertexTwo == null)
        {

            // Error!!
            log.error("{} Could not locate or create vertex for entity with guid {} used in relationship {}", methodName, vertexOne == null ? entityOne.getGUID() : entityTwo.getGUID(), relationship.getGUID());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.RELATIONSHIP_NOT_CREATED.getMessageDefinition(
                            relationship.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }


        // Process relationship
        Edge edge;

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, relationship.getGUID());

        if (edgeIt.hasNext())
        {

            edge = edgeIt.next();
            log.debug("{} found existing edge {}", methodName, edge);

            /*
             * Check the metadataCollectionId matches the metadataCollectionId of the
             * passed relationship
             */
            String edgeMetadataCollectionId = relationshipMapper.getRelationshipMetadataCollectionId(edge);

            if (! edgeMetadataCollectionId.equals(relationship.getMetadataCollectionId()))
            {
                /*
                 *  Error condition
                 *  Either the local repository already has a reference copy of a relationship from a repository other than the one that
                 *  submitted this reference copy.
                 */

                log.error("{} found an existing edge from a different source, with metadataCollectionId {}", methodName, edgeMetadataCollectionId);
                g.tx().rollback();

                final String parameterName = "relationship";

                throw new InvalidParameterException(
                        GraphOMRSErrorCode.RELATIONSHIP_ALREADY_EXISTS.getMessageDefinition(
                                relationship.getGUID(),
                                methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName,
                        parameterName);
            }

        }
        else
        {
            // No existing edge found. Create an edge for the relationship
            edge = vertexOne.addEdge("Relationship", vertexTwo);
        }


        // Populate the edge with the relationship
        try
        {

            relationshipMapper.mapRelationshipToEdge(relationship, edge);

        }
        catch (Exception e)
        {
            log.error("{} Caught exception from relationship mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            throw new RepositoryErrorException(
                    GraphOMRSErrorCode.RELATIONSHIP_NOT_CREATED.getMessageDefinition(
                            relationship.getGUID(),
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }

        log.debug("{} Commit tx containing creation or update of edge", methodName);
        g.tx().commit();
    }


    synchronized Relationship getRelationshipFromStore(String guid)

    throws RepositoryErrorException

    {
        String methodName = "getRelationshipFromStore";

        Relationship relationship = null;

        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, guid);
        if (edgeIt.hasNext())
        {
            Edge edge = edgeIt.next();
            log.debug("{} found existing edge {}", methodName, edge);

            relationship = new Relationship();

            // Map the properties
            relationshipMapper.mapEdgeToRelationship(edge, relationship);

            // Set the relationship ends...
            Vertex vertex = null;
            try
            {

                vertex = edge.outVertex();

                // Could test here whether each vertex is for a proxy, but it doesn't matter whether the vertex represents a full entity
                // (i.e. EntityDetail of a local/reference copy) as opposed to an EntityProxy. It can be retrieved as a proxy anyway...

                if (vertex != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertex);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertex, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }

                vertex = edge.inVertex();

                if (vertex != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertex);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertex, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            }
            catch (Exception e)
            {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.RELATIONSHIP_NOT_FOUND.getMessageDefinition(
                                entityMapper.getEntityGUID(vertex), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        g.tx().commit();

        return relationship;

    }


    synchronized void updateEntityInStore(EntityDetail entity)

    throws RepositoryErrorException

    {

        String methodName = "updateEntityInStore";

        // Look in the graph
        String guid = entity.getGUID();
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);

        // Only looking for non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

        if (gt.hasNext())
        {

            Vertex vertex = gt.next();
            log.debug("{} found entity vertex {}", methodName, vertex);

            try
            {

                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy)
                {

                    entityMapper.mapEntityDetailToVertex(entity, vertex);

                    updateEntityClassifications(entity, vertex, g);
                }

            }
            catch (Exception e)
            {
                log.error("{} caught exception {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_NOT_UPDATED.getMessageDefinition(
                                entity.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        log.debug("{} commit entity update tx: ", methodName);
        g.tx().commit();

    }


    synchronized void updateEntityInStore(EntityProxy entity)

            throws RepositoryErrorException

    {

        String methodName = "updateEntityInStore (EntityProxy)";

        // Look in the graph
        String guid = entity.getGUID();
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);

        // Only looking for proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, true);

        if (gt.hasNext())
        {

            Vertex vertex = gt.next();
            log.debug("{} found entity vertex {}", methodName, vertex);

            try
            {

                // Check if we have stumbled on a non-proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (isProxy)
                {

                    entityMapper.mapEntityProxyToVertex(entity, vertex);

                    updateEntityClassifications(entity, vertex, g);
                }

            }
            catch (Exception e)
            {
                log.error("{} caught exception {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_NOT_UPDATED.getMessageDefinition(
                                entity.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        log.debug("{} commit entity update tx: ", methodName);
        g.tx().commit();

    }


    // updateEntityClassifications
    private void updateEntityClassifications(EntitySummary         entity,
                                             Vertex                vertex,
                                             GraphTraversalSource  g)

    throws RepositoryErrorException

    {

        final String methodName = "updateEntityClassifications";

        // Synchronize the classifications
        // For each entity classification, determine whether it already exists and create and link any news ones to the entity vertex
        // If the entity already has an edge to a classification vertex for a classification of the same name then ensure that it is up
        // to date with the properties etc in the entity's classification.
        //
        // If multiple edges are found (to the same classification vertex by name) only one is retained (and updated); the others are removed.
        //
        // If a classification vertex is found (attached to the entity vertex) for a classification that is not in the entity, then it
        // is removed.
        Map<String, Classification> entityClassificationsByName = new HashMap<>();
        Map<String, Vertex> existingClassificationVerticesByName = new HashMap<>();
        Map<String, Edge> existingClassifierEdgesByName = new HashMap<>();

        // Map the desired classifications
        List<Classification> classifications = entity.getClassifications();
        if (classifications != null)
        {
            for (Classification entityClassification : classifications)
            {
                log.debug("{} entity should have classification: {}", methodName, entityClassification.getName());
                entityClassificationsByName.put(entityClassification.getName(), entityClassification);
            }
        }
        // Map the existing classifications
        Iterator<Edge> classifierEdges = vertex.edges(Direction.OUT, "Classifier");
        while (classifierEdges.hasNext())
        {
            Edge classifierEdge = classifierEdges.next();
            Vertex existingClassificationVertex = classifierEdge.inVertex();
            Classification existingClassification = new Classification();
            classificationMapper.mapVertexToClassification(existingClassificationVertex, existingClassification);
            log.debug("{} entity already has classification: {}", methodName, existingClassification.getName());
            existingClassificationVerticesByName.put(existingClassification.getName(), existingClassificationVertex);
            existingClassifierEdgesByName.put(existingClassification.getName(), classifierEdge);
        }
        // Now perform 1:1 synch - i) eliminate unnecessary classifications, then ii) update/add the desired classifications
        Set<String> namesToRemoveSet = new HashSet<>();
        Iterator<String> existingNamesIterator = existingClassificationVerticesByName.keySet().iterator();
        while (existingNamesIterator.hasNext())
        {
            String existingName = existingNamesIterator.next();
            if (!entityClassificationsByName.containsKey(existingName))
            {
                // remove old classification from graph and from map
                log.debug("{} entity remove classification: {}", methodName, existingName);
                Vertex classificationVertex = existingClassificationVerticesByName.get(existingName);
                classificationVertex.remove();
                namesToRemoveSet.add(existingName);
                Edge classifierEdge = existingClassifierEdgesByName.get(existingName);
                classifierEdge.remove();
                existingClassifierEdgesByName.remove(existingName);
            }
        }
        // remove from the map - needs to be done outside the previous loop to prevent a ConcurrentModificationException
        for (String name : namesToRemoveSet)
        {
            existingClassificationVerticesByName.remove(name);
        }
        // update/add the desired classifications
        Iterator<String> entityClassificationsNameIterator = entityClassificationsByName.keySet().iterator();
        while (entityClassificationsNameIterator.hasNext())
        {
            String newClassificationName = entityClassificationsNameIterator.next();
            if (!existingClassificationVerticesByName.containsKey(newClassificationName))
            {
                // add new classification vertex and edge
                log.debug("{} entity add classification: {}", methodName, newClassificationName);
                Vertex classificationVertex = g.addV("Classification").next();
                classificationMapper.mapClassificationToVertex(entityClassificationsByName.get(newClassificationName), classificationVertex);
                vertex.addEdge("Classifier", classificationVertex);
            }
            else
            {
                // update existing classification vertex
                log.debug("{} entity update classification: {}", methodName, newClassificationName);
                Vertex classificationVertex = existingClassificationVerticesByName.get(newClassificationName);
                classificationMapper.mapClassificationToVertex(entityClassificationsByName.get(newClassificationName), classificationVertex);
            }
        }

    }


    // updateRelationshipInStore
    synchronized void updateRelationshipInStore(Relationship relationship)

    throws RepositoryErrorException

    {

        String methodName = "updateRelationshipInStore";

        String guid = relationship.getGUID();
        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, guid);

        if (edgeIt.hasNext())
        {
            Edge edge = edgeIt.next();
            log.debug("{} found existing edge {}", methodName, edge);

            try
            {

                relationshipMapper.mapRelationshipToEdge(relationship, edge);

            }
            catch (Exception e)
            {

                log.error("{} Caught exception from relationship mapper {}", methodName, e.getMessage());
                g.tx().rollback();
                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.RELATIONSHIP_NOT_UPDATED.getMessageDefinition(
                                relationship.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        g.tx().commit();
    }

    // removeEntityFromStore
    //
    // This method will remove the entity vertex and any classifier edges and classification vertices linked off it

    synchronized void removeEntityFromStore(String entityGUID)
    {
        final String methodName = "removeEntityFromStore";

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID);

        // Only looking for non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

        if (gt.hasNext())
        {
            Vertex vertex = gt.next();

            Boolean isProxy = entityMapper.isProxy(vertex);
            if (!isProxy)
            {

                log.debug("{} found entity vertex {} to be removed", methodName, vertex);

                // Look for associated classifications.
                Iterator<Edge> classifierEdges = vertex.edges(Direction.OUT, "Classifier");
                while (classifierEdges.hasNext())
                {
                    Edge classifierEdge = classifierEdges.next();
                    Vertex classificationVertex = classifierEdge.inVertex();
                    // Get the classification's name for debug/info only
                    Classification existingClassification = new Classification();
                    try
                    {
                        classificationMapper.mapVertexToClassification(classificationVertex, existingClassification);
                    }
                    catch (Exception e)
                    {
                        log.error("{} caught exception from classification mapper for classification {}", methodName, existingClassification.getName());
                        // Nothing you can do - just keep going
                    }
                    log.debug("{} removing classification {} from entity", methodName, existingClassification.getName());
                    classifierEdge.remove();
                    classificationVertex.remove();
                }

                // Finally remove the entity vertex...
                vertex.remove();

                log.debug("{} removed entity vertex with guid {}", methodName, entityGUID);
            }
        }
        g.tx().commit();

    }


    // removeRelationshipFromStore
    synchronized void removeRelationshipFromStore(String relationshipGUID)
    {
        final String methodName = "removeRelationshipFromStore";

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGUID);
        if (edgeIt.hasNext())
        {
            Edge edge = edgeIt.next();
            log.debug("{} found existing edge {}", methodName, edge);
            edge.remove();
            log.debug("{} removed relationship edge with guid {}", methodName, relationshipGUID);
        }
        g.tx().commit();

    }

    // getRelationshipsForEntity
    synchronized List<Relationship> getRelationshipsForEntity(String entityGUID)

    throws RepositoryErrorException

    {
        final String methodName = "getRelationshipsForEntity";

        List<Relationship> relationships = new ArrayList<>();

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID);
        if (vi.hasNext())
        {
            Vertex vertex = vi.next();
            log.debug("{} found entity vertex {}", methodName, vertex);

            Iterator<Edge> edges = vertex.edges(Direction.BOTH, "Relationship");
            log.debug("{} entity has these edges {}", methodName, edges);
            while (edges.hasNext())
            {
                Edge edge = edges.next();
                log.debug("{} entity has edge {}", methodName, edge);

                Relationship relationship = new Relationship();
                relationshipMapper.mapEdgeToRelationship(edge, relationship);

                // Set the relationship ends...
                try
                {

                    vertex = edge.outVertex();

                    // Could test here whether each vertex is for a proxy, but it doesn't matter whether the vertex represents a full entity
                    // (i.e. EntityDetail of a local/reference copy) as opposed to an EntityProxy. It can be retrieved as a proxy anyway...

                    if (vertex != null)
                    {
                        log.debug("{} entity vertex {}", methodName, vertex);
                        EntityProxy entityOneProxy = new EntityProxy();
                        entityMapper.mapVertexToEntityProxy(vertex, entityOneProxy);
                        log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                        relationship.setEntityOneProxy(entityOneProxy);
                    }

                    vertex = edge.inVertex();

                    if (vertex != null)
                    {
                        log.debug("{} entity vertex {}", methodName, vertex);
                        EntityProxy entityTwoProxy = new EntityProxy();
                        entityMapper.mapVertexToEntityProxy(vertex, entityTwoProxy);
                        log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                        relationship.setEntityTwoProxy(entityTwoProxy);
                    }

                }
                catch (Exception e)
                {
                    log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                    g.tx().rollback();

                    throw new RepositoryErrorException(
                            GraphOMRSErrorCode.RELATIONSHIP_NOT_FOUND.getMessageDefinition(
                                    entityMapper.getEntityGUID(vertex), methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName, e);
                }

                relationships.add(relationship);
            }
        }

        g.tx().commit();

        return relationships;
    }



    // findEntitiesByPropertyForType
    List<EntityDetail> findEntitiesByPropertyForType(String               typeDefName,
                                                     InstanceProperties   matchProperties,
                                                     MatchCriteria        matchCriteria,
                                                     Boolean              fullMatch)

    throws InvalidParameterException

    {

        final String methodName = "findEntitiesByPropertyForType";

        List<EntityDetail> entities = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity");
        if (typeDefName != null)
        {
            gt = gt.has(PROPERTY_KEY_ENTITY_TYPE_NAME, typeDefName);
        }

        // Only accept non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);


        /*
         *
         * There are two origins of properties stored on an instance vertex in the graph -
         *   1. core properties from the audit header
         *   2. type-defined attributes from the typedef (including inheritance in the case of entities, but not relationships or classifications)
         *
         * The core property names are known (they are listed in the keys of GraphOMRSConstants.corePropertyTypes). Core properties are stored in the
         * graph (as vertex and edge properties) under their prefixed name - where the prefix depends on the type and purpose of the graph
         * element (e.g. vertex-entity, edge-relationship or vertex-classification). These are shortened to 've', 'er' and 'vc' as defined in the constants.
         * For example, for an entity the core 'createdBy' property from InstanceAuditHeader is stored under the key vecreatedBy.
         *
         * The type-defined attribute names are known from the typedef. The properties are stored in the graph as (as vertex and edge properties) under their
         * prefixed and qualified name. For example, for a Referenceable (or subtype) entity the type-defined attriute 'qualifiedName' property is stored under
         * the key 'veReferenceablexqualfiiedName'.
         *
         * There is only one namespace of properties - so a type0defined attribute should never clash with a core property. If there is a name clash between a
         * core property and a type-defined attribute it is an error in the type system (and should be fixed by an issue). This method cannot police such name
         * clashes, and tolerates them by giving precedence to the core property with the specified name.
         *
         * Match properties are specified using short (unqualified) names. Properties are stored in the graph with qualified property names - so we need to map
         * to those in order to hit the indexes and vertex/edge properties. The short names of type-defined attributes do not need to be unique - i.e. different
         * types that both define a type-defined attribute with the same (short) name. This is why the graph and indexes use the qualifiedPropertyNames.
         * The calling code supports wildcard searches (across many types) so the type identified by typeDefName may have different type defined attributes
         * to the attributes in matchProperties. Even if they match by name there is no guarantee that they are equivalent. They must be checked for both
         * property name and type. In the case that a matchProperties contains a short-named property intended for a type other than the one being searched -
         * this method checks the types match before issuing the graph traversal. This protects against type violations in the traversal. If the types do
         * not match the method reacts depending on how matchCriteria is set. If mc is ALL then no traversal is performed; if mc is ANY then a traversal is
         * performed WITHOUT the mismatched property; if mc is NONE a traversal is performed WITHOUT the mismatched property.
         *
         *
         * For the type of the entity or relationship, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
         */

        /*
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
         */

        Set<String> corePropertyNames = corePropertiesEntity.keySet();

        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

        Set<String> typeDefinedPropertyNames = qualifiedPropertyNames.keySet();


        if (matchProperties != null)
        {

            List<GraphTraversal<Vertex, Vertex>> propCriteria = new ArrayList<>();

            Iterator<String> propNames = matchProperties.getPropertyNames();

            while (propNames.hasNext())
            {


                String propName = propNames.next();


                String propNameToSearch = null;

                /*
                 * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
                 */
                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

                /*
                 * Check if this is a core property (from InstanceAuditHeader)
                 * Core properties take precedence over TDAs (in the event of a name clash)
                 */

                if (corePropertyNames.contains(propName))
                {

                    /*
                     * Treat the match property as a reference to a core property
                     *
                     * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
                     * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
                     * primitive def category of string.
                     */

                    /*
                     *  Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
                     */

                    String javaTypeForMatchProperty = null;
                    PrimitiveDefCategory mpCat;
                    InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                        mpCat = ppv.getPrimitiveDefCategory();
                        javaTypeForMatchProperty = mpCat.getJavaClassName();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                    /*
                     * This needs to be compared to the type of the core property...
                     */

                    String javaTypeForCoreProperty = corePropertyTypes.get(propName);

                    if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
                    {
                        /*
                         * If the types are the same we are good to go. There is also one case where they may differ
                         * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                         * whereas a Date match property is almost certainly going to be specified as a java.lang.Long.
                         * This should be OK - we can accept the type difference, provided we convert the match property
                         * to a Date before using it in the traversal (which is in the switch statement further on)
                         */

                        if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                                (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                        {
                            /*
                             * Types match, OK to include the property
                             */
                            propNameToSearch = PROPERTY_KEY_PREFIX_ENTITY + propName;
                            mapping = corePropertyMixedIndexMappings.get(propNameToSearch);
                        }
                        else
                        {
                            /*
                             * Match strictly for core properties because it will surface the problem - which lies at the
                             * heart of the type system, since there must be a name clash with the core properies.
                             */
                            throw new InvalidParameterException(
                                    GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                            propName,
                                            javaTypeForMatchProperty,
                                            javaTypeForCoreProperty,
                                            methodName,
                                            this.getClass().getName(),
                                            repositoryName),
                                    this.getClass().getName(),
                                    methodName,
                                    "matchProperties");
                        }
                    }

                }
                else if (typeDefinedPropertyNames.contains(propName))
                {

                    /*
                     * Treat the match property as a reference to a type-defined property. Check that it's type matches the TDA.
                     */

                    List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

                    for (TypeDefAttribute propertyDef : propertiesDef)
                    {
                        String definedPropertyName = propertyDef.getAttributeName();
                        if (definedPropertyName.equals(propName))
                        {

                            /*
                             * The match property name matches the name of a type-defined attribute
                             *
                             * Check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                             */

                            PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                            InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                            {
                                PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                                mpCat = ppv.getPrimitiveDefCategory();
                            }
                            else
                            {
                                log.debug("{} non-primitive match property {} ignored", methodName, propName);
                            }

                            PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            AttributeTypeDef atd = propertyDef.getAttributeType();
                            AttributeTypeDefCategory atdCat = atd.getCategory();
                            if (atdCat == PRIMITIVE)
                            {
                                PrimitiveDef pdef = (PrimitiveDef) atd;
                                pdCat = pdef.getPrimitiveDefCategory();
                            }

                            if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                            {
                                /*
                                 * Types match
                                 */
                                /*
                                 * Sort out the qualification and prefixing of the property name ready for graph search
                                 */
                                String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                                propNameToSearch = PROPERTY_KEY_PREFIX_ENTITY + qualifiedPropertyName;
                                mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

                            }
                            /*
                             * If types matched the code above will have set propNameToSearch. If the types did not match we should give up on this property - the search could be across a
                             * a variety of types (e.g. no type filtering is applied - and different types may have attributes with the same name but different types. If the name matches
                             * but the type doesn't then can only assume tha the match property was not intended for this type.
                             * Break out of the property for loop and drop through to catch all below
                             */
                            break;
                        }
                    }
                    /*
                     * if (!propertyFound) - The match property is not a supported, known type-defined property or does not have correct type - drop into the catch all below.
                     */

                }

                if (propNameToSearch == null)
                {

                    /*
                     * The match property is neither a core nor a type-defined property with matching name and type.
                     * If matchCriteria is ALL we need to give up at this point.
                     * If matchCriteria is ANY or NONE we can continue but just ignore this match property.
                     */
                    if (matchCriteria == MatchCriteria.ALL)
                    {
                        g.tx().rollback();
                        return null;
                    }
                    /*
                     * Skip this property but process the rest
                     */
                    //continue;

                }
                else
                {
                    /*
                     * Incorporate the property (propNameToSearch) into propCriteria for the traversal...
                     */


                    InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                    if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        // Primitives will have been stored in the graph as such
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                        PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                        Object primValue = ppv.getPrimitiveValue();
                        log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                        GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();

                        switch (pCat)
                        {

                            case OM_PRIMITIVE_TYPE_STRING:

                                // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.

                                String searchString = convertSearchStringToJanusRegex((String) primValue);
                                log.debug("{} primitive match property search string {}", methodName, searchString);

                                // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                                if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
                                {
                                    t = t.has(propNameToSearch, Text.textContainsRegex(searchString));     // for a field indexed using Text mapping use textContains or textContainsRegex
                                }
                                else
                                {
                                    if (!fullMatch)
                                    {
                                        // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                                        String ANYCHARS = ".*";
                                        t = t.has(propNameToSearch, Text.textRegex(ANYCHARS + searchString + ANYCHARS));         // for a field indexed using String mapping use textRegex
                                    }
                                    else
                                    {
                                        // Must be a full match...
                                        t = t.has(propNameToSearch, Text.textRegex(searchString));
                                    }
                                }
                                break;

                            case OM_PRIMITIVE_TYPE_DATE:
                                // If PrimitiveDefCategory is Date and this is a core property then you need to cast the Long to a Date
                                if (corePropertyNames.contains(propName))
                                {
                                    Date dateValue = new Date((Long) primValue);
                                    t = t.has(propNameToSearch, dateValue);
                                }
                                else
                                {
                                    // Can use the primitive match property value as is, since it is a Long
                                    t = t.has(propNameToSearch, primValue);
                                }
                                break;

                            default:
                                t = t.has(propNameToSearch, primValue);
                                break;

                        }
                        log.debug("{} primitive match property has property criterion {}", methodName, t);
                        propCriteria.add(t);
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                }
            }

            /*
             * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
             * between the match properties and the properties defined on the type (core or type defined). So
             * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
             * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
             * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
             * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
             * entity) so let that case continue.
             */

            if (matchCriteria != null)
            {

                switch (matchCriteria)
                {
                    case ALL:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case ANY:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case NONE:
                        GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                        t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                        gt = gt.not(t);
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;
                    default:
                        g.tx().rollback();
                        final String parameterName = "matchCriteria";

                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                parameterName);

                }
            }

        }


        while (gt.hasNext())
        {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            EntityDetail entityDetail = new EntityDetail();
            try
            {
                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy)
                {
                    entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                    entities.add(entityDetail);
                }
            }
            catch (Exception e)
            {
                log.error("{} caught exception from entity mapper, entity being ignored, {}", methodName, e.getMessage());
                // continue;
            }
        }

        g.tx().commit();

        return entities;

    }




    // findEntitiesByPropertyForTypes
    List<EntityDetail> findEntitiesByPropertyForTypes(List<String>                   entityTypeNames,
                                                      String                         filterTypeName,
                                                      Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                                      Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                                      InstanceProperties             matchProperties,
                                                      MatchCriteria                  matchCriteria)

    throws InvalidParameterException,
           RepositoryErrorException

    {

        final String methodName = "findEntitiesByPropertyForTypes";

        boolean performTypeFiltering = filterTypeName != null;

        List<EntityDetail> entities = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity");


        // Only accept non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);


        List<GraphTraversal<Vertex, Vertex>> propCriteria = new ArrayList<>();

        if (matchProperties != null)
        {

            /*
             * The caller has provided matchProperties - process the core properties and then the type defined attributes for the valid types, avoiding
             * duplication through inheritance by processing all properties for the root or filter type and only local properties for the subtypes.
             */

            /*
             * Will test against the core properties used by entities
             *
             * Short names for core properties
             */
            Set<String> corePropertyNames = corePropertiesEntity.keySet();

            Set<String> typeDefinedPropertyNames = null;
            if (!shortPropertyNameToQualifiedPropertyNames.isEmpty())
            {
                typeDefinedPropertyNames = shortPropertyNameToQualifiedPropertyNames.keySet();
            }

            /*
             * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
             */

            Iterator<String> propNames = matchProperties.getPropertyNames();
            while (propNames.hasNext())
            {
                /*
                 * For each property start with an empty map of matched prop names
                 */
                Map<String, GraphOMRSGraphFactory.MixedIndexMapping> matchedPropToMapping = new HashMap<>();

                String propName = propNames.next();

                /*
                 * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text.
                 * It is looked up for a core property, and default to String for a type-defined attribute.
                 */


                /*
                 * Check if this is a core property (from InstanceAuditHeader)
                 * Core properties take precedence over TDAs (in the event of a name clash)
                 */

                if (corePropertyNames.contains(propName))
                {
                    /*
                     * Treat the match property as a reference to a core property.
                     * Validate and add the core property key and match property value to propCriteria.
                     *
                     * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
                     * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
                     * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
                     * primitive def category of string.
                     */

                    String javaTypeForMatchProperty = null;
                    PrimitiveDefCategory mpCat;
                    InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                        mpCat = ppv.getPrimitiveDefCategory();
                        javaTypeForMatchProperty = mpCat.getJavaClassName();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                    /*
                     * This needs to be compared to the type of the core property...
                     */

                    String javaTypeForCoreProperty = corePropertyTypes.get(propName);

                    if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
                    {
                        /*
                         * If the types are the same we are good to go. There is also one case where they may differ
                         * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                         * whereas a Date match property is almost certainly going to be specified as a java.lang.Long.
                         * This should be OK - we can accept the type difference, provided we convert the match property
                         * to a Date before using it in the traversal (which is in the switch statement further on)
                         */

                        if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                                (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                        {
                            /*
                             * Types match, OK to include the property
                             */
                            String matchedPropName = PROPERTY_KEY_PREFIX_ENTITY + propName;
                            GraphOMRSGraphFactory.MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(matchedPropName);
                            matchedPropToMapping.put(matchedPropName, mapping);
                        }
                        else
                        {
                            /*
                             * Match strictly for core properties because it will surface the problem - which lies at the
                             * heart of the type system, since there must be a name clash with the core properies.
                             */
                            throw new InvalidParameterException(
                                    GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                            propName,
                                            javaTypeForMatchProperty,
                                            javaTypeForCoreProperty,
                                            methodName,
                                            this.getClass().getName(),
                                            repositoryName),
                                    this.getClass().getName(),
                                    methodName,
                                    "matchProperties");
                        }
                    }
                }

                else if (typeDefinedPropertyNames != null && typeDefinedPropertyNames.contains(propName))
                {
                    /*
                     * The match property name matches the name of one or more type-defined attribute
                     *
                     * Iterate over the matching TDAs and for each check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(propName);

                    if (qNameList != null && !qNameList.isEmpty())
                    {
                        // You could add a double check here - that there is exactly one entry in the list. It has already been checked by the caller.
                        String qualifiedName = qNameList.get(0);

                        if (qualifiedName != null)
                        {
                            /*
                             * For the qualifiedName perform type checking between the match property and TDA
                             */

                            TypeDefAttribute propertyDef = qualifiedPropertyNameToTypeDefinedAttribute.get(qualifiedName);
                            PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            AttributeTypeDef atd = propertyDef.getAttributeType();
                            AttributeTypeDefCategory atdCat = atd.getCategory();
                            if (atdCat == PRIMITIVE)
                            {
                                PrimitiveDef pdef = (PrimitiveDef) atd;
                                pdCat = pdef.getPrimitiveDefCategory();
                            }

                            PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                            InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                            {
                                PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                                mpCat = ppv.getPrimitiveDefCategory();
                            }
                            else
                            {
                                log.debug("{} non-primitive match property {} ignored", methodName, propName);
                            }

                            if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                            {
                                /*
                                 * Types match
                                 *
                                 * Qualify and prefix the property name ready for graph search
                                 */
                                String matchedPropName = PROPERTY_KEY_PREFIX_ENTITY + qualifiedName;
                                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                                matchedPropToMapping.put(matchedPropName, mapping);
                            }
                        }
                    }
                }

                /*
                 * If the matchedPropToMapping map is empty then there were no property matches. This means that
                 * the match property is not a supported, known type-defined property or does not have correct type.
                 *
                 * Otherwise iterate over the map and process each property.
                 */

                if (matchedPropToMapping.isEmpty())
                {
                    /*
                     * The match property is neither a core nor a type-defined property with matching name and type.
                     * If matchCriteria is ALL we need to give up at this point.
                     * If matchCriteria is ANY or NONE we can continue but just ignore this match property.
                     */
                    if (matchCriteria == MatchCriteria.ALL)
                    {
                        g.tx().rollback();
                        return null;
                    }
                    /*
                     * Skip this property but process the rest
                     */
                    //continue;

                }
                else
                {
                    /*
                     * There is at least one qualified property that matches the specified match property by
                     * both short name and type. If the re is exactly one, then just adding a simple  step
                     * to the overall traversal is sufficient. If there is more than one, construct a sub-traversal
                     * that can be offered up as a more complex propCriterion into propCriteria.
                     *
                     * Iterate over the map and incorporate each property (matchedPropName) into propCriteria for the traversal...
                     */

                    List<GraphTraversal<Vertex, Vertex>> localCriteria = new ArrayList<>();

                    Set<String> matchedPropNames = matchedPropToMapping.keySet();

                    Iterator<String> matchPropNameIterator = matchedPropNames.iterator();

                    while (matchPropNameIterator.hasNext())
                    {
                        String thisMatchedPropName = matchPropNameIterator.next();

                        GraphOMRSGraphFactory.MixedIndexMapping mapping = matchedPropToMapping.get(thisMatchedPropName);

                        InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                        InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                        if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                        {
                            // Primitives will have been stored in the graph as such
                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                            PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                            Object primValue = ppv.getPrimitiveValue();
                            log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();

                            switch (pCat)
                            {

                                case OM_PRIMITIVE_TYPE_STRING:

                                    // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.

                                    String searchString = convertSearchStringToJanusRegex((String) primValue);
                                    log.debug("{} primitive match property search string {}", methodName, searchString);

                                    // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                                    if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
                                    {
                                        t = t.has(thisMatchedPropName, Text.textContainsRegex(searchString));     // for a field indexed using Text mapping use textContains or textContainsRegex
                                    }
                                    else
                                    {
                                        /*
                                         * I know this looks weird - hard-coding the value of fullMatch immediately before testing it and never
                                         * using the 'false' case - but hopefully it is instructive as to the effect on the graph traversal if
                                         * in future, it becomes necessary to perform a graph traversal that does NOT perform a full match.
                                         */
                                        boolean fullMatch = true;
                                        if (!fullMatch)
                                        {
                                            // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                                            String ANYCHARS = ".*";
                                            t = t.has(thisMatchedPropName, Text.textRegex(ANYCHARS + searchString + ANYCHARS));         // for a field indexed using String mapping use textRegex
                                        }
                                        else
                                        {
                                            // Must be a full match...
                                            t = t.has(thisMatchedPropName, Text.textRegex(searchString));
                                        }
                                    }
                                    break;

                                case OM_PRIMITIVE_TYPE_DATE:
                                    // If PrimitiveDefCategory is Date and this is a core property then you need to cast the Long to a Date
                                    if (corePropertyNames.contains(propName))
                                    {
                                        Date dateValue = new Date((Long) primValue);
                                        t = t.has(thisMatchedPropName, dateValue);
                                    }
                                    else
                                    {
                                        // Can use the primitive match property value as is, since it is a Long
                                        t = t.has(thisMatchedPropName, primValue);
                                    }
                                    break;

                                default:
                                    t = t.has(thisMatchedPropName, primValue);
                                    break;

                            }
                            log.debug("{} primitive match property has property criterion {}", methodName, t);
                            localCriteria.add(t);
                        }
                        else
                        {
                            log.debug("{} non-primitive match property {} ignored", methodName, propName);
                        }
                    }

                    /* Add a criterion to the overall traversal */
                    propCriteria.add(localCriteria.get(0));

                }
            }



            /*
             * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
             * between the match properties and the properties defined on the type (core or type defined). So
             * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
             * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
             * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
             * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
             * entity) so let that case continue.
             */

            if (matchCriteria != null)
            {
                switch (matchCriteria)
                {
                    case ALL:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case ANY:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case NONE:
                        GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                        t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                        gt = gt.not(t);
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;
                    default:
                        g.tx().rollback();
                        final String parameterName = "matchCriteria";

                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                parameterName);

                }
            }
        }


        /*
         * Optionally perform type filtering
         */

        if (performTypeFiltering)
        {
            gt = gt.has(PROPERTY_KEY_ENTITY_TYPE_NAME, within(entityTypeNames));
        }


        /*
         * Iterate the traversal
         */

        while (gt.hasNext())
        {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            EntityDetail entityDetail = new EntityDetail();
            try
            {
                /*
                 * Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                 */
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy)
                {
                    entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                    entities.add(entityDetail);
                }
            }
            catch (Exception e)
            {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(
                                entityDetail.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        g.tx().commit();

        return entities;

    }






    // findEntitiesByProperty
    List<EntityDetail> findEntitiesByPropertyValueForTypes(List<String>                   entityTypeNames,
                                                           String                         filterTypeName,
                                                           Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                                           Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                                           InstanceProperties             matchProperties,
                                                           MatchCriteria                  matchCriteria)

    throws InvalidParameterException,
           RepositoryErrorException

    {

        final String methodName = "findEntitiesByProperty";

        boolean performTypeFiltering = filterTypeName != null;

        List<EntityDetail> entities = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity");


        // Only accept non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);


        List<GraphTraversal<Vertex, Vertex>> propCriteria = new ArrayList<>();

        if (matchProperties != null)
        {

            /*
             * The caller has provided matchProperties - process the core properties and then the type defined attributes for the valid types, avoiding
             * duplication through inheritance by processing all properties for the root or filter type and only local properties for the subtypes.
             */

            /*
             * Will test against the core properties used by entities
             *
             * Short names for core properties
             */
            Set<String> corePropertyNames = corePropertiesEntity.keySet();

            Set<String> typeDefinedPropertyNames = null;
            if (!shortPropertyNameToQualifiedPropertyNames.isEmpty())
            {
                typeDefinedPropertyNames = shortPropertyNameToQualifiedPropertyNames.keySet();
            }

            /*
             * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
             */

            Iterator<String> propNames = matchProperties.getPropertyNames();
            while (propNames.hasNext())
            {
                /*
                 * For each property start with an empty map of matched prop names
                 */
                Map<String, GraphOMRSGraphFactory.MixedIndexMapping> matchedPropToMapping = new HashMap<>();

                String propName = propNames.next();

                /*
                 * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text.
                 * It is looked up for a core property, and default to String for a type-defined attribute.
                 */


                /*
                 * Check if this is a core property (from InstanceAuditHeader)
                 * Core properties take precedence over TDAs (in the event of a name clash)
                 */

                if (corePropertyNames.contains(propName))
                {
                    /*
                     * Treat the match property as a reference to a core property.
                     * Validate and add the core property key and match property value to propCriteria.
                     *
                     * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
                     * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
                     * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
                     * primitive def category of string.
                     */

                    String javaTypeForMatchProperty = null;
                    PrimitiveDefCategory mpCat;
                    InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                        mpCat = ppv.getPrimitiveDefCategory();
                        javaTypeForMatchProperty = mpCat.getJavaClassName();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                    /*
                     * This needs to be compared to the type of the core property...
                     */

                    String javaTypeForCoreProperty = corePropertyTypes.get(propName);

                    if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
                    {
                        /*
                         * If the types are the same we are good to go. There is also one case where they may differ
                         * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                         * whereas a Date match property is almost certainly going to be specified as a java.lang.Long.
                         * This should be OK - we can accept the type difference, provided we convert the match property
                         * to a Date before using it in the traversal (which is in the switch statement further on)
                         */

                        if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                                (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                        {
                            /*
                             * Types match, OK to include the property
                             */
                            String matchedPropName = PROPERTY_KEY_PREFIX_ENTITY + propName;
                            GraphOMRSGraphFactory.MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(matchedPropName);
                            matchedPropToMapping.put(matchedPropName, mapping);
                        }
                        else
                        {
                            /*
                             * Match strictly for core properties because it will surface the problem - which lies at the
                             * heart of the type system, since there must be a name clash with the core properies.
                             */
                            throw new InvalidParameterException(
                                    GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                            propName,
                                            javaTypeForMatchProperty,
                                            javaTypeForCoreProperty,
                                            methodName,
                                            this.getClass().getName(),
                                            repositoryName),
                                    this.getClass().getName(),
                                    methodName,
                                    "matchProperties");
                        }
                    }
                }

                else if (typeDefinedPropertyNames != null && typeDefinedPropertyNames.contains(propName))
                {
                    /*
                     * The match property name matches the name of one or more type-defined attribute
                     *
                     * Iterate over the matching TDAs and for each check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(propName);

                    // Because this is supporting find by property value it needs to process all the qName entries in the list.
                    if (qNameList != null && !qNameList.isEmpty())
                    {
                        for (String qualifiedName : qNameList)
                        {
                            if (qualifiedName != null)
                            {
                                /*
                                 * For the qualifiedName perform type checking between the match property and TDA
                                 */

                                TypeDefAttribute propertyDef = qualifiedPropertyNameToTypeDefinedAttribute.get(qualifiedName);
                                PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                                AttributeTypeDef atd = propertyDef.getAttributeType();
                                AttributeTypeDefCategory atdCat = atd.getCategory();
                                if (atdCat == PRIMITIVE)
                                {
                                    PrimitiveDef pdef = (PrimitiveDef) atd;
                                    pdCat = pdef.getPrimitiveDefCategory();
                                }

                                PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                                InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                                InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                                if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                                {
                                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                                    mpCat = ppv.getPrimitiveDefCategory();
                                }
                                else
                                {
                                    log.debug("{} non-primitive match property {} ignored", methodName, propName);
                                }

                                if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                                {
                                    /*
                                     * Types match
                                     *
                                     * Qualify and prefix the property name ready for graph search
                                     */
                                    String matchedPropName = PROPERTY_KEY_PREFIX_ENTITY + qualifiedName;
                                    GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                                    matchedPropToMapping.put(matchedPropName, mapping);
                                }
                            }
                        }
                    }
                }

                /*
                 * If the matchedPropToMapping map is empty then there were no property matches. This means that
                 * the match property is not a supported, known type-defined property or does not have correct type.
                 *
                 * Otherwise iterate over the map and process each property.
                 */

                if (matchedPropToMapping.isEmpty())
                {
                    /*
                     * The match property is neither a core nor a type-defined property with matching name and type.
                     * If matchCriteria is ALL we need to give up at this point.
                     * If matchCriteria is ANY or NONE we can continue but just ignore this match property.
                     */
                    if (matchCriteria == MatchCriteria.ALL)
                    {
                        g.tx().rollback();
                        return null;
                    }
                    /*
                     * Skip this property but process the rest
                     */
                    //continue;

                }
                else
                {
                    /*
                     * There is at least one qualified property that matches the specified match property by
                     * both short name and type. If the re is exactly one, then just adding a simple  step
                     * to the overall traversal is sufficient. If there is more than one, construct a sub-traversal
                     * that can be offered up as a more complex propCriterion into propCriteria.
                     *
                     * Iterate over the map and incorporate each property (matchedPropName) into propCriteria for the traversal...
                     */

                    List<GraphTraversal<Vertex, Vertex>> localCriteria = new ArrayList<>();

                    Set<String> matchedPropNames = matchedPropToMapping.keySet();

                    Iterator<String> matchPropNameIterator = matchedPropNames.iterator();

                    while (matchPropNameIterator.hasNext())
                    {
                        String thisMatchedPropName = matchPropNameIterator.next();

                        GraphOMRSGraphFactory.MixedIndexMapping mapping = matchedPropToMapping.get(thisMatchedPropName);

                        InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                        InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                        if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                        {
                            // Primitives will have been stored in the graph as such
                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                            PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                            Object primValue = ppv.getPrimitiveValue();
                            log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();

                            switch (pCat)
                            {

                                case OM_PRIMITIVE_TYPE_STRING:

                                    // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.

                                    String searchString = convertSearchStringToJanusRegex((String) primValue);
                                    log.debug("{} primitive match property search string {}", methodName, searchString);

                                    // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                                    if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
                                    {
                                        t = t.has(thisMatchedPropName, Text.textContainsRegex(searchString));     // for a field indexed using Text mapping use textContains or textContainsRegex
                                    }
                                    else
                                    {
                                        /*
                                         * I know this looks weird - hard-coding the value of fullMatch immediately before testing it and never
                                         * using the 'false' case - but hopefully it is instructive as to the effect on the graph traversal if
                                         * in future, it becomes necessary to perform a graph traversal that does NOT perform a full match.
                                         */
                                        boolean fullMatch = true;
                                        if (!fullMatch)
                                        {
                                            // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                                            String ANYCHARS = ".*";
                                            t = t.has(thisMatchedPropName, Text.textRegex(ANYCHARS + searchString + ANYCHARS));         // for a field indexed using String mapping use textRegex
                                        }
                                        else
                                        {
                                            // Must be a full match...
                                            t = t.has(thisMatchedPropName, Text.textRegex(searchString));
                                        }
                                    }
                                    break;

                                case OM_PRIMITIVE_TYPE_DATE:
                                    // If PrimitiveDefCategory is Date and this is a core property then you need to cast the Long to a Date
                                    if (corePropertyNames.contains(propName))
                                    {
                                        Date dateValue = new Date((Long) primValue);
                                        t = t.has(thisMatchedPropName, dateValue);
                                    }
                                    else
                                    {
                                        // Can use the primitive match property value as is, since it is a Long
                                        t = t.has(thisMatchedPropName, primValue);
                                    }
                                    break;

                                default:
                                    t = t.has(thisMatchedPropName, primValue);
                                    break;

                            }
                            log.debug("{} primitive match property has property criterion {}", methodName, t);
                            localCriteria.add(t);
                        }
                        else
                        {
                            log.debug("{} non-primitive match property {} ignored", methodName, propName);
                        }
                    }

                    /* Add all local criteria to the overall traversal */
                    propCriteria.addAll(localCriteria);
                }
            }



            /*
             * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
             * between the match properties and the properties defined on the type (core or type defined). So
             * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
             * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
             * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
             * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
             * entity) so let that case continue.
             */

            if (matchCriteria != null)
            {
                switch (matchCriteria)
                {
                    case ALL:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case ANY:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case NONE:
                        GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                        t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                        gt = gt.not(t);
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;
                    default:
                        g.tx().rollback();
                        final String parameterName = "matchCriteria";

                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                parameterName);

                }
            }
        }

        /*
         * Optionally perform type filtering
         */

        if (performTypeFiltering)
        {
            gt = gt.has(PROPERTY_KEY_ENTITY_TYPE_NAME, within(entityTypeNames));
        }


        /*
         * Iterate the traversal
         */

        while (gt.hasNext())
        {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            EntityDetail entityDetail = new EntityDetail();
            try
            {
                /*
                 * Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                 */
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy)
                {
                    entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                    entities.add(entityDetail);
                }
            }
            catch (Exception e)
            {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(
                                entityDetail.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        g.tx().commit();

        return entities;

    }




    // findRelationshipsByPropertyForType
    List<Relationship> findRelationshipsByPropertyForType(String               typeDefName,
                                                          InstanceProperties   matchProperties,
                                                          MatchCriteria        matchCriteria,
                                                          Boolean              fullMatch)

    throws InvalidParameterException,
           RepositoryErrorException

    {

        final String methodName = "findRelationshipsByPropertyForType";

        List<Relationship> relationships = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Edge, Edge> gt = g.E().hasLabel("Relationship");
        if (typeDefName != null)
        {
            gt = gt.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, typeDefName);
        }


        /*
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
         */

        Set<String> corePropertyNames = corePropertiesRelationship.keySet();

        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

        Set<String> typeDefinedPropertyNames = qualifiedPropertyNames.keySet();


        if (matchProperties != null)
        {

            List<GraphTraversal<Edge, Edge>> propCriteria = new ArrayList<>();

            Iterator<String> propNames = matchProperties.getPropertyNames();

            while (propNames.hasNext())
            {

                String propName = propNames.next();

                String propNameToSearch = null;

                /*
                 * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
                 */
                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

                /*
                 * Check if this is a core property (from InstanceAuditHeader)
                 * Core properties take precedence over TDAs (in the event of a name clash)
                 */

                if (corePropertyNames.contains(propName))
                {

                    /*
                     * Treat the match property as a reference to a core property
                     *
                     * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
                     * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
                     * primitive def category of string.
                     */

                    /*
                     *  Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
                     */

                    String javaTypeForMatchProperty = null;
                    PrimitiveDefCategory mpCat;
                    InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                        mpCat = ppv.getPrimitiveDefCategory();
                        javaTypeForMatchProperty = mpCat.getJavaClassName();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                    /*
                     * This needs to be compared to the type of the core property...
                     */

                    String javaTypeForCoreProperty = corePropertyTypes.get(propName);

                    if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
                    {
                        /*
                         * If the types are the same we are good to go. There is also one case where they may differ
                         * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                         * whereas a Date match property is almost certainly going to be specified as a java.lang.Long.
                         * This should be OK - we can accept the type difference, provided we convert the match property
                         * to a Date before using it in the traversal (which is in the switch statement further on)
                         */

                        if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                                (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                        {
                            /*
                             * Types match, OK to include the property
                             */
                            propNameToSearch = PROPERTY_KEY_PREFIX_RELATIONSHIP + propName;
                            mapping = corePropertyMixedIndexMappings.get(propNameToSearch);
                        }
                        else
                        {
                            /*
                             * Match strictly for core properties because it will surface the problem - which lies at the
                             * heart of the type system, since there must be a name clash with the core properies.
                             */
                            throw new InvalidParameterException(
                                    GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                            propName,
                                            javaTypeForMatchProperty,
                                            javaTypeForCoreProperty,
                                            methodName,
                                            this.getClass().getName(),
                                            repositoryName),
                                    this.getClass().getName(),
                                    methodName,
                                    "matchProperties");
                        }
                    }

                }
                else if (typeDefinedPropertyNames.contains(propName))
                {

                    /*
                     * Treat the match property as a reference to a type-defined property. Check that it's type matches the TDA.
                     */

                    List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

                    for (TypeDefAttribute propertyDef : propertiesDef)
                    {
                        String definedPropertyName = propertyDef.getAttributeName();
                        if (definedPropertyName.equals(propName))
                        {

                            /*
                             * The match property name matches the name of a type-defined attribute
                             *
                             * Check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                             */

                            PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                            InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                            {
                                PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                                mpCat = ppv.getPrimitiveDefCategory();
                            }
                            else
                            {
                                log.debug("{} non-primitive match property {} ignored", methodName, propName);
                            }

                            PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            AttributeTypeDef atd = propertyDef.getAttributeType();
                            AttributeTypeDefCategory atdCat = atd.getCategory();
                            if (atdCat == PRIMITIVE)
                            {
                                PrimitiveDef pdef = (PrimitiveDef) atd;
                                pdCat = pdef.getPrimitiveDefCategory();
                            }

                            if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                            {
                                /*
                                 * Types match
                                 */
                                /*
                                 * Sort out the qualification and prefixing of the property name ready for graph search
                                 */
                                String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                                propNameToSearch = PROPERTY_KEY_PREFIX_RELATIONSHIP + qualifiedPropertyName;
                                mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

                            }
                            /*
                             * If types matched the code above will have set propNameToSearch. If the types did not match we should give up on this property - the search could be across a
                             * a variety of types (e.g. no type filtering is applied - and different types may have attributes with the same name but different types. If the name matches
                             * but the type doesn't then can only assume tha the match property was not intended for this type.
                             * Break out of the property for loop and drop through to catch all below
                             */
                            break;
                        }
                    }
                    /*
                     * if (!propertyFound) - The match property is not a supported, known type-defined property or does not have correct type - drop into the catch all below.
                     */

                }

                if (propNameToSearch == null)
                {

                    /*
                     * The match property is neither a core nor a type-defined property with matching name and type.
                     * If matchCriteria is ALL we need to give up at this point.
                     * If matchCriteria is ANY or NONE we can continue but just ignore this match property.
                     */
                    if (matchCriteria == MatchCriteria.ALL)
                    {
                        g.tx().rollback();
                        return null;
                    }
                    /*
                     * Skip this property but process the rest
                     */
                    //continue;

                }
                else
                {
                    /*
                     * Incorporate the property (propNameToSearch) into propCriteria for the traversal...
                     */


                    InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                    if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        // Primitives will have been stored in the graph as such
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                        PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                        Object primValue = ppv.getPrimitiveValue();
                        log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                        GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();

                        switch (pCat)
                        {

                            case OM_PRIMITIVE_TYPE_STRING:

                                // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.

                                String searchString = convertSearchStringToJanusRegex((String) primValue);
                                log.debug("{} primitive match property search string {}", methodName, searchString);

                                // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                                if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
                                {
                                    t = t.has(propNameToSearch, Text.textContainsRegex(searchString));     // for a field indexed using Text mapping use textContains or textContainsRegex
                                }
                                else
                                {
                                    if (!fullMatch)
                                    {
                                        // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                                        String ANYCHARS = ".*";
                                        t = t.has(propNameToSearch, Text.textRegex(ANYCHARS + searchString + ANYCHARS));         // for a field indexed using String mapping use textRegex
                                    }
                                    else
                                    {
                                        // Must be a full match...
                                        t = t.has(propNameToSearch, Text.textRegex(searchString));
                                    }
                                }
                                break;

                            case OM_PRIMITIVE_TYPE_DATE:
                                // If PrimitiveDefCategory is Date and this is a core property then you need to cast the Long to a Date
                                if (corePropertyNames.contains(propName))
                                {
                                    Date dateValue = new Date((Long) primValue);
                                    t = t.has(propNameToSearch, dateValue);
                                }
                                else
                                {
                                    // Can use the primitive match property value as is, since it is a Long
                                    t = t.has(propNameToSearch, primValue);
                                }
                                break;

                            default:
                                t = t.has(propNameToSearch, primValue);
                                break;

                        }
                        log.debug("{} primitive match property has property criterion {}", methodName, t);
                        propCriteria.add(t);
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                }
            }

            /*
             * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
             * between the match properties and the properties defined on the type (core or type defined). So
             * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
             * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
             * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
             * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
             * entity) so let that case continue.
             */


            if (matchCriteria != null)
            {
                switch (matchCriteria)
                {
                    case ALL:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case ANY:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case NONE:
                        GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
                        t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                        gt = gt.not(t);
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;
                    default:
                        g.tx().rollback();
                        final String parameterName = "matchCriteria";

                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                parameterName);

                }
            }
        }

        /*
         * Iterate the traversal
         */

        while (gt.hasNext())
        {
            Edge edge = gt.next();
            log.debug("{} found edge {}", methodName, edge);
            Relationship relationship = new Relationship();
            try
            {
                relationshipMapper.mapEdgeToRelationship(edge, relationship);

                // Set the relationship ends...

                Vertex vertexOne = edge.outVertex();
                Vertex vertexTwo = edge.inVertex();

                // Doesn't matter whether vertices represent proxy entities or full entities - retrieve the entities as proxies
                if (vertexOne != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexOne);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexOne, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }
                if (vertexTwo != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexTwo);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexTwo, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            }
            catch (Exception e)
            {
                log.error("{} Caught exception from relationship or entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(
                                relationship.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }

            relationships.add(relationship);
        }

        g.tx().commit();

        return relationships;


    }



    // findRelationshipsByProperty
    List<Relationship> findRelationshipsByPropertyForTypes(List<String>                   relationshipTypeNames,
                                                           String                         filterTypeName,
                                                           Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                                           Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                                           InstanceProperties             matchProperties,
                                                           MatchCriteria                  matchCriteria)

    throws InvalidParameterException,
           RepositoryErrorException

    {

        final String methodName = "findRelationshipsByProperty";

        boolean performTypeFiltering = filterTypeName != null;

        List<Relationship> relationships = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Edge, Edge> gt = g.E().hasLabel("Relationship");

        List<GraphTraversal<Edge, Edge>> propCriteria = new ArrayList<>();

        if (matchProperties != null)
        {

            /*
             * The caller has provided matchProperties - process the core properties and then the type defined attributes for the valid types, avoiding
             * duplication through inheritance by processing all properties for the root or filter type and only local properties for the subtypes.
             */

            /*
             * Will test against the core properties used by entities
             *
             * Short names for core properties
             */
            Set<String> corePropertyNames = corePropertiesRelationship.keySet();

            Set<String> typeDefinedPropertyNames = null;
            if (!shortPropertyNameToQualifiedPropertyNames.isEmpty())
            {
                typeDefinedPropertyNames = shortPropertyNameToQualifiedPropertyNames.keySet();
            }

            /*
             * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
             */

            Iterator<String> propNames = matchProperties.getPropertyNames();
            while (propNames.hasNext())
            {
                /*
                 * For each property start with an empty map of matched prop names
                 */
                Map<String, GraphOMRSGraphFactory.MixedIndexMapping> matchedPropToMapping = new HashMap<>();

                String propName = propNames.next();

                /*
                 * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text.
                 * It is looked up for a core property, and default to String for a type-defined attribute.
                 */


                /*
                 * Check if this is a core property (from InstanceAuditHeader)
                 * Core properties take precedence over TDAs (in the event of a name clash)
                 */

                if (corePropertyNames.contains(propName))
                {
                    /*
                     * Treat the match property as a reference to a core property.
                     * Validate and add the core property key and match property value to propCriteria.
                     *
                     * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
                     * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
                     * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
                     * primitive def category of string.
                     */

                    String javaTypeForMatchProperty = null;
                    PrimitiveDefCategory mpCat;
                    InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                        mpCat = ppv.getPrimitiveDefCategory();
                        javaTypeForMatchProperty = mpCat.getJavaClassName();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                    /*
                     * This needs to be compared to the type of the core property...
                     */

                    String javaTypeForCoreProperty = corePropertyTypes.get(propName);

                    if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
                    {
                        /*
                         * If the types are the same we are good to go. There is also one case where they may differ
                         * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                         * whereas a Date match property is almost certainly going to be specified as a java.lang.Long.
                         * This should be OK - we can accept the type difference, provided we convert the match property
                         * to a Date before using it in the traversal (which is in the switch statement further on)
                         */

                        if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                                (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                        {
                            /*
                             * Types match, OK to include the property
                             */
                            String matchedPropName = PROPERTY_KEY_PREFIX_RELATIONSHIP + propName;
                            GraphOMRSGraphFactory.MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(matchedPropName);
                            matchedPropToMapping.put(matchedPropName, mapping);
                        }
                        else
                        {
                            /*
                             * Match strictly for core properties because it will surface the problem - which lies at the
                             * heart of the type system, since there must be a name clash with the core properies.
                             */
                            throw new InvalidParameterException(
                                    GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                            propName,
                                            javaTypeForMatchProperty,
                                            javaTypeForCoreProperty,
                                            methodName,
                                            this.getClass().getName(),
                                            repositoryName),
                                    this.getClass().getName(),
                                    methodName,
                                    "matchProperties");
                        }
                    }
                }

                else if (typeDefinedPropertyNames != null && typeDefinedPropertyNames.contains(propName))
                {
                    /*
                     * The match property name matches the name of one or more type-defined attribute
                     *
                     * Iterate over the matching TDAs and for each check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(propName);

                    if (qNameList != null && !qNameList.isEmpty())
                    {
                        // You could add a double check here - that there is exactly one entry in the list. It has already been checked by the caller.
                        String qualifiedName = qNameList.get(0);

                        if (qualifiedName != null)
                        {
                            /*
                             * For the qualifiedName perform type checking between the match property and TDA
                             */

                            TypeDefAttribute propertyDef = qualifiedPropertyNameToTypeDefinedAttribute.get(qualifiedName);
                            PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            AttributeTypeDef atd = propertyDef.getAttributeType();
                            AttributeTypeDefCategory atdCat = atd.getCategory();
                            if (atdCat == PRIMITIVE)
                            {
                                PrimitiveDef pdef = (PrimitiveDef) atd;
                                pdCat = pdef.getPrimitiveDefCategory();
                            }

                            PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                            InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                            {
                                PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                                mpCat = ppv.getPrimitiveDefCategory();
                            }
                            else
                            {
                                log.debug("{} non-primitive match property {} ignored", methodName, propName);
                            }

                            if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                            {
                                /*
                                 * Types match
                                 *
                                 * Qualify and prefix the property name ready for graph search
                                 */
                                String matchedPropName = PROPERTY_KEY_PREFIX_RELATIONSHIP + qualifiedName;
                                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                                matchedPropToMapping.put(matchedPropName, mapping);
                            }
                        }
                    }
                }

                /*
                 * If the matchedPropToMapping map is empty then there were no property matches. This means that
                 * the match property is not a supported, known type-defined property or does not have correct type.
                 *
                 * Otherwise iterate over the map and process each property.
                 */

                if (matchedPropToMapping.isEmpty())
                {
                    /*
                     * The match property is neither a core nor a type-defined property with matching name and type.
                     * If matchCriteria is ALL we need to give up at this point.
                     * If matchCriteria is ANY or NONE we can continue but just ignore this match property.
                     */
                    if (matchCriteria == MatchCriteria.ALL)
                    {
                        g.tx().rollback();
                        return null;
                    }
                    /*
                     * Skip this property but process the rest
                     */
                    //continue;

                }
                else
                {
                    /*
                     * There is at least one qualified property that matches the specified match property by
                     * both short name and type. If the re is exactly one, then just adding a simple  step
                     * to the overall traversal is sufficient. If there is more than one, construct a sub-traversal
                     * that can be offered up as a more complex propCriterion into propCriteria.
                     *
                     * Iterate over the map and incorporate each property (matchedPropName) into propCriteria for the traversal...
                     */

                    List<GraphTraversal<Edge, Edge>> localCriteria = new ArrayList<>();

                    Set<String> matchedPropNames = matchedPropToMapping.keySet();

                    Iterator<String> matchPropNameIterator = matchedPropNames.iterator();

                    while (matchPropNameIterator.hasNext())
                    {
                        String thisMatchedPropName = matchPropNameIterator.next();

                        GraphOMRSGraphFactory.MixedIndexMapping mapping = matchedPropToMapping.get(thisMatchedPropName);

                        InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                        InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                        if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                        {
                            // Primitives will have been stored in the graph as such
                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                            PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                            Object primValue = ppv.getPrimitiveValue();
                            log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();

                            switch (pCat)
                            {

                                case OM_PRIMITIVE_TYPE_STRING:

                                    // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.

                                    String searchString = convertSearchStringToJanusRegex((String) primValue);
                                    log.debug("{} primitive match property search string {}", methodName, searchString);

                                    // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                                    if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
                                    {
                                        t = t.has(thisMatchedPropName, Text.textContainsRegex(searchString));     // for a field indexed using Text mapping use textContains or textContainsRegex
                                    }
                                    else
                                    {
                                        /*
                                         * I know this looks weird - hard-coding the value of fullMatch immediately before testing it and never
                                         * using the 'false' case - but hopefully it is instructive as to the effect on the graph traversal if
                                         * in future, it becomes necessary to perform a graph traversal that does NOT perform a full match.
                                         */
                                        boolean fullMatch = true;
                                        if (!fullMatch)
                                        {
                                            // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                                            String ANYCHARS = ".*";
                                            t = t.has(thisMatchedPropName, Text.textRegex(ANYCHARS + searchString + ANYCHARS));         // for a field indexed using String mapping use textRegex
                                        }
                                        else
                                        {
                                            // Must be a full match...
                                            t = t.has(thisMatchedPropName, Text.textRegex(searchString));
                                        }
                                    }
                                    break;

                                case OM_PRIMITIVE_TYPE_DATE:
                                    // If PrimitiveDefCategory is Date and this is a core property then you need to cast the Long to a Date
                                    if (corePropertyNames.contains(propName))
                                    {
                                        Date dateValue = new Date((Long) primValue);
                                        t = t.has(thisMatchedPropName, dateValue);
                                    }
                                    else
                                    {
                                        // Can use the primitive match property value as is, since it is a Long
                                        t = t.has(thisMatchedPropName, primValue);
                                    }
                                    break;

                                default:
                                    t = t.has(thisMatchedPropName, primValue);
                                    break;

                            }
                            log.debug("{} primitive match property has property criterion {}", methodName, t);
                            localCriteria.add(t);
                        }
                        else
                        {
                            log.debug("{} non-primitive match property {} ignored", methodName, propName);
                        }
                    }

                    /* Add a criterion to the overall traversal */
                    propCriteria.add(localCriteria.get(0));

                }
            }



            /*
             * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
             * between the match properties and the properties defined on the type (core or type defined). So
             * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
             * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
             * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
             * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
             * entity) so let that case continue.
             */

            if (matchCriteria != null)
            {
                switch (matchCriteria)
                {
                    case ALL:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case ANY:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case NONE:
                        GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
                        t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                        gt = gt.not(t);
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;
                    default:
                        g.tx().rollback();
                        final String parameterName = "matchCriteria";

                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                parameterName);

                }
            }
        }


        /*
         * Optionally perform type filtering
         */

        if (performTypeFiltering)
        {
            gt = gt.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, within(relationshipTypeNames));
        }


        /*
         * Iterate the traversal
         */

        while (gt.hasNext())
        {
            Edge edge = gt.next();
            log.debug("{} found edge {}", methodName, edge);
            Relationship relationship = new Relationship();
            try
            {
                relationshipMapper.mapEdgeToRelationship(edge, relationship);

                // Set the relationship ends...

                Vertex vertexOne = edge.outVertex();
                Vertex vertexTwo = edge.inVertex();

                // Doesn't matter whether vertices represent proxy entities or full entities - retrieve the entities as proxies
                if (vertexOne != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexOne);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexOne, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }
                if (vertexTwo != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexTwo);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexTwo, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            }
            catch (Exception e)
            {
                log.error("{} Caught exception from relationship or entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(
                                relationship.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }

            relationships.add(relationship);
        }

        g.tx().commit();

        return relationships;

    }




    // findRelationshipsByPropertyValueForTypes
    List<Relationship> findRelationshipsByPropertyValueForTypes(List<String>                   validTypeNames,
                                                                String                         filterTypeName,
                                                                Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                                                Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                                                InstanceProperties             matchProperties,
                                                                MatchCriteria                  matchCriteria)

    throws InvalidParameterException,
           RepositoryErrorException

    {

        final String methodName = "findRelationshipsByPropertyValueForTypes";

        boolean performTypeFiltering = filterTypeName != null;

        List<Relationship> relationships = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Edge, Edge> gt = g.E().hasLabel("Relationship");

        List<GraphTraversal<Edge, Edge>> propCriteria = new ArrayList<>();

        if (matchProperties != null)
        {

            /*
             * The caller has provided matchProperties - process the core properties and then the type defined attributes for the valid types, avoiding
             * duplication through inheritance by processing all properties for the root or filter type and only local properties for the subtypes.
             */

            /*
             * Will test against the core properties used by entities
             *
             * Short names for core properties
             */
            Set<String> corePropertyNames = corePropertiesRelationship.keySet();

            Set<String> typeDefinedPropertyNames = null;
            if (!shortPropertyNameToQualifiedPropertyNames.isEmpty())
            {
                typeDefinedPropertyNames = shortPropertyNameToQualifiedPropertyNames.keySet();
            }

            /*
             * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
             */

            Iterator<String> propNames = matchProperties.getPropertyNames();
            while (propNames.hasNext())
            {
                /*
                 * For each property start with an empty map of matched prop names
                 */
                Map<String, GraphOMRSGraphFactory.MixedIndexMapping> matchedPropToMapping = new HashMap<>();

                String propName = propNames.next();

                /*
                 * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text.
                 * It is looked up for a core property, and default to String for a type-defined attribute.
                 */


                /*
                 * Check if this is a core property (from InstanceAuditHeader)
                 * Core properties take precedence over TDAs (in the event of a name clash)
                 */

                if (corePropertyNames.contains(propName))
                {
                    /*
                     * Treat the match property as a reference to a core property.
                     * Validate and add the core property key and match property value to propCriteria.
                     *
                     * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
                     * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
                     * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
                     * primitive def category of string.
                     */

                    String javaTypeForMatchProperty = null;
                    PrimitiveDefCategory mpCat;
                    InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                    InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                        mpCat = ppv.getPrimitiveDefCategory();
                        javaTypeForMatchProperty = mpCat.getJavaClassName();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                    /*
                     * This needs to be compared to the type of the core property...
                     */

                    String javaTypeForCoreProperty = corePropertyTypes.get(propName);

                    if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
                    {
                        /*
                         * If the types are the same we are good to go. There is also one case where they may differ
                         * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                         * whereas a Date match property is almost certainly going to be specified as a java.lang.Long.
                         * This should be OK - we can accept the type difference, provided we convert the match property
                         * to a Date before using it in the traversal (which is in the switch statement further on)
                         */

                        if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                                (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                        {
                            /*
                             * Types match, OK to include the property
                             */
                            String matchedPropName = PROPERTY_KEY_PREFIX_RELATIONSHIP + propName;
                            GraphOMRSGraphFactory.MixedIndexMapping mapping = corePropertyMixedIndexMappings.get(matchedPropName);
                            matchedPropToMapping.put(matchedPropName, mapping);
                        }
                        else
                        {
                            /*
                             * Match strictly for core properties because it will surface the problem - which lies at the
                             * heart of the type system, since there must be a name clash with the core properies.
                             */
                            throw new InvalidParameterException(
                                    GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                            propName,
                                            javaTypeForMatchProperty,
                                            javaTypeForCoreProperty,
                                            methodName,
                                            this.getClass().getName(),
                                            repositoryName),
                                    this.getClass().getName(),
                                    methodName,
                                    "matchProperties");
                        }
                    }
                }

                else if (typeDefinedPropertyNames != null && typeDefinedPropertyNames.contains(propName))
                {
                    /*
                     * The match property name matches the name of one or more type-defined attribute
                     *
                     * Iterate over the matching TDAs and for each check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(propName);

                    // Because this is supporting find by property value it needs to process all the qName entries in the list.
                    if (qNameList != null && !qNameList.isEmpty())
                    {
                        for (String qualifiedName : qNameList)
                        {
                            if (qualifiedName != null)
                            {
                                /*
                                 * For the qualifiedName perform type checking between the match property and TDA
                                 */

                                TypeDefAttribute propertyDef = qualifiedPropertyNameToTypeDefinedAttribute.get(qualifiedName);
                                PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                                AttributeTypeDef atd = propertyDef.getAttributeType();
                                AttributeTypeDefCategory atdCat = atd.getCategory();
                                if (atdCat == PRIMITIVE)
                                {
                                    PrimitiveDef pdef = (PrimitiveDef) atd;
                                    pdCat = pdef.getPrimitiveDefCategory();
                                }

                                PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                                InstancePropertyValue mpv = matchProperties.getPropertyValue(propName);
                                InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                                if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                                {
                                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                                    mpCat = ppv.getPrimitiveDefCategory();
                                }
                                else
                                {
                                    log.debug("{} non-primitive match property {} ignored", methodName, propName);
                                }

                                if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                                {
                                    /*
                                     * Types match
                                     *
                                     * Qualify and prefix the property name ready for graph search
                                     */
                                    String matchedPropName = PROPERTY_KEY_PREFIX_RELATIONSHIP + qualifiedName;
                                    GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                                    matchedPropToMapping.put(matchedPropName, mapping);
                                }
                            }
                        }
                    }
                }

                /*
                 * If the matchedPropToMapping map is empty then there were no property matches. This means that
                 * the match property is not a supported, known type-defined property or does not have correct type.
                 *
                 * Otherwise iterate over the map and process each property.
                 */

                if (matchedPropToMapping.isEmpty())
                {
                    /*
                     * The match property is neither a core nor a type-defined property with matching name and type.
                     * If matchCriteria is ALL we need to give up at this point.
                     * If matchCriteria is ANY or NONE we can continue but just ignore this match property.
                     */
                    if (matchCriteria == MatchCriteria.ALL)
                    {
                        g.tx().rollback();
                        return null;
                    }
                    /*
                     * Skip this property but process the rest
                     */
                    //continue;

                }
                else
                {
                    /*
                     * There is at least one qualified property that matches the specified match property by
                     * both short name and type. If the re is exactly one, then just adding a simple  step
                     * to the overall traversal is sufficient. If there is more than one, construct a sub-traversal
                     * that can be offered up as a more complex propCriterion into propCriteria.
                     *
                     * Iterate over the map and incorporate each property (matchedPropName) into propCriteria for the traversal...
                     */

                    List<GraphTraversal<Edge, Edge>> localCriteria = new ArrayList<>();

                    Set<String> matchedPropNames = matchedPropToMapping.keySet();

                    Iterator<String> matchPropNameIterator = matchedPropNames.iterator();

                    while (matchPropNameIterator.hasNext())
                    {
                        String thisMatchedPropName = matchPropNameIterator.next();

                        GraphOMRSGraphFactory.MixedIndexMapping mapping = matchedPropToMapping.get(thisMatchedPropName);

                        InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                        InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                        if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                        {
                            // Primitives will have been stored in the graph as such
                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                            PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                            Object primValue = ppv.getPrimitiveValue();
                            log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();

                            switch (pCat)
                            {

                                case OM_PRIMITIVE_TYPE_STRING:

                                    // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.

                                    String searchString = convertSearchStringToJanusRegex((String) primValue);
                                    log.debug("{} primitive match property search string {}", methodName, searchString);

                                    // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                                    if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
                                    {
                                        t = t.has(thisMatchedPropName, Text.textContainsRegex(searchString));     // for a field indexed using Text mapping use textContains or textContainsRegex
                                    }
                                    else
                                    {
                                        /*
                                         * I know this looks weird - hard-coding the value of fullMatch immediately before testing it and never
                                         * using the 'false' case - but hopefully it is instructive as to the effect on the graph traversal if
                                         * in future, it becomes necessary to perform a graph traversal that does NOT perform a full match.
                                         */
                                        boolean fullMatch = true;
                                        if (!fullMatch)
                                        {
                                            // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                                            String ANYCHARS = ".*";
                                            t = t.has(thisMatchedPropName, Text.textRegex(ANYCHARS + searchString + ANYCHARS));         // for a field indexed using String mapping use textRegex
                                        }
                                        else
                                        {
                                            // Must be a full match...
                                            t = t.has(thisMatchedPropName, Text.textRegex(searchString));
                                        }
                                    }
                                    break;

                                case OM_PRIMITIVE_TYPE_DATE:
                                    // If PrimitiveDefCategory is Date and this is a core property then you need to cast the Long to a Date
                                    if (corePropertyNames.contains(propName))
                                    {
                                        Date dateValue = new Date((Long) primValue);
                                        t = t.has(thisMatchedPropName, dateValue);
                                    }
                                    else
                                    {
                                        // Can use the primitive match property value as is, since it is a Long
                                        t = t.has(thisMatchedPropName, primValue);
                                    }
                                    break;

                                default:
                                    t = t.has(thisMatchedPropName, primValue);
                                    break;

                            }
                            log.debug("{} primitive match property has property criterion {}", methodName, t);
                            localCriteria.add(t);
                        }
                        else
                        {
                            log.debug("{} non-primitive match property {} ignored", methodName, propName);
                        }
                    }

                    /* Add all local criteria to the overall traversal */
                    propCriteria.addAll(localCriteria);
                }
            }



            /*
             * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
             * between the match properties and the properties defined on the type (core or type defined). So
             * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
             * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
             * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
             * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
             * entity) so let that case continue.
             */

            if (matchCriteria != null)
            {
                switch (matchCriteria)
                {
                    case ALL:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case ANY:
                        if (propCriteria.isEmpty())
                        {
                            g.tx().rollback();
                            return null;
                        }
                        else
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                            log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        }
                        break;
                    case NONE:
                        GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
                        t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                        gt = gt.not(t);
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;
                    default:
                        g.tx().rollback();
                        final String parameterName = "matchCriteria";

                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                parameterName);

                }
            }
        }


        /*
         * Optionally perform type filtering
         */

        if (performTypeFiltering)
        {
            gt = gt.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, within(validTypeNames));
        }


        /*
         * Iterate the traversal
         */
        while (gt.hasNext())
        {
            Edge edge = gt.next();
            log.debug("{} found edge {}", methodName, edge);
            Relationship relationship = new Relationship();
            try
            {
                relationshipMapper.mapEdgeToRelationship(edge, relationship);

                // Set the relationship ends...

                Vertex vertexOne = edge.outVertex();
                Vertex vertexTwo = edge.inVertex();

                // Doesn't matter whether vertices represent proxy entities or full entities - retrieve the entities as proxies
                if (vertexOne != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexOne);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexOne, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }
                if (vertexTwo != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexTwo);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexTwo, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            }
            catch (Exception e)
            {
                log.error("{} Caught exception from relationship or entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(
                                relationship.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }

            relationships.add(relationship);
        }

        g.tx().commit();

        return relationships;

    }



    /*
     * This method converts an Egeria regex into an expression that can be used with the JanusGraph
     * text predicates.
     */
    private String convertSearchStringToJanusRegex(String str)
    {

        if (str == null || str.length() == 0)
            return null;

        boolean caseInsensitive = false;

        if (str.startsWith("(?i)"))
        {
            caseInsensitive = true;
            str = str.substring(4);
        }

        boolean prefixed = false;

        /*
         * A string may consist only of '.*' in which case it is referred to as suffixed rather than prefixed
         * This is to ensure that we don't double the prefix/suffix in the resultant string
         */

        boolean suffixed = str.endsWith(".*");
        if (!suffixed || str.length() > 2)
        {
            prefixed = str.startsWith(".*");
        }

        String innerString = str;
        if (suffixed)
        {
            innerString = innerString.substring(0, innerString.length() - 2);
        }
        if (prefixed)
        {
            innerString = innerString.substring(2);
        }
        if (innerString.length() == 0)
        {
            /*
             * There is nothing left after removing any suffix and prefix - return the original string
             */
            return str;
        }

        /*
         * There is at least some some substance to the inner string.
         * Check whether it has been entirely literalised
         */

        String outputString;

        boolean literalized = false;
        if (repositoryHelper.isExactMatchRegex(innerString))
        {
            if (innerString.length() == 4)
            {
                /*
                 * Although the innerString is wrapped as by exact match qualifiers, there is nothing else
                 */
                return null;
            }
            else
            {
                literalized = true;
                innerString = innerString.substring(2, innerString.length() - 2);
            }
        }

        /*
         * innerString now contains just the string that may need to be made case-insensitive and/or literalized
         */
        if (!caseInsensitive && !literalized)
        {
            /*
             * There is nothing more to do - just use the innerString as-is...
             */
            outputString = innerString;
        }
        else
        {
            /*
             * There is at least some work to do; characters may need to be literalized and/or makde case-insensitive
             */
            StringBuilder outputStringBldr = new StringBuilder();

            // Process chars
            for (int i = 0; i < innerString.length(); i++)
            {
                Character c = innerString.charAt(i);
                /*
                 * No need to escape a '-' char as it is only significant if inside '[]' brackets, and these will be escaped,
                 * so the '-' character has no special meaning
                 */

                /*
                 * Handle case where neither literalized nor case-insensitive are active
                 */
                if (!literalized && !caseInsensitive)
                {
                    outputStringBldr.append(c);
                }

                else
                {
                    /*
                     * At least one of literalized or caseInsensitive is active
                     */

                    /*
                     * Handle special chars - disjoint from alphas
                     */
                    switch (c)
                    {
                        case '.':
                        case '[':
                        case ']':
                        case '^':
                        case '*':
                        case '(':
                        case ')':
                        case '$':
                        case '{':
                        case '}':
                        case '|':
                        case '+':
                        case '?':
                        case '#':
                        case '&':
                        case '<':
                        case '\\':  // single backslash escaped for Java
                            if (literalized)
                            {
                                outputStringBldr.append('\\').append(c);
                            }
                            else
                            {
                                outputStringBldr.append(c);
                            }
                            continue;  // process the next character
                    }

                    /*
                     * Handle alphas - disjoint from specials
                     */
                    if (c >= 'a' && c <= 'z')
                    {
                        if (caseInsensitive)
                        {
                            outputStringBldr.append("[").append(c).append(Character.toUpperCase(c)).append("]");
                        }
                        else
                        {
                            outputStringBldr.append(c);
                        }
                    }
                    else if (c >= 'A' && c <= 'Z')
                    {
                        if (caseInsensitive)
                        {
                            outputStringBldr.append("[").append(Character.toLowerCase(c)).append(c).append("]");
                        }
                        else
                        {
                            outputStringBldr.append(c);
                        }
                    }
                    else
                    {
                        /*
                         * The character is not special, not an alpha, just append it...
                         */
                        outputStringBldr.append(c);
                    }
                }
            }
            outputString = outputStringBldr.toString();
        }


        /*
         * Re-frame depending on whether suffixed or prefixed
         */
        if (suffixed)
        {
            outputString = outputString + ".*";
        }
        if (prefixed)
        {
            outputString = ".*" + outputString;
        }

        return outputString;

    }





    /*
     * If performTypeFiltering is true iterate over the types in validTypeNames and construct a MatchProperties containing all their string
     * properties. Otherwise (performTypeFiltering is false) include all ENTITY types.
     *
     * For each searchable type convert searchCriteria into matchProperties
     */
    InstanceProperties constructMatchPropertiesForSearchCriteriaForTypes(TypeDefCategory                 category,
                                                                         String                          searchCriteria,
                                                                         String                          filterTypeName,
                                                                         List<String>                    validTypeNames)


    {

        final String methodName = "constructMatchPropertiesForSearchCriteria";

        boolean performTypeFiltering = filterTypeName != null;

        InstanceProperties stringMatchProperties = new InstanceProperties();

        /* Include any string-based core properties, apart from typeName since that is either specified (by typeGUID) or fully wild.
         * The core properties are not prefixed at this stage - this is still in matchProperty space - i.e. using raw short names. They
         * will be prefixed in the finder method that actually looks for matching elements in the graph.
         */


        /*
         * Only include the core properties for the type category
         */

        Set<String> relevantCoreProperties = new HashSet<>();
        switch (category)
        {
            case ENTITY_DEF:
                relevantCoreProperties = corePropertiesEntity.keySet();
                break;
            case RELATIONSHIP_DEF:
                relevantCoreProperties = corePropertiesRelationship.keySet();
                break;
            case CLASSIFICATION_DEF:
                relevantCoreProperties = corePropertiesClassification.keySet();
                break;
            default:
                return stringMatchProperties;
        }
        Iterator<String> relevantCorePropertiesIterator = relevantCoreProperties.iterator();
        while (relevantCorePropertiesIterator.hasNext())
        {
            String corePropName = relevantCorePropertiesIterator.next();
            if (corePropertyTypes.get(corePropName).equals("java.lang.String") && !corePropName.equals(PROPERTY_NAME_TYPE_NAME))
            {
                PrimitivePropertyValue ppv = new PrimitivePropertyValue();
                ppv.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                ppv.setPrimitiveValue(searchCriteria);
                log.debug("{} include string type core property {} value {}", methodName, corePropName, ppv);
                stringMatchProperties.setProperty(corePropName, ppv);
            }
        }


        /*
         * Include all string type type-defined properties
         */

        for (String typeName : validTypeNames)
        {
            // The caller has already filtered validTypeNames by category - so there is no need to check category again

            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);
            List<TypeDefAttribute> propertiesDefinition = null;

            if (performTypeFiltering && typeName.equals(filterTypeName))
            {
                /*
                 * Process all properties (including inherited properties)
                 */
                propertiesDefinition = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);
            }
            else
            {
                /*
                 * Process just the locally defined properties
                 */
                propertiesDefinition = typeDef.getPropertiesDefinition();
            }


            if (propertiesDefinition != null)
            {
                for (TypeDefAttribute typeDefAttribute : propertiesDefinition)
                {
                    if (typeDefAttribute != null)
                    {
                        String propertyName = typeDefAttribute.getAttributeName();

                        /*
                         * Only include the property if it's name does not conflict with a core property name.
                         * This could occur if any of the type defined attributes of the type has had a name clash,
                         * even if it has subsequently been deprecated. The type of the clashing TDA may conflict
                         * with the core property. For example the core property 'version' is a Java 'long', whereas
                         * the (deprecated) SoftwareCapability property 'version' was a Java 'String'. It still
                         * exists in the type definition, so need to avoid it here.
                         * Therefore check that any propertyName is NOT in the keySet of relevantCoreProperties (whether
                         * that property was included in stringMatchProperties or is of another (non-string) type.
                         */

                        if (propertyName != null &&  !relevantCoreProperties.contains(propertyName))
                        {
                            AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                            AttributeTypeDefCategory atdCategory = atd.getCategory();
                            if (atdCategory == PRIMITIVE)
                            {
                                PrimitiveDef primDef = (PrimitiveDef) atd;
                                PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                                if (primDefCat == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                                {
                                    PrimitivePropertyValue ppv = new PrimitivePropertyValue();
                                    ppv.setPrimitiveDefCategory(primDefCat);
                                    ppv.setPrimitiveValue(searchCriteria);
                                    log.debug("{} include search property {} value {}", methodName, propertyName, ppv);
                                    stringMatchProperties.setProperty(propertyName, ppv);
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                // no properties defined
                log.debug("{} no type-specific search properties to add", methodName);
            }
        } // done all types


        return stringMatchProperties;

    }


    void createEntityIndexes(TypeDef typeDef)
    {

        final String methodName = "createEntityIndexes";

        // Add mixed index for any primitive properties
        // (This does mean that we will introduce a mixed index for Date although we cannot yet query with date ranges for example..... )

        // MatchProperties are expressed using the short property name for each property.
        // Properties are stored in the graph with qualified property names - so we need to map to those in order to hit the indexes.
        // For the type of the entity, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

        List<TypeDefAttribute> propertyDefs = typeDef.getPropertiesDefinition();
        if (propertyDefs == null || propertyDefs.isEmpty())
        {
            log.debug("{} no vertex indexes needed for type {}", methodName, typeDef.getName());
            return;
        }

        log.debug("{} create vertex indexes for type {}", methodName, typeDef.getName());

        for (TypeDefAttribute typeDefAttribute : propertyDefs)
        {

            if (typeDefAttribute != null)
            {

                String propertyName = typeDefAttribute.getAttributeName();

                if (propertyName != null)
                {

                    AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                    AttributeTypeDefCategory atdCategory = atd.getCategory();

                    if (atdCategory == PRIMITIVE)
                    {

                        String qualifiedPropertyName = qualifiedPropertyNames.get(propertyName);
                        log.debug("{} qualified property name {}", methodName, qualifiedPropertyName);

                        PrimitiveDef primDef = (PrimitiveDef) atd;
                        PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                        GraphOMRSGraphFactory.MixedIndexMapping mapping;

                        if (primDefCat == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        else
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.Default;

                        graphFactory.createMixedIndexForVertexProperty(
                                qualifiedPropertyName,
                                getPropertyKeyEntity(qualifiedPropertyName),
                                primDefCat.getJavaClassName(),
                                mapping);

                    }
                }
            }
        }
    }


    void createClassificationIndexes(TypeDef typeDef)
    {

        final String methodName = "createClassificationIndexes";

        // Add mixed index for any primitive properties
        // (This does mean that we will introduce a mixed index for Date although we cannot yet query with date ranges for example..... )

        // MatchProperties are expressed using the short property name for each property.
        // Properties are stored in the graph with qualified property names - so we need to map to those in order to hit the indexes.
        // For the type of the classification, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);


        List<TypeDefAttribute> propertyDefs = typeDef.getPropertiesDefinition();
        if (propertyDefs == null || propertyDefs.isEmpty())
        {
            log.debug("{} no vertex indexes needed for type {}", methodName, typeDef.getName());
            return;
        }

        log.debug("{} create vertex indexes for type {}", methodName, typeDef.getName());

        for (TypeDefAttribute typeDefAttribute : propertyDefs)
        {

            if (typeDefAttribute != null)
            {

                String propertyName = typeDefAttribute.getAttributeName();

                if (propertyName != null)
                {

                    AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                    AttributeTypeDefCategory atdCategory = atd.getCategory();

                    if (atdCategory == PRIMITIVE)
                    {

                        String qualifiedPropertyName = qualifiedPropertyNames.get(propertyName);

                        PrimitiveDef primDef = (PrimitiveDef) atd;
                        PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                        GraphOMRSGraphFactory.MixedIndexMapping mapping;

                        if (primDefCat == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        else
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.Default;

                        graphFactory.createMixedIndexForVertexProperty(
                                qualifiedPropertyName,
                                getPropertyKeyClassification(qualifiedPropertyName),
                                primDefCat.getJavaClassName(),
                                mapping);

                    }
                }
            }
        }
    }


    void createRelationshipIndexes(TypeDef typeDef)
    {

        final String methodName = "createRelationshipIndexes";

        // Add mixed index for any primitive properties
        // (This does mean that we will introduce a mixed index for Date although we cannot yet query with date ranges for example..... )

        List<TypeDefAttribute> propertyDefs = typeDef.getPropertiesDefinition();
        if (propertyDefs == null || propertyDefs.isEmpty())
        {
            log.debug("{} no edge indexes needed for type {}", methodName, typeDef.getName());
            return;
        }

        // MatchProperties are expressed using the short property name for each property.
        // Properties are stored in the graph with qualified property names - so we need to map to those in order to hit the indexes.
        // For the type of the relationship, construct a map of short prop name -> qualified prop name. There is no type hierarchy for
        // relationships - but that doesn't matter, the util method will handle types for which superType is null.
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);


        log.debug("{} create edge indexes for type {}", methodName, typeDef.getName());

        for (TypeDefAttribute typeDefAttribute : propertyDefs)
        {

            if (typeDefAttribute != null)
            {

                String propertyName = typeDefAttribute.getAttributeName();

                if (propertyName != null)
                {

                    AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                    AttributeTypeDefCategory atdCategory = atd.getCategory();

                    if (atdCategory == PRIMITIVE)
                    {

                        String qualifiedPropertyName = qualifiedPropertyNames.get(propertyName);

                        PrimitiveDef primDef = (PrimitiveDef) atd;
                        PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                        GraphOMRSGraphFactory.MixedIndexMapping mapping;

                        if (primDefCat == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        else
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.Default;

                        graphFactory.createMixedIndexForEdgeProperty(
                                qualifiedPropertyName,
                                getPropertyKeyRelationship(qualifiedPropertyName),
                                primDefCat.getJavaClassName(),
                                mapping);

                    }
                }
            }
        }
    }




    // findEntitiesByClassification
    public List<EntityDetail> findEntitiesByClassification(String               classificationName,
                                                           InstanceProperties   classificationProperties,
                                                           MatchCriteria        matchCriteria,
                                                           boolean              performTypeFiltering,
                                                           List<String>         entityTypeNames)
    throws InvalidParameterException

    {


        final String methodName = "findEntitiesByClassification";

        List<EntityDetail> entities = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        // classificationName has already been validated and is known not to be null
        // entity typeName has already been validated and is known to be valid the classification type

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Classification");
        if (classificationName != null)
        {
            gt = gt.has(PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME, classificationName);
        }


        /*
         * For details of property namespace and how names are qualified please refer to comment in findEntitiesByProperty(). A
         * similar approach applies to classification properties.
         */

        Set<String> corePropertyNames = corePropertiesClassification.keySet();

        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, classificationName);
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);
        Set<String> typeDefinedPropertyNames = qualifiedPropertyNames.keySet();



        // This relies on the graph to enforce property validity - it does not pre-check that classification match properties are valid for requested type.
        if (classificationProperties != null)
        {
            List<GraphTraversal<Vertex, Vertex>> propCriteria = new ArrayList<>();
            Iterator<String> propNames = classificationProperties.getPropertyNames();

            while (propNames.hasNext())
            {
                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                String propName = propNames.next();

                String propNameToSearch = null;

                /*
                 * Check whether this is a core property or a type defined classification property
                 */

                if (corePropertyNames.contains(propName))
                {

                    /*
                     * Treat the match property as a reference to a core property
                     *
                     * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
                     * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
                     * primitive def category of string.
                     */

                    /*
                     *  Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
                     */
                    String javaTypeForMatchProperty = null;
                    PrimitiveDefCategory mpCat;
                    InstancePropertyValue mpv = classificationProperties.getPropertyValue(propName);
                    InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                        mpCat = ppv.getPrimitiveDefCategory();
                        javaTypeForMatchProperty = mpCat.getJavaClassName();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }
                    /*
                     * This needs to be compared to the type of the core property...
                     */

                    String javaTypeForCoreProperty = corePropertyTypes.get(propName);

                    if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
                    {
                        if (javaTypeForCoreProperty.equals(javaTypeForMatchProperty) ||
                                (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long"))
                        )
                        {
                            /*
                             * Types match, OK to include the property
                             */
                            propNameToSearch = PROPERTY_KEY_PREFIX_CLASSIFICATION + propName;
                            mapping = corePropertyMixedIndexMappings.get(propNameToSearch);
                        }
                        else
                        {
                            /*
                             * Match strictly for core properties because it will surface the problem - which lies at the
                             * heart of the type system, since there must be a name clash with the core properties.
                             */
                            throw new InvalidParameterException(
                                    GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                            propName,
                                            javaTypeForMatchProperty,
                                            javaTypeForCoreProperty,
                                            methodName,
                                            this.getClass().getName(),
                                            repositoryName),
                                    this.getClass().getName(),
                                    methodName,
                                    "classificationProperties");
                        }
                    }

                }
                else if (typeDefinedPropertyNames.contains(propName))
                {

                    /*
                     * Treat the match property as a reference to a type-defined property. Check that it's type matches the TDA.
                     */

                    List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

                    for (TypeDefAttribute propertyDef : propertiesDef)
                    {
                        String definedPropertyName = propertyDef.getAttributeName();
                        if (definedPropertyName.equals(propName))
                        {

                            /*
                             * The match property name matches the name of a type-defined attribute
                             *
                             * Check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                             */

                            PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            InstancePropertyValue mpv = classificationProperties.getPropertyValue(propName);
                            InstancePropertyCategory mpvCat = mpv.getInstancePropertyCategory();
                            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                            {
                                PrimitivePropertyValue ppv = (PrimitivePropertyValue) mpv;
                                mpCat = ppv.getPrimitiveDefCategory();
                            }
                            else
                            {
                                log.debug("{} non-primitive match property {} ignored", methodName, propName);
                            }

                            PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                            AttributeTypeDef atd = propertyDef.getAttributeType();
                            AttributeTypeDefCategory atdCat = atd.getCategory();
                            if (atdCat == PRIMITIVE)
                            {
                                PrimitiveDef pdef = (PrimitiveDef) atd;
                                pdCat = pdef.getPrimitiveDefCategory();
                            }

                            if (mpCat == pdCat && pdCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                            {
                                /*
                                 * Types match
                                 */
                                /*
                                 * Sort out the qualification and prefixing of the property name ready for graph search
                                 */
                                String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                                propNameToSearch = PROPERTY_KEY_PREFIX_CLASSIFICATION + qualifiedPropertyName;
                                mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

                            }
                            /*
                             * If types matched the code above will have set propNameToSearch. If the types did not match we should give up on this property - the search could be across a
                             * a variety of types (e.g. no type filtering is applied - and different types may have attributes with the same name but different types. If the name matches
                             * but the type doesn't then can only assume tha the match property was not intended for this type.
                             * Break out of the property for loop and drop through to catch all below
                             */
                            break;
                        }
                    }
                    /*
                     * if (!propertyFound) - The match property is not a supported, known type-defined property or does not have correct type - drop into the catch all below.
                     */


                    if (propNameToSearch == null)
                    {

                        /*
                         * The classification property is neither a core nor a type-defined property with matching name and type.
                         * If matchCriteria is ALL we need to give up at this point.
                         * If matchCriteria is ANY or NONE we can continue but just ignore this match property.
                         */
                        if (matchCriteria == MatchCriteria.ALL)
                        {
                            g.tx().rollback();
                            return null;
                        }
                        /*
                         * Skip this property but process the rest
                         */
                        // continue;

                    }
                    else
                    {
                        /*
                         * Incorporate the property (propNameToSearch) into propCriteria for the traversal...
                         */

                        InstancePropertyValue ipv = classificationProperties.getPropertyValue(propName);
                        InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                        if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                        {
                            // Primitives will have been stored in the graph as such
                            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                            PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                            Object primValue = ppv.getPrimitiveValue();
                            log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                            switch (pCat)
                            {

                                case OM_PRIMITIVE_TYPE_STRING:

                                    // Assume fullMatch is true
                                    boolean fullMatch = true;

                                    // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.

                                    String searchString = convertSearchStringToJanusRegex((String) primValue);
                                    log.debug("{} primitive match property search string {}", methodName, searchString);

                                    // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                                    if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
                                    {
                                        t = t.has(propNameToSearch, Text.textContainsRegex(searchString)); // for a field indexed using Text mapping use textContains or textContainsRegex
                                    }
                                    else
                                    {
                                        if (!fullMatch)
                                        {
                                            // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                                            String ANYCHARS = ".*";
                                            t = t.has(propNameToSearch, Text.textRegex(ANYCHARS + searchString + ANYCHARS));         // for a field indexed using String mapping use textRegex
                                        }
                                        else
                                        {
                                            // Must be a full match...
                                            t =t.has(propNameToSearch, Text.textRegex(searchString));
                                        }
                                    }
                                    break;

                                case OM_PRIMITIVE_TYPE_DATE:
                                    // If PrimitiveDefCategory is Date and this is a core property then you need to cast the Long to a Date
                                    if (corePropertyNames.contains(propName))
                                    {
                                        Date dateValue = new Date((Long) primValue);
                                        t = t.has(propNameToSearch, dateValue);
                                    }
                                    else
                                    {
                                        // Can use the primitive match property value as is, since it is a Long
                                        t = t.has(propNameToSearch, primValue);
                                    }
                                    break;

                                default:
                                    t = t.has(propNameToSearch, primValue);
                                    break;

                            }
                            log.debug("{} primitive match property has property criterion {}", methodName, t);
                            propCriteria.add(t);
                        }
                        else
                        {
                            log.debug("{} non-primitive match property {} ignored", methodName, propName);
                        }
                    }
                }
            }


            if (matchCriteria != null)
            {
                switch (matchCriteria)
                {
                    case ALL:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case ANY:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case NONE:
                        if (!propCriteria.isEmpty())
                        {
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                            t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                            gt = gt.not(t);
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    default:
                        g.tx().rollback();
                        final String parameterName = "matchCriteria";

                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                parameterName);

                }
            }
        }


        // Cannot return EntityProxy objects, so ensure that only traverse to a non-proxy entity vertex...

        gt = gt.in("Classifier").has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

        // Optionally perform type filtering
        if (performTypeFiltering)
        {
            gt = gt.has(PROPERTY_KEY_ENTITY_TYPE_NAME, within(entityTypeNames));
        }

        while (gt.hasNext())
        {
            Vertex entityVertex = gt.next();
            log.debug("{} found entity vertex {}", methodName, entityVertex);

            EntityDetail entityDetail = new EntityDetail();
            try
            {
                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(entityVertex);
                if (!isProxy)
                {
                    entityMapper.mapVertexToEntityDetail(entityVertex, entityDetail);
                    entities.add(entityDetail);
                }
            }
            catch (Exception e)
            {
                log.error("{} caught exception from entity mapper - entity will be ignored, {}", methodName, e.getMessage());
                // continue; // process the next vertex
            }
        }

        g.tx().commit();

        return entities;

    }




    InstanceGraph getSubGraph(String                entityGUID,
                              List<String>          entityTypeGUIDs,
                              List<String>          relationshipTypeGUIDs,
                              List<InstanceStatus>  limitResultsByStatus,
                              List<String>          limitResultsByClassification,
                              int level)

    throws TypeErrorException,
           EntityNotKnownException
    {

        final String methodName = "getSubGraph";
        final String entTypeGUIDsParameterName = "entityTypeGUIDs";
        final String relTypeGUIDsParameterName = "relationshipTypeGUIDs";

        boolean limited = true;

        log.debug("{} entityGUID = {}, entityTypeGUIDs = {}, relationshipTypeGUIDs = {}, limitResultsByStatus = {}, limitResultsByClassification = {}, level = {}",
                  methodName, entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, level);

        /*
         * Starting at the entity with entityGUID, traverse relationships and other entities, filtering by instance types and statuses
         * and classifications, if specified. Traverse to a maximum depth specified by level.
         * The root entity is always included regardless of the entityTypeGUIDs.
         *
         * Only EntityDetail objects are returned in InstanceGraph.entities, but EntityProxy objects are traversed and are embedded in InstanceGraph.relationships.
         */

        if (level == -1)
        {
            /*
             * Traversal limiting is disabled. This traversal will continue until it has no graph left to traverse. This is expensive on large graphs!
             */
            limited = false;
        }

        List<EntityDetail> entities = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();

        InstanceGraph subGraph = new InstanceGraph();


        // The optional entity type filter specifies which entity types values are permissible.
        List<String> entityTypeNames = new ArrayList<>();
        boolean entitiesWithin = false;
        if (entityTypeGUIDs != null)
        {
            entitiesWithin = true;
            for (String entTypeGUID : entityTypeGUIDs)
            {
                try
                {
                    TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entTypeGUIDsParameterName, entTypeGUID, methodName);
                    String entTypeName = typeDef.getName();
                    entityTypeNames.add(entTypeName);
                }
                catch (Exception e)
                {
                    log.error("{} caught exception from repository helper trying to resolve type with GUID {}", methodName, entTypeGUID);

                    throw new TypeErrorException(
                            GraphOMRSErrorCode.ENTITY_TYPE_GUID_NOT_KNOWN.getMessageDefinition(
                                    entTypeGUID,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName, e);
                }

            }
        }


        // The optional relationship type filter specifies which relationship types values are permissible.
        List<String> relationshipTypeNames = new ArrayList<>();
        boolean relationshipsWithin = false;
        if (relationshipTypeGUIDs != null)
        {
            relationshipsWithin = true;
            for (String relTypeGUID : relationshipTypeGUIDs)
            {
                try
                {
                    TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, relTypeGUIDsParameterName, relTypeGUID, methodName);
                    String relTypeName = typeDef.getName();
                    relationshipTypeNames.add(relTypeName);
                }
                catch (Exception e)
                {
                    log.error("{} caught exception from repository helper trying to resolve type with GUID {}", methodName, relTypeGUID);

                    throw new TypeErrorException(
                            GraphOMRSErrorCode.RELATIONSHIP_TYPE_GUID_NOT_KNOWN.getMessageDefinition(
                                    relTypeGUID,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName, e);
                }

            }
        }


        /*  The optional status filter specifies which status values are permissible.
         *  If no filter is specified, the default is that DELETED elements are not traversed.
         *  If a status filter is specified, it is taken literally - i.e. all and only the specified
         *  statuses specified are included; if it includes DELETED then DELETED elements are traversed.
         */
        List<Integer> statusOrdinals = new ArrayList<>();
        boolean statusWithin = false;
        if (limitResultsByStatus == null)
        {
            statusOrdinals.add(InstanceStatus.DELETED.getOrdinal());   // Do not traverse a DELETED element by default
        }
        else
        {  // positive status filter was specified
            statusWithin = true;
            for (InstanceStatus iStatus : limitResultsByStatus)
            {
                statusOrdinals.add(iStatus.getOrdinal());
            }
        }


        /* The optional classification filter specifies which classifications make an entity permissible.
         * If no filter is specified, the default is that no classification checks are performed.
         * If a classification filter is specified, then an entity with any of the specified classifications can be traversed.
         * This test and loop may seem a bit pointless - but it provides consistency and may be useful for debug.
         */
        List<String> classificationNames = new ArrayList<>();
        boolean classificationWithin = false;
        if (limitResultsByClassification != null)
        {
            classificationWithin = true;
            classificationNames.addAll(limitResultsByClassification);
        }


        /* The essence of the traversal is as follows, where V(rootVertex) is the vertex relating to the rootEntity,
         * specified by the entityGUID parameter.
         * The traversal repeats a sub-traversal that emits each traversed relationship and destination entity. The
         * traversal does not emit the root vertex - since the root entity is retrieved and added to the InstanceGraph
         * prior to commencement of the traversal. The traversal follows all relationships (subject to filtering conditions)
         * and visit all connected entities (subject to filtering conditions). The method returns an InstanceGraph,
         * which contains a list of Relationship objects and a list of EntityDetail objects. When the traversal reaches
         * an EntityProxy it will continue to traverse, despite not being able to include th proxy in the InstanceGraph.
         * Any proxy entities will be contained in the Relationship through which they are reached, whereas EntityDetail
         * objects (whether arising from a locally hosted entity or a reference copy of a remotely hosted entity) will be
         * include in the InstanceGraph.
         * After return of the InstanceGraph, the caller could retrieve a full EntityDetail of an entity that locally is
         * only a proxy, by issuing an enterprise level query using the GUID of the proxy.
         *
         * The following is pseudo-code:
         *
         * g.V(rootVertex).repeat(
         *     bothE("Relationship").has({relationship-status-filter}).has({relationship-type-filter}).as("r").
         *     otherV().has({entity-status-filter}).has({entity-type-filter}).as("e").
         *         where(out("Classifier").has({classification-filter})).
         *     simplePath()).
         * times(2).emit().select("r","e")
         *
         * THe various filters are optional and are implemented using has(<property>,within(<filter-collection>))
         */


        GraphTraversalSource g = instanceGraph.traversal();

        try
        {
            Vertex rootVertex;
            GraphTraversal<Vertex, Vertex> t = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID);

            if (!t.hasNext())
            {
                log.error("{} could not retrieve start entity with GUID {}", methodName, entityGUID);
                g.tx().rollback();

                throw new EntityNotKnownException(
                        GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                entityGUID, methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName);
            }
            else
            {

                // Find the root vertex
                rootVertex = t.next();
                log.debug("{} found root entity vertex {}", methodName, rootVertex);

                try
                {
                    EntityDetail rootEntity = new EntityDetail();
                    entityMapper.mapVertexToEntityDetail(rootVertex, rootEntity);
                    entities.add(rootEntity);
                    g.tx().commit();

                }
                catch (EntityProxyOnlyException | RepositoryErrorException e)
                {
                    log.error("{} caught exception whilst trying to map entity with GUID {}, exception {}", methodName, entityGUID, e.getMessage());
                    g.tx().rollback();

                    throw new EntityNotKnownException(
                            GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                    entityGUID, methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName, e);
                }

                if (level != 0)
                {

                    // Reset the traversal - not sure if this is strictly necessary

                    g = instanceGraph.traversal();

                    GraphTraversal<Vertex, Edge> edgesTraversal = new DefaultGraphTraversal<>();
                    edgesTraversal = edgesTraversal.bothE("Relationship");

                    // Optionally filter relationships by status
                    if (statusWithin) {
                        edgesTraversal = edgesTraversal.has(PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS, within(statusOrdinals));
                    } else {
                        edgesTraversal = edgesTraversal.has(PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS, without(statusOrdinals));
                    }

                    // Optionally filter by relationship type
                    if (relationshipsWithin)
                    {
                        edgesTraversal = edgesTraversal.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, within(relationshipTypeNames));
                    }

                    // Project the relationships and move on to the inVertex for each relationship...
                    GraphTraversal<Vertex, Vertex> vertexTraversal = edgesTraversal.as("r").otherV();

                    // Optionally filter entities by status
                    if (statusWithin) {
                        vertexTraversal = vertexTraversal.has(PROPERTY_KEY_ENTITY_CURRENT_STATUS, within(statusOrdinals));
                    } else {
                        vertexTraversal = vertexTraversal.has(PROPERTY_KEY_ENTITY_CURRENT_STATUS, without(statusOrdinals));
                    }

                    // Exclude EntityProxy vertices... or not... for now the traversal will traverse a proxy but only include it
                    // in the relationship reported, not in the entities list.
                    // repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

                    // Optionally filter by entity type
                    if (entitiesWithin)
                    {
                        vertexTraversal = vertexTraversal.has(PROPERTY_KEY_ENTITY_TYPE_NAME, within(entityTypeNames));
                    }

                    // Optionally filter (entities) by classification
                    if (classificationWithin)
                    {
                        //  where(out("Classifier").has("vcclassificationName",within("MobileAsset","Confidentiality"))).
                        vertexTraversal = vertexTraversal.where(out("Classifier").has(PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME, within(classificationNames)));

                    }

                    // Project the traversed TO entities (only, not the entities we have traversed FROM)...
                    vertexTraversal = vertexTraversal.as("e");

                    // Include simplePath to avoid back-tracking
                    vertexTraversal = vertexTraversal.simplePath();

                    // Construct the overall traversal

                    GraphTraversal<Vertex, Map<String,Element>> overallTraversal;
                    if (limited)
                    {
                        overallTraversal = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID).repeat(vertexTraversal).times(level).emit().select("r", "e");
                    }
                    else
                    {
                        overallTraversal = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID).repeat(vertexTraversal).emit().select("r", "e");
                    }

                    while (overallTraversal.hasNext())
                    {

                        Map<String, Element> resTuple = overallTraversal.next();
                        Edge edge = (Edge) resTuple.get("r");
                        Vertex vertex = (Vertex) resTuple.get("e");

                        log.debug("{} subgraph has edge {} and vertex {}", methodName, edge, vertex);

                        if (edge != null && vertex != null)
                        {

                            log.debug("{} save the relationship for edge {}", methodName, edge);

                            Relationship relationship = new Relationship();
                            relationshipMapper.mapEdgeToRelationship(edge, relationship);
                            relationships.add(relationship);

                            // Get the end entities and add them to the relationship as proxies.

                            try
                            {

                                /* Map the discovered entities - need a proxy for each end of the relationship,
                                 * plus, if the entity is an EntityDetail then you need to add that to
                                 * entities list in the InstanceGraph too.
                                 */

                                // Start with the outVertex
                                Vertex vout = edge.outVertex();

                                if (vout != null)
                                {
                                    log.debug("{} Create proxy for end 1 entity vertex {}", methodName, vout);
                                    EntityProxy entityOneProxy = new EntityProxy();
                                    entityMapper.mapVertexToEntityProxy(vout, entityOneProxy);
                                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                                    relationship.setEntityOneProxy(entityOneProxy);

                                }

                                // Move to the inVertex
                                Vertex vin = edge.inVertex();

                                if (vin != null)
                                {
                                    log.debug("{} Create proxy for end 2 entity vertex {}", methodName, vin);
                                    EntityProxy entityTwoProxy = new EntityProxy();
                                    entityMapper.mapVertexToEntityProxy(vin, entityTwoProxy);
                                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                                    relationship.setEntityTwoProxy(entityTwoProxy);

                                }

                                /*
                                 * You only add the arrived-at entity if it is not a proxy. This
                                 * is the vertex from the tuple above. Only need to add the arrived-at
                                 * vertex to the InstanceGraph because the traversed-from vertex will
                                 * already have been added (it is either the root or has been
                                 * traversed through already).
                                 */

                                log.debug("{} Create entity detail for remote vertex {}", methodName, vertex);

                                if (!entityMapper.isProxy(vertex))
                                {
                                    EntityDetail entityDetail = new EntityDetail();
                                    entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                                    log.debug("{} entityDetail {}", methodName, entityDetail);
                                    entities.add(entityDetail);
                                }
                            }
                            catch (EntityProxyOnlyException | RepositoryErrorException e)
                            {
                                /* This catch block abandons the whole traversal and neighbourhood search.
                                 * This may be a little draconian ut presumably better to know that something
                                 * is wrong rather than plough on in ignorance.
                                 */
                                log.error("{} caught exception whilst trying to map entity, exception {}", methodName, e.getMessage());
                                g.tx().rollback();

                                throw new EntityNotKnownException(
                                        GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                                entityMapper.getEntityGUID(vertex), methodName,
                                                this.getClass().getName(),
                                                repositoryName),
                                        this.getClass().getName(),
                                        methodName, e);
                            }
                        }
                    }
                    g.tx().commit();
                }
            }

            // Construct the InstanceGraph from entities and relationships
            subGraph.setEntities(entities);
            subGraph.setRelationships(relationships);

            return subGraph;

        }
        catch (EntityNotKnownException e)
        {
            log.error("{} caught entity not known exception from subgraph traversal {}", methodName, e.getMessage());
            g.tx().rollback();
            throw e;
        }
        catch (Exception e)
        {
            log.error("{} caught exception from subgraph traversal {}", methodName, e.getMessage());
            g.tx().rollback();
            return null;
        }


    }

    public InstanceGraph getPaths(String                startEntityGUID,
                                  String                endEntityGUID,
                                  List<InstanceStatus>  limitResultsByStatus,
                                  int                   maxPaths,
                                  int                   maxDepth)

    {

        final String methodName = "getPaths";


        log.debug("{} startEntityGUID = {}, endEntityGUID = {}, limitResultsByStatus = {}, maxPaths = {}, maxDepth {}",
                  methodName, startEntityGUID, endEntityGUID, limitResultsByStatus, maxPaths, maxDepth);

        /*
         * Starting at the entity wth startEntityGUID, traverse all relationships and entities, filtering by status
         * if specified, and filtering out DELETED elements if no status filter is specified.
         * Traverse to a maximum depth specified by maxDepth, at which point any traverser terminates.
         * Continue traversing until maxPaths have been discovered, at which point kill any surviving traversers.
         * The root entity is always included regardless of status.
         *
         * Only EntityDetail objects are returned in InstanceGraph.entities, but EntityProxy objects are traversed and are embedded in InstanceGraph.relationships.
         */

        List<EntityDetail> entities = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();

        InstanceGraph subGraph = new InstanceGraph();


        /*  The optional status filter specifies which status values are permissible.
         *  If no filter is specified, the default is that DELETED elements are not traversed.
         *  If a status filter is specified, it is taken literally - i.e. all and only the specified
         *  statuses specified are included; if it includes DELETED then DELETED elements are traversed.
         */
        List<Integer> statusOrdinals = new ArrayList<>();
        boolean statusWithin = false;
        if (limitResultsByStatus == null)
        {
            statusOrdinals.add(InstanceStatus.DELETED.getOrdinal());   // Do not traverse a DELETED element by default
        }
        else
        {  // positive status filter was specified
            statusWithin = true;
            for (InstanceStatus iStatus : limitResultsByStatus)
            {
                statusOrdinals.add(iStatus.getOrdinal());
            }
        }


        /* The essence of the traversal is as follows, where V(rootVertex) is the vertex relating to the rootEntity,
         * specified by the startEntityGUID parameter.
         * The traversal repeats a sub-traversal that traverses in/out relationships until the destination entity is
         * reached, at which point the path is emitted.
         * The traversal follows all relationships (subject to status filtering conditions) and all entities (subject
         * to status filtering conditions).
         * The method returns an InstanceGraph, which contains a list of Relationship objects and a list of EntityDetail
         * objects. When the traversal reaches an EntityProxy it will continue to traverse, despite not being able to include
         * the proxy in the InstanceGraph. Any proxy entities will be contained in the Relationship through which they are
         * reached, whereas EntityDetail objects (whether arising from a locally hosted entity or a reference copy of a
         * remotely hosted entity) will be include in the InstanceGraph.
         * After return of the InstanceGraph, the caller could retrieve a full EntityDetail of an entity that locally is
         * only a proxy, by issuing an enterprise level query using the GUID of the proxy.
         *
         * The following is pseudo-code:
         *
         * g.V(rootVertex).repeat(
         *     bothE("Relationship").has({relationship-status-filter}).
         *     otherV().has({entity-status-filter}).
         *         simplePath()).
         *     until(has(PROPERTY_KEY_ENTITY_GUID,"<endEntityGUID>").or(loops().is(gte(maxDepth))).has(PROPERTY_KEY_ENTITY_GUID,"<endEntityGUID>").
         *     path.limit(maxPaths).unfold().dedup().fold()
         *
         * The additional has clause after the loop is to filter out the traversers that reached maxDepth without finding the endEntity.
         *
         * THe various filters are optional and are implemented using has(<property>,within(<filter-collection>))
         */

        GraphTraversalSource g = instanceGraph.traversal();

        try
        {
            // Although not strictly needed for this traversal, it is important that the start entity exists - otherwise throw entity not found exception
            // represented by an EntityDetail.

            Vertex rootVertex;
            GraphTraversal<Vertex, Vertex> t = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, startEntityGUID);
            if (!t.hasNext())
            {

                log.error("{} could not retrieve start entity with GUID {}", methodName, startEntityGUID);
                g.tx().rollback();

                throw new EntityNotKnownException(
                        GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                startEntityGUID, methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName);
            }
            else
            {

                // Since we have access to it now, we might as well map the rootVertex to produce helpful debug information

                rootVertex = t.next();
                log.debug("{} found root entity vertex {}", methodName, rootVertex);

                try
                {

                    EntityDetail rootEntity = new EntityDetail();
                    entityMapper.mapVertexToEntityDetail(rootVertex, rootEntity);
                    log.debug("{} mapped root entity {}", methodName, rootEntity);

                    /*
                     * No need to add rootEntity to InstanceGraph.entities - it will be included in the paths discovered below.
                     * There is one exception - which is when the caller has passed the same GUID for both start and end - in
                     * this case we should return just the start/end entity; but the traversals will not find any paths, so in this
                     * case (only) we should add the entity to the result.
                     */
                    if (startEntityGUID.equals(endEntityGUID))
                        entities.add(rootEntity);

                    g.tx().commit();

                }
                catch (EntityProxyOnlyException | RepositoryErrorException e)
                {

                    log.error("{} caught exception whilst trying to map entity with GUID {}, exception {}", methodName, startEntityGUID, e.getMessage());
                    g.tx().rollback();

                    throw new EntityNotKnownException(
                            GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                    startEntityGUID, methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName, e);
                }


                // Reset the traversal - not sure if this is strictly necessary

                g = instanceGraph.traversal();

                GraphTraversal<Vertex, Edge> edgesTraversal = new DefaultGraphTraversal<>();
                edgesTraversal = edgesTraversal.bothE("Relationship");

                // Optionally filter relationships by status
                if (statusWithin)
                {
                    edgesTraversal = edgesTraversal.has(PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS, within(statusOrdinals));
                }
                else
                {
                    edgesTraversal = edgesTraversal.has(PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS, without(statusOrdinals));
                }

                // Move on to the inVertex for each relationship...
                GraphTraversal<Vertex, Vertex> vertexTraversal = edgesTraversal.otherV();

                // Optionally filter entities by status
                if (statusWithin)
                {
                    vertexTraversal = vertexTraversal.has(PROPERTY_KEY_ENTITY_CURRENT_STATUS, within(statusOrdinals));
                }
                else
                {
                    vertexTraversal = vertexTraversal.has(PROPERTY_KEY_ENTITY_CURRENT_STATUS, without(statusOrdinals));
                }


                // Include simplePath to avoid back-tracking
                vertexTraversal = vertexTraversal.simplePath();

                // Repeat terminator traversal...
                GraphTraversal<Vertex, Integer> untilTraversal = new DefaultGraphTraversal<>();
                untilTraversal = untilTraversal.has(PROPERTY_KEY_ENTITY_GUID, endEntityGUID).or().loops().is(gte(maxDepth));

                // Construct the overall traversal
                GraphTraversal<Vertex, List<Object>> overallTraversal = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, startEntityGUID).repeat(vertexTraversal).until(untilTraversal).
                        has(PROPERTY_KEY_ENTITY_GUID, endEntityGUID).path().limit(maxPaths).unfold().dedup().fold();

                while (overallTraversal.hasNext())
                {

                    List<Object> resList = overallTraversal.next();

                    if (!resList.isEmpty())
                    {

                        Edge edge;
                        Vertex vertex;

                        for (Object object : resList)
                        {
                            if (object instanceof Vertex)
                            {
                                vertex = (Vertex) object;
                                log.debug("{} subgraph has vertex {}", methodName, vertex);

                                if (!entityMapper.isProxy(vertex))
                                {
                                    try
                                    {
                                        EntityDetail entityDetail = new EntityDetail();
                                        entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                                        log.debug("{} entityDetail {}", methodName, entityDetail);
                                        entities.add(entityDetail);
                                    }
                                    catch (RepositoryErrorException | EntityProxyOnlyException e)
                                    {
                                        log.error("{} could not map vertex returned in path expression, entity GUID {}, exception {}", methodName, entityMapper.getEntityGUID(vertex), e.getMessage());
                                        g.tx().rollback();

                                        throw new EntityNotKnownException(
                                                GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                                        startEntityGUID, methodName,
                                                        this.getClass().getName(),
                                                        repositoryName),
                                                this.getClass().getName(),
                                                methodName);
                                    }
                                }
                            }
                            else if (object instanceof Edge)
                            {

                                edge = (Edge) object;


                                log.debug("{} subgraph has edge {} ", methodName, edge);

                                Relationship relationship = new Relationship();
                                relationshipMapper.mapEdgeToRelationship(edge, relationship);
                                relationships.add(relationship);

                                // Get the end entities and add them to the relationship as proxies.

                                vertex = null;

                                try
                                {
                                    /* Map the discovered entities - need a proxy for each end of the relationship,
                                     * plus, if the entity is an EntityDetail then you need to add that to
                                     * entities list in the InstanceGraph too.
                                     */

                                    // Start with the outVertex
                                    vertex = edge.outVertex();

                                    if (vertex != null)
                                    {
                                        log.debug("{} end 1 entity vertex {}", methodName, vertex);
                                        EntityProxy entityOneProxy = new EntityProxy();
                                        entityMapper.mapVertexToEntityProxy(vertex, entityOneProxy);
                                        log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                                        relationship.setEntityOneProxy(entityOneProxy);

                                    }

                                    // Move to the inVertex
                                    vertex = edge.inVertex();

                                    if (vertex != null)
                                    {
                                        log.debug("{} end 2 entity vertex {}", methodName, vertex);
                                        EntityProxy entityTwoProxy = new EntityProxy();
                                        entityMapper.mapVertexToEntityProxy(vertex, entityTwoProxy);
                                        log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                                        relationship.setEntityTwoProxy(entityTwoProxy);
                                    }
                                }
                                catch (RepositoryErrorException e)
                                {
                                    /* This catch block abandons the whole traversal and neighbourhood search.
                                     * This may be a little draconian but presumably better to know that something
                                     * is wrong rather than plough on in ignorance.
                                     */
                                    log.error("{} caught exception whilst trying to map entity, exception {}", methodName, e.getMessage());
                                    g.tx().rollback();

                                    throw new EntityNotKnownException(
                                            GraphOMRSErrorCode.ENTITY_NOT_FOUND.getMessageDefinition(
                                                    entityMapper.getEntityGUID(vertex), methodName,
                                                    this.getClass().getName(),
                                                    repositoryName),
                                            this.getClass().getName(),
                                            methodName, e);
                                }

                            }
                            else
                            {
                                log.error("{} unknown path element type - element {}", methodName, object);
                                break;
                            }
                        }
                    }
                }
            }

            g.tx().commit();

            // Construct the InstanceGraph from entities and relationships
            subGraph.setEntities(entities);
            subGraph.setRelationships(relationships);

            return subGraph;

        }
        catch (Exception e)
        {
            log.error("{} caught exception from subgraph traversal {}", methodName, e.getMessage());
            g.tx().rollback();
            return null;
        }


    }



    // findEntitiesForType
    List<EntityDetail> findEntitiesForType(String              typeDefName,
                                           SearchProperties    searchProperties,
                                           boolean             fullMatch)

    throws InvalidParameterException,
           RepositoryErrorException,
           FunctionNotSupportedException

    {

        final String methodName = "findEntitiesForType";

        List<EntityDetail> entities = new ArrayList<>();


        /*
         *
         * There are two origins of properties stored on an instance vertex in the graph -
         *   1. core properties from the audit header
         *   2. type-defined attributes from the typedef (including inheritance in the case of entities, but not relationships or classifications)
         *
         * The core property names are known (they are listed in the keys of GraphOMRSConstants.corePropertyTypes). Core properties are stored in the
         * graph (as vertex and edge properties) under their prefixed name - where the prefix depends on the type and purpose of the graph
         * element (e.g. vertex-entity, edge-relationship or vertex-classification). These are shortened to 've', 'er' and 'vc' as defined in the constants.
         * For example, for an entity the core 'createdBy' property from InstanceAuditHeader is stored under the key vecreatedBy.
         *
         * The type-defined attribute names are known from the typedef. The properties are stored in the graph (as vertex and edge properties) under their
         * prefixed and qualified name. For example, for a Referenceable (or subtype) entity the type-defined attribute 'qualifiedName' property is stored under
         * the key 'veReferenceablexqualfiiedName'.
         *
         * There is only one namespace of properties - so a type0defined attribute should never clash with a core property. If there is a name clash between a
         * core property and a type-defined attribute it is an error in the type system (and should be fixed by an issue). This method cannot police such name
         * clashes, and tolerates them by giving precedence to the core property with the specified name.
         *
         * Match or search properties are specified using short (unqualified) names. Properties are stored in the graph with qualified property names - so we
         * need to map to those in order to hit the indexes and vertex/edge properties. The short names of type-defined attributes do not need to be unique -
         * i.e. different types that both define a type-defined attribute with the same (short) name. This is why the graph and indexes use the
         * qualifiedPropertyNames.
         * The calling code supports wildcard searches (across many types) so the type identified by typeDefName may have different type defined attributes
         * to the attributes in searchProperties. Even if they match by name there is no guarantee that they are equivalent. They must be checked for both
         * property name and type. In the case that a searchProperties contains a short-named property intended for a type other than the one being searched -
         * this method checks the types match before issuing the graph traversal. This protects against type violations in the traversal. If the types do
         * not match the method reacts depending on how matchCriteria is set. If mc is ALL then no traversal is performed; if mc is ANY then a traversal is
         * performed WITHOUT the mismatched property; if mc is NONE a traversal is performed WITHOUT the mismatched property.
         *
         *
         * For the type of the entity or relationship, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
         */


        /*
         * Even if there are no search properties specified, the method performs a traversal.
         */

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity");
        if (typeDefName != null)
        {
            gt = gt.has(PROPERTY_KEY_ENTITY_TYPE_NAME, typeDefName);
        }
        // Only accept non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);


        if (searchProperties != null)
        {

            /*
             * searchProperties is a SearchProperties object which contains MatchCriteria and a list of
             * PropertyCondition objects, which in turn may contain nestedConditions. The rules around
             * searchProperties is that a PropertyCondition EITHER specifies a property value, condition
             * and operator OR it specifies none of them and instead provides a nested SearchProperties.
             *
             * Extract the conditions and construct the necessary graph traversals.
             *
             * Start at the top of the searchProperties and iterate over the list of PropertyConditions.
             * For each PropertyCondition, either process it (property, value, operator) or recursively
             * visit the nested SearchProperties it contains. At each stage in the iteration and recursion
             * add each discovered condition to the graph traversal.
             */

            try
            {
                List<GraphTraversal<Vertex, Vertex>> propCriteria = processEntitySearchPropertiesForType(typeDefName, searchProperties, fullMatch);

                /*
                 * Use the MatchCriteria to combine the properCriteria into the overall graph traversal.
                 *
                 * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
                 * between the match properties and the properties defined on the type (core or type defined). So
                 * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
                 * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
                 * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
                 * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
                 * entity) so let that case continue.
                 */

                switch (searchProperties.getMatchCriteria())
                {
                    case ALL:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case ANY:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case NONE:
                        if (!propCriteria.isEmpty())
                        {
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                            t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                            gt = gt.not(t);
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    default:
                        /*
                         * Invalid match criteria. Throw InvalidParameterException.
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");
                }
            }
            catch (Exception e)
            {
                /*
                 * If anything went wrong (e.g. invalid match criteria or invalid parameter) catch the
                 * exception and perform a rollback. Then rethrow for error reporting.
                 */
                g.tx().rollback();
                throw e;
            }
        }

        while (gt.hasNext())
        {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            EntityDetail entityDetail = new EntityDetail();
            try
            {
                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy)
                {
                    entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                    entities.add(entityDetail);
                }
            }
            catch (Exception e)
            {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(
                                entityDetail.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        g.tx().commit();

        return entities;

    }






    // findEntitiesForTypes
    List<EntityDetail> findEntitiesForTypes(List<String>                   validTypeNames,
                                            String                         filterTypeName,
                                            Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                            Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                            SearchProperties               searchProperties)

    throws InvalidParameterException,
           RepositoryErrorException,
           FunctionNotSupportedException

    {

        final String methodName = "findEntitiesForTypes";

        boolean performTypeFiltering = filterTypeName != null;


        /*
         * Get the traversal started,...
         */
        GraphTraversalSource g = instanceGraph.traversal();
        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity");

        /*
         * Only accept non-proxy entities:
         */
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);


        /*
         * There are two origins of properties stored on an instance vertex in the graph:
         *   1. core properties from the instance audit header
         *   2. type-defined attributes from the typedef (including inheritance in the case of entities, but not relationships or classifications)
         *
         * The core property names are known (they are listed in the keys of GraphOMRSConstants.corePropertyTypes). More specifically, the names of the core
         * properties that are valid for entities are provide by the corePropertiesEntity map. Core properties are stored in the graph (as vertex and edge
         * properties) under their prefixed name - where the prefix depends on the type and purpose of the graph element (e.g. vertex-entity, edge-relationship
         * or vertex-classification). These are shortened to 've', 'er' and 'vc' as defined in the constants. For example, for an entity the core 'createdBy'
         * property from InstanceAuditHeader is stored under the key vecreatedBy.
         *
         * The type-defined attribute names are known from the typedef. The properties are stored in the graph as (as vertex and edge properties) under their
         * prefixed and qualified name. For example, for a Referenceable entity (or any of its subtypes) the type-defined attribute 'qualifiedName' property
         * is stored under the key 'veReferenceablexqualfiiedName'.
         *
         * There is only one namespace of properties - so a type-defined attribute should never clash with a core property. If there is a name clash between a
         * core property and a type-defined attribute it is an error in the type system (and should be fixed by an issue). This method cannot police such name
         * clashes, and handles them by giving precedence to the core property with the specified name.
         *
         * Match properties are specified using short (unqualified) names. Properties are stored in the graph with qualified property names - so we need to map
         * to those in order to hit the indexes and vertex/edge properties. The short names of type-defined attributes do not need to be unique - i.e. different
         * types can both define a type-defined attribute with the same (short) name. This is why the graph and indexes use the qualifiedPropertyNames.
         * The calling code supports wildcard searches (across many types) so the type identified by typeDefName may have different type defined attributes
         * to the attributes in matchProperties. Even if they match by name there is no guarantee that they are equivalent. They must be checked for both
         * property name and type. In the case that a matchProperties contains a short-named property intended for a type other than the one being searched -
         * this method checks the types match before issuing the graph traversal. This protects against type violations in the traversal. If the types do
         * not match then a traversal step is added that will filter all traversers. This is so that when the macthCriteria is applied (at the next higher
         * level in the recursive parsing of the (optionally nested) SearchProperties) the traversal step will deliver the desired result. It is not possible
         * to perform an in-place and immediate optimisation of this as found in the fineEntitiesByProperty method for example.
         */


        if (searchProperties != null)
        {

            /*
             * The caller has provided SearchProperties so there is property matching to do.
             *
             * searchProperties is a SearchProperties object which contains MatchCriteria and a list of
             * PropertyCondition objects, which in turn may contain nestedConditions.
             *
             * The conditions are parsed and extracted to construct the necessary graph traversals.
             *
             * The rules governing searchProperties are that a PropertyCondition EITHER specifies a property
             * value, condition and operator OR it specifies none of them and instead provides a nested
             * SearchProperties. Each property condition is validated to establish its correctness and to
             * determine whether it represents a local or nested condition.
             *
             * Processing starts at the top level of the SearchProperties and iterates over the list of
             * PropertyConditions. Each PropertyCondition is either processed as a local condition (it has
             * property, value, operator) or recursively as a nested condition. For a nested condition the
             * process visits the nested SearchProperties and repeats the above logic. At each stage in the
             * iteration and recursion a list of graph traversal criteria is constructed, and subsequently
             * combined using the MatchCriteria applicable to that level in the SearchProperties.
             */





            /*
             * Process the search properties for all the valid types - i.e. against the above maps.
             */
            try
            {
                List<GraphTraversal<Vertex, Vertex>> propCriteria = processEntitySearchPropertiesForTypes(searchProperties,
                                                                                                          qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                                          shortPropertyNameToQualifiedPropertyNames);

                /*
                 * Use the MatchCriteria to combine the properCriteria into the overall graph traversal.
                 *
                 * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
                 * between the match properties and the properties defined on the type (core or type defined). So
                 * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
                 * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
                 * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
                 * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
                 * entity) so let that case continue.
                 */

                switch (searchProperties.getMatchCriteria())
                {
                    case ALL:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case ANY:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case NONE:
                        if (!propCriteria.isEmpty())
                        {
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                            t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                            gt = gt.not(t);
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    default:
                        /*
                         * Invalid match criteria. Throw InvalidParameterException.
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");
                }
            }
            catch (Exception e)
            {
                /*
                 * If anything went wrong (e.g. invalid match criteria or invalid parameter) catch the
                 * exception and perform a rollback. Then rethrow for error reporting.
                 */
                g.tx().rollback();
                throw e;
            }

        }

        /*
         * Optionally perform type filtering
         */

        if (performTypeFiltering)
        {
            gt = gt.has(PROPERTY_KEY_ENTITY_TYPE_NAME, within(validTypeNames));
        }


        List<EntityDetail> entities = new ArrayList<>();

        /*
         * Iterate the traversal
         */

        while (gt.hasNext())
        {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            EntityDetail entityDetail = new EntityDetail();
            try
            {
                /*
                 * Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                 */
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy)
                {
                    entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                    entities.add(entityDetail);
                }
            }
            catch (Exception e)
            {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(
                                entityDetail.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        g.tx().commit();

        return entities;
    }




    // findRelationshipsForTypes
    List<Relationship> findRelationshipsForTypes(List<String>                   validTypeNames,
                                                 String                         filterTypeName,
                                                 Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                                 Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                                 SearchProperties               searchProperties)

    throws InvalidParameterException,
           RepositoryErrorException,
           FunctionNotSupportedException

    {

        final String methodName = "findRelationshipsForTypes";

        boolean performTypeFiltering = filterTypeName != null;


        /*
         * Get the traversal started,...
         */
        GraphTraversalSource g = instanceGraph.traversal();
        GraphTraversal<Edge, Edge> gt = g.E().hasLabel("Relationship");


        if (searchProperties != null)
        {

            /*
             * The caller has provided SearchProperties so there is property matching to do.
             *
             * searchProperties is a SearchProperties object which contains MatchCriteria and a list of
             * PropertyCondition objects, which in turn may contain nestedConditions.
             *
             * The conditions are parsed and extracted to construct the necessary graph traversals.
             *
             * The rules governing searchProperties are that a PropertyCondition EITHER specifies a property
             * value, condition and operator OR it specifies none of them and instead provides a nested
             * SearchProperties. Each property condition is validated to establish its correctness and to
             * determine whether it represents a local or nested condition.
             *
             * Processing starts at the top level of the SearchProperties and iterates over the list of
             * PropertyConditions. Each PropertyCondition is either processed as a local condition (it has
             * property, value, operator) or recursively as a nested condition. For a nested condition the
             * process visits the nested SearchProperties and repeats the above logic. At each stage in the
             * iteration and recursion a list of graph traversal criteria is constructed, and subsequently
             * combined using the MatchCriteria applicable to that level in the SearchProperties.
             */



            /*
             * Process the search properties for all the valid types - i.e. against the above maps.
             */
            try
            {
                List<GraphTraversal<Edge, Edge>> propCriteria = processRelationshipSearchPropertiesForTypes(searchProperties,
                                                                                                      qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                                      shortPropertyNameToQualifiedPropertyNames);

                /*
                 * Use the MatchCriteria to combine the properCriteria into the overall graph traversal.
                 *
                 * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
                 * between the match properties and the properties defined on the type (core or type defined). So
                 * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
                 * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
                 * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
                 * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
                 * entity) so let that case continue.
                 */

                switch (searchProperties.getMatchCriteria())
                {
                    case ALL:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case ANY:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case NONE:
                        if (!propCriteria.isEmpty())
                        {
                            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
                            t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                            gt = gt.not(t);
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    default:
                        /*
                         * Invalid match criteria. Throw InvalidParameterException.
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");
                }
            }
            catch (Exception e)
            {
                /*
                 * If anything went wrong (e.g. invalid match criteria or invalid parameter) catch the
                 * exception and perform a rollback. Then rethrow for error reporting.
                 */
                g.tx().rollback();
                throw e;
            }

        }

        /*
         * Optionally perform type filtering
         */

        if (performTypeFiltering)
        {
            gt = gt.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, within(validTypeNames));
        }


        List<Relationship> relationships = new ArrayList<>();

        /*
         * Iterate the traversal
         */

        while (gt.hasNext())
        {
            Edge edge = gt.next();
            log.debug("{} found edge {}", methodName, edge);
            Relationship relationship = new Relationship();
            try
            {
                relationshipMapper.mapEdgeToRelationship(edge, relationship);

                // Set the relationship ends...

                Vertex vertexOne = edge.outVertex();
                Vertex vertexTwo = edge.inVertex();

                // Doesn't matter whether vertices represent proxy entities or full entities - retrieve the entities as proxies
                if (vertexOne != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexOne);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexOne, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }
                if (vertexTwo != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexTwo);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexTwo, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            }
            catch (Exception e)
            {
                log.error("{} Caught exception from relationship or entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(
                                relationship.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }

            relationships.add(relationship);
        }

        g.tx().commit();

        return relationships;
    }





    // findRelationshipsForType
    List<Relationship> findRelationshipsForType(String              typeDefName,
                                                SearchProperties    searchProperties,
                                                boolean             fullMatch)

    throws InvalidParameterException,
           RepositoryErrorException,
           FunctionNotSupportedException

    {

        final String methodName = "findRelationshipsForType";

        List<Relationship> relationships = new ArrayList<>();


        /*
         * Even if there are no search properties specified, the method performs a traversal.
         */

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Edge, Edge> gt = g.E().hasLabel("Relationship");
        if (typeDefName != null)
        {
            gt = gt.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, typeDefName);
        }

        if (searchProperties != null)
        {

            /*
             * searchProperties is a SearchProperties object which contains MatchCriteria and a list of
             * PropertyCondition objects, which in turn may contain nestedConditions. The rules around
             * searchProperties is that a PropertyCondition EITHER specifies a property value, condition
             * and operator OR it specifies none of them and instead provides a nested SearchProperties.
             *
             * Extract the conditions and construct the necessary graph traversals.
             *
             * Start at the top of the searchProperties and iterate over the list of PropertyConditions.
             * For each PropertyCondition, either process it (property, value, operator) or recursively
             * visit the nested SearchProperties it contains. At each stage in the iteration and recursion
             * add each discovered condition to the graph traversal.
             */

            try
            {

                List<GraphTraversal<Edge, Edge>> propCriteria = processRelationshipSearchPropertiesForType(typeDefName, searchProperties, fullMatch);


                /*
                 * Use the MatchCriteria to combine the properCriteria into the overall graph traversal.
                 *
                 * If matchProps is not null and matchCriteria is ALL or ANY we need to have some overlap at least
                 * between the match properties and the properties defined on the type (core or type defined). So
                 * it is essential that propCriteria is not empty. For example, suppose this is a find... ByPropertyValue
                 * with searchCriteria, in which only string properties will be included in the MatchProperties. If the
                 * type has no string properties then there is no overlap and it is impossible for ALL or ANY matches to
                 * be satisfied. For matchCriteria NONE we need to retrieve the vertex from the graph (to construct the
                 * entity) so let that case continue.
                 */

                switch (searchProperties.getMatchCriteria())
                {
                    case ALL:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.and(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case ANY:
                        if (!propCriteria.isEmpty())
                        {
                            gt = gt.or(propCriteria.toArray(new GraphTraversal[0]));
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    case NONE:
                        if (!propCriteria.isEmpty())
                        {
                            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
                            t = t.or(propCriteria.toArray(new GraphTraversal[0]));
                            gt = gt.not(t);
                        }
                        log.debug("{} traversal looks like this --> {} ", methodName, gt);
                        break;

                    default:
                        /*
                         * Invalid match criteria. Throw InvalidParameterException.
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");
                }
            }
            catch (Exception e)
            {
                /*
                 * If anything went wrong (e.g. invalid match criteria or invalid parameter) catch the
                 * exception and perform a rollback. Then rethrow for error reporting.
                 */
                g.tx().rollback();
                throw e;
            }
        }

        while (gt.hasNext())
        {
            Edge edge = gt.next();
            log.debug("{} found edge {}", methodName, edge);
            Relationship relationship = new Relationship();
            try
            {
                relationshipMapper.mapEdgeToRelationship(edge, relationship);

                // Set the relationship ends...

                Vertex vertexOne = edge.outVertex();
                Vertex vertexTwo = edge.inVertex();

                // Doesn't matter whether vertices represent proxy entities or full entities - retrieve the entities as proxies
                if (vertexOne != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexOne);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexOne, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }
                if (vertexTwo != null)
                {
                    log.debug("{} entity vertex {}", methodName, vertexTwo);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexTwo, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            }
            catch (Exception e)
            {
                log.error("{} Caught exception from relationship or entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                throw new RepositoryErrorException(
                        GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(
                                relationship.getGUID(), methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName, e);
            }

            relationships.add(relationship);
        }

        g.tx().commit();

        return relationships;

    }



    /*
     * Parse the conditions in the search properties into a list of propCriteria. Do not combine them
     * in this method, that is done by the caller.
     */
    private List<GraphTraversal<Vertex, Vertex>> processEntitySearchPropertiesForType(String                           typeDefName,
                                                                                      SearchProperties                 searchProperties,
                                                                                      boolean                          fullMatch)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        final String methodName = "processEntitySearchPropertiesForType";

        List<GraphTraversal<Vertex, Vertex>> propCriteria = new ArrayList<>();

        List<PropertyCondition> conditions = searchProperties.getConditions();
        for (PropertyCondition condition : conditions)
        {
            boolean localCondition = validatePropertyCondition(condition);

            /*
             * Condition is valid, and localCondition indicates whether to process property, value, operator or to
             * recurse into nestedConditions.
             */

            if (localCondition)
            {

                /* Construct a traversal for the property name, operator and value */
                String propertyName = condition.getProperty();
                PropertyComparisonOperator operator = condition.getOperator();
                InstancePropertyValue value = condition.getValue();

                GraphTraversal<Vertex, Vertex> propertyCriterion = parseEntityPropertyConditionToCriterionForType(typeDefName, propertyName, operator, value, fullMatch);

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }

            }
            else
            {
                /*
                 * Need to process nested conditions....
                 */
                SearchProperties nestedConditions = condition.getNestedConditions();

                List<GraphTraversal<Vertex, Vertex>> subCriteria = processEntitySearchPropertiesForType(typeDefName, nestedConditions, fullMatch);

                GraphTraversal<Vertex, Vertex> propertyCriterion = new DefaultGraphTraversal<>();

                switch (nestedConditions.getMatchCriteria())
                {
                    case ALL:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion =  propertyCriterion.and(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case ANY:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion =  propertyCriterion.or(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case NONE:
                        if (!subCriteria.isEmpty())
                        {
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                            t = t.or(subCriteria.toArray(new GraphTraversal[0]));
                            propertyCriterion = propertyCriterion.not(t);
                        }
                        break;

                    default:
                        /*
                         * Invalid combination of conditions. Throw InvalidParameterException
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");

                }

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }
            }
        }
        return propCriteria;
    }


    /*
     * processRelationshipSearchPropertiesForType
     *
     * Parse the conditions in the search properties into a list of propCriteria. Do not combine them
     * in this method, that is done by the caller.
     */
    private List<GraphTraversal<Edge, Edge>> processRelationshipSearchPropertiesForType(String                           typeDefName,
                                                                                        SearchProperties                 searchProperties,
                                                                                        boolean                          fullMatch)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        final String methodName = "processRelationshipSearchPropertiesForType";

        List<GraphTraversal<Edge, Edge>> propCriteria = new ArrayList<>();

        List<PropertyCondition> conditions = searchProperties.getConditions();
        for (PropertyCondition condition : conditions)
        {
            boolean localCondition = validatePropertyCondition(condition);

            /*
             * Condition is valid, and localCondition indicates whether to process property, value, operator or to
             * recurse into nestedConditions.
             */

            if (localCondition)
            {

                /* Construct a traversal for the property name, operator and value */
                String propertyName = condition.getProperty();
                PropertyComparisonOperator operator = condition.getOperator();
                InstancePropertyValue value = condition.getValue();

                GraphTraversal<Edge, Edge> propertyCriterion = parseRelationshipPropertyConditionToCriterionForType(typeDefName, propertyName, operator, value, fullMatch);

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }

            }
            else
            {
                /*
                 * Need to process nested conditions....
                 */
                SearchProperties nestedConditions = condition.getNestedConditions();

                List<GraphTraversal<Edge, Edge>> subCriteria = processRelationshipSearchPropertiesForType(typeDefName, nestedConditions, fullMatch);

                GraphTraversal<Edge, Edge> propertyCriterion = new DefaultGraphTraversal<>();

                switch (nestedConditions.getMatchCriteria())
                {
                    case ALL:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion =  propertyCriterion.and(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case ANY:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion =  propertyCriterion.or(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case NONE:
                        if (!subCriteria.isEmpty())
                        {
                            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
                            t = t.or(subCriteria.toArray(new GraphTraversal[0]));
                            propertyCriterion = propertyCriterion.not(t);
                        }
                        break;

                    default:
                        /*
                         * Invalid combination of conditions. Throw InvalidParameterException
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");

                }

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }
            }
        }
        return propCriteria;
    }






    /*
     * Parse the conditions in the search properties into a list of propCriteria. Do not combine them
     * in this method, that is done by the caller.
     */
    private List<GraphTraversal<Vertex, Vertex>> processEntitySearchPropertiesForTypes(SearchProperties                searchProperties,
                                                                                       Map<String, TypeDefAttribute>    qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                       Map<String, List<String>>        shortPropertyNameToQualifiedPropertyNames)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        final String methodName = "processEntitySearchProperties";


        List<GraphTraversal<Vertex, Vertex>> propCriteria = new ArrayList<>();

        List<PropertyCondition> conditions = searchProperties.getConditions();
        for (PropertyCondition condition : conditions)
        {
            boolean localCondition = validatePropertyCondition(condition);

            /*
             * Condition is valid, and localCondition indicates whether to process property, value, operator or to
             * recurse into nestedConditions.
             */

            if (localCondition)
            {
                /* Construct a traversal for the property name, operator and value */
                String propertyName = condition.getProperty();
                PropertyComparisonOperator operator = condition.getOperator();
                InstancePropertyValue value = condition.getValue();


                GraphTraversal<Vertex, Vertex> propertyCriterion = parseEntityPropertyConditionToCriterionForTypes(propertyName,
                                                                                                                   qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                                                   shortPropertyNameToQualifiedPropertyNames,
                                                                                                                   operator,
                                                                                                                   value);

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }

            }
            else
            {
                /*
                 * Recursively process nested conditions....
                 */
                SearchProperties nestedConditions = condition.getNestedConditions();

                List<GraphTraversal<Vertex, Vertex>> subCriteria = processEntitySearchPropertiesForTypes(nestedConditions,
                                                                                                         qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                                         shortPropertyNameToQualifiedPropertyNames);

                GraphTraversal<Vertex, Vertex> propertyCriterion = new DefaultGraphTraversal<>();

                switch (nestedConditions.getMatchCriteria())
                {
                    case ALL:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion = propertyCriterion.and(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case ANY:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion = propertyCriterion.or(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case NONE:
                        if (!subCriteria.isEmpty())
                        {
                            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
                            t = t.or(subCriteria.toArray(new GraphTraversal[0]));
                            propertyCriterion = propertyCriterion.not(t);
                        }
                        break;

                    default:
                        /*
                         * Invalid combination of conditions. Throw InvalidParameterException
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");

                }

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }
            }
        }
        return propCriteria;
    }






    /*
     * Parse the conditions in the search properties into a list of propCriteria. Do not combine them
     * in this method, that is done by the caller.
     */
    private List<GraphTraversal<Edge, Edge>> processRelationshipSearchPropertiesForTypes(SearchProperties                searchProperties,
                                                                                         Map<String, TypeDefAttribute>    qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                         Map<String, List<String>>        shortPropertyNameToQualifiedPropertyNames)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        final String methodName = "processEntitySearchProperties";


        List<GraphTraversal<Edge, Edge>> propCriteria = new ArrayList<>();

        List<PropertyCondition> conditions = searchProperties.getConditions();
        for (PropertyCondition condition : conditions)
        {
            boolean localCondition = validatePropertyCondition(condition);

            /*
             * Condition is valid, and localCondition indicates whether to process property, value, operator or to
             * recurse into nestedConditions.
             */

            if (localCondition)
            {
                /* Construct a traversal for the property name, operator and value */
                String propertyName = condition.getProperty();
                PropertyComparisonOperator operator = condition.getOperator();
                InstancePropertyValue value = condition.getValue();


                GraphTraversal<Edge, Edge> propertyCriterion = parseRelationshipPropertyConditionToCriterionForTypes(propertyName,
                                                                                                                     qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                                                     shortPropertyNameToQualifiedPropertyNames,
                                                                                                                     operator,
                                                                                                                     value);

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }

            }
            else
            {
                /*
                 * Recursively process nested conditions....
                 */
                SearchProperties nestedConditions = condition.getNestedConditions();

                List<GraphTraversal<Edge, Edge>> subCriteria = processRelationshipSearchPropertiesForTypes(nestedConditions,
                                                                                                           qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                                           shortPropertyNameToQualifiedPropertyNames);

                GraphTraversal<Edge, Edge> propertyCriterion = new DefaultGraphTraversal<>();

                switch (nestedConditions.getMatchCriteria())
                {
                    case ALL:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion = propertyCriterion.and(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case ANY:
                        if (!subCriteria.isEmpty())
                        {
                            propertyCriterion = propertyCriterion.or(subCriteria.toArray(new GraphTraversal[0]));
                        }
                        break;

                    case NONE:
                        if (!subCriteria.isEmpty())
                        {
                            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
                            t = t.or(subCriteria.toArray(new GraphTraversal[0]));
                            propertyCriterion = propertyCriterion.not(t);
                        }
                        break;

                    default:
                        /*
                         * Invalid combination of conditions. Throw InvalidParameterException
                         * The calling method will handle transaction rollback (and surface the error).
                         */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_MATCH_CRITERIA.getMessageDefinition(
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "searchProperties");

                }

                if (propertyCriterion != null)
                {
                    propCriteria.add(propertyCriterion);
                }
            }
        }
        return propCriteria;
    }



    /*
     * Verify that EITHER
     *   property, value and operator are all set and nestedConditions is null
     * OR
     *   nestedConditions is set and the others are null
     */

    // This method is package private - it is used locally and also by the GraphOMRSMetadataCollection class
    boolean validatePropertyCondition(PropertyCondition condition)

    throws InvalidParameterException
    {
        final String methodName = "validatePropertyCondition";
        boolean localCondition;

        String propertyName = condition.getProperty();
        PropertyComparisonOperator operator = condition.getOperator();
        InstancePropertyValue value = condition.getValue();
        SearchProperties nestedConditions = condition.getNestedConditions();

        if (propertyName != null && operator != null && value != null && nestedConditions == null)
        {
            localCondition = true;
        }
        else if (propertyName == null && operator == null && value == null && nestedConditions != null)
        {
            localCondition = false;
        }
        else
        {
            /*
             * Invalid combination of conditions. Throw InvalidParameterException
             */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_PROPERTY_CONDITION.getMessageDefinition(
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "searchProperties");
        }
        return localCondition;
    }



    private GraphTraversal<Vertex, Vertex> parseEntityPropertyConditionToCriterionForType(String                      typeDefName,
                                                                                         String                      propName,
                                                                                         PropertyComparisonOperator  operator,
                                                                                         InstancePropertyValue       value,
                                                                                         boolean                     fullMatch)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        String methodName = "parseEntityPropertyConditionToCriterion";

        /*
         * Use a separate method to handle the IN operator
         */



        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */

        /*
         * When JanusGraph 0.6.0 is available and Egeria updates to that version,
         * it will be possible to support the IN operator. Until then there is a
         * JanusGraph issue (#2188) which will prevent any use of containment predicates
         * when nested or wrapped under an AND or OR (and probably a NONE) step. Since
         * the SearchProperties will always introduce this nesting or wrapping, we need to
         * wait for 0.6.0.
         * In the meantime, Egeria has been tested with a modified traversal that emulates
         * the processing of the IN operator but avoided the nesting within an AND, OR or NOT
         * predicate. In the simulated test, the traversal was for a set of dates expressed
         * as Longs) and tested against a type-defined attribute (i.e. the property is stored
         * as a Long).
         *   Long[] longArray = {123456789L ,  987654321L};
         *   gt = gt.has("veToDoxdueTime", within(longArray));
         * The success of this test suggests that the contains predicates will support the
         * IN operator once JG is at 0.6.0.
         */

        if (operator == IN)
        {
            /* TODO - WHEN JG 0.6.0 IS AVAILABLE UNCOMMENT THE FOLLOWING LINE... (and remove the throw clause below) */
            //return parseEntityInOperatorToCriterion(typeDefName, propName, value);

            /* UNTIL THEN THROW A FunctionNotSupportedException */
            throw new FunctionNotSupportedException(
                    GraphOMRSErrorCode.UNSUPPORTED_SEARCH_PROPERTY_OPERATOR.getMessageDefinition(
                            operator.toString(),
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }

        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */

        GraphTraversal<Vertex, Vertex> propertyCriterion = null;

        /*
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
         */
        Set<String> corePropertyNames = corePropertiesEntity.keySet();
        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);
        Set<String> typeDefinedPropertyNames = qualifiedPropertyNames.keySet();


        String propNameInGraph = null;

        /*
         * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
         */
        GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

        /*
         * Check if this is a core property (from InstanceAuditHeader)
         * Core properties take precedence over TDAs (in the event of a name clash)
         */

        if (corePropertyNames.contains(propName))
        {

            /*
             * Treat the match property as a reference to a core property
             *
             * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
             * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
             * primitive def category of string.
             *
             * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
             * The type of the supplied property needs to be compared to the type of the core property.
             */
            String javaTypeForMatchProperty = null;
            PrimitiveDefCategory mpCat;
            InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
            {
                PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                mpCat = ppv.getPrimitiveDefCategory();
                javaTypeForMatchProperty = mpCat.getJavaClassName();
            }
            else
            {
                log.debug("{} non-primitive match property {} ignored", methodName, propName);
            }

            String javaTypeForCoreProperty = corePropertyTypes.get(propName);

            if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
            {
                /*
                 * If the types are the same, then this is good to go. There is also one case where they may differ
                 * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                 * whereas a Date match property is a;most certainly going to be specified as a java.lang.Long.
                 * This should be OK - we can accept the type difference, provided we convert the match property
                 * to a Date before using it in the traversal (which is in the switch statement further on)
                 */

                if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                        (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                {
                    /*
                     * Types match, OK to include the property
                     */
                    propNameInGraph = PROPERTY_KEY_PREFIX_ENTITY + propName;
                    mapping = corePropertyMixedIndexMappings.get(propNameInGraph);
                }
                else
                {

                    throw new InvalidParameterException(
                            GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                    propName,
                                    javaTypeForMatchProperty,
                                    javaTypeForCoreProperty,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName,
                            "matchProperties");
                }
            }
        }
        else if (typeDefinedPropertyNames.contains(propName))
        {
            /*
             * Treat the match property as a reference to a type-defined property. Check that it's type matches the TDA.
             */

            List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

            for (TypeDefAttribute propertyDef : propertiesDef)
            {
                String definedPropertyName = propertyDef.getAttributeName();
                if (definedPropertyName.equals(propName))
                {

                    /*
                     * The match property name matches the name of a type-defined attribute
                     *
                     * Check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                        mpCat = ppv.getPrimitiveDefCategory();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }

                    PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    AttributeTypeDef atd = propertyDef.getAttributeType();
                    AttributeTypeDefCategory atdCat = atd.getCategory();
                    if (atdCat == PRIMITIVE)
                    {
                        PrimitiveDef pdef = (PrimitiveDef) atd;
                        pdCat = pdef.getPrimitiveDefCategory();
                    }

                    if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                    {
                        /*
                         * Types match
                         */
                        /*
                         * Sort out the qualification and prefixing of the property name ready for graph search
                         */
                        String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                        propNameInGraph = PROPERTY_KEY_PREFIX_ENTITY + qualifiedPropertyName;
                        mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

                    }
                    /*
                     * If types matched the code above will have set propNameInGraph. If the types did not match we should ignore
                     * this property - it may be valid for a different type where the find is across multiple types. In either
                     * case break out of the property for loop and drop through to catch all below.
                     */
                    break;
                }
            }
            /*
             * if (!propertyFound) - The match property is not a supported, known type-defined property or does not have
             * correct type - drop into the catch all below.
             */
        }

        if (propNameInGraph != null)
        {
            /*
             * Incorporate the property (propNameInGraph) into a propCriterion for the traversal...
             */

            InstancePropertyCategory ipvCat = value.getInstancePropertyCategory();
            if (ipvCat == InstancePropertyCategory.PRIMITIVE)
            {
                PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                Object primValue = ppv.getPrimitiveValue();
                log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                GraphTraversal<Vertex, Vertex> t;

                switch (pCat)
                {
                    case OM_PRIMITIVE_TYPE_STRING:
                        t = vertexApplyOperatorToString(propNameInGraph, operator, primValue, mapping, fullMatch);
                        break;

                    case OM_PRIMITIVE_TYPE_DATE:
                        t = vertexApplyOperatorToDate(propNameInGraph, operator, primValue, propName, pCat);
                        break;

                    default:
                        t = vertexApplyOperatorToObject(propNameInGraph, operator, primValue);
                        break;
                }
                log.debug("{} primitive search property has property criterion {}", methodName, t);
                propertyCriterion = t;
            }
            else
            {
                log.debug("{} non-primitive match property {} ignored", methodName, propName);
            }
        }
        else
        {
            /*
             * The property name is neither known as a core property or type-defined property.
             * The traversal should fail if MatchCriteria is ALL but due to nesting that is
             * always applied at the next higher level on the tree. Therefore must include a
             * step that is guaranteed to fail - but not cause a graph key error. This will
             * cause this traverser to be filtered. The 'none' step was considered but is not
             * generally recommended.
             * There is no property that corresponds to the 'short' name provided by the caller.
             * It is not feasible to emulate an imagined property because this will cause a key
             * error in the graph db. Instead, use an existing property but with a value that is
             * known to be impossible.
             */
            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
            t = t.has(PROPERTY_KEY_ENTITY_TYPE_NAME,"InvalidTypeForProperty"+propName);
            propertyCriterion = t;

        }
        return propertyCriterion;
    }






    private GraphTraversal<Vertex, Vertex> parseEntityPropertyConditionToCriterionForTypes(String                        propName,
                                                                                           Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                           Map<String, List<String>>     shortPropertyNameToQualifiedPropertyNames,
                                                                                           PropertyComparisonOperator    operator,
                                                                                           InstancePropertyValue         value)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        String methodName = "parseEntityPropertyConditionToCriterion";

        /*
         * Use a separate method to handle the IN operator
         */



        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */

        /*
         * When JanusGraph 0.6.0 is available and Egeria updates to that version,
         * it will be possible to support the IN operator. Until then there is a
         * JanusGraph issue (#2188) which will prevent any use of containment predicates
         * when nested or wrapped under an AND or OR (and probably a NONE) step. Since
         * the SearchProperties will always introduce this nesting or wrapping, we need to
         * wait for 0.6.0.
         * In the meantime, Egeria has been tested with a modified traversal that emulates
         * the processing of the IN operator but avoided the nesting within an AND, OR or NOT
         * predicate. In the simulated test, the traversal was for a set of dates expressed
         * as Longs) and tested against a type-defined attribute (i.e. the property is stored
         * as a Long).
         *   Long[] longArray = {123456789L ,  987654321L};
         *   gt = gt.has("veToDoxdueTime", within(longArray));
         * The success of this test suggests that the contains predicates will support the
         * IN operator once JG is at 0.6.0.
         */

        if (operator == IN)
        {
            /* TODO - WHEN JG 0.6.0 IS AVAILABLE UNCOMMENT THE FOLLOWING LINE... (and remove the throw clause below) */
            //return parseEntityInOperatorToCriterion(typeDefName, propName, value);

            /* UNTIL THEN THROW A FunctionNotSupportedException */
            throw new FunctionNotSupportedException(
                    GraphOMRSErrorCode.UNSUPPORTED_SEARCH_PROPERTY_OPERATOR.getMessageDefinition(
                            operator.toString(),
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }

        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */

        GraphTraversal<Vertex, Vertex> propertyCriterion = null;


        Set<String> typeDefinedPropertyNames = null;
        if (!shortPropertyNameToQualifiedPropertyNames.isEmpty())
        {
            typeDefinedPropertyNames = shortPropertyNameToQualifiedPropertyNames.keySet();
        }

        Map<String, GraphOMRSGraphFactory.MixedIndexMapping> matchedPropToMapping = new HashMap<>();


        /*
         * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
         */
        GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;


        /*
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined
         * attributes (including inherited attributes). This sequence means that core properties take precedence over TDAs
         * (in the event of a name clash)
         */


        /*
         * Check if this is a core property (from InstanceAuditHeader)
         */
        Set<String> corePropertyNames = corePropertiesEntity.keySet();

        if (corePropertyNames.contains(propName))
        {

            /*
             * Treat the match property as a reference to a core property
             *
             * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
             * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
             * primitive def category of string.
             *
             * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
             * The type of the supplied property needs to be compared to the type of the core property.
             */
            String javaTypeForMatchProperty = null;
            PrimitiveDefCategory mpCat;
            InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
            {
                PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                mpCat = ppv.getPrimitiveDefCategory();
                javaTypeForMatchProperty = mpCat.getJavaClassName();
            }
            else
            {
                log.debug("{} non-primitive match property {} ignored", methodName, propName);
            }

            String javaTypeForCoreProperty = corePropertyTypes.get(propName);

            if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
            {
                /*
                 * If the types are the same, then this is good to go. There is also one case where they may differ
                 * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                 * whereas a Date match property is a;most certainly going to be specified as a java.lang.Long.
                 * This should be OK - we can accept the type difference, provided we convert the match property
                 * to a Date before using it in the traversal (which is in the switch statement further on)
                 */

                if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                        (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                {
                    /*
                     * Types match, OK to include the property
                     */
                    String matchedPropName = PROPERTY_KEY_PREFIX_ENTITY + propName;
                    mapping = corePropertyMixedIndexMappings.get(matchedPropName);
                    matchedPropToMapping.put(matchedPropName, mapping);
                }
                else
                {

                    throw new InvalidParameterException(
                            GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                    propName,
                                    javaTypeForMatchProperty,
                                    javaTypeForCoreProperty,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName,
                            "matchProperties");
                }
            }
        }

        else if (typeDefinedPropertyNames != null && typeDefinedPropertyNames.contains(propName))
        {

            /*
             * Treat the match property as a reference to a type-defined property. Check that it's type matches the
             * definiiton in the TDA's ATD.
             */

            List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(propName);

            if (qNameList != null && !qNameList.isEmpty())
            {

                // This method cannot handle dups so just take the first element of the list.
                String qualifiedName = qNameList.get(0);
                if (qualifiedName != null)
                {
                    /*
                     * For the qualifiedName perform type checking between the match property and TDA
                     */

                    TypeDefAttribute propertyDef = qualifiedPropertyNameToTypeDefinedAttribute.get(qualifiedName);
                    AttributeTypeDef atd = propertyDef.getAttributeType();

                    PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    AttributeTypeDefCategory atdCat = atd.getCategory();
                    if (atdCat == PRIMITIVE)
                    {
                        PrimitiveDef pdef = (PrimitiveDef) atd;
                        pdCat = pdef.getPrimitiveDefCategory();
                    }

                    PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                        mpCat = ppv.getPrimitiveDefCategory();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }

                    if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                    {
                        /*
                         * Types match.
                         * Qualify and prefix the property name ready for graph search
                         */
                        String matchedPropName = PROPERTY_KEY_PREFIX_ENTITY + qualifiedName;
                        mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        matchedPropToMapping.put(matchedPropName, mapping);
                    }
                }
            }
        }

        /*
         * If the matchedPropToMapping map is empty then there were no property matches. This means that
         * the match property is not a supported, known type-defined property or does not have correct type.
         *
         * Otherwise iterate over the map and process each property.
         */

        if (!matchedPropToMapping.isEmpty())
        {
            /*
             * Iterate over the map and incorporate each property (matchedPropName) into propCriteria for the traversal...
             */

            List<GraphTraversal<Vertex, Vertex>> localCriteria = new ArrayList<>();

            Set<String> matchedPropNames = matchedPropToMapping.keySet();
            Iterator<String> matchPropNameIterator = matchedPropNames.iterator();

            while (matchPropNameIterator.hasNext())
            {
                String thisMatchedPropName = matchPropNameIterator.next();

                /*
                 * Incorporate the property (propNameInGraph) into a propCriterion for the traversal...
                 */

                InstancePropertyCategory ipvCat = value.getInstancePropertyCategory();
                if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                {
                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                    PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                    Object primValue = ppv.getPrimitiveValue();
                    log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                    GraphTraversal<Vertex, Vertex> t;

                    switch (pCat)
                    {
                        case OM_PRIMITIVE_TYPE_STRING:
                            t = vertexApplyOperatorToString(thisMatchedPropName, operator, primValue, mapping, true);
                            break;

                        case OM_PRIMITIVE_TYPE_DATE:
                            t = vertexApplyOperatorToDate(thisMatchedPropName, operator, primValue, propName, pCat);
                            break;

                        default:
                            t = vertexApplyOperatorToObject(thisMatchedPropName, operator, primValue);
                            break;
                    }
                    log.debug("{} primitive search property has property criterion {}", methodName, t);
                    localCriteria.add(t);
                }
                else
                {
                    log.debug("{} non-primitive match property {} ignored", methodName, propName);
                }
            }

            /* Add a criterion to the overall traversal */
            propertyCriterion = localCriteria.get(0);
        }
        else
        {
            /*
             * The property name is neither known as a core property or type-defined property.
             * The traversal should fail if MatchCriteria is ALL but due to nesting that is
             * always applied at the next higher level on the tree. Therefore must include a
             * step that is guaranteed to fail - but not cause a graph key error. This will
             * cause this traverser to be filtered. The 'none' step was considered but is not
             * generally recommended.
             * There is no property that corresponds to the 'short' name provided by the caller.
             * It is not feasible to emulate an imagined property because this will cause a key
             * error in the graph db. Instead, use an existing property but with a value that is
             * known to be impossible.
             */
            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
            t = t.has(PROPERTY_KEY_ENTITY_TYPE_NAME,"InvalidTypeForProperty"+propName);
            propertyCriterion = t;

        }
        return propertyCriterion;

    }




    private GraphTraversal<Edge, Edge> parseRelationshipPropertyConditionToCriterionForType(String                      typeDefName,
                                                                                            String                      propName,
                                                                                            PropertyComparisonOperator  operator,
                                                                                            InstancePropertyValue       value,
                                                                                            boolean                     fullMatch)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        String methodName = "parseRelationshipPropertyConditionToCriterion";

        /*
         * Use a separate method to handle the IN operator
         */



        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */

        /*
         * When JanusGraph 0.6.0 is available and Egeria updates to that version,
         * it will be possible to support the IN operator. Until then there is a
         * JanusGraph issue (#2188) which will prevent any use of containment predicates
         * when nested or wrapped under an AND or OR (and probably a NONE) step. Since
         * the SearchProperties will always introduce this nesting or wrapping, we need to
         * wait for 0.6.0.
         * In the meantime, Egeria has been tested with a modified traversal that emulates
         * the processing of the IN operator but avoided the nesting within an AND, OR or NOT
         * predicate. In the simulated test, the traversal was for a set of dates expressed
         * as Longs) and tested against a type-defined attribute (i.e. the property is stored
         * as a Long).
         *   Long[] longArray = {123456789L ,  987654321L};
         *   gt = gt.has("veToDoxdueTime", within(longArray));
         * The success of this test suggests that the contains predicates will support the
         * IN operator once JG is at 0.6.0.
         */

        if (operator == IN)
        {
            /* TODO - WHEN JG 0.6.0 IS AVAILABLE UNCOMMENT THE FOLLOWING LINE... (and remove the throw clause below) */
            //return parseEntityInOperatorToCriterion(typeDefName, propName, value);

            /* UNTIL THEN THROW A FunctionNotSupportedException */
            throw new FunctionNotSupportedException(
                    GraphOMRSErrorCode.UNSUPPORTED_SEARCH_PROPERTY_OPERATOR.getMessageDefinition(
                            operator.toString(),
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }

        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */



        GraphTraversal<Edge, Edge> propertyCriterion = null;

        /*
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined attributes (including inherited attributes)
         */
        Set<String> corePropertyNames = corePropertiesRelationship.keySet();
        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);
        Set<String> typeDefinedPropertyNames = qualifiedPropertyNames.keySet();


        String propNameInGraph = null;

        /*
         * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
         */
        GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

        /*
         * Check if this is a core property (from InstanceAuditHeader)
         * Core properties take precedence over TDAs (in the event of a name clash)
         */

        if (corePropertyNames.contains(propName))
        {

            /*
             * Treat the match property as a reference to a core property
             *
             * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
             * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
             * primitive def category of string.
             *
             * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
             * The type of the supplied property needs to be compared to the type of the core property.
             */
            String javaTypeForMatchProperty = null;
            PrimitiveDefCategory mpCat;
            InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
            {
                PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                mpCat = ppv.getPrimitiveDefCategory();
                javaTypeForMatchProperty = mpCat.getJavaClassName();
            }
            else
            {
                log.debug("{} non-primitive match property {} ignored", methodName, propName);
            }

            String javaTypeForCoreProperty = corePropertyTypes.get(propName);

            if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
            {
                /*
                 * If the types are the same, then this is good to go. There is also one case where they may differ
                 * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                 * whereas a Date match property is a;most certainly going to be specified as a java.lang.Long.
                 * This should be OK - we can accept the type difference, provided we convert the match property
                 * to a Date before using it in the traversal (which is in the switch statement further on)
                 */

                if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                        (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                {
                    /*
                     * Types match, OK to include the property
                     */
                    propNameInGraph = PROPERTY_KEY_PREFIX_RELATIONSHIP + propName;
                    mapping = corePropertyMixedIndexMappings.get(propNameInGraph);
                }
                else
                {

                    throw new InvalidParameterException(
                            GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                    propName,
                                    javaTypeForMatchProperty,
                                    javaTypeForCoreProperty,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName,
                            "matchProperties");
                }
            }
        }
        else if (typeDefinedPropertyNames.contains(propName))
        {
            /*
             * Treat the match property as a reference to a type-defined property. Check that it's type matches the TDA.
             */

            List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

            for (TypeDefAttribute propertyDef : propertiesDef)
            {
                String definedPropertyName = propertyDef.getAttributeName();
                if (definedPropertyName.equals(propName))
                {

                    /*
                     * The match property name matches the name of a type-defined attribute
                     *
                     * Check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                        mpCat = ppv.getPrimitiveDefCategory();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }

                    PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    AttributeTypeDef atd = propertyDef.getAttributeType();
                    AttributeTypeDefCategory atdCat = atd.getCategory();
                    if (atdCat == PRIMITIVE)
                    {
                        PrimitiveDef pdef = (PrimitiveDef) atd;
                        pdCat = pdef.getPrimitiveDefCategory();
                    }

                    if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                    {
                        /*
                         * Types match
                         */
                        /*
                         * Sort out the qualification and prefixing of the property name ready for graph search
                         */
                        String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                        propNameInGraph = PROPERTY_KEY_PREFIX_RELATIONSHIP + qualifiedPropertyName;
                        mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;

                    }
                    /*
                     * If types matched the code above will have set propNameInGraph. If the types did not match we should ignore
                     * this property - it may be valid for a different type where the find is across multiple types. In either
                     * case break out of the property for loop and drop through to catch all below.
                     */
                    break;
                }
            }
            /*
             * if (!propertyFound) - The match property is not a supported, known type-defined property or does not have
             * correct type - drop into the catch all below.
             */
        }

        if (propNameInGraph != null)
        {
            /*
             * Incorporate the property (propNameInGraph) into a propCriterion for the traversal...
             */

            InstancePropertyCategory ipvCat = value.getInstancePropertyCategory();
            if (ipvCat == InstancePropertyCategory.PRIMITIVE)
            {
                PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                Object primValue = ppv.getPrimitiveValue();
                log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                GraphTraversal<Edge, Edge> t;

                switch (pCat)
                {
                    case OM_PRIMITIVE_TYPE_STRING:
                        t = edgeApplyOperatorToString(propNameInGraph, operator, primValue, mapping, fullMatch);
                        break;

                    case OM_PRIMITIVE_TYPE_DATE:
                        t = edgeApplyOperatorToDate(propNameInGraph, operator, primValue, propName, pCat);
                        break;

                    default:
                        t = edgeApplyOperatorToObject(propNameInGraph, operator, primValue);
                        break;
                }
                log.debug("{} primitive search property has property criterion {}", methodName, t);
                propertyCriterion = t;
            }
            else
            {
                log.debug("{} non-primitive match property {} ignored", methodName, propName);
            }
        }
        else
        {
            /*
             * The property name is neither known as a core property or type-defined property.
             * The traversal should fail if MatchCriteria is ALL but due to nesting that is
             * always applied at the next higher level on the tree. Therefore must include a
             * step that is guaranteed to fail - but not cause a graph key error. This will
             * cause this traverser to be filtered. The 'none' step was considered but is not
             * generally recommended.
             * There is no property that corresponds to the 'short' name provided by the caller.
             * It is not feasible to emulate an imagined property because this will cause a key
             * error in the graph db. Instead, use an existing property but with a value that is
             * known to be impossible.
             */
            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
            t = t.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME,"InvalidTypeForProperty"+propName);
            propertyCriterion = t;

        }
        return propertyCriterion;
    }




    private GraphTraversal<Edge, Edge> parseRelationshipPropertyConditionToCriterionForTypes(String                        propName,
                                                                                             Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                             Map<String, List<String>>     shortPropertyNameToQualifiedPropertyNames,
                                                                                             PropertyComparisonOperator    operator,
                                                                                             InstancePropertyValue         value)

    throws InvalidParameterException,
           FunctionNotSupportedException
    {

        String methodName = "parseRelationshipPropertyConditionToCriterion";

        /*
         * Use a separate method to handle the IN operator
         */



        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */

        /*
         * When JanusGraph 0.6.0 is available and Egeria updates to that version,
         * it will be possible to support the IN operator. Until then there is a
         * JanusGraph issue (#2188) which will prevent any use of containment predicates
         * when nested or wrapped under an AND or OR (and probably a NONE) step. Since
         * the SearchProperties will always introduce this nesting or wrapping, we need to
         * wait for 0.6.0.
         * In the meantime, Egeria has been tested with a modified traversal that emulates
         * the processing of the IN operator but avoided the nesting within an AND, OR or NOT
         * predicate. In the simulated test, the traversal was for a set of dates expressed
         * as Longs) and tested against a type-defined attribute (i.e. the property is stored
         * as a Long).
         *   Long[] longArray = {123456789L ,  987654321L};
         *   gt = gt.has("veToDoxdueTime", within(longArray));
         * The success of this test suggests that the contains predicates will support the
         * IN operator once JG is at 0.6.0.
         */

        if (operator == IN)
        {
            /* TODO - WHEN JG 0.6.0 IS AVAILABLE UNCOMMENT THE FOLLOWING LINE... (and remove the throw clause below) */
            //return parseEntityInOperatorToCriterion(typeDefName, propName, value);

            /* UNTIL THEN THROW A FunctionNotSupportedException */
            throw new FunctionNotSupportedException(
                    GraphOMRSErrorCode.UNSUPPORTED_SEARCH_PROPERTY_OPERATOR.getMessageDefinition(
                            operator.toString(),
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName);
        }

        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */
        /* *************************** PLEASE NOTE **************************** */



        GraphTraversal<Edge, Edge> propertyCriterion = null;


        Set<String> typeDefinedPropertyNames = null;
        if (!shortPropertyNameToQualifiedPropertyNames.isEmpty())
        {
            typeDefinedPropertyNames = shortPropertyNameToQualifiedPropertyNames.keySet();
        }

        Map<String, GraphOMRSGraphFactory.MixedIndexMapping> matchedPropToMapping = new HashMap<>();


        /*
         * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
         */
        GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;


        /*
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined
         * attributes (including inherited attributes). This sequence means that core properties take precedence over TDAs
         * (in the event of a name clash)
         */


        /*
         * Check if this is a core property (from InstanceAuditHeader)
         */
        Set<String> corePropertyNames = corePropertiesRelationship.keySet();

        if (corePropertyNames.contains(propName))
        {

            /*
             * Treat the match property as a reference to a core property
             *
             * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
             * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
             * primitive def category of string.
             *
             * Validate the type - so that a more meaningful error message can be delivered to the user, instead of a mid-traversal complaint about an anonymous key.
             * The type of the supplied property needs to be compared to the type of the core property.
             */
            String javaTypeForMatchProperty = null;
            PrimitiveDefCategory mpCat;
            InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
            if (mpvCat == InstancePropertyCategory.PRIMITIVE)
            {
                PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                mpCat = ppv.getPrimitiveDefCategory();
                javaTypeForMatchProperty = mpCat.getJavaClassName();
            }
            else
            {
                log.debug("{} non-primitive match property {} ignored", methodName, propName);
            }

            String javaTypeForCoreProperty = corePropertyTypes.get(propName);

            if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
            {
                /*
                 * If the types are the same, then this is good to go. There is also one case where they may differ
                 * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                 * whereas a Date match property is a;most certainly going to be specified as a java.lang.Long.
                 * This should be OK - we can accept the type difference, provided we convert the match property
                 * to a Date before using it in the traversal (which is in the switch statement further on)
                 */

                if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                        (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                {
                    /*
                     * Types match, OK to include the property
                     */
                    String matchedPropName = PROPERTY_KEY_PREFIX_RELATIONSHIP + propName;
                    mapping = corePropertyMixedIndexMappings.get(matchedPropName);
                    matchedPropToMapping.put(matchedPropName, mapping);
                }
                else
                {

                    throw new InvalidParameterException(
                            GraphOMRSErrorCode.INVALID_MATCH_PROPERTY.getMessageDefinition(
                                    propName,
                                    javaTypeForMatchProperty,
                                    javaTypeForCoreProperty,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName,
                            "matchProperties");
                }
            }
        }

        else if (typeDefinedPropertyNames != null && typeDefinedPropertyNames.contains(propName))
        {

            /*
             * Treat the match property as a reference to a type-defined property. Check that it's type matches the
             * definiiton in the TDA's ATD.
             */

            List<String> qNameList = shortPropertyNameToQualifiedPropertyNames.get(propName);

            if (qNameList != null && !qNameList.isEmpty())
            {

                // This method cannot handle dups so just take the first element of the list.
                String qualifiedName = qNameList.get(0);
                if (qualifiedName != null)
                {
                    /*
                     * For the qualifiedName perform type checking between the match property and TDA
                     */

                    TypeDefAttribute propertyDef = qualifiedPropertyNameToTypeDefinedAttribute.get(qualifiedName);
                    AttributeTypeDef atd = propertyDef.getAttributeType();

                    PrimitiveDefCategory pdCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    AttributeTypeDefCategory atdCat = atd.getCategory();
                    if (atdCat == PRIMITIVE)
                    {
                        PrimitiveDef pdef = (PrimitiveDef) atd;
                        pdCat = pdef.getPrimitiveDefCategory();
                    }

                    PrimitiveDefCategory mpCat = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                    InstancePropertyCategory mpvCat = value.getInstancePropertyCategory();
                    if (mpvCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                        mpCat = ppv.getPrimitiveDefCategory();
                    }
                    else
                    {
                        log.debug("{} non-primitive match property {} ignored", methodName, propName);
                    }

                    if (mpCat == pdCat && mpCat != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN)
                    {
                        /*
                         * Types match.
                         * Qualify and prefix the property name ready for graph search
                         */
                        String matchedPropName = PROPERTY_KEY_PREFIX_RELATIONSHIP + qualifiedName;
                        mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        matchedPropToMapping.put(matchedPropName, mapping);
                    }
                }
            }
        }

        /*
         * If the matchedPropToMapping map is empty then there were no property matches. This means that
         * the match property is not a supported, known type-defined property or does not have correct type.
         *
         * Otherwise iterate over the map and process each property.
         */

        if (!matchedPropToMapping.isEmpty())
        {
            /*
             * Iterate over the map and incorporate each property (matchedPropName) into propCriteria for the traversal...
             */

            List<GraphTraversal<Edge, Edge>> localCriteria = new ArrayList<>();

            Set<String> matchedPropNames = matchedPropToMapping.keySet();
            Iterator<String> matchPropNameIterator = matchedPropNames.iterator();

            while (matchPropNameIterator.hasNext())
            {
                String thisMatchedPropName = matchPropNameIterator.next();

                /*
                 * Incorporate the property (propNameInGraph) into a propCriterion for the traversal...
                 */

                InstancePropertyCategory ipvCat = value.getInstancePropertyCategory();
                if (ipvCat == InstancePropertyCategory.PRIMITIVE)
                {
                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
                    PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                    Object primValue = ppv.getPrimitiveValue();
                    log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                    GraphTraversal<Edge, Edge> t;

                    switch (pCat)
                    {
                        case OM_PRIMITIVE_TYPE_STRING:
                            t = edgeApplyOperatorToString(thisMatchedPropName, operator, primValue, mapping, true);
                            break;

                        case OM_PRIMITIVE_TYPE_DATE:
                            t = edgeApplyOperatorToDate(thisMatchedPropName, operator, primValue, propName, pCat);
                            break;

                        default:
                            t = edgeApplyOperatorToObject(thisMatchedPropName, operator, primValue);
                            break;
                    }
                    log.debug("{} primitive search property has property criterion {}", methodName, t);
                    localCriteria.add(t);
                }
                else
                {
                    log.debug("{} non-primitive match property {} ignored", methodName, propName);
                }
            }

            /* Add a criterion to the overall traversal */
            propertyCriterion = localCriteria.get(0);
        }
        else
        {
            /*
             * The property name is neither known as a core property or type-defined property.
             * The traversal should fail if MatchCriteria is ALL but due to nesting that is
             * always applied at the next higher level on the tree. Therefore must include a
             * step that is guaranteed to fail - but not cause a graph key error. This will
             * cause this traverser to be filtered. The 'none' step was considered but is not
             * generally recommended.
             * There is no property that corresponds to the 'short' name provided by the caller.
             * It is not feasible to emulate an imagined property because this will cause a key
             * error in the graph db. Instead, use an existing property but with a value that is
             * known to be impossible.
             */
            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
            t = t.has(PROPERTY_KEY_ENTITY_TYPE_NAME,"InvalidTypeForProperty"+propName);
            propertyCriterion = t;

        }
        return propertyCriterion;

    }



    /*
     * The entity search contains a PropertyCondition that uses the 'IN' operator, which is for testing a property
     * against an array of acceptable values.
     */
    private  GraphTraversal<Vertex, Vertex>  parseEntityInOperatorToCriterion(String                      typeDefName,
                                                                              String                      propName,
                                                                              InstancePropertyValue       value)

    throws InvalidParameterException
    {

        String methodName = "parseEntityInOperatorToCriterion";

        /*
         * The strategy is to:
         *
         * a) check the supplied value is an Array and that it contains primitive elements that are all the same primitive category
         *
         * b) determine whether the specified property is a core or type-defined attribute and check that it is a primitive and has
         *    the same primitive category as the elements in the array.
         *
         * c) construct the graph traversal, using a within step.
         */


        /*
         * a) Validate the supplied match value is an array property and has elements that are primitive
         *    and all the same primitive category. Remember the primitive category.
         *
         *    If any condition fails, throw InvalidParameterException naming the property.
         *
         * For the IN operator the caller must supply an ARRAY of values against which to
         * compare the named primitive property.
         *
         * Before going much further, validate that the parameter is an array and that it has
         * only primitive elements and they are all the same primitive category.
         */

        ArrayPropertyValue apv = (ArrayPropertyValue)value;
        /*
         * Check the array is non-trivial - e.g. empty
         */
        if (apv.getArrayCount() <= 0)
        {
            /* Empty array - not supported */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                            propName,
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "value");
        }

        InstanceProperties arrayValues = apv.getArrayValues();
        Map<String, InstancePropertyValue> arrayElementProperties = arrayValues.getInstanceProperties();
        Collection<InstancePropertyValue> arrayPropValues = arrayElementProperties.values();

        PrimitiveDefCategory allElementsPrimCat = null;
        for (InstancePropertyValue elementValue : arrayPropValues)
        {
            /* Check the array element value is primitive and has the same primitive cat
             * as the first element.
             */
            InstancePropertyCategory elementCategory = elementValue.getInstancePropertyCategory();
            if (elementCategory == InstancePropertyCategory.PRIMITIVE)
            {
                /* Remember the prim cat of the first element */
                PrimitivePropertyValue primElementValue = (PrimitivePropertyValue) elementValue;
                PrimitiveDefCategory thisElementPrimCat = primElementValue.getPrimitiveDefCategory();
                if (allElementsPrimCat == null)
                {
                    /* All elements will have to same primitive category as this one... */
                    allElementsPrimCat = thisElementPrimCat;
                }
                else
                {
                    /* Check prim cat against other elements */
                    if (thisElementPrimCat != allElementsPrimCat)
                    {
                        /* Mixed array - not supported */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                                        propName,
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "value");
                    }
                }
            }
            else
            {
                /*
                 * No support for array elements that are not primitive
                 */
                throw new InvalidParameterException(
                        GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                                propName,
                                methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName,
                        "value");
            }
        }
        if (allElementsPrimCat == null)
        {
            /*
             * Failed to establish prim def cat for array elements
             */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                            propName,
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "value");
        }

        /*
         * It has been established that all array elements are primitive and of the same (known) primitive category
         */

        /*
         * b) determine whether the specified property is a core or type-defined attribute, check that it is a primitive and has
         *    the same primitive category as the elements in the array.
         */

        /*
         * Check if this is a core property (from InstanceAuditHeader)
         * Core properties take precedence over TDAs (in the event of a name clash)
         *
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined
         * attributes (including inherited attributes)
         */

        Set<String> corePropertyNames = corePropertiesEntity.keySet();
        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);
        Set<String> typeDefinedPropertyNames = qualifiedPropertyNames.keySet();

        boolean isCoreProperty;

        if (corePropertyNames.contains(propName))
        {
            isCoreProperty = true;
        }
        else if (typeDefinedPropertyNames.contains(propName))
        {
            isCoreProperty = false;
        }
        else
        {
            /*
             * Property not found - The match property is not a supported, known type-defined property.
             */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_NAME.getMessageDefinition(
                            propName,
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "propName");
        }


        /*
         * Check that the core property or type-defined attribute is a primitive. The graph repo only
         * supports containment checks of primitives to arrays of primitives.
         * Also check that the primitive category is the same as that of the elements in the supplied array.
         * THe result of this should be to set propNameInGraph and mapping to appropriate values.
         */


        /*
         * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
         */
        GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
        String propNameInGraph = null;

        if (isCoreProperty)
        {
            /*
             * The match property is a reference to a core property.
             * It is known that core properties are primitive.
             * The type (primitive category) of the supplied array elements needs to be compared to that of the core property.
             *
             * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
             * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
             * primitive def category of string. Therefore this method validates the actual (java) type - so that a more meaningful error message can be
             * delivered to the user, instead of a mid-traversal complaint referring to an anonymous key.
             */

            /*
             * It has already been established that the array contains one or more elements of prim cat allElementsPrimCat
             */

            String javaTypeForMatchProperty = allElementsPrimCat.getJavaClassName();

            String javaTypeForCoreProperty = corePropertyTypes.get(propName);

            if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
            {
                /*
                 * If the types are the same, then this is good to go. There is also one case where they may differ
                 * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                 * whereas a Date match property is a;most certainly going to be specified as a java.lang.Long.
                 * This should be OK - we can accept the type difference, provided we convert the match property
                 * to a Date before using it in the traversal (which is in the switch statement further on)
                 */

                if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                        (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                {
                    /*
                     * Types match, OK to include the property
                     */
                    propNameInGraph = PROPERTY_KEY_PREFIX_ENTITY + propName;
                    mapping = corePropertyMixedIndexMappings.get(propNameInGraph);
                }
                else
                {
                    /* Type of array elements is not a match for the property type */
                    throw new InvalidParameterException(
                            GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_TYPE.getMessageDefinition(
                                    propName,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName,
                            "value");
                }
            }
            else
            {
                throw new InvalidParameterException(
                        GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_TYPE.getMessageDefinition(
                                propName,
                                javaTypeForMatchProperty,
                                javaTypeForCoreProperty,
                                methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName,
                        "value");
            }
        }


        else // type-define attribute
        {
            /*
             * Treat the match property as a reference to a type-defined property. Check that it is primitive and has the
             * same prim cat as the array elements.
             */

            List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

            for (TypeDefAttribute propertyDef : propertiesDef)
            {
                String definedPropertyName = propertyDef.getAttributeName();
                if (definedPropertyName.equals(propName))
                {

                    /*
                     * The match property name matches the name of a type-defined attribute
                     *
                     * Check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    AttributeTypeDef atd = propertyDef.getAttributeType();
                    AttributeTypeDefCategory atdCat = atd.getCategory();

                    if (atdCat == PRIMITIVE)
                    {
                        PrimitiveDef pdef = (PrimitiveDef) atd;
                        PrimitiveDefCategory pdCat = pdef.getPrimitiveDefCategory();
                        if (pdCat == allElementsPrimCat)
                        {
                            /*
                             * Types match
                             *
                             * Sort out the qualification and prefixing of the property name ready for graph search
                             */
                            String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                            propNameInGraph = PROPERTY_KEY_PREFIX_ENTITY + qualifiedPropertyName;
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                            // Found the property, no point continuing to inspect further propertyDefs...
                            break;
                        }
                    }
                    /*
                     * If the property is not primitive or is primitive but of a different PDC than the
                     * type-defined attribute, then ignore it. It may be valid in finds across multiple
                     * types, because it would be relevant to a different type. But for this type it
                     * is ignored. The code above will have set propNameInGraph in the event of a match.
                     */
                    break;
                }
            }
        }

        /*
         * Should have now established propNameInGraph and mapping. If proprNameInGraph is not set then we failed to find an attribute,
         * in either core properties or type-defined attributes.
         *
         * Construct the graph traversal for this condition.
         */
        GraphTraversal<Vertex, Vertex> propertyCriterion = null;

        if (propNameInGraph != null)
        {
            /*
             * Incorporate the property (propNameInGraph) into a propCriterion for the traversal...
             */

            /*
             * It has been established that the property is primitive and has a pimr cat that matches every
             * element in the supplied array of values.
             * If a string   --> Test the property value against the array elements as literals (not regexes)
             * If a date     --> Create a within step where the array elements are either Dates or Longs depending on core/type-defined
             * Anything else --> Create a within step passing the array elements as an array of object literals
             */

            InstancePropertyCategory ipvCat = value.getInstancePropertyCategory();


            /*
             * Build a collection of elements of type appropriate to the prim cat
             */
            switch (allElementsPrimCat)
            {

                /*
                 *  OM_PRIMITIVE_TYPE_BOOLEAN   (1,  "boolean",    "java.lang.Boolean",     "3863f010-611c-41fe-aaae-5d4d427f863b"),
                 *  OM_PRIMITIVE_TYPE_BYTE      (2,  "byte",       "java.lang.Byte",        "6b7d410a-2e8a-4d12-981a-a806449f9bdb"),
                 *  OM_PRIMITIVE_TYPE_CHAR      (3,  "char",       "java.lang.Character",   "b0abebe5-cf85-4065-86ad-f3c6360ed9c7"),
                 *  OM_PRIMITIVE_TYPE_SHORT     (4,  "short",      "java.lang.Short",       "8e95b966-ab60-46d4-a03f-40c5a1ba6c2a"),
                 *  OM_PRIMITIVE_TYPE_INT       (5,  "int",        "java.lang.Integer",     "7fc49104-fd3a-46c8-b6bf-f16b6074cd35"),
                 *  OM_PRIMITIVE_TYPE_LONG      (6,  "long",       "java.lang.Long",        "33a91510-92ee-4825-9f49-facd7a6f9db6"),
                 *  OM_PRIMITIVE_TYPE_FLOAT     (7,  "float",      "java.lang.Float",       "52aeb769-37b7-4b30-b949-ddc7dcebcfa2"),
                 *  OM_PRIMITIVE_TYPE_DOUBLE    (8,  "double",     "java.lang.Double",      "e13572e8-25c3-4994-acb6-2ea66c95812e"),
                 *  OM_PRIMITIVE_TYPE_BIGINTEGER(9,  "biginteger", "java.math.BigInteger",  "8aa56e52-1076-4e0d-9b66-3873a1ed7392"),
                 *  OM_PRIMITIVE_TYPE_BIGDECIMAL(10, "bigdecimal", "java.math.BigDecimal",  "d5c8ad9f-8fee-4a64-80b3-63ce1e47f6bb"),
                 *  OM_PRIMITIVE_TYPE_STRING    (11, "string",     "java.lang.String",      "b34a64b9-554a-42b1-8f8a-7d5c2339f9c4"),
                 *  OM_PRIMITIVE_TYPE_DATE      (12, "date",       "java.lang.Long",        "1bef35ca-d4f9-48db-87c2-afce4649362d");
                 */

                case OM_PRIMITIVE_TYPE_BOOLEAN:
                    Boolean[] booleanArray = marshallValuesAsBooleans(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, booleanArray);
                    break;

                case OM_PRIMITIVE_TYPE_BYTE:
                    Byte[] byteArray = marshallValuesAsBytes(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, byteArray);
                    break;

                case OM_PRIMITIVE_TYPE_CHAR:
                    Character[] characterArray = marshallValuesAsCharacters(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, characterArray);
                    break;

                case OM_PRIMITIVE_TYPE_SHORT:
                    Short[] shortArray = marshallValuesAsShorts(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, shortArray);
                    break;

                case OM_PRIMITIVE_TYPE_INT:
                    Integer[] integerArray = marshallValuesAsIntegers(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, integerArray);
                    break;

                case OM_PRIMITIVE_TYPE_LONG:
                    Long[] longArray = marshallValuesAsLongs(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, longArray);
                    break;

                case OM_PRIMITIVE_TYPE_FLOAT:
                    Float[] floatArray = marshallValuesAsFloats(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, floatArray);
                    break;

                case OM_PRIMITIVE_TYPE_DOUBLE:
                    Double[] doubleArray = marshallValuesAsDoubles(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, doubleArray);
                    break;

                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                    BigInteger[] bigIntegerArray = marshallValuesAsBigIntegers(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, bigIntegerArray);
                    break;

                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                    BigDecimal[] bigDecimalArray = marshallValuesAsBigDecimals(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, bigDecimalArray);
                    break;

                case OM_PRIMITIVE_TYPE_STRING:
                    String[] stringArray = marshallValuesAsStrings(apv);
                    propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, stringArray);
                    break;

                case OM_PRIMITIVE_TYPE_DATE:
                    /*
                     * Supplied array will contain Longs
                     *
                     * Condition the type according to whether core (hard) or type-defined (soft)
                     */
                    if (isCoreProperty)
                    {
                        Date[] dateArray = marshallValuesAsDates(apv);
                        propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, dateArray);
                    }
                    else // type-defined attribute
                    {
                        Long[] timestampArray = marshallValuesAsLongs(apv);
                        propertyCriterion = vertexApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, timestampArray);
                    }
                    break;


            }

            log.debug("{} primitive search property has property criterion {}", methodName, propertyCriterion);

            return propertyCriterion;

        }
        else
        {
            /*
             * The property name is neither known as a core property or type-defined property.
             * The traversal should fail if MatchCriteria is ALL but due to nesting that is
             * always applied at the next higher level on the tree. Therefore must include a
             * step that is guaranteed to fail - but not cause a graph key error. This will
             * cause this traverser to be filtered. The 'none' step was considered but is not
             * generally recommended.
             * There is no property that corresponds to the 'short' name provided by the caller.
             * It is not feasible to emulate an imagined property because this will cause a key
             * error in the graph db. Instead, use an existing property but with a value that is
             * known to be impossible.
             */
            GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();
            t = t.has(PROPERTY_KEY_ENTITY_TYPE_NAME,"InvalidTypeForProperty"+propName);
            propertyCriterion = t;

            return propertyCriterion;

        }

    }






    /*
     * The relationship search contains a PropertyCondition that uses the 'IN' operator, which is for testing a property
     * against an array of acceptable values.
     */
    private  GraphTraversal<Edge, Edge>  parseRelationshipInOperatorToCriterion(String                      typeDefName,
                                                                                String                      propName,
                                                                                InstancePropertyValue       value)

    throws InvalidParameterException
    {

        String methodName = "parseRelationshipInOperatorToCriterion";

        /*
         * The strategy is to:
         *
         * a) check the supplied value is an Array and that it contains primitive elements that are all the same primitive category
         *
         * b) determine whether the specified property is a core or type-defined attribute and check that it is a primitive and has
         *    the same primitive category as the elements in the array.
         *
         * c) construct the graph traversal, using a within step.
         */


        /*
         * a) Validate the supplied match value is an array property and has elements that are primitive
         *    and all the same primitive category. Remember the primitive category.
         *
         *    If any condition fails, throw InvalidParameterException naming the property.
         *
         * For the IN operator the caller must supply an ARRAY of values against which to
         * compare the named primitive property.
         *
         * Before going much further, validate that the parameter is an array and that it has
         * only primitive elements and they are all the same primitive category.
         */

        ArrayPropertyValue apv = (ArrayPropertyValue)value;
        /*
         * Check the array is non-trivial - e.g. empty
         */
        if (apv.getArrayCount() <= 0)
        {
            /* Empty array - not supported */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                            propName,
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "value");
        }

        InstanceProperties arrayValues = apv.getArrayValues();
        Map<String, InstancePropertyValue> arrayElementProperties = arrayValues.getInstanceProperties();
        Collection<InstancePropertyValue> arrayPropValues = arrayElementProperties.values();

        PrimitiveDefCategory allElementsPrimCat = null;
        for (InstancePropertyValue elementValue : arrayPropValues)
        {
            /* Check the array element value is primitive and has the same primitive cat
             * as the first element.
             */
            InstancePropertyCategory elementCategory = elementValue.getInstancePropertyCategory();
            if (elementCategory == InstancePropertyCategory.PRIMITIVE)
            {
                /* Remember the prim cat of the first element */
                PrimitivePropertyValue primElementValue = (PrimitivePropertyValue) elementValue;
                PrimitiveDefCategory thisElementPrimCat = primElementValue.getPrimitiveDefCategory();
                if (allElementsPrimCat == null)
                {
                    /* All elements will have to same primitive category as this one... */
                    allElementsPrimCat = thisElementPrimCat;
                }
                else
                {
                    /* Check prim cat against other elements */
                    if (thisElementPrimCat != allElementsPrimCat)
                    {
                        /* Mixed array - not supported */
                        throw new InvalidParameterException(
                                GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                                        propName,
                                        methodName,
                                        this.getClass().getName(),
                                        repositoryName),
                                this.getClass().getName(),
                                methodName,
                                "value");
                    }
                }
            }
            else
            {
                /*
                 * No support for array elements that are not primitive
                 */
                throw new InvalidParameterException(
                        GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                                propName,
                                methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName,
                        "value");
            }
        }
        if (allElementsPrimCat == null)
        {
            /*
             * Failed to establish prim def cat for array elements
             */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_VALUE.getMessageDefinition(
                            propName,
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "value");
        }

        /*
         * It has been established that all array elements are primitive and of the same (known) primitive category
         */

        /*
         * b) determine whether the specified property is a core or type-defined attribute, check that it is a primitive and has
         *    the same primitive category as the elements in the array.
         */

        /*
         * Check if this is a core property (from InstanceAuditHeader)
         * Core properties take precedence over TDAs (in the event of a name clash)
         *
         * Check the match properties' names against two sets - first is the core properties, second is the type-defined
         * attributes (including inherited attributes)
         */

        Set<String> corePropertyNames = corePropertiesRelationship.keySet();
        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
        Map<String, String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);
        Set<String> typeDefinedPropertyNames = qualifiedPropertyNames.keySet();

        boolean isCoreProperty;

        if (corePropertyNames.contains(propName))
        {
            isCoreProperty = true;
        }
        else if (typeDefinedPropertyNames.contains(propName))
        {
            isCoreProperty = false;
        }
        else
        {
            /*
             * Property not found - The match property is not a supported, known type-defined property.
             */
            throw new InvalidParameterException(
                    GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_NAME.getMessageDefinition(
                            propName,
                            methodName,
                            this.getClass().getName(),
                            repositoryName),
                    this.getClass().getName(),
                    methodName,
                    "propName");
        }


        /*
         * Check that the core property or type-defined attribute is a primitive. The graph repo only
         * supports containment checks of primitives to arrays of primitives.
         * Also check that the primitive category is the same as that of the elements in the supplied array.
         * THe result of this should be to set propNameInGraph and mapping to appropriate values.
         */


        /*
         * Mapping is String for all properties (core or type-specific) except for the subset of core properties that use Full-Text
         */
        GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
        String propNameInGraph = null;

        if (isCoreProperty)
        {
            /*
             * The match property is a reference to a core property.
             * It is known that core properties are primitive.
             * The type (primitive category) of the supplied array elements needs to be compared to that of the core property.
             *
             * For a core property to be held in a matchProperties (InstanceProperties) object, the caller will need to have converted from InstanceAuditHeader
             * type declaration to an appropriate 'soft' type. For example a java.lang.String field such as createdBy must have been converted to a primitive with
             * primitive def category of string. Therefore this method validates the actual (java) type - so that a more meaningful error message can be
             * delivered to the user, instead of a mid-traversal complaint referring to an anonymous key.
             */

            /*
             * It has already been established that the array contains one or more elements of prim cat allElementsPrimCat
             */

            String javaTypeForMatchProperty = allElementsPrimCat.getJavaClassName();;

            String javaTypeForCoreProperty = corePropertyTypes.get(propName);

            if (javaTypeForCoreProperty != null && javaTypeForMatchProperty != null)
            {
                /*
                 * If the types are the same, then this is good to go. There is also one case where they may differ
                 * but we should proceed: The core properties for createTime and updateTime are stored using type java.util.Date
                 * whereas a Date match property is a;most certainly going to be specified as a java.lang.Long.
                 * This should be OK - we can accept the type difference, provided we convert the match property
                 * to a Date before using it in the traversal (which is in the switch statement further on)
                 */

                if ((javaTypeForCoreProperty.equals(javaTypeForMatchProperty)) ||
                        (javaTypeForCoreProperty.equals("java.util.Date") && javaTypeForMatchProperty.equals("java.lang.Long")))
                {
                    /*
                     * Types match, OK to include the property
                     */
                    propNameInGraph = PROPERTY_KEY_PREFIX_RELATIONSHIP + propName;
                    mapping = corePropertyMixedIndexMappings.get(propNameInGraph);
                }
                else
                {
                    /* Type of array elements is not a match for the property type */
                    throw new InvalidParameterException(
                            GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_TYPE.getMessageDefinition(
                                    propName,
                                    methodName,
                                    this.getClass().getName(),
                                    repositoryName),
                            this.getClass().getName(),
                            methodName,
                            "value");
                }
            }
            else
            {
                throw new InvalidParameterException(
                        GraphOMRSErrorCode.INVALID_SEARCH_PROPERTY_TYPE.getMessageDefinition(
                                propName,
                                javaTypeForMatchProperty,
                                javaTypeForCoreProperty,
                                methodName,
                                this.getClass().getName(),
                                repositoryName),
                        this.getClass().getName(),
                        methodName,
                        "value");
            }
        }


        else // type-define attribute
        {
            /*
             * Treat the match property as a reference to a type-defined property. Check that it is primitive and has the
             * same prim cat as the array elements.
             */

            List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

            for (TypeDefAttribute propertyDef : propertiesDef)
            {
                String definedPropertyName = propertyDef.getAttributeName();
                if (definedPropertyName.equals(propName))
                {

                    /*
                     * The match property name matches the name of a type-defined attribute
                     *
                     * Check types match - i.e. that the match property instance property has the same type as the type-defined attribute
                     */

                    AttributeTypeDef atd = propertyDef.getAttributeType();
                    AttributeTypeDefCategory atdCat = atd.getCategory();

                    if (atdCat == PRIMITIVE)
                    {
                        PrimitiveDef pdef = (PrimitiveDef) atd;
                        PrimitiveDefCategory pdCat = pdef.getPrimitiveDefCategory();
                        if (pdCat == allElementsPrimCat)
                        {
                            /*
                             * Types match
                             *
                             * Sort out the qualification and prefixing of the property name ready for graph search
                             */
                            String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                            propNameInGraph = PROPERTY_KEY_PREFIX_RELATIONSHIP + qualifiedPropertyName;
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                            // Found the property, no point continuing to inspect further propertyDefs...
                            break;
                        }
                    }
                    /*
                     * If the property is not primitive or is primitive but of a different PDC than the
                     * type-defined attribute, then ignore it. It may be valid in finds across multiple
                     * types, because it would be relevant to a different type. But for this type it
                     * is ignored. The code above will have set propNameInGraph in the event of a match.
                     */
                    break;
                }
            }
        }

        /*
         * Should have now established propNameInGraph and mapping. If proprNameInGraph is not set then we failed to find an attribute,
         * in either core properties or type-defined attributes.
         *
         * Construct the graph traversal for this condition.
         */
        GraphTraversal<Edge, Edge> propertyCriterion = null;

        if (propNameInGraph != null)
        {
            /*
             * Incorporate the property (propNameInGraph) into a propCriterion for the traversal...
             */

            /*
             * It has been established that the property is primitive and has a prim cat that matches every
             * element in the supplied array of values.
             * If a string   --> Test the property value against the array elements as literals (not regexes)
             * If a date     --> Create a within step where the array elements are either Dates or Longs depending on core/type-defined
             * Anything else --> Create a within step passing the array elements as an array of object literals
             */

            /*
             * Build a collection of elements of type appropriate to the prim cat
             */
            switch (allElementsPrimCat)
            {

                /*
                 *  OM_PRIMITIVE_TYPE_BOOLEAN   (1,  "boolean",    "java.lang.Boolean",     "3863f010-611c-41fe-aaae-5d4d427f863b"),
                 *  OM_PRIMITIVE_TYPE_BYTE      (2,  "byte",       "java.lang.Byte",        "6b7d410a-2e8a-4d12-981a-a806449f9bdb"),
                 *  OM_PRIMITIVE_TYPE_CHAR      (3,  "char",       "java.lang.Character",   "b0abebe5-cf85-4065-86ad-f3c6360ed9c7"),
                 *  OM_PRIMITIVE_TYPE_SHORT     (4,  "short",      "java.lang.Short",       "8e95b966-ab60-46d4-a03f-40c5a1ba6c2a"),
                 *  OM_PRIMITIVE_TYPE_INT       (5,  "int",        "java.lang.Integer",     "7fc49104-fd3a-46c8-b6bf-f16b6074cd35"),
                 *  OM_PRIMITIVE_TYPE_LONG      (6,  "long",       "java.lang.Long",        "33a91510-92ee-4825-9f49-facd7a6f9db6"),
                 *  OM_PRIMITIVE_TYPE_FLOAT     (7,  "float",      "java.lang.Float",       "52aeb769-37b7-4b30-b949-ddc7dcebcfa2"),
                 *  OM_PRIMITIVE_TYPE_DOUBLE    (8,  "double",     "java.lang.Double",      "e13572e8-25c3-4994-acb6-2ea66c95812e"),
                 *  OM_PRIMITIVE_TYPE_BIGINTEGER(9,  "biginteger", "java.math.BigInteger",  "8aa56e52-1076-4e0d-9b66-3873a1ed7392"),
                 *  OM_PRIMITIVE_TYPE_BIGDECIMAL(10, "bigdecimal", "java.math.BigDecimal",  "d5c8ad9f-8fee-4a64-80b3-63ce1e47f6bb"),
                 *  OM_PRIMITIVE_TYPE_STRING    (11, "string",     "java.lang.String",      "b34a64b9-554a-42b1-8f8a-7d5c2339f9c4"),
                 *  OM_PRIMITIVE_TYPE_DATE      (12, "date",       "java.lang.Long",        "1bef35ca-d4f9-48db-87c2-afce4649362d");
                 */

                case OM_PRIMITIVE_TYPE_BOOLEAN:
                    Boolean[] booleanArray = marshallValuesAsBooleans(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, booleanArray);
                    break;

                case OM_PRIMITIVE_TYPE_BYTE:
                    Byte[] byteArray = marshallValuesAsBytes(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, byteArray);
                    break;

                case OM_PRIMITIVE_TYPE_CHAR:
                    Character[] characterArray = marshallValuesAsCharacters(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, characterArray);
                    break;

                case OM_PRIMITIVE_TYPE_SHORT:
                    Short[] shortArray = marshallValuesAsShorts(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, shortArray);
                    break;

                case OM_PRIMITIVE_TYPE_INT:
                    Integer[] integerArray = marshallValuesAsIntegers(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, integerArray);
                    break;

                case OM_PRIMITIVE_TYPE_LONG:
                    Long[] longArray = marshallValuesAsLongs(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, longArray);
                    break;

                case OM_PRIMITIVE_TYPE_FLOAT:
                    Float[] floatArray = marshallValuesAsFloats(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, floatArray);
                    break;

                case OM_PRIMITIVE_TYPE_DOUBLE:
                    Double[] doubleArray = marshallValuesAsDoubles(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, doubleArray);
                    break;

                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                    BigInteger[] bigIntegerArray = marshallValuesAsBigIntegers(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, bigIntegerArray);
                    break;

                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                    BigDecimal[] bigDecimalArray = marshallValuesAsBigDecimals(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, bigDecimalArray);
                    break;

                case OM_PRIMITIVE_TYPE_STRING:
                    String[] stringArray = marshallValuesAsStrings(apv);
                    propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, stringArray);
                    break;

                case OM_PRIMITIVE_TYPE_DATE:
                    /*
                     * Supplied array will contain Longs
                     *
                     * Condition the type according to whether core (hard) or type-defined (soft)
                     */
                    if (isCoreProperty)
                    {
                        Date[] dateArray = marshallValuesAsDates(apv);
                        propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, dateArray);
                    }
                    else // type-defined attribute
                    {
                        Long[] timestampArray = marshallValuesAsLongs(apv);
                        propertyCriterion = edgeApplyOperatorToObject(propNameInGraph, PropertyComparisonOperator.IN, timestampArray);
                    }
                    break;


            }

            log.debug("{} primitive search property has property criterion {}", methodName, propertyCriterion);

        }
        else
        {
            /*
             * The property name is neither known as a core property or type-defined property.
             * The traversal should fail if MatchCriteria is ALL but due to nesting that is
             * always applied at the next higher level on the tree. Therefore must include a
             * step that is guaranteed to fail - but not cause a graph key error. This will
             * cause this traverser to be filtered. The 'none' step was considered but is not
             * generally recommended.
             * There is no property that corresponds to the 'short' name provided by the caller.
             * It is not feasible to emulate an imagined property because this will cause a key
             * error in the graph db. Instead, use an existing property but with a value that is
             * known to be impossible.
             */
            GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();
            t = t.has(PROPERTY_KEY_ENTITY_TYPE_NAME,"InvalidTypeForProperty"+propName);
            propertyCriterion = t;

        }

        return propertyCriterion;

    }

    // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.
    private  GraphTraversal<Vertex, Vertex> vertexApplyOperatorToString(String                                   propNameInGraph,
                                                                        PropertyComparisonOperator               operator,
                                                                        Object                                   primValue,
                                                                        GraphOMRSGraphFactory.MixedIndexMapping  mapping,
                                                                        boolean                                  fullMatch)

    {
        String methodName = "vertexApplyOperatorToString";

        GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();

        /*
         * The LIKE operator is only valid for strings and requires special processing to cater for the index mapping
         * and fullMatch option.
         */
        if (operator == LIKE)
        {
            String searchString = convertSearchStringToJanusRegex((String) primValue);
            log.debug("{} primitive match property search string {}", methodName, searchString);

            // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
            if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
            {
                t = t.has(propNameInGraph, Text.textContainsRegex(searchString)); // for a field indexed using Text mapping use textContains or textContainsRegex
            }
            else
            {
                if (!fullMatch)
                {
                    // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                    String ANYCHARS = ".*";
                    t = t.has(propNameInGraph, Text.textRegex(ANYCHARS + searchString + ANYCHARS));   // for a field indexed using String mapping use textRegex
                }
                else // Must be a full match...
                {
                    t = t.has(propNameInGraph, Text.textRegex(searchString));
                }
            }
        }
        else
        {
            /*
             * For any other operators just treat the string the same as any other object
             */
            t = vertexApplyOperatorToObject(propNameInGraph, operator, primValue);
        }

        return t;
    }



    private  GraphTraversal<Vertex, Vertex> vertexApplyOperatorToDate(String                                  propNameInGraph,
                                                                      PropertyComparisonOperator              operator,
                                                                      Object                                  primValue,
                                                                      String                                  shortPropName,
                                                                      PrimitiveDefCategory                    pCat)
    {

        GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();

        /*
         * Condition the supplied value.
         * If the property to be matched is a core property then it is actually stored as a java.util.Date.
         * If it is a type-defined attribute then it is stored as a java.lang.Long.
         * Match up the types and values appropriately.
         */

        Set<String> corePropertyNames = corePropertiesEntity.keySet();
        String javaTypeForMatchProperty = pCat.getJavaClassName();
        if (corePropertyNames.contains(shortPropName))
        {
            /* This is a core property and is stored as Date */

            if (javaTypeForMatchProperty.equals("java.lang.Long"))
            {
                /* Need to convert Long to Date */
                Date dateValue = new Date((Long) primValue);
                t = vertexApplyOperatorToObject(propNameInGraph, operator, dateValue);
            }
            else
            {
                /* Otherwise just go for it... The types were matched above */
                t = vertexApplyOperatorToObject(propNameInGraph, operator, primValue);
            }
        }
        else
        {
            /* This is a type-defined attribute and is stored as Long */
            if (javaTypeForMatchProperty.equals("java.lang.Long"))
            {
                Long longValue = (Long) primValue;
                t = vertexApplyOperatorToObject(propNameInGraph, operator, longValue);
            }
            /* Otherwise (i.e. if not Long) this will already have failed above and thrown an InvalidParameterException */
        }

        return t;
    }

    private  GraphTraversal<Vertex, Vertex> vertexApplyOperatorToObject(String                                  propNameInGraph,
                                                                        PropertyComparisonOperator              operator,
                                                                        Object                                  primValue)
    {

        GraphTraversal<Vertex, Vertex> t = new DefaultGraphTraversal<>();

        switch (operator)
        {
            case EQ:
                t =  t.has(propNameInGraph, eq(primValue));
                break;
            case NEQ:
                t =  t.has(propNameInGraph, neq(primValue));
                break;
            case LT:
                t =  t.has(propNameInGraph, lt(primValue));
                break;
            case LTE:
                t =  t.has(propNameInGraph, lte(primValue));
                break;
            case GT:
                t =  t.has(propNameInGraph, gt(primValue));
                break;
            case GTE:
                t =  t.has(propNameInGraph, gte(primValue));
                break;
            case IN:
                t =  t.has(propNameInGraph, within(primValue));
                break;
            case IS_NULL:
                /* The property can either be not present or can be present and have a null value */
                GraphTraversal<? extends Element, ? extends Element> nonExistent = new DefaultGraphTraversal<>();
                GraphTraversal<? extends Element, ? extends Element> existentNull = new DefaultGraphTraversal<>();
                nonExistent = nonExistent.hasNot(propNameInGraph);
                existentNull = existentNull.has(propNameInGraph, eq(null));
                List<GraphTraversal<? extends Element, ? extends Element>> subCriteria = new ArrayList<>();
                subCriteria.add(nonExistent);
                subCriteria.add(existentNull);
                t =  t.or(subCriteria.toArray(new GraphTraversal[0]));
                break;
            case NOT_NULL:
                t =  t.has(propNameInGraph, neq(null));
                break;
        }
        return t;
    }


    // The graph connector has to map from Egeria's internal regex convention to a format that is supported by JanusGraph.
    private  GraphTraversal<Edge, Edge> edgeApplyOperatorToString(String                                   propNameInGraph,
                                                                  PropertyComparisonOperator               operator,
                                                                  Object                                   primValue,
                                                                  GraphOMRSGraphFactory.MixedIndexMapping  mapping,
                                                                  boolean                                  fullMatch)

    {
        String methodName = "edgeApplyOperatorToString";

        GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();

        /*
         * The LIKE operator is only valid for strings and requires special processing to cater for the index mapping
         * and fullMatch option.
         */
        if (operator == LIKE)
        {
            String searchString = convertSearchStringToJanusRegex((String) primValue);
            log.debug("{} primitive match property search string {}", methodName, searchString);

            // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
            if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text)
            {
                t = t.has(propNameInGraph, Text.textContainsRegex(searchString)); // for a field indexed using Text mapping use textContains or textContainsRegex
            }
            else
            {
                if (!fullMatch)
                {
                    // A partial match is sufficient...i.e. a value containing the search value as a substring will match
                    String ANYCHARS = ".*";
                    t = t.has(propNameInGraph, Text.textRegex(ANYCHARS + searchString + ANYCHARS));   // for a field indexed using String mapping use textRegex
                }
                else // Must be a full match...
                {
                    t = t.has(propNameInGraph, Text.textRegex(searchString));
                }
            }
        }
        else
        {
            /*
             * For any other operators just treat the string the same as any other object
             */
            t = edgeApplyOperatorToObject(propNameInGraph, operator, primValue);
        }

        return t;
    }



    private  GraphTraversal<Edge, Edge> edgeApplyOperatorToDate(String                                  propNameInGraph,
                                                                PropertyComparisonOperator              operator,
                                                                Object                                  primValue,
                                                                String                                  shortPropName,
                                                                PrimitiveDefCategory                    pCat)
    {

        GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();

        /*
         * Condition the supplied value.
         * If the property to be matched is a core property then it is actually stored as a java.util.Date.
         * If it is a type-defined attribute then it is stored as a java.lang.Long.
         * Match up the types and values appropriately.
         */

        Set<String> corePropertyNames = corePropertiesRelationship.keySet();
        String javaTypeForMatchProperty = pCat.getJavaClassName();
        if (corePropertyNames.contains(shortPropName))
        {
            /* This is a core property and is stored as Date */

            if (javaTypeForMatchProperty.equals("java.lang.Long"))
            {
                /* Need to convert Long to Date */
                Date dateValue = new Date((Long) primValue);
                t = edgeApplyOperatorToObject(propNameInGraph, operator, dateValue);
            }
            else
            {
                /* Otherwise just go for it... The types were matched above */
                t = edgeApplyOperatorToObject(propNameInGraph, operator, primValue);
            }
        }
        else
        {
            /* This is a type-defined attribute and is stored as Long */
            if (javaTypeForMatchProperty.equals("java.lang.Long"))
            {
                Long longValue = (Long) primValue;
                t = edgeApplyOperatorToObject(propNameInGraph, operator, longValue);
            }
            /* Otherwise (i.e. if not Long) this will already have failed above and thrown an InvalidParameterException */
        }

        return t;
    }

    private  GraphTraversal<Edge, Edge> edgeApplyOperatorToObject(String                                  propNameInGraph,
                                                                  PropertyComparisonOperator              operator,
                                                                  Object                                  primValue)
    {

        GraphTraversal<Edge, Edge> t = new DefaultGraphTraversal<>();

        switch (operator)
        {
            case EQ:
                t =  t.has(propNameInGraph, eq(primValue));
                break;
            case NEQ:
                t =  t.has(propNameInGraph, neq(primValue));
                break;
            case LT:
                t =  t.has(propNameInGraph, lt(primValue));
                break;
            case LTE:
                t =  t.has(propNameInGraph, lte(primValue));
                break;
            case GT:
                t =  t.has(propNameInGraph, gt(primValue));
                break;
            case GTE:
                t =  t.has(propNameInGraph, gte(primValue));
                break;
            case IN:
                t =  t.has(propNameInGraph, within(primValue));
                break;
            case IS_NULL:
                /* The property can either be not present or can be present and have a null value */
                GraphTraversal<? extends Element, ? extends Element> nonExistent = new DefaultGraphTraversal<>();
                GraphTraversal<? extends Element, ? extends Element> existentNull = new DefaultGraphTraversal<>();
                nonExistent = nonExistent.hasNot(propNameInGraph);
                existentNull = existentNull.has(propNameInGraph, eq(null));
                List<GraphTraversal<? extends Element, ? extends Element>> subCriteria = new ArrayList<>();
                subCriteria.add(nonExistent);
                subCriteria.add(existentNull);
                t =  t.or(subCriteria.toArray(new GraphTraversal[0]));
                break;
            case NOT_NULL:
                t =  t.has(propNameInGraph, neq(null));
                break;
        }
        return t;
    }



    private Collection<InstancePropertyValue> getArrayElementValues(ArrayPropertyValue apv)
    {
        InstanceProperties arrayValues = apv.getArrayValues();
        Map<String, InstancePropertyValue> arrayElementProperties = arrayValues.getInstanceProperties();
        return arrayElementProperties.values();
    }

    /*
    OM_PRIMITIVE_TYPE_UNKNOWN   (0,  "object",     "java.lang.Object",      "1c4b21f4-0b67-41a7-a6ed-2af185eb9b3b"),
    OM_PRIMITIVE_TYPE_BOOLEAN   (1,  "boolean",    "java.lang.Boolean",     "3863f010-611c-41fe-aaae-5d4d427f863b"),
    OM_PRIMITIVE_TYPE_BYTE      (2,  "byte",       "java.lang.Byte",        "6b7d410a-2e8a-4d12-981a-a806449f9bdb"),
    OM_PRIMITIVE_TYPE_CHAR      (3,  "char",       "java.lang.Character",   "b0abebe5-cf85-4065-86ad-f3c6360ed9c7"),
    OM_PRIMITIVE_TYPE_SHORT     (4,  "short",      "java.lang.Short",       "8e95b966-ab60-46d4-a03f-40c5a1ba6c2a"),
    OM_PRIMITIVE_TYPE_INT       (5,  "int",        "java.lang.Integer",     "7fc49104-fd3a-46c8-b6bf-f16b6074cd35"),
    OM_PRIMITIVE_TYPE_LONG      (6,  "long",       "java.lang.Long",        "33a91510-92ee-4825-9f49-facd7a6f9db6"),
    OM_PRIMITIVE_TYPE_FLOAT     (7,  "float",      "java.lang.Float",       "52aeb769-37b7-4b30-b949-ddc7dcebcfa2"),
    OM_PRIMITIVE_TYPE_DOUBLE    (8,  "double",     "java.lang.Double",      "e13572e8-25c3-4994-acb6-2ea66c95812e"),
    OM_PRIMITIVE_TYPE_BIGINTEGER(9,  "biginteger", "java.math.BigInteger",  "8aa56e52-1076-4e0d-9b66-3873a1ed7392"),
    OM_PRIMITIVE_TYPE_BIGDECIMAL(10, "bigdecimal", "java.math.BigDecimal",  "d5c8ad9f-8fee-4a64-80b3-63ce1e47f6bb"),
    OM_PRIMITIVE_TYPE_STRING    (11, "string",     "java.lang.String",      "b34a64b9-554a-42b1-8f8a-7d5c2339f9c4"),
    OM_PRIMITIVE_TYPE_DATE      (12, "date",       "java.lang.Long",        "1bef35ca-d4f9-48db-87c2-afce4649362d");
    */

    private Boolean[] marshallValuesAsBooleans(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Boolean[] booleanArray = new Boolean[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            booleanArray[index++] = (Boolean)primValue;
        }
        return booleanArray;
    }

    private Byte[] marshallValuesAsBytes(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Byte[] byteArray = new Byte[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            byteArray[index++] = (Byte)primValue;
        }
        return byteArray;
    }

    private Character[] marshallValuesAsCharacters(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Character[] characterArray = new Character[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            characterArray[index++] = (Character)primValue;
        }
        return characterArray;
    }

    private Short[] marshallValuesAsShorts(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Short[] shortArray = new Short[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            shortArray[index++] = (Short)primValue;
        }
        return shortArray;
    }

    private Integer[] marshallValuesAsIntegers(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Integer[] integerArray = new Integer[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            integerArray[index++] = (Integer)primValue;
        }
        return integerArray;
    }

    private Long[] marshallValuesAsLongs(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Long[] longArray = new Long[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            longArray[index++] = (Long)primValue;
        }
        return longArray;
    }


    private Float[] marshallValuesAsFloats(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Float[] floatArray = new Float[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            floatArray[index++] = (Float)primValue;
        }
        return floatArray;
    }

    private Double[] marshallValuesAsDoubles(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Double[] doubleArray = new Double[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            doubleArray[index++] = (Double)primValue;
        }
        return doubleArray;
    }

    private BigInteger[] marshallValuesAsBigIntegers(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        BigInteger[] bigIntegerArray = new BigInteger[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            bigIntegerArray[index++] = (BigInteger)primValue;
        }
        return bigIntegerArray;
    }

    private BigDecimal[] marshallValuesAsBigDecimals(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        BigDecimal[] bigDecimalArray = new BigDecimal[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            bigDecimalArray[index++] = (BigDecimal)primValue;
        }
        return bigDecimalArray;
    }

    private String[] marshallValuesAsStrings(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        String[] stringArray = new String[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            stringArray[index++] = (String)primValue;
        }
        return stringArray;
    }

    private Date[] marshallValuesAsDates(ArrayPropertyValue arrayPropertyValue)
    {
        int arrayLength = arrayPropertyValue.getArrayCount();
        Collection<InstancePropertyValue> arrayElementValues = getArrayElementValues(arrayPropertyValue);

        Date[] dateArray = new Date[arrayLength];
        int index = 0;
        for (InstancePropertyValue ipv : arrayElementValues)
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            dateArray[index++] = new Date((Long) primValue);
        }
        return dateArray;
    }

}
