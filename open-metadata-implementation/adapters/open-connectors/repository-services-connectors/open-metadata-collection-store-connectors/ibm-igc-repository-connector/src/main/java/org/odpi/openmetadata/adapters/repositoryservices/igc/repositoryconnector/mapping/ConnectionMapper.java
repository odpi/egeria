/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ConnectionMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(ConnectionMapper.class);

    private static final String R_CONNECTION_TO_ASSET = "ConnectionToAsset";
    private static final String R_CONNECTION_ENDPOINT = "ConnectionEndpoint";

    /**
     * Sets the basic criteria to use for mapping between an IGC 'data_connection' object and an OMRS 'Connection' object.
     *
     * @param dataConnection the IGC 'data_connection' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public ConnectionMapper(Reference dataConnection, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dataConnection,
                "data_connection",
                "Connection",
                igcomrsRepositoryConnector,
                userId,
                false
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                "imports_database",
                R_CONNECTION_TO_ASSET,
                "connections",
                "asset"
        );
        addSimpleRelationshipMapping(
                "data_connectors",
                "ConnectionConnectorType",
                "connections",
                "connectorType"
        );

        // Handle non-database connections to assets
        addComplexIgcRelationship("data_connectors");
        addComplexOmrsRelationship(R_CONNECTION_ENDPOINT);
        addComplexOmrsRelationship(R_CONNECTION_TO_ASSET);

    }

    /**
     * No classifications implemented for Connections.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do
    }

    @Override
    protected void complexRelationshipMappings() {

        addConnectionEndpointRelationships();
        addConnectionToAssetRelationships();

    }

    private void addConnectionEndpointRelationships() {

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_connections", "=", me.getId());
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

                    Relationship omrsRelationship = getMappedRelationship(
                            "data_connections",
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(SOURCE_NAME,
                                    R_CONNECTION_ENDPOINT),
                            "connections",
                            "connectionEndpoint",
                            host);

                    relationships.add(omrsRelationship);

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            }

        }

    }

    private void addConnectionToAssetRelationships() {

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_connection", "=", me.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        IGCSearch igcSearch = new IGCSearch("data_file_folder", igcSearchConditionSet);
        igcSearch.addType("amazon_s3_data_file_folder");
        igcSearch.addType("amazon_s3_bucket");

        ArrayList<String> aDataFileFolderRids = new ArrayList<>();

        ReferenceList dataConnectionAssets = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);
        dataConnectionAssets.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        for (Reference dataConnectionAsset : dataConnectionAssets.getItems()) {

            /* Only proceed with the connection object if it is not a 'main_object' asset
             * (in this scenario, 'main_object' represents ColumnAnalysisMaster objects that are not accessible
             *  and will throw bad request (400) REST API errors) */
            if (dataConnectionAsset != null && !dataConnectionAsset.getType().equals("main_object")) {
                try {

                    Relationship omrsRelationship = getMappedRelationship(
                            "data_connection",
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(SOURCE_NAME,
                                    R_CONNECTION_TO_ASSET),
                            "connections",
                            "asset",
                            dataConnectionAsset);

                    relationships.add(omrsRelationship);

                    if (dataConnectionAsset.getType().equals("data_file_folder")) {
                        aDataFileFolderRids.add(dataConnectionAsset.getId());
                    }

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            }

        }

        // TODO: need to also retrieve all data_files that are under a particular data_file_folder referenced
        //  by this data_connection?  Doing so will require getting all data file folders (recursively) under
        //  a the referenced data file folders, and then looking for any data_files in any of those folders
        //  (many queries, potentially huge set of ID matches to then run against)...

    }

}
