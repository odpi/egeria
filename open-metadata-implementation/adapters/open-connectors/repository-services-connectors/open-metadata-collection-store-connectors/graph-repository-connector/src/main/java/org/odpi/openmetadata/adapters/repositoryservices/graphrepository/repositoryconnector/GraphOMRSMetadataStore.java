/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.attribute.Text;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import static org.apache.tinkerpop.gremlin.process.traversal.P.within;
import static org.apache.tinkerpop.gremlin.process.traversal.P.without;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.*;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSGraphFactory.corePropertyMixedIndexMappings;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory.PRIMITIVE;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * GraphOMRSMetadataStore provides the graph store for the GraphRepositoryConnector
 * The Graph Store is implemented using JanusGraph and is used to store instances.
 * There is no type graph because the RCM is used to get any information about TypeDefs and AttributeTypeDefs.
 */
class GraphOMRSMetadataStore {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSMetadataStore.class);

    private String               repositoryName;
    private String               metadataCollectionId;
    private String               metadataCollectionName = null;
    private OMRSRepositoryHelper repositoryHelper;
    private OMRSAuditLog         auditLog;

    // The instance graph is used to store entities (vertices) and relationships (edges).
    private JanusGraph instanceGraph;
    private GraphOMRSRelationshipMapper relationshipMapper;
    private GraphOMRSEntityMapper entityMapper;
    private GraphOMRSClassificationMapper classificationMapper;


    /**
     * Default constructor
     */
    public GraphOMRSMetadataStore(String               metadataCollectionId,
                                  String               repositoryName,
                                  OMRSRepositoryHelper repositoryHelper,
                                  OMRSAuditLog         auditLog)
        throws
            RepositoryErrorException
    {

        final String methodName = "GraphOMRSMetadataStore";

        this.metadataCollectionId = metadataCollectionId;
        this.repositoryName = repositoryName;
        this.repositoryHelper = repositoryHelper;
        this.auditLog = auditLog;

        try {
            synchronized (GraphOMRSMetadataStore.class) {
                instanceGraph = GraphOMRSGraphFactory.open(metadataCollectionId, repositoryName, auditLog);
            }
        }
        catch (RepositoryErrorException e) {
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
            throws
            RepositoryErrorException,
            InvalidParameterException
    {
        final String methodName = "createEntityInStore";

        Vertex vertex;

        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entity.getGUID());

        if (vertexIt.hasNext()) {

            vertex = vertexIt.next();
            log.debug("{} found existing vertex {}", methodName, vertex);

            /*
             * If the existing vertex is for a proxy entity then check that the supplied entity is a ref copy (i.e.
             * metadataCollection is not local) and if so, clear flag and use the same vertex for the ref copy.
             * If the existing vertex is NOT for a proxy entity OR we find it is a local entity then throw error.
             */

            if (entityMapper.isProxy(vertex)) {


                if (!metadataCollectionId.equals(entity.getMetadataCollectionId())) {
                    log.debug("{} existing vertex is a proxy", methodName);
                    // Replace the proxy with the ref copy - make sure the proxy flag is cleared then proceed to populating it below...
                    entityMapper.clearProxy(vertex);

                } else {
                    log.error("{} existing vertex apparently a proxy, but has local metadataCollectionId", methodName);
                    g.tx().rollback();
                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS;

                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
                            this.getClass().getName(),
                            repositoryName);

                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }
            } else {
                log.error("{} existing vertex for GUID {} and it is not a proxy", methodName, entity.getGUID());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        } else {

            // No existing vertex found - create one
            log.debug("{} create vertex for entity {}", methodName, entity.getGUID());
            vertex = g.addV("Entity").next();
        }

        // Whether created new or reusing old proxy, populate the vertex.

        try {
            entityMapper.mapEntityDetailToVertex(entity, vertex);

            // Create a vertex per classification and link them to the entity vertex
            List<Classification> classifications = entity.getClassifications();
            if (classifications != null) {
                for (Classification classification : classifications) {
                    log.debug("{} add classification: {} ", methodName, classification.getName());
                    Vertex classificationVertex = g.addV("Classification").next();
                    classificationMapper.mapClassificationToVertex(classification, classificationVertex);
                    Edge classifierEdge = vertex.addEdge("Classifier", classificationVertex);
                }
            }

        } catch (Exception e) {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_CREATED;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
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
            throws
            RepositoryErrorException,
            InvalidParameterException
    {
        final String methodName = "createEntityProxyInStore";

        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vertexIt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityProxy.getGUID());
        if (vertexIt.hasNext()) {
            Vertex vertex = vertexIt.next();
            log.error("{} createEntityProxyInStore found existing vertex {}", methodName, vertex);
            g.tx().rollback();
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityProxy.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        Vertex vertex = g.addV("Entity").next();

        try {
            entityMapper.mapEntityProxyToVertex(entityProxy, vertex);

            // Create a vertex per classification and link them to the entity vertex
            List<Classification> classifications = entityProxy.getClassifications();
            if (classifications != null) {
                for (Classification classification : classifications) {
                    log.debug("{} add classification ", methodName, classification.getName());
                    Vertex classificationVertex = g.addV("Classification").next();
                    classificationMapper.mapClassificationToVertex(classification, classificationVertex);
                    Edge classifierEdge = vertex.addEdge("Classifier", classificationVertex);
                }
            }

        } catch (Exception e) {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_CREATED;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityProxy.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        g.tx().commit();

    }


    synchronized EntityDetail getEntityFromStore(String guid)
            throws
            EntityNotKnownException,
            RepositoryErrorException
    {

        String methodName = "getEntityFromStore";


        EntityDetail entity = null;

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);

        // Only looking for non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

        if (gt.hasNext()) {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            try {
                if (vertex != null) {
                    log.debug("{} found entity vertex {}", methodName, vertex);

                    // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                    Boolean isProxy = entityMapper.isProxy(vertex);
                    if (!isProxy) {

                        entity = new EntityDetail();

                        entityMapper.mapVertexToEntityDetail(vertex, entity);
                    }

                }

            } catch (EntityProxyOnlyException | RepositoryErrorException e) {

                log.error("{} Caught exception {}", methodName, e.getMessage());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        } else {

            // Entity was not found by GUID
            log.error("{} entity with GUID {} not found {}", methodName, guid);
            g.tx().rollback();
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        g.tx().commit();

        return entity;
    }


    synchronized EntityProxy getEntityProxyFromStore(String guid)
            throws
            RepositoryErrorException
    {
        String methodName = "getEntityProxyFromStore";

        EntityProxy entityProxy = null;

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Vertex> vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);
        if (vi.hasNext()) {
            Vertex vertex = vi.next();

            try {
                if (vertex != null) {
                    log.debug("{} entity vertex {}", methodName, vertex);

                    // Could test here whether vertex is for a proxy, but it doesn't matter whether the vertex represents a full entity
                    // (i.e. EntityDetail of a local/reference copy) as opposed to an EntityProxy. It can be retrieved as a proxy anyway...

                    entityProxy = new EntityProxy();

                    entityMapper.mapVertexToEntityProxy(vertex, entityProxy);


                }
            } catch (Exception e) {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
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
            throws
            RepositoryErrorException,
            InvalidParameterException

    {
        String methodName = "createRelationshipInStore";

        // Begin a graph transaction. Locate the vertices for the ends, and create an edge between them.

        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, relationship.getGUID());
        if (edgeIt.hasNext()) {
            Edge edge = edgeIt.next();
            log.error("{} found existing edge {}", methodName, edge);
            g.tx().rollback();
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_ALREADY_EXISTS;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        String entityOneGUID = relationship.getEntityOneProxy().getGUID();
        String entityTwoGUID = relationship.getEntityTwoProxy().getGUID();

        Vertex vertexOne = null;
        Vertex vertexTwo = null;

        Iterator<Vertex> vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityOneGUID);
        if (vi.hasNext()) {
            vertexOne = vi.next();
            log.debug("{} found entityOne vertex {}", methodName, vertexOne);
        }

        vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityTwoGUID);
        if (vi.hasNext()) {
            vertexTwo = vi.next();
            log.debug("{} found entityTwo vertex {}", methodName, vertexTwo);
        }

        // If either end vertex does not exist then abandon the attempt - there should be at least a proxy if not a full entity in the store for each end
        if (vertexOne == null || vertexTwo == null) {
            log.error("{} Could not find both ends for relationship {}", methodName, relationship.getGUID());
            g.tx().rollback();
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_NOT_CREATED;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        Edge edge = vertexOne.addEdge("Relationship", vertexTwo);

        try {

            relationshipMapper.mapRelationshipToEdge(relationship, edge);

        } catch (Exception e) {
            log.error("{} Caught exception from relationship mapper {}", methodName, e.getMessage());
            g.tx().rollback();
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_NOT_CREATED;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        log.debug("{} Commit tx containing creation of edge", methodName);
        g.tx().commit();

    }


    protected synchronized Relationship getRelationshipFromStore(String guid)
            throws RepositoryErrorException
    {
        String methodName = "getRelationshipFromStore";

        Relationship relationship = null;

        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, guid);
        if (edgeIt.hasNext()) {
            Edge edge = edgeIt.next();
            log.debug("{} found existing edge {}", methodName, edge);

            relationship = new Relationship();

            // Map the properties
            relationshipMapper.mapEdgeToRelationship(edge, relationship);

            // Set the relationship ends...
            Vertex vertex = null;
            try {

                vertex = edge.outVertex();

                // Could test here whether each vertex is for a proxy, but it doesn't matter whether the vertex represents a full entity
                // (i.e. EntityDetail of a local/reference copy) as opposed to an EntityProxy. It can be retrieved as a proxy anyway...

                if (vertex != null) {
                    log.debug("{} entity vertex {}", methodName, vertex);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertex, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }

                vertex = edge.inVertex();

                if (vertex != null) {
                    log.debug("{} entity vertex {}", methodName, vertex);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertex, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            } catch (Exception e) {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_NOT_FOUND;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityMapper.getEntityGUID(vertex), methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }

        g.tx().commit();

        return relationship;

    }


    synchronized void updateEntityInStore(EntityDetail entity)
            throws
            RepositoryErrorException
    {

        String methodName = "updateEntityInStore";

        // Look in the graph
        String guid = entity.getGUID();
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);

        // Only looking for non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

        if (gt.hasNext()) {

            Vertex vertex = gt.next();
           log.debug("{} found entity vertex {}", methodName, vertex);

            try {

                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy) {

                    entityMapper.mapEntityDetailToVertex(entity, vertex);

                    updateEntityClassifications(entity, vertex, g);
                }

            } catch (Exception e) {
                log.error("{} caught exception {}", methodName, e.getMessage());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_UPDATED;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }

       log.debug("{} commit entity update tx: ", methodName);
        g.tx().commit();

    }

    synchronized void updateEntityProxyInStore(EntityProxy entityProxy)
            throws
            RepositoryErrorException
    {

        String methodName = "updateEntityProxyInStore";

        log.debug("{}", methodName);

        // Look in the graph
        String guid = entityProxy.getGUID();
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, guid);

        // Only looking for proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, true);

        if (gt.hasNext()) {

            Vertex vertex = gt.next();

            log.debug("{} found entity vertex {}", methodName, vertex);

            try {

                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (isProxy) {

                    entityMapper.mapEntityProxyToVertex(entityProxy, vertex);

                    updateEntityClassifications(entityProxy, vertex, g);
                }

            } catch (Exception e) {
                log.error("{} caught exception {}", methodName, e.getMessage());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_UPDATED;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityProxy.getGUID(),
                        methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }

         log.debug("{} commit entity proxy update tx: ", methodName);
        g.tx().commit();

    }


    // updateEntityClassifications
    private void updateEntityClassifications(EntitySummary entity, Vertex vertex, GraphTraversalSource g)
            throws
            RepositoryErrorException
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
        if (classifications != null) {
            for (Classification entityClassification : classifications) {
                log.debug("{} entity should have classification: {}", methodName, entityClassification.getName());
                entityClassificationsByName.put(entityClassification.getName(), entityClassification);
            }
        }
        // Map the existing classifications
        Iterator<Edge> classifierEdges = vertex.edges(Direction.OUT, "Classifier");
        while (classifierEdges.hasNext()) {
            Edge classifierEdge = classifierEdges.next();
            Vertex existingClassificationVertex = classifierEdge.inVertex();
            Classification existingClassification = new Classification();
            classificationMapper.mapVertexToClassification(existingClassificationVertex, existingClassification);
            log.debug("{} entity already has classification: {}", methodName, existingClassification.getName());
            existingClassificationVerticesByName.put(existingClassification.getName(), existingClassificationVertex);
            existingClassifierEdgesByName.put(existingClassification.getName(), classifierEdge);
        }
        // Now perform 1:1 synch - i) eliminate unnecessary classifications, then ii) update/add the desired classifications
        Iterator<String> existingNamesIterator = existingClassificationVerticesByName.keySet().iterator();
        while (existingNamesIterator.hasNext()) {
            String existingName = existingNamesIterator.next();
            if (!entityClassificationsByName.containsKey(existingName)) {
                // remove old classification from graph and from map
                log.debug("{} entity remove classification: {}", methodName, existingName);
                Vertex classificationVertex = existingClassificationVerticesByName.get(existingName);
                classificationVertex.remove();
                existingClassificationVerticesByName.remove(existingName);
                Edge classifierEdge = existingClassifierEdgesByName.get(existingName);
                classifierEdge.remove();
                existingClassifierEdgesByName.remove(existingName);
            }
        }
        // update/add the desired classifications
        Iterator<String> entityClassificationsNameIterator = entityClassificationsByName.keySet().iterator();
        while (entityClassificationsNameIterator.hasNext()) {
            String newClassificationName = entityClassificationsNameIterator.next();
            if (!existingClassificationVerticesByName.containsKey(newClassificationName)) {
                // add new classification vertex and edge
                log.debug("{} entity add classification: {}", methodName, newClassificationName);
                Vertex classificationVertex = g.addV("Classification").next();
                classificationMapper.mapClassificationToVertex(entityClassificationsByName.get(newClassificationName), classificationVertex);
                Edge classifierEdge = vertex.addEdge("Classifier", classificationVertex);
            } else {
                // update existing classification vertex
               log.debug("{} entity update classification: {}", methodName, newClassificationName);
                Vertex classificationVertex = existingClassificationVerticesByName.get(newClassificationName);
                classificationMapper.mapClassificationToVertex(entityClassificationsByName.get(newClassificationName), classificationVertex);
            }
        }

    }


    // updateRelationshipInStore
    synchronized void updateRelationshipInStore(Relationship relationship)
            throws
            RepositoryErrorException
    {

        String methodName = "updateRelationshipInStore";

        String guid = relationship.getGUID();
        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, guid);

        if (edgeIt.hasNext()) {
            Edge edge = edgeIt.next();
            log.debug("{} found existing edge {}", methodName, edge);

            try {

                relationshipMapper.mapRelationshipToEdge(relationship, edge);

            } catch (Exception e) {

                log.error("{} Caught exception from relationship mapper {}", methodName, e.getMessage());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_NOT_UPDATED;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
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
        String guid = entityGUID;
        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID);

        // Only looking for non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

        if (gt.hasNext()) {
            Vertex vertex = gt.next();

            Boolean isProxy = entityMapper.isProxy(vertex);
            if (!isProxy) {

                log.debug("{} found entity vertex {} to be removed", methodName, vertex);

                // Look for associated classifications.
                Iterator<Edge> classifierEdges = vertex.edges(Direction.OUT, "Classifier");
                while (classifierEdges.hasNext()) {
                    Edge classifierEdge = classifierEdges.next();
                    Vertex classificationVertex = classifierEdge.inVertex();
                    // Get the classification's name for debug/info only
                    Classification existingClassification = new Classification();
                    try {
                        classificationMapper.mapVertexToClassification(classificationVertex, existingClassification);
                    } catch (Exception e) {
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

    // removeEntityProxyFromStore
    synchronized void removeEntityProxyFromStore(String entityGUID)
    {
        final String methodName = "removeEntityProxyFromStore";
        // TODO - could capture existing entity and move it to 'history'

        // Look in the graph

        GraphTraversalSource g = instanceGraph.traversal();


        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID);

        // Only looking for proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, true);

        if (gt.hasNext()) {
            Vertex vertex = gt.next();

            Boolean isProxy = entityMapper.isProxy(vertex);
            if (isProxy) {

                log.debug("{} found entity proxy vertex {} to be removed", methodName, vertex);

                // Look for associated classifications.
                Iterator<Edge> classifierEdges = vertex.edges(Direction.OUT, "Classifier");
                while (classifierEdges.hasNext()) {
                    Edge classifierEdge = classifierEdges.next();
                    Vertex classificationVertex = classifierEdge.inVertex();
                    // Get the classification's name for debug/info only
                    Classification existingClassification = new Classification();
                    try {
                        classificationMapper.mapVertexToClassification(classificationVertex, existingClassification);
                    } catch (Exception e) {
                        log.error("{} caught exception from classification mapper for classification {}", methodName, existingClassification.getName());
                        // Nothing you can do - just keep going
                    }
                    log.debug("{} removing classification {} from entity proxy", methodName, existingClassification.getName());
                    classifierEdge.remove();
                    classificationVertex.remove();
                }

                // Finally remove the entity vertex...
                vertex.remove();

                log.debug("{} removed entity proxy vertex with guid {}", methodName, entityGUID);
            }
        }
        g.tx().commit();

    }

    // removeRelationshipFromStore
    synchronized void removeRelationshipFromStore(String relationshipGUID)
    {
        final String methodName = "removeRelationshipFromStore";
        // TODO - could capture existing entity and move it to 'history'

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();

        Iterator<Edge> edgeIt = g.E().hasLabel("Relationship").has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGUID);
        if (edgeIt.hasNext()) {
            Edge edge = edgeIt.next();
            log.debug("{} found existing edge {}", methodName, edge);
            edge.remove();
            log.debug("{} removed relationship edge with guid {}", methodName, relationshipGUID);
        }
        g.tx().commit();

    }

    // getRelationshipsForEntity
    synchronized List<Relationship> getRelationshipsForEntity(String entityGUID)

            throws
            TypeErrorException,
            RepositoryErrorException
    {
        final String methodName = "getRelationshipsForEntity";

        List<Relationship> relationships = new ArrayList<>();

        // Look in the graph
        GraphTraversalSource g = instanceGraph.traversal();
        Iterator<Vertex> vi = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID);
        if (vi.hasNext()) {
            Vertex vertex = vi.next();
            log.debug("{} found entity vertex {}", methodName, vertex);

            Iterator<Edge> edges = vertex.edges(Direction.BOTH, "Relationship");
            log.debug("{} entity has these edges {}", methodName, edges);
            while (edges.hasNext()) {
                Edge edge = edges.next();
                log.debug("{} entity has edge {}", methodName, edge);

                Relationship relationship = new Relationship();
                relationshipMapper.mapEdgeToRelationship(edge, relationship);
                relationships.add(relationship);
            }
        }

        g.tx().commit();

        return relationships;
    }


    // findEntitiesByProperty
    List<EntityDetail> findEntitiesByProperty(String typeDefName,
                                              InstanceProperties matchProperties,
                                              MatchCriteria matchCriteria)
            throws
            RepositoryErrorException,
            InvalidParameterException

    {

        final String methodName = "findEntitiesByProperty";

        List<EntityDetail> entities = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Entity");
        if (typeDefName != null) {
            gt = gt.has(PROPERTY_KEY_ENTITY_TYPE_NAME, typeDefName);
        }

        // Only accept non-proxy entities:
        gt = gt.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);



        /*
         * There is one namespace covering both type-defined properties (the ones defined by a TypeDefAttribute in the TypeDef) and
         * the 'core properties' - fields like 'guid', 'createdBy' which are common to all instances regardless of type.
         *
         * MatchProperties are supplied using the short property name for each property, but the short names need to be qualified
         * to convert them into the unique property keys used in the graph.
         *
         * Type-specific properties are stored in the graph using their 'qualified property names' - which means the short name is
         * prefixed by the name of the type in which the property is defined.
         *
         * Core properties are prefixed by a prefix that indicates the type of graph element in which they appear. This ensures that
         * property keys are unique across vertices and edges (ad hence across all the global indexes) which makes startup and
         * index creation faster. They are not qualified by Type in case it is necessary to support cross-type graph queries, e.g.
         * find all the entities (of any type) that were createdBy 'admin'.
         *
         * Returning to the point about 'one namespace', the caller can provide a mix of type-specific and core properties, all
         * expressed using their short names. Any property (short) names that match type-specific properties are used in that
         * context, and any other property (short) names are assumed to be core properties. If a caller includes a property by
         * short name that is neither a type-defined nor core property then the search will fail - which is OK because the
         * match properties contain a condition that cannot be satisfied.
         *
         * In this find method it is necessary to map the provided short names to the qualified names in order to hit the indexes.
         *
         * For the type of entity, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
         */

        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        Map<String, String> qualifiedPropertyNames = GraphOMRSMapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);


        // This relies on the graph to enforce property validity - it does not pre-check that match properties are valid for requested type.
        if (matchProperties != null) {
            List<DefaultGraphTraversal> propCriteria = new ArrayList<>();
            Iterator<String> propNames = matchProperties.getPropertyNames();
            while (propNames.hasNext()) {
                // Assume that mapping is String for all properties (core or type-specific) - some core properties may use Full-Text; see below...
                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                String propName = propNames.next();
                String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                if (qualifiedPropertyName == null) {
                    // Assume this is a core property - if it is not then it's OK - the graph will reject.
                    // Because we are searching for entities prefix using the vertex prefix.
                    qualifiedPropertyName = PROPERTY_KEY_PREFIX_ENTITY + propName;
                    mapping = corePropertyMixedIndexMappings.get(qualifiedPropertyName);
                }
                InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                if (ipvCat == InstancePropertyCategory.PRIMITIVE) {
                    // Primitives will have been stored in the graph as such
                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                    PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                    Object primValue = ppv.getPrimitiveValue();
                    log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                    DefaultGraphTraversal t = new DefaultGraphTraversal();
                    switch (pCat) {
                        case OM_PRIMITIVE_TYPE_STRING:
                            // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                            if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text) {
                                t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, Text.textContainsRegex(primValue)); // for a field indexed using Text mapping use textContains or textContainsRegex
                            } else {
                                String ANYCHARS = ".*";
                                t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, Text.textRegex(ANYCHARS + primValue + ANYCHARS));         // for a field indexed using String mapping use textRegex
                            }
                            break;
                        default:
                            t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, primValue);
                            break;
                    }
                    log.debug("{} primitive match property has property criterion {}", methodName, t);
                    propCriteria.add(t);
                } else {
                    log.debug("{} non-primitive match property {} ignored", propName);
                }
            }

            switch (matchCriteria) {
                case ALL:
                    gt = gt.and(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                case ANY:
                    gt = gt.or(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                case NONE:
                    DefaultGraphTraversal t = new DefaultGraphTraversal();
                    t = (DefaultGraphTraversal) t.or(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    gt = gt.not(t);
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                default:
                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.INVALID_MATCH_CRITERIA;
                    g.tx().rollback();
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                            this.getClass().getName(),
                            repositoryName);

                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

            }
        }


        while (gt.hasNext()) {
            Vertex vertex = gt.next();
            log.debug("{} found vertex {}", methodName, vertex);

            EntityDetail entityDetail = new EntityDetail();
            try {
                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(vertex);
                if (!isProxy) {
                    entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                    entities.add(entityDetail);
                }
            } catch (Exception e) {
                log.error("{} caught exception from entity mapper, entity being ignored, {}", methodName, e.getMessage());
                continue;
            }
        }

        g.tx().commit();

        return entities;

    }


    // findRelationshipsByProperty
    List<Relationship> findRelationshipsByProperty(String typeDefName,
                                                   InstanceProperties matchProperties,
                                                   MatchCriteria matchCriteria)
            throws
            RepositoryErrorException,
            InvalidParameterException

    {

        final String methodName = "findRelationshipsByProperty";

        List<Relationship> relationships = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        GraphTraversal<Edge, Edge> gt = g.E().hasLabel("Relationship");
        if (typeDefName != null) {
            gt = gt.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, typeDefName);
        }

        // MatchProperties are expressed using the short property name for each property.
        // Properties are stored in the graph with qualified property names - so we need to map to those in order to hit the indexes.
        // For the type of the entity, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        Map<String, String> qualifiedPropertyNames = GraphOMRSMapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

        // This relies on the graph to enforce property validity - it does not pre-check that match properties are valid for requested type.
        if (matchProperties != null) {
            List<DefaultGraphTraversal> propCriteria = new ArrayList<>();
            Iterator<String> propNames = matchProperties.getPropertyNames();
            while (propNames.hasNext()) {
                // Assume that mapping is String for all properties (core or type-specific) - some core properties may use Full-Text; see below...
                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                String propName = propNames.next();
                String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                if (qualifiedPropertyName == null) {
                    // Assume this is a core property - if it is not then it's OK - the graph will reject.
                    qualifiedPropertyName = PROPERTY_KEY_PREFIX_RELATIONSHIP + propName;
                    mapping = corePropertyMixedIndexMappings.get(qualifiedPropertyName);
                }
                InstancePropertyValue ipv = matchProperties.getPropertyValue(propName);
                InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                if (ipvCat == InstancePropertyCategory.PRIMITIVE) {
                    // Primitives will have been stored in the graph as such
                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                    PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                    Object primValue = ppv.getPrimitiveValue();
                    log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                    DefaultGraphTraversal t = new DefaultGraphTraversal();
                    switch (pCat) {
                        case OM_PRIMITIVE_TYPE_STRING:
                            // NB This is using a JG specific approach to text predicates - see the static import above. From TP 3.4.0 try to use the TP text predicates.
                            if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text) {
                                t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, Text.textContainsRegex(primValue)); // for a field indexed using Text mapping use textContains or textContainsRegex
                            } else {
                                String ANYCHARS = ".*";
                                t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, Text.textRegex(ANYCHARS + primValue + ANYCHARS));         // for a field indexed using String mapping use textRegex
                            }
                            break;
                        default:
                            t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, primValue);
                            break;
                    }
                    log.debug("{} primitive match property has property criterion {}", methodName, t);
                    propCriteria.add(t);
                } else {
                    log.debug("{} non-primitive match property {} ignored", propName);
                }
            }

            switch (matchCriteria) {
                case ALL:
                    gt = gt.and(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                case ANY:
                    gt = gt.or(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                case NONE:
                    DefaultGraphTraversal t = new DefaultGraphTraversal();
                    t = (DefaultGraphTraversal) t.or(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    gt = gt.not(t);
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                default:
                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.INVALID_MATCH_CRITERIA;
                    g.tx().rollback();
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                            this.getClass().getName(),
                            repositoryName);

                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

            }
        }

        while (gt.hasNext()) {
            Edge edge = gt.next();
            log.debug("{} found edge {}", methodName, edge);
            Relationship relationship = new Relationship();
            relationshipMapper.mapEdgeToRelationship(edge, relationship);

            // Set the relationship ends...
            try {
                Vertex vertexOne = edge.outVertex();
                Vertex vertexTwo = edge.inVertex();

                // Doesn't matter whether vertices represent proxy entities or full entities - retrieve the entities as proxies
                if (vertexOne != null) {
                    log.debug("{} entity vertex {}", methodName, vertexOne);
                    EntityProxy entityOneProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexOne, entityOneProxy);
                    log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                    relationship.setEntityOneProxy(entityOneProxy);
                }
                if (vertexTwo != null) {
                    log.debug("{} entity vertex {}", methodName, vertexTwo);
                    EntityProxy entityTwoProxy = new EntityProxy();
                    entityMapper.mapVertexToEntityProxy(vertexTwo, entityTwoProxy);
                    log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                    relationship.setEntityTwoProxy(entityTwoProxy);
                }

            } catch (Exception e) {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

            relationships.add(relationship);
        }

        g.tx().commit();

        return relationships;

    }


    // For each searchable type convert searchCriteria into matchProperties
    public InstanceProperties constructMatchPropertiesForSearchCriteria(TypeDef typeDef, String searchCriteria, GraphOMRSConstants.ElementType elementType)
    {

        final String methodName = "constructMatchPropertiesForSearchCriteria";

        InstanceProperties stringMatchProperties = new InstanceProperties();

        // Include any string-based core properties, apart from typeName since that is either specified (by typeGUID) or fully wild.
        // The core properties are not prefixed at this stage - this is still in matchProperty space - i.e. using raw short names. They
        // will be prefixed in the finder method that actually looks for matching elements in the graph.

        // Only include the core properties for the type category

        Iterator<String> relevantCoreProperties = null;
        switch (typeDef.getCategory()) {
            case ENTITY_DEF:
                relevantCoreProperties = corePropertiesEntity.keySet().iterator();
                break;
            case RELATIONSHIP_DEF:
                relevantCoreProperties = corePropertiesRelationship.keySet().iterator();
                break;
            case CLASSIFICATION_DEF:
                relevantCoreProperties = corePropertiesClassification.keySet().iterator();
                break;
        }

        if (relevantCoreProperties != null) {
            while (relevantCoreProperties.hasNext()) {
                //for (String corePropName : relevantCoreProperties) {
                String corePropName = relevantCoreProperties.next();
                if (corePropertyTypes.get(corePropName).equals("java.lang.String") && !corePropName.equals(PROPERTY_NAME_TYPE_NAME)) {
                    PrimitivePropertyValue ppv = new PrimitivePropertyValue();
                    ppv.setPrimitiveDefCategory(OM_PRIMITIVE_TYPE_STRING);
                    ppv.setPrimitiveValue((Object) searchCriteria);
                    log.debug("{} include string type core property {} value {}", methodName, corePropName, ppv);
                    stringMatchProperties.setProperty(corePropName, ppv);
                }
            }
        }

        // Include string-based type-defined properties

        List<TypeDefAttribute> propertiesDefinition = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

        if (propertiesDefinition != null) {

            for (TypeDefAttribute typeDefAttribute : propertiesDefinition) {

                if (typeDefAttribute != null) {

                    String propertyName = typeDefAttribute.getAttributeName();

                    if (propertyName != null) {

                        AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                        AttributeTypeDefCategory atdCategory = atd.getCategory();
                        if (atdCategory == PRIMITIVE) {
                            PrimitiveDef primDef = (PrimitiveDef) atd;
                            PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                            if (primDefCat == OM_PRIMITIVE_TYPE_STRING) {
                                PrimitivePropertyValue ppv = new PrimitivePropertyValue();
                                ppv.setPrimitiveDefCategory(primDefCat);
                                ppv.setPrimitiveValue((Object) searchCriteria);
                                log.debug("{} include search property {} value {}", methodName, propertyName, ppv);
                                stringMatchProperties.setProperty(propertyName, ppv);
                            }
                        }
                    }
                }
            }
        } else {
            // no properties defined
            log.debug("{} no type-specific search properties to add", methodName);
        }

        return stringMatchProperties;

    }


    public void createEntityIndexes(TypeDef typeDef)
    {

        final String methodName = "createEntityIndexes";

        // Add mixed index for any primitive properties
        // (This does mean that we will introduce a mixed index for Date although we cannot yet query with date ranges for example..... )

        // MatchProperties are expressed using the short property name for each property.
        // Properties are stored in the graph with qualified property names - so we need to map to those in order to hit the indexes.
        // For the type of the entity, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
        Map<String, String> qualifiedPropertyNames = GraphOMRSMapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

        List<TypeDefAttribute> propertyDefs = typeDef.getPropertiesDefinition();
        if (propertyDefs == null || propertyDefs.isEmpty()) {
           log.debug("{} no vertex indexes needed for type {}", methodName, typeDef.getName());
            return;
        }

        log.debug("{} create vertex indexes for type {}", methodName, typeDef.getName());

        for (TypeDefAttribute typeDefAttribute : propertyDefs) {

            if (typeDefAttribute != null) {

                String propertyName = typeDefAttribute.getAttributeName();

                if (propertyName != null) {

                    AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                    AttributeTypeDefCategory atdCategory = atd.getCategory();

                    if (atdCategory == PRIMITIVE) {

                        String qualifiedPropertyName = qualifiedPropertyNames.get(propertyName);
                        log.debug("{} qualified property name {}", methodName, qualifiedPropertyName);

                        PrimitiveDef primDef = (PrimitiveDef) atd;
                        PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                        GraphOMRSGraphFactory.MixedIndexMapping mapping;

                        if (primDefCat == OM_PRIMITIVE_TYPE_STRING)
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        else
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.Default;

                        GraphOMRSGraphFactory.createMixedIndexForVertexProperty(
                                qualifiedPropertyName,
                                getPropertyKeyEntity(qualifiedPropertyName),
                                primDefCat.getJavaClassName(),
                                mapping);

                    }
                }
            }
        }
    }


    public void createClassificationIndexes(TypeDef typeDef)
    {

        final String methodName = "createClassificationIndexes";

        // Add mixed index for any primitive properties
        // (This does mean that we will introduce a mixed index for Date although we cannot yet query with date ranges for example..... )

        // MatchProperties are expressed using the short property name for each property.
        // Properties are stored in the graph with qualified property names - so we need to map to those in order to hit the indexes.
        // For the type of the classification, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
        Map<String, String> qualifiedPropertyNames = GraphOMRSMapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);


        List<TypeDefAttribute> propertyDefs = typeDef.getPropertiesDefinition();
        if (propertyDefs == null || propertyDefs.isEmpty()) {
            log.debug("{} no vertex indexes needed for type {}", methodName, typeDef.getName());
            return;
        }

        log.debug("{} create vertex indexes for type {}", methodName, typeDef.getName());

        for (TypeDefAttribute typeDefAttribute : propertyDefs) {

            if (typeDefAttribute != null) {

                String propertyName = typeDefAttribute.getAttributeName();

                if (propertyName != null) {

                    AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                    AttributeTypeDefCategory atdCategory = atd.getCategory();

                    if (atdCategory == PRIMITIVE) {

                        String qualifiedPropertyName = qualifiedPropertyNames.get(propertyName);

                        PrimitiveDef primDef = (PrimitiveDef) atd;
                        PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                        GraphOMRSGraphFactory.MixedIndexMapping mapping;

                        if (primDefCat == OM_PRIMITIVE_TYPE_STRING)
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        else
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.Default;

                        GraphOMRSGraphFactory.createMixedIndexForVertexProperty(
                                qualifiedPropertyName,
                                getPropertyKeyClassification(qualifiedPropertyName),
                                primDefCat.getJavaClassName(),
                                mapping);

                    }
                }
            }
        }
    }


    public void createRelationshipIndexes(TypeDef typeDef)
    {

        final String methodName = "createRelationshipIndexes";

        // Add mixed index for any primitive properties
        // (This does mean that we will introduce a mixed index for Date although we cannot yet query with date ranges for example..... )

        List<TypeDefAttribute> propertyDefs = typeDef.getPropertiesDefinition();
        if (propertyDefs == null || propertyDefs.isEmpty()) {
            log.debug("{} no edge indexes needed for type {}", methodName, typeDef.getName());
            return;
        }

        // MatchProperties are expressed using the short property name for each property.
        // Properties are stored in the graph with qualified property names - so we need to map to those in order to hit the indexes.
        // For the type of the relationship, construct a map of short prop name -> qualified prop name. There is no type hierarchy for
        // relationships - but that doesn't matter, the util method will handle types for which superType is null.
        Map<String, String> qualifiedPropertyNames = GraphOMRSMapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);


        log.debug("{} create edge indexes for type {}", methodName, typeDef.getName());

        for (TypeDefAttribute typeDefAttribute : propertyDefs) {

            if (typeDefAttribute != null) {

                String propertyName = typeDefAttribute.getAttributeName();

                if (propertyName != null) {

                    AttributeTypeDef atd = typeDefAttribute.getAttributeType();
                    AttributeTypeDefCategory atdCategory = atd.getCategory();

                    if (atdCategory == PRIMITIVE) {

                        String qualifiedPropertyName = qualifiedPropertyNames.get(propertyName);

                        PrimitiveDef primDef = (PrimitiveDef) atd;
                        PrimitiveDefCategory primDefCat = primDef.getPrimitiveDefCategory();
                        GraphOMRSGraphFactory.MixedIndexMapping mapping;

                        if (primDefCat == OM_PRIMITIVE_TYPE_STRING)
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                        else
                            mapping = GraphOMRSGraphFactory.MixedIndexMapping.Default;

                        GraphOMRSGraphFactory.createMixedIndexForEdgeProperty(
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
    public List<EntityDetail> findEntitiesByClassification(String classificationName,
                                                           InstanceProperties classificationProperties,
                                                           MatchCriteria matchCriteria,
                                                           String entityTypeName)
            throws
            InvalidParameterException,
            RepositoryErrorException
    {

        final String methodName = "findEntitiesByClassification";

        List<EntityDetail> entities = new ArrayList<>();

        GraphTraversalSource g = instanceGraph.traversal();

        // classificationName has already been validated and is known not to be null
        // entity typeName has already been validated and is known to be valid the classification type

        GraphTraversal<Vertex, Vertex> gt = g.V().hasLabel("Classification");
        if (classificationName != null) {
            gt = gt.has(PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME, classificationName);
        }



        /*
         * For details of property namespace and how names are qualified please refer to comment in findEntitiesByProperty(). A
         * similar approach applies to classification properties.
         */

        TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, classificationName);
        Map<String, String> qualifiedPropertyNames = GraphOMRSMapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);


        // This relies on the graph to enforce property validity - it does not pre-check that classification match properties are valid for requested type.
        if (classificationProperties != null) {
            List<DefaultGraphTraversal> propCriteria = new ArrayList<>();
            Iterator<String> propNames = classificationProperties.getPropertyNames();
            while (propNames.hasNext()) {
                GraphOMRSGraphFactory.MixedIndexMapping mapping = GraphOMRSGraphFactory.MixedIndexMapping.String;
                String propName = propNames.next();
                String qualifiedPropertyName = qualifiedPropertyNames.get(propName);
                if (qualifiedPropertyName == null) {
                    // Assume this is a core property - if it is not then it's OK - the graph will reject.
                    // Because we are searching for entities prefix using the vertex prefix.
                    qualifiedPropertyName = PROPERTY_KEY_PREFIX_ENTITY + propName;
                    mapping = corePropertyMixedIndexMappings.get(qualifiedPropertyName);
                }
                InstancePropertyValue ipv = classificationProperties.getPropertyValue(propName);
                InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                if (ipvCat == InstancePropertyCategory.PRIMITIVE) {
                    // Primitives will have been stored in the graph as such
                    PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                    PrimitiveDefCategory pCat = ppv.getPrimitiveDefCategory();
                    Object primValue = ppv.getPrimitiveValue();
                    log.debug("{} primitive match property has key {} value {}", methodName, propName, primValue);
                    DefaultGraphTraversal t = new DefaultGraphTraversal();
                    switch (pCat) {
                        case OM_PRIMITIVE_TYPE_STRING:
                            // NB This is using a JG specific approach to text predicates - see the static import above.
                            // From TP 3.4.0 try to use the TP text predicates.
                            if (mapping == GraphOMRSGraphFactory.MixedIndexMapping.Text) {
                                t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, Text.textContainsRegex(primValue)); // for a field indexed using Text mapping use textContains or textContainsRegex
                            } else {
                                String ANYCHARS = ".*";
                                t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, Text.textRegex(ANYCHARS + primValue + ANYCHARS));         // for a field indexed using String mapping use textRegex
                            }
                            break;
                        default:
                            t = (DefaultGraphTraversal) t.has(qualifiedPropertyName, primValue);
                            break;
                    }
                    log.debug("{} primitive match property has property criterion {}", methodName, t);
                    propCriteria.add(t);
                } else {
                    log.debug("{} non-primitive match property {} ignored", propName);
                }
            }

            switch (matchCriteria) {
                case ALL:
                    gt = gt.and(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                case ANY:
                    gt = gt.or(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                case NONE:
                    DefaultGraphTraversal t = new DefaultGraphTraversal();
                    t = (DefaultGraphTraversal) t.or(propCriteria.toArray(new DefaultGraphTraversal[0]));
                    gt = gt.not(t);
                    log.debug("{} traversal looks like this --> {} ", methodName, gt);
                    break;
                default:
                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.INVALID_MATCH_CRITERIA;
                    g.tx().rollback();
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                            this.getClass().getName(),
                            repositoryName);

                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

            }
        }


        // Cannot return EntityProxy objects, so ensure that only traverse to a non-proxy entity vertex...
        gt.in("Classifier").has(PROPERTY_KEY_ENTITY_IS_PROXY, false).has(PROPERTY_KEY_ENTITY_TYPE_NAME, entityTypeName);


        while (gt.hasNext()) {
            Vertex entityVertex = gt.next();
            log.debug("{} found entity vertex {}", methodName, entityVertex);

            EntityDetail entityDetail = new EntityDetail();
            try {
                // Check if we have stumbled on a proxy somehow, and if so avoid processing it.
                Boolean isProxy = entityMapper.isProxy(entityVertex);
                if (!isProxy) {
                    entityMapper.mapVertexToEntityDetail(entityVertex, entityDetail);
                    entities.add(entityDetail);
                }
            } catch (Exception e) {
                log.error("{} caught exception from entity mapper - entity will be ignored, {}", methodName, e.getMessage());
                continue; // process the next vertex
            }
        }

        g.tx().commit();

        return entities;

    }


    public InstanceGraph getSubGraph(String entityGUID,
                                     List<String> entityTypeGUIDs,
                                     List<String> relationshipTypeGUIDs,
                                     List<InstanceStatus> limitResultsByStatus,
                                     List<String> limitResultsByClassification,
                                     int level)
            throws
            TypeErrorException,
            EntityNotKnownException
    {

        final String methodName = "getSubGraph";
        final String entTypeGUIDsParameterName = "entityTypeGUIDs";
        final String relTypeGUIDsParameterName = "relationshipTypeGUIDs";


        log.debug("{} entityGUID = {}, entityTypeGUIDs = {}, relationshipTypeGUIDs = {}, limitResultsByStatus = {}, limitResultsByClassification = {}, level = {}",
                methodName, entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, level);

        /*
         * Starting at the entity wth entityGUID, traverse relationships and other entities, filtering by instance types and statuses
         * and classifications, if specified. Traverse to a maximum depth specified by level.
         * The root entity is always included regardless of the entityTypeGUIDs.
         *
         * Only EntityDetail objects are returned in InstanceGraph.entities, but EntityProxy objects are traversed and are embedded in InstanceGraph.relationships.
         */


        List<EntityDetail> entities = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();

        InstanceGraph subGraph = new InstanceGraph();


        // The optional entity type filter specifies which entity types values are permissible.
        List<String> entityTypeNames = new ArrayList<>();
        boolean entitiesWithin = false;
        if (entityTypeGUIDs != null) {
            entitiesWithin = true;
            for (String entTypeGUID : entityTypeGUIDs) {
                try {
                    TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entTypeGUIDsParameterName, entTypeGUID, methodName);
                    String entTypeName = typeDef.getName();
                    entityTypeNames.add(entTypeName);
                } catch (Exception e) {
                    log.error("{} caught exception from repository helper trying to resolve type with GUID {}", methodName, entTypeGUID);

                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_TYPE_GUID_NOT_KNOWN;

                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entTypeGUID, methodName, this.getClass().getName(), repositoryName);

                    throw new TypeErrorException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }

            }
        }


        // The optional relationship type filter specifies which relationship types values are permissible.
        List<String> relationshipTypeNames = new ArrayList<>();
        boolean relationshipsWithin = false;
        if (relationshipTypeGUIDs != null) {
            relationshipsWithin = true;
            for (String relTypeGUID : relationshipTypeGUIDs) {
                try {
                    TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, relTypeGUIDsParameterName, relTypeGUID, methodName);
                    String relTypeName = typeDef.getName();
                    relationshipTypeNames.add(relTypeName);
                } catch (Exception e) {
                    log.error("{} caught exception from repository helper trying to resolve type with GUID {}", methodName, relTypeGUID);

                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_TYPE_GUID_NOT_KNOWN;

                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relTypeGUID, methodName, this.getClass().getName(), repositoryName);

                    throw new TypeErrorException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
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
        if (limitResultsByStatus == null) {
            statusOrdinals.add(InstanceStatus.DELETED.getOrdinal());   // Do not traverse a DELETED element by default
        } else {  // positive status filter was specified
            statusWithin = true;
            for (InstanceStatus iStatus : limitResultsByStatus) {
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
        if (limitResultsByClassification != null) {
            classificationWithin = true;
            for (String cName : limitResultsByClassification) {
                classificationNames.add(cName);
            }
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
         *     inV().has({entity-status-filter}).has({entity-type-filter}).as("e").
         *         where(out("Classifier").has({classification-filter})).
         *     simplePath()).
         * times(2).emit().select("r","e")
         *
         * THe various filters are optional and are implemented using has(<property>,within(<filter-collection>))
         */


        GraphTraversalSource g = instanceGraph.traversal();

        try {

            Vertex rootVertex = null;
            GraphTraversal t = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID);

            if (!t.hasNext()) {

                log.error("{} could not retrieve start entity with GUID {}", methodName, entityGUID);
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID, methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {

                // Find the root vertex
                rootVertex = (Vertex) t.next();
                log.debug("{} found root entity vertex {}", methodName, rootVertex);

                try {
                    EntityDetail rootEntity = new EntityDetail();
                    entityMapper.mapVertexToEntityDetail(rootVertex, rootEntity);
                    entities.add(rootEntity);
                    g.tx().commit();
                } catch (EntityProxyOnlyException | RepositoryErrorException e) {
                    log.error("{} caught exception whilst trying to map entity with GUID {}, exception {}", methodName, entityGUID, e.getMessage());
                    g.tx().rollback();
                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID, methodName,
                            this.getClass().getName(),
                            repositoryName);

                    throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }


                // Reset the traversal - not sure if this is strictly necessary

                g = instanceGraph.traversal();

                DefaultGraphTraversal repeatTraversal = new DefaultGraphTraversal<>();
                repeatTraversal = (DefaultGraphTraversal) repeatTraversal.bothE("Relationship");

                // Optionally filter relationships by status
                if (statusWithin) {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_RELATIONSHIP_STATUS, within(statusOrdinals));
                } else {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_RELATIONSHIP_STATUS, without(statusOrdinals));
                }

                // Optionally filter by relationship type
                if (relationshipsWithin) {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, within(relationshipTypeNames));
                }

                // Project the relationships and move on to the inVertex for each relationship...
                repeatTraversal = (DefaultGraphTraversal) repeatTraversal.as("r").inV();

                // Optionally filter entities by status
                if (statusWithin) {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_ENTITY_STATUS, within(statusOrdinals));
                } else {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_ENTITY_STATUS, without(statusOrdinals));
                }

                // Exclude EntityProxy vertices... or not... for now the traversal will traverse a proxy but only include it
                // in the relationship reported, not in the entities list.
                // repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_ENTITY_IS_PROXY, false);

                // Optionally filter by entity type
                if (entitiesWithin) {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_ENTITY_TYPE_NAME, within(entityTypeNames));
                }

                // Optionally filter (entities) by classification
                if (classificationWithin) {
                    //  where(out("Classifier").has("vcclassificationName",within("MobileAsset","Confidentiality"))).
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.where(out("Classifier").has(PROPERTY_KEY_CLASSIFICATION_CLASSIFICATION_NAME, within(classificationNames)));

                }

                // Project the traversed TO entities (only, not the entities we have traversed FROM)...
                repeatTraversal = (DefaultGraphTraversal) repeatTraversal.as("e");

                // Include simplePath to avoid back-tracking
                repeatTraversal = (DefaultGraphTraversal) repeatTraversal.simplePath();

                // Construct the overall traversal

                t = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, entityGUID).repeat(repeatTraversal).times(level).emit().select("r", "e");

                while (t.hasNext()) {

                    Map<String, Element> resTuple = (Map<String, Element>) t.next();
                    Edge edge = (Edge) resTuple.get("r");
                    Vertex vertex = (Vertex) resTuple.get("e");

                    log.debug("{} subgraph has edge {} and vertex {}", methodName, edge, vertex);

                    if (edge != null && vertex != null) {

                        Relationship relationship = new Relationship();
                        relationshipMapper.mapEdgeToRelationship(edge, relationship);
                        relationships.add(relationship);

                        // Get the end entities and add them to the relationship as proxies.

                        vertex = null;

                        try {

                            /* Map the discovered entities - need a proxy for each end of the relationship,
                             * plus, if the entity is an EntityDetail then you need to add that to
                             * entities list in the InstanceGraph too.
                             */

                            // Start with the outVertex
                            vertex = edge.outVertex();

                            if (vertex != null) {
                                log.debug("{} end 1 entity vertex {}", methodName, vertex);
                                EntityProxy entityOneProxy = new EntityProxy();
                                entityMapper.mapVertexToEntityProxy(vertex, entityOneProxy);
                                log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                                relationship.setEntityOneProxy(entityOneProxy);

                            }

                            // Move to the inVertex
                            vertex = edge.inVertex();

                            if (vertex != null) {
                                log.debug("{} end 2 entity vertex {}", methodName, vertex);
                                EntityProxy entityTwoProxy = new EntityProxy();
                                entityMapper.mapVertexToEntityProxy(vertex, entityTwoProxy);
                                log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                                relationship.setEntityTwoProxy(entityTwoProxy);

                            }

                            /* Only need to add the arrived-at vertex to the InstanceGraph
                             * because the traversed-from vertex will already have been added.
                             * Also you only add the arrived-at entity if it is not a proxy.
                             */

                            if (!entityMapper.isProxy(vertex)) {
                                EntityDetail entityDetail = new EntityDetail();
                                entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                                log.debug("{} entityDetail {}", methodName, entityDetail);
                                entities.add(entityDetail);
                            }
                        }
                        catch (EntityProxyOnlyException | RepositoryErrorException e) {
                            /* This catch block abandons the whole traversal and neighbourhood search.
                             * This may be a little draconian ut presumably better to know that something
                             * is wrong rather than plough on in ignorance.
                             */
                            log.error("{} caught exception whilst trying to map entity, exception {}", methodName, e.getMessage());
                            g.tx().rollback();
                            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityMapper.getEntityGUID(vertex), methodName,
                                    this.getClass().getName(),
                                    repositoryName);

                            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                                    this.getClass().getName(),
                                    methodName,
                                    errorMessage,
                                    errorCode.getSystemAction(),
                                    errorCode.getUserAction());
                        }
                    }
                }
            }

            g.tx().commit();

            // Construct the InstanceGraph from entities and relationships
            subGraph.setEntities(entities);
            subGraph.setRelationships(relationships);

            return subGraph;

        } catch (Exception e) {
            log.error("{} caught exception from subgraph traversal {}", methodName, e.getMessage());
            g.tx().rollback();
            return null;
        }


    }

    public InstanceGraph getPaths(String startEntityGUID,
                                  String endEntityGUID,
                                  List<InstanceStatus> limitResultsByStatus,
                                  int maxPaths,
                                  int maxDepth)

            throws
            TypeErrorException,
            EntityNotKnownException
    {

        final String methodName = "getPaths";


        log.debug("{} startEntityGUID = {}, endEntityGUID = {}, limitResultsByStatus = {}, maxPaths = {}",
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
        if (limitResultsByStatus == null) {
            statusOrdinals.add(InstanceStatus.DELETED.getOrdinal());   // Do not traverse a DELETED element by default
        } else {  // positive status filter was specified
            statusWithin = true;
            for (InstanceStatus iStatus : limitResultsByStatus) {
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


        try {

            // Although not strictly needed for this traversal, it is important that the start entity exists - otherwise throw entity not found exception
            // represented by an EntityDetail.

            Vertex rootVertex = null;
            GraphTraversal t = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, startEntityGUID);
            if (!t.hasNext()) {

                log.error("{} could not retrieve start entity with GUID {}", methodName, startEntityGUID);
                g.tx().rollback();
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(startEntityGUID, methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {

                // Since we have access to it now, we might as well map the rootVertex to produce helpful debug information

                rootVertex = (Vertex) t.next();
                log.debug("{} found root entity vertex {}", methodName, rootVertex);

                try {

                    EntityDetail rootEntity = new EntityDetail();
                    entityMapper.mapVertexToEntityDetail(rootVertex, rootEntity);
                    log.debug("{} mapped root entity {}", methodName, rootEntity);
                    // No need to add rootEntity to InstanceGraph.entities - it will be included in the paths discovered below.
                    g.tx().commit();

                } catch (EntityProxyOnlyException | RepositoryErrorException e) {

                    log.error("{} caught exception whilst trying to map entity with GUID {}, exception {}", methodName, startEntityGUID, e.getMessage());
                    g.tx().rollback();
                    GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(startEntityGUID, methodName,
                            this.getClass().getName(),
                            repositoryName);

                    throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }


                // Reset the traversal - not sure if this is strictly necessary

                g = instanceGraph.traversal();

                DefaultGraphTraversal repeatTraversal = new DefaultGraphTraversal<>();
                repeatTraversal = (DefaultGraphTraversal) repeatTraversal.bothE("Relationship");

                // Optionally filter relationships by status
                if (statusWithin) {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_RELATIONSHIP_STATUS, within(statusOrdinals));
                } else {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_RELATIONSHIP_STATUS, without(statusOrdinals));
                }

                // Move on to the inVertex for each relationship...
                repeatTraversal = (DefaultGraphTraversal) repeatTraversal.otherV();

                // Optionally filter entities by status
                if (statusWithin) {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_ENTITY_STATUS, within(statusOrdinals));
                } else {
                    repeatTraversal = (DefaultGraphTraversal) repeatTraversal.has(PROPERTY_KEY_ENTITY_STATUS, without(statusOrdinals));
                }


                // Include simplePath to avoid back-tracking
                repeatTraversal = (DefaultGraphTraversal) repeatTraversal.simplePath();

                // Repeat terminator traversal...
                DefaultGraphTraversal untilTraversal = new DefaultGraphTraversal<>();
                untilTraversal = (DefaultGraphTraversal) untilTraversal.has(PROPERTY_KEY_ENTITY_GUID, endEntityGUID).or().loops().is(P.gte(maxDepth));

                // Construct the overall traversal

                t = g.V().hasLabel("Entity").has(PROPERTY_KEY_ENTITY_GUID, startEntityGUID).repeat(repeatTraversal).until(untilTraversal).
                        has(PROPERTY_KEY_ENTITY_GUID, endEntityGUID).path().limit(maxPaths).unfold().dedup().fold();

                while (t.hasNext()) {

                    List<Object> resList = (List<Object>) t.next();

                    if (!resList.isEmpty()) {

                        Edge edge;
                        Vertex vertex;

                        for (Object object : resList) {

                            if (object instanceof Vertex) {

                                vertex = (Vertex) object;

                                if (vertex != null) {
                                    log.debug("{} subgraph has vertex {}", methodName, vertex);

                                    if (!entityMapper.isProxy(vertex)) {
                                        try {
                                            EntityDetail entityDetail = new EntityDetail();
                                            entityMapper.mapVertexToEntityDetail(vertex, entityDetail);
                                            log.debug("{} entityDetail {}", methodName, entityDetail);
                                            entities.add(entityDetail);
                                        }
                                        catch (RepositoryErrorException | EntityProxyOnlyException e) {
                                            log.error("{} could not map vertex returned in path expression, entity GUID {}, exception {}", methodName, entityMapper.getEntityGUID(vertex), e.getMessage());
                                            g.tx().rollback();
                                            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                                            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(startEntityGUID, methodName,
                                                    this.getClass().getName(),
                                                    repositoryName);

                                            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
                                        }
                                    }
                                }

                            } else if (object instanceof Edge) {

                                edge = (Edge) object;

                                if (edge != null) {
                                    log.debug("{} subgraph has edge {} ", methodName, edge);

                                    Relationship relationship = new Relationship();
                                    relationshipMapper.mapEdgeToRelationship(edge, relationship);
                                    relationships.add(relationship);

                                    // Get the end entities and add them to the relationship as proxies.

                                    vertex = null;

                                    try {

                                        /* Map the discovered entities - need a proxy for each end of the relationship,
                                         * plus, if the entity is an EntityDetail then you need to add that to
                                         * entities list in the InstanceGraph too.
                                         */

                                        // Start with the outVertex
                                        vertex = edge.outVertex();

                                        if (vertex != null) {
                                            log.debug("{} end 1 entity vertex {}", methodName, vertex);
                                            EntityProxy entityOneProxy = new EntityProxy();
                                            entityMapper.mapVertexToEntityProxy(vertex, entityOneProxy);
                                            log.debug("{} entityOneProxy {}", methodName, entityOneProxy);
                                            relationship.setEntityOneProxy(entityOneProxy);

                                        }

                                        // Move to the inVertex
                                        vertex = edge.inVertex();

                                        if (vertex != null) {
                                            log.debug("{} end 2 entity vertex {}", methodName, vertex);
                                            EntityProxy entityTwoProxy = new EntityProxy();
                                            entityMapper.mapVertexToEntityProxy(vertex, entityTwoProxy);
                                            log.debug("{} entityTwoProxy {}", methodName, entityTwoProxy);
                                            relationship.setEntityTwoProxy(entityTwoProxy);

                                        }

                                    }
                                    catch (RepositoryErrorException e) {

                                        /* This catch block abandons the whole traversal and neighbourhood search.
                                         * This may be a little draconian but presumably better to know that something
                                         * is wrong rather than plough on in ignorance.
                                         */
                                        log.error("{} caught exception whilst trying to map entity, exception {}", methodName, e.getMessage());
                                        g.tx().rollback();
                                        GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.ENTITY_NOT_FOUND;

                                        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityMapper.getEntityGUID(vertex), methodName,
                                                this.getClass().getName(),
                                                repositoryName);

                                        throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());
                                    }
                                }
                            } else {
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

        } catch (Exception e) {
            log.error("{} caught exception from subgraph traversal {}", methodName, e.getMessage());
            g.tx().rollback();
            return null;
        }


    }
}
