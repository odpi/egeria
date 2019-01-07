/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "DataContentForDataSet" relationship between IGC "database" and "database_schema" assets.
 */
public class DataContentForDataSetMapper extends RelationshipMapping {

    private static class Singleton {
        private static final DataContentForDataSetMapper INSTANCE = new DataContentForDataSetMapper();
    }
    public static DataContentForDataSetMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private DataContentForDataSetMapper() {
        super(
                "database",
                "database_schema",
                "database_schemas",
                "database",
                "DataContentForDataSet",
                "dataContent",
                "supportedDataSets"
        );
    }

}
