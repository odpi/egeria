/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class DataContentForDataSetMapper extends RelationshipMapping {

    private static DataContentForDataSetMapper instance = new DataContentForDataSetMapper();
    public static DataContentForDataSetMapper getInstance() { return instance; }

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
