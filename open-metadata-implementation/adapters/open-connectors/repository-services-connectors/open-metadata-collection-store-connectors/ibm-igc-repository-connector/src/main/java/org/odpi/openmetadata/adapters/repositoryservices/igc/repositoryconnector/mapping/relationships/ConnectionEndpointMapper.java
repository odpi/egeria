/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton to represent the ConnectionEndpoint relationship in OMRS.
 */
public class ConnectionEndpointMapper extends RelationshipMapping {

    private static final Logger log = LoggerFactory.getLogger(ConnectionEndpointMapper.class);

    private static class Singleton {
        private static final ConnectionEndpointMapper INSTANCE = new ConnectionEndpointMapper();
    }
    public static ConnectionEndpointMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ConnectionEndpointMapper() {
        super(
                "host",
                "data_connection",
                "data_connections",
                "host",
                "ConnectionEndpoint",
                "connectionEndpoint",
                "connections"
        );
        setOptimalStart(OptimalStart.CUSTOM);
        setLinkingAssetType("connector");
    }

    /**
     * Retrieve the data_connection asset expected from a connector asset.
     *
     * @param connectorAsset the connector asset to translate into a data_connection asset
     * @param igcRestClient REST connectivity to the IGC environment
     * @return Reference - the data_connection asset
     */
    @Override
    public List<Reference> getProxyTwoAssetFromAsset(Reference connectorAsset, IGCRestClient igcRestClient) {
        String otherAssetType = connectorAsset.getType();
        if (otherAssetType.equals("connector")) {
            Reference withDataConnections = connectorAsset.getAssetWithSubsetOfProperties(igcRestClient,
                    new String[]{ "host", "data_connections" });
            ReferenceList dataConnections = (ReferenceList) withDataConnections.getPropertyByName("data_connections");
            dataConnections.getAllPages(igcRestClient);
            return dataConnections.getItems();
        } else {
            log.debug("Not a connector asset, just returning as-is: {}", connectorAsset);
            ArrayList<Reference> referenceAsList = new ArrayList<>();
            referenceAsList.add(connectorAsset);
            return referenceAsList;
        }
    }

    /**
     * Custom implementation of the relationship between an Endpoint (host) and a Connection (data_connection).
     * The relationship itself in IGC is complicated, from the host end it requires multiple hops (as the
     * 'data_connections' property on the host actually points to 'connector' assets, not 'data_connection' assets).
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject the host asset for which to create the relationship
     * @param userId
     */
    @Override
    public void addMappedOMRSRelationships(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                 List<Relationship> relationships,
                                                 Reference fromIgcObject,
                                                 String userId) {

        String assetType = Reference.getAssetTypeForSearch(fromIgcObject.getType());

        if (assetType.equals("host")) {
            addMappedOMRSRelationships_host(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
        } else if (assetType.equals("data_connection")) {
            addMappedOMRSRelationships_connection(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
        } else if (assetType.equals("connector")) {
            List<Reference> connections = getProxyTwoAssetFromAsset(fromIgcObject, igcomrsRepositoryConnector.getIGCRestClient());
            for (Reference connection : connections) {
                addMappedOMRSRelationships_connection(
                        igcomrsRepositoryConnector,
                        relationships,
                        connection,
                        userId
                );
            }
        } else {
            log.warn("Found unexpected asset type during relationship mapping: {}", fromIgcObject);
        }

    }

    /**
     * Custom implementation of the relationship between an Endpoint (host) and a Connection (data_connection).
     * The relationship itself in IGC is complicated, from the host end it requires multiple hops (as the
     * 'data_connections' property on the host actually points to 'connector' assets, not 'data_connection' assets).
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject the host asset for which to create the relationship
     * @param userId
     */
    private void addMappedOMRSRelationships_host(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                           List<Relationship> relationships,
                                           Reference fromIgcObject,
                                           String userId) {

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_connectors.host", "=", fromIgcObject.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        String[] properties = new String[]{ "name" };
        IGCSearch igcSearch = new IGCSearch("data_connection", properties, igcSearchConditionSet);

        ReferenceList dataConnections = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);
        dataConnections.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        for (Reference dataConnection : dataConnections.getItems()) {

            /* Only proceed with the connection object if it is not a 'main_object' asset
             * (in this scenario, 'main_object' represents ColumnAnalysisMaster objects that are not accessible
             *  and will throw bad request (400) REST API errors) */
            if (dataConnection != null && !dataConnection.getType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                try {

                    log.debug("Retrieved connection: {}", dataConnection);

                    Relationship relationship = getMappedRelationship(
                            igcomrsRepositoryConnector,
                            ConnectionEndpointMapper.getInstance(),
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                                    igcomrsRepositoryConnector.getRepositoryName(),
                                    "ConnectionEndpoint"),
                            fromIgcObject,
                            dataConnection,
                            "data_connections",
                            userId
                    );

                    relationships.add(relationship);

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            }

        }

    }

    /**
     * Custom implementation of the relationship between an Endpoint (host) and a Connection (data_connection).
     * The relationship itself in IGC is complicated, from this end it requires retrieving the host details from
     * the 'connector' object related through the 'data_connectors' property, as the 'host' property on'data_connection'
     * itself is inevitably blank.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject the data_connection object for which to create the relationship
     * @param userId
     */
    private void addMappedOMRSRelationships_connection(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                           List<Relationship> relationships,
                                           Reference fromIgcObject,
                                           String userId) {

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_connections", "=", fromIgcObject.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        String[] properties = new String[]{ "host" };
        IGCSearch igcSearch = new IGCSearch("connector", properties, igcSearchConditionSet);

        ReferenceList dataConnectors = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);
        dataConnectors.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        for (Reference dataConnector : dataConnectors.getItems()) {

            /* Only proceed with the connector object if it is not a 'main_object' asset
             * (in this scenario, 'main_object' represents ColumnAnalysisMaster objects that are not accessible
             *  and will throw bad request (400) REST API errors) */
            if (dataConnector != null && !dataConnector.getType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                try {

                    log.debug("Retrieved connector: {}", dataConnector);

                    //Reference host = (Reference) connectorGetPropertyByName.invoke(dataConnector, "host");
                    Reference host = (Reference) dataConnector.getPropertyByName("host");
                    log.debug("Retrieved host: {}", host);

                    Relationship relationship = getMappedRelationship(
                            igcomrsRepositoryConnector,
                            ConnectionEndpointMapper.getInstance(),
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                                    igcomrsRepositoryConnector.getRepositoryName(),
                                    "ConnectionEndpoint"),
                            host,
                            fromIgcObject,
                            "data_connections",
                            userId
                    );

                    relationships.add(relationship);

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            }

        }

    }

}
