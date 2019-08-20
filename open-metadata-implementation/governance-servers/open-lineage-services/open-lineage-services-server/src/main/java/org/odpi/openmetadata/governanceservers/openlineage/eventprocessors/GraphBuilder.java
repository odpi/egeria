/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.DeletePurgedRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.Element;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.RelationshipEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.mainGraph;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_ENTITY_NAME;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.PROPERTY_KEY_NAME_QUALIFIED_NAME;

public class GraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(GraphBuilder.class);


    public GraphBuilder() {
    }

    public void addAsset(RelationshipEvent event) {
        GlossaryTerm glossaryTerm = event.getGlossaryTerm();
        Element technicalTerm = event.getAssetContext().getBaseAsset();

        List<String> edgesLabels = new ArrayList<>();
        if(technicalTerm.getType().equals(RELATIONAL_TABLE)){
            edgesLabels = Arrays.asList(SEMANTIC_ASSIGNMENT,ATTRIBUTE_FOR_SCHEMA,SCHEMA_ATTRIBUTE_TYPE,ATTRIBUTE_FOR_SCHEMA);
        }

        if(technicalTerm.getType().equals(RELATIONAL_COLUMN)){
            edgesLabels = Arrays.asList(SEMANTIC_ASSIGNMENT,ATTRIBUTE_FOR_SCHEMA,SCHEMA_ATTRIBUTE_TYPE,
                    ATTRIBUTE_FOR_SCHEMA,SCHEMA_ATTRIBUTE_TYPE,ATTRIBUTE_FOR_SCHEMA);
        }

        Map<String, Element> context = event.getAssetContext().getContext();
        context.put(technicalTerm.getType(),technicalTerm);

        createGlossaryVertex(glossaryTerm);
        createElementVertex(context);
        createEdges(context,glossaryTerm,edgesLabels);

    }

    public void removeSemanticRelationship(DeletePurgedRelationshipEvent event){
        GraphTraversalSource g = mainGraph.traversal();

        try {
            boolean edgeExists = g.V().has(event.getGlossaryTerm().getType(), "veguid", event.getGlossaryTerm().getGuid())
                    .bothE()
                    .where(g.V().has(event.getEntityTypeDef(), "veguid", event.getEntityGuid())).hasNext();

            if(edgeExists){

              g.V().has(event.getGlossaryTerm().getType(), "veguid", event.getGlossaryTerm().getGuid())
                        .outE(SEMANTIC_ASSIGNMENT)
                        .where(g.V().has(event.getEntityTypeDef(), "veguid", event.getEntityGuid())).next().remove();
            }
            g.tx().commit();
        }catch (Exception e){
            log.error("Error occurred during deletion of the semantic assignment");
            g.tx().rollback();
        }
    }


    private void createGlossaryVertex(GlossaryTerm term){
        GraphTraversalSource g = mainGraph.traversal();

        Iterator<Vertex> vertexIt = g.V().hasLabel(term.getType()).has(PROPERTY_KEY_ENTITY_GUID, term.getGuid());
        if (!vertexIt.hasNext()) {


            Vertex v = g.addV(term.getType()).next();
            v.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, term.getQualifiedName());
            v.property(PROPERTY_KEY_ENTITY_GUID, term.getGuid());
            v.property(PROPERTY_KEY_ENTITY_NAME, term.getDisplayName());
            g.tx().commit();
        } else
            {
                log.info("{} createVertex found existing vertex {}", "createVertex", vertexIt.next());
                g.tx().rollback();
            }
    }

    private void createElementVertex(Map<String,Element> context) {
        GraphTraversalSource g = mainGraph.traversal();

        for (Map.Entry<String, Element> entry : context.entrySet()) {
            String key = entry.getKey();
            Element value = entry.getValue();

            Iterator<Vertex> vertexIt = g.V().hasLabel(key).has(PROPERTY_KEY_ENTITY_GUID, value.getGuid());
            if (!vertexIt.hasNext()) {

                Vertex v = g.addV(key).next();
                v.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, value.getQualifiedName());
                v.property(PROPERTY_KEY_ENTITY_GUID, value.getGuid());
                v.property(PROPERTY_KEY_ENTITY_NAME, value.getProperties().get("displayName"));

                g.tx().commit();
            } else {

                log.info("{} createVertex found existing vertex {}", "createElementVertex", vertexIt.next());
                g.tx().rollback();
            }

        }
    }

    private void createEdges(Map<String, Element> context, GlossaryTerm glossaryTerm,List<String> edgeLabels) {
        GraphTraversalSource g = mainGraph.traversal();

        final List<String> order = Arrays.asList(GLOSSARY_TERM,RELATIONAL_COLUMN,RELATIONAL_TABLE_TYPE,RELATIONAL_TABLE,RELATIONAL_DB_SCHEMA_TYPE,DEPLOYED_DB_SCHEMA_TYPE,DATABASE);
        List<Element> elementsByRelationship = new ArrayList<>((context.values()));
        elementsByRelationship.add(glossaryTerm);

        Collections.sort(elementsByRelationship,Comparator.comparing(
                (Element e)-> order.indexOf(e.getType())).thenComparing(Element::getType));

        for (int i = 0;i < elementsByRelationship.size()-1; i++) {

            String relationship = edgeLabels.get(i);
                boolean edge = g.V().has(elementsByRelationship.get(i).getType(), "veguid", elementsByRelationship.get(i).getGuid()).bothE(relationship)
                        .where(g.V().has(elementsByRelationship.get(i + 1).getType(), "veguid", elementsByRelationship.get(i + 1).getGuid())).hasNext();

                if(!edge){

                 Vertex from = g.V().has(elementsByRelationship.get(i).getType(),"veguid", elementsByRelationship.get(i).getGuid()).next();
                 Vertex to = g.V().has(elementsByRelationship.get(i + 1).getType(),"veguid", elementsByRelationship.get(i + 1).getGuid()).next();

                 from.addEdge(relationship,to);
                }

        }
        g.tx().commit();

    }




}