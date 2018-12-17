/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class DatabaseMapper extends ReferenceableMapper {

    /**
     * Sets the basic criteria to use for mapping between an IGC 'database' object and an OMRS 'Database' object.
     *
     * @param database the IGC 'database' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public DatabaseMapper(Reference database, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                database,
                "database",
                "Database",
                igcomrsRepositoryConnector,
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");
        addSimplePropertyMapping("dbms", "type");
        addSimplePropertyMapping("dbms_version", "version");
        addSimplePropertyMapping("dbms_server_instance", "instance");
        addSimplePropertyMapping("imported_from", "importedFrom");
        addSimplePropertyMapping("created_on", "createTime");
        addSimplePropertyMapping("modified_on", "modifiedTime");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                "database_schemas",
                "DataContentForDataSet",
                "dataContent",
                "supportedDataSets"
        );

        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)

    }

    /**
     * We implement this method to apply any classifications -- since IGC itself doesn't have a "Classification"
     * asset type, we need to apply our own translation between how we're using other IGC asset types and the
     * Classification(s) we want them to represent in OMRS.
     * <br><br>
     * No classifications for Database entities.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do...
    }

}
