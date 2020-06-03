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
//        createSchema();
        return traversal().withRemote(DriverRemoteConnection.using(cluster,"g"));
    }

    @Override
    public void createSchema(){
        String s = compositeIndex("vertexIndexCompositevertex--guid",PROPERTY_KEY_ENTITY_GUID,true,Vertex.class);
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


        final StringBuilder s = new StringBuilder();

        log.info("creating schema");
        s.append("JanusGraphManagement management = graph.openManagement();");

//        // composite indexes
//        s.append(compositeIndex(s,"vertexIndexCompositevertex--guid",PROPERTY_KEY_ENTITY_GUID,true,Vertex.class));

        s.append("management.commit(); ");


//                "created = true; }");

        log.debug(s.toString());
        return s.toString();

    }


    private String compositeIndex(String indexName,String propertyKeyName,boolean unique,Class classType)
    {
        final StringBuilder s = new StringBuilder();

        s.append("JanusGraphManagement management = graph.openManagement();");

        s.append("existingIndex = management.getGraphIndex(\""+indexName+"\");");
        s.append("if (existingIndex != null ){   management.rollback(); } else { ");
        s.append("existingPropertyKey = management.getPropertyKey(\""+propertyKeyName+"\");");
        s.append("if (existingPropertyKey != null){").append("propertyKey = existingPropertyKey;")
                .append("oldKey = true;")
                .append("} else {")
        //TODO make dyanmic the class of the proeprty
                .append("propertyKey")
                .append(" = management.makePropertyKey(\""+propertyKeyName+"\").dataType(String.class).make();")
                .append("oldKey = false;};");

        if(Vertex.class.equals(classType)){
            s.append("indexBuilder = management.buildIndex(\""+indexName+"\",Vertex.class).addKey(propertyKey);");

            if (unique){
                s.append("indexBuilder.unique();");
            }

            s.append("index = indexBuilder.buildCompositeIndex();");
            if (unique){
                s.append("management.setConsistency(indexBuilder,ConsistencyModifier.LOCK);");
            }
        }
        else if (Edge.class.equals(classType)){

            s.append("indexBuilder = management.buildIndex(").append(indexName).append(",Edge.class).addKey(propertyKey);");
            s.append("index = indexBuilder.buildCompositeIndex();");
        }

        s.append("if (oldKey){")
                .append(" ManagementSystem.awaitGraphIndexStatus(graph,\""+indexName+"\").status(SchemaStatus.REGISTERED).call();");
        s.append("management.getGraphIndex(\""+indexName+"\");");
        s.append("management.updateIndex(\""+indexName+"\",SchemaAction.REINDEX);");
        s.append("};");

        s.append("ManagementSystem.awaitGraphIndexStatus(graph,\""+indexName+"\").status(SchemaStatus.ENABLED).timeout(10,ChronoUnit.SECONDS).call();");

        s.append("};");
        s.append("management.commit(); ");
        log.debug(s.toString());

        return  s.toString();
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
