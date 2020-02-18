///* SPDX-License-Identifier: Apache-2.0 */
///* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.janusgraph.core.JanusGraphFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

import java.util.ArrayList;

public class JanusFactoryBeans {

    public JanusGraphFactory.Builder getJanusFactory(String graphDB,ConnectionProperties connectionProperties,String graphType){

        if(graphDB.equals("berkeleydb")){
            return janusFactoryBerkley(connectionProperties,graphType);
        }

        if(graphDB.equals("cassandra")){
            return janusFactoryCassandra(connectionProperties);
        }

        return null;
    }

    private JanusGraphFactory.Builder janusFactoryCassandra(ConnectionProperties connectionProperties){

        String listString = String.join(",", (ArrayList) connectionProperties.getConfigurationProperties().get("storageHostname"));

        return JanusGraphFactory.build().
                set("storage.backend",connectionProperties.getConfigurationProperties().get("storageBackend")).
                set("storage.username",connectionProperties.getConfigurationProperties().get("username")).
                set("storage.password",connectionProperties.getConfigurationProperties().get("password")).
                set("storage.hostname", listString).
                set("storage.cql.cluster-name",connectionProperties.getConfigurationProperties().get("clusterName")).
                set("storage.cql.keyspace",connectionProperties.getConfigurationProperties().get("storageCqlKeyspace")).
                set("index.search.backend",connectionProperties.getConfigurationProperties().get("indexSearchBackend")).
                set("index.search.hostname",connectionProperties.getConfigurationProperties().get("indexSearchHostname"));
    }

    private JanusGraphFactory.Builder janusFactoryBerkley(ConnectionProperties connectionProperties,String graphType){

        final String storagePath = "./egeria-lineage-repositories/"+graphType+"/berkeley";
        final String indexPath = "./egeria-lineage-repositories/"+graphType+"/searchindex";

        return JanusGraphFactory.build().
                set("storage.backend", connectionProperties.getConfigurationProperties().get("storageBackend")).
                set("storage.directory", storagePath).
                set("index.search.backend",connectionProperties.getConfigurationProperties().get("indexSearchBackend")).
                set("index.search.directory", indexPath);
    }



}
