/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.driver.*;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphRemoteConnectorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;

public class JanusGraphRemote extends GraphGremlinBase {

    private static final Logger log = LoggerFactory.getLogger(JanusGraphRemote.class);

    private Cluster cluster;
    private Client client;

    public JanusGraphRemote(ConnectionProperties connectionProperties){
        super(connectionProperties);
        this.supportingTransactions = false;
    }


    @Override
    public GraphTraversalSource openGraph() {

        cluster = createCluster();
        client = cluster.connect();
//        createSchema();

        return traversal().withRemote(DriverRemoteConnection.using(cluster,this.properties.get(LineageGraphRemoteConnectorProvider.SOURCE_NAME).toString()));
    }

    private Cluster createCluster() {

        GryoMapper.Builder builder = GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance()); //TODO: Check for replacement
        Cluster.Builder clusterBuilder = Cluster.build()
                .port(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_PORT).toString()))
                .addContactPoints(((List<String>)properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_HOSTS)).toArray(new String[0]))
                .serializer(new GryoMessageSerializerV3d0(builder)); //TODO: Check this setting. Binary serializer was not working.

        if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_CREDENTIALS_USERNAME)!=null && properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_CREDENTIALS_USERNAME)!=null)
            clusterBuilder.credentials(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_CREDENTIALS_USERNAME).toString(),properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE).toString());


        if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MIN_CONNECTION_POOL_SIZE)!=null)
            clusterBuilder.minConnectionPoolSize(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MIN_CONNECTION_POOL_SIZE).toString()));
        if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_CONNECTION_POOL_SIZE)!=null)
            clusterBuilder.maxConnectionPoolSize(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_CONNECTION_POOL_SIZE).toString()));
        if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_SIMULTANEOUS_USAGE_PER_CONNECTION)!=null)
            clusterBuilder.maxSimultaneousUsagePerConnection(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_SIMULTANEOUS_USAGE_PER_CONNECTION).toString()));
        if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_IN_PROCESS_PER_CONNECTION)!=null)
            clusterBuilder.maxInProcessPerConnection(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_IN_PROCESS_PER_CONNECTION).toString()));

        if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_ENABLE)!=null && properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_ENABLE).toString().equalsIgnoreCase("true")) {
            clusterBuilder.enableSsl(true);
            if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_TYPE)!=null)
                clusterBuilder.keyStoreType(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_TYPE).toString());
            if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE)!=null)
                clusterBuilder.keyStore(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE).toString());
            if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_PASSWORD)!=null)
                clusterBuilder.keyStorePassword(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_PASSWORD).toString());
            if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_SKIP_VALIDATION)!=null)
                clusterBuilder.sslSkipCertValidation(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_SKIP_VALIDATION).toString().equalsIgnoreCase("true"));
            if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE)!=null)
                clusterBuilder.trustStore(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE).toString());
            if(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE_PASSWORD)!=null)
                clusterBuilder.trustStorePassword(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE_PASSWORD).toString());
        }

        return clusterBuilder.create();
    }

    @Override
    public void createSchema() {
        String s = compositeIndex("vertexIndexCompositevertex--guid",PROPERTY_KEY_ENTITY_GUID,true,Vertex.class);
        ResultSet resultSet = client.submit(s);
        Stream<Result> futureList = resultSet.stream();
        futureList.map(Result::toString).forEach(log::debug);
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

    @Override
    public void closeGraph() {

        try {
            if (g != null) {
                g.close();
            }
            if (cluster != null) {
                cluster.close();
            }
        } catch(Exception e) {
            log.error("Exception while closing.",e);
        } finally {
            g = null;
            graph = null;
            client = null;
            cluster = null;
        }

    }
}
