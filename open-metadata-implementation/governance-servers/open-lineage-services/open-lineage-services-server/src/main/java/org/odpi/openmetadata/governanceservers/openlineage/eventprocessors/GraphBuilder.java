/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.DeletePurgedRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.Element;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.RelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.*;
import org.odpi.openmetadata.governanceservers.openlineage.GraphEntityMapper;
import org.odpi.openmetadata.governanceservers.openlineage.GraphRelationshipMapper;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.exceptions.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.bufferGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.mainGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.*;

public class GraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(GraphBuilder.class);

    private GraphEntityMapper graphEntityMapper;
    private GraphRelationshipMapper graphRelationshipMapper;
    private JobConfiguration jobConfiguration;

    public GraphBuilder() {

        this.graphEntityMapper = new GraphEntityMapper();
        this.graphRelationshipMapper = new GraphRelationshipMapper();
        this.jobConfiguration = new JobConfiguration();
    }

    //TODO make this generic for all entities for all uses cases
    public void createEntity(AssetLineageEntityEvent entity) {

        final String methodName = "createEntity";
        GraphTraversalSource g = bufferGraph.traversal();
        Vertex vertex;

        //TODO check for proxy entity
        Iterator<Vertex> vertexIt = g.V().hasLabel(entity.getTypeDefName()).has(PROPERTY_KEY_ENTITY_GUID, entity.getGUID());

        if (!vertexIt.hasNext()) {

            vertex = g.addV(entity.getTypeDefName()).next();

            try {
                vertex.property(PROPERTY_KEY_PROXY,false);
                graphEntityMapper.mapEntityToVertex(entity, vertex);


            } catch (Exception e) {
                log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                g.tx().rollback();

                OpenLineageErrorCode errorCode = OpenLineageErrorCode.ENTITY_NOT_CREATED;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
                        this.getClass().getName());

                throw new OpenLineageException(400,
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
            g.tx().commit();
        } else {
            vertex = vertexIt.next();
            Object isProxy = g.V().hasLabel(entity.getTypeDefName()).has(PROPERTY_KEY_ENTITY_GUID, entity.getGUID())
                    .values(PROPERTY_KEY_PROXY).next();
            if(Boolean.valueOf(isProxy.toString())){
                try {
                    vertex.property(PROPERTY_KEY_PROXY, false);
                    graphEntityMapper.mapEntityToVertex(entity, vertex);
                }catch (Exception e) {
                    log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
                    g.tx().rollback();

                    OpenLineageErrorCode errorCode = OpenLineageErrorCode.ENTITY_NOT_CREATED;

                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entity.getGUID(), methodName,
                            this.getClass().getName());

                    throw new OpenLineageException(400,
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

                }


                g.tx().commit();
            }
            else {
                log.debug("{} found existing vertex {}", methodName, vertex);
                g.tx().rollback();
            }

        }

    }

    public void removeSemanticRelationship(DeletePurgedRelationshipEvent event) {
        GraphTraversalSource g = mainGraph.traversal();

        final String relationshipType = event.getEntityTypeDef();

        try {
            boolean edgeExists = g.V().has(event.getGlossaryTerm().getType(), PROPERTY_KEY_ENTITY_GUID, event.getGlossaryTerm().getGuid())
                    .bothE()
                    .where(g.V().has(event.getEntityTypeDef(), PROPERTY_KEY_ENTITY_GUID, event.getEntityGuid())).hasNext();

            if (edgeExists) {

                g.V().has(event.getGlossaryTerm().getType(), PROPERTY_KEY_ENTITY_GUID, event.getGlossaryTerm().getGuid())
                        .outE(relationshipType)
                        .where(g.V().has(event.getEntityTypeDef(), PROPERTY_KEY_ENTITY_GUID, event.getEntityGuid())).next().remove();
            }
            g.tx().commit();
        } catch (Exception e) {
            log.error("Error occurred during deletion of the semantic assignment");
            g.tx().rollback();
        }
    }



    public void createRelationship(RelationshipEvent event) {

        String methodName = "createRelationship";

        final String entityProxyOne = "EntityProxyOne";
        final String entityProxyTwo = "EntityProxyTwo";

        final String relationshipType = event.getTypeDefName();
        final String relationshipGuid = event.getGUID();


        // Begin a graph transaction. Locate the vertices for the ends, and create an edge between them.
        if (relationshipType == null) {
            log.error("{} Relationship type name is missing", methodName);
            OpenLineageErrorCode errorCode = OpenLineageErrorCode.RELATIONSHIP_TYPE_NAME_NOT_KNOWN;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipGuid, methodName,
                    this.getClass().getName());

            throw new OpenLineageException(400,
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }

        if (relationshipType.equals(SEMANTIC_ASSIGNMENT)) {

            createEntitiesSemanticAssignment(event);
        }
        else {

            GraphTraversalSource g = bufferGraph.traversal();

            Iterator<Edge> edgeIt = g.E().hasLabel(relationshipType).has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGuid);
            if (edgeIt.hasNext()) {
                Edge edge = edgeIt.next();
                log.error("{} found existing edge {}", methodName, edge);
                g.tx().rollback();
                OpenLineageErrorCode errorCode = OpenLineageErrorCode.RELATIONSHIP_ALREADY_EXISTS;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipGuid, methodName,
                        this.getClass().getName());

                throw new OpenLineageException(400,
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }


            String entityOneGUID = event.getProxies().get(entityProxyOne).getGUID();
            String entityTwoGUID = event.getProxies().get(entityProxyTwo).getGUID();

            String entityOneType = event.getProxies().get(entityProxyOne).getTypeDefName();
            String entityTwoType = event.getProxies().get(entityProxyTwo).getTypeDefName();

            Vertex vertexOne = null;
            Vertex vertexTwo = null;

            Iterator<Vertex> vi = g.V().hasLabel(entityOneType).has(PROPERTY_KEY_ENTITY_GUID, entityOneGUID);
            if (vi.hasNext()) {
                vertexOne = vi.next();
                log.debug("{} found entityOne vertex {}", methodName, vertexOne);
            }

            vi = g.V().hasLabel(entityTwoType).has(PROPERTY_KEY_ENTITY_GUID, entityTwoGUID);
            if (vi.hasNext()) {
                vertexTwo = vi.next();
                log.debug("{} found entityTwo vertex {}", methodName, vertexTwo);
            }

            //Events coming asynchronously so entities must be created if do not exist
            if (vertexOne == null) {
                vertexOne = createEntityProxy(event.getProxies().get(entityProxyOne));

            }

            if (vertexTwo == null) {
                vertexTwo = createEntityProxy(event.getProxies().get(entityProxyTwo));

            }


            Edge edge = vertexOne.addEdge(relationshipType, vertexTwo);

            try {

                graphRelationshipMapper.mapRelationshipToEdge(event, edge);

            } catch (Exception e) {
                log.error("{} Caught exception from relationship mapper {}", methodName, e.getMessage());
                g.tx().rollback();
                OpenLineageErrorCode errorCode = OpenLineageErrorCode.RELATIONSHIP_NOT_CREATED;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipGuid, methodName,
                        this.getClass().getName());

                throw new OpenLineageException(400,
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

            log.debug("{} Commit tx containing creation of edge", methodName);
            g.tx().commit();
        }

    }

    private Vertex createEntityProxy(AssetLineageEntityEvent entityProxy) {

        final String methodName = "createEntityProxy";
        final String typeDefName = entityProxy.getTypeDefName();
        GraphTraversalSource g = bufferGraph.traversal();

        Vertex vertex = g.addV(typeDefName).next();

        try {
            vertex.property(PROPERTY_KEY_PROXY,true);
            graphEntityMapper.mapEntityToVertex(entityProxy, vertex);


        } catch (Exception e) {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();

            OpenLineageErrorCode errorCode = OpenLineageErrorCode.ENTITY_NOT_CREATED;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityProxy.getGUID(), methodName,
                    this.getClass().getName());

            throw new OpenLineageException(400,
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        g.tx().commit();
        return vertex;
    }

    private void createEntitiesSemanticAssignment(RelationshipEvent event) {

        GlossaryTerm glossaryTerm = event.getGlossaryTerm();
        Element technicalTerm = event.getAssetContext().getBaseAsset();
        String technicalTermType = technicalTerm.getType();

        Map<String, Element> context = event.getAssetContext().getContext();
        context.put(technicalTermType, technicalTerm);

        createGlossaryVertex(glossaryTerm,mainGraph);
        createGlossaryVertex(glossaryTerm,bufferGraph);

        createElementVertex(context,mainGraph,true);
        createElementVertex(context,bufferGraph,false);

        semanticAssignmentCreateRelationshipsBuffer(context, glossaryTerm, technicalTermType);
        semanticAssignmentCreateRelationshipsMain(context, glossaryTerm, technicalTermType);

    }

    private void createGlossaryVertex(GlossaryTerm term, JanusGraph graph) {
        GraphTraversalSource g = graph.traversal();

        Iterator<Vertex> vertexIt = g.V().hasLabel(term.getType()).has(PROPERTY_KEY_ENTITY_GUID, term.getGuid());
        if (!vertexIt.hasNext()) {

            Vertex v = g.addV(term.getType()).next();
            v.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, term.getQualifiedName());
            v.property(PROPERTY_KEY_ENTITY_GUID, term.getGuid());
            v.property(PROPERTY_KEY_ENTITY_NAME, term.getDisplayName());
            g.tx().commit();
        } else {
            log.debug("{} createVertex found existing vertex {}", "createGlossaryVertex", vertexIt.next());
            g.tx().rollback();
        }

    }

    private void createElementVertex(Map<String, Element> context, JanusGraph graph, boolean mainGraph) {
        GraphTraversalSource g = graph.traversal();
        List<String> mainGraphVertex = new ArrayList<>(Arrays.asList(RELATIONAL_COLUMN,RELATIONAL_TABLE,TABULAR_COLUMN,DATA_FILE));

        for (Map.Entry<String, Element> entry : context.entrySet()) {
            String key = entry.getKey();
            Element value = entry.getValue();

            Iterator<Vertex> vertexIt = g.V().hasLabel(key).has(PROPERTY_KEY_ENTITY_GUID, value.getGuid());
                if (!vertexIt.hasNext()) {

                    if(mainGraphVertex.contains(key) && mainGraph){
                        addPropertiesToElementVertex(g,key,value);

                    }

                    if(!mainGraph) {
                        addPropertiesToElementVertex(g,key,value);

                    }
                }
                else {

                    log.debug("{} createVertex found existing vertex {}", "createElementVertex", vertexIt.next());
                    g.tx().rollback();
                }
        }
    }

    private void addPropertiesToElementVertex(GraphTraversalSource g, String key, Element value){

        Vertex v = g.addV(key).next();
        v.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, value.getQualifiedName());
        v.property(PROPERTY_KEY_ENTITY_GUID, value.getGuid());
        v.property(PROPERTY_KEY_ENTITY_NAME, value.getProperties().get("displayName"));

        g.tx().commit();
    }

    private void semanticAssignmentCreateRelationshipsBuffer(Map<String, Element> context, GlossaryTerm
            glossaryTerm, String  technicalTermType) {

        GraphTraversalSource g = bufferGraph.traversal();
        List<Element> elementsByRelationship = new ArrayList<>((context.values()));

        if(technicalTermType.equals(RELATIONAL_COLUMN)){
            orderContextBasedOnType(g,elementsByRelationship,orderRelational,edgesForRelationalColumn);
            String assetGuid = context.get(technicalTermType).getGuid();

            addSemanticAssignmentRelationship(g,technicalTermType,assetGuid,glossaryTerm);
        }

        if(technicalTermType.equals(TABULAR_COLUMN)){
            orderContextBasedOnType(g,elementsByRelationship,orderTabular,edgesForTabularColumn);
            String assetGuid = context.get(technicalTermType).getGuid();

            addSemanticAssignmentRelationship(g,technicalTermType,assetGuid,glossaryTerm);

        }


    }

    private void semanticAssignmentCreateRelationshipsMain(Map<String, Element> context, GlossaryTerm
            glossaryTerm, String  technicalTermType){

        GraphTraversalSource g = mainGraph.traversal();
        String assetGuid = context.get(technicalTermType).getGuid();

        addSemanticAssignmentRelationship(g,technicalTermType,assetGuid,glossaryTerm);


    }

    private List<Element> orderContextBasedOnType(GraphTraversalSource g, List<Element> elementsByRelationship, List<String> order, List<String> edgesForRelationships){
        Collections.sort(elementsByRelationship, Comparator.comparing(
                (Element e) -> order.indexOf(e.getType())).thenComparing(Element::getType));

        createRelationshpBasedOnType(g,elementsByRelationship,edgesForRelationships);

        return  elementsByRelationship;
    }

    private void createRelationshpBasedOnType(GraphTraversalSource g,List<Element> elementsByRelationship,List<String> edgesForRelationships){

        for (int i = 0; i < elementsByRelationship.size() - 1; i++) {

            String relationship = edgesForRelationships.get(i);
            boolean edge = g.V().has(elementsByRelationship.get(i).getType(), PROPERTY_KEY_ENTITY_GUID, elementsByRelationship.get(i).getGuid()).outE(relationship)
                    .where(g.V().has(elementsByRelationship.get(i + 1).getType(), PROPERTY_KEY_ENTITY_GUID, elementsByRelationship.get(i + 1).getGuid())).hasNext();

            if (!edge) {

                Vertex from = g.V().has(elementsByRelationship.get(i).getType(), PROPERTY_KEY_ENTITY_GUID, elementsByRelationship.get(i).getGuid()).next();
                Vertex to = g.V().has(elementsByRelationship.get(i + 1).getType(), PROPERTY_KEY_ENTITY_GUID, elementsByRelationship.get(i + 1).getGuid()).next();

                from.addEdge(relationship, to);
            }

        }
    }

    private void addSemanticAssignmentRelationship(GraphTraversalSource g,String technicalTermType, String assetGuid, GlossaryTerm glossaryTerm){

        Vertex glossaryTermVertex = g.V().has(GLOSSARY_TERM,PROPERTY_KEY_ENTITY_GUID,glossaryTerm.getGuid()).next();

        Vertex technicalTermVertex = g.V().has(technicalTermType,PROPERTY_KEY_ENTITY_GUID,assetGuid).next();

       boolean edge = g.V().has(GLOSSARY_TERM, PROPERTY_KEY_ENTITY_GUID,glossaryTerm.getGuid()).bothE(SEMANTIC_ASSIGNMENT)
                .where(g.V().has(technicalTermType, PROPERTY_KEY_ENTITY_GUID, assetGuid)).hasNext();

        if (!edge) {

            glossaryTermVertex.addEdge(SEMANTIC_ASSIGNMENT, technicalTermVertex);
        }

        g.tx().commit();

    }
}
