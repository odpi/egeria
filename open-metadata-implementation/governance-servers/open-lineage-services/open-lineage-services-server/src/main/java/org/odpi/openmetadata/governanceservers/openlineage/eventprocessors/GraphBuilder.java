/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.eventprocessors;

import com.sleepycat.je.tree.INTargetRep;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetElement;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Element;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Term;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.RelationshipEvent;
import org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.apache.tinkerpop.gremlin.structure.io.IoCore.graphml;
import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.governanceservers.openlineage.util.Constants.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.*;

public class GraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(GraphBuilder.class);
    private GraphFactory graphFactory;
    private JanusGraph janusGraph;
    private OMRSAuditLog auditLog;

    public GraphBuilder() {

        try {
           janusGraph = graphFactory.open();
        } catch (RepositoryErrorException e) {
           log.error(e.getErrorMessage());
        }
    }


    public void addAsset(RelationshipEvent event) {

        List<Element> elements = new ArrayList<>();

        GlossaryTerm glossaryTerm = event.getGlossaryTerm();
        Element technicalTerm = event.getAssetContext().getBaseAsset();

        Map<String, Element> context = event.getAssetContext().getContext();

        context.put(glossaryTerm.getType(),glossaryTerm);
        context.put(technicalTerm.getType(),technicalTerm);

        try {
            createVertex(context);
        } catch (InvalidParameterException e) {
           log.error(e.getErrorMessage());
        }


    }

    public void exportGraph(){

//        try {
//            janusGraph.io(graphml()).writeGraph("my-graph.graphml");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void createVertex(Map<String,Element> context) throws InvalidParameterException {
        GraphTraversalSource g = janusGraph.traversal();

        for (Map.Entry<String, Element> entry : context.entrySet()) {
            String key = entry.getKey();
            Element value = entry.getValue();

            Iterator<Vertex> vertexIt = g.V().hasLabel(key).has(PROPERTY_KEY_ENTITY_GUID, value.getGuid());
            if(!vertexIt.hasNext()) {


                Vertex v = g.addV(key).next();
                v.property(PROPERTY_KEY_NAME_QUALIFIED_NAME, value.getQualifiedName());
                v.property(PROPERTY_KEY_ENTITY_GUID, value.getGuid());
                v.property(PROPERTY_KEY_ENTITY_NAME, value.getProperties().get("displayName"));

                g.tx().commit();
            }
            else{

                log.info("{} createVertex found existing vertex {}", "createVertex", vertexIt.next());
                g.tx().rollback();
                OpenLineageErrorCode errorCode = OpenLineageErrorCode.ENTITY_ALREADY_EXISTS;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(value.getGuid(), "createVertex",
                        this.getClass().getName());

                throw new InvalidParameterException(400,
                        this.getClass().getName(),
                        "createVertex",
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());

            }
        }

        createEdges(context);
    }

    private void createEdges(Map<String, Element> context) {
        GraphTraversalSource g = janusGraph.traversal();

        final List<String> order = Arrays.asList(GLOSSARY_TERM,RELATIONAL_COLUMN,RELATIONAL_TABLE_TYPE,RELATIONAL_TABLE,RELATIONAL_DB_SCHEMA_TYPE,DEPLOYED_DB_SCHEMA_TYPE,DATABASE);

        List<Element> elementsByRelationship = new ArrayList<>((context.values()));

        Collections.sort(elementsByRelationship,Comparator.comparing(
                (Element e)-> order.indexOf(e.getType())).thenComparing(Element::getType));


        for (int i = 0;i< elementsByRelationship.size(); i++) {


            if(elementsByRelationship.get(i).getType().equals(GLOSSARY_TERM)){

                boolean edge = g.V().has(elementsByRelationship.get(i).getType(), "guid", elementsByRelationship.get(i).getGuid()).bothE(SEMANTIC_ASSIGNMENT)
                        .where(g.V().has(elementsByRelationship.get(i + 1).getType(), "guid", elementsByRelationship.get(i + 1).getGuid())).hasNext();

                if(!edge){
                 Vertex from = g.V().has(elementsByRelationship.get(i).getType(),"guid", elementsByRelationship.get(i).getGuid()).next();

                 Vertex to = g.V().has(elementsByRelationship.get(i + 1).getType(),"guid", elementsByRelationship.get(i + 1).getGuid()).next();

                 from.addEdge(SEMANTIC_ASSIGNMENT,to);

                    g.tx().commit();
                }
            }

        }
    }


}