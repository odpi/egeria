/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class NestedFileMapper extends RelationshipMapping {

    private static class Singleton {
        private static final NestedFileMapper INSTANCE = new NestedFileMapper();
    }
    public static NestedFileMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private NestedFileMapper() {
        super(
                "data_file_folder",
                "data_file",
                "data_files",
                "parent_folder",
                "NestedFile",
                "homeFolder",
                "nestedFiles"
        );
    }

}
