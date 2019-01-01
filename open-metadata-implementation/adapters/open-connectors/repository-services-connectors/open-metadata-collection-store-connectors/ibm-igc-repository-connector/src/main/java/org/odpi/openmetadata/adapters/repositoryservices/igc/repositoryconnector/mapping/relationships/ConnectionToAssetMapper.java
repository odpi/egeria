/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

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
import java.util.List;

/**
 * Singleton to represent the ConnectionToAsset relationship in OMRS.
 */
public class ConnectionToAssetMapper extends RelationshipMapping {

    private static final Logger log = LoggerFactory.getLogger(ConnectionToAssetMapper.class);

    private static class Singleton {
        private static final ConnectionToAssetMapper INSTANCE = new ConnectionToAssetMapper();
    }
    public static ConnectionToAssetMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ConnectionToAssetMapper() {
        super(
                "data_connection",
                "database",
                "imports_database",
                "data_connections",
                "ConnectionToAsset",
                "connections",
                "asset"
        );
    }

    /**
     * Custom implementation of the relationship between a Connection (data_connection) and an Asset (main_object).
     * The relationship itself in IGC is complicated: simple for databases, but not for file-based assets.
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

        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_connection", "=", fromIgcObject.getId());
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

                    Relationship relationship = getMappedRelationship(
                            igcomrsRepositoryConnector,
                            ConnectionToAssetMapper.getInstance(),
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                                    igcomrsRepositoryConnector.getRepositoryName(),
                                    "ConnectionToAsset"),
                            fromIgcObject,
                            dataConnectionAsset,
                            "imports_database",
                            userId
                    );

                    relationships.add(relationship);

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
