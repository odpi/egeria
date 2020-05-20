/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.janusgraph.core.schema.*;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;

public class JanusGraphRemote extends JanusGraphEmbedded {

    private static final Logger log = LoggerFactory.getLogger(JanusGraphRemote.class);

    protected Client client;


    public JanusGraphRemote(ConnectionProperties connectionProperties){
        super(connectionProperties);
    }


    @Override
    public GraphTraversalSource openGraph(){
        Cluster cluster = cluster();
        createSchema();
        return traversal().withRemote(DriverRemoteConnection.using(cluster,"g"));
    }

    @Override
    public void createSchema(){
        String s = sendRemoteRequest();
        // submit the request to the server
        ResultSet resultSet = client.submit(s);
        // drain the results completely
        Stream<Result> futureList = resultSet.stream();
//        futureList.map(Result::toString).forEach(log::debug);
    }

    public Cluster cluster() {
        Cluster cluster = null;
        try {

            GryoMapper.Builder builder = GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance());
            Cluster.Builder clusterBuilder = Cluster.build().addContactPoint("localhost")
//                        .minConnectionPoolSize(4)
//                        .maxConnectionPoolSize(6)
//                        .maxInProcessPerConnection(32)
//                        .maxSimultaneousUsagePerConnection(32)
                    .serializer(new GryoMessageSerializerV3d0(builder)) //TODO: Check this setting. Binary serializer was not working.
                    .port(8182);
            cluster = clusterBuilder.create();
            client = cluster.connect();

        } catch (Exception e) {
            log.error("Error in connecting to host address.", e);
        }
        return cluster;
    }

    private String sendRemoteRequest() {
        Set<String> vertexLabels = schemaBasedOnGraphType(VertexLabelsLineageGraph.class);
        Set<String> relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsLineageGraph.class);

        final StringBuilder s = new StringBuilder();

        log.info("creating schema");
        s.append("JanusGraphManagement management = graph.openManagement(); ");
        s.append("boolean created = false; ");

        // naive check if the schema was previously created
//        s.append(
//                "if (management.getRelationTypes(RelationType.class).iterator().hasNext()) { management.rollback(); created = false; } else { ");
////        s = addLabel(s,vertexLabels);
//        // vertex labels
        s.append("management.makeVertexLabel(\"GlossaryTerm\").make(); ");
        s.append("management.makeVertexLabel(\"RelationalColumn\").make(); ");
        s.append("management.makeVertexLabel(\"RelationalTableType\").make(); ");
        s.append("management.makeVertexLabel(\"RelationalTable\").make(); ");
        s.append("management.makeVertexLabel(\"RelationalDbSchemaType\").make(); ");
        s.append("management.makeVertexLabel(\"DeployedDbSchemaType\").make(); ");
        s.append("management.makeVertexLabel(\"Database\").make(); ");
        s.append("management.makeVertexLabel(\"Connection\").make(); ");
        s.append("management.makeVertexLabel(\"Endpoint\").make(); ");
        s.append("management.makeVertexLabel(\"Process\").make(); ");
        s.append("management.makeVertexLabel(\"PortAlias\").make(); ");
        s.append("management.makeVertexLabel(\"PortImplementation\").make(); ");
        s.append("management.makeVertexLabel(\"TabularSchemaType\").make(); ");
        s.append("management.makeVertexLabel(\"TabularColumn\").make(); ");
        s.append("management.makeVertexLabel(\"TabularColumnType\").make(); ");
        s.append("management.makeVertexLabel(\"FileFolder\").make(); ");
        s.append("management.makeVertexLabel(\"SubProcess\").make(); ");

        // edge labels
        s.append("management.makeEdgeLabel(\"SemanticAssignment\").make();");
        s.append("management.makeEdgeLabel(\"ProcessPort\").make(); ");
        s.append("management.makeEdgeLabel(\"PortDelegation\").signature(reason).make(); ");
        s.append("management.makeEdgeLabel(\"PortSchema\").make(); ");
        s.append("management.makeEdgeLabel(\"AttributeForSchema\").make(); ");
        s.append("management.makeEdgeLabel(\"SchemaType\").make(); ");
        s.append("management.makeEdgeLabel(\"SchemaAttributeType\").make(); ");
        s.append("management.makeEdgeLabel(\"LineageMapping\").make(); ");
        s.append("management.makeEdgeLabel(\"NestedFile\").make(); ");
        s.append("management.makeEdgeLabel(\"FolderHierarchy\").make(); ");
        s.append("management.makeEdgeLabel(\"AssetToConnection\").make(); ");
        s.append("management.makeEdgeLabel(\"AssetSchemaType\").make(); ");
        s.append("management.makeEdgeLabel(\"DataContentForDataset\").make(); ");
        s.append("management.makeEdgeLabel(\"IncludedIn\").make(); ");
        s.append("management.makeEdgeLabel(\"DataFlowWithProcess\").make(); ");

