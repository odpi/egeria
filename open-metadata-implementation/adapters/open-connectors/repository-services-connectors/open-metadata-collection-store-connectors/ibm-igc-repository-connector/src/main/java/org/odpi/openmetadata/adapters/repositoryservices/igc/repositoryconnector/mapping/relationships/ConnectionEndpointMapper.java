/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

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
                "data_connectors.host",
                "ConnectionEndpoint",
                "connectionEndpoint",
                "connections"
        );
        setOptimalStart(OptimalStart.CUSTOM);
    }

    /**
     * Custom implementation of the relationship between an Endpoint (host) and a Connection (data_connection).
     * The relationship itself in IGC is complicated, from one end it requires multiple hops and from the other
     * it doesn't, so the mapping is done based on the type of asset received.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject
     * @param userId
     */
    @Override
    public void addMappedOMRSRelationships(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                           List<Relationship> relationships,
                                           Reference fromIgcObject,
                                           String userId) {

        String assetType = Reference.getAssetTypeForSearch(fromIgcObject.getType());

        if (assetType.equals("host")) {
            addMappedRelationshipsForIgcHost(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
        } else if (assetType.equals("data_connection")) {
            addMappedRelationshipsForIgcConnection(
                    igcomrsRepositoryConnector,
                    relationships,
                    fromIgcObject,
                    userId
            );
        }

    }

    /**
     * Mapping from an IGC host asset to the data connection.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject
     * @param userId
     */
    private void addMappedRelationshipsForIgcHost(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
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
     * Mapping from an IGC data_connection asset to the host.
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject
     * @param userId
     */
    private void addMappedRelationshipsForIgcConnection(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
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
            if (dataConnector != null && !dataConnector.getType().equals("main_object")) {
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