//        // composite indexes
        s.append(compositeIndex(s,"vertexIndexCompositevertex--guid",PROPERTY_KEY_ENTITY_GUID,true,Vertex.class));

        s.append("management.commit(); ");
//                "created = true; }");

        return s.toString();

    }

    private StringBuilder compositeIndex(StringBuilder s,String indexName,String propertyKeyName,boolean unique,Class classType)
    {
        s.append("existingIndex").append(indexName).append("\"").append(" = management.getGraphIndex(").append(indexName).append(");");
        s.append("if (existingIndex)").append(indexName).append("\"").append("{ created = true; } else { ");
        s.append("existingPropertyKey").append(propertyKeyName).append(" = management.getPropertyKey(").append(propertyKeyName).append(");");
        s.append("if (existingPropertyKey").append(propertyKeyName).append(" != null){").append("propertyKey")
                .append(propertyKeyName).append("= existingPropertyKey").append(propertyKeyName).append(";").append("oldKey = true;")
                .append("} else {")
        //TODO make dyanmic the class of the proeprty
                .append("propertyKey")
                .append(propertyKeyName).append("= existingPropertyKey").append(propertyKeyName)
                .append(" = management.makePropertyKey(").append(propertyKeyName).append(").dataType(String.class).make();")
                .append("oldKey = false;}");

        if(Vertex.class.equals(classType)){
            s.append("indexBuilder").append(propertyKeyName).append(" = management.buildIndex(").append(indexName).append(",").append(classType).append(").addKey(")
                    .append("propertyKey").append(propertyKeyName).append(");");

            if (unique){
                s.append("indexBuilder").append(propertyKeyName).append(".unique()");
            }

            s.append("index").append(indexName).append(" = ").append("indexBuilder").append(propertyKeyName).append(".buildCompositeIndex();");
            if (unique){
                s.append("management.setConsistency(").append("indexBuilder").append(propertyKeyName).append(",").append("ConsistencyModifier.LOCK);");
            }
        }
        else if (Edge.class.equals(classType)){

            s.append("indexBuilder").append(propertyKeyName).append(" = management.buildIndex(").append(indexName).append(",").append(classType).append(").addKey(")
                    .append("propertyKey").append(propertyKeyName).append(");");
            s.append("index").append(indexName).append(" = ").append("indexBuilder").append(propertyKeyName).append(".buildCompositeIndex();");

        }

        s.append("if (oldKey){")
                .append(" ManagementSystem.awaitGraphIndexStatus(graph,").append(indexName).append(").status(SchemaStatus.REGISTERED).call();");
        s.append("management.getGraphIndex(").append(indexName).append(");");
        s.append("management.updateIndex(").append(indexName).append(",SchemaAction.REINDEX);");
        s.append("}");

        s.append("anagementSystem.awaitGraphIndexStatus(graph,").append(indexName).append(").status(SchemaStatus.ENABLED).timeout(10,ChronoUnit.SECONDS).call();");

        s.append("}");
        return  s;
    }

    private <T extends Enum<T>> Set<String> schemaBasedOnGraphType(Class<T> aEnum) {
        return Stream.of(aEnum.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    private StringBuilder addLabel(StringBuilder s,Set<String> labels){
        labels.forEach(label -> s.append("management.makeVertexLabel(\""+label+"\").make(); "));
        return s;
    }

    private StringBuilder addEdge(StringBuilder s,Set<String> edges){
        edges.forEach(edge -> s.append("management.makeVertexLabel(\""+edge+"\").make(); "));
        return s;
    }
}
